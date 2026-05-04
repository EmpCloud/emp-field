package com.empcloud.empmonitor.ui.fragment.holidays;

import com.empcloud.empmonitor.network.NetworkRepository;
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
public final class HolidaysViewModel_Factory implements Factory<HolidaysViewModel> {
  private final Provider<NetworkRepository> repositoryProvider;

  public HolidaysViewModel_Factory(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public HolidaysViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static HolidaysViewModel_Factory create(Provider<NetworkRepository> repositoryProvider) {
    return new HolidaysViewModel_Factory(repositoryProvider);
  }

  public static HolidaysViewModel newInstance(NetworkRepository repository) {
    return new HolidaysViewModel(repository);
  }
}
