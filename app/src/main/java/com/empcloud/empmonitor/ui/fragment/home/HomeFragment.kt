package com.empcloud.empmonitor.ui.fragment.home

import android.Manifest
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.health.connect.datatypes.ExerciseRoute.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import android.text.SpannableString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.request.attendance.AttendanceRequestModel
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel
import com.empcloud.empmonitor.data.remote.request.mode_of_transport.ModeOFTransportModel
import com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel
import com.empcloud.empmonitor.data.remote.response.attendance.AttendanceResponse
import com.empcloud.empmonitor.data.remote.response.fetchattendacne.FetchAttendanceResponse
import com.empcloud.empmonitor.databinding.FragmentHomeBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.ui.fragment.attendance.AttendanceFragment
import com.empcloud.empmonitor.ui.fragment.holidays.HolidaysFragment
import com.empcloud.empmonitor.ui.fragment.leaves.LeavesFragment
import com.empcloud.empmonitor.ui.fragment.map.MapCurrentFragment
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.ui.services.location_tacking.LocationService
import com.empcloud.empmonitor.utils.ActiveTaskTracker
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ncorti.slidetoact.SlideToActView
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale
@AndroidEntryPoint
class HomeFragment constructor(private val listener: OnFragmentChangedListener? = null) : Fragment() {

    private var checkIn: String? = null
    private var mode: String? = null
    private var _mode_local: String? = null

    private var isServiceStart: Boolean? = null
    private lateinit var binding:FragmentHomeBinding
    private lateinit var slideActView:SlideToActView
    private val viewModel by viewModels<HomeViewModel> ()
    private var isReversed:Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private  var updateRunnable: Runnable? = null
    private  var timeLast:String? = null
    private val LOCATION_PERMISSION_REQUEST_CODE = 1


    private var isModeSelected:Boolean = false

    private  var timeCheckIn:String? = null
    private  var timeCheckout:String? = null

    private lateinit var locationManager: LocationManager
    private lateinit var locationProviderReceiver: BroadcastReceiver
    private var lat:Double? = null
    private var lon:Double? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var isGlobal =  false

    companion object {
        fun newInstance() = HomeFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Initialize the BroadcastReceiver
        locationProviderReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val isUserGlobal = CommonMethods.getSharedPrefernceBoolean(requireActivity(),Constants.IS_GLOBAL_USER)
        isGlobal = isUserGlobal

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        Log.d("created ","count")
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        checkLocationPermissionAndStartService()

//        val sharedPred = requireActivity().getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
//        val name = sharedPred.getString(Constants.NAME_FULL,"")
//        val role = sharedPred.getString(Constants.ROLE,"")
//
//
//        Log.d("agdasga","{$name  $role}")
//        if(!name.isNullOrEmpty() && !role.isNullOrEmpty()){
//            binding.name.text = name
//            binding.role.text = role
//        }


        if (isGlobal){

            binding.yesterdaytxthome.visibility = View.GONE
            binding.yesterdaybox.visibility = View.GONE

        }else{

            setUserData()
            setYesterdayDate()
        }

//        slideActView = binding.sliderActView
//        updateSliderForCheckIn()


        val sp = requireContext().getSharedPreferences(Constants.ISHANDLERSTOP,AppCompatActivity.MODE_PRIVATE)
        val ishandlerRunning = sp.getBoolean(Constants.ISHANDLERSTOP, false)

//        if (ishandlerRunning){
//            stopUpdatingTime()
//        }

        val token = CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN)

        val date = getCurentFormattedDate()
        binding.date.text = "$date"
        fetchAttendance(token!!)

        binding.swipeMultiple.checkinviamap.setOnClickListener {

            CommonMethods.switchFragment(requireActivity(),MapCurrentFragment(listener))
        }

