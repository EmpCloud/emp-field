package com.empcloud.empmonitor.ui.activity.mapaddress

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.data.remote.request.updateprofile.UpdateProfileModel
import com.empcloud.empmonitor.databinding.ActivityManuallyAddAddressBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileViewModel
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint

class ManuallyAddAddressActivity : AppCompatActivity() {

    private lateinit var binding:ActivityManuallyAddAddressBinding
    private lateinit var addressFirst:EditText
    private lateinit var addressSecond:EditText
    private lateinit var state:EditText
    private lateinit var city:EditText
    private lateinit var zip:EditText
    private lateinit var currentPhotoPath:String
    private val REQUEST_CAMERA_PERMISSION = 121
    private val viewmodel by viewModels<CreateProfileViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManuallyAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val addressIntent = intent.getStringExtra(Constants.ADDRESS)
        val cityIntent = intent.getStringExtra(Constants.CITY)
        val stateIntent = intent.getStringExtra(Constants.STATE)
        val zipIntent = intent.getStringExtra(Constants.ZIP)

        addressFirst = binding.addressfirst
        state = binding.state
        city = binding.city
        zip = binding.zip

        addressFirst.setText(addressIntent)
//        addressFirst.isEnabled = false

        city.setText(cityIntent)
        city.isEnabled = false

        state.setText(stateIntent)
        state.isEnabled = false

        zip.setText(zipIntent)
//        zip.isEnabled = false


//        binding.cameraProfile.setOnClickListener {
//
//            Toast.makeText(applicationContext,"Cmaera Clicked", Toast.LENGTH_SHORT).show()
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
//            } else {
//                dispatchTakePictureIntent()
//            }
//
//        }

        binding.submitbtn.setOnClickListener {

            updateData()

        }

        binding.backbtn.setOnClickListener {

            val intent = Intent(applicationContext,MapShowActivity::class.java)
            startActivity(intent)
            finish()
        }

//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)
        setUserPic()
        observerUpdateProfile()

    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(this, "com.empcloud.empmonitor.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent,
                        CreateProfileActivity.REQUEST_IMAGE_CAPTURE
                    )
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CreateProfileActivity.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)
            if (file.exists()) {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.fromFile(file))
                binding.userPic.visibility = View.VISIBLE
                binding.userPicdisbale.visibility = View.GONE
                binding.userPic.setImageBitmap(bitmap)
            }
        }
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

        val intent = Intent(applicationContext,MapShowActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun setUserPic(){

        val pic = CommonMethods.getBitmapFromSharedPreferences(applicationContext,Constants.BITMAP_RECIEVE)
        if(pic != null){
            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
            binding.userPic.setImageBitmap(pic)
        }

    }

    private fun updateData(){

        val pref = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
        val token = pref.getString(Constants.AUTH_TOKEN,"")


        val shp = getSharedPreferences(Constants.NAME, MODE_PRIVATE)
        val name = shp.getString(Constants.USER_FULL_NAME,"")
        val prefSecond = getSharedPreferences(Constants.CREATE_USER_PROFILE, MODE_PRIVATE)
        val nameFull = prefSecond.getString(Constants.USER_NAME,"")
        val email = prefSecond.getString(Constants.USER_CREATE_EMAIL,"email")
        val lat = prefSecond.getString(Constants.USER_LAT,"lat")
        val long = prefSecond.getString(Constants.USER_LONG,"lon")
        val zip = prefSecond.getString(Constants.USER_ZIP,"")
        val state = prefSecond.getString(Constants.USER_STATE,"")
        val city = prefSecond.getString(Constants.USER_CITY,"")
        val gender = prefSecond.getString(Constants.USER_GENDER,"")
        val age = prefSecond.getInt(Constants.USER_AGE,1)
        val country = prefSecond.getString(Constants.USER_OUNTRY,"country")
        val address2 = prefSecond.getString(Constants.USER_CREATE_ADDRESS2,"address2")
        val addresss1 = prefSecond.getString(Constants.USER_CREATE_ADDRES1,"")
        val phone = prefSecond.getString(Constants.USER_PHONE_MOBIILE,"phone")
        val profile = prefSecond.getString(Constants.PROFILE_URL,null)
//        Log.d("agechecking",age.toString())
//        Log.d("userphonenumber",phone.toString())

        val addressFirst = intent.getStringExtra(Constants.ADDRESS)
        val updateProfileModel = UpdateProfileModel(
            nameFull,age.toString(),gender,email,profile,binding.addressfirst.text.toString(),address2,lat,long,binding.city.text.toString(),binding.state.text.toString(),country,binding.zip.text.toString(),phone
        )
        viewmodel.invokeUpdataDataCall(token!!,updateProfileModel)
    }

    private fun observerUpdateProfile(){
//        Log.d("Response","Inseide observer")
        lifecycleScope.launch {
            with(viewmodel){
                observerUpdataData.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.code == 200){

//                                android.util.Log.d("ResponseUser",it.toString())
//                                saveUserData(it)
                                android.widget.Toast.makeText(applicationContext,it.body.message,
                                    android.widget.Toast.LENGTH_SHORT).show()
                                val prefShared = getSharedPreferences(Constants.CREATE_SECTION, MODE_PRIVATE)
                                prefShared.edit().putBoolean(Constants.CREATE_SECTION,true).apply()
                                val intent = Intent(applicationContext,MainActivity::class.java)
                                startActivity(intent)
                                finish()

                            }
                            if (it.code == 400){
                                android.widget.Toast.makeText(applicationContext,it.body.message,
                                    android.widget.Toast.LENGTH_SHORT).show()
                            }


                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(
//                                this@LoginActivity,"Loading...",
//                                Toast.LENGTH_LONG).show()

                        }
                        is ApiState.ERROR -> {


                        }
                    }
                }
            }
        }
    }
}