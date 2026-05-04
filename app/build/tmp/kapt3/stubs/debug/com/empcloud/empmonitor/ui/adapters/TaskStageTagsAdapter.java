package com.empcloud.empmonitor.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.response.task_stage_selection.Data;
import com.empcloud.empmonitor.ui.fragment.task.start_task_3.StartTaskFragment;
import com.empcloud.empmonitor.ui.listeners.TaskStageTagItemClickListener;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0015B#\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000b\u001a\u00020\fH\u0016J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\fH\u0016J\u0018\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\fH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/TaskStageTagsAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/empcloud/empmonitor/ui/adapters/TaskStageTagsAdapter$ViewHolder;", "context", "Landroid/content/Context;", "data", "", "Lcom/empcloud/empmonitor/data/remote/response/task_stage_selection/Data;", "taskStageTagItemClickListener", "Lcom/empcloud/empmonitor/ui/listeners/TaskStageTagItemClickListener;", "(Landroid/content/Context;Ljava/util/List;Lcom/empcloud/empmonitor/ui/listeners/TaskStageTagItemClickListener;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ViewHolder", "app_debug"})
public final class TaskStageTagsAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.empcloud.empmonitor.ui.adapters.TaskStageTagsAdapter.ViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.empcloud.empmonitor.data.remote.response.task_stage_selection.Data> data = null;
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.ui.listeners.TaskStageTagItemClickListener taskStageTagItemClickListener = null;
    
    public TaskStageTagsAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.response.task_stage_selection.Data> data, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.ui.listeners.TaskStageTagItemClickListener taskStageTagItemClickListener) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.empcloud.empmonitor.ui.adapters.TaskStageTagsAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.ui.adapters.TaskStageTagsAdapter.ViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u00a8\u0006\u0013"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/TaskStageTagsAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "containerTagsLinear", "Landroid/widget/LinearLayout;", "getContainerTagsLinear", "()Landroid/widget/LinearLayout;", "statusCard", "Landroidx/cardview/widget/CardView;", "getStatusCard", "()Landroidx/cardview/widget/CardView;", "statusTxt", "Landroid/widget/TextView;", "getStatusTxt", "()Landroid/widget/TextView;", "setStatusTxt", "(Landroid/widget/TextView;)V", "app_debug"})
    public static final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final androidx.cardview.widget.CardView statusCard = null;
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView statusTxt;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout containerTagsLinear = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.cardview.widget.CardView getStatusCard() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getStatusTxt() {
            return null;
        }
        
        public final void setStatusTxt(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getContainerTagsLinear() {
            return null;
        }
    }
}