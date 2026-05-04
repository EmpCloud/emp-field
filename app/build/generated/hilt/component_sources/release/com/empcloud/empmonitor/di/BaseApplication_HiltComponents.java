package com.empcloud.empmonitor.di;

import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileActivity_GeneratedInjector;
import com.empcloud.empmonitor.ui.activity.createprofile.CreateProfileViewModel_HiltModules;
import com.empcloud.empmonitor.ui.activity.forgotpassword.ForgotPasswordActivity_GeneratedInjector;
import com.empcloud.empmonitor.ui.activity.forgotpassword.ForgotPasswordViewModel_HiltModules;
import com.empcloud.empmonitor.ui.activity.login.EmailLoginActivity_GeneratedInjector;
import com.empcloud.empmonitor.ui.activity.login.EmailPasswordActivity_GeneratedInjector;
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity_GeneratedInjector;
import com.empcloud.empmonitor.ui.activity.login.LoginViewModel_HiltModules;
import com.empcloud.empmonitor.ui.activity.mainactivity.MainActivity_GeneratedInjector;
import com.empcloud.empmonitor.ui.activity.mapaddress.ManuallyAddAddressActivity_GeneratedInjector;
import com.empcloud.empmonitor.ui.activity.mapaddress.MapAddressActivity_GeneratedInjector;
import com.empcloud.empmonitor.ui.activity.mapaddress.MapShowActivity_GeneratedInjector;
import com.empcloud.empmonitor.ui.activity.mobilelogin.MobileLoginActivity_GeneratedInjector;
import com.empcloud.empmonitor.ui.activity.mobilelogin.otp.MobileOtpActivity_GeneratedInjector;
import com.empcloud.empmonitor.ui.activity.mobilelogin.otp.MobileVerifiactionViewModel_HiltModules;
import com.empcloud.empmonitor.ui.activity.resetpassword.ResetPasswordActivity_GeneratedInjector;
import com.empcloud.empmonitor.ui.activity.resetpassword.ResetPasswordViewModel_HiltModules;
import com.empcloud.empmonitor.ui.fragment.attendance.AttendanceFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.attendance.AttendanceViewModel_HiltModules;
import com.empcloud.empmonitor.ui.fragment.client.add_client_with_editoptions.AddClientEditOptionFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.client.addclient.AddClientViewModel_HiltModules;
import com.empcloud.empmonitor.ui.fragment.client.addcompleteaddress.ClientCompleteAddressFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.client.clientaddress.ClientAddressFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.client.clientdirection.ClientDirectionFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.client.clientdirection.ClientDirectionViewModel_HiltModules;
import com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.client.clienthome.ClientHomeViewModel_HiltModules;
import com.empcloud.empmonitor.ui.fragment.client.clientmap.ClinetMapShowFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.client.finalladd.ClientAddFinalFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.edit_client.client_address.EditClientAddressFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.edit_client.client_edit_map.ClientEditUpdateMapFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.edit_client.first.EditClientFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.edit_client.first.EditClientViewModel_HiltModules;
import com.empcloud.empmonitor.ui.fragment.edit_client.update_client.UpdaeEditClientFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.holidays.HolidaysFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.holidays.HolidaysViewModel_HiltModules;
import com.empcloud.empmonitor.ui.fragment.home.HomeFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.home.HomeViewModel_HiltModules;
import com.empcloud.empmonitor.ui.fragment.leaves.LeavesFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.leaves.LeavesViewModel_HiltModules;
import com.empcloud.empmonitor.ui.fragment.map.MapCurrentFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.notification.NotificationFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.notification.NotificationViewModel_HiltModules;
import com.empcloud.empmonitor.ui.fragment.settings.SettingsFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.task.select_client_3.SelectClientFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.task.start_task_3.StartTaskFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.task.start_task_3.StartTaskPicFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.task.start_task_3.StartTaskViewModel_HiltModules;
import com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_picture.AddPictureFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_picture.AddPictureViewModel_HiltModules;
import com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task.AddTaskFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task.AddTaskViewModel_HiltModules;
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeViewModel_HiltModules;
import com.empcloud.empmonitor.ui.fragment.update_address.UpdateAddressFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.update_address.UpdateAddressMapFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.update_address.UpdateFullAddressFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.update_profile.UpdateProfileFragment_GeneratedInjector;
import com.empcloud.empmonitor.ui.fragment.update_profile.UpdateProfileViewModel_HiltModules;
import com.empcloud.empmonitor.ui.services.location_tacking.LocationService_GeneratedInjector;
import com.empcloud.empmonitor.utils.NativeLibModule;
import com.empcloud.empmonitor.utils.broadcast_services.AutoCheckoutReceiver_GeneratedInjector;
import dagger.Binds;
import dagger.Component;
import dagger.Subcomponent;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.components.ViewComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.components.ViewWithFragmentComponent;
import dagger.hilt.android.flags.FragmentGetContextFix;
import dagger.hilt.android.flags.HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_DefaultViewModelFactories_ActivityModule;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ViewModelModule;
import dagger.hilt.android.internal.managers.ActivityComponentManager;
import dagger.hilt.android.internal.managers.FragmentComponentManager;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_LifecycleModule;
import dagger.hilt.android.internal.managers.HiltWrapper_SavedStateHandleModule;
import dagger.hilt.android.internal.managers.ServiceComponentManager;
import dagger.hilt.android.internal.managers.ViewComponentManager;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.HiltWrapper_ActivityModule;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.android.scopes.FragmentScoped;
import dagger.hilt.android.scopes.ServiceScoped;
import dagger.hilt.android.scopes.ViewModelScoped;
import dagger.hilt.android.scopes.ViewScoped;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedComponent;
import dagger.hilt.migration.DisableInstallInCheck;
import javax.annotation.processing.Generated;
import javax.inject.Singleton;

