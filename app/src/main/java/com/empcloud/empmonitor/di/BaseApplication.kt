package com.empcloud.empmonitor.di

import android.app.Activity
import android.app.Application
import android.os.Build
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.utils.CommonMethods
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication:Application(){

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        CommonMethods.scheduleServiceStop(this)
        CommonMethods.restorePendingAutoCheckout(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

}
