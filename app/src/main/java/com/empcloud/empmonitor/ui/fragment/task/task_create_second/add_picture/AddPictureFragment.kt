package com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_picture

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail
import com.empcloud.empmonitor.databinding.FragmentAddPictureBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task.AddTaskFragment
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class AddPictureFragment constructor(private val listener: OnFragmentChangedListener? = null): Fragment() {

    private lateinit var binding: FragmentAddPictureBinding
    private val viewModel by viewModels<AddPictureViewModel>()

    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private lateinit var cameraProvider: ProcessCameraProvider

    private lateinit var cameraExecutor: ExecutorService
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var currentPhotoPath: String

    private var picResponse:String? = null

    private lateinit var imageCapture: ImageCapture

    private var clientid:String? = null
    private var clientname:String? = null


    private var fragmentNo:Int? = null
    private val REQUEST_CODE_PICK_IMAGE = 1002
    private val REQUEST_CODE_PERMISSIONS = 1001
    private lateinit var preview: Preview

    private var fetchTaskDetail: FetchTaskDetail? = null

    companion object {
        fun newInstance() = AddPictureFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        fragmentNo = arguments?.getInt(Constants.FRAGMENT_NO)

        if(fragmentNo == 1){
            clientid = arguments?.getString(Constants.CLIENTIT_SEDN)
            clientname = arguments?.getString(Constants.CLIENT_NAME_SEND)

        }

        if (fragmentNo == 2){
            fetchTaskDetail = arguments?.getSerializable(Constants.SELECTED_MODEL_ITEM_1) as FetchTaskDetail

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPictureBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomSheet.cancelbtn.setOnClickListener {

            binding.bottomSheet.bottomDesc.visibility = View.GONE
        }

        binding.gallery.setOnClickListener {

            openGallery()
        }

        binding.imageCapturebtn.setOnClickListener {

            captureImage(it)
        }

//        binding.cameraToggle.setOnClickListener {
//
//            toggleCamera()
//        }



        cameraExecutor = Executors.newSingleThreadExecutor()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases(binding.previewView)
        }, ContextCompat.getMainExecutor(requireContext()))

        binding.cameraToggle.setOnClickListener {
            if (::cameraProvider.isInitialized) {
                toggleCamera(binding.previewView)
            } else {
                Log.e("MainActivity", "Camera provider is not yet initialized")
            }
        }
        binding.backbtnCamera.setOnClickListener {

            if(fragmentNo == 1){
                val fragment = AddTaskFragment(listener)
                val args = Bundle().apply {
                    putString(Constants.CLIENTIT_RESEDN,clientid)
                    putString(Constants.CLIENT_NAME_RESEND,clientname)
                }
                fragment.arguments = args
                CommonMethods.switchFragment(requireActivity(),fragment)
            }

        }

        val textWatcher = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                validateForm()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }

        validateForm()

        binding.bottomSheet.description.addTextChangedListener(textWatcher)

        binding.bottomSheet.donebtn.setOnClickListener {

            if (fragmentNo == 1){

                var count = CommonMethods.getSharedPrefernceInteger(requireActivity(),Constants.COUNT)
                if (count == 0)   {
                    count += 1
                    CommonMethods.saveSharedPrefernceInteger(requireActivity(),Constants.COUNT,Constants.COUNT,count)
                }
                else {

                    count += 1
                    CommonMethods.saveSharedPrefernceInteger(requireActivity(),Constants.COUNT,Constants.COUNT,count)
                }
                val fragment = AddTaskFragment(listener)
                val args = Bundle().apply {
                    putString(Constants.PIC_RESPONSE,picResponse)
                    val desc = binding.bottomSheet.description.text.toString()
                    putString(Constants.PIC_DESC,desc)
                    putString(Constants.CLIENTIT_RESEDN,clientid)
                    putString(Constants.CLIENT_NAME_RESEND,clientname)

                }

                fragment.arguments = args
                CommonMethods.switchFragment(requireActivity(),fragment)
            }

        }

        startCamera()
        observeSendPicsCall()

    }

    private fun validateForm() {

        val descrpic = binding.bottomSheet.description.text.isNotEmpty()
        binding.bottomSheet.donebtn.visibility = if (descrpic )
        {
            View.VISIBLE

        } else {
            View.GONE
        }

        binding.bottomSheet.donebtndisable.visibility = if (descrpic) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    private fun observeSendPicsCall() {

        Log.d("ADFASDGASD", "insdie")
        lifecycleScope.launch {
            with(viewModel) {
                observeSendPicsDataCall.collect { res ->
                    when (res) {
                        is ApiState.ERROR -> {
                            android.util.Log.d("insideAddTASK", "ERROR")
                        }

                        com.empcloud.empmonitor.network.api_satatemanagement.ApiState.LOADING -> {
                            android.util.Log.d("insideAddTASK", "LOADING")

                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is ApiState.SUCESS -> {

                            val it = res.getResponse
                            if (it.code == 200) {

                                android.widget.Toast.makeText(
                                    requireContext(), it.data.filesUrls[0].message,
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                                picResponse = it.data.filesUrls[0].url
                                binding.progressBar.visibility = View.GONE

                                binding.bottomSheet.bottomDesc.visibility = View.VISIBLE
                            }
                            if (it.code == 400) {

                                android.widget.Toast.makeText(
                                    requireContext(), it.data.filesUrls[0].message,
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    }
                }
            }
        }
    }

    private fun captureImage() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.empcloud.empmonitor.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }


    private fun captureImage(view: View) {
        val imageCapture = imageCapture ?: return

        val photoFile = createImageFile()

        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)

                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)

//                    val invertBitmap = invertBitmap(bitmap)
                    binding.picset.setImageBitmap(bitmap)
                    binding.picset.visibility = View.VISIBLE

                    val file = File(savedUri.path ?: "")
                    val requestFile =
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    val imgpath = getFileFromUri(requireContext(), savedUri)
                    val filePart =
                        MultipartBody.Part.createFormData("files", imgpath?.name, requestFile)

                    binding.bottomSheet.bottomDesc.visibility = View.VISIBLE


                    viewModel.invokeSendPicsCall(
                        CommonMethods.getSharedPrefernce(requireActivity(), Constants.AUTH_TOKEN),
                        filePart
                    )

                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                }
            }
        )
    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        val contentResolver = context.contentResolver

        // Handle different Uri schemes
        return when (uri.scheme) {
            "content" -> {
                contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        return File(cursor.getString(columnIndex))
                    }
                }
                null
            }

            "file" -> {
                return File(uri.path)
            }

            else -> {
                null // Unsupported scheme
            }
        }
    }

    private fun invertBitmap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()

        // Mirror the image horizontally
        matrix.postScale(-1f, 1f, (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat())

        // Rotate the image by 90 degrees
//        matrix.postRotate(90f, (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat())

        // Apply the transformations to the bitmap
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraUseCases(previewView: PreviewView) {
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        try {
            cameraProvider.unbindAll()

            preview = Preview.Builder().build()
            imageCapture = ImageCapture.Builder().build()

            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture
            )

            preview.setSurfaceProvider(previewView.surfaceProvider)

        } catch (exc: Exception) {
            Log.e("MainActivity", "Use case binding failed", exc)
        }
    }

    private fun toggleCamera(previewView: PreviewView) {
        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
        bindCameraUseCases(previewView)
    }



    override fun onDestroyView() {
        super.onDestroyView()

        if (this::cameraProvider.isInitialized) {
            cameraProvider.unbindAll()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                if (this::cameraProvider.isInitialized) {
                    cameraProvider.unbindAll()
                }
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                binding.picset.visibility = View.VISIBLE
                binding.picset.setImageBitmap(bitmap)
                binding.bottomSheet.bottomDesc.visibility = View.VISIBLE

                uploadImage(uri)
                // uploadImage(uri) // Uncomment this line if you want to upload the image
            }
        }
    }

    private fun uploadImage(uri: Uri) {

        val imagePath = CommonMethods.getFileFromUriGallery(requireContext(),uri)
        if (imagePath != null){
            val file = imagePath
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val filePart = MultipartBody.Part.createFormData("files", file.name, requestFile)

            viewModel.invokeSendPicsCall(
                CommonMethods.getSharedPrefernce(requireActivity(), Constants.AUTH_TOKEN),
                filePart
            )
        }

    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }
}