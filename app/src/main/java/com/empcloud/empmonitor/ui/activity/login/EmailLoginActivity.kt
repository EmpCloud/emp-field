package com.empcloud.empmonitor.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.data.remote.request.verify_email.VerifyEmailModel
import com.empcloud.empmonitor.databinding.ActivityEmailLoginBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmailLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailLoginBinding
    private lateinit var email:EditText
    private lateinit var loginbtnflase:LinearLayout
    private lateinit var loginbtntrue:LinearLayout
    private lateinit var listener:TextWatcher

    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = binding.email
        loginbtnflase = binding.loginDisable
        loginbtntrue = binding.login

        binding.backbtn.setOnClickListener {
            val intent = Intent(applicationContext,LoginOptionsActivity::class.java)
            startActivity(intent)
            finish()
        }
        proceedFurther()
//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)
        observeVerifyEmail()

    }

    fun proceedFurther(){

        textObserving()
        loginbtntrue.setOnClickListener {

            val email = email.text.toString().trim()

            if (isEamilValid(email)){

//                Log.d("asfgasdg",email)
                val verifyEmailModel = VerifyEmailModel(email)
                viewModel.invokeVerifyEmail(verifyEmailModel)

            }else{

                Toast.makeText(applicationContext,"Email is not valid",Toast.LENGTH_SHORT).show()
            }


        }
    }

    fun textObserving(){
        listener = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                if(!email.text.isEmpty()){

                    loginbtnflase.visibility = View.GONE
                    loginbtntrue.visibility = View.VISIBLE
                }else{

                    loginbtnflase.visibility = View.VISIBLE
                    loginbtntrue.visibility = View.GONE
                }
            }

        }

        email.addTextChangedListener(listener)
    }

    private fun isValidEmail(email: String): Boolean {
        // Regular expression to validate email
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"
        return email.matches(Regex(emailRegex))
    }

    private fun isEamilValid(email:String):Boolean{

        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    private fun observeVerifyEmail(){
//        Log.d("Apicode","InsideObserver")
        lifecycleScope.launch {
            with(viewModel){
                observerEmailData.collect{res->
//                    Log.d("Apicode","res")
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
//                            Log.d("Apicode",it.toString())
//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.statusCode == 200){

                                Toast.makeText(applicationContext,it.body.message,Toast.LENGTH_SHORT).show()

                                sendtonext()

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

    private fun sendtonext() {

        val email = email.text.toString().trim()
        val intent = Intent(applicationContext,EmailPasswordActivity::class.java)
        intent.putExtra(Constants.EMAIL,email)
        val pref = getSharedPreferences(Constants.EMAIL, MODE_PRIVATE)
        pref.edit().putString(Constants.EMAIL, email).apply()
        startActivity(intent)
        finish()    }

}