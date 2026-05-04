package com.empcloud.empmonitor.ui.fragment.client.clientdirection;

import com.empcloud.empmonitor.network.maps_service.MapApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ClientDirectionViewModel_Factory implements Factory<ClientDirectionViewModel> {
  private final Provider<MapApiService> mapApiServiceProvider;

  public ClientDirectionViewModel_Factory(Provider<MapApiService> mapApiServiceProvider) {
    this.mapApiServiceProvider = mapApiServiceProvider;
  }

  @Override
  public ClientDirectionViewModel get() {
    return newInstance(mapApiServiceProvider.get());
  }

  public static ClientDirectionViewModel_Factory create(
      Provider<MapApiService> mapApiServiceProvider) {
    return new ClientDirectionViewModel_Factory(mapApiServiceProvider);
  }

  public static ClientDirectionViewModel newInstance(MapApiService mapApiService) {
    return new ClientDirectionViewModel(mapApiService);
  }
}
