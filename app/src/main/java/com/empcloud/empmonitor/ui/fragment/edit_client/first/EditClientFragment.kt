package com.empcloud.empmonitor.ui.fragment.edit_client.first

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Picture
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.request.update_client.UpdatecClientModel
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail
import com.empcloud.empmonitor.databinding.FragmentEditClientBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.fragment.attendance.AttendanceFragment
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientFragment
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientViewModel
import com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeFragment
import com.empcloud.empmonitor.ui.fragment.edit_client.client_address.EditClientAddressFragment
import com.empcloud.empmonitor.ui.fragment.edit_client.update_client.UpdaeEditClientFragment
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.hbb20.CountryCodePicker
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
class EditClientFragment constructor(private val listener: OnFragmentChangedListener? = null): Fragment() {

    private lateinit var binding:FragmentEditClientBinding
    private var clientDetails: ClientFetchDetail? = null
    private val viewModel by viewModels<EditClientViewModel>()
    private val REQUEST_CAMERA_PERMISSION = 121
    private lateinit var currentPhotoPath:String
    private var savedUri:Uri?= null
    private var profilePic:String? = null
    private var lat:Double? = null
    private var lon:Double? = null
    private var countryCode:String? = null
    private var countryname:String? = null
    private var state:String? = null
    private var zip:String? = null
    private var city:String? = null
    private var address:String? = null
    private var category:String? = null


    private var fragmentno:Int? = null

    private lateinit var codePicker: CountryCodePicker

    companion object {
        fun newInstance() = EditClientFragment
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clientDetails = arguments?.getSerializable(Constants.CLIENT_DETIALS_BUNDLE) as? ClientFetchDetail
        fragmentno = arguments?.getInt("FramentCameFrom")
        countryCode = arguments?.getString(Constants.COUNTRYCODE_1)
        countryname = arguments?.getString(Constants.COUNTRYNAME_1)
        state = arguments?.getString(Constants.STATE_UPDATE_1)
        city = arguments?.getString(Constants.CITY_UPDATE_1)
        zip = arguments?.getString(Constants.ZIP_UPDATE_1)

        address = arguments?.getString(Constants.ADDRESS_CLIENT_1)

//        Log.d("thrher1",address.toString())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditClientBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countryCodeGetterAndSetter()

        binding.cameraProfile.setOnClickListener {

            showImagePickerOptions()
        }

        if (fragmentno == 300 ) {
            lat =  clientDetails?.latitude?.toDouble()
            lon =  clientDetails?.longitude?.toDouble()
        }else{

            lat = arguments?.getDouble(Constants.LAT_UPDATE_1)
            lon = arguments?.getDouble(Constants.LON_UPDATE_1)
        }

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateForm()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        validateForm()

        btnClickEvents()


        binding.fullName.addTextChangedListener(watcher)
//        binding.emailId.addTextChangedListener(watcher)
        binding.mobileNo.addTextChangedListener(watcher)
        binding.address.addTextChangedListener(watcher)


        setUserData()

//        setUserPic()
        observeProfileImgUpdate()
        observeUpdateClientCall()

    }

    private fun countryCodeGetterAndSetter() {

        codePicker = binding.myPhoneInput
//        val countryCodeextract  = clientDetails!!.countryCode
//        val numericCode = countryCodeextract.replace("+","").toInt()
//        codePicker.setCountryForPhoneCode(numericCode)
        val countryCodeWithPlus = clientDetails?.countryCode

// Remove the plus sign and handle country codes with hyphens
        if (!countryCodeWithPlus.isNullOrEmpty()) {
            val numericCodeString = countryCodeWithPlus.replace(
                "+",
                ""
            ). split ("-")[0] // Get the part before the hyphen
            val numericCode = numericCodeString.toIntOrNull() // Convert to Int safely

            if (numericCode != null) {
                codePicker.setCountryForPhoneCode(numericCode)
            } else {
                // Handle error if country code cannot be converted to an integer
                Log.e("CountryCodeError", "Invalid country code: $numericCodeString")
            }
        }
    }

