package com.empcloud.empmonitor.ui.fragment.edit_client.first;

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
public final class EditClientViewModel_Factory implements Factory<EditClientViewModel> {
  private final Provider<NetworkRepository> repositoryProvider;

  public EditClientViewModel_Factory(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public EditClientViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static EditClientViewModel_Factory create(Provider<NetworkRepository> repositoryProvider) {
    return new EditClientViewModel_Factory(repositoryProvider);
  }

  public static EditClientViewModel newInstance(NetworkRepository repository) {
    return new EditClientViewModel(repository);
  }
}
