package com.empcloud.empmonitor.ui.fragment.client.addcompleteaddress;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.request.createclient.CreateClientModel;
import com.empcloud.empmonitor.databinding.FragmentClientCompleteAddressBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.fragment.client.add_client_with_editoptions.AddClientEditOptionFragment;
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientViewModel;
import com.empcloud.empmonitor.ui.fragment.client.clientmap.ClinetMapShowFragment;
import com.empcloud.empmonitor.ui.fragment.client.finalladd.ClientAddFinalFragment;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import dagger.hilt.android.AndroidEntryPoint;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0006J\b\u0010\u001b\u001a\u00020\u001cH\u0002J\b\u0010\u001d\u001a\u00020\u001eH\u0002J\u000e\u0010\u001f\u001a\u00020\b2\u0006\u0010 \u001a\u00020!J\b\u0010\"\u001a\u00020\u001cH\u0002J\u0012\u0010#\u001a\u00020\u001c2\b\u0010$\u001a\u0004\u0018\u00010%H\u0016J&\u0010&\u001a\u0004\u0018\u00010\'2\u0006\u0010(\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010+2\b\u0010$\u001a\u0004\u0018\u00010%H\u0016J\b\u0010,\u001a\u00020\u001cH\u0016J\b\u0010-\u001a\u00020\u001cH\u0016J\b\u0010.\u001a\u00020\u001cH\u0017J\u001a\u0010/\u001a\u00020\u001c2\u0006\u00100\u001a\u00020\'2\b\u0010$\u001a\u0004\u0018\u00010%H\u0016J\b\u00101\u001a\u00020\u001cH\u0002J\b\u00102\u001a\u00020\u001cH\u0002J\b\u00103\u001a\u00020\u001cH\u0002J\b\u00104\u001a\u00020\u001cH\u0002J\b\u00105\u001a\u00020\u001cH\u0002J\b\u00106\u001a\u00020\u001cH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u000e\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\tR\u0012\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0011R\u001b\u0010\u0012\u001a\u00020\u00138BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0016\u0010\u0017\u001a\u0004\b\u0014\u0010\u0015\u00a8\u00067"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/client/addcompleteaddress/ClientCompleteAddressFragment;", "Landroidx/fragment/app/Fragment;", "()V", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentClientCompleteAddressBinding;", "clientId", "", "latD", "", "Ljava/lang/Double;", "locationManager", "Landroid/location/LocationManager;", "locationProviderReceiver", "Landroid/content/BroadcastReceiver;", "longd", "status", "", "Ljava/lang/Integer;", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/client/addclient/AddClientViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/client/addclient/AddClientViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "base64ToBitmap", "Landroid/graphics/Bitmap;", "base64String", "callApi", "", "isLocationEnabled", "", "longToDouble", "longValue", "", "observeCreateClientCall", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroy", "onPause", "onResume", "onViewCreated", "view", "setData", "setUserPic", "showLocationOffDialog", "switchToggle", "updateTextData", "validateForm", "app_release"})
public final class ClientCompleteAddressFragment extends androidx.fragment.app.Fragment {
    private com.empcloud.empmonitor.databinding.FragmentClientCompleteAddressBinding binding;
    private android.location.LocationManager locationManager;
    private android.content.BroadcastReceiver locationProviderReceiver;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer status = 0;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String clientId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double latD;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double longd;
    
    public ClientCompleteAddressFragment() {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientViewModel getViewModel() {
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
    
    private final void switchToggle() {
    }
    
    private final void showLocationOffDialog() {
    }
    
    @java.lang.Override()
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public void onResume() {
    }
    
    @java.lang.Override()
    public void onPause() {
    }
    
    private final boolean isLocationEnabled() {
        return false;
    }
    
    private final void validateForm() {
    }
    
    private final void observeCreateClientCall() {
    }
    
    private final void callApi() {
    }
    
    public final double longToDouble(long longValue) {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.graphics.Bitmap base64ToBitmap(@org.jetbrains.annotations.NotNull()
    java.lang.String base64String) {
        return null;
    }
    
    private final void setUserPic() {
    }
    
    private final void setData() {
    }
    
    private final void updateTextData() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
}