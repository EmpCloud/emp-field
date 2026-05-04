package com.empcloud.empmonitor.ui.adapters

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.attendance.AttendanceFullData
import com.empcloud.empmonitor.ui.listeners.AttendacneRecyclerItemClickListener
import com.empcloud.empmonitor.utils.CommonMethods
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AttendanceAdapter(
    private val context: Context,
    private val listener:AttendacneRecyclerItemClickListener?,
    private var attList: List<AttendanceFullData>
):
    RecyclerView.Adapter<AttendanceAdapter.AttendanceAdapter>() {

    class AttendanceAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateR: TextView = itemView.findViewById(R.id.dateATT)
        val statusR: TextView = itemView.findViewById(R.id.statusaTT)
        val checkIn: TextView = itemView.findViewById(R.id.checkin)
        val checkout: TextView = itemView.findViewById(R.id.checkout)
        val edit:ImageView = itemView.findViewById(R.id.edit)
        val dividerview:View = itemView.findViewById(R.id.dividerview)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceAdapter {

        val itemView = LayoutInflater.from(context).inflate(R.layout.attendance_list_items,parent,false)
        return AttendanceAdapter(itemView)
    }

    override fun getItemCount(): Int {

        return  attList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AttendanceAdapter, position: Int) {

        val leavesModel = attList[position]
        val dateTimeString = leavesModel.date
        val formatedDate = convertDateToDDMMYY(dateTimeString)
        holder.dateR.text = formatedDate


        holder.edit.setOnClickListener {
//            Log.d("checkingListener",position.toString())
//            Log.d("checkingListener",leavesModel.toString())

            if(listener != null){

                if (leavesModel.date != null){

                    val date = CommonMethods.parseDate(leavesModel.date)
                    if (CommonMethods.isDateBeforeCurrent(date))

                        listener.onItemClicked(position,leavesModel)

                    else

                        Toast.makeText(context,"Not allowed to request for  current/upcoming dates",Toast.LENGTH_SHORT).show()
                }

            }
//            else{
//                Toast.makeText(context,"Null Listenre",Toast.LENGTH_SHORT).show()
//            }

        }

//        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (position == itemCount -1 ) {

//            layoutParams.bottomMargin = context.resources.getDimensionPixelSize(R.dimen.margin_fifty)
            holder.dividerview.visibility = View.GONE

        }else{
            holder.dividerview.visibility = View.VISIBLE
        }

//        holder.itemView.layoutParams = layoutParams

        setData(leavesModel,holder)


    }


    fun convertDateToDDMMYY(dateString: String, inputFormat: String = "yyyy-MM-dd"): String {
        // Define the input and output date formats
        val inputDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())

        // Parse the input date string to a Date object
        val date: Date = inputDateFormat.parse(dateString)

        // Format the Date object to the desired output format
        return outputDateFormat.format(date)
    }

    fun parseTime(timeDate: String): String? {
        val parts = timeDate.split("T") // Split based on 'T'

        if (parts.size == 2) {
            val timePart = parts[1].split(".") // Split time component based on '.'
            return timePart[0] // Return time part without milliseconds
        }

        return null // Return null if the format is invalid
    }

    fun parseTimeData(time:String):String{

        val timeExtract = time.split(Regex("[T ]"))
        return  timeExtract[1]
    }


    fun againparseTimeData(time:String):String{

        val timeExtract = time.split(".")
        return  timeExtract[0]
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData(leavesModel: AttendanceFullData, holder: AttendanceAdapter) {

            if (leavesModel.day_off) {

                if (leavesModel.holiday_name!!.isEmpty()) {

                    if (leavesModel.open_request == null) {

//                    if (CommonMethods.isDateBeforeCurrent(CommonMethods.parseDate(leavesModel.date))){
//
//                        holder.statusR.text = "Absent"
//                        if (!leavesModel.start_time.isNullOrEmpty()) holder.checkIn.text = againparseTimeData(parseTimeData(leavesModel.start_time!!))
//                        if (!leavesModel.end_time.isNullOrEmpty()) holder.checkout.text = againparseTimeData(parseTimeData(leavesModel.end_time!!))
//
//                    }

                        if (!leavesModel.start_time.isNullOrEmpty() && !leavesModel.end_time.isNullOrEmpty()) {

                            holder.checkIn.text =
                                againparseTimeData(parseTimeData(leavesModel.start_time))
                            holder.checkout.text =
                                againparseTimeData(parseTimeData(leavesModel.end_time))
                            holder.statusR.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.green_listitem_color
                                )
                            )
                            if (leavesModel.open_attendance_request == null) {

                                if (leavesModel.leave_type == 0 && leavesModel.half_day_status == null){

//                                    if (leavesModel.logged_duration > (leavesModel.min_hours[0].manual_hours/2))   holder.statusR.text = "Present"
//                                    else if(leavesModel.logged_duration < (leavesModel.min_hours[0].manual_hours/2)) holder.statusR.text = "Absent"
//                                    else holder.statusR.text = "Hafl Day Present"

                                    if (leavesModel.logged_duration < (leavesModel.min_hours[0].manual_hours/2)) holder.statusR.text = "Absent"
                                    else if(leavesModel.logged_duration >= (leavesModel.min_hours[0].manual_hours/2) && leavesModel.logged_duration < leavesModel.min_hours[0].manual_hours) holder.statusR.text = "Half Day Present"
                                    else    holder.statusR.text = "Present"

                                }else{

                                    if (leavesModel.half_day_leave != null){
                                        val  halfDay = leavesModel.half_day_leave
                                        holder.statusR.text = "Half Day Present" + "\n" + "/$halfDay"
                                    }
                                }

                            } else {
                                if (leavesModel.open_attendance_request.request_status.equals("3")) {

                                    holder.statusR.text = "Pending"
                                    holder.statusR.setTextColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.yellow_text_color
                                        )
                                    )
                                } else if (leavesModel.open_attendance_request.request_status.equals(
                                        "1"
                                    )
                                ) {

                                    if (leavesModel.half_day_leave != null){
                                        val  halfDay = leavesModel.half_day_leave
                                        holder.statusR.text = "Half Day Present" + "\n" + "/$halfDay"
                                    }
                                    else {
                                        if (leavesModel.logged_duration < (leavesModel.min_hours[0].manual_hours/2)) holder.statusR.text = "Absent"
                                        else if(leavesModel.logged_duration >= (leavesModel.min_hours[0].manual_hours/2) && leavesModel.logged_duration < leavesModel.min_hours[0].manual_hours) holder.statusR.text = "Half Day Present"
                                        else    holder.statusR.text = "Present"
                                    }
                                    holder.statusR.setTextColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.green_listitem_color
                                        )
                                    )

                                }

                            }
                            holder.statusR.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.green_listitem_color
                                )
                            )
                            holder.checkIn.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.green_listitem_color
                                )
                            )
                            holder.checkout.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.red_listitems_color
                                )
                            )


                        } else if (!leavesModel.start_time.isNullOrEmpty() && leavesModel.end_time.isNullOrEmpty()) {


                            if (leavesModel.open_request == null && leavesModel.leave_type == 0) {

                                holder.statusR.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.weekoff
                                    )
                                )
                                if (leavesModel.start_time.isNullOrEmpty() && leavesModel.end_time.isNullOrEmpty()) {

                                    holder.statusR.text = leavesModel.leave_name
                                    holder.checkIn.text = "-- --"
                                    holder.checkout.text = "-- --"
                                } else {

                                    if (!leavesModel.start_time.isNullOrEmpty()) holder.checkIn.text =
                                        againparseTimeData(parseTimeData(leavesModel.start_time))
                                    if (!leavesModel.end_time.isNullOrEmpty()) holder.checkout.text =
                                        againparseTimeData(parseTimeData(leavesModel.end_time!!))

                                    val halfDay = leavesModel.half_day_leave
                                    if (leavesModel.half_day_leave != null) holder.statusR.text =  "Half Day Present" + "\n" + "/$halfDay"
                                    else holder.statusR.text = "Absent"
                                }
