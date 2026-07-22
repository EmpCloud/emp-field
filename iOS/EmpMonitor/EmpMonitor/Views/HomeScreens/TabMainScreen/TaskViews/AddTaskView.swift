//
//  AddTaskView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct AddTaskView: View {
    
    @Environment(\.dismiss) var dismiss
    
    @StateObject private var cameraViewModel = CameraViewModel()
    @StateObject private var createTaskViewModel = CreateTaskViewModel()
    @StateObject private var uploadFileViewModel = UploadFilesViewModel()
    
    @StateObject private var dateViewModel = DateViewModel()
    
    //Time Picker
    @State private var showPicker: Bool = false
    
    @State private var taskName: String = ""
    @State private var description: String = ""
    @State private var selectedCurrency: String = "INR"
    @State private var taskValue: String = "0"
    @State private var taskVolume: String = "0"
    
    //For uploading PDF
    @State private var selectedPDF: [URL] = []
//    @State private var selectedPDFURLs: [String] = []
    @State private var isDocumentPickerPresented: Bool = false
    
    //For scheduling the task
    @State private var startTime: String = ""
    @State private var stopTime: String = ""
    
    //For camera
    @State private var showCamera: Bool = false
    @State private var showImagePreview: Bool = false
    @State private var selectedImage: UIImage? = nil
    @State private var savedImageURLs: [URL] = []
    @State private var savedImages: [SavedImage] = []
    
    //
    @State private var showCurrencyPopup: Bool = false
    @State private var showSelectClient: Bool = false
    @State private var showScheduleCalendar: Bool = false
    
    @State private var selectedClientData: ClientListResponseData?
    
    //warning
    @State private var showWarningPopup: Bool = false
    @State private var showCameraPermissionAlert: Bool = false

    var body: some View {
        
        ZStack{
            
            //MARK: BG Gradient
            LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                .ignoresSafeArea()
            
            //TODO: To create the content of the View
            
            RoundedRectangle(cornerRadius: 25.0)
                .fill(Color.rectangleBG)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .padding(.top)
                .ignoresSafeArea(edges: .bottom)
                .overlay(alignment: .top) {
                    ScrollView {
                        VStack {
                            Text("Add Task")
                                .font(AppFont.primary(size: AppFont.Size.title3, weight: AppFont.Weight.regular))
                                .fontWeight(AppFont.Weight.semibold)
                                .foregroundStyle(Color.headingText)
                                .padding(.top)
                            
                            VStack(alignment: .leading){
                                Text("Task Name")
                                    .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                    .fontWeight(AppFont.Weight.semibold)
                                    .foregroundStyle(Color.subText)
                                
                                AddTaskTextField(text: $taskName, placeholder: "Task Name")
                                
                                Text("Select Client")
                                    .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                    .fontWeight(AppFont.Weight.semibold)
                                    .foregroundStyle(Color.subText)
                                
                                TextFieldButton(placeholder: selectedClientData?.clientName ?? "Add Client", imageName: "chevron.right")
                                    .onTapGesture {
                                        withAnimation {
                                            showSelectClient.toggle()
                                        }
                                    }
                                    
                                
                                Text("Schedule ")
                                    .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                    .fontWeight(AppFont.Weight.semibold)
                                    .foregroundStyle(Color.subText)
                                
                                HStack {
                                    //Start Time
                                    HStack{
                                        if startTime != "" {
                                            Text("\(startTime)")
                                                .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                                                .fontWeight(AppFont.Weight.bold)
                                                .foregroundStyle(Color.subText)
                                        }else {
                                            Text("Start Time")
                                                .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                                                .fontWeight(AppFont.Weight.bold)
                                                .foregroundStyle(Color.subText)
                                        }
                                        

                                        Circle()
                                            .fill(Color.white)
                                            .frame(width: 35.46, height: 35.46)
                                            .overlay {
                                                Image(systemName: "clock")
                                                    .resizable()
                                                    .aspectRatio(contentMode: .fit)
                                                    .frame(width: 17.71, height: 17.71)
                                                    .foregroundStyle(Color.primaryButton1)
                                            }
                                    }
                                    .padding(.horizontal, 25)
                                    .padding(.vertical, 7)
                                    .background(Color.white)
                                    .clipShape(RoundedRectangle(cornerRadius: 6))
                                    .onTapGesture {
                                        dateViewModel.setStartTime = true
                                        dateViewModel.setStopTime = false
                                        dateViewModel.showPicker.toggle()
                                    }
                                    .onChange(of: startTime) { _, newStartTime in
                                        createTaskViewModel.startTime = FormatterHelper.shared.getDateTimeJoined(with: newStartTime) ?? FormatterHelper.shared.getCurrentDateTimeFormatted()
                                    }
//                                    ScheduleStartTimeView(startTime: $startTime)
//                                        .onTapGesture {
//                                            //setting type of timer
//                                            dateViewModel.setStartTime = true
//                                            dateViewModel.setStopTime = false
//                                            
//                                            //setting time as previous selected Time..
//                                            dateViewModel.setTime()
//                                            withAnimation {
//                                                dateViewModel.changeToMin = false
//                                                dateViewModel.showPicker.toggle()
//                                            }
//                                        }
                                    
                                    Spacer()
                                    
                                    //Stop Time
                                    HStack{
                                        if stopTime != "" {
                                            Text("\(stopTime)")
                                                .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                                                .fontWeight(AppFont.Weight.bold)
                                                .foregroundStyle(Color.subText)
                                        }else {
                                            Text("End Time")
                                                .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                                                .fontWeight(AppFont.Weight.bold)
                                                .foregroundStyle(Color.subText)
                                        }
                                        

                                        Circle()
                                            .fill(Color.white)
                                            .frame(width: 35.46, height: 35.46)
                                            .overlay {
                                                Image(systemName: "clock")
                                                    .resizable()
                                                    .aspectRatio(contentMode: .fit)
                                                    .frame(width: 17.71, height: 17.71)
                                                    .foregroundStyle(Color.primaryButton1)
                                            }
                                    }
                                    .padding(.horizontal, 25)
                                    .padding(.vertical, 7)
                                    .background(Color.white)
                                    .clipShape(RoundedRectangle(cornerRadius: 6))
                                    .onTapGesture {
                                        dateViewModel.setStopTime = true
                                        dateViewModel.setStartTime = false
                                        dateViewModel.showPicker = true
                                    }
                                    .onChange(of: stopTime) { _, newStopTime in
                                        if stopTime < startTime {
                                            showWarningPopup.toggle()
                                        }
                                        createTaskViewModel.endTime = FormatterHelper.shared.getDateTimeJoined(with: newStopTime) ?? FormatterHelper.shared.getCurrentDateTimeFormatted()
                                    }
//                                    ScheduleStopTimeView(stopTime: $stopTime)
//                                        .onTapGesture {
//                                            //setting type of timer
//                                            dateViewModel.setStartTime = false
//                                            dateViewModel.setStopTime = true
//                                            
//                                            //setting time as previous selected Time..
//                                            dateViewModel.setTime()
//                                            withAnimation {
//                                                dateViewModel.changeToMin = false
//                                                dateViewModel.showPicker.toggle()
//                                            }
//                                        }
                                    
                                }
                                
//                                TextFieldButton(placeholder: "00:00AM - 00:00AM ", imageName: "clock")
//                                    .onTapGesture {
//                                        showScheduleCalendar.toggle()
//                                    }
                                
                                Text("Description")
                                    .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                    .fontWeight(AppFont.Weight.semibold)
                                    .foregroundStyle(Color.subText)
                                
                                AddTaskTextEditor(selectedPDF: $selectedPDF, isDocumentPickerPresented: $isDocumentPickerPresented, descriptionText: $description)
                                    .onChange(of: selectedPDF) { _, _ in
                                        Task {
                                            //TODO: Upload the files
                                            
                                            // ensuring every url is remove to fill new one only(remain one)
                                            createTaskViewModel.selectedPDFURLs.removeAll()
                                            createTaskViewModel.files.removeAll()
                                            
                                            AppLog.debug("PDF URLS: \(createTaskViewModel.selectedPDFURLs)")
                                            
                                            if !selectedPDF.isEmpty{
                                                
                                                uploadFileViewModel.selectedPDF = selectedPDF
                                                await uploadFileViewModel.upload()
                                                
                                                createTaskViewModel.addLatestPDFURLs(uploadFileViewModel.fetchedURL)
                                                
                                                if let url = uploadFileViewModel.fetchedURL.first?.url {
                                                    createTaskViewModel.addToFiles(url: url)
                                                }
                                                
                                            }
                                            
                                        }
                                    }
                                
                                Text("Task Volume")
                                    .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                    .fontWeight(AppFont.Weight.semibold)
                                    .foregroundStyle(Color.subText)
                                    .padding(.top, 10)
//                                    .onTapGesture {
//                                        Task {
//                                            if !selectedPDF.isEmpty{
//                                                uploadFileViewModel.selectedPDF = selectedPDF
//                                                await uploadFileViewModel.upload()
//                                            }
//                                        }
//                                    }
                                
                                AddTaskTextField(text: $taskVolume, placeholder: "Task Volume")
                                    .keyboardType(.numberPad)
//                                    .toolbarDoneButton()
                                
                                Text("Task Value")
                                    .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                    .fontWeight(AppFont.Weight.semibold)
                                    .foregroundStyle(Color.subText)
                                
	                                HStack(spacing: AppSpacing.stackSpacingDefault) {
	                                    Button {
	                                        showCurrencyPopup.toggle()
	                                    } label: {
	                                        HStack(spacing: AppSpacing.iconTextSpacing) {
	                                            Text(selectedCurrency)
	                                                .lineLimit(1)
	                                                .minimumScaleFactor(0.85)

	                                            Image(systemName: "chevron.down")
	                                                .font(AppFont.primary(size: AppFont.Size.closeIcon, weight: AppFont.Weight.semibold))
	                                        }
	                                        .font(AppFont.primary(size: AppFont.Size.subheadline, weight: AppFont.Weight.regular))
	                                        .foregroundStyle(Color.white)
	                                        .frame(width: 110)
	                                        .frame(minHeight: AppLayout.textFieldHeight)
	                                        .background(Color.taskSearchBar)
	                                        .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
	                                    }
	                                    .buttonStyle(.plain)
	                                    .accessibilityLabel("Select currency")

	                                    AddTaskTextField(text: $taskValue, placeholder: "Enter Task Value")
	                                        .keyboardType(.numberPad)
	                                        .toolbarDoneButton()
	                                }
                                
                            }
                            .frame(maxWidth: .infinity, alignment: .topLeading)
                            .padding()
                            
                            
                            //MARK: Add Pic, Add Form
                            VStack{
                                //Pics
                                if !cameraViewModel.savedImages.isEmpty {
                                    HStack {
                                        Text("Warning:")
                                            .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.semibold))
                                        Text("You can only add upto 4 images (Image = \(cameraViewModel.savedImages.count))")
                                            .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.medium))
                                    }
                                    .foregroundStyle(Color.absent)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .padding(.horizontal)
                                    
                                    //MARK: Captured Image Small Preview
                                    CaptureImageSmallPreview(cameraViewModel: cameraViewModel, selectedImage: $selectedImage, showImagePreview: $showImagePreview, savedImageURLs: $savedImageURLs)
                                    
                                }
                                
                                AddPictureView()
                                    .onTapGesture {
//                                        withAnimation {
                                            Task {
                                                // to check camera permission here
                                                cameraViewModel.checkPermissions()
                                                
                                                if cameraViewModel.cameraAuthStatus {
                                                    if cameraViewModel.savedImages.count < 4 {
                                                        showCamera.toggle()
                                                    }else{
                                                        //TODO: Show alert
                                                    }
                                                }else {
                                                    showCameraPermissionAlert.toggle()
                                                    
                                                }
                                            }
                                            
                                            
//                                        }
                                    }
                                    .onChange(of: savedImageURLs) { _, _ in
                                        Task {
                                            //TODO: Upload the Images
                                            
                                            createTaskViewModel.selectedImageURLs.removeAll() // ensuring every url is remove to fill new one only(remain one)
                                            createTaskViewModel.images.removeAll() // removing the all Image
                                            
//                                            AppLog.debug("Images URLS: \(createTaskViewModel.selectedImageURLs)")
//                                            
//                                            AppLog.debug("SavedURLs: \(savedImageURLs)")
                                            
                                            if !savedImageURLs.isEmpty{
                                                
                                                for image in savedImages {
                                                    
                                                    if let url = image.url {
                                                        uploadFileViewModel.selectedImageURLs.append(url)
                                                        await uploadFileViewModel.uploadImages()
                                                    }
                                                    
                                                    
//                                                    AppLog.debug("Response Message: \(NetworkManager.shared.responseMessage)")
//                                                    AppLog.debug("Status Code: \(NetworkManager.shared.statusCode)")
//                                                    
//                                                    AppLog.debug("FetchURL: \(uploadFileViewModel.fetchedURL)")
                                                    
                                                    
                                                    if let url = uploadFileViewModel.fetchedURL.first?.url {
                                                        createTaskViewModel.addToImages(description: image.description, url: url)
                                                    }
                                                    
                                                    
                                                    createTaskViewModel.addLatestImageURLs(uploadFileViewModel.fetchedURL)
                                                    
                                                    uploadFileViewModel.selectedImageURLs.removeAll()
                                                }
//                                                uploadFileViewModel.selectedImageURLs = savedImageURLs
//                                                await uploadFileViewModel.uploadImages()
                                                
//                                                AppLog.debug("Response Message: \(NetworkManager.shared.responseMessage)")
//                                                AppLog.debug("Status Code: \(NetworkManager.shared.statusCode)")
                                                
//                                                createTaskViewModel.addLatestImageURLs(uploadFileViewModel.fetchedURL)
                                            }
                                            
                                            AppLog.debug("Images URLs: \(createTaskViewModel.selectedImageURLs)")
                                            
                                        }
                                    }
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.leading)
                            
                            
                            //MARK: Add task button
                            PrimaryButton(text: "Add Task") {
                                Task {
                                    createTaskViewModel.taskName = taskName
                                    createTaskViewModel.clientID = selectedClientData?.id ?? ""
                                    createTaskViewModel.taskDescription = description
                                    createTaskViewModel.value = Value(currency: selectedCurrency, amount: Int(taskValue) ?? 0, convertedAmountInUSD: nil)
                                    createTaskViewModel.taskVolume = Int(taskVolume) ?? 0
                                    createTaskViewModel.date = FormatterHelper.shared.getTodaysDate()

                                    try await createTaskViewModel.createTask()

                                    if NetworkManager.shared.statusCode == 200 {
                                        dismiss()
                                    } else {
                                        showWarningPopup.toggle()
                                    }
                                }
                            }
                            .padding()
                            .disableWithOpacity(taskName.isEmpty || selectedClientData == nil || description.isEmpty)
                            
                        }
                    }
                    .padding(.top)
                }
            
            //MARK: Currency Popup
            if showCurrencyPopup{
                ModalOverlayView(dismissOnBackgroundTap: {
                    showCurrencyPopup.toggle()
                }) {
                    CurrencyPopupView(selectedCurrency: $selectedCurrency, showCurrencyPopup: $showCurrencyPopup)
                }
            }
            
            //MARK: Calendar Popup
            if showScheduleCalendar {
                ModalOverlayView(dismissOnBackgroundTap: {
                    showScheduleCalendar.toggle()
                }) {
                    TaskCalendarView(startDate: .constant(""), endDate: .constant(""), setStartDate: .constant(false), setEndDate: .constant(false), showCalendar: $showScheduleCalendar)
                }
            }
            
            //MARK: ImagePreview Popup
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
                            .clipShape(RoundedRectangle(cornerRadius: 12))
                            .onTapGesture {
                                showImagePreview = false
                            }
                    }
                }
            }
            
            if dateViewModel.showPicker {
                ModalOverlayView(dismissOnBackgroundTap: {
                    dateViewModel.showPicker.toggle()
                }) {
                    TimePickerView(dateViewModel: dateViewModel, startTime: $startTime, stopTime: $stopTime)
                }
            }
            
            
            //MARK: Warning
            if showWarningPopup {
                if startTime == "" {
                    ModalOverlayView(dismissOnBackgroundTap: {
                        showWarningPopup.toggle()
                    }) {
                        WarningPopupView(titleText: "Try Again", description: "Enter start time first.", showWarningPopup: $showWarningPopup)
                    }
                }
                
                else if stopTime < startTime {
                    ModalOverlayView(dismissOnBackgroundTap: {
                        showWarningPopup.toggle()
                    }) {
                        WarningPopupView(titleText: "Try Again", description: "End time should be later than start time.", showWarningPopup: $showWarningPopup)
                    }
                }
                
                else {
                    ModalOverlayView(dismissOnBackgroundTap: {
                        showWarningPopup.toggle()
                    }) {
                        WarningPopupView(titleText: "Try Again", description: NetworkManager.shared.responseMessage, showWarningPopup: $showWarningPopup)
                    }

                }
            }
            
            
            //MARK: Permission Alert
            if showCameraPermissionAlert {
                ModalOverlayView(dismissOnBackgroundTap: {
                    showCameraPermissionAlert.toggle()
                }) {
                    LocationSettingWarningView(titleText: "Camera Permission Not Given", description: "Provide the camera permission in order to take picture for the task", showWarningPopup: $showCameraPermissionAlert)
                }
            }
           
        }
        
        
        .onAppear{
//            AppLog.debug("Image")
//            AppLog.debug(cameraViewModel.savedImages.description)
            
            savedImageURLs.removeAll()
            savedImages.removeAll()
            
            savedImageURLs = cameraViewModel.getCapturedImageURLs()
            savedImages = cameraViewModel.savedImages
            
            AppLog.debug("IMage URLs")
            AppLog.debug(savedImageURLs)
        }
        .navigationDestination(isPresented: $showSelectClient) {
            SelectClientView(selectedClient: $selectedClientData)
                .navigationBarBackButtonHidden()
        }
        .navigationDestination(isPresented: $showCamera) {
            CameraView(cameraViewModel: cameraViewModel).navigationBarBackButtonHidden()
        }
        .sheet(isPresented: $isDocumentPickerPresented, content: {
            DocumentPicker(selectedPDF: $selectedPDF)
        })
        .toolbar {
            ToolbarItem(placement: .topBarLeading) {
                BackButtonView()
                    .onTapGesture {
                        dismiss()
                        
                    }
            }
            ToolbarItem(placement: .principal) {
                Text("Task")
                    .font(AppFont.primary(size: AppFont.Size.navigationTitle, weight: AppFont.Weight.regular))
                    .fontWeight(AppFont.Weight.semibold)
                    .foregroundStyle(Color.white)
            }
        }
    }
}

#Preview {
    AddTaskView()
        .environmentObject(CalendarViewModel())
}
