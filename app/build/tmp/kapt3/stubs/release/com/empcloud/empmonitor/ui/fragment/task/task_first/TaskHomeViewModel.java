package com.empcloud.empmonitor.ui.fragment.task.task_first;

import androidx.lifecycle.ViewModel;
import com.empcloud.empmonitor.data.remote.request.filter_task.FilteerTaskModel;
import com.empcloud.empmonitor.data.remote.request.update_reschedule.UpdateRescheduleModel;
import com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel;
import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponse;
import com.empcloud.empmonitor.data.remote.response.filter_task.FilterTaskResponse;
import com.empcloud.empmonitor.data.remote.response.update_reschedue.UpdateResceduleResponse;
import com.empcloud.empmonitor.data.remote.response.update_task.UpdateTaskResponse;
import com.empcloud.empmonitor.network.NetworkRepository;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dJ\u0016\u0010\u001e\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020 J\u0016\u0010!\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\"\u001a\u00020#J\u0016\u0010$\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010%\u001a\u00020&R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u001d\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00070\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\rR\u001d\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00070\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/task/task_first/TaskHomeViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/empcloud/empmonitor/network/NetworkRepository;", "(Lcom/empcloud/empmonitor/network/NetworkRepository;)V", "fetchTaskDataFlow", "Lkotlinx/coroutines/channels/Channel;", "Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState;", "Lcom/empcloud/empmonitor/data/remote/response/fetch_task/FetchTaskResponse;", "filterTaskFlow", "observeFetchTaskCall", "Lkotlinx/coroutines/flow/Flow;", "getObserveFetchTaskCall", "()Lkotlinx/coroutines/flow/Flow;", "observeFilterTaskCall", "getObserveFilterTaskCall", "observeUpdateRescheduleTaskFlow", "Lcom/empcloud/empmonitor/data/remote/response/update_reschedue/UpdateResceduleResponse;", "getObserveUpdateRescheduleTaskFlow", "observeUpdateTaskFlow", "Lcom/empcloud/empmonitor/data/remote/response/update_task/UpdateTaskResponse;", "getObserveUpdateTaskFlow", "getRepository", "()Lcom/empcloud/empmonitor/network/NetworkRepository;", "updateReschedleTaskDataFlow", "updateTaskDataFlow", "invokeFetchTask", "", "accessToken", "", "invokeFilterTask", "filteerTaskModel", "Lcom/empcloud/empmonitor/data/remote/request/filter_task/FilteerTaskModel;", "invokeRescheduleTaskCall", "updateRescheduleModel", "Lcom/empcloud/empmonitor/data/remote/request/update_reschedule/UpdateRescheduleModel;", "invokeUpdateTaskCall", "updateTaskModel", "Lcom/empcloud/empmonitor/data/remote/request/update_task/UpdateTaskModel;", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class TaskHomeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.network.NetworkRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponse>> fetchTaskDataFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponse>> observeFetchTaskCall = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.update_task.UpdateTaskResponse>> updateTaskDataFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.update_task.UpdateTaskResponse>> observeUpdateTaskFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponse>> filterTaskFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponse>> observeFilterTaskCall = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.update_reschedue.UpdateResceduleResponse>> updateReschedleTaskDataFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.update_reschedue.UpdateResceduleResponse>> observeUpdateRescheduleTaskFlow = null;
    
    @javax.inject.Inject()
    public TaskHomeViewModel(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.network.NetworkRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.network.NetworkRepository getRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponse>> getObserveFetchTaskCall() {
        return null;
    }
    
    public final void invokeFetchTask(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.update_task.UpdateTaskResponse>> getObserveUpdateTaskFlow() {
        return null;
    }
    
    public final void invokeUpdateTaskCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel updateTaskModel) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponse>> getObserveFilterTaskCall() {
        return null;
    }
    
    public final void invokeFilterTask(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.filter_task.FilteerTaskModel filteerTaskModel) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.update_reschedue.UpdateResceduleResponse>> getObserveUpdateRescheduleTaskFlow() {
        return null;
    }
    
    public final void invokeRescheduleTaskCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.update_reschedule.UpdateRescheduleModel updateRescheduleModel) {
    }
}