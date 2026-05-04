package com.empcloud.empmonitor.ui.fragment.task.start_task_3

import android.Manifest
import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency
import com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl
import com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl
import com.empcloud.empmonitor.data.remote.request.update_reschedule.UpdateRescheduleModel
import com.empcloud.empmonitor.data.remote.request.update_task.Tags
import com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel
import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail
import com.empcloud.empmonitor.data.remote.response.task_stage_selection.Data
import com.empcloud.empmonitor.data.remote.response.update_task.UpdateTaskResponse
import com.empcloud.empmonitor.databinding.FragmentStartTaskBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter
import com.empcloud.empmonitor.ui.adapters.TaskStageTagsAdapter
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.ui.listeners.TaskStageTagItemClickListener
import com.empcloud.empmonitor.utils.ActiveTaskTracker
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.empcloud.empmonitor.utils.NativeLib
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Currency

@AndroidEntryPoint
class   StartTaskFragment constructor(private val listener: OnFragmentChangedListener? = null) : Fragment(), OnMapReadyCallback ,TaskStageTagItemClickListener{

    private var selectedCountry: String? = null
    private var code: Int? = null
    private lateinit var binding:FragmentStartTaskBinding

    private lateinit var searchResults: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var placesClient: PlacesClient
    private lateinit var map: GoogleMap
    private lateinit var currentMarker: Marker
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private  var taskName:String? = null
    private  var taskvol:Double? = null
    private  var taskval:Double? = null
    private  var currency:String? = null

    private  var time:String? = null
    private  var address:String? = null
    private var taskId:String? = null
    private var isTaskStarted:Int? = null
    private var clientName:String? = null

    private var lat:String? = null
    private var lon:String? = null

    private lateinit var calendarView: CalendarView
    private var selectedDate: String? = null

    private var popUptimeStart:String? = null
    private var popUpTimeEnd:String? = null

    private var fetchTaskDetail:FetchTaskDetail? = null
    private var backno:Int? = null

    private var startdone:Int? = null
    private var firstclick:Int? = null

    private var buttonClickednumber:Int? = null

    private var isTaskStartedInfo:Boolean = false

    private  var picResponse:String? = null
    private var picDesc:String? = null
    private val viewModel by viewModels<StartTaskViewModel>()


    private var amountCurrency: AmountCurrency? = null
    private var tags:ArrayList<Tags>? = null
    private var taskVolume:Double? = null
    private var taskAmount:Double? = null


    private var pic1:String? = null
    private var pic2:String? = null
    private var pic3:String? = null
    private var pic4:String? = null

    private var tagname:String? = null

    private lateinit var  stageAdapter: TaskStageTagsAdapter


    private var docsList:MutableList<FilesUrl> = mutableListOf()
    private var imagesList:MutableList<ImgaesUrl> = mutableListOf()

    private var IS_PENDING_TASK_RESCHEDULE:Boolean? = false

    companion object {
        fun newInstance() = StartTaskFragment
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backno = arguments?.getInt(Constants.BACKNO)
        startdone = arguments?.getInt(Constants.STARTDONEBACK)
        firstclick = arguments?.getInt(Constants.FIRST_HOME_CLICK)
        IS_PENDING_TASK_RESCHEDULE = arguments?.getBoolean(Constants.IS_PENDING_TASK_RESCHEDULE)

        if (startdone == 25){

            fetchTaskDetail = arguments?.getSerializable(Constants.SELECTED_MODEL_ITEM_3) as FetchTaskDetail
            picResponse = arguments?.getString(Constants.PICRESPONSE_GALLERY)
            picDesc = arguments?.getString(Constants.PIC_DESC_NEW)

        }
        else if (backno == 1){
            fetchTaskDetail = arguments?.getSerializable(Constants.SELECTED_MODEL_ITEM_2) as FetchTaskDetail
        }
        else if (firstclick == 26){

            val bundle = arguments

            taskName = bundle?.getString(Constants.TASK_NAME)
            taskval = bundle?.getDouble(Constants.TASKVALUE1)
            taskvol = bundle?.getDouble(Constants.TASKVOLUME1)
            currency = bundle?.getString(Constants.CURRENCYSELECTION1)

            time = bundle?.getString(Constants.TASK_TIMING)
                address = bundle?.getString(Constants.TASK_ADDRESS)
                isTaskStarted = bundle?.getInt(Constants.ISTASK_STARTED)
                taskId = bundle?.getString(Constants.TASK_ID)
                clientName = bundle?.getString(Constants.CLIENT_NAME_TASK)
                fetchTaskDetail = bundle?.getSerializable(Constants.SELECTED_MODEL_ITEM) as FetchTaskDetail

        }
        else{

            val bundle = arguments
            taskName = bundle?.getString(Constants.TASK_NAME)
            taskval = bundle?.getDouble(Constants.TASKVALUE1)
            taskvol = bundle?.getDouble(Constants.TASKVOLUME1)
            currency = bundle?.getString(Constants.CURRENCYSELECTION1)
            time = bundle?.getString(Constants.TASK_TIMING)
            address = bundle?.getString(Constants.TASK_ADDRESS)
            isTaskStarted = bundle?.getInt(Constants.ISTASK_STARTED)
            taskId = bundle?.getString(Constants.TASK_ID)
            clientName = bundle?.getString(Constants.CLIENT_NAME_TASK)
            fetchTaskDetail = bundle?.getSerializable(Constants.SELECTED_MODEL_ITEM) as FetchTaskDetail
        }

//        Log.d("jdghdfghfs",fetchTaskDetail.toString())


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStartTaskBinding.inflate(layoutInflater,container,false)


        Places.initialize(requireContext(),NativeLib().getGoogleMapApiKey())
        placesClient = Places.createClient(requireContext())

        searchBar = binding.searchBar
        searchResults = binding.searchResults
        searchResults.layoutManager = LinearLayoutManager(requireContext())
        searchResultsAdapter = SearchResultsAdapter(placesClient){ placeId ->
            fetchPlaceDetails(placeId)
        }

        if(searchBar.text.isEmpty()){
            searchResults.visibility = View.GONE
        }

        searchResults.adapter = searchResultsAdapter

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No action needed
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length > 3) {

                    getCurrentLocationAndSearch(s.toString())
                } else {
                    searchResults.visibility = View.GONE
                }
            }
        })

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Log.d("taskstatus",isTaskStarted.toString())
        calendarView = binding.calendarPopUp.calendarView

