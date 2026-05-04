package com.empcloud.empmonitor.ui.activity.mobilelogin.otp

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel
import com.empcloud.empmonitor.data.remote.request.verifymobile.VerifyMobileModel
import com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsData
import com.empcloud.empmonitor.databinding.ActivityMobileOtpBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity
import com.empcloud.empmonitor.ui.activity.mobilelogin.MobileLoginActivity
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.empcloud.empmonitor.ui.services.location_tacking.LocationService
import com.empcloud.empmonitor.utils.broadcast_services.SmsBroadcastReceiver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.LocationServices
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

@AndroidEntryPoint
class MobileOtpActivity : AppCompatActivity(),SmsBroadcastReceiver.SmsBroadcastListener {

    private lateinit var binding:ActivityMobileOtpBinding
    private val viewModel by viewModels<MobileVerifiactionViewModel>()
    private lateinit var et1: EditText
    private lateinit var et2: EditText
    private lateinit var et3: EditText
    private lateinit var et4: EditText
    private lateinit var et5: EditText
    private lateinit var et6: EditText

    private  var phoneNumberSaved : String? = null
    private lateinit var loginbtn:LinearLayout
    private lateinit var resendbtn:LinearLayout

    private  var storedVerificationId:String? = null
    private var mResendToken: ForceResendingToken? = null
    private lateinit var auth:FirebaseAuth

    private lateinit var editTexts: List<EditText>

    private lateinit var otpEditTexts: Array<EditText>

    private lateinit var locaalOtpReciever : BroadcastReceiver

    private val SMS_CONSENT_REQUEST = 2
    private lateinit var smsBroadcastReceiver: SmsBroadcastReceiver
    private lateinit var consentLauncher : ActivityResultLauncher<Intent>

    val SMS_PERMISSION_CODE = 101
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMobileOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        observeTrackingSettings()
        observeAutoCheckIn()

