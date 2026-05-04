package com.empcloud.empmonitor.ui.activity.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.empcloud.empmonitor.data.remote.request.forgotpassword.ForgotPasswordDataModel;
import com.empcloud.empmonitor.data.remote.request.login.LoginDataModel;
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel;
import com.empcloud.empmonitor.data.remote.response.login.LoginResponse;
import com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsData;
import com.empcloud.empmonitor.databinding.ActivityEmailPasswordBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity;
import com.empcloud.empmonitor.ui.activity.forgotpassword.ForgotPasswordActivity;
import com.empcloud.empmonitor.ui.activity.forgotpassword.ForgotPasswordViewModel;
import com.empcloud.empmonitor.ui.services.location_tacking.LocationService;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.material.textfield.TextInputEditText;
import dagger.hilt.android.AndroidEntryPoint;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0010\u0010!\u001a\u00020\u001e2\u0006\u0010\"\u001a\u00020#H\u0002J \u0010$\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020&2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010\'\u001a\u00020\u0006H\u0002J\u0018\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020&2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0010\u0010+\u001a\u00020\u001e2\u0006\u0010,\u001a\u00020-H\u0002J\b\u0010.\u001a\u00020\u001eH\u0002J\b\u0010/\u001a\u00020\u001eH\u0002J\b\u00100\u001a\u00020\u001eH\u0002J\b\u00101\u001a\u00020\u001eH\u0002J\b\u00102\u001a\u00020\u001eH\u0002J\u0012\u00103\u001a\u00020\u001e2\b\u00104\u001a\u0004\u0018\u000105H\u0014J\b\u00106\u001a\u00020\u001eH\u0014J\u0010\u00107\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0010\u00108\u001a\u00020\u001e2\u0006\u00109\u001a\u00020:H\u0002J\u0010\u0010;\u001a\u00020)2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0006\u0010<\u001a\u00020\u001eJ\u0010\u0010=\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0012\u001a\u00020\u00138BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0016\u0010\u0017\u001a\u0004\b\u0014\u0010\u0015R\u001b\u0010\u0018\u001a\u00020\u00198BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001c\u0010\u0017\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006>"}, d2 = {"Lcom/empcloud/empmonitor/ui/activity/login/EmailPasswordActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/empcloud/empmonitor/databinding/ActivityEmailPasswordBinding;", "email", "", "forgotPass", "Landroid/widget/TextView;", "fusedLocationClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "listener", "Landroid/text/TextWatcher;", "loginbtnflase", "Landroid/widget/LinearLayout;", "loginbtntrue", "password", "Lcom/google/android/material/textfield/TextInputEditText;", "viewModel", "Lcom/empcloud/empmonitor/ui/activity/login/LoginViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/activity/login/LoginViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "viewModel1", "Lcom/empcloud/empmonitor/ui/activity/forgotpassword/ForgotPasswordViewModel;", "getViewModel1", "()Lcom/empcloud/empmonitor/ui/activity/forgotpassword/ForgotPasswordViewModel;", "viewModel1$delegate", "attemptAutoCheckIn", "", "data", "Lcom/empcloud/empmonitor/data/remote/response/tracking_settings/TrackingSettingsData;", "forgotOtpRequest", "forgotPasswordDataModel", "Lcom/empcloud/empmonitor/data/remote/request/forgotpassword/ForgotPasswordDataModel;", "handleAutoCheckInLocation", "location", "Landroid/location/Location;", "locationSource", "isUserInsideAnyGeofence", "", "userLocation", "loginRequestCall", "loginDataModel", "Lcom/empcloud/empmonitor/data/remote/request/login/LoginDataModel;", "navigateToCreateProfile", "observeAutoCheckIn", "observeLoginData", "observeOtp", "observeTrackingSettings", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "saveTrackingSettings", "saveUserData", "it", "Lcom/empcloud/empmonitor/data/remote/response/login/LoginResponse;", "shouldAttemptAutoCheckIn", "textObserving", "tryAutoCheckInWithLastKnownLocation", "app_debug"})
public final class EmailPasswordActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.empcloud.empmonitor.databinding.ActivityEmailPasswordBinding binding;
    private com.google.android.material.textfield.TextInputEditText password;
    private android.text.TextWatcher listener;
    private android.widget.LinearLayout loginbtnflase;
    private android.widget.LinearLayout loginbtntrue;
    private android.widget.TextView forgotPass;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String email;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel1$delegate = null;
    private com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient;
    
    public EmailPasswordActivity() {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.activity.login.LoginViewModel getViewModel() {
        return null;
    }
    
    private final com.empcloud.empmonitor.ui.activity.forgotpassword.ForgotPasswordViewModel getViewModel1() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    public final void textObserving() {
    }
    
    private final void loginRequestCall(com.empcloud.empmonitor.data.remote.request.login.LoginDataModel loginDataModel) {
    }
    
    private final void observeLoginData() {
    }
    
    private final void saveUserData(com.empcloud.empmonitor.data.remote.response.login.LoginResponse it) {
    }
    
    private final void navigateToCreateProfile() {
    }
    
    private final void saveTrackingSettings(com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsData data) {
    }
    
    private final void observeTrackingSettings() {
    }
    
    private final void attemptAutoCheckIn(com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsData data) {
    }
    
    private final void tryAutoCheckInWithLastKnownLocation(com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsData data) {
    }
    
    private final void handleAutoCheckInLocation(android.location.Location location, com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsData data, java.lang.String locationSource) {
    }
    
    private final boolean shouldAttemptAutoCheckIn(com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsData data) {
        return false;
    }
    
    private final boolean isUserInsideAnyGeofence(android.location.Location userLocation, com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsData data) {
        return false;
    }
    
    private final void observeAutoCheckIn() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    private final void forgotOtpRequest(com.empcloud.empmonitor.data.remote.request.forgotpassword.ForgotPasswordDataModel forgotPasswordDataModel) {
    }
    
    private final void observeOtp() {
    }
}