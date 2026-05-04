package com.empcloud.empmonitor.ui.fragment.leaves

import android.app.DatePickerDialog
import android.content.Intent
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
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isNotEmpty
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.request.LEAVES.LeavesRequestModel
import com.empcloud.empmonitor.data.remote.request.createleave.CreateLeaveRequestModel
import com.empcloud.empmonitor.data.remote.request.delete.DeleteLeaveRequest
import com.empcloud.empmonitor.data.remote.request.editleave.EditLeaveRequestModel
import com.empcloud.empmonitor.data.remote.response.get_leave_type.LeaveType
import com.empcloud.empmonitor.data.remote.response.leaves.LeavesList
import com.empcloud.empmonitor.databinding.FragmentLeavesBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.ui.adapters.LeavesRecyclerAdapter
import com.empcloud.empmonitor.ui.adapters.SpinnerLeaveAdapter
import com.empcloud.empmonitor.ui.listeners.LeaveRecyclerItemClickListener
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.play.integrity.internal.i
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class LeavesFragment constructor(private val listener: OnFragmentChangedListener? = null): Fragment(),LeaveRecyclerItemClickListener {


    private var leaveSelection:Int ? = null
    private var empname: String? = null
    private var startDate: String? = null
    private var endDate: String? = null

    private lateinit var spinner: Spinner
    private lateinit var leaveAdapter: SpinnerLeaveAdapter
    private var leaveCode: Int = 0
    private var leaveTypes: List<LeaveType> = listOf()


//    private var listener:OnFragmentChangedListener? = null

    private lateinit var binding:FragmentLeavesBinding
    private val viewModel by viewModels<LeavesViewModel>()
    private var calendar: Calendar = Calendar.getInstance()
    var startDATE:String? = null
    var endDATE:String? = null
    var dayCode:Int? = null
    var laeaveCode:Int? = null

    var startDATEEdit:String? = null
    var endDATEEdit:String? = null
    var dayCodeEdit:Int? = null
    var laeaveCodeEdit:Int? = null

    var leaveList:List<LeavesList>? = null

    val defaultYear = calendar.get(Calendar.YEAR)
    val defaultMonth = calendar.get(Calendar.MONTH)
    val defaultDay = calendar.get(Calendar.DAY_OF_MONTH)

    companion object {
        fun newInstance() = LeavesFragment
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLeavesBinding.inflate(layoutInflater,container,false)

        spinnerShow()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        CommonSpinner.spinnerShow(binding.spinner,requireContext(),requireActivity() as FragmentActivity)
        val startDate = getStartDateOfCurrentMonth()
        val endDate = getEndDateOfCurrentMonth()
        val pref = requireContext().getSharedPreferences(Constants.AUTH_TOKEN, AppCompatActivity.MODE_PRIVATE)
        val token = pref.getString(Constants.AUTH_TOKEN,"")

        val leavesRequestModel = LeavesRequestModel(startDate,endDate)
        invokeLeavesData(token!!,leavesRequestModel)

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateForm()
            }
            override fun afterTextChanged(s: Editable?) {

                if (CommonMethods.isValidDateFormat(startDate)) binding.popLeaves.nextbtn.isClickable = true
                else Toast.makeText(requireContext(),"Date is Invalid",Toast.LENGTH_SHORT).show()
            }
        }
        validateForm()




        binding.popLeaves.empName.addTextChangedListener(watcher)
        binding.popLeaves.reason.addTextChangedListener(watcher)
        binding.popLeaves.startDate.addTextChangedListener(watcher)
        binding.popLeaves.endDate.addTextChangedListener(watcher)

        binding.backbtn.setOnClickListener {

            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
//            val homeFragement = HomeFragment(listener)
//            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,homeFragement).commit()
        }

        binding.calendar.setOnClickListener{
//            showDateRangePicker()
//            showDatePickerDialog()

            showDatePickerDialognew()
        }

        binding.addLeavesBtn.setOnClickListener {

            if (!empname.isNullOrEmpty()) {

                binding.popLeaves.empName.setText(leaveList!![0].employee_name)
                binding.popLeaves.empName.isEnabled = false
            }
            else {
                val sp = requireContext().getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
                val name =CommonMethods.getSharedPrefernce(requireActivity(),Constants.NAME_FULL)
//                Log.d("leavebame",name.toString())
                binding.popLeaves.empName.setText(sp.getString(Constants.NAME_FULL,""))
            }

            binding.popLeaves.leavesScreenApply.visibility = View.VISIBLE

            viewModel.invokeGetLeaveType(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN))
            initPopScreens()
            getData()
        }

        binding.popLeaves.cancebtn.setOnClickListener {
            binding.popLeaves.leavesScreenApply.visibility = View.GONE
        }

        binding.popLeaves.startDate.setOnClickListener {

        }

        binding.popLeaves.endDate.setOnClickListener {

        }

        spinner = binding.popLeaves.autoCompleteLeave

        // Initialize the adapter with an empty list
