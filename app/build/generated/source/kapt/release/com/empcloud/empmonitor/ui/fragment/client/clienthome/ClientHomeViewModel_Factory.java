package com.empcloud.empmonitor.ui.fragment.client.clienthome;

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
public final class ClientHomeViewModel_Factory implements Factory<ClientHomeViewModel> {
  private final Provider<NetworkRepository> repositoryProvider;

  public ClientHomeViewModel_Factory(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ClientHomeViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ClientHomeViewModel_Factory create(Provider<NetworkRepository> repositoryProvider) {
    return new ClientHomeViewModel_Factory(repositoryProvider);
  }

  public static ClientHomeViewModel newInstance(NetworkRepository repository) {
    return new ClientHomeViewModel(repository);
  }
}
