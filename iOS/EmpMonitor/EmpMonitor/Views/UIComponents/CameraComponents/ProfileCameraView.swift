//
//  ProfileCameraView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/08/24.
//

import SwiftUI

struct ProfileCameraView: View {
    
    @Environment(\.dismiss) var dismiss
    
    @ObservedObject var profileCameraViewModel: ProfileCameraViewModel
    
    @State private var isImagePickerPresented = false
    
    @State private var selectedGallaryImages: [UIImage] = []

    private var previewImage: UIImage? {
        selectedGallaryImages.last ?? profileCameraViewModel.capturedImage
    }

    private var isPreviewingImage: Bool {
        previewImage != nil
    }
    
    var body: some View {
        ZStack {
            if let previewImage {
                profileImagePreview(previewImage)
            } else {
                ProfileCameraPreview(profileCameraViewModel: profileCameraViewModel)
    //            Color.black
                    .ignoresSafeArea()
            }
            
            VStack {
                Spacer()
                
                HStack {
                        //MARK: Button Section
                        HStack {
                            
                            //Button to open Gallary
                            Button(action: {
                                isImagePickerPresented = true
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
                            
                            Spacer()
                            
                            //Click Button
                            Button(action: {
                                selectedGallaryImages.removeAll()
                                profileCameraViewModel.takePicture()
                            }) {
                                Circle()
                                    .fill(Color.white)
                                    .frame(width: 65, height: 65)
                                    .overlay(Circle().stroke(Color.blue, lineWidth: 5))
                            }
                            
                            Spacer()
                            //Flip the camera to front camera button
                            Button(action: {
                                profileCameraViewModel.flipCamera()
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
                        .padding(.bottom, isPreviewingImage ? 0 : 40)
                }
                .padding(.bottom)
                
                if isPreviewingImage {
                    Rectangle()
                        .fill(Color.white)
                        .frame(height: 100)
                        .ignoresSafeArea(edges: .bottom)
                        .overlay {
                            VStack {
                                PrimaryButton(text: "Done") {
                                    //TODO: Save the pic
                                    // Handle image saving or uploading here
                                    if let image = previewImage {
                                        profileCameraViewModel.saveImage(image)
                                    }
                                    dismiss()
                                }
                                .padding(.horizontal, 40)
//                                .disableWithOpacity(!cameraViewModel.isTaken && selectedGallaryImages.isEmpty)
                            }
                        }
                        .transition(.move(edge: .bottom))
                }
            }
            .ignoresSafeArea(.container)
        }
        .animation(.easeInOut(duration: 0.2), value: isPreviewingImage)
        .onChange(of: selectedGallaryImages.count) { _, newCount in
            guard newCount > 0 else { return }
            profileCameraViewModel.capturedImage = nil
            profileCameraViewModel.isTaken = false
        }
        .onAppear {
            profileCameraViewModel.checkPermissions()
            profileCameraViewModel.isTaken = false  //it will remove the last image preview from the CameraView
        }
        .onDisappear {
            profileCameraViewModel.capturedImage = nil
            selectedGallaryImages.removeAll()
        }
        .alert(isPresented: $profileCameraViewModel.alert) {
            Alert(title: Text("Camera Access Denied"),
                  message: Text("Please enable camera access in settings."),
                  dismissButton: .default(Text("OK")))
        }
        .toolbar {
            ToolbarItem(placement: .topBarLeading) {
                BackButtonView()
                    .onTapGesture {
                        
                        if profileCameraViewModel.isTaken {
                            profileCameraViewModel.capturedImage = nil
                        }else if !selectedGallaryImages.isEmpty{
                            selectedGallaryImages.removeAll()
                        }
                       
                        
                        dismiss()
                        
                    }
            }
            ToolbarItem(placement: .topBarTrailing) {
                if !isPreviewingImage {
                    Image(systemName: profileCameraViewModel.isFlashLightON ? "flashlight.off.fill" : "flashlight.slash")
                        .foregroundStyle(Color.white)
                        .padding(.horizontal)
                        .onTapGesture {
                            withAnimation {
                                profileCameraViewModel.toggleFlashlight()
                            }
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
                        profileCameraViewModel.retake()
                        if !selectedGallaryImages.isEmpty {
                            selectedGallaryImages.removeAll()
                        }
                }
            }
        }
        .sheet(isPresented: $isImagePickerPresented, content: {
            //Image picker
            ImagePicker(selectedImages: $selectedGallaryImages)
        })

    }
    
    private func profileImagePreview(_ image: UIImage) -> some View {
        GeometryReader { proxy in
            let availableSize = max(min(proxy.size.width - 48, proxy.size.height - 220), 120)
            let previewSize = min(availableSize, 340)

            ZStack {
                Color.black.ignoresSafeArea()

                Image(uiImage: image)
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(width: previewSize, height: previewSize)
                    .clipShape(Circle())
                    .overlay {
                        Circle()
                            .stroke(Color.white.opacity(0.85), lineWidth: 2)
                    }
                    .shadow(color: Color.black.opacity(0.35), radius: 18, x: 0, y: 12)
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .padding(.bottom, 120)
            }
        }
        .ignoresSafeArea()
    }
}

#Preview {
    ProfileCameraView(profileCameraViewModel: ProfileCameraViewModel())
}
