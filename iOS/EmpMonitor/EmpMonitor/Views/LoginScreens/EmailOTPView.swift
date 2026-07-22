//
//  EmailOTPView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 19/06/24.
//

import SwiftUI

struct EmailOTPView: View {
    
    @Environment(\.dismiss) var dismiss
    
    @ObservedObject var forgetPasswordViewModel: ForgetPasswordViewModel
//    @ObservedObject var userLoginViewModel: UserLoginViewModel
    @StateObject private var resetPasswordViewModel = ResetPasswordViewModel()
    @StateObject private var verifyEmailOTPViewModel = VerifyEmailOTPViewModel()
    
    @State private var emailOTP = ""
    
    ///- Keyboard State
    @FocusState private var isEmailKeyboardShowing: Bool
    
    //Show Reset password screen
    @State private var showResetPasswordScreen: Bool = false
    
    //Warning
    @State private var showWarningPopup: Bool = false
    
    
    //Success
    @State private var showOTPSendConfirmation: Bool = false
    
    var body: some View {
        ZStack(alignment: .top){
            Color.white
                .ignoresSafeArea(.all)
            
            Image(.loginScreenTopBg)
                .ignoresSafeArea(.all)
            
            VStack(spacing: 20) {
                
                //MARK: App ICON
                CircularAppIcon()
                
                Text("Login")
                    .font(AppFont.primary(size: AppFont.Size.screenTitle))
                    .padding(.top, 130)
                    .padding(.bottom, 20)
                
                //MARK: OTP Textfield
                
                //OTP Box
                HStack(spacing: 0){
                    ForEach(0..<4, id: \.self) { index in
                        OTPTextBox(index)
                    }
                }
                .background(content: {
                    ///- Hiding the TextField
                    TextField("", text: $emailOTP.limit(4))
                        .keyboardType(.numberPad)
                    //                        .textContentType(.emailAddress)    // It will show the most recent email text content
                        .frame(width: 1, height: 1)
                        .opacity(0.001)
                        .blendMode(.screen)
                        .focused($isEmailKeyboardShowing)
                        .toolbarDoneButton()
                })
                .contentShape(Rectangle())
                .onTapGesture {
                    isEmailKeyboardShowing.toggle()
                }
                .padding(.horizontal, 25)
                
                
                //MARK: OTP Button
                PrimaryButton(text: "Proceed") {
                    // next screen
                    Task {
                        resetPasswordViewModel.verifyToken = emailOTP
                        resetPasswordViewModel.email = forgetPasswordViewModel.email
//                        AppLog.debug(forgetPasswordViewModel.email)
//                        AppLog.debug(emailOTP)
                        
                        try await verifyEmailOTPViewModel.verifyEmailOTP(email: forgetPasswordViewModel.email, emailOTP: emailOTP)
                        
                        if NetworkManager.shared.statusCode == 200 {
                            showResetPasswordScreen.toggle()
                        }else {
                            showWarningPopup.toggle()
                        }
                        
                    }
                    
                }
                .padding(.horizontal, 35)
                .disableWithOpacity(emailOTP.isEmpty || emailOTP.count <= 3)
                
                
                HStack {
                    Text("Didn't received OTP?")
                        .font(AppFont.primary(size: AppFont.Size.body))
                        .foregroundStyle(Color.authSubText)
                    
                    Text("Resend")
                        .font(AppFont.primary(size: AppFont.Size.headline))
                        .foregroundStyle(Color.authBlueSubText)
                        .onTapGesture {
                            Task {
                                try await forgetPasswordViewModel.forgetPassword()
                                
                                //next screen
                                if NetworkManager.shared.statusCode == 200 {
                                    //                                        AppLog.debug(userLoginViewModel.email)
                                    showOTPSendConfirmation = true
                                }else {
                                    withAnimation {
//                                        showWarning.toggle()
                                    }
                                }
                            }
                        }
                }
                .padding()
                
            }
            
            
            //Success Popup
//            if showOTPSendConfirmation {
//                ZStack {
//                    WarningPopupView(titleText: "Try Again", description: NetworkManager.shared.responseMessage, showWarningPopup: $showWarningPopup)
//                }
//                .frame(maxWidth: .infinity, maxHeight: .infinity)
//                .background(Color.black.opacity(0.5))
//                .onTapGesture {
//                    showWarningPopup.toggle()
//                }
//            }

            
            //Warning Popup
            if showWarningPopup {
                ModalOverlayView {
                    WarningPopupView(titleText: "Try Again", description: NetworkManager.shared.responseMessage, showWarningPopup: $showWarningPopup)
                }
            }
            
        }
        .navigationDestination(isPresented: $showResetPasswordScreen) {
            ResetPasswordView(resetPasswordViewModel: resetPasswordViewModel)
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
    
    // MARK: OTP Text Box
    @ViewBuilder
    func OTPTextBox(_ index: Int) -> some View{
        ZStack {
            if emailOTP.count > index {    // safe check for avoiding crashes while reading the string index
                
                /// - Finding Char AT Index
                let startIndex = emailOTP.startIndex
                let charIndex = emailOTP.index(startIndex, offsetBy: index)
                let charToString = String(emailOTP[charIndex])
                Text(charToString)
                    .font(AppFont.primary(size: AppFont.Size.title3))
                //                    .foregroundStyle(Color.baseRed)
                
            }else{
                Text(" ")
            }
        }
        .frame(width: 56.41, height: 58)
        
        .background {
            ///- Highlighting Current Active Box
            let status = (isEmailKeyboardShowing && emailOTP.count == index)
            RoundedRectangle(cornerRadius: 8)
                .fill(Color.white)
                .animation(.easeOut(duration: 0.2), value: status)
                .shadow(color: Color.textFieldShadow, radius: 10)
                .overlay {
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(status ? Color.strokeGray : Color.white)
                }
            
        }
        .shadow(color: Color.textFieldShadow, radius: 10)
        .frame(maxWidth: .infinity)
    }
    
}

#Preview {
    EmailOTPView(forgetPasswordViewModel: ForgetPasswordViewModel())
}
