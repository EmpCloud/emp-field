package com.empcloud.empmonitor.ui.fragment.task.select_client_3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail;
import com.empcloud.empmonitor.data.remote.response.create_task.CreateTaskDetail;
import com.empcloud.empmonitor.databinding.FragmentSelectClientBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity;
import com.empcloud.empmonitor.ui.adapters.SelectClientAdapter;
import com.empcloud.empmonitor.ui.fragment.attendance.AttendanceFragment;
import com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeViewModel;
import com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task.AddTaskFragment;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
import com.empcloud.empmonitor.ui.listeners.SelectClientItemRecyclerListener;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import dagger.hilt.android.AndroidEntryPoint;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0092\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0006\b\u0007\u0018\u0000 C2\u00020\u00012\u00020\u0002:\u0001CB\u0011\u0012\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\u0002\u0010\u0005J\b\u0010%\u001a\u00020&H\u0002J\b\u0010\'\u001a\u00020&H\u0002J\u0016\u0010(\u001a\u00020&2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00170*H\u0002J\b\u0010+\u001a\u00020&H\u0002J\u0012\u0010,\u001a\u00020&2\b\u0010-\u001a\u0004\u0018\u00010.H\u0016J&\u0010/\u001a\u0004\u0018\u0001002\u0006\u00101\u001a\u0002022\b\u00103\u001a\u0004\u0018\u0001042\b\u0010-\u001a\u0004\u0018\u00010.H\u0016J\u0018\u00105\u001a\u00020&2\u0006\u00106\u001a\u00020\u00072\u0006\u00107\u001a\u000208H\u0016J-\u00109\u001a\u00020&2\u0006\u0010:\u001a\u00020\u00072\u000e\u0010;\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00110<2\u0006\u0010=\u001a\u00020>H\u0016\u00a2\u0006\u0002\u0010?J\b\u0010@\u001a\u00020&H\u0016J\u001a\u0010A\u001a\u00020&2\u0006\u0010B\u001a\u0002002\b\u0010-\u001a\u0004\u0018\u00010.H\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u000eR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u000f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u000eR\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\"\u0010\u0015\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u00010\u0016X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001f\u001a\u00020 8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b#\u0010$\u001a\u0004\b!\u0010\"\u00a8\u0006D"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/task/select_client_3/SelectClientFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/empcloud/empmonitor/ui/listeners/SelectClientItemRecyclerListener;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "(Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;)V", "LOCATION_PERMISSION_REQUEST_CODE", "", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentSelectClientBinding;", "fusedLocationClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "lat", "", "Ljava/lang/Double;", "lon", "picdesc", "", "picresponse", "selectClientAdaper", "Lcom/empcloud/empmonitor/ui/adapters/SelectClientAdapter;", "taskList", "", "Lcom/empcloud/empmonitor/data/remote/response/clientfetch/ClientFetchDetail;", "getTaskList", "()Ljava/util/List;", "setTaskList", "(Ljava/util/List;)V", "taskName", "taskdesc", "time", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/client/clienthome/ClientHomeViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/client/clienthome/ClientHomeViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "checkLocationPermission", "", "getCurrentLocation", "initRecycler", "data", "", "observeClientFetchCall", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onItemClick", "position", "createTaskDetail", "Lcom/empcloud/empmonitor/data/remote/response/create_task/CreateTaskDetail;", "onRequestPermissionsResult", "requestCode", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onResume", "onViewCreated", "view", "Companion", "app_release"})
public final class SelectClientFragment extends androidx.fragment.app.Fragment implements com.empcloud.empmonitor.ui.listeners.SelectClientItemRecyclerListener {
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double lat;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double lon;
    private com.empcloud.empmonitor.databinding.FragmentSelectClientBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> taskList;
    private com.empcloud.empmonitor.ui.adapters.SelectClientAdapter selectClientAdaper;
    private com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String taskName;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String time;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String picresponse;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String picdesc;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String taskdesc;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.fragment.task.select_client_3.SelectClientFragment.Companion Companion = null;
    
    public SelectClientFragment(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeViewModel getViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> getTaskList() {
        return null;
    }
    
    public final void setTaskList(@org.jetbrains.annotations.Nullable()
    java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> p0) {
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
    
    private final void initRecycler(java.util.List<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail> data) {
    }
    
    @java.lang.Override()
    public void onItemClick(int position, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.create_task.CreateTaskDetail createTaskDetail) {
    }
    
    private final void getCurrentLocation() {
    }
    
    private final void checkLocationPermission() {
    }
    
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    public SelectClientFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0000\u00a8\u0006\u0004"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/task/select_client_3/SelectClientFragment$Companion;", "", "()V", "newInstance", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.empcloud.empmonitor.ui.fragment.task.select_client_3.SelectClientFragment.Companion newInstance() {
            return null;
        }
    }
}