package com.empcloud.empmonitor.utils.broadcast_services;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u000fB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\u0010"}, d2 = {"Lcom/empcloud/empmonitor/utils/broadcast_services/SmsBroadcastReceiver;", "Landroid/content/BroadcastReceiver;", "()V", "listener", "Lcom/empcloud/empmonitor/utils/broadcast_services/SmsBroadcastReceiver$SmsBroadcastListener;", "getListener", "()Lcom/empcloud/empmonitor/utils/broadcast_services/SmsBroadcastReceiver$SmsBroadcastListener;", "setListener", "(Lcom/empcloud/empmonitor/utils/broadcast_services/SmsBroadcastReceiver$SmsBroadcastListener;)V", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "SmsBroadcastListener", "app_release"})
public final class SmsBroadcastReceiver extends android.content.BroadcastReceiver {
    @org.jetbrains.annotations.Nullable()
    private com.empcloud.empmonitor.utils.broadcast_services.SmsBroadcastReceiver.SmsBroadcastListener listener;
    
    public SmsBroadcastReceiver() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.empcloud.empmonitor.utils.broadcast_services.SmsBroadcastReceiver.SmsBroadcastListener getListener() {
        return null;
    }
    
    public final void setListener(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.utils.broadcast_services.SmsBroadcastReceiver.SmsBroadcastListener p0) {
    }
    
    @java.lang.Override()
    public void onReceive(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.content.Intent intent) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0003H&\u00a8\u0006\u0007"}, d2 = {"Lcom/empcloud/empmonitor/utils/broadcast_services/SmsBroadcastReceiver$SmsBroadcastListener;", "", "onSmsRetrieved", "", "consentIntent", "Landroid/content/Intent;", "onTimeout", "app_release"})
    public static abstract interface SmsBroadcastListener {
        
        public abstract void onSmsRetrieved(@org.jetbrains.annotations.NotNull()
        android.content.Intent consentIntent);
        
        public abstract void onTimeout();
    }
}