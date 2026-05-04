package com.empcloud.empmonitor.utils.broadcast_services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeoFenceBroadCastReciever:BroadcastReceiver() {

    companion object {
        private const val TAG = "GeofenceReceiver"
    }
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent!!.hasError()) {

            return
        }
        val geofenceTransition = geofencingEvent.geofenceTransition
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            Toast.makeText(context, "Entered Geofence", Toast.LENGTH_SHORT).show()

        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            Toast.makeText(context, "Exited Geofence", Toast.LENGTH_SHORT).show()
        }
    }
}