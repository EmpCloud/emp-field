package com.empcloud.empmonitor.ui.adapters;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.empcloud.empmonitor.R;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.squareup.picasso.Picasso;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001*B!\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006\u00a2\u0006\u0002\u0010\tJ(\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0015\u001a\u00020\u000bH\u0002J.\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\u00072\u001c\u0010\u0018\u001a\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u0007\u0012\u0004\u0012\u00020\b0\u0019H\u0002J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u0018\u0010\u001d\u001a\u00020\b2\u0006\u0010\u001e\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u001cH\u0016J\u0018\u0010 \u001a\u00020\u00022\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u001cH\u0016J\u001f\u0010$\u001a\u00020\b2\b\u0010%\u001a\u0004\u0018\u00010\u000b2\b\u0010&\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\u0002\u0010\'J\u0014\u0010(\u001a\u00020\b2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fR\u0012\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\fR\u0012\u0010\r\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\fR\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/SearchResultsAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/empcloud/empmonitor/ui/adapters/SearchResultsAdapter$SearchResultViewHolder;", "placesClient", "Lcom/google/android/libraries/places/api/net/PlacesClient;", "onPlaceSelected", "Lkotlin/Function1;", "", "", "(Lcom/google/android/libraries/places/api/net/PlacesClient;Lkotlin/jvm/functions/Function1;)V", "currentLat", "", "Ljava/lang/Double;", "currentLng", "results", "", "Lcom/google/android/libraries/places/api/model/AutocompletePrediction;", "calculateDistance", "lat1", "lng1", "lat2", "lng2", "fetchPlaceDetails", "placeId", "callback", "Lkotlin/Function2;", "Lcom/google/android/libraries/places/api/model/Place;", "getItemCount", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "updateCurrentLocation", "lat", "lng", "(Ljava/lang/Double;Ljava/lang/Double;)V", "updateData", "newResults", "SearchResultViewHolder", "app_debug"})
public final class SearchResultsAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter.SearchResultViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final com.google.android.libraries.places.api.net.PlacesClient placesClient = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<java.lang.String, kotlin.Unit> onPlaceSelected = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<? extends com.google.android.libraries.places.api.model.AutocompletePrediction> results;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double currentLat;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double currentLng;
    
    public SearchResultsAdapter(@org.jetbrains.annotations.NotNull()
    com.google.android.libraries.places.api.net.PlacesClient placesClient, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onPlaceSelected) {
        super();
    }
    
    public final void updateData(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends com.google.android.libraries.places.api.model.AutocompletePrediction> newResults) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter.SearchResultViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.ui.adapters.SearchResultsAdapter.SearchResultViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    private final void fetchPlaceDetails(java.lang.String placeId, kotlin.jvm.functions.Function2<? super com.google.android.libraries.places.api.model.Place, ? super java.lang.String, kotlin.Unit> callback) {
    }
    
    private final double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        return 0.0;
    }
    
    public final void updateCurrentLocation(@org.jetbrains.annotations.Nullable()
    java.lang.Double lat, @org.jetbrains.annotations.Nullable()
    java.lang.Double lng) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\bR\u0011\u0010\u0012\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\b\u00a8\u0006\u0014"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/SearchResultsAdapter$SearchResultViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "distance", "Landroid/widget/TextView;", "getDistance", "()Landroid/widget/TextView;", "divider", "getDivider", "()Landroid/view/View;", "icon", "Landroid/widget/ImageView;", "getIcon", "()Landroid/widget/ImageView;", "textView", "getTextView", "textView2", "getTextView2", "app_debug"})
    public static final class SearchResultViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView textView = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView textView2 = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView icon = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView distance = null;
        @org.jetbrains.annotations.NotNull()
        private final android.view.View divider = null;
        
        public SearchResultViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTextView() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTextView2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getIcon() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getDistance() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.view.View getDivider() {
            return null;
        }
    }
}