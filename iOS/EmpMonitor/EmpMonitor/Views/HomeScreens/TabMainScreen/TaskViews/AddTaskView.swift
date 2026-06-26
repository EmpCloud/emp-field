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
                                .font(.system(size: 18, weight: .regular))
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.headingText)
                                .padding(.top)
                            
                            VStack(alignment: .leading){
                                Text("Task Name")
                                    .font(.system(size: 14, weight: .regular))
                                    .fontWeight(.semibold)
                                    .foregroundStyle(Color.subText)
                                
                                AddTaskTextField(text: $taskName, placeholder: "Task Name")
                                
                                Text("Select Client")
                                    .font(.system(size: 14, weight: .regular))
                                    .fontWeight(.semibold)
                                    .foregroundStyle(Color.subText)
                                
                                TextFieldButton(placeholder: selectedClientData?.clientName ?? "Add Client", imageName: "chevron.right")
                                    .onTapGesture {
                                        withAnimation {
                                            showSelectClient.toggle()
                                        }
                                    }
                                    
                                
                                Text("Schedule ")
                                    .font(.system(size: 14, weight: .regular))
                                    .fontWeight(.semibold)
                                    .foregroundStyle(Color.subText)
                                
                                HStack {
                                    //Start Time
                                    HStack{
                                        if startTime != "" {
                                            Text("\(startTime)")
                                                .font(.system(size: 12, weight: .regular))
                                                .fontWeight(.bold)
                                                .foregroundStyle(Color.subText)
                                        }else {
                                            Text("Start Time")
                                                .font(.system(size: 12, weight: .regular))
                                                .fontWeight(.bold)
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
                                                .font(.system(size: 12, weight: .regular))
                                                .fontWeight(.bold)
                                                .foregroundStyle(Color.subText)
                                        }else {
                                            Text("End Time")
                                                .font(.system(size: 12, weight: .regular))
                                                .fontWeight(.bold)
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
                                    .font(.system(size: 14, weight: .regular))
                                    .fontWeight(.semibold)
                                    .foregroundStyle(Color.subText)
                                
                                AddTaskTextEditor(selectedPDF: $selectedPDF, isDocumentPickerPresented: $isDocumentPickerPresented, descriptionText: $description)
                                    .onChange(of: selectedPDF) { _, _ in
                                        Task {
                                            //TODO: Upload the files
                                            
                                            // ensuring every url is remove to fill new one only(remain one)
                                            createTaskViewModel.selectedPDFURLs.removeAll()
                                            createTaskViewModel.files.removeAll()
                                            
                                            print("PDF URLS: \(createTaskViewModel.selectedPDFURLs)")
                                            
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
                                    .font(.system(size: 14, weight: .regular))
                                    .fontWeight(.semibold)
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
                                    .font(.system(size: 14, weight: .regular))
                                    .fontWeight(.semibold)
                                    .foregroundStyle(Color.subText)
                                
                                HStack(spacing: 0) {
                                    Rectangle()
                                        .fill(Color.taskSearchBar)
                                        .frame(width: 110, height: 46)
                                        .padding(.trailing, 10)
                                        .clipShape(RoundedRectangle(cornerRadius: 10))
                                        .overlay {
                                            HStack(spacing: 30){
                                                Text(selectedCurrency)
                                                Image(systemName: "chevron.down")
                                                    .resizable()
                                                    .aspectRatio(contentMode: .fit)
                                                    .frame(width: 12.32, height: 15.13)
                                            }
                                            .font(.system(size: 15, weight: .regular))
                                            .foregroundStyle(Color.white)
                                            .onTapGesture {
                                                showCurrencyPopup.toggle()
                                            }
                                        }
                                    AddTaskTextField(text: $taskValue, placeholder: "Enter Task Value")
                                        .offset(x: -10)
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
                                            .font(.system(size: 12, weight: .semibold))
                                        Text("You can only add upto 4 images (Image = \(cameraViewModel.savedImages.count))")
                                            .font(.system(size: 12, weight: .medium))
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
                                            
//                                            print("Images URLS: \(createTaskViewModel.selectedImageURLs)")
//                                            
//                                            print("SavedURLs: \(savedImageURLs)")
                                            
                                            if !savedImageURLs.isEmpty{
                                                
                                                for image in savedImages {
                                                    
                                                    if let url = image.url {
                                                        uploadFileViewModel.selectedImageURLs.append(url)
                                                        await uploadFileViewModel.uploadImages()
                                                    }
                                                    
                                                    
//                                                    print("Response Message: \(NetworkManager.shared.responseMessage)")
//                                                    print("Status Code: \(NetworkManager.shared.statusCode)")
//                                                    
//                                                    print("FetchURL: \(uploadFileViewModel.fetchedURL)")
                                                    
                                                    
                                                    if let url = uploadFileViewModel.fetchedURL.first?.url {
                                                        createTaskViewModel.addToImages(description: image.description, url: url)
                                                    }
                                                    
                                                    
                                                    createTaskViewModel.addLatestImageURLs(uploadFileViewModel.fetchedURL)
                                                    
                                                    uploadFileViewModel.selectedImageURLs.removeAll()
                                                }
//                                                uploadFileViewModel.selectedImageURLs = savedImageURLs
//                                                await uploadFileViewModel.uploadImages()
                                                
//                                                print("Response Message: \(NetworkManager.shared.responseMessage)")
//                                                print("Status Code: \(NetworkManager.shared.statusCode)")
                                                
//                                                createTaskViewModel.addLatestImageURLs(uploadFileViewModel.fetchedURL)
                                            }
                                            
                                            print("Images URLs: \(createTaskViewModel.selectedImageURLs)")
                                            
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
                ZStack {
                    CurrencyPopupView(selectedCurrency: $selectedCurrency, showCurrencyPopup: $showCurrencyPopup)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.black.opacity(0.5))
                .onTapGesture {
                    showCurrencyPopup.toggle()
                }
            }
            
            //MARK: Calendar Popup
            if showScheduleCalendar {
                ZStack {
                    TaskCalendarView(startDate: .constant(""), endDate: .constant(""), setStartDate: .constant(false), setEndDate: .constant(false), showCalendar: $showScheduleCalendar)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.black.opacity(0.5))
                .onTapGesture {
                    showScheduleCalendar.toggle()
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
                ZStack {
                    TimePickerView(dateViewModel: dateViewModel, startTime: $startTime, stopTime: $stopTime)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.black.opacity(0.5))
                .onTapGesture {
                    dateViewModel.showPicker.toggle()
                }
            }
            
            
            //MARK: Warning
            if showWarningPopup {
                if startTime == "" {
                    ZStack {
                        WarningPopupView(titleText: "Try Again !", description: "Enter start time first", showWarningPopup: $showWarningPopup)
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .background(Color.black.opacity(0.5))
                    .onTapGesture {
                        showWarningPopup.toggle()
                    }
                }
                
                else if stopTime < startTime {
                    ZStack {
                        WarningPopupView(titleText: "Try Again !", description: "End Time should be greater than start time", showWarningPopup: $showWarningPopup)
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .background(Color.black.opacity(0.5))
                    .onTapGesture {
                        showWarningPopup.toggle()
                    }
                }
                
                else {
                    ZStack {
                        WarningPopupView(titleText: "Try Again !", description: NetworkManager.shared.responseMessage, showWarningPopup: $showWarningPopup)
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .background(Color.black.opacity(0.5))
                    .ignoresSafeArea()
                    .onTapGesture {
                        showWarningPopup.toggle()
                    }

                }
            }
            
            
            //MARK: Permission Alert
            if showCameraPermissionAlert {
                ZStack {
                    LocationSettingWarningView(titleText: "Camera Permission Not Given", description: "Provide the camera permission in order to take picture for the task", showWarningPopup: $showCameraPermissionAlert)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.black.opacity(0.5))
                .onTapGesture {
                    showCameraPermissionAlert.toggle()
                }
            }
           
        }
        
        
        .onAppear{
//            print("Image")
//            print(cameraViewModel.savedImages.description)
            
            savedImageURLs.removeAll()
            savedImages.removeAll()
            
            savedImageURLs = cameraViewModel.getCapturedImageURLs()
            savedImages = cameraViewModel.savedImages
            
            print("IMage URLs")
            print(savedImageURLs)
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
                    .font(.system(size: 20, weight: .regular))
                    .fontWeight(.semibold)
                    .foregroundStyle(Color.white)
            }
        }
    }
}

#Preview {
    AddTaskView()
        .environmentObject(CalendarViewModel())
}
