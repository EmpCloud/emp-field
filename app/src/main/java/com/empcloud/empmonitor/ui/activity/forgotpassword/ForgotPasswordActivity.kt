package com.empcloud.empmonitor.ui.activity.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.data.remote.request.forgotpassword.ForgotPasswordDataModel
import com.empcloud.empmonitor.databinding.ActivityForgotPasswordBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity
import com.empcloud.empmonitor.ui.activity.resetpassword.ResetPasswordActivity
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var et1:EditText
    private lateinit var et2:EditText
    private lateinit var et3:EditText
    private lateinit var et4:EditText
    private val editTexts = mutableListOf<EditText>()
    private lateinit var resetPasswordbtn:LinearLayout
    private var email:String? = null
    private lateinit var listener:TextWatcher
    private var opt:String? = null
    private lateinit var resend:LinearLayout
    private val Otp:Int?  = null
    private val viewModel by viewModels<ForgotPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        et1 = binding.et1
        et2 = binding.et2
        et3 = binding.et3
        et4 = binding.et4
        resetPasswordbtn = binding.reset
        resend = binding.resend

        email = intent.getStringExtra(Constants.EMAIL)
        val forgotpassmodel = ForgotPasswordDataModel(email!!)
//        forgotOtpRequest(forgotpassmodel)
        setupOtpEditTexts(et1,et2,et3,et4)

        resend.setOnClickListener {

            forgotOtpRequest(forgotpassmodel)
        }


        binding.backbtn.setOnClickListener {

            val intent = Intent(applicationContext,LoginOptionsActivity::class.java)
            intent.putExtra(Constants.EMAIL,email)
            startActivity(intent)
            finish()
        }


        resetPasswordbtn.setOnClickListener {

            val otp = getOtpFromEditTexts(et1,et2,et3,et4)
            if(!otp.isEmpty()){

//                Log.d("asdfsdf",otp)
//                Log.d("asdfsdf",email!!)

                val intent = Intent(applicationContext,ResetPasswordActivity::class.java)
                intent.putExtra(Constants.EMAIL,email)
                intent.putExtra(Constants.FORGOT_PASS_OTP,otp)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext,"Enter Otp",Toast.LENGTH_SHORT).show()
            }

        }

//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)
        observeOtp()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(applicationContext,LoginOptionsActivity::class.java)
        startActivity(intent)
        finish()


    }
    private fun forgotOtpRequest(forgotPasswordDataModel: ForgotPasswordDataModel) {

        viewModel.invokeOtpCall(forgotPasswordDataModel)

    }

    private fun observeOtp(){
        lifecycleScope.launchWhenStarted {
            with(viewModel){
                observerOTP.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
                            if (it.statusCode == 200){

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
            sb.append(editText.text.toString())
        }
        return sb.toString()
    }


}