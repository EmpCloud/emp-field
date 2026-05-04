package com.empcloud.empmonitor.ui.fragment.task.task_first;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency;
import com.empcloud.empmonitor.data.remote.request.filter_task.FilteerTaskModel;
import com.empcloud.empmonitor.data.remote.request.update_reschedule.UpdateRescheduleModel;
import com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel;
import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail;
import com.empcloud.empmonitor.databinding.FragmentTaskHomeBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity;
import com.empcloud.empmonitor.ui.adapters.TaskHomeRecyclerAdapter;
import com.empcloud.empmonitor.ui.fragment.task.start_task_3.StartTaskFragment;
import com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task.AddTaskFragment;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
import com.empcloud.empmonitor.ui.listeners.TaskHomeRecyclerItemClickListener;
import com.empcloud.empmonitor.utils.ActiveTaskTracker;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import dagger.hilt.android.AndroidEntryPoint;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00a4\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\f\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 `2\u00020\u00012\u00020\u0002:\u0001`B\u0011\u0012\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\u0002\u0010\u0005J\u0018\u00101\u001a\u0002022\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010*\u001a\u00020\u0007H\u0003J\u000e\u00103\u001a\u00020\u00112\u0006\u00104\u001a\u00020\u0011J\u000e\u00105\u001a\u0002022\u0006\u0010 \u001a\u00020\u0007J\b\u00106\u001a\u000202H\u0002J\u0016\u00107\u001a\u0002022\f\u00108\u001a\b\u0012\u0004\u0012\u00020%09H\u0002J\b\u0010:\u001a\u000202H\u0002J\b\u0010;\u001a\u000202H\u0002J\b\u0010<\u001a\u000202H\u0002J\b\u0010=\u001a\u000202H\u0002J\u0012\u0010>\u001a\u0002022\b\u0010?\u001a\u0004\u0018\u00010@H\u0016J&\u0010A\u001a\u0004\u0018\u00010B2\u0006\u0010C\u001a\u00020D2\b\u0010E\u001a\u0004\u0018\u00010F2\b\u0010?\u001a\u0004\u0018\u00010@H\u0016J(\u0010G\u001a\u0002022\u0006\u0010H\u001a\u00020\u00072\u0006\u0010I\u001a\u00020%2\u0006\u0010J\u001a\u00020\u00072\u0006\u0010 \u001a\u00020\u0007H\u0017J-\u0010K\u001a\u0002022\u0006\u0010L\u001a\u00020\u00072\u000e\u0010M\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00110N2\u0006\u0010O\u001a\u00020PH\u0016\u00a2\u0006\u0002\u0010QJ\u001a\u0010R\u001a\u0002022\u0006\u0010S\u001a\u00020B2\b\u0010?\u001a\u0004\u0018\u00010@H\u0017J\u0012\u0010T\u001a\u0002022\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0003J\u0006\u0010U\u001a\u000202J\b\u0010V\u001a\u000202H\u0002J\u0010\u0010W\u001a\u0002022\u0006\u0010X\u001a\u00020\u0007H\u0002J\u0018\u0010Y\u001a\u0002022\u0006\u0010I\u001a\u00020%2\u0006\u0010J\u001a\u00020\u0007H\u0002J\b\u0010Z\u001a\u000202H\u0002J\b\u0010[\u001a\u000202H\u0002J\u0012\u0010\\\u001a\u00020\u0007*\u00020]2\u0006\u0010^\u001a\u00020_R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u0016X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0018R\u0012\u0010\u0019\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u001aR\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001f\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010#\u001a\n\u0012\u0004\u0012\u00020%\u0018\u00010$X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\'\"\u0004\b(\u0010)R\u0012\u0010*\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u001aR\u001b\u0010+\u001a\u00020,8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b/\u00100\u001a\u0004\b-\u0010.\u00a8\u0006a"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/task/task_first/TaskHomeFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/empcloud/empmonitor/ui/listeners/TaskHomeRecyclerItemClickListener;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "(Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;)V", "LOCATION_PERMISSION_REQUEST_CODE", "", "adapterTaskFetch", "Lcom/empcloud/empmonitor/ui/adapters/TaskHomeRecyclerAdapter;", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentTaskHomeBinding;", "calendar", "Ljava/util/Calendar;", "calendarView", "Landroid/widget/CalendarView;", "date", "", "dateTxt", "fusedLocationClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "isApiSucces", "", "isPendingTaskNotification", "Ljava/lang/Boolean;", "isSwipedNo", "Ljava/lang/Integer;", "lat", "lon", "popUpTimeEnd", "popUptimeStart", "selectedDate", "status", "swipeId", "tagSaved", "taskList", "", "Lcom/empcloud/empmonitor/data/remote/response/fetch_task/FetchTaskDetail;", "getTaskList", "()Ljava/util/List;", "setTaskList", "(Ljava/util/List;)V", "taskStatus", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/task/task_first/TaskHomeViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/task/task_first/TaskHomeViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "callApiWithDate", "", "extractTime", "time", "filterApiCall", "getLastLocation", "initRecycler", "data", "", "observeFetchTaskCall", "observeFilterFetchTaskCall", "observeUpdateRescheduleCall", "observeUpdateTaskCall", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onItemClickTask", "position", "fetchTaskDetail", "tag", "onRequestPermissionsResult", "requestCode", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onViewCreated", "view", "parseDateAndSet", "showDatePickerDialog", "showNothingFound", "showTimePickerDialogPopUp", "i", "updateDate", "updateSelectedDate", "validatebtn", "dpToPx", "", "context", "Landroid/content/Context;", "Companion", "app_release"})
public final class TaskHomeFragment extends androidx.fragment.app.Fragment implements com.empcloud.empmonitor.ui.listeners.TaskHomeRecyclerItemClickListener {
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener = null;
    private int tagSaved = -1;
    private com.empcloud.empmonitor.databinding.FragmentTaskHomeBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private java.util.List<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail> taskList;
    private com.empcloud.empmonitor.ui.adapters.TaskHomeRecyclerAdapter adapterTaskFetch;
    private com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    @org.jetbrains.annotations.NotNull()
    private java.util.Calendar calendar;
    private android.widget.CalendarView calendarView;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String selectedDate;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String lat;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String lon;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String dateTxt = "";
    private int status = -1;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer isSwipedNo;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String swipeId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer taskStatus;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String date;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Boolean isPendingTaskNotification = false;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String popUptimeStart;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String popUpTimeEnd;
    private boolean isApiSucces = false;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment.Companion Companion = null;
    
    public TaskHomeFragment(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeViewModel getViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail> getTaskList() {
        return null;
    }
    
    public final void setTaskList(@org.jetbrains.annotations.Nullable()
    java.util.List<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail> p0) {
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
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    private final void parseDateAndSet(java.lang.String date) {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    private final void callApiWithDate(java.lang.String date, int taskStatus) {
    }
    
    private final void observeFetchTaskCall() {
    }
    
    private final void initRecycler(java.util.List<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail> data) {
    }
    
    @java.lang.Override()
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public void onItemClickTask(int position, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail fetchTaskDetail, int tag, int status) {
    }
    
    private final void updateDate(com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail fetchTaskDetail, int tag) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String extractTime(@org.jetbrains.annotations.NotNull()
    java.lang.String time) {
        return null;
    }
    
    public final int dpToPx(float $this$dpToPx, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return 0;
    }
    
    private final void observeUpdateTaskCall() {
    }
    
    private final void getLastLocation() {
    }
    
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    private final void observeFilterFetchTaskCall() {
    }
    
    public final void showDatePickerDialog() {
    }
    
    private final void updateSelectedDate() {
    }
    
    public final void filterApiCall(int status) {
    }
    
    private final void showNothingFound() {
    }
    
    private final void showTimePickerDialogPopUp(int i) {
    }
    
    private final void observeUpdateRescheduleCall() {
    }
    
    private final void validatebtn() {
    }
    
    public TaskHomeFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0000\u00a8\u0006\u0004"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/task/task_first/TaskHomeFragment$Companion;", "", "()V", "newInstance", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment.Companion newInstance() {
            return null;
        }
    }
}