package com.empcloud.empmonitor.ui.fragment.task.task_first;

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
public final class TaskHomeViewModel_Factory implements Factory<TaskHomeViewModel> {
  private final Provider<NetworkRepository> repositoryProvider;

  public TaskHomeViewModel_Factory(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public TaskHomeViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static TaskHomeViewModel_Factory create(Provider<NetworkRepository> repositoryProvider) {
    return new TaskHomeViewModel_Factory(repositoryProvider);
  }

  public static TaskHomeViewModel newInstance(NetworkRepository repository) {
    return new TaskHomeViewModel(repository);
  }
}
