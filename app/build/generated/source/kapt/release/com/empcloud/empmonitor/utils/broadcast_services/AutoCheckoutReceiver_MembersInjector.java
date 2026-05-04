package com.empcloud.empmonitor.utils.broadcast_services;

import com.empcloud.empmonitor.network.NetworkRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class AutoCheckoutReceiver_MembersInjector implements MembersInjector<AutoCheckoutReceiver> {
  private final Provider<NetworkRepository> repositoryProvider;

  public AutoCheckoutReceiver_MembersInjector(Provider<NetworkRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  public static MembersInjector<AutoCheckoutReceiver> create(
      Provider<NetworkRepository> repositoryProvider) {
    return new AutoCheckoutReceiver_MembersInjector(repositoryProvider);
  }

  @Override
  public void injectMembers(AutoCheckoutReceiver instance) {
    injectRepository(instance, repositoryProvider.get());
  }

  @InjectedFieldSignature("com.empcloud.empmonitor.utils.broadcast_services.AutoCheckoutReceiver.repository")
  public static void injectRepository(AutoCheckoutReceiver instance, NetworkRepository repository) {
    instance.repository = repository;
  }
}
