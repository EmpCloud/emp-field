package com.empcloud.empmonitor.di

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.EdgeToEdgeUtils
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

        // Android 16 (targetSdk 36) forces edge-to-edge. Apply system-bar / IME
        // inset padding to every activity from a single place so UI no longer
        // overlaps the status bar, navigation bar or keyboard.
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                EdgeToEdgeUtils.apply(activity)
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

}
