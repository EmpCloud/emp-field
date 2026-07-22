//
//  ResetPasswordView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 19/06/24.
//

import SwiftUI

struct ResetPasswordView: View {
    
    @Environment(\.dismiss) var dismiss
    
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    @ObservedObject var resetPasswordViewModel: ResetPasswordViewModel
    
    @State private var emailLoginScreen: Bool = false
    @State private var passwordText: String = ""
    @State private var confirmPasswordText: String = ""
    @State private var showNewPassword: Bool = false
    @State private var showConfirmPassword: Bool = false
    
    //Warning
    @State private var showWarningPopup: Bool = false
    
    var body: some View {
        ZStack(alignment: .top){
            Color.white
                .ignoresSafeArea(.all)
            
            Image(.loginScreenTopBg)
                .ignoresSafeArea(.all)
            
            VStack(spacing: 20) {
                
                //MARK: AppIcon
                CircularAppIcon()
                
                Text("Reset Password")
                    .font(AppFont.primary(size: AppFont.Size.screenTitle))
                    .padding(.top, 150)
                    .padding(.bottom, 20)
                
                AuthPasswordTextField(text: $passwordText, showPassword: $showNewPassword, placeholder: "New Password")
                    .textContentType(.newPassword)
                    .padding(.horizontal, 50)

                AuthPasswordTextField(text: $confirmPasswordText, showPassword: $showConfirmPassword, placeholder: "Confirm Password")
                    .textContentType(.newPassword)
                    .padding(.horizontal, 50)
                
                //MARK: Send Password Button
                PrimaryButton(text: "Login", action: {
                    //next screen
                    
                    resetPasswordViewModel.newPassword = confirmPasswordText
                    
                    //TODO: Reset password api call
                    Task {
                        try await resetPasswordViewModel.resetPassword()
                    }
                    if NetworkManager.shared.statusCode == 200 {
                        emailLoginScreen = true
                    }else{
                        showWarningPopup.toggle()
                    }
                    
                })
                .padding(.horizontal, 50)
            }
            
            if showWarningPopup {
                ModalOverlayView {
                    WarningPopupView(titleText: "Try Again", description: NetworkManager.shared.responseMessage, showWarningPopup: $showWarningPopup)
                }
            }
            
        }
        .fullScreenCover(isPresented: $emailLoginScreen, content: {
            EmailLoginView()
                .environmentObject(profileImageLoader)
                .navigationBarBackButtonHidden()
        })
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

#Preview {
    ResetPasswordView(resetPasswordViewModel: ResetPasswordViewModel())
}
