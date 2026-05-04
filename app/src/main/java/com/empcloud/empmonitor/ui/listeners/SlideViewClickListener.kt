package com.empcloud.empmonitor.ui.listeners

import android.app.Activity
import android.view.View

interface SlideViewClickListener {

    fun onSlideViewClicked(position: Int, view: View?, viewId: Int,activity: Activity)

    fun onLayoutInit(view: View, layout:Array<Int?>)

    fun onPlay(view: View)


}