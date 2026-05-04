package com.empcloud.empmonitor.ui.fragment.task.start_task_3;

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
public final class StartTaskViewModel_Factory implements Factory<StartTaskViewModel> {
  private final Provider<NetworkRepository> repositoryProvider;

  public StartTaskViewModel_Factory(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public StartTaskViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static StartTaskViewModel_Factory create(Provider<NetworkRepository> repositoryProvider) {
    return new StartTaskViewModel_Factory(repositoryProvider);
  }

  public static StartTaskViewModel newInstance(NetworkRepository repository) {
    return new StartTaskViewModel(repository);
  }
}
