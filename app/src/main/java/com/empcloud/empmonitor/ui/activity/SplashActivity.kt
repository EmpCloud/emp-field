package com.empcloud.empmonitor.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.databinding.ActivitySplashBinding
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.ui.activity.welcom_tutorial.WelcomeActivity
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants

class SplashActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        openWelcome()
//        CommonMethods.setStatusBarColor(window,applicationContext,binding.root,this)
        animationSplash()

    }

    private fun animationSplash(){

        val logoAnimatin = AnimationUtils.loadAnimation(applicationContext,R.anim.splash_logo)
        binding.splashLogo.startAnimation(logoAnimatin)

        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.splash_icon)
        binding.splashIcon.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {

                proceedFurther()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }


        })
    }

    private fun proceedFurther(){

        val sharedpref = getSharedPreferences(Constants.FIRST_RUN, MODE_PRIVATE)

        val firstRun = sharedpref.getBoolean(Constants.FIRST_RUN,false)

        val auth = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({

            if (firstRun){
                openLogin()
            }else{
                openWelcome()
            }
        },0)
    }
    private fun openMain() {
        val intent = Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openLogin() {
        val intent = Intent(applicationContext,LoginOptionsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openWelcome() {

        val intent = Intent(applicationContext,WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openCreateSetion() {

        val intent = Intent(applicationContext,CreateProfileActivity::class.java)
        startActivity(intent)
        finish()
    }



}