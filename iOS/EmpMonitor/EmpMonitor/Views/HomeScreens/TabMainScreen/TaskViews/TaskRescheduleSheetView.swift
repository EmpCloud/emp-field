//
//  TaskRescheduleSheetView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/09/24.
//

import SwiftUI

struct TaskRescheduleSheetView: View {
    
    @ObservedObject var cameraViewModel: CameraViewModel
    @ObservedObject var uploadFileViewModel: UploadFilesViewModel
    @ObservedObject var updateTaskViewModel: UpdateTaskViewModel
    
    @ObservedObject var tagViewModel: TagViewModel
    @ObservedObject var dateViewModel: DateViewModel
    
    //sheet manipulation
    @Binding var sheetHeight: CGFloat
    @Binding var currentDetent: PresentationDetent
    @Binding var showTaskRescheduleSheet: Bool
    
    @Binding var taskVolume: String
    @Binding var taskValue: String
    
    @Binding var showCurrencyPopup: Bool
    @Binding var selectedCurrency: String
    
    //camera
    @Binding var showCamera: Bool
    @Binding var showImagePreview: Bool
    @Binding var selectedImage: UIImage?
    @Binding var savedImageURLs: [URL]
    @Binding var savedImages: [SavedImage]
    
    //calendar
    @State var showCalendar: Bool = false
    // for rescheduling
    @State private var startDate: String?
    @State private var endDate: String?
    @State private var setStartDate: Bool?
    @State private var setEndDate: Bool?
    
    @State private var rescheduleStartTime: String?
    @State private var rescheduleStopTime: String?
    
    //time
//    @State private var showTimePicker: Bool = false
    @State private var startTime: String = ""
    @State private var stopTime: String = ""
//    @State private var setStartTime: Bool?
    
    
    //DATA
    @Binding var selectedTask: FilterTaskListResponseData?
    @Binding var taskStatus: [String : Int]  // for task status "start", "pause", "resume"

    
    
    //dismiss the view when the task is updated
    @Binding var isDismiss: Bool
    
