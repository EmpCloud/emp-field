package com.empcloud.empmonitor.data.remote.response.attendance;

import com.empcloud.empmonitor.utils.CommonDetailsDeserializer;
import com.google.gson.annotations.JsonAdapter;
import java.io.Serializable;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b+\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001B\u0091\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\r\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0010\u001a\u00020\u0005\u0012\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\u0006\u0010\u0016\u001a\u00020\u0017\u00a2\u0006\u0002\u0010\u0018J\t\u00100\u001a\u00020\u0003H\u00c6\u0003J\u0010\u00101\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\"J\u000b\u00102\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u00103\u001a\u00020\u0005H\u00c6\u0003J\u000f\u00104\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u00c6\u0003J\t\u00105\u001a\u00020\u0015H\u00c6\u0003J\t\u00106\u001a\u00020\u0017H\u00c6\u0003J\t\u00107\u001a\u00020\u0005H\u00c6\u0003J\t\u00108\u001a\u00020\u0005H\u00c6\u0003J\t\u00109\u001a\u00020\bH\u00c6\u0003J\u000b\u0010:\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010;\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010<\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010=\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0010\u0010>\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\"J\u00b8\u0001\u0010?\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00052\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u00c6\u0001\u00a2\u0006\u0002\u0010@J\u0013\u0010A\u001a\u00020\b2\b\u0010B\u001a\u0004\u0018\u00010CH\u00d6\u0003J\t\u0010D\u001a\u00020\u0005H\u00d6\u0001J\t\u0010E\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0013\u0010\n\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001aR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001aR\u0015\u0010\u000e\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010#\u001a\u0004\b!\u0010\"R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001aR\u0013\u0010\f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001aR\u0015\u0010\r\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010#\u001a\u0004\b&\u0010\"R\u0011\u0010\u0010\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001fR\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0016\u0010\u0016\u001a\u00020\u00178\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0016\u0010\u0014\u001a\u00020\u00158\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010-R\u0013\u0010\t\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\u001aR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\u001f\u00a8\u0006F"}, d2 = {"Lcom/empcloud/empmonitor/data/remote/response/attendance/AttendanceFullData;", "Ljava/io/Serializable;", "date", "", "status", "", "half_day", "day_off", "", "start_time", "end_time", "holiday_name", "leave_name", "leave_type", "half_day_status", "half_day_leave", "logged_duration", "min_hours", "", "Lcom/empcloud/empmonitor/data/remote/response/attendance/MinHourBody;", "open_request", "Lcom/empcloud/empmonitor/data/remote/response/attendance/AObjectDetails;", "open_attendance_request", "Lcom/empcloud/empmonitor/data/remote/response/attendance/BObjectDetails;", "(Ljava/lang/String;IIZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/String;ILjava/util/List;Lcom/empcloud/empmonitor/data/remote/response/attendance/AObjectDetails;Lcom/empcloud/empmonitor/data/remote/response/attendance/BObjectDetails;)V", "getDate", "()Ljava/lang/String;", "getDay_off", "()Z", "getEnd_time", "getHalf_day", "()I", "getHalf_day_leave", "getHalf_day_status", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getHoliday_name", "getLeave_name", "getLeave_type", "getLogged_duration", "getMin_hours", "()Ljava/util/List;", "getOpen_attendance_request", "()Lcom/empcloud/empmonitor/data/remote/response/attendance/BObjectDetails;", "getOpen_request", "()Lcom/empcloud/empmonitor/data/remote/response/attendance/AObjectDetails;", "getStart_time", "getStatus", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;IIZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/String;ILjava/util/List;Lcom/empcloud/empmonitor/data/remote/response/attendance/AObjectDetails;Lcom/empcloud/empmonitor/data/remote/response/attendance/BObjectDetails;)Lcom/empcloud/empmonitor/data/remote/response/attendance/AttendanceFullData;", "equals", "other", "", "hashCode", "toString", "app_debug"})
public final class AttendanceFullData implements java.io.Serializable {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String date = null;
    private final int status = 0;
    private final int half_day = 0;
    private final boolean day_off = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String start_time = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String end_time = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String holiday_name = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String leave_name = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer leave_type = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer half_day_status = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String half_day_leave = null;
    private final int logged_duration = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.empcloud.empmonitor.data.remote.response.attendance.MinHourBody> min_hours = null;
    @com.google.gson.annotations.JsonAdapter(value = com.empcloud.empmonitor.utils.CommonDetailsDeserializer.class)
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.data.remote.response.attendance.AObjectDetails open_request = null;
    @com.google.gson.annotations.JsonAdapter(value = com.empcloud.empmonitor.utils.CommonDetailsDeserializer.class)
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.data.remote.response.attendance.BObjectDetails open_attendance_request = null;
    
    public AttendanceFullData(@org.jetbrains.annotations.NotNull()
    java.lang.String date, int status, int half_day, boolean day_off, @org.jetbrains.annotations.Nullable()
    java.lang.String start_time, @org.jetbrains.annotations.Nullable()
    java.lang.String end_time, @org.jetbrains.annotations.Nullable()
    java.lang.String holiday_name, @org.jetbrains.annotations.Nullable()
    java.lang.String leave_name, @org.jetbrains.annotations.Nullable()
    java.lang.Integer leave_type, @org.jetbrains.annotations.Nullable()
    java.lang.Integer half_day_status, @org.jetbrains.annotations.Nullable()
    java.lang.String half_day_leave, int logged_duration, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.response.attendance.MinHourBody> min_hours, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.attendance.AObjectDetails open_request, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.attendance.BObjectDetails open_attendance_request) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDate() {
        return null;
    }
    
    public final int getStatus() {
        return 0;
    }
    
    public final int getHalf_day() {
        return 0;
    }
    
    public final boolean getDay_off() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getStart_time() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getEnd_time() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getHoliday_name() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getLeave_name() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getLeave_type() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getHalf_day_status() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getHalf_day_leave() {
        return null;
    }
    
    public final int getLogged_duration() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.empcloud.empmonitor.data.remote.response.attendance.MinHourBody> getMin_hours() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.remote.response.attendance.AObjectDetails getOpen_request() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.remote.response.attendance.BObjectDetails getOpen_attendance_request() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component11() {
        return null;
    }
    
    public final int component12() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.empcloud.empmonitor.data.remote.response.attendance.MinHourBody> component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.remote.response.attendance.AObjectDetails component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.remote.response.attendance.BObjectDetails component15() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.remote.response.attendance.AttendanceFullData copy(@org.jetbrains.annotations.NotNull()
    java.lang.String date, int status, int half_day, boolean day_off, @org.jetbrains.annotations.Nullable()
    java.lang.String start_time, @org.jetbrains.annotations.Nullable()
    java.lang.String end_time, @org.jetbrains.annotations.Nullable()
    java.lang.String holiday_name, @org.jetbrains.annotations.Nullable()
    java.lang.String leave_name, @org.jetbrains.annotations.Nullable()
    java.lang.Integer leave_type, @org.jetbrains.annotations.Nullable()
    java.lang.Integer half_day_status, @org.jetbrains.annotations.Nullable()
    java.lang.String half_day_leave, int logged_duration, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.response.attendance.MinHourBody> min_hours, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.attendance.AObjectDetails open_request, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.attendance.BObjectDetails open_attendance_request) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}