        binding.swipeRefreshLayout.setOnRefreshListener {

            CommonMethods.switchFragment(requireActivity(),HomeFragment(listener))

        }
        binding.attendacebtn.setOnClickListener {

            val attendanceFragment = AttendanceFragment(listener)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,attendanceFragment).commit()
        }

        binding.holidaysbtn.setOnClickListener{

            val holidaysFragment = HolidaysFragment(listener)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,holidaysFragment).commit()
        }

        binding.mapCurrent.setOnClickListener {

            if (isLocationEnabled()){
                val mapFragment = MapCurrentFragment(listener)
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,mapFragment).commit()
            }else{
                Toast.makeText(requireContext(),"Please On Device Location ",Toast.LENGTH_SHORT).show()
            }

        }

        binding.leavesbtn.setOnClickListener{

            val leavesFragment = LeavesFragment(listener)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,leavesFragment).commit()
        }

        binding.checkoutpopup.cancelbtn.setOnClickListener {

            binding.checkoutpopup.checkoutRestrict.visibility = View.GONE
        }

        binding.checkoutpopup.okayBtn.setOnClickListener {

            binding.checkoutpopup.checkoutRestrict.visibility = View.GONE
        }

        binding.scrollview.setOnScrollChangeListener { _, _, _, _, _ ->

            binding.swipeRefreshLayout.isEnabled = !binding.scrollview.canScrollVertically(-1)

        }


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
            _mode_local = "bike"
            commonSelection()

            binding.transportselection.bike.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.car.setBackgroundResource(R.drawable.add_leave_bg)

            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
//            binding.transportselection.textcar.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))

        }

        binding.transportselection.car.setOnClickListener {

            isModeSelected = true
            mode = "car"
            _mode_local = "car"
            commonSelection()
            binding.transportselection.car.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.bike.setBackgroundResource(R.drawable.add_leave_bg)

            binding.transportselection.textcar.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

//            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))


        }

        binding.transportselection.rail.setOnClickListener {

            isModeSelected = true
            mode = "car"
            _mode_local = "rail"
            commonSelection()
            binding.transportselection.rail.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.bike.setBackgroundResource(R.drawable.add_leave_bg)

            binding.transportselection.textrail.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

//            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))


        }

        binding.transportselection.auto.setOnClickListener {

            isModeSelected = true
            mode = "car"
            _mode_local = "auto"
            commonSelection()
            binding.transportselection.auto.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.bike.setBackgroundResource(R.drawable.add_leave_bg)

            binding.transportselection.textauto.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

//            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))


        }

        binding.transportselection.bus.setOnClickListener {

            isModeSelected = true
            mode = "car"
            _mode_local = "bus"
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


        slideToAct()

        // Hide all check-in UI initially to avoid flicker before fetchAttendance API returns
        binding.sliderActView.visibility = View.GONE
        binding.autoCheckInText.visibility = View.GONE
        binding.mapCurrent.visibility = View.GONE
        binding.timeShow.visibility = View.GONE
        binding.swipeMultiple.checkinviamap.visibility = View.GONE
        binding.swipeMultiple.checkInBiometric.visibility = View.GONE
        binding.swipeMultiple.checkInWeb.visibility = View.GONE
        binding.swipeMultiple.checkInBiometricWeb.visibility = View.GONE

        // Show cached auto check-in state immediately from SharedPreferences
        val cachedAutoCheckInTime = requireContext().getSharedPreferences(Constants.AUTO_CHECK_IN_TIME, AppCompatActivity.MODE_PRIVATE)
            .getString(Constants.AUTO_CHECK_IN_TIME, null)
        val cachedIsCheckedIn = CommonMethods.getSharedPrefernce(requireActivity(), Constants.IS_CHECKEDIN)
        val cachedCheckInMethod = CommonMethods.getSharedPrefernce(requireActivity(), Constants.CHECK_IN_METHOD)

        if (!cachedAutoCheckInTime.isNullOrEmpty() && cachedIsCheckedIn == "YES") {
            if (cachedCheckInMethod == Constants.CHECK_IN_METHOD_MAP) {
                binding.autoCheckInText.text = "Auto Checked In via Map at $cachedAutoCheckInTime"
            } else {
                binding.autoCheckInText.text = "Auto Checked In at $cachedAutoCheckInTime"
            }
            binding.autoCheckInText.visibility = View.VISIBLE
        }

        observeAttendanceCall()
        observeAttendanceFetchCall()
        observeUpdationTransport()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        (activity as? MainActivity)?.setMenuButtonVisibility(true)

        (activity as? MainActivity)?.setQrVisibility(true)

//        val sharedPred = requireActivity().getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
//        val name = sharedPred.getString(Constants.NAME_FULL,"")
//        val role = sharedPred.getString(Constants.ROLE,"")
//
//
//        if(!name.isNullOrEmpty() && !role.isNullOrEmpty()){
//            binding.name.text = name
//            binding.role.text = role
//        }

        setUserData()

        val mMessageReceiver = LocationReciever(viewModel)
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            mMessageReceiver,  IntentFilter("GPSLocationUpdates")
        );
        requireContext().registerReceiver(
            locationProviderReceiver,
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION), Context.RECEIVER_NOT_EXPORTED
        )

        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    class LocationReciever constructor(val viewModel:HomeViewModel):BroadcastReceiver(){


        @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        override fun onReceive(context: Context?, intent: Intent?) {

            if(intent?.action.equals("GPSLocationUpdates")){

                val bundle = intent!!.extras
                val location = bundle!!.getParcelable("Location",Location::class.java)
                val lat = location?.latitude
                val lon = location?.longitude
//                Log.d("loactionChecking","$lat/ $lon")
            }
        }

    }

    fun getCurentFormattedDate():String{
        val currentDate = System.currentTimeMillis()
        val format = SimpleDateFormat("E, d MMM", Locale.getDefault())
        return format.format(Date(currentDate))
    }

    private fun slideToAct(){
        val swipeRefreshLayout = binding.swipeRefreshLayout
        slideActView = binding.sliderActView
        slideActView.isReversed = false
//        slideActView.isReversed = isReversed

        slideActView.onSlideCompleteListener = object :SlideToActView.OnSlideCompleteListener{

            override fun onSlideComplete(view: SlideToActView) {

//                Log.d("SlideToAct", "Slide complete event started")

                lifecycleScope.launch {

                    slideActView.resetSlider()
//                    Log.d("SlideToAct", "Slider reset")

                    if(slideActView.isReversed){
//                        Log.d("SlideToAct", "Slider is reversed")
                        updateSliderForCheckIn()
                        showPopUpConfirmation()

                    }else {
//                        Log.d("SlideToAct", "Slider is not reversed")
                        updateSliderForCheckOut()
//                        CommonMethods.clearLocationDataList(requireContext())
                        getLastLocation()

                    }
                }

                lifecycleScope.launch{
                    if (!binding.checkoutTime.text.isEmpty()){
//                        Log.d("SlideToAct", "checkoutTime is not empty")
                        updateSliderForCheckOut()
                    }
                }
//                Log.d("SlideToAct", "Slide complete event ended")
            }

        }

        slideActView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    swipeRefreshLayout.isEnabled = false
                }
                MotionEvent.ACTION_MOVE -> {
//                    val progress = calculateProgress(event)
//                    if (progress < 0.3f) {
                        // Reset the slider position
                        resetSliderPosition()
//                    }
                }
                MotionEvent.ACTION_UP -> {
//                    val progress = calculateProgress(event)
//                    if (progress < 0.3f) {
                        // Reset the slider position
                        resetSliderPosition()
//                    }
                    swipeRefreshLayout.isEnabled = true
                }
                MotionEvent.ACTION_CANCEL -> {
                    swipeRefreshLayout.isEnabled = true
                }
            }
            false
        }

    }

