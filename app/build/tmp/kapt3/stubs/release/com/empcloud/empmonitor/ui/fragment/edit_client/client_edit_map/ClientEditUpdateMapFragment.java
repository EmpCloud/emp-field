package com.empcloud.empmonitor.ui.fragment.edit_client.client_edit_map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail;
import com.empcloud.empmonitor.databinding.FragmentClientEditUpdateMapBinding;
import com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter;
import com.empcloud.empmonitor.ui.fragment.client.addcompleteaddress.ClientCompleteAddressFragment;
import com.empcloud.empmonitor.ui.fragment.client.clientaddress.ClientAddressFragment;
import com.empcloud.empmonitor.ui.fragment.edit_client.client_address.EditClientAddressFragment;
import com.empcloud.empmonitor.ui.fragment.edit_client.update_client.UpdaeEditClientFragment;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.empcloud.empmonitor.utils.NativeLib;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.Locale;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u009a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010$\u001a\u00020%H\u0002J\u000e\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020\u000fJ\b\u0010)\u001a\u00020%H\u0002J\u0010\u0010*\u001a\u00020%2\u0006\u0010+\u001a\u00020\u0005H\u0002J\u0010\u0010,\u001a\u00020%2\u0006\u0010-\u001a\u00020\u0005H\u0002J\u0010\u0010.\u001a\u00020/2\u0006\u00100\u001a\u000201H\u0002J\u0010\u00102\u001a\u00020%2\u0006\u0010-\u001a\u00020\u0005H\u0003J\b\u00103\u001a\u00020%H\u0002J\u0010\u00104\u001a\u00020%2\u0006\u00105\u001a\u000206H\u0002J\u0012\u00107\u001a\u00020%2\b\u00108\u001a\u0004\u0018\u000109H\u0016J&\u0010:\u001a\u0004\u0018\u00010;2\u0006\u0010<\u001a\u00020=2\b\u0010>\u001a\u0004\u0018\u00010?2\b\u00108\u001a\u0004\u0018\u000109H\u0016J\u0010\u0010@\u001a\u00020%2\u0006\u0010A\u001a\u00020\u0019H\u0016J\u001a\u0010B\u001a\u00020%2\u0006\u0010C\u001a\u00020;2\b\u00108\u001a\u0004\u0018\u000109H\u0016J\u0018\u0010D\u001a\u00020%2\u0006\u0010-\u001a\u00020\u00052\u0006\u0010E\u001a\u000206H\u0002J\u0010\u0010F\u001a\u00020%2\u0006\u00105\u001a\u000206H\u0002J\b\u0010G\u001a\u00020%H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u0012\u0010\u0011\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0016\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006H"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/edit_client/client_edit_map/ClientEditUpdateMapFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/google/android/gms/maps/OnMapReadyCallback;", "()V", "addressText", "", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentClientEditUpdateMapBinding;", "city", "clientDetails", "Lcom/empcloud/empmonitor/data/remote/response/clientfetch/ClientFetchDetail;", "country", "countryCode", "countryName", "currentLat", "", "Ljava/lang/Double;", "currentLng", "currentMarker", "Lcom/google/android/gms/maps/model/Marker;", "fusedLocationClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "latitude", "longitude", "map", "Lcom/google/android/gms/maps/GoogleMap;", "placesClient", "Lcom/google/android/libraries/places/api/net/PlacesClient;", "searchBar", "Landroid/widget/EditText;", "searchResults", "Landroidx/recyclerview/widget/RecyclerView;", "searchResultsAdapter", "Lcom/empcloud/empmonitor/ui/adapters/SearchResultsAdapter;", "state", "zip", "disableMapGestures", "", "doubleToLong", "", "doubleValue", "enableMapGestures", "fetchPlaceDetails", "placeId", "getAutocompletePredictions", "query", "getBitmapFromDrawable", "Landroid/graphics/Bitmap;", "drawableId", "", "getCurrentLocationAndSearch", "getUserLocation", "moveMarker", "latLng", "Lcom/google/android/gms/maps/model/LatLng;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onMapReady", "googleMap", "onViewCreated", "view", "searchPlaces", "currentLatLng", "updateLocationText", "zoomToCurrentLocation", "app_release"})
public final class ClientEditUpdateMapFragment extends androidx.fragment.app.Fragment implements com.google.android.gms.maps.OnMapReadyCallback {
    private com.empcloud.empmonitor.databinding.FragmentClientEditUpdateMapBinding binding;
    @org.jetbrains.annotations.Nullable()
    private com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail clientDetails;
    private com.google.android.gms.maps.GoogleMap map;
    private com.google.android.gms.maps.model.Marker currentMarker;
    private com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient;
    private java.lang.String city;
    private java.lang.String country;
    private java.lang.String addressText;
    private java.lang.String zip;
    private java.lang.String state;
    private androidx.recyclerview.widget.RecyclerView searchResults;
    private android.widget.EditText searchBar;
    private com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter searchResultsAdapter;
    private com.google.android.libraries.places.api.net.PlacesClient placesClient;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double latitude;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double longitude;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String countryCode;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String countryName;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double currentLat;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double currentLng;
    
    public ClientEditUpdateMapFragment() {
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
    
    public final long doubleToLong(double doubleValue) {
        return 0L;
    }
    
    private final void updateLocationText(com.google.android.gms.maps.model.LatLng latLng) {
    }
    
    private final void zoomToCurrentLocation() {
    }
    
    private final void fetchPlaceDetails(java.lang.String placeId) {
    }
    
    private final void moveMarker(com.google.android.gms.maps.model.LatLng latLng) {
    }
    
    private final void searchPlaces(java.lang.String query, com.google.android.gms.maps.model.LatLng currentLatLng) {
    }
    
    private final android.graphics.Bitmap getBitmapFromDrawable(int drawableId) {
        return null;
    }
    
    private final void getAutocompletePredictions(java.lang.String query) {
    }
    
    private final void disableMapGestures() {
    }
    
    private final void enableMapGestures() {
    }
    
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    private final void getCurrentLocationAndSearch(java.lang.String query) {
    }
    
    @java.lang.Override()
    public void onMapReady(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap googleMap) {
    }
    
    private final void getUserLocation() {
    }
}