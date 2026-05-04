package com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task;

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
public final class AddTaskViewModel_Factory implements Factory<AddTaskViewModel> {
  private final Provider<NetworkRepository> repositoryProvider;

  public AddTaskViewModel_Factory(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddTaskViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddTaskViewModel_Factory create(Provider<NetworkRepository> repositoryProvider) {
    return new AddTaskViewModel_Factory(repositoryProvider);
  }

  public static AddTaskViewModel newInstance(NetworkRepository repository) {
    return new AddTaskViewModel(repository);
  }
}
