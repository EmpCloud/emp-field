//
//  BackgroundPermissionView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI

struct BackgroundPermissionView: View {
    var body: some View {
        ZStack(alignment: .top) {
            Color.white
                .ignoresSafeArea(.all)
            
            Image(.topSetupScreenBg)
                .ignoresSafeArea(.all)

            
            VStack{
                
                Image(.backgroundPermission)
                    .padding(.top, 150)
                
                HStack{
                    Text("Background")
                        .font(.custom("Montserrat", size: 25))
                        .fontWeight(.semibold)
                        .foregroundStyle(Color.welcomeText)
                    
                    Text("Permission")
                        .font(.custom("Montserrat", size: 25))
                        .fontWeight(.semibold)
                        .foregroundStyle(Color.sideTitleText)
                }
                .padding(.top, 70)
                
                Text("Asking the background Permission")
                    .font(.custom("Montserrat", size: 16))
                    .foregroundStyle(Color.subText)
                    .padding()
            }
        }
    }
}

#Preview {
    BackgroundPermissionView()
}
