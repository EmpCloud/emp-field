package com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.empcloud.empmonitor.R;
import com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency;
import com.empcloud.empmonitor.data.remote.request.create_task.CreateTaskModel;
import com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl;
import com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl;
import com.empcloud.empmonitor.data.remote.request.create_task.RecurrenceData;
import com.empcloud.empmonitor.databinding.FragmentAddTaskBinding;
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState;
import com.empcloud.empmonitor.ui.fragment.task.select_client_3.SelectClientFragment;
import com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_picture.AddPictureFragment;
import com.empcloud.empmonitor.ui.fragment.task.task_first.TaskHomeFragment;
import com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener;
import com.empcloud.empmonitor.utils.CommonMethods;
import com.empcloud.empmonitor.utils.Constants;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MultipartBody;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u009e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0015\b\u0007\u0018\u0000 s2\u00020\u0001:\u0001sB\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010;\u001a\u00020<2\b\u0010=\u001a\u0004\u0018\u00010\t2\b\u0010>\u001a\u0004\u0018\u00010\tH\u0002J\b\u0010?\u001a\u00020@H\u0002J\b\u0010A\u001a\u00020<H\u0002J\u0006\u0010B\u001a\u00020<J\u0010\u0010C\u001a\u00020<2\u0006\u0010D\u001a\u00020\u0006H\u0002J\b\u0010E\u001a\u00020<H\u0002J\b\u0010F\u001a\u00020<H\u0002J\b\u0010G\u001a\u00020<H\u0002J\b\u0010H\u001a\u00020<H\u0002J\u0018\u0010I\u001a\u00020\t2\u0006\u0010J\u001a\u00020\u00062\u0006\u0010K\u001a\u00020\u0006H\u0002J\u0012\u0010L\u001a\u0004\u0018\u00010\t2\u0006\u0010M\u001a\u00020\u001dH\u0002J\u0018\u0010N\u001a\u00020<2\u0006\u0010M\u001a\u00020\u001d2\u0006\u0010D\u001a\u00020\u0006H\u0002J\b\u0010O\u001a\u00020<H\u0002J\b\u0010P\u001a\u00020<H\u0002J\b\u0010Q\u001a\u00020<H\u0002J\"\u0010R\u001a\u00020<2\u0006\u0010S\u001a\u00020\u00062\u0006\u0010T\u001a\u00020\u00062\b\u0010U\u001a\u0004\u0018\u00010VH\u0016J\u0012\u0010W\u001a\u00020<2\b\u0010X\u001a\u0004\u0018\u00010YH\u0016J&\u0010Z\u001a\u0004\u0018\u00010[2\u0006\u0010\\\u001a\u00020]2\b\u0010^\u001a\u0004\u0018\u00010_2\b\u0010X\u001a\u0004\u0018\u00010YH\u0016J\b\u0010`\u001a\u00020<H\u0016J\u001a\u0010a\u001a\u00020<2\u0006\u0010b\u001a\u00020[2\b\u0010X\u001a\u0004\u0018\u00010YH\u0017J\u0012\u0010c\u001a\u00020<2\b\u0010d\u001a\u0004\u0018\u00010\u001dH\u0002J\b\u0010e\u001a\u00020<H\u0002J\b\u0010f\u001a\u00020<H\u0002J\b\u0010g\u001a\u00020<H\u0002J\b\u0010h\u001a\u00020<H\u0002J\b\u0010i\u001a\u00020<H\u0002J\b\u0010j\u001a\u00020<H\u0002J\b\u0010k\u001a\u00020<H\u0002J\b\u0010l\u001a\u00020<H\u0002J\b\u0010m\u001a\u00020<H\u0002J\u0006\u0010n\u001a\u00020<J\u0010\u0010o\u001a\u00020<2\u0006\u0010D\u001a\u00020\u0006H\u0002J\u0006\u0010p\u001a\u00020<J\u0006\u0010q\u001a\u00020<J\b\u0010r\u001a\u00020<H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\nR\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00190\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020 0\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010#\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010%\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\'\u001a\u0010\u0012\f\u0012\n )*\u0004\u0018\u00010\t0\t0(X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010+\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010,\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010-\u001a\u0004\u0018\u00010\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010.\u001a\u0004\u0018\u00010\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010/\u001a\u0004\u0018\u000100X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u00101R\u000e\u00102\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00103\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u00105\u001a\u0002068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b9\u0010:\u001a\u0004\b7\u00108\u00a8\u0006t"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/task/task_create_second/add_task/AddTaskFragment;", "Landroidx/fragment/app/Fragment;", "listener", "Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;", "(Lcom/empcloud/empmonitor/ui/listeners/OnFragmentChangedListener;)V", "REQUEST_CODE_PICK_DOCUMENT", "", "REQUIRED_PERMISSIONS", "", "", "[Ljava/lang/String;", "amountCurrency", "Lcom/empcloud/empmonitor/data/remote/request/create_task/AmountCurrency;", "binding", "Lcom/empcloud/empmonitor/databinding/FragmentAddTaskBinding;", "cameraExecutor", "Ljava/util/concurrent/ExecutorService;", "clientID", "clientName", "descpic1", "descpic2", "descpic3", "descpic4", "docsList", "", "Lcom/empcloud/empmonitor/data/remote/request/create_task/FilesUrl;", "docsResponse", "docsResponse2", "doscUri1", "Landroid/net/Uri;", "doscUri2", "imagesList", "Lcom/empcloud/empmonitor/data/remote/request/create_task/ImgaesUrl;", "pic1", "pic2", "pic3", "pic4", "picDesc", "picResponse", "requestPermissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "kotlin.jvm.PlatformType", "savedEndTime", "savedStartTime", "selectedCountry", "selectedFileUri", "selectedStartTime", "taskVolume", "", "Ljava/lang/Double;", "timeEnd", "timeRaw", "timeStart", "viewModel", "Lcom/empcloud/empmonitor/ui/fragment/task/task_create_second/add_task/AddTaskViewModel;", "getViewModel", "()Lcom/empcloud/empmonitor/ui/fragment/task/task_create_second/add_task/AddTaskViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "addDataToTList", "", "picture", "descritpion", "allPermissionsGranted", "", "clearAllValuesForFirstTime", "clearAllValuesOFPref", "clearSelectedFile", "i", "collectPicResponse", "currencySpinner", "decreaseCountValue", "docsUploadingResponse", "formatTime", "hour", "minute", "getFileName", "uri", "handleFileUri", "initPicResponse", "observeCreateTaskCall", "observeSendPicsCall", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroyView", "onViewCreated", "view", "openDocs", "doscUri", "openFilePicker", "picsAddButton", "previewImage", "previewImageCancel", "requestPermissions", "selectClientOption", "selectDocument", "setInitialPreviousData", "setpic", "shiftImagesLeft", "showTimePickerDialog", "updatePref", "updateSharedPrefData", "validateForm", "Companion", "app_release"})
public final class AddTaskFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private final com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener = null;
    @org.jetbrains.annotations.Nullable()
    private com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency amountCurrency;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double taskVolume;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri doscUri1;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri doscUri2;
    private java.util.concurrent.ExecutorService cameraExecutor;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String timeRaw;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String selectedStartTime = null;
    private com.empcloud.empmonitor.databinding.FragmentAddTaskBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String clientID;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String clientName;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String timeStart = "";
    @org.jetbrains.annotations.NotNull()
    private java.lang.String timeEnd = "";
    private final int REQUEST_CODE_PICK_DOCUMENT = 102;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String picResponse;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String docsResponse;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String docsResponse2;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String picDesc;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String selectedCountry;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl> docsList;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl> imagesList;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String savedStartTime;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String savedEndTime;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri selectedFileUri;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String pic1;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String pic2;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String pic3;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String pic4;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String descpic1;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String descpic2;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String descpic3;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String descpic4;
    private static final int REQUEST_CODE_PICK_FILE = 1;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String[] REQUIRED_PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE"};
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> requestPermissionLauncher = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task.AddTaskFragment.Companion Companion = null;
    
    public AddTaskFragment(@org.jetbrains.annotations.Nullable()
    com.empcloud.empmonitor.ui.listeners.OnFragmentChangedListener listener) {
        super();
    }
    
    private final com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task.AddTaskViewModel getViewModel() {
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
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void addDataToTList(java.lang.String picture, java.lang.String descritpion) {
    }
    
    private final void setInitialPreviousData() {
    }
    
    private final void initPicResponse() {
    }
    
    private final void collectPicResponse() {
    }
    
    private final void selectClientOption() {
    }
    
    private final void picsAddButton() {
    }
    
    private final void docsUploadingResponse() {
    }
    
    private final void previewImageCancel() {
    }
    
    private final void previewImage() {
    }
    
    private final void decreaseCountValue() {
    }
    
    private final void setpic() {
    }
    
    private final void openDocs(android.net.Uri doscUri) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    private final void observeCreateTaskCall() {
    }
    
    private final void validateForm() {
    }
    
    private final void selectDocument() {
    }
    
    private final boolean allPermissionsGranted() {
        return false;
    }
    
    private final void requestPermissions() {
    }
    
    private final void openFilePicker() {
    }
    
    @java.lang.Override()
    public void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    private final void handleFileUri(android.net.Uri uri, int i) {
    }
    
    private final java.lang.String getFileName(android.net.Uri uri) {
        return null;
    }
    
    private final void clearSelectedFile(int i) {
    }
    
    private final void observeSendPicsCall() {
    }
    
    private final void showTimePickerDialog(int i) {
    }
    
    private final java.lang.String formatTime(int hour, int minute) {
        return null;
    }
    
    private final void currencySpinner() {
    }
    
    public final void shiftImagesLeft() {
    }
    
    public final void updatePref() {
    }
    
    public final void clearAllValuesOFPref() {
    }
    
    public final void updateSharedPrefData() {
    }
    
    private final void clearAllValuesForFirstTime() {
    }
    
    public AddTaskFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/empcloud/empmonitor/ui/fragment/task/task_create_second/add_task/AddTaskFragment$Companion;", "", "()V", "REQUEST_CODE_PICK_FILE", "", "newInstance", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task.AddTaskFragment.Companion newInstance() {
            return null;
        }
    }
}