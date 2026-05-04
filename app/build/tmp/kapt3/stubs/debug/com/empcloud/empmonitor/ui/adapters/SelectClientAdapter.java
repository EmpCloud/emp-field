package com.empcloud.empmonitor.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail;
import com.empcloud.empmonitor.ui.listeners.SelectClientItemRecyclerListener;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.squareup.picasso.Picasso;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001)BK\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u000b\u0012\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000e\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u001cJ\b\u0010\u001d\u001a\u00020\u0019H\u0016J\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0019H\u0016J\u0010\u0010!\u001a\u00020\u00192\u0006\u0010 \u001a\u00020\u0019H\u0016J\u0006\u0010\"\u001a\u00020\u0019J\u0018\u0010#\u001a\u00020\u00102\u0006\u0010$\u001a\u00020\u00022\u0006\u0010 \u001a\u00020\u0019H\u0016J\u0018\u0010%\u001a\u00020\u00022\u0006\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020\u0019H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u0012\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0017R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u0004\u0018\u00010\u000bX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0017R\u001a\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/SelectClientAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/empcloud/empmonitor/ui/adapters/SelectClientAdapter$ClientAdapter;", "context", "Landroid/content/Context;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/SelectClientItemRecyclerListener;", "listdata", "", "Lcom/empcloud/empmonitor/data/remote/response/clientfetch/ClientFetchDetail;", "lat", "", "lon", "onLocationSelected", "Lkotlin/Function1;", "", "", "(Landroid/content/Context;Lcom/empcloud/empmonitor/ui/listeners/SelectClientItemRecyclerListener;Ljava/util/List;Ljava/lang/Double;Ljava/lang/Double;Lkotlin/jvm/functions/Function1;)V", "filteredItems", "getFilteredItems", "()Ljava/util/List;", "setFilteredItems", "(Ljava/util/List;)V", "Ljava/lang/Double;", "selectedPosition", "", "filter", "query", "", "getItemCount", "getItemId", "", "position", "getItemViewType", "getSelectedPosition", "onBindViewHolder", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ClientAdapter", "app_debug"})
public final class SelectClientAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.empcloud.empmonitor.ui.adapters.SelectClientAdapter.ClientAdapter> {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.ui.listeners.SelectClientItemRecyclerListener listener = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> listdata;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double lat = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double lon = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<java.lang.Boolean, kotlin.Unit> onLocationSelected = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> filteredItems;
    private int selectedPosition = androidx.recyclerview.widget.RecyclerView.NO_POSITION;
    
    public SelectClientAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.ui.listeners.SelectClientItemRecyclerListener listener, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> listdata, @org.jetbrains.annotations.Nullable()
    java.lang.Double lat, @org.jetbrains.annotations.Nullable()
    java.lang.Double lon, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onLocationSelected) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> getFilteredItems() {
        return null;
    }
    
    public final void setFilteredItems(@org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> p0) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.empcloud.empmonitor.ui.adapters.SelectClientAdapter.ClientAdapter onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.ui.adapters.SelectClientAdapter.ClientAdapter holder, int position) {
    }
    
    public final void filter(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final int getSelectedPosition() {
        return 0;
    }
    
    @java.lang.Override()
    public int getItemViewType(int position) {
        return 0;
    }
    
    @java.lang.Override()
    public long getItemId(int position) {
        return 0L;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u000f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\fR\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0015\u001a\u00020\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0019\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0014R\u0011\u0010\u001b\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0014R\u0011\u0010\u001d\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0014\u00a8\u0006#"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/SelectClientAdapter$ClientAdapter;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "card", "Landroid/widget/LinearLayout;", "getCard", "()Landroid/widget/LinearLayout;", "clientAddress", "Landroid/widget/TextView;", "getClientAddress", "()Landroid/widget/TextView;", "clientNsme", "getClientNsme", "distance", "getDistance", "greenLoc", "Landroid/widget/ImageView;", "getGreenLoc", "()Landroid/widget/ImageView;", "piccard", "Landroidx/cardview/widget/CardView;", "getPiccard", "()Landroidx/cardview/widget/CardView;", "profilePic", "getProfilePic", "redLoc", "getRedLoc", "userPic", "getUserPic", "bind", "", "isSelected", "", "app_debug"})
    public static final class ClientAdapter extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView userPic = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView profilePic = null;
        @org.jetbrains.annotations.NotNull()
        private final androidx.cardview.widget.CardView piccard = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView clientNsme = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView clientAddress = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView distance = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView greenLoc = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView redLoc = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout card = null;
        
        public ClientAdapter(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getUserPic() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getProfilePic() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.cardview.widget.CardView getPiccard() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getClientNsme() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getClientAddress() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getDistance() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getGreenLoc() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getRedLoc() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getCard() {
            return null;
        }
        
        public final void bind(boolean isSelected) {
        }
    }
}