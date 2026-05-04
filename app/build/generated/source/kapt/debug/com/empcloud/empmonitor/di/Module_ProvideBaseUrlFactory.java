package com.empcloud.empmonitor.di;

import com.empcloud.empmonitor.utils.NativeLib;
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
public final class Module_ProvideBaseUrlFactory implements Factory<String> {
  private final Provider<NativeLib> nativeLibProvider;

  public Module_ProvideBaseUrlFactory(Provider<NativeLib> nativeLibProvider) {
    this.nativeLibProvider = nativeLibProvider;
  }

  @Override
  public String get() {
    return provideBaseUrl(nativeLibProvider.get());
  }

  public static Module_ProvideBaseUrlFactory create(Provider<NativeLib> nativeLibProvider) {
    return new Module_ProvideBaseUrlFactory(nativeLibProvider);
  }

  public static String provideBaseUrl(NativeLib nativeLib) {
    return Preconditions.checkNotNullFromProvides(Module.INSTANCE.provideBaseUrl(nativeLib));
  }
}
