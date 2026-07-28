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
    @State private var startDateTime: Date = Date()
    @State private var stopDateTime: Date = Calendar.current.date(byAdding: .hour, value: 1, to: Date()) ?? Date()
    
    //For camera
    @State private var showCamera: Bool = false
    @State private var showImagePreview: Bool = false
    @State private var selectedImage: UIImage? = nil
    @State private var savedImageURLs: [URL] = []
    @State private var savedImages: [SavedImage] = []
    
    //
    @State private var showSelectClient: Bool = false
    
    @State private var selectedClientData: ClientListResponseData?
    
    //warning
    @State private var showWarningPopup: Bool = false
    @State private var showCameraPermissionAlert: Bool = false

    private let currencyOptions = CurrencyPopupView.availableCurrencies

    private var isAddTaskDisabled: Bool {
        taskName.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
            || selectedClientData == nil
            || description.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
            || stopDateTime <= startDateTime
    }

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
                                        UIApplication.shared.dismissKeyboard()
                                        withAnimation {
                                            showSelectClient.toggle()
                                        }
                                    }
                                    
                                
                                Text("Schedule")
                                    .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                    .fontWeight(AppFont.Weight.semibold)
                                    .foregroundStyle(Color.subText)

                                HStack(spacing: AppSpacing.stackSpacingDefault) {
                                    TaskTimePickerField(title: "Start", selection: $startDateTime)
                                        .onChange(of: startDateTime) { _, _ in
                                            normalizeScheduleAfterStartChange()
                                        }

                                    TaskTimePickerField(title: "End", selection: $stopDateTime)
                                        .onChange(of: stopDateTime) { _, _ in
                                            syncScheduleTimes()
                                        }
                                }
                                
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
                                    .toolbarDoneButton()
                                
                                Text("Task Value")
                                    .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                    .fontWeight(AppFont.Weight.semibold)
                                    .foregroundStyle(Color.subText)
                                
                                HStack(spacing: AppSpacing.stackSpacingDefault) {
                                    Menu {
                                        ForEach(currencyOptions, id: \.self) { currencyCode in
                                            Button {
                                                selectedCurrency = currencyCode
                                            } label: {
                                                Text("\(CurrencyPopupView.currencyName(currencyCode: currencyCode)) (\(currencyCode))")
                                            }
                                        }
                                    } label: {
                                        HStack(spacing: AppSpacing.iconTextSpacing) {
                                            Text(selectedCurrency)
                                                .lineLimit(1)
                                                .minimumScaleFactor(0.85)

                                            Image(systemName: "chevron.up.chevron.down")
                                                .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.semibold))
                                        }
                                        .font(AppFont.primary(size: AppFont.Size.subheadline, weight: AppFont.Weight.medium))
                                        .foregroundStyle(Color.headingText)
                                        .frame(width: 112)
                                        .frame(minHeight: AppLayout.textFieldHeight)
                                        .background(Color.white)
                                        .overlay {
                                            RoundedRectangle(cornerRadius: AppRadius.small)
                                                .stroke(Color.taskSearchBar.opacity(0.28), lineWidth: 1)
                                        }
                                        .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
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
                                                UIApplication.shared.dismissKeyboard()

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
                                    UIApplication.shared.dismissKeyboard()
                                    syncScheduleTimes()

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
                            .disableWithOpacity(isAddTaskDisabled)
                            
                        }
                    }
                    .padding(.top)
                    .scrollDismissesKeyboard(.interactively)
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
            
            //MARK: Warning
            if showWarningPopup {
                if stopDateTime <= startDateTime {
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
            syncScheduleTimes()
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

    private static let taskTimeFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.dateFormat = "HH:mm"
        return formatter
    }()

    private static let taskDateTimeFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        return formatter
    }()

    private func normalizeScheduleAfterStartChange() {
        if stopDateTime <= startDateTime {
            stopDateTime = Calendar.current.date(byAdding: .hour, value: 1, to: startDateTime)
                ?? startDateTime.addingTimeInterval(3600)
        }

        syncScheduleTimes()
    }

    private func syncScheduleTimes() {
        startTime = Self.taskTimeFormatter.string(from: startDateTime)
        stopTime = Self.taskTimeFormatter.string(from: stopDateTime)
        createTaskViewModel.startTime = Self.taskDateTimeFormatter.string(from: startDateTime)
        createTaskViewModel.endTime = Self.taskDateTimeFormatter.string(from: stopDateTime)
    }
}

private struct TaskTimePickerField: View {
    let title: String
    @Binding var selection: Date

    var body: some View {
        VStack(alignment: .leading, spacing: AppSpacing.xs) {
            Text(title)
                .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.medium))
                .foregroundStyle(Color.addressText2)

            DatePicker(title, selection: $selection, displayedComponents: .hourAndMinute)
                .labelsHidden()
                .datePickerStyle(.compact)
                .tint(Color.primaryButton1)
                .frame(maxWidth: .infinity, alignment: .leading)
        }
        .padding(.horizontal, AppSpacing.md)
        .padding(.vertical, AppSpacing.sm)
        .frame(maxWidth: .infinity, minHeight: AppLayout.textFieldHeight, alignment: .leading)
        .background(Color.white)
        .overlay {
            RoundedRectangle(cornerRadius: AppRadius.small)
                .stroke(Color.taskSearchBar.opacity(0.28), lineWidth: 1)
        }
        .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
    }
}

#Preview {
    AddTaskView()
        .environmentObject(CalendarViewModel())
}
