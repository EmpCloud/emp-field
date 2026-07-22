//
//  TaskRescheduleView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/09/24.
//

import SwiftUI
import MapKit

struct TaskRescheduleView: View {
    
    @Environment(\.dismiss) var dismiss
    
    @EnvironmentObject var permissionManager: PermissionManager
    
    @StateObject private var dateViewModel = DateViewModel()
    @StateObject private var cameraViewModel = CameraViewModel()
    @StateObject private var uploadFileViewModel = UploadFilesViewModel()
    @StateObject private var updateTaskViewModel = UpdateTaskViewModel()
    
    @ObservedObject var tagViewModel: TagViewModel
    
    //for sheet manipulation
    @State private var showTaskRescheduleSheet: Bool = true
    @State private var sheetHeight: CGFloat = 0.3
    @State private var currentDetent: PresentationDetent = .fraction(0.3)
    
    //Data
    @Binding var selectedTask: FilterTaskListResponseData?
    @Binding var taskStatus: [String : Int]  // for task status "start", "pause", "resume"
    
    @State private var showCurrencyPopup: Bool = false
    @State private var selectedCurrency: String = "INR"
    @State private var taskVolume: String = ""
    @State private var taskValue: String = ""
    
    //Camera
    @State private var showCamera: Bool = false
    @State private var showImagePreview: Bool = false
    @State private var selectedImage: UIImage? = nil
    @State private var savedImageURLs: [URL] = []
    @State private var savedImages: [SavedImage] = []
    @State private var shouldRenderMap: Bool = false
    
    //dismiss the view when the task is updated
    @State private var isDismiss: Bool = false
    
    //userLocation
    @State private var userLocation: CLLocationCoordinate2D?
    
    //taking the client location
    var clientLocation: CLLocationCoordinate2D {
        return CLLocationCoordinate2D(latitude: lat, longitude: long)
    }
    
    var lat: Double {
        if let latitude = Double(selectedTask?.latitude ?? "\(permissionManager.userLocation?.coordinate.latitude ?? 0.0)") {
            return latitude
        }
        return 0.0 // Provide a default value if the conversion fails
    }
    
    var long: Double {
        if let longitude = Double(selectedTask?.longitude ?? "\(permissionManager.userLocation?.coordinate.longitude ?? 0.0)") {
            return longitude
        }
        return 0.0 // Provide a default value if the conversion fails
    }
    
    
    //Warning
    @State private var showWarningPopup: Bool = false
    
    
    //To refresh the taskList after updating status
    @Binding var refreshScreen: Bool
    
