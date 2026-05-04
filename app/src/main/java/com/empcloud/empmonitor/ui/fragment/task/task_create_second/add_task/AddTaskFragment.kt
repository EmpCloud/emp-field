package com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency
import com.empcloud.empmonitor.data.remote.request.create_task.CreateTaskModel
import com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl
import com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl
import com.empcloud.empmonitor.data.remote.request.create_task.RecurrenceData
import com.empcloud.empmonitor.databinding.FragmentAddTaskBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.fragment.task.select_client_3.SelectClientFragment
import com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_picture.AddPictureFragment
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Currency
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@AndroidEntryPoint
class AddTaskFragment constructor(private val listener: OnFragmentChangedListener? = null) : Fragment() {


    private var amountCurrency: AmountCurrency? = null
    private var taskVolume:Double? = null
    private var doscUri1: Uri? = null
    private var doscUri2: Uri? = null

    private lateinit var cameraExecutor: ExecutorService
    private var timeRaw: String? = null
    private val selectedStartTime: String? = null
    private lateinit var binding:FragmentAddTaskBinding
    private val viewModel by viewModels<AddTaskViewModel>()
    private  var clientID:String? = null
    private  var clientName:String? = null
    private  var timeStart:String = ""
    private  var timeEnd:String = ""
    private val REQUEST_CODE_PICK_DOCUMENT = 102
    private var picResponse:String? = null
    private var docsResponse:String? = null
    private var docsResponse2:String? = null

    private var picDesc:String? = null
    private var selectedCountry: String? = null
    private var docsList:MutableList<FilesUrl> = mutableListOf()
    private var imagesList:MutableList<ImgaesUrl> = mutableListOf()

    private var savedStartTime:String? = null
    private var savedEndTime:String? = null

    private var selectedFileUri: Uri? = null

    private var pic1:String? = null
    private var pic2:String? = null
    private var pic3:String? = null
    private var pic4:String? = null

    private var descpic1:String? = null
    private var descpic2:String? = null
    private var descpic3:String? = null
    private var descpic4:String? = null

    companion object {
        fun newInstance() = AddTaskFragment
        private const val REQUEST_CODE_PICK_FILE = 1
    }

//    private lateinit var sharedViewModel: AddTaskViewModel

    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            selectDocument()
        } else {
//            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firstTimeOpen = arguments?.getInt(Constants.CHANGED_FRAGMENT_FIRST_TIME)
        if(firstTimeOpen == 1)    clearAllValuesForFirstTime()

        picResponse = arguments?.getString(Constants.PIC_RESPONSE)
        picDesc = arguments?.getString(Constants.PIC_DESC)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddTaskBinding.inflate(layoutInflater,container,false)

        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            requestPermissions()
        } else {
            selectDocument()
        }
        clientID = arguments?.getString(Constants.CLIENT_ID)
//        Log.d("sssssssssss",clientID.toString())
        clientName = arguments?.getString(Constants.CLIENT_NAME_SELECTED)
