package com.empcloud.empmonitor.di;

import com.empcloud.empmonitor.network.ApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class Module_ProvideServicesFactory implements Factory<ApiService> {
  private final Provider<String> baseUrlProvider;

  public Module_ProvideServicesFactory(Provider<String> baseUrlProvider) {
    this.baseUrlProvider = baseUrlProvider;
  }

  @Override
  public ApiService get() {
    return provideServices(baseUrlProvider.get());
  }

  public static Module_ProvideServicesFactory create(Provider<String> baseUrlProvider) {
    return new Module_ProvideServicesFactory(baseUrlProvider);
  }

  public static ApiService provideServices(String baseUrl) {
    return Preconditions.checkNotNullFromProvides(Module.INSTANCE.provideServices(baseUrl));
  }
}