//        leaveAdapter = SpinnerLeaveAdapter()
//        spinner.adapter = leaveAdapter


        observeLeavesData()
        observeCreateLeaveCall()
        observeEditLeaves()
        observeDeleteLeaves()
        observeGetLeaveTypes()
    }

    fun invokeLeavesData(authToken:String,leavesRequestModel: LeavesRequestModel){
        viewModel.invokeLeaves(authToken,leavesRequestModel)
    }

    private fun observeLeavesData(){
//        Log.d("Response","Inseide observer")
        lifecycleScope.launchWhenStarted {
            with(viewModel){
                observeLeavesFlow.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.statusCode == 200){

                                initRecycler(it.body.data)
                            }
                            if (it.statusCode == 400){
                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
//                                playAnimationLottie()
                                showNoIcon()
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

    private fun initRecycler(data: List<LeavesList>) {

        binding.datashowcard.visibility = View.VISIBLE
        binding.nothingfoundanimation.visibility = View.GONE
        leaveList = data
        empname = leaveList!![0].employee_name
        binding.leavesRecycler.setHasFixedSize(false)
        binding.leavesRecycler.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,false)

        val view = binding.editLeaves

        binding.leavesRecycler.adapter = LeavesRecyclerAdapter(requireContext(),this,data)
    }


    private fun showDateRangePicker() {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())

        val datePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Date Range")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")


        datePicker.addOnPositiveButtonClickListener { dateRange ->
            val startDate = dateRange.first
            val endDate = dateRange.second

            val startDateString = formatDate(startDate)
            val endDateString = formatDate(endDate)

            // Call API with the selected dates
            val pref = requireContext().getSharedPreferences(Constants.AUTH_TOKEN, AppCompatActivity.MODE_PRIVATE)
            val token = pref.getString(Constants.AUTH_TOKEN,"")

            val leavesRequestModel = LeavesRequestModel(startDateString,endDateString)
            invokeLeavesData(token!!,leavesRequestModel)
        }
    }

