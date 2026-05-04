package com.empcloud.empmonitor.ui.activity.tutorial

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.databinding.ActivityTutorialBinding
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity
import com.empcloud.empmonitor.ui.listeners.SlideViewClickListener
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import kotlinx.coroutines.launch
import android.provider.Settings

class TutorialActivity : AppCompatActivity(),SlideViewClickListener {

    private lateinit var binding:ActivityTutorialBinding
    private lateinit var tutorialPager: ViewPager
    private lateinit var viewPagerAdapter:ViewPagerAdapter
    private lateinit var layoutDots: LinearLayout
    private var dots: Array<TextView?> = arrayOfNulls(7)
    private val layouts: Array<Int?> = arrayOfNulls(7)
    private lateinit var youtubeView: WebView

    private val FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE = 101
    private val LOCATIONPERMISSION_REQUEST_CODE = 102
    private val POST_NOTITIFICATION_REQUEST_CODE = 103
    private val BODYSENSOR_REQUEST_CODE = 105
    private val CAMERA_GALLERY_REQUEST_CODE = 104


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        CommonMethods.setStatusBarColor(window, applicationContext, binding.root, this)
        tutorialPager = binding.slidesPager
        layoutDots =binding.layoutDots

        layouts[0] = R.layout.productivity_insights
        layouts[1] = R.layout.location_permission
        layouts[2] = R.layout.background_permission
        layouts[3] = R.layout.activity_permission
        layouts[4] = R.layout.run_in_background
        layouts[5] = R.layout.video_turtorial
        layouts[6] = R.layout.finsh_screen

        addBottomDots(0)


        viewPagerAdapter = ViewPagerAdapter(layouts,applicationContext,this)
        viewPagerAdapter.setOnViewClickListener(this)

        tutorialPager.adapter = viewPagerAdapter
        tutorialPager.addOnPageChangeListener(viewListener)

