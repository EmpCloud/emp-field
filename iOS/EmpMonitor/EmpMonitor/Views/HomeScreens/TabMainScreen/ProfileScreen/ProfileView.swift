//
//  ProfileView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 03/09/24.
//

import SwiftUI

struct ProfileView: View {
    
    @Environment(\.dismiss) var dismiss
    
    @EnvironmentObject var searchLocationViewModel: SearchLocationViewModel
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    @StateObject private var profileCameraViewModel = ProfileCameraViewModel()
    @StateObject private var uploadFileViewModel = UploadFilesViewModel()
    
    @ObservedObject var getProfileViewModel: GetProfileViewModel
    @ObservedObject var updateProfileViewModel: UpdateProfileViewModel
    
    
    
    
//    @State private var name: String = ""
//    @State private var email: String = ""
//    @State private var phoneNumber: String = ""
//    @State private var age: String = ""
//    @State private var gender: String = ""
    @State private var profilePicURL: String = ""
    
    @Binding var selectedGender: String
    
    @State private var showEditAddress: Bool = false
    
    //Camera
    @State private var showProfileCamera: Bool = false
    @State private var savedImageURL: URL?
    
    //Alert:
    @State private var showCameraPermissionAlert: Bool = false
    @State private var showUpdateError: Bool = false
    @State private var updateErrorMessage: String = ""
    @State private var toastMessage: ToastMessage?
    
    @FocusState private var isKeyboardShowing: Bool
    
