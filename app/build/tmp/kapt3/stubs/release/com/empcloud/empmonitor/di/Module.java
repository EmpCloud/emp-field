package com.empcloud.empmonitor.di;

import android.content.Context;
import androidx.room.Room;
import com.empcloud.empmonitor.data.local.AppDatabase;
import com.empcloud.empmonitor.data.local.Location.LocationDao;
import com.empcloud.empmonitor.data.remote.response.attendance.AObjectDetails;
import com.empcloud.empmonitor.data.remote.response.attendance.BObjectDetails;
import com.empcloud.empmonitor.network.ApiService;
import com.empcloud.empmonitor.network.maps_service.MapApiService;
import com.empcloud.empmonitor.utils.CommonDetailsDeserializer;
import com.empcloud.empmonitor.utils.Constants;
import com.empcloud.empmonitor.utils.NativeLib;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0012\u0010\f\u001a\u00020\r2\b\b\u0001\u0010\u000e\u001a\u00020\u000fH\u0007J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\rH\u0007J\b\u0010\u0013\u001a\u00020\u0014H\u0007J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\tH\u0007R\u0019\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0018"}, d2 = {"Lcom/empcloud/empmonitor/di/Module;", "", "()V", "gson", "Lcom/google/gson/Gson;", "kotlin.jvm.PlatformType", "getGson", "()Lcom/google/gson/Gson;", "provideBaseUrl", "", "nativeLib", "Lcom/empcloud/empmonitor/utils/NativeLib;", "provideDatabase", "Lcom/empcloud/empmonitor/data/local/AppDatabase;", "appContext", "Landroid/content/Context;", "provideLocationDao", "Lcom/empcloud/empmonitor/data/local/Location/LocationDao;", "database", "provideMapsRoutes", "Lcom/empcloud/empmonitor/network/maps_service/MapApiService;", "provideServices", "Lcom/empcloud/empmonitor/network/ApiService;", "baseUrl", "app_release"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class Module {
    private static final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.di.Module INSTANCE = null;
    
    private Module() {
        super();
    }
    
    public final com.google.gson.Gson getGson() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String provideBaseUrl(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.utils.NativeLib nativeLib) {
        return null;
    }
    
    @javax.inject.Singleton()
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.network.ApiService provideServices(@org.jetbrains.annotations.NotNull()
    java.lang.String baseUrl) {
        return null;
    }
    
    @javax.inject.Singleton()
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.network.maps_service.MapApiService provideMapsRoutes() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.local.AppDatabase provideDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context appContext) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.local.Location.LocationDao provideLocationDao(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.local.AppDatabase database) {
        return null;
    }
}