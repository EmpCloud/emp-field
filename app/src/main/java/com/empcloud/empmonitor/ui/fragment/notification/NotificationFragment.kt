package com.empcloud.empmonitor.ui.fragment.notification

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.notification.Data
import com.empcloud.empmonitor.databinding.FragmentNotificationBinding
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity
import com.empcloud.empmonitor.ui.adapters.NotificationRecyclerAdapter
import com.empcloud.empmonitor.ui.fragment.home.HomeFragment
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment
import com.empcloud.empmonitor.ui.listeners.NotificationRecyclerItemClikedListener
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NotificationFragment(private val listener: OnFragmentChangedListener? = null) : Fragment(),NotificationRecyclerItemClikedListener {

    private lateinit var binding:FragmentNotificationBinding
    private val viewModel by viewModels<NotificationViewModel> ()
    private lateinit var notificationRecyclerAdapter: NotificationRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentNotificationBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isAllReadedNotification = CommonMethods.getSharedPrefernceBoolean(requireActivity(),Constants.NOTIFICATION_ALL_READ)
        if (!isAllReadedNotification)  viewModel.invokeGetNotification(CommonMethods.getSharedPrefernce(requireActivity(),Constants.AUTH_TOKEN))

        binding.backbtn.setOnClickListener {

            CommonMethods.switchFragment(requireActivity(),HomeFragment(listener))

        }

        binding.readall.setOnClickListener {


            animateClose()

        }

        observeNotificationData()
    }

    private fun animateClose() {

        val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.slid_recycler)
        binding.notificationRecycler.startAnimation(fadeOut)
        fadeOut.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {
                // No action needed
            }

            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                binding.notificationRecycler.visibility = View.GONE
                CommonMethods.saveSharedPrefernceBoolean(requireActivity(),Constants.NOTIFICATION_ALL_READ,Constants.NOTIFICATION_ALL_READ,true)
            }

            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {
                // No action needed
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setMenuButtonVisibility(false)
    }

    private fun observeNotificationData(){
        lifecycleScope.launch {
            with(viewModel){
                observenNotificationFlow.collect{res ->
                    when(res){

                        is ApiState.ERROR -> {}
                        ApiState.LOADING -> {}
                        is ApiState.SUCESS -> {

                            val it = res.getResponse

                            if (it.statusCode == 200){

//                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                                initRecycler(it.body.data)
                            }

                            if (it.statusCode == 400){

                                Toast.makeText(requireContext(),it.body.message,Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initRecycler(data: Data) {

        binding.notificationRecycler.visibility = View.VISIBLE
        binding.notificationRecycler.setHasFixedSize(false)
        binding.notificationRecycler.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)

        notificationRecyclerAdapter = NotificationRecyclerAdapter(requireContext(),data,this)
        binding.notificationRecycler.adapter = notificationRecyclerAdapter
        notificationRecyclerAdapter.onAttachedToRecyclerView(binding.notificationRecycler)


    }

    override fun onNotificationItemClicked(date: String) {

        Log.d("GetDate","$date")
        val fragment = TaskHomeFragment(listener)
        val args = Bundle().apply {

            putString(Constants.ITEM_DATE,date)
            putInt(Constants.TASK_STATUS,0)
            putBoolean(Constants.IS_PENDING_TASK,true)
        }
        fragment.arguments = args
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment).commit()

    }

}