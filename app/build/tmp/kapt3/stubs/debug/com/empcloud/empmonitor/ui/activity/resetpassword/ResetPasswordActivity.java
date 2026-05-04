package com.empcloud.empmonitor.ui.activity.resetpassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.empcloud.empmonitor.data.remote.request.resetpassword.ResetPasswordModel;
import com.empcloud.empmonitor.data.remote.response.resetpassword.ResetPasswordResponse;
import com.empcloud.empmonitor.databinding.ActivityResetPasswordBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.login.EmailLoginActivity;
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.google.android.material.textfield.TextInputEditText;
import dagger.hilt.android.AndroidEntryPoint;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\b\u0010\u0018\u001a\u00020\u0017H\u0016J\u0012\u0010\u0019\u001a\u00020\u00172\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0014J\b\u0010\u001c\u001a\u00020\u0017H\u0014J\u0010\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0010\u0010 \u001a\u00020\u00172\u0006\u0010!\u001a\u00020\"H\u0002J\u0006\u0010#\u001a\u00020\u0017J\b\u0010$\u001a\u00020\u0017H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0010\u001a\u00020\u00118BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006%"}, d2 = {"Lcom/empcloud/empmonitor/ui/activity/resetpassword/ResetPasswordActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/empcloud/empmonitor/databinding/ActivityResetPasswordBinding;", "email", "", "listener", "Landroid/text/TextWatcher;", "ncnfpass", "Lcom/google/android/material/textfield/TextInputEditText;", "newpass", "otp", "save", "Landroid/widget/LinearLayout;", "saveDiasble", "viewModel", "Lcom/empcloud/empmonitor/ui/activity/resetpassword/ResetPasswordViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/activity/resetpassword/ResetPasswordViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "observeresetPassword", "", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "resetPassword", "resetPasswordModel", "Lcom/empcloud/empmonitor/data/remote/request/resetpassword/ResetPasswordModel;", "saveResetData", "it", "Lcom/empcloud/empmonitor/data/remote/response/resetpassword/ResetPasswordResponse;", "textObserving", "validateForm", "app_debug"})
public final class ResetPasswordActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.empcloud.empmonitor.databinding.ActivityResetPasswordBinding binding;
    private com.google.android.material.textfield.TextInputEditText newpass;
    private com.google.android.material.textfield.TextInputEditText ncnfpass;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private android.widget.LinearLayout saveDiasble;
    private android.widget.LinearLayout save;
    private android.text.TextWatcher listener;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String email = "";
    @org.jetbrains.annotations.NotNull()
    private java.lang.String otp = "";
    
    public ResetPasswordActivity() {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.activity.resetpassword.ResetPasswordViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void resetPassword(com.empcloud.empmonitor.data.remote.request.resetpassword.ResetPasswordModel resetPasswordModel) {
    }
    
    private final void observeresetPassword() {
    }
    
    private final void saveResetData(com.empcloud.empmonitor.data.remote.response.resetpassword.ResetPasswordResponse it) {
    }
    
    public final void textObserving() {
    }
    
    private final void validateForm() {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
}