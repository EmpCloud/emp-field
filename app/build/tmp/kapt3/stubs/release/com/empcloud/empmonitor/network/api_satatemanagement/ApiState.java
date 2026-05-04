package com.empcloud.empmonitor.network.api_satatemanagement;

import okhttp3.ResponseBody;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002:\u0003\u0004\u0005\u0006B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0003\u0082\u0001\u0003\u0007\b\t\u00a8\u0006\n"}, d2 = {"Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState;", "T", "", "()V", "ERROR", "LOADING", "SUCESS", "Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState$ERROR;", "Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState$LOADING;", "Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState$SUCESS;", "app_release"})
public abstract class ApiState<T extends java.lang.Object> {
    
    private ApiState() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\b\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B)\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0004H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0006H\u00c6\u0003J\u0010\u0010\u0016\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u000fJ\u000b\u0010\u0017\u001a\u0004\u0018\u00010\nH\u00c6\u0003J:\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nH\u00c6\u0001\u00a2\u0006\u0002\u0010\u0019J\u0013\u0010\u001a\u001a\u00020\u00062\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u00d6\u0003J\t\u0010\u001d\u001a\u00020\bH\u00d6\u0001J\t\u0010\u001e\u001a\u00020\u0004H\u00d6\u0001R\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0015\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u0010\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0011R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\u001f"}, d2 = {"Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState$ERROR;", "Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState;", "", "message", "", "isNetworkERROR", "", "errorCode", "", "errorBody", "Lokhttp3/ResponseBody;", "(Ljava/lang/String;ZLjava/lang/Integer;Lokhttp3/ResponseBody;)V", "getErrorBody", "()Lokhttp3/ResponseBody;", "getErrorCode", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "()Z", "getMessage", "()Ljava/lang/String;", "component1", "component2", "component3", "component4", "copy", "(Ljava/lang/String;ZLjava/lang/Integer;Lokhttp3/ResponseBody;)Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState$ERROR;", "equals", "other", "", "hashCode", "toString", "app_release"})
    public static final class ERROR extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String message = null;
        private final boolean isNetworkERROR = false;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Integer errorCode = null;
        @org.jetbrains.annotations.Nullable()
        private final okhttp3.ResponseBody errorBody = null;
        
        public ERROR(@org.jetbrains.annotations.NotNull()
        java.lang.String message, boolean isNetworkERROR, @org.jetbrains.annotations.Nullable()
        java.lang.Integer errorCode, @org.jetbrains.annotations.Nullable()
        okhttp3.ResponseBody errorBody) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getMessage() {
            return null;
        }
        
        public final boolean isNetworkERROR() {
            return false;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer getErrorCode() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final okhttp3.ResponseBody getErrorBody() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        public final boolean component2() {
            return false;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Integer component3() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final okhttp3.ResponseBody component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.empcloud.empmonitor.network.api_satatemanagement.ApiState.ERROR copy(@org.jetbrains.annotations.NotNull()
        java.lang.String message, boolean isNetworkERROR, @org.jetbrains.annotations.Nullable()
        java.lang.Integer errorCode, @org.jetbrains.annotations.Nullable()
        okhttp3.ResponseBody errorBody) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState$LOADING;", "Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState;", "", "()V", "app_release"})
    public static final class LOADING extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.empcloud.empmonitor.network.api_satatemanagement.ApiState.LOADING INSTANCE = null;
        
        private LOADING() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00028\u0001\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\b\u001a\u00028\u0001H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00010\u00002\b\b\u0002\u0010\u0003\u001a\u00028\u0001H\u00c6\u0001\u00a2\u0006\u0002\u0010\nJ\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u00d6\u0003J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001R\u0013\u0010\u0003\u001a\u00028\u0001\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0013"}, d2 = {"Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState$SUCESS;", "R", "Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState;", "getResponse", "(Ljava/lang/Object;)V", "getGetResponse", "()Ljava/lang/Object;", "Ljava/lang/Object;", "component1", "copy", "(Ljava/lang/Object;)Lcom/empcloud/empmonitor/network/api_satatemanagement/ApiState$SUCESS;", "equals", "", "other", "", "hashCode", "", "toString", "", "app_release"})
    public static final class SUCESS<R extends java.lang.Object> extends com.empcloud.empmonitor.network.api_satatemanagement.ApiState<R> {
        private final R getResponse = null;
        
        public SUCESS(R getResponse) {
        }
        
        public final R getGetResponse() {
            return null;
        }
        
        public final R component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.empcloud.empmonitor.network.api_satatemanagement.ApiState.SUCESS<R> copy(R getResponse) {
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
}