        startSmsRetrieverLocal()

        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver.listener = this

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter,RECEIVER_EXPORTED)
//        registerReceiver(smsBroadcastReceiver,intentFilter,SmsRetriever.SEND_PERMISSION,null)


        et1 = binding.et1
        et2 = binding.et2
        et3 = binding.et3
        et4 = binding.et4
        et5 = binding.et5
        et6 = binding.et6

        loginbtn = binding.login
        resendbtn = binding.resendOtp


        setupOtpEditTexts(et1,et2,et3,et4,et5,et6)


        val phoneNumber = intent.getStringExtra(Constants.PHONE_NUMBER)
        val code = intent.getStringExtra(Constants.COUNTRY_CODE)
        val mobileNo = phoneNumber!!
        phoneNumberSaved = phoneNumber

        startPhoneNumberVerification(mobileNo)

        loginbtn.setOnClickListener {


            val otp = getOtpFromEditTexts(binding.et1,binding.et2,binding.et3,binding.et4,binding.et5,binding.et6)

            Log.d("ResponseCheckingFirebase","$otp")
            if(otp.isNotEmpty()) verifyPhoneNumberWithCode(otp)
            else Toast.makeText(applicationContext,"Please enter the otp!",Toast.LENGTH_SHORT).show()


        }

        binding.resendbtn.setOnClickListener {

            startPhoneNumberVerification(mobileNo)
//            resendOTP(mobileNo,mResendToken!!)
        }

        binding.backbtn.setOnClickListener {

            CommonMethods.clearAllValues(applicationContext,Constants.AUTH_TOKEN)
            val intent = Intent(applicationContext,MobileLoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        editTexts = listOf(

            et1,et2,et3,et4,et5,et6
            // Add references to editText3 to editText6
        )

        editTexts.forEach { editText ->
            editText.onPaste { pastedText ->
                // Split pasted text into individual characters
                val characters = pastedText.toCharArray()

                // Distribute characters to corresponding EditText fields
                for (i in characters.indices) {
                    if (i < editTexts.size) {
                        editTexts[i].setText(characters[i].toString())
                    }
                }
            }
        }

//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)
        resultLauncher()
//        requestPermission()

    }

    private fun requestPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS), 101)
        } else {

        }    }



    private fun extractOtp(messageBody: String): String {

        val otpRegex = "(\\d{6})".toRegex()
        val matchResult = otpRegex.find(messageBody)
        return matchResult?.value ?: ""
    }


    private fun setOtpInEditText(otp: String) {
        if (otp.length >= 6) {
            et1.setText(otp[0].toString())
            et2.setText(otp[1].toString())
            et3.setText(otp[2].toString())
            et4.setText(otp[3].toString())
            et5.setText(otp[4].toString())
            et6.setText(otp[5].toString())
        }
    }


    private fun setupClipboardListener() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.addPrimaryClipChangedListener {
            val clip = clipboard.primaryClip
            if (clip != null && clip.itemCount > 0) {
                val text = clip.getItemAt(0).text.toString()
                if (text.length == 6 && text.all { it.isDigit() }) {
                    for (i in text.indices) {
                        otpEditTexts[i].setText(text[i].toString())
                    }
                }
            }
        }
    }

    private fun setupOtpEditTexts(vararg editTexts: EditText) {
        for (i in editTexts.indices) {
            val editText = editTexts[i]

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        if (i < editTexts.size - 1) {
                            editTexts[i + 1].requestFocus()
                        }
                    } else if (s?.length == 0) {
                        if (i > 0) {
                            editTexts[i - 1].requestFocus()
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

        }
    }

    private fun getOtpFromEditTexts(vararg editTexts: EditText): String {
        val sb = StringBuilder()
        for (editText in editTexts) {
            val text = editText.text.toString().trim()
            if (text.isEmpty()) {
                Log.e("ResponseCheckingFirebase", "One of the EditTexts is empty!")
                Log.e("ResponseCheckingFirebase", "$text")

                return "" // Return empty if any field is empty
            }
            sb.append(text)
        }
        return sb.toString()
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {

         val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

//                val otp = credential.smsCode
//                Log.d("ResponseChecking","$otp")
//                if (!otp.isNullOrEmpty()) setOtpInEditText(otp)
//                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

                Log.d("ResponseChecking","Failed to get otp")
                Toast.makeText(applicationContext, "Failed to get otp", Toast.LENGTH_SHORT).show()
                CommonMethods.clearAllValues(applicationContext,Constants.AUTH_TOKEN)
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {

                Log.d("ResponseChecking","$verificationId")
                storedVerificationId = verificationId
                mResendToken = token

            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)

                Log.d("ResponseChecking","Code Timeout")

            }

        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(0L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneNumberWithCode (code: String) {
        Log.d("otpCheckingFirebase","$code $storedVerificationId")
        try {
            if(storedVerificationId != null && code != null){
                val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
                signInWithPhoneAuthCredential(credential)
            }else{
                Toast.makeText(applicationContext,"Invalid OTP",Toast.LENGTH_SHORT).show()
            }

        } catch (e: IllegalArgumentException) {
            Log.e("otpFirebase", "IllegalArgumentException: ${e.message}")
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Authentication successful
                    val user = auth.currentUser
                    Log.d("ResponseCheckingFirebase","Authentication successful")

                    // Call tracking settings API after successful Firebase auth
                    Log.d("AutoCheckIn", "Firebase auth successful, calling tracking settings API")
                    val token = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
                        .getString(Constants.AUTH_TOKEN, "") ?: ""
                    if (token.isNotEmpty()) {
                        viewModel.invokeTrackingSettings(token)
                    } else {
                        Log.d("AutoCheckIn", "No auth token found, skipping tracking settings")
                        navigateToCreateProfile()
                    }

                } else {
                    // Authentication failed
                    Log.d("ResponseChecking","Authentication failed")
                }
            }
    }

    private fun navigateToCreateProfile() {
        val intent = Intent(applicationContext, CreateProfileActivity::class.java)
        intent.putExtra(Constants.LOGIN_TYPE, "2")
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
        location: android.location.Location,
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

    private fun isUserInsideAnyGeofence(userLocation: android.location.Location, data: TrackingSettingsData): Boolean {
        val locations = data.employeeLocations
        if (!locations.isNullOrEmpty()) {
            Log.d("AutoCheckIn", "Checking ${locations.size} employee locations")
            for (empLocation in locations) {
                val empLat = empLocation.latitude.toDoubleOrNull()
                val empLon = empLocation.longitude.toDoubleOrNull()
                if (empLat != null && empLon != null) {
                    val orgLocation = android.location.Location("").apply {
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

        Log.d("AutoCheckIn", "No employee locations, using fallback orgLat/orgLon/orgRadius")
        val orgLat = data.latitude.toDoubleOrNull()
        val orgLon = data.longitude.toDoubleOrNull()
        if (orgLat != null && orgLon != null) {
            val orgLocation = android.location.Location("").apply {
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
                            CommonMethods.clearStringFromSharedPreferences(this@MobileOtpActivity, Constants.IS_CHECKEDIN)
                            CommonMethods.saveSharedPrefernce(this@MobileOtpActivity, Constants.IS_CHECKEDIN, Constants.IS_CHECKEDIN, "YES")
                            val checkInMethod = if (getSharedPreferences(Constants.IS_GEO_FENCING_ON, MODE_PRIVATE).getInt(Constants.IS_GEO_FENCING_ON, 0) == 1) Constants.CHECK_IN_METHOD_MAP else Constants.CHECK_IN_METHOD_MOBILE
                            CommonMethods.saveSharedPrefernce(this@MobileOtpActivity, Constants.CHECK_IN_METHOD, Constants.CHECK_IN_METHOD, checkInMethod)
                            CommonMethods.scheduleAutoCheckout(this@MobileOtpActivity)
                            Log.d("AutoCheckIn", "Auto check-in successful: ${it.body.data.message}, time=$autoCheckInTime, method=$checkInMethod")
                            Toast.makeText(applicationContext, "Auto Check-In Successful at $autoCheckInTime", Toast.LENGTH_SHORT).show()

                            // Start LocationService for location logging after auto check-in
                            val geoLogStatus = getSharedPreferences(Constants.GEO_LOG_STATUS, MODE_PRIVATE)
                                .getBoolean(Constants.GEO_LOG_STATUS, false)
                            val isGlobalUser = getSharedPreferences(Constants.IS_GLOBAL_USER, MODE_PRIVATE)
                                .getBoolean(Constants.IS_GLOBAL_USER, false)
                            if (geoLogStatus && !isGlobalUser) {
                                val serviceIntent = Intent(this@MobileOtpActivity, LocationService::class.java)
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




    private fun extractOTPAndSetToEditText(s: String){

        val pattern = Pattern.compile("\\b\\d{6}\\b")
        val matcher = pattern.matcher(s)
        if(matcher.find()){
            val otp = matcher.group()
            val digits = otp.toCharArray()

            if(digits.size == 6){
                et1.text = digits[0].toString() as Editable
                et2.text = digits[1].toString() as Editable
                et3.text = digits[2].toString() as Editable
                et4.text = digits[3].toString() as Editable
                et5.text = digits[4].toString() as Editable
                et6.text = digits[5].toString() as Editable

            }
        }
    }

    private fun verifyMobile(verifyMobileModel: VerifyMobileModel) {

        viewModel.invokeVerifyCall(verifyMobileModel)

    }


    private fun EditText.onPaste(callback: (String) -> Unit) {
        setOnClickListener  { view ->
            // Show paste option in context menu
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = clipboard.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                val text = clipData.getItemAt(0).coerceToText(view.context).toString()
                callback(text)
            }
            true
        }
    }

    private fun startSmsRetriever() {
        val client = SmsRetriever.getClient(this)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener {
            Log.d("SMSRetriever", "SMS Retriever Started")
        }
        task.addOnFailureListener {
            Log.e("SMSRetriever", "Failed to start SMS Retriever", it)
        }
    }


    private fun extractOTPFromMessage(message: String?): String {
        if (message.isNullOrEmpty()) {
            Log.e("extractOTPFromMessage", "Message is null or empty")
            return ""
        }
        val otpPattern = Pattern.compile("\\d{6}") // Assuming a 6-digit OTP
        val matcher = otpPattern.matcher(message)
        return if (matcher.find()) matcher.group(0) else ""
    }


    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(smsBroadcastReceiver)
    }



    private fun startSmsRetrieverLocal() {

        val client = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)  // Pass the sender's phone number if known, or null for any sender

    }


    override fun onSmsRetrieved(consentIntent: Intent) {
        Log.e("MainActivity", "Consent come")
        consentLauncher.launch(consentIntent)
//        startActivityForResult(consentIntent, SMS_CONSENT_REQUEST)
    }

    override fun onTimeout() {

        Log.e("MainActivity", "TimeOut Otp")
    }

//    override fun onMessageRecieved(message: String?) {
//
////        if (message != null){
////            val otp = extractOtp(message)
////            setOtpInEditText(otp)
////        }
//    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        registerReceiver(smsBroadcastReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION),
            RECEIVER_EXPORTED
        )
//        registerReceiver(smsBroadcastReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION),SmsRetriever.SEND_PERMISSION,null)
    }

    private fun resultLauncher(){

        Log.d("MainActivity", "Log1")
        consentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            Log.d("MainActivity", "Log2")
            if (result.resultCode == Activity.RESULT_OK && result.data != null){
                Log.d("MainActivity", "Log3")
                val message = result.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                if (message != null && message.isNotEmpty()){
                    val otp = extractOtp(message)
                    otp?.let {
                        Log.d("MainActivity", "Extracted OTP: $it")
                        setOtpInEditText(it)
                        verifyPhoneNumberWithCode(otp)
//                        if (!phoneNumberSaved.isNullOrEmpty()){
//                            val verifyMobileModel = VerifyMobileModel(phoneNumberSaved!!)
//                            verifyMobile(verifyMobileModel)
//                        }
                    }
                } else {
                    // Consent denied, handle the situation
                    Log.e("MainActivity", "Consent denied by user")
                }
            } else {
                // Consent denied, handle the situation
                Log.e("MainActivity", "Consent denied by user")
            }
        }
    }


    private lateinit var smsOtpReciever : BroadcastReceiver
    private fun setupSmsBroadcastReceiver() {
        smsOtpReciever = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
                    val extras = intent.extras
                    val consentIntent = extras?.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                    try {
                        if (consentIntent != null) {
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST)
                        }
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == SMS_CONSENT_REQUEST) {
//            if (resultCode == Activity.RESULT_OK && data != null) {
//                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
//                val otp = extractOTPFromMessage(message!!)
//                setOtpInEditText(otp)
//            } else {
//                // Handle case where user denied SMS reading
//            }
//        }
//    }
}
