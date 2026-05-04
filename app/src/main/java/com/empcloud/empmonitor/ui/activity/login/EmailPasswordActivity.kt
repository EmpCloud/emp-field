package com.empcloud.empmonitor.ui.activity.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.data.remote.request.forgotpassword.ForgotPasswordDataModel
import com.empcloud.empmonitor.data.remote.request.login.LoginDataModel
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel
import com.empcloud.empmonitor.data.remote.response.login.LoginResponse
import com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsData
import com.empcloud.empmonitor.databinding.ActivityEmailPasswordBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity
import com.empcloud.empmonitor.ui.activity.forgotpassword.ForgotPasswordActivity
import com.empcloud.empmonitor.ui.activity.forgotpassword.ForgotPasswordViewModel
import com.empcloud.empmonitor.ui.services.location_tacking.LocationService
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmailPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailPasswordBinding
    private lateinit var password:TextInputEditText
    private lateinit var listener:TextWatcher
    private lateinit var loginbtnflase: LinearLayout
    private lateinit var loginbtntrue:LinearLayout
    private lateinit var forgotPass:TextView
    private var email:String? = null
    private val viewModel by viewModels<LoginViewModel>()

    private val viewModel1 by viewModels<ForgotPasswordViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmailPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        password = binding.password
        loginbtnflase = binding.loginDisable
        loginbtntrue = binding.login
        forgotPass = binding.forgotPassword
