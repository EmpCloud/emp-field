package com.empcloud.empmonitor.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.response.notification.Data;
import com.empcloud.empmonitor.data.remote.response.notification.PreviousTask;
import com.empcloud.empmonitor.data.remote.response.notification.RescheduledTask;
import com.empcloud.empmonitor.ui.fragment.notification.NotificationFragment;
import com.empcloud.empmonitor.ui.listeners.NotificationRecyclerItemClikedListener;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \"2\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0002\"#B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\u0006\u0010\r\u001a\u00020\u000eJ\b\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0010H\u0016J\u0010\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0010H\u0016J\u001c\u0010\u0015\u001a\u00020\u000e2\n\u0010\u0016\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0010H\u0016J\u001c\u0010\u0017\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0010H\u0016J\u0014\u0010\u001b\u001a\u00020\u000e2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dJ\u0014\u0010\u001f\u001a\u00020\u000e2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020!0\u001dR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/NotificationRecyclerAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/empcloud/empmonitor/ui/adapters/NotificationRecyclerAdapter$NotificationViewHolder;", "context", "Landroid/content/Context;", "data", "Lcom/empcloud/empmonitor/data/remote/response/notification/Data;", "notificationListener", "Lcom/empcloud/empmonitor/ui/listeners/NotificationRecyclerItemClikedListener;", "(Landroid/content/Context;Lcom/empcloud/empmonitor/data/remote/response/notification/Data;Lcom/empcloud/empmonitor/ui/listeners/NotificationRecyclerItemClikedListener;)V", "notificatioList", "", "", "clearAllTasks", "", "getItemCount", "", "getItemId", "", "position", "getItemViewType", "onBindViewHolder", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "updatePreviousTasks", "previousTasks", "", "Lcom/empcloud/empmonitor/data/remote/response/notification/PreviousTask;", "updateRescheduledTasks", "rescheduledTasks", "Lcom/empcloud/empmonitor/data/remote/response/notification/RescheduledTask;", "Companion", "NotificationViewHolder", "app_release"})
public final class NotificationRecyclerAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.empcloud.empmonitor.ui.adapters.NotificationRecyclerAdapter.NotificationViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.data.remote.response.notification.Data data = null;
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.ui.listeners.NotificationRecyclerItemClikedListener notificationListener = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<java.lang.Object> notificatioList;
    private static final int VIEW_TYPE_PREVIOUS = 1;
    private static final int VIEW_TYPE_RESCHEDULE = 2;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.adapters.NotificationRecyclerAdapter.Companion Companion = null;
    
    public NotificationRecyclerAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.notification.Data data, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.ui.listeners.NotificationRecyclerItemClikedListener notificationListener) {
        super();
    }
    
    @java.lang.Override()
    public int getItemViewType(int position) {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.empcloud.empmonitor.ui.adapters.NotificationRecyclerAdapter.NotificationViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.ui.adapters.NotificationRecyclerAdapter.NotificationViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    public long getItemId(int position) {
        return 0L;
    }
    
    public final void updatePreviousTasks(@org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.response.notification.PreviousTask> previousTasks) {
    }
    
    public final void updateRescheduledTasks(@org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.response.notification.RescheduledTask> rescheduledTasks) {
    }
    
    public final void clearAllTasks() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/NotificationRecyclerAdapter$Companion;", "", "()V", "VIEW_TYPE_PREVIOUS", "", "VIEW_TYPE_RESCHEDULE", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u000f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\f\u00a8\u0006\u0015"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/NotificationRecyclerAdapter$NotificationViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/empcloud/empmonitor/ui/adapters/NotificationRecyclerAdapter;Landroid/view/View;)V", "statusLay", "Landroid/widget/LinearLayout;", "getStatusLay", "()Landroid/widget/LinearLayout;", "statustxt", "Landroid/widget/TextView;", "getStatustxt", "()Landroid/widget/TextView;", "taskDescriptiontext", "getTaskDescriptiontext", "taskTitle", "getTaskTitle", "bind", "", "task", "", "app_release"})
    public final class NotificationViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView taskTitle = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView taskDescriptiontext = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView statustxt = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout statusLay = null;
        
        public NotificationViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTaskTitle() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTaskDescriptiontext() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getStatustxt() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getStatusLay() {
            return null;
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        java.lang.Object task) {
        }
    }
}