package com.empcloud.empmonitor.ui.activity.mainactivity

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.location.LocationManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.databinding.ActivityMain1Binding
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity
import com.empcloud.empmonitor.ui.fragment.settings.SettingsFragment
import com.empcloud.empmonitor.ui.fragment.attendance.AttendanceFragment
import com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeFragment
import com.empcloud.empmonitor.ui.fragment.holidays.HolidaysFragment
import com.empcloud.empmonitor.ui.fragment.home.HomeFragment
import com.empcloud.empmonitor.ui.fragment.leaves.LeavesFragment
import com.empcloud.empmonitor.ui.fragment.map.MapCurrentFragment
import com.empcloud.empmonitor.ui.fragment.notification.NotificationFragment
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment
import com.empcloud.empmonitor.ui.fragment.update_profile.UpdateProfileFragment
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.empcloud.empmonitor.utils.NativeLib
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import kotlin.concurrent.thread

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),OnFragmentChangedListener {

    private lateinit var binding: ActivityMain1Binding
    private lateinit var fragmentContainer: FragmentContainerView
    private lateinit var bottomNavigationView: BottomNavigationView
    private var currentFragment: Fragment? = null

    private val PERMISSION_REQUEST_CODE = 1001
    private val PERMISSION_REQUEST_CODE_NOTIFICATION = 1002

    private lateinit var sideMenu: ConstraintLayout
    private var isMenuOpen = false

    private lateinit var handler: Handler
    private lateinit var locationCheckingRunnable:Runnable
    private lateinit var batteryCheckingRunnable:Runnable

    private var isGlobal = false

    private val channelId = "download_channel"
//    private lateinit var drawerLayout: DrawerLayout
//    private lateinit var navView: NavigationView
//    private lateinit var toggle: ActionBarDrawerToggle

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain1Binding.inflate(layoutInflater)
        setContentView(binding.root)

//        chagneApiKey()

//        highlightMenuItem(binding.sidebar.menuhomeicon,binding.sidebar.hometxt)

        fragmentContainer = binding.fragmentContainer
        val isUserGlobal = CommonMethods.getSharedPrefernceBoolean(this,Constants.IS_GLOBAL_USER)
         isGlobal = isUserGlobal

        if (savedInstanceState == null) {

            val homeFragment = HomeFragment(this@MainActivity)
            binding.customizebottomNavigationView.home.visibility = View.GONE
            binding.customizebottomNavigationView.homeSelect.visibility = View.VISIBLE
            binding.menu.visibility = View.VISIBLE
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, homeFragment)
            transaction.commit()
        }

        handler = Handler(Looper.getMainLooper())


        if (isGlobal) {

            binding.notify.visibility = View.GONE
            binding.customizebottomNavigationView.map.visibility = View.VISIBLE
            binding.customizebottomNavigationView.qrbottom.visibility = View.VISIBLE

            binding.customizebottomNavigationView.task.visibility = View.GONE
            binding.customizebottomNavigationView.client.visibility = View.GONE

            binding.sidebar.taskmenu.visibility = View.GONE
            binding.sidebar.menuclients.visibility = View.GONE

        }
        else{

            binding.notify.visibility = View.VISIBLE
            binding.customizebottomNavigationView.map.visibility = View.GONE
            binding.customizebottomNavigationView.qrbottom.visibility = View.GONE

            binding.customizebottomNavigationView.task.visibility = View.VISIBLE
            binding.customizebottomNavigationView.client.visibility = View.VISIBLE


            binding.sidebar.taskmenu.visibility = View.VISIBLE
            binding.sidebar.menuclients.visibility = View.VISIBLE
        }

        sideMenu = binding.sidebar.sidebarMenu

        binding.notify.setOnClickListener {

            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,NotificationFragment(this)).commit()
        }
        binding.locationalert.okayBtn.setOnClickListener {

            binding.locationalert.locationalertpop.visibility = View.GONE
        }

        binding.locationalert.cancelbtn.setOnClickListener {

            binding.locationalert.locationalertpop.visibility = View.GONE
        }

        binding.batteryalert.okayBtn.setOnClickListener {

            binding.batteryalert.batterypopup.visibility = View.GONE
        }

        binding.batteryalert.cancelbtn.setOnClickListener {

            binding.batteryalert.batterypopup.visibility = View.GONE
        }

        binding.sidebar.cancelbtn.setOnClickListener {
            closeMenu()
        }

        val menu:ImageView = findViewById(R.id.menu)
        menu.setOnClickListener {

            resetMenuItemColors()
            binding.sidebar.sidebarMenu.visibility = View.VISIBLE
            toggleMenu()
        }


          customizeNavigationBottom()

