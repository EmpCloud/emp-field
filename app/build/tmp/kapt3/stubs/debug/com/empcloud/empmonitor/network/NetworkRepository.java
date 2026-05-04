package com.empcloud.empmonitor.network;

import com.empcloud.empmonitor.data.remote.request.LEAVES.LeavesRequestModel;
import com.empcloud.empmonitor.data.remote.request.attendance.AttendanceRequestModel;
import com.empcloud.empmonitor.data.remote.request.create_task.CreateTaskModel;
import com.empcloud.empmonitor.data.remote.request.createclient.CreateClientModel;
import com.empcloud.empmonitor.data.remote.request.createleave.CreateLeaveRequestModel;
import com.empcloud.empmonitor.data.remote.request.delete.DeleteLeaveRequest;
import com.empcloud.empmonitor.data.remote.request.edit_attendanc.EditAttendanceModel;
import com.empcloud.empmonitor.data.remote.request.editleave.EditLeaveRequestModel;
import com.empcloud.empmonitor.data.remote.request.filter_task.FilteerTaskModel;
import com.empcloud.empmonitor.data.remote.request.forgotpassword.ForgotPasswordDataModel;
import com.empcloud.empmonitor.data.remote.response.otpresponse.OtpResponseModel;
import com.empcloud.empmonitor.data.remote.request.login.LoginDataModel;
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel;
import com.empcloud.empmonitor.data.remote.request.mode_of_transport.ModeOFTransportModel;
import com.empcloud.empmonitor.data.remote.request.resetpassword.ResetPasswordModel;
import com.empcloud.empmonitor.data.remote.request.send_location.LocationList;
import com.empcloud.empmonitor.data.remote.request.send_location.SendLocationModel;
import com.empcloud.empmonitor.data.remote.request.update_client.UpdatecClientModel;
import com.empcloud.empmonitor.data.remote.request.update_reschedule.UpdateRescheduleModel;
import com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel;
import com.empcloud.empmonitor.data.remote.request.updateprofile.UpdateProfileModel;
import com.empcloud.empmonitor.data.remote.request.verify_email.VerifyEmailModel;
import com.empcloud.empmonitor.data.remote.request.verifymobile.VerifyMobileModel;
import com.empcloud.empmonitor.data.remote.response.attendance.AttendanceResponse;
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchResponse;
import com.empcloud.empmonitor.data.remote.response.create_task.CreateTaskResponse;
import com.empcloud.empmonitor.data.remote.response.createclient.CreateClientResponse;
import com.empcloud.empmonitor.data.remote.response.createlevae.CreateLeaveResponse;
import com.empcloud.empmonitor.data.remote.response.deleteleave.DeleteLeaveResponse;
import com.empcloud.empmonitor.data.remote.response.edit_attendance.EditAttendanceResponse;
import com.empcloud.empmonitor.data.remote.response.editleave.EditLeaveResponse;
import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponse;
import com.empcloud.empmonitor.data.remote.response.fetchattendacne.FetchAttendanceResponse;
import com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse;
import com.empcloud.empmonitor.data.remote.response.uploadprofile.UpdateProfileResponse;
import com.empcloud.empmonitor.data.remote.response.filter_task.FilterTaskResponse;
import com.empcloud.empmonitor.data.remote.response.get_leave_type.GetLeaveTypeResponse;
import com.empcloud.empmonitor.data.remote.response.holidays.HolidaysResponse;
import com.empcloud.empmonitor.data.remote.response.leaves.LeavesResponse;
import com.empcloud.empmonitor.data.remote.response.login.LoginResponse;
import com.empcloud.empmonitor.data.remote.response.markattendance.MarkAttendanceResponse;
import com.empcloud.empmonitor.data.remote.response.mobilelogin.MobileLoginResponse;
import com.empcloud.empmonitor.data.remote.response.mode_of_transport.ModeTransportResponse;
import com.empcloud.empmonitor.data.remote.response.notification.NotificationResponse;
import com.empcloud.empmonitor.data.remote.response.resetpassword.ResetPasswordResponse;
import com.empcloud.empmonitor.data.remote.response.send_location.SendLocationResponse;
import com.empcloud.empmonitor.data.remote.response.task_stage_selection.TaskStageSelectionResponse;
import com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsResponse;
import com.empcloud.empmonitor.data.remote.response.update_client.UpdateClientResponse;
import com.empcloud.empmonitor.data.remote.response.update_reschedue.UpdateResceduleResponse;
import com.empcloud.empmonitor.data.remote.response.update_task.UpdateTaskResponse;
import com.empcloud.empmonitor.data.remote.response.uploadprofile.UploadProfileResponse;
import com.empcloud.empmonitor.data.remote.response.url_response_pics.UrlPicsResponse;
import com.empcloud.empmonitor.data.remote.response.verify_email.VerifyEmailResponse;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.network.api_satatemanagement.GetResponse;
import kotlinx.coroutines.flow.Flow;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import java.io.File;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0094\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J*\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u000fJ*\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0014J*\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u0018H\u0086@\u00a2\u0006\u0002\u0010\u0019J*\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001c\u001a\u00020\u001dH\u0086@\u00a2\u0006\u0002\u0010\u001eJ*\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020 0\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010!\u001a\u00020\"H\u0086@\u00a2\u0006\u0002\u0010#J\"\u0010$\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0\t0\b2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010&J*\u0010\'\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020(0\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010)\u001a\u00020*H\u0086@\u00a2\u0006\u0002\u0010+J\"\u0010,\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020-0\t0\b2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010&J*\u0010.\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020-0\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010/\u001a\u000200H\u0086@\u00a2\u0006\u0002\u00101J\"\u00102\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002030\t0\b2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010&J*\u00104\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002050\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u00106\u001a\u000207H\u0086@\u00a2\u0006\u0002\u00108J*\u00109\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020:0\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010;\u001a\u00020<H\u0086@\u00a2\u0006\u0002\u0010=J\"\u0010>\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020?0\t0\b2\u0006\u0010@\u001a\u00020AH\u0086@\u00a2\u0006\u0002\u0010BJ\"\u0010C\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020D0\t0\b2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010&J\"\u0010E\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020F0\t0\b2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010&J\"\u0010G\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020H0\t0\b2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010&J*\u0010I\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020J0\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010K\u001a\u00020LH\u0086@\u00a2\u0006\u0002\u0010MJ\"\u0010N\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020O0\t0\b2\u0006\u0010P\u001a\u00020QH\u0086@\u00a2\u0006\u0002\u0010RJ*\u0010S\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020T0\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010U\u001a\u00020VH\u0086@\u00a2\u0006\u0002\u0010WJ\"\u0010X\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020Y0\t0\b2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010&J\"\u0010Z\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020[0\t0\b2\u0006\u0010\\\u001a\u00020]H\u0086@\u00a2\u0006\u0002\u0010^J\"\u0010_\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020`0\t0\b2\u0006\u0010a\u001a\u00020bH\u0086@\u00a2\u0006\u0002\u0010cJ\"\u0010d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020e0\t0\b2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010&J\"\u0010f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020g0\t0\b2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010&J*\u0010h\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020i0\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010j\u001a\u00020kH\u0086@\u00a2\u0006\u0002\u0010lJ*\u0010m\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020:0\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010;\u001a\u00020<H\u0086@\u00a2\u0006\u0002\u0010=J\"\u0010n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020o0\t0\b2\u0006\u0010p\u001a\u00020qH\u0086@\u00a2\u0006\u0002\u0010rJ0\u0010s\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020t0\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\f\u0010u\u001a\b\u0012\u0004\u0012\u00020w0vH\u0086@\u00a2\u0006\u0002\u0010xJ*\u0010y\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020z0\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010{\u001a\u00020|H\u0086@\u00a2\u0006\u0002\u0010}J*\u0010~\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u007f0\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010;\u001a\u00020<H\u0086@\u00a2\u0006\u0002\u0010=J8\u0010\u0080\u0001\u001a\u000f\u0012\u000b\u0012\t\u0012\u0005\u0012\u00030\u0081\u00010\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\u0007\u0010\u0082\u0001\u001a\u00020\f2\b\u0010\u0083\u0001\u001a\u00030\u0084\u0001H\u0086@\u00a2\u0006\u0003\u0010\u0085\u0001J/\u0010\u0086\u0001\u001a\u000f\u0012\u000b\u0012\t\u0012\u0005\u0012\u00030\u0087\u00010\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\u0088\u0001\u001a\u00030\u0089\u0001H\u0086@\u00a2\u0006\u0003\u0010\u008a\u0001J/\u0010\u008b\u0001\u001a\u000f\u0012\u000b\u0012\t\u0012\u0005\u0012\u00030\u008c\u00010\t0\b2\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\u008d\u0001\u001a\u00030\u008e\u0001H\u0086@\u00a2\u0006\u0003\u0010\u008f\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0090\u0001"}, d2 = {"Lcom/empcloud/empmonitor/network/NetworkRepository;", "", "service", "Lcom/empcloud/empmonitor/network/ApiService;", "(Lcom/empcloud/empmonitor/network/ApiService;)V", "getService", "()Lcom/empcloud/empmonitor/network/ApiService;", "createClientCall", "Lkotlinx/coroutines/flow/Flow;", "Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState;", "Lcom/empcloud/empmonitor/data/remote/response/createclient/CreateClientResponse;", "accessToken", "", "createClientModel", "Lcom/empcloud/empmonitor/data/remote/request/createclient/CreateClientModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/createclient/CreateClientModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createLeaveCall", "Lcom/empcloud/empmonitor/data/remote/response/createlevae/CreateLeaveResponse;", "createLeaveRequestModel", "Lcom/empcloud/empmonitor/data/remote/request/createleave/CreateLeaveRequestModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/createleave/CreateLeaveRequestModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createTaskCall", "Lcom/empcloud/empmonitor/data/remote/response/create_task/CreateTaskResponse;", "createTaskModel", "Lcom/empcloud/empmonitor/data/remote/request/create_task/CreateTaskModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/create_task/CreateTaskModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteLeaveCall", "Lcom/empcloud/empmonitor/data/remote/response/deleteleave/DeleteLeaveResponse;", "deleteLeaveRequest", "Lcom/empcloud/empmonitor/data/remote/request/delete/DeleteLeaveRequest;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/delete/DeleteLeaveRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "editLeaveCall", "Lcom/empcloud/empmonitor/data/remote/response/editleave/EditLeaveResponse;", "editLeaveRequestModel", "Lcom/empcloud/empmonitor/data/remote/request/editleave/EditLeaveRequestModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/editleave/EditLeaveRequestModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchClientCall", "Lcom/empcloud/empmonitor/data/remote/response/clientfetch/ClientFetchResponse;", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchEditAttendanceCall", "Lcom/empcloud/empmonitor/data/remote/response/edit_attendance/EditAttendanceResponse;", "editAttendanceModel", "Lcom/empcloud/empmonitor/data/remote/request/edit_attendanc/EditAttendanceModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/edit_attendanc/EditAttendanceModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchTaskCall", "Lcom/empcloud/empmonitor/data/remote/response/fetch_task/FetchTaskResponse;", "filterTaskCall", "filteerTaskModel", "Lcom/empcloud/empmonitor/data/remote/request/filter_task/FilteerTaskModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/filter_task/FilteerTaskModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAttendacenData", "Lcom/empcloud/empmonitor/data/remote/response/fetchattendacne/FetchAttendanceResponse;", "getAttendance", "Lcom/empcloud/empmonitor/data/remote/response/attendance/AttendanceResponse;", "attendanceRequestModel", "Lcom/empcloud/empmonitor/data/remote/request/attendance/AttendanceRequestModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/attendance/AttendanceRequestModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getClinetUploadProfile", "Lcom/empcloud/empmonitor/data/remote/response/uploadprofile/UploadProfileResponse;", "files", "Lokhttp3/MultipartBody$Part;", "(Ljava/lang/String;Lokhttp3/MultipartBody$Part;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getEmailVerify", "Lcom/empcloud/empmonitor/data/remote/response/verify_email/VerifyEmailResponse;", "verifyEmailModel", "Lcom/empcloud/empmonitor/data/remote/request/verify_email/VerifyEmailModel;", "(Lcom/empcloud/empmonitor/data/remote/request/verify_email/VerifyEmailModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getFetchProfile", "Lcom/empcloud/empmonitor/data/remote/response/fetchprofile/FetchProfileResponse;", "getHolidays", "Lcom/empcloud/empmonitor/data/remote/response/holidays/HolidaysResponse;", "getLeaveType", "Lcom/empcloud/empmonitor/data/remote/response/get_leave_type/GetLeaveTypeResponse;", "getLeaves", "Lcom/empcloud/empmonitor/data/remote/response/leaves/LeavesResponse;", "leavesRequestModel", "Lcom/empcloud/empmonitor/data/remote/request/LEAVES/LeavesRequestModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/LEAVES/LeavesRequestModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLoginData", "Lcom/empcloud/empmonitor/data/remote/response/login/LoginResponse;", "loginData", "Lcom/empcloud/empmonitor/data/remote/request/login/LoginDataModel;", "(Lcom/empcloud/empmonitor/data/remote/request/login/LoginDataModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMarkAttendanceData", "Lcom/empcloud/empmonitor/data/remote/response/markattendance/MarkAttendanceResponse;", "markAttendanceModel", "Lcom/empcloud/empmonitor/data/remote/request/mark_attendance/MarkAttendanceModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/mark_attendance/MarkAttendanceModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getNotification", "Lcom/empcloud/empmonitor/data/remote/response/notification/NotificationResponse;", "getOtp", "Lcom/empcloud/empmonitor/data/remote/response/otpresponse/OtpResponseModel;", "forgotPasswordDataModel", "Lcom/empcloud/empmonitor/data/remote/request/forgotpassword/ForgotPasswordDataModel;", "(Lcom/empcloud/empmonitor/data/remote/request/forgotpassword/ForgotPasswordDataModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getResetData", "Lcom/empcloud/empmonitor/data/remote/response/resetpassword/ResetPasswordResponse;", "resetPasswordModel", "Lcom/empcloud/empmonitor/data/remote/request/resetpassword/ResetPasswordModel;", "(Lcom/empcloud/empmonitor/data/remote/request/resetpassword/ResetPasswordModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTaskStageTags", "Lcom/empcloud/empmonitor/data/remote/response/task_stage_selection/TaskStageSelectionResponse;", "getTrackingSettings", "Lcom/empcloud/empmonitor/data/remote/response/tracking_settings/TrackingSettingsResponse;", "getUpdateProfile", "Lcom/empcloud/empmonitor/data/remote/response/uploadprofile/UpdateProfileResponse;", "updateProfileModel", "Lcom/empcloud/empmonitor/data/remote/request/updateprofile/UpdateProfileModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/updateprofile/UpdateProfileModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUploadProfile", "getVerifyData", "Lcom/empcloud/empmonitor/data/remote/response/mobilelogin/MobileLoginResponse;", "verifyMobileModel", "Lcom/empcloud/empmonitor/data/remote/request/verifymobile/VerifyMobileModel;", "(Lcom/empcloud/empmonitor/data/remote/request/verifymobile/VerifyMobileModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendLocationCall", "Lcom/empcloud/empmonitor/data/remote/response/send_location/SendLocationResponse;", "sendLocationModel", "", "Lcom/empcloud/empmonitor/data/remote/request/send_location/LocationList;", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendModeSelection", "Lcom/empcloud/empmonitor/data/remote/response/mode_of_transport/ModeTransportResponse;", "modeOFTransportModel", "Lcom/empcloud/empmonitor/data/remote/request/mode_of_transport/ModeOFTransportModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/mode_of_transport/ModeOFTransportModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendPicsToApi", "Lcom/empcloud/empmonitor/data/remote/response/url_response_pics/UrlPicsResponse;", "updateCLientCall", "Lcom/empcloud/empmonitor/data/remote/response/update_client/UpdateClientResponse;", "clientId", "updatecClientModel", "Lcom/empcloud/empmonitor/data/remote/request/update_client/UpdatecClientModel;", "(Ljava/lang/String;Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/update_client/UpdatecClientModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateRescheduleCall", "Lcom/empcloud/empmonitor/data/remote/response/update_reschedue/UpdateResceduleResponse;", "updateRescheduleModel", "Lcom/empcloud/empmonitor/data/remote/request/update_reschedule/UpdateRescheduleModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/update_reschedule/UpdateRescheduleModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTaskCall", "Lcom/empcloud/empmonitor/data/remote/response/update_task/UpdateTaskResponse;", "updateTaskModel", "Lcom/empcloud/empmonitor/data/remote/request/update_task/UpdateTaskModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/update_task/UpdateTaskModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class NetworkRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.network.ApiService service = null;
    
    @javax.inject.Inject()
    public NetworkRepository(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.network.ApiService service) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.network.ApiService getService() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getEmailVerify(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.verify_email.VerifyEmailModel verifyEmailModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.verify_email.VerifyEmailResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLoginData(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.login.LoginDataModel loginData, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.login.LoginResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getOtp(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.forgotpassword.ForgotPasswordDataModel forgotPasswordDataModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.otpresponse.OtpResponseModel>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getResetData(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.resetpassword.ResetPasswordModel resetPasswordModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.resetpassword.ResetPasswordResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getFetchProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getVerifyData(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.verifymobile.VerifyMobileModel verifyMobileModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.mobilelogin.MobileLoginResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getUpdateProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.updateprofile.UpdateProfileModel updateProfileModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.uploadprofile.UpdateProfileResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getMarkAttendanceData(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel markAttendanceModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.markattendance.MarkAttendanceResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAttendacenData(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.fetchattendacne.FetchAttendanceResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getUploadProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    okhttp3.MultipartBody.Part files, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.uploadprofile.UploadProfileResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getClinetUploadProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    okhttp3.MultipartBody.Part files, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.uploadprofile.UploadProfileResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getHolidays(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.holidays.HolidaysResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLeaves(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.LEAVES.LeavesRequestModel leavesRequestModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.leaves.LeavesResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAttendance(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.attendance.AttendanceRequestModel attendanceRequestModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.attendance.AttendanceResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createLeaveCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.createleave.CreateLeaveRequestModel createLeaveRequestModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.createlevae.CreateLeaveResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object editLeaveCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.editleave.EditLeaveRequestModel editLeaveRequestModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.editleave.EditLeaveResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteLeaveCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.delete.DeleteLeaveRequest deleteLeaveRequest, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.deleteleave.DeleteLeaveResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createClientCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.createclient.CreateClientModel createClientModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.createclient.CreateClientResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchClientCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchEditAttendanceCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.edit_attendanc.EditAttendanceModel editAttendanceModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.edit_attendance.EditAttendanceResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchTaskCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateTaskCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel updateTaskModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.update_task.UpdateTaskResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createTaskCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.create_task.CreateTaskModel createTaskModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.create_task.CreateTaskResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sendLocationCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.request.send_location.LocationList> sendLocationModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.send_location.SendLocationResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateRescheduleCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.update_reschedule.UpdateRescheduleModel updateRescheduleModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.update_reschedue.UpdateResceduleResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sendPicsToApi(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    okhttp3.MultipartBody.Part files, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.url_response_pics.UrlPicsResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object filterTaskCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.filter_task.FilteerTaskModel filteerTaskModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateCLientCall(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    java.lang.String clientId, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.update_client.UpdatecClientModel updatecClientModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.update_client.UpdateClientResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLeaveType(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.get_leave_type.GetLeaveTypeResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object sendModeSelection(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.mode_of_transport.ModeOFTransportModel modeOFTransportModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.mode_of_transport.ModeTransportResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.notification.NotificationResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTaskStageTags(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.task_stage_selection.TaskStageSelectionResponse>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTrackingSettings(@org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsResponse>>> $completion) {
        return null;
    }
}