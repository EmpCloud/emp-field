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
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail
import com.empcloud.empmonitor.ui.listeners.ClientRecyclerItemClickListener
import com.squareup.picasso.Picasso

class ClientFetchRecyclerAdapter(
    private val context: Context,
    private var attList: List<ClientFetchDetail>,
    private val listener: ClientRecyclerItemClickListener
):
    RecyclerView.Adapter<ClientFetchRecyclerAdapter.ClientFetchAdapter>() {

    private var filteredItems: List<ClientFetchDetail> = attList
    private var clickedPosition: Int = RecyclerView.NO_POSITION

    class ClientFetchAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val profilePic:ImageView = itemView.findViewById(R.id.profilePic)
        val profilePicdisable:ImageView = itemView.findViewById(R.id.profilePicdisable)
        val piccard:CardView = itemView.findViewById(R.id.picard)
        val clientName:TextView = itemView.findViewById(R.id.clientName)
        val locationIcon:ImageView = itemView.findViewById(R.id.location)
        val dropMenu:ImageView = itemView.findViewById(R.id.dropdown_menu)
        val address:TextView = itemView.findViewById(R.id.address)
        val firstLinear: LinearLayout = itemView.findViewById(R.id.firstRecycler)
        val secondRecycler: LinearLayout = itemView.findViewById(R.id.recyclersecond)
        val upmenu:ImageView = itemView.findViewById(R.id.up_menu)
        val call:LinearLayout = itemView.findViewById(R.id.call)
        val message:LinearLayout = itemView.findViewById(R.id.message)
        val direction:LinearLayout = itemView.findViewById(R.id.direction)
        val firstRecycler:LinearLayout = itemView.findViewById(R.id.firstRecycler)



    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClientFetchRecyclerAdapter.ClientFetchAdapter {

        val itemView = LayoutInflater.from(context).inflate(R.layout.clients_screen_listitems,parent,false)
        return ClientFetchRecyclerAdapter.ClientFetchAdapter(itemView)
    }

    override fun onBindViewHolder(
        holder: ClientFetchRecyclerAdapter.ClientFetchAdapter,
        position: Int
    ) {

        val clientModel = filteredItems[position]

        holder.clientName.text = clientModel.clientName
        holder.address.text = clientModel.address1

        if (!clientModel.clientProfilePic.isNullOrEmpty()){

            Log.d("picclent",clientModel.clientProfilePic)
            holder.profilePic.visibility = View.VISIBLE
            holder.profilePicdisable.visibility = View.GONE
            holder.piccard.visibility = View.VISIBLE
            Picasso.get().load(clientModel.clientProfilePic).into(holder.profilePic)
        }else{

            holder.profilePic.visibility = View.GONE
            holder.profilePicdisable.visibility = View.VISIBLE
            holder.piccard.visibility = View.GONE

        }

//        holder.dropMenu.setOnClickListener {
//
//            holder.firstLinear.visibility = View.GONE
//            holder.secondRecycler.visibility = View.VISIBLE
//        }

        holder.upmenu.setOnClickListener {

            holder.firstLinear.visibility = View.VISIBLE
            holder.secondRecycler.visibility = View.GONE

        }

        holder.call.setOnClickListener {
            listener?.onItemClicked(position,clientModel,1)

        }

        holder.message.setOnClickListener {
            listener?.onItemClicked(position,clientModel,2)
        }

        holder.direction.setOnClickListener {
            listener?.onItemClicked(position,clientModel,3)
        }

        holder.firstRecycler.setOnClickListener {

            listener?.onItemClicked(position,clientModel,4)
        }


//        if (!clientModel.clientProfilePic.isNullOrEmpty()){
//            Picasso.get().load(clientModel.clientProfilePic).into(holder.profilePic)
//        }
        if (position === clickedPosition) {

            holder.secondRecycler.visibility = View.VISIBLE
            holder.firstLinear.visibility = View.GONE
        } else {

            holder.firstLinear.visibility = View.VISIBLE
            holder.secondRecycler.visibility = View.GONE
        }

        // Set click listener for each item

        // Set click listener for each item
        holder.dropMenu.setOnClickListener { // Update clicked position
            val previousClickedPosition: Int = clickedPosition
            clickedPosition = position

            // Notify adapter of item changes
            notifyItemChanged(previousClickedPosition)
            notifyItemChanged(clickedPosition)
        }

        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (position == (itemCount -1)){

            layoutParams.bottomMargin = context.resources.getDimensionPixelSize(R.dimen.margin_hundred)
        }else{

            layoutParams.bottomMargin = 20
        }


        holder.itemView.layoutParams = layoutParams
    }

    override fun getItemCount(): Int {
        return  filteredItems.size
    }

    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            attList
        } else {
            attList.filter { it.clientName.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }


}
