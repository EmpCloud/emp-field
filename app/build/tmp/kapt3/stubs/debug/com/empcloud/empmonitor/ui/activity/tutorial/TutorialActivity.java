package com.empcloud.empmonitor.ui.activity.tutorial;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.airbnb.lottie.LottieAnimationView;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.databinding.ActivityTutorialBinding;
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity;
import com.empcloud.empmonitor.ui.listeners.SlideViewClickListener;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import android.provider.Settings;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0015\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u00012\u00020\u0002:\u0001BB\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0005H\u0002J!\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020(0\r\u00a2\u0006\u0002\u0010)J\b\u0010*\u001a\u00020!H\u0002J\b\u0010+\u001a\u00020!H\u0002J\u0012\u0010,\u001a\u00020!2\b\u0010-\u001a\u0004\u0018\u00010.H\u0014J%\u0010/\u001a\u00020!2\u0006\u00100\u001a\u0002012\u000e\u00102\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\rH\u0016\u00a2\u0006\u0002\u00103J\u0010\u00104\u001a\u00020!2\u0006\u00100\u001a\u000201H\u0016J-\u00105\u001a\u00020!2\u0006\u00106\u001a\u00020\u00052\u000e\u0010\'\u001a\n\u0012\u0006\b\u0001\u0012\u00020(0\r2\u0006\u00107\u001a\u000208H\u0016\u00a2\u0006\u0002\u00109J*\u0010:\u001a\u00020!2\u0006\u0010\"\u001a\u00020\u00052\b\u00100\u001a\u0004\u0018\u0001012\u0006\u0010;\u001a\u00020\u00052\u0006\u0010<\u001a\u00020=H\u0017J\u000e\u0010>\u001a\u00020!2\u0006\u00100\u001a\u000201J)\u0010?\u001a\u00020!2\u0006\u0010%\u001a\u00020=2\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020(0\r2\u0006\u00106\u001a\u00020\u0005\u00a2\u0006\u0002\u0010@J\u000e\u0010A\u001a\u00020!2\u0006\u00100\u001a\u000201R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0018\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\rX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u0018\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\rX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0013R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0016\u001a\u00020\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\u001dX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006C"}, d2 = {"Lcom/empcloud/empmonitor/ui/activity/tutorial/TutorialActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/empcloud/empmonitor/ui/listeners/SlideViewClickListener;", "()V", "BODYSENSOR_REQUEST_CODE", "", "CAMERA_GALLERY_REQUEST_CODE", "FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE", "LOCATIONPERMISSION_REQUEST_CODE", "POST_NOTITIFICATION_REQUEST_CODE", "binding", "Lcom/empcloud/empmonitor/databinding/ActivityTutorialBinding;", "dots", "", "Landroid/widget/TextView;", "[Landroid/widget/TextView;", "layoutDots", "Landroid/widget/LinearLayout;", "layouts", "[Ljava/lang/Integer;", "tutorialPager", "Landroidx/viewpager/widget/ViewPager;", "viewListener", "Landroidx/viewpager/widget/ViewPager$OnPageChangeListener;", "getViewListener", "()Landroidx/viewpager/widget/ViewPager$OnPageChangeListener;", "setViewListener", "(Landroidx/viewpager/widget/ViewPager$OnPageChangeListener;)V", "viewPagerAdapter", "Lcom/empcloud/empmonitor/ui/activity/tutorial/TutorialActivity$ViewPagerAdapter;", "youtubeView", "Landroid/webkit/WebView;", "addBottomDots", "", "position", "areLocPermissionsGranted", "", "context", "Landroid/content/Context;", "permissions", "", "(Landroid/content/Context;[Ljava/lang/String;)Z", "cleanupWebView", "firstRun", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onLayoutInit", "view", "Landroid/view/View;", "layout", "(Landroid/view/View;[Ljava/lang/Integer;)V", "onPlay", "onRequestPermissionsResult", "requestCode", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onSlideViewClicked", "viewId", "activity", "Landroid/app/Activity;", "playYoutube", "requestMultiplePermissions", "(Landroid/app/Activity;[Ljava/lang/String;I)V", "splashAnimation", "ViewPagerAdapter", "app_debug"})
public final class TutorialActivity extends androidx.appcompat.app.AppCompatActivity implements com.empcloud.empmonitor.ui.listeners.SlideViewClickListener {
    private com.empcloud.empmonitor.databinding.ActivityTutorialBinding binding;
    private androidx.viewpager.widget.ViewPager tutorialPager;
    private com.empcloud.empmonitor.ui.activity.tutorial.TutorialActivity.ViewPagerAdapter viewPagerAdapter;
    private android.widget.LinearLayout layoutDots;
    @org.jetbrains.annotations.NotNull()
    private android.widget.TextView[] dots;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Integer[] layouts = null;
    private android.webkit.WebView youtubeView;
    private final int FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE = 101;
    private final int LOCATIONPERMISSION_REQUEST_CODE = 102;
    private final int POST_NOTITIFICATION_REQUEST_CODE = 103;
    private final int BODYSENSOR_REQUEST_CODE = 105;
    private final int CAMERA_GALLERY_REQUEST_CODE = 104;
    @org.jetbrains.annotations.NotNull()
    private androidx.viewpager.widget.ViewPager.OnPageChangeListener viewListener;
    
