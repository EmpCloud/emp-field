package com.empcloud.empmonitor.ui.activity.createprofile;

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
public final class CreateProfileViewModel_Factory implements Factory<CreateProfileViewModel> {
  private final Provider<NetworkRepository> repositoryProvider;

  public CreateProfileViewModel_Factory(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CreateProfileViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static CreateProfileViewModel_Factory create(
      Provider<NetworkRepository> repositoryProvider) {
    return new CreateProfileViewModel_Factory(repositoryProvider);
  }

  public static CreateProfileViewModel newInstance(NetworkRepository repository) {
    return new CreateProfileViewModel(repository);
  }
}
