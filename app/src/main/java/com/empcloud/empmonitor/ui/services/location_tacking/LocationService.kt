package com.empcloud.empmonitor.ui.services.location_tacking


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.local.Location.LocationDao
import com.empcloud.empmonitor.data.remote.request.send_location.LocationList
import com.empcloud.empmonitor.data.remote.response.send_location.SendLocationResponse
import com.empcloud.empmonitor.network.NetworkRepository
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.empcloud.empmonitor.utils.device_status.DeviceBatteryReader
import com.empcloud.empmonitor.utils.device_status.DeviceStatusHeartbeatScheduler
import com.empcloud.empmonitor.utils.device_status.DeviceStatusLogger
import com.empcloud.empmonitor.utils.device_status.DeviceStatusSessionHelper
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.math.*

@AndroidEntryPoint
class LocationService: Service() {

    private var numberFreq: Int? = null

    @Inject
    lateinit var repository: NetworkRepository

    @Inject
    lateinit var locationDao: LocationDao

    private var isLastLocationSaved:Boolean = false

    private var latitudelastsaved:Double? = null
    private var longitudelastsaved:Double? = null

    private var distanceFence:Int? = null


    // Guards against overlapping uploads. Without it, a save-triggered upload and an
    // onStartCommand flush could send the same queued points concurrently, and the server
    // (points carry no id) would render them as duplicate / backtracking route segments.
    private val isUploading = AtomicBoolean(false)

