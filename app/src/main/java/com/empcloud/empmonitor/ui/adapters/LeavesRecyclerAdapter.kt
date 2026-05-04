package com.empcloud.empmonitor.ui.adapters
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.leaves.LeavesList
import com.empcloud.empmonitor.ui.listeners.LeaveRecyclerItemClickListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LeavesRecyclerAdapter(
    private val context: Context,
    private val listener:LeaveRecyclerItemClickListener,
    private var holidayList: List<LeavesList>
):
    RecyclerView.Adapter<LeavesRecyclerAdapter.LeavesAdapter>() {

    class LeavesAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateR: TextView = itemView.findViewById(R.id.date)
        val leave: TextView = itemView.findViewById(R.id.leave)
        val dayType: TextView = itemView.findViewById(R.id.dataType)
        val day: TextView = itemView.findViewById(R.id.day)
        val statusR: TextView = itemView.findViewById(R.id.status)
        val dividerView:View = itemView.findViewById(R.id.viewdivider)
        val edit:ImageView = itemView.findViewById(R.id.editLeave)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeavesAdapter {

        val itemView = LayoutInflater.from(context).inflate(R.layout.leaves_list_items,parent,false)
        return LeavesAdapter(itemView)
    }

    override fun getItemCount(): Int {

        return  holidayList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: LeavesAdapter, position: Int) {

        val leavesModel = holidayList[position]

        val dateTimeString = leavesModel.start_date

        val dateOnly = dateTimeString.split("T")[0]

        val formatedDate = convertDateToDDMMYY(dateOnly)

        holder.dateR.text = formatedDate

        holder.leave.text = capitalizeFirstLetter(leavesModel.name)

            if(leavesModel.day_type == 2){

            holder.dayType.text = "Full Day"

        }
        else if (leavesModel.day_type == 1){

            holder.dayType.text = "First Half"

        }
        else{

            holder.dayType.text = "Second Half"

        }

        holder.day.text = leavesModel.number_of_days

        if(leavesModel.leave_status.approved_leaves == 0 && leavesModel.leave_status.approved_leaves == 0){

            holder.statusR.text = "Pending"

            holder.statusR.setTextColor(ContextCompat.getColor(context,R.color.pending_yellow))

        }else{

            holder.statusR.text = "Approved"

        }

        if (position == itemCount - 1) {

            holder.dividerView.visibility = View.GONE

        } else {

            holder.dividerView.visibility = View.VISIBLE

        }

        holder.edit.setOnClickListener {

            listener.onItemClicked(leavesModel,position)

        }

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

    fun capitalizeFirstLetter(input: String): String {
        return input.split(" ").joinToString(" ") { it.toLowerCase().capitalize() }
    }

}