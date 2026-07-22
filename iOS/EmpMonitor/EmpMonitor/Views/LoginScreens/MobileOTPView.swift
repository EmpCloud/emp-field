//
//  MobileOTPView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 19/06/24.
//

import SwiftUI

struct MobileOTPView: View {
    
    @State var enteredOTP = ""
    
    ///- Keyboard State
    @FocusState private var isKeyboardShowing: Bool
    
    var body: some View {
        ZStack(alignment: .top){
            Color.white
                .ignoresSafeArea(.all)
            
            Image(.loginScreenTopBg)
                .ignoresSafeArea(.all)
            
            VStack {
                
                //MARK: App ICON
                CircularAppIcon()
                
                Text("Login")
                    .font(AppFont.primary(size: AppFont.Size.screenTitle))
                    .padding(.top, 150)
                    .padding(.bottom, 20)
                
                //MARK: OTP Textfield
                
                //OTP Box
                HStack(spacing: 5){
                    ForEach(0..<6, id: \.self) { index in
                         OTPTextBox(index)
                    }
                }
                .background(content: {
                    ///- Hiding the TextField
                    TextField("", text: $enteredOTP.limit(6))
                        .keyboardType(.numberPad)
//                        .textContentType(.emailAddress)    // It will show the most recent email text content
                        .frame(width: 1, height: 1)
                        .opacity(0.001)
                        .blendMode(.screen)
                        .focused($isKeyboardShowing)
                })
                .contentShape(Rectangle())
                .onTapGesture {
                    isKeyboardShowing.toggle()
                }
                .padding(.horizontal, 25)
                
                
                //MARK: OTP Button
                PrimaryButton(text: "Proceed") {
                    // next screen
                }
                .padding(.horizontal, 25)
                
                
                HStack {
                    Text("Didn't received OTP?")
                        .font(AppFont.primary(size: AppFont.Size.body))
                        .foregroundStyle(Color.authSubText)
                    
                    Text("Resend")
                        .font(AppFont.primary(size: AppFont.Size.headline))
                        .foregroundStyle(Color.authBlueSubText)
                }
                .padding()
                
            }

        }
    }
    
    // MARK: OTP Text Box
    @ViewBuilder
    func OTPTextBox(_ index: Int) -> some View{
        ZStack {
            if enteredOTP.count > index {    // safe check for avoiding crashes while reading the string index
                
                /// - Finding Char AT Index
                let startIndex = enteredOTP.startIndex
                let charIndex = enteredOTP.index(startIndex, offsetBy: index)
                let charToString = String(enteredOTP[charIndex])
                Text(charToString)
                    .font(AppFont.primary(size: AppFont.Size.title3))
//                    .foregroundStyle(Color.baseRed)
                
            }else{
                Text(" ")
            }
        }
        .frame(width: 50, height: 58)
        
        .background {
            ///- Highlighting Current Active Box
            let status = (isKeyboardShowing && enteredOTP.count == index)
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


//MARK: Binding <String> Extension
//limiting the input to 4 digits
extension Binding where Value == String {
    func limit(_ length: Int) -> Self {
        if self.wrappedValue.count > length {
            DispatchQueue.main.async {
                self.wrappedValue = String(self.wrappedValue.prefix(length))

            }
        }
        return self
    }
}


#Preview {
    MobileOTPView()
}