    //Warning
    @Binding var showWarningPopup: Bool
    
    
    //To refresh the taskList after updating status
    @Binding var refreshScreen: Bool
    
    
    var body: some View {
        ZStack {
            VStack {
                if sheetHeight == 0.05 {
                    HStack {
                        Text("Swipe Up")
                            .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                        Image(systemName: "arrow.up")
                    }
                    .fontWeight(AppFont.Weight.bold)
                    .foregroundStyle(Color.subText)
                    .padding(.top, 20)
                }
                else if sheetHeight == 0.3 {
                    VStack(spacing: AppSpacing.stackSpacingDefault) {
                        Text(selectedTask?.taskName ?? "Task")
                            .font(AppFont.primary(size: AppFont.Size.title3, weight: AppFont.Weight.regular))
                            .fontWeight(AppFont.Weight.semibold)
                            .foregroundStyle(Color.addAddressText)
                            .lineLimit(2)
                            .multilineTextAlignment(.center)
                            .frame(maxWidth: .infinity)
                            .padding(.top, 10)
                        
                        
                        //NAME
                        HStack(spacing: AppSpacing.stackSpacingDefault) {
                            Circle()
                                .fill(
                                    .shadow(.inner(color: Color.taskSearchBar.opacity(0.5), radius: 4))
                                )
                                .foregroundStyle(Color.white)
                                .frame(width: 30, height: 30)
                                .overlay {
                                    Image(.profileIcon)
                                }
                            
                            Text(selectedTask?.clientName ?? "")
                                .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                .fontWeight(AppFont.Weight.medium)
                                .foregroundStyle(Color.subText)
                                .lineLimit(1)
                                .truncationMode(.tail)
                            
                            Spacer()
                        }
                        
                        //Timing
                        HStack(spacing: AppSpacing.stackSpacingDefault) {
                            Circle()
                                .fill(
                                    .shadow(.inner(color: Color.taskSearchBar.opacity(0.5), radius: 4))
                                )
                                .foregroundStyle(Color.white)
                                .frame(width: 30, height: 30)
                                .overlay {
                                    Image(.clock)
                                        .resizable()
                                        .aspectRatio(contentMode: .fit)
                                        .frame(width: 16.3, height: 16.3)
                                }
                            
                            //Time
                            HStack(spacing: 0) {
                                if let startTime = rescheduleStartTime {
                                    Text(startTime)
                                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                        .fontWeight(AppFont.Weight.semibold)
                                        .foregroundStyle(Color.primaryButton1)
                                }
                                else if let startTime = selectedTask?.startTime {
                                    Text(FormatterHelper.shared.checkTimeFormatter(from: startTime) ?? "")
//                                    Text(self.startTime)
                                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                        .fontWeight(AppFont.Weight.semibold)
                                        .foregroundStyle(Color.primaryButton1)
                                }else{
                                    Text("__")
                                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                        .fontWeight(AppFont.Weight.semibold)
                                        .foregroundStyle(Color.primaryButton1)
                                }
                                
                                Text(" - ")
                                
                                if let stopTime = rescheduleStopTime {
                                    Text(stopTime)
                                            .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                            .fontWeight(AppFont.Weight.semibold)
                                            .foregroundStyle(Color.primaryButton1)
                                }
                                else if let endTime = selectedTask?.endTime {
                                    Text(FormatterHelper.shared.checkTimeFormatter(from: endTime) ?? "")
//                                    Text(stopTime)
                                            .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                            .fontWeight(AppFont.Weight.semibold)
                                            .foregroundStyle(Color.primaryButton1)
                                }else{
                                    Text("__")
                                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                        .fontWeight(AppFont.Weight.semibold)
                                        .foregroundStyle(Color.primaryButton1)
                                }
                            }
                            .lineLimit(1)
                            .minimumScaleFactor(0.9)
                            
                            
                            Spacer()
                        }
                        
                        //Address
                        HStack(alignment: .top, spacing: AppSpacing.stackSpacingDefault) {
                            Circle()
                                .fill(
                                    .shadow(.inner(color: Color.taskSearchBar.opacity(0.5), radius: 4))
                                )
                                .foregroundStyle(Color.white)
                                .frame(width: 30, height: 30)
                                .overlay {
                                    Image(.locationPointerBlueIcon)
                                }
                            
                            HStack(spacing: 0) {
                                if let address1 = selectedTask?.address1 {
                                    Text(address1)
                                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                        .fontWeight(AppFont.Weight.medium)
                                        .foregroundStyle(Color.subText)
                                        .lineLimit(2)
                                        .fixedSize(horizontal: false, vertical: true)
                                }else if let address2 = selectedTask?.address2 {
                                    Text(address2)
                                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                        .fontWeight(AppFont.Weight.medium)
                                        .foregroundStyle(Color.subText)
                                        .lineLimit(2)
                                        .fixedSize(horizontal: false, vertical: true)
                                }
                            }
                            
                            
                            Spacer()
                        }
                        
                        //MARK: Button
                        if taskStatus[selectedTask?.id ?? ""] == 0 {
                            PrimaryThinButton(text: "Start") {
                                //TODO: To start the task
                                Task {
                                    updateTaskViewModel.taskID = selectedTask?.id ?? ""
                                    updateTaskViewModel.status = 1
                                    updateTaskViewModel.currentDateTime = FormatterHelper.shared.getCurrentDateTimeFormatted()
                                    updateTaskViewModel.latitude = selectedTask?.latitude ?? ""
                                    updateTaskViewModel.longitude = selectedTask?.longitude ?? ""
//                                    updateTaskViewModel.value = selectedTask?.value ?? FilterTaskValue(convertedAmountInUSD: nil, currency: "", amount: 0)
                                    updateTaskViewModel.taskValue = TaskValue(amount: selectedTask?.value?.amount, currency: selectedTask?.value?.currency)
                                    updateTaskViewModel.taskVolume = selectedTask?.taskVolume ?? 0
                                    updateTaskViewModel.tagLogs = selectedTask?.tagLogs ?? []
                                    
                                    
                                    try await updateTaskViewModel.updateTaskStatus()
                                    
                                    if NetworkManager.shared.statusCode == 400 {
                                        currentDetent = .fraction(0.05)
                                        showWarningPopup = true
                                    }
                                    else{
                                        taskStatus[selectedTask?.id ?? ""] = 1
                                        refreshScreen.toggle()
                                    }
                                    
                                }
                            }
                            .padding()
                        }
                        else if taskStatus[selectedTask?.id ?? ""] == 1 || taskStatus[selectedTask?.id ?? ""] == 3 {
                            RedThinButton(text: "Pause") {
                                //TODO: To pause the task
                                Task {
                                    updateTaskViewModel.taskID = selectedTask?.id ?? ""
                                    updateTaskViewModel.status = 2
                                    updateTaskViewModel.currentDateTime = FormatterHelper.shared.getCurrentDateTimeFormatted()
                                    updateTaskViewModel.latitude = selectedTask?.latitude ?? ""
                                    updateTaskViewModel.longitude = selectedTask?.longitude ?? ""
//                                    updateTaskViewModel.value = selectedTask?.value ?? FilterTaskValue(convertedAmountInUSD: nil, currency: "", amount: 0)
                                    updateTaskViewModel.taskValue = TaskValue(amount: selectedTask?.value?.amount, currency: selectedTask?.value?.currency)
                                    updateTaskViewModel.taskVolume = selectedTask?.taskVolume ?? 0
                                    updateTaskViewModel.tagLogs = selectedTask?.tagLogs ?? []
                                    
                                    
                                    
                                    try await updateTaskViewModel.updateTaskStatus()
                                    
                                    if NetworkManager.shared.statusCode == 400 {
                                        showWarningPopup = true
                                    }else{
                                        taskStatus[selectedTask?.id ?? ""] = 2
                                        refreshScreen.toggle()
                                    }
                                    
                                }
                                
                            }
                        }
                        else if taskStatus[selectedTask?.id ?? ""] == 2 {
                            RedThinButton(text: "Resume") {
                                //TODO: To resume the task
                                Task {
                                    updateTaskViewModel.taskID = selectedTask?.id ?? ""
                                    updateTaskViewModel.status = 3
                                    updateTaskViewModel.currentDateTime = FormatterHelper.shared.getCurrentDateTimeFormatted()
                                    updateTaskViewModel.latitude = selectedTask?.latitude ?? ""
                                    updateTaskViewModel.longitude = selectedTask?.longitude ?? ""
//                                    updateTaskViewModel.value = selectedTask?.value ?? FilterTaskValue(convertedAmountInUSD: nil, currency: "", amount: 0)
                                    updateTaskViewModel.taskValue = TaskValue(amount: selectedTask?.value?.amount, currency: selectedTask?.value?.currency)
                                    updateTaskViewModel.taskVolume = selectedTask?.taskVolume ?? 0
                                    updateTaskViewModel.tagLogs = selectedTask?.tagLogs ?? []
                                    
                                    
                                    try await updateTaskViewModel.updateTaskStatus()
                                    
                                    if NetworkManager.shared.statusCode == 400 {
                                        showWarningPopup = true
                                    }
                                    else{
                                        taskStatus[selectedTask?.id ?? ""] = 3
                                        refreshScreen.toggle()
                                    }
                                }
                            }
                        }
                        
                        
                    }
                    .padding(.horizontal)
                    .frame(height: 250, alignment: .top)
                    .frame(maxWidth: .infinity)
                    .background(Color.white)
                    .padding(.top, 50)
                }
                else if sheetHeight == 0.99  {
                    TaskRescheduleDetailSheetView(tagViewModel: tagViewModel, cameraViewModel: cameraViewModel, uploadFileViewModel: uploadFileViewModel, updateTaskViewModel: updateTaskViewModel, sheetHeight: $sheetHeight, currentDetent: $currentDetent, showTaskRescheduleSheet: $showTaskRescheduleSheet, taskVolume: $taskVolume, taskValue: $taskValue, showCurrencyPopup: $showCurrencyPopup, selectedCurrency: $selectedCurrency, showCamera: $showCamera, showImagePreview: $showImagePreview, selectedImage: $selectedImage, savedImageURLs: $savedImageURLs, savedImages: $savedImages, showCalendar: $showCalendar, startDate: $startDate, endDate: $endDate, setStartDate: $setStartDate, setEndDate: $setEndDate, rescheduleStartTime: $rescheduleStartTime, rescheduleStopTime: $rescheduleStopTime, startTime: $startTime, stopTime: $stopTime, selectedTask: $selectedTask, taskStatus: $taskStatus, isDismiss: $isDismiss, refreshScreen: $refreshScreen)
                }
                
            }
            
            //MARK: Currency Popup
            if showCurrencyPopup {
                ModalOverlayView(dismissOnBackgroundTap: {
                    showCurrencyPopup.toggle()
                }) {
                    CurrencyPopupView(selectedCurrency: $selectedCurrency, showCurrencyPopup: $showCurrencyPopup)
                }
            }
            
            //MARK: Calendar
            if showCalendar {
                ModalOverlayView(dismissOnBackgroundTap: {
                    showCalendar.toggle()
                    setStartDate = false
                }) {
                    RescheduleCalendarView(dateViewModel: dateViewModel, startDate: $startDate, endDate: $endDate, setStartDate: $setStartDate, setEndDate: $setEndDate, rescheduleStartTime: $rescheduleStartTime, rescheduleStopTime: $rescheduleStopTime, startTime: $startTime, stopTime: $stopTime, showCalendar: $showCalendar)
                }
            }

            //MARK: Image Preview
            if showImagePreview {
                ZStack {
                    Color.black.opacity(0.5)
                        .ignoresSafeArea()
                        .onTapGesture {
                            showImagePreview = false
                        }

                    if let selectedImage = selectedImage {
                        ImagePreviewView(image: selectedImage)
                            .frame(width: UIScreen.main.bounds.width * 0.8, height: UIScreen.main.bounds.height * 0.6)
                            .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
                            .onTapGesture {
                                showImagePreview = false
                            }
                    }
                }
            }
            
            //MARK: show time picker
            if dateViewModel.showPicker {
                ModalOverlayView(dismissOnBackgroundTap: {
                    dateViewModel.showPicker.toggle()
                }) {
                    TimePickerView(dateViewModel: dateViewModel, startTime: $startTime, stopTime: $stopTime)
                }
            }
            
        }
        .onAppear {
//            if let startTime = selectedTask?.startTime {
//                self.startTime = FormatterHelper.shared.checkTimeFormatter(from: startTime) ?? ""
//            }
//            if let stopTime = selectedTask?.endTime {
//                self.stopTime = FormatterHelper.shared.checkTimeFormatter(from: stopTime) ?? ""
//            }
        }
        .onChange(of: currentDetent) { _, newValue in
            // Optionally, you can update the sheetHeight state based on the currentDetent
            switch newValue {
            case .fraction(0.05):
                sheetHeight = 0.05
            case .fraction(0.3):
                sheetHeight = 0.3
            case .fraction(0.99):
                sheetHeight = 0.99
            default:
                break
            }
        }
        
    }
}

