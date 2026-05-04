package com.empcloud.empmonitor.ui.fragment.client.clientdirection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.response.map_response.DirectionsResponse;
import com.empcloud.empmonitor.databinding.FragmentClientDirectionBinding;
import com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter;
import com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeFragment;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.empcloud.empmonitor.utils.NativeLib;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.android.PolyUtil;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0096\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0011\u0012\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\u0002\u0010\u0005J\u0010\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u0007H\u0002J\u0010\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020)H\u0002J\b\u0010*\u001a\u00020$H\u0002J\u0010\u0010+\u001a\u00020$2\u0006\u0010,\u001a\u00020\u0007H\u0002J\u0010\u0010-\u001a\u00020$2\u0006\u0010.\u001a\u00020\u0007H\u0002J\u0010\u0010/\u001a\u00020$2\u0006\u00100\u001a\u000201H\u0002J\u0012\u00102\u001a\u00020$2\b\u00103\u001a\u0004\u0018\u000104H\u0016J&\u00105\u001a\u0004\u0018\u0001062\u0006\u00107\u001a\u0002082\b\u00109\u001a\u0004\u0018\u00010:2\b\u00103\u001a\u0004\u0018\u000104H\u0016J\u0010\u0010;\u001a\u00020$2\u0006\u0010<\u001a\u00020\u0013H\u0016J\u001a\u0010=\u001a\u00020$2\u0006\u0010>\u001a\u0002062\b\u00103\u001a\u0004\u0018\u000104H\u0016J\u0010\u0010?\u001a\u00020$2\u0006\u0010@\u001a\u00020\u0007H\u0002J\b\u0010A\u001a\u00020$H\u0002R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u0012\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\fR\u0012\u0010\r\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\fR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001d\u001a\u00020\u001e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b!\u0010\"\u001a\u0004\b\u001f\u0010 \u00a8\u0006B"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/client/clientdirection/ClientDirectionFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/google/android/gms/maps/OnMapReadyCallback;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "(Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;)V", "address", "", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentClientDirectionBinding;", "currentLat", "", "Ljava/lang/Double;", "currentLng", "currentMarker", "Lcom/google/android/gms/maps/model/Marker;", "fusedLocationClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "map", "Lcom/google/android/gms/maps/GoogleMap;", "name", "placesClient", "Lcom/google/android/libraries/places/api/net/PlacesClient;", "searchBar", "Landroid/widget/EditText;", "searchResults", "Landroidx/recyclerview/widget/RecyclerView;", "searchResultsAdapter", "Lcom/empcloud/empmonitor/ui/adapters/SearchResultsAdapter;", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/client/clientdirection/ClientDirectionViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/client/clientdirection/ClientDirectionViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "fetchPlaceDetails", "", "placeId", "getBitmapFromDrawable", "Landroid/graphics/Bitmap;", "drawableId", "", "getUserLocation", "loadMap", "html", "loadWebView", "googleApiKey", "moveMarker", "latLng", "Lcom/google/android/gms/maps/model/LatLng;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onMapReady", "googleMap", "onViewCreated", "view", "searchPlaces", "query", "zoomToCurrentLocation", "app_release"})
public final class ClientDirectionFragment extends androidx.fragment.app.Fragment implements com.google.android.gms.maps.OnMapReadyCallback {
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener = null;
    private com.empcloud.empmonitor.databinding.FragmentClientDirectionBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private androidx.recyclerview.widget.RecyclerView searchResults;
    private android.widget.EditText searchBar;
    private com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter searchResultsAdapter;
    private com.google.android.libraries.places.api.net.PlacesClient placesClient;
    private com.google.android.gms.maps.GoogleMap map;
    private com.google.android.gms.maps.model.Marker currentMarker;
    private com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String name;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String address;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double currentLat;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double currentLng;
    
    public ClientDirectionFragment(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.client.clientdirection.ClientDirectionViewModel getViewModel() {
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
    
    private final void loadWebView(java.lang.String googleApiKey) {
    }
    
    private final void loadMap(java.lang.String html) {
    }
    
    @java.lang.Override()
    public void onMapReady(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap googleMap) {
    }
    
    private final void fetchPlaceDetails(java.lang.String placeId) {
    }
    
    private final void moveMarker(com.google.android.gms.maps.model.LatLng latLng) {
    }
    
    private final void searchPlaces(java.lang.String query) {
    }
    
    private final android.graphics.Bitmap getBitmapFromDrawable(int drawableId) {
        return null;
    }
    
    private final void getUserLocation() {
    }
    
    private final void zoomToCurrentLocation() {
    }
    
    public ClientDirectionFragment() {
        super();
    }
}