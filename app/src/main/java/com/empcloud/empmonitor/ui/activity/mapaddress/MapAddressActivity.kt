package com.empcloud.empmonitor.ui.activity.mapaddress

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.empcloud.empmonitor.databinding.ActivityMapAddressBinding
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity.Companion.REQUEST_IMAGE_CAPTURE
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class MapAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapAddressBinding
    private lateinit var currentPhotoPath:String
    private val REQUEST_CAMERA_PERMISSION = 121
    private val CAMERA_REQUEST_CODE = 131

    private lateinit var locationManager: LocationManager
    private lateinit var locationProviderReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get and set userpic from sharedpref

        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Initialize the BroadcastReceiver
        locationProviderReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                binding.switchbtn.isChecked = locationEnabled
                binding.toast.visibility = View.GONE

            }
        }
//        binding.camera.setOnClickListener {
//
//            Toast.makeText(applicationContext,"Cmaera Clicked", Toast.LENGTH_SHORT).show()
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
//            } else {
//                dispatchTakePictureIntent()
//            }
//
//        }

        binding.addAddressSearch.setOnClickListener {
            if(isLocationEnabled()){
                moveToNext()
            }else{
                binding.toast.visibility = View.VISIBLE
            }
        }

        binding.nextbtn.setOnClickListener {

            if(isLocationEnabled()){
                moveToNext()
            }else{
                binding.toast.visibility = View.VISIBLE
            }

        }

        binding.addAddressManually.setOnClickListener {
            if(isLocationEnabled()){
                moveToNext()
            }else{
                binding.toast.visibility = View.VISIBLE
            }
        }

        binding.backbtn.setOnClickListener {
            moveToPrevious()
        }

        binding.cancebtnToast.setOnClickListener {
            binding.toast.visibility = View.GONE
        }

//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)
        setUserPic()
        switchToggle()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        switchToggle()
        applicationContext.registerReceiver(
            locationProviderReceiver,
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION), Context.RECEIVER_NOT_EXPORTED
        )

        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        binding.switchbtn.isChecked = locationEnabled
        binding.toast.visibility = View.GONE

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                dispatchTakePictureIntent()
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(applicationContext,CreateProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun moveToNext(){
        val intent = Intent(applicationContext,MapShowActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun moveToPrevious(){

        val intent = Intent(applicationContext,CreateProfileActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
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
        if(isLocationEnabled()){
            binding.toast.visibility = View.GONE
        }else{
            binding.toast.visibility = View.VISIBLE
            binding.addAddressManually.isClickable = false
            binding.addAddressSearch.isClickable = false
        }
    }

    private fun showLocationOffDialog() {
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

//        AlertDialog.Builder(this)
//            .setTitle("Location Services")
//            .setMessage("Please turn on location services manually in the settings.")
//            .setPositiveButton("Open Settings") { _, _ ->
//                val locationSettingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(locationSettingsIntent)
//            }
//            .setNegativeButton("Cancel") { _, _ ->
//                // If the user cancels, revert the switch to its previous state
//                binding.switchbtn.isChecked = locationEnabled
//            }
//            .setOnCancelListener {
//                // If the dialog is canceled by touching outside, revert the switch to its previous state
//                binding.switchbtn.isChecked = locationEnabled
//            }
//            .show()

        binding.toast.visibility = View.VISIBLE
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
            binding.userPic.setImageBitmap(imageBitmap)
        }
    }




    override fun onPause() {
        super.onPause()
        // Unregister the BroadcastReceiver to stop listening for location provider changes
        applicationContext.unregisterReceiver(locationProviderReceiver)
    }

    private fun setUserPic(){

        val pic = CommonMethods.getBitmapFromSharedPreferences(applicationContext,Constants.BITMAP_RECIEVE)
        if(pic != null){
            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
            binding.userPic.setImageBitmap(pic)
        }

    }

}