struct TaskRescheduleDetailSheetView: View {
    
    @ObservedObject var tagViewModel: TagViewModel
    @ObservedObject var cameraViewModel: CameraViewModel
    @ObservedObject var uploadFileViewModel: UploadFilesViewModel
    @ObservedObject var updateTaskViewModel: UpdateTaskViewModel
    
    //sheetManipulation
    @Binding var sheetHeight: CGFloat
    @Binding var currentDetent: PresentationDetent
    @Binding var showTaskRescheduleSheet: Bool
    
    @Binding var taskVolume: String
    @Binding var taskValue: String
    @Binding var showCurrencyPopup: Bool
    @Binding var selectedCurrency: String
   
    @State private var showPause: Bool = false
    
    //Camera
    @Binding var showCamera: Bool
    @Binding var showImagePreview: Bool
    @Binding var selectedImage: UIImage?
    @Binding var savedImageURLs: [URL]
    @Binding var savedImages: [SavedImage]
    
    //Calendar
    @Binding var showCalendar: Bool
    // for rescheduling
    @Binding var startDate: String?
    @Binding var endDate: String?
    @Binding var setStartDate: Bool?
    @Binding var setEndDate: Bool?
    
    @Binding var rescheduleStartTime: String?
    @Binding var rescheduleStopTime: String?
    