//    fun showDatePickerDialog() {
//        // Get current date to set as default start date
//        val calendar = Calendar.getInstance()
//        val defaultYear = calendar.get(Calendar.YEAR)
//        val defaultMonth = calendar.get(Calendar.MONTH)
//        val defaultDay = calendar.get(Calendar.DAY_OF_MONTH)
//
//        // Create a date picker dialog
//        val datePickerDialog = DatePickerDialog(
//            requireContext(),
//            { _, year, month, dayOfMonth ->
//                val selectedDate = Calendar.getInstance()
//                selectedDate.set(year, month, dayOfMonth)
//                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                val selectedDateString = dateFormat.format(selectedDate.time)
//
//                // Check if start date is selected
//                if (startDate == null) {
//                    // Set start date
//                    startDate = selectedDateString
//
//                    // Show the same dialog to select the end date
//                    showDatePickerDialog()
//                } else {
//                    // Set end date
//                    endDate = selectedDateString
//
//                    // Validate if end date is after start date
//                    if (endDate!!.compareTo(startDate!!) >= 0) {
//                        // Perform API call with selected date range
//                        val pref = requireContext().getSharedPreferences(Constants.AUTH_TOKEN, AppCompatActivity.MODE_PRIVATE)
//                        val token = pref.getString(Constants.AUTH_TOKEN,"")
//
//                        val leavesRequestModel = LeavesRequestModel(startDate!!,endDate!!)
//                        invokeLeavesData(token!!,leavesRequestModel)
//                    } else {
//                        // Show error message if end date is before start date
//                        Toast.makeText(requireContext(), "End date must be after start date", Toast.LENGTH_SHORT).show()
//                    }
//
//                    // Reset start and end dates for next selection
//                    startDate = null
//                    endDate = null
//                }
//            },
//            defaultYear,
//            defaultMonth,
//            defaultDay
//        )
//
//        // Show the date picker dialog
//        datePickerDialog.show()
//    }

    private fun formatDate(timestamp: Long?): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date(timestamp ?: 0))
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getStartDateOfCurrentMonth(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now()
        return currentDate.withDayOfMonth(1).format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEndDateOfCurrentMonth(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now()
        return currentDate.withDayOfMonth(currentDate.lengthOfMonth()).format(formatter)
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setMenuButtonVisibility(false)
        initPopScreens()
    }

    private fun initPopScreens(){

//        val dateType = resources.getStringArray(R.array.dateType)
//        val arrayAdapter = ArrayAdapter<String>(requireContext(),R.layout.text_layout_leaves)
//        binding.popLeaves.autoCompletedate.setAdapter(arrayAdapter)
//
//
        val leaveType = resources.getStringArray(R.array.leaveType)
        val arrayAdapterLeave = ArrayAdapter(requireActivity(),R.layout.text_layout_leaves,leaveType.toList())
        val autoCompleteTextView = binding.popLeaves.autoCompleteLeave
//        autoCompleteTextView.threshold = 1
//        autoCompleteTextView.setAdapter(arrayAdapterLeave)



//        val items = listOf("Item 1", "Item 2", "Item 3") // Your list of items
//
//        val autoCompleteTextView = binding.popLeaves.autoCompleteLeave
//
//        val adapter = ArrayAdapter<String>(requireContext(),R.layout.text_layout_leaves,items)
//        autoCompleteTextView.setAdapter(adapter)

    }

    private fun getData(){



//        val sharedPred = requireActivity().getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
//        val nameset = sharedPred.getString(Constants.USER_FULL_NAME,"")
//        if (leaveList!![0].employee_name != null)     binding.popLeaves.empName.setText(leaveList!![0].employee_name)
        spinnerShowPopup()
//        showSpinnerLeave()

//        val reason = binding.popLeaves.reason.text.toString()
        binding.popLeaves.startDate.setOnClickListener {
            showDatePickerDialog(1)
        }
        binding.popLeaves.endDate.setOnClickListener {
            showDatePickerDialog(2)
        }

        binding.popLeaves.nextbtn.setOnClickListener {

            val sp = requireContext().getSharedPreferences(Constants.AUTH_TOKEN,AppCompatActivity.MODE_PRIVATE)
            val token = sp.getString(Constants.AUTH_TOKEN,"")

//            Log.d("code",dayCode.toString())
//            Log.d("code",laeaveCode.toString())
//            Log.d("code",startDATE.toString())
//            Log.d("code",endDATE.toString())
//            Log.d("code",binding.popLeaves.reason.text.toString())

            if (!startDATE.isNullOrEmpty() && !endDATE.isNullOrEmpty()){
                val createLeaveRequestModel = CreateLeaveRequestModel(dayCode!!,laeaveCode!!,startDATE!!,endDATE!!,binding.popLeaves.reason.text.toString())
                viewModel.invokeCreateLeave(token!!,createLeaveRequestModel)
            }else{
                Toast.makeText(requireContext(),"Please Enter Date",Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun showDatePickerDialog(i: Int) {
        // Get current date as default selection
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create and show DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),R.style.CustomDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                // Update EditText with the selected date
                val selectedDate = "$year-${month + 1}-$dayOfMonth"
                if(i == 1){
                    binding.popLeaves.startDate.setText(selectedDate)
                    startDATE = selectedDate
                }else{
                    binding.popLeaves.endDate.setText(selectedDate)
                    endDATE = selectedDate
                }

            },
            year,
            month,
            day
        )

//        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.calendarBtn));
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.calendarBtn));
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeCreateLeaveCall(){
        lifecycleScope.launch {
            with(viewModel){
                observeCreateLeaveFlow.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
                            Log.d("Inside viewmodel","observer")

                            if(it.statusCode == 200){

                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                                binding.popLeaves.leavesScreenApply.visibility = View.GONE
                                val sharPref = requireActivity().getSharedPreferences(Constants.LEAVE_ID,AppCompatActivity.MODE_PRIVATE)
                                sharPref.edit().putInt(Constants.LEAVE_ID,it.body.data.data.leave.leave_id).apply()
                                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,LeavesFragment(listener)).commit()

                            }

                            if(it.statusCode == 400){

                                if (it.body.error.message == null)   Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                                else Toast.makeText(requireContext(),it.body.error.message, Toast.LENGTH_SHORT).show()
                                binding.popLeaves.leavesScreenApply.visibility = View.GONE
                                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,LeavesFragment(listener)).commit()
//                                binding.popLeaves.leavesScreenApply.visibility = View.GONE
                            }

                        }
                        is ApiState.LOADING -> {

                        }
                        is ApiState.ERROR -> {


                        }
                    }
                }
            }
        }
    }

    fun spinnerShowPopup(){

        // Define the items to show in the spinner
            val items = listOf("First Half", "Second Half", "Full Day")
            val adapter = ArrayAdapter(requireContext(),R.layout.text_layout_leaves, items)

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.text_layout_leaves)

            // Apply the adapter to the spinner
            binding.popLeaves.autoCompletedate.adapter = adapter

            // Set an OnItemSelectedListener to the spinner
            binding.popLeaves.autoCompletedate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    // Get the selected item
                    val selectedItem = parent.getItemAtPosition(position) as String

                    // Perform actions based on the selected item
                    when (selectedItem) {
                        "First Half" -> {
                            dayCode = 1
                        }
                        "Second Half" -> {

                            dayCode = 3
                        }
                        "Full Day" -> {

                            dayCode = 2
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing if nothing is selected
                }
            }

    }

