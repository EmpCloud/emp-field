package com.empcloud.empmonitor.ui.fragment.client.addclient

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
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
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
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.databinding.FragmentAddClientBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity
import com.empcloud.empmonitor.ui.fragment.attendance.AttendanceViewModel
import com.empcloud.empmonitor.ui.fragment.client.clientaddress.ClientAddressFragment
import com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeFragment
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.internal.wait
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.Pattern

@AndroidEntryPoint
class AddClientFragment : Fragment() {

    private lateinit var binding :FragmentAddClientBinding
    private val viewModel by viewModels<AddClientViewModel>()
    private val REQUEST_CAMERA_PERMISSION = 121
    private lateinit var currentPhotoPath:String
    private var savedUri:Uri?= null
    var bitmapReciceve:Bitmap? = null
    private var profilePic:String? = null
    private var sendPic:String? = null

    private lateinit var camerLayout: LinearLayout
    private lateinit var codePicker: CountryCodePicker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddClientBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        camerLayout = binding.cameraProfile
        codePicker = binding.myPhoneInput


        binding.addAddressBtn.setOnClickListener {

            CommonMethods.saveSharedPrefernce(requireActivity(),Constants.CATEGORY_CLIENT,Constants.CATEGORY_CLIENT,binding.category.text.toString())
//            Log.d("agsagasfa",binding.category.text.toString())

            val isEditText1Filled = binding.empname.text.toString().isNotEmpty()
//        val isEditText2Filled = binding.emailId.text.toString().isNotEmpty()
            val isEditText3Filled = binding.mobileNo.text.toString().isNotEmpty()
            val isEditText4Filled = binding.category.text.toString().isNotEmpty()

            if (isEditText1Filled && isEditText3Filled && isEditText4Filled){
//                if (isValidPhoneNumber(binding.mobileNo.text.toString())){
                if(isEamilValid(binding.emailId.text.toString()))
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,ClientAddressFragment()).commit()
                else
                    Toast.makeText(requireContext(),"EmailId  is not valid",Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(requireContext(),"Mobile no is not valid",Toast.LENGTH_SHORT).show()
//                }
            }else{

                Toast.makeText(requireContext(),"Please enter required fields",Toast.LENGTH_SHORT).show()
            }
            val phoneNumber = binding.mobileNo.text.toString()
            val countryCode = codePicker.selectedCountryCodeWithPlus
            val nameCode = codePicker.selectedCountryNameCode

            val shredPref = requireActivity().getSharedPreferences(Constants.CLIENT_DETAILS,AppCompatActivity.MODE_PRIVATE)
            shredPref.edit().putString(Constants.CLIENT_NAME,binding.empname.text.toString()).apply()
            shredPref.edit().putString(Constants.CLIENT_PHONE,phoneNumber).apply()
            shredPref.edit().putString(Constants.CLIENT_PHONE_COUTRY_CODE,countryCode).apply()
            shredPref.edit().putString(Constants.CLIENT_PHONE_COUTRY_NAME_CODE,nameCode).apply()
//            Log.d("Countrycode","$countryCode $nameCode")
            if (binding.emailId.text.isNotEmpty()) shredPref.edit().putString(Constants.CLIENT_EMAIL,binding.emailId.text.toString()).apply()
            else shredPref.edit().putString(Constants.CLIENT_EMAIL,null).apply()
            shredPref.edit().putString(Constants.CLIENT_CATEGORY,binding.category.text.toString()).apply()

        }

