package com.empcloud.empmonitor.ui.fragment.notification;

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
public final class NotificationViewModel_Factory implements Factory<NotificationViewModel> {
  private final Provider<NetworkRepository> repositoryProvider;

  public NotificationViewModel_Factory(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public NotificationViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static NotificationViewModel_Factory create(
      Provider<NetworkRepository> repositoryProvider) {
    return new NotificationViewModel_Factory(repositoryProvider);
  }

  public static NotificationViewModel newInstance(NetworkRepository repository) {
    return new NotificationViewModel(repository);
  }
}
