//
//  SplashView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 26/06/24.
//

import SwiftUI

struct SplashView: View {
    @State var showAnimation: Bool = false
    @Binding var showSplashScreen: Bool
    var body: some View {
        ZStack{
            Color.white
                .ignoresSafeArea()
            
            VStack{
                Spacer()
                Image(.splashScreenAppLogo)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 280, height: 110)
                    .onAppear {
                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5){
                            withAnimation(.snappy(duration: 1)) {
                                showAnimation = true
                            }
                        }
                    }
                
                Spacer()
                if showAnimation {
                    Image(.splashScreen)
                        .padding(.bottom, -40)
                    Image(.splashScreenBottomBackground)
                        .frame(height: 210, alignment: .bottom)
                }
            }
            .ignoresSafeArea()
        }
        .onAppear{
            withAnimation(.easeInOut(duration: 3)){
                DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                    showSplashScreen.toggle()
                }
            }
        }
    }
}

#Preview {
    SplashView(showSplashScreen: .constant(false))
}
