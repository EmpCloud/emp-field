package com.empcloud.empmonitor.network

import com.empcloud.empmonitor.data.remote.request.LEAVES.LeavesRequestModel
import com.empcloud.empmonitor.data.remote.request.attendance.AttendanceRequestModel
import com.empcloud.empmonitor.data.remote.request.create_task.CreateTaskModel
import com.empcloud.empmonitor.data.remote.request.createclient.CreateClientModel
import com.empcloud.empmonitor.data.remote.request.createleave.CreateLeaveRequestModel
import com.empcloud.empmonitor.data.remote.request.delete.DeleteLeaveRequest
import com.empcloud.empmonitor.data.remote.request.edit_attendanc.EditAttendanceModel
import com.empcloud.empmonitor.data.remote.request.editleave.EditLeaveRequestModel
import com.empcloud.empmonitor.data.remote.request.filter_task.FilteerTaskModel
import com.empcloud.empmonitor.data.remote.request.forgotpassword.ForgotPasswordDataModel
import com.empcloud.empmonitor.data.remote.response.otpresponse.OtpResponseModel
import com.empcloud.empmonitor.data.remote.request.login.LoginDataModel
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel
import com.empcloud.empmonitor.data.remote.request.mode_of_transport.ModeOFTransportModel
import com.empcloud.empmonitor.data.remote.request.resetpassword.ResetPasswordModel
import com.empcloud.empmonitor.data.remote.request.send_location.LocationList
import com.empcloud.empmonitor.data.remote.request.send_location.SendLocationModel
import com.empcloud.empmonitor.data.remote.request.update_client.UpdatecClientModel
import com.empcloud.empmonitor.data.remote.request.update_reschedule.UpdateRescheduleModel
import com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel
import com.empcloud.empmonitor.data.remote.request.updateprofile.UpdateProfileModel
import com.empcloud.empmonitor.data.remote.request.verify_email.VerifyEmailModel
import com.empcloud.empmonitor.data.remote.request.verifymobile.VerifyMobileModel
import com.empcloud.empmonitor.data.remote.response.attendance.AttendanceResponse
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchResponse
import com.empcloud.empmonitor.data.remote.response.create_task.CreateTaskResponse
import com.empcloud.empmonitor.data.remote.response.createclient.CreateClientResponse
import com.empcloud.empmonitor.data.remote.response.createlevae.CreateLeaveResponse
import com.empcloud.empmonitor.data.remote.response.deleteleave.DeleteLeaveResponse
import com.empcloud.empmonitor.data.remote.response.edit_attendance.EditAttendanceResponse
import com.empcloud.empmonitor.data.remote.response.editleave.EditLeaveResponse
import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponse
import com.empcloud.empmonitor.data.remote.response.fetchattendacne.FetchAttendanceResponse
import com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse
import com.empcloud.empmonitor.data.remote.response.uploadprofile.UpdateProfileResponse
import com.empcloud.empmonitor.data.remote.response.filter_task.FilterTaskResponse
import com.empcloud.empmonitor.data.remote.response.get_leave_type.GetLeaveTypeResponse
import com.empcloud.empmonitor.data.remote.response.holidays.HolidaysResponse
import com.empcloud.empmonitor.data.remote.response.leaves.LeavesResponse
import com.empcloud.empmonitor.data.remote.response.login.LoginResponse
import com.empcloud.empmonitor.data.remote.response.markattendance.MarkAttendanceResponse
import com.empcloud.empmonitor.data.remote.response.mobilelogin.MobileLoginResponse
import com.empcloud.empmonitor.data.remote.response.mode_of_transport.ModeTransportResponse
import com.empcloud.empmonitor.data.remote.response.notification.NotificationResponse
import com.empcloud.empmonitor.data.remote.response.resetpassword.ResetPasswordResponse
import com.empcloud.empmonitor.data.remote.response.send_location.SendLocationResponse
import com.empcloud.empmonitor.data.remote.response.task_stage_selection.TaskStageSelectionResponse
import com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsResponse
import com.empcloud.empmonitor.data.remote.response.update_client.UpdateClientResponse
import com.empcloud.empmonitor.data.remote.response.update_reschedue.UpdateResceduleResponse
import com.empcloud.empmonitor.data.remote.response.update_task.UpdateTaskResponse
import com.empcloud.empmonitor.data.remote.response.uploadprofile.UploadProfileResponse
import com.empcloud.empmonitor.data.remote.response.url_response_pics.UrlPicsResponse
import com.empcloud.empmonitor.data.remote.response.verify_email.VerifyEmailResponse
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.network.api_satatemanagement.GetResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import java.io.File
import javax.inject.Inject

