package com.empcloud.empmonitor.ui.fragment.edit_client.client_address

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AnimRes
import androidx.annotation.RequiresApi
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail
import com.empcloud.empmonitor.databinding.FragmentEditClientAddressBinding
import com.empcloud.empmonitor.databinding.FragmentEditClientBinding
import com.empcloud.empmonitor.ui.fragment.edit_client.client_edit_map.ClientEditUpdateMapFragment
import com.empcloud.empmonitor.ui.fragment.edit_client.first.EditClientFragment
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditClientAddressFragment : Fragment() {

    private lateinit var binding:FragmentEditClientAddressBinding
    private lateinit var locationManager: LocationManager
    private lateinit var locationProviderReceiver: BroadcastReceiver
    private var clientDetails: ClientFetchDetail? = null
    private var lat:Double? = null
    private var lon:Double? = null
    private var countryCode:String? = null
    private var countryname:String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clientDetails = arguments?.getSerializable(Constants.CLIENT_DETIALS_BUNDLE) as? ClientFetchDetail

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditClientAddressBinding.inflate(layoutInflater, container,false)

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Initialize the BroadcastReceiver
        locationProviderReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                binding.switchbtn.isChecked = locationEnabled
            }
        }
        switchToggle()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.addressfirst.setText(clientDetails!!.address1)
//        binding.addressSecond.setText(clientDetails!!.address2)
//        binding.city.setText(clientDetails!!.city)
//        binding.state.setText(clientDetails!!.state)
//        binding.zip.setText(clientDetails!!.zipCode)


        countryCode = arguments?.getString(Constants.COUNTRYCODE)
        countryname = arguments?.getString(Constants.COUNTRYNAME)
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateForm()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        validateForm()
        binding.addressfirst.addTextChangedListener(watcher)
        binding.city.addTextChangedListener(watcher)
        binding.state.addTextChangedListener(watcher)
//        binding.zip.addTextChangedListener(watcher)

        binding.backbtn.setOnClickListener {

            val editFragmentback = EditClientFragment()
            val args = Bundle().apply {

                putSerializable(Constants.CLIENT_DETIALS_BUNDLE,clientDetails)
            }

            editFragmentback.arguments = args
            CommonMethods.switchFragment(requireActivity(),editFragmentback)
        }

        binding.addAddressSearch.setOnClickListener {

            val fragment = ClientEditUpdateMapFragment()
            val args = Bundle().apply {
                putSerializable(Constants.CLIENT_DETIALS_BUNDLE,clientDetails)
            }
            fragment.arguments = args
            CommonMethods.switchFragment(requireActivity(),fragment)
//            CommonMethods.switchFragment(requireActivity(),ClientEditUpdateMapFragment())
        }

        binding.addAddressManually.setOnClickListener {

            val fragment = ClientEditUpdateMapFragment()
            val args = Bundle().apply {
                putSerializable(Constants.CLIENT_DETIALS_BUNDLE,clientDetails)
            }
            fragment.arguments = args
            CommonMethods.switchFragment(requireActivity(),fragment)
//            CommonMethods.switchFragment(requireActivity(),ClientEditUpdateMapFragment())
        }

        binding.addAddressBtn.setOnClickListener {

            val fragment = EditClientFragment()
            val args = Bundle().apply {
                putSerializable(Constants.CLIENT_DETIALS_BUNDLE,clientDetails)
                putString(Constants.ADDRESS_CLIENT_1,binding.addressfirst.text.toString())
                putString(Constants.CITY_UPDATE_1,binding.city.text.toString())
                putString(Constants.STATE_UPDATE_1,binding.state.text.toString())
                putString(Constants.ZIP_UPDATE_1,binding.zip.text.toString())
                putDouble(Constants.LON_UPDATE_1,lon!!)
                putDouble(Constants.LAT_UPDATE_1,lat!!)
                putString(Constants.COUNTRYCODE_1,countryCode)
                putString(Constants.COUNTRYNAME_1,countryname)

//                Log.d("thrher",binding.addressfirst.text.toString())
//                Log.d("thrher",lon.toString())
            }
            fragment.arguments = args
                CommonMethods.switchFragment(requireActivity(),fragment)
        }
        binding.addressfirst.setText(arguments?.getString(Constants.ADDRESS_CLIENT))
        binding.city.setText(arguments?.getString(Constants.CITY_UPDATE))
        binding.state.setText(arguments?.getString(Constants.STATE_UPDATE))
        binding.zip.setText(arguments?.getString(Constants.ZIP_UPDATE))
        lat = arguments?.getDouble(Constants.LAT_UPDATE)
        lon = arguments?.getDouble(Constants.LON_UPDATE)

        val pic = CommonMethods.getSharedPrefernce(requireActivity(),Constants.CLIENT_EDIT_UPDATE_PROFILE)
        if (!pic.isNullOrEmpty()){

            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
            Picasso.get().load(pic).into(binding.userPic)
        }
        else if (!clientDetails?.clientProfilePic.isNullOrEmpty()){

            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
            Picasso.get().load(clientDetails!!.clientProfilePic).into(binding.userPic)
        }


    }

    private fun validateForm() {
        val isEditText1Filled = binding.addressfirst.text.toString().isNotEmpty()
//        val isEditText2Filled = binding.addressSecond.text.toString().isNotEmpty()
        val isEditText3Filled = binding.state.text.toString().isNotEmpty()
        val isEditText4Filled = binding.city.text.toString().isNotEmpty()
//        val isEditText5Filled = binding.zip.text.toString().isNotEmpty()


        binding.addAddressBtn.visibility = if (isEditText1Filled && isEditText3Filled && isEditText4Filled )
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

}