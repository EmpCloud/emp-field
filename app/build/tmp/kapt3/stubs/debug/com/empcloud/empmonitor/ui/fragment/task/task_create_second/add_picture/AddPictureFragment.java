package com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_picture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail;
import com.empcloud.empmonitor.databinding.FragmentAddPictureBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task.AddTaskFragment;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00a0\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\b\u0007\u0018\u0000 J2\u00020\u0001:\u0001JB\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&H\u0002J\b\u0010\'\u001a\u00020$H\u0002J\u0010\u0010\'\u001a\u00020$2\u0006\u0010(\u001a\u00020)H\u0002J\b\u0010*\u001a\u00020+H\u0002J\u0018\u0010,\u001a\u0004\u0018\u00010+2\u0006\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u000200J\u0010\u00101\u001a\u0002022\u0006\u00103\u001a\u000202H\u0002J\b\u00104\u001a\u00020$H\u0002J\"\u00105\u001a\u00020$2\u0006\u00106\u001a\u00020\u00062\u0006\u00107\u001a\u00020\u00062\b\u00108\u001a\u0004\u0018\u000109H\u0016J\u0012\u0010:\u001a\u00020$2\b\u0010;\u001a\u0004\u0018\u00010<H\u0016J&\u0010=\u001a\u0004\u0018\u00010)2\u0006\u0010>\u001a\u00020?2\b\u0010@\u001a\u0004\u0018\u00010A2\b\u0010;\u001a\u0004\u0018\u00010<H\u0016J\b\u0010B\u001a\u00020$H\u0016J\b\u0010C\u001a\u00020$H\u0016J\u001a\u0010D\u001a\u00020$2\u0006\u0010(\u001a\u00020)2\b\u0010;\u001a\u0004\u0018\u00010<H\u0016J\b\u0010E\u001a\u00020$H\u0002J\b\u0010F\u001a\u00020$H\u0002J\u0010\u0010G\u001a\u00020$2\u0006\u0010%\u001a\u00020&H\u0002J\u0010\u0010H\u001a\u00020$2\u0006\u0010/\u001a\u000200H\u0002J\b\u0010I\u001a\u00020$H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001d\u001a\u00020\u001e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b!\u0010\"\u001a\u0004\b\u001f\u0010 \u00a8\u0006K"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/task/task_create_second/add_picture/AddPictureFragment;", "Landroidx/fragment/app/Fragment;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "(Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;)V", "REQUEST_CODE_PERMISSIONS", "", "REQUEST_CODE_PICK_IMAGE", "REQUEST_IMAGE_CAPTURE", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentAddPictureBinding;", "cameraExecutor", "Ljava/util/concurrent/ExecutorService;", "cameraProvider", "Landroidx/camera/lifecycle/ProcessCameraProvider;", "clientid", "", "clientname", "currentPhotoPath", "fetchTaskDetail", "Lcom/empcloud/empmonitor/data/remote/response/fetch_task/FetchTaskDetail;", "fragmentNo", "Ljava/lang/Integer;", "imageCapture", "Landroidx/camera/core/ImageCapture;", "lensFacing", "picResponse", "preview", "Landroidx/camera/core/Preview;", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/task/task_create_second/add_picture/AddPictureViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/task/task_create_second/add_picture/AddPictureViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "bindCameraUseCases", "", "previewView", "Landroidx/camera/view/PreviewView;", "captureImage", "view", "Landroid/view/View;", "createImageFile", "Ljava/io/File;", "getFileFromUri", "context", "Landroid/content/Context;", "uri", "Landroid/net/Uri;", "invertBitmap", "Landroid/graphics/Bitmap;", "bitmap", "observeSendPicsCall", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroyView", "onResume", "onViewCreated", "openGallery", "startCamera", "toggleCamera", "uploadImage", "validateForm", "Companion", "app_debug"})
public final class AddPictureFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener = null;
    private com.empcloud.empmonitor.databinding.FragmentAddPictureBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private int lensFacing = androidx.camera.core.CameraSelector.LENS_FACING_BACK;
    private androidx.camera.lifecycle.ProcessCameraProvider cameraProvider;
    private java.util.concurrent.ExecutorService cameraExecutor;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private java.lang.String currentPhotoPath;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String picResponse;
    private androidx.camera.core.ImageCapture imageCapture;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String clientid;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String clientname;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer fragmentNo;
    private final int REQUEST_CODE_PICK_IMAGE = 1002;
    private final int REQUEST_CODE_PERMISSIONS = 1001;
    private androidx.camera.core.Preview preview;
    @org.jetbrains.annotations.Nullable()
    private com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail fetchTaskDetail;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_picture.AddPictureFragment.Companion Companion = null;
    
    public AddPictureFragment(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_picture.AddPictureViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void validateForm() {
    }
    
    private final void observeSendPicsCall() {
    }
    
    private final void captureImage() {
    }
    
    @kotlin.jvm.Throws(exceptionClasses = {java.io.IOException.class})
    private final java.io.File createImageFile() throws java.io.IOException {
        return null;
    }
    
    private final void captureImage(android.view.View view) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.io.File getFileFromUri(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
        return null;
    }
    
    private final android.graphics.Bitmap invertBitmap(android.graphics.Bitmap bitmap) {
        return null;
    }
    
    private final void startCamera() {
    }
    
    private final void bindCameraUseCases(androidx.camera.view.PreviewView previewView) {
    }
    
    private final void toggleCamera(androidx.camera.view.PreviewView previewView) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    private final void openGallery() {
    }
    
    @java.lang.Override()
    public void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    private final void uploadImage(android.net.Uri uri) {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    public AddPictureFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0000\u00a8\u0006\u0004"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/task/task_create_second/add_picture/AddPictureFragment$Companion;", "", "()V", "newInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_picture.AddPictureFragment.Companion newInstance() {
            return null;
        }
    }
}