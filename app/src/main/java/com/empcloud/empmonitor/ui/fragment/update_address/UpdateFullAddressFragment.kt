package com.empcloud.empmonitor.ui.fragment.update_address

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.request.createclient.CreateClientModel
import com.empcloud.empmonitor.data.remote.request.updateprofile.UpdateProfileModel
import com.empcloud.empmonitor.databinding.FragmentUpdateFullAddressBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.fragment.client.add_client_with_editoptions.AddClientEditOptionFragment
import com.empcloud.empmonitor.ui.fragment.client.clientmap.ClinetMapShowFragment
import com.empcloud.empmonitor.ui.fragment.client.finalladd.ClientAddFinalFragment
import com.empcloud.empmonitor.ui.fragment.home.HomeFragment
import com.empcloud.empmonitor.ui.fragment.update_profile.UpdateProfileFragment
import com.empcloud.empmonitor.ui.fragment.update_profile.UpdateProfileViewModel
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpdateFullAddressFragment : Fragment() {

    private lateinit var binding:FragmentUpdateFullAddressBinding
    private lateinit var locationManager: LocationManager
    private lateinit var locationProviderReceiver: BroadcastReceiver
    private val viewModel by viewModels<UpdateProfileViewModel>()
    private var address1:String? = null
    private var city:String? = null
    private var state:String? = null
    private var zip:String? = null
    private var lat:Double? = null
    private var lon:Double? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        address1 = arguments?.getString(Constants.UPDATE_ADDRESS)
//        city = arguments?.getString(Constants.UPDATE_CITY)
//        state = arguments?.getString(Constants.UPDATE_STATE)
//        zip = arguments?.getString(Constants.UPDATE_ZIP)
//        lat = arguments?.getDouble(Constants.UPDATE_LAT)
//        lon = arguments?.getDouble(Constants.UPDATE_LON)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentUpdateFullAddressBinding.inflate(layoutInflater,container,false)

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Initialize the BroadcastReceiver
        locationProviderReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                binding.switchbtn.isChecked = locationEnabled
            }
        }

        address1 = arguments?.getString(Constants.UPDATE_ADDRESS)
        city = arguments?.getString(Constants.UPDATE_CITY)
        state = arguments?.getString(Constants.UPDATE_STATE)
        zip = arguments?.getString(Constants.UPDATE_ZIP)
        lat = arguments?.getDouble(Constants.UPDATE_LAT)
        lon = arguments?.getDouble(Constants.UPDATE_LON)

        if (lat != null && lon != null){

            CommonMethods.saveSharedPrefernce(requireActivity(),Constants.LAT_UPDATED,Constants.LAT_UPDATED,lat.toString())
            CommonMethods.saveSharedPrefernce(requireActivity(),Constants.LON_UPDATED,Constants.LON_UPDATED,lon.toString())

        }

        switchToggle()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addressfirst.setText(address1)
        binding.city.setText(city)
        binding.state.setText(state)
        binding.zip.setText(zip)

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                validateForm()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.addressfirst.addTextChangedListener(watcher)
        binding.city.addTextChangedListener(watcher)
        binding.state.addTextChangedListener(watcher)
