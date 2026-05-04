package com.empcloud.empmonitor.ui.activity.address

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.empcloud.empmonitor.databinding.ActivityAddAddressBinding
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants

class AddAddressActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddAddressBinding
    private val REQUEST_CAMERA_PERMISSION = 121
    private val CAMERA_REQUEST_CODE = 131
    private lateinit var locationManager: LocationManager
    private lateinit var locationProviderReceiver: BroadcastReceiver
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.cameraProfile.setOnClickListener {
//            requestCameraPermission()
//
//        }
        binding.backbtn.setOnClickListener {
            finish()
        }
        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Initialize the BroadcastReceiver
        locationProviderReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                binding.switchbtn.isChecked = locationEnabled
            }
        }

//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)
        setUserPic()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CAMERA_PERMISSION
            )
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted
                // Initialize Camera
                openCamera()
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle the result of the camera activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Handle the captured image if needed
        }
    }


    fun base64ToBitmap(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    private fun setUserPic(){

        val shared = application.getSharedPreferences(Constants.BITMAP_USER_PIC,AppCompatActivity.MODE_PRIVATE)
        val pic = shared.getString(Constants.BITMAP_USER_PIC,"")
        val bitmap = base64ToBitmap(pic!!)
        binding.userPicdisbale.visibility = View.GONE
        binding.userPic.visibility = View.VISIBLE

        binding.userPic.setImageBitmap(bitmap)
    }

    private fun switchToggle(){

        binding.switchbtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switch is ON, check if location is enabled
                if (!isLocationEnabled()) {
                    // Location is not enabled, prompt user to enable it
                    val enableLocationIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(enableLocationIntent)
                }
            } else {
                // Switch is OFF
                showLocationOffDialog()
            }
        }

        // Check if location is enabled initially and update the Switch state
        binding.switchbtn.isChecked = isLocationEnabled()
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun showLocationOffDialog() {
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        AlertDialog.Builder(applicationContext)
            .setTitle("Location Services")
            .setMessage("Please turn on location services manually in the settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                val locationSettingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(locationSettingsIntent)
            }
            .setNegativeButton("Cancel") { _, _ ->
                // If the user cancels, revert the switch to its previous state
                binding.switchbtn.isChecked = locationEnabled
            }
            .setOnCancelListener {
                // If the dialog is canceled by touching outside, revert the switch to its previous state
                binding.switchbtn.isChecked = locationEnabled
            }
            .show()
    }

}