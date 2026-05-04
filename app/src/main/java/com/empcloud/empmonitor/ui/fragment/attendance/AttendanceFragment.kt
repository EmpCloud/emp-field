package com.empcloud.empmonitor.ui.fragment.attendance

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.request.attendance.AttendanceRequestModel
import com.empcloud.empmonitor.data.remote.request.edit_attendanc.EditAttendanceModel
import com.empcloud.empmonitor.data.remote.response.attendance.AttendanceFullData
import com.empcloud.empmonitor.databinding.FragmentAttendanceBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.ui.adapters.AttendanceAdapter
import com.empcloud.empmonitor.ui.fragment.home.HomeFragment
import com.empcloud.empmonitor.ui.listeners.AttendacneRecyclerItemClickListener
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern


@AndroidEntryPoint
class AttendanceFragment constructor(private val listener: OnFragmentChangedListener? = null): Fragment(),AttendacneRecyclerItemClickListener {

    private lateinit var binding:FragmentAttendanceBinding
    private val viewModel by viewModels<AttendanceViewModel>()
    private var calendar: Calendar = Calendar.getInstance()
//    private var listener:OnFragmentChangedListener? = null

    private var startDate: String? = null
    private var endDate: String? = null
    private var editStartDate: String? = null
    private var editEndDate: String? = null

    private var editTimeStart: String? = null
    private var editEndTime: String? = null
    var attList:List<AttendanceFullData>? = null


    companion object {
        fun newInstance() = AttendanceFragment
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAttendanceBinding.inflate(layoutInflater, container, false)
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

        val attendanceRequestModel = AttendanceRequestModel(startDate,endDate)
        invokeAttendance(token!!,attendanceRequestModel)

        binding.calendartbn.setOnClickListener{
//            showDateRangePicker()
//            showDatePickerDialog()
            showDatePickerDialognew()
        }

        binding.backbtn.setOnClickListener {

            val intent = Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
//            val homeFragment = HomeFragment(listener)
//            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,homeFragment).commit()
        }

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                validateForm()
            }
            override fun afterTextChanged(s: Editable?) {

                if (!binding.popLeaves.checkintime.text.isNullOrEmpty() && !binding.popLeaves.checkoutime.text.isNullOrEmpty()){
                    if (isValidTimeFormat(binding.popLeaves.checkintime.text.toString()!!) && isValidTimeFormat(binding.popLeaves.checkoutime.text.toString()!!)) {

                        binding.popLeaves.nextbtn.isClickable = true
                    }else{
                        Toast.makeText(requireContext(),"Time is invalid",Toast.LENGTH_SHORT).show()
                    }
                }

                }
        }

        binding.popLeaves.checkindate.addTextChangedListener(watcher)
        binding.popLeaves.checkoutdate.addTextChangedListener(watcher)
        binding.popLeaves.checkoutime.addTextChangedListener(watcher)
        binding.popLeaves.checkintime.addTextChangedListener(watcher)
        binding.popLeaves.reason.addTextChangedListener(watcher)


//        if (CommonMethods.isValidTimeFormat(binding.popLeaves.checkintime.text.toString()) && CommonMethods.isValidTimeFormat(binding.popLeaves.checkoutime.text.toString())) {
//
//            binding.popLeaves.nextbtn.isClickable = true
//        }else{
//            Toast.makeText(requireContext(),"Time is invalid",Toast.LENGTH_SHORT).show()
//        }
        validateForm()
        observeAttendance()
        observeEditAttendance()

    }

    private fun isValidTimeFormat(time: String): Boolean {
        // Regex for hh:mm[:ss] format where ss is optional
        val timePattern = "^([01]\\d|2[0-3]):([0-5]\\d)(:[0-5]\\d)?$"
        val pattern = Pattern.compile(timePattern)
        val matcher = pattern.matcher(time)
        return matcher.matches()
    }

    fun invokeAttendance(accessToken:String,attendanceRequestModel: AttendanceRequestModel){
        viewModel.invokeAttendanceCall(accessToken,attendanceRequestModel)
    }


    private fun observeAttendance(){
//        Log.d("Response","Inseide observer")
        lifecycleScope.launchWhenStarted {
            with(viewModel){
                observeAttendaceFlow.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse

//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.statusCode == 200){
                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                                initRecycler(it.body.data[0].attendance)
                            }
                            if (it.statusCode == 400){
                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                            }


                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(
//                                this@LoginActivity,"Loading...",
//                                Toast.LENGTH_LONG).show()
                            Log.d("attendace","Loading")
                        }
                        is ApiState.ERROR -> {
                            Log.d("attendace","api_error")

                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun initRecycler(it: List<AttendanceFullData>) {
        attList = it
        binding.attendanceRecycler.setHasFixedSize(false)
        binding.attendanceRecycler.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,false)

        binding.attendanceRecycler.adapter = AttendanceAdapter(requireContext(),this,it)
    }


    fun showDatePickerDialog() {
        // Get current date to set as default start date
        val calendar = Calendar.getInstance()
        val defaultYear = calendar.get(Calendar.YEAR)
        val defaultMonth = calendar.get(Calendar.MONTH)
        val defaultDay = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a date picker dialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),R.style.CustomDatePickerDialogTheme,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDateString = dateFormat.format(selectedDate.time)

                // Check if start date is selected
                if (startDate == null) {
                    // Set start date
                    startDate = selectedDateString

                    // Show the same dialog to select the end date
                    showDatePickerDialog()
                } else {
                    // Set end date
                    endDate = selectedDateString

                    // Validate if end date is after start date
                    if (endDate!!.compareTo(startDate!!) >= 0) {
                        // Perform API call with selected date range
                        val pref = requireContext().getSharedPreferences(Constants.AUTH_TOKEN, AppCompatActivity.MODE_PRIVATE)
                        val token = pref.getString(Constants.AUTH_TOKEN,"")

                        val attendanceRequestModel = AttendanceRequestModel(startDate!!,endDate!!)
                        invokeAttendance(token!!,attendanceRequestModel)
                    } else {
                        // Show error message if end date is before start date
                        Toast.makeText(requireContext(), "End date must be after start date", Toast.LENGTH_SHORT).show()
                    }

                    // Reset start and end dates for next selection
                    startDate = null
                    endDate = null
                }
            },
            defaultYear,
            defaultMonth,
            defaultDay
        )

        // Show the date picker dialog

        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.calendarBtn));
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.calendarBtn));

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

    fun spinnerShow(){

        // Define the items to show in the spinner
        val items = listOf("Attendance", "Holidays", "Leaves")

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter(requireContext(), R.layout.attendance_spinner, items)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.attendance_spinner)

        // Apply the adapter to the spinner
        binding.spinner.adapter = adapter


