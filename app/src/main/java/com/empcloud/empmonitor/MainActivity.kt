package com.empcloud.empmonitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen_frist)
//####### Code to implement the thumb color of switch #####///

//        val customSwitch: Switch = findViewById(R.id.customSwitch)
//
//        // Define the colors for different states
//        val thumbColors = ColorStateList(
//            arrayOf(
//                intArrayOf(android.R.attr.state_checked),
//                intArrayOf(-android.R.attr.state_checked)
//            ),
//            intArrayOf(
//                ContextCompat.getColor(this, R.color.your_active_thumb_color),
//                ContextCompat.getColor(this, R.color.your_inactive_thumb_color)
//            )
//        )
//
//        // Apply the ColorStateList to the switch thumb
//        customSwitch.thumbTintList = thumbColors
    }
}