package com.empcloud.empmonitor.ui.fragment.update_profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.data.remote.request.updateprofile.UpdateProfileModel
import com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse
import com.empcloud.empmonitor.data.remote.response.fetchprofile.UserDataFetch
import com.empcloud.empmonitor.databinding.FragmentUpdateProfileBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientFragment
import com.empcloud.empmonitor.ui.fragment.update_address.UpdateAddressFragment
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
class UpdateProfileFragment constructor(private val listener: OnFragmentChangedListener? = null) : Fragment() {

    private lateinit var binding:FragmentUpdateProfileBinding
    private val viewModel by viewModels<UpdateProfileViewModel>()
    private lateinit var userdata:List<UserDataFetch>

    private lateinit var currentPhotoPath:String
    private  var gender:String? = null


    private lateinit var camerLayout: LinearLayout
    private var savedUri: Uri?= null
    private var profileLink:String? = null

    private var latitude:String? = null
    private var lonitude:String? = null



    companion object {
        fun newInstance() = UpdateProfileFragment
        const val REQUEST_IMAGE_CAPTURE = 1
//        const val REQUEST_GALLERY_IMAGE = 1021
        private const val REQUEST_CAMERA_PERMISSION = 100
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (!Environment.isExternalStorageManager()) {
//            val getpermission = Intent()
//            getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
//            startActivity(getpermission)
//        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateProfileBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchProfile(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN))

        camerLayout = binding.cameraProfile

        camerLayout.setOnClickListener {

//            Toast.makeText(applicationContext,"Cmaera Clicked",Toast.LENGTH_SHORT).show()
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
            else {
//                dispatchTakePictureIntent()
                showImagePickerOptions()

            }

        }


        binding.backbtn.setOnClickListener {

//            CommonMethods.switchFragment(requireActivity(),HomeFragment(listener))
//            callUpdateApi()
            val intent = Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.savebtn.setOnClickListener {

            CommonMethods.saveSharedPrefernce(requireActivity(),Constants.USER_FULL_NAME,Constants.NAME_FULL,binding.name.text.toString())
            if (!binding.age.text.isNullOrEmpty() && isValidAge(binding.age.text.toString().toInt()))   callUpdateApi()
            else    Toast.makeText(requireContext(),"Age should be above 18",Toast.LENGTH_SHORT).show()
        }

        binding.editbtn.setOnClickListener {

            CommonMethods.saveSharedPrefernce(requireActivity(),Constants.UMN,Constants.UMN,binding.mobileNo.text.toString())
            val sp = requireContext().getSharedPreferences(Constants.UPDATE_PROFILE_DATA,AppCompatActivity.MODE_PRIVATE)
            sp.edit().putString(Constants.UPDATE_NAME,binding.name.text.toString()).apply()
            sp.edit().putString(Constants.UPDATE_AGE,binding.age.text.toString()).apply()
            sp.edit().putString(Constants.UPDATE_MOBILENO,binding.mobileNo.text.toString()).apply()
            sp.edit().putString(Constants.UPDATE_EMAIL,binding.email.text.toString()).apply()
            sp.edit().putString(Constants.UPDATE_GENDER,gender).apply()
            sp.edit().putString(Constants.UPDATE_PROFILE_NEW,profileLink).apply()

//            Log.d("agadfga",  gender.toString() )
            CommonMethods.switchFragment(requireActivity(),UpdateAddressFragment())

        }

        (activity as? MainActivity)?.setMenuButtonVisibility(false)


//        binding.male.setOnCheckedChangeListener { buttonView, isChecked ->
//
//            if (isChecked){
//
//                gender = "Male"
//            }
//        }
//
//        binding.female.setOnCheckedChangeListener { buttonView, isChecked ->
//
//            if (isChecked)     gender = "Female"
//        }
//
//        binding.other.setOnCheckedChangeListener { buttonView, isChecked ->
//
//           if(isChecked) gender = "Other"
//        }

        binding.malesection.setOnClickListener {

            maleSelection()
            gender = "Male"

        }

        binding.femalesction.setOnClickListener {

            femaleSelection()
            gender = "Female"

        }

        binding.othersection.setOnClickListener {

           otherSelection()
            gender = "Other"

        }


//        setUserPic()
        observeProfileData()
        observeProfileImgUpdate()
        observeUpdateProfile()
    }

    private fun fetchProfile(authToken :String){

        viewModel.invokeFetchProfileCall(authToken)
//        Log.d("Response",authToken)
    }

    private fun observeProfileData(){
        Log.d("Response","Inseide observer")
        lifecycleScope.launchWhenStarted {
            with(viewModel){
                observerProfileData.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.code == 200){

//                                android.util.Log.d("ResponseUser",it.toString())
                                showData(it)

                            }
                            if (it.code == 400){
                                android.widget.Toast.makeText(requireContext(),"API CODE 400",
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

    private fun showData(fetchProfileResponse: FetchProfileResponse){

        if (!fetchProfileResponse.body.data.resultData.isEmpty()) {

            userdata = listOf(fetchProfileResponse.body.data.resultData[0])

            lifecycleScope.launch {

                binding.name.setText(userdata[0].fullName)
                binding.age.setText(userdata[0].age)
                binding.email.setText(userdata[0].email)
                binding.email.isEnabled = false
                binding.address.setText(userdata[0].address1)
                binding.mobileNo.setText(userdata[0].phoneNumber)
//                binding.mobileNo.isEnabled = false

                if (!userdata[0].latitude.isNullOrEmpty()) latitude = userdata[0].latitude
                if (!userdata[0].longitude.isNullOrEmpty()) lonitude = userdata[0].longitude


                if (!userdata[0].profilePic.isNullOrEmpty()) {

                    profileLink = userdata[0].profilePic
                    binding.userPic.visibility = View.VISIBLE
                    binding.userPicdisbale.visibility = View.GONE
                    Picasso.get().load(userdata[0].profilePic).into(binding.userPic)

//                profileLink = userdata[0].profilePic
                    CommonMethods.saveSharedPrefernce(
                        requireActivity(),
                        Constants.PROFILE_PIC_URL_USER,
                        Constants.PROFILE_PIC_URL_USER,
                        userdata[0].profilePic!!
                    )

                }else{
                    binding.userPicdisbale.visibility = View.VISIBLE
                    binding.userPic.visibility = View.GONE
                }

                val genderRecieve = userdata[0].gender
                gender = userdata[0].gender
//                Log.d("gajg", genderRecieve.toString())

                if (genderRecieve.equals("Male")) {

//                binding.male.isChecked = true
                    maleSelection()

                } else if (genderRecieve.equals("Female")) {

//                binding.female.isChecked = true
                    femaleSelection()

                } else if (genderRecieve.equals("Other")) {

//                binding.other.isChecked = true
                    otherSelection()
                }

//            CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.USER_FULL_NAME)


//            if (!userdata[0].profilePic.isNullOrEmpty()) {
//
//                binding.userPicdisbale.visibility = View.GONE
//                binding.userPic.visibility = View.VISIBLE
//                Picasso.get().load(userdata[0].profilePic).into(binding.userPic)
//            }

            }
        }


    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun showImagePickerOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(requireContext())
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
        Log.d("cameraopening","request")
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                Log.d("cameraopening","request1")

                dispatchTakePictureIntent()
            }
            else -> {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission is granted
                dispatchTakePictureIntent()
            } else {
                // Permission is denied
                Log.d("cameraopening", "Camera permission denied")
            }
        }
    }

//    private fun checkPermissions() {
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(requireActivity(  ), arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CAMERA_PERMISSION)
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            // Permissions granted
//
//            dispatchTakePictureIntent()
//        } else {
//            // Permissions denied
//        }
//    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, AddClientFragment.REQUEST_GALLERY_IMAGE)
    }

    private fun dispatchTakePictureIntent() {
        Log.d("cameraopening","request2")

        if (isCameraAppAvailable()) {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->

                Log.d("cameraopening", "request3")
                takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                    val photoFile: File? = try {
                        Log.d("cameraopening", "request4")
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    photoFile?.also {
                        savedUri = FileProvider.getUriForFile(
                            requireContext(),
                            "com.empcloud.empmonitor.fileprovider",
                            it
                        )

                        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, savedUri)
                        startActivityForResult(
                            takePictureIntent,
                            REQUEST_IMAGE_CAPTURE
                        )


                    }
                }
            }
        }else{

            Toast.makeText(requireContext(),"Camera not available",Toast.LENGTH_SHORT).show()
        }
    }

    private fun isCameraAppAvailable(): Boolean {
        val packageManager = requireContext().packageManager
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val resolveInfo = packageManager.queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo.isNotEmpty()
    }

//    private fun hasCamera(): Boolean {
//        return requireContext().packageManager.hasSystemFeature(
//            PackageManager.FEATURE_CAMERA_ANY
//        )
//    }

//    private fun dispatchTakePictureIntent() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_FRONT)
//            takePictureIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
//            takePictureIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
//
//            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
//                val photoFile: File? = try {
//                    createImageFile()
//                } catch (ex: IOException) {
//                    null
//                }
//                photoFile?.also {
//                    savedUri = FileProvider.getUriForFile(requireContext(), "com.empcloud.empmonitor.fileprovider", it)
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, savedUri)
//                    startActivityForResult(takePictureIntent, CreateProfileActivity.REQUEST_IMAGE_CAPTURE)
//                }
//            }
//        }
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {

//                        handleCameraImage()
                        furtherProcess()

                }
                AddClientFragment.REQUEST_GALLERY_IMAGE -> data?.data?.let { handleGalleryImage(it)

//                proceedFurther()
                }
            }
        }
    }

    private fun handleGalleryImage(imageUri: Uri) {

        if (!profileLink.isNullOrEmpty()){

            binding.userPicdisbale.visibility = View.GONE
            binding.userPic.visibility = View.VISIBLE
            Picasso.get().load(profileLink).into(binding.userPic)
            CommonMethods.saveSharedPrefernce(requireActivity(), Constants.BITMAP_RECIEVE_UPDATE,Constants.BITMAP_RECIEVE_UPDATE,profileLink!!)
            CommonMethods.saveSharedPrefernce(requireActivity(), Constants.PROFILE_PIC_URL_USER,Constants.PROFILE_PIC_URL_USER,profileLink!!)
        }

//        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
//        binding.userPic.visibility = View.VISIBLE
//        binding.userPicdisbale.visibility = View.GONE
//        if (bitmap != null){
//            binding.userPic.setImageBitmap(bitmap)
//            CommonMethods.saveBitmapToSharedPreferences(requireContext(), bitmap, Constants.BITMAP_RECIEVE)
//            CommonMethods.saveBitmapToSharedPreferences(requireContext(),bitmap,Constants.BITMAP_RECIEVE_UPDATE)
//
//            CommonMethods.saveSharedPrefernce(requireActivity(), Constants.PROFILE_PIC_URL_USER,Constants.PROFILE_PIC_URL_USER,profileLink!!)
//        }

        // Convert the imageUri to a real file path and create a multipart request
        CoroutineScope(Dispatchers.IO).launch {
            val imgPath = CommonMethods.getFileFromUriGallery(requireContext(), imageUri)

            if (imgPath != null) {
                val file = imgPath
                val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                val filePart = MultipartBody.Part.createFormData("files", file.name, requestFile)

                val sp = requireContext().getSharedPreferences(Constants.AUTH_TOKEN, AppCompatActivity.MODE_PRIVATE)
                val token = sp.getString(Constants.AUTH_TOKEN, "")
                if (token != null) {

                    Log.d("imagefilepart",filePart.toString())
                    invokeProfileImg(token, filePart)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to get image file path", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


//    private fun handleCameraImage() {
//
//        lifecycleScope.launch {
//            try {
//                // Load the bitmap asynchronously
//                val deferredBitmap = async(Dispatchers.IO) {
//                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, savedUri)
//                }
//                val bitmap = deferredBitmap.await()
//
//                // Update UI on the main thread
//                lifecycleScope.launch {
//                    binding.userPicdisbale.visibility = View.GONE
//                    binding.userPic.visibility = View.VISIBLE
//                    bitmap?.let {
//                        binding.userPic.setImageBitmap(it)
//                        CommonMethods.saveBitmapToSharedPreferences(requireContext(), it, Constants.BITMAP_RECIEVE)
//                        CommonMethods.saveBitmapToSharedPreferences(requireContext(), it, Constants.BITMAP_RECIEVE_UPDATE)
//                    }
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//                // Handle error if bitmap loading fails
//            }
//        }
//    }


    private fun furtherProcess() {

//        CoroutineScope(Dispatchers.Default).launch {
//        val photoFile = createImageFile()
        val photoFile = File(currentPhotoPath)


        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//        CoroutineScope(Dispatchers.IO).launch {\
            val savedUri = Uri.fromFile(photoFile)

            val file = File(savedUri.path ?: "")
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val imgpath = CommonMethods.getFileFromUri(requireContext(),savedUri)
            val filePart = MultipartBody.Part.createFormData("files", imgpath?.name, requestFile)
            val sp = requireContext().getSharedPreferences(Constants.AUTH_TOKEN, AppCompatActivity.MODE_PRIVATE)
            val token = sp.getString(Constants.AUTH_TOKEN,"")


            invokeProfileImg(token!!, filePart)


//        }

    }
//}


    private fun invokeProfileImg(authToken: String,files:MultipartBody.Part){

        Log.d("inside","viewmodel")
        viewModel.invokeUpdateImage(authToken,files)
    }

    private fun observeProfileImgUpdate(){
        Log.d("inside","img data")
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
                                profileLink = it.body.data.profileURL
//                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                                binding.userPic.visibility = View.VISIBLE
                                binding.userPicdisbale.visibility = View.GONE
                                Picasso.get().load(profileLink).into(binding.userPic)

                                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.PROFILE_PIC_URL_USER)
                                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.BITMAP_RECIEVE_UPDATE)

                                CommonMethods.saveSharedPrefernce(requireActivity(), Constants.BITMAP_RECIEVE_UPDATE,Constants.BITMAP_RECIEVE_UPDATE,profileLink!!)
                                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PROFILE_PIC_URL_USER,Constants.PROFILE_PIC_URL_USER,profileLink!!)

                            }
                            if (it.statusCode == 400){

                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                            }



                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(
//                                requireContext(),"Loading...",
//                                Toast.LENGTH_LONG).show()
                            android.util.Log.d("ResponseImg","Loading")

                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is ApiState.ERROR -> {
                            android.util.Log.d("ResponseImg",res.errorBody.toString())


                        }
                    }
                }
            }
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        Log.d("cameraopening","request5")

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "PNG_${timeStamp}_", // Change "JPEG" to "PNG" and adjust the extension
            ".png", // Set the extension to ".png"
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
//            Log.d("currentphoth","$currentPhotoPath")
        }

    }

    private fun setUserPic(){

//        val pic = CommonMethods.getBitmapFromSharedPreferences(requireContext(),Constants.BITMAP_RECIEVE)
        val pic = CommonMethods.getSharedPrefernce(requireActivity(),Constants.PROFILE_PIC_URL_USER)
        if(!pic.isNullOrEmpty()){
            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
//            binding.userPic.setImageBitmap(pic)
            Picasso.get().load(pic).into(binding.userPic)
        }

    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        callUpdateApi()
//    }

    override fun onResume() {
        super.onResume()

        fetchProfile(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN))

    }

    private fun callUpdateApi(){

        var lat = CommonMethods.getSharedPrefernce(requireActivity(),Constants.UPDATE_PROFILE_LAT)
        var lon = CommonMethods.getSharedPrefernce(requireActivity(),Constants.UPDATE_PROFILE_LON)

        if (lat.isEmpty()) lat = CommonMethods.getSharedPrefernce(requireActivity(),Constants.USER_LAT)
        if (lon.isEmpty()) lon = CommonMethods.getSharedPrefernce(requireActivity(),Constants.USER_LONG)

        val updateProfileModel = UpdateProfileModel(binding.name.text.toString(), binding.age.text.toString(), gender, binding.email.text.toString(), profileLink,
            userdata[0].address1,
            userdata[0].address2,
            latitude,
            lonitude,
            userdata[0].city,
            userdata[0].state,
            userdata[0].country,
            userdata[0].zipCode,
            binding.mobileNo.text.toString()
        )
//        viewModel.invokeUpdataDataCall()

//        CoroutineScope(Dispatchers.IO).launch {
            viewModel.invokeUpdataDataCall(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),updateProfileModel)

