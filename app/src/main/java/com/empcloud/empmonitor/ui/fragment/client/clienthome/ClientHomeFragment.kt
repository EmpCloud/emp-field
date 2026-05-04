package com.empcloud.empmonitor.ui.fragment.client.clienthome


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail
import com.empcloud.empmonitor.databinding.FragmentClientHomeBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.ui.adapters.ClientFetchRecyclerAdapter
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientFragment
import com.empcloud.empmonitor.ui.fragment.client.clientdirection.ClientDirectionFragment
import com.empcloud.empmonitor.ui.fragment.edit_client.update_client.UpdaeEditClientFragment
import com.empcloud.empmonitor.ui.listeners.ClientRecyclerItemClickListener
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientHomeFragment  constructor(private val listener: OnFragmentChangedListener? = null): Fragment(),ClientRecyclerItemClickListener {

    private lateinit var  binding :FragmentClientHomeBinding
    private val viewModel by viewModels<ClientHomeViewModel>()
    private lateinit var adapter: ClientFetchRecyclerAdapter
    private lateinit var deatilsList:List<ClientFetchDetail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sp = requireContext().getSharedPreferences(Constants.AUTH_TOKEN,AppCompatActivity.MODE_PRIVATE)
        val token = sp.getString(Constants.AUTH_TOKEN,"")
        viewModel.invokeFetchClient(token!!)

        binding.addClient.setOnClickListener {

            CommonMethods.clearAllValues(requireContext(),Constants.CLIENT_DETAILS)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,AddClientFragment()).commit()
        }

        binding.backbtn.setOnClickListener {
            val intent = Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
//            CommonMethods.switchFragment(requireActivity(),HomeFragment(listener))
        }

        observeClientFetchCall()
    }

    private fun observeClientFetchCall(){
//        Log.d("Response","Inseide observer")
        lifecycleScope.launch() {
            with(viewModel){
                observeClientFetch.collect{res->
                    when(res){
                        is ApiState.SUCESS -> {
                            val it = res.getResponse

//                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT);
                            if (it.statusCode == 200){
//                                Log.d("aaaaaaaaaa",it.body.data.toString())
//                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                                binding.progressBar.visibility = View.GONE
                                if (it.body.data.size > 1)
                                    initRecycler(it.body.data)
                                else {

                                    binding.clientRecycler.visibility = View.GONE
                                    binding.noClientFound.visibility = View.VISIBLE
                                }
                            }
                            if (it.statusCode == 400){

                                binding.progressBar.visibility = View.GONE
                                binding.clientRecycler.visibility = View.GONE
                                binding.noClientFound.visibility = View.VISIBLE
                                Toast.makeText(requireContext(),it.body.message, Toast.LENGTH_SHORT).show()
                            }


                        }
                        is ApiState.LOADING -> {
//                            Toast.makeText(
//                                this@LoginActivity,"Loading...",
//                                Toast.LENGTH_LONG).show()
                            binding.progressBar.visibility = View.VISIBLE
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


    private fun initRecycler(clientFetchDetail: List<ClientFetchDetail>) {

//        deatilsList = clientFetchDetail

        binding.clientRecycler.visibility = View.VISIBLE
        binding.noClientFound.visibility = View.GONE

        binding.clientRecycler.setHasFixedSize(false)
        binding.clientRecycler.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        adapter = ClientFetchRecyclerAdapter(requireContext(),getFilteredData(clientFetchDetail),this)
        binding.clientRecycler.adapter = adapter

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter the adapter's data based on the input
                adapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed after text is changed
            }
        })

        // Set up EditText listener
    }

    fun getFilteredData(clientFetchDetail: List<ClientFetchDetail>): List<ClientFetchDetail> {
        val data = clientFetchDetail
        return data.filter { !it.clientName.contains("Default Client", ignoreCase = true) }
    }

    override fun onItemClicked(position: Int, clientFetchDetail: ClientFetchDetail, tag: Int) {

        val sp = requireContext().getSharedPreferences(Constants.DIAL_NUMBER,AppCompatActivity.MODE_PRIVATE)
        sp.edit().putString(Constants.DIAL_NUMBER,clientFetchDetail.contactNumber).apply()
        if (tag == 1){
            val intent = Intent(Intent.ACTION_DIAL).apply {
                if(!clientFetchDetail.contactNumber.isNullOrEmpty())
                data = Uri.parse("tel:${clientFetchDetail.contactNumber}")
            }
            startActivity(intent)
        }

        if(tag == 2){
            if(!clientFetchDetail.contactNumber.isNullOrEmpty()){
                val uri = Uri.parse("smsto:${clientFetchDetail.contactNumber}")
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.putExtra("PHONE_NUMBER",clientFetchDetail.contactNumber)
                startActivity(intent)
            }
        }

        if(tag == 3){
            val fragment = ClientDirectionFragment(listener)
            val bundle = Bundle().apply {
                putString(Constants.DIRECTION_NAME,clientFetchDetail.clientName)
                putString(Constants.DIRECTION_ADDRESS,clientFetchDetail.address1)
                putDouble(Constants.LAT,clientFetchDetail.latitude.toDouble())
                putDouble(Constants.LON,clientFetchDetail.longitude.toDouble())
//                Log.d("lkjh",clientFetchDetail.latitude.toString())
//                Log.d("lkjh",clientFetchDetail.longitude.toString())

            }

            fragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment).commit()
        }

        if(tag == 4){

            val fragment = UpdaeEditClientFragment(listener)
            val bundle = Bundle().apply {

                CommonMethods.clearStringFromSharedPreferences(requireContext(),Constants.CLIENT_DETIALS_BUNDLE)
                putSerializable(Constants.CLIENT_DETIALS_BUNDLE,clientFetchDetail!!)
            }
//            bundle.putString(Constants.DIRECTION_NAME,clientFetchDetail.clientName)
//            bundle.putString(Constants.DIRECTION_ADDRESS,clientFetchDetail.address1)
            fragment.arguments = bundle
//            Log.d("emailcheckingclient",clientFetchDetail.emailId)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment).commit()

        }

    }


//    private fun initRecycler(it: List<AttendanceFullData>) {
//        binding.attendanceRecycler.setHasFixedSize(false)
//        binding.attendanceRecycler.layoutManager = LinearLayoutManager(requireContext(),
//            RecyclerView.VERTICAL,false)
//
//        binding.attendanceRecycler.adapter = AttendanceAdapter(requireContext(),it)
//    }



}