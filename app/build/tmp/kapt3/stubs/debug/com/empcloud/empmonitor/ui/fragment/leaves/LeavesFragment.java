package com.empcloud.empmonitor.ui.fragment.leaves;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.request.LEAVES.LeavesRequestModel;
import com.empcloud.empmonitor.data.remote.request.createleave.CreateLeaveRequestModel;
import com.empcloud.empmonitor.data.remote.request.delete.DeleteLeaveRequest;
import com.empcloud.empmonitor.data.remote.request.editleave.EditLeaveRequestModel;
import com.empcloud.empmonitor.data.remote.response.get_leave_type.LeaveType;
import com.empcloud.empmonitor.data.remote.response.leaves.LeavesList;
import com.empcloud.empmonitor.databinding.FragmentLeavesBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity;
import com.empcloud.empmonitor.ui.adapters.LeavesRecyclerAdapter;
import com.empcloud.empmonitor.ui.adapters.SpinnerLeaveAdapter;
import com.empcloud.empmonitor.ui.listeners.LeaveRecyclerItemClickListener;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.play.integrity.internal.i;
import dagger.hilt.android.AndroidEntryPoint;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u008e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0010\u000e\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0018\b\u0007\u0018\u0000 {2\u00020\u00012\u00020\u0002:\u0001{B\u0011\u0012\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\u0002\u0010\u0005J\u0017\u0010H\u001a\u00020\u001c2\b\u0010I\u001a\u0004\u0018\u00010JH\u0002\u00a2\u0006\u0002\u0010KJ\b\u0010L\u001a\u00020MH\u0002J\b\u0010N\u001a\u00020\u001cH\u0007J\b\u0010O\u001a\u00020\u001cH\u0007J\b\u0010P\u001a\u00020MH\u0002J\u0016\u0010Q\u001a\u00020M2\f\u0010R\u001a\b\u0012\u0004\u0012\u00020100H\u0002J\u0016\u0010S\u001a\u00020M2\u0006\u0010T\u001a\u00020\u001c2\u0006\u0010U\u001a\u00020VJ\b\u0010W\u001a\u00020MH\u0003J\b\u0010X\u001a\u00020MH\u0003J\b\u0010Y\u001a\u00020MH\u0003J\b\u0010Z\u001a\u00020MH\u0003J\b\u0010[\u001a\u00020MH\u0002J\u0012\u0010\\\u001a\u00020M2\b\u0010]\u001a\u0004\u0018\u00010^H\u0016J&\u0010_\u001a\u0004\u0018\u00010`2\u0006\u0010a\u001a\u00020b2\b\u0010c\u001a\u0004\u0018\u00010d2\b\u0010]\u001a\u0004\u0018\u00010^H\u0016J\u0010\u0010e\u001a\u00020M2\u0006\u0010f\u001a\u00020\u001cH\u0007J\u0018\u0010g\u001a\u00020M2\u0006\u0010h\u001a\u0002012\u0006\u0010i\u001a\u00020\u000bH\u0016J\b\u0010j\u001a\u00020MH\u0016J\u001a\u0010k\u001a\u00020M2\u0006\u0010l\u001a\u00020`2\b\u0010]\u001a\u0004\u0018\u00010^H\u0017J\u0018\u0010m\u001a\u00020M2\u0006\u0010i\u001a\u00020\u000b2\u0006\u0010h\u001a\u000201H\u0002J\u0010\u0010n\u001a\u00020M2\u0006\u0010o\u001a\u00020\u000bH\u0002J\u0010\u0010p\u001a\u00020M2\u0006\u0010o\u001a\u00020\u000bH\u0002J\b\u0010q\u001a\u00020MH\u0003J\b\u0010r\u001a\u00020MH\u0002J\b\u0010s\u001a\u00020MH\u0002J\u0006\u0010t\u001a\u00020MJ\u0016\u0010u\u001a\u00020M2\f\u0010R\u001a\b\u0012\u0004\u0012\u00020800H\u0002J\u0006\u0010v\u001a\u00020MJ\u0006\u0010w\u001a\u00020MJ\u0016\u0010x\u001a\u00020M2\u0006\u0010h\u001a\u0002012\u0006\u0010i\u001a\u00020\u000bJ\u0016\u0010y\u001a\u00020M2\f\u0010R\u001a\b\u0012\u0004\u0012\u00020800H\u0002J\b\u0010z\u001a\u00020MH\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\u0010\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001e\u0010\u0011\u001a\u0004\u0018\u00010\u000bX\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\u0010\u001a\u0004\b\u0012\u0010\r\"\u0004\b\u0013\u0010\u000fR\u0011\u0010\u0014\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0017\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u0011\u0010\u0019\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u001cX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u001c\u0010\"\u001a\u0004\u0018\u00010\u001cX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u001f\"\u0004\b$\u0010!R\u0010\u0010%\u001a\u0004\u0018\u00010\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010&\u001a\u0004\u0018\u00010\u000bX\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\u0010\u001a\u0004\b\'\u0010\r\"\u0004\b(\u0010\u000fR\u001e\u0010)\u001a\u0004\u0018\u00010\u000bX\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\u0010\u001a\u0004\b*\u0010\r\"\u0004\b+\u0010\u000fR\u000e\u0010,\u001a\u00020-X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010/\u001a\n\u0012\u0004\u0012\u000201\u0018\u000100X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b2\u00103\"\u0004\b4\u00105R\u0012\u00106\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u0014\u00107\u001a\b\u0012\u0004\u0012\u00020800X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020:X\u0082.\u00a2\u0006\u0002\n\u0000R\u001c\u0010;\u001a\u0004\u0018\u00010\u001cX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b<\u0010\u001f\"\u0004\b=\u0010!R\u001c\u0010>\u001a\u0004\u0018\u00010\u001cX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b?\u0010\u001f\"\u0004\b@\u0010!R\u0010\u0010A\u001a\u0004\u0018\u00010\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010B\u001a\u00020C8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\bF\u0010G\u001a\u0004\bD\u0010E\u00a8\u0006|"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/leaves/LeavesFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/empcloud/empmonitor/ui/listeners/LeaveRecyclerItemClickListener;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "(Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;)V", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentLeavesBinding;", "calendar", "Ljava/util/Calendar;", "dayCode", "", "getDayCode", "()Ljava/lang/Integer;", "setDayCode", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "dayCodeEdit", "getDayCodeEdit", "setDayCodeEdit", "defaultDay", "getDefaultDay", "()I", "defaultMonth", "getDefaultMonth", "defaultYear", "getDefaultYear", "empname", "", "endDATE", "getEndDATE", "()Ljava/lang/String;", "setEndDATE", "(Ljava/lang/String;)V", "endDATEEdit", "getEndDATEEdit", "setEndDATEEdit", "endDate", "laeaveCode", "getLaeaveCode", "setLaeaveCode", "laeaveCodeEdit", "getLaeaveCodeEdit", "setLaeaveCodeEdit", "leaveAdapter", "Lcom/empcloud/empmonitor/ui/adapters/SpinnerLeaveAdapter;", "leaveCode", "leaveList", "", "Lcom/empcloud/empmonitor/data/remote/response/leaves/LeavesList;", "getLeaveList", "()Ljava/util/List;", "setLeaveList", "(Ljava/util/List;)V", "leaveSelection", "leaveTypes", "Lcom/empcloud/empmonitor/data/remote/response/get_leave_type/LeaveType;", "spinner", "Landroid/widget/Spinner;", "startDATE", "getStartDATE", "setStartDATE", "startDATEEdit", "getStartDATEEdit", "setStartDATEEdit", "startDate", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/leaves/LeavesViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/leaves/LeavesViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "formatDate", "timestamp", "", "(Ljava/lang/Long;)Ljava/lang/String;", "getData", "", "getEndDateOfCurrentMonth", "getStartDateOfCurrentMonth", "initPopScreens", "initRecycler", "data", "invokeLeavesData", "authToken", "leavesRequestModel", "Lcom/empcloud/empmonitor/data/remote/request/LEAVES/LeavesRequestModel;", "observeCreateLeaveCall", "observeDeleteLeaves", "observeEditLeaves", "observeGetLeaveTypes", "observeLeavesData", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDateSelected", "selectedDate", "onItemClicked", "leave", "position", "onResume", "onViewCreated", "view", "openEditPopUp", "showDatePickerDialog", "i", "showDatePickerDialogEdit", "showDatePickerDialognew", "showDateRangePicker", "showNoIcon", "showSpinnerLeaveEdit", "spinnerEditLeave", "spinnerShow", "spinnerShowPopup", "spinnerShowPopupEdit", "spinnerinit", "validateForm", "Companion", "app_debug"})
public final class LeavesFragment extends androidx.fragment.app.Fragment implements com.empcloud.empmonitor.ui.listeners.LeaveRecyclerItemClickListener {
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer leaveSelection;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String empname;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String startDate;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String endDate;
    private android.widget.Spinner spinner;
    private com.empcloud.empmonitor.ui.adapters.SpinnerLeaveAdapter leaveAdapter;
    private int leaveCode = 0;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.empcloud.empmonitor.data.remote.response.get_leave_type.LeaveType> leaveTypes;
    private com.empcloud.empmonitor.databinding.FragmentLeavesBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.Calendar calendar;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String startDATE;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String endDATE;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer dayCode;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer laeaveCode;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String startDATEEdit;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String endDATEEdit;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer dayCodeEdit;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer laeaveCodeEdit;
    @org.jetbrains.annotations.Nullable()
    private java.util.List<com.empcloud.empmonitor.data.remote.response.leaves.LeavesList> leaveList;
    private final int defaultYear = 0;
    private final int defaultMonth = 0;
    private final int defaultDay = 0;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.fragment.leaves.LeavesFragment.Companion Companion = null;
    
