package com.empcloud.empmonitor.ui.fragment.client.clientaddress

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
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.databinding.FragmentClientAddressBinding
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity
import com.empcloud.empmonitor.ui.activity.mapaddress.MapShowActivity
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientFragment
import com.empcloud.empmonitor.ui.fragment.client.addcompleteaddress.ClientCompleteAddressFragment
import com.empcloud.empmonitor.ui.fragment.client.clientmap.ClinetMapShowFragment
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientAddressFragment : Fragment() {

    private lateinit var binding: FragmentClientAddressBinding

    private val LOCATION_SETTINGS_REQUEST = 235
    private lateinit var locationManager: LocationManager
    private lateinit var locationProviderReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientAddressBinding.inflate(layoutInflater,container,false)

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Initialize the BroadcastReceiver
        locationProviderReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                binding.switchbtn.isChecked = locationEnabled
            }
        }
        setUserPic()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.addAddressSearch.setOnClickListener {
            moveToNext()
        }

//        binding.nextbtn.setOnClickListener {
//            moveToNext()
//        }

        binding.addAddressManually.setOnClickListener {
            moveToNext()
        }

        binding.backbtn.setOnClickListener {
            moveToPrevious()
        }

        switchToggle()
//        setUserPic()
    }


    private fun moveToNext(){
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,ClinetMapShowFragment()).commit()

    }

    private fun moveToPrevious(){

        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,AddClientFragment()).commit()

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
    }

//    private fun showLocationOffDialog() {
//        AlertDialog.Builder(requireContext())
//            .setTitle("Location Services")
//            .setMessage("Please turn off location services manually in the settings.")
//            .setPositiveButton("Open Settings") { _, _ ->
//                val locationSettingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(locationSettingsIntent)
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }

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

        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        binding.switchbtn.isChecked = locationEnabled


    }

    override fun onPause() {
        super.onPause()
        // Unregister the BroadcastReceiver to stop listening for location provider changes
        requireContext().unregisterReceiver(locationProviderReceiver)
    }


    private fun setUserPic(){

        val pic = CommonMethods.getBitmapFromSharedPreferences(requireContext(),Constants.CLIENT_BITMAP_RECIEVE)
        if(pic != null){
            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
            binding.userPic.setImageBitmap(pic)
        }
    }
}