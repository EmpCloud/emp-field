package com.empcloud.empmonitor.ui.fragment.task.task_first

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency
import com.empcloud.empmonitor.data.remote.request.filter_task.FilteerTaskModel
import com.empcloud.empmonitor.data.remote.request.update_reschedule.UpdateRescheduleModel
import com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel
import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail
import com.empcloud.empmonitor.databinding.FragmentTaskHomeBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.ui.adapters.TaskHomeRecyclerAdapter
import com.empcloud.empmonitor.ui.fragment.task.start_task_3.StartTaskFragment
import com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task.AddTaskFragment
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.ui.listeners.TaskHomeRecyclerItemClickListener
import com.empcloud.empmonitor.utils.ActiveTaskTracker
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class TaskHomeFragment constructor(private val listener: OnFragmentChangedListener? = null) : Fragment(),TaskHomeRecyclerItemClickListener {

    private var tagSaved: Int = -1
    private lateinit var binding:FragmentTaskHomeBinding
    private val viewModel by viewModels<TaskHomeViewModel>()
    var taskList:MutableList<FetchTaskDetail>? = null
    private lateinit var adapterTaskFetch:TaskHomeRecyclerAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var calendar: Calendar = Calendar.getInstance()
    private lateinit var calendarView: CalendarView
    private var selectedDate: String? = null
    private var lat:String? = null
    private var lon:String? = null
    private var dateTxt: String = ""
    private var status:Int = -1

    private  var isSwipedNo:Int? = null
    private  var swipeId:String? = null


    private var taskStatus:Int? = null
    private var date:String? = null

    private var isPendingTaskNotification:Boolean?  = false
    private var popUptimeStart:String? = null
    private var popUpTimeEnd:String? = null

    private var isApiSucces:Boolean = false
    companion object {
        fun newInstance() = TaskHomeFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        date = arguments?.getString(Constants.ITEM_DATE)

        taskStatus = arguments?.getInt(Constants.TASK_STATUS)
        Log.d("GetDate","$taskStatus")
        isPendingTaskNotification = arguments?.getBoolean(Constants.IS_PENDING_TASK)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            getLastLocation()
        }

//        taskStatus = arguments?.getInt(Constants.TASK_STATUS)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTaskHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (taskStatus != null){
//            val filteerTaskModel = FilteerTaskModel(CommonMethods.getCurrentDate(),taskStatus!!)
//            viewModel.invokeFilterTask(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),filteerTaskModel)
            Log.d("GetDate","$taskStatus")
            if (taskStatus == 1){
                binding.current.isChecked = true
                binding.finished.isChecked = false
                binding.all.isChecked = false
                callApiWithDate(CommonMethods.getCurrentDate(),taskStatus!!)
            }else if (taskStatus == 4){
                binding.finished.isChecked = true
                binding.all.isChecked = false
                binding.current.isChecked = false
                callApiWithDate(CommonMethods.getCurrentDate(), taskStatus!!)

            }else{
                binding.all.isChecked = true
                binding.current.isChecked = false
                binding.finished.isChecked = false
                callApiWithDate(date!!, taskStatus!!)
            }
        }else{

            Log.d("InsideElse","inside else")
            if (!date.isNullOrEmpty()) callApiWithDate(date!!, taskStatus ?: 0)
            else  callApiWithDate(CommonMethods.getCurrentDate(), taskStatus ?: 0)

            binding.all.isChecked = true
            binding.current.isChecked = false
            binding.finished.isChecked = false

        }

        val sp = requireContext().getSharedPreferences(Constants.AUTH_TOKEN,AppCompatActivity.MODE_PRIVATE)
        val token = sp.getString(Constants.AUTH_TOKEN,"")


        calendarView = binding.filterCalendar.calendarView

