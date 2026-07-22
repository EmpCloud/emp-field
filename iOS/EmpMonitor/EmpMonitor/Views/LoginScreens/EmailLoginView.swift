//
//  EmailLoginView.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 19/06/24.
//

import SwiftUI

struct EmailLoginView: View {
    
    @Environment(\.dismiss) var dismiss
    
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    @StateObject private var userLoginViewModel: UserLoginViewModel = UserLoginViewModel()
    @StateObject private var verifyEmailViewModel: VerifyEmailViewModel = VerifyEmailViewModel()
    
    @State private var isValid: Bool = false
    @State private var passwordScreen: Bool = false
    @State private var isValidEmail: Bool = false
//    @State private var emailText: String = ""
    
    @State private var showEmailFieldEmpty: Bool = false
    
    //warning Popup
    @State private var showWarning: Bool = false
    
    //Email OTP Screen
//    @State private var showEmailOTPScreen: Bool = false
    
    var body: some View {
        NavigationStack {
            ZStack(alignment: .top){
                Color.white
    //                .ignoresSafeArea(.container)
                
                Image(.loginScreenTopBg)
    //                .ignoresSafeArea(.container)
                
                VStack(spacing: 20) {
                    
                    //MARK: AppIcon
                    CircularAppIcon()
                        .padding(.top, 40)
                    
                    Text("Login")
                        .font(AppFont.primary(size: AppFont.Size.screenTitle))
                        .padding(.top, 150)
                        .padding(.bottom, 20)
                    
                    //MARK: Email Validation warning
                    if !isValidEmail && !userLoginViewModel.email.isEmpty {
                        VStack(alignment: .leading) {
                            Text("Please enter a valid email*")
                                .font(AppFont.primary(size: AppFont.Size.xSmall))
                                .foregroundStyle(Color.absent)
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(.horizontal, 60)
                    }
                    
                    //MARK: Warning for forget email
                    if showEmailFieldEmpty {
                        VStack(alignment: .leading) {
                            Text("Email field is empty. Please enter a valid email.")
                                .font(AppFont.primary(size: AppFont.Size.xSmall))
                                .foregroundStyle(Color.absent)
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(.horizontal, 60)
                        .onAppear {
                            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                                withAnimation {
                                    showEmailFieldEmpty.toggle()
                                }
                            }
                        }
                    }
                    
                    AuthTextField(text: $userLoginViewModel.email, placeholder: "Type Email")
                        .padding(.horizontal, 50)
                   
                    
                    //MARK: Send Password Button
                    PrimaryButton(text: "Add Password", action: {
                        
                        Task {
                            verifyEmailViewModel.email = userLoginViewModel.email
                            try await verifyEmailViewModel.verifyUserEmail()
                            
                            //next screen
                            if NetworkManager.shared.statusCode == 200 {
                                AppLog.debug(userLoginViewModel.email)
                                passwordScreen = true
                            }else {
                                showWarning.toggle()
                            }
                        }
                    })
                    .padding(.horizontal, 50)
                    .disableWithOpacity(!isValidEmail)
                    
                    
    //                Text("Forgot Password ?")
    //                    .font(AppFont.primary(size: AppFont.Size.body))
    //                    .foregroundStyle(Color.authSubText)
    //                    .padding(.top)
                        
                }
                
                
                //Warning Popup
                if showWarning {
                    ModalOverlayView {
                        WarningPopupView(titleText: "Try Again", description: NetworkManager.shared.responseMessage, showWarningPopup: $showWarning)
                    }
                }
                
                
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
            .ignoresSafeArea(.container)
            .onChange(of: userLoginViewModel.email) { _, newValue in
                withAnimation {
                    isValidEmail = Validator.validateEmail(newValue)
                }
            }
            .navigationDestination(isPresented: $passwordScreen) {
                PasswordLoginView(userLoginViewModel: userLoginViewModel)
                    .environmentObject(profileImageLoader)
                    .navigationBarBackButtonHidden()
            }
    //        .navigationDestination(isPresented: $showEmailOTPScreen) {
    //            EmailOTPView()
    //                .navigationBarBackButtonHidden()
    //        }
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    BackButtonView()
                        .onTapGesture {
                            dismiss()
                        }
                }
            }
        }
        
    }
}

#Preview {
    EmailLoginView()
        .environmentObject(ProfileImageLoader())
}
