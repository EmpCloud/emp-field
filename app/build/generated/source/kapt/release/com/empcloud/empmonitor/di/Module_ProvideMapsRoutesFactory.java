package com.empcloud.empmonitor.di;

import com.empcloud.empmonitor.network.maps_service.MapApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class Module_ProvideMapsRoutesFactory implements Factory<MapApiService> {
  @Override
  public MapApiService get() {
    return provideMapsRoutes();
  }

  public static Module_ProvideMapsRoutesFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MapApiService provideMapsRoutes() {
    return Preconditions.checkNotNullFromProvides(Module.INSTANCE.provideMapsRoutes());
  }

  private static final class InstanceHolder {
    private static final Module_ProvideMapsRoutesFactory INSTANCE = new Module_ProvideMapsRoutesFactory();
  }
}
