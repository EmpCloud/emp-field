package com.empcloud.empmonitor.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail;
import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponseBody;
import com.empcloud.empmonitor.ui.listeners.TaskHomeRecyclerItemClickListener;
import com.empcloud.empmonitor.utils.ActiveTaskTracker;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import retrofit2.http.POST;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u000201B5\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\u0010\f\u001a\u0004\u0018\u00010\r\u00a2\u0006\u0002\u0010\u000eJ\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bJ\u0018\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u0003\u001a\u00020\u0004H\u0002J\u000e\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020 J\u000e\u0010\"\u001a\u00020\u00192\u0006\u0010#\u001a\u00020 J\b\u0010$\u001a\u00020\u0014H\u0016J\u000e\u0010%\u001a\u00020\u00192\u0006\u0010&\u001a\u00020\u0014J\u0018\u0010\'\u001a\u00020\u00192\u0006\u0010(\u001a\u00020\u00022\u0006\u0010&\u001a\u00020\u0014H\u0017J\u0018\u0010)\u001a\u00020\u00022\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\u0014H\u0016J\u000e\u0010-\u001a\u00020\u00192\u0006\u0010&\u001a\u00020\u0014J \u0010.\u001a\u00020\u00192\u0006\u0010(\u001a\u00020\u00022\u0006\u0010/\u001a\u00020\u00142\u0006\u0010\f\u001a\u00020\rH\u0002R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\b8F\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0016\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0015R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0017R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00062"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/TaskHomeRecyclerAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/empcloud/empmonitor/ui/adapters/TaskHomeRecyclerAdapter$HomeViewHolder;", "context", "Landroid/content/Context;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/TaskHomeRecyclerItemClickListener;", "listData", "", "Lcom/empcloud/empmonitor/data/remote/response/fetch_task/FetchTaskDetail;", "requireActivity", "Landroidx/fragment/app/FragmentActivity;", "isPreviousPendingTask", "", "(Landroid/content/Context;Lcom/empcloud/empmonitor/ui/listeners/TaskHomeRecyclerItemClickListener;Ljava/util/List;Landroidx/fragment/app/FragmentActivity;Ljava/lang/Boolean;)V", "_filteredItems", "get_filteredItems", "()Ljava/util/List;", "colors", "", "", "[Ljava/lang/Integer;", "filteredItems", "Ljava/lang/Boolean;", "attachToRecyclerView", "", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "dpToPx", "", "dp", "extractTime", "", "time", "filter", "query", "getItemCount", "markItemAsFinished", "position", "onBindViewHolder", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "removeItem", "updateButtons", "status", "HomeViewHolder", "ItemTouchHelperCallback", "app_debug"})
public final class TaskHomeRecyclerAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.empcloud.empmonitor.ui.adapters.TaskHomeRecyclerAdapter.HomeViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.ui.listeners.TaskHomeRecyclerItemClickListener listener = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail> listData = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.fragment.app.FragmentActivity requireActivity = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Boolean isPreviousPendingTask = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail> filteredItems;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Integer[] colors = null;
    
    public TaskHomeRecyclerAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.ui.listeners.TaskHomeRecyclerItemClickListener listener, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail> listData, @org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentActivity requireActivity, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean isPreviousPendingTask) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail> get_filteredItems() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.empcloud.empmonitor.ui.adapters.TaskHomeRecyclerAdapter.HomeViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
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
    com.empcloud.empmonitor.ui.adapters.TaskHomeRecyclerAdapter.HomeViewHolder holder, int position) {
    }
    
    public final void filter(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String extractTime(@org.jetbrains.annotations.NotNull()
    java.lang.String time) {
        return null;
    }
    
    public final void removeItem(int position) {
    }
    
    public final void markItemAsFinished(int position) {
    }
    
    public final void attachToRecyclerView(@org.jetbrains.annotations.NotNull()
    androidx.recyclerview.widget.RecyclerView recyclerView) {
    }
    
    private final float dpToPx(int dp, android.content.Context context) {
        return 0.0F;
    }
    
    private final void updateButtons(com.empcloud.empmonitor.ui.adapters.TaskHomeRecyclerAdapter.HomeViewHolder holder, int status, boolean isPreviousPendingTask) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0011\u0010\u0015\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\fR\u0011\u0010\u0017\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\fR\u0011\u0010\u0019\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\fR\u0011\u0010\u001b\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0012R\u0011\u0010\u001d\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\fR\u0011\u0010\u001f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0012R\u0011\u0010!\u001a\u00020\"\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010%\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'\u00a8\u0006("}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/TaskHomeRecyclerAdapter$HomeViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "cardMain", "Landroidx/cardview/widget/CardView;", "getCardMain", "()Landroidx/cardview/widget/CardView;", "complete", "Landroid/widget/LinearLayout;", "getComplete", "()Landroid/widget/LinearLayout;", "delete", "getDelete", "description", "Landroid/widget/TextView;", "getDescription", "()Landroid/widget/TextView;", "endTime", "getEndTime", "pausebtn", "getPausebtn", "reshedule", "getReshedule", "resume", "getResume", "startTime", "getStartTime", "startbtn", "getStartbtn", "taskName", "getTaskName", "tickShhow", "Landroid/widget/ImageView;", "getTickShhow", "()Landroid/widget/ImageView;", "viewSlide", "getViewSlide", "()Landroid/view/View;", "app_debug"})
    public static final class HomeViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView taskName = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView description = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView startTime = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView endTime = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout startbtn = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout pausebtn = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView tickShhow = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout resume = null;
        @org.jetbrains.annotations.NotNull()
        private final android.view.View viewSlide = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout delete = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout complete = null;
        @org.jetbrains.annotations.NotNull()
        private final androidx.cardview.widget.CardView cardMain = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout reshedule = null;
        
        public HomeViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTaskName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getDescription() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getStartTime() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getEndTime() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getStartbtn() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getPausebtn() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getTickShhow() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getResume() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.view.View getViewSlide() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getDelete() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getComplete() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.cardview.widget.CardView getCardMain() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getReshedule() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J@\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J \u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\bH\u0016J\u0018\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0010H\u0017\u00a8\u0006\u0017"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/TaskHomeRecyclerAdapter$ItemTouchHelperCallback;", "Landroidx/recyclerview/widget/ItemTouchHelper$SimpleCallback;", "(Lcom/empcloud/empmonitor/ui/adapters/TaskHomeRecyclerAdapter;)V", "clearView", "", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "viewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "onChildDraw", "c", "Landroid/graphics/Canvas;", "dX", "", "dY", "actionState", "", "isCurrentlyActive", "", "onMove", "target", "onSwiped", "direction", "app_debug"})
    public final class ItemTouchHelperCallback extends androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback {
        
        public ItemTouchHelperCallback() {
            super(0, 0);
        }
        
        @java.lang.Override()
        public boolean onMove(@org.jetbrains.annotations.NotNull()
        androidx.recyclerview.widget.RecyclerView recyclerView, @org.jetbrains.annotations.NotNull()
        androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, @org.jetbrains.annotations.NotNull()
        androidx.recyclerview.widget.RecyclerView.ViewHolder target) {
            return false;
        }
        
        @java.lang.Override()
        @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
        public void onSwiped(@org.jetbrains.annotations.NotNull()
        androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, int direction) {
        }
        
        @java.lang.Override()
        public void onChildDraw(@org.jetbrains.annotations.NotNull()
        android.graphics.Canvas c, @org.jetbrains.annotations.NotNull()
        androidx.recyclerview.widget.RecyclerView recyclerView, @org.jetbrains.annotations.NotNull()
        androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        }
        
        @java.lang.Override()
        public void clearView(@org.jetbrains.annotations.NotNull()
        androidx.recyclerview.widget.RecyclerView recyclerView, @org.jetbrains.annotations.NotNull()
        androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder) {
        }
    }
}