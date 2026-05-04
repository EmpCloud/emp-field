package com.empcloud.empmonitor.data.remote.response.login;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b%\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\bS\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001B\u00cf\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\b\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u0012\u0006\u0010\f\u001a\u00020\u0003\u0012\u0006\u0010\r\u001a\u00020\u0003\u0012\u0006\u0010\u000e\u001a\u00020\u0003\u0012\u0006\u0010\u000f\u001a\u00020\u0003\u0012\u0006\u0010\u0010\u001a\u00020\u0003\u0012\u0006\u0010\u0011\u001a\u00020\u0003\u0012\u0006\u0010\u0012\u001a\u00020\u0003\u0012\u0006\u0010\u0013\u001a\u00020\u0003\u0012\u0006\u0010\u0014\u001a\u00020\u0003\u0012\u0006\u0010\u0015\u001a\u00020\u0003\u0012\u0006\u0010\u0016\u001a\u00020\u0003\u0012\u0006\u0010\u0017\u001a\u00020\u0003\u0012\u0006\u0010\u0018\u001a\u00020\u0003\u0012\u0006\u0010\u0019\u001a\u00020\u0003\u0012\u0006\u0010\u001a\u001a\u00020\u0003\u0012\u0006\u0010\u001b\u001a\u00020\u0003\u0012\u0006\u0010\u001c\u001a\u00020\u0003\u0012\u0006\u0010\u001d\u001a\u00020\u0003\u0012\u0006\u0010\u001e\u001a\u00020\u0003\u0012\u0006\u0010\u001f\u001a\u00020\u0003\u0012\u0006\u0010 \u001a\u00020\u0003\u0012\u0006\u0010!\u001a\u00020\u0003\u0012\u0006\u0010\"\u001a\u00020\u0003\u0012\u0006\u0010#\u001a\u00020\u0003\u0012\u0006\u0010$\u001a\u00020\u0003\u0012\u0006\u0010%\u001a\u00020\u0003\u0012\u0006\u0010&\u001a\u00020\u0003\u0012\u0006\u0010\'\u001a\u00020\u0003\u0012\u0006\u0010(\u001a\u00020)\u0012\u0006\u0010*\u001a\u00020\u0003\u0012\u0006\u0010+\u001a\u00020\u0003\u0012\u0006\u0010,\u001a\u00020-\u00a2\u0006\u0002\u0010.J\t\u0010U\u001a\u00020\u0003H\u00c6\u0003J\t\u0010V\u001a\u00020\u0003H\u00c6\u0003J\t\u0010W\u001a\u00020\u0003H\u00c6\u0003J\t\u0010X\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Y\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Z\u001a\u00020\u0003H\u00c6\u0003J\t\u0010[\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\\\u001a\u00020\u0003H\u00c6\u0003J\t\u0010]\u001a\u00020\u0003H\u00c6\u0003J\t\u0010^\u001a\u00020\u0003H\u00c6\u0003J\t\u0010_\u001a\u00020\u0003H\u00c6\u0003J\t\u0010`\u001a\u00020\u0003H\u00c6\u0003J\t\u0010a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010c\u001a\u00020\u0003H\u00c6\u0003J\t\u0010d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010g\u001a\u00020\u0003H\u00c6\u0003J\t\u0010h\u001a\u00020\u0003H\u00c6\u0003J\t\u0010i\u001a\u00020\u0003H\u00c6\u0003J\t\u0010j\u001a\u00020\u0003H\u00c6\u0003J\t\u0010k\u001a\u00020\u0003H\u00c6\u0003J\t\u0010l\u001a\u00020\u0003H\u00c6\u0003J\t\u0010m\u001a\u00020\u0003H\u00c6\u0003J\t\u0010n\u001a\u00020\u0003H\u00c6\u0003J\t\u0010o\u001a\u00020\u0003H\u00c6\u0003J\t\u0010p\u001a\u00020\u0003H\u00c6\u0003J\t\u0010q\u001a\u00020\u0003H\u00c6\u0003J\t\u0010r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010s\u001a\u00020\u0003H\u00c6\u0003J\t\u0010t\u001a\u00020)H\u00c6\u0003J\t\u0010u\u001a\u00020\u0003H\u00c6\u0003J\t\u0010v\u001a\u00020\u0003H\u00c6\u0003J\t\u0010w\u001a\u00020\u0003H\u00c6\u0003J\t\u0010x\u001a\u00020-H\u00c6\u0003J\t\u0010y\u001a\u00020\u0003H\u00c6\u0003J\t\u0010z\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010{\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010|\u001a\u00020\u0003H\u00c6\u0003J\t\u0010}\u001a\u00020\u0003H\u00c6\u0003J\u00a5\u0003\u0010~\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00032\b\b\u0002\u0010\u0016\u001a\u00020\u00032\b\b\u0002\u0010\u0017\u001a\u00020\u00032\b\b\u0002\u0010\u0018\u001a\u00020\u00032\b\b\u0002\u0010\u0019\u001a\u00020\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u00032\b\b\u0002\u0010\u001b\u001a\u00020\u00032\b\b\u0002\u0010\u001c\u001a\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\u00032\b\b\u0002\u0010\u001e\u001a\u00020\u00032\b\b\u0002\u0010\u001f\u001a\u00020\u00032\b\b\u0002\u0010 \u001a\u00020\u00032\b\b\u0002\u0010!\u001a\u00020\u00032\b\b\u0002\u0010\"\u001a\u00020\u00032\b\b\u0002\u0010#\u001a\u00020\u00032\b\b\u0002\u0010$\u001a\u00020\u00032\b\b\u0002\u0010%\u001a\u00020\u00032\b\b\u0002\u0010&\u001a\u00020\u00032\b\b\u0002\u0010\'\u001a\u00020\u00032\b\b\u0002\u0010(\u001a\u00020)2\b\b\u0002\u0010*\u001a\u00020\u00032\b\b\u0002\u0010+\u001a\u00020\u00032\b\b\u0002\u0010,\u001a\u00020-H\u00c6\u0001J\u0015\u0010\u007f\u001a\u00020-2\n\u0010\u0080\u0001\u001a\u0005\u0018\u00010\u0081\u0001H\u00d6\u0003J\n\u0010\u0082\u0001\u001a\u00020)H\u00d6\u0001J\n\u0010\u0083\u0001\u001a\u00020\u0003H\u00d6\u0001R\u0016\u0010\u000f\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u0016\u0010\u0005\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u00100R\u0016\u0010\u0010\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u00100R\u0016\u0010\u0012\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u00100R\u0016\u0010\u0014\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u00100R\u0016\u0010%\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u00100R\u0016\u0010\u0007\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u00100R\u0016\u0010 \u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u00100R\u0016\u0010\u001f\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u00100R\u0016\u0010\u0019\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u00100R\u0016\u0010\u001c\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u00100R\u0011\u0010\'\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u00100R\u0016\u0010\"\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u00100R\u0016\u0010#\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u00100R\u0016\u0010\u0004\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u00100R\u0016\u0010\u0006\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u00100R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u00100R\u0016\u0010\u001a\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u00100R\u0016\u0010\f\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u00100R\u0011\u0010*\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u00100R\u0011\u0010,\u001a\u00020-\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010BR\u0011\u0010+\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u00100R\u0016\u0010\u001b\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u00100R\u0016\u0010\r\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u00100R\u0016\u0010\u0017\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u00100R\u0016\u0010\u001d\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u00100R\u0016\u0010\u001e\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bE\u00100R\u0016\u0010\u000e\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bF\u00100R\u0016\u0010\b\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u00100R\u0016\u0010$\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bH\u00100R\u0016\u0010\u0015\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u00100R\u0016\u0010\u0018\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bJ\u00100R\u0018\u0010\t\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bK\u00100R\u0016\u0010\u000b\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bL\u00100R\u0016\u0010\u0011\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bM\u00100R\u0016\u0010\u0016\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bN\u00100R\u0016\u0010&\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bO\u00100R\u0016\u0010\n\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bP\u00100R\u0016\u0010(\u001a\u00020)8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bQ\u0010RR\u0016\u0010!\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bS\u00100R\u0016\u0010\u0013\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bT\u00100\u00a8\u0006\u0084\u0001"}, d2 = {"Lcom/empcloud/empmonitor/data/remote/response/login/UserData;", "Ljava/io/Serializable;", "id", "", "fullName", "age", "gender", "email", "password", "profilePic", "userType", "role", "isEmpMonitorAdmin", "isTwoFactorEnabled", "orgId", "address", "city", "state", "country", "zipCode", "countryCode", "phoneNumber", "timezone", "language", "phoneVerified", "emailVerified", "invitation", "isSuspended", "empMonitor", "numberValidateOtp", "numberValidateOtpExpire", "emailValidateToken", "emailTokenExpire", "verificationEmailSentCount", "forgotPasswordToken", "forgotTokenExpire", "passwordEmailSentCount", "createdAt", "updatedAt", "emp_id", "v", "", "isGeoFencingOn", "isMobileDeviceEnabled", "isGlobalUser", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Z)V", "getAddress", "()Ljava/lang/String;", "getAge", "getCity", "getCountry", "getCountryCode", "getCreatedAt", "getEmail", "getEmailTokenExpire", "getEmailValidateToken", "getEmailVerified", "getEmpMonitor", "getEmp_id", "getForgotPasswordToken", "getForgotTokenExpire", "getFullName", "getGender", "getId", "getInvitation", "()Z", "getLanguage", "getNumberValidateOtp", "getNumberValidateOtpExpire", "getOrgId", "getPassword", "getPasswordEmailSentCount", "getPhoneNumber", "getPhoneVerified", "getProfilePic", "getRole", "getState", "getTimezone", "getUpdatedAt", "getUserType", "getV", "()I", "getVerificationEmailSentCount", "getZipCode", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component29", "component3", "component30", "component31", "component32", "component33", "component34", "component35", "component36", "component37", "component38", "component39", "component4", "component40", "component41", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "", "hashCode", "toString", "app_debug"})
public final class UserData implements java.io.Serializable {
    @com.google.gson.annotations.SerializedName(value = "id")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    @com.google.gson.annotations.SerializedName(value = "fullName")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String fullName = null;
    @com.google.gson.annotations.SerializedName(value = "age")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String age = null;
    @com.google.gson.annotations.SerializedName(value = "gender")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String gender = null;
    @com.google.gson.annotations.SerializedName(value = "email")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String email = null;
    @com.google.gson.annotations.SerializedName(value = "password")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String password = null;
    @com.google.gson.annotations.SerializedName(value = "profilePic")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String profilePic = null;
    @com.google.gson.annotations.SerializedName(value = "userType")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String userType = null;
    @com.google.gson.annotations.SerializedName(value = "role")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String role = null;
    @com.google.gson.annotations.SerializedName(value = "isEmpMonitorAdmin")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String isEmpMonitorAdmin = null;
    @com.google.gson.annotations.SerializedName(value = "isTwoFactorEnabled")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String isTwoFactorEnabled = null;
    @com.google.gson.annotations.SerializedName(value = "orgId")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String orgId = null;
    @com.google.gson.annotations.SerializedName(value = "address")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String address = null;
    @com.google.gson.annotations.SerializedName(value = "city")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String city = null;
    @com.google.gson.annotations.SerializedName(value = "state")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String state = null;
    @com.google.gson.annotations.SerializedName(value = "country")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String country = null;
    @com.google.gson.annotations.SerializedName(value = "zipCode")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String zipCode = null;
    @com.google.gson.annotations.SerializedName(value = "countryCode")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String countryCode = null;
    @com.google.gson.annotations.SerializedName(value = "phoneNumber")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String phoneNumber = null;
    @com.google.gson.annotations.SerializedName(value = "timezone")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String timezone = null;
    @com.google.gson.annotations.SerializedName(value = "language")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String language = null;
    @com.google.gson.annotations.SerializedName(value = "phoneVerified")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String phoneVerified = null;
    @com.google.gson.annotations.SerializedName(value = "emailVerified")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String emailVerified = null;
    @com.google.gson.annotations.SerializedName(value = "invitation")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String invitation = null;
    @com.google.gson.annotations.SerializedName(value = "isSuspended")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String isSuspended = null;
    @com.google.gson.annotations.SerializedName(value = "empMonitor")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String empMonitor = null;
    @com.google.gson.annotations.SerializedName(value = "numberValidateOtp")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String numberValidateOtp = null;
    @com.google.gson.annotations.SerializedName(value = "numberValidateOtpExpire")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String numberValidateOtpExpire = null;
    @com.google.gson.annotations.SerializedName(value = "emailValidateToken")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String emailValidateToken = null;
    @com.google.gson.annotations.SerializedName(value = "emailTokenExpire")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String emailTokenExpire = null;
    @com.google.gson.annotations.SerializedName(value = "verificationEmailSentCount")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String verificationEmailSentCount = null;
    @com.google.gson.annotations.SerializedName(value = "forgotPasswordToken")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String forgotPasswordToken = null;
    @com.google.gson.annotations.SerializedName(value = "forgotTokenExpire")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String forgotTokenExpire = null;
    @com.google.gson.annotations.SerializedName(value = "passwordEmailSentCount")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String passwordEmailSentCount = null;
    @com.google.gson.annotations.SerializedName(value = "createdAt")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String createdAt = null;
    @com.google.gson.annotations.SerializedName(value = "updatedAt")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String updatedAt = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String emp_id = null;
    @com.google.gson.annotations.SerializedName(value = "__v")
    private final int v = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String isGeoFencingOn = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String isMobileDeviceEnabled = null;
    private final boolean isGlobalUser = false;
    
