package com.empcloud.empmonitor.ui.services.location_tacking;

import com.empcloud.empmonitor.data.local.Location.LocationDao;
import com.empcloud.empmonitor.network.NetworkRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class LocationService_MembersInjector implements MembersInjector<LocationService> {
  private final Provider<NetworkRepository> repositoryProvider;

  private final Provider<LocationDao> locationDaoProvider;

  public LocationService_MembersInjector(Provider<NetworkRepository> repositoryProvider,
      Provider<LocationDao> locationDaoProvider) {
    this.repositoryProvider = repositoryProvider;
    this.locationDaoProvider = locationDaoProvider;
  }

  public static MembersInjector<LocationService> create(
      Provider<NetworkRepository> repositoryProvider, Provider<LocationDao> locationDaoProvider) {
    return new LocationService_MembersInjector(repositoryProvider, locationDaoProvider);
  }

  @Override
  public void injectMembers(LocationService instance) {
    injectRepository(instance, repositoryProvider.get());
    injectLocationDao(instance, locationDaoProvider.get());
  }

  @InjectedFieldSignature("com.empcloud.empmonitor.ui.services.location_tacking.LocationService.repository")
  public static void injectRepository(LocationService instance, NetworkRepository repository) {
    instance.repository = repository;
  }

  @InjectedFieldSignature("com.empcloud.empmonitor.ui.services.location_tacking.LocationService.locationDao")
  public static void injectLocationDao(LocationService instance, LocationDao locationDao) {
    instance.locationDao = locationDao;
  }
}
