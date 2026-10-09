//
//  LoginView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI

struct LoginView: View {
    
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    @State private var buttons: [String] = ["Email", "Mobile"]
    
    @State private var selectedButton: String = ""
    
    @State private var emailScreen: Bool = false
    @State private var mobileScreen: Bool = false
    
    var body: some View {
        NavigationStack {
            ZStack(alignment: .top){
                Color.white
                    .ignoresSafeArea(.all)
                
                Image.fullWidthBackground(.loginScreenTopBg)
                    .ignoresSafeArea(.all)
                
                VStack {
                    
                    //MARK: AppIcon
                    CircularAppIcon()
                    
                    Text("Login With")
                        .font(AppFont.primary(size: AppFont.Size.screenTitle))
                        .fontWeight(AppFont.Weight.medium)
                        .foregroundStyle(Color.headingText)
                        .padding(.top, 170)
                        .padding(.bottom)
                    
                    
                    //MARK: Radio Buttons
                    HStack{
                        ForEach(buttons, id:\.self){button in
                            Button(action: {
                                selectedButton = button
    //                            AppLog.debug(selectedButton)
                            }, label: {
                                HStack{
                                    Circle()
                                        .stroke(
                                            LinearGradient(gradient: Gradient(colors: [Color.blueGradient1, Color.blueGradient2]), startPoint: .top, endPoint: .bottom) ,
                                                lineWidth: 8.0
                                        )
                                        .frame(width: 24, height: 24)
                                        .overlay {
                                            Circle()
                                                .fill( selectedButton == button ?
                                                       LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                                                       :
                                                        LinearGradient(gradient: Gradient(colors: [Color.white]), startPoint: .top, endPoint: .bottom)  )
                                                .frame(width: 16, height: 16)
                                        }
                                    
                                    Text(button)
                                        .font(AppFont.primary(size: AppFont.Size.screenTitle))
                                        .foregroundStyle(Color.subText)
                                        .lineLimit(1)
                                        .minimumScaleFactor(0.5)
                                }
                                .frame(minHeight: AppLayout.minimumTouchTarget)
                                .contentShape(Rectangle())
                                .accessibilityAddTraits(selectedButton == button ? .isSelected : [])

                            })
                            Spacer()
                        }
                      
                    }
                    .padding()
                    .padding(.top)
                    .padding(.leading, 50)
                    .padding(.trailing, 0)
                    // Keeps the options and button a readable width on iPad, in any orientation.
                    .frame(maxWidth: 500)
                    
                    //MARK: Continue button
                    PrimaryButton(text: "Continue") {
    //                    AppLog.debug(selectedButton)
                        
                        if selectedButton == "Email" {
                            
                            emailScreen = true
                            mobileScreen = false
                        }
                        else if selectedButton == "Mobile" {
                            mobileScreen = true
                            emailScreen = false
                        }
                    }
                    .padding(.top, 50)
                    .padding(.horizontal, 50)
                    .frame(maxWidth: 500)
                    .disableWithOpacity(selectedButton == "")
                }

            }
            .navigationDestination(isPresented: $mobileScreen) {
                MobileLoginView()
                    .environmentObject(profileImageLoader)
            }
            .navigationDestination(isPresented: $emailScreen) {
                EmailLoginView()
                    .environmentObject(profileImageLoader)
                    .navigationBarBackButtonHidden()
            }
        }
    }
}

#Preview {
    LoginView()
}
