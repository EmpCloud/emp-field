package com.empcloud.empmonitor.ui.fragment.client.clienthome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail;
import com.empcloud.empmonitor.databinding.FragmentClientHomeBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity;
import com.empcloud.empmonitor.ui.adapters.ClientFetchRecyclerAdapter;
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientFragment;
import com.empcloud.empmonitor.ui.fragment.client.clientdirection.ClientDirectionFragment;
import com.empcloud.empmonitor.ui.fragment.edit_client.update_client.UpdaeEditClientFragment;
import com.empcloud.empmonitor.ui.listeners.ClientRecyclerItemClickListener;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import dagger.hilt.android.AndroidEntryPoint;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0011\u0012\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\u0002\u0010\u0005J\u001a\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\f0\u000bJ\u0016\u0010\u0015\u001a\u00020\u00162\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0002J\b\u0010\u0017\u001a\u00020\u0016H\u0002J\u0012\u0010\u0018\u001a\u00020\u00162\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J&\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 2\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J \u0010!\u001a\u00020\u00162\u0006\u0010\"\u001a\u00020#2\u0006\u0010\u0014\u001a\u00020\f2\u0006\u0010$\u001a\u00020#H\u0016J\u001a\u0010%\u001a\u00020\u00162\u0006\u0010&\u001a\u00020\u001c2\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\r\u001a\u00020\u000e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\'"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/client/clienthome/ClientHomeFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/empcloud/empmonitor/ui/listeners/ClientRecyclerItemClickListener;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "(Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;)V", "adapter", "Lcom/empcloud/empmonitor/ui/adapters/ClientFetchRecyclerAdapter;", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentClientHomeBinding;", "deatilsList", "", "Lcom/empcloud/empmonitor/data/remote/response/clientfetch/ClientFetchDetail;", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/client/clienthome/ClientHomeViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/client/clienthome/ClientHomeViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "getFilteredData", "clientFetchDetail", "initRecycler", "", "observeClientFetchCall", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onItemClicked", "position", "", "tag", "onViewCreated", "view", "app_debug"})
public final class ClientHomeFragment extends androidx.fragment.app.Fragment implements com.empcloud.empmonitor.ui.listeners.ClientRecyclerItemClickListener {
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener = null;
    private com.empcloud.empmonitor.databinding.FragmentClientHomeBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.empcloud.empmonitor.ui.adapters.ClientFetchRecyclerAdapter adapter;
    private java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> deatilsList;
    
    public ClientHomeFragment(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void observeClientFetchCall() {
    }
    
    private final void initRecycler(java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> clientFetchDetail) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> getFilteredData(@org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> clientFetchDetail) {
        return null;
    }
    
    @java.lang.Override()
    public void onItemClicked(int position, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail clientFetchDetail, int tag) {
    }
    
    public ClientHomeFragment() {
        super();
    }
}