//
//  PasswordLoginView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 19/06/24.
//

import SwiftUI

struct PasswordLoginView: View {
    
    @Environment(\.dismiss) var dismiss
    
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    //    @EnvironmentObject var appState: AppState
    
    @StateObject private var forgetPasswordViewModel = ForgetPasswordViewModel()
    
    @ObservedObject var userLoginViewModel: UserLoginViewModel
    
    @State private var createProfileScreen: Bool = false
    @State private var showTabMainView: Bool = false
    
    @State private var showEmailOTPScreen: Bool = false
    @State private var disableButton: Bool = true
    //    @State private var passwordText: String = ""
    
    //warning Popup
    @State private var showWarning: Bool = false
    @State private var warningMessage: String = ""
    
    //Hide the password
    @State private var showPassword: Bool = false
    
    private func hasExistingProfile() -> Bool {
        let createProfile = AuthStore.shared.getUserProfileData(as: CreateProfileResponseModel.self)
        let fetchedProfile = AuthStore.shared.getUserProfileData(as: ProfileResponseModel.self)
        return createProfile?.body.data.resultData.first != nil
            || fetchedProfile?.body.data.resultData.first != nil
    }
    
    var body: some View {
        ZStack(alignment: .top){
            Color.white
                .ignoresSafeArea(.all)
            
            Image(.loginScreenTopBg)
                .ignoresSafeArea(.all)
            
            VStack(spacing: 20) {
                
                //MARK: AppIcon
                CircularAppIcon()
                    .padding(.top, -40)
                
                Text("Login")
                    .font(AppFont.primary(size: AppFont.Size.screenTitle))
                    .padding(.top, 170)
                    .padding(.bottom, 20)
                
                AuthPasswordTextField(text: $userLoginViewModel.password, showPassword: $showPassword, placeholder: "Enter Password")
                    .textContentType(.password)
                    .padding(.horizontal, 50)
                
                //MARK: Login Button
                PrimaryButton(text: "Login") {
                    //next screen
                    guard !userLoginViewModel.password.isEmpty else {
                        warningMessage = "Please enter your password."
                        showWarning = true
                        return
                    }
                    Task {
                        try await userLoginViewModel.loginUser()

                        if NetworkManager.shared.statusCode == 200 {
                            // Fetch profile and tracking settings here so loginUser() stays fast
                            let profileVM = GetProfileViewModel()
                            try? await profileVM.getProfile()
                            await TrackingSettingsViewModel.shared.fetchTrackingSettings()

                            profileImageLoader.profileImageURL = UserDefaults.standard.string(forKey: "UserProfilePic") ?? ""

                            // Only route to CreateProfile when the API confirmed no profile exists.
                            // If getProfile() itself failed (network error), fall through to TabMain
                            // to avoid erroneously sending an existing user to the create-profile flow.
                            if profileVM.error == nil && !hasExistingProfile() {
                                createProfileScreen = true
                            } else {
                                showTabMainView = true
                            }

                            //-------------logout changes -------
                        }
                        else {
                            warningMessage = NetworkManager.shared.responseMessage.isEmpty
                                ? "Something went wrong. Please try again."
                                : NetworkManager.shared.responseMessage
                            showWarning = true
                        }

                        //                        appState.showToaster.toggle()
                    }

                }
                .padding(.horizontal, 50)
                .disableWithOpacity(disableButton)
                
                
                Text("Forgot Password?")
                    .font(AppFont.primary(size: AppFont.Size.body))
                    .foregroundStyle(Color.authSubText)
                    .padding(.top)
                    .onTapGesture {
                        Task {
                            forgetPasswordViewModel.email = userLoginViewModel.email
                            try await forgetPasswordViewModel.forgetPassword()
                            
                            //next screen
                            if NetworkManager.shared.statusCode == 200 {
                                //                                        AppLog.debug(userLoginViewModel.email)
                                showEmailOTPScreen = true
                            }else {
                                warningMessage = NetworkManager.shared.responseMessage.isEmpty
                                    ? "Something went wrong. Please try again."
                                    : NetworkManager.shared.responseMessage
                                withAnimation {
                                    showWarning = true
                                }
                            }
                        }
                    }
                    .onChange(of: userLoginViewModel.password) { _, newValue in
                        if newValue.isEmpty {
                            disableButton = true
                        }else{
                            disableButton = false
                        }
                    }
                    .fullScreenCover(isPresented: $createProfileScreen){
                        CreateProfileView()
                            .environmentObject(profileImageLoader)
                    }
                    .fullScreenCover(isPresented: $showTabMainView){
                        TabMainView()
                            .environmentObject(profileImageLoader)
                    }
                    .navigationDestination(isPresented: $showEmailOTPScreen) {
                        EmailOTPView(forgetPasswordViewModel: forgetPasswordViewModel)
                            .navigationBarBackButtonHidden()
                    }
                    .toolbar {
                        ToolbarItem(placement: .topBarLeading) {
                            BackButtonView()
                                .onTapGesture {
                                    dismiss()
                                }
                        }
                    }
            }
            
            //Warning Popup
            if showWarning {
                ModalOverlayView {
                    WarningPopupView(titleText: "Try Again", description: warningMessage, showWarningPopup: $showWarning)
                }
            }
        }
    }
}

#Preview {
    PasswordLoginView(userLoginViewModel: UserLoginViewModel())
        .environmentObject(ProfileImageLoader())
}