//        val (currentMonth,currentDate) = CommonMethods.getCurrentMonthAndDate()
//        binding.dateSelection.text = "${currentMonth.toUpperCase()} \n$currentDate"

        binding.filtericonbtn.setOnClickListener {

            if(binding.options.visibility == View.GONE){
                binding.options.visibility = View.VISIBLE
            } else{
                binding.options.visibility = View.GONE
            }
        }

        binding.all.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                filterApiCall(0)
                status = 0
                binding.current.isChecked = false
                binding.finished.isChecked = false
                binding.options.visibility = View.GONE
            }
        }

        binding.current.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                filterApiCall(1)
                status = 1
                binding.all.isChecked = false
                binding.finished.isChecked = false
                binding.options.visibility = View.GONE
            }
        }

        binding.finished.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                filterApiCall(4)
                status = 4
                binding.all.isChecked = false
                binding.current.isChecked = false
                binding.options.visibility = View.GONE
            }
        }

        binding.backbtn.setOnClickListener {

            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
//            CommonMethods.switchFragment(requireActivity(),HomeFragment(listener))
        }



        binding.dateSelection.setOnClickListener {

            binding.filterCalendar.popUpDateandTime.visibility = View.VISIBLE
//            showDatePickerDialog()
        }

        val calendar = Calendar.getInstance()
