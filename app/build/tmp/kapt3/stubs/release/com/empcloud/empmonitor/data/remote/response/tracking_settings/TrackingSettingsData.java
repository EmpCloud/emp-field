package com.empcloud.empmonitor.data.remote.response.tracking_settings;

import java.io.Serializable;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b \n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001B\u007f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0006\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\u0006\u0012\u0006\u0010\f\u001a\u00020\u0006\u0012\u0006\u0010\r\u001a\u00020\u0003\u0012\u0006\u0010\u000e\u001a\u00020\u0006\u0012\u0006\u0010\u000f\u001a\u00020\u0006\u0012\u0006\u0010\u0010\u001a\u00020\u0006\u0012\u0006\u0010\u0011\u001a\u00020\u0006\u0012\u0010\b\u0002\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u0013\u00a2\u0006\u0002\u0010\u0015J\t\u0010$\u001a\u00020\u0003H\u00c6\u0003J\t\u0010%\u001a\u00020\u0006H\u00c6\u0003J\t\u0010&\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0006H\u00c6\u0003J\t\u0010(\u001a\u00020\u0006H\u00c6\u0003J\u0011\u0010)\u001a\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u0013H\u00c6\u0003J\t\u0010*\u001a\u00020\u0003H\u00c6\u0003J\t\u0010+\u001a\u00020\u0006H\u00c6\u0003J\t\u0010,\u001a\u00020\u0006H\u00c6\u0003J\t\u0010-\u001a\u00020\u0006H\u00c6\u0003J\t\u0010.\u001a\u00020\nH\u00c6\u0003J\t\u0010/\u001a\u00020\u0006H\u00c6\u0003J\t\u00100\u001a\u00020\u0006H\u00c6\u0003J\t\u00101\u001a\u00020\u0003H\u00c6\u0003J\u009d\u0001\u00102\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\u00062\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00062\b\b\u0002\u0010\u0010\u001a\u00020\u00062\b\b\u0002\u0010\u0011\u001a\u00020\u00062\u0010\b\u0002\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u0013H\u00c6\u0001J\u0013\u00103\u001a\u00020\n2\b\u00104\u001a\u0004\u0018\u000105H\u00d6\u0003J\t\u00106\u001a\u00020\u0006H\u00d6\u0001J\t\u00107\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0011\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u000f\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0017R\u0011\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0017R\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\f\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0017R\u0019\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u0017R\u0011\u0010\u0010\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0017R\u0011\u0010\u000e\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0017R\u0011\u0010\b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0017R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001bR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001bR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0017\u00a8\u00068"}, d2 = {"Lcom/empcloud/empmonitor/data/remote/response/tracking_settings/TrackingSettingsData;", "Ljava/io/Serializable;", "latitude", "", "longitude", "orgRadius", "", "isBioMetricEnabled", "isWebEnabled", "geoLogStatus", "", "currentFrequency", "currentRadius", "currentMode", "isMobileDeviceEnabled", "autoCheckInByMobile", "isGeoFencingOn", "autoCheckInByGeoFencing", "employeeLocations", "", "Lcom/empcloud/empmonitor/data/remote/response/tracking_settings/EmployeeLocationItem;", "(Ljava/lang/String;Ljava/lang/String;IIIZIILjava/lang/String;IIIILjava/util/List;)V", "getAutoCheckInByGeoFencing", "()I", "getAutoCheckInByMobile", "getCurrentFrequency", "getCurrentMode", "()Ljava/lang/String;", "getCurrentRadius", "getEmployeeLocations", "()Ljava/util/List;", "getGeoLogStatus", "()Z", "getLatitude", "getLongitude", "getOrgRadius", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "", "hashCode", "toString", "app_release"})
public final class TrackingSettingsData implements java.io.Serializable {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String latitude = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String longitude = null;
    private final int orgRadius = 0;
    private final int isBioMetricEnabled = 0;
    private final int isWebEnabled = 0;
    private final boolean geoLogStatus = false;
    private final int currentFrequency = 0;
    private final int currentRadius = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String currentMode = null;
    private final int isMobileDeviceEnabled = 0;
    private final int autoCheckInByMobile = 0;
    private final int isGeoFencingOn = 0;
    private final int autoCheckInByGeoFencing = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<com.empcloud.empmonitor.data.remote.response.tracking_settings.EmployeeLocationItem> employeeLocations = null;
    
    public TrackingSettingsData(@org.jetbrains.annotations.NotNull()
    java.lang.String latitude, @org.jetbrains.annotations.NotNull()
    java.lang.String longitude, int orgRadius, int isBioMetricEnabled, int isWebEnabled, boolean geoLogStatus, int currentFrequency, int currentRadius, @org.jetbrains.annotations.NotNull()
    java.lang.String currentMode, int isMobileDeviceEnabled, int autoCheckInByMobile, int isGeoFencingOn, int autoCheckInByGeoFencing, @org.jetbrains.annotations.Nullable()
    java.util.List<com.empcloud.empmonitor.data.remote.response.tracking_settings.EmployeeLocationItem> employeeLocations) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLatitude() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLongitude() {
        return null;
    }
    
    public final int getOrgRadius() {
        return 0;
    }
    
    public final int isBioMetricEnabled() {
        return 0;
    }
    
    public final int isWebEnabled() {
        return 0;
    }
    
    public final boolean getGeoLogStatus() {
        return false;
    }
    
    public final int getCurrentFrequency() {
        return 0;
    }
    
    public final int getCurrentRadius() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentMode() {
        return null;
    }
    
    public final int isMobileDeviceEnabled() {
        return 0;
    }
    
    public final int getAutoCheckInByMobile() {
        return 0;
    }
    
    public final int isGeoFencingOn() {
        return 0;
    }
    
    public final int getAutoCheckInByGeoFencing() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.empcloud.empmonitor.data.remote.response.tracking_settings.EmployeeLocationItem> getEmployeeLocations() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final int component10() {
        return 0;
    }
    
    public final int component11() {
        return 0;
    }
    
    public final int component12() {
        return 0;
    }
    
    public final int component13() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.empcloud.empmonitor.data.remote.response.tracking_settings.EmployeeLocationItem> component14() {
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
    
    public final boolean component6() {
        return false;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final int component8() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsData copy(@org.jetbrains.annotations.NotNull()
    java.lang.String latitude, @org.jetbrains.annotations.NotNull()
    java.lang.String longitude, int orgRadius, int isBioMetricEnabled, int isWebEnabled, boolean geoLogStatus, int currentFrequency, int currentRadius, @org.jetbrains.annotations.NotNull()
    java.lang.String currentMode, int isMobileDeviceEnabled, int autoCheckInByMobile, int isGeoFencingOn, int autoCheckInByGeoFencing, @org.jetbrains.annotations.Nullable()
    java.util.List<com.empcloud.empmonitor.data.remote.response.tracking_settings.EmployeeLocationItem> employeeLocations) {
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