package com.empcloud.empmonitor.network;

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
public final class NetworkRepository_Factory implements Factory<NetworkRepository> {
  private final Provider<ApiService> serviceProvider;

  public NetworkRepository_Factory(Provider<ApiService> serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  @Override
  public NetworkRepository get() {
    return newInstance(serviceProvider.get());
  }

  public static NetworkRepository_Factory create(Provider<ApiService> serviceProvider) {
    return new NetworkRepository_Factory(serviceProvider);
  }

  public static NetworkRepository newInstance(ApiService service) {
    return new NetworkRepository(service);
  }
}