    public UserData(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String fullName, @org.jetbrains.annotations.NotNull()
    java.lang.String age, @org.jetbrains.annotations.NotNull()
    java.lang.String gender, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.Nullable()
    java.lang.String profilePic, @org.jetbrains.annotations.NotNull()
    java.lang.String userType, @org.jetbrains.annotations.NotNull()
    java.lang.String role, @org.jetbrains.annotations.NotNull()
    java.lang.String isEmpMonitorAdmin, @org.jetbrains.annotations.NotNull()
    java.lang.String isTwoFactorEnabled, @org.jetbrains.annotations.NotNull()
    java.lang.String orgId, @org.jetbrains.annotations.NotNull()
    java.lang.String address, @org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String state, @org.jetbrains.annotations.NotNull()
    java.lang.String country, @org.jetbrains.annotations.NotNull()
    java.lang.String zipCode, @org.jetbrains.annotations.NotNull()
    java.lang.String countryCode, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String timezone, @org.jetbrains.annotations.NotNull()
    java.lang.String language, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneVerified, @org.jetbrains.annotations.NotNull()
    java.lang.String emailVerified, @org.jetbrains.annotations.NotNull()
    java.lang.String invitation, @org.jetbrains.annotations.NotNull()
    java.lang.String isSuspended, @org.jetbrains.annotations.NotNull()
    java.lang.String empMonitor, @org.jetbrains.annotations.NotNull()
    java.lang.String numberValidateOtp, @org.jetbrains.annotations.NotNull()
    java.lang.String numberValidateOtpExpire, @org.jetbrains.annotations.NotNull()
    java.lang.String emailValidateToken, @org.jetbrains.annotations.NotNull()
    java.lang.String emailTokenExpire, @org.jetbrains.annotations.NotNull()
    java.lang.String verificationEmailSentCount, @org.jetbrains.annotations.NotNull()
    java.lang.String forgotPasswordToken, @org.jetbrains.annotations.NotNull()
    java.lang.String forgotTokenExpire, @org.jetbrains.annotations.NotNull()
    java.lang.String passwordEmailSentCount, @org.jetbrains.annotations.NotNull()
    java.lang.String createdAt, @org.jetbrains.annotations.NotNull()
    java.lang.String updatedAt, @org.jetbrains.annotations.NotNull()
    java.lang.String emp_id, int v, @org.jetbrains.annotations.NotNull()
    java.lang.String isGeoFencingOn, @org.jetbrains.annotations.NotNull()
    java.lang.String isMobileDeviceEnabled, boolean isGlobalUser) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFullName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAge() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getGender() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmail() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPassword() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getProfilePic() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUserType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRole() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String isEmpMonitorAdmin() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String isTwoFactorEnabled() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOrgId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAddress() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCity() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCountry() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getZipCode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCountryCode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPhoneNumber() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTimezone() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLanguage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPhoneVerified() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmailVerified() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getInvitation() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String isSuspended() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmpMonitor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNumberValidateOtp() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNumberValidateOtpExpire() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmailValidateToken() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmailTokenExpire() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getVerificationEmailSentCount() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getForgotPasswordToken() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getForgotTokenExpire() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPasswordEmailSentCount() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCreatedAt() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUpdatedAt() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmp_id() {
        return null;
    }
    
