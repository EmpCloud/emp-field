package com.empcloud.empmonitor.ui.fragment.update_address

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.databinding.ActivityMapAddressBinding
import com.empcloud.empmonitor.databinding.FragmentUpdateAddressBinding
import com.empcloud.empmonitor.databinding.FragmentUpdateProfileBinding
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity
import com.empcloud.empmonitor.ui.activity.mapaddress.MapShowActivity
import com.empcloud.empmonitor.ui.fragment.edit_client.client_address.EditClientAddressFragment
import com.empcloud.empmonitor.ui.fragment.update_profile.UpdateProfileFragment
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateAddressFragment : Fragment() {

    private lateinit var binding:FragmentUpdateAddressBinding
    private lateinit var currentPhotoPath:String
    private val REQUEST_CAMERA_PERMISSION = 121
    private val CAMERA_REQUEST_CODE = 131

    private lateinit var locationManager: LocationManager
    private lateinit var locationProviderReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Initialize the BroadcastReceiver
        locationProviderReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                binding.switchbtn.isChecked = locationEnabled
                binding.toast.visibility = View.GONE

            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateAddressBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addAddressSearch12.setOnClickListener {

            val updateFragement = UpdateAddressMapFragment()
            val args = Bundle().apply {

                putInt("fragementMap",2001)
            }
            updateFragement.arguments = args
            CommonMethods.switchFragment(requireActivity(),updateFragement)
//            moveToNext()

        }

        binding.nextbtn.setOnClickListener {
            if(isLocationEnabled()){
                moveToNext()
            }else{
                binding.toast.visibility = View.VISIBLE
            }

        }

        binding.addAddressManually.setOnClickListener {
            moveToNext()
        }

        binding.backbtn.setOnClickListener {
            moveToPrevious()
        }

        binding.cancebtnToast.setOnClickListener {
            binding.toast.visibility = View.GONE
        }

        setUserPic()
        switchToggle()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()

        switchToggle()

        requireContext().registerReceiver(
            locationProviderReceiver,
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION), Context.RECEIVER_NOT_EXPORTED
        )

        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        binding.switchbtn.isChecked = locationEnabled
        binding.toast.visibility = View.GONE
    }


    private fun moveToNext(){

        val fragment = UpdateAddressMapFragment()
        CommonMethods.switchFragment(requireActivity(),fragment)
    }

    private fun moveToPrevious(){

        CommonMethods.switchFragment(requireActivity(),UpdateProfileFragment())
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
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
        if(isLocationEnabled()){
            binding.toast.visibility = View.GONE
        }else{
            binding.toast.visibility = View.VISIBLE
        }
    }

    private fun showLocationOffDialog() {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

//        AlertDialog.Builder(this)
//            .setTitle("Location Services")
//            .setMessage("Please turn on location services manually in the settings.")
//            .setPositiveButton("Open Settings") { _, _ ->
//                val locationSettingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(locationSettingsIntent)
//            }
//            .setNegativeButton("Cancel") { _, _ ->
//                // If the user cancels, revert the switch to its previous state
//                binding.switchbtn.isChecked = locationEnabled
//            }
//            .setOnCancelListener {
//                // If the dialog is canceled by touching outside, revert the switch to its previous state
//                binding.switchbtn.isChecked = locationEnabled
//            }
//            .show()

        binding.toast.visibility = View.VISIBLE
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