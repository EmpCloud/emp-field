package com.empcloud.empmonitor.ui.fragment.client.finalladd

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.request.createclient.CreateClientModel
import com.empcloud.empmonitor.databinding.FragmentClientAddFinalBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.fragment.client.add_client_with_editoptions.AddClientEditOptionFragment
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientViewModel
import com.empcloud.empmonitor.ui.fragment.client.addcompleteaddress.ClientCompleteAddressFragment
import com.empcloud.empmonitor.ui.fragment.client.clientaddress.ClientAddressFragment
import com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeFragment
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientAddFinalFragment : Fragment() {

    private lateinit var binding: FragmentClientAddFinalBinding
    private var status:Int? = 0
    private var clientId:String? = null

    private val viewModel by viewModels<AddClientViewModel>()
    private var latD:Double? = null
    private var longd:Double? = null
    private lateinit var codePicker: CountryCodePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        status = arguments?.getInt(Constants.EDIT_BACK)
        clientId = arguments?.getString(Constants.CLIENT_ID_CREATED)

            latD = arguments?.getDouble(Constants.LATITUDE_CLIENT_NEW)
        longd = arguments?.getDouble(Constants.LONGITUDE_CLIENT_NEW)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientAddFinalBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        codePicker = binding.myPhoneInput

        val pref = requireActivity().getSharedPreferences(Constants.CLIENT_DETAILS,AppCompatActivity.MODE_PRIVATE)
        val name = pref.getString(Constants.CLIENT_NAME,"")
        val email = pref.getString(Constants.CLIENT_EMAIL,"")
        val mobile = pref.getString(Constants.CLIENT_PHONE,"")
        val category = pref.getString(Constants.CLIENT_CATEGORY,"")
        val address = pref.getString(Constants.ADDRESS_CLIENT,"")
        val countryCode = pref.getString(Constants.CLIENT_PHONE_COUTRY_NAME_CODE,"")

//        Log.d("Countrycode","$countryCode")
        binding.fullName.setText(name)
        binding.emailId.setText(email)
        binding.mobileNo.setText(mobile)
        binding.category.setText(category)
        binding.address.setText(address)
        binding.category.setText(CommonMethods.getSharedPrefernce(requireActivity(),Constants.CATEGORY_CLIENT))
        codePicker.setCountryForNameCode(countryCode)
//        Log.d("adfadfas",mobile.toString())

        binding.fullName.isEnabled = false
        binding.emailId.isEnabled = false
        binding.mobileNo.isEnabled = false
        binding.category.isEnabled = false
        binding.address.isEnabled = false
        codePicker.isEnabled = false

        binding.enableClientBtn.setOnClickListener {

            callApi()

            //commented for direct to home client

//            val fragment = AddClientEditOptionFragment()
//            val args = Bundle().apply {
//                putString("Name",binding.fullName.text.toString())
//                putString("Address",binding.address.text.toString())
//                putInt("status",1)
//                putString("mobileno",binding.mobileNo.text.toString())
//
//            }
//            fragment.arguments = args
//
//            CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.CLIENT_BITMAP_RECIEVE)
//            CommonMethods.saveSharedPrefernce(requireActivity(),Constants.CLIENT_BITMAP_RECIEVE,Constants.CLIENT_BITMAP_RECIEVE,"")
//
//            CommonMethods.switchFragment(requireActivity(),fragment)
//            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,ClientHomeFragment()).commit()

//            Toast.makeText(requireContext(),"Implement further",Toast.LENGTH_SHORT).show()
        }

        binding.backbtn.setOnClickListener {

            if(status != null){
                val fragment = ClientCompleteAddressFragment()
                val args = Bundle().apply {

                    putInt(Constants.EDIT_BACK_FULL,status!!)
                }
                fragment.arguments = args
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment).commit()
            }else{
                CommonMethods.switchFragment(requireActivity(),ClientCompleteAddressFragment())
            }

        }

        setUserPic()
        observeCreateClientCall()

    }


    private fun setUserPic(){

        val pic = CommonMethods.getBitmapFromSharedPreferences(requireContext(),Constants.CLIENT_BITMAP_RECIEVE)
        if(pic != null){
            binding.userPic.visibility = View.VISIBLE
            binding.userPicdisbale.visibility = View.GONE
            binding.userPic.setImageBitmap(pic)
        }
    }


    private fun callApi(){

        val prefShared = requireContext().getSharedPreferences(Constants.AUTH_TOKEN,AppCompatActivity.MODE_PRIVATE)
        val token = prefShared.getString(Constants.AUTH_TOKEN,"")

        val prefSecond = requireContext().getSharedPreferences(Constants.CLIENT_DETAILS,AppCompatActivity.MODE_PRIVATE)
        val name = prefSecond.getString(Constants.CLIENT_NAME,"")
        val email = prefSecond.getString(Constants.CLIENT_EMAIL,null)
        val mobile = prefSecond.getString(Constants.CLIENT_PHONE,"")
        val category = CommonMethods.getSharedPrefernce(requireActivity(),Constants.CATEGORY_CLIENT)
        val address = prefSecond.getString(Constants.ADDRESS_CLIENT,"")
        val city = prefSecond.getString(Constants.CITY_CLIENT,"")
        val state = prefSecond.getString(Constants.STATE_CLIENT,"")
        val zip = prefSecond.getString(Constants.ZIP_CLIENT,"")
        val lat = prefSecond.getLong(Constants.LATITUDE_CLIENT,0)
        val long = prefSecond.getLong(Constants.LONGITUDE_CLIENT,0)
        val country = prefSecond.getString(Constants.CLIENT_PHONE_COUTRY_CODE,null)
        var profilePic:String? = CommonMethods.getSharedPrefernce(requireActivity(),Constants.CLIENT_PROFILE_URL)
        if (profilePic!!.isEmpty())  profilePic = null

        // Use the value
        val createClientModel = CreateClientModel(name!!,email,mobile!!,category!!,country,address!!,
            null.toString(),
            country,state!!,city!!,zip!!,latD!!,longd!!,profilePic)
//        Log.d("sdfgkjldfhgjkldh",profilePic.toString())
        viewModel.invokeCreateClient(token!!,createClientModel)
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
                                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.CLIENT_BITMAP_RECIEVE)
                                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.CLIENT_DETAILS)
                                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.CLIENT_PROFILE_URL)
                                CommonMethods.clearAllValues(requireContext(),Constants.CLIENT_PROFILE_URL)

                                CommonMethods.clearAllValues(requireContext(),Constants.CLIENT_DETAILS)