//        binding.zip.addTextChangedListener(watcher)
        validateForm()

        binding.addAddressSearch.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                UpdateAddressMapFragment()
            ).commit()
        }

        binding.addAddressBtn.setOnClickListener {

            callApi()
        }

        binding.backbtn.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                UpdateAddressMapFragment()
            ).commit()
        }


        setUserPic()
        observeUpdateProfile()

    }

    private fun switchToggle(){


        binding.switchbtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switch is ON, check if location is enabled
                if (!isLocationEnabled()) {
                    // Location is not enabled, prompt user to enable it
                    val enableLocationIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(enableLocationIntent)
                }
            } else {
                // Switch is OFF
                showLocationOffDialog()
            }
        }

        // Check if location is enabled initially and update the Switch state
        binding.switchbtn.isChecked = isLocationEnabled()
    }

    private fun showLocationOffDialog() {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        AlertDialog.Builder(requireContext())
            .setTitle("Location Services")
            .setMessage("Please turn on location services manually in the settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                val locationSettingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(locationSettingsIntent)
            }
            .setNegativeButton("Cancel") { _, _ ->
                // If the user cancels, revert the switch to its previous state
                binding.switchbtn.isChecked = locationEnabled
            }
            .setOnCancelListener {
                // If the dialog is canceled by touching outside, revert the switch to its previous state
                binding.switchbtn.isChecked = locationEnabled
            }
            .show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        // Register the BroadcastReceiver to listen for location provider changes
        requireContext().registerReceiver(
            locationProviderReceiver,
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION), Context.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        // Unregister the BroadcastReceiver to stop listening for location provider changes
        requireContext().unregisterReceiver(locationProviderReceiver)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun validateForm() {
        val isEditText1Filled = binding.addressfirst.text.toString().isNotEmpty()
//        val isEditText2Filled = binding.addressSecond.text.toString().isNotEmpty()
        val isEditText3Filled = binding.state.text.toString().isNotEmpty()
        val isEditText4Filled = binding.city.text.toString().isNotEmpty()
//        val isEditText5Filled = binding.zip.text.toString().isNotEmpty()


        binding.addAddressBtn.visibility = if (isEditText1Filled && isEditText3Filled && isEditText4Filled)
        {
            View.VISIBLE

        } else {
            View.GONE
        }

        binding.addAddressBtndisable.visibility = if (isEditText1Filled && isEditText3Filled && isEditText4Filled ) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun observeUpdateProfile(){

        lifecycleScope.launch {
            with(viewModel){
                observerUpdataData.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse

//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.code == 200){
//                                Log.d("asgas",it.body.message)
                                android.widget.Toast.makeText(requireContext(),it.body.message, android.widget.Toast.LENGTH_SHORT).show()
                                requireActivity().supportFragmentManager.beginTransaction().replace(
                                    com.empcloud.empmonitor.R.id.fragmentContainer,
                                    UpdateProfileFragment()
                                ).commit()

                                android.util.Log.d("attendace","Inseide observer")
                            }
                            if (it.code == 400){
                                android.widget.Toast.makeText(requireContext(),it.body.message, android.widget.Toast.LENGTH_SHORT).show()
                                android.util.Log.d("attendace","Inseide observer")
                            }


                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(
//                                this@LoginActivity,"Loading...",
//                                Toast.LENGTH_LONG).show()
                            android.util.Log.d("attendace","Loading")
                        }
                        is ApiState.ERROR -> {
                            android.util.Log.d("attendace","api_error")

                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun callApi(){

        val sp = requireContext().getSharedPreferences(Constants.UPDATE_PROFILE_DATA,AppCompatActivity.MODE_PRIVATE)
        val name = sp.getString(Constants.UPDATE_NAME,"")
        val age = sp.getString(Constants.UPDATE_AGE,"")
        val mobileno = sp.getString(Constants.UPDATE_MOBILENO,null)
        val email = sp.getString(Constants.UPDATE_EMAIL,"")
        val gender = sp.getString(Constants.UPDATE_GENDER,"")
        val profile = sp.getString(Constants.UPDATE_PROFILE_NEW,null)

        val mn = CommonMethods.getSharedPrefernce(requireActivity(),Constants.UMN)
//        Log.d("updateprofielapi","$name")
//        Log.d("updateprofielapi","$age")
//        Log.d("updateprofielapi","$gender")
//        Log.d("updateprofielapi","$email")
//        Log.d("updateprofielapi","$profile")
//        Log.d("updateprofielapi","$address1")
//        Log.d("updateprofielapi","$lat.toString()")
//        Log.d("updateprofielapi","$lon.toString()")
//        Log.d("updateprofielapi","$city")
//        Log.d("updateprofielapi","$state")
//        Log.d("updateprofielapi","$zip")
//        Log.d("updateprofielapi","$mobileno")

        // Use the value
        val updateProfileModel = UpdateProfileModel(name!!,age!!,gender!!,email!!,profile,address1!!,"address",lat.toString(),lon.toString(),city!!,state!!,"null",zip!!,mobileno)


        viewModel.invokeUpdataDataCall(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN),updateProfileModel)
    }

    fun longToDouble(longValue: Long): Double {
        return longValue.toDouble()
    }


    private fun setUserPic(){

        val pic = CommonMethods.getSharedPrefernce(requireActivity(), Constants.BITMAP_RECIEVE_UPDATE)
        if(!pic.isNullOrEmpty()){
            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
//            binding.userPic.setImageBitmap(pic)
            Picasso.get().load(pic).into(binding.userPic)
        }
    }


}