//        Log.d("sssssssssss",clientName.toString())

        if (clientID.isNullOrEmpty() && clientName.isNullOrEmpty()){
            clientID = arguments?.getString(Constants.CLIENTIT_RESEDN)
            clientName = arguments?.getString(Constants.CLIENT_NAME_RESEND)


//            Log.d("sssssssssss1",clientName.toString())
//
//            Log.d("sssssssssss1",clientID.toString())

        }

        collectPicResponse()

        initPicResponse()

        cameraExecutor = Executors.newSingleThreadExecutor()

        setInitialPreviousData()

        binding.backbtn.setOnClickListener {

            CommonMethods.switchFragment(requireActivity(),TaskHomeFragment(listener))
        }

        binding.selectClient.setText(clientName)

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateForm()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        validateForm()

        binding.docsUpload.setOnClickListener {

            openFilePicker()
        }

        binding.cancelbtn.setOnClickListener {

            clearSelectedFile(1)
        }

        binding.cancelbtn2.setOnClickListener {

            clearSelectedFile(2)
        }

        binding.firstdocs.setOnClickListener {

            if (doscUri1 != null) openDocs(doscUri1)
            else{

                doscUri1 = CommonMethods.getUriFromSharedPref(requireContext(),Constants.DOCS_URI_1)
                openDocs(doscUri1)
            }
        }

        binding.seconddocs.setOnClickListener {

            if (doscUri2 != null) openDocs(doscUri2)
            else{

                doscUri2 = CommonMethods.getUriFromSharedPref(requireContext(),Constants.DOCS_URI_2)
                openDocs(doscUri2)
            }
        }

        binding.taskName.addTextChangedListener(watcher)

        binding.description.addTextChangedListener(watcher)
        binding.timestart.addTextChangedListener(watcher)
        binding.timeEnd.addTextChangedListener(watcher)

        setpic()

        binding.timestart.setOnClickListener {

            showTimePickerDialog(1)

        }

        binding.timeEnd.setOnClickListener {

            showTimePickerDialog(2)

        }

        previewImage()

        previewImageCancel()

        selectClientOption()

        picsAddButton()


        val currentDate = CommonMethods.getCurrentDate()
        val timeF = currentDate + timeRaw

        binding.enablebtn.setOnClickListener {

            if (timeStart.isEmpty()) timeStart = CommonMethods.getSharedPrefernce(requireActivity(),Constants.ADDTASK_TIME)
            if (timeEnd.isEmpty()) timeEnd = CommonMethods.getSharedPrefernce(requireActivity(),Constants.ADDTASK_TIME2)

//
            val starttime = "$currentDate $timeStart:00"
            val endtime = "$currentDate $timeEnd:00"

//            Log.d("agasgsagas","$timeStart $timeEnd")

            if (binding.taskvalue.text.toString().isNotEmpty())  {

                val taskValue = binding.taskvalue.text.toString().toDouble()
                if (taskValue != null && taskValue >= 0.0) amountCurrency = AmountCurrency(selectedCountry!!,taskValue)
                else {

                    Toast.makeText(requireContext(),"Enter task value is not valid",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

            }

            if (binding.taskvolume.text.toString().isNotEmpty()) {


                val taskValueString = binding.taskvolume.text.toString()
                val taskValue = taskValueString.toDoubleOrNull()
                if (taskValue != null && taskValue >= 0.0)   taskVolume = binding.taskvolume.text.toString().toDouble()
                else {

                    Toast.makeText(requireContext(),"Enter task volume is not valid",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            if (!pic1.isNullOrEmpty() && !descpic1.isNullOrEmpty()) addDataToTList(pic1,descpic1)
            if (!pic2.isNullOrEmpty() && !descpic2.isNullOrEmpty()) addDataToTList(pic2,descpic2)
            if (!pic3.isNullOrEmpty() && !descpic3.isNullOrEmpty()) addDataToTList(pic3,descpic3)
            if (!pic4.isNullOrEmpty() && !descpic4.isNullOrEmpty()) addDataToTList(pic4,descpic4)

            if (!docsResponse.isNullOrEmpty()) docsList.add(FilesUrl(docsResponse))
            if (!docsResponse2.isNullOrEmpty()) docsList.add(FilesUrl(docsResponse2))

//            val recurrenceData = RecurrenceData(startDate = null, endDate = null, emptyList())
//            val gson = GsonBuilder()
//                .serializeNulls()
//                .create()
//
//            val jsonRecurrence = gson.toJson(recurrenceData)
//
//            Log.d("Recurrence JSON", jsonRecurrence)

            val createTaskModel = CreateTaskModel(clientID!!,binding.taskName.text.toString(),
                starttime,endtime ,null,null,binding.description.text.toString(),
                docsList,imagesList,amountCurrency,taskVolume,currentDate
            )
            Log.d("Recurrence JSON", createTaskModel.toString())
            viewModel.invokeCreateTask(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),createTaskModel)

        }


        docsUploadingResponse()

        currencySpinner()

        observeCreateTaskCall()

        observeSendPicsCall()

    }

    private fun addDataToTList(picture: String?, descritpion: String?) {

        Log.d("alldatachecing","$picture \n $descritpion")

        val imageurl = ImgaesUrl(picture!!,descritpion!!)
        imagesList.add(imageurl)

    }

    private fun setInitialPreviousData() {

        binding.taskName.setText(CommonMethods.getSharedPrefernce(requireActivity(),Constants.ADDTASK_TASKNAME))
        binding.description.setText(CommonMethods.getSharedPrefernce(requireActivity(),Constants.ADDTASK_TASKDESC))
        binding.taskvolume.setText(CommonMethods.getSharedPrefernceInteger(requireActivity(),Constants.TASK_VOLUME).toString())
        binding.taskvalue.setText(CommonMethods.getSharedPrefernceInteger(requireActivity(),Constants.TASK_VALUE).toString())

//        binding.time.setText(CommonMethods.getSharedPrefernce(requireActivity(),Constants.ADDTASK_TIME))
        binding.timestart.setText(CommonMethods.getSharedPrefernce(requireActivity(),Constants.ADDTASK_TIME))
        binding.timeEnd.setText(CommonMethods.getSharedPrefernce(requireActivity(),Constants.ADDTASK_TIME2))

        binding.selectClient.setText(CommonMethods.getSharedPrefernce(requireActivity(),Constants.ADDTASK_CLIENT_NAME))
        val picres = CommonMethods.getSharedPrefernce(requireActivity(),Constants.ADDTASK_PICRESPONSE)
        val picdesc = CommonMethods.getSharedPrefernce(requireActivity(),Constants.ADDTASK_PICDESCRIPTION)
//        binding.taskName.setText(CommonMethods.getSharedPrefernce(requireActivity(),"name"))



    }

    private fun initPicResponse() {


        val count = CommonMethods.getSharedPrefernceInteger(requireActivity(),Constants.COUNT)

        if (count != 0){

            if (count == 1 && picResponse != null && picDesc != null) {

                pic1 = picResponse
                descpic1 = picDesc
                if (picResponse.isNullOrEmpty() && picDesc.isNullOrEmpty())  {

                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_URL_1,Constants.PICS_URL_1,picResponse!!)
                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_DISC_1,Constants.PICS_DISC_1,picDesc!!)

                }
            }else if(count == 2 && picResponse != null && picDesc != null){

                pic2 = picResponse
                descpic2 = picDesc
                if (picResponse.isNullOrEmpty()){

                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_URL_2,Constants.PICS_URL_2,picResponse!!)
                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_DISC_2,Constants.PICS_DISC_2,picDesc!!)
                }

            }else if(count == 3 && picResponse != null && picDesc != null){


                pic3 = picResponse
                descpic3 = picDesc
                if (picResponse.isNullOrEmpty()) {

                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_URL_3,Constants.PICS_URL_3,picResponse!!)
                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_DISC_3,Constants.PICS_DISC_3,picDesc!!)
                }
            }else{

                if (picResponse != null && picDesc != null){

                    pic4 = picResponse
                    descpic4 = picDesc
                    if (picResponse.isNullOrEmpty() && picDesc.isNullOrEmpty()){

                        CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_URL_4,Constants.PICS_URL_4,picResponse!!)
                        CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_DISC_4,Constants.PICS_DISC_4,picDesc!!)
                    }
                }
            }
        }
    }

    private fun collectPicResponse() {

        pic1 = CommonMethods.getSharedPrefernce(requireActivity(),Constants.PICS_URL_1)
        pic2 = CommonMethods.getSharedPrefernce(requireActivity(),Constants.PICS_URL_2)
        pic3 = CommonMethods.getSharedPrefernce(requireActivity(),Constants.PICS_URL_3)
        pic4 = CommonMethods.getSharedPrefernce(requireActivity(),Constants.PICS_URL_4)

        descpic1 = CommonMethods.getSharedPrefernce(requireActivity(),Constants.PICS_DISC_1)
        descpic2 = CommonMethods.getSharedPrefernce(requireActivity(),Constants.PICS_DISC_2)
        descpic3 = CommonMethods.getSharedPrefernce(requireActivity(),Constants.PICS_DISC_3)
        descpic4 = CommonMethods.getSharedPrefernce(requireActivity(),Constants.PICS_DISC_4)

    }

    private fun selectClientOption() {

        binding.selectClient.setOnClickListener {

            val fragment = SelectClientFragment(listener)
//            val args = Bundle().apply {
//                putString("taskname",binding.taskName.text.toString())
//                putString("time",binding.time.text.toString())
//                putString("desc",binding.description.text.toString())
//                putString("picresponse",picResponse)
//                putString("picdesc",picDesc)
//            }
//            fragment.arguments = args
            CommonMethods.switchFragment(requireActivity(),fragment)
        }

    }

    private fun picsAddButton() {

        binding.picadd.setOnClickListener {

            var count = CommonMethods.getSharedPrefernceInteger(requireActivity(),Constants.COUNT)
            if (count < 4) {

                val fragment = AddPictureFragment(listener)
                val args = Bundle().apply {

                    putInt(Constants.FRAGMENT_NO, 1)
                    putString(Constants.CLIENTIT_SEDN, clientID)
                    putString(Constants.CLIENT_NAME_SEND, clientName)


                }
                fragment.arguments = args
                CommonMethods.switchFragment(requireActivity(), fragment)
            }else{

                Toast.makeText(requireContext(),"Only allow to add 4 images",Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun docsUploadingResponse() {

        docsResponse = CommonMethods.getSharedPrefernce(requireActivity(),Constants.ADDTASK_UPLOADDOSC_1)
        if (!docsResponse.isNullOrEmpty()){
            binding.docsfiledatashow.visibility = View.VISIBLE
            binding.firstdocs.visibility = View.VISIBLE
            binding.doctxt.text = CommonMethods.getSharedPrefernce(requireActivity(),Constants.FILENAME_DOCS_1)
        }

        docsResponse2 = CommonMethods.getSharedPrefernce(requireActivity(),Constants.ADDTASK_UPLOADDOSC_2)
        if (!docsResponse2.isNullOrEmpty()){
            binding.docsfiledatashow.visibility = View.VISIBLE

            binding.seconddocs.visibility = View.VISIBLE
            binding.doctxt2.text = CommonMethods.getSharedPrefernce(requireActivity(),Constants.FILENAME_DOCS_2)
        }
    }


    private fun previewImageCancel() {

        binding.imgpreview.cancelbtnpic.setOnClickListener {

            binding.imgpreview.containerpreview.visibility = View.GONE
        }
    }

    private fun previewImage() {

        binding.imgfirst.setOnClickListener {

            binding.imgpreview.containerpreview.visibility = View.VISIBLE
            if (!pic1.isNullOrEmpty()) Picasso.get().load(pic1).into(binding.imgpreview.picpreview)

        }

        binding.secondimg.setOnClickListener {

            binding.imgpreview.containerpreview.visibility = View.VISIBLE
            if (!pic2.isNullOrEmpty()) Picasso.get().load(pic2).into(binding.imgpreview.picpreview)

        }

        binding.imgthird.setOnClickListener {

            binding.imgpreview.containerpreview.visibility = View.VISIBLE
            if (!pic3.isNullOrEmpty()) Picasso.get().load(pic3).into(binding.imgpreview.picpreview)

        }
        binding.fourthimg.setOnClickListener {

            binding.imgpreview.containerpreview.visibility = View.VISIBLE
            if (!pic4.isNullOrEmpty()) Picasso.get().load(pic4).into(binding.imgpreview.picpreview)

        }
    }

    private fun decreaseCountValue(){

        var count = CommonMethods.getSharedPrefernceInteger(requireActivity(),Constants.COUNT)
        count -= 1
        Log.d("coutnvaluedec",count.toString())
        CommonMethods.saveSharedPrefernceInteger(requireActivity(),Constants.COUNT,Constants.COUNT,count)
    }

    private fun setpic() {

        if (!pic1.isNullOrEmpty()){

            binding.imageshow.visibility = View.VISIBLE
            binding.imgfirst.visibility = View.VISIBLE
            Picasso.get().load(pic1).into(binding.firstimgshow)
            binding.imgfirsttxt.text = pic1
        }

        binding.imgfirstcancelbtn.setOnClickListener {

            decreaseCountValue()
            pic1 = null
            descpic1 = null
            binding.imgfirst.visibility = View.GONE
            CommonMethods.clearAllValues(requireContext(),Constants.PICS_URL_1)
            CommonMethods.clearAllValues(requireContext(),Constants.PICS_DISC_1)
            shiftImagesLeft()

            if (pic2.isNullOrEmpty() && pic3.isNullOrEmpty() && pic4.isNullOrEmpty())  binding.imageshow.visibility = View.GONE
        }

        if (!pic2.isNullOrEmpty()){


            binding.imageshow.visibility = View.VISIBLE
            binding.secondimg.visibility = View.VISIBLE
            Picasso.get().load(pic2).into(binding.secshowimg)
            binding.secondimgxt2.text = pic2
        }

        binding.secondimgcancelbtn2.setOnClickListener {

            decreaseCountValue()
            pic2 = null
            descpic2 = null
            binding.secondimg.visibility = View.GONE

            CommonMethods.clearAllValues(requireContext(),Constants.PICS_URL_2)
            CommonMethods.clearAllValues(requireContext(),Constants.PICS_DISC_2)
            shiftImagesLeft()
            if (pic1.isNullOrEmpty() &&  pic3.isNullOrEmpty() && pic4.isNullOrEmpty())  {

                binding.imageshow.visibility = View.GONE
            }
        }

        if (!pic3.isNullOrEmpty()){


            binding.imageshow.visibility = View.VISIBLE
            binding.imgthird.visibility = View.VISIBLE
            binding.fourthimg.visibility = View.INVISIBLE
            Picasso.get().load(pic3).into(binding.thirdimgshow)
            binding.imgthirdtxt.text = pic3
        }

        binding.imgthirdcancelbtn.setOnClickListener {

            decreaseCountValue()
            pic3 = null
            descpic3 = null
            binding.imgthird.visibility = View.GONE
            CommonMethods.clearAllValues(requireContext(),Constants.PICS_URL_3)
            CommonMethods.clearAllValues(requireContext(),Constants.PICS_DISC_3)
            shiftImagesLeft()
            if (pic1.isNullOrEmpty() && pic2.isNullOrEmpty() &&  pic4.isNullOrEmpty())   binding.imageshow.visibility = View.GONE

        }

        if (!pic4.isNullOrEmpty()){


            binding.imageshow.visibility = View.VISIBLE
            binding.fourthimg.visibility = View.VISIBLE
            Picasso.get().load(pic4).into(binding.foruthhowimg)
            binding.fourthimgxt2.text = pic4
        }

        binding.fourthimgcancelbtn2.setOnClickListener {

            decreaseCountValue()

            pic4 = null
            descpic4 = null
            binding.fourthimg.visibility = View.GONE

            CommonMethods.clearAllValues(requireContext(),Constants.PICS_URL_4)
            CommonMethods.clearAllValues(requireContext(),Constants.PICS_DISC_4)

            if (pic1.isNullOrEmpty() && pic2.isNullOrEmpty() && pic3.isNullOrEmpty() )  binding.imageshow.visibility = View.GONE


        }
    }


    private fun openDocs(doscUri: Uri?) {

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(doscUri,"application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            startActivity(intent)
        }catch (e:Exception){

            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        CommonMethods.saveSharedPrefernce(requireActivity(),Constants.ADDTASK_TASKNAME,Constants.ADDTASK_TASKNAME,binding.taskName.text.toString())
        if (!binding.taskvolume.text.isNullOrEmpty())         CommonMethods.saveSharedPrefernceInteger(requireActivity(),Constants.TASK_VOLUME,Constants.TASK_VOLUME,binding.taskvolume.text.toString().toInt())
        if (!binding.taskvalue.text.isNullOrEmpty())         CommonMethods.saveSharedPrefernceInteger(requireActivity(),Constants.TASK_VALUE,Constants.TASK_VALUE,binding.taskvalue.text.toString().toInt())

        if (!clientID.isNullOrEmpty())     CommonMethods.saveSharedPrefernce(requireActivity(),Constants.ADDTASK_CLIENTIT,Constants.ADDTASK_CLIENTIT,clientID!!)
        if (!clientName.isNullOrEmpty())     CommonMethods.saveSharedPrefernce(requireActivity(),Constants.ADDTASK_CLIENT_NAME,Constants.ADDTASK_CLIENT_NAME,clientName!!)
        CommonMethods.saveSharedPrefernce(requireActivity(),Constants.ADDTASK_TASKDESC,Constants.ADDTASK_TASKDESC,binding.description.text.toString())
        CommonMethods.saveSharedPrefernce(requireActivity(),Constants.ADDTASK_TIME,Constants.ADDTASK_TIME,binding.timestart.text.toString())
        CommonMethods.saveSharedPrefernce(requireActivity(),Constants.ADDTASK_TIME2,Constants.ADDTASK_TIME2,binding.timeEnd.text.toString())

        if (picResponse != null && picDesc != null){
            CommonMethods.saveSharedPrefernce(requireActivity(),Constants.ADDTASK_PICRESPONSE,Constants.ADDTASK_PICRESPONSE,picResponse!!)
            CommonMethods.saveSharedPrefernce(requireActivity(),Constants.ADDTASK_PICDESCRIPTION,Constants.ADDTASK_PICDESCRIPTION,picDesc!!)
        }

        updateSharedPrefData()


    }


    private fun  observeCreateTaskCall(){

        lifecycleScope.launch {
            Log.d("issueccesornot","insideobserver")
            with(viewModel){
                observeCreateTaskCall.collect{res ->
                    when(res){
                        is ApiState.SUCESS -> {

                            val it = res.getResponse
//                            Log.d("issueccesornot",it.toString())

                            if(it.statusCode == 200){

//                                Log.d("issueccesornot",it.toString())
                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                                CommonMethods.switchFragment(requireActivity(),TaskHomeFragment(listener))

                            }
                            if (it.statusCode == 400){

                                Log.d("issueccesornot","yes 400")
                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                            }
                        }

                        is ApiState.ERROR -> {
                            Log.d("TaskFETCH","ERROR")
                        }
                        ApiState.LOADING -> {
//                            Toast.makeText(requireContext(),"Loading", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
    }
    private fun validateForm() {
        val isEditText1Filled = binding.taskName.text.toString().isNotEmpty()
        val isEditText2Filled = binding.description.text.toString().isNotEmpty()
        val isEditText3Filled = binding.selectClient.text.toString().isNotEmpty()
        val isEditText4Filled = binding.timestart.text.toString().isNotEmpty()
        val isEditText5Filled = binding.timeEnd.text.toString().isNotEmpty()

        binding.enablebtn.visibility = if (isEditText1Filled && isEditText2Filled && isEditText3Filled && isEditText4Filled && isEditText5Filled)
        {
            View.VISIBLE

        } else {
            View.GONE
        }

        binding.disablebtn.visibility = if (isEditText1Filled && isEditText2Filled && isEditText3Filled && isEditText4Filled && isEditText5Filled) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

//    private fun showTimePickerDialog() {
//        val calendar = Calendar.getInstance()
//        val hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)
//
//        val timePickerDialog = TimePickerDialog(
//            requireActivity(),R.style.CustomDatePickerDialogTheme,
//            TimePickerDialog.OnTimeSetListener { _, startHour, startMinute ->
//
//                val endCalendar = Calendar.getInstance().apply {
//                    set(Calendar.HOUR_OF_DAY, startHour)
//                    set(Calendar.MINUTE, startMinute)
//                    add(Calendar.HOUR_OF_DAY, 1) // Add 1 hour to start time to get end time
//                }
//
//                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
//                timeRaw = timeFormat.format(calendar.time)
//
//
//                val startTime = formatTime(startHour, startMinute)
//                val endTime = formatTime(endCalendar.get(Calendar.HOUR_OF_DAY), endCalendar.get(Calendar.MINUTE))
//
//                timeStart = startTime
//                timeEnd = endTime
//
////                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.TASK_TIME_SCHEDULE)
////                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.TASK_TIME_SCHEDULE2)
////
////                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.TASK_TIME_SCHEDULE,Constants.TASK_TIME_SCHEDULE,timeStart!!)
////                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.TASK_TIME_SCHEDULE2,Constants.TASK_TIME_SCHEDULE2,timeEnd!!)
//
//                // Set the time range in EditText
//                binding.time.setText("$startTime -$endTime")
//            },
//            hour,
//            minute,
//            false // Use 12-hour format (AM/PM)
//        )
//
//        timePickerDialog.show()
//        timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.calendarBtn))
//        timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.calendarBtn));
//    }
//
//    private fun formatTime(hour: Int, minute: Int): String {
//        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
//        val calendar = Calendar.getInstance().apply {
//            set(Calendar.HOUR_OF_DAY, hour)
//            set(Calendar.MINUTE, minute)
//        }
//        return timeFormat.format(calendar.time)
//    }

//    private fun showTimePickerDialog(i: Int) {
//        val calendar = Calendar.getInstance()
//        val hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)
//
//        val timePickerDialog = TimePickerDialog(
//            requireActivity(),
//            R.style.CustomDatePickerDialogTheme,
//            TimePickerDialog.OnTimeSetListener { _, startHour, startMinute ->
//
//                val endCalendar = Calendar.getInstance().apply {
//                    set(Calendar.HOUR_OF_DAY, startHour)
//                    set(Calendar.MINUTE, startMinute)
//                    add(Calendar.HOUR_OF_DAY, 1) // Add 1 hour to start time to get end time
//                }
//
//                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
//                val timeRaw = timeFormat.format(endCalendar.time) // Use endCalendar here
//
//                val startTime = formatTime(startHour, startMinute)
//                val endTime = formatTime(endCalendar.get(Calendar.HOUR_OF_DAY), endCalendar.get(Calendar.MINUTE))
//
//                timeStart = formatTime(startHour, startMinute)
//                timeEnd = formatTime(endCalendar.get(Calendar.HOUR_OF_DAY), endCalendar.get(Calendar.MINUTE))
//
//                // Save the start and end times in shared preferences if needed
//                // CommonMethods.clearStringFromSharedPreferences(requireContext(), Constants.TASK_TIME_SCHEDULE)
//                // CommonMethods.clearStringFromSharedPreferences(requireContext(), Constants.TASK_TIME_SCHEDULE2)
//                // CommonMethods.saveSharedPrefernce(requireActivity(), Constants.TASK_TIME_SCHEDULE, Constants.TASK_TIME_SCHEDULE, timeStart!!)
//                // CommonMethods.saveSharedPrefernce(requireActivity(), Constants.TASK_TIME_SCHEDULE2, Constants.TASK_TIME_SCHEDULE2, timeEnd!!)
//
//                // Set the time range in EditText
////                binding.time.setText("$startTime - $endTime")
//            },
//            hour,
//            minute,
//            false // Use 12-hour format (AM/PM)
//        )
//
//        timePickerDialog.show()
//        timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.calendarBtn))
//        timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.calendarBtn))
//    }
//
//    private fun formatTime(hour: Int, minute: Int): String {
//        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
//        val calendar = Calendar.getInstance().apply {
//            set(Calendar.HOUR_OF_DAY, hour)
//            set(Calendar.MINUTE, minute)
//        }
//        return timeFormat.format(calendar.time)
//    }


    private fun selectDocument() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
//        pickDocumentLauncher.launch(intent)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*" // Allow all file types
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation"))
        }
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == Activity.RESULT_OK) {
//            data?.data?.also { uri ->
//                selectedFileUri = uri
//                handleFileUri(uri)
//            }

            data?.let {

                val clipData = it.clipData
                if (clipData != null){
                    if (clipData.itemCount > 2){

                        Toast.makeText(requireContext(),"Please select only 2 pdf",Toast.LENGTH_SHORT).show()
                    }else{

                        val pdfUris = mutableListOf<Uri>()
                        for (i in 0 until clipData.itemCount){

                            val uri = clipData.getItemAt(i).uri
                            pdfUris.add(uri)
                            selectedFileUri = uri
                            handleFileUri(uri,i)
                        }

                    }
                }else{
                    val uri = it.data
                    if (uri != null) {
                        handleFileUri(uri,0)
                    }
                }
            }
        }
    }

//    private fun handleFileUri(uri: Uri) {
//        // Example: Get file name and display it
//        val fileName = getFileName(uri)
//        binding.docsfiledatashow.visibility = View.VISIBLE
//        binding.doctxt.text = fileName
//
//
//        val filePath = getFilePathFromUri(requireContext(),uri)
//        // You can also upload the file or read its content here
//        lifecycleScope.launch {
//            val file = File(filePath!!)
//            if (file.exists()){
//                val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//                val filePart = MultipartBody.Part.createFormData("files", file.name, requestFile)
//
//                viewModel.invokeSendPicsCall(
//                    CommonMethods.getSharedPrefernce(requireActivity(), Constants.AUTH_TOKEN),
//                    filePart
//                )
//            }else{
//                Toast.makeText(requireContext(),"File Not Found",Toast.LENGTH_SHORT).show()
//            }
//
//        }
//
//    }

    private fun handleFileUri(uri: Uri, i: Int) {
        // Example: Get file name and display it
        val fileName = getFileName(uri)
        binding.docsfiledatashow.visibility = View.VISIBLE
        if (i == 0){

            doscUri1 = uri
            binding.firstdocs.visibility = View.VISIBLE
            binding.doctxt.text = fileName
            CommonMethods.saveSharedPrefernce(requireActivity(),Constants.FILENAME_DOCS_1,Constants.FILENAME_DOCS_1,fileName.toString())
            CommonMethods.saveUriToSharedPref(requireContext(),Constants.DOCS_URI_1,uri)

        }else if (i == 1){

            doscUri2 = uri
            binding.seconddocs.visibility = View.VISIBLE
            binding.doctxt2.text = fileName
            CommonMethods.saveSharedPrefernce(requireActivity(),Constants.FILENAME_DOCS_2,Constants.FILENAME_DOCS_2,fileName.toString())

            CommonMethods.saveUriToSharedPref(requireContext(),Constants.DOCS_URI_2,uri)

        }


        // Launch a coroutine in lifecycle scope
        lifecycleScope.launch {
            try {
                // Open an input stream from the Uri
                requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                    // Use Okio to read the stream and create a request body
                    val requestFile = inputStream.use { input ->
                        input.readBytes().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    }

                    // Create MultipartBody.Part for Retrofit upload
                    val filePart = MultipartBody.Part.createFormData("files", fileName, requestFile)

                    // Example of ViewModel method to handle API call
                    viewModel.invokeSendPicsCall(
                        CommonMethods.getSharedPrefernce(requireActivity(), Constants.AUTH_TOKEN),
                        filePart
                    )
                } ?: run {
                    Toast.makeText(requireContext(), "Failed to read file", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error handling file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    fileName = it.getString(displayNameIndex)
                }
            }
        }
        return fileName
    }

    private fun clearSelectedFile(i: Int) {

        if (i == 1){

            binding.firstdocs.visibility = View.GONE
            if (doscUri2 == null) binding.docsfiledatashow.visibility = View.GONE
            CommonMethods.clearAllValues(requireContext(),Constants.ADDTASK_UPLOADDOSC_1)
            docsResponse = null
            doscUri1 = null
        }else if (i == 2){

            binding.seconddocs.visibility = View.GONE
            doscUri2 = null
            if (doscUri1 == null) binding.docsfiledatashow.visibility = View.GONE
            CommonMethods.clearAllValues(requireContext(),Constants.ADDTASK_UPLOADDOSC_2)
            docsResponse2 = null
        }

        selectedFileUri = null
//        binding.docsfiledatashow.visibility = View.GONE
    }

    private fun observeSendPicsCall() {

        Log.d("ADFASDGASD", "insdie")
        lifecycleScope.launch {
            with(viewModel) {
                observeSendPicsDataCall.collect { res ->
                    when (res) {
                        is ApiState.ERROR -> {
                            Log.d("insideAddTASK", "ERROR")
                        }

                        ApiState.LOADING -> {
                            Log.d("insideAddTASK", "LOADING")
                        }

                        is ApiState.SUCESS -> {

                            val it = res.getResponse
                            if (it.code == 200) {

                                Toast.makeText(
                                    requireContext(), it.data.filesUrls[0].message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                if (docsResponse.isNullOrEmpty()) {

                                    docsResponse = it.data.filesUrls[0].url
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.ADDTASK_UPLOADDOSC_1,Constants.ADDTASK_UPLOADDOSC_1,docsResponse!!)
                                }else if(docsResponse2.isNullOrEmpty()){

                                    docsResponse2 = it.data.filesUrls[0].url
                                    CommonMethods.saveSharedPrefernce(requireActivity(),Constants.ADDTASK_UPLOADDOSC_2,Constants.ADDTASK_UPLOADDOSC_2,docsResponse2!!)

                                }
//                                docsList.add(FilesUrl(docsResponse))
                            }
                            if (it.code == 400) {

                                Toast.makeText(
                                    requireContext(), it.data.filesUrls[0].message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

                    }
                }
            }
        }
    }

    private fun showTimePickerDialog(i: Int) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireActivity(),
            R.style.CustomDatePickerDialogTheme,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->

                val endCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    add(Calendar.HOUR_OF_DAY, 1) // Add 1 hour to start time to get end time
                }

                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val timeRaw = timeFormat.format(endCalendar.time) // Use endCalendar here

                if (i == 1) {
                    val startTime = formatTime(selectedHour, selectedMinute)
                    timeStart = startTime
                    binding.timestart.setText(timeStart)

                    // Optionally save the start time in shared preferences
                    // CommonMethods.saveSharedPrefernce(requireActivity(), Constants.TASK_TIME_SCHEDULE, Constants.TASK_TIME_SCHEDULE, timeStart!!)

                    // Update the EditText or other UI elements
                    // binding.startTimeEditText.setText(startTime)
                }
                else if (i == 2) {
                    // End time selected


                    if (!timeStart.isNullOrEmpty()) {

                        val selectedTime = formatTime(selectedHour, selectedMinute)
                        val startTime = timeStart?.split(":")?.map { it.toInt() }
                        val endTime = listOf(selectedHour, selectedMinute)

                        if (CommonMethods.isEndTimeValid(startTime!!, endTime)){

                            timeEnd = selectedTime
                            binding.timeEnd.setText(timeEnd)
                        }else {
                            Toast.makeText(requireContext(), "End time should be greater than start time", Toast.LENGTH_SHORT).show()
//                        binding.timeEnd.setText("")
                        }

                    } else{
                        Toast.makeText(requireContext(), "Please select start time", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            hour,
            minute,
            false // Use 12-hour format (AM/PM)
        )

        timePickerDialog.show()
        timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.calendarBtn))
        timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.calendarBtn))
    }

    private fun formatTime(hour: Int, minute: Int): String {
        return String.format("%02d:%02d", hour, minute)
    }


    private fun currencySpinner() {

        val spinner = binding.currencydrop
        val currency = Currency.getAvailableCurrencies().map { it.currencyCode }.sorted()
        val adapter = ArrayAdapter(requireContext(),R.layout.currencytextlayout,currency)
        spinner.adapter = adapter

        val getCurrency = CommonMethods.getSharedPrefernce(requireActivity(),Constants.CURRENCY_SELECTION)
        if (getCurrency.isNullOrEmpty()){
            val defaultCurrency = "INR"
            val defaultPosition = currency.indexOf(defaultCurrency)

            if (defaultPosition != -1){

                spinner.setSelection(defaultPosition)
            }
        }else{

            val defaultPosition = currency.indexOf(getCurrency)
            if (defaultPosition != -1){

                spinner.setSelection(defaultPosition)
            }
        }


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCountry = parent?.getItemAtPosition(position).toString()
                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.CURRENCY_SELECTION,Constants.CURRENCY_SELECTION,selectedCountry!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    fun shiftImagesLeft() {
        if (pic1 == null && descpic1 == null) {
            pic1 = pic2
            pic2 = pic3
            pic3 = pic4
            pic4 = null

            descpic1 = descpic2
            descpic2 = descpic3
            descpic3 = descpic4
            descpic4 = null
            updatePref()

        }
        else if (pic2 == null && descpic2 == null) {
            pic2 = pic3
            pic3 = pic4
            pic4 = null

            descpic2 = descpic3
            descpic3 = descpic4
            descpic4 = null
            updatePref()
        }
        else if (pic3 == null && descpic3 == null) {
            pic3 = pic4
            pic4 = null

            descpic3 = descpic4
            descpic4 = null
            updatePref()
        }

    }

    fun updatePref(){

        clearAllValuesOFPref()
        updateSharedPrefData()
        collectPicResponse()
        setpic()
    }

    fun clearAllValuesOFPref(){

        CommonMethods.clearAllValues(requireContext(),Constants.PICS_URL_1)
        CommonMethods.clearAllValues(requireContext(),Constants.PICS_URL_2)
        CommonMethods.clearAllValues(requireContext(),Constants.PICS_URL_3)
        CommonMethods.clearAllValues(requireContext(),Constants.PICS_URL_4)

        CommonMethods.clearAllValues(requireContext(),Constants.PICS_DISC_1)
        CommonMethods.clearAllValues(requireContext(),Constants.PICS_DISC_2)
        CommonMethods.clearAllValues(requireContext(),Constants.PICS_DISC_3)
        CommonMethods.clearAllValues(requireContext(),Constants.PICS_DISC_4)

    }

    fun updateSharedPrefData(){

        if (!pic1.isNullOrEmpty()) CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_URL_1,Constants.PICS_URL_1,pic1!!)
        if (!pic2.isNullOrEmpty()) CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_URL_2,Constants.PICS_URL_2,pic2!!)
        if (!pic3.isNullOrEmpty()) CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_URL_3,Constants.PICS_URL_3,pic3!!)
        if (!pic4.isNullOrEmpty()) CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_URL_4,Constants.PICS_URL_4,pic4!!)


        if (!descpic1.isNullOrEmpty()) CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_DISC_1,Constants.PICS_DISC_1,descpic1!!)
        if (!descpic2.isNullOrEmpty()) CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_DISC_2,Constants.PICS_DISC_2,descpic2!!)
        if (!descpic3.isNullOrEmpty()) CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_DISC_3,Constants.PICS_DISC_3,descpic3!!)
        if (!descpic4.isNullOrEmpty()) CommonMethods.saveSharedPrefernce(requireActivity(),Constants.PICS_DISC_4,Constants.PICS_DISC_4,descpic4!!)
    }

    private fun clearAllValuesForFirstTime() {

        CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.ADDTASK_TASKNAME)
        CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.ADDTASK_TIME)
        CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.ADDTASK_TIME2)
        CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.ADDTASK_TASKDESC)
        CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.ADDTASK_UPLOADDOSC_1)
        CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.ADDTASK_UPLOADDOSC_2)
        CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.ADDTASK_PICRESPONSE)
        CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.ADDTASK_PICDESCRIPTION)
        CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.TASK_VALUE)
        CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.TASK_VOLUME)
        CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.CURRENCY_SELECTION)
        CommonMethods.clearAllValues(requireContext(),Constants.PICS_URL_1)
        CommonMethods.clearAllValues(requireContext(),Constants.PICS_URL_2)
        CommonMethods.clearAllValues(requireContext(),Constants.PICS_URL_3)
        CommonMethods.clearAllValues(requireContext(),Constants.PICS_URL_4)
        CommonMethods.clearAllValues(requireContext(),Constants.PICS_DISC_1)
        CommonMethods.clearAllValues(requireContext(),Constants.PICS_DISC_2)
        CommonMethods.clearAllValues(requireContext(),Constants.PICS_DISC_3)
        CommonMethods.clearAllValues(requireContext(),Constants.PICS_DISC_4)

        CommonMethods.clearAllValues(requireContext(),Constants.COUNT)
    }
}