//            CommonMethods.saveSharedPrefernce(requireActivity(),Constants.CLIENT_BITMAP_RECIEVE,Constants.CLIENT_BITMAP_RECIEVE,"")
//
//            CommonMethods.switchFragment(requireActivity(),fragment)
                                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,ClientHomeFragment()).commit()
//                                binding.progressBar.visibility = View.GONE
//                                clientId = it.body.data.clientId
//                                val fragment = ClientAddFinalFragment()
//                                val args = Bundle().apply {
//                                    putString(Constants.CLIENT_ID_CREATED,it.body.data.clientId)
//                                }
//                                fragment.arguments = args
//                                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment).commit()
//                                CommonMethods.saveSharedPrefernce(requireActivity(),Constants.BITMAP_RECIEVE,Constants.BITMAP_RECIEVE,"")

//                                Log.d("attendace","Inseide observer")
                            }
                            if (it.statusCode == 400){
                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
//                                binding.progressBar.visibility = View.GONE
//                                Log.d("attendace","Inseide observer")
                            }


                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(
//                                this@LoginActivity,"Loading...",
//                                Toast.LENGTH_LONG).show()
//                            Log.d("attendace","Loading")
//                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is ApiState.ERROR -> {
//                            Log.d("attendace","api_error")

                        }

                        else -> {}
                    }
                }
            }
        }
    }
}