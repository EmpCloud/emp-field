package com.empcloud.empmonitor.network.socket

import android.content.Context
import com.empcloud.empmonitor.utils.Constants
import com.empcloud.empmonitor.utils.NativeLib
import com.empcloud.empmonitor.utils.device_status.DeviceStatusLogger
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.json.JSONObject
import java.net.URI
import java.net.URISyntaxException

// Backend emits this event on the same server that serves the REST APIs whenever
// POST /hrmsAdmin/location-update creates or updates an org's location/geofence config.
// There is no server-side room targeting yet, so every connected client receives every
// update and must filter by orgId itself (see mobile-location-socket-integration.md).
object SocketManager {

    private const val EVENT_LOCATION_UPDATE = "location:update"

    private var socket: Socket? = null
    private var connectedOrgId: String? = null

    private val _locationUpdateEvents = MutableSharedFlow<LocationConfigUpdateEvent>(extraBufferCapacity = 1)
    val locationUpdateEvents: SharedFlow<LocationConfigUpdateEvent> = _locationUpdateEvents.asSharedFlow()

    fun connect(context: Context) {
        val appContext = context.applicationContext

        val token = appContext.getSharedPreferences(Constants.AUTH_TOKEN, Context.MODE_PRIVATE)
            .getString(Constants.AUTH_TOKEN, "") ?: ""
        if (token.isEmpty()) {
            DeviceStatusLogger.d("socket connect skipped: no auth token")
            return
        }

        val orgId = appContext.getSharedPreferences(Constants.ORG_ID, Context.MODE_PRIVATE)
            .getString(Constants.ORG_ID, "") ?: ""

        if (socket?.connected() == true && connectedOrgId == orgId) return

        disconnect()
        connectedOrgId = orgId

        try {
            val origin = originOf(NativeLib().getBaseUrlDev())
            val options = IO.Options()
            options.transports = arrayOf("websocket")
            options.reconnection = true

            val newSocket = IO.socket(origin, options)

            newSocket.on(Socket.EVENT_CONNECT) {
                DeviceStatusLogger.d("socket connected id=${newSocket.id()}")
            }
            newSocket.on(Socket.EVENT_DISCONNECT) { args ->
                DeviceStatusLogger.d("socket disconnected reason=${args.firstOrNull()}")
            }
            newSocket.on(Socket.EVENT_CONNECT_ERROR) { args ->
                DeviceStatusLogger.e("socket connect_error: ${args.firstOrNull()}")
            }
            newSocket.on(EVENT_LOCATION_UPDATE) { args ->
                val json = args.firstOrNull() as? JSONObject ?: return@on
                val event = parseLocationUpdateEvent(json)
                if (orgId.isNotEmpty() && event.orgId.isNotEmpty() && event.orgId != orgId) {
                    DeviceStatusLogger.d("ignoring location:update for other org=${event.orgId}")
                    return@on
                }
                DeviceStatusLogger.d("location:update received message=${event.message}")
                _locationUpdateEvents.tryEmit(event)
            }

            newSocket.connect()
            socket = newSocket
        } catch (e: URISyntaxException) {
            DeviceStatusLogger.e("socket init failed: ${e.message}", e)
        }
    }

    fun disconnect() {
        socket?.off()
        socket?.disconnect()
        socket = null
        connectedOrgId = null
    }

    private fun originOf(apiBaseUrl: String): String {
        val uri = URI(apiBaseUrl)
        return "${uri.scheme}://${uri.authority}"
    }

    private fun parseLocationUpdateEvent(json: JSONObject): LocationConfigUpdateEvent {
        return LocationConfigUpdateEvent(
            orgId = json.optString("orgId", ""),
            locationName = json.optNullableString("locationName"),
            isGlobalUpdate = json.optBoolean("isGlobalUpdate", false),
            updatedAt = json.optNullableString("updatedAt"),
            message = json.optNullableString("message")
        )
    }

    private fun JSONObject.optNullableString(key: String): String? =
        if (has(key) && !isNull(key)) getString(key) else null
}
