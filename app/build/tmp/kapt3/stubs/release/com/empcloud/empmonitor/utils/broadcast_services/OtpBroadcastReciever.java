package com.empcloud.empmonitor.utils.broadcast_services;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import java.security.MessageDigest;
import java.util.regex.Pattern;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u0011B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0004J\u001c\u0010\f\u001a\u00020\n2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0016R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\u0012"}, d2 = {"Lcom/empcloud/empmonitor/utils/broadcast_services/OtpBroadcastReciever;", "Landroid/content/BroadcastReceiver;", "()V", "otpListener", "Lcom/empcloud/empmonitor/utils/broadcast_services/OtpBroadcastReciever$OTPReceiveListener;", "getOtpListener", "()Lcom/empcloud/empmonitor/utils/broadcast_services/OtpBroadcastReciever$OTPReceiveListener;", "setOtpListener", "(Lcom/empcloud/empmonitor/utils/broadcast_services/OtpBroadcastReciever$OTPReceiveListener;)V", "initListener", "", "listener", "onReceive", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "OTPReceiveListener", "app_release"})
public final class OtpBroadcastReciever extends android.content.BroadcastReceiver {
    @org.jetbrains.annotations.Nullable()
    private com.empcloud.empmonitor.utils.broadcast_services.OtpBroadcastReciever.OTPReceiveListener otpListener;
    
    public OtpBroadcastReciever() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.empcloud.empmonitor.utils.broadcast_services.OtpBroadcastReciever.OTPReceiveListener getOtpListener() {
        return null;
    }
    
    public final void setOtpListener(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.utils.broadcast_services.OtpBroadcastReciever.OTPReceiveListener p0) {
    }
    
    @java.lang.Override()
    public void onReceive(@org.jetbrains.annotations.Nullable()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
    }
    
    public final void initListener(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.utils.broadcast_services.OtpBroadcastReciever.OTPReceiveListener listener) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0003H&\u00a8\u0006\u0007"}, d2 = {"Lcom/empcloud/empmonitor/utils/broadcast_services/OtpBroadcastReciever$OTPReceiveListener;", "", "onOTPReceived", "", "otp", "", "onOTPTimeOut", "app_release"})
    public static abstract interface OTPReceiveListener {
        
        public abstract void onOTPReceived(@org.jetbrains.annotations.NotNull()
        java.lang.String otp);
        
        public abstract void onOTPTimeOut();
    }
}