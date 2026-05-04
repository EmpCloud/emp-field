package com.empcloud.empmonitor.data.remote.response.fetchattendacne;

import com.empcloud.empmonitor.data.remote.response.markattendance.AttInnerData;
import java.io.Serializable;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b&\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001B\u008f\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0006\u0012\u0006\u0010\t\u001a\u00020\u0006\u0012\u0006\u0010\n\u001a\u00020\u0006\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u0006\u0012\u0006\u0010\u000e\u001a\u00020\u0003\u0012\u0006\u0010\u000f\u001a\u00020\u0003\u0012\u0006\u0010\u0010\u001a\u00020\u0003\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0003\u0012\u0010\b\u0002\u0010\u0016\u001a\n\u0012\u0004\u0012\u00020\u0018\u0018\u00010\u0017\u00a2\u0006\u0002\u0010\u0019J\t\u0010,\u001a\u00020\u0003H\u00c6\u0003J\t\u0010-\u001a\u00020\u0003H\u00c6\u0003J\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\t\u0010/\u001a\u00020\u0003H\u00c6\u0003J\t\u00100\u001a\u00020\u0012H\u00c6\u0003J\t\u00101\u001a\u00020\u0014H\u00c6\u0003J\t\u00102\u001a\u00020\u0003H\u00c6\u0003J\u0011\u00103\u001a\n\u0012\u0004\u0012\u00020\u0018\u0018\u00010\u0017H\u00c6\u0003J\t\u00104\u001a\u00020\u0003H\u00c6\u0003J\t\u00105\u001a\u00020\u0006H\u00c6\u0003J\t\u00106\u001a\u00020\u0006H\u00c6\u0003J\t\u00107\u001a\u00020\u0006H\u00c6\u0003J\t\u00108\u001a\u00020\u0006H\u00c6\u0003J\t\u00109\u001a\u00020\u0006H\u00c6\u0003J\t\u0010:\u001a\u00020\fH\u00c6\u0003J\t\u0010;\u001a\u00020\u0006H\u00c6\u0003J\u00b1\u0001\u0010<\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\n\u001a\u00020\u00062\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u00062\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u00032\u0010\b\u0002\u0010\u0016\u001a\n\u0012\u0004\u0012\u00020\u0018\u0018\u00010\u0017H\u00c6\u0001J\u0013\u0010=\u001a\u00020\u00142\b\u0010>\u001a\u0004\u0018\u00010?H\u00d6\u0003J\t\u0010@\u001a\u00020\u0006H\u00d6\u0001J\t\u0010A\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u0015\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\n\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001bR\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0019\u0010\u0016\u001a\n\u0012\u0004\u0012\u00020\u0018\u0018\u00010\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0011\u0010\u0013\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010\b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u001bR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u001bR\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u001bR\u0011\u0010\r\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u001bR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010&R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001dR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001dR\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001dR\u0011\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001dR\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001d\u00a8\u0006B"}, d2 = {"Lcom/empcloud/empmonitor/data/remote/response/fetchattendacne/AttendanceDataResponse;", "Ljava/io/Serializable;", "orglatitude", "", "orglongitude", "isGeoFencingOn", "", "isMobileDeviceEnabled", "isBioMetricEnabled", "currentFrequency", "currentRadius", "orgRadius", "", "isWebEnabled", "yesterdayDist", "yesterdaytask", "yesterdayHrs", "data", "Lcom/empcloud/empmonitor/data/remote/response/fetchattendacne/AttendanceListData;", "geoLogStatus", "", "currentMode", "employeeLocations", "", "Lcom/empcloud/empmonitor/data/remote/response/fetchattendacne/EmployeeLocation;", "(Ljava/lang/String;Ljava/lang/String;IIIIIFILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/empcloud/empmonitor/data/remote/response/fetchattendacne/AttendanceListData;ZLjava/lang/String;Ljava/util/List;)V", "getCurrentFrequency", "()I", "getCurrentMode", "()Ljava/lang/String;", "getCurrentRadius", "getData", "()Lcom/empcloud/empmonitor/data/remote/response/fetchattendacne/AttendanceListData;", "getEmployeeLocations", "()Ljava/util/List;", "getGeoLogStatus", "()Z", "getOrgRadius", "()F", "getOrglatitude", "getOrglongitude", "getYesterdayDist", "getYesterdayHrs", "getYesterdaytask", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "", "hashCode", "toString", "app_debug"})
public final class AttendanceDataResponse implements java.io.Serializable {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String orglatitude = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String orglongitude = null;
    private final int isGeoFencingOn = 0;
    private final int isMobileDeviceEnabled = 0;
    private final int isBioMetricEnabled = 0;
    private final int currentFrequency = 0;
    private final int currentRadius = 0;
    private final float orgRadius = 0.0F;
    private final int isWebEnabled = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String yesterdayDist = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String yesterdaytask = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String yesterdayHrs = null;
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.data.remote.response.fetchattendacne.AttendanceListData data = null;
    private final boolean geoLogStatus = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String currentMode = null;
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<com.empcloud.empmonitor.data.remote.response.fetchattendacne.EmployeeLocation> employeeLocations = null;
    
