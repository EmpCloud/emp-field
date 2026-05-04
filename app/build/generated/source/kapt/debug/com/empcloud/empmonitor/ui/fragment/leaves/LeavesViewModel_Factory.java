package com.empcloud.empmonitor.ui.fragment.leaves;

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
public final class LeavesViewModel_Factory implements Factory<LeavesViewModel> {
  private final Provider<NetworkRepository> repositoryProvider;

  public LeavesViewModel_Factory(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public LeavesViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static LeavesViewModel_Factory create(Provider<NetworkRepository> repositoryProvider) {
    return new LeavesViewModel_Factory(repositoryProvider);
  }

  public static LeavesViewModel newInstance(NetworkRepository repository) {
    return new LeavesViewModel(repository);
  }
}
