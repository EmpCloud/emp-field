package com.empcloud.empmonitor.ui.fragment.leaves;

import androidx.lifecycle.ViewModel;
import com.empcloud.empmonitor.data.remote.request.LEAVES.LeavesRequestModel;
import com.empcloud.empmonitor.data.remote.request.createleave.CreateLeaveRequestModel;
import com.empcloud.empmonitor.data.remote.request.delete.DeleteLeaveRequest;
import com.empcloud.empmonitor.data.remote.request.editleave.EditLeaveRequestModel;
import com.empcloud.empmonitor.data.remote.response.createlevae.CreateLeaveResponse;
import com.empcloud.empmonitor.data.remote.response.deleteleave.DeleteLeaveResponse;
import com.empcloud.empmonitor.data.remote.response.editleave.EditLeaveResponse;
import com.empcloud.empmonitor.data.remote.response.get_leave_type.GetLeaveTypeResponse;
import com.empcloud.empmonitor.data.remote.response.leaves.LeavesResponse;
import com.empcloud.empmonitor.network.NetworkRepository;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$J\u0016\u0010%\u001a\u00020 2\u0006\u0010&\u001a\u00020\"2\u0006\u0010\'\u001a\u00020(J\u0016\u0010)\u001a\u00020 2\u0006\u0010&\u001a\u00020\"2\u0006\u0010*\u001a\u00020+J\u000e\u0010,\u001a\u00020 2\u0006\u0010&\u001a\u00020\"J\u0016\u0010-\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010.\u001a\u00020/R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0014R\u001d\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00070\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0014R\u001d\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u00070\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0014R\u001d\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0014R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001e\u00a8\u00060"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/leaves/LeavesViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/empcloud/empmonitor/network/NetworkRepository;", "(Lcom/empcloud/empmonitor/network/NetworkRepository;)V", "createLeaveFlow", "Lkotlinx/coroutines/channels/Channel;", "Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState;", "Lcom/empcloud/empmonitor/data/remote/response/createlevae/CreateLeaveResponse;", "deleteLeaveFlow", "Lcom/empcloud/empmonitor/data/remote/response/deleteleave/DeleteLeaveResponse;", "editLeaveFlow", "Lcom/empcloud/empmonitor/data/remote/response/editleave/EditLeaveResponse;", "getleaveTypeFlow", "Lcom/empcloud/empmonitor/data/remote/response/get_leave_type/GetLeaveTypeResponse;", "leavesDataFlow", "Lcom/empcloud/empmonitor/data/remote/response/leaves/LeavesResponse;", "observeCreateLeaveFlow", "Lkotlinx/coroutines/flow/Flow;", "getObserveCreateLeaveFlow", "()Lkotlinx/coroutines/flow/Flow;", "observeEditLeaveCall", "getObserveEditLeaveCall", "observeGetLeaveType", "getObserveGetLeaveType", "observeLeavesFlow", "getObserveLeavesFlow", "observedeleteLeaveCall", "getObservedeleteLeaveCall", "getRepository", "()Lcom/empcloud/empmonitor/network/NetworkRepository;", "invokeCreateLeave", "", "authToken", "", "createLeaveRequestModel", "Lcom/empcloud/empmonitor/data/remote/request/createleave/CreateLeaveRequestModel;", "invokeDeleteLeave", "accesToken", "deleteLeaveRequest", "Lcom/empcloud/empmonitor/data/remote/request/delete/DeleteLeaveRequest;", "invokeEditLeave", "editLeaveRequestModel", "Lcom/empcloud/empmonitor/data/remote/request/editleave/EditLeaveRequestModel;", "invokeGetLeaveType", "invokeLeaves", "leavesRequestModel", "Lcom/empcloud/empmonitor/data/remote/request/LEAVES/LeavesRequestModel;", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class LeavesViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.network.NetworkRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.leaves.LeavesResponse>> leavesDataFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.leaves.LeavesResponse>> observeLeavesFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.createlevae.CreateLeaveResponse>> createLeaveFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.createlevae.CreateLeaveResponse>> observeCreateLeaveFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.editleave.EditLeaveResponse>> editLeaveFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.editleave.EditLeaveResponse>> observeEditLeaveCall = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.deleteleave.DeleteLeaveResponse>> deleteLeaveFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.deleteleave.DeleteLeaveResponse>> observedeleteLeaveCall = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.get_leave_type.GetLeaveTypeResponse>> getleaveTypeFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.get_leave_type.GetLeaveTypeResponse>> observeGetLeaveType = null;
    
    @javax.inject.Inject()
    public LeavesViewModel(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.network.NetworkRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.network.NetworkRepository getRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.leaves.LeavesResponse>> getObserveLeavesFlow() {
        return null;
    }
    
    public final void invokeLeaves(@org.jetbrains.annotations.NotNull()
    java.lang.String authToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.LEAVES.LeavesRequestModel leavesRequestModel) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.createlevae.CreateLeaveResponse>> getObserveCreateLeaveFlow() {
        return null;
    }
    
    public final void invokeCreateLeave(@org.jetbrains.annotations.NotNull()
    java.lang.String authToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.createleave.CreateLeaveRequestModel createLeaveRequestModel) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.editleave.EditLeaveResponse>> getObserveEditLeaveCall() {
        return null;
    }
    
    public final void invokeEditLeave(@org.jetbrains.annotations.NotNull()
    java.lang.String accesToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.editleave.EditLeaveRequestModel editLeaveRequestModel) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.deleteleave.DeleteLeaveResponse>> getObservedeleteLeaveCall() {
        return null;
    }
    
    public final void invokeDeleteLeave(@org.jetbrains.annotations.NotNull()
    java.lang.String accesToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.delete.DeleteLeaveRequest deleteLeaveRequest) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.get_leave_type.GetLeaveTypeResponse>> getObserveGetLeaveType() {
        return null;
    }
    
    public final void invokeGetLeaveType(@org.jetbrains.annotations.NotNull()
    java.lang.String accesToken) {
    }
}