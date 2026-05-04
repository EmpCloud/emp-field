package com.empcloud.empmonitor.ui.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.response.leaves.LeavesList;
import com.empcloud.empmonitor.ui.listeners.LeaveRecyclerItemClickListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u001bB#\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\u0002\u0010\nJ\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fJ\u0018\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\f2\b\b\u0002\u0010\u0010\u001a\u00020\fJ\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00022\u0006\u0010\u0016\u001a\u00020\u0012H\u0017J\u0018\u0010\u0017\u001a\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0012H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/LeavesRecyclerAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/empcloud/empmonitor/ui/adapters/LeavesRecyclerAdapter$LeavesAdapter;", "context", "Landroid/content/Context;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/LeaveRecyclerItemClickListener;", "holidayList", "", "Lcom/empcloud/empmonitor/data/remote/response/leaves/LeavesList;", "(Landroid/content/Context;Lcom/empcloud/empmonitor/ui/listeners/LeaveRecyclerItemClickListener;Ljava/util/List;)V", "capitalizeFirstLetter", "", "input", "convertDateToDDMMYY", "dateString", "inputFormat", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "LeavesAdapter", "app_debug"})
public final class LeavesRecyclerAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.empcloud.empmonitor.ui.adapters.LeavesRecyclerAdapter.LeavesAdapter> {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.ui.listeners.LeaveRecyclerItemClickListener listener = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.empcloud.empmonitor.data.remote.response.leaves.LeavesList> holidayList;
    
    public LeavesRecyclerAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.ui.listeners.LeaveRecyclerItemClickListener listener, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.response.leaves.LeavesList> holidayList) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.empcloud.empmonitor.ui.adapters.LeavesRecyclerAdapter.LeavesAdapter onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.ui.adapters.LeavesRecyclerAdapter.LeavesAdapter holder, int position) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String convertDateToDDMMYY(@org.jetbrains.annotations.NotNull()
    java.lang.String dateString, @org.jetbrains.annotations.NotNull()
    java.lang.String inputFormat) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String capitalizeFirstLetter(@org.jetbrains.annotations.NotNull()
    java.lang.String input) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\bR\u0011\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\bR\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0014\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\bR\u0011\u0010\u0016\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\b\u00a8\u0006\u0018"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/LeavesRecyclerAdapter$LeavesAdapter;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "dateR", "Landroid/widget/TextView;", "getDateR", "()Landroid/widget/TextView;", "day", "getDay", "dayType", "getDayType", "dividerView", "getDividerView", "()Landroid/view/View;", "edit", "Landroid/widget/ImageView;", "getEdit", "()Landroid/widget/ImageView;", "leave", "getLeave", "statusR", "getStatusR", "app_debug"})
    public static final class LeavesAdapter extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView dateR = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView leave = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView dayType = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView day = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView statusR = null;
        @org.jetbrains.annotations.NotNull()
        private final android.view.View dividerView = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView edit = null;
        
        public LeavesAdapter(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getDateR() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getLeave() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getDayType() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getDay() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getStatusR() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.view.View getDividerView() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getEdit() {
            return null;
        }
    }
}