package com.empcloud.empmonitor.ui.activity.custom_sidebar

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.databinding.ActivityCustomSidebarBinding
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity

class CustomSidebarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomSidebarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomSidebarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cancelbtn.setOnClickListener {

            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right)
            finish()
        }
    }

}