//    fun showSpinnerLeave(){
//
//
//        val items = listOf("Casual Leave", "Emergency Leave")
//        val adapter = ArrayAdapter(requireContext(), R.layout.text_layout_leaves, items)
//
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(R.layout.text_layout_leaves)
//
//        // Apply the adapter to the spinner
//        binding.popLeaves.autoCompleteLeave.adapter = adapter
//
//        // Set an OnItemSelectedListener to the spinner
//        binding.popLeaves.autoCompleteLeave.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                // Get the selected item
//                val selectedItem = parent.getItemAtPosition(position) as String
//
//                // Perform actions based on the selected item
//                when (selectedItem) {
//                    "Casual Leave" -> {
//                        laeaveCode = 156
//                    }
//                    "Emergency Leave" -> {
//
//                        laeaveCode = 163
//                    }
//
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // Do nothing if nothing is selected
//
//            }
//        }
//    }


//    private fun showSpinnerLeave() {
//        // Fetch data from API and update spinner
//        lifecycleScope.launch {
//            try {
//                // Update data in the adapter
//                leaveAdapter.updateData(leaveTypes)
//
//                spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
//                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                        // Get the selected item
//                        val selectedItem = parent.getItemAtPosition(position) as LeaveType
//
//                        // Perform actions based on the selected item
//                        leaveCode = selectedItem.id
//                    }
//
//                    override fun onNothingSelected(parent: AdapterView<*>) {
//                        // Do nothing if nothing is selected
//                    }
//                })
//            } catch (e: Exception) {
//                e.printStackTrace()
//                // Handle the error
//            }
//        }
//    }


    override fun onItemClicked(leave: LeavesList, position: Int) {

        binding.editLeaves.editScreenApply.visibility = View.VISIBLE

        binding.editLeaves.cancebtn.setOnClickListener {

            binding.editLeaves.editScreenApply.visibility = View.GONE

        }
        openEditPopUp(position,leave)

//        val watcheredit = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                validateFormEdit()
//            }
//            override fun afterTextChanged(s: Editable?) {
//
//                if (CommonMethods.isValidDateFormat(startDate!!)) binding.editLeaves.nextbtn.isClickable = true
//                else Toast.makeText(requireContext(),"Date is Invalid",Toast.LENGTH_SHORT).show()
//            }
//        }
//        validateFormEdit()
//
//        binding.editLeaves.empName.addTextChangedListener(watcheredit)
//        binding.editLeaves.reason.addTextChangedListener(watcheredit)
//        binding.editLeaves.startDate.addTextChangedListener(watcheredit)
//        binding.editLeaves.endDate.addTextChangedListener(watcheredit)

    }

    private fun openEditPopUp(position: Int, leave: LeavesList) {

        binding.editLeaves.empName.setText(leave.employee_name)
        binding.editLeaves.empName.isEnabled = false

//        binding.editLeaves.empName.setText(CommonMethods.getSharedPrefernce(requireActivity(),Constants.USER_FULL_NAME))
        startDATEEdit = binding.editLeaves.startDate.setText(leaveList!![position].start_date.split("T")[0]).toString()
        endDATEEdit = binding.editLeaves.endDate.setText(leaveList!![position].end_date.split("T")[0]).toString()
        binding.editLeaves.reason.setText(leave.reason)

//        spinner.setSelection(leave.leave_type)

        leaveSelection = leave.leave_type

        spinnerShowPopupEdit(leave,position)
        viewModel.invokeGetLeaveType(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN))