    public TutorialActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.viewpager.widget.ViewPager.OnPageChangeListener getViewListener() {
        return null;
    }
    
    public final void setViewListener(@org.jetbrains.annotations.NotNull()
    androidx.viewpager.widget.ViewPager.OnPageChangeListener p0) {
    }
    
    private final void addBottomDots(int position) {
    }
    
    public final boolean areLocPermissionsGranted(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions) {
        return false;
    }
    
    @java.lang.Override()
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.R)
    public void onSlideViewClicked(int position, @org.jetbrains.annotations.Nullable()
    android.view.View view, int viewId, @org.jetbrains.annotations.NotNull()
    android.app.Activity activity) {
    }
    
    public final void requestMultiplePermissions(@org.jetbrains.annotations.NotNull()
    android.app.Activity context, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, int requestCode) {
    }
    
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    @java.lang.Override()
    public void onLayoutInit(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.NotNull()
    java.lang.Integer[] layout) {
    }
    
    @java.lang.Override()
    public void onPlay(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    public final void playYoutube(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    public final void splashAnimation(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    private final void firstRun() {
    }
    
    private final void cleanupWebView() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B%\u0012\u000e\u0010\u0002\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ \u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0004H\u0016J\u0018\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0004H\u0016J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u001c\u001a\u00020\u00102\b\u0010\r\u001a\u0004\u0018\u00010\u000eR\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0018\u0010\u0002\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0003X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\fR\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/empcloud/empmonitor/ui/activity/tutorial/TutorialActivity$ViewPagerAdapter;", "Landroidx/viewpager/widget/PagerAdapter;", "layouts", "", "", "context", "Landroid/content/Context;", "activity", "Landroid/app/Activity;", "([Ljava/lang/Integer;Landroid/content/Context;Landroid/app/Activity;)V", "layoutInflater", "Landroid/view/LayoutInflater;", "[Ljava/lang/Integer;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/SlideViewClickListener;", "destroyItem", "", "container", "Landroid/view/ViewGroup;", "position", "object", "", "getCount", "instantiateItem", "isViewFromObject", "", "view", "Landroid/view/View;", "setOnViewClickListener", "app_debug"})
    public static final class ViewPagerAdapter extends androidx.viewpager.widget.PagerAdapter {
        @org.jetbrains.annotations.Nullable()
        private android.view.LayoutInflater layoutInflater;
        @org.jetbrains.annotations.Nullable()
        private com.empcloud.empmonitor.ui.listeners.SlideViewClickListener listener;
        @org.jetbrains.annotations.NotNull()
        private final android.content.Context context = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.Integer[] layouts = null;
        @org.jetbrains.annotations.NotNull()
        private final android.app.Activity activity = null;
        
        public ViewPagerAdapter(@org.jetbrains.annotations.NotNull()
        java.lang.Integer[] layouts, @org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.NotNull()
        android.app.Activity activity) {
            super();
        }
        
        public final void setOnViewClickListener(@org.jetbrains.annotations.Nullable()
        com.empcloud.empmonitor.ui.listeners.SlideViewClickListener listener) {
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.Object instantiateItem(@org.jetbrains.annotations.NotNull()
        android.view.ViewGroup container, int position) {
            return null;
        }
        
        @java.lang.Override()
        public int getCount() {
            return 0;
        }
        
        @java.lang.Override()
        public boolean isViewFromObject(@org.jetbrains.annotations.NotNull()
        android.view.View view, @org.jetbrains.annotations.NotNull()
        java.lang.Object object) {
            return false;
        }
        
        @java.lang.Override()
        public void destroyItem(@org.jetbrains.annotations.NotNull()
        android.view.ViewGroup container, int position, @org.jetbrains.annotations.NotNull()
        java.lang.Object object) {
        }
    }
}