    companion object {
        const val ACTION_NETWORK_AVAILABLE = "com.empcloud.empmonitor.ACTION_NETWORK_AVAILABLE"
        // BUG_04: reject fixes worse than this accuracy (meters) before recording/uploading
        private const val ACCURACY_THRESHOLD_M = 50f
        // Reject cached/stale fixes so a last-known location from an earlier place doesn't
        // become a phantom start waypoint (a straight line from "where the user wasn't").
        private const val MAX_LOCATION_AGE_MS = 30_000L
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val handler = Handler(Looper.getMainLooper())
//    private lateinit var repository: NetworkRepository
    private val sendLocationResponse = Channel<ApiState<SendLocationResponse>>(Channel.BUFFERED)
    val observeSendLocationCall = sendLocationResponse.receiveAsFlow()
    override fun onCreate() {
        super.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
        val frequency = applicationContext.getSharedPreferences(Constants.FREQUENCY,
            MODE_PRIVATE)
        numberFreq = frequency.getInt(Constants.FREQUENCY,1)
        distanceFence = frequency.getInt(Constants.FENCE_RADIUS,1)


//        startLocationUpdates()
        observeSend()
//        val api = Module.provideServices()
//        repository = NetworkRepository(api)

        locationRequest = LocationRequest.create().apply {

            Log.d("frequency","$numberFreq")
            interval = numberFreq!! * 1000L
//            interval = 10000 // 10 seconds
            fastestInterval = interval
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    Log.d("LocationService", "Location: ${location.latitude}, ${location.longitude}")

                    // BUG_04: drop garbage fixes (poor accuracy or null-island 0,0) so they don't
                    // become phantom waypoints in the recorded travel route.
                    if (!location.hasAccuracy() || location.accuracy > ACCURACY_THRESHOLD_M ||
                        (location.latitude == 0.0 && location.longitude == 0.0)) {
                        Log.d("LocationService", "Skipping low-quality fix: accuracy=${if (location.hasAccuracy()) location.accuracy else -1f}")
                        continue
                    }

                    // Drop stale/cached fixes (e.g. fused's first last-known result) so a point
                    // from an earlier location can't be recorded as a phantom waypoint.
                    val ageMs = (SystemClock.elapsedRealtimeNanos() - location.elapsedRealtimeNanos) / 1_000_000L
                    if (ageMs > MAX_LOCATION_AGE_MS) {
                        Log.d("LocationService", "Skipping stale fix: age=${ageMs}ms")
                        continue
                    }
//                    val locationEntity = LocationEntity(
//                        latitude = location.latitude,
//                        longitude = location.longitude,
//                        timeStamp = getCurrentTimeFormatted()
//                    )

//                    val locationListData = LocationList(CommonMethods.getCurrentDate(),getCurrentTimeFormatted(),location.latitude,location.longitude)
//                    val locationList = listOf(locationListData)
//                    CommonMethods.saveLocationDataList(applicationContext,locationListData)
//
////                    saveLocationToRoom(locationEntity)
//                    if (isInternetAvailable(applicationContext)) {
//                        CoroutineScope(Dispatchers.IO).launch {
//                            val sp = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
//                            val token = sp.getString(Constants.AUTH_TOKEN, "")
//                            val dataReturn = CommonMethods.getCurrentDateTime()
//                            val arr = dataReturn.split(" ")
//                            val time = arr[1]
//                            val date = arr[0]
//                            Log.d("dsgsdh", date)
////                            val sendLocationModel =
////                                SendLocationModel(date, time, location.latitude, location.longitude)
//
//                            val sendloc = CommonMethods.getLocationDataList(applicationContext)
//                            Log.d("savedLocation1",sendloc.toString())
//                            repository.sendLocationCall(token!!, sendloc!!)
//                                .collect { response ->
//                                    sendLocationResponse.send(response)
//                                }
//                        }
//                    }else{
//
//                        Log.d("savedLocation",locationList.toString())
//                        CommonMethods.saveLocationDataList(applicationContext,locationListData)
//
//                    }

                    if (longitudelastsaved == null && latitudelastsaved == null){

                        saveLocation(location.latitude,location.longitude)
                        if (isInternetAvailable(applicationContext)) callApiLocation()

                    }else {

                        Log.d("nesaved1","$longitudelastsaved $latitudelastsaved")
                        var distanceMin = getLocationDistanceMeter(
                            latitudelastsaved!!,
                            longitudelastsaved!!,
                            location.latitude,
                            location.longitude
                        )

                        Log.d("nesaved","$distanceMin")
                        if (distanceMin > distanceFence!!) {

                            saveLocation(location.latitude,location.longitude)
                            if (isInternetAvailable(applicationContext))  callApiLocation()

                        }
                    }

//                    getLastKnownLocation()
//                    val sendloc = CommonMethods.getLocationDataList(applicationContext)
//                    Log.d("savedLocation151", sendloc.toString())
                }
            }
        }

//        getLastKnownLocation()

    }

    private fun getCurrentTimeFormatted(): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(System.currentTimeMillis())
    }

    private fun observeSend() {
        CoroutineScope(Dispatchers.IO).launch {
            observeSendLocationCall.collect{res ->
                when(res){

                    is ApiState.ERROR -> {
                        Log.d("APiResponseLna","ERROR")
                    }
                    ApiState.LOADING -> {
                        Log.d("APiResponseLna","LOADING")
                    }
                    is ApiState.SUCESS -> {
                        val it = res.getResponse
                        if (DeviceStatusSessionHelper.isSessionExpired(it.resolvedStatusCode, it.resolvedMessage)) {
                            DeviceStatusSessionHelper.handleSessionExpired(applicationContext)
                            return@collect
                        }

                        if(it.isSuccessfulPayload()){

                            Log.d("responselocation",it.resolvedData.toString())
                            val currentFrequency = it.resolvedData?.currentFrequency
                            val currentRadius = it.resolvedData?.currentRadius
                            if (currentFrequency != null && currentRadius != null) {
                                numberFreq = currentFrequency
                                distanceFence = currentRadius
                            }
//                            locationRequest.interval = it.body.data.currentFrequency * 1000L
//                            locationRequest.fastestInterval = it.body.data.currentFrequency * 1000L
                            val r = currentFrequency
                            val d = currentRadius
                            CommonMethods.clearLocationDataList(applicationContext)
                            Log.d("APiResponseLna","$numberFreq $distanceFence")
                            Log.d("APiResponseLna1",r.toString())
                            Log.d("APiResponseLna2",d.toString())

                            if (currentFrequency != null && currentRadius != null) {
                                updateLocationRequest(currentFrequency,currentRadius)
                            }


                        }

                    }
                }
            }
        }
    }

    private fun updateLocationRequest(currentFrequency: Int, currentRadius: Int) {

        locationRequest = LocationRequest.create().apply {

            interval = currentFrequency * 1000L
            fastestInterval = interval
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // Previously the rebuilt request was never re-registered, so server-driven frequency/radius
        // changes only took effect after a full service restart. Re-register now so sampling density
        // actually matches the server config (avoids inconsistent point spacing on the route).
        startLocationUpdates()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!canRunTracking()) {
            DeviceStatusLogger.d("location service skipped because user not checked in")
            stopSelf()
            return START_NOT_STICKY
        }

        val lastSavedData = CommonMethods.getLocationDataList(applicationContext)
        if (!lastSavedData.isNullOrEmpty()) {

            if (isInternetAvailable(applicationContext)) callApiLocation()
        }

        startForegroundService()
        startLocationUpdates()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    private fun startForegroundService() {
        val notificationChannelId = "LOCATION_CHANNEL"

        val notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Location Service")
            .setContentText("Tracking location in background")
            .setSmallIcon(R.drawable.location)
            .build()

        startForeground(1, notification)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
//        getLastKnownLocation()

        // Remove any existing registration first so repeated onStartCommand/updateLocationRequest
        // calls (START_STICKY, connectivity changes, boot) don't stack multiple update streams.
        fusedLocationClient.removeLocationUpdates(locationCallback)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "LOCATION_CHANNEL",
                "Location Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(channel)
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

//    private fun isInternetAvailable(): Boolean {
//        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = connectivityManager.activeNetworkInfo
//        return activeNetwork != null && activeNetwork.isConnected
//    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

//    private fun saveLocationToRoom(location: LocationEntity) {
//        GlobalScope.launch {
//            locationDao.insert(location)
//            Log.d("LocationService", "Location saved to Room: $location")
//            checkAndSendStoredLocations()
//        }
//    }

//    private fun checkAndSendStoredLocations() {
//        GlobalScope.launch {
//            val storedLocations = locationDao.getAllLocations()
//            if (isInternetAvailable()) {
//                for (location in storedLocations) {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val sp = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
//                        val token = sp.getString(Constants.AUTH_TOKEN,"")
//                        val dataReturn = CommonMethods.getCurrentDateTime()
//                        val arr = dataReturn.split(" ")
//                        val time = arr[1]
//                        val date = arr[0]
//                        Log.d("dsgsdh",date)
//                        val sendLocationModel = SendLocationModel(date,time,location.latitude,location.longitude)
//                        repository.sendLocationCall(token!!,sendLocationModel).collect{response ->
//                            sendLocationResponse.send(response)
//                        }
//                    }
//                    locationDao.deleteById(location.id)
//                }
//            }
//        }
//    }

    private fun getLastKnownLocation() {
//        CoroutineScope(Dispatchers.IO).launch {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            location?.let {
                val latitude = it.latitude
                val longitude = it.longitude
                Log.d("LocationService", "Location: $latitude, $longitude")


                if (longitudelastsaved == null && latitudelastsaved == null){

                    saveLocation(latitude,longitude)
                    if (isInternetAvailable(applicationContext)){

                        callApiLocation()

                    }
//                    else{
//
//                        saveLocation(location.latitude,location.longitude)
//
//                        val locationListData = LocationList(
//                            CommonMethods.getCurrentDate(),
//                            CommonMethods.getCurrentTime(),
//                            latitude,
//                            longitude
//                        )
//
//                        Log.d("nesaved10","$locationListData")
//                        CommonMethods.saveLocationDataList(applicationContext, locationListData)
//                    }

                }
                else if (longitudelastsaved != null && latitudelastsaved != null) {

                    Log.d("nesaved1","$longitudelastsaved $latitudelastsaved")

                    Log.d("APiResponseLna","$numberFreq $distanceFence ${locationRequest.interval}")
                    var distanceMin = getLocationDistanceMeter(
                        latitudelastsaved!!,
                        longitudelastsaved!!,
                        location.latitude,
                        location.longitude
                    )

                    Log.d("savedLocation1","$distanceMin")

                    if (distanceMin > distanceFence!!) {

                        saveLocation(latitude,longitude)
                        if (isInternetAvailable(applicationContext)){

                            callApiLocation()

                        }
//                        else{
//
//                            val locationListData = LocationList(
//                                CommonMethods.getCurrentDate(),
//                                CommonMethods.getCurrentTime(),
//                                latitude,
//                                longitude
//                            )
//
//                            Log.d("nesaved11","$locationListData")
//                            CommonMethods.saveLocationDataList(applicationContext, locationListData)
//                        }


                    }
                }
//            }
        }
    }


    private fun saveLocation(latitude: Double, longitude: Double) {

        latitudelastsaved = latitude
        longitudelastsaved = longitude

        val battery = DeviceBatteryReader.read(applicationContext)

        val locationListData = LocationList(
            CommonMethods.getCurrentDate(),
            CommonMethods.getCurrentTime(),
            latitude,
            longitude,
            battery.batteryPercent,
            battery.isCharging
        )

        Log.d("nesaved2","$locationListData")
        DeviceStatusLogger.d(
            "offline location saved with batteryPercent=${locationListData.batteryPercent}, isCharging=${locationListData.isCharging}"
        )
        CommonMethods.saveLocationDataList(applicationContext, locationListData)
    }


    private fun callApiLocation(){
        if (!isInternetAvailable(applicationContext)) return
        if (!canRunTracking()) {
            DeviceStatusLogger.d("location upload skipped because user not checked in")
            return
        }
        // Only one upload at a time: a second concurrent call would re-send the same queued
        // points and duplicate them on the server-rendered route.
        if (!isUploading.compareAndSet(false, true)) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sp = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
                val token = sp.getString(Constants.AUTH_TOKEN, "") ?: ""
                if (token.isEmpty()) return@launch

                // Snapshot exactly what we send; on success we remove only these points, so any
                // points saved during the request survive and are never re-sent as duplicates.
                val sendloc = CommonMethods.getLocationDataList(applicationContext) ?: emptyList()
                if (sendloc.isEmpty()) return@launch
                val sentCount = sendloc.size
                val lastPoint = sendloc.last()
                DeviceStatusLogger.d(
                    "location payload prepared with batteryPercent=${lastPoint.batteryPercent}, isCharging=${lastPoint.isCharging}, count=$sentCount"
                )
                DeviceStatusLogger.d(
                    "offline flush sent with last point batteryPercent=${lastPoint.batteryPercent}, isCharging=${lastPoint.isCharging}"
                )

                repository.sendLocationCall(token, sendloc)
                    .collect { response ->
                        when (response) {
                            is ApiState.SUCESS -> {
                                val res = response.getResponse
                                if (DeviceStatusSessionHelper.isSessionExpired(res.resolvedStatusCode, res.resolvedMessage)) {
                                    DeviceStatusSessionHelper.handleSessionExpired(applicationContext)
                                    return@collect
                                }
                                if (res.isSuccessfulPayload()) {
                                    CommonMethods.removeSentLocations(applicationContext, sentCount)
                                    DeviceStatusHeartbeatScheduler.markLocationUploadSuccess(applicationContext)
                                    val currentFrequency = res.resolvedData?.currentFrequency
                                    val currentRadius = res.resolvedData?.currentRadius
                                    if (currentFrequency != null && currentRadius != null) {
                                        numberFreq = currentFrequency
                                        distanceFence = currentRadius
                                        updateLocationRequest(
                                            currentFrequency,
                                            currentRadius
                                        )
                                    }
                                    DeviceStatusLogger.d("location piggyback API success count=$sentCount")
                                    Log.d("LocationService", "Uploaded $sentCount points; freq=$numberFreq radius=$distanceFence")
                                }
                            }
                            is ApiState.ERROR -> {
                                // Keep the queue intact so points retry on the next tick/reconnect.
                                if (DeviceStatusSessionHelper.isSessionExpired(response)) {
                                    DeviceStatusSessionHelper.handleSessionExpired(applicationContext)
                                    return@collect
                                }
                                DeviceStatusLogger.d("location piggyback API failure message=${response.message}")
                                Log.d("LocationService", "Upload error, keeping queue: ${response.message}")
                            }
                            ApiState.LOADING -> {}
                        }
                    }
            } catch (e: Exception) {
                DeviceStatusLogger.e("location piggyback API failure exception=${e.message}", e)
                Log.d("LocationService", "Upload exception, keeping queue: ${e.message}")
            } finally {
                isUploading.set(false)
            }
        }
    }

    private fun canRunTracking(): Boolean {
        val token = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
            .getString(Constants.AUTH_TOKEN, "") ?: ""
        val isCheckedIn = getSharedPreferences(Constants.IS_CHECKEDIN, MODE_PRIVATE)
            .getString(Constants.IS_CHECKEDIN, "NO") == "YES"
        return token.isNotEmpty() && isCheckedIn
    }

    fun getLocationDistanceMeter(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
        val R = 6371000 // Radius of the Earth in meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distanceMeters = (R * c).roundToInt() // Distance in meters

        return distanceMeters
    }
}
