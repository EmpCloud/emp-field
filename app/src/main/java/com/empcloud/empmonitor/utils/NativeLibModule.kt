package com.empcloud.empmonitor.utils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NativeLibModule {

    @Provides
    @Singleton
    fun getProvideNativeLib() : NativeLib = NativeLib()
}