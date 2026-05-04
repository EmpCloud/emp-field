package com.empcloud.empmonitor.ui.activity.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.empcloud.empmonitor.data.remote.request.forgotpassword.ForgotPasswordDataModel;
import com.empcloud.empmonitor.databinding.ActivityForgotPasswordBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity;
import com.empcloud.empmonitor.ui.activity.resetpassword.ResetPasswordActivity;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import dagger.hilt.android.AndroidEntryPoint;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0002J!\u0010!\u001a\u00020\f2\u0012\u0010\b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0\"\"\u00020\nH\u0002\u00a2\u0006\u0002\u0010#J\b\u0010$\u001a\u00020\u001eH\u0002J\b\u0010%\u001a\u00020\u001eH\u0016J\u0012\u0010&\u001a\u00020\u001e2\b\u0010\'\u001a\u0004\u0018\u00010(H\u0014J!\u0010)\u001a\u00020\u001e2\u0012\u0010\b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0\"\"\u00020\nH\u0002\u00a2\u0006\u0002\u0010*R\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0017\u001a\u00020\u00188BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001b\u0010\u001c\u001a\u0004\b\u0019\u0010\u001a\u00a8\u0006+"}, d2 = {"Lcom/empcloud/empmonitor/ui/activity/forgotpassword/ForgotPasswordActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "Otp", "", "Ljava/lang/Integer;", "binding", "Lcom/empcloud/empmonitor/databinding/ActivityForgotPasswordBinding;", "editTexts", "", "Landroid/widget/EditText;", "email", "", "et1", "et2", "et3", "et4", "listener", "Landroid/text/TextWatcher;", "opt", "resend", "Landroid/widget/LinearLayout;", "resetPasswordbtn", "viewModel", "Lcom/empcloud/empmonitor/ui/activity/forgotpassword/ForgotPasswordViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/activity/forgotpassword/ForgotPasswordViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "forgotOtpRequest", "", "forgotPasswordDataModel", "Lcom/empcloud/empmonitor/data/remote/request/forgotpassword/ForgotPasswordDataModel;", "getOtpFromEditTexts", "", "([Landroid/widget/EditText;)Ljava/lang/String;", "observeOtp", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "setupOtpEditTexts", "([Landroid/widget/EditText;)V", "app_release"})
public final class ForgotPasswordActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.empcloud.empmonitor.databinding.ActivityForgotPasswordBinding binding;
    private android.widget.EditText et1;
    private android.widget.EditText et2;
    private android.widget.EditText et3;
    private android.widget.EditText et4;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<android.widget.EditText> editTexts = null;
    private android.widget.LinearLayout resetPasswordbtn;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String email;
    private android.text.TextWatcher listener;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String opt;
    private android.widget.LinearLayout resend;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer Otp = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    
    public ForgotPasswordActivity() {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.activity.forgotpassword.ForgotPasswordViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    private final void forgotOtpRequest(com.empcloud.empmonitor.data.remote.request.forgotpassword.ForgotPasswordDataModel forgotPasswordDataModel) {
    }
    
    private final void observeOtp() {
    }
    
    private final void setupOtpEditTexts(android.widget.EditText... editTexts) {
    }
    
    private final java.lang.String getOtpFromEditTexts(android.widget.EditText... editTexts) {
        return null;
    }
}