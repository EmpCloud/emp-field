package com.empcloud.empmonitor.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.task_stage_selection.Data
import com.empcloud.empmonitor.ui.fragment.task.start_task_3.StartTaskFragment
import com.empcloud.empmonitor.ui.listeners.TaskStageTagItemClickListener

class TaskStageTagsAdapter(
    private val context: Context,
    private val data: List<Data>,
    private val taskStageTagItemClickListener: TaskStageTagItemClickListener
) : RecyclerView.Adapter<TaskStageTagsAdapter.ViewHolder>() {

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

        val statusCard : CardView = itemView.findViewById(R.id.statusiconcard)
        var statusTxt : TextView = itemView.findViewById(R.id.stagestaustxt)
        val containerTagsLinear : LinearLayout = itemView.findViewById(R.id.containerTags)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(context).inflate(R.layout.stage_selection_recycler_items,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val listItem = data[position]

        if (listItem.tagName.isNotEmpty()) holder.statusTxt.text = listItem.tagName

        val colorCode = listItem.color

        if (listItem.color.isNotEmpty()) holder.statusCard.setCardBackgroundColor(Color.parseColor(colorCode))

        if (position == itemCount - 1){

            val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.bottomMargin = context.resources.getDimensionPixelSize(R.dimen.margin_fifteen)
            holder.itemView.layoutParams = layoutParams

        }

        holder.itemView.setOnClickListener {

            taskStageTagItemClickListener.onItemClickTags(position,listItem)
        }

    }
}