//        showSpinnerLeaveEdit()

//        val reason = binding.popLeaves.reason.text.toString()
        binding.editLeaves.startDate.setOnClickListener {
            showDatePickerDialogEdit(1)
        }
        binding.editLeaves.endDate.setOnClickListener {
            showDatePickerDialogEdit(2)
        }

        binding.editLeaves.nextbtn.setOnClickListener {

            val sp = requireContext().getSharedPreferences(Constants.AUTH_TOKEN,AppCompatActivity.MODE_PRIVATE)
            val token = sp.getString(Constants.AUTH_TOKEN,"")

//            Log.d("code1",dayCodeEdit.toString())
//            Log.d("code1",laeaveCodeEdit.toString())
//            Log.d("code1",binding.editLeaves.startDate.text.toString())
//            Log.d("code1",binding.editLeaves.endDate.text.toString())
//            Log.d("code1",binding.editLeaves.reason.text.toString())
//            Log.d("code1",leaveList!![0].id!!.toString())


            val editLeavesRequestModel = EditLeaveRequestModel(leaveList!![position].id,dayCodeEdit!!,laeaveCodeEdit!!,binding.editLeaves.startDate.text.toString(),binding.editLeaves.endDate.text.toString(),binding.editLeaves.reason.text.toString())
//            Log.d("code1",
//                EditLeaveRequestModel(leaveList!![position].id,dayCodeEdit!!,laeaveCodeEdit!!,binding.editLeaves.startDate.text.toString(),binding.editLeaves.endDate.text.toString(),binding.editLeaves.reason.text.toString().toString()).toString()
//            )

            viewModel.invokeEditLeave(token!!,editLeavesRequestModel)

        }

        binding.editLeaves.deletebtn.setOnClickListener {

            val sp = requireContext().getSharedPreferences(Constants.AUTH_TOKEN,AppCompatActivity.MODE_PRIVATE)
            val token = sp.getString(Constants.AUTH_TOKEN,"")

            val deleteLeaveRequest = DeleteLeaveRequest(leaveList!![position].id)

            viewModel.invokeDeleteLeave(token!!,deleteLeaveRequest)


        }

    }

    private fun showDatePickerDialogEdit(i: Int) {
        // Get current date as default selection
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create and show DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),R.style.CustomDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                // Update EditText with the selected date
                val selectedDate = "$year-${month + 1}-$dayOfMonth"
                if(i == 1){
                    binding.editLeaves.startDate.setText(selectedDate)
                    startDATEEdit = selectedDate
                }else{
                    binding.editLeaves.endDate.setText(selectedDate)
                    endDATEEdit = selectedDate
                }

            },
            year,
            month,
            day
        )
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.buttonbgcolor))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.buttonbgcolor))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeEditLeaves(){
        lifecycleScope.launch {
            with(viewModel){
                observeEditLeaveCall.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
                            Log.d("Inside viewmodel","observer")

                            if(it.statusCode == 200){

                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                                binding.editLeaves.editScreenApply.visibility = View.GONE
                                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,LeavesFragment(listener)).commit()


                            }

                            if(it.statusCode == 400){

//                                if (!it.body.data.message.isNullOrEmpty())  Toast.makeText(requireContext(),it.body.data.message, Toast.LENGTH_SHORT).show()
                                  Toast.makeText(requireContext(),it.body.error.message, Toast.LENGTH_SHORT).show()

                                binding.editLeaves.editScreenApply.visibility = View.GONE
                                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,LeavesFragment(listener)).commit()


//                                binding.popLeaves.leavesScreenApply.visibility = View.GONE
                            }

                        }
                        is ApiState.LOADING -> {

                        }
                        is ApiState.ERROR -> {


                        }
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeDeleteLeaves(){
        lifecycleScope.launch {
            with(viewModel){
                observedeleteLeaveCall.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
                            Log.d("Inside viewmodel","observer")

                            if(it.statusCode == 200){

                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                                binding.editLeaves.editScreenApply.visibility = View.GONE
                                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,LeavesFragment(listener)).commit()

                            }

                            if(it.statusCode == 400){
                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                                binding.editLeaves.editScreenApply.visibility = View.GONE
                                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,LeavesFragment(listener)).commit()
//                                binding.popLeaves.leavesScreenApply.visibility = View.GONE
                            }

                        }
                        is ApiState.LOADING -> {

                        }
                        is ApiState.ERROR -> {


                        }
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeGetLeaveTypes(){
        lifecycleScope.launch {
            with(viewModel){
                observeGetLeaveType.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
                            Log.d("Inside viewmodel","observer")

                            if(it.statusCode == 200){

//                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                                spinnerinit(it.body.data.data)
                                spinnerEditLeave(it.body.data.data)
                                leaveTypes = it.body.data.data

                            }

                            if(it.statusCode == 400){

                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()

                            }

                        }
                        is ApiState.LOADING -> {

                        }
                        is ApiState.ERROR -> {


                        }
                    }
                }
            }
        }
    }

    private fun spinnerEditLeave(data: List<LeaveType>) {

        val spinner = binding.editLeaves.autoCompleteLeave
        val adapter = SpinnerLeaveAdapter(requireContext(),data)
        spinner.adapter = adapter

        val position = data.indexOfFirst { it.id == leaveSelection }
        spinner.setSelection(position)

        spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                laeaveCodeEdit  = data[position].id

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    private fun spinnerinit(data: List<LeaveType>) {

        val spinner = binding.popLeaves.autoCompleteLeave
        spinner.adapter =SpinnerLeaveAdapter(requireContext(),data)

        spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                laeaveCode = data[position].id

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }


    }

    fun spinnerShowPopupEdit(leave: LeavesList, position: Int) {

        // Define the items to show in the spinner
        val items = listOf("First Half", "Second Half", "Full Day")
        val adapter = ArrayAdapter(requireContext(),R.layout.text_layout_leaves, items)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.text_layout_leaves)

        binding.editLeaves.autoCompletedate.adapter = adapter

        // Set the selection based on leave.day_type
        when (leave.day_type) {
            1 -> binding.editLeaves.autoCompletedate.setSelection(0) // First Half
            3 -> binding.editLeaves.autoCompletedate.setSelection(1) // Second Half
            else -> binding.editLeaves.autoCompletedate.setSelection(2) // Full Day by default
        }
        // Apply the adapter to the spinner
