package com.empcloud.empmonitor.ui.fragment.client.addcompleteaddress

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
import com.empcloud.empmonitor.databinding.FragmentClientCompleteAddressBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.fragment.client.add_client_with_editoptions.AddClientEditOptionFragment
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientViewModel
import com.empcloud.empmonitor.ui.fragment.client.clientmap.ClinetMapShowFragment
import com.empcloud.empmonitor.ui.fragment.client.finalladd.ClientAddFinalFragment
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientCompleteAddressFragment  : Fragment() {

    private lateinit var binding: FragmentClientCompleteAddressBinding
    private lateinit var locationManager: LocationManager
    private lateinit var locationProviderReceiver: BroadcastReceiver
    private val viewModel by viewModels<AddClientViewModel>()
    private var status:Int? = 0
    private var clientId:String? = null

    private var latD:Double? = null
    private var longd:Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        status = arguments?.getInt(Constants.EDIT_BACK)
        latD = arguments?.getDouble(Constants.LATITUDE_CLIENT)
        longd = arguments?.getDouble(Constants.LONGITUDE_CLIENT)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientCompleteAddressBinding.inflate(layoutInflater,container,false)


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

        binding.addAddressSearch.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,ClinetMapShowFragment()).commit()
        }
        binding.addAddressBtn.setOnClickListener {

//            callApi()

            val fragment = ClientAddFinalFragment()
            val bundle = Bundle().apply {

                putDouble(Constants.LATITUDE_CLIENT_NEW,latD!! )
                putDouble(Constants.LONGITUDE_CLIENT_NEW,longd!! )

            }

            fragment.arguments = bundle
            CommonMethods.switchFragment(requireActivity(),fragment)


        }

        binding.backbtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,ClinetMapShowFragment()).commit()
        }

        setData()
        setUserPic()
        observeCreateClientCall()

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
        val isEditText5Filled = binding.zip.text.toString().isNotEmpty()


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

    private fun observeCreateClientCall(){

        lifecycleScope.launch {
            with(viewModel){
                observeCreateClientCall.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse

//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.statusCode == 200){

                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                                binding.progressBar.visibility = View.GONE
                                clientId = it.body.data.clientId
                                val fragment = ClientAddFinalFragment()
                                val args = Bundle().apply {
                                    putString(Constants.CLIENT_ID_CREATED,it.body.data.clientId)
                                }
                                fragment.arguments = args
                                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment).commit()
//                                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.BITMAP_RECIEVE,Constants.BITMAP_RECIEVE,"")

//                                Log.d("attendace","Inseide observer")
                            }
                            if (it.statusCode == 400){
                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                                binding.progressBar.visibility = View.GONE
//                                Log.d("attendace","Inseide observer")
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

    private fun callApi(){

        val prefShared = requireContext().getSharedPreferences(Constants.AUTH_TOKEN,AppCompatActivity.MODE_PRIVATE)
        val token = prefShared.getString(Constants.AUTH_TOKEN,"")

        val prefSecond = requireContext().getSharedPreferences(Constants.CLIENT_DETAILS,AppCompatActivity.MODE_PRIVATE)
        val name = prefSecond.getString(Constants.CLIENT_NAME,"")
        val email = prefSecond.getString(Constants.CLIENT_EMAIL,"")
        val mobile = prefSecond.getString(Constants.CLIENT_PHONE,"")
        val category = CommonMethods.getSharedPrefernce(requireActivity(),Constants.CATEGORY_CLIENT)
        val address = prefSecond.getString(Constants.ADDRESS_CLIENT,"")
        val city = prefSecond.getString(Constants.CITY_CLIENT,"")
        val state = prefSecond.getString(Constants.STATE_CLIENT,"")
        val zip = prefSecond.getString(Constants.ZIP_CLIENT,"")
        val lat = prefSecond.getLong(Constants.LATITUDE_CLIENT,0)
        val long = prefSecond.getLong(Constants.LONGITUDE_CLIENT,0)
        val profilePic = CommonMethods.getSharedPrefernce(requireActivity(),Constants.CLIENT_PROFILE_URL)

        val latConverted = longToDouble(lat)
        val longConverted = longToDouble(long)

//        Log.d("latloncheck","$latD $longd")
        val fragment = AddClientEditOptionFragment()
        val bundle = Bundle()
        bundle.putString(Constants.DIRECTION_NAME,name)
        bundle.putString(Constants.DIRECTION_ADDRESS,address)
        bundle.putDouble(Constants.LAT_SEC,lat.toDouble())
        bundle.putDouble(Constants.LON_SEC,long.toDouble())
        bundle.putString(Constants.CLIENT_ADDRES1,binding.addressfirst.text.toString())
//        Log.d("adfadfas",binding.addressfirst.text.toString())
//        Log.d("categoruText",category.toString())
        fragment.arguments = bundle
        // Use the value
        val createClientModel = CreateClientModel(name!!,email!!,mobile!!,category!!,null,address!!,
            null.toString(),
            null.toString(),state!!,city!!,zip!!,latD!!,longd!!,profilePic)
        viewModel.invokeCreateClient(token!!,createClientModel)
    }

    fun longToDouble(longValue: Long): Double {
        return longValue.toDouble()
    }

    fun base64ToBitmap(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    private fun setUserPic(){

        val pic = CommonMethods.getBitmapFromSharedPreferences(requireContext(),Constants.CLIENT_BITMAP_RECIEVE)
        if(pic != null){
            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
            binding.userPic.setImageBitmap(pic)
        }
    }

    private fun setData(){

        val sp = requireContext().getSharedPreferences(Constants.CLIENT_DETAILS,AppCompatActivity.MODE_PRIVATE)

        val address = sp.getString(Constants.ADDRESS_CLIENT,"")
        val city = sp.getString(Constants.CITY_CLIENT,"")
        val state = sp.getString(Constants.STATE_CLIENT,"")
        val zip = sp.getString(Constants.ZIP_CLIENT,"")

//        Log.d("cccccccccccc",city.toString())
        binding.addressfirst.setText(address)
        binding.city.setText(city)
        binding.state.setText(state)
        binding.zip.setText(zip)

    }

    private fun updateTextData(){

        val sp = requireContext().getSharedPreferences(Constants.CLIENT_DETAILS,AppCompatActivity.MODE_PRIVATE)
        sp.edit().putString(Constants.ADDRESS_CLIENT,binding.addressfirst.text.toString()).apply()
        sp.edit().putString(Constants.CITY_CLIENT,binding.city.text.toString()).apply()
        sp.edit().putString(Constants.STATE_CLIENT,binding.state.text.toString()).apply()
        sp.edit().putString(Constants.ZIP_CLIENT,binding.zip.text.toString()).apply()


    }

    override fun onDestroy() {
        super.onDestroy()

        updateTextData()
    }


}