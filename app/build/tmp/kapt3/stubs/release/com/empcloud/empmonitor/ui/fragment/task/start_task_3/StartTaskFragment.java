package com.empcloud.empmonitor.ui.fragment.task.start_task_3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency;
import com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl;
import com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl;
import com.empcloud.empmonitor.data.remote.request.update_reschedule.UpdateRescheduleModel;
import com.empcloud.empmonitor.data.remote.request.update_task.Tags;
import com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel;
import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail;
import com.empcloud.empmonitor.data.remote.response.task_stage_selection.Data;
import com.empcloud.empmonitor.data.remote.response.update_task.UpdateTaskResponse;
import com.empcloud.empmonitor.databinding.FragmentStartTaskBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter;
import com.empcloud.empmonitor.ui.adapters.TaskStageTagsAdapter;
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
import com.empcloud.empmonitor.ui.listeners.TaskStageTagItemClickListener;
import com.empcloud.empmonitor.utils.ActiveTaskTracker;
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
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.squareup.picasso.Picasso;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.Calendar;
import java.util.Currency;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0094\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u0000 \u009c\u00012\u00020\u00012\u00020\u00022\u00020\u0003:\u0002\u009c\u0001B\u0011\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010V\u001a\u00020WH\u0002J\b\u0010X\u001a\u00020WH\u0002J\b\u0010Y\u001a\u00020WH\u0002J\b\u0010Z\u001a\u00020WH\u0002J\b\u0010[\u001a\u00020WH\u0002J\u0010\u0010\\\u001a\u00020W2\u0006\u0010]\u001a\u00020\u000bH\u0002J\b\u0010^\u001a\u00020WH\u0002J\b\u0010_\u001a\u00020WH\u0002J:\u0010`\u001a\u00020W2\u0006\u0010a\u001a\u00020b2\u0006\u0010c\u001a\u00020d2\u0006\u0010e\u001a\u00020\u000b2\u0006\u0010f\u001a\u00020g2\b\u0010h\u001a\u0004\u0018\u00010\u000b2\u0006\u0010i\u001a\u00020jH\u0002J\u0010\u0010k\u001a\u00020W2\u0006\u0010l\u001a\u00020\u000bH\u0003J\u0010\u0010m\u001a\u00020W2\u0006\u0010n\u001a\u00020\u000fH\u0002J\b\u0010o\u001a\u00020WH\u0002J\b\u0010p\u001a\u00020WH\u0002J\b\u0010q\u001a\u00020WH\u0002J\u0016\u0010r\u001a\u00020W2\f\u0010s\u001a\b\u0012\u0004\u0012\u00020u0tH\u0002J\u0010\u0010v\u001a\u00020W2\u0006\u0010w\u001a\u00020xH\u0002J\b\u0010y\u001a\u00020WH\u0002J\b\u0010z\u001a\u00020WH\u0002J\b\u0010{\u001a\u00020WH\u0002J\u0012\u0010|\u001a\u00020W2\b\u0010}\u001a\u0004\u0018\u00010~H\u0016J+\u0010\u007f\u001a\u0005\u0018\u00010\u0080\u00012\b\u0010\u0081\u0001\u001a\u00030\u0082\u00012\n\u0010\u0083\u0001\u001a\u0005\u0018\u00010\u0084\u00012\b\u0010}\u001a\u0004\u0018\u00010~H\u0016J\u001b\u0010\u0085\u0001\u001a\u00020W2\u0007\u0010\u0086\u0001\u001a\u00020\u000f2\u0007\u0010\u0087\u0001\u001a\u00020uH\u0016J\u0012\u0010\u0088\u0001\u001a\u00020W2\u0007\u0010\u0089\u0001\u001a\u00020.H\u0016J\u001d\u0010\u008a\u0001\u001a\u00020W2\b\u0010\u008b\u0001\u001a\u00030\u0080\u00012\b\u0010}\u001a\u0004\u0018\u00010~H\u0017J\u0012\u0010\u008c\u0001\u001a\u00020W2\u0007\u0010\u008d\u0001\u001a\u00020\u000bH\u0002J\u001a\u0010\u008e\u0001\u001a\u00020W2\u0006\u0010l\u001a\u00020\u000b2\u0007\u0010\u008f\u0001\u001a\u00020xH\u0002J\t\u0010\u0090\u0001\u001a\u00020WH\u0002J\u0012\u0010\u0091\u0001\u001a\u00020W2\u0007\u0010\u0092\u0001\u001a\u00020\u000fH\u0002J\t\u0010\u0093\u0001\u001a\u00020WH\u0002J\t\u0010\u0094\u0001\u001a\u00020WH\u0002J\u0013\u0010\u0095\u0001\u001a\u00020W2\b\u0010\u0096\u0001\u001a\u00030\u0097\u0001H\u0002J\u0011\u0010\u0098\u0001\u001a\u00020W2\u0006\u0010n\u001a\u00020\u000fH\u0002J\t\u0010\u0099\u0001\u001a\u00020WH\u0002J\t\u0010\u009a\u0001\u001a\u00020WH\u0002J\t\u0010\u009b\u0001\u001a\u00020WH\u0002R\u0012\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\tR\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0013\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u001bR\u0012\u0010\u001c\u001a\u0004\u0018\u00010\u001aX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u001bR\u000e\u0010\u001d\u001a\u00020\u001eX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010$\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u000e\u0010%\u001a\u00020&X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\'\u001a\b\u0012\u0004\u0012\u00020(0 X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010)\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u000e\u0010*\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010+\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010,\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020.X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010/\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00100\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00101\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00102\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00103\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00104\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u000206X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u00107\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00108\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020:X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020<X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020>X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010?\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010@\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020BX\u0082.\u00a2\u0006\u0002\n\u0000R\u0012\u0010C\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0010R\u0010\u0010D\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010E\u001a\u0016\u0012\u0004\u0012\u00020G\u0018\u00010Fj\n\u0012\u0004\u0012\u00020G\u0018\u0001`HX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010I\u001a\u0004\u0018\u00010\u001aX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u001bR\u0010\u0010J\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010K\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010L\u001a\u0004\u0018\u00010\u001aX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u001bR\u0012\u0010M\u001a\u0004\u0018\u00010\u001aX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u001bR\u0012\u0010N\u001a\u0004\u0018\u00010\u001aX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u001bR\u0010\u0010O\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010P\u001a\u00020Q8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\bT\u0010U\u001a\u0004\bR\u0010S\u00a8\u0006\u009d\u0001"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/task/start_task_3/StartTaskFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/google/android/gms/maps/OnMapReadyCallback;", "Lcom/empcloud/empmonitor/ui/listeners/TaskStageTagItemClickListener;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "(Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;)V", "IS_PENDING_TASK_RESCHEDULE", "", "Ljava/lang/Boolean;", "address", "", "amountCurrency", "Lcom/empcloud/empmonitor/data/remote/request/create_task/AmountCurrency;", "backno", "", "Ljava/lang/Integer;", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentStartTaskBinding;", "buttonClickednumber", "calendarView", "Landroid/widget/CalendarView;", "clientName", "code", "currency", "currentLat", "", "Ljava/lang/Double;", "currentLng", "currentMarker", "Lcom/google/android/gms/maps/model/Marker;", "docsList", "", "Lcom/empcloud/empmonitor/data/remote/request/create_task/FilesUrl;", "fetchTaskDetail", "Lcom/empcloud/empmonitor/data/remote/response/fetch_task/FetchTaskDetail;", "firstclick", "fusedLocationClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "imagesList", "Lcom/empcloud/empmonitor/data/remote/request/create_task/ImgaesUrl;", "isTaskStarted", "isTaskStartedInfo", "lat", "lon", "map", "Lcom/google/android/gms/maps/GoogleMap;", "pic1", "pic2", "pic3", "pic4", "picDesc", "picResponse", "placesClient", "Lcom/google/android/libraries/places/api/net/PlacesClient;", "popUpTimeEnd", "popUptimeStart", "searchBar", "Landroid/widget/EditText;", "searchResults", "Landroidx/recyclerview/widget/RecyclerView;", "searchResultsAdapter", "Lcom/empcloud/empmonitor/ui/adapters/SearchResultsAdapter;", "selectedCountry", "selectedDate", "stageAdapter", "Lcom/empcloud/empmonitor/ui/adapters/TaskStageTagsAdapter;", "startdone", "tagname", "tags", "Ljava/util/ArrayList;", "Lcom/empcloud/empmonitor/data/remote/request/update_task/Tags;", "Lkotlin/collections/ArrayList;", "taskAmount", "taskId", "taskName", "taskVolume", "taskval", "taskvol", "time", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/task/start_task_3/StartTaskViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/task/start_task_3/StartTaskViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "currencySpinner", "", "customizebottomsheet", "disableMapGestures", "docsShowing", "enableMapGestures", "fetchPlaceDetails", "placeId", "firstPic", "fourthPic", "getAndSetData", "imageshow", "Landroid/widget/LinearLayout;", "imgcard", "Landroidx/cardview/widget/CardView;", "imgurl", "imgshow", "Landroid/widget/ImageView;", "name", "imgtext", "Landroid/widget/TextView;", "getCurrentLocationAndSearch", "query", "getLastLocation", "statusCode", "getUserLocation", "imagePreviewHandling", "imageShowing", "initRecyclerStage", "data", "", "Lcom/empcloud/empmonitor/data/remote/response/task_stage_selection/Data;", "moveMarker", "latLng", "Lcom/google/android/gms/maps/model/LatLng;", "observeGetTags", "observeUpdateRescheduleCall", "observeUpdateTaskCall", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onItemClickTags", "position", "dataTag", "onMapReady", "googleMap", "onViewCreated", "view", "openDocs", "url", "searchPlaces", "currentLatLng", "secondPic", "showTimePickerDialogPopUp", "i", "stagSelectionProcess", "thirdPic", "updateButtons", "it", "Lcom/empcloud/empmonitor/data/remote/response/update_task/UpdateTaskResponse;", "updateTaskDetail", "updateValue", "validatebtn", "zoomToCurrentLocation", "Companion", "app_release"})
public final class StartTaskFragment extends androidx.fragment.app.Fragment implements com.google.android.gms.maps.OnMapReadyCallback, com.empcloud.empmonitor.ui.listeners.TaskStageTagItemClickListener {
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String selectedCountry;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer code;
    private com.empcloud.empmonitor.databinding.FragmentStartTaskBinding binding;
    private androidx.recyclerview.widget.RecyclerView searchResults;
    private android.widget.EditText searchBar;
    private com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter searchResultsAdapter;
    private com.google.android.libraries.places.api.net.PlacesClient placesClient;
    private com.google.android.gms.maps.GoogleMap map;
    private com.google.android.gms.maps.model.Marker currentMarker;
    private com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String taskName;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double taskvol;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double taskval;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String currency;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String time;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String address;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String taskId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer isTaskStarted;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String clientName;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String lat;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String lon;
    private android.widget.CalendarView calendarView;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String selectedDate;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String popUptimeStart;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String popUpTimeEnd;
    @org.jetbrains.annotations.Nullable()
    private com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail fetchTaskDetail;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer backno;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer startdone;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer firstclick;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer buttonClickednumber;
    private boolean isTaskStartedInfo = false;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String picResponse;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String picDesc;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency amountCurrency;
    @org.jetbrains.annotations.Nullable()
    private java.util.ArrayList<com.empcloud.empmonitor.data.remote.request.update_task.Tags> tags;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double taskVolume;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double taskAmount;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String pic1;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String pic2;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String pic3;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String pic4;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String tagname;
    private com.empcloud.empmonitor.ui.adapters.TaskStageTagsAdapter stageAdapter;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl> docsList;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl> imagesList;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Boolean IS_PENDING_TASK_RESCHEDULE = false;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double currentLat;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double currentLng;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.fragment.task.start_task_3.StartTaskFragment.Companion Companion = null;
    