    //Time
    @Binding var startTime: String
    @Binding var stopTime: String
    
//    @State private var testText: String = ""
    
    //DATA
    @Binding var selectedTask: FilterTaskListResponseData?
    @State private var selectedStage: TagResponseData?
    @State private var selectedStageTitle: String?
    @Binding var taskStatus: [String : Int]  // for task status "start", "pause", "resume"
    
    //TO Finish the task
    @State private var showFinishAlert: Bool = false
    
    
    //dismiss the view when the task is updated
    @Binding var isDismiss: Bool
    
    //Warning
    @State private var showWarningPopup: Bool = false
    
    //To refresh the taskList after updating status
    @Binding var refreshScreen: Bool
    
    var body: some View {
        ZStack {
            ScrollView(.vertical, showsIndicators: false) {
                VStack(spacing: AppSpacing.stackSpacingMedium) {
                    
                    Text(selectedTask?.taskName ?? "Task")
                        .font(AppFont.primary(size: AppFont.Size.title3, weight: AppFont.Weight.regular))
                        .fontWeight(AppFont.Weight.semibold)
                        .foregroundStyle(Color.addAddressText)
                        .lineLimit(2)
                        .multilineTextAlignment(.center)
                        .frame(maxWidth: .infinity)
                    
                    //UserInfo
                    VStack(spacing: AppSpacing.stackSpacingDefault) {
                        //NAME
                        HStack {
                            Circle()
                                .fill(
                                    .shadow(.inner(color: Color.taskSearchBar.opacity(0.5), radius: 4))
                                )
                                .foregroundStyle(Color.white)
                                .frame(width: 30, height: 30)
                                .overlay {
                                    Image(.profileIcon)
                                }
                            
                            Text(selectedTask?.clientName ?? "")
                                .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                .fontWeight(AppFont.Weight.medium)
                                .foregroundStyle(Color.subText)
                                .lineLimit(1)
                                .truncationMode(.tail)
                            
                            Spacer()
                        }
                        
                        //Timing
                        HStack {
                            Circle()
                                .fill(
                                    .shadow(.inner(color: Color.taskSearchBar.opacity(0.5), radius: 4))
                                )
                                .foregroundStyle(Color.white)
                                .frame(width: 30, height: 30)
                                .overlay {
                                    Image(.clock)
                                        .resizable()
                                        .aspectRatio(contentMode: .fit)
                                        .frame(width: 16.3, height: 16.3)
                                }
                            
                            //Time
                            HStack(spacing: 0) {
                                if let startTime = rescheduleStartTime {
                                    Text(startTime)
                                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                        .fontWeight(AppFont.Weight.semibold)
                                        .foregroundStyle(Color.primaryButton1)
                                }
                                else if let startTime = selectedTask?.startTime {
                                    Text(FormatterHelper.shared.checkTimeFormatter(from: startTime) ?? "")
    //                                    Text(self.startTime)
                                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                        .fontWeight(AppFont.Weight.semibold)
                                        .foregroundStyle(Color.primaryButton1)
                                }else{
                                    Text("__")
                                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                        .fontWeight(AppFont.Weight.semibold)
                                        .foregroundStyle(Color.primaryButton1)
                                }
                                
                                Text(" - ")
                                
                                if let stopTime = rescheduleStopTime {
                                    Text(stopTime)
                                            .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                            .fontWeight(AppFont.Weight.semibold)
                                            .foregroundStyle(Color.primaryButton1)
                                }
                                else if let endTime = selectedTask?.endTime {
                                    Text(FormatterHelper.shared.checkTimeFormatter(from: endTime) ?? "")
    //                                    Text(stopTime)
                                            .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                            .fontWeight(AppFont.Weight.semibold)
                                            .foregroundStyle(Color.primaryButton1)
                                }else{
                                    Text("__")
                                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                        .fontWeight(AppFont.Weight.semibold)
                                        .foregroundStyle(Color.primaryButton1)
                                }
                            }
                            .lineLimit(1)
                            
                            
                            Spacer()
                        }
                        
                        //Address
                        HStack {
                            Circle()
                                .fill(
                                    .shadow(.inner(color: Color.taskSearchBar.opacity(0.5), radius: 4))
                                )
                                .foregroundStyle(Color.white)
                                .frame(width: 30, height: 30)
                                .overlay {
                                    Image(.locationPointerBlueIcon)
                                }
                            
                            HStack(spacing: 0) {
                                if let address1 = selectedTask?.address1 {
	                                    Text(address1)
	                                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
	                                        .fontWeight(AppFont.Weight.medium)
	                                        .foregroundStyle(Color.subText)
                                            .lineLimit(2)
                                            .fixedSize(horizontal: false, vertical: true)
	                                }else if let address2 = selectedTask?.address2 {
	                                    Text(address2)
	                                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
	                                        .fontWeight(AppFont.Weight.medium)
	                                        .foregroundStyle(Color.subText)
                                            .lineLimit(2)
                                            .fixedSize(horizontal: false, vertical: true)
	                                }
	                            }
                            
                            
                            Spacer()
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .topLeading)
                    
                   
                    //MARK: Edit here
                    VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
                        Text("Stage of Task")
                            .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                            .fontWeight(AppFont.Weight.semibold)
                            .foregroundStyle(Color.subText)
                        
                        TaskStageDropDownView(selectedStage: $selectedStage, options: tagViewModel.tagDataList, selectedStageTitle: $selectedStageTitle)
                            .zIndex(1)
                        
                        Text("Task Volume")
                            .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                            .fontWeight(AppFont.Weight.semibold)
                            .foregroundStyle(Color.subText)
                            .padding(.top, AppSpacing.xs)
                        
                        RescheduleTextFieldView(text: $taskVolume, placeholder: "Task Volume")
                            .keyboardType(.numberPad)
                            .toolbarDoneButton()
                        
                        
                        Text("Task Value")
                            .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                            .fontWeight(AppFont.Weight.semibold)
                            .foregroundStyle(Color.subText)
                            .padding(.top, AppSpacing.xs)
                        
                        VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
                            currencyPickerButton
                                .frame(maxWidth: .infinity)

                            taskValueField
                                .frame(maxWidth: .infinity)
                        }
                        
                        LazyVGrid(
                            columns: [
                                GridItem(.flexible(), spacing: AppSpacing.stackSpacingMedium),
                                GridItem(.flexible(), spacing: AppSpacing.zero)
                            ],
                            spacing: AppSpacing.stackSpacingMedium
                        ) {
                            Button {
                                withAnimation {
                                    showCalendar.toggle()
                                    setStartDate = true
                                }
                            } label: {
                                RescheduleTaskTime(fillsAvailableWidth: true)
                            }
                            .buttonStyle(.plain)
                            .accessibilityLabel("Reschedule task")

                            Button {
                                showTaskRescheduleSheet = false

                                if cameraViewModel.savedImages.count < 4 {
                                    showCamera.toggle()
                                    AppLog.debug("Camera btn tapped")
                                } else {
                                    //TODO: Show alert
                                }
                            } label: {
                                AddPictureView(fillsAvailableWidth: true)
                            }
                            .buttonStyle(.plain)
                            .accessibilityLabel("Add picture")
                        }
                        .padding(.vertical, AppSpacing.sm)
                        .onChange(of: savedImageURLs) { _, _ in
                            Task {
                                AppLog.debug(savedImageURLs)

                                if !savedImageURLs.isEmpty {
                                    for image in savedImages {
                                        if let url = image.url {
                                            uploadFileViewModel.selectedImageURLs.append(url)
                                            await uploadFileViewModel.uploadImages()
                                        }

                                        uploadFileViewModel.selectedImageURLs.removeAll()
                                    }
                                }

                                AppLog.debug(uploadFileViewModel.fetchedURL)
                            }
                        }
                            
                            //MARK: Pics Preview
                            if !cameraViewModel.savedImages.isEmpty {
                                HStack {
                                    Text("Warning:")
                                        .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                                        .fontWeight(AppFont.Weight.semibold)
                                    Text("You can add up to 4 images (\(cameraViewModel.savedImages.count)/4 selected)")
                                        .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                                        .fontWeight(AppFont.Weight.medium)
                                }
                                .foregroundStyle(Color.absent)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .fixedSize(horizontal: false, vertical: true)
                                
                                //MARK: Captured Image Small Preview
                                CaptureImageSmallPreview(cameraViewModel: cameraViewModel, selectedImage: $selectedImage, showImagePreview: $showImagePreview, savedImageURLs: $savedImageURLs)
                                
                            }
                            
                            
                            //MARK: Update Task Buttons
                            
                            PrimaryThinButton(text: "Update Task") {
                                //TODO: Update the task
                                Task {
                                    updateTaskViewModel.taskID = selectedTask?.id ?? ""
                                    updateTaskViewModel.clientID = selectedTask?.clientID ?? ""
                                    updateTaskViewModel.taskName = selectedTask?.taskName ?? ""
                                    
                                    if let startDate = startDate, let rescheduleStartTime = rescheduleStartTime {
                                        updateTaskViewModel.startTime = FormatterHelper.shared.formatDateAndTime(dateString: startDate, timeString: rescheduleStartTime) ?? ""
                                    }
                                    if let startDate = startDate, let rescheduleStopTime = rescheduleStopTime {
                                        updateTaskViewModel.endTime = FormatterHelper.shared.formatDateAndTime(dateString: startDate, timeString: rescheduleStopTime) ?? ""
                                    }
                                    
                                    updateTaskViewModel.taskDescription = selectedTask?.taskDescription ?? ""
                                    
                                    if let startDate = startDate {
                                        updateTaskViewModel.date = startDate
                                    }
                                    
                                    updateTaskViewModel.taskValue = TaskValue(amount: selectedTask?.value?.amount, currency: selectedTask?.value?.currency)
                                    
                                    updateTaskViewModel.taskVolume = selectedTask?.taskVolume ?? 0
                                    
//                                    AppLog.debug("Up :\(updateTaskViewModel.startTime)")
//                                    AppLog.debug("Up :\(updateTaskViewModel.endTime)")
                                    
                                    
                                    try await updateTaskViewModel.updateTask()
                                    
                                    if NetworkManager.shared.statusCode == 200 {
                                        currentDetent = .fraction(0.05)
                                        
                                        isDismiss = true
                                    }else{
                                        showWarningPopup.toggle()
                                    }
                                }
                            }
                            .disableWithOpacity(startDate == nil || rescheduleStartTime == nil || rescheduleStopTime == nil)

                            HStack(spacing: AppSpacing.stackSpacingDefault) {
//                                if showPause {
//                                    RedBorderButton(text: "Pause") {
//                                        //TODO: to pause the task
//                                    }
//                                }else{
//                                    RedThinButton(text: "Resume") {
//                                        //TODO: To resume the task
//                                    }
//                                }
                                //MARK: Button
                                if taskStatus[selectedTask?.id ?? ""] == 0 {
                                    PrimaryThinButton(text: "Start") {
                                        //TODO: To start the task
                                        Task {
                                            updateTaskViewModel.taskID = selectedTask?.id ?? ""
                                            updateTaskViewModel.status = 1
                                            updateTaskViewModel.currentDateTime = FormatterHelper.shared.getCurrentDateTimeFormatted()
                                            updateTaskViewModel.latitude = selectedTask?.latitude ?? ""
                                            updateTaskViewModel.longitude = selectedTask?.longitude ?? ""
//                                            updateTaskViewModel.value = selectedTask?.value ?? FilterTaskValue(convertedAmountInUSD: nil, currency: "", amount: 0)
                                            updateTaskViewModel.taskValue = TaskValue(amount: selectedTask?.value?.amount, currency: selectedTask?.value?.currency)
                                            updateTaskViewModel.taskVolume = selectedTask?.taskVolume ?? 0
                                            updateTaskViewModel.tagLogs = selectedTask?.tagLogs ?? []
                                            
                                            
                                            try await updateTaskViewModel.updateTaskStatus()
                                            
                                            if NetworkManager.shared.statusCode == 400 {
                                                showWarningPopup = true
                                            }
                                            else{
                                                taskStatus[selectedTask?.id ?? ""] = 1
                                                refreshScreen.toggle()
                                            }
                                            
                                        }
                                    }
                                }
                                else if taskStatus[selectedTask?.id ?? ""] == 1 || taskStatus[selectedTask?.id ?? ""] == 3 {
                                    RedThinButton(text: "Pause") {
                                        //TODO: To pause the task
                                        Task {
                                            updateTaskViewModel.taskID = selectedTask?.id ?? ""
                                            updateTaskViewModel.status = 2
                                            updateTaskViewModel.currentDateTime = FormatterHelper.shared.getCurrentDateTimeFormatted()
                                            updateTaskViewModel.latitude = selectedTask?.latitude ?? ""
                                            updateTaskViewModel.longitude = selectedTask?.longitude ?? ""
//                                            updateTaskViewModel.value = selectedTask?.value ?? FilterTaskValue(convertedAmountInUSD: nil, currency: "", amount: 0)
                                            updateTaskViewModel.taskValue = TaskValue(amount: selectedTask?.value?.amount, currency: selectedTask?.value?.currency)
                                            updateTaskViewModel.taskVolume = selectedTask?.taskVolume ?? 0
                                            updateTaskViewModel.tagLogs = selectedTask?.tagLogs ?? []
                                            
                                            
                                            
                                            try await updateTaskViewModel.updateTaskStatus()
                                            
                                            if NetworkManager.shared.statusCode == 400 {
                                                showWarningPopup = true
                                            }else{
                                                taskStatus[selectedTask?.id ?? ""] = 2
                                                refreshScreen.toggle()
                                            }
                                            
                                        }
                                    }
                                }
                                else if taskStatus[selectedTask?.id ?? ""] == 2 {
                                    RedThinButton(text: "Resume") {
                                        //TODO: To resume the task
                                        Task {
                                            updateTaskViewModel.taskID = selectedTask?.id ?? ""
                                            updateTaskViewModel.status = 3
                                            updateTaskViewModel.currentDateTime = FormatterHelper.shared.getCurrentDateTimeFormatted()
                                            updateTaskViewModel.latitude = selectedTask?.latitude ?? ""
                                            updateTaskViewModel.longitude = selectedTask?.longitude ?? ""
//                                            updateTaskViewModel.value = selectedTask?.value ?? FilterTaskValue(convertedAmountInUSD: nil, currency: "", amount: 0)
                                            updateTaskViewModel.taskValue = TaskValue(amount: selectedTask?.value?.amount, currency: selectedTask?.value?.currency)
                                            updateTaskViewModel.taskVolume = selectedTask?.taskVolume ?? 0
                                            updateTaskViewModel.tagLogs = selectedTask?.tagLogs ?? []
                                            
                                            
                                            try await updateTaskViewModel.updateTaskStatus()
                                            
                                            if NetworkManager.shared.statusCode == 400 {
                                                showWarningPopup = true
                                            }
                                            else{
                                                taskStatus[selectedTask?.id ?? ""] = 3
                                                refreshScreen.toggle()
                                            }
                                        }
                                    }
                                }
                                
                                PrimaryThinButton(text: "Finish") {
                                    //TODO: To update the task
                                    
                                    showFinishAlert.toggle()
                                }
                            }
                        
                        
                                                
                    }
                    .padding(.bottom, AppSpacing.xxl)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    
                    
                }
                .padding(.horizontal, AppSpacing.screenHorizontalPadding)
                .padding(.top, AppSpacing.lg)
                .frame(maxWidth: .infinity, alignment: .top)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
            .background(Color.white)
            .padding(.top, AppSpacing.lg)
            .onAppear {
                if let volume = selectedTask?.taskVolume {
                    taskVolume = "\(volume)"
                }
                if tagViewModel.tagDataList.isEmpty {
                    Task {
                        try await tagViewModel.getTags()
                    }
                }

                //To manager the click/selectedImages
                savedImageURLs.removeAll()
                savedImages.removeAll()

                savedImageURLs = cameraViewModel.getCapturedImageURLs()
                savedImages = cameraViewModel.savedImages

                AppLog.debug("IMage URLs")
                AppLog.debug(savedImageURLs)
            }
            
            //MARK: Warning Popup
            if showWarningPopup {
                ModalOverlayView(dismissOnBackgroundTap: {
                    withAnimation {
                        showWarningPopup.toggle()
                    }
                }) {
                    WarningPopupView(titleText: "Try Again", description: NetworkManager.shared.responseMessage, showWarningPopup: $showWarningPopup)
                }
            }
            
        }
        .alert(isPresented: $showFinishAlert) {
                // MARK: To complete the task
                Alert(title: Text("Do you really want to complete the task ?"),
                      primaryButton: .default(Text("Complete"), action: {
                    
                    withAnimation {
                        //TODO: to remove with animation
                    }
                    
                    updateTaskViewModel.taskID = selectedTask?.id ?? ""
                    updateTaskViewModel.status = 4
                    updateTaskViewModel.currentDateTime = FormatterHelper.shared.getCurrentDateTimeFormatted()
                    updateTaskViewModel.latitude = selectedTask?.latitude ?? ""
                    updateTaskViewModel.longitude = selectedTask?.longitude ?? ""
//                    updateTaskViewModel.value = selectedTask?.value ?? FilterTaskValue(convertedAmountInUSD: nil, currency: "INR", amount: 0)
                    updateTaskViewModel.taskValue = TaskValue(amount: selectedTask?.value?.amount, currency: selectedTask?.value?.currency)
                    updateTaskViewModel.taskVolume = selectedTask?.taskVolume ?? 0
                    updateTaskViewModel.tagLogs = selectedTask?.tagLogs ?? []
                    
                    Task {
                        try await updateTaskViewModel.updateTaskStatus()
                        
                        if NetworkManager.shared.statusCode == 200 {
                            taskStatus[selectedTask?.id ?? ""] = 4
                        }else {
                            showWarningPopup.toggle()
                        }
                    }
                }),
                      secondaryButton: .cancel())
        }

        
        
    }

