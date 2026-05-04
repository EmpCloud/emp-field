package com.empcloud.empmonitor.ui.fragment.update_profile;

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
import android.util.Log;
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
import androidx.fragment.app.Fragment;
import com.empcloud.empmonitor.data.remote.request.updateprofile.UpdateProfileModel;
import com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse;
import com.empcloud.empmonitor.data.remote.response.fetchprofile.UserDataFetch;
import com.empcloud.empmonitor.databinding.FragmentUpdateProfileBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity;
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientFragment;
import com.empcloud.empmonitor.ui.fragment.update_address.UpdateAddressFragment;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
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
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00a8\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 Z2\u00020\u0001:\u0001ZB\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J \u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001b2\u0006\u0010\u001f\u001a\u00020\u001bH\u0002J\b\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020#H\u0002J\b\u0010$\u001a\u00020!H\u0002J\b\u0010%\u001a\u00020!H\u0002J\u0010\u0010&\u001a\u00020!2\u0006\u0010\'\u001a\u00020\nH\u0002J\b\u0010(\u001a\u00020!H\u0002J\b\u0010)\u001a\u00020!H\u0002J\u0010\u0010*\u001a\u00020!2\u0006\u0010+\u001a\u00020\u0010H\u0002J\u0018\u0010,\u001a\u00020!2\u0006\u0010\'\u001a\u00020\n2\u0006\u0010-\u001a\u00020.H\u0002J\b\u0010/\u001a\u000200H\u0002J\u0010\u00101\u001a\u0002002\u0006\u00102\u001a\u00020\u001bH\u0002J\u001a\u00103\u001a\u0004\u0018\u0001042\b\u00105\u001a\u0004\u0018\u00010\u0010H\u0082@\u00a2\u0006\u0002\u00106J\b\u00107\u001a\u00020!H\u0002J\b\u00108\u001a\u00020!H\u0002J\b\u00109\u001a\u00020!H\u0002J\b\u0010:\u001a\u00020!H\u0002J\"\u0010;\u001a\u00020!2\u0006\u0010<\u001a\u00020\u001b2\u0006\u0010=\u001a\u00020\u001b2\b\u0010>\u001a\u0004\u0018\u00010?H\u0016J\u0012\u0010@\u001a\u00020!2\b\u0010A\u001a\u0004\u0018\u00010BH\u0017J&\u0010C\u001a\u0004\u0018\u00010D2\u0006\u0010E\u001a\u00020F2\b\u0010G\u001a\u0004\u0018\u00010H2\b\u0010A\u001a\u0004\u0018\u00010BH\u0016J-\u0010I\u001a\u00020!2\u0006\u0010<\u001a\u00020\u001b2\u000e\u0010J\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0K2\u0006\u0010L\u001a\u00020MH\u0017\u00a2\u0006\u0002\u0010NJ\b\u0010O\u001a\u00020!H\u0016J\u001a\u0010P\u001a\u00020!2\u0006\u0010Q\u001a\u00020D2\b\u0010A\u001a\u0004\u0018\u00010BH\u0017J\b\u0010R\u001a\u00020!H\u0002J\b\u0010S\u001a\u00020!H\u0002J\b\u0010T\u001a\u00020!H\u0002J\b\u0010U\u001a\u00020!H\u0002J\u0010\u0010V\u001a\u00020!2\u0006\u0010W\u001a\u00020XH\u0002J\b\u0010Y\u001a\u00020!H\u0003R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0014\u001a\u00020\u00158BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0018\u0010\u0019\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006["}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/update_profile/UpdateProfileFragment;", "Landroidx/fragment/app/Fragment;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "(Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;)V", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentUpdateProfileBinding;", "camerLayout", "Landroid/widget/LinearLayout;", "currentPhotoPath", "", "gender", "latitude", "lonitude", "profileLink", "savedUri", "Landroid/net/Uri;", "userdata", "", "Lcom/empcloud/empmonitor/data/remote/response/fetchprofile/UserDataFetch;", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/update_profile/UpdateProfileViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/update_profile/UpdateProfileViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "calculateInSampleSize", "", "options", "Landroid/graphics/BitmapFactory$Options;", "reqWidth", "reqHeight", "callUpdateApi", "", "createImageFile", "Ljava/io/File;", "dispatchTakePictureIntent", "femaleSelection", "fetchProfile", "authToken", "furtherProcess", "handleCameraImage", "handleGalleryImage", "imageUri", "invokeProfileImg", "files", "Lokhttp3/MultipartBody$Part;", "isCameraAppAvailable", "", "isValidAge", "age", "loadBitmapFromUri", "Landroid/graphics/Bitmap;", "uri", "(Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "maleSelection", "observeProfileData", "observeProfileImgUpdate", "observeUpdateProfile", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onRequestPermissionsResult", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onResume", "onViewCreated", "view", "openGallery", "otherSelection", "requestCameraPermission", "setUserPic", "showData", "fetchProfileResponse", "Lcom/empcloud/empmonitor/data/remote/response/fetchprofile/FetchProfileResponse;", "showImagePickerOptions", "Companion", "app_debug"})
public final class UpdateProfileFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener = null;
    private com.empcloud.empmonitor.databinding.FragmentUpdateProfileBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private java.util.List<com.empcloud.empmonitor.data.remote.response.fetchprofile.UserDataFetch> userdata;
    private java.lang.String currentPhotoPath;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String gender;
    private android.widget.LinearLayout camerLayout;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri savedUri;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String profileLink;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String latitude;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String lonitude;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.fragment.update_profile.UpdateProfileFragment.Companion Companion = null;
    
    public UpdateProfileFragment(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.update_profile.UpdateProfileViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.R)
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
    
    private final void fetchProfile(java.lang.String authToken) {
    }
    
    private final void observeProfileData() {
    }
    
    private final void showData(com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse fetchProfileResponse) {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.R)
    private final void showImagePickerOptions() {
    }
    
    private final void requestCameraPermission() {
    }
    
    @java.lang.Override()
    @java.lang.Deprecated()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    private final void openGallery() {
    }
    
    private final void dispatchTakePictureIntent() {
    }
    
    private final boolean isCameraAppAvailable() {
        return false;
    }
    
    @java.lang.Override()
    public void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    private final void handleGalleryImage(android.net.Uri imageUri) {
    }
    
    private final void furtherProcess() {
    }
    
    private final void invokeProfileImg(java.lang.String authToken, okhttp3.MultipartBody.Part files) {
    }
    
    private final void observeProfileImgUpdate() {
    }
    
    @kotlin.jvm.Throws(exceptionClasses = {java.io.IOException.class})
    private final java.io.File createImageFile() throws java.io.IOException {
        return null;
    }
    
    private final void setUserPic() {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    private final void callUpdateApi() {
    }
    
    private final void observeUpdateProfile() {
    }
    
    private final boolean isValidAge(int age) {
        return false;
    }
    
    private final void handleCameraImage() {
    }
    
    private final java.lang.Object loadBitmapFromUri(android.net.Uri uri, kotlin.coroutines.Continuation<? super android.graphics.Bitmap> $completion) {
        return null;
    }
    
    private final int calculateInSampleSize(android.graphics.BitmapFactory.Options options, int reqWidth, int reqHeight) {
        return 0;
    }
    
    private final void maleSelection() {
    }
    
    private final void femaleSelection() {
    }
    
    private final void otherSelection() {
    }
    
    public UpdateProfileFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0006\u001a\u00020\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/update_profile/UpdateProfileFragment$Companion;", "", "()V", "REQUEST_CAMERA_PERMISSION", "", "REQUEST_IMAGE_CAPTURE", "newInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.empcloud.empmonitor.ui.fragment.update_profile.UpdateProfileFragment.Companion newInstance() {
            return null;
        }
    }
}