//        }
    }


    private fun observeUpdateProfile(){

        lifecycleScope.launch {
            with(viewModel){
                observerUpdataData.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse

//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.code == 200){
//                                Log.d("asgas",it.body.message)
                                android.widget.Toast.makeText(requireContext(),it.body.message, android.widget.Toast.LENGTH_SHORT).show()


                                android.util.Log.d("attendace","Inseide observer")
                            }
                            if (it.code == 400){
                                android.widget.Toast.makeText(requireContext(),it.body.message, android.widget.Toast.LENGTH_SHORT).show()
                                android.util.Log.d("attendace","Inseide observer")
                            }


                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(
//                                this@LoginActivity,"Loading...",
//                                Toast.LENGTH_LONG).show()
                            android.util.Log.d("attendace","Loading")
                        }
                        is ApiState.ERROR -> {
                            android.util.Log.d("attendace","api_error")

                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun isValidAge(age: Int): Boolean {
        // Define the valid age range
        val minAge = 18
        val maxAge = 100

        // Check if the age is within the valid range
        return age in minAge..maxAge
    }

    private fun handleCameraImage() {
        lifecycleScope.launch {

            if (!profileLink.isNullOrEmpty()){

                binding.userPicdisbale.visibility = View.GONE
                binding.userPic.visibility = View.VISIBLE
                Picasso.get().load(profileLink).into(binding.userPic)
                CommonMethods.saveSharedPrefernce(requireActivity(), Constants.BITMAP_RECIEVE_UPDATE,Constants.BITMAP_RECIEVE_UPDATE,profileLink!!)
                CommonMethods.saveSharedPrefernce(requireActivity(), Constants.PROFILE_PIC_URL_USER,Constants.PROFILE_PIC_URL_USER,profileLink!!)
            }

//            try {
//                // Load the bitmap asynchronously
//                val bitmap = withContext(Dispatchers.IO) {
//                    loadBitmapFromUri(savedUri)
//                }
//
//                // Update UI on the main thread
//                bitmap?.let {
//                    binding.userPicdisbale.visibility = View.GONE
//                    binding.userPic.visibility = View.VISIBLE
////                    binding.userPic.setImageBitmap(CommonMethods.handleSamplingAndRotationBitmap(it))
//                    CommonMethods.saveBitmapToSharedPreferences(requireContext(), it, Constants.BITMAP_RECIEVE)
////                    CommonMethods.saveBitmapToSharedPreferences(requireContext(), it, Constants.BITMAP_RECIEVE_UPDATE)
//
//                    CommonMethods.saveSharedPrefernce(requireActivity(), Constants.BITMAP_RECIEVE_UPDATE,Constants.BITMAP_RECIEVE_UPDATE,profileLink!!)
//                    CommonMethods.saveSharedPrefernce(requireActivity(), Constants.PROFILE_PIC_URL_USER,Constants.PROFILE_PIC_URL_USER,profileLink!!)
//
//                } ?: run {
//                    Log.d("handleCameraImage", "Bitmap is null")
//                    Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//                Toast.makeText(requireContext(), "Failed to load image: IO Exception", Toast.LENGTH_SHORT).show()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(requireContext(), "Failed to load image: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
        }
    }

    private suspend fun loadBitmapFromUri(uri: Uri?): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                uri?.let {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val options = BitmapFactory.Options().apply {
                        // Set inJustDecodeBounds to true to obtain bitmap dimensions without loading it into memory
                        inJustDecodeBounds = true
                    }
                    // Decode bitmap dimensions
                    BitmapFactory.decodeStream(inputStream, null, options)
                    inputStream?.close()

                    // Calculate sample size to scale down the bitmap if necessary
                    val requiredWidth = 800 // Adjust as needed
                    val requiredHeight = 800 // Adjust as needed
                    options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight)

                    // Set inJustDecodeBounds to false to load the actual bitmap
                    options.inJustDecodeBounds = false

                    // Load bitmap with adjusted options
                    val bitmapStream = requireContext().contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(bitmapStream, null, options)
                    bitmapStream?.close()

                    return@withContext bitmap
                }
                null
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } catch (e: SecurityException) {
                e.printStackTrace()
                null
            }
        }
    }

    // Function to calculate sample size based on required dimensions
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }


    private fun maleSelection(){

        binding.maleSelect.visibility = View.VISIBLE
        binding.maleUnselect.visibility = View.GONE

        binding.femaleUnselect.visibility = View.VISIBLE
        binding.othersUnselect.visibility = View.VISIBLE

        binding.femaleSelect.visibility = View.GONE
        binding.otherSelect.visibility = View.GONE

    }

    private fun femaleSelection(){

        binding.femaleSelect.visibility = View.VISIBLE
        binding.femaleUnselect.visibility = View.GONE

        binding.maleUnselect.visibility = View.VISIBLE
        binding.othersUnselect.visibility = View.VISIBLE

        binding.maleSelect.visibility = View.GONE
        binding.otherSelect.visibility = View.GONE

    }

    private fun otherSelection(){

        binding.otherSelect.visibility = View.VISIBLE
        binding.othersUnselect.visibility = View.GONE

        binding.maleUnselect.visibility = View.VISIBLE
        binding.maleSelect.visibility = View.GONE

        binding.femaleSelect.visibility = View.GONE
        binding.femaleUnselect.visibility = View.VISIBLE

    }

}