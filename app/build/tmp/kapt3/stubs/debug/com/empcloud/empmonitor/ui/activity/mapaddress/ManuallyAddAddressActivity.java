package com.empcloud.empmonitor.ui.activity.mapaddress;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.empcloud.empmonitor.data.remote.request.updateprofile.UpdateProfileModel;
import com.empcloud.empmonitor.databinding.ActivityManuallyAddAddressBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity;
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileViewModel;
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import dagger.hilt.android.AndroidEntryPoint;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0002J\b\u0010\u0019\u001a\u00020\u0018H\u0002J\"\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\u00042\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0014J\b\u0010\u001f\u001a\u00020\u0018H\u0016J\u0012\u0010 \u001a\u00020\u00182\b\u0010!\u001a\u0004\u0018\u00010\"H\u0014J-\u0010#\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u00042\u000e\u0010$\u001a\n\u0012\u0006\b\u0001\u0012\u00020\f0%2\u0006\u0010&\u001a\u00020\'H\u0016\u00a2\u0006\u0002\u0010(J\b\u0010)\u001a\u00020\u0018H\u0002J\b\u0010*\u001a\u00020\u0018H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000e\u001a\u00020\u000f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0014\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/empcloud/empmonitor/ui/activity/mapaddress/ManuallyAddAddressActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "REQUEST_CAMERA_PERMISSION", "", "addressFirst", "Landroid/widget/EditText;", "addressSecond", "binding", "Lcom/empcloud/empmonitor/databinding/ActivityManuallyAddAddressBinding;", "city", "currentPhotoPath", "", "state", "viewmodel", "Lcom/empcloud/empmonitor/ui/activity/createprofile/CreateProfileViewModel;", "getViewmodel", "()Lcom/empcloud/empmonitor/ui/activity/createprofile/CreateProfileViewModel;", "viewmodel$delegate", "Lkotlin/Lazy;", "zip", "createImageFile", "Ljava/io/File;", "dispatchTakePictureIntent", "", "observerUpdateProfile", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onRequestPermissionsResult", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "setUserPic", "updateData", "app_debug"})
public final class ManuallyAddAddressActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.empcloud.empmonitor.databinding.ActivityManuallyAddAddressBinding binding;
    private android.widget.EditText addressFirst;
    private android.widget.EditText addressSecond;
    private android.widget.EditText state;
    private android.widget.EditText city;
    private android.widget.EditText zip;
    private java.lang.String currentPhotoPath;
    private final int REQUEST_CAMERA_PERMISSION = 121;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewmodel$delegate = null;
    
    public ManuallyAddAddressActivity() {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileViewModel getViewmodel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void dispatchTakePictureIntent() {
    }
    
    @kotlin.jvm.Throws(exceptionClasses = {java.io.IOException.class})
    private final java.io.File createImageFile() throws java.io.IOException {
        return null;
    }
    
    @java.lang.Override()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    private final void setUserPic() {
    }
    
    private final void updateData() {
    }
    
    private final void observerUpdateProfile() {
    }
}