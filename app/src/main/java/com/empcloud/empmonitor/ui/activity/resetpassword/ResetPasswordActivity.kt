package com.empcloud.empmonitor.ui.activity.resetpassword

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.data.remote.request.resetpassword.ResetPasswordModel
import com.empcloud.empmonitor.data.remote.response.resetpassword.ResetPasswordResponse
import com.empcloud.empmonitor.databinding.ActivityResetPasswordBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.login.EmailLoginActivity
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var newpass:TextInputEditText
    private lateinit var ncnfpass:TextInputEditText
    private val viewModel by viewModels<ResetPasswordViewModel>()
    private lateinit var saveDiasble:LinearLayout
    private lateinit var save:LinearLayout
    private lateinit var listener:TextWatcher
    private var email:String = ""
    private var otp:String= ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newpass = binding.etPasswordnew
        ncnfpass = binding.etPasswordcfm
        saveDiasble = binding.saveDisable
        save = binding.save

        val newpass = newpass.text.toString()
        val cnfpass = ncnfpass.text.toString()

        email = intent.getStringExtra(Constants.EMAIL).toString()
        otp = intent.getStringExtra(Constants.FORGOT_PASS_OTP).toString()




        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateForm()
            }
            override fun afterTextChanged(s: Editable?) {

//                if(binding.etPasswordcfm.text.toString().equals( binding.etPasswordcfm.text.toString())){
//
//                    binding.save.isClickable = true
//                }else{
//
//                    Toast.makeText(applicationContext,"Password Doesn't matched",Toast.LENGTH_SHORT).show()
//                }

            }
        }

        validateForm()
        binding.etPasswordcfm.addTextChangedListener(watcher)
        binding.etPasswordnew.addTextChangedListener(watcher)
//        textObserving()

        val passfirst = binding.etPasswordnew.text.toString()
        val passsecond = binding.etPasswordcfm.text.toString()
//
//        Log.d("asdfsdf",otp)
//        Log.d("asdfsdf",email)
//        Log.d("asdfsdf",binding.etPasswordcfm.text.toString())

        binding.save.setOnClickListener {

            val text2 = binding.etPasswordcfm.text.toString().trim()
            val text1 = binding.etPasswordnew.text.toString().trim()

//            Log.d("passwordchecking","$text2 $text1")
            if(text1 == text2){

                if (text1.isEmpty()) {
                    Toast.makeText(applicationContext, "Password fields cannot be empty", Toast.LENGTH_SHORT).show()
                } else {

                    val resetPasswordModel = ResetPasswordModel(email!!, otp!!, text1)
                    resetPassword(resetPasswordModel)
                }

            }else{

                Toast.makeText(applicationContext,"Password Doesn't matched",Toast.LENGTH_SHORT).show()
            }

//                Toast.makeText(applicationContext,"Password  matched",Toast.LENGTH_SHORT).show()
//                val resetPasswordModel = ResetPasswordModel(email!!,otp!!,binding.etPasswordcfm.text.toString())
//                resetPassword(resetPasswordModel)


        }

//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)
        observeresetPassword()
    }

    private fun resetPassword(resetPasswordModel: ResetPasswordModel) {

        viewModel.invokeResetPassCall(resetPasswordModel)

    }

    private fun observeresetPassword(){
        lifecycleScope.launch {
            with(viewModel){
                observerResetPassData.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse

                            if (it.code == 200){

//                                Log.d("Response",it.code.toString())
//                                Toast.makeText(applicationContext,"Password update successfully",Toast.LENGTH_SHORT).show()
                                Toast.makeText(applicationContext,it.body.message,Toast.LENGTH_SHORT).show()
//                                Log.d("asdfsdf",otp)
//                                Log.d("asdfsdf",email)
//                                Log.d("asdfsdf",binding.etPasswordcfm.text.toString())
                                saveResetData(it)

                            }
                            if (it.code == 400){
                                Toast.makeText(applicationContext,it.body.message,Toast.LENGTH_SHORT).show()
//                                Log.d("asdfsdf",otp)
//                                Log.d("asdfsdf",email)
//                                Log.d("asdfsdf",binding.etPasswordcfm.text.toString())
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

    private fun saveResetData(it: ResetPasswordResponse) {

        val intent = Intent(applicationContext, EmailLoginActivity::class.java)
        startActivity(intent)
        finish()

    }

    fun textObserving(){

        listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(!newpass.text!!.isEmpty() && !ncnfpass.text!!.isEmpty()){

                        saveDiasble.visibility = View.GONE
                        save.visibility = View.VISIBLE

                }else{
                    saveDiasble.visibility = View.VISIBLE
                    save.visibility = View.GONE
                }
            }

        }

        newpass.addTextChangedListener(listener)
    }


    private fun validateForm() {
        val isEditText1Filled = binding.etPasswordnew.text.toString().isNotEmpty()
        val isEditText2Filled = binding.etPasswordcfm.text.toString().isNotEmpty()

        binding.save.visibility = if (isEditText1Filled && isEditText2Filled )
        {
            View.VISIBLE

        } else {
            View.GONE
        }

        binding.saveDisable.visibility = if (isEditText1Filled && isEditText2Filled) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()

//        save.setOnClickListener {
//
//            if(binding.etPasswordcfm.text.toString().equals( binding.etPasswordcfm.text.toString())){
//
////                Toast.makeText(applicationContext,"Password  matched",Toast.LENGTH_SHORT).show()
//
//                val resetPasswordModel = ResetPasswordModel(email!!,otp!!,binding.etPasswordcfm.text.toString())
//                resetPassword(resetPasswordModel)
//
//            }else{
//
//                Toast.makeText(applicationContext,"Password Doesn't matched",Toast.LENGTH_SHORT).show()
//            }
//
//
//        }

    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(applicationContext,LoginOptionsActivity::class.java)
        startActivity(intent)
        finish()
    }
}