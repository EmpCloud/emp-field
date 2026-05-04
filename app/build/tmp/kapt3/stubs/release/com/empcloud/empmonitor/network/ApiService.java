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
import com.empcloud.empmonitor.data.remote.response.edit_attendance.EditAttendanceBody;
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
import com.empcloud.empmonitor.data.remote.response.otpresponse.OtpResponseModel;
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
import com.empcloud.empmonitor.utils.Constants;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import java.io.File;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0088\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001e\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\fJ\u001e\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\fJ\u001e\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u00032\b\b\u0001\u0010\u0011\u001a\u00020\u0012H\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u001e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u00032\b\b\u0001\u0010\u0016\u001a\u00020\u0017H\u00a7@\u00a2\u0006\u0002\u0010\u0018J\u001e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00032\b\b\u0001\u0010\u001b\u001a\u00020\u001cH\u00a7@\u00a2\u0006\u0002\u0010\u001dJ(\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010 \u001a\u00020!H\u00a7@\u00a2\u0006\u0002\u0010\"J(\u0010#\u001a\b\u0012\u0004\u0012\u00020$0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010%\u001a\u00020&H\u00a7@\u00a2\u0006\u0002\u0010\'J(\u0010(\u001a\b\u0012\u0004\u0012\u00020)0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010*\u001a\u00020+H\u00a7@\u00a2\u0006\u0002\u0010,J(\u0010-\u001a\b\u0012\u0004\u0012\u00020.0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010/\u001a\u000200H\u00a7@\u00a2\u0006\u0002\u00101J(\u00102\u001a\b\u0012\u0004\u0012\u0002030\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u00104\u001a\u000205H\u00a7@\u00a2\u0006\u0002\u00106J(\u00107\u001a\b\u0012\u0004\u0012\u0002080\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u00109\u001a\u00020:H\u00a7@\u00a2\u0006\u0002\u0010;J\u001e\u0010<\u001a\b\u0012\u0004\u0012\u00020=0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\fJ(\u0010>\u001a\b\u0012\u0004\u0012\u00020?0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010@\u001a\u00020AH\u00a7@\u00a2\u0006\u0002\u0010BJ\u001e\u0010C\u001a\b\u0012\u0004\u0012\u00020D0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\fJ(\u0010E\u001a\b\u0012\u0004\u0012\u00020F0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010G\u001a\u00020HH\u00a7@\u00a2\u0006\u0002\u0010IJ\u001e\u0010J\u001a\b\u0012\u0004\u0012\u00020K0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\fJ(\u0010L\u001a\b\u0012\u0004\u0012\u00020K0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010M\u001a\u00020NH\u00a7@\u00a2\u0006\u0002\u0010OJ\u001e\u0010P\u001a\b\u0012\u0004\u0012\u00020Q0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\fJ\u001e\u0010R\u001a\b\u0012\u0004\u0012\u00020S0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\fJ\u001e\u0010T\u001a\b\u0012\u0004\u0012\u00020U0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\fJ\u001e\u0010V\u001a\b\u0012\u0004\u0012\u00020W0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\fJ(\u0010X\u001a\b\u0012\u0004\u0012\u00020Y0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010 \u001a\u00020!H\u00a7@\u00a2\u0006\u0002\u0010\"J\u001e\u0010Z\u001a\b\u0012\u0004\u0012\u00020[0\u00032\b\b\u0001\u0010\\\u001a\u00020]H\u00a7@\u00a2\u0006\u0002\u0010^J(\u0010_\u001a\b\u0012\u0004\u0012\u00020`0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010a\u001a\u00020bH\u00a7@\u00a2\u0006\u0002\u0010cJ.\u0010d\u001a\b\u0012\u0004\u0012\u00020e0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\u000e\b\u0001\u0010f\u001a\b\u0012\u0004\u0012\u00020h0gH\u00a7@\u00a2\u0006\u0002\u0010iJ(\u0010j\u001a\b\u0012\u0004\u0012\u00020k0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010l\u001a\u00020mH\u00a7@\u00a2\u0006\u0002\u0010nJ2\u0010o\u001a\b\u0012\u0004\u0012\u00020p0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010q\u001a\u00020\u00062\b\b\u0001\u0010r\u001a\u00020sH\u00a7@\u00a2\u0006\u0002\u0010tJ(\u0010u\u001a\b\u0012\u0004\u0012\u00020v0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010w\u001a\u00020xH\u00a7@\u00a2\u0006\u0002\u0010yJ(\u0010z\u001a\b\u0012\u0004\u0012\u00020{0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010|\u001a\u00020}H\u00a7@\u00a2\u0006\u0002\u0010~J,\u0010\u007f\u001a\t\u0012\u0005\u0012\u00030\u0080\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\n\b\u0001\u0010\u0081\u0001\u001a\u00030\u0082\u0001H\u00a7@\u00a2\u0006\u0003\u0010\u0083\u0001J)\u0010\u0084\u0001\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010 \u001a\u00020!H\u00a7@\u00a2\u0006\u0002\u0010\"J#\u0010\u0085\u0001\u001a\t\u0012\u0005\u0012\u00030\u0086\u00010\u00032\n\b\u0001\u0010\u0087\u0001\u001a\u00030\u0088\u0001H\u00a7@\u00a2\u0006\u0003\u0010\u0089\u0001\u00a8\u0006\u008a\u0001"}, d2 = {"Lcom/empcloud/empmonitor/network/ApiService;", "", "EditAttendance", "Lretrofit2/Response;", "Lcom/empcloud/empmonitor/data/remote/response/edit_attendance/EditAttendanceResponse;", "accessToken", "", "editAttendanceModel", "Lcom/empcloud/empmonitor/data/remote/request/edit_attendanc/EditAttendanceModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/edit_attendanc/EditAttendanceModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "FetchClient", "Lcom/empcloud/empmonitor/data/remote/response/clientfetch/ClientFetchResponse;", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "FetchProfileRequest", "Lcom/empcloud/empmonitor/data/remote/response/fetchprofile/FetchProfileResponse;", "OtpRequest", "Lcom/empcloud/empmonitor/data/remote/response/otpresponse/OtpResponseModel;", "forgotPasswordDataModel", "Lcom/empcloud/empmonitor/data/remote/request/forgotpassword/ForgotPasswordDataModel;", "(Lcom/empcloud/empmonitor/data/remote/request/forgotpassword/ForgotPasswordDataModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "ResetPasswordRequest", "Lcom/empcloud/empmonitor/data/remote/response/resetpassword/ResetPasswordResponse;", "resetPasswordModel", "Lcom/empcloud/empmonitor/data/remote/request/resetpassword/ResetPasswordModel;", "(Lcom/empcloud/empmonitor/data/remote/request/resetpassword/ResetPasswordModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "VerifyMobileNumber", "Lcom/empcloud/empmonitor/data/remote/response/mobilelogin/MobileLoginResponse;", "verifyMobileModel", "Lcom/empcloud/empmonitor/data/remote/request/verifymobile/VerifyMobileModel;", "(Lcom/empcloud/empmonitor/data/remote/request/verifymobile/VerifyMobileModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clientUploadProfileImg", "Lcom/empcloud/empmonitor/data/remote/response/uploadprofile/UploadProfileResponse;", "files", "Lokhttp3/MultipartBody$Part;", "(Ljava/lang/String;Lokhttp3/MultipartBody$Part;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createClient", "Lcom/empcloud/empmonitor/data/remote/response/createclient/CreateClientResponse;", "createClientModel", "Lcom/empcloud/empmonitor/data/remote/request/createclient/CreateClientModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/createclient/CreateClientModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createLeave", "Lcom/empcloud/empmonitor/data/remote/response/createlevae/CreateLeaveResponse;", "createLeaveRequestModel", "Lcom/empcloud/empmonitor/data/remote/request/createleave/CreateLeaveRequestModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/createleave/CreateLeaveRequestModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createTask", "Lcom/empcloud/empmonitor/data/remote/response/create_task/CreateTaskResponse;", "createTaskModel", "Lcom/empcloud/empmonitor/data/remote/request/create_task/CreateTaskModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/create_task/CreateTaskModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteLeave", "Lcom/empcloud/empmonitor/data/remote/response/deleteleave/DeleteLeaveResponse;", "deleteLeaveRequest", "Lcom/empcloud/empmonitor/data/remote/request/delete/DeleteLeaveRequest;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/delete/DeleteLeaveRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "editLeave", "Lcom/empcloud/empmonitor/data/remote/response/editleave/EditLeaveResponse;", "editLeaveRequestModel", "Lcom/empcloud/empmonitor/data/remote/request/editleave/EditLeaveRequestModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/editleave/EditLeaveRequestModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchAttendacne", "Lcom/empcloud/empmonitor/data/remote/response/fetchattendacne/FetchAttendanceResponse;", "fetchAttendanceDetail", "Lcom/empcloud/empmonitor/data/remote/response/attendance/AttendanceResponse;", "attendanceRequestModel", "Lcom/empcloud/empmonitor/data/remote/request/attendance/AttendanceRequestModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/attendance/AttendanceRequestModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchHoliday", "Lcom/empcloud/empmonitor/data/remote/response/holidays/HolidaysResponse;", "fetchLeaves", "Lcom/empcloud/empmonitor/data/remote/response/leaves/LeavesResponse;", "leavesRequestModel", "Lcom/empcloud/empmonitor/data/remote/request/LEAVES/LeavesRequestModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/LEAVES/LeavesRequestModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchTask", "Lcom/empcloud/empmonitor/data/remote/response/fetch_task/FetchTaskResponse;", "filterTaskCall", "filteerTaskModel", "Lcom/empcloud/empmonitor/data/remote/request/filter_task/FilteerTaskModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/filter_task/FilteerTaskModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLeaveType", "Lcom/empcloud/empmonitor/data/remote/response/get_leave_type/GetLeaveTypeResponse;", "getNotification", "Lcom/empcloud/empmonitor/data/remote/response/notification/NotificationResponse;", "getTaskStageTags", "Lcom/empcloud/empmonitor/data/remote/response/task_stage_selection/TaskStageSelectionResponse;", "getTrackingSettings", "Lcom/empcloud/empmonitor/data/remote/response/tracking_settings/TrackingSettingsResponse;", "getUrlFromApi", "Lcom/empcloud/empmonitor/data/remote/response/url_response_pics/UrlPicsResponse;", "loginData", "Lcom/empcloud/empmonitor/data/remote/response/login/LoginResponse;", "loginDataModel", "Lcom/empcloud/empmonitor/data/remote/request/login/LoginDataModel;", "(Lcom/empcloud/empmonitor/data/remote/request/login/LoginDataModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markAttendance", "Lcom/empcloud/empmonitor/data/remote/response/markattendance/MarkAttendanceResponse;", "markAttendanceModel", "Lcom/empcloud/empmonitor/data/remote/request/mark_attendance/MarkAttendanceModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/mark_attendance/MarkAttendanceModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendLocation", "Lcom/empcloud/empmonitor/data/remote/response/send_location/SendLocationResponse;", "sendLocationModel", "", "Lcom/empcloud/empmonitor/data/remote/request/send_location/LocationList;", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendModeSelection", "Lcom/empcloud/empmonitor/data/remote/response/mode_of_transport/ModeTransportResponse;", "modeOFTransportModel", "Lcom/empcloud/empmonitor/data/remote/request/mode_of_transport/ModeOFTransportModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/mode_of_transport/ModeOFTransportModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateClientCall", "Lcom/empcloud/empmonitor/data/remote/response/update_client/UpdateClientResponse;", "clientId", "updatecClientModel", "Lcom/empcloud/empmonitor/data/remote/request/update_client/UpdatecClientModel;", "(Ljava/lang/String;Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/update_client/UpdatecClientModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateProfile", "Lcom/empcloud/empmonitor/data/remote/response/uploadprofile/UpdateProfileResponse;", "updateProfileModel", "Lcom/empcloud/empmonitor/data/remote/request/updateprofile/UpdateProfileModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/updateprofile/UpdateProfileModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateReschedule", "Lcom/empcloud/empmonitor/data/remote/response/update_reschedue/UpdateResceduleResponse;", "updateRescheduleModel", "Lcom/empcloud/empmonitor/data/remote/request/update_reschedule/UpdateRescheduleModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/update_reschedule/UpdateRescheduleModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTask", "Lcom/empcloud/empmonitor/data/remote/response/update_task/UpdateTaskResponse;", "updateTaskModel", "Lcom/empcloud/empmonitor/data/remote/request/update_task/UpdateTaskModel;", "(Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/request/update_task/UpdateTaskModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "uploadProfileImg", "verifyEmail", "Lcom/empcloud/empmonitor/data/remote/response/verify_email/VerifyEmailResponse;", "verifyEmailModel", "Lcom/empcloud/empmonitor/data/remote/request/verify_email/VerifyEmailModel;", "(Lcom/empcloud/empmonitor/data/remote/request/verify_email/VerifyEmailModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
public abstract interface ApiService {
    
    @retrofit2.http.POST(value = "open-user/verify-email")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object verifyEmail(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.verify_email.VerifyEmailModel verifyEmailModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.verify_email.VerifyEmailResponse>> $completion);
    
    @retrofit2.http.POST(value = "open-user/user-login")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object loginData(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.login.LoginDataModel loginDataModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.login.LoginResponse>> $completion);
    
    @retrofit2.http.POST(value = "open-user/forgot-password")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object OtpRequest(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.forgotpassword.ForgotPasswordDataModel forgotPasswordDataModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.otpresponse.OtpResponseModel>> $completion);
    
    @retrofit2.http.PUT(value = "open-user/reset-password")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object ResetPasswordRequest(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.resetpassword.ResetPasswordModel resetPasswordModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.resetpassword.ResetPasswordResponse>> $completion);
    
    @retrofit2.http.GET(value = "profile/fetchProfile")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object FetchProfileRequest(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse>> $completion);
    
    @retrofit2.http.POST(value = "open-user/verify-phone")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object VerifyMobileNumber(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.verifymobile.VerifyMobileModel verifyMobileModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.mobilelogin.MobileLoginResponse>> $completion);
    
    @retrofit2.http.POST(value = "profile/updateProfile")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateProfile(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.updateprofile.UpdateProfileModel updateProfileModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.uploadprofile.UpdateProfileResponse>> $completion);
    
    @retrofit2.http.POST(value = "attendance/mark-attendance")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markAttendance(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel markAttendanceModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.markattendance.MarkAttendanceResponse>> $completion);
    
    @retrofit2.http.GET(value = "attendance/attendance")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchAttendacne(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.fetchattendacne.FetchAttendanceResponse>> $completion);
    
    @retrofit2.http.Multipart()
    @retrofit2.http.POST(value = "profile/uploadProfileImage")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object uploadProfileImg(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Part()
    @org.jetbrains.annotations.NotNull()
    okhttp3.MultipartBody.Part files, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.uploadprofile.UploadProfileResponse>> $completion);
    
    @retrofit2.http.Multipart()
    @retrofit2.http.POST(value = "client/clientUploadProfileImage")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clientUploadProfileImg(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Part()
    @org.jetbrains.annotations.NotNull()
    okhttp3.MultipartBody.Part files, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.uploadprofile.UploadProfileResponse>> $completion);
    
    @retrofit2.http.GET(value = "holiday/get-holiday")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchHoliday(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.holidays.HolidaysResponse>> $completion);
    
    @retrofit2.http.POST(value = "leaves/get-leaves")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchLeaves(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.LEAVES.LeavesRequestModel leavesRequestModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.leaves.LeavesResponse>> $completion);
    
    @retrofit2.http.POST(value = "attendance/fetch-attendance")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchAttendanceDetail(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.attendance.AttendanceRequestModel attendanceRequestModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.attendance.AttendanceResponse>> $completion);
    
    @retrofit2.http.POST(value = "leaves/create-leave")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createLeave(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.createleave.CreateLeaveRequestModel createLeaveRequestModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.createlevae.CreateLeaveResponse>> $completion);
    
    @retrofit2.http.POST(value = "leaves/update-leaves")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object editLeave(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.editleave.EditLeaveRequestModel editLeaveRequestModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.editleave.EditLeaveResponse>> $completion);
    
    @retrofit2.http.POST(value = "leaves/delete-leaves")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteLeave(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.delete.DeleteLeaveRequest deleteLeaveRequest, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.deleteleave.DeleteLeaveResponse>> $completion);
    
    @retrofit2.http.POST(value = "client/create")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createClient(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.createclient.CreateClientModel createClientModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.createclient.CreateClientResponse>> $completion);
    
    @retrofit2.http.GET(value = "client/fetch")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object FetchClient(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchResponse>> $completion);
    
    @retrofit2.http.POST(value = "attendance/attendance-request")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object EditAttendance(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.edit_attendanc.EditAttendanceModel editAttendanceModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.edit_attendance.EditAttendanceResponse>> $completion);
    
    @retrofit2.http.GET(value = "task/fetch")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchTask(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponse>> $completion);
    
    @retrofit2.http.POST(value = "task/update-taskStatus")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateTask(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel updateTaskModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.update_task.UpdateTaskResponse>> $completion);
    
    @retrofit2.http.POST(value = "task/create")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createTask(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.create_task.CreateTaskModel createTaskModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.create_task.CreateTaskResponse>> $completion);
    
    @retrofit2.http.POST(value = "track/get-location")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object sendLocation(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.request.send_location.LocationList> sendLocationModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.send_location.SendLocationResponse>> $completion);
    
    @retrofit2.http.PUT(value = "task/update")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateReschedule(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.update_reschedule.UpdateRescheduleModel updateRescheduleModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.update_reschedue.UpdateResceduleResponse>> $completion);
    
    @retrofit2.http.Multipart()
    @retrofit2.http.POST(value = "task/uploadTask-files")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUrlFromApi(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Part()
    @org.jetbrains.annotations.NotNull()
    okhttp3.MultipartBody.Part files, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.url_response_pics.UrlPicsResponse>> $completion);
    
    @retrofit2.http.POST(value = "task/filterTask")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object filterTaskCall(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.filter_task.FilteerTaskModel filteerTaskModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponse>> $completion);
    
    @retrofit2.http.PUT(value = "client/update")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateClientCall(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Query(value = "clientId")
    @org.jetbrains.annotations.NotNull()
    java.lang.String clientId, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.update_client.UpdatecClientModel updatecClientModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.update_client.UpdateClientResponse>> $completion);
    
    @retrofit2.http.POST(value = "leaves/fetch-leave-type")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLeaveType(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.get_leave_type.GetLeaveTypeResponse>> $completion);
    
    @retrofit2.http.POST(value = "profile/Update-Emp-mode-of-transport")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object sendModeSelection(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.mode_of_transport.ModeOFTransportModel modeOFTransportModel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.mode_of_transport.ModeTransportResponse>> $completion);
    
    @retrofit2.http.GET(value = "task/getNotification")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getNotification(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.notification.NotificationResponse>> $completion);
    
    @retrofit2.http.GET(value = "tags/getTags")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTaskStageTags(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.task_stage_selection.TaskStageSelectionResponse>> $completion);
    
    @retrofit2.http.GET(value = "open-user/get-tracking-settings")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTrackingSettings(@retrofit2.http.Header(value = "x-access-token")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accessToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsResponse>> $completion);
}