//        Log.d("statuschekcing","$isTaskStarted")


        binding.customshow.cancelbtn.setOnClickListener {

            binding.customshow.customeAlert.visibility = View.GONE
        }

        binding.customshow.okayBtn.setOnClickListener {

            binding.customshow.customeAlert.visibility = View.GONE
        }

        binding.bottomSheet.reschedulebtn.setOnClickListener {

            binding.calendarPopUp.popUpDateandTime.visibility = View.VISIBLE
        }

        //start btn

        binding.bottomSheet.startTaskbtn.setOnClickListener {

            if (IS_PENDING_TASK_RESCHEDULE!!){

                binding.calendarPopUp.popUpDateandTime.visibility = View.VISIBLE

                val currentDate = Calendar.getInstance().timeInMillis

                // Set the minimum selectable date to the current date
                calendarView.minDate = currentDate
                calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
//                Toast.makeText(requireContext(), "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
                }
            }else{
                val ischeckin = CommonMethods.getSharedPrefernce(requireActivity(),Constants.IS_CHECKEDIN)

                if (ischeckin.equals("YES")){

                    if (CommonMethods.isCurrentDate(fetchTaskDetail!!.start_time)){
                        if (ActiveTaskTracker.activeTaskId == null) {
                            buttonClickednumber = 1
                            updateTaskDetail(1)
                        }
                        if (ActiveTaskTracker.activeTaskId != null){

                            binding.customshow.customeAlert.visibility = View.VISIBLE

                        }
                    } else if(CommonMethods.isDateBeforeCurrentWithTime(fetchTaskDetail!!.start_time)){

                        Toast.makeText(context,"Previous task updation is restricted!",Toast.LENGTH_SHORT).show()
                    }
                    else{

                        Toast.makeText(context,"Future task updation is restricted!",Toast.LENGTH_SHORT).show()
                    }
                }else{

                    val isCheckedOut = CommonMethods.getSharedPrefernce(requireActivity(),Constants.IS_CHECKED_OUT)
                    if (isCheckedOut.equals("YES")) Toast.makeText(context,"Task updation is restricted after checkout.",Toast.LENGTH_SHORT).show()
                    else Toast.makeText(context,"Please check-in to start the task.",Toast.LENGTH_SHORT).show()
                }
            }




        }

        //pause btn

        binding.bottomSheet.pauseBtn.setOnClickListener {

            binding.bottomSheet.pauseBtn.visibility = View.GONE
            binding.bottomSheet.resumsebtn.visibility = View.VISIBLE
            buttonClickednumber = 2
            if (ActiveTaskTracker.activeTaskId == fetchTaskDetail?._id)    ActiveTaskTracker.activeTaskId = null
            updateTaskDetail(2)
        }

        //resume btn

        binding.bottomSheet.resumsebtn.setOnClickListener {

            if (CommonMethods.isCurrentDate(fetchTaskDetail!!.start_time)){

                val isCheckedOut = CommonMethods.getSharedPrefernce(requireActivity(),Constants.IS_CHECKED_OUT)
                if (isCheckedOut.equals("YES"))
                    Toast.makeText(context,"Task updation is restricted after checkout.",Toast.LENGTH_SHORT).show()
                else{

                    if (ActiveTaskTracker.activeTaskId == null) {

                        binding.bottomSheet.pauseBtn.visibility = View.VISIBLE
                        binding.bottomSheet.resumsebtn.visibility = View.GONE
                        buttonClickednumber = 3
                        updateTaskDetail(3)
                    }
                    else if (ActiveTaskTracker.activeTaskId != null){

                        binding.customshow.customeAlert.visibility = View.VISIBLE
                        binding.bottomSheet.pauseBtn.visibility = View.GONE
                        binding.bottomSheet.resumsebtn.visibility = View.VISIBLE
                    }
                }

            } else if(CommonMethods.isDateBeforeCurrentWithTime(fetchTaskDetail!!.start_time)){

                Toast.makeText(context,"Previous task updation is restricted!",Toast.LENGTH_SHORT).show()
            }
            else{

                Toast.makeText(context,"Future task updation is restricted!",Toast.LENGTH_SHORT).show()
            }
        }

        //finish btn

        binding.bottomSheet.finishBtn.setOnClickListener {

            code = 4
           updateTaskDetail(4)

//            ActiveTaskTracker.activeTaskId = null
//            if (code == 200){
//                if (ActiveTaskTracker.activeTaskId == fetchTaskDetail?._id)    {
//
//                    ActiveTaskTracker.activeTaskId = null
//                    CommonMethods.switchFragment(requireActivity(),TaskHomeFragment(listener))
//
//                }
//            }


//
        }


        binding.backbtn.setOnClickListener {

            CommonMethods.switchFragment(requireActivity(),TaskHomeFragment(listener))
        }

        binding.calendarPopUp.cancelbtn.setOnClickListener {
            binding.calendarPopUp.popUpDateandTime.visibility = View.GONE
        }