        binding.enableClientbtn.setOnClickListener {

            val fragment = ClientAddressFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment).commit()

        }

        camerLayout.setOnClickListener {

            showImagePickerOptions()
        }

        binding.backbtn.setOnClickListener {


            CommonMethods.clearAllValues(requireContext(),Constants.CLIENT_DETAILS)

            CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.CLIENT_PROFILE_URL)
            CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.CLIENT_BITMAP_RECIEVE)

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,ClientHomeFragment()).commit()
        }


        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateForm()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.empname.addTextChangedListener(watcher)
        binding.emailId.addTextChangedListener(watcher)
        binding.mobileNo.addTextChangedListener(watcher)

        saveSetUserData()
        observeProfileImgUpdate()
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
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                dispatchTakePictureIntent()
            }
            else -> {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    savedUri = FileProvider.getUriForFile(requireContext(), "com.empcloud.empmonitor.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, savedUri)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
//        return File.createTempFile(
//            "PNG_${timeStamp}_",
//            ".png",
//            storageDir
//        ).apply {
//            currentPhotoPath = absolutePath
//        }
//    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",  // Change the file extension to ".jpg"
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    camerProcess()
                    handleCameraImage()
                }
                REQUEST_GALLERY_IMAGE -> data?.data?.let { handleGalleryImage(it) }
            }
        }
    }

    private fun handleCameraImage() {

//        val photoFile = createImageFile()

        val photoFile = File(currentPhotoPath)

        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//        CoroutineScope(Dispatchers.IO).launch {
        val savedUri = Uri.fromFile(photoFile)

        val file = File(savedUri.path ?: "")
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val imgpath = CommonMethods.getFileFromUri(requireContext(),savedUri)
        val filePart = MultipartBody.Part.createFormData("files", imgpath?.name, requestFile)
        val sp = requireContext().getSharedPreferences(Constants.AUTH_TOKEN, AppCompatActivity.MODE_PRIVATE)
        val token = sp.getString(Constants.AUTH_TOKEN,"")
        invokeProfileImg(token!!, filePart)

    }


    private fun handleGalleryImage(imageUri: Uri) {

        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
        binding.userPic.visibility = View.VISIBLE
        binding.userPicdisbale.visibility = View.GONE
        if (bitmap != null){
            binding.userPic.setImageBitmap(bitmap)
            CommonMethods.saveBitmapToSharedPreferences(requireContext(),bitmap,Constants.CLIENT_BITMAP_RECIEVE)
        }

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
                    invokeProfileImg(token, filePart)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to get image file path", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun invokeProfileImg(token: String, part: MultipartBody.Part) {
        // Your API call logic here

        viewModel.invokeUpdateImage(token,part)
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_GALLERY_IMAGE = 2
        private const val REQUEST_CAMERA_PERMISSION = 100
    }

    private fun observeProfileImgUpdate(){
//        Log.d("inside","img data")
        lifecycleScope.launch {
            with(viewModel){
                observeImageData.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.statusCode == 200){

//                                android.util.Log.d("ResponseImg",it.toString())
//                                saveUserData(it)
                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                                profilePic = it.body.data.profileURL
                                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.CLIENT_PROFILE_URL,Constants.CLIENT_PROFILE_URL,profilePic!!)
                                binding.progressBar.visibility = View.GONE
//                                Log.d("adfafads",profilePic.toString())

                            }
                            if (it.statusCode == 400){
                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                            }

                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(
//                                requireContext(),"Loading...",
//                                Toast.LENGTH_LONG).show()
//                            android.util.Log.d("ResponseImg","Loading")
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


    private fun validateForm() {

        val isEditText1Filled = binding.empname.text.toString().isNotEmpty()
//        val isEditText2Filled = binding.emailId.text.toString().isNotEmpty()
        val isEditText3Filled = binding.mobileNo.text.toString().isNotEmpty()
        val isEditText4Filled = binding.category.text.toString().isNotEmpty()



        binding.enableClientbtn.visibility = if (isEditText1Filled &&  isEditText3Filled && isEditText4Filled)
        {
            View.VISIBLE

        } else {
            View.GONE

        }

        binding.disableClientbtn.visibility = if (isEditText1Filled &&  isEditText3Filled && isEditText4Filled) {
            View.VISIBLE
        } else {
            View.GONE
        }


//        Log.d("categoruText",binding.category.text.toString())


    }

    private fun camerProcess(){

//        val  bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, savedUri)
//            if(bitmap != null){
//                binding.userPicdisbale.visibility = View.GONE
//                binding.userPic.visibility = View.VISIBLE
//                binding.userPic.setImageBitmap(bitmap)
//                CommonMethods.saveBitmapToSharedPreferences(requireContext(),bitmap!!,Constants.CLIENT_BITMAP_RECIEVE)
//            }

        lifecycleScope.launch {
            try {
                // Load the bitmap asynchronously
                val bitmap = withContext(Dispatchers.IO) {
                    CommonMethods.loadBitmapFromUri(savedUri,requireContext())
                }

                // Update UI on the main thread
                bitmap?.let {
                    binding.userPicdisbale.visibility = View.GONE
                    binding.userPic.visibility = View.VISIBLE
                    binding.userPic.setImageBitmap(CommonMethods.handleSamplingAndRotationBitmap(it))

                    CommonMethods.saveBitmapToSharedPreferences(requireContext(),bitmap!!,Constants.CLIENT_BITMAP_RECIEVE)
                } ?: run {
//                    Log.d("handleCameraImage", "Bitmap is null")
                    Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to load image: IO Exception", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to load image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun isEamilValid(email:String):Boolean{

        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // Define a regex pattern for a valid phone number with country code
        val phoneNumberPattern = Pattern.compile("^\\d{10}$")

        return phoneNumberPattern.matcher(phoneNumber).matches()
    }

    private fun saveSetUserData(){

        val shredPref = requireActivity().getSharedPreferences(Constants.CLIENT_DETAILS,AppCompatActivity.MODE_PRIVATE)

        val nameEmp = shredPref.getString(Constants.CLIENT_NAME,"")
        val phoneno = shredPref.getString(Constants.CLIENT_PHONE,"")
        val email = shredPref.getString(Constants.CLIENT_EMAIL,"")
        val category = shredPref.getString(Constants.CLIENT_CATEGORY,"")

//        Log.d("agklahlkghl","$nameEmp $phoneno $email $category")
        binding.empname.setText(nameEmp )
//        binding.mobileNo.setText(phoneno)
        binding.emailId.setText(email)
        binding.category.setText(category)

    }
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(emailRegex)
    }

}