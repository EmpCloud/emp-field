package com.empcloud.empmonitor.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.notification.Data
import com.empcloud.empmonitor.data.remote.response.notification.PreviousTask
import com.empcloud.empmonitor.data.remote.response.notification.RescheduledTask
import com.empcloud.empmonitor.ui.fragment.notification.NotificationFragment
import com.empcloud.empmonitor.ui.listeners.NotificationRecyclerItemClikedListener

class NotificationRecyclerAdapter(
    private val context: Context,
    private val data: Data,
    private val notificationListener:NotificationRecyclerItemClikedListener
):RecyclerView.Adapter<NotificationRecyclerAdapter.NotificationViewHolder>() {

    private var notificatioList : MutableList<Any> = mutableListOf()

    companion object{

        private const val VIEW_TYPE_PREVIOUS = 1
        private const val VIEW_TYPE_RESCHEDULE = 2

    }

    init {

        notificatioList.addAll(data.previousTasks)
        notificatioList.addAll(data.rescheduledTasks)
    }

    override fun getItemViewType(position: Int): Int {
        return when(notificatioList[position]){

            is PreviousTask -> VIEW_TYPE_PREVIOUS
            is RescheduledTask -> VIEW_TYPE_RESCHEDULE
            else -> throw  IllegalArgumentException("Invalid task type")
        }
    }
    inner class NotificationViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val taskTitle:TextView = itemView.findViewById(R.id.tasktitle)
        val taskDescriptiontext:TextView = itemView.findViewById(R.id.taskdescription)
        val statustxt:TextView = itemView.findViewById(R.id.taskstatustxt)
        val statusLay:LinearLayout = itemView.findViewById(R.id.statuslayout)

        fun bind(task:Any){
            when(task){
                is PreviousTask -> {

                    taskTitle.text = task.taskName
                    taskDescriptiontext.text = task.taskDescription
                    statustxt.text = "Pending Task"
                    itemView.setOnClickListener {

                        notificationListener.onNotificationItemClicked(task.date)

                    }
                }
                is RescheduledTask -> {

                    taskTitle.text = task.taskName
                    taskDescriptiontext.text = task.taskDescription
                    statustxt.text = "Reschedule Task"
                    statusLay.setBackgroundResource(R.drawable.butoon_bg_shape)
                    itemView.setOnClickListener {

                        notificationListener.onNotificationItemClicked(task.date)
                    }
                }
            }


        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationRecyclerAdapter.NotificationViewHolder {

        val itemView = LayoutInflater.from(context).inflate(R.layout.notificationrecycleritem,parent,false)
        return NotificationViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: NotificationRecyclerAdapter.NotificationViewHolder,
        position: Int
    ) {

        val list = notificatioList[position]
        holder.bind(list)

    }

    override fun getItemCount(): Int {

        return notificatioList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
    fun updatePreviousTasks(previousTasks: List<PreviousTask>) {
        notificatioList.addAll(previousTasks)
        notifyDataSetChanged()
    }

    // Method to update rescheduled tasks
    fun updateRescheduledTasks(rescheduledTasks: List<RescheduledTask>) {
        notificatioList.addAll(rescheduledTasks)
        notifyDataSetChanged()
    }

    fun clearAllTasks() {
        // Notify that all items are about to be removed
        notifyItemRangeRemoved(0, notificatioList.size)
        notificatioList.clear()
        // Notify that the list has been cleared
        notifyDataSetChanged()
    }
}