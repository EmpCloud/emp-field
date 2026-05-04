package com.empcloud.empmonitor.ui.fragment.update_profile;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.empcloud.empmonitor.data.remote.request.updateprofile.UpdateProfileModel;
import com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse;
import com.empcloud.empmonitor.data.remote.response.uploadprofile.UpdateProfileResponse;
import com.empcloud.empmonitor.data.remote.response.uploadprofile.UploadProfileResponse;
import com.empcloud.empmonitor.network.NetworkRepository;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MultipartBody;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aJ\u0016\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001dJ\u0016\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001f\u001a\u00020 J\u0016\u0010!\u001a\u00020\u00182\u0006\u0010\"\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001dR\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\nR\u001a\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u001a\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00070\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/update_profile/UpdateProfileViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/empcloud/empmonitor/network/NetworkRepository;", "(Lcom/empcloud/empmonitor/network/NetworkRepository;)V", "observeImageData", "Lkotlinx/coroutines/flow/Flow;", "Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState;", "Lcom/empcloud/empmonitor/data/remote/response/uploadprofile/UploadProfileResponse;", "getObserveImageData", "()Lkotlinx/coroutines/flow/Flow;", "observerProfileData", "Lcom/empcloud/empmonitor/data/remote/response/fetchprofile/FetchProfileResponse;", "getObserverProfileData", "observerUpdataData", "Lcom/empcloud/empmonitor/data/remote/response/uploadprofile/UpdateProfileResponse;", "getObserverUpdataData", "profileDataFlow", "Lkotlinx/coroutines/channels/Channel;", "profileImgFlow", "getRepository", "()Lcom/empcloud/empmonitor/network/NetworkRepository;", "updateDataFlow", "invokeFetchProfileCall", "", "authToken", "", "invokeUpdataDataCall", "updateProfileModel", "Lcom/empcloud/empmonitor/data/remote/request/updateprofile/UpdateProfileModel;", "invokeUpdateImage", "files", "Lokhttp3/MultipartBody$Part;", "uploadData", "accesToken", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class UpdateProfileViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.network.NetworkRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse>> profileDataFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse>> observerProfileData = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.uploadprofile.UpdateProfileResponse>> updateDataFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.uploadprofile.UpdateProfileResponse>> observerUpdataData = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.uploadprofile.UploadProfileResponse>> profileImgFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.uploadprofile.UploadProfileResponse>> observeImageData = null;
    
    @javax.inject.Inject()
    public UpdateProfileViewModel(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.network.NetworkRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.network.NetworkRepository getRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse>> getObserverProfileData() {
        return null;
    }
    
    public final void invokeFetchProfileCall(@org.jetbrains.annotations.NotNull()
    java.lang.String authToken) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.uploadprofile.UpdateProfileResponse>> getObserverUpdataData() {
        return null;
    }
    
    public final void invokeUpdataDataCall(@org.jetbrains.annotations.NotNull()
    java.lang.String authToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.updateprofile.UpdateProfileModel updateProfileModel) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.uploadprofile.UploadProfileResponse>> getObserveImageData() {
        return null;
    }
    
    public final void invokeUpdateImage(@org.jetbrains.annotations.NotNull()
    java.lang.String authToken, @org.jetbrains.annotations.NotNull()
    okhttp3.MultipartBody.Part files) {
    }
    
    public final void uploadData(@org.jetbrains.annotations.NotNull()
    java.lang.String accesToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.updateprofile.UpdateProfileModel updateProfileModel) {
    }
}