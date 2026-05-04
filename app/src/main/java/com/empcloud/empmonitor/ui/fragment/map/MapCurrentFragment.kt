package com.empcloud.empmonitor.ui.fragment.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.content.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel
import com.empcloud.empmonitor.data.remote.request.mode_of_transport.ModeOFTransportModel
import com.empcloud.empmonitor.data.remote.response.attendance.AttendanceFullData
import com.empcloud.empmonitor.data.remote.response.map_response.DirectionsResponse
import com.empcloud.empmonitor.data.remote.response.fetchattendacne.EmployeeLocation
import com.empcloud.empmonitor.databinding.FragmentMapCurrentBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.ui.fragment.client.clientdirection.ClientDirectionViewModel
import com.empcloud.empmonitor.ui.fragment.home.HomeFragment
import com.empcloud.empmonitor.ui.fragment.home.HomeViewModel
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.ui.services.location_tacking.LocationService
import com.empcloud.empmonitor.utils.ActiveTaskTracker
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.empcloud.empmonitor.utils.NativeLib
import com.empcloud.empmonitor.utils.broadcast_services.GeoFenceBroadCastReciever
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ncorti.slidetoact.SlideToActView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

@AndroidEntryPoint
class MapCurrentFragment constructor(private val listener: OnFragmentChangedListener? = null): Fragment(), OnMapReadyCallback {

    private data class GeofenceLocation(
        val id: String,
        val latLng: LatLng,
        val radius: Float,
        val title: String
    )

    private var checkIn: String? = null
    private var isInsideGeofence: Boolean = false
    private var geoCurrentLat: Double? = null
    private var geoCurrentLon: Double? = null
    private var isServiceStart: Boolean? = null

    private var isModeSelected:Boolean = false

    private var mode: String? = null
    private var mode_local: String? = null

    private var isGeoFencingOn:Int? = null

    private var lat:Double? = null
    private var lon:Double? = null


    private lateinit var binding:FragmentMapCurrentBinding
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModelMap by viewModels<ClientDirectionViewModel>()

    private lateinit var geofencingClient: GeofencingClient
    private lateinit var slideActView:SlideToActView
    private val viewModel by viewModels<HomeViewModel> ()
    private val geo_lat = 12.9782871
    private val geo_lon = 77.635809
    private var geo_radius:Float = 50f

    private var isFirstTimeZoom:Boolean = true

    private var isGlobal =  false
    private var isAutoCheckInByGeoFencing = false
    private var hasAutoCheckedIn = false
    private var geofenceLocations: List<GeofenceLocation> = emptyList()
    private var currentPolyline: Polyline? = null


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private var YOUR_GEOFENCE_LATITUDE = 0.0 // Replace with your geofence latitude
        private var YOUR_GEOFENCE_LONGITUDE = 0.0 // Replace with your geofence longitude
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isUserGlobal = CommonMethods.getSharedPrefernceBoolean(requireActivity(),Constants.IS_GLOBAL_USER)
        isGlobal = isUserGlobal