    public final int getV() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String isGeoFencingOn() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String isMobileDeviceEnabled() {
        return null;
    }
    
    public final boolean isGlobalUser() {
        return false;
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
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component17() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component18() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component19() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component20() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component21() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component22() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component23() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component24() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component25() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component26() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component27() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component28() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component29() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component30() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component31() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component32() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component33() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component34() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component35() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component36() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component37() {
        return null;
    }
    
    public final int component38() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component39() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component40() {
        return null;
    }
    
    public final boolean component41() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.remote.response.login.UserData copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String fullName, @org.jetbrains.annotations.NotNull()
    java.lang.String age, @org.jetbrains.annotations.NotNull()
    java.lang.String gender, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.Nullable()
    java.lang.String profilePic, @org.jetbrains.annotations.NotNull()
    java.lang.String userType, @org.jetbrains.annotations.NotNull()
    java.lang.String role, @org.jetbrains.annotations.NotNull()
    java.lang.String isEmpMonitorAdmin, @org.jetbrains.annotations.NotNull()
    java.lang.String isTwoFactorEnabled, @org.jetbrains.annotations.NotNull()
    java.lang.String orgId, @org.jetbrains.annotations.NotNull()
    java.lang.String address, @org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String state, @org.jetbrains.annotations.NotNull()
    java.lang.String country, @org.jetbrains.annotations.NotNull()
    java.lang.String zipCode, @org.jetbrains.annotations.NotNull()
    java.lang.String countryCode, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String timezone, @org.jetbrains.annotations.NotNull()
    java.lang.String language, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneVerified, @org.jetbrains.annotations.NotNull()
    java.lang.String emailVerified, @org.jetbrains.annotations.NotNull()
    java.lang.String invitation, @org.jetbrains.annotations.NotNull()
    java.lang.String isSuspended, @org.jetbrains.annotations.NotNull()
    java.lang.String empMonitor, @org.jetbrains.annotations.NotNull()
    java.lang.String numberValidateOtp, @org.jetbrains.annotations.NotNull()
    java.lang.String numberValidateOtpExpire, @org.jetbrains.annotations.NotNull()
    java.lang.String emailValidateToken, @org.jetbrains.annotations.NotNull()
    java.lang.String emailTokenExpire, @org.jetbrains.annotations.NotNull()
    java.lang.String verificationEmailSentCount, @org.jetbrains.annotations.NotNull()
    java.lang.String forgotPasswordToken, @org.jetbrains.annotations.NotNull()
    java.lang.String forgotTokenExpire, @org.jetbrains.annotations.NotNull()
    java.lang.String passwordEmailSentCount, @org.jetbrains.annotations.NotNull()
    java.lang.String createdAt, @org.jetbrains.annotations.NotNull()
    java.lang.String updatedAt, @org.jetbrains.annotations.NotNull()
    java.lang.String emp_id, int v, @org.jetbrains.annotations.NotNull()
    java.lang.String isGeoFencingOn, @org.jetbrains.annotations.NotNull()
    java.lang.String isMobileDeviceEnabled, boolean isGlobalUser) {
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