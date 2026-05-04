package com.empcloud.empmonitor.ui.fragment.attendance;

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
public final class AttendanceViewModel_Factory implements Factory<AttendanceViewModel> {
  private final Provider<NetworkRepository> repositoryProvider;

  public AttendanceViewModel_Factory(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AttendanceViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AttendanceViewModel_Factory create(Provider<NetworkRepository> repositoryProvider) {
    return new AttendanceViewModel_Factory(repositoryProvider);
  }

  public static AttendanceViewModel newInstance(NetworkRepository repository) {
    return new AttendanceViewModel(repository);
  }
}
