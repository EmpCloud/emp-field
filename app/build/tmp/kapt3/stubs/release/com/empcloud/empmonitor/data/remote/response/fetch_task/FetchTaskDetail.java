package com.empcloud.empmonitor.data.remote.response.fetch_task;

import com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency;
import com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl;
import com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl;
import com.empcloud.empmonitor.data.remote.request.update_task.Tags;
import java.io.Serializable;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\bC\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001B\u00f9\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\u0006\u0010\n\u001a\u00020\u0005\u0012\u0006\u0010\u000b\u001a\u00020\u0005\u0012\u0006\u0010\f\u001a\u00020\u0005\u0012\u0006\u0010\r\u001a\u00020\u0005\u0012\u0006\u0010\u000e\u001a\u00020\u0005\u0012\u0006\u0010\u000f\u001a\u00020\u0005\u0012\u0006\u0010\u0010\u001a\u00020\u0005\u0012\u0006\u0010\u0011\u001a\u00020\u0005\u0012\u0006\u0010\u0012\u001a\u00020\u0005\u0012\u0006\u0010\u0013\u001a\u00020\u0005\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\u0006\u0010\u0016\u001a\u00020\u0005\u0012\u0006\u0010\u0017\u001a\u00020\u0005\u0012\u0006\u0010\u0018\u001a\u00020\u0005\u0012\u0006\u0010\u0019\u001a\u00020\u0005\u0012\u0006\u0010\u001a\u001a\u00020\u0005\u0012\u0006\u0010\u001b\u001a\u00020\u0005\u0012\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001d\u0012\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020 0\u001d\u0012\u0006\u0010!\u001a\u00020\"\u0012\u000e\u0010#\u001a\n\u0012\u0004\u0012\u00020%\u0018\u00010$\u0012\u0006\u0010&\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\'J\t\u0010K\u001a\u00020\u0003H\u00c6\u0003J\t\u0010L\u001a\u00020\u0005H\u00c6\u0003J\t\u0010M\u001a\u00020\u0005H\u00c6\u0003J\t\u0010N\u001a\u00020\u0005H\u00c6\u0003J\t\u0010O\u001a\u00020\u0005H\u00c6\u0003J\t\u0010P\u001a\u00020\u0005H\u00c6\u0003J\t\u0010Q\u001a\u00020\u0005H\u00c6\u0003J\t\u0010R\u001a\u00020\u0005H\u00c6\u0003J\t\u0010S\u001a\u00020\u0015H\u00c6\u0003J\t\u0010T\u001a\u00020\u0005H\u00c6\u0003J\t\u0010U\u001a\u00020\u0005H\u00c6\u0003J\t\u0010V\u001a\u00020\u0005H\u00c6\u0003J\t\u0010W\u001a\u00020\u0005H\u00c6\u0003J\t\u0010X\u001a\u00020\u0005H\u00c6\u0003J\t\u0010Y\u001a\u00020\u0005H\u00c6\u0003J\t\u0010Z\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010[\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dH\u00c6\u0003J\u000f\u0010\\\u001a\b\u0012\u0004\u0012\u00020 0\u001dH\u00c6\u0003J\t\u0010]\u001a\u00020\"H\u00c6\u0003J\u0011\u0010^\u001a\n\u0012\u0004\u0012\u00020%\u0018\u00010$H\u00c6\u0003J\t\u0010_\u001a\u00020\u0005H\u00c6\u0003J\t\u0010`\u001a\u00020\u0005H\u00c6\u0003J\t\u0010a\u001a\u00020\u0005H\u00c6\u0003J\t\u0010b\u001a\u00020\u0005H\u00c6\u0003J\t\u0010c\u001a\u00020\u0005H\u00c6\u0003J\t\u0010d\u001a\u00020\u0005H\u00c6\u0003J\t\u0010e\u001a\u00020\u0005H\u00c6\u0003J\t\u0010f\u001a\u00020\u0005H\u00c6\u0003J\u00b5\u0002\u0010g\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\u00052\b\b\u0002\u0010\r\u001a\u00020\u00052\b\b\u0002\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u00052\b\b\u0002\u0010\u0011\u001a\u00020\u00052\b\b\u0002\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u0013\u001a\u00020\u00052\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u00052\b\b\u0002\u0010\u0017\u001a\u00020\u00052\b\b\u0002\u0010\u0018\u001a\u00020\u00052\b\b\u0002\u0010\u0019\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\u00052\b\b\u0002\u0010\u001b\u001a\u00020\u00052\u000e\b\u0002\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001d2\u000e\b\u0002\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020 0\u001d2\b\b\u0002\u0010!\u001a\u00020\"2\u0010\b\u0002\u0010#\u001a\n\u0012\u0004\u0012\u00020%\u0018\u00010$2\b\b\u0002\u0010&\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010h\u001a\u00020i2\b\u0010j\u001a\u0004\u0018\u00010kH\u00d6\u0003J\t\u0010l\u001a\u00020\u0015H\u00d6\u0001J\t\u0010m\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0011\u0010\f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010)R\u0011\u0010\r\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010)R\u0011\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010)R\u0011\u0010\t\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010)R\u0011\u0010\u001b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010)R\u0011\u0010\u0011\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010)R\u0011\u0010&\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010)R\u0011\u0010\u0013\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010)R\u0011\u0010\u001a\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010)R\u0011\u0010\u0019\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010)R\u0011\u0010\b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010)R\u0011\u0010\u000b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010)R\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u00107R\u0011\u0010\u0018\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010)R\u0017\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020 0\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u00107R\u0011\u0010\u000f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010)R\u0011\u0010\u0010\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010)R\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010)R\u0011\u0010\n\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010)R\u0019\u0010#\u001a\n\u0012\u0004\u0012\u00020%\u0018\u00010$\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u00107R\u001a\u0010\u0014\u001a\u00020\u0015X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b?\u0010@\"\u0004\bA\u0010BR\u0011\u0010\u0016\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010)R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010)R\u0011\u0010!\u001a\u00020\"\u00a2\u0006\b\n\u0000\u001a\u0004\bE\u0010FR\u0011\u0010\u0017\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u0010)R\u0011\u0010\u0012\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\bH\u0010)R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u0010J\u00a8\u0006n"}, d2 = {"Lcom/empcloud/empmonitor/data/remote/response/fetch_task/FetchTaskDetail;", "Ljava/io/Serializable;", "value", "Lcom/empcloud/empmonitor/data/remote/request/create_task/AmountCurrency;", "_id", "", "taskName", "orgId", "emp_id", "clientId", "start_time", "end_time", "address1", "address2", "city", "latitude", "longitude", "createdAt", "updatedAt", "deletedStatus", "taskApproveStatus", "", "taskDescription", "task_image_url", "form_url", "empStartTime", "empEndTime", "clientName", "files", "", "Lcom/empcloud/empmonitor/data/remote/request/create_task/FilesUrl;", "images", "Lcom/empcloud/empmonitor/data/remote/request/create_task/ImgaesUrl;", "taskVolume", "", "tagLogs", "", "Lcom/empcloud/empmonitor/data/remote/request/update_task/Tags;", "date", "(Lcom/empcloud/empmonitor/data/remote/request/create_task/AmountCurrency;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/util/List;DLjava/util/List;Ljava/lang/String;)V", "get_id", "()Ljava/lang/String;", "getAddress1", "getAddress2", "getCity", "getClientId", "getClientName", "getCreatedAt", "getDate", "getDeletedStatus", "getEmpEndTime", "getEmpStartTime", "getEmp_id", "getEnd_time", "getFiles", "()Ljava/util/List;", "getForm_url", "getImages", "getLatitude", "getLongitude", "getOrgId", "getStart_time", "getTagLogs", "getTaskApproveStatus", "()I", "setTaskApproveStatus", "(I)V", "getTaskDescription", "getTaskName", "getTaskVolume", "()D", "getTask_image_url", "getUpdatedAt", "getValue", "()Lcom/empcloud/empmonitor/data/remote/request/create_task/AmountCurrency;", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "", "hashCode", "toString", "app_release"})
public final class FetchTaskDetail implements java.io.Serializable {
    @org.jetbrains.annotations.NotNull()
    private final com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency value = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String _id = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String taskName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String orgId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String emp_id = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String clientId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String start_time = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String end_time = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String address1 = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String address2 = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String city = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String latitude = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String longitude = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String createdAt = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String updatedAt = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String deletedStatus = null;
    private int taskApproveStatus;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String taskDescription = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String task_image_url = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String form_url = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String empStartTime = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String empEndTime = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String clientName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl> files = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl> images = null;
    private final double taskVolume = 0.0;
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<com.empcloud.empmonitor.data.remote.request.update_task.Tags> tagLogs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String date = null;
    