    private fun btnClickEvents() {

        binding.editbtn.setOnClickListener {

            val fragment = EditClientAddressFragment()
            val args = Bundle().apply {
                putSerializable(Constants.CLIENT_DETIALS_BUNDLE,clientDetails)
            }
            fragment.arguments = args
            CommonMethods.switchFragment(requireActivity(),fragment)
        }

        binding.backbtn.setOnClickListener {

            val updateeditFragment = UpdaeEditClientFragment(listener)
            val args = Bundle().apply {

                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.CLIENT_DETIALS_BUNDLE)
                putSerializable(Constants.CLIENT_DETIALS_BUNDLE,clientDetails)
            }
            updateeditFragment.arguments = args
            CommonMethods.switchFragment(requireActivity(),updateeditFragment)
        }


        binding.enableClientBtn.setOnClickListener {

            val token = CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN)

            if (address == null) address = clientDetails?.address1
            if (countryCode == null) countryCode = clientDetails?.countryCode
            if (countryname == null) countryname = clientDetails?.country
            if (zip == null) zip = clientDetails?.zipCode
            if (lat == null) lat = clientDetails?.latitude?.toDouble()
            if (lon == null) lon = clientDetails?.longitude?.toDouble()
            if (city == null) city = clientDetails?.city
            if (state.isNullOrEmpty()) state = clientDetails?.state
            if(category == null) category = clientDetails?.category
//            Log.d("hadgdg","$lat $lon")
            val clientId = clientDetails?._id
            val updatecClientModel = UpdatecClientModel(
                binding.fullName.text.toString(),
                binding.emailId.text.toString(),
                binding.mobileNo.text.toString(),
                profilePic,
                binding.category.text.toString(),
//                category!!,
//                countryCode!!,
                codePicker.selectedCountryCodeWithPlus,
                address!!.toString(),
                address!!.toString(),
                countryname!!,
                state!!,
                city!!,
                zip!!,
                lat!!.toString(),
                lon!!.toString()
            )
//            Log.d("hadgdg",UpdatecClientModel(
//                binding.fullName.text.toString(),
//                binding.emailId.text.toString(),
//                binding.mobileNo.text.toString(),
//                profilePic,
////                binding.category.text.toString(),
//                "category",
////                countryCode!!,
//                "91",
//                address!!.toString(),
//                address!!.toString(),
//                countryname!!,
//                state!!,
//                city!!,
//                zip!!,
//                lat!!.toString(),
//                lon!!.toString()
//            ).toString())
            viewModel.invokeUpdateClientCall(token,clientId!!,updatecClientModel)
            CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.CLIENT_BITMAP_RECIEVE)
        }
    }

    private fun setUserData() {

        val pic = CommonMethods.getSharedPrefernce(requireActivity(),Constants.CLIENT_EDIT_UPDATE_PROFILE)
        if (!pic.isNullOrEmpty()){

            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
            profilePic = pic
            Picasso.get().load(pic).into(binding.userPic)
        }
        else if (!clientDetails?.clientProfilePic.isNullOrEmpty()){

            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
            Picasso.get().load(clientDetails!!.clientProfilePic).into(binding.userPic)
            profilePic = clientDetails!!.clientProfilePic
        }

        if(!clientDetails?.clientName.isNullOrEmpty()) binding.fullName.setText(clientDetails!!.clientName)

        if (!address.isNullOrEmpty()) binding.address.setText(address)
        else{
            if(!clientDetails?.address1.isNullOrEmpty() && !countryname.isNullOrEmpty()) {

                binding.address.setText(arguments?.getString(Constants.ADDRESS_CLIENT_1))
            }
            else{
                binding.address.setText(clientDetails!!.address1)
            }
        }


        if(!clientDetails?.contactNumber.isNullOrEmpty())binding.mobileNo.setText(clientDetails!!.contactNumber)

        if(!clientDetails?.emailId.isNullOrEmpty())binding.emailId.setText(clientDetails!!.emailId)

        if(!clientDetails?.category.isNullOrEmpty())binding.category.setText(clientDetails!!.category)



    }

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
        startActivityForResult(intent, AddClientFragment.REQUEST_GALLERY_IMAGE)
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
                    startActivityForResult(takePictureIntent,
                        AddClientFragment.REQUEST_IMAGE_CAPTURE
                    )
                }
            }
        }
    }

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
                AddClientFragment.REQUEST_IMAGE_CAPTURE -> {
//                    camerProcess()
                    handleCameraImage()
                }
                AddClientFragment.REQUEST_GALLERY_IMAGE -> data?.data?.let { handleGalleryImage(it) }
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

