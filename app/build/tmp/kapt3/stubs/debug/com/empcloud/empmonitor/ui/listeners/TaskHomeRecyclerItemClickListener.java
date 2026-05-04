package com.empcloud.empmonitor.ui.listeners;

import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u0005H&\u00a8\u0006\n"}, d2 = {"Lcom/empcloud/empmonitor/ui/listeners/TaskHomeRecyclerItemClickListener;", "", "onItemClickTask", "", "position", "", "fetchTaskDetail", "Lcom/empcloud/empmonitor/data/remote/response/fetch_task/FetchTaskDetail;", "tag", "status", "app_debug"})
public abstract interface TaskHomeRecyclerItemClickListener {
    
    public abstract void onItemClickTask(int position, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail fetchTaskDetail, int tag, int status);
}