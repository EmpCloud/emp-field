package com.empcloud.empmonitor.ui.activity.mobilelogin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.data.remote.request.verifymobile.VerifyMobileModel
import com.empcloud.empmonitor.data.remote.response.mobilelogin.MobileUserData
import com.empcloud.empmonitor.databinding.ActivityMobileLoginBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity
import com.empcloud.empmonitor.ui.activity.mobilelogin.otp.MobileOtpActivity
import com.empcloud.empmonitor.ui.activity.mobilelogin.otp.MobileVerifiactionViewModel
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MobileLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMobileLoginBinding
    private lateinit var listener:TextWatcher
    private  var countryCode:String? = null
    private lateinit var codePicker:CountryCodePicker
    private val viewModel by viewModels<MobileVerifiactionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMobileLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        codePicker = binding.myPhoneInput

        listener = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(binding.phonenoPlace.text.isNotEmpty()){

                    if(s!!.length >= 9){
                        binding.disablebtnOtp.visibility = View.GONE
                        binding.sendOtp.visibility = View.VISIBLE
                    }else{
                        binding.disablebtnOtp.visibility = View.VISIBLE
                        binding.sendOtp.visibility = View.GONE
                    }
                }
            }

        }

        binding.backbtn.setOnClickListener {
            val intent = Intent(applicationContext, LoginOptionsActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.phonenoPlace.addTextChangedListener(listener)

//        getCountryCode()
        binding.sendOtp.setOnClickListener {

            val verifyMobileModel = VerifyMobileModel(binding.phonenoPlace.text.toString())
            verifyMobile(verifyMobileModel)

        }

//        codePicker.setOnClickListener {
//
////            if(binding.myPhoneInput.isValid){
////                val selectedCountry = binding.myPhoneInput.selectedCountry
////
////                Log.d("CountryCode",selectedCountry.toString())
////                if(selectedCountry != null){
////                    countryCode = selectedCountry.dialCode.toString()
////                }
////
////            }
//            val country_code = codePicker.selectedCountryCode
//            Log.d("aaaaaaaaaaaaa",country_code)
//            intent.putExtra(Constants.COUNTRY_CODE,country_code)
//
//        }

//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)
        observerVerificationMobile()

    }

    private fun getCountryCode(){
        codePicker.setOnClickListener {

//            if(binding.myPhoneInput.isValid){
//                val selectedCountry = binding.myPhoneInput.selectedCountry
//
//                Log.d("CountryCode",selectedCountry.toString())
//                if(selectedCountry != null){
//                    countryCode = selectedCountry.dialCode.toString()
//                }
//
//            }
//            val country_code = codePicker.selectedCountryCode
//            Log.d("aaaaaaaaaaaaa",country_code)
//            intent.putExtra(Constants.COUNTRY_CODE,country_code)

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding.phonenoPlace.removeTextChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        binding.phonenoPlace.removeTextChangedListener(listener)
    }

    override fun onResume() {
        super.onResume()

        binding.phonenoPlace.addTextChangedListener(listener)
    }

    private fun verifyMobile(verifyMobileModel: VerifyMobileModel) {

        viewModel.invokeVerifyCall(verifyMobileModel)

    }

    private fun observerVerificationMobile(){
        lifecycleScope.launchWhenStarted {
            with(viewModel){
                observerMobileData.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse

                            if (it.code == 200){


                                val sp2 = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
                                sp2.edit().putString(Constants.AUTH_TOKEN,it.body.data.accessToken).apply()

                                val country_code = codePicker.selectedCountryCode
                                intent.putExtra(Constants.COUNTRY_CODE,country_code)
                                val phoneNumber = "+$country_code" + binding.phonenoPlace.text.toString()

                                proceedFurther(it.body.data.userData)

                                val intent = Intent(applicationContext,MobileOtpActivity::class.java)
                                intent.putExtra(Constants.PHONE_NUMBER,phoneNumber)
                                startActivity(intent)
                                finish()

                            }
                            if (it.code == 400){

                                android.widget.Toast.makeText(applicationContext,it.body.message,
                                    android.widget.Toast.LENGTH_SHORT).show()

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

    private fun proceedFurther(mobileLoginResponse: MobileUserData) {

        val phoneNumber = intent.getStringExtra(Constants.PHONE_NUMBER)
        val sp = getSharedPreferences(Constants.MOBILE_LOGIN, MODE_PRIVATE)

        sp.edit().putBoolean(Constants.MOBILE_LOGIN,true).apply()


        val newpref = getSharedPreferences(Constants.USER_ID, MODE_PRIVATE)
        newpref.edit().putString(Constants.USER_ID,mobileLoginResponse.emp_id).apply()

        val pref1 = getSharedPreferences(Constants.USER_FULL_NAME, MODE_PRIVATE)
        pref1.edit().putString(Constants.NAME_FULL,mobileLoginResponse.fullName).apply()
        pref1.edit().putString(Constants.ROLE,mobileLoginResponse.role).apply()

        val namesave = getSharedPreferences(Constants.LOGIN_NAME_PERSON, MODE_PRIVATE)
        namesave.edit().putString(Constants.LOGIN_NAME_PERSON,mobileLoginResponse.fullName).apply()

        val rolesave = getSharedPreferences(Constants.LOGIN_ROLE_PERSON, MODE_PRIVATE)
        rolesave.edit().putString(Constants.LOGIN_ROLE_PERSON,mobileLoginResponse.role).apply()

        val isGlobalpref = getSharedPreferences(Constants.IS_GLOBAL_USER, MODE_PRIVATE)
        isGlobalpref.edit().putBoolean(Constants.IS_GLOBAL_USER,mobileLoginResponse.isGlobalUser).apply()

    }
}