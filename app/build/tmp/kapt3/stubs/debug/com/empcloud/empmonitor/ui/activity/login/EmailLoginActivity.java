package com.empcloud.empmonitor.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.empcloud.empmonitor.data.remote.request.verify_email.VerifyEmailModel;
import com.empcloud.empmonitor.databinding.ActivityEmailLoginBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import dagger.hilt.android.AndroidEntryPoint;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0005\u001a\u00020\u0014H\u0002J\u0010\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0005\u001a\u00020\u0014H\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\u0012\u0010\u0018\u001a\u00020\u00172\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0014J\u0006\u0010\u001b\u001a\u00020\u0017J\b\u0010\u001c\u001a\u00020\u0017H\u0002J\u0006\u0010\u001d\u001a\u00020\u0017R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\f\u001a\u00020\r8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001e"}, d2 = {"Lcom/empcloud/empmonitor/ui/activity/login/EmailLoginActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/empcloud/empmonitor/databinding/ActivityEmailLoginBinding;", "email", "Landroid/widget/EditText;", "listener", "Landroid/text/TextWatcher;", "loginbtnflase", "Landroid/widget/LinearLayout;", "loginbtntrue", "viewModel", "Lcom/empcloud/empmonitor/ui/activity/login/LoginViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/activity/login/LoginViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "isEamilValid", "", "", "isValidEmail", "observeVerifyEmail", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "proceedFurther", "sendtonext", "textObserving", "app_debug"})
public final class EmailLoginActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.empcloud.empmonitor.databinding.ActivityEmailLoginBinding binding;
    private android.widget.EditText email;
    private android.widget.LinearLayout loginbtnflase;
    private android.widget.LinearLayout loginbtntrue;
    private android.text.TextWatcher listener;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    
    public EmailLoginActivity() {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.activity.login.LoginViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    public final void proceedFurther() {
    }
    
    public final void textObserving() {
    }
    
    private final boolean isValidEmail(java.lang.String email) {
        return false;
    }
    
    private final boolean isEamilValid(java.lang.String email) {
        return false;
    }
    
    private final void observeVerifyEmail() {
    }
    
    private final void sendtonext() {
    }
}