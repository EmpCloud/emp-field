package com.empcloud.empmonitor.ui.fragment.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel;
import com.empcloud.empmonitor.data.remote.request.mode_of_transport.ModeOFTransportModel;
import com.empcloud.empmonitor.data.remote.response.attendance.AttendanceFullData;
import com.empcloud.empmonitor.data.remote.response.map_response.DirectionsResponse;
import com.empcloud.empmonitor.data.remote.response.fetchattendacne.EmployeeLocation;
import com.empcloud.empmonitor.databinding.FragmentMapCurrentBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity;
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity;
import com.empcloud.empmonitor.ui.fragment.client.clientdirection.ClientDirectionViewModel;
import com.empcloud.empmonitor.ui.fragment.home.HomeFragment;
import com.empcloud.empmonitor.ui.fragment.home.HomeViewModel;
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
import com.empcloud.empmonitor.ui.services.location_tacking.LocationService;
import com.empcloud.empmonitor.utils.ActiveTaskTracker;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.empcloud.empmonitor.utils.NativeLib;
import com.empcloud.empmonitor.utils.broadcast_services.GeoFenceBroadCastReciever;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ncorti.slidetoact.SlideToActView;
import dagger.hilt.android.AndroidEntryPoint;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00e4\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0007\u0018\u0000 \u0094\u00012\u00020\u00012\u00020\u0002:\u0004\u0094\u0001\u0095\u0001B\u0011\u0012\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\u0002\u0010\u0005J\u000e\u0010A\u001a\u00020\t2\u0006\u0010B\u001a\u00020\tJ\u000e\u0010C\u001a\u00020\t2\u0006\u0010B\u001a\u00020\tJ:\u0010D\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u000e\u0010E\u001a\n\u0012\u0004\u0012\u00020F\u0018\u00010\u00172\b\u0010G\u001a\u0004\u0018\u00010\t2\b\u0010H\u001a\u0004\u0018\u00010\t2\u0006\u0010I\u001a\u00020\u0015H\u0002J\u0018\u0010J\u001a\u00020\u000f2\u0006\u0010K\u001a\u00020L2\u0006\u0010M\u001a\u00020LH\u0002J\u0018\u0010N\u001a\u00020L2\u0006\u0010K\u001a\u00020L2\u0006\u0010M\u001a\u00020LH\u0002J\u0018\u0010O\u001a\u00020\u000f2\u0006\u0010K\u001a\u00020L2\u0006\u0010M\u001a\u00020LH\u0002J\b\u0010P\u001a\u00020QH\u0002J\b\u0010R\u001a\u00020QH\u0002J\u0018\u0010S\u001a\u00020\u00152\u0006\u0010T\u001a\u00020U2\u0006\u0010V\u001a\u00020\u0018H\u0002J\u0010\u0010W\u001a\u00020Q2\u0006\u0010X\u001a\u00020\u0018H\u0002J\u0010\u0010Y\u001a\u00020Q2\u0006\u0010Z\u001a\u00020\tH\u0002J\u0012\u0010[\u001a\u0004\u0018\u00010\u00182\u0006\u0010T\u001a\u00020UH\u0002J\u0012\u0010\\\u001a\u0004\u0018\u00010\u00182\u0006\u0010T\u001a\u00020UH\u0002J\u0010\u0010]\u001a\u00020\t2\u0006\u0010^\u001a\u00020\tH\u0007J\u0010\u0010_\u001a\u00020`2\u0006\u0010a\u001a\u00020&H\u0002J\u0006\u0010b\u001a\u00020\tJ\b\u0010c\u001a\u00020\tH\u0007J\b\u0010d\u001a\u00020QH\u0002J\b\u0010e\u001a\u00020QH\u0002J\b\u0010f\u001a\u00020\"H\u0002J\u0018\u0010g\u001a\u00020\"2\u0006\u0010T\u001a\u00020U2\u0006\u0010V\u001a\u00020\u0018H\u0002J\u0018\u0010h\u001a\u00020Q2\u0006\u0010Z\u001a\u00020\t2\u0006\u0010i\u001a\u00020jH\u0002J\b\u0010k\u001a\u00020QH\u0002J\b\u0010l\u001a\u00020QH\u0002J\b\u0010m\u001a\u00020QH\u0002J\u0012\u0010n\u001a\u00020Q2\b\u0010o\u001a\u0004\u0018\u00010pH\u0016J&\u0010q\u001a\u0004\u0018\u00010r2\u0006\u0010s\u001a\u00020t2\b\u0010u\u001a\u0004\u0018\u00010v2\b\u0010o\u001a\u0004\u0018\u00010pH\u0016J\b\u0010w\u001a\u00020QH\u0016J\u0010\u0010x\u001a\u00020Q2\u0006\u0010y\u001a\u000202H\u0016J\u001a\u0010z\u001a\u00020Q2\u0006\u0010{\u001a\u00020r2\b\u0010o\u001a\u0004\u0018\u00010pH\u0017J\u0019\u0010|\u001a\u0004\u0018\u00010\u000f2\b\u0010}\u001a\u0004\u0018\u00010\tH\u0002\u00a2\u0006\u0002\u0010~J\u0019\u0010\u007f\u001a\u0004\u0018\u00010\u000f2\b\u0010}\u001a\u0004\u0018\u00010\tH\u0002\u00a2\u0006\u0002\u0010~J\u000f\u0010\u0080\u0001\u001a\u00020\t2\u0006\u0010B\u001a\u00020\tJ\u0016\u0010\u0081\u0001\u001a\u00020Q2\u000b\b\u0002\u0010\u0082\u0001\u001a\u0004\u0018\u00010UH\u0002J\u001c\u0010\u0083\u0001\u001a\u00020Q2\b\u0010\u0084\u0001\u001a\u00030\u0085\u00012\u0007\u0010\u0086\u0001\u001a\u00020\tH\u0007J\u0007\u0010\u0087\u0001\u001a\u00020QJ\t\u0010\u0088\u0001\u001a\u00020QH\u0002J\u0013\u0010\u0089\u0001\u001a\u00020Q2\b\u0010\u008a\u0001\u001a\u00030\u008b\u0001H\u0002J\t\u0010\u008c\u0001\u001a\u00020QH\u0002J\t\u0010\u008d\u0001\u001a\u00020QH\u0002J\t\u0010\u008e\u0001\u001a\u00020QH\u0002J\t\u0010\u008f\u0001\u001a\u00020QH\u0002J\t\u0010\u0090\u0001\u001a\u00020QH\u0002J\t\u0010\u0091\u0001\u001a\u00020QH\u0002J\t\u0010\u0092\u0001\u001a\u00020QH\u0002J\t\u0010\u0093\u0001\u001a\u00020QH\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u0012\u0010\u0011\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u000e\u0010\u0012\u001a\u00020\u000fX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000fX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0019\u001a\u00020\u001a8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001d\u0010\u001e\u001a\u0004\b\u001b\u0010\u001cR\u000e\u0010\u001f\u001a\u00020 X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010%\u001a\u0004\u0018\u00010&X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\'R\u000e\u0010(\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\"X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010+\u001a\u0004\u0018\u00010\"X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010,R\u0012\u0010-\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020/X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u00100\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u000e\u00101\u001a\u000202X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u00103\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00104\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u000206X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u00107\u001a\u0002088BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b;\u0010\u001e\u001a\u0004\b9\u0010:R\u001b\u0010<\u001a\u00020=8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b@\u0010\u001e\u001a\u0004\b>\u0010?\u00a8\u0006\u0096\u0001"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/map/MapCurrentFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/google/android/gms/maps/OnMapReadyCallback;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "(Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;)V", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentMapCurrentBinding;", "checkIn", "", "currentPolyline", "Lcom/google/android/gms/maps/model/Polyline;", "fusedLocationClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "geoCurrentLat", "", "Ljava/lang/Double;", "geoCurrentLon", "geo_lat", "geo_lon", "geo_radius", "", "geofenceLocations", "", "Lcom/empcloud/empmonitor/ui/fragment/map/MapCurrentFragment$GeofenceLocation;", "geofencePendingIntent", "Landroid/app/PendingIntent;", "getGeofencePendingIntent", "()Landroid/app/PendingIntent;", "geofencePendingIntent$delegate", "Lkotlin/Lazy;", "geofencingClient", "Lcom/google/android/gms/location/GeofencingClient;", "hasAutoCheckedIn", "", "isAutoCheckInByGeoFencing", "isFirstTimeZoom", "isGeoFencingOn", "", "Ljava/lang/Integer;", "isGlobal", "isInsideGeofence", "isModeSelected", "isServiceStart", "Ljava/lang/Boolean;", "lat", "locationCallback", "Lcom/google/android/gms/location/LocationCallback;", "lon", "map", "Lcom/google/android/gms/maps/GoogleMap;", "mode", "mode_local", "slideActView", "Lcom/ncorti/slidetoact/SlideToActView;", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/home/HomeViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/home/HomeViewModel;", "viewModel$delegate", "viewModelMap", "Lcom/empcloud/empmonitor/ui/fragment/client/clientdirection/ClientDirectionViewModel;", "getViewModelMap", "()Lcom/empcloud/empmonitor/ui/fragment/client/clientdirection/ClientDirectionViewModel;", "viewModelMap$delegate", "SPLit", "time", "againparseTimeData", "buildGeofenceLocations", "employeeLocations", "Lcom/empcloud/empmonitor/data/remote/response/fetchattendacne/EmployeeLocation;", "fallbackLatitude", "fallbackLongitude", "fallbackRadius", "calculateDistance", "point1", "Lcom/google/android/gms/maps/model/LatLng;", "point2", "calculateMidpoint", "calculateZoomLevel", "commonSelection", "", "createGeofence", "distanceToGeofence", "location", "Landroid/location/Location;", "geofenceLocation", "drawRouteToGeofence", "targetGeofence", "fetchAttendance", "authToken", "findMatchingGeofence", "findNearestGeofence", "formatApiTime", "apiTime", "getBitmapFromDrawable", "Landroid/graphics/Bitmap;", "drawableId", "getCurentFormattedDate", "getCurrentTime", "getLastLocation", "handleBatteryOptimizations", "hasValidGeofenceConfig", "isWithinGeofence", "markAttendance", "markAttendanceModel", "Lcom/empcloud/empmonitor/data/remote/request/mark_attendance/MarkAttendanceModel;", "observeAttendanceCall", "observeAttendanceFetchCall", "observeUpdationTransport", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroy", "onMapReady", "googleMap", "onViewCreated", "view", "parseLatitude", "value", "(Ljava/lang/String;)Ljava/lang/Double;", "parseLongitude", "parseTimeData", "renderMap", "currentLocation", "setFormattedTimeWithAmPm", "textView", "Landroid/widget/TextView;", "timeString", "setUserData", "setUserPic", "showCheckoutConfirmationDialog", "context", "Landroid/content/Context;", "showPopUpConfirmation", "slideToAct", "startLocationService", "startLocationUpdates", "stopLocationService", "updateSliderForCheckIn", "updateSliderForCheckOut", "zoomToCurrentLocation", "Companion", "GeofenceLocation", "app_release"})
public final class MapCurrentFragment extends androidx.fragment.app.Fragment implements com.google.android.gms.maps.OnMapReadyCallback {
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String checkIn;
    private boolean isInsideGeofence = false;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double geoCurrentLat;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double geoCurrentLon;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Boolean isServiceStart;
    private boolean isModeSelected = false;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String mode;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String mode_local;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer isGeoFencingOn;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double lat;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double lon;
    private com.empcloud.empmonitor.databinding.FragmentMapCurrentBinding binding;
    private com.google.android.gms.maps.GoogleMap map;
    private com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModelMap$delegate = null;
    private com.google.android.gms.location.GeofencingClient geofencingClient;
    private com.ncorti.slidetoact.SlideToActView slideActView;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private final double geo_lat = 12.9782871;
    private final double geo_lon = 77.635809;
    private float geo_radius = 50.0F;
    private boolean isFirstTimeZoom = true;
    private boolean isGlobal = false;
    private boolean isAutoCheckInByGeoFencing = false;
    private boolean hasAutoCheckedIn = false;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.empcloud.empmonitor.ui.fragment.map.MapCurrentFragment.GeofenceLocation> geofenceLocations;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.gms.maps.model.Polyline currentPolyline;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static double YOUR_GEOFENCE_LATITUDE = 0.0;
    private static double YOUR_GEOFENCE_LONGITUDE = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy geofencePendingIntent$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.android.gms.location.LocationCallback locationCallback = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.fragment.map.MapCurrentFragment.Companion Companion = null;
    
    public MapCurrentFragment(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.client.clientdirection.ClientDirectionViewModel getViewModelMap() {
        return null;
    }
    
    private final com.empcloud.empmonitor.ui.fragment.home.HomeViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final android.app.PendingIntent getGeofencePendingIntent() {
        return null;
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
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void fetchAttendance(java.lang.String authToken) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurentFormattedDate() {
        return null;
    }
    
    @java.lang.Override()
    public void onMapReady(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap googleMap) {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentTime() {
        return null;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public final void setFormattedTimeWithAmPm(@org.jetbrains.annotations.NotNull()
    android.widget.TextView textView, @org.jetbrains.annotations.NotNull()
    java.lang.String timeString) {
    }
    
    private final void slideToAct() {
    }
    
    private final void markAttendance(java.lang.String authToken, com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel markAttendanceModel) {
    }
    
    private final void observeAttendanceCall() {
    }
    
    private final void updateSliderForCheckIn() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String SPLit(@org.jetbrains.annotations.NotNull()
    java.lang.String time) {
        return null;
    }
    
    @android.annotation.SuppressLint(value = {"SimpleDateFormat"})
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatApiTime(@org.jetbrains.annotations.NotNull()
    java.lang.String apiTime) {
        return null;
    }
    
    private final android.graphics.Bitmap getBitmapFromDrawable(int drawableId) {
        return null;
    }
    
    private final void observeAttendanceFetchCall() {
    }
    
    private final void createGeofence() {
    }
    
    private final void startLocationUpdates() {
    }
    
    private final void drawRouteToGeofence(com.empcloud.empmonitor.ui.fragment.map.MapCurrentFragment.GeofenceLocation targetGeofence) {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    private final void getLastLocation() {
    }
    
    private final void showCheckoutConfirmationDialog(android.content.Context context) {
    }
    
    private final void setUserPic() {
    }
    
    private final void stopLocationService() {
    }
    
    private final void startLocationService() {
    }
    
    private final void handleBatteryOptimizations() {
    }
    
    private final void showPopUpConfirmation() {
    }
    
    private final void zoomToCurrentLocation() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String parseTimeData(@org.jetbrains.annotations.NotNull()
    java.lang.String time) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String againparseTimeData(@org.jetbrains.annotations.NotNull()
    java.lang.String time) {
        return null;
    }
    
    public final void setUserData() {
    }
    
    private final double calculateZoomLevel(com.google.android.gms.maps.model.LatLng point1, com.google.android.gms.maps.model.LatLng point2) {
        return 0.0;
    }
    
    private final com.google.android.gms.maps.model.LatLng calculateMidpoint(com.google.android.gms.maps.model.LatLng point1, com.google.android.gms.maps.model.LatLng point2) {
        return null;
    }
    
    private final double calculateDistance(com.google.android.gms.maps.model.LatLng point1, com.google.android.gms.maps.model.LatLng point2) {
        return 0.0;
    }
    
    private final void observeUpdationTransport() {
    }
    
    private final void commonSelection() {
    }
    
    private final void updateSliderForCheckOut() {
    }
    
    private final java.lang.Double parseLatitude(java.lang.String value) {
        return null;
    }
    
    private final java.lang.Double parseLongitude(java.lang.String value) {
        return null;
    }
    
    private final java.util.List<com.empcloud.empmonitor.ui.fragment.map.MapCurrentFragment.GeofenceLocation> buildGeofenceLocations(java.util.List<com.empcloud.empmonitor.data.remote.response.fetchattendacne.EmployeeLocation> employeeLocations, java.lang.String fallbackLatitude, java.lang.String fallbackLongitude, float fallbackRadius) {
        return null;
    }
    
    private final com.empcloud.empmonitor.ui.fragment.map.MapCurrentFragment.GeofenceLocation findMatchingGeofence(android.location.Location location) {
        return null;
    }
    
    private final com.empcloud.empmonitor.ui.fragment.map.MapCurrentFragment.GeofenceLocation findNearestGeofence(android.location.Location location) {
        return null;
    }
    
    private final boolean isWithinGeofence(android.location.Location location, com.empcloud.empmonitor.ui.fragment.map.MapCurrentFragment.GeofenceLocation geofenceLocation) {
        return false;
    }
    
    private final float distanceToGeofence(android.location.Location location, com.empcloud.empmonitor.ui.fragment.map.MapCurrentFragment.GeofenceLocation geofenceLocation) {
        return 0.0F;
    }
    
    private final void renderMap(android.location.Location currentLocation) {
    }
    
    private final boolean hasValidGeofenceConfig() {
        return false;
    }
    
    public MapCurrentFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/map/MapCurrentFragment$Companion;", "", "()V", "LOCATION_PERMISSION_REQUEST_CODE", "", "YOUR_GEOFENCE_LATITUDE", "", "YOUR_GEOFENCE_LONGITUDE", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J1\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000b\u00a8\u0006\u001c"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/map/MapCurrentFragment$GeofenceLocation;", "", "id", "", "latLng", "Lcom/google/android/gms/maps/model/LatLng;", "radius", "", "title", "(Ljava/lang/String;Lcom/google/android/gms/maps/model/LatLng;FLjava/lang/String;)V", "getId", "()Ljava/lang/String;", "getLatLng", "()Lcom/google/android/gms/maps/model/LatLng;", "getRadius", "()F", "getTitle", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "app_release"})
    static final class GeofenceLocation {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String id = null;
        @org.jetbrains.annotations.NotNull()
        private final com.google.android.gms.maps.model.LatLng latLng = null;
        private final float radius = 0.0F;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String title = null;
        
        public GeofenceLocation(@org.jetbrains.annotations.NotNull()
        java.lang.String id, @org.jetbrains.annotations.NotNull()
        com.google.android.gms.maps.model.LatLng latLng, float radius, @org.jetbrains.annotations.NotNull()
        java.lang.String title) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getId() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.google.android.gms.maps.model.LatLng getLatLng() {
            return null;
        }
        
        public final float getRadius() {
            return 0.0F;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getTitle() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.google.android.gms.maps.model.LatLng component2() {
            return null;
        }
        
        public final float component3() {
            return 0.0F;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.empcloud.empmonitor.ui.fragment.map.MapCurrentFragment.GeofenceLocation copy(@org.jetbrains.annotations.NotNull()
        java.lang.String id, @org.jetbrains.annotations.NotNull()
        com.google.android.gms.maps.model.LatLng latLng, float radius, @org.jetbrains.annotations.NotNull()
        java.lang.String title) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}