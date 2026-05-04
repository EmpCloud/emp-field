package com.empcloud.empmonitor.ui.activity.createprofile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse;
import com.empcloud.empmonitor.data.remote.response.fetchprofile.UserDataFetch;
import com.empcloud.empmonitor.databinding.ActivityCreateProfileBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity;
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity;
import com.empcloud.empmonitor.ui.activity.mapaddress.MapAddressActivity;
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientFragment;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.squareup.picasso.Picasso;
import dagger.hilt.android.AndroidEntryPoint;
import kotlinx.coroutines.Dispatchers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 N2\u00020\u0001:\u0001NB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0002J\b\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020\u001fH\u0002J\u0010\u0010#\u001a\u00020\u001f2\u0006\u0010$\u001a\u00020\u000eH\u0002J\b\u0010%\u001a\u00020\u001fH\u0002J\b\u0010&\u001a\u00020\u001fH\u0002J\u0010\u0010\'\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020\u0014H\u0002J\u0012\u0010)\u001a\u0004\u0018\u00010\n2\u0006\u0010*\u001a\u00020\nH\u0002J\u0018\u0010+\u001a\u00020\u001f2\u0006\u0010$\u001a\u00020\u000e2\u0006\u0010,\u001a\u00020\u0011H\u0002J\u0010\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020\u0004H\u0002J\b\u00100\u001a\u00020\u001fH\u0002J\b\u00101\u001a\u00020\u001fH\u0002J\"\u00102\u001a\u00020\u001f2\u0006\u00103\u001a\u00020\u00042\u0006\u00104\u001a\u00020\u00042\b\u00105\u001a\u0004\u0018\u000106H\u0014J\b\u00107\u001a\u00020\u001fH\u0016J\u0012\u00108\u001a\u00020\u001f2\b\u00109\u001a\u0004\u0018\u00010:H\u0015J\b\u0010;\u001a\u00020\u001fH\u0014J-\u0010<\u001a\u00020\u001f2\u0006\u00103\u001a\u00020\u00042\u000e\u0010=\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u000e0>2\u0006\u0010?\u001a\u00020@H\u0016\u00a2\u0006\u0002\u0010AJ\b\u0010B\u001a\u00020\u001fH\u0002J\b\u0010C\u001a\u00020\u001fH\u0002J\b\u0010D\u001a\u00020\u001fH\u0002J\u001a\u0010E\u001a\u0004\u0018\u00010\n2\u0006\u0010F\u001a\u00020\n2\u0006\u0010G\u001a\u00020\u0004H\u0002J\u0012\u0010H\u001a\u0004\u0018\u00010\n2\u0006\u0010*\u001a\u00020\nH\u0002J\u0010\u0010I\u001a\u00020\u001f2\u0006\u0010J\u001a\u00020KH\u0002J\b\u0010L\u001a\u00020\u001fH\u0003J\b\u0010M\u001a\u00020\u001fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0004\n\u0002\b\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0018\u001a\u00020\u00198BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006O"}, d2 = {"Lcom/empcloud/empmonitor/ui/activity/createprofile/CreateProfileActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "CAMERA_REQUEST_CODE", "", "REQUEST_CAMERA_PERMISSION", "REQUEST_CAMERA_PERMISSION$1", "binding", "Lcom/empcloud/empmonitor/databinding/ActivityCreateProfileBinding;", "bitmapRecieve", "Landroid/graphics/Bitmap;", "camerLayout", "Landroid/widget/LinearLayout;", "currentPhotoPath", "", "gender", "imagePart", "Lokhttp3/MultipartBody$Part;", "profileLink", "savedUri", "Landroid/net/Uri;", "userdata", "", "Lcom/empcloud/empmonitor/data/remote/response/fetchprofile/UserDataFetch;", "viewModel", "Lcom/empcloud/empmonitor/ui/activity/createprofile/CreateProfileViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/activity/createprofile/CreateProfileViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "apiCall", "", "createImageFile", "Ljava/io/File;", "dispatchTakePictureIntent", "fetchProfile", "authToken", "furtherProcess", "handleCameraImage", "handleGalleryImage", "imageUri", "handleSamplingAndRotationBitmap", "bitmap", "invokeProfileImg", "files", "isValidAge", "", "age", "observeLoginData", "observeProfileImgUpdate", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onRequestPermissionsResult", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "openGallery", "openMain", "requestCameraPermission", "rotateImage", "img", "degree", "rotateImageIfRequired", "saveUserData", "it", "Lcom/empcloud/empmonitor/data/remote/response/fetchprofile/FetchProfileResponse;", "showImagePickerOptions", "validateForm", "Companion", "app_release"})
public final class CreateProfileActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.Nullable()
    private android.graphics.Bitmap bitmapRecieve;
    private com.empcloud.empmonitor.databinding.ActivityCreateProfileBinding binding;
    private final int REQUEST_CAMERA_PERMISSION$1 = 121;
    private final int CAMERA_REQUEST_CODE = 131;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private android.widget.LinearLayout camerLayout;
    @org.jetbrains.annotations.Nullable()
    private okhttp3.MultipartBody.Part imagePart;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri savedUri;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String profileLink;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String gender;
    private java.util.List<com.empcloud.empmonitor.data.remote.response.fetchprofile.UserDataFetch> userdata;
    private java.lang.String currentPhotoPath;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity.Companion Companion = null;
    
    public CreateProfileActivity() {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void apiCall() {
    }
    
    private final void fetchProfile(java.lang.String authToken) {
    }
    
    private final void observeLoginData() {
    }
    
    private final void saveUserData(com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse it) {
    }
    
    @kotlin.jvm.Throws(exceptionClasses = {java.io.IOException.class})
    private final java.io.File createImageFile() throws java.io.IOException {
        return null;
    }
    
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    private final void validateForm() {
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    private final void invokeProfileImg(java.lang.String authToken, okhttp3.MultipartBody.Part files) {
    }
    
    private final void observeProfileImgUpdate() {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.R)
    private final void showImagePickerOptions() {
    }
    
    private final void requestCameraPermission() {
    }
    
    private final void openGallery() {
    }
    
    private final void dispatchTakePictureIntent() {
    }
    
    @java.lang.Override()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    private final void handleGalleryImage(android.net.Uri imageUri) {
    }
    
    private final void handleCameraImage() {
    }
    
    private final void furtherProcess() {
    }
    
    private final void openMain() {
    }
    
    private final boolean isValidAge(int age) {
        return false;
    }
    
    private final android.graphics.Bitmap handleSamplingAndRotationBitmap(android.graphics.Bitmap bitmap) {
        return null;
    }
    
    @kotlin.jvm.Throws(exceptionClasses = {java.io.IOException.class})
    private final android.graphics.Bitmap rotateImageIfRequired(android.graphics.Bitmap bitmap) throws java.io.IOException {
        return null;
    }
    
    private final android.graphics.Bitmap rotateImage(android.graphics.Bitmap img, int degree) {
        return null;
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/empcloud/empmonitor/ui/activity/createprofile/CreateProfileActivity$Companion;", "", "()V", "REQUEST_CAMERA_PERMISSION", "", "REQUEST_IMAGE_CAPTURE", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}