package com.empcloud.empmonitor.network.maps_service;

import com.empcloud.empmonitor.data.remote.response.map_response.DirectionsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J,\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\u00062\b\b\u0001\u0010\b\u001a\u00020\u0006H\'\u00a8\u0006\t"}, d2 = {"Lcom/empcloud/empmonitor/network/maps_service/MapApiService;", "", "getDirections", "Lretrofit2/Call;", "Lcom/empcloud/empmonitor/data/remote/response/map_response/DirectionsResponse;", "origin", "", "destination", "apiKey", "app_debug"})
public abstract interface MapApiService {
    
    @retrofit2.http.GET(value = "maps/api/directions/json")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<com.empcloud.empmonitor.data.remote.response.map_response.DirectionsResponse> getDirections(@retrofit2.http.Query(value = "origin")
    @org.jetbrains.annotations.NotNull()
    java.lang.String origin, @retrofit2.http.Query(value = "destination")
    @org.jetbrains.annotations.NotNull()
    java.lang.String destination, @retrofit2.http.Query(value = "key")
    @org.jetbrains.annotations.NotNull()
    java.lang.String apiKey);
}