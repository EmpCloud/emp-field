package com.empcloud.empmonitor.ui.listeners

import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail

interface ClientRecyclerItemClickListener {

    fun onItemClicked(position:Int,clientFetchDetail: ClientFetchDetail,tag:Int)
}