        tutorialPager.setOnTouchListener(View.OnTouchListener { view, motionEvent -> true })
        tutorialPager.setClickable(false)


    }

    var viewListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            Log.i("POS : ", position.toString() + "")
            addBottomDots(position)


        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
    private fun addBottomDots(position: Int) {
        dots = arrayOfNulls(layouts.size)
        val colorActive = resources.getColor(R.color.colorPrimaryDark)
        val colorInactive = resources.getColor(R.color.colorPrimaryLight)


        layoutDots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP,40f)
            dots[i]!!.setTextColor(colorInactive)
            layoutDots.addView(dots[i])
        }
        if (dots.size > 0) {
            dots[position]!!.setTextColor(colorActive)

        }
    }

    class ViewPagerAdapter constructor(layouts: Array<Int?> ,context: Context,activity: Activity) : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        private var listener: SlideViewClickListener? = null
        private val context = context
        private val layouts:Array<Int?> = layouts
        private val activity = activity
        fun setOnViewClickListener(listener: SlideViewClickListener?) {
            this.listener = listener
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v: View = layoutInflater.inflate(layouts[position]!!, container, false)


            if (layouts.get(position) == R.layout.productivity_insights) {
                val nextBtn = v.findViewById<View>(R.id.nextBtn1)
                nextBtn.setOnClickListener {
                    listener!!.onSlideViewClicked(
                        position,
                        v,
                        R.id.nextBtn1,
                        activity
                    )
                }
            }
            if (layouts.get(position) == R.layout.location_permission) {
                val nextbtn = v.findViewById<View>(R.id.nextBtn2)
                nextbtn?.setOnClickListener {
                    listener!!.onSlideViewClicked(
                        position,
                        v,
                        R.id.nextBtn2,
                        activity
                    )
                }
            }
            if (layouts.get(position) == R.layout.background_permission) {
                val nextbtn = v.findViewById<View>(R.id.nextBtn3)
                nextbtn?.setOnClickListener {
                    listener!!.onSlideViewClicked(
                        position,
                        v,
                        R.id.nextBtn3,
                        activity
                    )
                }
            }
            if (layouts.get(position) == R.layout.activity_permission) {
                val nextbtn = v.findViewById<View>(R.id.nextBtn4)
                nextbtn?.setOnClickListener {
                    listener!!.onSlideViewClicked(
                        position,
                        v,
                        R.id.nextBtn4,
                        activity
                    )
                }
            }
            if (layouts.get(position) == R.layout.run_in_background) {
//                listener!!.onLayoutInit(v,  layouts)
                val nextbtn = v.findViewById<View>(R.id.nextBtn5)
                nextbtn?.setOnClickListener {
                    listener!!.onSlideViewClicked(
                        position,
                        v,
                        R.id.nextBtn5,
                        activity
                    )
                }

            }
            if (layouts.get(position) == R.layout.video_turtorial) {
                listener!!.onPlay(v)
                val nextbtn = v.findViewById<View>(R.id.nextBtn6)
                nextbtn?.setOnClickListener {

                    listener!!.onSlideViewClicked(
                        position,
                        v,
                        R.id.nextBtn6,
                        activity
                    )
                }

            }
            if (layouts.get(position) == R.layout.finsh_screen) {
                listener!!.onLayoutInit(v,  layouts)
                val finishbtn = v.findViewById<View>(R.id.nextBtn7)
                finishbtn?.setOnClickListener {
                    listener!!.onSlideViewClicked(
                        position,
                        v,
                        R.id.nextBtn7,
                        activity
                    )
                }
            }
            Log.i("Position : ", position.toString())
            container.addView(v)
            return v
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val v = `object` as View
            container.removeView(v)
        }
    }
    fun areLocPermissionsGranted(context: Context, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // If any permission is not granted, return false
                return false
            }
        }
        // All permissions are granted
        return true
    }
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onSlideViewClicked(position: Int, view: View?, viewId: Int, activity: Activity) {
        if (viewId == R.id.nextBtn1) {
            tutorialPager.setCurrentItem(position + 1)
        }
        if (viewId == R.id.nextBtn2) {
            val permissionsToCheck = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            if (areLocPermissionsGranted(activity, permissionsToCheck)) {
                // Permissions are granted, proceed with your logic

                tutorialPager.setCurrentItem(position + 1)
            } else {
                // Permissions are not granted, request them
                requestMultiplePermissions(activity,permissionsToCheck, LOCATIONPERMISSION_REQUEST_CODE)
            }

        }
            if (viewId == R.id.nextBtn3) {

                if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), POST_NOTITIFICATION_REQUEST_CODE)
                }else{

                    tutorialPager.setCurrentItem(position + 1)
                }

            }
        if (viewId == R.id.nextBtn4) {

//            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BODY_SENSORS)
//                    != PackageManager.PERMISSION_GRANTED
//                ) {
//                    // If any permission is not granted, return false
//                ActivityCompat.requestPermissions(
//                    activity,
//                    arrayOf(Manifest.permission.BODY_SENSORS),
//                    BODYSENSOR_REQUEST_CODE
//                )
//            } else {
//
//                tutorialPager.setCurrentItem(position + 1)
//
//            }
            if (Build.VERSION.SDK_INT >= 36) {

                tutorialPager.setCurrentItem(position + 1)

            } else {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BODY_SENSORS)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    // Request the old BODY_SENSORS permission
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.BODY_SENSORS),
                        BODYSENSOR_REQUEST_CODE
                    )
                } else {
                    // Permission is already granted, proceed
                    tutorialPager.setCurrentItem(position + 1)
                }
            }

        }
            if (viewId == R.id.nextBtn5) {

                val permissionsToCheck = arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )

                if (areLocPermissionsGranted(activity, permissionsToCheck)) {
                    // Permissions are granted, proceed with your logic

                    tutorialPager.setCurrentItem(position + 1)
                } else {
                    // Permissions are not granted, request them
                    requestMultiplePermissions(activity,permissionsToCheck, CAMERA_GALLERY_REQUEST_CODE)
                }
            }
            if(viewId == R.id.nextBtn6){
                tutorialPager.setCurrentItem(position+1)
                cleanupWebView()
            }
            if (viewId == R.id.nextBtn7) {

                firstRun()
                val intent = Intent(applicationContext, LoginOptionsActivity::class.java)
                startActivity(intent)
                finish()
            }

    }

    fun requestMultiplePermissions(context: Activity , permissions: Array<String>, requestCode: Int) {

            ActivityCompat.requestPermissions(
                context,
                permissions,
                requestCode
            )

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

            // Check if the requestCode matches the one you used when requesting permissions
            LOCATIONPERMISSION_REQUEST_CODE -> {
                // Check if the permissions were granted
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tutorialPager.setCurrentItem(2)
                }
                else {

                    Toast.makeText(applicationContext,"Please Provide permission",Toast.LENGTH_SHORT).show()
//                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//                        data = Uri.fromParts("package", packageName, null)
//                    }
//                    startActivity(intent)
//                    finish()
                }
            }
            BODYSENSOR_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tutorialPager.setCurrentItem(4)
                } else {

                    Toast.makeText(applicationContext,"Please Provide permission",Toast.LENGTH_SHORT).show()

//                    finish()
                }
            }
            CAMERA_GALLERY_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tutorialPager.setCurrentItem(5)
                } else {

                    Toast.makeText(applicationContext,"Please Provide permission",Toast.LENGTH_SHORT).show()

//                    finish()
                }
            }

            POST_NOTITIFICATION_REQUEST_CODE -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tutorialPager.setCurrentItem(3)
                } else {

                    Toast.makeText(applicationContext,"Please Provide permission",Toast.LENGTH_SHORT).show()

//                    finish()
                }
            }

        }
    }
    override fun onLayoutInit(view: View, layout: Array<Int?>) {
        splashAnimation(view)

    }

    override fun onPlay(view: View) {
        playYoutube(view)


    }


    fun playYoutube(view: View) {
        youtubeView = view.findViewById(R.id.youtube_view)
//        val url = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/xs49wIGeqTc?si=P4vHm9LKVGnDA3OD\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val url = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/eV8fejyQx9g?si=7nrWizH-blXFXdi3\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
//       val url = "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/xs49wIGeqTc?si=P4vHm9LKVGnDA3OD\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
        youtubeView.loadData(url,"text/html","utf-8")
        youtubeView.settings.javaScriptEnabled = true
        youtubeView.webChromeClient = WebChromeClient()

    }

     fun splashAnimation(view: View) {
        lifecycleScope.launch {

            val animationView = view.findViewById<LottieAnimationView>(R.id.splash_done_animation)
            animationView!!.visibility = View.VISIBLE
            animationView.playAnimation()
            animationView.repeatCount = 1


        }
    }

    private fun firstRun() {
        val sharepref = getSharedPreferences(Constants.FIRST_RUN, MODE_PRIVATE)
        val edit = sharepref.edit()
        edit.putBoolean(Constants.FIRST_RUN,true)
        edit.apply()
    }

    private fun cleanupWebView() {
        if (::youtubeView.isInitialized) {
            youtubeView.stopLoading()
            youtubeView.webChromeClient = null
            youtubeView.clearHistory()
            youtubeView.clearCache(true)
            youtubeView.loadUrl("about:blank")
            youtubeView.removeAllViews()
            youtubeView.destroy()
        }
    }

}