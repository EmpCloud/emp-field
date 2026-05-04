package com.empcloud.empmonitor.ui.listeners;

import com.empcloud.empmonitor.data.remote.response.task_stage_selection.Data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\b"}, d2 = {"Lcom/empcloud/empmonitor/ui/listeners/TaskStageTagItemClickListener;", "", "onItemClickTags", "", "position", "", "dataTag", "Lcom/empcloud/empmonitor/data/remote/response/task_stage_selection/Data;", "app_release"})
public abstract interface TaskStageTagItemClickListener {
    
    public abstract void onItemClickTags(int position, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.task_stage_selection.Data dataTag);
}