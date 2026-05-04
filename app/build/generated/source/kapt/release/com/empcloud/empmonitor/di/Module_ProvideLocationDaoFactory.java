package com.empcloud.empmonitor.di;

import com.empcloud.empmonitor.data.local.AppDatabase;
import com.empcloud.empmonitor.data.local.Location.LocationDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class Module_ProvideLocationDaoFactory implements Factory<LocationDao> {
  private final Provider<AppDatabase> databaseProvider;

  public Module_ProvideLocationDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public LocationDao get() {
    return provideLocationDao(databaseProvider.get());
  }

  public static Module_ProvideLocationDaoFactory create(Provider<AppDatabase> databaseProvider) {
    return new Module_ProvideLocationDaoFactory(databaseProvider);
  }

  public static LocationDao provideLocationDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(Module.INSTANCE.provideLocationDao(database));
  }
}
