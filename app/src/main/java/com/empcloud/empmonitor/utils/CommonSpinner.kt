package com.empcloud.empmonitor.utils

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.FragmentActivity
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.ui.fragment.attendance.AttendanceFragment
import com.empcloud.empmonitor.ui.fragment.holidays.HolidaysFragment
import com.empcloud.empmonitor.ui.fragment.leaves.LeavesFragment

object CommonSpinner {
    fun spinnerShow(spinner: Spinner, context: Context, activity: FragmentActivity){

        // Define the items to show in the spinner
        val items = listOf("Attendance", "Holidays", "Leaves")

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter(context, R.layout.attendance_spinner, items)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.attendance_spinner)

        // Apply the adapter to the spinner
        spinner.adapter = adapter


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Get the selected item
                val selectedItem = parent.getItemAtPosition(position) as String

                // Perform actions based on the selected item
                when (selectedItem) {
                    "Attendance" -> {
                        activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,AttendanceFragment()).commit()

                    }
                    "Holidays" -> {
                        activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,HolidaysFragment()).commit()

                    }
                    "Leaves" -> {
                        activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,LeavesFragment()).commit()

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing if nothing is selected
            }
        }
    }
}