//        Log.d("tasktstsagds",IS_PENDING_TASK_RESCHEDULE.toString())

        if (isTaskStarted == 0){

            binding.bottomSheet.resumeSection.visibility = View.GONE
            binding.bottomSheet.picUpSection.visibility = View.GONE
            binding.bottomSheet.startTaskbtn.visibility = View.VISIBLE
            if (IS_PENDING_TASK_RESCHEDULE!!) binding.bottomSheet.startbtntxt.text = "Reschedule Task" else binding.bottomSheet.startbtntxt.text = "Start"
        }else if (isTaskStarted == 2){

            if (!IS_PENDING_TASK_RESCHEDULE!!){
                binding.bottomSheet.resumeSection.visibility = View.VISIBLE
                binding.bottomSheet.pauseBtn.visibility = View.GONE
                binding.bottomSheet.resumsebtn.visibility = View.VISIBLE
                binding.bottomSheet.startTaskbtn.visibility = View.GONE
            }else{
                binding.bottomSheet.resumeSection.visibility = View.GONE
                binding.bottomSheet.pauseBtn.visibility = View.GONE
                binding.bottomSheet.resumsebtn.visibility = View.GONE
                binding.bottomSheet.startTaskbtn.visibility = View.GONE
                binding.bottomSheet.finishBtn.visibility = View.GONE
            }

            customizebottomsheet()
        }else if (isTaskStarted == 3){

            binding.bottomSheet.resumeSection.visibility = View.VISIBLE
            binding.bottomSheet.pauseBtn.visibility = View.VISIBLE
            binding.bottomSheet.resumsebtn.visibility = View.GONE
            binding.bottomSheet.startTaskbtn.visibility = View.GONE
            customizebottomsheet()
        }else if (isTaskStarted == 1){


            binding.bottomSheet.resumeSection.visibility = View.VISIBLE
            binding.bottomSheet.pauseBtn.visibility = View.VISIBLE
            binding.bottomSheet.resumsebtn.visibility = View.GONE
            binding.bottomSheet.startTaskbtn.visibility = View.GONE
            customizebottomsheet()

        }else if (isTaskStarted == 4){

            binding.bottomSheet.resumeSection.visibility = View.VISIBLE
            binding.bottomSheet.pauseBtn.visibility = View.GONE
            binding.bottomSheet.resumsebtn.visibility = View.GONE
            binding.bottomSheet.startTaskbtn.visibility = View.GONE
            binding.bottomSheet.finishBtn.visibility = View.GONE
            binding.bottomSheet.buttonSection.visibility = View.GONE
            binding.bottomSheet.infotaskvalue.isEnabled = false
            binding.bottomSheet.infotaskvolume.isEnabled = false
            binding.bottomSheet.stageSelection.isEnabled = false
            binding.bottomSheet.stageSelectionShowinTxt.isEnabled = false
            customizebottomsheet()

        }

//        if(isTaskStarted == 1 && firstclick == 26){
//
//            binding.bottomSheet.resumeSection.visibility = View.VISIBLE
//            binding.bottomSheet.startTaskbtn.visibility = View.GONE
//
//        }else{
//
//            if (firstclick == 27){
//
//                binding.bottomSheet.resumeSection.visibility = View.VISIBLE
//                binding.bottomSheet.pauseBtn.visibility = View.GONE
//                binding.bottomSheet.resumsebtn.visibility = View.VISIBLE
//                binding.bottomSheet.startTaskbtn.visibility = View.GONE
//            }
//        }


