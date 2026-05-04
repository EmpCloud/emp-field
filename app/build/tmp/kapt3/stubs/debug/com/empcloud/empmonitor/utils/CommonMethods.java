package com.empcloud.empmonitor.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.Window;
import android.view.WindowInsets;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.request.send_location.LocationList;
import com.empcloud.empmonitor.ui.activity.SplashActivity;
import com.empcloud.empmonitor.utils.broadcast_services.AutoCheckoutReceiver;
import com.empcloud.empmonitor.utils.broadcast_services.StopocationService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kotlinx.coroutines.Dispatchers;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0092\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b)\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0004J\u000e\u0010\t\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0004J \u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000bH\u0002J\u0016\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0006J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u0016\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0006J\u000e\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u0016\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0006J\u000e\u0010\u001b\u001a\u00020\u00062\u0006\u0010\u001c\u001a\u00020\u0006J\u0010\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u001e\u001a\u00020\u0006H\u0007J\u0010\u0010\u001f\u001a\u0004\u0018\u00010\u00062\u0006\u0010 \u001a\u00020\u0006J\"\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\"2\u0006\u0010#\u001a\u00020\u00062\u0006\u0010$\u001a\u00020\u0006J\u0010\u0010%\u001a\u00020\u00062\u0006\u0010&\u001a\u00020\u0006H\u0007J\u000e\u0010\'\u001a\u00020\u00062\u0006\u0010\u001c\u001a\u00020(J\u0018\u0010)\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0006J\u0006\u0010*\u001a\u00020\u0006J\u0006\u0010+\u001a\u00020\u0006J\u0012\u0010,\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\"J\u0006\u0010-\u001a\u00020\u0006J\b\u0010.\u001a\u00020\u0006H\u0007J\u0018\u0010/\u001a\u0004\u0018\u0001002\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u00101\u001a\u000202J\u0018\u00103\u001a\u0004\u0018\u0001002\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u00101\u001a\u000202J\u001a\u00104\u001a\u0004\u0018\u00010\u00062\u0006\u00101\u001a\u0002022\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0016\u00105\u001a\n\u0012\u0004\u0012\u000207\u0018\u0001062\u0006\u0010\u0015\u001a\u00020\u0016J&\u00108\u001a\u00020\u00062\u0006\u00109\u001a\u00020:2\u0006\u0010;\u001a\u00020:2\u0006\u0010<\u001a\u00020:2\u0006\u0010=\u001a\u00020:J&\u0010>\u001a\u00020\u000b2\u0006\u00109\u001a\u00020:2\u0006\u0010;\u001a\u00020:2\u0006\u0010<\u001a\u00020:2\u0006\u0010=\u001a\u00020:J\u0016\u0010?\u001a\u00020\u00062\u0006\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020\u0006J\u0016\u0010C\u001a\u00020D2\u0006\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020\u0006J\u0016\u0010E\u001a\u00020\u000b2\u0006\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020\u0006J\b\u0010F\u001a\u00020\u0006H\u0007J\u0018\u0010G\u001a\u0004\u0018\u0001022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0006J\u0010\u0010H\u001a\u0004\u0018\u00010\u00042\u0006\u0010\b\u001a\u00020\u0004J\u0010\u0010I\u001a\u00020D2\u0006\u0010J\u001a\u00020\u0006H\u0007J\u0010\u0010K\u001a\u00020D2\u0006\u0010L\u001a\u00020\u0006H\u0007J\u0010\u0010M\u001a\u00020D2\u0006\u0010L\u001a\u00020\u0006H\u0007J\u0010\u0010N\u001a\u00020D2\u0006\u0010L\u001a\u00020\u0006H\u0007J\"\u0010O\u001a\u00020D2\f\u0010P\u001a\b\u0012\u0004\u0012\u00020\u000b062\f\u0010Q\u001a\b\u0012\u0004\u0012\u00020\u000b06J\u0010\u0010R\u001a\u00020D2\u0006\u0010P\u001a\u00020\u0006H\u0007J\u000e\u0010S\u001a\u00020D2\u0006\u0010T\u001a\u00020\u0006J\u000e\u0010U\u001a\u00020D2\u0006\u0010T\u001a\u00020\u0006J\u000e\u0010V\u001a\u00020D2\u0006\u0010W\u001a\u00020\u0006J\"\u0010X\u001a\u0004\u0018\u00010\u00042\b\u00101\u001a\u0004\u0018\u0001022\u0006\u0010\u0015\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010YJ\u000e\u0010Z\u001a\u00020\u00062\u0006\u0010[\u001a\u00020\u0006J\u000e\u0010\\\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u0018\u0010]\u001a\u0004\u0018\u00010\u00042\u0006\u0010^\u001a\u00020\u00042\u0006\u0010_\u001a\u00020\u000bJ\u0010\u0010`\u001a\u0004\u0018\u00010\u00042\u0006\u0010\b\u001a\u00020\u0004J\u001e\u0010a\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0006J\u0016\u0010b\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010c\u001a\u000207J&\u0010d\u001a\u00020\u00142\u0006\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020\u00062\u0006\u0010e\u001a\u00020\u00062\u0006\u0010f\u001a\u00020\u0006J&\u0010g\u001a\u00020\u00142\u0006\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020\u00062\u0006\u0010e\u001a\u00020\u00062\u0006\u0010f\u001a\u00020DJ&\u0010h\u001a\u00020\u00142\u0006\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020\u00062\u0006\u0010e\u001a\u00020\u00062\u0006\u0010f\u001a\u00020\u000bJ\u001e\u0010i\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u00062\u0006\u00101\u001a\u000202J\u000e\u0010j\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010k\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J&\u0010l\u001a\u00020\u00142\u0006\u0010m\u001a\u00020n2\u0006\u0010o\u001a\u00020\u00162\u0006\u0010p\u001a\u00020q2\u0006\u0010@\u001a\u00020rJ\u000e\u0010s\u001a\u00020\u00042\u0006\u0010t\u001a\u00020\u0006J\u0016\u0010u\u001a\u00020\u00142\u0006\u0010@\u001a\u00020A2\u0006\u0010v\u001a\u00020w\u00a8\u0006x"}, d2 = {"Lcom/empcloud/empmonitor/utils/CommonMethods;", "", "()V", "base64ToBitmap", "Landroid/graphics/Bitmap;", "base64String", "", "bitmapToBase64", "bitmap", "bitmapToString", "calculateInSampleSize", "", "options", "Landroid/graphics/BitmapFactory$Options;", "reqWidth", "reqHeight", "calculateTimeWorked", "loginTime", "logoutTime", "cancelAutoCheckout", "", "context", "Landroid/content/Context;", "clearAllValues", "key", "clearLocationDataList", "clearStringFromSharedPreferences", "combineDateAndTime", "time", "convertTimeToGmt", "inputTimeString", "extractFileNameFromUrl", "url", "extractMonthAndDay", "Lkotlin/Pair;", "originalDateString", "originalFormat", "formatApiTime", "apiTime", "formatTime", "Ljava/util/Date;", "getBitmapFromSharedPreferences", "getCurrentDate", "getCurrentDateTime", "getCurrentMonthAndDate", "getCurrentTime", "getEndDateOfCurrentMonth", "getFileFromUri", "Ljava/io/File;", "uri", "Landroid/net/Uri;", "getFileFromUriGallery", "getFileName", "getLocationDataList", "", "Lcom/empcloud/empmonitor/data/remote/request/send_location/LocationList;", "getLocationDistance", "lat1", "", "lon1", "lat2", "lon2", "getLocationDistanceMeter", "getSharedPrefernce", "activity", "Landroidx/fragment/app/FragmentActivity;", "prefName", "getSharedPrefernceBoolean", "", "getSharedPrefernceInteger", "getStartDateOfCurrentMonth", "getUriFromSharedPref", "handleSamplingAndRotationBitmap", "isCurrentDate", "dateTime", "isDateBeforeCurrent", "dateStr", "isDateBeforeCurrentWithTime", "isDateBeforeOrOnCurrent", "isEndTimeValid", "startTime", "endTime", "isNotFutureDate", "isValidDateFormat", "dateString", "isValidDateFormatChange", "isValidTimeFormat", "timeString", "loadBitmapFromUri", "(Landroid/net/Uri;Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "parseDate", "date", "restorePendingAutoCheckout", "rotateImage", "img", "degree", "rotateImageIfRequired", "saveBitmapToSharedPreferences", "saveLocationDataList", "newLocationData", "saveSharedPrefernce", "dataPref", "dataValue", "saveSharedPrefernceBoolean", "saveSharedPrefernceInteger", "saveUriToSharedPref", "scheduleAutoCheckout", "scheduleServiceStop", "setStatusBarColor", "window", "Landroid/view/Window;", "applicationContext", "root", "Landroidx/constraintlayout/widget/ConstraintLayout;", "Landroid/app/Activity;", "stringToBitmap", "encodedString", "switchFragment", "fragment", "Landroidx/fragment/app/Fragment;", "app_debug"})
public final class CommonMethods {
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.utils.CommonMethods INSTANCE = null;
    
    private CommonMethods() {
        super();
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatApiTime(@org.jetbrains.annotations.NotNull()
    java.lang.String apiTime) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentDateTime() {
        return null;
    }
    
    public final void switchFragment(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentActivity activity, @org.jetbrains.annotations.NotNull()
    androidx.fragment.app.Fragment fragment) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSharedPrefernce(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentActivity activity, @org.jetbrains.annotations.NotNull()
    java.lang.String prefName) {
        return null;
    }
    
    public final boolean getSharedPrefernceBoolean(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentActivity activity, @org.jetbrains.annotations.NotNull()
    java.lang.String prefName) {
        return false;
    }
    
    public final int getSharedPrefernceInteger(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentActivity activity, @org.jetbrains.annotations.NotNull()
    java.lang.String prefName) {
        return 0;
    }
    
    public final void saveSharedPrefernce(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentActivity activity, @org.jetbrains.annotations.NotNull()
    java.lang.String prefName, @org.jetbrains.annotations.NotNull()
    java.lang.String dataPref, @org.jetbrains.annotations.NotNull()
    java.lang.String dataValue) {
    }
    
    public final void saveSharedPrefernceBoolean(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentActivity activity, @org.jetbrains.annotations.NotNull()
    java.lang.String prefName, @org.jetbrains.annotations.NotNull()
    java.lang.String dataPref, boolean dataValue) {
    }
    
    public final void saveSharedPrefernceInteger(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.FragmentActivity activity, @org.jetbrains.annotations.NotNull()
    java.lang.String prefName, @org.jetbrains.annotations.NotNull()
    java.lang.String dataPref, int dataValue) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatTime(@org.jetbrains.annotations.NotNull()
    java.util.Date time) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLocationDistance(double lat1, double lon1, double lat2, double lon2) {
        return null;
    }
    
    public final int getLocationDistanceMeter(double lat1, double lon1, double lat2, double lon2) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String combineDateAndTime(@org.jetbrains.annotations.NotNull()
    java.lang.String time) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentDate() {
        return null;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getStartDateOfCurrentMonth() {
        return null;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEndDateOfCurrentMonth() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.io.File getFileFromUriGallery(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.io.File getFileFromUri(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.graphics.Bitmap base64ToBitmap(@org.jetbrains.annotations.NotNull()
    java.lang.String base64String) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.graphics.Bitmap getBitmapFromSharedPreferences(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String key) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String bitmapToBase64(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap) {
        return null;
    }
    
    public final void saveBitmapToSharedPreferences(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap, @org.jetbrains.annotations.NotNull()
    java.lang.String key) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String bitmapToString(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.graphics.Bitmap stringToBitmap(@org.jetbrains.annotations.NotNull()
    java.lang.String encodedString) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.Pair<java.lang.String, java.lang.String> getCurrentMonthAndDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.Pair<java.lang.String, java.lang.String> extractMonthAndDay(@org.jetbrains.annotations.NotNull()
    java.lang.String originalDateString, @org.jetbrains.annotations.NotNull()
    java.lang.String originalFormat) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String calculateTimeWorked(@org.jetbrains.annotations.NotNull()
    java.lang.String loginTime, @org.jetbrains.annotations.NotNull()
    java.lang.String logoutTime) {
        return null;
    }
    
    public final void clearStringFromSharedPreferences(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String key) {
    }
    
    public final boolean isValidDateFormat(@org.jetbrains.annotations.NotNull()
    java.lang.String dateString) {
        return false;
    }
    
    public final boolean isValidDateFormatChange(@org.jetbrains.annotations.NotNull()
    java.lang.String dateString) {
        return false;
    }
    
    public final boolean isValidTimeFormat(@org.jetbrains.annotations.NotNull()
    java.lang.String timeString) {
        return false;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public final boolean isDateBeforeCurrent(@org.jetbrains.annotations.NotNull()
    java.lang.String dateStr) {
        return false;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public final boolean isDateBeforeCurrentWithTime(@org.jetbrains.annotations.NotNull()
    java.lang.String dateStr) {
        return false;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public final boolean isCurrentDate(@org.jetbrains.annotations.NotNull()
    java.lang.String dateTime) {
        return false;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public final boolean isDateBeforeOrOnCurrent(@org.jetbrains.annotations.NotNull()
    java.lang.String dateStr) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String parseDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    public final boolean isEndTimeValid(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Integer> startTime, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Integer> endTime) {
        return false;
    }
    
    public final void saveLocationDataList(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.send_location.LocationList newLocationData) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.empcloud.empmonitor.data.remote.request.send_location.LocationList> getLocationDataList(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    public final void clearLocationDataList(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final void clearAllValues(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String key) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.graphics.Bitmap handleSamplingAndRotationBitmap(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.graphics.Bitmap rotateImageIfRequired(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap bitmap) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.graphics.Bitmap rotateImage(@org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap img, int degree) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object loadBitmapFromUri(@org.jetbrains.annotations.Nullable()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super android.graphics.Bitmap> $completion) {
        return null;
    }
    
    private final int calculateInSampleSize(android.graphics.BitmapFactory.Options options, int reqWidth, int reqHeight) {
        return 0;
    }
    
    public final void scheduleServiceStop(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final void scheduleAutoCheckout(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final void cancelAutoCheckout(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final void restorePendingAutoCheckout(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final void saveUriToSharedPref(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String key, @org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.net.Uri getUriFromSharedPref(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String key) {
        return null;
    }
    
    private final java.lang.String getFileName(android.net.Uri uri, android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String extractFileNameFromUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String url) {
        return null;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String convertTimeToGmt(@org.jetbrains.annotations.NotNull()
    java.lang.String inputTimeString) {
        return null;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public final boolean isNotFutureDate(@org.jetbrains.annotations.NotNull()
    java.lang.String startTime) {
        return false;
    }
    
    public final void setStatusBarColor(@org.jetbrains.annotations.NotNull()
    android.view.Window window, @org.jetbrains.annotations.NotNull()
    android.content.Context applicationContext, @org.jetbrains.annotations.NotNull()
    androidx.constraintlayout.widget.ConstraintLayout root, @org.jetbrains.annotations.NotNull()
    android.app.Activity activity) {
    }
}