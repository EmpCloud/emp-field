package com.empcloud.empmonitor.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail
import com.empcloud.empmonitor.data.remote.response.get_leave_type.LeaveType

class SpinnerLeaveAdapter constructor(private val context: Context,private val locations:List<LeaveType>): BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return locations.size
    }

    override fun getItem(position: Int): Any {
        return locations[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout. text_layout_leaves, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.name.text = locations[position].name



        return view
    }

    private class ItemHolder(row: View?) {
        val name: TextView

        init {
            name = row?.findViewById(R.id.textViewDateType) as TextView
        }
    }
}