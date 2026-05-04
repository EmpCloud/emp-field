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
import com.empcloud.empmonitor.ui.listeners.ClientRecyclerItemClickListener;
import com.squareup.picasso.Picasso;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u001aB#\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J\b\u0010\u0012\u001a\u00020\fH\u0016J\u0018\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\fH\u0016J\u0018\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\fH\u0016R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/ClientFetchRecyclerAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/empcloud/empmonitor/ui/adapters/ClientFetchRecyclerAdapter$ClientFetchAdapter;", "context", "Landroid/content/Context;", "attList", "", "Lcom/empcloud/empmonitor/data/remote/response/clientfetch/ClientFetchDetail;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/ClientRecyclerItemClickListener;", "(Landroid/content/Context;Ljava/util/List;Lcom/empcloud/empmonitor/ui/listeners/ClientRecyclerItemClickListener;)V", "clickedPosition", "", "filteredItems", "filter", "", "query", "", "getItemCount", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ClientFetchAdapter", "app_debug"})
public final class ClientFetchRecyclerAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.empcloud.empmonitor.ui.adapters.ClientFetchRecyclerAdapter.ClientFetchAdapter> {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> attList;
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.ui.listeners.ClientRecyclerItemClickListener listener = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> filteredItems;
    private int clickedPosition = androidx.recyclerview.widget.RecyclerView.NO_POSITION;
    
    public ClientFetchRecyclerAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> attList, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.ui.listeners.ClientRecyclerItemClickListener listener) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.empcloud.empmonitor.ui.adapters.ClientFetchRecyclerAdapter.ClientFetchAdapter onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.ui.adapters.ClientFetchRecyclerAdapter.ClientFetchAdapter holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    public final void filter(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\bR\u0011\u0010\u000f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\fR\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0015\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\fR\u0011\u0010\u0017\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\fR\u0011\u0010\u0019\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0014R\u0011\u0010\u001b\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\fR\u0011\u0010\u001d\u001a\u00020\u001e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010!\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0014R\u0011\u0010#\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0014R\u0011\u0010%\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\fR\u0011\u0010\'\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u0014\u00a8\u0006)"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/ClientFetchRecyclerAdapter$ClientFetchAdapter;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "address", "Landroid/widget/TextView;", "getAddress", "()Landroid/widget/TextView;", "call", "Landroid/widget/LinearLayout;", "getCall", "()Landroid/widget/LinearLayout;", "clientName", "getClientName", "direction", "getDirection", "dropMenu", "Landroid/widget/ImageView;", "getDropMenu", "()Landroid/widget/ImageView;", "firstLinear", "getFirstLinear", "firstRecycler", "getFirstRecycler", "locationIcon", "getLocationIcon", "message", "getMessage", "piccard", "Landroidx/cardview/widget/CardView;", "getPiccard", "()Landroidx/cardview/widget/CardView;", "profilePic", "getProfilePic", "profilePicdisable", "getProfilePicdisable", "secondRecycler", "getSecondRecycler", "upmenu", "getUpmenu", "app_debug"})
    public static final class ClientFetchAdapter extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView profilePic = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView profilePicdisable = null;
        @org.jetbrains.annotations.NotNull()
        private final androidx.cardview.widget.CardView piccard = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView clientName = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView locationIcon = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView dropMenu = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView address = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout firstLinear = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout secondRecycler = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView upmenu = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout call = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout message = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout direction = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout firstRecycler = null;
        
        public ClientFetchAdapter(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getProfilePic() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getProfilePicdisable() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.cardview.widget.CardView getPiccard() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getClientName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getLocationIcon() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getDropMenu() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getAddress() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getFirstLinear() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getSecondRecycler() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getUpmenu() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getCall() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getMessage() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getDirection() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getFirstRecycler() {
            return null;
        }
    }
}