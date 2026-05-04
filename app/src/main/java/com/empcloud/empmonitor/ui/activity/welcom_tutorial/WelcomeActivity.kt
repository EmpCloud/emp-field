package com.empcloud.empmonitor.ui.activity.welcom_tutorial

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.empcloud.empmonitor.databinding.ActivityWelcomeBinding
import com.empcloud.empmonitor.ui.activity.tutorial.TutorialActivity
import com.empcloud.empmonitor.utils.CommonMethods

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
     lateinit var checkBox: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        firstRun()

        binding.termsconditions.setOnClickListener {

            loadWebView("https://empmonitor.com/terms-and-conditions/")
        }

        binding.backbtn.setOnClickListener {

            binding.webviewlayout.visibility = View.GONE
        }

//        checkBox = binding.termConditionCheckBox
//        checkBox.setOnCheckedChangeListener{ buttonView, isChecked ->
//            if (isChecked){
//
//                val intent = Intent(applicationContext,TutorialActivity::class.java)
//                startActivity(intent)
//                finish()
//            }else{
//                val intent = Intent(applicationContext,SplashActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }

        binding.uncheckedterms.setOnClickListener {

            binding.uncheckedterms.visibility = View.GONE
            binding.checkedbterms.visibility = View.VISIBLE
            val intent = Intent(applicationContext,TutorialActivity::class.java)
            startActivity(intent)
            finish()
        }

//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)
    }

//    private fun firstRun() {
//        val sharepref = getSharedPreferences(Constants.FIRST_RUN, MODE_PRIVATE)
//        val edit = sharepref.edit()
//        edit.putBoolean(Constants.FIRST_RUN,true)
//        edit.apply()
//    }

    private fun loadWebView(url:String){

        val webview: WebView = binding.webview
        binding.webviewlayout.visibility = View.VISIBLE
        binding.webview.visibility = View.VISIBLE
        webview.visibility = View.VISIBLE
        val webSettings: WebSettings = webview.settings
        webSettings.javaScriptEnabled = true // Enable JavaScript

        webview.loadUrl(url)
        webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return true
            }
        }
    }
}