//        val profile:ImageView = findViewById(R.id.profile)
        binding.profile.setOnClickListener {

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, UpdateProfileFragment(this)).commit()
            binding.menu.visibility = View.GONE
        }

        binding.picmainshowCard.setOnClickListener {

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, UpdateProfileFragment(this)).commit()
            binding.menu.visibility = View.GONE
        }

        binding.sidebar.menuHome.setOnClickListener {

            closeMenu()
            highlightMenuItem(binding.sidebar.menuhomeicon,binding.sidebar.hometxt)

//            val intent = Intent(applicationContext,MainActivity::class.java)
//            startActivity(intent)
//            finish()
//            switchFragment(HomeFragment(this))

        }

        binding.sidebar.menuattendance.setOnClickListener {

            closeMenu()
            binding.menu.visibility = View.GONE
            highlightMenuItem(binding.sidebar.atticon,binding.sidebar.attendacncetxt)
            switchFragment(AttendanceFragment(this))

        }

        binding.sidebar.menuleaves.setOnClickListener {

            closeMenu()
            binding.menu.visibility = View.GONE
            highlightMenuItem(binding.sidebar.lvivon,binding.sidebar.leavestxt)
            switchFragment(LeavesFragment(this))

        }

        binding.sidebar.menuholidays.setOnClickListener {

            closeMenu()
            binding.menu.visibility = View.GONE
            highlightMenuItem(binding.sidebar.hllicon,binding.sidebar.holidaystxt)
            switchFragment(HolidaysFragment(this))

        }

        binding.sidebar.taskmenu.setOnClickListener {


            binding.sidebar.subtaskexpand.visibility = if (binding.sidebar.subtaskexpand.visibility == View.VISIBLE) View.GONE else View.VISIBLE


//            closeMenu()
//            highlightMenuItem(binding.sidebar.taskic,binding.sidebar.tasktxt)
//            switchFragment(TaskHomeFragment(this))

        }

        binding.sidebar.subcurrenttxt.setOnClickListener {
            closeMenu()
            binding.menu.visibility = View.GONE
            val fragment = TaskHomeFragment(this)
            val args = Bundle().apply {
                putInt(Constants.TASK_STATUS,1)
            }
            fragment.arguments = args
            switchFragment(fragment)
            taskSelection()
            binding.sidebar.subtaskexpand.visibility = View.GONE
        }

        binding.sidebar.subfinishtxt.setOnClickListener {
            closeMenu()
            binding.menu.visibility = View.GONE
            val fragment = TaskHomeFragment(this)
            val args = Bundle().apply {
                putInt(Constants.TASK_STATUS,4)
            }
            fragment.arguments = args
            switchFragment(fragment)
            taskSelection()
            binding.sidebar.subtaskexpand.visibility = View.GONE
        }


        binding.sidebar.menuclients.setOnClickListener {

            closeMenu()
            binding.menu.visibility = View.GONE
            highlightMenuItem(binding.sidebar.clicoon,binding.sidebar.clientstxt)
            switchFragment(ClientHomeFragment(this))
            clientSelection()

        }

        binding.sidebar.menusettings.setOnClickListener {

            closeMenu()
            binding.menu.visibility = View.GONE
            highlightMenuItem(binding.sidebar.setticon,binding.sidebar.settingstxt)
            switchFragment(SettingsFragment(this))
            settingSelection()

        }

        binding.sidebar.menuItemLogout.setOnClickListener {

            highlightMenuItem(binding.sidebar.logoutIcon,binding.sidebar.logoutText)
            closeMenu()
            CommonMethods.clearStringFromSharedPreferences(applicationContext,Constants.BITMAP_RECIEVE)
            CommonMethods.clearStringFromSharedPreferences(applicationContext,Constants.BITMAP_RECIEVE_UPDATE)
            CommonMethods.clearStringFromSharedPreferences(applicationContext,Constants.LAST_MODE_SELECTED)

            CommonMethods.clearStringFromSharedPreferences(applicationContext,Constants.PROFILE_PIC_URL_USER)

            CommonMethods.clearStringFromSharedPreferences(applicationContext,Constants.IS_CHECKEDIN)
            CommonMethods.clearAllValues(applicationContext,Constants.IS_GLOBAL_USER)

            // Cancel auto-checkout alarm and clear auto check-in data
            CommonMethods.cancelAutoCheckout(applicationContext)
            CommonMethods.clearStringFromSharedPreferences(applicationContext, Constants.AUTO_CHECK_IN_TIME)
            CommonMethods.clearStringFromSharedPreferences(applicationContext, Constants.CHECK_IN_METHOD)
            CommonMethods.clearAllValues(applicationContext, Constants.AUTO_CHECK_IN_BY_MOBILE)
            CommonMethods.clearAllValues(applicationContext, Constants.AUTO_CHECK_IN_BY_GEO_FENCING)
            CommonMethods.clearAllValues(applicationContext, Constants.IS_GEO_FENCING_ON)
            CommonMethods.clearAllValues(applicationContext, Constants.GEO_LOG_STATUS)

            val sp = getSharedPreferences(Constants.AUTH_TOKEN, MODE_PRIVATE)
            sp.edit().putString(Constants.AUTH_TOKEN,"").apply()
            val intent = Intent(applicationContext,LoginOptionsActivity::class.java)
            startActivity(intent)
            finish()

        }

        binding.customizebottomNavigationView.homeSelect.setOnClickListener {

            CommonMethods.switchFragment(this,HomeFragment(this))
        }

        binding.customizebottomNavigationView.taskSelected.setOnClickListener {

            CommonMethods.switchFragment(this,TaskHomeFragment(this))
        }

        binding.customizebottomNavigationView.clientSelected.setOnClickListener {

            CommonMethods.switchFragment(this,ClientHomeFragment(this))
        }

        binding.customizebottomNavigationView.settingsSelected.setOnClickListener {

            CommonMethods.switchFragment(this,SettingsFragment(this))
        }

        binding.customizebottomNavigationView.slelectedmap.setOnClickListener {

            CommonMethods.switchFragment(this,MapCurrentFragment(this))
        }
        binding.qrshow.setOnClickListener {

            binding.qrshowpopup.qrpopup.visibility = View.VISIBLE
            val sp = getSharedPreferences(Constants.USER_ID, MODE_PRIVATE)
            val empid = sp.getString(Constants.USER_ID,"")
            if (!empid.isNullOrEmpty())  getQr(empid)
            setUserDataQr()
        }

        checkPermissions()
        val layout = findViewById<View>(R.id.qrshowpopup)
        val cancelbtn = layout.findViewById<ImageView>(R.id.cancelbtnqr)
        val downloadButton: LinearLayout = layout.findViewById(R.id.downloadqr)
        val cardPopUp: CardView = layout.findViewById(R.id.dataCard)
        binding.qrshowpopup.downloadqr.setOnClickListener {

            val sp = getSharedPreferences(Constants.USER_ID, MODE_PRIVATE)
            val empid = sp.getString(Constants.USER_ID,"")
            val imageurl = NativeLib().getQRLinkLive() + empid
            val sharedPred = getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
            val name = CommonMethods.getSharedPrefernce(this,Constants.LOGIN_NAME_PERSON)
            cancelbtn.visibility = View.GONE
            downloadButton.visibility = View.GONE
            val layoutParams = cardPopUp.layoutParams  as ViewGroup.MarginLayoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.marginStart = 0
            layoutParams.marginEnd = 0
//            layoutParams.setMargins(0,0,0,0)
            cardPopUp.layoutParams = layoutParams

            createPdf(layout,"$name.pdf")

            cancelbtn.visibility = View.VISIBLE
            downloadButton.visibility = View.VISIBLE

            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
//            downloadImageAndSaveAsPdf(imageurl)
        }

        binding.qrshowpopup.cancelbtnqr.setOnClickListener {

            binding.qrshowpopup.qrpopup.visibility = View.GONE
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//
//            if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//
//                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), PERMISSION_REQUEST_CODE_NOTIFICATION)
//            }
//        }

        if (!isLocationEnabled()) binding.locationalert.locationalertpop.visibility = View.VISIBLE
        else binding.locationalert.locationalertpop.visibility = View.GONE

//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)
        createNotificationChannel()
        batteryChecking()
        locationChecking()

    }