//        binding.spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                listener?.onSpinnerItemSelection(position, 1)
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
//                    "Attendance" -> {
//                        listener?.onSpinnerItemSelection(position,1,null)
////                        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
////                            AttendanceFragment()
////                        ).commit()
//
//                    }
                    1 -> {
                        listener?.onSpinnerItemSelection(position,1,listener)
//                        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
//                            HolidaysFragment()
//                        ).commit()

                    }
                    2 -> {
                        listener?.onSpinnerItemSelection(position,2,listener)
//                        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
//                            LeavesFragment()
//                        ).commit()

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing if nothing is selected
            }
        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        listener = null
//    }


    private fun setData(position: Int) {

        binding.popLeaves.checkoutdate.setText(parseTimeData(attList!![position].date))
        binding.popLeaves.checkindate.setText(parseTimeData(attList!![position].date))

//        if(attList!![position].start_time != null)  binding.popLeaves.checkintime.setText(againparseTimeData(parseTimeFromDate(attList!![position].start_time!!)))
//        if(attList!![position].end_time != null)  binding.popLeaves.checkoutime.setText(againparseTimeData(parseTimeFromDate(attList!![position].end_time!!)))

    }

    private fun showDatePickerDialogEdit(i: Int) {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create and show the DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireActivity(),R.style.CustomDatePickerDialogTheme,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the TextView with the selected date
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"

                if(i == 1){
                    binding.popLeaves.checkindate.setText(selectedDate)
                    editStartDate = selectedDate
//                    Log.d("timeaaaaaaaaaa",editStartDate!!)
                }
                if(i == 2){
                    binding.popLeaves.checkoutdate.setText(selectedDate)
                    editEndDate = selectedDate
//                    Log.d("timeaaaaaaaaaa",editEndDate!!)
                }

            },
            year, month, day
        )

        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.buttonbgcolor))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.buttonbgcolor))

    }

    private fun showTimePickerDialog(i: Int) {
        // Get the current time as the default values for the picker
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and show it
        val timePickerDialog = TimePickerDialog(
            requireActivity(),R.style.CustomDatePickerDialogTheme,
            { _, selectedHour, selectedMinute ->
                // Update the TextView with the selected time
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                if (i == 1) {
                    binding.popLeaves.checkintime.setText(formattedTime)
                    editTimeStart = formattedTime
//                    Log.d("timeaaaaaaaaaa", editTimeStart!!)
                } else if (i == 2) {
                    if (editTimeStart == null) {
                        Toast.makeText(requireActivity(), "Please select the check-in time first", Toast.LENGTH_SHORT).show()
                    } else {
                        val startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(editTimeStart!!)
                        val endTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(formattedTime)

                        if (endTime <= startTime) {
                            Toast.makeText(requireActivity(), "Check-out time must be greater than check-in time", Toast.LENGTH_SHORT).show()
                        } else {
                            binding.popLeaves.checkoutime.setText(formattedTime)
                            editEndTime = formattedTime
//                            Log.d("timeaaaaaaaaaa", editEndTime!!)
                        }
                    }
                }
            },
            hour,
            minute,
            true // 24-hour time format
        )

        timePickerDialog.show()
        timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.buttonbgcolor))
        timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.buttonbgcolor))

    }

    private fun observeEditAttendance(){
        lifecycleScope.launchWhenStarted {
            with(viewModel){
                observeEditAttendanceFlow.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse

//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.statusCode == 200){
                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                                binding.popLeaves.attendanceEditScreenApply.visibility = View.GONE
                                CommonMethods.switchFragment(requireActivity(),AttendanceFragment(listener))

                            }
                            if (it.statusCode == 400){
                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                            }


                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(
//                                this@LoginActivity,"Loading...",
//                                Toast.LENGTH_LONG).show()
                            Log.d("attendace","Loading")
                        }
                        is ApiState.ERROR -> {
                            Log.d("attendace","api_error")

                        }

                        else -> {}
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClicked(position: Int, attendanceFullData: AttendanceFullData) {
        binding.popLeaves.attendanceEditScreenApply.visibility = View.VISIBLE

        binding.popLeaves.cancebtn.setOnClickListener {
            binding.popLeaves.attendanceEditScreenApply.visibility = View.GONE
        }
//
//
        setData(position)
//
        binding.popLeaves.checkindate.setOnClickListener {
            showDatePickerDialogEdit(1)
        }
//
        binding.popLeaves.checkoutdate.setOnClickListener {
            showDatePickerDialogEdit(2)
        }
//
//
        binding.popLeaves.checkintime.setOnClickListener {
            showTimePickerDialog(1)
        }
//
        binding.popLeaves.checkoutime.setOnClickListener {
            showTimePickerDialog(2)
        }

        binding.popLeaves.nextbtn.setOnClickListener {

            val token = CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN)

//            val editAttendanceModel = EditAttendanceModel(editStartDate!!,editStartDate!!+" "+editTimeStart!!,editEndDate!!+" " +editEndTime!!,binding.popLeaves.reason.text.toString())

                val editAttendanceModel = EditAttendanceModel(binding.popLeaves.checkindate.text.toString(),binding.popLeaves.checkindate.text.toString()!!+" "+CommonMethods.convertTimeToGmt(binding.popLeaves.checkintime.text.toString()!!),binding.popLeaves.checkoutdate.text.toString()!!+" " +CommonMethods.convertTimeToGmt(binding.popLeaves.checkoutime.text.toString()!!),binding.popLeaves.reason.text.toString())

                viewModel.invokeEditAttendaceCall(token,editAttendanceModel)


        }
//

            }


    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setMenuButtonVisibility(false)
        (activity as? MainActivity)?.setQrVisibility(false)

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

        val attendanceRequestModel = AttendanceRequestModel(startDateStr!!,endDateStr!!)
        invokeAttendance(token!!,attendanceRequestModel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialognew() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Calculate the end of next month
        val nextMonth = (month + 1) % 12
        val nextYear = if (nextMonth == 0) year + 1 else year
        c.set(Calendar.YEAR, nextYear)
        c.set(Calendar.MONTH, nextMonth)
        c.set(Calendar.DAY_OF_MONTH, 1) // Set to the first day of next month
        c.add(Calendar.DATE, -1) // Subtract one day to get the last day of next month
        val maxDateInMillis = c.timeInMillis

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialogTheme,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date
                val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)

                // Check if selected date is the last day of current month
                val isLastDayOfMonth = selectedDay == c.getActualMaximum(Calendar.DAY_OF_MONTH)
                        && selectedMonth == month && selectedYear == year

                if (isLastDayOfMonth) {
                    Toast.makeText(requireContext(), "You cannot proceed to the next month.", Toast.LENGTH_SHORT).show()
                } else {
                    onDateSelected(selectedDate)
                }
            },
            year, month, day
        )

        // Set min date to current date (optional)