    public FetchTaskDetail(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency value, @org.jetbrains.annotations.NotNull()
    java.lang.String _id, @org.jetbrains.annotations.NotNull()
    java.lang.String taskName, @org.jetbrains.annotations.NotNull()
    java.lang.String orgId, @org.jetbrains.annotations.NotNull()
    java.lang.String emp_id, @org.jetbrains.annotations.NotNull()
    java.lang.String clientId, @org.jetbrains.annotations.NotNull()
    java.lang.String start_time, @org.jetbrains.annotations.NotNull()
    java.lang.String end_time, @org.jetbrains.annotations.NotNull()
    java.lang.String address1, @org.jetbrains.annotations.NotNull()
    java.lang.String address2, @org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String latitude, @org.jetbrains.annotations.NotNull()
    java.lang.String longitude, @org.jetbrains.annotations.NotNull()
    java.lang.String createdAt, @org.jetbrains.annotations.NotNull()
    java.lang.String updatedAt, @org.jetbrains.annotations.NotNull()
    java.lang.String deletedStatus, int taskApproveStatus, @org.jetbrains.annotations.NotNull()
    java.lang.String taskDescription, @org.jetbrains.annotations.NotNull()
    java.lang.String task_image_url, @org.jetbrains.annotations.NotNull()
    java.lang.String form_url, @org.jetbrains.annotations.NotNull()
    java.lang.String empStartTime, @org.jetbrains.annotations.NotNull()
    java.lang.String empEndTime, @org.jetbrains.annotations.NotNull()
    java.lang.String clientName, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl> files, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl> images, double taskVolume, @org.jetbrains.annotations.Nullable()
    java.util.List<com.empcloud.empmonitor.data.remote.request.update_task.Tags> tagLogs, @org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency getValue() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String get_id() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTaskName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getOrgId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmp_id() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getClientId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getStart_time() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEnd_time() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAddress1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAddress2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCity() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLatitude() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLongitude() {
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
    public final java.lang.String getDeletedStatus() {
        return null;
    }
    
    public final int getTaskApproveStatus() {
        return 0;
    }
    
    public final void setTaskApproveStatus(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTaskDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTask_image_url() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getForm_url() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmpStartTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getEmpEndTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getClientName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl> getFiles() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl> getImages() {
        return null;
    }
    
    public final double getTaskVolume() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.empcloud.empmonitor.data.remote.request.update_task.Tags> getTagLogs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency component1() {
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
    
    public final int component17() {
        return 0;
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
    public final java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl> component24() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl> component25() {
        return null;
    }
    
    public final double component26() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.empcloud.empmonitor.data.remote.request.update_task.Tags> component27() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component28() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
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
    public final com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail copy(@org.jetbrains.annotations.NotNull()
    com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency value, @org.jetbrains.annotations.NotNull()
    java.lang.String _id, @org.jetbrains.annotations.NotNull()
    java.lang.String taskName, @org.jetbrains.annotations.NotNull()
    java.lang.String orgId, @org.jetbrains.annotations.NotNull()
    java.lang.String emp_id, @org.jetbrains.annotations.NotNull()
    java.lang.String clientId, @org.jetbrains.annotations.NotNull()
    java.lang.String start_time, @org.jetbrains.annotations.NotNull()
    java.lang.String end_time, @org.jetbrains.annotations.NotNull()
    java.lang.String address1, @org.jetbrains.annotations.NotNull()
    java.lang.String address2, @org.jetbrains.annotations.NotNull()
    java.lang.String city, @org.jetbrains.annotations.NotNull()
    java.lang.String latitude, @org.jetbrains.annotations.NotNull()
    java.lang.String longitude, @org.jetbrains.annotations.NotNull()
    java.lang.String createdAt, @org.jetbrains.annotations.NotNull()
    java.lang.String updatedAt, @org.jetbrains.annotations.NotNull()
    java.lang.String deletedStatus, int taskApproveStatus, @org.jetbrains.annotations.NotNull()
    java.lang.String taskDescription, @org.jetbrains.annotations.NotNull()
    java.lang.String task_image_url, @org.jetbrains.annotations.NotNull()
    java.lang.String form_url, @org.jetbrains.annotations.NotNull()
    java.lang.String empStartTime, @org.jetbrains.annotations.NotNull()
    java.lang.String empEndTime, @org.jetbrains.annotations.NotNull()
    java.lang.String clientName, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl> files, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl> images, double taskVolume, @org.jetbrains.annotations.Nullable()
    java.util.List<com.empcloud.empmonitor.data.remote.request.update_task.Tags> tagLogs, @org.jetbrains.annotations.NotNull()
    java.lang.String date) {
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