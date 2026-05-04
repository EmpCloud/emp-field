package com.empcloud.empmonitor.ui.activity.savecookie

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.empcloud.empmonitor.databinding.ActivitySaveCookieBinding
import com.empcloud.empmonitor.ui.activity.webview_activity.WebViewActivity
import com.empcloud.empmonitor.utils.Constants
import com.empcloud.empmonitor.utils.NativeLib

class SaveCookieActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaveCookieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySaveCookieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cookies =
            android.webkit.CookieManager.getInstance().getCookie(NativeLib().getAdminWebLive())

        if (cookies != null) {
            println("All COOKIES $cookies")
            val cookieArr = cookies.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val cookie = cookieArr[0].trim { it <= ' ' }
            val tokenArr = cookie.split("=".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val token = tokenArr[1].trim { it <= ' ' }

            val sharedPreferences = getSharedPreferences(Constants.COOKIES_WEB, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(Constants.TOKEN_ID, token)
            editor.apply()
            Log.i("Login Cockies : ", token)
//            encryptUserEmail(id,cookies,token);


//            Toast.makeText(getApplicationContext(), "All Cookies " + cookies, Toast.LENGTH_LONG).show();
        } else {
            val sharedPreferences = getSharedPreferences(Constants.COOKIES_WEB, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("cookies", "")
            editor.putString(Constants.ENCRYPT_EMAIL_DATA, "")
            editor.putString(Constants.TOKEN_ID, "")
            editor.apply()
            val tokenPreferences = getSharedPreferences(Constants.COOKIES_WEB, MODE_PRIVATE)
            val editor1 = tokenPreferences.edit()
            editor1.putString(Constants.TOKEN_ID, "")
            editor1.apply()
        }
        val handler = Handler()

        // Post a delayed action to finish the activity after 2 seconds

        // Post a delayed action to finish the activity after 2 seconds
        handler.postDelayed({

            val i = Intent(applicationContext, WebViewActivity::class.java)
            startActivity(i)
            finish()
        }, 2000)


    }


}