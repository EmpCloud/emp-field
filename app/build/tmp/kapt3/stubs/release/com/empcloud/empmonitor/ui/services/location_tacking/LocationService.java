package com.empcloud.empmonitor.ui.services.location_tacking;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.local.Location.LocationDao;
import com.empcloud.empmonitor.data.remote.request.send_location.LocationList;
import com.empcloud.empmonitor.data.remote.response.send_location.SendLocationResponse;
import com.empcloud.empmonitor.network.NetworkRepository;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.google.android.gms.location.*;
import dagger.hilt.android.AndroidEntryPoint;
import kotlinx.coroutines.Dispatchers;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.inject.Inject;
import kotlin.math.*;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\b\u0007\u0018\u0000 J2\u00020\u0001:\u0001JB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010)\u001a\u00020*H\u0002J\b\u0010+\u001a\u00020*H\u0002J\b\u0010,\u001a\u00020-H\u0002J\b\u0010.\u001a\u00020*H\u0002J&\u0010/\u001a\u00020\u00042\u0006\u00100\u001a\u00020\r2\u0006\u00101\u001a\u00020\r2\u0006\u00102\u001a\u00020\r2\u0006\u00103\u001a\u00020\rJ\u0010\u00104\u001a\u00020\u000b2\u0006\u00105\u001a\u000206H\u0002J\b\u00107\u001a\u00020*H\u0002J\u0014\u00108\u001a\u0004\u0018\u0001092\b\u0010:\u001a\u0004\u0018\u00010;H\u0016J\b\u0010<\u001a\u00020*H\u0016J\b\u0010=\u001a\u00020*H\u0016J\"\u0010>\u001a\u00020\u00042\b\u0010:\u001a\u0004\u0018\u00010;2\u0006\u0010?\u001a\u00020\u00042\u0006\u0010@\u001a\u00020\u0004H\u0016J\u0018\u0010A\u001a\u00020*2\u0006\u0010B\u001a\u00020\r2\u0006\u0010C\u001a\u00020\rH\u0002J\b\u0010D\u001a\u00020*H\u0002J\b\u0010E\u001a\u00020*H\u0002J\b\u0010F\u001a\u00020*H\u0002J\u0018\u0010G\u001a\u00020*2\u0006\u0010H\u001a\u00020\u00042\u0006\u0010I\u001a\u00020\u0004H\u0002R\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0011\u001a\u00020\u00128\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082.\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0019\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u000eR\u0012\u0010\u001a\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0005R\u001d\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\u001d0\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u001e\u0010!\u001a\u00020\"8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u001a\u0010\'\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\u001d0(X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006K"}, d2 = {"Lcom/empcloud/empmonitor/ui/services/location_tacking/LocationService;", "Landroid/app/Service;", "()V", "distanceFence", "", "Ljava/lang/Integer;", "fusedLocationClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "handler", "Landroid/os/Handler;", "isLastLocationSaved", "", "latitudelastsaved", "", "Ljava/lang/Double;", "locationCallback", "Lcom/google/android/gms/location/LocationCallback;", "locationDao", "Lcom/empcloud/empmonitor/data/local/Location/LocationDao;", "getLocationDao", "()Lcom/empcloud/empmonitor/data/local/Location/LocationDao;", "setLocationDao", "(Lcom/empcloud/empmonitor/data/local/Location/LocationDao;)V", "locationRequest", "Lcom/google/android/gms/location/LocationRequest;", "longitudelastsaved", "numberFreq", "observeSendLocationCall", "Lkotlinx/coroutines/flow/Flow;", "Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState;", "Lcom/empcloud/empmonitor/data/remote/response/send_location/SendLocationResponse;", "getObserveSendLocationCall", "()Lkotlinx/coroutines/flow/Flow;", "repository", "Lcom/empcloud/empmonitor/network/NetworkRepository;", "getRepository", "()Lcom/empcloud/empmonitor/network/NetworkRepository;", "setRepository", "(Lcom/empcloud/empmonitor/network/NetworkRepository;)V", "sendLocationResponse", "Lkotlinx/coroutines/channels/Channel;", "callApiLocation", "", "createNotificationChannel", "getCurrentTimeFormatted", "", "getLastKnownLocation", "getLocationDistanceMeter", "lat1", "lon1", "lat2", "lon2", "isInternetAvailable", "context", "Landroid/content/Context;", "observeSend", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "flags", "startId", "saveLocation", "latitude", "longitude", "startForegroundService", "startLocationUpdates", "stopLocationUpdates", "updateLocationRequest", "currentFrequency", "currentRadius", "Companion", "app_release"})
public final class LocationService extends android.app.Service {
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer numberFreq;
    @javax.inject.Inject()
    public com.empcloud.empmonitor.network.NetworkRepository repository;
    @javax.inject.Inject()
    public com.empcloud.empmonitor.data.local.Location.LocationDao locationDao;
    private boolean isLastLocationSaved = false;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double latitudelastsaved;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double longitudelastsaved;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer distanceFence;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_NETWORK_AVAILABLE = "com.empcloud.empmonitor.ACTION_NETWORK_AVAILABLE";
    private com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient;
    private com.google.android.gms.location.LocationRequest locationRequest;
    private com.google.android.gms.location.LocationCallback locationCallback;
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler handler = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.send_location.SendLocationResponse>> sendLocationResponse = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.send_location.SendLocationResponse>> observeSendLocationCall = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.services.location_tacking.LocationService.Companion Companion = null;
    
    public LocationService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.network.NetworkRepository getRepository() {
        return null;
    }
    
    public final void setRepository(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.network.NetworkRepository p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.local.Location.LocationDao getLocationDao() {
        return null;
    }
    
    public final void setLocationDao(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.local.Location.LocationDao p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.send_location.SendLocationResponse>> getObserveSendLocationCall() {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    private final java.lang.String getCurrentTimeFormatted() {
        return null;
    }
    
    private final void observeSend() {
    }
    
    private final void updateLocationRequest(int currentFrequency, int currentRadius) {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    private final void startForegroundService() {
    }
    
    private final void startLocationUpdates() {
    }
    
    private final void stopLocationUpdates() {
    }
    
    private final void createNotificationChannel() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    private final boolean isInternetAvailable(android.content.Context context) {
        return false;
    }
    
    private final void getLastKnownLocation() {
    }
    
    private final void saveLocation(double latitude, double longitude) {
    }
    
    private final void callApiLocation() {
    }
    
    public final int getLocationDistanceMeter(double lat1, double lon1, double lat2, double lon2) {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/empcloud/empmonitor/ui/services/location_tacking/LocationService$Companion;", "", "()V", "ACTION_NETWORK_AVAILABLE", "", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}