package com.empcloud.empmonitor.ui.activity.mobilelogin.otp;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel;
import com.empcloud.empmonitor.data.remote.request.verifymobile.VerifyMobileModel;
import com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsData;
import com.empcloud.empmonitor.databinding.ActivityMobileOtpBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity;
import com.empcloud.empmonitor.ui.activity.mobilelogin.MobileLoginActivity;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.empcloud.empmonitor.ui.services.location_tacking.LocationService;
import com.empcloud.empmonitor.utils.broadcast_services.SmsBroadcastReceiver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00ae\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u00101\u001a\u0002022\u0006\u00103\u001a\u000204H\u0002J\u0010\u00105\u001a\u0002022\u0006\u00106\u001a\u00020%H\u0002J\u0012\u00107\u001a\u00020%2\b\u00108\u001a\u0004\u0018\u00010%H\u0002J\u0010\u00109\u001a\u00020%2\u0006\u0010:\u001a\u00020%H\u0002J!\u0010;\u001a\u00020%2\u0012\u0010\u0010\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00120\"\"\u00020\u0012H\u0002\u00a2\u0006\u0002\u0010<J \u0010=\u001a\u0002022\u0006\u0010>\u001a\u00020?2\u0006\u00103\u001a\u0002042\u0006\u0010@\u001a\u00020%H\u0002J\u0018\u0010A\u001a\u00020B2\u0006\u0010C\u001a\u00020?2\u0006\u00103\u001a\u000204H\u0002J\b\u0010D\u001a\u000202H\u0002J\b\u0010E\u001a\u000202H\u0002J\b\u0010F\u001a\u000202H\u0002J\u0012\u0010G\u001a\u0002022\b\u0010H\u001a\u0004\u0018\u00010IH\u0015J\b\u0010J\u001a\u000202H\u0014J\b\u0010K\u001a\u000202H\u0015J\u0010\u0010L\u001a\u0002022\u0006\u0010M\u001a\u00020\u000fH\u0016J\b\u0010N\u001a\u000202H\u0016J\b\u0010O\u001a\u000202H\u0002J\b\u0010P\u001a\u000202H\u0002J\u0010\u0010Q\u001a\u0002022\u0006\u00103\u001a\u000204H\u0002J\u0010\u0010R\u001a\u0002022\u0006\u0010S\u001a\u00020%H\u0002J\b\u0010T\u001a\u000202H\u0002J!\u0010U\u001a\u0002022\u0012\u0010\u0010\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00120\"\"\u00020\u0012H\u0002\u00a2\u0006\u0002\u0010VJ\b\u0010W\u001a\u000202H\u0002J\u0010\u0010X\u001a\u00020B2\u0006\u00103\u001a\u000204H\u0002J\u0010\u0010Y\u001a\u0002022\u0006\u0010Z\u001a\u00020[H\u0002J\u0010\u0010\\\u001a\u0002022\u0006\u0010]\u001a\u00020%H\u0002J\b\u0010^\u001a\u000202H\u0002J\b\u0010_\u001a\u000202H\u0002J\u0010\u0010`\u001a\u0002022\u0006\u00103\u001a\u000204H\u0002J\u0010\u0010a\u001a\u0002022\u0006\u0010b\u001a\u00020cH\u0002J\u0010\u0010d\u001a\u0002022\u0006\u0010e\u001a\u00020%H\u0002J \u0010f\u001a\u000202*\u00020\u00122\u0012\u0010g\u001a\u000e\u0012\u0004\u0012\u00020%\u0012\u0004\u0012\u0002020hH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u00020\u0005X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001f\u001a\u0004\u0018\u00010 X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00120\"X\u0082.\u00a2\u0006\u0004\n\u0002\u0010#R\u0010\u0010$\u001a\u0004\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u001eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\'\u001a\u00020(X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u001cX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u0004\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010+\u001a\u00020,8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b/\u00100\u001a\u0004\b-\u0010.\u00a8\u0006i"}, d2 = {"Lcom/empcloud/empmonitor/ui/activity/mobilelogin/otp/MobileOtpActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/empcloud/empmonitor/utils/broadcast_services/SmsBroadcastReceiver$SmsBroadcastListener;", "()V", "SMS_CONSENT_REQUEST", "", "SMS_PERMISSION_CODE", "getSMS_PERMISSION_CODE", "()I", "auth", "Lcom/google/firebase/auth/FirebaseAuth;", "binding", "Lcom/empcloud/empmonitor/databinding/ActivityMobileOtpBinding;", "consentLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "editTexts", "", "Landroid/widget/EditText;", "et1", "et2", "et3", "et4", "et5", "et6", "fusedLocationClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "locaalOtpReciever", "Landroid/content/BroadcastReceiver;", "loginbtn", "Landroid/widget/LinearLayout;", "mResendToken", "Lcom/google/firebase/auth/PhoneAuthProvider$ForceResendingToken;", "otpEditTexts", "", "[Landroid/widget/EditText;", "phoneNumberSaved", "", "resendbtn", "smsBroadcastReceiver", "Lcom/empcloud/empmonitor/utils/broadcast_services/SmsBroadcastReceiver;", "smsOtpReciever", "storedVerificationId", "viewModel", "Lcom/empcloud/empmonitor/ui/activity/mobilelogin/otp/MobileVerifiactionViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/activity/mobilelogin/otp/MobileVerifiactionViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "attemptAutoCheckIn", "", "data", "Lcom/empcloud/empmonitor/data/remote/response/tracking_settings/TrackingSettingsData;", "extractOTPAndSetToEditText", "s", "extractOTPFromMessage", "message", "extractOtp", "messageBody", "getOtpFromEditTexts", "([Landroid/widget/EditText;)Ljava/lang/String;", "handleAutoCheckInLocation", "location", "Landroid/location/Location;", "locationSource", "isUserInsideAnyGeofence", "", "userLocation", "navigateToCreateProfile", "observeAutoCheckIn", "observeTrackingSettings", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onResume", "onSmsRetrieved", "consentIntent", "onTimeout", "requestPermission", "resultLauncher", "saveTrackingSettings", "setOtpInEditText", "otp", "setupClipboardListener", "setupOtpEditTexts", "([Landroid/widget/EditText;)V", "setupSmsBroadcastReceiver", "shouldAttemptAutoCheckIn", "signInWithPhoneAuthCredential", "credential", "Lcom/google/firebase/auth/PhoneAuthCredential;", "startPhoneNumberVerification", "phoneNumber", "startSmsRetriever", "startSmsRetrieverLocal", "tryAutoCheckInWithLastKnownLocation", "verifyMobile", "verifyMobileModel", "Lcom/empcloud/empmonitor/data/remote/request/verifymobile/VerifyMobileModel;", "verifyPhoneNumberWithCode", "code", "onPaste", "callback", "Lkotlin/Function1;", "app_debug"})
public final class MobileOtpActivity extends androidx.appcompat.app.AppCompatActivity implements com.empcloud.empmonitor.utils.broadcast_services.SmsBroadcastReceiver.SmsBroadcastListener {
    private com.empcloud.empmonitor.databinding.ActivityMobileOtpBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private android.widget.EditText et1;
    private android.widget.EditText et2;
    private android.widget.EditText et3;
    private android.widget.EditText et4;
    private android.widget.EditText et5;
    private android.widget.EditText et6;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String phoneNumberSaved;
    private android.widget.LinearLayout loginbtn;
    private android.widget.LinearLayout resendbtn;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String storedVerificationId;
    @org.jetbrains.annotations.Nullable()
    private com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken mResendToken;
    private com.google.firebase.auth.FirebaseAuth auth;
    private java.util.List<? extends android.widget.EditText> editTexts;
    private android.widget.EditText[] otpEditTexts;
    private android.content.BroadcastReceiver locaalOtpReciever;
    private final int SMS_CONSENT_REQUEST = 2;
    private com.empcloud.empmonitor.utils.broadcast_services.SmsBroadcastReceiver smsBroadcastReceiver;
    private androidx.activity.result.ActivityResultLauncher<android.content.Intent> consentLauncher;
    private final int SMS_PERMISSION_CODE = 101;
    private com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient;
    private android.content.BroadcastReceiver smsOtpReciever;
    
