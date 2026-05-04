package com.empcloud.empmonitor.ui.fragment.home;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.health.connect.datatypes.ExerciseRoute.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.SpannableString;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.request.attendance.AttendanceRequestModel;
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel;
import com.empcloud.empmonitor.data.remote.request.mode_of_transport.ModeOFTransportModel;
import com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel;
import com.empcloud.empmonitor.data.remote.response.attendance.AttendanceResponse;
import com.empcloud.empmonitor.data.remote.response.fetchattendacne.FetchAttendanceResponse;
import com.empcloud.empmonitor.databinding.FragmentHomeBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity;
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity;
import com.empcloud.empmonitor.ui.fragment.attendance.AttendanceFragment;
import com.empcloud.empmonitor.ui.fragment.holidays.HolidaysFragment;
import com.empcloud.empmonitor.ui.fragment.leaves.LeavesFragment;
import com.empcloud.empmonitor.ui.fragment.map.MapCurrentFragment;
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
import com.empcloud.empmonitor.ui.services.location_tacking.LocationService;
import com.empcloud.empmonitor.utils.ActiveTaskTracker;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.ncorti.slidetoact.SlideToActView;
import com.squareup.picasso.Picasso;
import dagger.hilt.android.AndroidEntryPoint;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00ae\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\b\u0007\u0018\u0000 m2\u00020\u0001:\u0002mnB\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010,\u001a\u00020\b2\u0006\u0010-\u001a\u00020\bJ\u000e\u0010.\u001a\u00020\b2\u0006\u0010-\u001a\u00020\bJ\b\u0010/\u001a\u000200H\u0002J\b\u00101\u001a\u000200H\u0002J\u000e\u00102\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\bJ\u0010\u00103\u001a\u00020\b2\u0006\u00104\u001a\u00020\bH\u0002J\u0010\u00105\u001a\u0002002\u0006\u00106\u001a\u00020\bH\u0002J\u0010\u00107\u001a\u00020\b2\u0006\u00108\u001a\u00020\bH\u0007J\u0006\u00109\u001a\u00020\bJ\u0006\u0010:\u001a\u00020\bJ\b\u0010;\u001a\u000200H\u0002J\b\u0010<\u001a\u000200H\u0002J\b\u0010=\u001a\u00020\u0011H\u0002J\u0018\u0010>\u001a\u0002002\u0006\u00106\u001a\u00020\b2\u0006\u0010?\u001a\u00020@H\u0002J\b\u0010A\u001a\u000200H\u0003J\b\u0010B\u001a\u000200H\u0003J\b\u0010C\u001a\u000200H\u0002J\u0012\u0010D\u001a\u0002002\b\u0010E\u001a\u0004\u0018\u00010FH\u0016J&\u0010G\u001a\u0004\u0018\u00010H2\u0006\u0010I\u001a\u00020J2\b\u0010K\u001a\u0004\u0018\u00010L2\b\u0010E\u001a\u0004\u0018\u00010FH\u0016J\b\u0010M\u001a\u000200H\u0016J\b\u0010N\u001a\u000200H\u0016J+\u0010O\u001a\u0002002\u0006\u0010P\u001a\u00020\u00062\f\u0010Q\u001a\b\u0012\u0004\u0012\u00020\b0R2\u0006\u0010S\u001a\u00020TH\u0016\u00a2\u0006\u0002\u0010UJ\b\u0010V\u001a\u000200H\u0017J\u001a\u0010W\u001a\u0002002\u0006\u0010X\u001a\u00020H2\b\u0010E\u001a\u0004\u0018\u00010FH\u0017J\u000e\u0010Y\u001a\u00020\b2\u0006\u0010-\u001a\u00020\bJ\b\u0010Z\u001a\u000200H\u0002J\u0006\u0010[\u001a\u000200J\b\u0010\\\u001a\u000200H\u0002J\u0010\u0010\\\u001a\u0002002\u0006\u0010-\u001a\u00020\bH\u0002J\u0006\u0010]\u001a\u000200J\b\u0010^\u001a\u000200H\u0007J\u0010\u0010_\u001a\u0002002\u0006\u0010`\u001a\u00020aH\u0002J\b\u0010b\u001a\u000200H\u0002J\u0010\u0010c\u001a\u0002002\u0006\u0010d\u001a\u00020eH\u0002J\b\u0010f\u001a\u000200H\u0002J\b\u0010g\u001a\u000200H\u0002J\b\u0010h\u001a\u000200H\u0002J\b\u0010i\u001a\u000200H\u0002J\b\u0010j\u001a\u000200H\u0002J\b\u0010k\u001a\u000200H\u0002J\b\u0010l\u001a\u000200H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0015R\u0012\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0018R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082.\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u001d\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0018R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010#\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u0004\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010&\u001a\u00020\'8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b*\u0010+\u001a\u0004\b(\u0010)\u00a8\u0006o"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/home/HomeFragment;", "Landroidx/fragment/app/Fragment;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "(Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;)V", "LOCATION_PERMISSION_REQUEST_CODE", "", "_mode_local", "", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentHomeBinding;", "checkIn", "fusedLocationClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "handler", "Landroid/os/Handler;", "isGlobal", "", "isModeSelected", "isReversed", "isServiceStart", "Ljava/lang/Boolean;", "lat", "", "Ljava/lang/Double;", "locationManager", "Landroid/location/LocationManager;", "locationProviderReceiver", "Landroid/content/BroadcastReceiver;", "lon", "mode", "slideActView", "Lcom/ncorti/slidetoact/SlideToActView;", "timeCheckIn", "timeCheckout", "timeLast", "updateRunnable", "Ljava/lang/Runnable;", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/home/HomeViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/home/HomeViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "SPLit", "time", "againparseTimeData", "checkLocationPermissionAndStartService", "", "commonSelection", "convertTimeToSeconds", "extractDist", "yesterdayDist", "fetchAttendance", "authToken", "formatApiTime", "apiTime", "getCurentFormattedDate", "getCurrentTime", "getLastLocation", "handleBatteryOptimizations", "isLocationEnabled", "markAttendance", "markAttendanceModel", "Lcom/empcloud/empmonitor/data/remote/request/mark_attendance/MarkAttendanceModel;", "observeAttendanceCall", "observeAttendanceFetchCall", "observeUpdationTransport", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroy", "onDestroyView", "onRequestPermissionsResult", "requestCode", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onResume", "onViewCreated", "view", "parseTimeData", "resetSliderPosition", "setProfileData", "setTime", "setUserData", "setYesterdayDate", "showCheckoutConfirmationDialog", "context", "Landroid/content/Context;", "showPopUpConfirmation", "showSwipeBasedUponStatus", "it", "Lcom/empcloud/empmonitor/data/remote/response/fetchattendacne/FetchAttendanceResponse;", "slideToAct", "startLocationService", "startUpdatingTime", "stopLocationService", "stopUpdatingTime", "updateSliderForCheckIn", "updateSliderForCheckOut", "Companion", "LocationReciever", "app_debug"})
public final class HomeFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String checkIn;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String mode;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String _mode_local;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Boolean isServiceStart;
    private com.empcloud.empmonitor.databinding.FragmentHomeBinding binding;
    private com.ncorti.slidetoact.SlideToActView slideActView;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private boolean isReversed = false;
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler handler = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Runnable updateRunnable;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String timeLast;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean isModeSelected = false;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String timeCheckIn;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String timeCheckout;
    private android.location.LocationManager locationManager;
    private android.content.BroadcastReceiver locationProviderReceiver;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double lat;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double lon;
    private com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient;
    private boolean isGlobal = false;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.fragment.home.HomeFragment.Companion Companion = null;
    
    public HomeFragment(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.home.HomeViewModel getViewModel() {
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
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public void onResume() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurentFormattedDate() {
        return null;
    }
    
    private final void slideToAct() {
    }
    
    private final void resetSliderPosition() {
    }
    
    private final void updateSliderForCheckIn() {
    }
    
    private final void updateSliderForCheckOut() {
    }
    
    private final void markAttendance(java.lang.String authToken, com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel markAttendanceModel) {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    private final void observeAttendanceCall() {
    }
    
    private final void fetchAttendance(java.lang.String authToken) {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    private final void observeAttendanceFetchCall() {
    }
    
    private final java.lang.String extractDist(java.lang.String yesterdayDist) {
        return null;
    }
    
    private final void showSwipeBasedUponStatus(com.empcloud.empmonitor.data.remote.response.fetchattendacne.FetchAttendanceResponse it) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String SPLit(@org.jetbrains.annotations.NotNull()
    java.lang.String time) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentTime() {
        return null;
    }
    
    public final int convertTimeToSeconds(@org.jetbrains.annotations.NotNull()
    java.lang.String time) {
        return 0;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatApiTime(@org.jetbrains.annotations.NotNull()
    java.lang.String apiTime) {
        return null;
    }
    
    private final void setTime(java.lang.String time) {
    }
    
    private final void setTime() {
    }
    
    private final void startUpdatingTime() {
    }
    
    private final void stopUpdatingTime() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    private final void checkLocationPermissionAndStartService() {
    }
    
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    private final void startLocationService() {
    }
    
    private final void stopLocationService() {
    }
    
    private final void handleBatteryOptimizations() {
    }
    
    private final void showCheckoutConfirmationDialog(android.content.Context context) {
    }
    
    private final void showPopUpConfirmation() {
    }
    
    private final void getLastLocation() {
    }
    
    private final boolean isLocationEnabled() {
        return false;
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String parseTimeData(@org.jetbrains.annotations.NotNull()
    java.lang.String time) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String againparseTimeData(@org.jetbrains.annotations.NotNull()
    java.lang.String time) {
        return null;
    }
    
    public final void setProfileData() {
    }
    
    public final void setUserData() {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public final void setYesterdayDate() {
    }
    
    private final void observeUpdationTransport() {
    }
    
    private final void commonSelection() {
    }
    
    public HomeFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0000\u00a8\u0006\u0004"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/home/HomeFragment$Companion;", "", "()V", "newInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.empcloud.empmonitor.ui.fragment.home.HomeFragment.Companion newInstance() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0017R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\r"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/home/HomeFragment$LocationReciever;", "Landroid/content/BroadcastReceiver;", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/home/HomeViewModel;", "(Lcom/empcloud/empmonitor/ui/fragment/home/HomeViewModel;)V", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/home/HomeViewModel;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "app_debug"})
    public static final class LocationReciever extends android.content.BroadcastReceiver {
        @org.jetbrains.annotations.NotNull()
        private final com.empcloud.empmonitor.ui.fragment.home.HomeViewModel viewModel = null;
        
        public LocationReciever(@org.jetbrains.annotations.NotNull()
        com.empcloud.empmonitor.ui.fragment.home.HomeViewModel viewModel) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.empcloud.empmonitor.ui.fragment.home.HomeViewModel getViewModel() {
            return null;
        }
        
        @java.lang.Override()
        @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        public void onReceive(@org.jetbrains.annotations.Nullable()
        android.content.Context context, @org.jetbrains.annotations.Nullable()
        android.content.Intent intent) {
        }
    }
}