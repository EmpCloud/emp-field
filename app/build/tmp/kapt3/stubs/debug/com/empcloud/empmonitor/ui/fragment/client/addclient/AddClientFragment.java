package com.empcloud.empmonitor.ui.fragment.client.addclient;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModel;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.databinding.FragmentAddClientBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity;
import com.empcloud.empmonitor.ui.fragment.attendance.AttendanceViewModel;
import com.empcloud.empmonitor.ui.fragment.client.clientaddress.ClientAddressFragment;
import com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeFragment;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.hbb20.CountryCodePicker;
import dagger.hilt.android.AndroidEntryPoint;
import kotlinx.coroutines.Dispatchers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u0000 F2\u00020\u0001:\u0001FB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0002J\b\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020\u001fH\u0002J\b\u0010#\u001a\u00020\u001fH\u0002J\u0010\u0010$\u001a\u00020\u001f2\u0006\u0010%\u001a\u00020\u0016H\u0002J\u0018\u0010&\u001a\u00020\u001f2\u0006\u0010\'\u001a\u00020\u00132\u0006\u0010(\u001a\u00020)H\u0002J\u0010\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\u0013H\u0002J\u000e\u0010-\u001a\u00020+2\u0006\u0010,\u001a\u00020\u0013J\u0010\u0010.\u001a\u00020+2\u0006\u0010/\u001a\u00020\u0013H\u0002J\b\u00100\u001a\u00020\u001fH\u0002J\"\u00101\u001a\u00020\u001f2\u0006\u00102\u001a\u00020\u00042\u0006\u00103\u001a\u00020\u00042\b\u00104\u001a\u0004\u0018\u000105H\u0016J\u0012\u00106\u001a\u00020\u001f2\b\u00107\u001a\u0004\u0018\u000108H\u0016J&\u00109\u001a\u0004\u0018\u00010:2\u0006\u0010;\u001a\u00020<2\b\u0010=\u001a\u0004\u0018\u00010>2\b\u00107\u001a\u0004\u0018\u000108H\u0016J\u001a\u0010?\u001a\u00020\u001f2\u0006\u0010@\u001a\u00020:2\b\u00107\u001a\u0004\u0018\u000108H\u0017J\b\u0010A\u001a\u00020\u001fH\u0002J\b\u0010B\u001a\u00020\u001fH\u0002J\b\u0010C\u001a\u00020\u001fH\u0002J\b\u0010D\u001a\u00020\u001fH\u0003J\b\u0010E\u001a\u00020\u001fH\u0002R\u0010\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0004\n\u0002\b\u0005R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0018\u001a\u00020\u00198BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006G"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/client/addclient/AddClientFragment;", "Landroidx/fragment/app/Fragment;", "()V", "REQUEST_CAMERA_PERMISSION", "", "REQUEST_CAMERA_PERMISSION$1", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentAddClientBinding;", "bitmapReciceve", "Landroid/graphics/Bitmap;", "getBitmapReciceve", "()Landroid/graphics/Bitmap;", "setBitmapReciceve", "(Landroid/graphics/Bitmap;)V", "camerLayout", "Landroid/widget/LinearLayout;", "codePicker", "Lcom/hbb20/CountryCodePicker;", "currentPhotoPath", "", "profilePic", "savedUri", "Landroid/net/Uri;", "sendPic", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/client/addclient/AddClientViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/client/addclient/AddClientViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "camerProcess", "", "createImageFile", "Ljava/io/File;", "dispatchTakePictureIntent", "handleCameraImage", "handleGalleryImage", "imageUri", "invokeProfileImg", "token", "part", "Lokhttp3/MultipartBody$Part;", "isEamilValid", "", "email", "isValidEmail", "isValidPhoneNumber", "phoneNumber", "observeProfileImgUpdate", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onViewCreated", "view", "openGallery", "requestCameraPermission", "saveSetUserData", "showImagePickerOptions", "validateForm", "Companion", "app_debug"})
public final class AddClientFragment extends androidx.fragment.app.Fragment {
    private com.empcloud.empmonitor.databinding.FragmentAddClientBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private final int REQUEST_CAMERA_PERMISSION$1 = 121;
    private java.lang.String currentPhotoPath;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri savedUri;
    @org.jetbrains.annotations.Nullable()
    private android.graphics.Bitmap bitmapReciceve;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String profilePic;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String sendPic;
    private android.widget.LinearLayout camerLayout;
    private com.hbb20.CountryCodePicker codePicker;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_GALLERY_IMAGE = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientFragment.Companion Companion = null;
    
    public AddClientFragment() {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientViewModel getViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.graphics.Bitmap getBitmapReciceve() {
        return null;
    }
    
    public final void setBitmapReciceve(@org.jetbrains.annotations.Nullable()
    android.graphics.Bitmap p0) {
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.R)
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
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
    
    @kotlin.jvm.Throws(exceptionClasses = {java.io.IOException.class})
    private final java.io.File createImageFile() throws java.io.IOException {
        return null;
    }
    
    @java.lang.Override()
    public void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    private final void handleCameraImage() {
    }
    
    private final void handleGalleryImage(android.net.Uri imageUri) {
    }
    
    private final void invokeProfileImg(java.lang.String token, okhttp3.MultipartBody.Part part) {
    }
    
    private final void observeProfileImgUpdate() {
    }
    
    private final void validateForm() {
    }
    
    private final void camerProcess() {
    }
    
    private final boolean isEamilValid(java.lang.String email) {
        return false;
    }
    
    private final boolean isValidPhoneNumber(java.lang.String phoneNumber) {
        return false;
    }
    
    private final void saveSetUserData() {
    }
    
    public final boolean isValidEmail(@org.jetbrains.annotations.NotNull()
    java.lang.String email) {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/client/addclient/AddClientFragment$Companion;", "", "()V", "REQUEST_CAMERA_PERMISSION", "", "REQUEST_GALLERY_IMAGE", "REQUEST_IMAGE_CAPTURE", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}