class NetworkRepository  @Inject constructor(val service: ApiService){

    suspend fun getEmailVerify(verifyEmailModel: VerifyEmailModel): Flow<ApiState<VerifyEmailResponse>> {
        return GetResponse.fromFlow {
            service.verifyEmail(verifyEmailModel)
        }
    }
    suspend fun getLoginData(loginData: LoginDataModel): Flow<ApiState<LoginResponse>> {
        return GetResponse.fromFlow {
            service.loginData(loginData)
        }
    }

    suspend fun getOtp(forgotPasswordDataModel: ForgotPasswordDataModel): Flow<ApiState<OtpResponseModel>>{
        return  GetResponse.fromFlow {
            service.OtpRequest(forgotPasswordDataModel)
        }
    }

    suspend fun getResetData(resetPasswordModel: ResetPasswordModel): Flow<ApiState<ResetPasswordResponse>>{
        return  GetResponse.fromFlow {
            service.ResetPasswordRequest(resetPasswordModel)
        }
    }

    suspend fun getFetchProfile(accessToken:String):Flow<ApiState<FetchProfileResponse>>{
        return GetResponse.fromFlow {
            service.FetchProfileRequest(accessToken)
        }
    }

    suspend fun getVerifyData(verifyMobileModel: VerifyMobileModel):Flow<ApiState<MobileLoginResponse>>{
        return GetResponse.fromFlow {
            service.VerifyMobileNumber(verifyMobileModel)
        }
    }

    suspend fun getUpdateProfile(accessToken:String,updateProfileModel: UpdateProfileModel):Flow<ApiState<UpdateProfileResponse>>{
        return GetResponse.fromFlow {
            service.updateProfile(accessToken,updateProfileModel)
        }
    }

    suspend fun getMarkAttendanceData(accessToken:String,markAttendanceModel: MarkAttendanceModel):Flow<ApiState<MarkAttendanceResponse>>{
        return GetResponse.fromFlow {
            service.markAttendance(accessToken,markAttendanceModel)
        }
    }

    suspend fun getAttendacenData(accessToken:String):Flow<ApiState<FetchAttendanceResponse>>{
        return GetResponse.fromFlow {
            service.fetchAttendacne(accessToken)
        }
    }

    suspend fun getUploadProfile(accessToken:String,files:MultipartBody.Part):Flow<ApiState<UploadProfileResponse>>{
        return GetResponse.fromFlow {
            service.uploadProfileImg(accessToken,files)
        }
    }

    suspend fun getClinetUploadProfile(accessToken:String,files:MultipartBody.Part):Flow<ApiState<UploadProfileResponse>>{
        return GetResponse.fromFlow {
            service.clientUploadProfileImg(accessToken,files)
        }
    }

    suspend fun getHolidays(accessToken:String):Flow<ApiState<HolidaysResponse>>{
        return GetResponse.fromFlow {
            service.fetchHoliday(accessToken)
        }
    }

    suspend fun getLeaves(accessToken:String,leavesRequestModel: LeavesRequestModel):Flow<ApiState<LeavesResponse>>{
        return GetResponse.fromFlow {
            service.fetchLeaves(accessToken,leavesRequestModel)
        }
    }

    suspend fun getAttendance(accessToken:String,attendanceRequestModel: AttendanceRequestModel):Flow<ApiState<AttendanceResponse>>{
        return GetResponse.fromFlow {

            service.fetchAttendanceDetail(accessToken,attendanceRequestModel)
        }
    }

    suspend fun createLeaveCall( accessToken:String, createLeaveRequestModel: CreateLeaveRequestModel): Flow<ApiState<CreateLeaveResponse>>{
        return GetResponse.fromFlow {
            service.createLeave(accessToken,createLeaveRequestModel)
        }
    }

    suspend fun editLeaveCall( accessToken:String, editLeaveRequestModel: EditLeaveRequestModel): Flow<ApiState<EditLeaveResponse>>{
        return GetResponse.fromFlow {
            service.editLeave(accessToken,editLeaveRequestModel)
        }
    }