//        val password = password.text.toString()

        val intent = intent
        email = intent.getStringExtra(Constants.EMAIL)

        forgotPass.setOnClickListener{

            val forgotpassmodel = ForgotPasswordDataModel(email!!)
            forgotOtpRequest(forgotpassmodel)
//            val intent = Intent(this,ForgotPasswordActivity::class.java)
//            intent.putExtra(Constants.EMAIL,email)
//            startActivity(intent)
//            finish()

        }

        binding.backbtn.setOnClickListener {

            val intent = Intent(applicationContext,EmailLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        loginbtntrue.setOnClickListener {

            val pref = getSharedPreferences(Constants.PASSWORD, MODE_PRIVATE)
            pref.edit().putString(Constants.PASSWORD,password.text.toString()).apply()

//            Log.d("Password",password.text.toString())
            val loginDataModel = LoginDataModel(email!!,password.text.toString())
            loginRequestCall(loginDataModel)

        }

//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)
        textObserving()
        observeLoginData()
        observeOtp()
        observeTrackingSettings()
        observeAutoCheckIn()
    }

    fun textObserving(){
        listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                if(!password.text!!.isEmpty()){

                    loginbtnflase.visibility = View.GONE
                    loginbtntrue.visibility = View.VISIBLE
                }else{

                    loginbtnflase.visibility = View.VISIBLE
                    loginbtntrue.visibility = View.GONE
                }
            }

        }

        password.addTextChangedListener(listener)
    }

    private fun loginRequestCall(loginDataModel: LoginDataModel){

        viewModel.invokeLoginCall(loginDataModel)

    }

    private fun observeLoginData(){
//        Log.d("Apicode","InsideObserver")
        lifecycleScope.launch {
            with(viewModel){
                observerLoginData.collect{res->
//                    Log.d("Apicode","res")
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
//                            Log.d("Apicode",it.toString())
//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.statusCode == 200){
//                                Toast.makeText(applicationContext,it.body.message,Toast.LENGTH_SHORT).show()
                                saveUserData(it)

                            }
                            if (it.statusCode == 400){

                                Toast.makeText(applicationContext,it.body.message,Toast.LENGTH_SHORT).show()

                            }

                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(
//                                this@LoginActivity,"Loading...",
//                                Toast.LENGTH_LONG).show()
//                            Toast.makeText(applicationContext,"Loading",Toast.LENGTH_SHORT).show()
                        }
                        is ApiState.ERROR -> {
                            Toast.makeText(applicationContext,"Error",Toast.LENGTH_SHORT).show()

                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun saveUserData(it: LoginResponse) {

        val sharedpref = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
        sharedpref.edit().putString(Constants.AUTH_TOKEN,it.body.data.accessToken).apply()

        val newpref = getSharedPreferences(Constants.USER_ID, MODE_PRIVATE)
        newpref.edit().putString(Constants.USER_ID,it.body.data.userData.emp_id).apply()

        val pref1 = getSharedPreferences(Constants.USER_FULL_NAME, MODE_PRIVATE)
        pref1.edit().putString(Constants.NAME_FULL,it.body.data.userData.fullName).apply()
        pref1.edit().putString(Constants.ROLE,it.body.data.userData.role).apply()

        val namesave = getSharedPreferences(Constants.LOGIN_NAME_PERSON, MODE_PRIVATE)
        namesave.edit().putString(Constants.LOGIN_NAME_PERSON,it.body.data.userData.fullName).apply()

        val rolesave = getSharedPreferences(Constants.LOGIN_ROLE_PERSON, MODE_PRIVATE)
        rolesave.edit().putString(Constants.LOGIN_ROLE_PERSON,it.body.data.userData.role).apply()

        if (!it.body.data.userData.profilePic.isNullOrEmpty())     CommonMethods.saveSharedPrefernce(this,Constants.PROFILE_PIC_URL_USER,Constants.PROFILE_PIC_URL_USER,it.body.data.userData.profilePic)

        val isGlobalpref = getSharedPreferences(Constants.IS_GLOBAL_USER, MODE_PRIVATE)
        isGlobalpref.edit().putBoolean(Constants.IS_GLOBAL_USER,it.body.data.userData.isGlobalUser).apply()

        // Call tracking settings API after login
        Log.d("AutoCheckIn", "Login successful, calling tracking settings API")
        viewModel.invokeTrackingSettings(it.body.data.accessToken)

    }

    private fun navigateToCreateProfile() {
        val intent = Intent(this, CreateProfileActivity::class.java)
        intent.putExtra(Constants.LOGIN_TYPE, "1")
        startActivity(intent)
        finish()
    }

    private fun saveTrackingSettings(data: TrackingSettingsData) {
        val sp = getSharedPreferences(Constants.AUTO_CHECK_IN_BY_MOBILE, MODE_PRIVATE)
        sp.edit().putInt(Constants.AUTO_CHECK_IN_BY_MOBILE, data.autoCheckInByMobile).apply()

        val sp2 = getSharedPreferences(Constants.AUTO_CHECK_IN_BY_GEO_FENCING, MODE_PRIVATE)
        sp2.edit().putInt(Constants.AUTO_CHECK_IN_BY_GEO_FENCING, data.autoCheckInByGeoFencing).apply()

        val sp3 = getSharedPreferences(Constants.IS_GEO_FENCING_ON, MODE_PRIVATE)
        sp3.edit().putInt(Constants.IS_GEO_FENCING_ON, data.isGeoFencingOn).apply()

        val sp4 = getSharedPreferences(Constants.ORG_LATITUDE, MODE_PRIVATE)
        sp4.edit().putString(Constants.ORG_LATITUDE, data.latitude).apply()

        val sp5 = getSharedPreferences(Constants.ORG_LONGITUDE, MODE_PRIVATE)
        sp5.edit().putString(Constants.ORG_LONGITUDE, data.longitude).apply()

        val sp6 = getSharedPreferences(Constants.ORG_RADIUS, MODE_PRIVATE)
        sp6.edit().putInt(Constants.ORG_RADIUS, data.orgRadius).apply()

        val sp7 = getSharedPreferences(Constants.GEO_LOG_STATUS, MODE_PRIVATE)
        sp7.edit().putBoolean(Constants.GEO_LOG_STATUS, data.geoLogStatus).apply()

        val sp8 = getSharedPreferences(Constants.FREQUENCY, MODE_PRIVATE)
        sp8.edit().putInt(Constants.FREQUENCY, data.currentFrequency).apply()
        sp8.edit().putInt(Constants.FENCE_RADIUS, data.currentRadius).apply()
    }

    private fun observeTrackingSettings() {
        lifecycleScope.launch {
            viewModel.observeTrackingSettings.collect { res ->
                when (res) {
                    is ApiState.SUCESS -> {
                        val it = res.getResponse
                        if (it.statusCode == 200) {
                            val data = it.body.data
                            Log.d("AutoCheckIn", "Tracking settings fetched: autoCheckInByMobile=${data.autoCheckInByMobile}, autoCheckInByGeoFencing=${data.autoCheckInByGeoFencing}, isGeoFencingOn=${data.isGeoFencingOn}, employeeLocations=${data.employeeLocations?.size ?: 0}")
                            saveTrackingSettings(data)

                            if (shouldAttemptAutoCheckIn(data)) {
                                Log.d("AutoCheckIn", "Auto check-in is enabled from tracking settings, attempting auto check-in")
                                attemptAutoCheckIn(data)
                            } else {
                                Log.d("AutoCheckIn", "Auto check-in is OFF in tracking settings, proceeding to normal flow")
                                navigateToCreateProfile()
                            }
                        } else {
                            Log.d("AutoCheckIn", "Tracking settings API failed with statusCode=${it.statusCode}")
                            navigateToCreateProfile()
                        }
                    }
                    is ApiState.ERROR -> {
                        Log.d("AutoCheckIn", "Tracking settings API error")
                        navigateToCreateProfile()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun attemptAutoCheckIn(data: TrackingSettingsData) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("AutoCheckIn", "No location permission, skipping auto check-in")
            navigateToCreateProfile()
            return
        }

        val currentLocationRequest = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdateAgeMillis(0)
            .build()
        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(currentLocationRequest, cancellationTokenSource.token)
            .addOnSuccessListener { location ->
                if (location != null) {
                    handleAutoCheckInLocation(location, data, "current")
                } else {
                    Log.d("AutoCheckIn", "Current location is null, trying last known location")
                    tryAutoCheckInWithLastKnownLocation(data)
                }
            }
            .addOnFailureListener {
                Log.d("AutoCheckIn", "Failed to get current location: ${it.message}, trying last known location")
                tryAutoCheckInWithLastKnownLocation(data)
            }
    }

    private fun tryAutoCheckInWithLastKnownLocation(data: TrackingSettingsData) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    handleAutoCheckInLocation(location, data, "lastKnown")
                } else {
                    Log.d("AutoCheckIn", "Last known location is null, skipping auto check-in")
                    navigateToCreateProfile()
                }
            }
            .addOnFailureListener {
                Log.d("AutoCheckIn", "Failed to get last known location: ${it.message}")
                navigateToCreateProfile()
            }
    }

    private fun handleAutoCheckInLocation(
        location: Location,
        data: TrackingSettingsData,
        locationSource: String
    ) {
        Log.d(
            "AutoCheckIn",
            "Using $locationSource location for auto check-in: lat=${location.latitude}, lon=${location.longitude}"
        )
        val shouldValidateGeofence = data.isGeoFencingOn == 1
        val canAutoCheckIn = if (shouldValidateGeofence) {
            isUserInsideAnyGeofence(location, data)
        } else {
            true
        }

        if (canAutoCheckIn) {
            Log.d("AutoCheckIn", "Auto check-in allowed, marking attendance automatically")
            val token = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
                .getString(Constants.AUTH_TOKEN, "") ?: ""
            val markAttendanceModel = MarkAttendanceModel(
                CommonMethods.getCurrentTime(),
                location.latitude,
                location.longitude
            )
            viewModel.invokeMarkAttendance(token, markAttendanceModel)
        } else {
            Log.d("AutoCheckIn", "User is OUTSIDE all geofences, skipping auto check-in")
            navigateToCreateProfile()
        }
    }

    private fun shouldAttemptAutoCheckIn(data: TrackingSettingsData): Boolean {
        val isMobileAutoCheckInEnabled = data.autoCheckInByMobile == 1
        val isGeoFenceAutoCheckInEnabled = data.isGeoFencingOn == 1 && data.autoCheckInByGeoFencing == 1
        return isMobileAutoCheckInEnabled || isGeoFenceAutoCheckInEnabled
    }

    private fun isUserInsideAnyGeofence(userLocation: Location, data: TrackingSettingsData): Boolean {
        val locations = data.employeeLocations
        if (!locations.isNullOrEmpty()) {
            Log.d("AutoCheckIn", "Checking ${locations.size} employee locations")
            for (empLocation in locations) {
                val empLat = empLocation.latitude.toDoubleOrNull()
                val empLon = empLocation.longitude.toDoubleOrNull()
                if (empLat != null && empLon != null) {
                    val orgLocation = Location("").apply {
                        latitude = empLat
                        longitude = empLon
                    }
                    val distance = userLocation.distanceTo(orgLocation)
                    Log.d("AutoCheckIn", "Location '${empLocation.description}': distance=${distance}m, range=${empLocation.range}m, inside=${distance <= empLocation.range}")
                    if (distance <= empLocation.range) {
                        return true
                    }
                }
            }
            return false
        }

        // Fallback to top-level latitude/longitude/orgRadius
        Log.d("AutoCheckIn", "No employee locations, using fallback orgLat/orgLon/orgRadius")
        val orgLat = data.latitude.toDoubleOrNull()
        val orgLon = data.longitude.toDoubleOrNull()
        if (orgLat != null && orgLon != null) {
            val orgLocation = Location("").apply {
                latitude = orgLat
                longitude = orgLon
            }
            val distance = userLocation.distanceTo(orgLocation)
            Log.d("AutoCheckIn", "Fallback: distance=${distance}m, orgRadius=${data.orgRadius}m, inside=${distance <= data.orgRadius}")
            return distance <= data.orgRadius
        }
        return false
    }

    private fun observeAutoCheckIn() {
        lifecycleScope.launch {
            viewModel.observeMarkAttendance.collect { res ->
                when (res) {
                    is ApiState.SUCESS -> {
                        val it = res.getResponse
                        if (it.statusCode == 200 && it.body.data.code == 200) {
                            val autoCheckInTime = CommonMethods.formatApiTime(CommonMethods.getCurrentTime())
                            getSharedPreferences(Constants.AUTO_CHECK_IN_TIME, MODE_PRIVATE)
                                .edit()
                                .putString(Constants.AUTO_CHECK_IN_TIME, autoCheckInTime)
                                .apply()
                            CommonMethods.clearStringFromSharedPreferences(this@EmailPasswordActivity, Constants.IS_CHECKEDIN)
                            CommonMethods.saveSharedPrefernce(this@EmailPasswordActivity, Constants.IS_CHECKEDIN, Constants.IS_CHECKEDIN, "YES")
                            val checkInMethod = if (getSharedPreferences(Constants.IS_GEO_FENCING_ON, MODE_PRIVATE).getInt(Constants.IS_GEO_FENCING_ON, 0) == 1) Constants.CHECK_IN_METHOD_MAP else Constants.CHECK_IN_METHOD_MOBILE
                            CommonMethods.saveSharedPrefernce(this@EmailPasswordActivity, Constants.CHECK_IN_METHOD, Constants.CHECK_IN_METHOD, checkInMethod)
                            CommonMethods.scheduleAutoCheckout(this@EmailPasswordActivity)
                            Log.d("AutoCheckIn", "Auto check-in successful: ${it.body.data.message}, time=$autoCheckInTime, method=$checkInMethod")
                            Toast.makeText(applicationContext, "Auto Check-In Successful at $autoCheckInTime", Toast.LENGTH_SHORT).show()

                            // Start LocationService for location logging after auto check-in
                            val geoLogStatus = getSharedPreferences(Constants.GEO_LOG_STATUS, MODE_PRIVATE)
                                .getBoolean(Constants.GEO_LOG_STATUS, false)
                            val isGlobalUser = getSharedPreferences(Constants.IS_GLOBAL_USER, MODE_PRIVATE)
                                .getBoolean(Constants.IS_GLOBAL_USER, false)
                            if (geoLogStatus && !isGlobalUser) {
                                val serviceIntent = Intent(this@EmailPasswordActivity, LocationService::class.java)
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    startForegroundService(serviceIntent)
                                } else {
                                    startService(serviceIntent)
                                }
                                Log.d("AutoCheckIn", "LocationService started after auto check-in")
                            }
                        } else {
                            Log.d("AutoCheckIn", "Auto check-in API returned: statusCode=${it.statusCode}, code=${it.body.data.code}, message=${it.body.data.message}")
                        }
                        navigateToCreateProfile()
                    }
                    is ApiState.ERROR -> {
                        Log.d("AutoCheckIn", "Auto check-in API error")
                        navigateToCreateProfile()
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        password.removeTextChangedListener(listener)
    }
//

    private fun forgotOtpRequest(forgotPasswordDataModel: ForgotPasswordDataModel) {

        viewModel1.invokeOtpCall(forgotPasswordDataModel)

    }

    private fun observeOtp(){
        lifecycleScope.launchWhenStarted {
            with(viewModel1){
                observerOTP.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
                            if (it.statusCode == 200){

                                val intent = Intent(applicationContext,ForgotPasswordActivity::class.java)
                                intent.putExtra(Constants.EMAIL,email)
                                startActivity(intent)
                                finish()
                                Toast.makeText(applicationContext,"OTP send",Toast.LENGTH_SHORT).show()
                            }
                            if (it.statusCode == 400){
                                android.widget.Toast.makeText(applicationContext,it.body.message,
                                    android.widget.Toast.LENGTH_SHORT).show()
                            }


                        }
                        is ApiState.LOADING -> {
                            android.widget.Toast.makeText(applicationContext," LOADING",
                                android.widget.Toast.LENGTH_SHORT).show()

                        }
                        is ApiState.ERROR -> {
                            android.widget.Toast.makeText(applicationContext,"Invalid ERROR",
                                android.widget.Toast.LENGTH_SHORT).show()


                        }
                    }
                }
            }
        }
    }


//    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(applicationContext,EmailLoginActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
}