    public AttendanceDataResponse(@org.jetbrains.annotations.NotNull()
    java.lang.String orglatitude, @org.jetbrains.annotations.NotNull()
    java.lang.String orglongitude, int isGeoFencingOn, int isMobileDeviceEnabled, int isBioMetricEnabled, int currentFrequency, int currentRadius, float orgRadius, int isWebEnabled, @org.jetbrains.annotations.NotNull()
    java.lang.String yesterdayDist, @org.jetbrains.annotations.NotNull()
    java.lang.String yesterdaytask, @org.jetbrains.annotations.NotNull()
    java.lang.String yesterdayHrs, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.fetchattendacne.AttendanceListData data, boolean geoLogStatus, @org.jetbrains.annotations.NotNull()
    java.lang.String currentMode, @org.jetbrains.annotations.Nullable()
    java.util.List<com.empcloud.empmonitor.data.remote.response.fetchattendacne.EmployeeLocation> employeeLocations) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOrglatitude() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOrglongitude() {
        return null;
    }
    
    public final int isGeoFencingOn() {
        return 0;
    }
    
    public final int isMobileDeviceEnabled() {
        return 0;
    }
    
    public final int isBioMetricEnabled() {
        return 0;
    }
    
    public final int getCurrentFrequency() {
        return 0;
    }
    
    public final int getCurrentRadius() {
        return 0;
    }
    
    public final float getOrgRadius() {
        return 0.0F;
    }
    
    public final int isWebEnabled() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getYesterdayDist() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getYesterdaytask() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getYesterdayHrs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.remote.response.fetchattendacne.AttendanceListData getData() {
        return null;
    }
    
    public final boolean getGeoLogStatus() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentMode() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.empcloud.empmonitor.data.remote.response.fetchattendacne.EmployeeLocation> getEmployeeLocations() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.remote.response.fetchattendacne.AttendanceListData component13() {
        return null;
    }
    
    public final boolean component14() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.empcloud.empmonitor.data.remote.response.fetchattendacne.EmployeeLocation> component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final int component5() {
        return 0;
    }
    
    public final int component6() {
        return 0;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final float component8() {
        return 0.0F;
    }
    
    public final int component9() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.remote.response.fetchattendacne.AttendanceDataResponse copy(@org.jetbrains.annotations.NotNull()
    java.lang.String orglatitude, @org.jetbrains.annotations.NotNull()
    java.lang.String orglongitude, int isGeoFencingOn, int isMobileDeviceEnabled, int isBioMetricEnabled, int currentFrequency, int currentRadius, float orgRadius, int isWebEnabled, @org.jetbrains.annotations.NotNull()
    java.lang.String yesterdayDist, @org.jetbrains.annotations.NotNull()
    java.lang.String yesterdaytask, @org.jetbrains.annotations.NotNull()
    java.lang.String yesterdayHrs, @org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.response.fetchattendacne.AttendanceListData data, boolean geoLogStatus, @org.jetbrains.annotations.NotNull()
    java.lang.String currentMode, @org.jetbrains.annotations.Nullable()
    java.util.List<com.empcloud.empmonitor.data.remote.response.fetchattendacne.EmployeeLocation> employeeLocations) {
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