//    private fun calculateProgress(event: MotionEvent): Float {
//        val totalWidth = slideActView.width.toFloat() - slideActView.paddingLeft - slideActView.paddingRight
//        val progress = (event.x - slideActView.paddingLeft) / totalWidth
//        return progress.coerceIn(0f, 1f)
//    }

    private fun resetSliderPosition() {
        // Logic to reset the slider to its initial position
//        Log.d("SlideToAct", "Resetting slider position")
        slideActView.resetSlider() // This method should reset the slider to the initial position
    }

    private fun updateSliderForCheckIn() {

        slideActView.isReversed = false
        slideActView.text = "Swipe to Check IN"
        slideActView.textColor = requireActivity().resources.getColor(R.color.swipe_text)
        slideActView.outerColor = requireActivity().resources.getColor(R.color.swipe_outercolor)
        slideActView.innerColor = requireActivity().resources.getColor(R.color.swipe_innercolor)

    }

    private fun updateSliderForCheckOut() {
        slideActView.isReversed = true
        slideActView.text = "Swipe to Check OUT"
        slideActView.textColor = requireActivity().resources.getColor(R.color.white)
        slideActView.outerColor = requireActivity().resources.getColor(R.color.swipe_reverse_outer)
        slideActView.innerColor = requireActivity().resources.getColor(R.color.white)
        slideActView.iconColor = requireActivity().resources.getColor(R.color.swipe_reverse_outer)



    }

    private fun markAttendance(authToken:String,markAttendanceModel: MarkAttendanceModel){

        viewModel.invokeAttendaceMarking(authToken,markAttendanceModel)
//        Log.d("Inside viewmodel","viewmodel_swipe")
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeAttendanceCall(){
        lifecycleScope.launch {
            with(viewModel){
                observerAttendanceFlow.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse

                            Log.d("CODECHECK",res.getResponse.toString())

                                if (it.statusCode == 400 ){

                                    Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                                    if (it.body.status.equals("failed")) {
                                        if (checkIn.isNullOrEmpty()) updateSliderForCheckIn() else updateSliderForCheckOut()
                                    }
                                    if (it.body.message.equals("Please Pause or complete OnGoing Tasks to Proceed!"))  CommonMethods.switchFragment(requireActivity(),TaskHomeFragment(listener))
//                                    else updateSliderForCheckIn()
                                }
                                else if(it.body.data.code == 200){
//                                    Log.d("InsideSwipeComplete","200")
//                                    Toast.makeText(requireContext(),it.body.data.message, Toast.LENGTH_SHORT).show()
                                    val time = it.body.data.data.time

                                    if(it.body.data.message.equals("Successfully Checked Out")){

                                        val timeSplited =  parseTimeData(time)
                                        binding.checkoutTime.text =  formatApiTime(timeSplited)
//                                        binding.checkoutTime.text =  SPLit(time)
                                        if(updateRunnable != null) handler.removeCallbacks(updateRunnable!!)
                                        binding.checkoutConfirm.customeAlertcheckout.visibility = View.GONE
                                        val sp = requireContext().getSharedPreferences(Constants.ISHANDLERSTOP,AppCompatActivity.MODE_PRIVATE)
                                        sp.edit().putBoolean(Constants.ISHANDLERSTOP,true).apply()
                                        stopLocationService()
                                        CommonMethods.clearLocationDataList(requireContext())
                                        CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.IS_CHECKEDIN)
                                        CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKEDIN,Constants.IS_CHECKEDIN,"NO")
                                        CommonMethods.clearStringFromSharedPreferences(requireContext(), Constants.AUTO_CHECK_IN_TIME)
                                        CommonMethods.cancelAutoCheckout(requireContext())
//                                        setTime(SPLit(time))

                                    }else if(it.body.data.message.equals("Successfully Checked In")) {

                                        binding.checkinTime.text = formatApiTime( parseTimeData(time))
                                        binding.timeShow.visibility = View.VISIBLE
                                        setTime()
//                                        setTime(SPLit(time))
//                                        Log.d("servicelog",isServiceStart.toString())
                                        if (isServiceStart == true && !isGlobal) checkLocationPermissionAndStartService()
                                        if (!isGlobal) binding.transportselection.poptransport.visibility = View.VISIBLE
                                        else binding.transportselection.poptransport.visibility = View.GONE
                                        binding.swipeRefreshLayout.isEnabled = false
                                        ActiveTaskTracker.activeTaskId = null
                                        CommonMethods.clearLocationDataList(requireContext())
                                        CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.IS_CHECKEDIN)
                                        CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKEDIN,Constants.IS_CHECKEDIN,"YES")
                                        CommonMethods.saveSharedPrefernceBoolean(requireActivity(),Constants.NOTIFICATION_ALL_READ,Constants.NOTIFICATION_ALL_READ,false)
                                        CommonMethods.saveSharedPrefernce(requireActivity(),Constants.CHECK_IN_METHOD,Constants.CHECK_IN_METHOD,Constants.CHECK_IN_METHOD_MOBILE)


                                    }else{
//                                        Toast.makeText(requireContext(),it.body.data.message,Toast.LENGTH_SHORT).show()
                                    }


                                }

                                else if(it.body.data.code == 400){
                                    Toast.makeText(requireContext(),it.body.data.message, Toast.LENGTH_SHORT).show()
                                    updateSliderForCheckIn()
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

                        else -> {}
                    }
                }
            }
        }
    }


    private fun fetchAttendance(authToken: String){

        viewModel.invokeFetchAttendance(authToken)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeAttendanceFetchCall(){
        lifecycleScope.launch {
            with(viewModel){
                observeFetchAttendanceCall.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
//                            Log.d("Inside viewmodel","observer")
                            Log.d("status",it.statusCode.toString())
                            if(it.statusCode == 200){

//                                Toast.makeText(requireContext(),it.body.data.data.message, Toast.LENGTH_SHORT).show()
                                showSwipeBasedUponStatus(it)
                                isServiceStart = it.body.data.geoLogStatus

                                checkIn = it.body.data.data.data.check_in
                                val checkinTime = it.body.data.data.data.check_in
                                val checkOutTime  = it.body.data.data.data.check_out

//                                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.LAST_MODE_SELECTED)
//                                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.LAST_MODE_SELECTED,Constants.LAST_MODE_SELECTED,it.body.data.currentMode)

                                if (it.body.data.currentFrequency != null){

                                    CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.FREQUENCY)
                                    val sp = requireContext().getSharedPreferences(Constants.FREQUENCY,AppCompatActivity.MODE_PRIVATE)
                                    sp.edit().putInt(Constants.FREQUENCY,it.body.data.currentFrequency).apply()
                                    sp.edit().putInt(Constants.FENCE_RADIUS,it.body.data.currentRadius).apply()
//                                    Log.d("frequencyChecking",it.body.data.currentFrequency.toString())
                                }


                                if (!it.body.data.yesterdayDist.isNullOrEmpty())  binding.distancetxt.text = extractDist(it.body.data.yesterdayDist) + " KM"
                                if (!it.body.data.yesterdaytask.isNullOrEmpty())  binding.tasktxt.text = it.body.data.yesterdaytask
                                if (!it.body.data.yesterdayHrs.isNullOrEmpty())  binding.timetxt.text = it.body.data.yesterdayHrs


                                if(!checkinTime.isNullOrEmpty()){
                                    val formatInTime = parseTimeData(checkinTime)
                                    binding.checkinTime.text = formatApiTime(formatInTime)
//
                                    setTime(parseTimeData(checkinTime))
//                                    setTime()
                                    timeCheckIn = parseTimeData(checkinTime)

                                    lifecycleScope.launch {
                                        slideActView.isReversed = true
                                        slideActView.text = "Swipe to Check OUT"
                                        slideActView.textColor = requireActivity().resources.getColor(R.color.white)
                                        slideActView.outerColor = requireActivity().resources.getColor(R.color.swipe_reverse_outer)
                                        slideActView.innerColor = requireActivity().resources.getColor(R.color.white)
                                        slideActView.iconColor = requireActivity().resources.getColor(R.color.swipe_reverse_outer)
                                                                 }

                                    CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.IS_CHECKEDIN)
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKEDIN,Constants.IS_CHECKEDIN,"YES")

                                }
                                if(!checkOutTime.isNullOrEmpty()){

                                    val formatOutTime =  parseTimeData(checkOutTime)
                                    binding.checkoutTime.text = formatApiTime(formatOutTime)
                                    timeCheckout = parseTimeData(checkOutTime)
                                    stopUpdatingTime()
                                    CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.IS_CHECKED_OUT)
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKED_OUT,Constants.IS_CHECKED_OUT,"YES")

                                }
                                if(checkinTime.isNullOrEmpty() && checkOutTime.isNullOrEmpty()){

                                    CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.IS_CHECKEDIN)
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKEDIN,Constants.IS_CHECKEDIN,"NO")
                                    CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.IS_CHECKED_OUT)
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKED_OUT,Constants.IS_CHECKED_OUT,"NO")
                                    binding.timeShow.visibility = View.GONE
                                    stopLocationService()

                                }else{

                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKED_OUT,Constants.IS_CHECKED_OUT,"YES")
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKEDIN,Constants.IS_CHECKEDIN,"NO")
                                }
                                if (!checkinTime.isNullOrEmpty() && !checkOutTime.isNullOrEmpty()){

                                    val timecal = CommonMethods.calculateTimeWorked(parseTimeData(it.body.data.data.data.check_in),parseTimeData(it.body.data.data.data.check_out))
                                    binding.timeShow.text = timecal

                                }

                                if (!checkinTime.isNullOrEmpty() &&  checkOutTime.isNullOrEmpty()){

                                    CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.IS_CHECKEDIN)
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKEDIN,Constants.IS_CHECKEDIN,"YES")
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKED_OUT,Constants.IS_CHECKED_OUT,"NO")
                                    if (isServiceStart == true && !isGlobal) checkLocationPermissionAndStartService()
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

                        else -> {}
                    }
                }
            }
        }
    }

    private fun extractDist(yesterdayDist: String): String {

        val dist = yesterdayDist.split(".")
        val distcal = dist[0]
        return  distcal

    }

    private fun showSwipeBasedUponStatus(it: FetchAttendanceResponse) {

        val isMobileDeviceEnabled = it.body.data.isMobileDeviceEnabled
        val isGeoFencingOn = it.body.data.isGeoFencingOn
        val isBiometricEnable = it.body.data.isBioMetricEnabled
        val iswebEnable = it.body.data.isWebEnabled
        val hasCheckIn = !it.body.data.data.data.check_in.isNullOrEmpty()
        val hasCheckOut = !it.body.data.data.data.check_out.isNullOrEmpty()
        val shouldShowCheckInViaMap = isGeoFencingOn == 1

        // Check auto check-in flags from SharedPreferences
        val autoCheckInByMobile = requireContext().getSharedPreferences(Constants.AUTO_CHECK_IN_BY_MOBILE, AppCompatActivity.MODE_PRIVATE)
            .getInt(Constants.AUTO_CHECK_IN_BY_MOBILE, 0)
        val autoCheckInByGeoFencing = requireContext().getSharedPreferences(Constants.AUTO_CHECK_IN_BY_GEO_FENCING, AppCompatActivity.MODE_PRIVATE)
            .getInt(Constants.AUTO_CHECK_IN_BY_GEO_FENCING, 0)
        val isAutoCheckInEnabled = autoCheckInByMobile == 1 || (isGeoFencingOn == 1 && autoCheckInByGeoFencing == 1)
        val autoCheckInTime = requireContext().getSharedPreferences(Constants.AUTO_CHECK_IN_TIME, AppCompatActivity.MODE_PRIVATE)
            .getString(Constants.AUTO_CHECK_IN_TIME, null)
        val wasAutoCheckedIn = !autoCheckInTime.isNullOrEmpty() && hasCheckIn

        Log.d("AutoCheckIn", "HomeFragment showSwipeBasedUponStatus: isMobileDeviceEnabled=$isMobileDeviceEnabled, isGeoFencingOn=$isGeoFencingOn, autoCheckInByMobile=$autoCheckInByMobile, autoCheckInByGeoFencing=$autoCheckInByGeoFencing, wasAutoCheckedIn=$wasAutoCheckedIn")

        binding.sliderActView.visibility = View.GONE
        binding.autoCheckInText.visibility = View.GONE
        binding.autoCheckInText.text = "Auto Check-In Enabled"
        binding.mapCurrent.visibility = View.GONE
        binding.timeShow.visibility = View.GONE
        binding.swipeMultiple.checkinviamap.visibility = View.GONE
        binding.swipeMultiple.checkInBiometric.visibility = View.GONE
        binding.swipeMultiple.checkInWeb.visibility = View.GONE
        binding.swipeMultiple.checkInBiometricWeb.visibility = View.GONE
        binding.swipeMultiple.checkinviamap.visibility =
            if (shouldShowCheckInViaMap) View.VISIBLE else View.GONE

        // Get formatted check-in time from API for display in map check-in label
        val rawCheckInTime = it.body.data.data.data.check_in
        val formattedCheckInTime = if (!rawCheckInTime.isNullOrEmpty()) formatApiTime(parseTimeData(rawCheckInTime)) else ""

        // WHEN MOBILE AND GEOFENCING BOTH ARE ON
        if (isMobileDeviceEnabled == 1 && isGeoFencingOn == 1) {

            binding.mapCurrent.visibility = View.VISIBLE
            binding.timeShow.visibility = View.VISIBLE

            if (!hasCheckIn && !hasCheckOut && isAutoCheckInEnabled) {
                Log.d("AutoCheckIn", "HomeFragment: Hiding slider, showing 'Auto Check-In Enabled' text")
                binding.autoCheckInText.text = "Auto Check-In Enabled"
                binding.autoCheckInText.visibility = View.VISIBLE
            } else {
                if (wasAutoCheckedIn) {
                    binding.autoCheckInText.text = "Auto Checked In via Map at $autoCheckInTime"
                    binding.autoCheckInText.visibility = View.VISIBLE
                } else if (hasCheckIn) {
                    binding.autoCheckInText.text = "Checked In via Map at $formattedCheckInTime"
                    binding.autoCheckInText.visibility = View.VISIBLE
                }
                binding.sliderActView.visibility = View.VISIBLE
            }
        }

        // WHEN ONLY MOBILE DEVICE CHECK-IN IS ON
        else if (isMobileDeviceEnabled == 1 && isGeoFencingOn == 0) {

            Log.d("AutoCheckIn", "HomeFragment: Showing swipe slider for mobile check-in when geofencing is OFF")
            if (!hasCheckIn && !hasCheckOut && isAutoCheckInEnabled) {
                binding.autoCheckInText.text = "Auto Check-In Enabled"
                binding.autoCheckInText.visibility = View.VISIBLE
            } else {
                if (wasAutoCheckedIn) {
                    binding.autoCheckInText.text = "Auto Checked In at $autoCheckInTime"
                    binding.autoCheckInText.visibility = View.VISIBLE
                } else {
                    binding.autoCheckInText.visibility = View.GONE
                }
                binding.sliderActView.visibility = View.VISIBLE
            }
            binding.timeShow.visibility = View.VISIBLE

        }

        // WHEN ONLY GEOFENCING IS ON, SHOW CHECK-IN VIA MAP
        else if (isMobileDeviceEnabled == 0 && isGeoFencingOn == 1) {

            binding.mapCurrent.visibility = View.VISIBLE
            binding.timeShow.visibility = View.VISIBLE
            if (wasAutoCheckedIn) {
                binding.autoCheckInText.text = "Auto Checked In via Map at $autoCheckInTime"
                binding.autoCheckInText.visibility = View.VISIBLE
            } else if (hasCheckIn) {
                binding.autoCheckInText.text = "Checked In via Map at $formattedCheckInTime"
                binding.autoCheckInText.visibility = View.VISIBLE
            }
        }

        // SHOW BIOMETRIC ONLY WHEN BOTH MOBILE DEVICE AND GEOFENCING ARE OFF
        else if (isMobileDeviceEnabled == 0 && isGeoFencingOn == 0 && isBiometricEnable == 1 && iswebEnable == 0) {

            binding.swipeMultiple.checkInBiometric.visibility = View.VISIBLE
        }

        else if (isMobileDeviceEnabled == 0 && isGeoFencingOn == 0 && isBiometricEnable == 0 && iswebEnable == 1) {

            binding.swipeMultiple.checkInWeb.visibility = View.VISIBLE

        }

        else if (isMobileDeviceEnabled == 0 && isGeoFencingOn == 0 && isBiometricEnable == 1 && iswebEnable == 1) {
            binding.swipeMultiple.checkInBiometricWeb.visibility = View.VISIBLE
        }

        else {
            binding.swipeMultiple.checkInBiometricWeb.visibility = View.VISIBLE
        }


    }


    fun SPLit(time: String):String{
        val timeSplit = time
        val parts = timeSplit.split(" ")

        return parts[1]
    }

    fun getCurrentTime(): String {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    fun convertTimeToSeconds(time: String): Int {
        val parts = time.split(":")
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        val seconds = parts[2].toInt()
        return hours * 3600 + minutes * 60 + seconds
    }

//    fun setTime(time: String) {
//        val checkinTime = convertTimeToSeconds(time)
//        val currentTime = getCurrentTime()
//        val someT = convertTimeToSeconds(currentTime)
//        val timeDiff = someT - checkinTime
//        val minutes = (timeDiff % 3600) / 60
//        val hours = timeDiff / 3600
//        val timeFormatted = String.format("%02d:%02d", hours, minutes)
//        binding.timeShow.visibility = View.VISIBLE
//        binding.timeShow.text = timeFormatted
//    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun formatApiTime(apiTime: String): String {
        // Define the formatter for the incoming time string
        val inputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        // Parse the time string to a LocalTime object
        val time = LocalTime.parse(apiTime, inputFormatter)
        // Define the formatter for the output time string with uppercase AM/PM
        val outputFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        // Format the LocalTime object to the desired pattern
        val formattedTime = time.format(outputFormatter)
        // Convert the AM/PM part to lowercase
        return formattedTime.replace("AM", "am").replace("PM", "pm")
    }

    private fun setTime(time: String) {

        val checkinTime = convertTimeToSeconds(time)
        updateRunnable = object : Runnable {
            override fun run() {
                val currentTime = CommonMethods.getCurrentTime()
                val someT = convertTimeToSeconds(currentTime)
                val timeDiff = someT - checkinTime
                val seconds = timeDiff % 60
                val minutes = (timeDiff % 3600) / 60
                val hours = timeDiff / 3600
                val timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                binding.timeShow.visibility = View.VISIBLE
                binding.timeShow.text = timeFormatted
                timeLast = timeFormatted
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(updateRunnable!!)
    }


    private fun setTime() {

        val checkinTime = convertTimeToSeconds(CommonMethods.getCurrentTime())
        updateRunnable = object : Runnable {
            override fun run() {
                val currentTime = getCurrentTime()
                val someT = convertTimeToSeconds(currentTime)
                val timeDiff = someT - checkinTime
                val seconds = timeDiff % 60
                val minutes = (timeDiff % 3600) / 60
                val hours = timeDiff / 3600
                val timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                binding.timeShow.visibility = View.VISIBLE
                binding.timeShow.text = timeFormatted
                timeLast = timeFormatted
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(updateRunnable!!)
    }

    private fun startUpdatingTime() {
        updateRunnable?.let { handler.post(it) }
    }

    private fun stopUpdatingTime() {
        updateRunnable?.let { handler.removeCallbacks(it) }
    }


    override fun onDestroy() {
        super.onDestroy()

        (activity as MainActivity?)?.setQrVisibility(false)
        if (updateRunnable != null){
            stopUpdatingTime()
        }

    }

    private fun checkLocationPermissionAndStartService() {
        context?.let { context ->
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            } else {
                startLocationService()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startLocationService()
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
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

    private fun stopLocationService() {
        context?.let { context ->
            val serviceIntent = Intent(context, LocationService::class.java)
            context.stopService(serviceIntent)
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

//        val locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
//        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//
//        location.let {
//
//            val lat = it?.latitude
//            val lon = it?.longitude
//
//            val markAttendanceModel = MarkAttendanceModel(CommonMethods.getCurrentTime(),location!!.latitude,location.longitude)
//            Log.d("markingatt",markAttendanceModel.toString())
//            markAttendance(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),markAttendanceModel)
//        }


        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    val markAttendanceModel = MarkAttendanceModel(CommonMethods.getCurrentTime(),latitude,longitude)
                    markAttendance(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),markAttendanceModel)
                    lat = latitude
                    lon = longitude
//                    Toast.makeText(requireContext(), "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_LONG).show()
                } ?: run {
                    Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_LONG).show()
            }
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unregisterReceiver(locationProviderReceiver)

    }


    fun parseTimeData(time:String):String{

        val timeExtract = time.split(Regex("[T ]"))
        return  againparseTimeData(timeExtract[1])
    }


    fun againparseTimeData(time:String):String{

        val timeExtract = time.split(".")
        return  timeExtract[0]
    }

    fun setProfileData(){
        val sharedPred = requireActivity().getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
        val name = sharedPred.getString(Constants.NAME_FULL,"")
        val role = sharedPred.getString(Constants.ROLE,"")


//        Log.d("agdasga","{$name  $role}")
        if(!name.isNullOrEmpty() && !role.isNullOrEmpty()){
            binding.name.text = name
            binding.role.text = role
        }
    }

    fun setUserData(){

        val sharedPred = requireActivity().getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
        val name = sharedPred.getString(Constants.NAME_FULL,"")
        val role = sharedPred.getString(Constants.ROLE,"")
        if(!name.isNullOrEmpty()){
            binding.name.text = name
            binding.role.text = CommonMethods.getSharedPrefernce(requireActivity(),Constants.LOGIN_ROLE_PERSON)
        }else{

            binding.name.text = CommonMethods.getSharedPrefernce(requireActivity(),Constants.LOGIN_NAME_PERSON)
            binding.role.text = CommonMethods.getSharedPrefernce(requireActivity(),Constants.LOGIN_ROLE_PERSON)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setYesterdayDate(){

        val today = LocalDate.now()

        // Get yesterday's date by subtracting one day from today's date
        val yesterday = today.minusDays(1)

        // Extract day of the week, date, and month
        val dayOfWeek = yesterday.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val dayOfMonth = yesterday.dayOfMonth
        val month = yesterday.month.getDisplayName(TextStyle.FULL, Locale.getDefault())

        binding.yesterdayDate.text = dayOfWeek +" "+dayOfMonth+" "+month
    }

    private fun observeUpdationTransport(){

        lifecycleScope.launch {
            with(viewModel){
                observeTransportCall.collect{res ->
                    when(res){

                        is ApiState.ERROR -> {}
                        ApiState.LOADING -> {}
                        is ApiState.SUCESS -> {

                            val it = res.getResponse
                            if (it.statusCode == 200){

                                binding.transportselection.poptransport.visibility = View.GONE

                                binding.swipeRefreshLayout.isEnabled = true
                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.LAST_MODE_SELECTED)
                                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.LAST_MODE_SELECTED,Constants.LAST_MODE_SELECTED,_mode_local!!)
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

}
