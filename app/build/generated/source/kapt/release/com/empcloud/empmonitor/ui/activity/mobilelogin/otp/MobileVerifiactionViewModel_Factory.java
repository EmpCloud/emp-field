package com.empcloud.empmonitor.ui.activity.mobilelogin.otp;

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
public final class MobileVerifiactionViewModel_Factory implements Factory<MobileVerifiactionViewModel> {
  private final Provider<NetworkRepository> repositoryProvider;

  public MobileVerifiactionViewModel_Factory(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public MobileVerifiactionViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static MobileVerifiactionViewModel_Factory create(
      Provider<NetworkRepository> repositoryProvider) {
    return new MobileVerifiactionViewModel_Factory(repositoryProvider);
  }

  public static MobileVerifiactionViewModel newInstance(NetworkRepository repository) {
    return new MobileVerifiactionViewModel(repository);
  }
}