//        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
//        binding.userPic.visibility = View.VISIBLE
//        binding.userPicdisbale.visibility = View.GONE
//        if (bitmap != null){
//            binding.userPic.setImageBitmap(bitmap)
//            CommonMethods.saveBitmapToSharedPreferences(requireContext(),bitmap,Constants.CLIENT_BITMAP_RECIEVE)
//        }

        if (!profilePic.isNullOrEmpty()){

            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
            Picasso.get().load(profilePic).into(binding.userPic)
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

//    companion object {
//        const val REQUEST_IMAGE_CAPTURE = 1
//        const val REQUEST_GALLERY_IMAGE = 2
//        private const val REQUEST_CAMERA_PERMISSION = 100
//    }

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
                                binding.userPic.visibility = View.VISIBLE
                                binding.userPicdisbale.visibility = View.GONE
                                Picasso.get().load(profilePic).into(binding.userPic)
                                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.CLIENT_EDIT_UPDATE_PROFILE,Constants.CLIENT_EDIT_UPDATE_PROFILE,profilePic!!)
//                                Log.d("adfafads",profilePic.toString())

                            }
                            if (it.statusCode == 400){
                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                            }

                        }
                        is ApiState.LOADING -> {
                            Toast.makeText(
                                requireContext(),"Loading...",
                                Toast.LENGTH_LONG).show()
                            android.util.Log.d("ResponseImg","Loading")
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

        val isEditText1Filled = binding.fullName.text.toString().isNotEmpty()
//        val isEditText2Filled = binding.emailId.text.toString().isNotEmpty()
        val isEditText3Filled = binding.mobileNo.text.toString().isNotEmpty()
        val isEditText4Filled = binding.address.text.toString().isNotEmpty()


        binding.enableClientBtn.visibility = if (isEditText1Filled && isEditText3Filled && isEditText4Filled)
        {
            View.VISIBLE

        } else {
            View.GONE
        }

        binding.enableClientBtnDisable.visibility = if (isEditText1Filled  && isEditText3Filled && isEditText4Filled) {
            View.GONE
        } else {
            View.VISIBLE
        }

    }

    private fun camerProcess(){

        val  bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, savedUri)
        if(bitmap != null){
            binding.userPicdisbale.visibility = View.GONE
            binding.userPic.visibility = View.VISIBLE
            binding.userPic.setImageBitmap(bitmap)
            CommonMethods.saveBitmapToSharedPreferences(requireContext(),bitmap!!,Constants.CLIENT_BITMAP_RECIEVE)
        }

    }


    private fun observeUpdateClientCall(){
        Log.d("inside","img data")
        lifecycleScope.launch {
            with(viewModel){
                observeUpdateClinetFlow.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.statusCode == 200){

//                                android.util.Log.d("ResponseImg",it.toString())
//                                saveUserData(it)
                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.CLIENT_EDIT_UPDATE_PROFILE)
                                CommonMethods.switchFragment(requireActivity(),ClientHomeFragment(listener))



                            }
                            if (it.statusCode == 400){

                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                            }

                        }
                        is ApiState.LOADING -> {
                            Toast.makeText(
                                requireContext(),"Loading...",
                                Toast.LENGTH_LONG).show()
                            android.util.Log.d("ResponseImg","Loading")
                        }
                        is ApiState.ERROR -> {
                            android.util.Log.d("ResponseImg",res.errorBody.toString())


                        }
                    }
                }
            }
        }
    }


    private fun setUserPic(){

        val pic = CommonMethods.getBitmapFromSharedPreferences(requireContext(),Constants.CLIENT_BITMAP_RECIEVE)
        if(pic != null){
            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
            binding.userPic.setImageBitmap(pic)
        }
    }

}