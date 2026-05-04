package com.empcloud.empmonitor.ui.fragment.settings

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.marginEnd
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.request.mode_of_transport.ModeOFTransportModel
import com.empcloud.empmonitor.databinding.FragmentSettingsBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.ui.activity.webview_activity.WebViewActivity
import com.empcloud.empmonitor.ui.fragment.home.HomeFragment
import com.empcloud.empmonitor.ui.fragment.home.HomeViewModel
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment
import com.empcloud.empmonitor.ui.fragment.update_profile.UpdateProfileFragment
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.utils.ActiveTaskTracker
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.empcloud.empmonitor.utils.NativeLib
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import kotlin.concurrent.thread

@AndroidEntryPoint
class SettingsFragment constructor(private val listener: OnFragmentChangedListener? = null) : Fragment() {

    private lateinit var binding:FragmentSettingsBinding
    private var isModeSelected:Boolean = false
    private val channelId = "download_channel"
    private var mode: String? = null
    private var mode_local: String? = null

    private val PERMISSION_REQUEST_CODE = 1001
    private val PERMISSION_REQUEST_CODE_2 = 1002

    private var isGlobal =  false

    private val viewModel by viewModels<HomeViewModel> ()
    companion object {
        fun newInstance() = SettingsFragment
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isUserGlobal = CommonMethods.getSharedPrefernceBoolean(requireActivity(),Constants.IS_GLOBAL_USER)
        isGlobal = isUserGlobal

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val sharedPred = requireActivity().getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
//        val name = sharedPred.getString(Constants.NAME_FULL,"")
//        val role = sharedPred.getString(Constants.ROLE,"")
//        if(!name.isNullOrEmpty() && !role.isNullOrEmpty()){
//            binding.name.text = name
//            binding.role.text = role
//        }

        if (isGlobal) binding.modeOption.visibility = View.GONE
        else binding.modeOption.visibility = View.VISIBLE
        setUserData()
        binding.logoutbtn.setOnClickListener {

            val sp = requireContext().getSharedPreferences(Constants.AUTH_TOKEN,AppCompatActivity.MODE_PRIVATE)
            sp.edit().putString(Constants.AUTH_TOKEN,"").apply()
            CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.BITMAP_RECIEVE)
            CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.BITMAP_RECIEVE_UPDATE)
            CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.LAST_MODE_SELECTED)
            CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.PROFILE_PIC_URL_USER)


            CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.IS_CHECKEDIN)
            CommonMethods.saveSharedPrefernce(requireActivity(),Constants.IS_CHECKEDIN,Constants.IS_CHECKEDIN,"NO")
            CommonMethods.clearAllValues(requireActivity(),Constants.IS_GLOBAL_USER)

            // Cancel auto-checkout alarm and clear auto check-in data
            CommonMethods.cancelAutoCheckout(requireContext())
            CommonMethods.clearStringFromSharedPreferences(requireContext(), Constants.AUTO_CHECK_IN_TIME)
            CommonMethods.clearStringFromSharedPreferences(requireContext(), Constants.CHECK_IN_METHOD)
            CommonMethods.clearAllValues(requireContext(), Constants.AUTO_CHECK_IN_BY_MOBILE)
            CommonMethods.clearAllValues(requireContext(), Constants.AUTO_CHECK_IN_BY_GEO_FENCING)
            CommonMethods.clearAllValues(requireContext(), Constants.IS_GEO_FENCING_ON)
            CommonMethods.clearAllValues(requireContext(), Constants.GEO_LOG_STATUS)


            val intent = Intent(requireContext(),LoginOptionsActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.profile.setOnClickListener {

            CommonMethods.switchFragment(requireActivity(),UpdateProfileFragment(listener))
        }

        binding.backbtn.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
//            CommonMethods.switchFragment(requireActivity(),HomeFragment(listener))
        }
        binding.termsandcondition.setOnClickListener {

            loadWebView("https://empmonitor.com/terms-and-conditions/")
//            val intent = Intent(requireActivity(),WebViewActivity::class.java)
//            intent.putExtra(Constants.STATUS_SETTINGS,1)
//            startActivity(intent)
        }

        binding.privacyandpolicy.setOnClickListener {

            loadWebView("https://empmonitor.com/privacy-policy/")
//            val intent = Intent(requireActivity(),WebViewActivity::class.java)
//            intent.putExtra(Constants.STATUS_SETTINGS,2)
//            startActivity(intent)
        }

        binding.modeoftransport.setOnClickListener {

            binding.transportselection.poptransport.visibility = View.VISIBLE
        }

        binding.qropen.setOnClickListener {

            binding.qropenshow.qrpopup.visibility = View.VISIBLE
            val sp = requireContext().getSharedPreferences(Constants.USER_ID, AppCompatActivity.MODE_PRIVATE)
            val empid = sp.getString(Constants.USER_ID,"")
            if (!empid.isNullOrEmpty())  getQr(empid)
            setUserDataQr()
        }

        binding.qropenshow.cancelbtnqr.setOnClickListener {

            binding.qropenshow.qrpopup.visibility = View.GONE
        }

        binding.transportselection.bike.setOnClickListener {

            isModeSelected = true
            mode = "bike"
            mode_local = "bike"
            commonSelection()

            binding.transportselection.bike.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.car.setBackgroundResource(R.drawable.add_leave_bg)

            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
//            binding.transportselection.textcar.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))

        }

        binding.transportselection.car.setOnClickListener {

            isModeSelected = true
            mode = "car"
            mode_local = "car"
            commonSelection()
            binding.transportselection.car.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.bike.setBackgroundResource(R.drawable.add_leave_bg)

            binding.transportselection.textcar.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

//            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))


        }

        binding.transportselection.rail.setOnClickListener {

            isModeSelected = true
            mode = "car"
            mode_local = "rail"
            commonSelection()
            binding.transportselection.rail.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.bike.setBackgroundResource(R.drawable.add_leave_bg)

            binding.transportselection.textrail.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

//            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))


        }

        binding.transportselection.auto.setOnClickListener {

            isModeSelected = true
            mode = "car"
            mode_local = "auto"
            commonSelection()
            binding.transportselection.auto.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.bike.setBackgroundResource(R.drawable.add_leave_bg)

            binding.transportselection.textauto.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

//            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))


        }

        binding.transportselection.bus.setOnClickListener {

            isModeSelected = true
            mode = "car"
            mode_local = "bus"
            commonSelection()
            binding.transportselection.bus.setBackgroundResource(R.drawable.transport_selection_bg)
//            binding.transportselection.bike.setBackgroundResource(R.drawable.add_leave_bg)

            binding.transportselection.textbus.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

//            binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))


        }

        binding.transportselection.selectionbtn.setOnClickListener {

            if (isModeSelected){

                val modeOFTransportModel = ModeOFTransportModel(mode!!)
                viewModel.invokeTransportUpdation(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),modeOFTransportModel)
            }else Toast.makeText(requireContext(),"Please select mode of transport",Toast.LENGTH_SHORT).show()

        }

        binding.webbackbtn.setOnClickListener {

            binding.webcontain.visibility = View.GONE
        }

        checkPermissions()

        val layout = requireActivity().findViewById<View>(R.id.qropenshow)

        val cancelbtn = layout.findViewById<ImageView>(R.id.cancelbtnqr)

        val downloadButton:LinearLayout = layout.findViewById(R.id.downloadqr)

        val cardPopUp:CardView = layout.findViewById(R.id.dataCard)

        binding.qropenshow.downloadqr.setOnClickListener {

            val sp = requireContext().getSharedPreferences(Constants.USER_ID, AppCompatActivity.MODE_PRIVATE)
            val empid = sp.getString(Constants.USER_ID,"")
            val imageurl = NativeLib().getGoogleMapApiKey() + empid

            val sharedPred = requireActivity().getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
            val name =  CommonMethods.getSharedPrefernce(requireActivity(),Constants.LOGIN_NAME_PERSON)
            Log.d("nameFullpdf",name.toString())
            cancelbtn.visibility = View.GONE
            downloadButton.visibility = View.GONE
            val layoutParams = cardPopUp.layoutParams  as ViewGroup.MarginLayoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.marginStart = 0
            layoutParams.marginEnd = 0
//            layoutParams.setMargins(0,0,0,0)
            cardPopUp.layoutParams = layoutParams


            createPdf(layout,"$name.pdf")

            cancelbtn.visibility = View.VISIBLE
            downloadButton.visibility = View.VISIBLE

            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
//            downloadImageAndSaveAsPdf(imageurl)
        }

        (activity as? MainActivity)?.setMenuButtonVisibility(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
        createNotificationChannel()
        transportModeLastSelected()
        setUserPic()
        observeUpdationTransport()
    }

    private fun commonSelection() {

        binding.transportselection.bike.setBackgroundResource(R.drawable.add_leave_bg)
        binding.transportselection.car.setBackgroundResource(R.drawable.add_leave_bg)
        binding.transportselection.auto.setBackgroundResource(R.drawable.add_leave_bg)
        binding.transportselection.rail.setBackgroundResource(R.drawable.add_leave_bg)
        binding.transportselection.bus.setBackgroundResource(R.drawable.add_leave_bg)


        binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))
        binding.transportselection.textcar.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))
        binding.transportselection.textauto.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))
        binding.transportselection.textrail.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))
        binding.transportselection.textbus.setTextColor(ContextCompat.getColor(requireContext(),R.color.new_txt_3a))

    }

    private fun loadWebView(url:String){

        val webview:WebView = binding.webview
        binding.webcontain.visibility = View.VISIBLE
        webview.visibility = View.VISIBLE
        val webSettings: WebSettings = webview.settings
        webSettings.javaScriptEnabled = true // Enable JavaScript

        webview.loadUrl(url)
        webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return true
            }
        }
    }

    private fun setUserPic(){

//        val pic = CommonMethods.getBitmapFromSharedPreferences(requireContext(),Constants.BITMAP_RECIEVE)
        val pic = CommonMethods.getSharedPrefernce(requireActivity(),Constants.PROFILE_PIC_URL_USER)
        if(!pic.isNullOrEmpty()){
            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
            Picasso.get().load(pic).into(binding.userPic)
//            binding.userPic.setImageBitmap(pic)
        }else{
            binding.userPicdisbale.visibility = View.VISIBLE
            binding.userPic.visibility = View.GONE
        }

    }

    fun setUserData(){

        val sharedPred = requireActivity().getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
        val name = sharedPred.getString(Constants.NAME_FULL,"")
        val role = sharedPred.getString(Constants.ROLE,"")
        if(!name.isNullOrEmpty()){
            binding.name.text = name
            binding.role.text = CommonMethods.getSharedPrefernce(requireActivity(),Constants.LOGIN_ROLE_PERSON)
//            binding.role.text = role
        }else{

            binding.name.text = CommonMethods.getSharedPrefernce(requireActivity(),Constants.LOGIN_NAME_PERSON)
            binding.role.text = CommonMethods.getSharedPrefernce(requireActivity(),Constants.LOGIN_ROLE_PERSON)

        }
    }


    private fun observeUpdationTransport(){

        lifecycleScope.launch {
            with(viewModel){
                observeTransportCall.collect{res ->
                    when(res){

                        is ApiState.ERROR -> {}
                        com.empcloud.empmonitor.network.api_satatemanagement.ApiState.LOADING -> {}
                        is ApiState.SUCESS -> {

                            val it = res.getResponse
                            if (it.statusCode == 200){

                                binding.transportselection.poptransport.visibility = android.view.View.GONE
                                android.widget.Toast.makeText(requireContext(),it.body.message,
                                    android.widget.Toast.LENGTH_SHORT).show()
                                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.LAST_MODE_SELECTED)
                                CommonMethods.clearAllValues(requireContext(),Constants.LAST_MODE_SELECTED)
                                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.LAST_MODE_SELECTED,Constants.LAST_MODE_SELECTED,mode_local!!)
                                Log.d("asgsd",mode_local.toString())
                            }

                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun transportModeLastSelected(){

        var modelast = CommonMethods.getSharedPrefernce(requireActivity(),Constants.LAST_MODE_SELECTED)
        Log.d("asgsd1",modelast.toString())

        if (modelast != null){

            when(modelast){

                "bike"->{
                    isModeSelected = true
                    mode = "bike"
                    mode_local = "bike"
                    commonSelection()
                    binding.transportselection.bike.setBackgroundResource(R.drawable.transport_selection_bg)
                    binding.transportselection.textbike.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

                }

                "car"->{
                    isModeSelected = true
                    mode = "car"
                    mode_local = "car"
                    commonSelection()
                    binding.transportselection.car.setBackgroundResource(R.drawable.transport_selection_bg)
                    binding.transportselection.textcar.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                }
                "bus"->{
                    isModeSelected = true
                    mode = "car"
                    mode_local = "bus"
                    commonSelection()
                    binding.transportselection.bus.setBackgroundResource(R.drawable.transport_selection_bg)
                    binding.transportselection.textbus.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                }
                "rail"->{
                    isModeSelected = true
                    mode = "car"
                    mode_local = "rail"
                    commonSelection()
                    binding.transportselection.rail.setBackgroundResource(R.drawable.transport_selection_bg)
                    binding.transportselection.textrail.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                }
                "auto"->{
                    isModeSelected = true
                    mode = "car"
                    mode_local = "auto"
                    commonSelection()
                    binding.transportselection.auto.setBackgroundResource(R.drawable.transport_selection_bg)
                    binding.transportselection.textauto.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                }
            }

        }

    }
//
//    fun setUserDataQr(){
//
//        val sharedPred = requireActivity().getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
//        val name = sharedPred.getString(Constants.NAME_FULL,"")
//        val role = sharedPred.getString(Constants.ROLE,"")
//
//        binding.qropenshow.roletxt.text = CommonMethods.getSharedPrefernce(requireActivity(),Constants.LOGIN_ROLE_PERSON)
//        if(!name.isNullOrEmpty()){
//
//            val namesplit = name.split(" ")
//            binding.qropenshow.name.text = namesplit[0]
//            binding.qropenshow.namelast.text = namesplit[1]
//
//        }else{
//            val namesplit = CommonMethods.getSharedPrefernce(requireActivity(),Constants.LOGIN_NAME_PERSON)
//            val namesection = namesplit.split(" ")
//            binding.qropenshow.name.text = namesection[0]
//            binding.qropenshow.namelast.text = namesection[1]
//        }
//    }

    fun setUserDataQr() {
        val sharedPred = requireActivity().getSharedPreferences(Constants.USER_FULL_NAME, AppCompatActivity.MODE_PRIVATE)
        val name = sharedPred.getString(Constants.NAME_FULL, "")
        val role = sharedPred.getString(Constants.ROLE, "")

        binding.qropenshow.roletxt.text = CommonMethods.getSharedPrefernce(requireActivity(), Constants.LOGIN_ROLE_PERSON)

        val fullName = if (!name.isNullOrEmpty()) name
        else CommonMethods.getSharedPrefernce(requireActivity(), Constants.LOGIN_NAME_PERSON)

        val nameParts = fullName.trim().split(" ")

        binding.qropenshow.name.text = nameParts.getOrNull(0) ?: ""
        binding.qropenshow.namelast.text = nameParts.getOrNull(1) ?: ""
    }


    private fun getQr(empid:String){

        val link = NativeLib().getQRLinkLive() + empid
        Picasso.get().load(link).into(binding.qropenshow.qrpreview)
    }
    override fun onResume() {
        super.onResume()

        setUserPic()
        setUserData()
    }

    private fun downloadImageAndSaveAsPdf(imageUrl: String) {
        thread {
            try {
                Log.d("DownloadPDF", "Starting image download from URL: $imageUrl")

                // Download the image
                val url = URL(imageUrl)
                val bitmap: Bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())

                Log.d("DownloadPDF", "Image downloaded successfully")

                // Create a PDF document
                val pdfDocument = PdfDocument()
                val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
                val page = pdfDocument.startPage(pageInfo)

                Log.d("DownloadPDF", "PDF page created with dimensions: ${bitmap.width}x${bitmap.height}")

                // Draw the image on the PDF page
                val canvas: Canvas = page.canvas
                canvas.drawBitmap(bitmap, 0f, 0f, null)
                pdfDocument.finishPage(page)

                Log.d("DownloadPDF", "Image drawn on PDF page")

                // Save the PDF to a file
                val sharedPred = requireActivity().getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
                val name = sharedPred.getString(Constants.NAME_FULL,"")
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "$name.pdf")
                val outputStream = FileOutputStream(file)
                pdfDocument.writeTo(outputStream)

                Log.d("DownloadPDF", "PDF saved to ${file.absolutePath}")

                // Close the document and streams
                pdfDocument.close()
                outputStream.close()

                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "PDF saved to Downloads/image.pdf", Toast.LENGTH_LONG).show()
//                    showDownloadNotification(file.absolutePath)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("DownloadPDF", "Error: ${e.message}", e)
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Failed to download image or save PDF: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Download Notifications"
            val descriptionText = "Notifications for download status"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

//    private fun showDownloadNotification(filePath: String) {
//        val notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java)
//        val notification = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(android.R.drawable.stat_sys_download_done)
//            .setContentTitle("Download Complete")
//            .setContentText("PDF saved to: $filePath")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setAutoCancel(true)
//            .build()
//
//        notificationManager?.notify(1, notification)
//    }

    private fun showDownloadNotification(fileUri: Uri) {
        // Create an Intent to open the PDF file using the content URI from MediaStore
        val openPdfIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(fileUri, "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        // Create a PendingIntent that will open the PDF when the notification is clicked
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            openPdfIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notificationManager = ContextCompat.getSystemService(requireContext(), NotificationManager::class.java)
        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle("Download Complete")
            .setContentText("PDF saved successfully")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)  // Attach the PendingIntent to the notification
            .setAutoCancel(true)
            .build()

        // Show the notification
        notificationManager?.notify(1, notification)
    }


//    private fun showDownloadNotification(filePath: String) {
//        // Create an Intent to open the PDF file
//        val file = File(filePath)
//        val uri = FileProvider.getUriForFile(requireContext(), "com.empcloud.empmonitor.fileprovider", file)
//        val openPdfIntent = Intent(Intent.ACTION_VIEW).apply {
//            setDataAndType(uri, "application/pdf")
//            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
//        }
//
//        // Create a PendingIntent that will open the PDF when the notification is clicked
//        val pendingIntent = PendingIntent.getActivity(
//            requireContext(),
//            0,
//            openPdfIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        // Build the notification
//        val notificationManager = ContextCompat.getSystemService(requireContext(), NotificationManager::class.java)
//        val notification = NotificationCompat.Builder(requireContext(), channelId)
//            .setSmallIcon(android.R.drawable.stat_sys_download_done)
//            .setContentTitle("Download Complete")
//            .setContentText("PDF saved to: $filePath")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)  // Attach the PendingIntent to the notification
//            .setAutoCancel(true)
//            .build()
//
//        // Show the notification
//        notificationManager?.notify(1, notification)
//    }

    private fun getBitMapFromView(view: View):Bitmap{

        view.measure(
            View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY)
        )
        view.layout(0,0,view.measuredWidth,view.measuredHeight)

        val bitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

//    private fun createPdf(view: View, fileName: String) {
//        val bitmap = getBitMapFromView(view)
//
//        // Create a PdfDocument with a page size matching the bitmap dimensions
//        val document = PdfDocument()
//        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
//        val page = document.startPage(pageInfo)
//
//        // Draw the bitmap onto the PDF page
//        val canvas = page.canvas
//        canvas.drawBitmap(bitmap, 0f, 0f, null)
//        document.finishPage(page)
//
//        // Save the PDF file to storage
//        val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
//        val file = File(directoryPath + fileName)
////        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
//        try {
//            document.writeTo(FileOutputStream(file))
//            Toast.makeText(context, "PDF saved at: ${file.absolutePath}", Toast.LENGTH_LONG).show()
//            showDownloadNotification(file.absolutePath)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
//        } finally {
//            document.close()
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createPdf(view: View, fileName: String) {
        val bitmap = getBitMapFromView(view)

        // Create a PdfDocument with a page size matching the bitmap dimensions
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = document.startPage(pageInfo)

        // Draw the bitmap onto the PDF page
        val canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)

        // Use MediaStore for saving the PDF to the Downloads folder (Android 10+)
        val resolver = context?.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, "$fileName.pdf")  // File name with extension
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")  // MIME type for PDF
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)  // Target directory
        }

        try {
            val uri = resolver?.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            if (uri != null) {
                resolver.openOutputStream(uri)?.use { outputStream ->
                    document.writeTo(outputStream)
                    Toast.makeText(context, "PDF saved successfully!", Toast.LENGTH_LONG).show()
                    showDownloadNotification(uri)  // Show download notification with the URI
                }
            } else {
                Toast.makeText(context, "Failed to create PDF file.", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            document.close()
        }
    }


    private fun checkPermissions() {

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE_2)
        }
    }


}