    private var currencyPickerButton: some View {
        Button {
            showCurrencyPopup.toggle()
        } label: {
            HStack(alignment: .center, spacing: AppSpacing.iconTextSpacing) {
                Text(selectedCurrency)
                    .lineLimit(2)
                    .fixedSize(horizontal: false, vertical: true)
                    .frame(maxWidth: .infinity, alignment: .leading)

                Spacer(minLength: AppSpacing.xs)

                Image(systemName: "chevron.down")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 10, height: 6)
                    .layoutPriority(1)
            }
            .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.medium))
            .foregroundStyle(Color.white)
            .padding(.horizontal, AppSpacing.md)
            .padding(.vertical, AppSpacing.sm)
            .frame(maxWidth: .infinity)
            .frame(minHeight: AppLayout.minimumTouchTarget, alignment: .center)
            .background(Color.taskSearchBar)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
        }
        .buttonStyle(.plain)
        .accessibilityLabel("Select currency")
    }

    private var taskValueField: some View {
        RescheduleTextFieldView(text: $taskValue, placeholder: "Enter Task Value")
            .keyboardType(.numberPad)
            .toolbarDoneButton()
    }
}

//#Preview {
//    TaskRescheduleSheetView(sheetHeight: .constant(0.99), currentDetent: .constant(.fraction(0.99)))
//}