//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//
//                    ActivityCompat.requestPermissions(
//                        this,
//                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
//                        1
//                    )
////
////                Toast.makeText(applicationContext,"Notification permission required to stay update",Toast.LENGTH_SHORT).show()
//            }
//
//    }
    private fun toggleMenu() {
        if (isMenuOpen) {
            closeMenu()
        } else {
            openMenu()
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }


    private fun customizeNavigationBottom() {

        binding.customizebottomNavigationView.home.setOnClickListener {

            binding.qrshowpopup.qrpopup.visibility = View.GONE
            homeSelection()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment(this)).commit()

        }

        binding.customizebottomNavigationView.client.setOnClickListener {

            binding.qrshowpopup.qrpopup.visibility = View.GONE
            clientSelection()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ClientHomeFragment(this)).commit()

        }

        binding.customizebottomNavigationView.task.setOnClickListener {

            binding.qrshowpopup.qrpopup.visibility = View.GONE
            taskSelection()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TaskHomeFragment(this)).commit()

        }

        binding.customizebottomNavigationView.settings.setOnClickListener {

            binding.qrshowpopup.qrpopup.visibility = View.GONE
            settingSelection()
            supportFragmentManager.beginTransaction().replace(
                R.id.fragmentContainer,
                SettingsFragment(this)
            ).commit()

        }

        binding.customizebottomNavigationView.map.setOnClickListener {

            binding.qrshowpopup.qrpopup.visibility = View.GONE
            mapSelection()
            CommonMethods.switchFragment(this,MapCurrentFragment(this))
        }

        binding.customizebottomNavigationView.qrbottom.setOnClickListener {

            binding.qrshowpopup.qrpopup.visibility = View.GONE
            qrSelection()
            binding.qrshowpopup.qrpopup.visibility = View.VISIBLE
            val sp = getSharedPreferences(Constants.USER_ID, MODE_PRIVATE)
            val empid = sp.getString(Constants.USER_ID,"")
            if (!empid.isNullOrEmpty())  getQr(empid)
            setUserDataQr()
        }

    }

    private fun settingSelection() {

        binding.customizebottomNavigationView.settingsSelected.visibility = View.VISIBLE
        binding.customizebottomNavigationView.settings.visibility = View.GONE
        binding.menu.visibility = View.GONE

        if (isGlobal)  {

            binding.customizebottomNavigationView.map.visibility = View.VISIBLE
            binding.customizebottomNavigationView.qrbottom.visibility = View.VISIBLE


            binding.customizebottomNavigationView.slelectedmap.visibility = View.GONE
            binding.customizebottomNavigationView.slelectedqr.visibility = View.GONE

        }else{

            binding.customizebottomNavigationView.task.visibility = View.VISIBLE
            binding.customizebottomNavigationView.client.visibility = View.VISIBLE


            binding.customizebottomNavigationView.taskSelected.visibility = View.GONE
            binding.customizebottomNavigationView.clientSelected.visibility = View.GONE
        }
        binding.customizebottomNavigationView.home.visibility = View.VISIBLE

        binding.customizebottomNavigationView.homeSelect.visibility = View.GONE
    }

    private fun taskSelection() {

        binding.customizebottomNavigationView.taskSelected.visibility = View.VISIBLE
        binding.customizebottomNavigationView.task.visibility = View.GONE
        binding.menu.visibility = View.GONE

        binding.customizebottomNavigationView.home.visibility = View.VISIBLE
        binding.customizebottomNavigationView.settings.visibility = View.VISIBLE
        binding.customizebottomNavigationView.client.visibility = View.VISIBLE

        binding.customizebottomNavigationView.settingsSelected.visibility = View.GONE
        binding.customizebottomNavigationView.clientSelected.visibility = View.GONE
        binding.customizebottomNavigationView.homeSelect.visibility = View.GONE
    }

    private fun clientSelection() {

        binding.customizebottomNavigationView.clientSelected.visibility = View.VISIBLE
        binding.customizebottomNavigationView.client.visibility = View.GONE
        binding.menu.visibility = View.GONE

        binding.customizebottomNavigationView.home.visibility = View.VISIBLE
        binding.customizebottomNavigationView.settings.visibility = View.VISIBLE
        binding.customizebottomNavigationView.task.visibility = View.VISIBLE

        binding.customizebottomNavigationView.settingsSelected.visibility = View.GONE
        binding.customizebottomNavigationView.taskSelected.visibility = View.GONE
        binding.customizebottomNavigationView.homeSelect.visibility = View.GONE
    }

    private fun homeSelection() {

        binding.customizebottomNavigationView.homeSelect.visibility = View.VISIBLE
        binding.customizebottomNavigationView.home.visibility = View.GONE
        binding.menu.visibility = View.VISIBLE

        if (isGlobal){

            binding.customizebottomNavigationView.qrbottom.visibility = View.VISIBLE
            binding.customizebottomNavigationView.map.visibility = View.VISIBLE

            binding.customizebottomNavigationView.slelectedmap.visibility = View.GONE
            binding.customizebottomNavigationView.slelectedqr.visibility = View.GONE

        }else{

            binding.customizebottomNavigationView.task.visibility = View.VISIBLE
            binding.customizebottomNavigationView.client.visibility = View.VISIBLE


            binding.customizebottomNavigationView.taskSelected.visibility = View.GONE
            binding.customizebottomNavigationView.clientSelected.visibility = View.GONE
        }

        binding.customizebottomNavigationView.settings.visibility = View.VISIBLE

        binding.customizebottomNavigationView.settingsSelected.visibility = View.GONE
    }

    private fun mapSelection(){

        binding.customizebottomNavigationView.slelectedmap.visibility = View.VISIBLE
        binding.customizebottomNavigationView.map.visibility = View.GONE
        binding.menu.visibility = View.GONE

        binding.customizebottomNavigationView.qrbottom.visibility = View.VISIBLE
        binding.customizebottomNavigationView.settings.visibility = View.VISIBLE
        binding.customizebottomNavigationView.home.visibility = View.VISIBLE

        binding.customizebottomNavigationView.settingsSelected.visibility = View.GONE
        binding.customizebottomNavigationView.slelectedqr.visibility = View.GONE
        binding.customizebottomNavigationView.homeSelect.visibility = View.GONE

    }

    private fun qrSelection(){

        binding.customizebottomNavigationView.slelectedqr.visibility = View.VISIBLE
        binding.customizebottomNavigationView.qrbottom.visibility = View.GONE
        binding.menu.visibility = View.GONE

        binding.customizebottomNavigationView.map.visibility = View.VISIBLE
        binding.customizebottomNavigationView.settings.visibility = View.VISIBLE
        binding.customizebottomNavigationView.home.visibility = View.VISIBLE

        binding.customizebottomNavigationView.slelectedmap.visibility = View.GONE
        binding.customizebottomNavigationView.settingsSelected.visibility = View.GONE
        binding.customizebottomNavigationView.homeSelect.visibility = View.GONE

    }

    override fun onSpinnerItemSelection(
        position: Int,
        i: Int,
        listener: OnFragmentChangedListener?
    ) {
//        Log.d("spinnerCheking", i.toString())

        val fragment: Fragment = when (i) {
            0 -> AttendanceFragment(this)
            1 -> HolidaysFragment(this)
            2 -> LeavesFragment(this)

            else -> {
//                currentFragment ?: FragmentA()
                HomeFragment(this)

            }

        }
//        if (newFragment::class != currentFragment!!::class) {
//            currentFragment = newFragment
//            switchFragment(currentFragment!!)
//        }
        switchFragment(fragment)
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        if (currentFragment !is HomeFragment) {
            // Navigate to home fragmentc
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment(this)).commit()
            binding.customizebottomNavigationView.homeSelect.visibility = View.VISIBLE
            binding.customizebottomNavigationView.home.visibility = View.GONE

            binding.menu.visibility = View.VISIBLE

            binding.customizebottomNavigationView.client.visibility = View.VISIBLE
            binding.customizebottomNavigationView.settings.visibility = View.VISIBLE
            binding.customizebottomNavigationView.task.visibility = View.VISIBLE

            binding.customizebottomNavigationView.settingsSelected.visibility = View.GONE
            binding.customizebottomNavigationView.taskSelected.visibility = View.GONE
            binding.customizebottomNavigationView.clientSelected.visibility = View.GONE
        } else {
            // If already on the home fragment, follow the default back press behavior

            if (binding.qrshowpopup.qrpopup.visibility == View.VISIBLE)  binding.qrshowpopup.qrpopup.visibility = View.GONE
            else {
                super.onBackPressed()
                finish()
            }

        }
    }

    private fun openMenu() {
        val animation = TranslateAnimation(-sideMenu.width.toFloat(), 0f, 0f, 0f)
        animation.duration = 300
        animation.fillAfter = true
        sideMenu.startAnimation(animation)
        sideMenu.translationX = 0f
        isMenuOpen = true
    }


    private fun closeMenu() {
        ObjectAnimator.ofFloat(sideMenu, "translationX", 0f, -sideMenu.width.toFloat()).apply {
            duration = 300
            start()
        }
        isMenuOpen = false

    }

    @SuppressLint("ResourceAsColor")
    private fun highlightMenuItem(selectedIcon: ImageView, selectedText: TextView) {
        // Reset colors for all menu items
//        resetMenuItemColors()

        // Highlight the selected menu item
        selectedIcon.setColorFilter(R.color.sidear_tint)
        selectedText.setTextColor(R.color.sidear_tint)
    }

    @SuppressLint("ResourceAsColor")
    private fun resetMenuItemColors() {

        val defaultColor = R.color.black

        binding.sidebar.menuhomeicon.setColorFilter(defaultColor)
        binding.sidebar.hometxt.setTextColor(defaultColor)

        binding.sidebar.atticon.setColorFilter(defaultColor)
        binding.sidebar.attendacncetxt.setTextColor(defaultColor)

        binding.sidebar.lvivon.setColorFilter(defaultColor)
        binding.sidebar.leavestxt.setTextColor(defaultColor)

        binding.sidebar.hllicon.setColorFilter(defaultColor)
        binding.sidebar.holidaystxt.setTextColor(defaultColor)

        binding.sidebar.taskic.setColorFilter(defaultColor)
        binding.sidebar.tasktxt.setTextColor(defaultColor)

        binding.sidebar.clicoon.setColorFilter(defaultColor)
        binding.sidebar.clientstxt.setTextColor(defaultColor)

        binding.sidebar.setticon.setColorFilter(defaultColor)
        binding.sidebar.settingstxt.setTextColor(defaultColor)

        binding.sidebar.logoutIcon.setColorFilter(defaultColor)
        binding.sidebar.logoutText.setTextColor(defaultColor)

    }

    fun setMenuButtonVisibility(visible: Boolean) {
        binding.menu.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setQrVisibility(visible: Boolean){

        binding.qrshow.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun setUserPic() {
//        lifecycleScope.launch {
            // Retrieve bitmap in a background thread
//            val pic: Bitmap? = withContext(Dispatchers.Default) {
//                CommonMethods.getBitmapFromSharedPreferences(applicationContext, Constants.BITMAP_RECIEVE)
//            }
//
//            // Update UI on the main thread
//            if (pic != null) {
//                binding.sidebar.userPic.visibility = View.VISIBLE
//                binding.sidebar.userPicdisbale.visibility = View.GONE
//                binding.sidebar.userPic.setImageBitmap(pic)
//            }
            //        val pic = CommonMethods.getBitmapFromSharedPreferences(requireContext(),Constants.BITMAP_RECIEVE)
//        lifecycleScope.launch {

            val pic = CommonMethods.getSharedPrefernce(this@MainActivity,Constants.PROFILE_PIC_URL_USER)
            if(!pic.isNullOrEmpty()){
                binding.sidebar.userPic.visibility = View.VISIBLE
                binding.sidebar.userPicdisbale.visibility = View.GONE
                Picasso.get().load(pic).into(binding.sidebar.userPic)

                binding.picmainshowCard.visibility = View.VISIBLE
                binding.profile.visibility = View.INVISIBLE
                Picasso.get().load(pic).into(binding.profilepicshow)

            }else{
                binding.sidebar.userPic.visibility = View.GONE
                binding.sidebar.userPicdisbale.visibility = View.VISIBLE

                binding.picmainshowCard.visibility = View.GONE
                binding.profile.visibility = View.VISIBLE


            }
//        }

//        }
    }


    fun setUserData(){

//        lifecycleScope.launch{

            val sharedPred = applicationContext.getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
            val name = sharedPred.getString(Constants.NAME_FULL,"")
            val role = sharedPred.getString(Constants.ROLE,"")
            if(!name.isNullOrEmpty()){
                binding.sidebar.name.text = name
                binding.sidebar.role.text = CommonMethods.getSharedPrefernce(this@MainActivity,Constants.LOGIN_ROLE_PERSON)
//            binding.role.text = role
            }else{

                binding.sidebar.name.text = CommonMethods.getSharedPrefernce(this@MainActivity,Constants.LOGIN_NAME_PERSON)
                binding.sidebar.role.text = CommonMethods.getSharedPrefernce(this@MainActivity,Constants.LOGIN_ROLE_PERSON)

            }
//        }

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onResume() {
        super.onResume()

        if (!isLocationEnabled()){

            binding.locationalert.locationalertpop.visibility = View.VISIBLE
        }


        setUserData()
        setUserPic()
    }

    private fun locationChecking(){

        locationCheckingRunnable = object :Runnable{
            override fun run() {

                if (!isLocationEnabled()) binding.locationalert.locationalertpop.visibility = View.VISIBLE
                else binding.locationalert.locationalertpop.visibility = View.GONE
                handler.postDelayed(locationCheckingRunnable,1000)
            }

        }

        handler.post(locationCheckingRunnable)
    }

    private fun batteryChecking(){

        batteryCheckingRunnable = object :Runnable{
            override fun run() {

                batteryCheckingStatus()
                handler.postDelayed(batteryCheckingRunnable,60000)
            }


        }
        handler.post(batteryCheckingRunnable)
    }

    private fun batteryCheckingStatus(){

        val batteryStatus = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL,-1)
        val scale = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_SCALE,-1)
        val batteryPct = level / scale?.toFloat()!! * 100

        if (batteryPct <= 15) binding.batteryalert.batterypopup.visibility = View.VISIBLE
        else binding.batteryalert.batterypopup.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()

        handler.removeCallbacks(batteryCheckingRunnable)
        handler.removeCallbacks(locationCheckingRunnable)
    }

//    fun setUserDataQr(){
//
//        val sharedPred = getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
//        val name = sharedPred.getString(Constants.NAME_FULL,"")
//        val role = sharedPred.getString(Constants.ROLE,"")
//
//        binding.qrshowpopup.roletxt.text = CommonMethods.getSharedPrefernce(this,Constants.LOGIN_ROLE_PERSON)
//        if(!name.isNullOrEmpty()){
//
//            val namesplit = name.split(" ")
//            binding.qrshowpopup.name.text = namesplit[0]
//            binding.qrshowpopup.namelast.text = namesplit[1]
//
//        }else{
//            val namesplit = CommonMethods.getSharedPrefernce(this,Constants.LOGIN_NAME_PERSON)
//            val namesection = namesplit.split(" ")
//            binding.qrshowpopup.name.text = namesection[0]
//            binding.qrshowpopup.namelast.text = namesection[1]
//        }
//    }

    fun setUserDataQr() {
        val sharedPred = getSharedPreferences(Constants.USER_FULL_NAME, AppCompatActivity.MODE_PRIVATE)
        val name = sharedPred.getString(Constants.NAME_FULL, "")
        val role = sharedPred.getString(Constants.ROLE, "")

        binding.qrshowpopup.roletxt.text = CommonMethods.getSharedPrefernce(this, Constants.LOGIN_ROLE_PERSON)

        val fullName = if (!name.isNullOrEmpty()) name
        else CommonMethods.getSharedPrefernce(this, Constants.LOGIN_NAME_PERSON)

        val nameParts = fullName.trim().split(" ")

        binding.qrshowpopup.name.text = nameParts.getOrNull(0) ?: ""
        binding.qrshowpopup.namelast.text = nameParts.getOrNull(1) ?: ""
    }

    private fun getQr(empid:String){

        val link = NativeLib().getQRLinkLive() + empid
        Picasso.get().load(link).into(binding.qrshowpopup.qrpreview)
    }

    private fun downloadImageAndSaveAsPdf(imageUrl: String) {
        thread {
            try {
                Log.d("DownloadPDF", "Starting image download from URL: $imageUrl")

                // Download the image
                val url = URL(imageUrl)
                val bitmap: Bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())

                Log.d("DownloadPDF", "Image downloaded successfully")

                // Create a PDF document
                val pdfDocument = PdfDocument()
                val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
                val page = pdfDocument.startPage(pageInfo)

                Log.d("DownloadPDF", "PDF page created with dimensions: ${bitmap.width}x${bitmap.height}")

                // Draw the image on the PDF page
                val canvas: Canvas = page.canvas
                canvas.drawBitmap(bitmap, 0f, 0f, null)
                pdfDocument.finishPage(page)

                Log.d("DownloadPDF", "Image drawn on PDF page")

                // Save the PDF to a file
                val sharedPred = getSharedPreferences(Constants.USER_FULL_NAME,AppCompatActivity.MODE_PRIVATE)
                val name = sharedPred.getString(Constants.NAME_FULL,"")
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "$name.pdf")
//                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "image.pdf")
                val outputStream = FileOutputStream(file)
                pdfDocument.writeTo(outputStream)

                Log.d("DownloadPDF", "PDF saved to ${file.absolutePath}")

                // Close the document and streams
                pdfDocument.close()
                outputStream.close()

                runOnUiThread {
                    Toast.makeText(this, "PDF saved to Downloads/image.pdf", Toast.LENGTH_LONG).show()
//                    showDownloadNotification(file.absolutePath)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("DownloadPDF", "Error: ${e.message}", e)
                runOnUiThread {
                    Toast.makeText(this, "Failed to download image or save PDF: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Download Notifications"
            val descriptionText = "Notifications for download status"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

//    private fun showDownloadNotification(filePath: String) {
//        val notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java)
//        val notification = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(android.R.drawable.stat_sys_download_done)
//            .setContentTitle("Download Complete")
//            .setContentText("PDF saved to: $filePath")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setAutoCancel(true)
//            .build()
//
//        notificationManager?.notify(1, notification)
//    }



    private fun showDownloadNotification(fileUri: Uri) {
        // Create an Intent to open the PDF file using the content URI from MediaStore
        val openPdfIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(fileUri, "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        // Create a PendingIntent that will open the PDF when the notification is clicked
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            openPdfIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle("Download Complete")
            .setContentText("PDF saved successfully")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)  // Attach the PendingIntent to the notification
            .setAutoCancel(true)
            .build()

        // Show the notification
        notificationManager?.notify(1, notification)
    }
    private fun getBitMapFromView(view: View):Bitmap{

        view.measure(
            View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY)
        )
        view.layout(0,0,view.measuredWidth,view.measuredHeight)

        val bitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createPdf(view: View, fileName: String) {
        val bitmap = getBitMapFromView(view)

        // Create a PdfDocument with a page size matching the bitmap dimensions
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = document.startPage(pageInfo)

        // Draw the bitmap onto the PDF page
        val canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)

        // Use MediaStore for saving the PDF to the Downloads folder (Android 10+)
        val resolver = applicationContext?.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, "$fileName.pdf")  // File name with extension
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")  // MIME type for PDF
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)  // Target directory
        }

        try {
            val uri = resolver?.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            if (uri != null) {
                resolver.openOutputStream(uri)?.use { outputStream ->
                    document.writeTo(outputStream)
                    Toast.makeText(applicationContext, "PDF saved successfully!", Toast.LENGTH_LONG).show()
                    showDownloadNotification(uri)  // Show download notification with the URI
                }
            } else {
                Toast.makeText(applicationContext, "Failed to create PDF file.", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            document.close()
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        } else {
            // Permission is already granted

        }
    }

    private fun chagneApiKey(){

        val manifestApiKey = getMetaDataValue("com.example.API_KEY")
        if (manifestApiKey == "API_KEY_FROM_NATIVE") {
            val actualApiKey = NativeLib().getGoogleMapApiKey()
            Log.d("API_KEY_NATIVE", "API Key from Native: $actualApiKey")

            // Use the actual API key (from native code) for your API calls
        }
    }

    private fun getMetaDataValue(name: String): String? {
        val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val bundle = appInfo.metaData
        return bundle?.getString(name)
    }

}