//        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

        // Set max date to the end of next month
        datePickerDialog.datePicker.maxDate = maxDateInMillis

        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.calendarBtn))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.calendarBtn))
    }


    private fun validateForm() {
        val isEditText1Filled = binding.popLeaves.checkindate.text.toString().isNotEmpty()
        val isEditText2Filled = binding.popLeaves.checkintime.text.toString().isNotEmpty()
        val isEditText3Filled = binding.popLeaves.checkoutdate.text.toString().isNotEmpty()
        val isEditText4Filled = binding.popLeaves.reason.text.toString().trim().isNotEmpty()
        val isEditText5Filled = binding.popLeaves.checkoutime.text.toString().isNotEmpty()



        binding.popLeaves.nextbtn.visibility = if (isEditText1Filled && isEditText2Filled && isEditText3Filled &&  isEditText4Filled && isEditText5Filled )
        {
            View.VISIBLE

        } else {
            View.GONE
        }

        binding.popLeaves.nextbtndisable.visibility = if (isEditText1Filled && isEditText2Filled && isEditText3Filled && isEditText4Filled && isEditText5Filled) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun parseTimeData(time:String):String{

        val timeExtract = time.split("T")
        return  timeExtract[0]
    }

    fun parseTimeFromDate(time:String):String{

        val timeExtract = time.split("T")
        return  timeExtract[1]
    }

    fun againparseTimeData(time:String):String{

        val timeExtract = time.split(".")
        return  timeExtract[0]
    }
}