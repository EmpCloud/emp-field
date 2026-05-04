package com.empcloud.empmonitor.data.remote.request.create_task;

import java.io.Serializable;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\"\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001By\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u0012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000b\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u0012\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0014J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\'\u001a\u0004\u0018\u00010\u0010H\u00c6\u0003J\u0010\u0010(\u001a\u0004\u0018\u00010\u0012H\u00c6\u0003\u00a2\u0006\u0002\u0010!J\t\u0010)\u001a\u00020\u0003H\u00c6\u0003J\t\u0010*\u001a\u00020\u0003H\u00c6\u0003J\t\u0010+\u001a\u00020\u0003H\u00c6\u0003J\t\u0010,\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010-\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010.\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010/\u001a\u00020\u0003H\u00c6\u0003J\u000f\u00100\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u00c6\u0003J\u000f\u00101\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000bH\u00c6\u0003J\u009a\u0001\u00102\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000b2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u0003H\u00c6\u0001\u00a2\u0006\u0002\u00103J\u0013\u00104\u001a\u0002052\b\u00106\u001a\u0004\u0018\u000107H\u00d6\u0003J\t\u00108\u001a\u000209H\u00d6\u0001J\t\u0010:\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0016R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001aR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0016R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0016R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0016R\u0015\u0010\u0011\u001a\u0004\u0018\u00010\u0012\u00a2\u0006\n\n\u0002\u0010\"\u001a\u0004\b \u0010!R\u0013\u0010\b\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0016R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%\u00a8\u0006;"}, d2 = {"Lcom/empcloud/empmonitor/data/remote/request/create_task/CreateTaskModel;", "Ljava/io/Serializable;", "clientId", "", "taskName", "start_time", "end_time", "form_url", "task_image_url", "taskDescription", "files", "", "Lcom/empcloud/empmonitor/data/remote/request/create_task/FilesUrl;", "images", "Lcom/empcloud/empmonitor/data/remote/request/create_task/ImgaesUrl;", "value", "Lcom/empcloud/empmonitor/data/remote/request/create_task/AmountCurrency;", "taskVolume", "", "date", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/util/List;Lcom/empcloud/empmonitor/data/remote/request/create_task/AmountCurrency;Ljava/lang/Double;Ljava/lang/String;)V", "getClientId", "()Ljava/lang/String;", "getDate", "getEnd_time", "getFiles", "()Ljava/util/List;", "getForm_url", "getImages", "getStart_time", "getTaskDescription", "getTaskName", "getTaskVolume", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getTask_image_url", "getValue", "()Lcom/empcloud/empmonitor/data/remote/request/create_task/AmountCurrency;", "component1", "component10", "component11", "component12", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/util/List;Lcom/empcloud/empmonitor/data/remote/request/create_task/AmountCurrency;Ljava/lang/Double;Ljava/lang/String;)Lcom/empcloud/empmonitor/data/remote/request/create_task/CreateTaskModel;", "equals", "", "other", "", "hashCode", "", "toString", "app_release"})
public final class CreateTaskModel implements java.io.Serializable {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String clientId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String taskName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String start_time = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String end_time = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String form_url = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String task_image_url = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String taskDescription = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl> files = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl> images = null;
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency value = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double taskVolume = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String date = null;
    
    public CreateTaskModel(@org.jetbrains.annotations.NotNull()
    java.lang.String clientId, @org.jetbrains.annotations.NotNull()
    java.lang.String taskName, @org.jetbrains.annotations.NotNull()
    java.lang.String start_time, @org.jetbrains.annotations.NotNull()
    java.lang.String end_time, @org.jetbrains.annotations.Nullable()
    java.lang.String form_url, @org.jetbrains.annotations.Nullable()
    java.lang.String task_image_url, @org.jetbrains.annotations.NotNull()
    java.lang.String taskDescription, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl> files, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl> images, @org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency value, @org.jetbrains.annotations.Nullable()
    java.lang.Double taskVolume, @org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getClientId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTaskName() {
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
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getForm_url() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTask_image_url() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTaskDescription() {
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
    
    @org.jetbrains.annotations.Nullable()
    public final com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency getValue() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getTaskVolume() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
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
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl> component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.empcloud.empmonitor.data.remote.request.create_task.CreateTaskModel copy(@org.jetbrains.annotations.NotNull()
    java.lang.String clientId, @org.jetbrains.annotations.NotNull()
    java.lang.String taskName, @org.jetbrains.annotations.NotNull()
    java.lang.String start_time, @org.jetbrains.annotations.NotNull()
    java.lang.String end_time, @org.jetbrains.annotations.Nullable()
    java.lang.String form_url, @org.jetbrains.annotations.Nullable()
    java.lang.String task_image_url, @org.jetbrains.annotations.NotNull()
    java.lang.String taskDescription, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl> files, @org.jetbrains.annotations.NotNull()
    java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl> images, @org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency value, @org.jetbrains.annotations.Nullable()
    java.lang.Double taskVolume, @org.jetbrains.annotations.NotNull()
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