//        if(isTaskStarted == 2){
//
//            binding.bottomSheet.resumeSection.visibility = View.GONE
//            binding.bottomSheet.startTaskbtn.visibility = View.VISIBLE
//
//        }

        binding.calendarPopUp.editTextStartTime.setOnClickListener {
            showTimePickerDialogPopUp(1)
        }

        binding.calendarPopUp.editTextEndTime.setOnClickListener {
            showTimePickerDialogPopUp(2)
        }

        selectedDate = CommonMethods.getCurrentDate()

        binding.bottomSheet.reschedulebtn.setOnClickListener {

            binding.calendarPopUp.popUpDateandTime.visibility = View.VISIBLE

            val currentDate = Calendar.getInstance().timeInMillis

            // Set the minimum selectable date to the current date
            calendarView.minDate = currentDate
            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
//                Toast.makeText(requireContext(), "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
            }


        }

        binding.calendarPopUp.okayBtn.setOnClickListener {

            updateValue()
//            Log.d("dateofreschedule","$selectedDate $popUptimeStart $popUpTimeEnd" )
//            val value = AmountCurrency(selectedCountry!!,binding.bottomSheet.infotaskvalue.text.toString())

//            if (selectedCountry.isNullOrEmpty() && binding.bottomSheet.infotaskvalue.text.toString().isNullOrEmpty())  amountCurrency = AmountCurrency(selectedCountry!!,binding.bottomSheet.infotaskvalue.text.toString())
//
//            if (binding.bottomSheet.infotaskvolume.text.toString().isNullOrEmpty()) taskVolume = binding.bottomSheet.infotaskvolume.text.toString()

            if (binding.bottomSheet.infotaskvalue.text.toString().isNotEmpty())  {

                val taskValue = binding.bottomSheet.infotaskvalue.text.toString().toDouble()
                if (taskValue != null && taskValue >= 0.0) amountCurrency = AmountCurrency(selectedCountry!!,taskValue)
                else {

                    Toast.makeText(requireContext(),"Enter task value is not valid",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

            }

            if (binding.bottomSheet.infotaskvolume.text.toString().isNotEmpty()) {


                val taskValueString = binding.bottomSheet.infotaskvolume.text.toString()
                val taskValue = taskValueString.toDoubleOrNull()
                if (taskValue != null && taskValue >= 0.0)   taskVolume = binding.bottomSheet.infotaskvolume.text.toString().toDouble()
                else {

                    Toast.makeText(requireContext(),"Enter task volume is not valid",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val updateRescheduleModel = UpdateRescheduleModel(fetchTaskDetail!!._id,fetchTaskDetail!!.clientId,fetchTaskDetail!!.taskName,"$selectedDate $popUptimeStart:00","$selectedDate $popUpTimeEnd:00",fetchTaskDetail!!.taskDescription,"$selectedDate",amountCurrency,taskVolume,tags!!)

            viewModel.invokeRescheduleTaskCall(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),updateRescheduleModel)
        }

        binding.bottomSheet.addPic.setOnClickListener {

            val fragment = StartTaskPicFragment(listener)
            val args = Bundle().apply {
                putInt(Constants.FRAGMENT_NO,2)
                putSerializable(Constants.SELECTED_MODEL_ITEM_1,fetchTaskDetail)

            }
            fragment.arguments = args
//            Log.d("jdghdfghfs",fetchTaskDetail.toString())
            CommonMethods.switchFragment(requireActivity(),fragment)

        }

        if (backno == 1){

            binding.bottomSheet.taskName.text = fetchTaskDetail!!.taskName
            binding.bottomSheet.taskAddress.text = fetchTaskDetail!!.address1 ?: "Default"
            binding.bottomSheet.timeTask.text = fetchTaskDetail!!.start_time
            binding.bottomSheet.clientNametask.text = fetchTaskDetail!!.clientName
            binding.bottomSheet.resumeSection.visibility = View.VISIBLE
            binding.bottomSheet.startTaskbtn.visibility = View.GONE
            binding.bottomSheet.infotaskvalue.setText(fetchTaskDetail!!.taskVolume.toString())
            binding.bottomSheet.infotaskvolume.setText(fetchTaskDetail!!.value.amount.toString())
//            if (!fetchTaskDetail!!.tagLogs?.isEmpty()!! && !fetchTaskDetail!!.tagLogs?.get(0)?.tagName?.isEmpty()!!) binding.bottomSheet.stageSelectionShowinTxt.setText(
//                fetchTaskDetail!!.tagLogs?.get(0)?.tagName ?: ""
//            )

            if (!fetchTaskDetail?.tagLogs.isNullOrEmpty() && fetchTaskDetail?.tagLogs?.lastOrNull()?.tagName != null) {
                binding.bottomSheet.stageSelectionShowinTxt.setText(
                    fetchTaskDetail?.tagLogs?.lastOrNull()?.tagName ?: "")
            }
            customizebottomsheet()
        }else if (startdone == 25){

            binding.bottomSheet.taskName.text = fetchTaskDetail!!.taskName
            binding.bottomSheet.taskAddress.text = fetchTaskDetail!!.address1 ?: "Default"
            binding.bottomSheet.timeTask.text = fetchTaskDetail!!.start_time
            binding.bottomSheet.clientNametask.text = fetchTaskDetail!!.clientName
            binding.bottomSheet.resumeSection.visibility = View.VISIBLE
            binding.bottomSheet.startTaskbtn.visibility = View.GONE
            binding.bottomSheet.infotaskvalue.setText(fetchTaskDetail!!.taskVolume.toString())
            binding.bottomSheet.infotaskvolume.setText(fetchTaskDetail!!.value.amount.toString())
//            if (!fetchTaskDetail!!.tagLogs?.isEmpty()!! && !fetchTaskDetail!!.tagLogs?.get(0)?.tagName?.isEmpty()!!) binding.bottomSheet.stageSelectionShowinTxt.setText(
//                fetchTaskDetail!!.tagLogs?.get(0)?.tagName ?: ""
//            )

            if (!fetchTaskDetail?.tagLogs.isNullOrEmpty() && fetchTaskDetail?.tagLogs?.lastOrNull()?.tagName != null) {
                binding.bottomSheet.stageSelectionShowinTxt.setText(
                    fetchTaskDetail?.tagLogs?.lastOrNull()?.tagName ?: "")
            }

            customizebottomsheet()
        }
        else{

            binding.bottomSheet.taskName.text = taskName
            binding.bottomSheet.taskAddress.text = address ?: "Default"
            binding.bottomSheet.timeTask.text = time
            binding.bottomSheet.clientNametask.text = clientName
            binding.bottomSheet.infotaskvolume.setText(fetchTaskDetail!!.taskVolume.toString())
            binding.bottomSheet.infotaskvalue.setText(fetchTaskDetail!!.value.amount.toString())
//            Log.d("tagmane",fetchTaskDetail!!.tagLogs.toString())
//            if (!fetchTaskDetail!!.tagLogs?.isEmpty()!! && fetchTaskDetail!!.tagLogs?.get(0)?.tagName != null)
//
//                binding.bottomSheet.stageSelectionShowinTxt.setText(
//                fetchTaskDetail!!.tagLogs?.get(0)?.tagName ?: ""
//            )

            if (!fetchTaskDetail?.tagLogs.isNullOrEmpty() && fetchTaskDetail?.tagLogs?.lastOrNull()?.tagName != null) {
                binding.bottomSheet.stageSelectionShowinTxt.setText(
                    fetchTaskDetail?.tagLogs?.lastOrNull()?.tagName ?: "")
            }
            docsShowing()

        }
//        Log.d("agadg",fetchTaskDetail.toString())

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatebtn()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.calendarPopUp.editTextStartTime.addTextChangedListener(watcher)
        binding.calendarPopUp.editTextEndTime.addTextChangedListener(watcher)


        if (!picResponse.isNullOrEmpty()){

            binding.bottomSheet.addpicinfo.visibility = View.VISIBLE
            binding.bottomSheet.imgtext.text = picResponse

        }

        binding.bottomSheet.cancelbtn.setOnClickListener {

            picResponse = null
            binding.bottomSheet.addpicinfo.visibility = View.GONE
        }

        binding.bottomSheet.addpicinfo.setOnClickListener {

            if (!picResponse.isNullOrEmpty()){

                binding.picpreview.containerpreview.visibility = View.VISIBLE
                Picasso.get().load(picResponse).into(binding.picpreview.picpreview)
            }
        }

        binding.picpreview.cancelbtnpic.setOnClickListener {

            binding.picpreview.containerpreview.visibility = View.GONE
        }

        binding.bottomSheet.firstdocs.setOnClickListener {


            openDocs(fetchTaskDetail!!.files[0].url!!)

        }

        binding.bottomSheet.seconddocs.setOnClickListener {

            openDocs(fetchTaskDetail!!.files[1].url!!)
        }


        binding.bottomSheet.firstcancelbtn.visibility = View.GONE
        binding.bottomSheet.secondimgcancelbtn2.visibility = View.GONE
        binding.bottomSheet.imgthirdcancelbtn.visibility = View.GONE
        binding.bottomSheet.fourthimgcancelbtn2.visibility = View.GONE

        stagSelectionProcess()

        imagePreviewHandling()

        currencySpinner()
        validatebtn()
        zoomToCurrentLocation()
        getUserLocation()
        observeUpdateTaskCall()
        observeUpdateRescheduleCall()
        observeGetTags()
    }



    private fun currencySpinner() {

        val spinner = binding.bottomSheet.currencydrop
        val currency = Currency.getAvailableCurrencies().map { it.currencyCode }.sorted()
        val adapter = ArrayAdapter(requireContext(),R.layout.currencytextlayout,currency)
        spinner.adapter = adapter

        var defaultCurrency = ""

        if (!fetchTaskDetail!!.value.currency.isNullOrEmpty()) {

            defaultCurrency = fetchTaskDetail!!.value.currency!!
            selectedCountry = defaultCurrency
        }
        else  {
            defaultCurrency = "INR"
            selectedCountry = defaultCurrency
        }


        val defaultPosition = currency.indexOf(defaultCurrency)

        if (defaultPosition != -1){

            spinner.setSelection(defaultPosition)
        }

        spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCountry = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun  observeUpdateRescheduleCall(){

        lifecycleScope.launch {
            with(viewModel){
                observeUpdateRescheduleTaskFlow.collect{res ->
                    when(res){
                        is ApiState.SUCESS -> {

                            val it = res.getResponse
                            if(it.statusCode == 200){

                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                                ActiveTaskTracker.activeTaskId = null
                                binding.calendarPopUp.popUpDateandTime.visibility = View.GONE
                                CommonMethods.switchFragment(requireActivity(),TaskHomeFragment(listener))
                            }
                            if (it.statusCode == 400){
                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                                binding.calendarPopUp.popUpDateandTime.visibility  = View.GONE
                            }
                        }

                        is ApiState.ERROR -> {
                            Log.d("TaskFETCHRescedule","ERROR")
                        }
                        ApiState.LOADING -> {
                            Toast.makeText(requireContext(),"Loading", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if (!isTaskStartedInfo) {


            map.uiSettings.isScrollGesturesEnabled = false
            map.uiSettings.isZoomGesturesEnabled = false
            map.uiSettings.isRotateGesturesEnabled = false
            // Move camera to the marker position
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 10f))

            // Add a marker to a default location and move the camera
            val defaultLocation = LatLng(-34.0, 151.0)
            currentMarker = map.addMarker(
                MarkerOptions()
                    .position(defaultLocation)
                    .draggable(true)
                    .title("Selected Location")
            )!!
            currentMarker.isVisible = false
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))


            // Set up camera move listener
            map.setOnCameraMoveListener {
                // Update marker position to the center of the camera
                val center = map.cameraPosition.target
                currentMarker.position = center
            }


            // Set up map click listener
            map.setOnMapClickListener {
                // Hide search results when the map is clicked
                searchResults.visibility = View.GONE
            }
        }
    }
    private var currentLat: Double? = null
    private var currentLng: Double? = null

    private fun getUserLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        currentLat = location.latitude
                        currentLng = location.longitude
                        // Update the adapter with the new location
                        searchResultsAdapter.updateCurrentLocation(currentLat, currentLng)
                    }
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationAndSearch(query: String) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                searchPlaces(query, currentLatLng)
            } else {
                // Handle location being null
            }
        }
    }

    private fun moveMarker(latLng: LatLng) {
        currentMarker.position = latLng
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        searchResults.visibility = View.GONE

    }


    private fun searchPlaces(query: String, currentLatLng: LatLng) {
        val bounds = RectangularBounds.newInstance(
            LatLng(currentLatLng.latitude - 0.1, currentLatLng.longitude - 0.1),
            LatLng(currentLatLng.latitude + 0.1, currentLatLng.longitude + 0.1)
        )

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .setLocationBias(bounds)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                val predictions = response.autocompletePredictions
                searchResultsAdapter.updateData(predictions)
                searchResults.visibility = View.VISIBLE
                enableMapGestures()
            }
            .addOnFailureListener { exception ->
                // Handle failure
                enableMapGestures()
            }
    }

    private fun fetchPlaceDetails(placeId: String) {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            val latLng = place.latLng
            if (latLng != null) {
                moveMarker(latLng)
            }
        }.addOnFailureListener { exception ->
//            Log.e("MainActivity", "Place not found: ${exception.message}")
        }
    }

    private fun zoomToCurrentLocation() {
        if (checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED &&
            checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }

    private fun disableMapGestures() {
        map.uiSettings.isScrollGesturesEnabled = false
        map.uiSettings.isZoomGesturesEnabled = false
    }

    private fun enableMapGestures() {
        map.uiSettings.isScrollGesturesEnabled = true
        map.uiSettings.isZoomGesturesEnabled = true
    }

    private fun observeUpdateTaskCall(){
        lifecycleScope.launch {
            with(viewModel){
                observeUpdateTaskFlow.collect{res ->
                    when(res) {
                        is ApiState.ERROR -> {

                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(requireContext(),"Loading", Toast.LENGTH_SHORT).show()
                        }
                        is ApiState.SUCESS -> {

                            val it = res.getResponse
                            if(it.statusCode == 200){

//                                code = it.statusCode
                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()

                                updateButtons(it)
                                binding.bottomSheet.resumeSection.visibility = View.VISIBLE
                                binding.bottomSheet.startTaskbtn.visibility = View.GONE
                                if (code == 4){
                                    if (ActiveTaskTracker.activeTaskId == fetchTaskDetail?._id)    {

                                        ActiveTaskTracker.activeTaskId = null
                                        CommonMethods.switchFragment(requireActivity(),TaskHomeFragment(listener))

                                    }
                                }
//                                CommonMethods.switchFragment(requireActivity(),TaskHomeFragment(listener))
                            }
                            if(it.statusCode == 400){
//                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
//                                binding.customshow.customeAlert.visibility = View.VISIBLE
                                if (buttonClickednumber == 1) ActiveTaskTracker.activeTaskId = null
                                if (it.body.message.equals("Can't start new task if previous tasks is Pending or Completed!"))
                                {
                                    binding.customshow.customeAlert.visibility = View.VISIBLE
                                    if (buttonClickednumber == 1) ActiveTaskTracker.activeTaskId = null


                                }else if (it.body.message.equals("You have been logged out. Unable to perform the update.")){

                                    Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                                    ActiveTaskTracker.activeTaskId = null

                                } else if (it.body.message.equals("Invalid Task Id or the Task does not exist!")){

                                    ActiveTaskTracker.activeTaskId = null
                                    if (buttonClickednumber == 2) {

                                        binding.bottomSheet.pauseBtn.visibility = View.VISIBLE
                                        binding.bottomSheet.resumsebtn.visibility = View.GONE

                                    }else if (buttonClickednumber == 3){

                                        binding.bottomSheet.pauseBtn.visibility = View.GONE
                                        binding.bottomSheet.resumsebtn.visibility = View.VISIBLE
                                    }
                                }else if (it.body.message.equals("Task updates are restricted for future tasks!")){

                                    ActiveTaskTracker.activeTaskId = null
                                }
                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                }
            }
        }
    }

    private fun updateButtons(it: UpdateTaskResponse) {

        if (buttonClickednumber == 1){

            fetchTaskDetail?.taskApproveStatus = 1
            ActiveTaskTracker.activeTaskId = fetchTaskDetail?._id
            customizebottomsheet()
        }
        else if (buttonClickednumber == 2){

            fetchTaskDetail?.taskApproveStatus = 2
            ActiveTaskTracker.activeTaskId = null
        }
        else if (buttonClickednumber == 3){

            fetchTaskDetail?.taskApproveStatus = 3
            ActiveTaskTracker.activeTaskId = fetchTaskDetail?._id
        }
    }

    private fun updateTaskDetail(statusCode: Int) {

        getLastLocation(statusCode)

    }

    private fun showTimePickerDialogPopUp(i:Int) {
        // Get the current time as the default values for the picker
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        // Create a new instance of TimePickerDialog and show it
        val timePickerDialog = TimePickerDialog(requireContext(),R.style.CustomDatePickerDialogTheme,
            { view, hourOfDay, minute -> // Handle the selected time
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                if(i == 1){
                    binding.calendarPopUp.editTextStartTime.setText("$selectedTime")
                    popUptimeStart = selectedTime
                }
                else if (i == 2) {
                    val startTime = popUptimeStart?.split(":")?.map { it.toInt() }
                    val endTime = listOf(hourOfDay, minute)

                    if (startTime != null && CommonMethods.isEndTimeValid(startTime, endTime)) {
                        binding.calendarPopUp.editTextEndTime.setText(selectedTime)
                        popUpTimeEnd = selectedTime
                    } else {
                        Toast.makeText(requireContext(), "End time should be greater than start time", Toast.LENGTH_SHORT).show()
                    }
                }


            }, hour, minute, true
        )
        timePickerDialog.show()
        timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.buttonbgcolor))
        timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.buttonbgcolor))

    }

    private fun getLastLocation(statusCode: Int) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    lat = latitude.toString()
                    lon = longitude.toString()

