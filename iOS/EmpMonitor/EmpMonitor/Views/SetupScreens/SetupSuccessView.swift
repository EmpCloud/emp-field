//
//  SetupSuccessView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI
import Lottie

struct SetupSuccessView: View {
    var body: some View {
        ZStack(alignment: .top) {
            Color.white
                .ignoresSafeArea(.all)
            
            Image(.topSetupScreenBg)
                .ignoresSafeArea()
            
            VStack {
                
//                Image(.setupSuccess)
//                    .padding(.top, 150)
                
                LottieView(animation: .named("Success.json"))
                    .configure { lottieAnimationView in
                        lottieAnimationView.contentMode = .scaleAspectFit
                    }
                    .playbackMode(.playing(.toProgress(1, loopMode: .loop)))
                    .padding(.top, 150)
                
                HStack{
                    Text("Setup")
                        .font(.custom("Montserrat", size: 25))
                        .fontWeight(.semibold)
                        .foregroundStyle(Color.welcomeText)
                    
                    Text("Successful")
                        .font(.custom("Montserrat", size: 25))
                        .fontWeight(.semibold)
                        .foregroundStyle(Color.sideTitleText)
                }
                .padding(.top, 100)
                
                Text("Activity tracking permission")
                    .font(.custom("Montserrat", size: 16))
                    .foregroundStyle(Color.subText)
                    .padding()
            }

        }
    }
}

#Preview {
    SetupSuccessView()
}
