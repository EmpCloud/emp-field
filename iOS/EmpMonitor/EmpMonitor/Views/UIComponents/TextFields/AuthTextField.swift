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
                .font(.custom("Comfortaa", size: 15))
                .foregroundStyle(Color.subText)
                .padding()
                .padding(.horizontal, 20)
                .frame(maxWidth: .infinity)
                .background(.white)
                .clipShape(RoundedRectangle(cornerRadius: 10))
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
                        .font(.custom("Comfortaa", size: 15))
                        .foregroundStyle(Color.subText)
                        .padding()
                        .padding(.horizontal, 20)
                        .autocapitalization(.none)
                }else{
                    SecureField(placeholder, text: $text)
                        .font(.custom("Comfortaa", size: 15))
                        .foregroundStyle(Color.subText)
                        .padding()
                        .padding(.horizontal, 20)
                        .autocapitalization(.none)
                }
                
                Button {
                    withAnimation {
                        showPassword.toggle()
                    }
                } label: {
                    Image(systemName: showPassword ? "eye.slash" : "eye")
                        .foregroundStyle( Color.primaryButton1)
                        .padding(.horizontal, 7)
                    
                }
                .padding(.horizontal)
            }
            .frame(maxWidth: .infinity)
            .background(.white)
            .clipShape(RoundedRectangle(cornerRadius: 10))
            .shadow(color: Color.textFieldShadow, radius: 19)
            
        }
    }
}

#Preview {
    AuthPasswordTextField(text: .constant(""), showPassword: .constant(false), placeholder: "Mobile Number")
}