        (activity as? MainActivity)?.setMenuButtonVisibility(false)
        val pref = requireActivity().getSharedPreferences(Constants.AUTH_TOKEN,AppCompatActivity.MODE_PRIVATE)
        val token = pref.getString(Constants.AUTH_TOKEN,"")
        fetchAttendance(token!!)

    }


    private val geofencePendingIntent: PendingIntent by lazy {

        val intent = Intent(requireContext(), GeoFenceBroadCastReciever::class.java)
        PendingIntent.getBroadcast(requireContext(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapCurrentBinding.inflate(layoutInflater,container,false)

        geofencingClient = LocationServices.getGeofencingClient(requireActivity())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

//        createGeofence()
//        startLocationUpdates()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
//
//        val pref = requireActivity().getSharedPreferences(Constants.AUTH_TOKEN,AppCompatActivity.MODE_PRIVATE)
//        val token = pref.getString(Constants.AUTH_TOKEN,"")
//        fetchAttendance(token!!)

//        val sharedPred = requireActivity().getSharedPreferences(
//            Constants.USER_FULL_NAME,
//            AppCompatActivity.MODE_PRIVATE)
//        val name = sharedPred.getString(Constants.NAME_FULL,"")
//        val role = sharedPred.getString(Constants.ROLE,"")
//        if(!name.isNullOrEmpty() && !role.isNullOrEmpty()){
//            binding.name.text = name
//            binding.role.text = role
//        }

        setUserData()

        binding.backbtn.setOnClickListener {

            val homeFragment = HomeFragment(listener)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,homeFragment).commit()
        }

        val date = getCurentFormattedDate()
        binding.bottomSheet.currentDay.text = "$date"
        val time =  getCurrentTime()
        binding.bottomSheet.currentTime.text = "$time"

        binding.checkoutpopup.cancelbtn.setOnClickListener {

            binding.checkoutpopup.checkoutRestrict.visibility = View.GONE
        }

        binding.checkoutpopup.okayBtn.setOnClickListener {

            binding.checkoutpopup.checkoutRestrict.visibility = View.GONE
        }

//
//        binding.transportselection.bike.setOnClickListener {
//
//            isModeSelected = true
//            mode = "bike"
//            binding.transportselection.bike.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.car.setBackgroundResource(R.drawable.add_leave_bg)
//
//            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
//            binding.transportselection.textcar.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))
//
//        }
//
//        binding.transportselection.car.setOnClickListener {
//
//            isModeSelected = true
//            mode = "car"
//            binding.transportselection.car.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.bike.setBackgroundResource(R.drawable.add_leave_bg)
//
//            binding.transportselection.textcar.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
//
//            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))
//
//
//        }

        binding.transportselection.bike.setOnClickListener {

            isModeSelected = true
            mode = "bike"
            mode_local = "bike"
            commonSelection()

            binding.transportselection.bike.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.car.setBackgroundResource(R.drawable.add_leave_bg)

            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
//            binding.transportselection.textcar.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))

        }

        binding.transportselection.car.setOnClickListener {

            isModeSelected = true
            mode = "car"
            mode_local = "car"
            commonSelection()
            binding.transportselection.car.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.bike.setBackgroundResource(R.drawable.add_leave_bg)

            binding.transportselection.textcar.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

//            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))


        }

        binding.transportselection.rail.setOnClickListener {

            isModeSelected = true
            mode = "car"
            mode_local = "rail"
            commonSelection()
            binding.transportselection.rail.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.bike.setBackgroundResource(R.drawable.add_leave_bg)

            binding.transportselection.textrail.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

//            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))


        }

        binding.transportselection.auto.setOnClickListener {

            isModeSelected = true
            mode = "car"
            mode_local = "auto"
            commonSelection()
            binding.transportselection.auto.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.bike.setBackgroundResource(R.drawable.add_leave_bg)

            binding.transportselection.textauto.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

//            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))


        }

        binding.transportselection.bus.setOnClickListener {

            isModeSelected = true
            mode = "car"
            mode_local = "bus"
            commonSelection()
            binding.transportselection.bus.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.bike.setBackgroundResource(R.drawable.add_leave_bg)

            binding.transportselection.textbus.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

//            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))


        }

        binding.transportselection.selectionbtn.setOnClickListener {

            if (isModeSelected){

                val modeOFTransportModel = ModeOFTransportModel(mode!!)
                viewModel.invokeTransportUpdation(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),modeOFTransportModel)
            }else{

                Toast.makeText(requireContext(),"Please select mode of transport",Toast.LENGTH_SHORT).show()
            }

        }


        setUserPic()
//        checkSlidAct()
        slideToAct()
        observeAttendanceCall()
        observeAttendanceFetchCall()
        observeUpdationTransport()

    }

    private fun fetchAttendance(authToken: String){

        viewModel.invokeFetchAttendance(authToken)
    }
    fun getCurentFormattedDate():String{
        val currentDate = System.currentTimeMillis()
        val format = SimpleDateFormat("E, d MMM", Locale.getDefault())
        return format.format(Date(currentDate))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

//        map.uiSettings.isScrollGesturesEnabled = false

        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.uiSettings.isCompassEnabled = false

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = false
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    geoCurrentLat = location.latitude
                    geoCurrentLon = location.longitude
                    renderMap(location)
                } else {
                    renderMap()
                }
            }
        } else {
            renderMap()
        }

        googleMap.setOnCameraMoveListener(null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(): String {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        val formattedTime = currentTime.format(formatter)
        return formattedTime.replace("AM", "am").replace("PM", "pm")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setFormattedTimeWithAmPm(textView: TextView, timeString: String) {
        // Create a SpannableString from the formatted time
        val spannableString = SpannableString(timeString)

        // Find the start index of the "am"/"pm" part
        val amPmStartIndex = if (timeString.contains("am")) {
            timeString.indexOf("am")
        } else {
            timeString.indexOf("pm")
        }

        // Apply RelativeSizeSpan to the "am"/"pm" part to set its size to 0.8 times the original size
        spannableString.setSpan(
            RelativeSizeSpan(0.8f), // Adjust this value to achieve the desired size (0.8f means 80% of the default size)
            amPmStartIndex,
            amPmStartIndex + 2, // Assuming "am" or "pm" are two characters
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set the SpannableString to the TextView
        textView.text = spannableString
    }

    private fun slideToAct(){
        slideActView = binding.bottomSheet.sliderActView
        slideActView.isReversed = false


        slideActView.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener{
            override fun onSlideComplete(view: SlideToActView) {
                lifecycleScope.launch {

                    slideActView.resetSlider()

                    if(slideActView.isReversed){

                        slideActView.isReversed = false
                        slideActView.text = "Swipe to Check IN"
                        slideActView.textColor = requireActivity().resources.getColor(R.color.swipe_text)
                        slideActView.outerColor = requireActivity().resources.getColor(R.color.swipe_outercolor)
                        slideActView.innerColor = requireActivity().resources.getColor(R.color.swipe_innercolor)
                        showPopUpConfirmation()

                    }else {

                        slideActView.isReversed = true
                        slideActView.text = "Swipe to Check OUT"
                        slideActView.textColor = requireActivity().resources.getColor(R.color.white)
                        slideActView.outerColor = requireActivity().resources.getColor(R.color.swipe_reverse_outer)
                        slideActView.innerColor = requireActivity().resources.getColor(R.color.white)
                        slideActView.iconColor = requireActivity().resources.getColor(R.color.swipe_reverse_outer)
//                        CommonMethods.clearLocationDataList(requireContext())
                        getLastLocation()


                    }
                }

                lifecycleScope.launch{
                    if (!binding.bottomSheet.outTime.text.isEmpty()){

                        slideActView.isReversed = true
                        slideActView.text = "Swipe to Check OUT"
                        slideActView.textColor = requireActivity().resources.getColor(R.color.white)
                        slideActView.outerColor = requireActivity().resources.getColor(R.color.swipe_reverse_outer)
                        slideActView.innerColor = requireActivity().resources.getColor(R.color.white)
                        slideActView.iconColor = requireActivity().resources.getColor(R.color.swipe_reverse_outer)

//                        showPopUpConfirmation()


                    }
                }
            }

        }

    }

    private fun markAttendance(authToken:String,markAttendanceModel: MarkAttendanceModel){

        viewModel.invokeAttendaceMarking(authToken,markAttendanceModel)
        Log.d("Inside viewmodel","viewmodel")
    }


    private fun observeAttendanceCall(){
        lifecycleScope.launch {
            with(viewModel){
                observerAttendanceFlow.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
                            android.util.Log.d("Inside viewmodel","observer")

                            if (it.statusCode == 400 ){

                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                                if (it.body.status.equals("failed")) {
                                    if (checkIn.isNullOrEmpty()) updateSliderForCheckIn() else updateSliderForCheckOut()
                                }
                                if (it.body.message.equals("Please Pause or complete OnGoing Tasks to Proceed!"))  CommonMethods.switchFragment(requireActivity(),TaskHomeFragment(listener))
//
                            }
                            else if(it.body.data.code == 200){


//                                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.GEO_PREF,Constants.GEO_RADIUS,"30")
//                                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.GEO_PREF,Constants.GEO_LAT,"12.9782871")
//                                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.GEO_PREF,Constants.GEO_LON,"77.635809")
//                                android.widget.Toast.makeText(requireContext(),it.body.data.message, android.widget.Toast.LENGTH_SHORT).show()
                                val time = it.body.data.data.time
                                if(it.body.data.message.equals("Successfully Checked Out")){

                                    stopLocationService()
                                    CommonMethods.clearLocationDataList(requireContext())
                                    CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.IS_CHECKEDIN)
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKEDIN,Constants.IS_CHECKEDIN,"NO")
                                    CommonMethods.clearStringFromSharedPreferences(requireContext(), Constants.AUTO_CHECK_IN_TIME)
                                    CommonMethods.cancelAutoCheckout(requireContext())
                                    binding.checkoutConfirm.customeAlertcheckout.visibility = View.GONE
                                    binding.bottomSheet.outTime.text =  formatApiTime(parseTimeData(time))
                                    binding.bottomSheet.outCheck.isChecked = true
                                    binding.bottomSheet.outCheck.isEnabled = false

                                }else{
                                    if (isServiceStart == true && !isGlobal) startLocationService()
                                    checkIn = time
                                    binding.bottomSheet.inTime.text =  formatApiTime(parseTimeData(time))
                                    binding.bottomSheet.inCheckBox.isChecked = true
                                    binding.bottomSheet.inCheckBox.isEnabled = false

                                    ActiveTaskTracker.activeTaskId = null
                                    CommonMethods.clearLocationDataList(requireContext())
                                    if (!isGlobal) binding.transportselection.poptransport.visibility = View.VISIBLE
                                    else binding.transportselection.poptransport.visibility = View.GONE
                                    CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.IS_CHECKEDIN)
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKEDIN,Constants.IS_CHECKEDIN,"YES")
                                    CommonMethods.saveSharedPrefernceBoolean(requireActivity(),Constants.NOTIFICATION_ALL_READ,Constants.NOTIFICATION_ALL_READ,false)
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.CHECK_IN_METHOD,Constants.CHECK_IN_METHOD,Constants.CHECK_IN_METHOD_MAP)

                                    if (isAutoCheckInByGeoFencing) {
                                        val autoCheckInTime = formatApiTime(parseTimeData(time))
                                        requireContext().getSharedPreferences(Constants.AUTO_CHECK_IN_TIME, AppCompatActivity.MODE_PRIVATE)
                                            .edit()
                                            .putString(Constants.AUTO_CHECK_IN_TIME, autoCheckInTime)
                                            .apply()
                                        CommonMethods.scheduleAutoCheckout(requireContext())
                                        binding.bottomSheet.autoCheckInText.text = "Auto Checked In via Map at $autoCheckInTime"
                                        binding.bottomSheet.autoCheckInText.visibility = View.VISIBLE
                                        binding.bottomSheet.sliderActView.visibility = View.VISIBLE
                                        updateSliderForCheckOut()
                                    }

                                }

                            }

                            else if(it.body.data.code == 400){
                                android.widget.Toast.makeText(requireContext(),it.body.data.message, android.widget.Toast.LENGTH_SHORT).show()
                                slideActView.isReversed = false
                                slideActView.text = "Swipe to Check IN"
                                slideActView.textColor = requireActivity().resources.getColor(R.color.swipe_text)
                                slideActView.outerColor = requireActivity().resources.getColor(R.color.swipe_outercolor)
                                slideActView.innerColor = requireActivity().resources.getColor(R.color.swipe_innercolor)
                            }

                            else if(it.body.data.code == 403){

                                binding.checkoutpopup.checkoutRestrict.visibility = View.VISIBLE
                                binding.checkoutConfirm.customeAlertcheckout.visibility = View.GONE

//                                Toast.makeText(requireContext(),it.body.data.message, Toast.LENGTH_SHORT).show()
                            }


                        }
                        is ApiState.LOADING -> {

                        }
                        is ApiState.ERROR -> {


                        }
                    }
                }
            }
        }
    }

    private fun updateSliderForCheckIn() {

        slideActView.isReversed = false
        slideActView.text = "Swipe to Check IN"
        slideActView.textColor = requireActivity().resources.getColor(R.color.swipe_text)
        slideActView.outerColor = requireActivity().resources.getColor(R.color.swipe_outercolor)
        slideActView.innerColor = requireActivity().resources.getColor(R.color.swipe_innercolor)

    }

    fun SPLit(time: String):String{
        val timeSplit = time
        val parts = timeSplit.split(" ")

        return parts[1]
    }


    @SuppressLint("SimpleDateFormat")
    fun formatApiTime(apiTime: String): String {
        // Define the formatter for the incoming time string
        val inputFormatter = SimpleDateFormat("HH:mm:ss")
        val parsedDate = inputFormatter.parse(apiTime)
        val cal = Calendar.getInstance()
        cal.time = parsedDate!!

        // Define the formatter for the output time string with lowercase AM/PM
        val outputFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        // Format the time to the desired pattern
        val formattedTime = outputFormatter.format(cal.time)

        // Convert the AM/PM part to lowercase
        return formattedTime.replace("AM", "am").replace("PM", "pm")
    }

    private fun getBitmapFromDrawable(drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(requireContext(), drawableId)!!
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun observeAttendanceFetchCall(){
        lifecycleScope.launch {
            with(viewModel){
                observeFetchAttendanceCall.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
                            Log.d("Inside viewmodel","observer")

                            if(it.statusCode == 200){

//                                Toast.makeText(requireContext(),it.body.data.data.message, Toast.LENGTH_SHORT).show()
                                val checkinTime = it.body.data.data.data.check_in
                                checkIn = it.body.data.data.data.check_in
                                val checkOutTime  = it.body.data.data.data.check_out
                                isServiceStart = it.body.data.geoLogStatus
//                                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.LAST_MODE_SELECTED)
//                                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.LAST_MODE_SELECTED,Constants.LAST_MODE_SELECTED,it.body.data.currentMode)


                                if (!it.body.data.orglatitude.isNullOrEmpty() && !it.body.data.orglongitude.isNullOrEmpty()){

                                    parseLatitude(it.body.data.orglatitude)?.let { latitude ->
                                        YOUR_GEOFENCE_LATITUDE = latitude
                                    }
                                    parseLongitude(it.body.data.orglongitude)?.let { longitude ->
                                        YOUR_GEOFENCE_LONGITUDE = longitude
                                    }
                                }

//                                Log.d("asgasdf",it.body.data.orgRadius.toString())
                                if (it.body.data.orgRadius != null && it.body.data.orgRadius > 0f){
                                    geo_radius = it.body.data.orgRadius
                                }

                                geofenceLocations = buildGeofenceLocations(
                                    it.body.data.employeeLocations,
                                    it.body.data.orglatitude,
                                    it.body.data.orglongitude,
                                    it.body.data.orgRadius
                                )
                                if (geofenceLocations.isNotEmpty()) {
                                    val primaryGeofence = geofenceLocations.first()
                                    YOUR_GEOFENCE_LATITUDE = primaryGeofence.latLng.latitude
                                    YOUR_GEOFENCE_LONGITUDE = primaryGeofence.latLng.longitude
                                    geo_radius = primaryGeofence.radius
                                }
                                hasAutoCheckedIn = !checkinTime.isNullOrEmpty()
                                renderMap()

                                if (it.body.data.currentFrequency != null){

                                    CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.FREQUENCY)
                                    val sp = requireContext().getSharedPreferences(Constants.FREQUENCY,AppCompatActivity.MODE_PRIVATE)
                                    sp.edit().putInt(Constants.FREQUENCY,it.body.data.currentFrequency).apply()
                                    sp.edit().putInt(Constants.FENCE_RADIUS,it.body.data.currentRadius).apply()
                                }

//                                Log.d("statuscodegeofencing",it.body.data.isGeoFencingOn.toString())

                                // Check auto check-in by geofencing flag
                                val autoCheckInByGeoFencing = requireContext().getSharedPreferences(Constants.AUTO_CHECK_IN_BY_GEO_FENCING, AppCompatActivity.MODE_PRIVATE)
                                    .getInt(Constants.AUTO_CHECK_IN_BY_GEO_FENCING, 0)

                                Log.d("AutoCheckIn", "MapFragment: isGeoFencingOn=${it.body.data.isGeoFencingOn}, isMobileDeviceEnabled=${it.body.data.isMobileDeviceEnabled}, autoCheckInByGeoFencing=$autoCheckInByGeoFencing")

                                if (it.body.data.isGeoFencingOn == 1) {

                                    isGeoFencingOn = it.body.data.isGeoFencingOn
                                    isAutoCheckInByGeoFencing = autoCheckInByGeoFencing == 1
                                    Log.d("AutoCheckIn", "MapFragment: Geofencing ON, isAutoCheckInByGeoFencing=$isAutoCheckInByGeoFencing")
                                    if (hasValidGeofenceConfig()) {
                                        createGeofence()
                                        startLocationUpdates()
                                    } else {
                                        Log.e(
                                            "AutoCheckIn",
                                            "MapFragment: Invalid geofence config lat=$YOUR_GEOFENCE_LATITUDE lon=$YOUR_GEOFENCE_LONGITUDE radius=$geo_radius"
                                        )
                                        binding.bottomSheet.sliderActView.visibility = View.GONE
                                        binding.bottomSheet.autoCheckInText.visibility = View.GONE
                                        binding.bottomSheet.swipeDisable.visibility = View.VISIBLE
                                    }

                                }
                                else if (it.body.data.isGeoFencingOn == 0) {

                                    zoomToCurrentLocation()
                                    val autoCheckInTime = requireContext().getSharedPreferences(Constants.AUTO_CHECK_IN_TIME, AppCompatActivity.MODE_PRIVATE)
                                        .getString(Constants.AUTO_CHECK_IN_TIME, null)
                                    if (!checkinTime.isNullOrEmpty() && !autoCheckInTime.isNullOrEmpty()) {
                                        binding.bottomSheet.autoCheckInText.text = "Auto Checked In at $autoCheckInTime"
                                        binding.bottomSheet.autoCheckInText.visibility = View.VISIBLE
                                    } else {
                                        binding.bottomSheet.autoCheckInText.visibility = View.GONE
                                    }
                                    binding.bottomSheet.sliderActView.visibility = View.VISIBLE
                                }


                                if (!checkinTime.isNullOrEmpty()){

                                    binding.bottomSheet.inTime.text = formatApiTime(parseTimeData(checkinTime))
                                    binding.bottomSheet.inCheckBox.isChecked = true

                                    val autoCheckInTime = requireContext().getSharedPreferences(Constants.AUTO_CHECK_IN_TIME, AppCompatActivity.MODE_PRIVATE)
                                        .getString(Constants.AUTO_CHECK_IN_TIME, null)
                                    if (!autoCheckInTime.isNullOrEmpty()) {
                                        binding.bottomSheet.autoCheckInText.text = "Auto Checked In via Map at $autoCheckInTime"
                                        binding.bottomSheet.autoCheckInText.visibility = View.VISIBLE
                                        binding.bottomSheet.sliderActView.visibility = View.GONE
                                    } else {
                                        slideActView.isReversed = true
                                        slideActView.text = "Swipe to Check OUT"
                                        slideActView.textColor = requireActivity().resources.getColor(R.color.white)
                                        slideActView.outerColor = requireActivity().resources.getColor(R.color.swipe_reverse_outer)
                                        slideActView.innerColor = requireActivity().resources.getColor(R.color.white)
                                        slideActView.iconColor = requireActivity().resources.getColor(R.color.swipe_reverse_outer)
                                        val formattedTime = formatApiTime(parseTimeData(checkinTime))
                                        binding.bottomSheet.autoCheckInText.text = "Checked In via Map at $formattedTime"
                                        binding.bottomSheet.autoCheckInText.visibility = View.VISIBLE
                                        binding.bottomSheet.sliderActView.visibility = View.VISIBLE
                                    }
                                }

                                if(!checkOutTime.isNullOrEmpty())
                                {
                                    binding.bottomSheet.outTime.text = formatApiTime(parseTimeData(checkOutTime))
                                    binding.bottomSheet.outCheck.isChecked = true

                                }

                                if(checkinTime.isNullOrEmpty() && checkOutTime.isNullOrEmpty()){

                                    CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.IS_CHECKEDIN)
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKEDIN,Constants.IS_CHECKEDIN,"NO")
                                    stopLocationService()

                                    CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.IS_CHECKED_OUT)
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKED_OUT,Constants.IS_CHECKED_OUT,"NO")

                                }else{

                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKED_OUT,Constants.IS_CHECKED_OUT,"YES")
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKEDIN,Constants.IS_CHECKEDIN,"NO")
                                }


                                if (!checkinTime.isNullOrEmpty() &&  checkOutTime.isNullOrEmpty()){

                                    CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.IS_CHECKEDIN)
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKEDIN,Constants.IS_CHECKEDIN,"YES")
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKED_OUT,Constants.IS_CHECKED_OUT,"NO")
                                    if (isServiceStart == true && !isGlobal) startLocationService()
                                    else stopLocationService()
                                }

                            }

                            if(it.statusCode == 400){

                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()

                            }



                        }
                        is ApiState.LOADING -> {

                        }
                        is ApiState.ERROR -> {

                            Toast.makeText(requireContext(),"Session Expired",Toast.LENGTH_SHORT).show()
                            CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.AUTH_TOKEN)
                            val intent = Intent(requireContext(),LoginOptionsActivity::class.java)
                            requireContext().startActivity(intent)
                            requireActivity().finish()

                        }
                    }
                }
            }
        }
    }



    private fun createGeofence() {
        if (!hasValidGeofenceConfig()) {
            Log.e(
                "AutoCheckIn",
                "Skipping geofence creation because config is invalid. geofenceCount=${geofenceLocations.size}"
            )
            return
        }

        val geofences = geofenceLocations.map { geofenceLocation ->
            Geofence.Builder()
                .setRequestId(geofenceLocation.id)
                .setCircularRegion(
                    geofenceLocation.latLng.latitude,
                    geofenceLocation.latLng.longitude,
                    geofenceLocation.radius
                )
                .setExpirationDuration(TimeUnit.DAYS.toMillis(1))
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
        }

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofences)
            .build()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
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
        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)?.run {
            addOnSuccessListener {
                // Geofences added successfully
            }
            addOnFailureListener {
                // Failed to add geofences
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.requestLocationUpdates(
            LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 10000 // Update interval in milliseconds
                fastestInterval = 5000 // Fastest update interval in milliseconds
            },
            locationCallback,
            null
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.lastLocation?.let { currentLocation ->
                geoCurrentLat = currentLocation.latitude
                geoCurrentLon = currentLocation.longitude
                renderMap(currentLocation)

                val activeGeofence = findMatchingGeofence(currentLocation) ?: findNearestGeofence(currentLocation)
                isInsideGeofence = activeGeofence?.let { isWithinGeofence(currentLocation, it) } == true

                if (isInsideGeofence) {
                    Log.d("AutoCheckIn", "MapFragment: User is INSIDE geofence, isAutoCheckInByGeoFencing=$isAutoCheckInByGeoFencing")
                    binding.messageFencing.visibility = View.GONE
                    binding.bottomSheet.swipeDisable.visibility = View.GONE
                    binding.bottomSheet.swipeDisablecheckout.visibility = View.GONE

                    if (isAutoCheckInByGeoFencing) {
                        if (checkIn.isNullOrEmpty() && !hasAutoCheckedIn) {
                            Log.d("AutoCheckIn", "MapFragment: Hiding slider, showing 'Automatic Check In' text")
                            binding.bottomSheet.sliderActView.visibility = View.GONE
                            binding.bottomSheet.autoCheckInText.text = "Automatic Check-In in progress"
                            binding.bottomSheet.autoCheckInText.visibility = View.VISIBLE
                            Log.d("AutoCheckIn", "MapFragment: Auto check-in triggered via geofence")
                            hasAutoCheckedIn = true
                            getLastLocation()
                        } else if (checkIn.isNullOrEmpty()) {
                            Log.d("AutoCheckIn", "MapFragment: Auto check-in is in progress")
                            binding.bottomSheet.sliderActView.visibility = View.GONE
                            binding.bottomSheet.autoCheckInText.text = "Automatic Check-In in progress"
                            binding.bottomSheet.autoCheckInText.visibility = View.VISIBLE
                        } else {
                            Log.d("AutoCheckIn", "MapFragment: Auto check-in already completed, showing checkout slider")
                            val autoCheckInTime = requireContext().getSharedPreferences(Constants.AUTO_CHECK_IN_TIME, AppCompatActivity.MODE_PRIVATE)
                                .getString(Constants.AUTO_CHECK_IN_TIME, null)
                            if (!autoCheckInTime.isNullOrEmpty()) {
                                binding.bottomSheet.autoCheckInText.text = "Auto Checked In via Map at $autoCheckInTime"
                                binding.bottomSheet.autoCheckInText.visibility = View.VISIBLE
                                binding.bottomSheet.sliderActView.visibility = View.GONE
                            } else if (!checkIn.isNullOrEmpty()) {
                                val formattedTime = formatApiTime(parseTimeData(checkIn!!))
                                binding.bottomSheet.autoCheckInText.text = "Checked In via Map at $formattedTime"
                                binding.bottomSheet.autoCheckInText.visibility = View.VISIBLE
                                binding.bottomSheet.sliderActView.visibility = View.VISIBLE
                                updateSliderForCheckOut()
                            } else {
                                binding.bottomSheet.autoCheckInText.visibility = View.GONE
                                binding.bottomSheet.sliderActView.visibility = View.VISIBLE
                                updateSliderForCheckOut()
                            }
                        }
                    } else {
                        Log.d("AutoCheckIn", "MapFragment: Showing normal swipe slider")
                        val autoCheckInTime = requireContext().getSharedPreferences(Constants.AUTO_CHECK_IN_TIME, AppCompatActivity.MODE_PRIVATE)
                            .getString(Constants.AUTO_CHECK_IN_TIME, null)
                        if (!checkIn.isNullOrEmpty() && !autoCheckInTime.isNullOrEmpty()) {
                            binding.bottomSheet.autoCheckInText.text = "Auto Checked In via Map at $autoCheckInTime"
                            binding.bottomSheet.autoCheckInText.visibility = View.VISIBLE
                            binding.bottomSheet.sliderActView.visibility = View.GONE
                        } else {
                            binding.bottomSheet.autoCheckInText.visibility = View.GONE
                            binding.bottomSheet.sliderActView.visibility = View.VISIBLE
                        }
                    }
                } else {
                    Log.d("AutoCheckIn", "MapFragment: User is OUTSIDE geofence")
                    if(!checkIn.isNullOrEmpty()) binding.bottomSheet.swipeDisablecheckout.visibility = View.VISIBLE
                    else binding.bottomSheet.swipeDisable.visibility = View.VISIBLE
                    binding.bottomSheet.sliderActView.visibility = View.GONE
                    binding.bottomSheet.autoCheckInText.visibility = View.GONE

                    binding.messageFencing.visibility = View.VISIBLE

                }

                activeGeofence?.let { drawRouteToGeofence(it) }
            }
        }
    }

    private fun drawRouteToGeofence(targetGeofence: GeofenceLocation) {
        val currentLat = geoCurrentLat ?: return
        val currentLon = geoCurrentLon ?: return

                        val call = viewModelMap.mMapApiService.getDirections(
                            "$currentLat,$currentLon",
                            "${targetGeofence.latLng.latitude},${targetGeofence.latLng.longitude}",
                            NativeLib().getGoogleMapApiKey()

                        )
                        call.enqueue(object : Callback<DirectionsResponse> {
                            override fun onResponse(
                                call: Call<DirectionsResponse>,
                                response: Response<DirectionsResponse>
                            ) {
                                if (response.isSuccessful) {

                                    val directionsResponse = response.body()
                                    if (directionsResponse != null) {
                                        val route = directionsResponse.routes.firstOrNull()
                                        if (route != null) {
                                            val points = route.overview_polyline.points
                                            val decodedPath = PolyUtil.decode(points)
                                            currentPolyline?.remove()
                                            currentPolyline = map.addPolyline(
                                                PolylineOptions().addAll(decodedPath).color(
                                                    Color.RED
                                                ).width(10f)
                                            )
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                                // Handle failure
                                Toast.makeText(
                                    requireContext(),
                                    "ERROR WHILE DRAWING",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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

        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

//        location.let {
//
//            val lat = it?.latitude
//            val lon = it?.longitude
//
//            Log.d("checkingcrash","$location")
//            val markAttendanceModel = MarkAttendanceModel(CommonMethods.getCurrentTime(),location!!.latitude,location.longitude)
//            markAttendance(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),markAttendanceModel)
//
//        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : android.location.Location? ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    val markAttendanceModel = MarkAttendanceModel(CommonMethods.getCurrentTime(),latitude,longitude)
                    markAttendance(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),markAttendanceModel)
//                    lat = latitude
//                    lon = longitude
//                    Toast.makeText(requireContext(), "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_LONG).show()
                } ?: run {
                    Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_LONG).show()
            }
    }

    private fun showCheckoutConfirmationDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Are you sure you want to checkout?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                // User clicked Yes button
                val pref = requireActivity().getSharedPreferences(Constants.AUTH_TOKEN,AppCompatActivity.MODE_PRIVATE)
                val token = pref.getString(Constants.AUTH_TOKEN,"")
                getLastLocation()

            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                // User clicked No button
                dialog.dismiss()
            })
        // Create the AlertDialog object and show it
        builder.create().show()
    }

    private fun setUserPic(){

//        val pic = CommonMethods.getBitmapFromSharedPreferences(requireContext(),Constants.BITMAP_RECIEVE)
        val pic = CommonMethods.getSharedPrefernce(requireActivity(),Constants.PROFILE_PIC_URL_USER)
        if(!pic.isNullOrEmpty()){
            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
            Picasso.get().load(pic).into(binding.userPic)
//            binding.userPic.setImageBitmap(pic)
        }
        else{
            binding.userPic.visibility = View.GONE
            binding.userPicdisbale.visibility = View.VISIBLE
        }

    }

    private fun stopLocationService() {
        context?.let { context ->
            val serviceIntent = Intent(context, LocationService::class.java)
            context.stopService(serviceIntent)
        }
    }

    private fun startLocationService() {
        context?.let { context ->
            val serviceIntent = Intent(context, LocationService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, serviceIntent)
            } else {
                context.startService(serviceIntent)
            }

            handleBatteryOptimizations()
        }
    }

    private fun handleBatteryOptimizations() {
        context?.let { context ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val packageName = context.packageName
                val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                    intent.data = Uri.parse("package:$packageName")
                    startActivity(intent)
                }
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        (activity as? MainActivity)?.setMenuButtonVisibility(false)
//        setUserPic()
//        setUserData()
//        Log.d("checkingcall","first")
//        val pref = requireActivity().getSharedPreferences(Constants.AUTH_TOKEN,AppCompatActivity.MODE_PRIVATE)
//        val token = pref.getString(Constants.AUTH_TOKEN,"")
//        fetchAttendance(token!!)
//        Log.d("camehere","second")
//    }


    private fun showPopUpConfirmation(){

        binding.checkoutConfirm.customeAlertcheckout.visibility = View.VISIBLE

        binding.checkoutConfirm.okayBtn.setOnClickListener {

            getLastLocation()
            binding.checkoutConfirm.customeAlertcheckout.visibility = View.GONE


        }

        binding.checkoutConfirm.cancelbtn.setOnClickListener {

            binding.checkoutConfirm.customeAlertcheckout.visibility = View.GONE

        }

        binding.checkoutConfirm.abortbtn.setOnClickListener {

            binding.checkoutConfirm.customeAlertcheckout.visibility = View.GONE

        }
    }

    private fun zoomToCurrentLocation() {
        if (checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED &&
            checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                renderMap(location)
            }
        }
    }

    fun parseTimeData(time:String):String{

        val timeExtract = time.split(Regex("[T ]"))
        return  againparseTimeData(timeExtract[1])
    }


    fun againparseTimeData(time:String):String{

        val timeExtract = time.split(".")
        return  timeExtract[0]
    }


    fun setUserData(){

        val sharedPred = requireActivity().getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
        val name = sharedPred.getString(Constants.NAME_FULL,"")
        val role = sharedPred.getString(Constants.ROLE,"")
        if(!name.isNullOrEmpty() && !role.isNullOrEmpty()){
            binding.name.text = name
            binding.role.text = role
        }else{

            binding.name.text = CommonMethods.getSharedPrefernce(requireActivity(),Constants.LOGIN_NAME_PERSON)
            binding.role.text = CommonMethods.getSharedPrefernce(requireActivity(),Constants.LOGIN_ROLE_PERSON)

        }
    }


    private fun calculateZoomLevel(point1: LatLng, point2: LatLng): Double {
        // Example implementation using a simple zoom level calculation based on distance
        // You may need to adjust this based on your specific requirements
        val distance = calculateDistance(point1, point2)
        return 14.0 - Math.log(distance) / Math.log(2.0)  // Example formula for zoom level
    }

    // Example function to calculate midpoint between two LatLng points
    private fun calculateMidpoint(point1: LatLng, point2: LatLng): LatLng {
        val lat = (point1.latitude + point2.latitude) / 2
        val lng = (point1.longitude + point2.longitude) / 2
        return LatLng(lat, lng)
    }

    // Example function to calculate distance between two LatLng points (using Haversine formula)
    private fun calculateDistance(point1: LatLng, point2: LatLng): Double {
        // Example implementation using Haversine formula
        // You may need to implement a more accurate distance calculation based on your needs
        // For simplicity, assuming a flat surface here (not accounting for earth curvature)
        val latDistance = Math.toRadians(point2.latitude - point1.latitude)
        val lngDistance = Math.toRadians(point2.longitude - point1.longitude)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(point1.latitude)) * Math.cos(Math.toRadians(point2.latitude)) *
                Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return 6371000.0 * c  // Radius of the Earth in meters
    }

    private fun observeUpdationTransport(){

        lifecycleScope.launch {
            with(viewModel){
                observeTransportCall.collect{res ->
                    when(res){

                        is ApiState.ERROR -> {}
                        com.empcloud.empmonitor.network.api_satatemanagement.ApiState.LOADING -> {}
                        is ApiState.SUCESS -> {

                            val it = res.getResponse
                            if (it.statusCode == 200){

                                binding.transportselection.poptransport.visibility = android.view.View.GONE
                                android.widget.Toast.makeText(requireContext(),it.body.message,
                                    android.widget.Toast.LENGTH_SHORT).show()
                                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.LAST_MODE_SELECTED)
                                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.LAST_MODE_SELECTED,Constants.LAST_MODE_SELECTED,mode_local!!)
                            }

                        }
                    }
                }
            }
        }
    }


    private fun commonSelection() {

        binding.transportselection.bike.setBackgroundResource(R.drawable.add_leave_bg)
        binding.transportselection.car.setBackgroundResource(R.drawable.add_leave_bg)
        binding.transportselection.auto.setBackgroundResource(R.drawable.add_leave_bg)
        binding.transportselection.rail.setBackgroundResource(R.drawable.add_leave_bg)
        binding.transportselection.bus.setBackgroundResource(R.drawable.add_leave_bg)


        binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))
        binding.transportselection.textcar.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))
        binding.transportselection.textauto.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))
        binding.transportselection.textrail.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))
        binding.transportselection.textbus.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))

    }

    private fun updateSliderForCheckOut(){
        slideActView.isReversed = true
        slideActView.text = "Swipe to Check OUT"
        slideActView.textColor = requireActivity().resources.getColor(R.color.white)
        slideActView.outerColor = requireActivity().resources.getColor(R.color.swipe_reverse_outer)
        slideActView.innerColor = requireActivity().resources.getColor(R.color.white)
        slideActView.iconColor = requireActivity().resources.getColor(R.color.swipe_reverse_outer)
    }

    private fun parseLatitude(value: String?): Double? {
        val parsed = value?.toDoubleOrNull()
        return if (parsed != null && parsed in -90.0..90.0) parsed else null
    }

    private fun parseLongitude(value: String?): Double? {
        val parsed = value?.toDoubleOrNull()
        return if (parsed != null && parsed in -180.0..180.0) parsed else null
    }

    private fun buildGeofenceLocations(
        employeeLocations: List<EmployeeLocation>?,
        fallbackLatitude: String?,
        fallbackLongitude: String?,
        fallbackRadius: Float
    ): List<GeofenceLocation> {
        val mappedEmployeeLocations = employeeLocations.orEmpty().mapNotNull { employeeLocation ->
            val latitude = parseLatitude(employeeLocation.latitude) ?: return@mapNotNull null
            val longitude = parseLongitude(employeeLocation.longitude) ?: return@mapNotNull null
            if (employeeLocation.range <= 0) return@mapNotNull null

            GeofenceLocation(
                id = employeeLocation._id,
                latLng = LatLng(latitude, longitude),
                radius = employeeLocation.range.toFloat(),
                title = employeeLocation.description?.ifBlank { employeeLocation.address ?: "" } ?: employeeLocation.address ?: ""
            )
        }

        if (mappedEmployeeLocations.isNotEmpty()) {
            return mappedEmployeeLocations
        }

        val latitude = parseLatitude(fallbackLatitude) ?: return emptyList()
        val longitude = parseLongitude(fallbackLongitude) ?: return emptyList()
        if (fallbackRadius <= 0f) return emptyList()

        return listOf(
            GeofenceLocation(
                id = "fallback_org_location",
                latLng = LatLng(latitude, longitude),
                radius = fallbackRadius,
                title = "Office Location"
            )
        )
    }

    private fun findMatchingGeofence(location: Location): GeofenceLocation? {
        return geofenceLocations.firstOrNull { geofenceLocation ->
            isWithinGeofence(location, geofenceLocation)
        }
    }

    private fun findNearestGeofence(location: Location): GeofenceLocation? {
        return geofenceLocations.minByOrNull { geofenceLocation ->
            distanceToGeofence(location, geofenceLocation)
        }
    }

    private fun isWithinGeofence(location: Location, geofenceLocation: GeofenceLocation): Boolean {
        return distanceToGeofence(location, geofenceLocation) <= geofenceLocation.radius
    }

    private fun distanceToGeofence(location: Location, geofenceLocation: GeofenceLocation): Float {
        val targetLocation = Location("").apply {
            latitude = geofenceLocation.latLng.latitude
            longitude = geofenceLocation.latLng.longitude
        }
        return location.distanceTo(targetLocation)
    }

    private fun renderMap(currentLocation: Location? = null) {
        if (!::map.isInitialized) return

        map.clear()
        currentPolyline = null

        val boundsBuilder = LatLngBounds.Builder()
        var hasBounds = false

        geofenceLocations.forEach { geofenceLocation ->
            map.addMarker(
                MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(R.drawable.office_cahnge)))
                    .position(geofenceLocation.latLng)
                    .title(geofenceLocation.title)
            )

            map.addCircle(
                CircleOptions()
                    .center(geofenceLocation.latLng)
                    .radius(geofenceLocation.radius.toDouble())
                    .strokeColor(ContextCompat.getColor(requireContext(), R.color.officefence))
                    .fillColor(Color.argb(64, 72, 152, 226))
                    .strokeWidth(0f)
            )

            boundsBuilder.include(geofenceLocation.latLng)
            hasBounds = true
        }

        currentLocation?.let { location ->
            val currentLatLng = LatLng(location.latitude, location.longitude)
            map.addCircle(
                CircleOptions()
                    .center(currentLatLng)
                    .radius(25.0)
                    .strokeColor(ContextCompat.getColor(requireContext(), R.color.officefence))
                    .fillColor(Color.argb(64, 72, 152, 226))
                    .strokeWidth(0f)
            )
            map.addMarker(
                MarkerOptions()
                    .position(currentLatLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(R.drawable.current_loc_change)))
                    .anchor(0.5f, 0.5f)
                    .title("You are here")
                    .draggable(false)
            )
            boundsBuilder.include(currentLatLng)
            hasBounds = true
        }

        if (!hasBounds) return

        if (geofenceLocations.size == 1 && currentLocation == null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(geofenceLocations.first().latLng, 14f))
            return
        }

        try {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 160))
        } catch (_: IllegalStateException) {
        }
    }

    private fun hasValidGeofenceConfig(): Boolean {
        return geofenceLocations.isNotEmpty()
    }
}