@Generated("dagger.hilt.processor.internal.root.RootProcessor")
public final class BaseApplication_HiltComponents {
  private BaseApplication_HiltComponents() {
  }

  @dagger.Module(
      subcomponents = ServiceC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ServiceCBuilderModule {
    @Binds
    ServiceComponentBuilder bind(ServiceC.Builder builder);
  }

  @dagger.Module(
      subcomponents = ActivityRetainedC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ActivityRetainedCBuilderModule {
    @Binds
    ActivityRetainedComponentBuilder bind(ActivityRetainedC.Builder builder);
  }

  @dagger.Module(
      subcomponents = ActivityC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ActivityCBuilderModule {
    @Binds
    ActivityComponentBuilder bind(ActivityC.Builder builder);
  }

  @dagger.Module(
      subcomponents = ViewModelC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewModelCBuilderModule {
    @Binds
    ViewModelComponentBuilder bind(ViewModelC.Builder builder);
  }

  @dagger.Module(
      subcomponents = ViewC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewCBuilderModule {
    @Binds
    ViewComponentBuilder bind(ViewC.Builder builder);
  }

  @dagger.Module(
      subcomponents = FragmentC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface FragmentCBuilderModule {
    @Binds
    FragmentComponentBuilder bind(FragmentC.Builder builder);
  }

  @dagger.Module(
      subcomponents = ViewWithFragmentC.class
  )
  @DisableInstallInCheck
  @Generated("dagger.hilt.processor.internal.root.RootProcessor")
  abstract interface ViewWithFragmentCBuilderModule {
    @Binds
    ViewWithFragmentComponentBuilder bind(ViewWithFragmentC.Builder builder);
  }

  @Component(
      modules = {
          ApplicationContextModule.class,
          ActivityRetainedCBuilderModule.class,
          ServiceCBuilderModule.class,
          HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule.class,
          Module.class,
          NativeLibModule.class
      }
  )
  @Singleton
  public abstract static class SingletonC implements BaseApplication_GeneratedInjector,
      AutoCheckoutReceiver_GeneratedInjector,
      FragmentGetContextFix.FragmentGetContextFixEntryPoint,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint,
      ServiceComponentManager.ServiceComponentBuilderEntryPoint,
      SingletonComponent,
      GeneratedComponent {
  }

  @Subcomponent
  @ServiceScoped
  public abstract static class ServiceC implements LocationService_GeneratedInjector,
      ServiceComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ServiceComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          AddClientViewModel_HiltModules.KeyModule.class,
          AddPictureViewModel_HiltModules.KeyModule.class,
          AddTaskViewModel_HiltModules.KeyModule.class,
          AttendanceViewModel_HiltModules.KeyModule.class,
          ActivityCBuilderModule.class,
          ViewModelCBuilderModule.class,
          ClientDirectionViewModel_HiltModules.KeyModule.class,
          ClientHomeViewModel_HiltModules.KeyModule.class,
          CreateProfileViewModel_HiltModules.KeyModule.class,
          EditClientViewModel_HiltModules.KeyModule.class,
          ForgotPasswordViewModel_HiltModules.KeyModule.class,
          HiltWrapper_ActivityRetainedComponentManager_LifecycleModule.class,
          HiltWrapper_SavedStateHandleModule.class,
          HolidaysViewModel_HiltModules.KeyModule.class,
          HomeViewModel_HiltModules.KeyModule.class,
          LeavesViewModel_HiltModules.KeyModule.class,
          LoginViewModel_HiltModules.KeyModule.class,
          MobileVerifiactionViewModel_HiltModules.KeyModule.class,
          NotificationViewModel_HiltModules.KeyModule.class,
          ResetPasswordViewModel_HiltModules.KeyModule.class,
          StartTaskViewModel_HiltModules.KeyModule.class,
          TaskHomeViewModel_HiltModules.KeyModule.class,
          UpdateProfileViewModel_HiltModules.KeyModule.class
      }
  )
  @ActivityRetainedScoped
  public abstract static class ActivityRetainedC implements ActivityRetainedComponent,
      ActivityComponentManager.ActivityComponentBuilderEntryPoint,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityRetainedComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          FragmentCBuilderModule.class,
          ViewCBuilderModule.class,
          HiltWrapper_ActivityModule.class,
          HiltWrapper_DefaultViewModelFactories_ActivityModule.class
      }
  )
  @ActivityScoped
  public abstract static class ActivityC implements CreateProfileActivity_GeneratedInjector,
      ForgotPasswordActivity_GeneratedInjector,
      EmailLoginActivity_GeneratedInjector,
      EmailPasswordActivity_GeneratedInjector,
      LoginOptionsActivity_GeneratedInjector,
      MainActivity_GeneratedInjector,
      ManuallyAddAddressActivity_GeneratedInjector,
      MapAddressActivity_GeneratedInjector,
      MapShowActivity_GeneratedInjector,
      MobileLoginActivity_GeneratedInjector,
      MobileOtpActivity_GeneratedInjector,
      ResetPasswordActivity_GeneratedInjector,
      ActivityComponent,
      DefaultViewModelFactories.ActivityEntryPoint,
      HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint,
      FragmentComponentManager.FragmentComponentBuilderEntryPoint,
      ViewComponentManager.ViewComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          AddClientViewModel_HiltModules.BindsModule.class,
          AddPictureViewModel_HiltModules.BindsModule.class,
          AddTaskViewModel_HiltModules.BindsModule.class,
          AttendanceViewModel_HiltModules.BindsModule.class,
          ClientDirectionViewModel_HiltModules.BindsModule.class,
          ClientHomeViewModel_HiltModules.BindsModule.class,
          CreateProfileViewModel_HiltModules.BindsModule.class,
          EditClientViewModel_HiltModules.BindsModule.class,
          ForgotPasswordViewModel_HiltModules.BindsModule.class,
          HiltWrapper_HiltViewModelFactory_ViewModelModule.class,
          HolidaysViewModel_HiltModules.BindsModule.class,
          HomeViewModel_HiltModules.BindsModule.class,
          LeavesViewModel_HiltModules.BindsModule.class,
          LoginViewModel_HiltModules.BindsModule.class,
          MobileVerifiactionViewModel_HiltModules.BindsModule.class,
          NotificationViewModel_HiltModules.BindsModule.class,
          ResetPasswordViewModel_HiltModules.BindsModule.class,
          StartTaskViewModel_HiltModules.BindsModule.class,
          TaskHomeViewModel_HiltModules.BindsModule.class,
          UpdateProfileViewModel_HiltModules.BindsModule.class
      }
  )
  @ViewModelScoped
  public abstract static class ViewModelC implements ViewModelComponent,
      HiltViewModelFactory.ViewModelFactoriesEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewModelComponentBuilder {
    }
  }

  @Subcomponent
  @ViewScoped
  public abstract static class ViewC implements ViewComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewComponentBuilder {
    }
  }

  @Subcomponent(
      modules = ViewWithFragmentCBuilderModule.class
  )
  @FragmentScoped
  public abstract static class FragmentC implements AttendanceFragment_GeneratedInjector,
      AddClientEditOptionFragment_GeneratedInjector,
      AddClientFragment_GeneratedInjector,
      ClientCompleteAddressFragment_GeneratedInjector,
      ClientAddressFragment_GeneratedInjector,
      ClientDirectionFragment_GeneratedInjector,
      ClientHomeFragment_GeneratedInjector,
      ClinetMapShowFragment_GeneratedInjector,
      ClientAddFinalFragment_GeneratedInjector,
      EditClientAddressFragment_GeneratedInjector,
      ClientEditUpdateMapFragment_GeneratedInjector,
      EditClientFragment_GeneratedInjector,
      UpdaeEditClientFragment_GeneratedInjector,
      HolidaysFragment_GeneratedInjector,
      HomeFragment_GeneratedInjector,
      LeavesFragment_GeneratedInjector,
      MapCurrentFragment_GeneratedInjector,
      NotificationFragment_GeneratedInjector,
      SettingsFragment_GeneratedInjector,
      SelectClientFragment_GeneratedInjector,
      StartTaskFragment_GeneratedInjector,
      StartTaskPicFragment_GeneratedInjector,
      AddPictureFragment_GeneratedInjector,
      AddTaskFragment_GeneratedInjector,
      TaskHomeFragment_GeneratedInjector,
      UpdateAddressFragment_GeneratedInjector,
      UpdateAddressMapFragment_GeneratedInjector,
      UpdateFullAddressFragment_GeneratedInjector,
      UpdateProfileFragment_GeneratedInjector,
      FragmentComponent,
      DefaultViewModelFactories.FragmentEntryPoint,
      ViewComponentManager.ViewWithFragmentComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends FragmentComponentBuilder {
    }
  }

  @Subcomponent
  @ViewScoped
  public abstract static class ViewWithFragmentC implements ViewWithFragmentComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewWithFragmentComponentBuilder {
    }
  }
}
