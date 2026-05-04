package com.empcloud.empmonitor.utils;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class NativeLibModule_GetProvideNativeLibFactory implements Factory<NativeLib> {
  @Override
  public NativeLib get() {
    return getProvideNativeLib();
  }

  public static NativeLibModule_GetProvideNativeLibFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NativeLib getProvideNativeLib() {
    return Preconditions.checkNotNullFromProvides(NativeLibModule.INSTANCE.getProvideNativeLib());
  }

  private static final class InstanceHolder {
    private static final NativeLibModule_GetProvideNativeLibFactory INSTANCE = new NativeLibModule_GetProvideNativeLibFactory();
  }
}
