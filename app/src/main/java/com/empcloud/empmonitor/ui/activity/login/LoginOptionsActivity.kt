package com.empcloud.empmonitor.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.databinding.ActivityLoginOptionsBinding
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.ui.activity.mobilelogin.MobileLoginActivity
import com.empcloud.empmonitor.ui.activity.webview_activity.WebViewActivity
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginOptionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginOptionsBinding
    private lateinit var emailCheckBox: RadioButton
    private lateinit var mobileCheckBox: RadioButton
    private lateinit var continuebtn:LinearLayout
    private var selection  = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.adminLogin.setOnClickListener {

            val spannableString = SpannableString(binding.adminLogin.text)
            spannableString.setSpan(UnderlineSpan(),0,spannableString.length,0)
            binding.adminLogin.text = spannableString
            val intent = Intent(this,WebViewActivity::class.java)
            startActivity(intent)
        }

        val auth = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
        val token = auth.getString(Constants.AUTH_TOKEN,"")
        val pref = getSharedPreferences(Constants.CREATE_SECTION, MODE_PRIVATE)
        val isComplete = pref.getBoolean(Constants.CREATE_SECTION,false)

        if (!token.isNullOrEmpty()){
//            if(isComplete){
//                openMain()
//            }else{
                openCreateSetion()
//            }
        }

        binding.emailselection.setOnClickListener {

            binding.uncheeckBtnEmail.visibility = View.GONE
            binding.checkedbtemail.visibility = View.VISIBLE

            binding.checkedbtmobile.visibility = View.GONE
            binding.uncheckedBtnMobile.visibility = View.VISIBLE

            selection = 1
            binding.continuebtn.setBackgroundResource(R.drawable.butoon_bg_shape)
            continuebtn.isClickable = true


        }

        binding.mobileselection.setOnClickListener {

            binding.uncheeckBtnEmail.visibility = View.VISIBLE
            binding.checkedbtemail.visibility = View.GONE

            binding.checkedbtmobile.visibility = View.VISIBLE
            binding.uncheckedBtnMobile.visibility = View.GONE

            selection = 2
            binding.continuebtn.setBackgroundResource(R.drawable.butoon_bg_shape)
            continuebtn.isClickable = true


        }

//        emailCheckBox = binding.emailLogin
//        mobileCheckBox = binding.mobileLogin
        continuebtn = binding.continuebtn


//        emailCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
//
//            if(isChecked){
//                selection = 1
//                continuebtn.isClickable = true
//
//            }
//
//
//        }
//
//        mobileCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
//
//            if(isChecked){
//                selection = 2
//                continuebtn.isClickable = true
//
//
//            }
//
//
//        }


        continuebtn.setOnClickListener {

            if(selection == 1){
                val intent = Intent(applicationContext,EmailLoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            if(selection == 2){
                val intent = Intent(applicationContext,MobileLoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)

       }

    private fun openMain() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openCreateSetion() {

        val intent = Intent(applicationContext, CreateProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()

        binding.adminLogin.text = binding.adminLogin.text.toString()
    }


}