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
import com.empcloud.empmonitor.data.remote.response.edit_attendance.EditAttendanceBody
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
import com.empcloud.empmonitor.data.remote.response.otpresponse.OtpResponseModel
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
import com.empcloud.empmonitor.utils.Constants
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query
import java.io.File

interface   ApiService {

    @POST(Constants.VERIFY_EMAIL)
    suspend fun verifyEmail(@Body verifyEmailModel: VerifyEmailModel): Response<VerifyEmailResponse>
    @POST(Constants.USER_LOGIN)
    suspend fun loginData(@Body loginDataModel: LoginDataModel): Response<LoginResponse>

    @POST(Constants.FORGOT_PASSWORD)
    suspend fun OtpRequest(@Body forgotPasswordDataModel: ForgotPasswordDataModel): Response<OtpResponseModel>

    @PUT(Constants.RESET_PASSWORD)
    suspend fun ResetPasswordRequest(@Body resetPasswordModel: ResetPasswordModel): Response<ResetPasswordResponse>

    @GET(Constants.FETCH_PROFILE)
    suspend fun FetchProfileRequest(@Header("x-access-token")   accessToken:String):Response<FetchProfileResponse>

    @POST(Constants.VERIFY_PHONE)
    suspend fun VerifyMobileNumber(@Body verifyMobileModel: VerifyMobileModel):Response<MobileLoginResponse>

    @POST(Constants.UPDATE_PROFILE)
    suspend fun updateProfile(@Header("x-access-token")   accessToken:String,@Body updateProfileModel: UpdateProfileModel):Response<UpdateProfileResponse>

    @POST(Constants.MARK_ATTENDANCE)
    suspend fun markAttendance(@Header("x-access-token")   accessToken:String,@Body markAttendanceModel: MarkAttendanceModel):Response<MarkAttendanceResponse>

    @GET(Constants.FETCH_ATTENDANCE)
    suspend fun fetchAttendacne(@Header("x-access-token")   accessToken:String):Response<FetchAttendanceResponse>

    @Multipart
    @POST(Constants.UPLOAD_PROFILE_IMAGE)
    suspend fun uploadProfileImg(@Header("x-access-token")   accessToken:String, @Part files:MultipartBody.Part):Response<UploadProfileResponse>

    @Multipart
    @POST(Constants.CLIENT_UPLOAD_PROFLE_IMAGE)
    suspend fun clientUploadProfileImg(@Header("x-access-token")   accessToken:String, @Part files:MultipartBody.Part):Response<UploadProfileResponse>

    @GET(Constants.HOLIDAT_FETCH)
    suspend fun fetchHoliday(@Header("x-access-token")   accessToken:String):Response<HolidaysResponse>

    @POST(Constants.FETCH_LEAVES)
    suspend fun fetchLeaves(@Header("x-access-token")   accessToken:String,@Body leavesRequestModel: LeavesRequestModel):Response<LeavesResponse>

    @POST(Constants.ATTENDANCE)
    suspend fun fetchAttendanceDetail(@Header("x-access-token")   accessToken:String,@Body attendanceRequestModel: AttendanceRequestModel): Response<AttendanceResponse>

    @POST(Constants.CREATE_LEAVE)
    suspend fun createLeave(@Header("x-access-token")   accessToken:String,@Body createLeaveRequestModel: CreateLeaveRequestModel): Response<CreateLeaveResponse>

    @POST(Constants.EDIT_LEAVE)
    suspend fun editLeave(@Header("x-access-token")   accessToken:String,@Body editLeaveRequestModel: EditLeaveRequestModel): Response<EditLeaveResponse>

    @POST(Constants.DELETE_LEAVE)
    suspend fun deleteLeave(@Header("x-access-token")   accessToken:String,@Body deleteLeaveRequest: DeleteLeaveRequest): Response<DeleteLeaveResponse>

    @POST(Constants.CREATE_CLIENT)
    suspend fun createClient(@Header("x-access-token")   accessToken:String,@Body createClientModel: CreateClientModel): Response<CreateClientResponse>

    @GET(Constants.FETCH_CLEINT)
    suspend fun FetchClient(@Header("x-access-token")   accessToken:String): Response<ClientFetchResponse>

    @POST(Constants.EDIT_ATTENDANCE)
    suspend fun EditAttendance(@Header("x-access-token")   accessToken:String,@Body editAttendanceModel: EditAttendanceModel): Response<EditAttendanceResponse>

    @GET(Constants.FETCH_TASK)
    suspend fun fetchTask(@Header("x-access-token")   accessToken:String): Response<FetchTaskResponse>

    @POST(Constants.UPDATE_TASK)
    suspend fun updateTask(@Header("x-access-token")   accessToken:String,@Body updateTaskModel: UpdateTaskModel):Response<UpdateTaskResponse>


    @POST(Constants.CREATE_TASK)
    suspend fun createTask(@Header("x-access-token")   accessToken:String,@Body createTaskModel: CreateTaskModel):Response<CreateTaskResponse>

    @POST(Constants.SEND_LOCATION)
    suspend fun sendLocation(@Header("x-access-token")   accessToken:String,@Body sendLocationModel: List<LocationList>):Response<SendLocationResponse>

    @PUT(Constants.UPDATE_RESCHEDULE)
    suspend fun updateReschedule(@Header("x-access-token")   accessToken:String,@Body updateRescheduleModel: UpdateRescheduleModel):Response<UpdateResceduleResponse>

    @Multipart
    @POST(Constants.UPDATE_TASK_FILES)
    suspend  fun getUrlFromApi(@Header("x-access-token")   accessToken:String,@Part files:MultipartBody.Part):Response<UrlPicsResponse>

    @POST(Constants.FILTER_TASK)
    suspend fun filterTaskCall(@Header("x-access-token")   accessToken:String,@Body filteerTaskModel: FilteerTaskModel):Response<FetchTaskResponse>

    @PUT(Constants.UPDATE_CLIENT)
    suspend fun updateClientCall(@Header("x-access-token")   accessToken:String,
                                 @Query("clientId") clientId:String,
                                 @Body updatecClientModel: UpdatecClientModel):Response<UpdateClientResponse>


    @POST(Constants.GET_LEAVE_TYPE)
    suspend fun getLeaveType(@Header("x-access-token") accessToken: String): Response<GetLeaveTypeResponse>


    @POST(Constants.MODE_TRANSPORT_SELECTION)
    suspend fun sendModeSelection(@Header("x-access-token") accessToken: String,@Body modeOFTransportModel: ModeOFTransportModel): Response<ModeTransportResponse>

    @GET(Constants.NOTIFICATION)
    suspend fun getNotification(@Header("x-access-token") accessToken: String):Response<NotificationResponse>

    @GET(Constants.GET_TASK_STATE_TAGS)
    suspend fun getTaskStageTags(@Header("x-access-token")accessToken: String):Response<TaskStageSelectionResponse>

    @GET(Constants.GET_TRACKING_SETTINGS)
    suspend fun getTrackingSettings(@Header("x-access-token") accessToken: String):Response<TrackingSettingsResponse>
}