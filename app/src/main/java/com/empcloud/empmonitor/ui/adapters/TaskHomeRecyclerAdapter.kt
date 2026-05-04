package com.empcloud.empmonitor.ui.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail
import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponseBody
import com.empcloud.empmonitor.ui.listeners.TaskHomeRecyclerItemClickListener
import com.empcloud.empmonitor.utils.ActiveTaskTracker
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import retrofit2.http.POST

class TaskHomeRecyclerAdapter(
    private val context: Context,
    private val listener: TaskHomeRecyclerItemClickListener,
    private val listData: MutableList<FetchTaskDetail>,
    private val requireActivity: FragmentActivity,
    private val isPreviousPendingTask: Boolean?
):RecyclerView.Adapter<TaskHomeRecyclerAdapter.HomeViewHolder>(){

    private var filteredItems: MutableList<FetchTaskDetail> = listData.toMutableList()
    val _filteredItems: MutableList<FetchTaskDetail> // Public getter
        get() = filteredItems

    private val colors = arrayOf(
        R.color.colorRecycler1,
        R.color.colorRecycler2,
        R.color.colorRecycler3,
        R.color.colorRecycler4,
        R.color.colorRecycler5
    )


    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val taskName:TextView = itemView.findViewById(R.id.taskName)
        val description:TextView = itemView.findViewById(R.id.taskDESC)
        val startTime:TextView = itemView.findViewById(R.id.startTime)
        val endTime:TextView = itemView.findViewById(R.id.endTime)
        val startbtn:LinearLayout = itemView.findViewById(R.id.startbtn)
        val pausebtn:LinearLayout = itemView.findViewById(R.id.pausebtn)
        val tickShhow:ImageView = itemView.findViewById(R.id.tickbtn)
        val resume:LinearLayout = itemView.findViewById(R.id.resume)
        val viewSlide:View = itemView.findViewById(R.id.viewSide)
        val delete:LinearLayout = itemView.findViewById(R.id.deleteBtn)
        val complete:LinearLayout = itemView.findViewById(R.id.completeBtn)
        val cardMain:CardView = itemView.findViewById(R.id.card_data)
        val reshedule:LinearLayout = itemView.findViewById(R.id.reschedulebtnlayout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {

        val itemView = LayoutInflater.from(context).inflate(R.layout.task_listview_items,parent,false)
        return HomeViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        Log.d("datasizechecking3","${filteredItems.size}")
        return filteredItems.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        val listModel = filteredItems[position]
        holder.taskName.text = listModel.taskName
        holder.description.text = listModel.taskDescription
        val timeStart = listModel.start_time

        val timest = listModel.start_time
        val realTimes = timest.split(" ")

        val timeet = listModel.end_time
        val realTimeet = timeet.split(" ")

        holder.startTime.text = CommonMethods.formatApiTime(extractTime(timeStart))
//        if (realTimes != null) holder.startTime.text = realTimes[1] + realTimes[2]

        val timeEnd = listModel.end_time
            holder.endTime.text = CommonMethods.formatApiTime(extractTime(timeEnd))

//        if (realTimeet.size > 2) holder.endTime.text = realTimeet[1] + realTimeet[2]
//        else      holder.endTime.text = CommonMethods.formatApiTime(extractTime(timeEnd))


        var status = listModel.taskApproveStatus


        val colorIndex = position % colors.size
        holder.viewSlide.setBackgroundColor(ContextCompat.getColor(context, colors[colorIndex]))

        holder.itemView.setOnClickListener {

//            if (listModel.taskApproveStatus != 4){

//                if (holder.pending.visibility == View.VISIBLE)   listener?.onItemClickTask(position,listModel,6,3)
//                else listener?.onItemClickTask(position,listModel,6,0)

               if (isPreviousPendingTask == true) listener?.onItemClickTask(position,listModel,6,12) else listener?.onItemClickTask(position,listModel,6,0)
//            }

        }


        if (isPreviousPendingTask != null) {

            updateButtons(holder, listModel.taskApproveStatus,isPreviousPendingTask)
        }else {

            updateButtons(holder, listModel.taskApproveStatus,false)
            if (listModel.taskApproveStatus == 3) ActiveTaskTracker.activeTaskId = listModel._id

        }


        holder.startbtn.setOnClickListener {

            if (CommonMethods.isCurrentDate(listModel.start_time)){

                val ischeckin = CommonMethods.getSharedPrefernce(requireActivity,Constants.IS_CHECKEDIN)
                val isCheckedOut = CommonMethods.getSharedPrefernce(requireActivity,Constants.IS_CHECKED_OUT)

                Log.d("chekcingdata","$isCheckedOut")
                if (ischeckin.equals("YES")){
                    if (ActiveTaskTracker.activeTaskId == null) {
                        // Start the task
                        listModel.taskApproveStatus = 1
                        ActiveTaskTracker.activeTaskId = listModel._id
                        listener?.onItemClickTask(position, listModel, 1,0)
                        notifyDataSetChanged()
                    } else {
                        listener?.onItemClickTask(position, listModel, 10,0)
//                Toast.makeText(context, "Please pause or finish the active task first", Toast.LENGTH_SHORT).show()
                    }
                }else{

                    if (isCheckedOut.equals("YES"))
                        Toast.makeText(context,"Task updation is restricted after checkout.",Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(context,"Please check-in to start the task.",Toast.LENGTH_SHORT).show()
                }
            }
         else if(CommonMethods.isDateBeforeCurrentWithTime(listModel.start_time)){

                Toast.makeText(context,"Previous task updation is restricted!",Toast.LENGTH_SHORT).show()
            }
        else{

                Toast.makeText(context,"Future task updation is restricted!",Toast.LENGTH_SHORT).show()
            }


        }

        holder.reshedule.setOnClickListener {

            listener?.onItemClickTask(position,listModel,12,12)
        }

        holder.pausebtn.setOnClickListener {
            // Pause the task
            listModel.taskApproveStatus = 2
            ActiveTaskTracker.activeTaskId = null
            listener?.onItemClickTask(position, listModel, 2,0)
            notifyDataSetChanged()
        }

        holder.resume.setOnClickListener {


            if (CommonMethods.isCurrentDate(listModel.start_time)){

                val isCheckedOut = CommonMethods.getSharedPrefernce(requireActivity,Constants.IS_CHECKED_OUT)
                if (isCheckedOut.equals("YES"))
                    Toast.makeText(context,"Task updation is restricted after checkout.",Toast.LENGTH_SHORT).show()
                else{

                    if (ActiveTaskTracker.activeTaskId == null) {
                        listModel.taskApproveStatus = 3
                        ActiveTaskTracker.activeTaskId = listModel._id
                        listener?.onItemClickTask(position, listModel, 3,0)
                        notifyDataSetChanged()
                    } else {
                        listener?.onItemClickTask(position, listModel, 10,0)

//                Toast.makeText(context, "Please pause or finish the active task first", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            else if(CommonMethods.isDateBeforeCurrentWithTime(listModel.start_time)){

                Toast.makeText(context,"Previous task updation is restricted!",Toast.LENGTH_SHORT).show()
            }
            else{

                Toast.makeText(context,"Future task updation is restricted!",Toast.LENGTH_SHORT).show()
            }


        }

        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (position == itemCount)  layoutParams.bottomMargin = context.resources.getDimensionPixelSize(R.dimen.margin_hundred)

        holder.itemView.layoutParams = layoutParams

    }

    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            listData.toMutableList()
        } else {
            listData.filter { it.taskName.contains(query, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun extractTime(time:String):String{

        val realTime = time.split(" ")

        return realTime[1]
    }

    fun removeItem(position: Int) {
        filteredItems.removeAt(position)
        notifyItemRemoved(position)
    }

    fun markItemAsFinished(position: Int) {
        // Implement your logic to mark item as finished
        // For example, you might change the appearance or move it to a different list
        notifyItemChanged(position)
    }


    fun attachToRecyclerView(recyclerView: RecyclerView) {
        val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback())
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }



    inner class ItemTouchHelperCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {


        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            val myViewHolder = viewHolder as HomeViewHolder
            val  originalTranslationX = myViewHolder.cardMain.translationX
            val position = viewHolder.adapterPosition
            Log.d("datasizechecking1","${filteredItems.size}")

            val recyclerView = viewHolder.itemView.parent as? RecyclerView
            val adapter = recyclerView?.adapter as? TaskHomeRecyclerAdapter ?: return

            val currentFilteredItems = adapter.filteredItems
            var selectedPositon = currentFilteredItems[position]

            if(selectedPositon.taskApproveStatus != 4 && direction == ItemTouchHelper.RIGHT){

//                if (CommonMethods.isNotFutureDate(selectedPositon.date) == false){
                    listener.onItemClickTask(position, selectedPositon,4,0)
                    if (ActiveTaskTracker.activeTaskId == selectedPositon._id)    ActiveTaskTracker.activeTaskId = null
//                }

            }
            if(direction == ItemTouchHelper.LEFT){
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->

                        listener.onItemClickTask(position, selectedPositon,5,0)
                        if (ActiveTaskTracker.activeTaskId == selectedPositon._id)    ActiveTaskTracker.activeTaskId = null

                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                        // Dismiss the dialog or perform any other action if needed

                        listener.onItemClickTask(position,selectedPositon,9,0)
                        dialog.dismiss()
//                        myViewHolder.delete.visibility = View.GONE
//                        myViewHolder.cardMain.visibility = View.VISIBLE
//                        myViewHolder.cardMain.translationX = 0f // Reset translation
//
//                        val alpha = 1.0f
//                        myViewHolder.cardMain.setAlpha(alpha)


                    })
                builder.create().show()

            }
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {

            val myViewHolder = viewHolder as HomeViewHolder
            val position = viewHolder.adapterPosition
//            Log.d("checkingposition","$position")

            val recyclerView = viewHolder.itemView.parent as? RecyclerView
            val adapter = recyclerView?.adapter as? TaskHomeRecyclerAdapter ?: return

            val currentFilteredItems = adapter.filteredItems
            var selectedPositon:FetchTaskDetail? = null
            if (position != RecyclerView.NO_POSITION && position < filteredItems.size){

                selectedPositon = currentFilteredItems[position]
            }
            Log.d("datasizechecking4","${filteredItems.size}")

//            if(position >= 0) selectedPositon = filteredItems[position]


            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                val myViewHolder = viewHolder as HomeViewHolder
                val itemView = myViewHolder.cardMain
                var translationX = dX

                val maxTranslation = dpToPx(100, viewHolder.itemView.context)
                val screenWidth = context.resources.displayMetrics.widthPixels.toFloat()
                if (dX < 0) { // Swiping left

                    myViewHolder.delete.visibility = View.VISIBLE
                    myViewHolder.complete.visibility = View.GONE
                    if (Math.abs(dX) > screenWidth) {
                        translationX = -screenWidth

                    }
                    itemView.translationX = translationX
                    val alpha = 1.0f - (Math.abs(dX) / maxTranslation)*0.2f
                    itemView.setAlpha(alpha)

                } else if (selectedPositon?.taskApproveStatus != 4 && dX > 0) { // Swiping right

                    myViewHolder.complete.visibility = View.VISIBLE
                    myViewHolder.delete.visibility = View.GONE
                    if (Math.abs(dX) > screenWidth) {
                        translationX = screenWidth
                    }
                    itemView.translationX = translationX
                    val alpha = 1.0f - (Math.abs(dX) / maxTranslation)*0.2f
                    itemView.setAlpha(alpha)
                }

            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            val myViewHolder = viewHolder as HomeViewHolder
            myViewHolder.cardMain.translationX = 0f // Reset translation
            val alpha = 1.0f
            myViewHolder.cardMain.setAlpha(alpha)
        }
    }

    // Helper method to convert dp to pixels
    private fun dpToPx(dp: Int, context: Context): Float {
        val displayMetrics = context.resources.displayMetrics
        return dp * (displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
    private fun updateButtons(holder: HomeViewHolder, status: Int, isPreviousPendingTask: Boolean) {

        if (!isPreviousPendingTask) {

            when (status) {
                0 -> { // Pending
                    holder.startbtn.visibility = View.VISIBLE
                    holder.pausebtn.visibility = View.GONE
                    holder.tickShhow.visibility = View.GONE
                    holder.resume.visibility = View.GONE
                    holder.reshedule.visibility = View.GONE
                }

                1 -> { // Start
                    holder.startbtn.visibility = View.GONE
                    holder.pausebtn.visibility = View.VISIBLE
                    holder.tickShhow.visibility = View.GONE
                    holder.resume.visibility = View.GONE
                    holder.reshedule.visibility = View.GONE
                }

                2 -> { // Pause
                    holder.startbtn.visibility = View.GONE
                    holder.pausebtn.visibility = View.GONE
                    holder.tickShhow.visibility = View.GONE
                    holder.resume.visibility = View.VISIBLE
                    holder.reshedule.visibility = View.GONE
                }

                3 -> { // Resume
                    holder.startbtn.visibility = View.GONE
                    holder.pausebtn.visibility = View.VISIBLE
                    holder.tickShhow.visibility = View.GONE
                    holder.resume.visibility = View.GONE
                    holder.reshedule.visibility = View.GONE
                }

                4 -> { // Finish
                    holder.startbtn.visibility = View.GONE
                    holder.pausebtn.visibility = View.GONE
                    holder.tickShhow.visibility = View.VISIBLE
                    holder.resume.visibility = View.GONE
                    holder.reshedule.visibility = View.GONE
                }
            }

        }else{

            holder.startbtn.visibility = View.GONE
            holder.pausebtn.visibility = View.GONE
            holder.tickShhow.visibility = View.GONE
            holder.resume.visibility = View.GONE
            holder.reshedule.visibility = View.VISIBLE
        }
    }

}