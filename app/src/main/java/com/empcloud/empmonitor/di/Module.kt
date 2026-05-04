package com.empcloud.empmonitor.di

import android.content.Context
import androidx.room.Room
import com.empcloud.empmonitor.data.local.AppDatabase
import com.empcloud.empmonitor.data.local.Location.LocationDao
import com.empcloud.empmonitor.data.remote.response.attendance.AObjectDetails
import com.empcloud.empmonitor.data.remote.response.attendance.BObjectDetails
import com.empcloud.empmonitor.network.ApiService
import com.empcloud.empmonitor.network.maps_service.MapApiService
import com.empcloud.empmonitor.utils.CommonDetailsDeserializer
import com.empcloud.empmonitor.utils.Constants
import com.empcloud.empmonitor.utils.NativeLib
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

//    val gson = GsonBuilder()
//        .registerTypeAdapter(AObjectDetails::class.java, AObjectDetailsDeserializer())
//        .create()
val gson = GsonBuilder()
    .registerTypeAdapter(AObjectDetails::class.java, CommonDetailsDeserializer<AObjectDetails>())
    .registerTypeAdapter(BObjectDetails::class.java, CommonDetailsDeserializer<BObjectDetails>())
    .create()

    @Provides
    @Singleton
    fun provideBaseUrl(nativeLib: NativeLib):String{
        return nativeLib.getBaseUrlDev()
    }
    @Singleton
    @Provides
    fun provideServices(baseUrl: String): ApiService {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            }
                    )
                    .callTimeout(4, TimeUnit.MINUTES)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()

            )
            .addCallAdapterFactory(CoroutineCallAdapterFactory()).addConverterFactory(GsonConverterFactory.create(
                gson
            ))
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMapsRoutes(): MapApiService {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(NativeLib().getGoogleMapUrlKey())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            }
                    )
                    .callTimeout(4, TimeUnit.MINUTES)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()

            )
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(MapApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "location-database"
        ).build()
    }

    @Provides
    fun provideLocationDao(database: AppDatabase): LocationDao {
        return database.locationDao()
    }

}