//        binding.editLeaves.autoCompletedate.adapter = adapter

        // Set an OnItemSelectedListener to the spinner
        binding.editLeaves.autoCompletedate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Get the selected item
                val selectedItem = parent.getItemAtPosition(position) as String

                // Perform actions based on the selected item
                when (selectedItem) {
                    "First Half" -> {
                        dayCodeEdit = 1
                    }
                    "Second Half" -> {

                        dayCodeEdit = 3
                    }
                    "Full Day" -> {

                        dayCodeEdit = 2
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing if nothing is selected
            }
        }

    }

    fun showSpinnerLeaveEdit(){
        val items = listOf("Casual Leave", "Emergency Leave")
        val adapter = ArrayAdapter(requireContext(), R.layout.text_layout_leaves, items)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.text_layout_leaves)

        // Apply the adapter to the spinner
        binding.editLeaves.autoCompleteLeave.adapter = adapter

        // Set an OnItemSelectedListener to the spinner
        binding.editLeaves.autoCompleteLeave.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Get the selected item
                val selectedItem = parent.getItemAtPosition(position) as String

                // Perform actions based on the selected item
                when (selectedItem) {
                    "Casual Leave" -> {
                        laeaveCodeEdit = 156
                    }
                    "Emergency Leave" -> {

                        laeaveCodeEdit = 163
                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing if nothing is selected

            }
        }
    }

    fun spinnerShow(){

        // Define the items to show in the spinner
        val items = listOf("Leaves","Attendance", "Holidays")

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter(requireContext(), R.layout.attendance_spinner, items)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.attendance_spinner)

        // Apply the adapter to the spinner
        binding.spinner.adapter = adapter
        binding.spinner.setSelection(0)



