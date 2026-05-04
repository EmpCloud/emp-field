package com.empcloud.empmonitor.ui.fragment.notification;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.response.notification.Data;
import com.empcloud.empmonitor.databinding.FragmentNotificationBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity;
import com.empcloud.empmonitor.ui.adapters.NotificationRecyclerAdapter;
import com.empcloud.empmonitor.ui.fragment.home.HomeFragment;
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment;
import com.empcloud.empmonitor.ui.listeners.NotificationRecyclerItemClikedListener;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import dagger.hilt.android.AndroidEntryPoint;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0011\u0012\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\u0010\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0011H\u0002J\u0012\u0010\u0016\u001a\u00020\u00112\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J&\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\u0010\u0010\u001f\u001a\u00020\u00112\u0006\u0010 \u001a\u00020!H\u0016J\b\u0010\"\u001a\u00020\u0011H\u0016J\u001a\u0010#\u001a\u00020\u00112\u0006\u0010$\u001a\u00020\u001a2\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\n\u001a\u00020\u000b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\f\u0010\r\u00a8\u0006%"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/notification/NotificationFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/empcloud/empmonitor/ui/listeners/NotificationRecyclerItemClikedListener;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "(Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;)V", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentNotificationBinding;", "notificationRecyclerAdapter", "Lcom/empcloud/empmonitor/ui/adapters/NotificationRecyclerAdapter;", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/notification/NotificationViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/notification/NotificationViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "animateClose", "", "initRecycler", "data", "Lcom/empcloud/empmonitor/data/remote/response/notification/Data;", "observeNotificationData", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onNotificationItemClicked", "date", "", "onResume", "onViewCreated", "view", "app_release"})
public final class NotificationFragment extends androidx.fragment.app.Fragment implements com.empcloud.empmonitor.ui.listeners.NotificationRecyclerItemClikedListener {
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener = null;
    private com.empcloud.empmonitor.databinding.FragmentNotificationBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.empcloud.empmonitor.ui.adapters.NotificationRecyclerAdapter notificationRecyclerAdapter;
    
    public NotificationFragment(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.notification.NotificationViewModel getViewModel() {
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
    
    private final void animateClose() {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    private final void observeNotificationData() {
    }
    
    private final void initRecycler(com.empcloud.empmonitor.data.remote.response.notification.Data data) {
    }
    
    @java.lang.Override()
    public void onNotificationItemClicked(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
    }
    
    public NotificationFragment() {
        super();
    }
}