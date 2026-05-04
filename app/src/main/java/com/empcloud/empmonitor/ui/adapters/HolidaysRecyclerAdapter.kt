package com.empcloud.empmonitor.ui.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.holidays.HolidayList
import com.empcloud.empmonitor.utils.CommonMethods
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HolidaysRecyclerAdapter(private val context: Context,
                              private var holidayList: List<HolidayList>):
    RecyclerView.Adapter<HolidaysRecyclerAdapter.HolidayHolder>() {

    class HolidayHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val holidayTitle: TextView = itemView.findViewById(R.id.holidayTitle)
        val holidayDate: TextView = itemView.findViewById(R.id.holidayDate)
        val dividerView:View = itemView.findViewById(R.id.viewdivider)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayHolder {

        val itemView = LayoutInflater.from(context).inflate(R.layout.holidays_listitems,parent,false)
        return HolidayHolder(itemView)
    }

    override fun getItemCount(): Int {

        return  holidayList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HolidayHolder, position: Int) {

        val holidaysModel = holidayList[position]

        val date = CommonMethods.parseDate(holidaysModel.holiday_date)
        if (CommonMethods.isDateBeforeCurrent(date)){

            holder.holidayTitle.text = holidaysModel.holiday_name
            holder.holidayDate.text = convertTimezonDateToString(holidaysModel.holiday_date)

        }
        else{

            holder.holidayDate.setTextColor(ContextCompat.getColor(context, R.color.holidaysitemscolor))

            holder.holidayTitle.text = holidaysModel.holiday_name
            holder.holidayDate.text = convertTimezonDateToString(holidaysModel.holiday_date)
        }

        if (position == itemCount - 1) {
            holder.dividerView.visibility = View.GONE
        } else {
            holder.dividerView.visibility = View.VISIBLE
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTimezonDateToString(inputDate: String): String {
        // Parse the input string to Instant
        val instant = Instant.parse(inputDate)

        // Convert Instant to LocalDateTime
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

        // Define the desired date format
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        // Format LocalDateTime to the desired string
        return localDateTime.format(formatter)
    }
}