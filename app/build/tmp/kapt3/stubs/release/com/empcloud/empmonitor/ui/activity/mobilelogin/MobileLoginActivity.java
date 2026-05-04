package com.empcloud.empmonitor.ui.activity.mobilelogin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.empcloud.empmonitor.data.remote.request.verifymobile.VerifyMobileModel;
import com.empcloud.empmonitor.data.remote.response.mobilelogin.MobileUserData;
import com.empcloud.empmonitor.databinding.ActivityMobileLoginBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity;
import com.empcloud.empmonitor.ui.activity.mobilelogin.otp.MobileOtpActivity;
import com.empcloud.empmonitor.ui.activity.mobilelogin.otp.MobileVerifiactionViewModel;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.hbb20.CountryCodePicker;
import dagger.hilt.android.AndroidEntryPoint;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0002J\b\u0010\u0013\u001a\u00020\u0012H\u0002J\u0012\u0010\u0014\u001a\u00020\u00122\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\b\u0010\u0017\u001a\u00020\u0012H\u0014J\b\u0010\u0018\u001a\u00020\u0012H\u0014J\b\u0010\u0019\u001a\u00020\u0012H\u0014J\u0010\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0010\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u001e\u001a\u00020\u001fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000b\u001a\u00020\f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\r\u0010\u000e\u00a8\u0006 "}, d2 = {"Lcom/empcloud/empmonitor/ui/activity/mobilelogin/MobileLoginActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/empcloud/empmonitor/databinding/ActivityMobileLoginBinding;", "codePicker", "Lcom/hbb20/CountryCodePicker;", "countryCode", "", "listener", "Landroid/text/TextWatcher;", "viewModel", "Lcom/empcloud/empmonitor/ui/activity/mobilelogin/otp/MobileVerifiactionViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/activity/mobilelogin/otp/MobileVerifiactionViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "getCountryCode", "", "observerVerificationMobile", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onPause", "onResume", "proceedFurther", "mobileLoginResponse", "Lcom/empcloud/empmonitor/data/remote/response/mobilelogin/MobileUserData;", "verifyMobile", "verifyMobileModel", "Lcom/empcloud/empmonitor/data/remote/request/verifymobile/VerifyMobileModel;", "app_release"})
public final class MobileLoginActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.empcloud.empmonitor.databinding.ActivityMobileLoginBinding binding;
    private android.text.TextWatcher listener;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String countryCode;
    private com.hbb20.CountryCodePicker codePicker;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    
    public MobileLoginActivity() {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.activity.mobilelogin.otp.MobileVerifiactionViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void getCountryCode() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    @java.lang.Override()
    protected void onPause() {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    private final void verifyMobile(com.empcloud.empmonitor.data.remote.request.verifymobile.VerifyMobileModel verifyMobileModel) {
    }
    
    private final void observerVerificationMobile() {
    }
    
    private final void proceedFurther(com.empcloud.empmonitor.data.remote.response.mobilelogin.MobileUserData mobileLoginResponse) {
    }
}