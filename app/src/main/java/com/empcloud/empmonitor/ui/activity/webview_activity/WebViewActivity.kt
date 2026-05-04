package com.empcloud.empmonitor.ui.activity.webview_activity

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.empcloud.empmonitor.databinding.ActivityWebViewBinding
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.empcloud.empmonitor.utils.NativeLib

class WebViewActivity : AppCompatActivity() {


    private lateinit var binding: ActivityWebViewBinding
    private lateinit var webView: WebView
    private lateinit var backtbtn:ImageView

    private var loginUrl = NativeLib().getAdminWebLive()
    private var logeedInOut:Boolean = false
    private var isLoadingCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webView = binding.webview
        backtbtn = binding.backbtn

        loadWebView(loginUrl)

//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)

        // Ensure links open within the WebView
//        webView.webViewClient = object : WebViewClient() {
//
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                view?.loadUrl(url ?: "")
//                return true
//            }
//
//            override fun onLoadResource(view: WebView?, url: String?) {
//                super.onLoadResource(view, url)
//
//                if (view?.url.equals(loginUrl)){
//
//                    if (logeedInOut){
//
//                        val intent = Intent(applicationContext,SaveCookieActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                        logeedInOut = false
//                    }
//                }
//
//                if (view?.url.equals("https://field-tracking-dev.empmonitor.com/admin/live-tracking")){
//
//                    val sharedPreferences =
//                        getSharedPreferences(Constants.COOKIES_WEB, MODE_PRIVATE)
//                    val cookie = sharedPreferences.getString("cookies", "")
//
//                    if (cookie!!.isEmpty() ||
//                        cookie == null
//                    ) {
//                        if (isLoadingCount == 0) {
//                            isLoadingCount++
//                            logeedInOut = true
//                            val i = Intent(applicationContext, SaveCookieActivity::class.java)
//                            startActivity(i)
//                            finish()
//                        }
//                    } else {
//                        logeedInOut = true
//                    }
//
//                }
//
//            }
//
//
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//
//                CookieManager.getInstance().flush()
//
//            }
//
//
//
//        }


        binding.backbtn.setOnClickListener {

            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences(Constants.COOKIES_WEB, MODE_PRIVATE)
        val cookie = sharedPreferences.getString(Constants.ENCRYPT_EMAIL_DATA, "")
    }

    private fun loadWebView(url:String){

        val webview:WebView = binding.webview
        binding.webview.visibility = View.VISIBLE
        webview.visibility = View.VISIBLE
        val webSettings: WebSettings = webview.settings
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.domStorageEnabled = true
        webSettings.allowContentAccess = true
        webSettings.displayZoomControls = true
        webSettings.allowFileAccess = true
        webSettings.loadsImagesAutomatically = true
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.javaScriptEnabled = true // Enable JavaScript

        webview.loadUrl(url)
        webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding.webview.clearCache(true)
        binding.webview.clearHistory()
    }

}