package com.empcloud.empmonitor.ui.activity.createprofile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse
import com.empcloud.empmonitor.data.remote.response.fetchprofile.UserDataFetch
import com.empcloud.empmonitor.databinding.ActivityCreateProfileBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.ui.activity.mapaddress.MapAddressActivity
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientFragment
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientFragment.Companion.REQUEST_GALLERY_IMAGE
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class CreateProfileActivity : AppCompatActivity() {

    private var bitmapRecieve: Bitmap? = null
    private lateinit var binding: ActivityCreateProfileBinding
    private val REQUEST_CAMERA_PERMISSION = 121
    private val CAMERA_REQUEST_CODE = 131
    private val viewModel by viewModels<CreateProfileViewModel>()
    private lateinit var camerLayout:LinearLayout
    private  var imagePart: MultipartBody.Part? = null
    private var savedUri:Uri?= null
    private var profileLink:String? = null

    private var gender:String? = null

    private lateinit var userdata:List<UserDataFetch>

    private lateinit var currentPhotoPath:String

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_CAMERA_PERMISSION = 100
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp = getSharedPreferences(Constants.CREATE_SECTION, MODE_PRIVATE)
        val run = sp.getBoolean(Constants.CREATE_SECTION,false)

        if(run){

            openMain()
        }
        apiCall()

        camerLayout = binding.cameraProfile

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateForm()
            }
            override fun afterTextChanged(s: Editable?) {}
        }


        binding.name.addTextChangedListener(watcher)
        binding.email.addTextChangedListener(watcher)
//        binding.age.addTextChangedListener(watcher)
//        binding.mobileNo.addTextChangedListener(watcher)


        binding.radiogrp.setOnCheckedChangeListener { _, _ ->
            validateForm()
        }


        binding.male.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){

                gender = "Male"
            }
        }

        binding.female.setOnCheckedChangeListener { buttonView, isChecked ->

            gender = "Female"
        }

        binding.other.setOnCheckedChangeListener { buttonView, isChecked ->

            gender = "Other"
        }

        binding.backbtn.setOnClickListener {

            val sp = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
            sp.edit().putString(Constants.AUTH_TOKEN,"").apply()
            val intent = Intent(applicationContext,LoginOptionsActivity::class.java)
            startActivity(intent)
            finish()
        }

        camerLayout.setOnClickListener {

//            Toast.makeText(applicationContext,"Cmaera Clicked",Toast.LENGTH_SHORT).show()
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            } else {
//                dispatchTakePictureIntent()
                showImagePickerOptions()

            }

        }

        binding.truebtn.setOnClickListener {

//            Log.d("phoneNumberchecking",gender.toString())

            if (!binding.age.text.isNullOrEmpty()){

                if (isValidAge(binding.age.text.toString().toInt())){

                    val intent = Intent(applicationContext,MapAddressActivity::class.java)
                    startActivity(intent)

                    val sharedPred = getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
                    sharedPred.edit().putString(Constants.NAME_FULL,binding.name.text.toString()).apply()

                    val sharePref = getSharedPreferences(Constants.CREATE_USER_PROFILE, MODE_PRIVATE)
                    sharePref.edit().putString(Constants.USER_NAME, binding.name.text.toString()).apply()
                    sharePref.edit().putString(Constants.USER_MAIL_ID, binding.email.text.toString()).apply()
                    sharePref.edit().putString(Constants.USER_PHONE_MOBIILE, binding.mobileNo.text.toString()).apply()

//                    Log.d("userphonenumber",binding.mobileNo.text.toString())

                    val userAge = binding.age.text.toString().toIntOrNull() ?: 1 // Convert editable to string, then parse to Int
                    sharePref.edit().putInt(Constants.USER_AGE, userAge).apply()
                    sharePref.edit().putString(Constants.USER_GENDER,gender).apply()
                    sharePref.edit().putString(Constants.PROFILE_URL,profileLink).apply()

                    finish()

                }else{
                    Toast.makeText(applicationContext,"Age should be above 18",Toast.LENGTH_SHORT).show()
                }
            }else{
                val intent = Intent(applicationContext,MapAddressActivity::class.java)
                startActivity(intent)
                val sharePref = getSharedPreferences(Constants.CREATE_USER_PROFILE, MODE_PRIVATE)
                sharePref.edit().putString(Constants.USER_NAME, binding.name.text.toString()).apply()
                sharePref.edit().putString(Constants.USER_MAIL_ID, binding.email.text.toString()).apply()
                sharePref.edit().putString(Constants.USER_PHONE_MOBIILE, binding.mobileNo.text.toString()).apply()
                val userAge = binding.age.text.toString().toIntOrNull() ?: 1 // Convert editable to string, then parse to Int
//                Log.d("agechecking",userAge.toString())
                sharePref.edit().putInt(Constants.USER_AGE, userAge).apply()
                sharePref.edit().putString(Constants.USER_GENDER,gender).apply()
                sharePref.edit().putString(Constants.PROFILE_URL,profileLink).apply()
                finish()
            }

        }