//        calendarView.maxDate = calendar.timeInMillis
        // Listener for date change on CalendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Set the selected date in the calendar instance
            calendar.set(year, month, dayOfMonth)
        }

        // OK button click listener
        binding.filterCalendar.okayBtn.setOnClickListener {
            // Format the selected date
            val dateFormat = SimpleDateFormat("MMM \n d", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)
            binding.dateSelection.text = formattedDate

            // Store the selected date in the desired format
            selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            binding.filterCalendar.popUpDateandTime.visibility = View.GONE
            val filteerTaskModel = FilteerTaskModel(selectedDate!!,status)
            isPendingTaskNotification = false
            viewModel.invokeFilterTask(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),filteerTaskModel)

        }
        binding.filterCalendar.cancelbtn.setOnClickListener {

            binding.filterCalendar.popUpDateandTime.visibility = View.GONE
        }

        binding.addtbn.setOnClickListener {

            val fragment = AddTaskFragment(listener)
            val args = Bundle().apply {
                putInt(Constants.CHANGED_FRAGMENT_FIRST_TIME,1)
            }
            fragment.arguments = args
            CommonMethods.switchFragment(requireActivity(),fragment)
        }

        binding.customshow.okayBtn.setOnClickListener {

            binding.customshow.customeAlert.visibility = View.GONE

        }

        binding.customshow.cancelbtn.setOnClickListener {

            binding.customshow.customeAlert.visibility = View.GONE

        }

        binding.taskRefresh.setOnRefreshListener {

            CommonMethods.switchFragment(requireActivity(),TaskHomeFragment(listener))
        }

        binding.recyclerTask.setOnTouchListener { _, _ ->
            binding.taskRefresh.isEnabled = false
            false
        }

        (activity as? MainActivity)?.setMenuButtonVisibility(false)

        binding.calendarPopUp.cancelbtn.setOnClickListener {

            binding.calendarPopUp.popUpDateandTime.visibility = View.GONE
        }

        validatebtn()

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatebtn()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.calendarPopUp.editTextStartTime.addTextChangedListener(watcher)
        binding.calendarPopUp.editTextEndTime.addTextChangedListener(watcher)



        observeUpdateTaskCall()
        observeFilterFetchTaskCall()
        observeUpdateRescheduleCall()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDateAndSet(date: String?) {

        Log.d("DateChecking",date!!)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localeDate = LocalDate.parse(date,formatter)

        val currentMonth = localeDate.month.getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.getDefault())
        val currentData = localeDate.dayOfMonth

        binding.dateSelection.text = "${currentMonth.toUpperCase()}\n $currentData"
        selectedDate = date

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun callApiWithDate(date: String, taskStatus: Int) {

        Log.d("InsideElse","inside else $date")
        parseDateAndSet(date)

        val filteerTaskModel = FilteerTaskModel(date,taskStatus)
//        binding.all.isChecked = true
        viewModel.invokeFilterTask(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),filteerTaskModel)
    }

    private fun  observeFetchTaskCall(){

        lifecycleScope.launch {
            with(viewModel){
                observeFetchTaskCall.collect{res ->
                    when(res){
                        is ApiState.SUCESS -> {

                            val it = res.getResponse
                            if(it.statusCode == 200){

                                initRecycler(it.body.data)
                            }
                            if (it.statusCode == 400){
                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()

                            }
                        }

                        is ApiState.ERROR -> {
                            Log.d("TaskFETCH","ERROR")
                        }
                        ApiState.LOADING -> {
//                            Toast.makeText(requireContext(),"Loading",Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
    }

    private fun initRecycler(data: List<FetchTaskDetail>) {


        Log.d("datasizechecking2","${data.size}")
        binding.recyclerTask.visibility = View.VISIBLE
        binding.nothingfoundanimation.visibility = View.GONE

        taskList = data.toMutableList()
        binding.recyclerTask.setHasFixedSize(false)
        binding.recyclerTask.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,false)


        if (ActiveTaskTracker.activeTaskId != null && taskList != null){

            val matchData = taskList!!.find { it._id == ActiveTaskTracker.activeTaskId }
            if (matchData == null){

                ActiveTaskTracker.activeTaskId = null
            }
        }
        adapterTaskFetch = TaskHomeRecyclerAdapter(requireContext(),this,taskList!!,requireActivity(),isPendingTaskNotification)
        binding.recyclerTask.adapter = adapterTaskFetch
        adapterTaskFetch.attachToRecyclerView(binding.recyclerTask)


        binding.search.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapterTaskFetch.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClickTask(position: Int, fetchTaskDetail: FetchTaskDetail,tag:Int,status:Int) {

        tagSaved = tag

        //Task start
        if(tag == 1){

            updateDate(fetchTaskDetail,tag)

        }

        //task Pause
        else if (tag == 2){

            updateDate(fetchTaskDetail,tag)

        }

        //resumse task
        else if (tag == 3){

            updateDate(fetchTaskDetail,tag)

        }
        //Finish task
        else if(tag == 4){

            isSwipedNo = 4
            swipeId = fetchTaskDetail._id
            updateDate(fetchTaskDetail,tag)
            if (isApiSucces){

                adapterTaskFetch.removeItem(position)
                adapterTaskFetch.notifyDataSetChanged()
            }else initRecycler(taskList!!)


        }


        //Delete Task

        else if(tag == 5){

            isSwipedNo = 5
            swipeId = fetchTaskDetail._id
            updateDate(fetchTaskDetail,tag)
            if (isApiSucces) {

                adapterTaskFetch.removeItem(position)
                adapterTaskFetch.notifyItemRemoved(position)

            }else{

                initRecycler(taskList!!)

            }


        }

        //when press no then it reinitialise recycler

        else if (tag == 9){

            initRecycler(taskList!!)
//            CommonMethods.switchFragment(requireActivity(),TaskHomeFragment())
        }


        //On Item Click
        else if(tag == 6){

            val fragment = StartTaskFragment(listener)

            val timeStart = CommonMethods.formatApiTime(extractTime(fetchTaskDetail.start_time))
            val timeEnd = CommonMethods.formatApiTime(extractTime(fetchTaskDetail.end_time))
            val args = Bundle().apply {
                putString(Constants.TASK_NAME,fetchTaskDetail.taskName)
                putString(Constants.TASK_ADDRESS,fetchTaskDetail.address1)
                putString(Constants.TASK_TIMING,timeStart+" "+timeEnd)
                if (fetchTaskDetail.value.amount != null) putDouble(Constants.TASKVALUE1,fetchTaskDetail.value.amount!!)
                putDouble(Constants.TASKVOLUME1,fetchTaskDetail.taskVolume)
                if (fetchTaskDetail.value.currency != null) putString(Constants.CURRENCYSELECTION1,fetchTaskDetail.value.currency)

                putInt(Constants.ISTASK_STARTED,fetchTaskDetail.taskApproveStatus)
//                Log.d("dfjklakjsdk",fetchTaskDetail.taskApproveStatus.toString())
                putString(Constants.CLIENT_NAME_TASK,fetchTaskDetail.clientName)
                putString(Constants.TASK_ID,fetchTaskDetail._id)
                putSerializable(Constants.SELECTED_MODEL_ITEM,fetchTaskDetail)
//                if (status == 3)  putInt(Constants.FIRST_HOME_CLICK,27)
//                else
                    putInt(Constants.FIRST_HOME_CLICK,26)
                if (status == 12) putBoolean(Constants.IS_PENDING_TASK_RESCHEDULE,true)
            }
            fragment.arguments = args
            CommonMethods.switchFragment(requireActivity(),fragment)
        }

        else if (tag == 10){

            binding.customshow.customeAlert.visibility = View.VISIBLE
        }
        else if (tag == 12){

            binding.calendarPopUp.popUpDateandTime.visibility = View.VISIBLE
            var calendarView: CalendarView

            calendarView = binding.calendarPopUp.calendarView
            val currentDate = Calendar.getInstance().timeInMillis

            // Set the minimum selectable date to the current date
            calendarView.minDate = currentDate

            var _selectedDate:String? = null

            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

                _selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                Log.d("datetimechecking","$_selectedDate")

            }

            binding.calendarPopUp.editTextStartTime.setOnClickListener {
                showTimePickerDialogPopUp(1)
            }

            binding.calendarPopUp.editTextEndTime.setOnClickListener {
                showTimePickerDialogPopUp(2)
            }


            binding.calendarPopUp.okayBtn.setOnClickListener {

                if (_selectedDate.isNullOrEmpty()){

                    _selectedDate = CommonMethods.getCurrentDate()
                }
                val amount = AmountCurrency(fetchTaskDetail!!.value.currency,fetchTaskDetail!!.value.amount)
                val updateRescheduleModel = UpdateRescheduleModel(fetchTaskDetail!!._id,fetchTaskDetail!!.clientId,fetchTaskDetail!!.taskName,"$_selectedDate $popUptimeStart:00","$_selectedDate $popUpTimeEnd:00",fetchTaskDetail!!.taskDescription,"$_selectedDate",amount,fetchTaskDetail!!.taskVolume,fetchTaskDetail!!.tagLogs!!)

                viewModel.invokeRescheduleTaskCall(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),updateRescheduleModel)
            }
        }

//        if (tag == 6 && status == 3){
//
//            val fragment = StartTaskFragment(listener)
//
//            val timeStart = CommonMethods.formatApiTime(extractTime(fetchTaskDetail.start_time))
//            val timeEnd = CommonMethods.formatApiTime(extractTime(fetchTaskDetail.end_time))
//            val args = Bundle().apply {
//                putString(Constants.TASK_NAME,fetchTaskDetail.taskName)
//                putString(Constants.TASK_ADDRESS,fetchTaskDetail.address1)
//                putString(Constants.TASK_TIMING,timeStart+" "+timeEnd)
//                putInt(Constants.ISTASK_STARTED,fetchTaskDetail.taskApproveStatus)
//                Log.d("dfjklakjsdk",fetchTaskDetail.taskApproveStatus.toString())
//                putString(Constants.CLIENT_NAME_TASK,fetchTaskDetail.clientName)
//                putString(Constants.TASK_ID,fetchTaskDetail._id)
//                putSerializable(Constants.SELECTED_MODEL_ITEM,fetchTaskDetail)
//                putInt(Constants.FIRST_HOME_CLICK,27)
//            }
//            fragment.arguments = args
//            CommonMethods.switchFragment(requireActivity(),fragment)
//        }


    }



    private fun updateDate(fetchTaskDetail: FetchTaskDetail, tag: Int) {

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
//                    Toast.makeText(requireContext(), "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_LONG).show()

                     val amount = AmountCurrency(fetchTaskDetail!!.value.currency,fetchTaskDetail!!.value.amount)
                     val tags = fetchTaskDetail!!.tagLogs
                     val updateTaskModel = UpdateTaskModel(fetchTaskDetail._id,tag,CommonMethods.getCurrentDateTime(),latitude.toString(),longitude.toString(),amount,fetchTaskDetail!!.taskVolume,tags,fetchTaskDetail!!.files,fetchTaskDetail!!.images)
                     viewModel.invokeUpdateTaskCall(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),updateTaskModel)
                 } ?: run {
                     Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_LONG).show()
                 }
             }
             .addOnFailureListener {
                 Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_LONG).show()
             }

     }

    fun extractTime(time:String):String{

        val realTime = time.split(" ")

        return realTime[1]
    }