    public LeavesFragment(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.leaves.LeavesViewModel getViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getStartDATE() {
        return null;
    }
    
    public final void setStartDATE(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getEndDATE() {
        return null;
    }
    
    public final void setEndDATE(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getDayCode() {
        return null;
    }
    
    public final void setDayCode(@org.jetbrains.annotations.Nullable()
    java.lang.Integer p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getLaeaveCode() {
        return null;
    }
    
    public final void setLaeaveCode(@org.jetbrains.annotations.Nullable()
    java.lang.Integer p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getStartDATEEdit() {
        return null;
    }
    
    public final void setStartDATEEdit(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getEndDATEEdit() {
        return null;
    }
    
    public final void setEndDATEEdit(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getDayCodeEdit() {
        return null;
    }
    
    public final void setDayCodeEdit(@org.jetbrains.annotations.Nullable()
    java.lang.Integer p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getLaeaveCodeEdit() {
        return null;
    }
    
    public final void setLaeaveCodeEdit(@org.jetbrains.annotations.Nullable()
    java.lang.Integer p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.empcloud.empmonitor.data.remote.response.leaves.LeavesList> getLeaveList() {
        return null;
    }
    
    public final void setLeaveList(@org.jetbrains.annotations.Nullable()
    java.util.List<com.empcloud.empmonitor.data.remote.response.leaves.LeavesList> p0) {
    }
    
    public final int getDefaultYear() {
        return 0;
    }
    
    public final int getDefaultMonth() {
        return 0;
    }
    
    public final int getDefaultDay() {
        return 0;
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
    
    public final void invokeLeavesData(@org.jetbrains.annotations.NotNull()
    java.lang.String authToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.LEAVES.LeavesRequestModel leavesRequestModel) {
    }
    
    private final void observeLeavesData() {
    }
    
    private final void initRecycler(java.util.List<com.empcloud.empmonitor.data.remote.response.leaves.LeavesList> data) {
    }
    
    private final void showDateRangePicker() {
    }
    
    private final java.lang.String formatDate(java.lang.Long timestamp) {
        return null;
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
    
    @java.lang.Override()
    public void onResume() {
    }
    
    private final void initPopScreens() {
    }
    
    private final void getData() {
    }
    
    private final void showDatePickerDialog(int i) {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    private final void observeCreateLeaveCall() {
    }
    
    public final void spinnerShowPopup() {
    }
    
    @java.lang.Override()
    public void onItemClicked(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.leaves.LeavesList leave, int position) {
    }
    
    private final void openEditPopUp(int position, com.empcloud.empmonitor.data.remote.response.leaves.LeavesList leave) {
    }
    
    private final void showDatePickerDialogEdit(int i) {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    private final void observeEditLeaves() {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    private final void observeDeleteLeaves() {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    private final void observeGetLeaveTypes() {
    }
    
    private final void spinnerEditLeave(java.util.List<com.empcloud.empmonitor.data.remote.response.get_leave_type.LeaveType> data) {
    }
    
    private final void spinnerinit(java.util.List<com.empcloud.empmonitor.data.remote.response.get_leave_type.LeaveType> data) {
    }
    
    public final void spinnerShowPopupEdit(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.leaves.LeavesList leave, int position) {
    }
    
    public final void showSpinnerLeaveEdit() {
    }
    
    public final void spinnerShow() {
    }
    
    private final void validateForm() {
    }
    
    private final void showNoIcon() {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    private final void showDatePickerDialognew() {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public final void onDateSelected(@org.jetbrains.annotations.NotNull()
    java.lang.String selectedDate) {
    }
    
    public LeavesFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0000\u00a8\u0006\u0004"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/leaves/LeavesFragment$Companion;", "", "()V", "newInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.empcloud.empmonitor.ui.fragment.leaves.LeavesFragment.Companion newInstance() {
            return null;
        }
    }
}