//                    val token = CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN)

//                    Toast.makeText(requireContext(), "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_LONG).show()
//                    Log.d("adfafa",fetchTaskDetail!!._id.toString())
//                    Log.d("adfafa",latitude.toString())
//                    Log.d("adfafa",longitude.toString())

                    updateValue()
                    val updateTaskModel = UpdateTaskModel(fetchTaskDetail!!._id,statusCode,CommonMethods.getCurrentDateTime(),latitude.toString(),longitude.toString(),amountCurrency!!,taskVolume!!,tags!!,docsList,imagesList)
                    viewModel.invokeUpdateTaskCall(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),updateTaskModel)

                } ?: run {
                    Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_LONG).show()
            }
    }

    private fun validatebtn(){

        val textfirst = binding.calendarPopUp.editTextStartTime.text.isNotEmpty()
        val textSecond = binding.calendarPopUp.editTextEndTime.text.isNotEmpty()

        binding.calendarPopUp.okayBtn.visibility = if (textfirst && textSecond)
        {

            View.VISIBLE

        }else{
            View.GONE
        }

        binding.calendarPopUp.okayBtndisable.visibility = if (textfirst && textSecond) {
            View.GONE
        } else {
            View.VISIBLE
        }

    }

    private fun customizebottomsheet(){

        val bottomsheet = binding.bottomSheet.root
        val params = bottomsheet.layoutParams as ConstraintLayout.LayoutParams

        params.height = 0
        params.topToTop = binding.cardView4.id
        bottomsheet.layoutParams = params

        binding.bottomSheet.containerSection.setBackgroundResource(R.drawable.taskinfobg)

        binding.bottomSheet.picUpSection.visibility = View.VISIBLE
        binding.bottomSheet.resumeSection.visibility = View.VISIBLE
        binding.bottomSheet.startTaskbtn.visibility = View.GONE
        isTaskStartedInfo = true

        docsShowing()
        imageShowing()
    }

    private fun docsShowing() {

        if (!fetchTaskDetail!!.files.isEmpty()){


            if (fetchTaskDetail!!.files.size > 1){

                val docs1 = fetchTaskDetail!!.files[0].url
                val docs2 = fetchTaskDetail!!.files[1].url

                if (!docs1.isNullOrEmpty()){

                    binding.bottomSheet.docsfiledatashow.visibility = View.VISIBLE
                    binding.bottomSheet.firstdocs.visibility = View.VISIBLE
                    binding.bottomSheet.docstitle.visibility = View.VISIBLE
                    binding.bottomSheet.firstcancelbtn.visibility = View.GONE
                    binding.bottomSheet.doctxt.text = CommonMethods.extractFileNameFromUrl(docs1!!)
                }
                if (!docs2.isNullOrEmpty()){

                    binding.bottomSheet.docsfiledatashow.visibility = View.VISIBLE
                    binding.bottomSheet.seconddocs.visibility = View.VISIBLE
                    binding.bottomSheet.docstitle.visibility = View.VISIBLE
                    binding.bottomSheet.cancelbtn2.visibility = View.GONE
                    binding.bottomSheet.doctxt2.text = CommonMethods.extractFileNameFromUrl(docs2!!)
                }
            }else{

                val docs1 = fetchTaskDetail!!.files[0].url
                if (!docs1.isNullOrEmpty()){

                    binding.bottomSheet.docsfiledatashow.visibility = View.VISIBLE
                    binding.bottomSheet.firstdocs.visibility = View.VISIBLE
                    binding.bottomSheet.docstitle.visibility = View.VISIBLE
                    binding.bottomSheet.firstcancelbtn.visibility = View.GONE
                    binding.bottomSheet.doctxt.text = CommonMethods.extractFileNameFromUrl(docs1!!)
                }
            }

        }

    }
    private fun imageShowing() {

        if (!fetchTaskDetail!!.images.isEmpty()){


            binding.bottomSheet.imgtitle.visibility = View.VISIBLE

            if (fetchTaskDetail!!.images.size == 1  ){

                firstPic()

            }
            else if (fetchTaskDetail!!.images.size == 2  ){

                firstPic()
                secondPic()

            }

            else if (fetchTaskDetail!!.images.size == 3  ){

                firstPic()
                secondPic()
                thirdPic()

            }

            else if (fetchTaskDetail!!.images.size == 4  ){

                firstPic()
                secondPic()
                thirdPic()
                fourthPic()

            }

        }

    }

    private fun fourthPic() {
        val img4 = fetchTaskDetail!!.images[1].url
        if (!img4.isNullOrEmpty()){

            pic4 = img4
            val name = CommonMethods.extractFileNameFromUrl(img4)
            getAndSetData(
                binding.bottomSheet.imageshow,
                binding.bottomSheet.fourthimg,
                img4,
                binding.bottomSheet.foruthhowimg,
                name,
                binding.bottomSheet.fourthimgxt2
            )

        }
    }

    private fun thirdPic() {

        val img3 = fetchTaskDetail!!.images[1].url
        if (!img3.isNullOrEmpty()){

            pic3 = img3
            val name = CommonMethods.extractFileNameFromUrl(img3)
            getAndSetData(
                binding.bottomSheet.imageshow,
                binding.bottomSheet.imgthird,
                img3,
                binding.bottomSheet.thirdimgshow,
                name,
                binding.bottomSheet.imgthirdtxt
            )

        }
    }

    private fun secondPic() {
        val img2 = fetchTaskDetail!!.images[1].url
        if (!img2.isNullOrEmpty()){

            pic2 = img2
            val name = CommonMethods.extractFileNameFromUrl(img2)
            getAndSetData(
                binding.bottomSheet.imageshow,
                binding.bottomSheet.secondimg,
                img2,
                binding.bottomSheet.secshowimg,
                name,
                binding.bottomSheet.secondimgxt2
            )

        }
    }

    private fun firstPic() {
        val img1 = fetchTaskDetail!!.images[0].url
        if (!img1.isNullOrEmpty()){

            pic1 = img1
            val name = CommonMethods.extractFileNameFromUrl(img1)
            getAndSetData(binding.bottomSheet.imageshow, binding.bottomSheet.imgfirst,img1,binding.bottomSheet.firstimgshow,name,binding.bottomSheet.imgfirsttxt)

        }

    }


    private fun getAndSetData(
        imageshow: LinearLayout,
        imgcard: CardView,
        imgurl: String,
        imgshow: ImageView,
        name: String?,
        imgtext: TextView
    ) {

        imageshow.visibility = View.VISIBLE
        imgcard.visibility = View.VISIBLE
        Picasso.get().load(imgurl).into(imgshow)
        imgtext.text = imgurl


    }

    private fun imagePreviewHandling() {

        binding.bottomSheet.fourthimg.setOnClickListener {

            if (!pic4.isNullOrEmpty()){

                binding.picpreview.containerpreview.visibility = View.VISIBLE
                Picasso.get().load(pic4).into(binding.picpreview.picpreview)
            }

        }

        binding.bottomSheet.imgthird.setOnClickListener {

            if (!pic3.isNullOrEmpty()){

                binding.picpreview.containerpreview.visibility = View.VISIBLE
                Picasso.get().load(pic3).into(binding.picpreview.picpreview)
            }

        }

        binding.bottomSheet.secondimg.setOnClickListener {

            if (!pic2.isNullOrEmpty()){

                binding.picpreview.containerpreview.visibility = View.VISIBLE
                Picasso.get().load(pic2).into(binding.picpreview.picpreview)
            }

        }

        binding.bottomSheet.imgfirst.setOnClickListener {

            if (!pic1.isNullOrEmpty()){

                binding.picpreview.containerpreview.visibility = View.VISIBLE
                Picasso.get().load(pic1).into(binding.picpreview.picpreview)
            }

        }

        binding.bottomSheet.firstcancelbtn.setOnClickListener {

            binding.picpreview.containerpreview.visibility = View.GONE

        }

        binding.bottomSheet.secondimgcancelbtn2.setOnClickListener {

            binding.picpreview.containerpreview.visibility = View.GONE

        }

        binding.bottomSheet.imgthirdcancelbtn.setOnClickListener {

            binding.picpreview.containerpreview.visibility = View.GONE

        }

        binding.bottomSheet.fourthimgcancelbtn2.setOnClickListener {

            binding.picpreview.containerpreview.visibility = View.GONE

        }
    }

    private fun stagSelectionProcess() {

        viewModel.invokeGetTagsStage(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN))

        binding.bottomSheet.stageSelection.setOnClickListener {

            if (binding.bottomSheet.stageselectionrecycler.visibility == View.VISIBLE) binding.bottomSheet.stageselectionrecycler.visibility = View.GONE
            else binding.bottomSheet.stageselectionrecycler.visibility = View.VISIBLE
        }

        binding.bottomSheet.stageSelectionShowinTxt.setOnClickListener {

            if (binding.bottomSheet.stageselectionrecycler.visibility == View.VISIBLE) binding.bottomSheet.stageselectionrecycler.visibility = View.GONE
            else binding.bottomSheet.stageselectionrecycler.visibility = View.VISIBLE
        }
    }

    private fun observeGetTags(){
        lifecycleScope.launch {
            with(viewModel){
                observeTagsFlow.collect { res ->
                    when (res) {
                        is ApiState.ERROR -> {}
                        ApiState.LOADING -> {}
                        is ApiState.SUCESS -> {

                            val it = res.getResponse

                            if (it.statusCode == 200){

                                  initRecyclerStage(it.body.data)
//                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                            }

                            if (it.statusCode == 400){

                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()

                            }
                        }
                    }
                }
            }
        }

    }

    private fun initRecyclerStage(data: List<Data>) {

        binding.bottomSheet.stageselectionrecycler.setHasFixedSize(false)
        binding.bottomSheet.stageselectionrecycler.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)

        stageAdapter = TaskStageTagsAdapter(requireContext(),data,this)
        binding.bottomSheet.stageselectionrecycler.adapter = stageAdapter
        stageAdapter.onAttachedToRecyclerView(binding.bottomSheet.stageselectionrecycler)

    }

    override fun onItemClickTags(position: Int, dataTag: Data) {


        binding.bottomSheet.statusiconcard.visibility = View.VISIBLE
        binding.bottomSheet.statusiconcard.setCardBackgroundColor(Color.parseColor(dataTag.color))
        binding.bottomSheet.stageSelectionShowinTxt.setText(dataTag.tagName)
        binding.bottomSheet.stageselectionrecycler.visibility = View.GONE

        tagname = dataTag.tagName

    }

    private fun updateValue(){

        val defaultTaskAmount = fetchTaskDetail!!.value.amount
        val defaultTaskVolume = fetchTaskDetail!!.taskVolume
        val defaultTagName = if (fetchTaskDetail!!.tagLogs != null){
            if (fetchTaskDetail!!.tagLogs!!.size > 0  && !fetchTaskDetail!!.tagLogs?.get(0)?.tagName.isNullOrEmpty()) fetchTaskDetail!!.tagLogs?.get(0)?.tagName else null
        }else null

        taskAmount = if (binding.bottomSheet.infotaskvalue.text.isNullOrEmpty())  defaultTaskAmount else binding.bottomSheet.infotaskvalue.text.toString().toDouble()

        taskVolume = if (binding.bottomSheet.infotaskvolume.text.isNullOrEmpty()) defaultTaskVolume else binding.bottomSheet.infotaskvolume.text.toString().toDouble()

        amountCurrency = AmountCurrency(selectedCountry!!,taskAmount!!)

        tagname = if (binding.bottomSheet.stageSelectionShowinTxt.text.isNullOrEmpty()) defaultTagName else binding.bottomSheet.stageSelectionShowinTxt.text.toString()

        if (tags == null) tags = ArrayList()
        if (!tagname.isNullOrEmpty()) tags!!.add(Tags(tagname!!,CommonMethods.getCurrentDateTime()))

        docsList.add(FilesUrl(""))

        if (picDesc != null && picResponse != null){

            imagesList.add(ImgaesUrl(picResponse!!,picDesc!!))
        }else{
            if (imagesList.size > 0) imagesList.add(ImgaesUrl(fetchTaskDetail?.images?.get(0)?.url!!,fetchTaskDetail?.images?.get(0)?.description!!))
        }

    }

    private fun openDocs(url:String){

        val intent = Intent(Intent.ACTION_VIEW)
        val docsUri = Uri.parse(url)
        intent.data = Uri.parse(url)
        intent.setDataAndType(docsUri,"application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            startActivity(intent)
        }catch (e:Exception){

            e.printStackTrace()
        }

    }

}