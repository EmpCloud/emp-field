package com.empcloud.empmonitor.ui.activity.login;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.empcloud.empmonitor.data.remote.request.login.LoginDataModel;
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel;
import com.empcloud.empmonitor.data.remote.request.verify_email.VerifyEmailModel;
import com.empcloud.empmonitor.data.remote.response.login.LoginResponse;
import com.empcloud.empmonitor.data.remote.response.markattendance.MarkAttendanceResponse;
import com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsResponse;
import com.empcloud.empmonitor.data.remote.response.verify_email.VerifyEmailResponse;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.network.NetworkRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cJ\u0016\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!J\u000e\u0010\"\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\u001fJ\u000e\u0010#\u001a\u00020\u001a2\u0006\u0010$\u001a\u00020%R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u001d\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000eR\u001d\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000eR\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/empcloud/empmonitor/ui/activity/login/LoginViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/empcloud/empmonitor/network/NetworkRepository;", "(Lcom/empcloud/empmonitor/network/NetworkRepository;)V", "loginDataFlow", "Lkotlinx/coroutines/channels/Channel;", "Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState;", "Lcom/empcloud/empmonitor/data/remote/response/login/LoginResponse;", "markAttendanceFlow", "Lcom/empcloud/empmonitor/data/remote/response/markattendance/MarkAttendanceResponse;", "observeMarkAttendance", "Lkotlinx/coroutines/flow/Flow;", "getObserveMarkAttendance", "()Lkotlinx/coroutines/flow/Flow;", "observeTrackingSettings", "Lcom/empcloud/empmonitor/data/remote/response/tracking_settings/TrackingSettingsResponse;", "getObserveTrackingSettings", "observerEmailData", "Lcom/empcloud/empmonitor/data/remote/response/verify_email/VerifyEmailResponse;", "getObserverEmailData", "observerLoginData", "getObserverLoginData", "trackingSettingsFlow", "verifyEmailFlow", "invokeLoginCall", "", "loginDataModel", "Lcom/empcloud/empmonitor/data/remote/request/login/LoginDataModel;", "invokeMarkAttendance", "accessToken", "", "markAttendanceModel", "Lcom/empcloud/empmonitor/data/remote/request/mark_attendance/MarkAttendanceModel;", "invokeTrackingSettings", "invokeVerifyEmail", "verifyEmailModel", "Lcom/empcloud/empmonitor/data/remote/request/verify_email/VerifyEmailModel;", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class LoginViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.network.NetworkRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.login.LoginResponse>> loginDataFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.login.LoginResponse>> observerLoginData = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.verify_email.VerifyEmailResponse>> verifyEmailFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.verify_email.VerifyEmailResponse>> observerEmailData = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsResponse>> trackingSettingsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsResponse>> observeTrackingSettings = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.markattendance.MarkAttendanceResponse>> markAttendanceFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.markattendance.MarkAttendanceResponse>> observeMarkAttendance = null;
    
    @javax.inject.Inject()
    public LoginViewModel(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.network.NetworkRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.login.LoginResponse>> getObserverLoginData() {
        return null;
    }
    
    public final void invokeLoginCall(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.login.LoginDataModel loginDataModel) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.verify_email.VerifyEmailResponse>> getObserverEmailData() {
        return null;
    }
    
    public final void invokeVerifyEmail(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.verify_email.VerifyEmailModel verifyEmailModel) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsResponse>> getObserveTrackingSettings() {
        return null;
    }
    
    public final void invokeTrackingSettings(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.markattendance.MarkAttendanceResponse>> getObserveMarkAttendance() {
        return null;
    }
    
    public final void invokeMarkAttendance(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel markAttendanceModel) {
    }
}