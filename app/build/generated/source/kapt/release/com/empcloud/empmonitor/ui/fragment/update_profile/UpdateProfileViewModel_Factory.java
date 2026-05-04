package com.empcloud.empmonitor.ui.fragment.update_profile;

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
public final class UpdateProfileViewModel_Factory implements Factory<UpdateProfileViewModel> {
  private final Provider<NetworkRepository> repositoryProvider;

  public UpdateProfileViewModel_Factory(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public UpdateProfileViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static UpdateProfileViewModel_Factory create(
      Provider<NetworkRepository> repositoryProvider) {
    return new UpdateProfileViewModel_Factory(repositoryProvider);
  }

  public static UpdateProfileViewModel newInstance(NetworkRepository repository) {
    return new UpdateProfileViewModel(repository);
  }
}
