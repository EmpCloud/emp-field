//
//  AuthTextField.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI

struct AuthTextField: View {

    @Binding var text: String
    var placeholder: String
    var body: some View {
        ZStack {
            TextField(placeholder, text: $text)
                .font(AppFont.primary(size: AppFont.Size.subheadline))
                .foregroundStyle(Color.subText)
                .padding(.horizontal, AppSpacing.lg)
                .frame(maxWidth: .infinity)
                .frame(minHeight: AppLayout.textFieldHeightLarge)
                .background(.white)
                .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
                .shadow(color: Color.textFieldShadow, radius: 19)
                .keyboardType(.emailAddress)
                .textContentType(.emailAddress)
                .autocapitalization(.none)
                .autocorrectionDisabled()
        }
    }
}

struct AuthPasswordTextField: View {
    
    @Binding var text: String
    @Binding var showPassword: Bool
    var placeholder: String
    var body: some View {
        ZStack {
            HStack {
                if showPassword {
                    TextField(placeholder, text: $text)
                        .font(AppFont.primary(size: AppFont.Size.subheadline))
                        .foregroundStyle(Color.subText)
                        .padding(.leading, AppSpacing.lg)
                        .autocapitalization(.none)
                }else{
                    SecureField(placeholder, text: $text)
                        .font(AppFont.primary(size: AppFont.Size.subheadline))
                        .foregroundStyle(Color.subText)
                        .padding(.leading, AppSpacing.lg)
                        .autocapitalization(.none)
                }
                
                Button {
                    withAnimation {
                        showPassword.toggle()
                    }
                } label: {
                    Image(systemName: showPassword ? "eye.slash" : "eye")
                        .foregroundStyle( Color.primaryButton1)
                        .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                    
                }
                .padding(.trailing, AppSpacing.sm)
                .accessibilityLabel(showPassword ? "Hide password" : "Show password")
            }
            .frame(maxWidth: .infinity)
            .frame(minHeight: AppLayout.textFieldHeightLarge)
            .background(.white)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
            .shadow(color: Color.textFieldShadow, radius: 19)
            
        }
    }
}

#Preview {
    AuthPasswordTextField(text: .constant(""), showPassword: .constant(false), placeholder: "Mobile Number")
}