//    private fun callUpdateApi(deletedItem: FetchTaskDetail, i: Int) {
//
//        val sp = requireContext().getSharedPreferences(Constants.AUTH_TOKEN,AppCompatActivity.MODE_PRIVATE)
//        val token = sp.getString(Constants.AUTH_TOKEN,"")
//
//        if(i == 1){
////            getLastLocation()
//            val updateTaskModel = UpdateTaskModel(deletedItem._id,4,CommonMethods.getCurrentDateTime(),lat!!,lon!!)
//            viewModel.invokeUpdateTaskCall(token!!,updateTaskModel)
//        }
//        if (i == 2){
////            getLastLocation()
//            val updateTaskModel = UpdateTaskModel(deletedItem._id,5,CommonMethods.getCurrentDateTime(),lat!!,lon!!)
//            viewModel.invokeUpdateTaskCall(token!!,updateTaskModel)
//        }
//
//
//    }

    // Extension function to convert dp to pixels
    fun Float.dpToPx(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            context.resources.displayMetrics
        ).toInt()
    }

    private fun observeUpdateTaskCall(){
        lifecycleScope.launch {
            with(viewModel){
                observeUpdateTaskFlow.collect{res ->
                    when(res) {
                        is ApiState.ERROR -> {

                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(requireContext(),"Loading",Toast.LENGTH_SHORT).show()
                        }
                        is ApiState.SUCESS -> {

                            val it = res.getResponse
                            if(it.statusCode == 200){

                                isApiSucces = true
                                if (isSwipedNo == 4) {

                                    if (ActiveTaskTracker.activeTaskId == swipeId) ActiveTaskTracker.activeTaskId = null
                                }

                                if (isSwipedNo == 5) {

                                    if (ActiveTaskTracker.activeTaskId == swipeId) ActiveTaskTracker.activeTaskId = null

                                }
                                binding.taskRefresh.isEnabled = true
                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                            }
                            if(it.statusCode == 400){

                                if (tagSaved == 1) ActiveTaskTracker.activeTaskId = null
                                binding.taskRefresh.isEnabled = true
                                if (it.body.message.equals("Can't start new task if previous tasks is Pending or Completed!"))
                                {
                                    binding.customshow.customeAlert.visibility = View.VISIBLE
                                    if (tagSaved == 1) ActiveTaskTracker.activeTaskId = null


                                }else if (it.body.message.equals("You have been logged out. Unable to perform the update.")){

                                    Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                                    ActiveTaskTracker.activeTaskId = null
                                }
                                else if (it.body.message.equals("Invalid Task Id or the Task does not exist!")){

                                    ActiveTaskTracker.activeTaskId = null
                                }else if (it.body.message.equals("Task updates are restricted for future tasks!")){

                                    ActiveTaskTracker.activeTaskId = null
                                }

                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()

//                                initRecycler(taskList!!)
                            }
                        }


                    }
                }
            }
        }
    }

    private fun getLastLocation() {
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
//                    Toast.makeText(requireContext(), "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_LONG).show()
                } ?: run {
                    Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_LONG).show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLastLocation()
                } else {
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }



    private fun  observeFilterFetchTaskCall(){
        lifecycleScope.launch {
            with(viewModel){
                observeFilterTaskCall.collect{res ->
                    when(res){
                        is ApiState.SUCESS -> {

                            val it = res.getResponse
                            if(it.statusCode == 200){

                                initRecycler(it.body.data)
                            }
                            if (it.statusCode == 400){
//                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                                showNothingFound()
                            }
                        }

                        is ApiState.ERROR -> {
                            Log.d("TaskFETCH","ERROR")
                        }
                        ApiState.LOADING -> {
//                            Toast.makeText(requireContext(),"Loading",Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
    }

    fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                // Update the calendar with the selected date

                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Update the TextView with the selected date
                updateSelectedDate()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )



        // Show the DatePickerDialog
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(
            R.color.calendarBtn));
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.calendarBtn));
    }

    private fun updateSelectedDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        dateTxt =  dateFormat.format(calendar.time)

        val (newMonth,newDate) = CommonMethods.extractMonthAndDay(dateTxt!!, "yyyy-MM-dd")
        binding.dateSelection.text = "${newMonth.toUpperCase()} \n$newDate "

//        Log.d("DateChecking",dateTxt)


    }

    fun filterApiCall(status:Int){

//        Log.d("Adgas",selectedDate.toString())
        val filteerTaskModel = FilteerTaskModel(selectedDate!!,status)
        viewModel.invokeFilterTask(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),filteerTaskModel)
    }


    private fun showNothingFound(){

        binding.recyclerTask.visibility = View.GONE
        binding.nothingfoundanimation.visibility = View.VISIBLE
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

}