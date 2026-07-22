//
//  CameraView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 23/08/24.
//

import SwiftUI

struct CameraView: View {
    
    @Environment(\.dismiss) var dismiss
    
    @ObservedObject var cameraViewModel: CameraViewModel
    
    @State private var description: String = ""
    @State private var showDescription: Bool = true
    @State private var isImagePickerPresented = false
    
    @State private var selectedGallaryImages: [UIImage] = []

    private var remainingImageSlots: Int {
        max(0, 4 - cameraViewModel.savedImages.count)
    }

    private var hasPendingImageSelection: Bool {
        cameraViewModel.capturedImage != nil || !selectedGallaryImages.isEmpty
    }
    
    var body: some View {
        ZStack {
            CameraPreview(cameraViewModel: cameraViewModel)
//            Color.black
                .ignoresSafeArea()
            
            VStack {
                Spacer()
                
                HStack {
                        //MARK: Button Section
                        HStack {
                            
                            //Button to open Gallary
                            Button(action: {
                                if remainingImageSlots > 0 {
                                    isImagePickerPresented = true
                                }
                            }, label: {
                                Circle()
                                    .fill(Color.white)
                                    .frame(width: 42, height: 42)
                                    .overlay {
                                        Image(.previewImageIcon)
                                            .resizable()
                                            .aspectRatio(contentMode: .fit)
                                            .frame(width: 20, height: 20)
                                    }
                                
                            })
                            .disabled(remainingImageSlots == 0)
                            
                            Spacer()
                            
                            //Click Button
                            Button(action: {
                                cameraViewModel.takePicture()
                                showDescription = true
                            }) {
                                Circle()
                                    .fill(Color.white)
                                    .frame(width: 65, height: 65)
                                    .overlay(Circle().stroke(Color.blue, lineWidth: 5))
                            }
                            
                            Spacer()
                            //Flip the camera to front camera button
                            Button(action: {
                                cameraViewModel.flipCamera()
                            }, label: {
                                Circle()
                                    .fill(Color.white)
                                    .frame(width: 42, height: 42)
                                    .overlay {
                                        Image(.flipCameraIcon)
                                            .resizable()
                                            .aspectRatio(contentMode: .fit)
                                            .frame(width: 20, height: 20)
                                    }
                            })
                        }
                        .padding(.horizontal, 40)
                }
                .padding(.bottom)
                
                if showDescription || hasPendingImageSelection {
                    Rectangle()
                        .fill(Color.white)
                        .frame(height: selectedGallaryImages.count > 1 ? 208 : 183)
                        .ignoresSafeArea(edges: .bottom)
                        .overlay {
                            VStack(spacing: AppSpacing.stackSpacingDefault) {
                                HStack {
                                    Text("Description")
                                        .font(AppFont.primary(size: AppFont.Size.body))
                                        .fontWeight(AppFont.Weight.semibold)
                                        .foregroundStyle(Color.subText)
                                    
                                    Spacer()
                                    
                                    Image(systemName: "xmark")
                                        .resizable()
                                        .aspectRatio(contentMode: .fit)
                                        .frame(width: 13, height: 13)
                                        .fontWeight(AppFont.Weight.bold)
                                        .foregroundStyle(Color.primaryButton1)
                                        .onTapGesture {
                                            withAnimation {
                                                showDescription.toggle()
                                            }
                                        }
                                }
                                .padding(.horizontal, 20)

                                if selectedGallaryImages.count > 1 {
                                    Text("\(selectedGallaryImages.count) photos selected")
                                        .font(AppFont.primary(size: AppFont.Size.caption))
                                        .fontWeight(AppFont.Weight.medium)
                                        .foregroundStyle(Color.subText)
                                        .frame(maxWidth: .infinity, alignment: .leading)
                                        .padding(.horizontal, AppSpacing.md)
                                }
                                
                                PopupLargeTextField(text: $description, placeholder: "Enter Description")
                                    .padding(.horizontal)
                                
                                PrimaryButton(text: "Done") {
                                    //TODO: Save the pic
                                    // Handle image saving or uploading here
                                    if cameraViewModel.savedImages.count < 4 {
                                        if let image = cameraViewModel.capturedImage {
                                            cameraViewModel.saveImage(image, description: description)
                                        } else if !selectedGallaryImages.isEmpty {
                                            let availableSlots = max(0, 4 - cameraViewModel.savedImages.count)
                                            for image in selectedGallaryImages.prefix(availableSlots) {
                                                cameraViewModel.saveImage(image, description: description)
                                            }
                                        }
                                    }
                                    
                                    dismiss()
                                }
                                .padding(.horizontal, 40)
                                .disableWithOpacity((!cameraViewModel.isTaken && selectedGallaryImages.isEmpty ) || description.isEmpty)
                            }
                        }
                        .transition(.move(edge: .bottom))
                }
            }
            .ignoresSafeArea(.container)
        }
        .onChange(of: selectedGallaryImages.count) { _, newCount in
            guard newCount > 0 else { return }
            cameraViewModel.capturedImage = nil
            cameraViewModel.isTaken = false
            showDescription = true
        }
        .onAppear {
            cameraViewModel.checkPermissions()
            cameraViewModel.isTaken = false  //it will remove the last image preview from the CameraView
        }
        .onDisappear {
            cameraViewModel.capturedImage = nil
            selectedGallaryImages.removeAll()
        }
        .alert(isPresented: $cameraViewModel.alert) {
            Alert(title: Text("Camera Access Denied"),
                  message: Text("Please enable camera access in settings."),
                  dismissButton: .default(Text("OK")))
        }
        .toolbar {
            ToolbarItem(placement: .topBarLeading) {
                BackButtonView()
                    .onTapGesture {
                        
                        if cameraViewModel.isTaken {
                            cameraViewModel.capturedImage = nil
                        }else if !selectedGallaryImages.isEmpty{
                            selectedGallaryImages.removeAll()
                        }
                       
                        
                        dismiss()
                        
                    }
            }
            ToolbarItem(placement: .topBarTrailing) {
                Image(systemName: cameraViewModel.isFlashLightON ? "flashlight.off.fill" : "flashlight.slash")
                    .foregroundStyle(Color.white)
                    .padding(.horizontal)
                    .onTapGesture {
                        withAnimation {
                            cameraViewModel.toggleFlashlight()
                        }
                    }
            }
            ToolbarItem(placement: .topBarTrailing) {
                Circle()
                    .fill(Color.white)
                    .frame(width: 42, height: 42)
                    .overlay {
                        Image(systemName: "xmark")
                    }
                    .onTapGesture() {
                    cameraViewModel.retake()
                        if !selectedGallaryImages.isEmpty {
                            selectedGallaryImages.removeAll()
                        }
                }
            }
        }
        .sheet(isPresented: $isImagePickerPresented, content: {
            //Image picker
            ImagePicker(selectedImages: $selectedGallaryImages, maxSelectionCount: remainingImageSlots)
        })

    }
    
//    func saveImage(_ image: UIImage) {
//        // Implement your save or upload logic here
//        AppLog.debug("Image saved or uploaded")
////        AppLog.debug(i)
//    }
}


#Preview {
    CameraView(cameraViewModel: CameraViewModel())
}
