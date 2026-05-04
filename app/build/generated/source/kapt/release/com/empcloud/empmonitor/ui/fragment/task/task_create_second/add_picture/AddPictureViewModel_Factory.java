package com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_picture;

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
public final class AddPictureViewModel_Factory implements Factory<AddPictureViewModel> {
  private final Provider<NetworkRepository> repositoryProvider;

  public AddPictureViewModel_Factory(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddPictureViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddPictureViewModel_Factory create(Provider<NetworkRepository> repositoryProvider) {
    return new AddPictureViewModel_Factory(repositoryProvider);
  }

  public static AddPictureViewModel newInstance(NetworkRepository repository) {
    return new AddPictureViewModel(repository);
  }
}
