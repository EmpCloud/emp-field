package com.empcloud.empmonitor.ui.fragment.holidays

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.holidays.HolidayList
import com.empcloud.empmonitor.databinding.FragmentHolidaysBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.ui.adapters.HolidaysRecyclerAdapter
import com.empcloud.empmonitor.ui.fragment.home.HomeFragment
import com.empcloud.empmonitor.ui.fragment.leaves.LeavesFragment
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HolidaysFragment constructor(private val listener: OnFragmentChangedListener? = null) : Fragment() {

    private lateinit var binding:FragmentHolidaysBinding
    private val viewModel by viewModels<HolidaysViewModel>()
//    private  var listener: OnFragmentChangedListener? = null
companion object {
    fun newInstance() = HolidaysFragment
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHolidaysBinding.inflate(layoutInflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerShow()
//        CommonSpinner.spinnerShow(binding.spinner,requireContext(),requireActivity() as FragmentActivity)
        val pref = requireContext().getSharedPreferences(Constants.AUTH_TOKEN,AppCompatActivity.MODE_PRIVATE)
        val token = pref.getString(Constants.AUTH_TOKEN,"")
        if(!token.isNullOrEmpty()){
            invokeHolidays(token)
        }

        binding.backbtn.setOnClickListener {

            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
//            val homeFragement = HomeFragment(listener)
//            requireActivity().supportFragmentManager.beginTransaction().replace(com.empcloud.empmonitor.R.id.fragmentContainer,homeFragement).commit()
        }
        observeHolidays()
    }

    fun invokeHolidays(accessToken:String){
        viewModel.invokeHolidayFetch(accessToken)
    }

    private fun observeHolidays(){
//        Log.d("Response","Inseide observer")
        lifecycleScope.launchWhenStarted {
            with(viewModel){
                observeHolidayData.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse
//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.statusCode == 200){

                                initRecycler(it.body.data)
                            }
                            if (it.statusCode == 400){
//                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
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

    private fun initRecycler(it: List<HolidayList>) {
        binding.holidayRecycler.setHasFixedSize(false)
        binding.holidayRecycler.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)


        binding.holidayRecycler.adapter = HolidaysRecyclerAdapter(requireContext(),it)
    }


    fun spinnerShow(){

        // Define the items to show in the spinner
        val items = listOf("Holidays","Attendance",  "Leaves")

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
//                listener?.onSpinnerItemSelection(position, 1)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//
//        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                // Perform actions based on the selected item
                when (position) {
                    1 -> {
                        listener?.onSpinnerItemSelection(position,0,listener)
//                        requireActivity().supportFragmentManager.beginTransaction().replace(
//                            R.id.fragmentContainer,
//                            AttendanceFragment()
//                        ).commit()

                    }
//                    2 -> {
//                        listener?.onSpinnerItemSelection(position,2,listener)
////                        requireActivity().supportFragmentManager.beginTransaction().replace(
////                            R.id.fragmentContainer,
////                            HolidaysFragment()
////                        ).commit()
//
//                    }
                    2 -> {
                        listener?.onSpinnerItemSelection(position,2, listener)
//                        requireActivity().supportFragmentManager.beginTransaction().replace(
//                            R.id.fragmentContainer,
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

//    private fun playAnimationLottie(){
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

//    override fun onDestroy() {
//        super.onDestroy()
//        listener = null
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }

    private fun showNoIcon(){

        binding.datashowcard.visibility = View.GONE
        binding.nothingfoundanimation.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setMenuButtonVisibility(false)
    }
}