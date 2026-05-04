package com.empcloud.empmonitor.ui.fragment.task.select_client_3

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail
import com.empcloud.empmonitor.data.remote.response.create_task.CreateTaskDetail
import com.empcloud.empmonitor.databinding.FragmentSelectClientBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.ui.adapters.SelectClientAdapter
import com.empcloud.empmonitor.ui.fragment.attendance.AttendanceFragment
import com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeViewModel
import com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task.AddTaskFragment
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.ui.listeners.SelectClientItemRecyclerListener
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectClientFragment constructor(private val listener: OnFragmentChangedListener? = null): Fragment(),SelectClientItemRecyclerListener {

    private var lat: Double? = null
    private var lon: Double? = null

    private lateinit var binding:FragmentSelectClientBinding
    private  val viewModel by viewModels<ClientHomeViewModel>()
    var taskList:MutableList<ClientFetchDetail>? = null
    private lateinit var selectClientAdaper:SelectClientAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var taskName:String? = null
    private var time:String? = null
    private var picresponse:String? = null
    private var picdesc:String? = null
    private var taskdesc:String? = null


    companion object {
        fun newInstance() = SelectClientFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        taskName = arguments?.getString("taskname")
        time = arguments?.getString("time")
        taskdesc = arguments?.getString("desc")
        picresponse = arguments?.getString("picresponse")
        picdesc = arguments?.getString("picdesc")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectClientBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.enablebtn.setOnClickListener {
//            CommonMethods.switchFragment(requireActivity(),AddTaskFragment())
//        }

        binding.backbtn.setOnClickListener {

            CommonMethods.switchFragment(requireActivity(),AddTaskFragment())
        }
        checkLocationPermission()
        viewModel.invokeFetchClient(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN))
        observeClientFetchCall()

    }

    private fun observeClientFetchCall(){
        lifecycleScope.launch() {
            with(viewModel){
                observeClientFetch.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse

//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.statusCode == 200){
//                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                                initRecycler(it.body.data)
                                binding.progressBar.visibility = View.GONE
                            }
                            if (it.statusCode == 400){

                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                            }


                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(
//                                this@LoginActivity,"Loading...",
//                                Toast.LENGTH_LONG).show()
                            Log.d("attendace","Loading")
                            binding.progressBar.visibility = View.VISIBLE
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

    private fun initRecycler(data: List<ClientFetchDetail>) {

        taskList = data.toMutableList()
        binding.selectClientRecycler.setHasFixedSize(false)
        binding.selectClientRecycler.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,false)

        selectClientAdaper = SelectClientAdapter(requireContext(),this,taskList!!,lat,lon){ isSelected ->
            binding.enablebtn.visibility = if (isSelected) View.VISIBLE else View.GONE
            // Navigation logic when the button is enabled
            binding.enablebtn.setOnClickListener {
                if (isSelected) {
                    // Get the selected item position
                        val selectedItemPosition = selectClientAdaper.getSelectedPosition()

                        // Navigate to the next fragment and pass the selected item position data
                        val bundle = Bundle().apply {
                            putString(Constants.CLIENT_ID,selectClientAdaper.filteredItems[selectedItemPosition]._id)
                            putString(
                                Constants.CLIENT_NAME_SELECTED,
                                selectClientAdaper.filteredItems[selectedItemPosition].clientName
                            )

                            // Add any other data if needed
                        }

                        val nextFragment = AddTaskFragment()
                        nextFragment.arguments = bundle
                        CommonMethods.switchFragment(requireActivity(), nextFragment)

                }
            }

        }
        binding.selectClientRecycler.adapter = selectClientAdaper


        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectClientAdaper.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }


    override fun onItemClick(position: Int, createTaskDetail: CreateTaskDetail) {
        TODO("Not yet implemented")
    }

    private fun getCurrentLocation() {
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
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                location?.let {
                    val currentLat = it.latitude
                    val currentLon = it.longitude
                    // Use the current location
                    // e.g., update the RecyclerView adapter with current location
                    lat = currentLat
                    lon = currentLon
                } ?: run {
                    // Handle location being null
                    Toast.makeText(requireContext(), "Unable to get current location", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get current location", Toast.LENGTH_SHORT).show()
            }
    }

    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            // Permission already granted
            getCurrentLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted
                getCurrentLocation()
            } else {
                // Permission denied
                Toast.makeText(requireContext(), "Location permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        (activity as? MainActivity)?.setQrVisibility(false)
    }

}