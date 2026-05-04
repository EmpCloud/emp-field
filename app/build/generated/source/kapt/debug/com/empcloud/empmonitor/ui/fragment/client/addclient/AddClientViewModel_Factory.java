package com.empcloud.empmonitor.ui.fragment.client.addclient;

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
public final class AddClientViewModel_Factory implements Factory<AddClientViewModel> {
  private final Provider<NetworkRepository> repositoryProvider;

  public AddClientViewModel_Factory(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddClientViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddClientViewModel_Factory create(Provider<NetworkRepository> repositoryProvider) {
    return new AddClientViewModel_Factory(repositoryProvider);
  }

  public static AddClientViewModel newInstance(NetworkRepository repository) {
    return new AddClientViewModel(repository);
  }
}