//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)
        observeLoginData()
//        observerUpdateProfile()
        observeProfileImgUpdate()
    }

    private fun apiCall() {

        val sharePref = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
        val token = sharePref.getString(Constants.AUTH_TOKEN,"")
//        Log.d("AUTH_TOKEN",token.toString())
        fetchProfile(token!!)
    }


    private fun fetchProfile(authToken :String){

        viewModel.invokeFetchProfileCall(authToken)
//        Log.d("Response",authToken)
    }

    private fun observeLoginData(){
//        Log.d("Response","Inseide observer")
        lifecycleScope.launchWhenStarted {
            with(viewModel){
                observerProfileData.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.code == 200){

//                                android.util.Log.d("ResponseUser",it.toString())
                                saveUserData(it)

                            }
                            if (it.code == 400){
                                Toast.makeText(applicationContext,it.body.message,Toast.LENGTH_SHORT).show()
                            }


                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(
//                                this@LoginActivity,"Loading...",
//                                Toast.LENGTH_LONG).show()

                        }
                        is ApiState.ERROR -> {

                            Toast.makeText(applicationContext,"Session Expired",Toast.LENGTH_SHORT).show()
                            CommonMethods.clearStringFromSharedPreferences(applicationContext,Constants.AUTH_TOKEN)
                            val intent = Intent(applicationContext,LoginOptionsActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }


    private fun saveUserData(it: FetchProfileResponse) {

        userdata = listOf(it.body.data.resultData[0])

        lifecycleScope.launch {

        val email = it.body?.data?.resultData?.getOrNull(0)?.email
        binding.email.text = email?.let { Editable.Factory.getInstance().newEditable(it) } ?: Editable.Factory.getInstance().newEditable("")
        binding.email.isEnabled = false

        val pref = getSharedPreferences(Constants.CREATE_USER_PROFILE, MODE_PRIVATE)
        pref.edit().putString(Constants.USER_CREATE_EMAIL,email).apply()

        if (!userdata[0].phoneNumber.isNullOrEmpty()) binding.mobileNo.setText(userdata[0].phoneNumber)
        if (!userdata[0].age.isNullOrEmpty()) binding.age.setText(userdata[0].age)

//        val phoneNumber = it.body?.data?.resultData?.getOrNull(0)?.phoneNumber
//        binding.mobileNo.text = phoneNumber?.let { Editable.Factory.getInstance().newEditable(it) } ?: Editable.Factory.getInstance().newEditable("")
            if(it.body.data.resultData[0].phoneNumber.isNullOrEmpty())  binding.mobileNo.setText(it.body.data.resultData[0].phoneNumber)
//        binding.mobileNo.isEnabled = false
//        pref.edit().putString(Constants.CREATE_USER_PHONE,binding.mobileNo.text.toString()).apply()

            val genderRecieve = userdata[0].gender
//            Log.d("genderChecking",genderRecieve.toString())
            if (genderRecieve.equals("Male")){

                binding.male.isChecked = true
                binding.female.isChecked = false
                binding.other.isChecked = false


            }else if (  genderRecieve.equals("Female")){

                binding.female.isChecked = true
                binding.male.isChecked = false
                binding.other.isChecked = false


            }else if ( genderRecieve.equals("Other")){

                binding.other.isChecked = true
                binding.male.isChecked = false
                binding.female.isChecked = false

            }

        binding.name.setText(it.body.data.resultData[0].fullName)
        val sp = getSharedPreferences(Constants.NAME, MODE_PRIVATE)
        sp.edit().putString(Constants.NAME,it.body.data.resultData[0].fullName).apply()

        if(it.body.data.resultData[0].profilePic != null){
            profileLink = it.body.data.resultData[0].profilePic
            Picasso.get().load(it.body.data.resultData[0].profilePic).into(binding.userPic)
        }

        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "PNG_${timeStamp}_", // Change "JPEG" to "PNG" and adjust the extension
            ".png", // Set the extension to ".png"
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
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

    private fun validateForm() {
        val isEditText1Filled = binding.name.text.toString().isNotEmpty()
        val isEditText2Filled = binding.email.text.toString().isNotEmpty()
//        val isEditText3Filled = binding.mobileNo.text.toString().isNotEmpty()
//        val isEditText4Filled = binding.age.text.toString().isNotEmpty()
        val isRadioButtonSelected = binding.radiogrp.checkedRadioButtonId != -1

        binding.truebtn.visibility = if (isEditText1Filled && isEditText2Filled  &&  isRadioButtonSelected)
        {
            View.VISIBLE

        } else {
            View.GONE
        }

        binding.disbalebtn.visibility = if (isEditText1Filled && isEditText2Filled  && isRadioButtonSelected) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(applicationContext,LoginOptionsActivity::class.java)
        CommonMethods.clearStringFromSharedPreferences(applicationContext,Constants.AUTH_TOKEN)
        startActivity(intent)
        finish()
    }


    private fun invokeProfileImg(authToken: String,files:MultipartBody.Part){

//        Log.d("inside","viewmodel")
        viewModel.invokeUpdateImage(authToken,files)
    }

    private fun observeProfileImgUpdate(){
//        Log.d("inside","img data")
        lifecycleScope.launchWhenStarted {
            with(viewModel){
                observeImageData.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.statusCode == 200){

                                binding.progressBar.visibility = View.GONE
//                                android.util.Log.d("ResponseImg",it.body.data.profileURL)
//                                saveUserData(it)
//                                Toast.makeText(applicationContext,it.body.message,Toast.LENGTH_SHORT).show()
                                profileLink = it.body.data.profileURL
                                val sp = getSharedPreferences(Constants.PROFILE_PIC_URL_USER,
                                    MODE_PRIVATE)
                                sp.edit().putString(Constants.PROFILE_PIC_URL_USER,it.body.data.profileURL).apply()

                            }
                            if (it.statusCode == 400){
                                Toast.makeText(applicationContext,it.body.message,Toast.LENGTH_SHORT).show()
                            }



                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(
//                                applicationContext,"Loading...",
//                                Toast.LENGTH_LONG).show()
                            binding.progressBar.visibility = View.VISIBLE
                            binding.userPicdisbale.visibility = View.GONE

//                            android.util.Log.d("ResponseImg","Loading")
                        }
                        is ApiState.ERROR -> {
                            android.util.Log.d("ResponseImg",res.errorBody.toString())


                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showImagePickerOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> requestCameraPermission()
                1 -> {
//                    if (!Environment.isExternalStorageManager()) {
//                        val getpermission = Intent()
//                        getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
//                        startActivity(getpermission)
//                    }else{
                        openGallery()
//                    }

                }
            }
        }
        builder.show()
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                dispatchTakePictureIntent()
            }
            else -> {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, AddClientFragment.REQUEST_GALLERY_IMAGE)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(applicationContext.packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    savedUri = FileProvider.getUriForFile(applicationContext, "com.empcloud.empmonitor.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, savedUri)
                    startActivityForResult(takePictureIntent,
                        REQUEST_IMAGE_CAPTURE
                    )
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {

                    CoroutineScope(Dispatchers.IO).launch {
                        furtherProcess()
                    }
                    handleCameraImage()

                }
                REQUEST_GALLERY_IMAGE -> data?.data?.let { handleGalleryImage(it)

//                proceedFurther()
                }
            }
        }
    }

    private fun handleGalleryImage(imageUri: Uri) {

        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, imageUri)
        binding.userPic.visibility = View.VISIBLE
        binding.userPicdisbale.visibility = View.GONE
        if (bitmap != null){
            binding.userPic.setImageBitmap(bitmap)
            CommonMethods.saveBitmapToSharedPreferences(applicationContext,bitmap,Constants.BITMAP_RECIEVE)
            CommonMethods.saveSharedPrefernce(this,Constants.PROFILE_PIC_URL_USER,Constants.PROFILE_PIC_URL_USER,profileLink!!)

//            CommonMethods.saveSharedPrefernce(this,Constants.BITMAP_RECIEVE,Constants.BITMAP_RECIEVE,profileLink!!)
        }

        // Convert the imageUri to a real file path and create a multipart request
        CoroutineScope(Dispatchers.IO).launch {
            val imgPath = CommonMethods.getFileFromUriGallery(applicationContext, imageUri)

            if (imgPath != null) {
                val file = imgPath
                val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                val filePart = MultipartBody.Part.createFormData("files", file.name, requestFile)

                val sp = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
                val token = sp.getString(Constants.AUTH_TOKEN, "")
                if (token != null) {
                    invokeProfileImg(token, filePart)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Failed to get image file path", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun handleCameraImage() {
        lifecycleScope.launch {
            try {
                // Load the bitmap asynchronously
                val deferredBitmap = async(Dispatchers.IO) {
                    MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, savedUri)
                }
                val bitmap = deferredBitmap.await()

                // Update UI on the main thread
                withContext(Dispatchers.Main) {
                    binding.userPicdisbale.visibility = View.GONE
                    binding.userPic.visibility = View.VISIBLE
                    bitmap?.let {
                        binding.userPic.setImageBitmap(handleSamplingAndRotationBitmap(it))

                        CommonMethods.saveBitmapToSharedPreferences(applicationContext, it, Constants.BITMAP_RECIEVE)

//                        CommonMethods.saveSharedPrefernce(thi,Constants.PROFILE_PIC_URL_USER,Constants.PROFILE_PIC_URL_USER,profileLink!!)
                        val sp = getSharedPreferences(Constants.PROFILE_PIC_URL_USER, MODE_PRIVATE)
                        sp.edit().putString(Constants.PROFILE_PIC_URL_USER,profileLink).apply()

                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle error if bitmap loading fails
            }
        }
    }

    private fun furtherProcess() {

//        val photoFile = createImageFile()
        val photoFile = File(currentPhotoPath)


        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//        CoroutineScope(Dispatchers.IO).launch {
            val savedUri = Uri.fromFile(photoFile)

            val file = File(savedUri.path ?: "")
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val imgpath = CommonMethods.getFileFromUri(applicationContext,savedUri)
            val filePart = MultipartBody.Part.createFormData("files", imgpath?.name, requestFile)
            val sp = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
            val token = sp.getString(Constants.AUTH_TOKEN,"")
            invokeProfileImg(token!!, filePart)


        }
//}

    private fun openMain() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isValidAge(age: Int): Boolean {
        // Define the valid age range
        val minAge = 18
        val maxAge = 100

        // Check if the age is within the valid range
        return age in minAge..maxAge
    }

    private fun handleSamplingAndRotationBitmap(bitmap: Bitmap): Bitmap? {
        var rotatedBitmap: Bitmap? = bitmap
        try {
            rotatedBitmap = rotateImageIfRequired(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rotatedBitmap
    }

    @Throws(IOException::class)
    private fun rotateImageIfRequired(bitmap: Bitmap): Bitmap? {
        val matrix = Matrix()
        val orientation = ExifInterface.ORIENTATION_NORMAL
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    override fun onDestroy() {
        super.onDestroy()

        val sp = getSharedPreferences(Constants.USER_FULL_NAME, MODE_PRIVATE)
        sp.edit().putString(Constants.NAME_FULL,binding.name.text.toString()).apply()

    }
}