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
    
    var body: some View {
        ZStack {
            ProfileCameraPreview(profileCameraViewModel: profileCameraViewModel)
//            Color.black
                .ignoresSafeArea()
            
            VStack {
                if profileCameraViewModel.isTaken, let image = profileCameraViewModel.capturedImage {
                    Image(uiImage: image)  //last clicked image preview
                        .resizable()
                        .scaledToFit()
                        .frame(height: 200)
                        .background(Color.black.opacity(0.7))
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                        .padding()
                }
                else if let image = selectedGallaryImages.last {        // to preview the selected image from the gallary
                    Image(uiImage: image)
                        .resizable()
                        .scaledToFit()
                        .frame(height: 200)
                        .background(Color.black.opacity(0.7))
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                        .padding()
                }
                
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
                        .padding(.bottom, profileCameraViewModel.isTaken || !selectedGallaryImages.isEmpty ? 0 : 40)
                }
                .padding(.bottom)
                
                if profileCameraViewModel.isTaken || !selectedGallaryImages.isEmpty /*true*/{
                    Rectangle()
                        .fill(Color.white)
                        .frame(height: 100)
                        .ignoresSafeArea(edges: .bottom)
                        .overlay {
                            VStack {
                                PrimaryButton(text: "Done") {
                                    //TODO: Save the pic
                                    // Handle image saving or uploading here
                                        if let image = profileCameraViewModel.capturedImage {
                                            profileCameraViewModel.saveImage(image)
                                            
                                            
                                        }else if let image = selectedGallaryImages.first{
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
                Image(systemName: profileCameraViewModel.isFlashLightON ? "flashlight.off.fill" : "flashlight.slash")
                    .foregroundStyle(Color.white)
                    .padding(.horizontal)
                    .onTapGesture {
                        withAnimation {
                            profileCameraViewModel.toggleFlashlight()
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
    
//    func saveImage(_ image: UIImage) {
//        // Implement your save or upload logic here
//        AppLog.debug("Image saved or uploaded")
////        AppLog.debug(i)
//    }
}

#Preview {
    ProfileCameraView(profileCameraViewModel: ProfileCameraViewModel())
}