    public MobileOtpActivity() {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.activity.mobilelogin.otp.MobileVerifiactionViewModel getViewModel() {
        return null;
    }
    
    public final int getSMS_PERMISSION_CODE() {
        return 0;
    }
    
    @java.lang.Override()
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.TIRAMISU)
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void requestPermission() {
    }
    
    private final java.lang.String extractOtp(java.lang.String messageBody) {
        return null;
    }
    
    private final void setOtpInEditText(java.lang.String otp) {
    }
    
    private final void setupClipboardListener() {
    }
    
    private final void setupOtpEditTexts(android.widget.EditText... editTexts) {
    }
    
    private final java.lang.String getOtpFromEditTexts(android.widget.EditText... editTexts) {
        return null;
    }
    
    private final void startPhoneNumberVerification(java.lang.String phoneNumber) {
    }
    
    private final void verifyPhoneNumberWithCode(java.lang.String code) {
    }
    
    private final void signInWithPhoneAuthCredential(com.google.firebase.auth.PhoneAuthCredential credential) {
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
    
    private final void extractOTPAndSetToEditText(java.lang.String s) {
    }
    
    private final void verifyMobile(com.empcloud.empmonitor.data.remote.request.verifymobile.VerifyMobileModel verifyMobileModel) {
    }
    
    private final void onPaste(android.widget.EditText $this$onPaste, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> callback) {
    }
    
    private final void startSmsRetriever() {
    }
    
    private final java.lang.String extractOTPFromMessage(java.lang.String message) {
        return null;
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    private final void startSmsRetrieverLocal() {
    }
    
    @java.lang.Override()
    public void onSmsRetrieved(@org.jetbrains.annotations.NotNull()
    android.content.Intent consentIntent) {
    }
    
    @java.lang.Override()
    public void onTimeout() {
    }
    
    @java.lang.Override()
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    protected void onResume() {
    }
    
    private final void resultLauncher() {
    }
    
    private final void setupSmsBroadcastReceiver() {
    }
}