    public StartTaskFragment(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.task.start_task_3.StartTaskViewModel getViewModel() {
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
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void currencySpinner() {
    }
    
    private final void observeUpdateRescheduleCall() {
    }
    
    @java.lang.Override()
    public void onMapReady(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap googleMap) {
    }
    
    private final void getUserLocation() {
    }
    
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    private final void getCurrentLocationAndSearch(java.lang.String query) {
    }
    
    private final void moveMarker(com.google.android.gms.maps.model.LatLng latLng) {
    }
    
    private final void searchPlaces(java.lang.String query, com.google.android.gms.maps.model.LatLng currentLatLng) {
    }
    
    private final void fetchPlaceDetails(java.lang.String placeId) {
    }
    
    private final void zoomToCurrentLocation() {
    }
    
    private final void disableMapGestures() {
    }
    
    private final void enableMapGestures() {
    }
    
    private final void observeUpdateTaskCall() {
    }
    
    private final void updateButtons(com.empcloud.empmonitor.data.remote.response.update_task.UpdateTaskResponse it) {
    }
    
    private final void updateTaskDetail(int statusCode) {
    }
    
    private final void showTimePickerDialogPopUp(int i) {
    }
    
    private final void getLastLocation(int statusCode) {
    }
    
    private final void validatebtn() {
    }
    
    private final void customizebottomsheet() {
    }
    
    private final void docsShowing() {
    }
    
    private final void imageShowing() {
    }
    
    private final void fourthPic() {
    }
    
    private final void thirdPic() {
    }
    
    private final void secondPic() {
    }
    
    private final void firstPic() {
    }
    
    private final void getAndSetData(android.widget.LinearLayout imageshow, androidx.cardview.widget.CardView imgcard, java.lang.String imgurl, android.widget.ImageView imgshow, java.lang.String name, android.widget.TextView imgtext) {
    }
    
    private final void imagePreviewHandling() {
    }
    
    private final void stagSelectionProcess() {
    }
    
    private final void observeGetTags() {
    }
    
    private final void initRecyclerStage(java.util.List<com.empcloud.empmonitor.data.remote.response.task_stage_selection.Data> data) {
    }
    
    @java.lang.Override()
    public void onItemClickTags(int position, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.task_stage_selection.Data dataTag) {
    }
    
    private final void updateValue() {
    }
    
    private final void openDocs(java.lang.String url) {
    }
    
    public StartTaskFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0000\u00a8\u0006\u0004"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/task/start_task_3/StartTaskFragment$Companion;", "", "()V", "newInstance", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.empcloud.empmonitor.ui.fragment.task.start_task_3.StartTaskFragment.Companion newInstance() {
            return null;
        }
    }
}