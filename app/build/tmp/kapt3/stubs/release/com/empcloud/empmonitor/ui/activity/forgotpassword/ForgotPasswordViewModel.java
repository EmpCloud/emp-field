package com.empcloud.empmonitor.ui.activity.forgotpassword;

import androidx.lifecycle.ViewModel;
import com.empcloud.empmonitor.data.remote.request.forgotpassword.ForgotPasswordDataModel;
import com.empcloud.empmonitor.data.remote.response.otpresponse.OtpResponseModel;
import com.empcloud.empmonitor.network.NetworkRepository;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0013"}, d2 = {"Lcom/empcloud/empmonitor/ui/activity/forgotpassword/ForgotPasswordViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/empcloud/empmonitor/network/NetworkRepository;", "(Lcom/empcloud/empmonitor/network/NetworkRepository;)V", "forgotDataFlow", "Lkotlinx/coroutines/channels/Channel;", "Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState;", "Lcom/empcloud/empmonitor/data/remote/response/otpresponse/OtpResponseModel;", "observerOTP", "Lkotlinx/coroutines/flow/Flow;", "getObserverOTP", "()Lkotlinx/coroutines/flow/Flow;", "getRepository", "()Lcom/empcloud/empmonitor/network/NetworkRepository;", "invokeOtpCall", "", "forgotPasswordDataModel", "Lcom/empcloud/empmonitor/data/remote/request/forgotpassword/ForgotPasswordDataModel;", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ForgotPasswordViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.network.NetworkRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.otpresponse.OtpResponseModel>> forgotDataFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.otpresponse.OtpResponseModel>> observerOTP = null;
    
    @javax.inject.Inject()
    public ForgotPasswordViewModel(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.network.NetworkRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.network.NetworkRepository getRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.otpresponse.OtpResponseModel>> getObserverOTP() {
        return null;
    }
    
    public final void invokeOtpCall(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.forgotpassword.ForgotPasswordDataModel forgotPasswordDataModel) {
    }
}