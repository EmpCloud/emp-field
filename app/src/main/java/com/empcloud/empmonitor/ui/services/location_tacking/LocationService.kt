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


    companion object {
        const val ACTION_NETWORK_AVAILABLE = "com.empcloud.empmonitor.ACTION_NETWORK_AVAILABLE"
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
                        if(it.statusCode == 200){

                            Log.d("responselocation",it.body.data.toString())
                            numberFreq = it.body.data.currentFrequency
                            distanceFence = it.body.data.currentRadius
//                            locationRequest.interval = it.body.data.currentFrequency * 1000L
//                            locationRequest.fastestInterval = it.body.data.currentFrequency * 1000L
                            val r = it.body.data.currentFrequency
                            val d = it.body.data.currentRadius
                            CommonMethods.clearLocationDataList(applicationContext)
                            Log.d("APiResponseLna","$numberFreq $distanceFence")
                            Log.d("APiResponseLna1",r.toString())
                            Log.d("APiResponseLna2",d.toString())

                            updateLocationRequest(it.body.data.currentFrequency,it.body.data.currentRadius)


                        }

                    }
                }
            }
        }
    }

    private fun updateLocationRequest(currentFrequency: Int, currentRadius: Int) {

        locationRequest = LocationRequest.create().apply {

            interval = currentFrequency!! * 1000L
            fastestInterval = interval
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

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


        val locationListData = LocationList(
            CommonMethods.getCurrentDate(),
            CommonMethods.getCurrentTime(),
            latitude,
            longitude
        )

        Log.d("nesaved2","$locationListData")
        CommonMethods.saveLocationDataList(applicationContext, locationListData)
    }


    private fun callApiLocation(){

            CoroutineScope(Dispatchers.IO).launch {
                val sp = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
                val token = sp.getString(Constants.AUTH_TOKEN, "")
                val dataReturn = CommonMethods.getCurrentDateTime()
                val arr = dataReturn.split(" ")
                val time = arr[1]
                val date = arr[0]
//                            val sendLocationModel =
//                                SendLocationModel(date, time, location.latitude, location.longitude)

                val sendloc = CommonMethods.getLocationDataList(applicationContext)
//                Log.d("locationtrackingdata",sendloc.toString())
                repository.sendLocationCall(token!!, sendloc!!)
                    .collect { response ->
//                                CommonMethods.clearLocationDataList(applicationContext)
                        sendLocationResponse.send(response)
                    }


//                if (sendloc!!.size == 1) {
//
//                    repository.sendLocationCall(token!!, sendloc!!)
//                        .collect { response ->
////                                CommonMethods.clearLocationDataList(applicationContext)
//                            sendLocationResponse.send(response)
//                        }
//                } else if (sendloc!!.size > 1 && sendloc!!.size <= 100) {
//
//                    repository.sendLocationCall(token!!, sendloc!!)
//                        .collect { response ->
////                                CommonMethods.clearLocationDataList(applicationContext)
//                            sendLocationResponse.send(response)
//                        }
//
//                    delay(5000)
//                } else if (sendloc!!.size > 100) {
//
//                    repository.sendLocationCall(token!!, sendloc!!)
//                        .collect { response ->
////                                CommonMethods.clearLocationDataList(applicationContext)
//                            sendLocationResponse.send(response)
//                        }
//                    delay(15000)
//                }

            }
//        else {
////                Log.d("savedLocation",locationList.toString())
//            CommonMethods.saveLocationDataList(applicationContext, locationListData)
//
//        }
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