//
//  WelcomeView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI

struct WelcomeView: View {

    @EnvironmentObject var profileImageLoader: ProfileImageLoader

    @State private var nextScreen: Bool = false
    @State private var isChecked: Bool = false
    @State private var showTermsWebView: Bool = false
    
    
    var body: some View {
        ZStack(alignment: .bottom) {
            Image(.splashScreenBottomBackground)
                .ignoresSafeArea()

            VStack(spacing: 0) {
                Spacer()

                VStack(spacing: 32) {
                    Text("Welcome!")
                        .font(.custom("Montserrat", size: 56))
                        .fontWeight(.bold)
                        .foregroundStyle(Color.welcomeText)
                        .tracking(0.5)

                    Image(.welcomeScreen)
                        .resizable()
                        .scaledToFit()
                        .frame(maxHeight: 240)
                }
                .frame(maxWidth: .infinity, alignment: .center)

                Spacer()

                VStack(spacing: 20) {
                    Button(action: { isChecked.toggle() }) {
                        HStack(spacing: 12) {
                            Image(systemName: isChecked ? "checkmark.square.fill" : "square")
                                .font(.system(size: 28, weight: .semibold))
                                .foregroundStyle(isChecked ? Color.blue : Color.checkbox)
                                .frame(width: 28, height: 28)

                            Text("Accept Terms & Condition")
                                .font(.custom("Comfortaa", size: 16))
                                .foregroundStyle(Color.termCondition)
                                .fontWeight(.medium)
                                .multilineTextAlignment(.center)
                        }
                        .frame(maxWidth: .infinity)
                        .contentShape(Rectangle())
                    }
                    .padding(.horizontal, 20)
                    .padding(.vertical, 20)
                    .background(Color.white.opacity(0.95))
                    .cornerRadius(12)
                    .shadow(color: Color.black.opacity(0.1), radius: 8, x: 0, y: 2)
                    .onChange(of: isChecked) { newValue in
                        if newValue {
                            startDelay()
                        }
                    }
                    .accessibilityLabel("Accept Terms and Conditions")
                    .accessibilityHint("Double tap to accept")

                    VStack(spacing: 8) {
                        VStack(spacing: 4) {
                            Text("You must agree to")
                                .font(.custom("Montserrat", size: 14))
                                .foregroundStyle(Color.subText)

                            HStack(spacing: 4, content: {
                                Button(action: { showTermsWebView = true }) {
                                    Text("terms and conditions")
                                        .font(.custom("Montserrat", size: 14))
                                        .fontWeight(.semibold)
                                        .foregroundStyle(.blue)
                                        .underline(true, color: .blue)
                                }
                                .accessibilityLabel("Terms and Conditions")
                                .accessibilityHint("Double tap to view")
                            })

                            Text("to proceed")
                                .font(.custom("Montserrat", size: 14))
                                .foregroundStyle(Color.subText)
                        }
                        .frame(maxWidth: .infinity)
                        .multilineTextAlignment(.center)
                    }
                    .frame(maxWidth: .infinity, alignment: .center)
                    .padding(.horizontal, 16)
                }
                .frame(maxWidth: .infinity)
                .padding(.horizontal, 16)
                .padding(.bottom, 50)
            }
        }
        .ignoresSafeArea(.all)
        .navigationDestination(isPresented: $nextScreen) {
//            LanguageView()
//                .environmentObject(profileImageLoader)
//                .navigationBarBackButtonHidden()
            SetupTabView()
                .environmentObject(profileImageLoader)
                .navigationBarBackButtonHidden()
        }
        .sheet(isPresented: $showTermsWebView) {
            TermConditionView()
        }
    }
    
    func startDelay(){
        DispatchQueue.main.asyncAfter(deadline: .now() + 1){
            UserDefaults.standard.set(true, forKey: "hasAcceptedTerms")
            nextScreen = true
        }
    }
}

#Preview {
    WelcomeView()
}
