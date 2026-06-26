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
    
    var body: some View {
        ZStack {
            CameraPreview(cameraViewModel: cameraViewModel)
//            Color.black
                .ignoresSafeArea()
            
            VStack {
                if cameraViewModel.isTaken, let image = cameraViewModel.capturedImage {
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
                
                if showDescription {
                    Rectangle()
                        .fill(Color.white)
                        .frame(height: 183)
                        .ignoresSafeArea(edges: .bottom)
                        .overlay {
                            VStack {
                                HStack {
                                    Text("Description")
                                        .font(.custom("Montserrat", size: 14))
                                        .fontWeight(.semibold)
                                        .foregroundStyle(Color.subText)
                                    
                                    Spacer()
                                    
                                    Image(systemName: "xmark")
                                        .resizable()
                                        .aspectRatio(contentMode: .fit)
                                        .frame(width: 13, height: 13)
                                        .fontWeight(.bold)
                                        .foregroundStyle(Color.primaryButton1)
                                        .onTapGesture {
                                            withAnimation {
                                                showDescription.toggle()
                                            }
                                        }
                                }
                                .padding(.horizontal, 20)
                                
                                PopupLargeTextField(text: $description, placeholder: "Enter Description")
                                    .padding(.horizontal)
                                
                                PrimaryButton(text: "Done") {
                                    //TODO: Save the pic
                                    // Handle image saving or uploading here
                                    if cameraViewModel.savedImages.count < 4 {
                                        if let image = cameraViewModel.capturedImage {
                                            cameraViewModel.saveImage(image, description: description)
                                        }else if let image = selectedGallaryImages.first{
//                                            cameraViewModel.savedImages.append(SavedImage(image: image, description: "", url: nil))
                                            cameraViewModel.saveImage(image, description: description)
                                           
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
            ImagePicker(selectedImages: $selectedGallaryImages)
        })

    }
    
//    func saveImage(_ image: UIImage) {
//        // Implement your save or upload logic here
//        print("Image saved or uploaded")
////        print(i)
//    }
}


#Preview {
    CameraView(cameraViewModel: CameraViewModel())
}
