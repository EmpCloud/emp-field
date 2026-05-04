package com.empcloud.empmonitor.ui.activity.mainactivity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.databinding.ActivityMain1Binding;
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity;
import com.empcloud.empmonitor.ui.fragment.settings.SettingsFragment;
import com.empcloud.empmonitor.ui.fragment.attendance.AttendanceFragment;
import com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeFragment;
import com.empcloud.empmonitor.ui.fragment.holidays.HolidaysFragment;
import com.empcloud.empmonitor.ui.fragment.home.HomeFragment;
import com.empcloud.empmonitor.ui.fragment.leaves.LeavesFragment;
import com.empcloud.empmonitor.ui.fragment.map.MapCurrentFragment;
import com.empcloud.empmonitor.ui.fragment.notification.NotificationFragment;
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment;
import com.empcloud.empmonitor.ui.fragment.update_profile.UpdateProfileFragment;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.empcloud.empmonitor.utils.NativeLib;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;
import dagger.hilt.android.AndroidEntryPoint;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u001b\u001a\u00020\u001cH\u0002J\b\u0010\u001d\u001a\u00020\u001cH\u0002J\b\u0010\u001e\u001a\u00020\u001cH\u0002J\b\u0010\u001f\u001a\u00020\u001cH\u0002J\b\u0010 \u001a\u00020\u001cH\u0002J\b\u0010!\u001a\u00020\u001cH\u0002J\b\u0010\"\u001a\u00020\u001cH\u0002J\u0018\u0010#\u001a\u00020\u001c2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u000eH\u0003J\b\u0010\'\u001a\u00020\u001cH\u0002J\u0010\u0010(\u001a\u00020\u001c2\u0006\u0010)\u001a\u00020\u000eH\u0002J\u0010\u0010*\u001a\u00020+2\u0006\u0010$\u001a\u00020%H\u0002J\u0012\u0010,\u001a\u0004\u0018\u00010\u000e2\u0006\u0010-\u001a\u00020\u000eH\u0002J\u0010\u0010.\u001a\u00020\u001c2\u0006\u0010/\u001a\u00020\u000eH\u0002J\u0018\u00100\u001a\u00020\u001c2\u0006\u00101\u001a\u0002022\u0006\u00103\u001a\u000204H\u0003J\b\u00105\u001a\u00020\u001cH\u0002J\b\u00106\u001a\u00020\u0016H\u0002J\b\u00107\u001a\u00020\u001cH\u0002J\b\u00108\u001a\u00020\u001cH\u0002J\b\u00109\u001a\u00020\u001cH\u0016J\u0012\u0010:\u001a\u00020\u001c2\b\u0010;\u001a\u0004\u0018\u00010<H\u0015J\b\u0010=\u001a\u00020\u001cH\u0014J\b\u0010>\u001a\u00020\u001cH\u0014J\"\u0010?\u001a\u00020\u001c2\u0006\u0010@\u001a\u00020\u00052\u0006\u0010A\u001a\u00020\u00052\b\u0010B\u001a\u0004\u0018\u00010\u0002H\u0016J\b\u0010C\u001a\u00020\u001cH\u0002J\b\u0010D\u001a\u00020\u001cH\u0002J\b\u0010E\u001a\u00020\u001cH\u0003J\u000e\u0010F\u001a\u00020\u001c2\u0006\u0010G\u001a\u00020\u0016J\u000e\u0010H\u001a\u00020\u001c2\u0006\u0010G\u001a\u00020\u0016J\u0006\u0010I\u001a\u00020\u001cJ\u0006\u0010J\u001a\u00020\u001cJ\b\u0010K\u001a\u00020\u001cH\u0002J\b\u0010L\u001a\u00020\u001cH\u0002J\u0010\u0010M\u001a\u00020\u001c2\u0006\u0010N\u001a\u00020OH\u0002J\u0010\u0010P\u001a\u00020\u001c2\u0006\u0010Q\u001a\u00020\u0010H\u0002J\b\u0010R\u001a\u00020\u001cH\u0002J\b\u0010S\u001a\u00020\u001cH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006T"}, d2 = {"Lcom/empcloud/empmonitor/ui/activity/mainactivity/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "()V", "PERMISSION_REQUEST_CODE", "", "PERMISSION_REQUEST_CODE_NOTIFICATION", "batteryCheckingRunnable", "Ljava/lang/Runnable;", "binding", "Lcom/empcloud/empmonitor/databinding/ActivityMain1Binding;", "bottomNavigationView", "Lcom/google/android/material/bottomnavigation/BottomNavigationView;", "channelId", "", "currentFragment", "Landroidx/fragment/app/Fragment;", "fragmentContainer", "Landroidx/fragment/app/FragmentContainerView;", "handler", "Landroid/os/Handler;", "isGlobal", "", "isMenuOpen", "locationCheckingRunnable", "sideMenu", "Landroidx/constraintlayout/widget/ConstraintLayout;", "batteryChecking", "", "batteryCheckingStatus", "chagneApiKey", "checkPermissions", "clientSelection", "closeMenu", "createNotificationChannel", "createPdf", "view", "Landroid/view/View;", "fileName", "customizeNavigationBottom", "downloadImageAndSaveAsPdf", "imageUrl", "getBitMapFromView", "Landroid/graphics/Bitmap;", "getMetaDataValue", "name", "getQr", "empid", "highlightMenuItem", "selectedIcon", "Landroid/widget/ImageView;", "selectedText", "Landroid/widget/TextView;", "homeSelection", "isLocationEnabled", "locationChecking", "mapSelection", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onResume", "onSpinnerItemSelection", "position", "i", "listener", "openMenu", "qrSelection", "resetMenuItemColors", "setMenuButtonVisibility", "visible", "setQrVisibility", "setUserData", "setUserDataQr", "setUserPic", "settingSelection", "showDownloadNotification", "fileUri", "Landroid/net/Uri;", "switchFragment", "fragment", "taskSelection", "toggleMenu", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity implements com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener {
    private com.empcloud.empmonitor.databinding.ActivityMain1Binding binding;
    private androidx.fragment.app.FragmentContainerView fragmentContainer;
    private com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView;
    @org.jetbrains.annotations.Nullable()
    private androidx.fragment.app.Fragment currentFragment;
    private final int PERMISSION_REQUEST_CODE = 1001;
    private final int PERMISSION_REQUEST_CODE_NOTIFICATION = 1002;
    private androidx.constraintlayout.widget.ConstraintLayout sideMenu;
    private boolean isMenuOpen = false;
    private android.os.Handler handler;
    private java.lang.Runnable locationCheckingRunnable;
    private java.lang.Runnable batteryCheckingRunnable;
    private boolean isGlobal = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String channelId = "download_channel";
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override()
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.Q)
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void toggleMenu() {
    }
    
    private final void switchFragment(androidx.fragment.app.Fragment fragment) {
    }
    
    private final void customizeNavigationBottom() {
    }
    
    private final void settingSelection() {
    }
    
    private final void taskSelection() {
    }
    
    private final void clientSelection() {
    }
    
    private final void homeSelection() {
    }
    
    private final void mapSelection() {
    }
    
    private final void qrSelection() {
    }
    
    @java.lang.Override()
    public void onSpinnerItemSelection(int position, int i, @org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    private final void openMenu() {
    }
    
    private final void closeMenu() {
    }
    
    @android.annotation.SuppressLint(value = {"ResourceAsColor"})
    private final void highlightMenuItem(android.widget.ImageView selectedIcon, android.widget.TextView selectedText) {
    }
    
    @android.annotation.SuppressLint(value = {"ResourceAsColor"})
    private final void resetMenuItemColors() {
    }
    
    public final void setMenuButtonVisibility(boolean visible) {
    }
    
    public final void setQrVisibility(boolean visible) {
    }
    
    private final void setUserPic() {
    }
    
    public final void setUserData() {
    }
    
    private final boolean isLocationEnabled() {
        return false;
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    private final void locationChecking() {
    }
    
    private final void batteryChecking() {
    }
    
    private final void batteryCheckingStatus() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    public final void setUserDataQr() {
    }
    
    private final void getQr(java.lang.String empid) {
    }
    
    private final void downloadImageAndSaveAsPdf(java.lang.String imageUrl) {
    }
    
    private final void createNotificationChannel() {
    }
    
    private final void showDownloadNotification(android.net.Uri fileUri) {
    }
    
    private final android.graphics.Bitmap getBitMapFromView(android.view.View view) {
        return null;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.Q)
    private final void createPdf(android.view.View view, java.lang.String fileName) {
    }
    
    private final void checkPermissions() {
    }
    
    private final void chagneApiKey() {
    }
    
    private final java.lang.String getMetaDataValue(java.lang.String name) {
        return null;
    }
}