package com.empcloud.empmonitor.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail
import com.empcloud.empmonitor.ui.listeners.SelectClientItemRecyclerListener
import com.empcloud.empmonitor.utils.CommonMethods
import com.squareup.picasso.Picasso

class SelectClientAdapter(
    private val context: Context,
    private val listener: SelectClientItemRecyclerListener,
    private var listdata: List<ClientFetchDetail>,
    private val lat: Double?,
    private val lon: Double?,
    private val onLocationSelected: (Boolean) -> Unit
):RecyclerView.Adapter<SelectClientAdapter.ClientAdapter> (){

//    private var filteredItems: MutableList<ClientFetchDetail> = listdata.toMutableList()
    public var filteredItems: List<ClientFetchDetail> = listdata

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    class ClientAdapter(itemView: View):RecyclerView.ViewHolder(itemView) {

        val userPic:ImageView = itemView.findViewById(R.id.userImg)
        val profilePic:ImageView = itemView.findViewById(R.id.profilePicselect)
        val piccard: CardView = itemView.findViewById(R.id.picardselect)
        val clientNsme:TextView = itemView.findViewById(R.id.clientNameSelect)
        val clientAddress:TextView = itemView.findViewById(R.id.clientAddressSelect)
        val distance:TextView = itemView.findViewById(R.id.distanceSelect)
        val greenLoc:ImageView = itemView.findViewById(R.id.greenLOc)
        val redLoc:ImageView = itemView.findViewById(R.id.redLoc)
        val card:LinearLayout = itemView.findViewById(R.id.card)


        fun bind(isSelected: Boolean) {

            // Update itemView border based on selection state
            if (isSelected) {
                card.setBackgroundResource(R.drawable.recycler_selected_client_bg)

            } else {
                card.setBackgroundResource(R.drawable.edit_text_back)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientAdapter {
        val itemView = LayoutInflater.from(context).inflate(R.layout.task_listitems_screen,parent,false)
        return SelectClientAdapter.ClientAdapter(itemView)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    override fun onBindViewHolder(holder: ClientAdapter, position: Int) {

        val dataModel = filteredItems[position]
        val isSelected = position == selectedPosition
        holder.bind(isSelected)
        holder.clientNsme.text = dataModel.clientName
        holder.clientAddress.text = dataModel.address1
        val pic = dataModel.clientProfilePic

        holder.piccard.visibility = View.GONE
        holder.userPic.visibility = View.GONE
        holder.profilePic.setImageDrawable(null)

//        if(!pic.isNullOrEmpty()){
//            Picasso.get().load(pic).into(holder.userPic)
//        }

        if (!pic.isNullOrEmpty()){

            holder.piccard.visibility = View.VISIBLE
            holder.userPic.visibility = View.GONE
            Picasso.get().load(pic).into(holder.profilePic)
        }else{

            holder.userPic.visibility = View.VISIBLE
            holder.piccard.visibility = View.GONE
        }

        holder.distance.visibility = View.VISIBLE

        if(!dataModel.clientName.equals("Default Client")) {
            if (lat != null && lon != null && dataModel.latitude != null && dataModel.longitude != null) {
                val distance = CommonMethods.getLocationDistance(
                    lat!!,
                    lon!!,
                    dataModel.latitude.toDouble(),
                    dataModel.longitude.toDouble()
                )
                if (distance != null) holder.distance.text = distance
                val distanceCalc = distance.split(" ")
                val disInt = distanceCalc[0].toInt()
                if (disInt <= 10) {
                    holder.greenLoc.visibility = View.VISIBLE

                    holder.redLoc.visibility = View.GONE
                } else {
                    holder.redLoc.visibility = View.VISIBLE

                    holder.greenLoc.visibility = View.GONE
                }
            }
        }else{

            holder.greenLoc.visibility = View.GONE
            holder.distance.visibility = View.GONE
        }



        // Handle item click
        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition

            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            onLocationSelected(selectedPosition != RecyclerView.NO_POSITION)

        }



    }

//    fun filter(query: String) {
//
//        filteredItems = if (query.isEmpty()) {
//            listdata.toMutableList()
//        } else {
//            listdata.filter { it.clientName.contains(query, ignoreCase = true) }.toMutableList()
//        }
//        notifyDataSetChanged()
//    }

    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            listdata
        } else {
            listdata.filter { it.clientName.contains(query, ignoreCase = true) }
        }
        selectedPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }


    fun getSelectedPosition(): Int {
        return selectedPosition
    }


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}