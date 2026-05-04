package com.empcloud.empmonitor.ui.fragment.attendance;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.request.attendance.AttendanceRequestModel;
import com.empcloud.empmonitor.data.remote.request.edit_attendanc.EditAttendanceModel;
import com.empcloud.empmonitor.data.remote.response.attendance.AttendanceFullData;
import com.empcloud.empmonitor.databinding.FragmentAttendanceBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity;
import com.empcloud.empmonitor.ui.adapters.AttendanceAdapter;
import com.empcloud.empmonitor.ui.fragment.home.HomeFragment;
import com.empcloud.empmonitor.ui.listeners.AttendacneRecyclerItemClickListener;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import dagger.hilt.android.AndroidEntryPoint;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0010\b\u0007\u0018\u0000 I2\u00020\u00012\u00020\u0002:\u0001IB\u0011\u0012\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\u0002\u0010\u0005J\u000e\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u0012J\b\u0010 \u001a\u00020\u0012H\u0007J\b\u0010!\u001a\u00020\u0012H\u0007J\u0016\u0010\"\u001a\u00020#2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0002J\u0016\u0010%\u001a\u00020#2\u0006\u0010&\u001a\u00020\u00122\u0006\u0010\'\u001a\u00020(J\u0010\u0010)\u001a\u00020*2\u0006\u0010\u001f\u001a\u00020\u0012H\u0002J\b\u0010+\u001a\u00020#H\u0002J\b\u0010,\u001a\u00020#H\u0002J\u0012\u0010-\u001a\u00020#2\b\u0010.\u001a\u0004\u0018\u00010/H\u0016J&\u00100\u001a\u0004\u0018\u0001012\u0006\u00102\u001a\u0002032\b\u00104\u001a\u0004\u0018\u0001052\b\u0010.\u001a\u0004\u0018\u00010/H\u0016J\u0010\u00106\u001a\u00020#2\u0006\u00107\u001a\u00020\u0012H\u0007J\u0018\u00108\u001a\u00020#2\u0006\u00109\u001a\u00020:2\u0006\u0010;\u001a\u00020\bH\u0017J\b\u0010<\u001a\u00020#H\u0016J\u001a\u0010=\u001a\u00020#2\u0006\u0010>\u001a\u0002012\b\u0010.\u001a\u0004\u0018\u00010/H\u0017J\u000e\u0010?\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u0012J\u000e\u0010@\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u0012J\u0010\u0010A\u001a\u00020#2\u0006\u00109\u001a\u00020:H\u0002J\u0006\u0010B\u001a\u00020#J\u0010\u0010C\u001a\u00020#2\u0006\u0010D\u001a\u00020:H\u0002J\b\u0010E\u001a\u00020#H\u0003J\u0010\u0010F\u001a\u00020#2\u0006\u0010D\u001a\u00020:H\u0002J\u0006\u0010G\u001a\u00020#J\b\u0010H\u001a\u00020#H\u0002R\"\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0018\u001a\u00020\u00198BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006J"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/attendance/AttendanceFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/empcloud/empmonitor/ui/listeners/AttendacneRecyclerItemClickListener;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "(Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;)V", "attList", "", "Lcom/empcloud/empmonitor/data/remote/response/attendance/AttendanceFullData;", "getAttList", "()Ljava/util/List;", "setAttList", "(Ljava/util/List;)V", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentAttendanceBinding;", "calendar", "Ljava/util/Calendar;", "editEndDate", "", "editEndTime", "editStartDate", "editTimeStart", "endDate", "startDate", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/attendance/AttendanceViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/attendance/AttendanceViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "againparseTimeData", "time", "getEndDateOfCurrentMonth", "getStartDateOfCurrentMonth", "initRecycler", "", "it", "invokeAttendance", "accessToken", "attendanceRequestModel", "Lcom/empcloud/empmonitor/data/remote/request/attendance/AttendanceRequestModel;", "isValidTimeFormat", "", "observeAttendance", "observeEditAttendance", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDateSelected", "selectedDate", "onItemClicked", "position", "", "attendanceFullData", "onResume", "onViewCreated", "view", "parseTimeData", "parseTimeFromDate", "setData", "showDatePickerDialog", "showDatePickerDialogEdit", "i", "showDatePickerDialognew", "showTimePickerDialog", "spinnerShow", "validateForm", "Companion", "app_debug"})
public final class AttendanceFragment extends androidx.fragment.app.Fragment implements com.empcloud.empmonitor.ui.listeners.AttendacneRecyclerItemClickListener {
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener = null;
    private com.empcloud.empmonitor.databinding.FragmentAttendanceBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.Calendar calendar;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String startDate;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String endDate;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String editStartDate;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String editEndDate;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String editTimeStart;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String editEndTime;
    @org.jetbrains.annotations.Nullable()
    private java.util.List<com.empcloud.empmonitor.data.remote.response.attendance.AttendanceFullData> attList;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.fragment.attendance.AttendanceFragment.Companion Companion = null;
    
    public AttendanceFragment(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.attendance.AttendanceViewModel getViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.empcloud.empmonitor.data.remote.response.attendance.AttendanceFullData> getAttList() {
        return null;
    }
    
    public final void setAttList(@org.jetbrains.annotations.Nullable()
    java.util.List<com.empcloud.empmonitor.data.remote.response.attendance.AttendanceFullData> p0) {
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
    
    private final boolean isValidTimeFormat(java.lang.String time) {
        return false;
    }
    
    public final void invokeAttendance(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.attendance.AttendanceRequestModel attendanceRequestModel) {
    }
    
    private final void observeAttendance() {
    }
    
    private final void initRecycler(java.util.List<com.empcloud.empmonitor.data.remote.response.attendance.AttendanceFullData> it) {
    }
    
    public final void showDatePickerDialog() {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getStartDateOfCurrentMonth() {
        return null;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEndDateOfCurrentMonth() {
        return null;
    }
    
    public final void spinnerShow() {
    }
    
    private final void setData(int position) {
    }
    
    private final void showDatePickerDialogEdit(int i) {
    }
    
    private final void showTimePickerDialog(int i) {
    }
    
    private final void observeEditAttendance() {
    }
    
    @java.lang.Override()
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public void onItemClicked(int position, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.attendance.AttendanceFullData attendanceFullData) {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public final void onDateSelected(@org.jetbrains.annotations.NotNull()
    java.lang.String selectedDate) {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    private final void showDatePickerDialognew() {
    }
    
    private final void validateForm() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String parseTimeData(@org.jetbrains.annotations.NotNull()
    java.lang.String time) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String parseTimeFromDate(@org.jetbrains.annotations.NotNull()
    java.lang.String time) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String againparseTimeData(@org.jetbrains.annotations.NotNull()
    java.lang.String time) {
        return null;
    }
    
    public AttendanceFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0000\u00a8\u0006\u0004"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/attendance/AttendanceFragment$Companion;", "", "()V", "newInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.empcloud.empmonitor.ui.fragment.attendance.AttendanceFragment.Companion newInstance() {
            return null;
        }
    }
}