//        binding.spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                listener?.onSpinnerItemSelection(position)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//
//        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Get the selected item
                val selectedItem = parent.getItemAtPosition(position) as String

                // Perform actions based on the selected item
                when (position) {
//                    "Leaves" -> {
//                        listener?.onSpinnerItemSelection(position,3,listener)
//                        Log.d("spinnerCheking","clicked leaves")
////                        Toast.makeText(requireContext(),"selected",Toast.LENGTH_SHORT).show()
////                        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
////                            LeavesFragment()
////                        ).commit()
//
//                    }
                    1 -> {
                        listener?.onSpinnerItemSelection(position,0,listener)

                        Log.d("spinnerCheking","clicked attendance")
//                        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
//                            HolidaysFragment()
//                        ).commit()

                    }
                    2 -> {
                        listener?.onSpinnerItemSelection(position,1,listener)

                        Log.d("spinnerCheking","clicked holidays")
//                        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,AttendanceFragment()).commit()

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing if nothing is selected
            }
        }
    }


    private fun validateForm() {
        val isEditText1Filled = binding.popLeaves.empName.text.toString().isNotEmpty()
        val isEditText2Filled = binding.popLeaves.startDate.text.toString().isNotEmpty()
        val isEditText3Filled = binding.popLeaves.endDate.text.toString().isNotEmpty()
        val isEditText4Filled = binding.popLeaves.reason.text.toString().trim().isNotEmpty()
        val isEditText5Filled = binding.popLeaves.autoCompletedate.isNotEmpty()
        val isEditText6Filled = binding.popLeaves.autoCompleteLeave.isNotEmpty()



        binding.popLeaves.nextbtn.visibility = if (isEditText1Filled && isEditText2Filled && isEditText3Filled &&  isEditText4Filled && isEditText5Filled && isEditText6Filled)
        {
            View.VISIBLE

        } else {
            View.GONE
        }

        binding.popLeaves.nextbtndisable.visibility = if (isEditText1Filled && isEditText2Filled && isEditText3Filled && isEditText4Filled && isEditText5Filled && isEditText6Filled) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

//    private fun validateFormEdit() {
//        val isEditText1Filled = binding.editLeaves.empName.text.toString().isNotEmpty()
//        val isEditText2Filled = binding.editLeaves.startDate.text.toString().isNotEmpty()
//        val isEditText3Filled = binding.editLeaves.endDate.text.toString().isNotEmpty()
//        val isEditText4Filled = binding.editLeaves.reason.text.toString().trim().isNotEmpty()
//        val isEditText5Filled = binding.editLeaves.autoCompletedate.isNotEmpty()
//        val isEditText6Filled = binding.editLeaves.autoCompleteLeave.isNotEmpty()
//
//
//
//        binding.editLeaves.nextbtn.visibility = if (isEditText1Filled && isEditText2Filled && isEditText3Filled &&  isEditText4Filled && isEditText5Filled && isEditText6Filled)
//        {
//            View.VISIBLE
//
//        } else {
//            View.GONE
//        }
//
//        binding.editLeaves.nextbtndiable.visibility = if (isEditText1Filled && isEditText2Filled && isEditText3Filled && isEditText4Filled && isEditText5Filled && isEditText6Filled) {
//            View.GONE
//        } else {
//            View.VISIBLE
//        }
//    }

//    private fun playAnimationLottie(){
//        Log.d("playanitmation","entered")
//        lifecycleScope.launch {
//
//            val animationView = binding.nothingfoundanimation
//            binding.datashowcard.visibility = View.GONE
//            animationView!!.visibility = View.VISIBLE
//            animationView.playAnimation()
//            animationView.repeatCount = 1
//
//
//        }
//    }

    private fun showNoIcon(){

        binding.datashowcard.visibility = View.GONE
        binding.nothingfoundanimation.visibility = View.VISIBLE
    }
//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialognew() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),R.style.CustomDatePickerDialogTheme,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date
                val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                onDateSelected(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.calendarBtn));
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.calendarBtn));
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onDateSelected(selectedDate: String) {
        // Parse the selected date
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDate = LocalDate.parse(selectedDate, formatter)

        // Calculate the end date by adding 30 days
        val endDate = startDate.plusDays(30)

        // Format dates to String if necessary
        val startDateStr = startDate.format(formatter)
        val endDateStr = endDate.format(formatter)

        val pref = requireContext().getSharedPreferences(Constants.AUTH_TOKEN, AppCompatActivity.MODE_PRIVATE)
        val token = pref.getString(Constants.AUTH_TOKEN,"")

        val leavesRequestModel = LeavesRequestModel(startDateStr,endDateStr)
        invokeLeavesData(token!!,leavesRequestModel)
    }

}