    var body: some View {
        ZStack(alignment: .bottom) {
            LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                .ignoresSafeArea()
            
            VStack{
                if shouldRenderMap {
                    ClientDetailMap(userLocation: $userLocation, clientLocation: clientLocation)
                } else {
                    Color.rectangleBG
                }
            }
//            .frame(height: 500)
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
            .background(Color.rectangleBG)
            .clipShape(RoundedRectangle(cornerRadius: 20))
            .shadow(color: .black.opacity(0.25), radius: 12)
            .padding(.top)
            .ignoresSafeArea(edges: .bottom)
            
            
            //MARK: Warning Popup
            if showWarningPopup {
                ModalOverlayView(dismissOnBackgroundTap: {
                    withAnimation {
                        showWarningPopup.toggle()
                    }
                }) {
                    WarningPopupView(titleText: "Try Again: \(NetworkManager.shared.statusCode)", description: NetworkManager.shared.responseMessage, showWarningPopup: $showWarningPopup)
                }
            }
        }
        .onChange(of: isDismiss) { _, newValue in
            if newValue {
                dismiss()
            }
        }
        .onAppear {
            
            userLocation = CLLocationCoordinate2D(latitude: permissionManager.userLocation?.coordinate.latitude ?? lat, longitude: permissionManager.userLocation?.coordinate.longitude ?? long)
            
            showTaskRescheduleSheet = true
            if let selectedTaskID = selectedTask?.id {
                taskStatus[selectedTaskID] = selectedTask?.taskApproveStatus
            }

            DispatchQueue.main.async {
                shouldRenderMap = true
            }
            
        }
        .sheet(isPresented: $showTaskRescheduleSheet) {
            if #available(iOS 16.4, *) {
                TaskRescheduleSheetView(cameraViewModel: cameraViewModel, uploadFileViewModel: uploadFileViewModel, updateTaskViewModel: updateTaskViewModel, tagViewModel: tagViewModel, dateViewModel: dateViewModel, sheetHeight: $sheetHeight, currentDetent: $currentDetent, showTaskRescheduleSheet: $showTaskRescheduleSheet, taskVolume: $taskVolume, taskValue: $taskValue, showCurrencyPopup: $showCurrencyPopup, selectedCurrency: $selectedCurrency, showCamera: $showCamera, showImagePreview: $showImagePreview, selectedImage: $selectedImage, savedImageURLs: $savedImageURLs, savedImages: $savedImages, selectedTask: $selectedTask, taskStatus: $taskStatus, isDismiss: $isDismiss, showWarningPopup: $showWarningPopup, refreshScreen: $refreshScreen)
                    .presentationDetents([.fraction(0.3), .fraction(0.05), .fraction(0.99)], selection: $currentDetent)
                    .presentationDragIndicator(.visible)
                    .presentationBackgroundInteraction(.enabled(upThrough: .fraction(0.3)))
                    .interactiveDismissDisabled()
                    
            } else {
                // Fallback on earlier versions
                TaskRescheduleSheetView(cameraViewModel: cameraViewModel, uploadFileViewModel: uploadFileViewModel, updateTaskViewModel: updateTaskViewModel, tagViewModel: tagViewModel, dateViewModel: dateViewModel, sheetHeight: $sheetHeight, currentDetent: $currentDetent, showTaskRescheduleSheet: $showTaskRescheduleSheet, taskVolume: $taskVolume, taskValue: $taskValue, showCurrencyPopup: $showCurrencyPopup, selectedCurrency: $selectedCurrency, showCamera: $showCamera, showImagePreview: $showImagePreview, selectedImage: $selectedImage, savedImageURLs: $savedImageURLs, savedImages: $savedImages, selectedTask: $selectedTask, taskStatus: $taskStatus, isDismiss: $isDismiss, showWarningPopup: $showWarningPopup, refreshScreen: $refreshScreen)
                    .presentationDetents([.fraction(0.3), .fraction(0.05), .fraction(0.99)], selection: $currentDetent)
                    .presentationDragIndicator(.visible)
                    .interactiveDismissDisabled()
            }
        }
        .navigationDestination(isPresented: $showCamera) {
            CameraView(cameraViewModel: cameraViewModel)
                .navigationBarBackButtonHidden()
        }
        .onChange(of: showCamera) { _, isShowing in
            guard !isShowing else { return }

            syncSavedImagesFromCamera()

            if !isDismiss {
                sheetHeight = 0.99
                currentDetent = .fraction(0.99)
                showTaskRescheduleSheet = true
            }
        }
        .toolbar {
            ToolbarItem(placement: .topBarLeading) {
//                HStack {
                    BackButtonView()
                        .onTapGesture {
                            dismiss()
//                        }
                    
                }
            }
            ToolbarItem(placement: .principal) {
                Text("Task")
                    .font(AppFont.primary(size: AppFont.Size.navigationTitle))
                    .fontWeight(AppFont.Weight.semibold)
                    .foregroundStyle(Color.white)
//                        .padding(.horizontal, 70)
            }
        }
    }

    private func syncSavedImagesFromCamera() {
        savedImageURLs = cameraViewModel.getCapturedImageURLs()
        savedImages = cameraViewModel.savedImages
    }
}

//#Preview {
//    TaskRescheduleView()
//        .environmentObject(SearchLocationViewModel())
//}
