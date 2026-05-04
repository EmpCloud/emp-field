package com.empcloud.empmonitor.ui.fragment.update_address;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.RequiresApi;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.databinding.ActivityMapAddressBinding;
import com.empcloud.empmonitor.databinding.FragmentUpdateAddressBinding;
import com.empcloud.empmonitor.databinding.FragmentUpdateProfileBinding;
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity;
import com.empcloud.empmonitor.ui.activity.mapaddress.MapShowActivity;
import com.empcloud.empmonitor.ui.fragment.edit_client.client_address.EditClientAddressFragment;
import com.empcloud.empmonitor.ui.fragment.update_profile.UpdateProfileFragment;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.squareup.picasso.Picasso;
import dagger.hilt.android.AndroidEntryPoint;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\b\u0010\u0012\u001a\u00020\u0011H\u0002J\u0012\u0010\u0013\u001a\u00020\u00112\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J&\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J\b\u0010\u001c\u001a\u00020\u0011H\u0017J\u001a\u0010\u001d\u001a\u00020\u00112\u0006\u0010\u001e\u001a\u00020\u00172\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J\b\u0010\u001f\u001a\u00020\u0011H\u0002J\b\u0010 \u001a\u00020\u0011H\u0002J\b\u0010!\u001a\u00020\u0011H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/update_address/UpdateAddressFragment;", "Landroidx/fragment/app/Fragment;", "()V", "CAMERA_REQUEST_CODE", "", "REQUEST_CAMERA_PERMISSION", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentUpdateAddressBinding;", "currentPhotoPath", "", "locationManager", "Landroid/location/LocationManager;", "locationProviderReceiver", "Landroid/content/BroadcastReceiver;", "isLocationEnabled", "", "moveToNext", "", "moveToPrevious", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onPause", "onViewCreated", "view", "setUserPic", "showLocationOffDialog", "switchToggle", "app_release"})
public final class UpdateAddressFragment extends androidx.fragment.app.Fragment {
    private com.empcloud.empmonitor.databinding.FragmentUpdateAddressBinding binding;
    private java.lang.String currentPhotoPath;
    private final int REQUEST_CAMERA_PERMISSION = 121;
    private final int CAMERA_REQUEST_CODE = 131;
    private android.location.LocationManager locationManager;
    private android.content.BroadcastReceiver locationProviderReceiver;
    
    public UpdateAddressFragment() {
        super();
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
    
    @java.lang.Override()
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public void onPause() {
    }
    
    private final void moveToNext() {
    }
    
    private final void moveToPrevious() {
    }
    
    private final boolean isLocationEnabled() {
        return false;
    }
    
    private final void switchToggle() {
    }
    
    private final void showLocationOffDialog() {
    }
    
    private final void setUserPic() {
    }
}