    var body: some View {
//        NavigationStack {
            ZStack(alignment: .top) {
                LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                    .ignoresSafeArea(.all)
                
                VStack(){
                    
                    //MARK: Profile Image
                    ProfileLargeWithCameraView(profileCameraViewModel: profileCameraViewModel, showCameraPermissionAlert: $showCameraPermissionAlert, showProfileCamera: $showProfileCamera)
                        .environmentObject(profileImageLoader)
                        .padding(.top, 80)
//                        .padding(.bottom, 10)
                    
                    //MARK: Form
                    VStack(alignment: .leading, spacing: 15){
                        
                        VStack(spacing: 3) {
                            Text("Full Name*")
                                .font(.system(size: 15, weight: .semibold))
                                .frame(maxWidth: .infinity, alignment: .leading)
                            TextFieldEditableCreateProfileView(text: $updateProfileViewModel.fullName, placeholder: "Enter Full Name")
                        }
                        .padding(.trailing, 20)
                        .padding(.leading, 20)
                        
                       
                        //MARK: Email
                        VStack(spacing: 3) {
                            Text("Email ID*")
                                .font(.system(size: 15, weight: .semibold))
                                .frame(maxWidth: .infinity, alignment: .leading)
                            TextFieldCreateProfileView(text: $updateProfileViewModel.email, isEditable: .constant(false), placeholder: "Enter Email ID")
                        }
                        .padding(.trailing, 20)
                        .padding(.leading, 20)
                        
                        
                        //MARK: Phone
                        VStack(spacing: 3) {
                            Text("Mobile No.")
                                .font(.system(size: 15, weight: .semibold))
                                .frame(maxWidth: .infinity, alignment: .leading)
                            TextFieldEditableCreateProfileView(text: $updateProfileViewModel.phoneNumber, placeholder: "Enter mobile no.")
                        }
                        .padding(.trailing, 20)
                        .padding(.leading, 20)
                        
                        //MARK: Age
                        VStack(spacing: 3) {
                            Text("Select Age")
                                .font(.system(size: 15, weight: .semibold))
                                .frame(maxWidth: .infinity, alignment: .leading)
                            if Int(updateProfileViewModel.age) ?? 100 > 100 || Int(updateProfileViewModel.age) ?? 0 < 0 {
                                Text("Age Should be between 0-100*")
                                    .font(.system(size: 12, weight: .regular))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .foregroundStyle(Color.absent)
                            }
                            TextFieldEditableCreateProfileView(text: $updateProfileViewModel.age, placeholder: "Age")
                                .keyboardType(.numberPad)
                                .toolbarDoneButton()
                                .focused($isKeyboardShowing)
//                                .onChange(of: updateProfileViewModel.age, { _, newAge in
//                                    withAnimation {
//                                        updateProfileViewModel.age = HelperFunction.shared.validateAgeInput(inputAge: updateProfileViewModel.age)
//                                    }
//                                })
                                .onTapGesture {
                                    withAnimation {
                                        isKeyboardShowing = false
                                    }
                                }
                        }
                        .padding(.trailing, 20)
                        .padding(.leading, 20)
                        
                        
                        //MARK: Gender
                        VStack(spacing: 3)  {
                            Text("Select Gender*")
                                .font(.system(size: 15, weight: .semibold))
                                .frame(maxWidth: .infinity, alignment: .leading)
                            HStack{
                                
                                ForEach(["Male", "Female", "Other"], id: \.self) { gender in
                                    Button {
                                        selectedGender = gender
                                    } label: {
                                        Circle()
                                            .fill(gender == selectedGender.capitalized ? Color.blue: Color.white)
                                            .frame(width: 14, height: 14)
                                            .overlay {
                                                Circle()
                                                    .stroke(Color(UIColor.lightGray), lineWidth: 3.0)
                                            }
                                        Text(gender)
                                            .font(.system(size: 14, weight: .regular))
                                        
                                    }
                                    .padding(.trailing, 20)

                                }
                            }
                            .padding(.top, 3)
                            .foregroundStyle(Color(red: 95/255, green: 95/255, blue: 95/255, opacity: 1.0))
                            
                        }
                        .padding(.trailing, 20)
                        .padding(.leading, 20)
                        
                        
                        //MARK: Line
                        LineView()
                            .padding()
                            .padding(.horizontal)
                        
                        
                        //MARK: Edit Button
                        HStack(alignment: .top){
                            VStack(alignment: .leading){
                                Text("Address")
                                    .font(.system(size: 15, weight: .semibold))
                                
                                Text(updateProfileViewModel.address1)
                                    .font(.system(size: 12, weight: .regular))
                                    .fontWeight(.medium)
                                    .foregroundStyle(Color.addressText2)
                                
                                Text(updateProfileViewModel.address2)
                                    .font(.system(size: 12, weight: .regular))
                                    .fontWeight(.medium)
                                    .foregroundStyle(Color.addressText2)
                            }
                            
                            Spacer()
                            
                            EditButton {
                                //TODO: To take to edit address screen
                                showEditAddress.toggle()
                            }
                        }
//                        .padding(.top, -15)
                        .padding(.horizontal, 40)
                        
//                        //MARK: Line
                        LineView()
                            .padding()
                            .padding(.horizontal)
                        
                        //MARK: Save Button
                        PrimaryThinButton(text: "Save") {
                            Task {
                                do {
                                    try await updateProfileViewModel.updateProfile()
                                    toastMessage = ToastMessage(style: .success, message: "Profile updated successfully")
                                    try? await Task.sleep(for: .seconds(1))
                                    dismiss()
                                } catch let networkError as NetworkError {
                                    // Use the message carried in the error — avoids reading stale
                                    // NetworkManager.shared.responseMessage from a previous API call.
                                    updateErrorMessage = networkError.errorDescription
                                        ?? "Failed to update profile. Please try again."
                                    showUpdateError = true
                                } catch {
                                    updateErrorMessage = "Failed to update profile. Please try again."
                                    showUpdateError = true
                                }
                            }
                        }
                        .padding(.bottom, 50)
                        .padding(.horizontal)
                        .disableWithOpacity(Int(updateProfileViewModel.age) ?? 100 > 100 || Int(updateProfileViewModel.age) ?? 0 < 0)

                        
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
//                        .padding()
                    .padding(.top)
                    .background(Color.white)
                    .clipShape(RoundedRectangle(cornerRadius: 20))
                    .padding(.vertical, 20)
                    .padding(.horizontal, 10)


                }
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
//                .padding(.bottom, 70)
                
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

                //MARK: Update Error Alert
                if showUpdateError {
                    ZStack {
                        WarningPopupView(titleText: "Update Failed", description: updateErrorMessage, showWarningPopup: $showUpdateError)
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .background(Color.black.opacity(0.5))
                    .onTapGesture {
                        showUpdateError = false
                    }
                }
            }
            .toast(message: $toastMessage)
            .onChange(of: savedImageURL) { _, _ in
                Task {
                    if let imageURL = savedImageURL {
                        uploadFileViewModel.selectedImageURLs.append(imageURL)
                        await uploadFileViewModel.uploadUserProfileImages()
                        
                        if NetworkManager.shared.statusCode == 200 {
                            updateProfileViewModel.profilePic = uploadFileViewModel.fetchProfileURL
                            
                            //update the profile displaying currently
                            profileImageLoader.profileImageURL = updateProfileViewModel.profilePic
                            
                            //Update the profile Image
                            try await updateProfileViewModel.updateProfile()
                        }
                        
                        AppLog.debug("Profile Pic updated: \(updateProfileViewModel.profilePic)")
                    }
                }
            }
            .navigationTitle("Profile")
            .navigationBarTitleDisplayMode(.inline)
            .navigationDestination(isPresented: $showEditAddress) {
                UpdateProfileAddressView(updateProfileViewModel: updateProfileViewModel)
                    .navigationBarBackButtonHidden()
                    .environmentObject(searchLocationViewModel)
                    .environmentObject(profileImageLoader)
            }
            .onChange(of: selectedGender) { _, newGender in
                updateProfileViewModel.gender = selectedGender
    //            AppLog.debug(createProfileViewModel.gender)
            }
            .toolbar{
                ToolbarItem(placement: .topBarLeading) {
                    BackButtonView()
                        .onTapGesture {
                            dismiss()
                        }
                }
            }
            .onAppear{
                
                //when this screen appears click image should be populate if click/selected
                savedImageURL = profileCameraViewModel.getCapturedImageURLs()
                
                if let profilePicURL = getProfileViewModel.profileDetail.first?.profilePic {
                    self.profilePicURL = profilePicURL
                }
                
                
                UINavigationBar.appearance().titleTextAttributes = [
                    .foregroundColor: UIColor.white
                ]
            }
            .navigationDestination(isPresented: $showProfileCamera, destination: {
                ProfileCameraView(profileCameraViewModel: profileCameraViewModel)
                    .navigationBarBackButtonHidden()
            })
            .ignoresSafeArea(.container)
                //        }
                
                
                
            }
    
  
}

#Preview {
    ProfileView(getProfileViewModel: GetProfileViewModel(), updateProfileViewModel: UpdateProfileViewModel(), selectedGender: .constant(""))
        .environmentObject(PermissionManager())
        .environmentObject(SearchLocationViewModel())
}