//                            else holder.statusR.text = leavesModel.leave_name
//                            holder.checkIn.text ="-- --"
//                            holder.checkout.text = "-- --"
                            } else if (CommonMethods.isDateBeforeCurrent(
                                    CommonMethods.parseDate(
                                        leavesModel.date
                                    )
                                )
                            ) {

                                val halfDay = leavesModel.half_day_leave
                                if (leavesModel.half_day_leave != null) holder.statusR.text =  "Half Day Present" + "\n" + "/$halfDay"
                                else holder.statusR.text = "Half Day Present"

                                holder.checkIn.text =  againparseTimeData(parseTimeData(leavesModel.start_time))
                                holder.checkout.text = "-- --"

                            } else holder.statusR.text = "Absent"
                            holder.statusR.setTextColor(ContextCompat.getColor(context, R.color.green_listitem_color))
                            holder.checkIn.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.green_listitem_color
                                )
                            )
                            holder.checkout.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.red_listitems_color
                                )
                            )
                        } else if (leavesModel.start_time.isNullOrEmpty() && !leavesModel.end_time.isNullOrEmpty()) {

                            holder.checkIn.text = "-- --"
                            holder.checkout.text =
                                againparseTimeData(parseTimeData(leavesModel.end_time))
                            val halfDay = leavesModel.half_day_leave

                            if (leavesModel.half_day_leave != null) holder.statusR.text = "Half Day Present" + "\n" + "/$halfDay"
                            else holder.statusR.text = "Half Day Present"

                            holder.statusR.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.green_listitem_color
                                )
                            )
                            holder.checkIn.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.green_listitem_color
                                )
                            )
                            holder.checkout.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.red_listitems_color
                                )
                            )
                        }
                        else if (leavesModel.leave_type != 0 && leavesModel.start_time.isNullOrEmpty() && leavesModel.end_time.isNullOrEmpty() ) {

                            holder.statusR.setTextColor(ContextCompat.getColor(context,R.color.weekoff))
                            holder.checkout.setTextColor(ContextCompat.getColor(context,R.color.weekoff))
                            holder.checkIn.setTextColor(ContextCompat.getColor(context,R.color.weekoff))

                            holder.statusR.text = leavesModel.leave_name
                            holder.checkIn.text = "-- --"
                            holder.checkout.text = "-- --"
                        }
                        else {

                            if (CommonMethods.isDateBeforeOrOnCurrent(
                                    CommonMethods.parseDate(
                                        leavesModel.date
                                    )
                                )
                            ) {

                                holder.statusR.text = "Absent"
                                holder.statusR.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.red_listitems_color
                                    )
                                )
                                holder.checkIn.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.weekoff
                                    )
                                )
                                holder.checkout.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.weekoff
                                    )
                                )
                                val time = "-- --"
                                holder.checkIn.text = time
                                holder.checkout.text = time
                            } else {
                                holder.statusR.text = "-- --"
                                holder.statusR.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.red_listitems_color
                                    )
                                )
                                holder.checkIn.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.weekoff
                                    )
                                )
                                holder.checkout.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.weekoff
                                    )
                                )
                                val time = "-- --"
                                holder.checkIn.text = time
                                holder.checkout.text = time
                            }

                        }
                    } else {

                        if (leavesModel.open_request == null && leavesModel.leave_name != null) {

                            holder.statusR.text = leavesModel.open_request.leave_name

                        } else {
                            holder.statusR.text = "Pending"

                        }
                        val time = "-- --"
                        holder.checkIn.text = time
                        holder.checkIn.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.weekoff
                            )
                        )
                        holder.checkout.text = time
                        holder.checkout.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.weekoff
                            )
                        )
                    }


                } else {

                    if (leavesModel.start_time.isNullOrEmpty() && leavesModel.end_time.isNullOrEmpty()) {

                        holder.statusR.setTextColor(ContextCompat.getColor(context,R.color.weekoff))
                        holder.statusR.text = leavesModel.holiday_name
                        val time = "-- --"
                        holder.checkIn.text = time
                        holder.checkIn.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.weekoff
                            )
                        )
                        holder.checkout.text = time
                        holder.checkout.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.weekoff
                            )
                        )
                    } else {

                        if (leavesModel.logged_duration < (leavesModel.min_hours[0].manual_hours/2)) holder.statusR.text = "Absent"
                        else if(leavesModel.logged_duration >= (leavesModel.min_hours[0].manual_hours/2) && leavesModel.logged_duration < leavesModel.min_hours[0].manual_hours) holder.statusR.text = "Half Day Present"
                        else    holder.statusR.text = "Present"
                        holder.statusR.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.green_listitem_color
                            )
                        )


                        if (leavesModel.start_time != null) holder.checkIn.text =
                            againparseTimeData(parseTimeData(leavesModel.start_time!!))
                        if (leavesModel.end_time != null) holder.checkout.text =
                            againparseTimeData(parseTimeData(leavesModel.end_time!!))

                        holder.checkIn.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.green_listitem_color
                            )
                        )

                        holder.checkout.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.red_listitems_color
                            )
                        )
                    }

                }
            } else {

                if (leavesModel.start_time.isNullOrEmpty() && leavesModel.end_time.isNullOrEmpty()) {
                    holder.statusR.text = "Week off"
                    holder.statusR.setTextColor(ContextCompat.getColor(context, R.color.weekoff))
                    val time = "-- --"
                    holder.checkIn.text = time
                    holder.checkout.text = time
                    holder.checkIn.setTextColor(ContextCompat.getColor(context, R.color.weekoff))

                    holder.checkout.setTextColor(ContextCompat.getColor(context, R.color.weekoff))
                } else {

                    if (leavesModel.open_attendance_request != null) {

                        if (leavesModel.open_attendance_request.request_status.equals("3")) {
                            holder.statusR.text = "Pending"
                            holder.statusR.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.yellow_text_color
                                )
                            )
                        } else if (leavesModel.open_attendance_request.request_status.equals("1")) {
                            if (leavesModel.logged_duration < (leavesModel.min_hours[0].manual_hours/2)) holder.statusR.text = "Absent"
                            else if(leavesModel.logged_duration >= (leavesModel.min_hours[0].manual_hours/2) && leavesModel.logged_duration < leavesModel.min_hours[0].manual_hours) holder.statusR.text = "Half Day Present"
                            else    holder.statusR.text = "Present"
                            holder.statusR.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.green_listitem_color
                                )
                            )
                        }
                    }


                    if (leavesModel.start_time != null) holder.checkIn.text =
                        againparseTimeData(parseTimeData(leavesModel.start_time!!))
                    if (leavesModel.end_time != null) holder.checkout.text =
                        againparseTimeData(parseTimeData(leavesModel.end_time!!))

                    holder.checkIn.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.green_listitem_color
                        )
                    )

                    holder.checkout.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.red_listitems_color
                        )
                    )
                }


            }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}