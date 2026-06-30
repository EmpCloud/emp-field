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
    
    //Hide the password
    @State private var showPassword: Bool = false
    
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
                    .font(.custom("Montserrat", size: 25))
                    .padding(.top, 170)
                    .padding(.bottom, 20)
                
                AuthPasswordTextField(text: $userLoginViewModel.password, showPassword: $showPassword, placeholder: "Enter Password")
                    .padding(.horizontal, 50)
                
                //MARK: Login Button
                PrimaryButton(text: "Login") {
                    //next screen
                    Task {
                        try await userLoginViewModel.loginUser()
                        
                        if NetworkManager.shared.statusCode == 200{
                            
                            if AuthStore.shared.getUserProfileData(as: CreateProfileResponseModel.self) != nil {
                                
                                profileImageLoader.profileImageURL = UserDefaults.standard.string(forKey: "UserProfilePic") ?? ""
                                
                                showTabMainView = true
                                
                            }else{
                                createProfileScreen = true
                            }
                            
                            //-------------logout changes -------
                        }
                        else {
                            showWarning.toggle()
                        }
                        
                        //                        appState.showToaster.toggle()
                    }
                    
                }
                .padding(.horizontal, 50)
                .disableWithOpacity(disableButton)
                
                
                Text("Forgot Password?")
                    .font(.custom("Montserrat", size: 14))
                    .foregroundStyle(Color.authSubText)
                    .padding(.top)
                    .onTapGesture {
                        Task {
                            forgetPasswordViewModel.email = userLoginViewModel.email
                            try await forgetPasswordViewModel.forgetPassword()
                            
                            //next screen
                            if NetworkManager.shared.statusCode == 200 {
                                //                                        print(userLoginViewModel.email)
                                showEmailOTPScreen = true
                            }else {
                                withAnimation {
                                    showWarning.toggle()
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
                ZStack {
                    WarningPopupView(titleText: "Try Again", description: NetworkManager.shared.responseMessage, showWarningPopup: $showWarning)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.black.opacity(0.5))
                .onTapGesture {
                    showWarning.toggle()
                }
            }
        }
    }
}

#Preview {
    PasswordLoginView(userLoginViewModel: UserLoginViewModel())
        .environmentObject(ProfileImageLoader())
}