    suspend fun deleteLeaveCall( accessToken:String, deleteLeaveRequest: DeleteLeaveRequest): Flow<ApiState<DeleteLeaveResponse>>{
        return GetResponse.fromFlow {
            service.deleteLeave(accessToken,deleteLeaveRequest)
        }
    }

    suspend fun createClientCall( accessToken:String,createClientModel: CreateClientModel): Flow<ApiState<CreateClientResponse>>{
        return GetResponse.fromFlow {
            service.createClient(accessToken,createClientModel)
        }
    }

    suspend fun fetchClientCall( accessToken:String): Flow<ApiState<ClientFetchResponse>>{
        return GetResponse.fromFlow {
            service.FetchClient(accessToken)
        }
    }

    suspend fun fetchEditAttendanceCall( accessToken:String,editAttendanceModel: EditAttendanceModel): Flow<ApiState<EditAttendanceResponse>>{
        return GetResponse.fromFlow {
            service.EditAttendance(accessToken,editAttendanceModel)
        }
    }

    suspend fun fetchTaskCall( accessToken:String): Flow<ApiState<FetchTaskResponse>>{
        return GetResponse.fromFlow {
            service.fetchTask(accessToken)
        }
    }

    suspend fun updateTaskCall( accessToken:String,updateTaskModel: UpdateTaskModel): Flow<ApiState<UpdateTaskResponse>>{
        return GetResponse.fromFlow {
            service.updateTask(accessToken,updateTaskModel)
        }
    }

    suspend fun createTaskCall( accessToken:String,createTaskModel: CreateTaskModel): Flow<ApiState<CreateTaskResponse>>{
        return GetResponse.fromFlow {
            service.createTask(accessToken,createTaskModel)
        }
    }

    suspend  fun sendLocationCall( accessToken:String,sendLocationModel: List<LocationList>): Flow<ApiState<SendLocationResponse>>{
        return GetResponse.fromFlow {
            service.sendLocation(accessToken,sendLocationModel)
        }
    }

    suspend fun updateRescheduleCall( accessToken:String,updateRescheduleModel: UpdateRescheduleModel): Flow<ApiState<UpdateResceduleResponse>>{
        return GetResponse.fromFlow {
            service.updateReschedule(accessToken,updateRescheduleModel)
        }
    }

    suspend fun sendPicsToApi( accessToken:String,files:MultipartBody.Part): Flow<ApiState<UrlPicsResponse>>{
        return GetResponse.fromFlow {
            service.getUrlFromApi(accessToken,files)
        }
    }

    suspend fun filterTaskCall( accessToken:String,filteerTaskModel: FilteerTaskModel): Flow<ApiState<FetchTaskResponse>>{
        return GetResponse.fromFlow {
            service.filterTaskCall(accessToken,filteerTaskModel)
        }
    }

   suspend fun updateCLientCall(accessToken: String,clientId:String,updatecClientModel: UpdatecClientModel):Flow<ApiState<UpdateClientResponse>>{
       return GetResponse.fromFlow {
           service.updateClientCall(accessToken,clientId,updatecClientModel)
       }
   }

    suspend fun getLeaveType(accessToken: String):Flow<ApiState<GetLeaveTypeResponse>>{
        return GetResponse.fromFlow {
            service.getLeaveType(accessToken)
        }
    }

    suspend fun sendModeSelection(accessToken: String,modeOFTransportModel: ModeOFTransportModel):Flow<ApiState<ModeTransportResponse>>{
        return GetResponse.fromFlow {
            service.sendModeSelection(accessToken,modeOFTransportModel)
        }
    }

    suspend fun getNotification(accessToken: String):Flow<ApiState<NotificationResponse>>{
        return GetResponse.fromFlow {
            service.getNotification(accessToken)
        }
    }

    suspend fun getTaskStageTags(accessToken: String):Flow<ApiState<TaskStageSelectionResponse>>{
        return GetResponse.fromFlow {
            service.getTaskStageTags(accessToken)
        }
    }

    suspend fun getTrackingSettings(accessToken: String):Flow<ApiState<TrackingSettingsResponse>>{
        return GetResponse.fromFlow {
            service.getTrackingSettings(accessToken)
        }
    }
}