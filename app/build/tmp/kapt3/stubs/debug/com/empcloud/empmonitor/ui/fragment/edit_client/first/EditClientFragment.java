package com.empcloud.empmonitor.ui.fragment.edit_client.first;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.request.update_client.UpdatecClientModel;
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail;
import com.empcloud.empmonitor.databinding.FragmentEditClientBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.fragment.attendance.AttendanceFragment;
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientFragment;
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientViewModel;
import com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeFragment;
import com.empcloud.empmonitor.ui.fragment.edit_client.client_address.EditClientAddressFragment;
import com.empcloud.empmonitor.ui.fragment.edit_client.update_client.UpdaeEditClientFragment;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.hbb20.CountryCodePicker;
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
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\b\u0007\u0018\u0000 K2\u00020\u0001:\u0001KB\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010%\u001a\u00020&H\u0002J\b\u0010\'\u001a\u00020&H\u0002J\b\u0010(\u001a\u00020&H\u0002J\b\u0010)\u001a\u00020*H\u0002J\b\u0010+\u001a\u00020&H\u0002J\b\u0010,\u001a\u00020&H\u0002J\u0010\u0010-\u001a\u00020&2\u0006\u0010.\u001a\u00020\u001cH\u0002J\u0018\u0010/\u001a\u00020&2\u0006\u00100\u001a\u00020\b2\u0006\u00101\u001a\u000202H\u0002J\b\u00103\u001a\u00020&H\u0002J\b\u00104\u001a\u00020&H\u0002J\"\u00105\u001a\u00020&2\u0006\u00106\u001a\u00020\u00062\u0006\u00107\u001a\u00020\u00062\b\u00108\u001a\u0004\u0018\u000109H\u0016J\u0012\u0010:\u001a\u00020&2\b\u0010;\u001a\u0004\u0018\u00010<H\u0016J&\u0010=\u001a\u0004\u0018\u00010>2\u0006\u0010?\u001a\u00020@2\b\u0010A\u001a\u0004\u0018\u00010B2\b\u0010;\u001a\u0004\u0018\u00010<H\u0016J\u001a\u0010C\u001a\u00020&2\u0006\u0010D\u001a\u00020>2\b\u0010;\u001a\u0004\u0018\u00010<H\u0016J\b\u0010E\u001a\u00020&H\u0002J\b\u0010F\u001a\u00020&H\u0002J\b\u0010G\u001a\u00020&H\u0002J\b\u0010H\u001a\u00020&H\u0002J\b\u0010I\u001a\u00020&H\u0002J\b\u0010J\u001a\u00020&H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0015R\u0012\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0018R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0019\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0018R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001e\u001a\u00020\u001f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\"\u0010#\u001a\u0004\b \u0010!R\u0010\u0010$\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006L"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/edit_client/first/EditClientFragment;", "Landroidx/fragment/app/Fragment;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "(Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;)V", "REQUEST_CAMERA_PERMISSION", "", "address", "", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentEditClientBinding;", "category", "city", "clientDetails", "Lcom/empcloud/empmonitor/data/remote/response/clientfetch/ClientFetchDetail;", "codePicker", "Lcom/hbb20/CountryCodePicker;", "countryCode", "countryname", "currentPhotoPath", "fragmentno", "Ljava/lang/Integer;", "lat", "", "Ljava/lang/Double;", "lon", "profilePic", "savedUri", "Landroid/net/Uri;", "state", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/edit_client/first/EditClientViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/edit_client/first/EditClientViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "zip", "btnClickEvents", "", "camerProcess", "countryCodeGetterAndSetter", "createImageFile", "Ljava/io/File;", "dispatchTakePictureIntent", "handleCameraImage", "handleGalleryImage", "imageUri", "invokeProfileImg", "token", "part", "Lokhttp3/MultipartBody$Part;", "observeProfileImgUpdate", "observeUpdateClientCall", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onViewCreated", "view", "openGallery", "requestCameraPermission", "setUserData", "setUserPic", "showImagePickerOptions", "validateForm", "Companion", "app_debug"})
public final class EditClientFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener = null;
    private com.empcloud.empmonitor.databinding.FragmentEditClientBinding binding;
    @org.jetbrains.annotations.Nullable()
    private com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail clientDetails;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private final int REQUEST_CAMERA_PERMISSION = 121;
    private java.lang.String currentPhotoPath;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri savedUri;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String profilePic;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double lat;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double lon;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String countryCode;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String countryname;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String state;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String zip;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String city;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String address;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String category;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer fragmentno;
    private com.hbb20.CountryCodePicker codePicker;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.fragment.edit_client.first.EditClientFragment.Companion Companion = null;
    
    public EditClientFragment(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.edit_client.first.EditClientViewModel getViewModel() {
        return null;
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
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void countryCodeGetterAndSetter() {
    }
    
    private final void btnClickEvents() {
    }
    
    private final void setUserData() {
    }
    
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
    
    private final void observeUpdateClientCall() {
    }
    
    private final void setUserPic() {
    }
    
    public EditClientFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0000\u00a8\u0006\u0004"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/edit_client/first/EditClientFragment$Companion;", "", "()V", "newInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.empcloud.empmonitor.ui.fragment.edit_client.first.EditClientFragment.Companion newInstance() {
            return null;
        }
    }
}