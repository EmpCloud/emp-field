//
//  ActivityPermissionView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI

struct ActivityPermissionView: View {
    
    @EnvironmentObject var permissionManager: PermissionManager
    
    var body: some View {
        ZStack(alignment: .top) {
            Color.white
                .ignoresSafeArea(.all)
            
            Image(.topSetupScreenBg)
                .ignoresSafeArea()
            
            VStack {
                
                Image(.activityPermission)
                    .padding(.top, 120)
                
                HStack{
                    Text("Activity")
                        .font(.custom("Montserrat", size: 25))
                        .fontWeight(.semibold)
                        .foregroundStyle(Color.welcomeText)
                    
                    Text("Permission")
                        .font(.custom("Montserrat", size: 25))
                        .fontWeight(.semibold)
                        .foregroundStyle(Color.sideTitleText)
                }
                .padding(.top, 30)
                
                Text("Activity tracking permission")
                    .font(.custom("Montserrat", size: 16))
                    .foregroundStyle(Color.subText)
                    .padding()
            }

        }
        .onAppear{
            permissionManager.requestMotionPermission()
        }
    }
}

#Preview {
    ActivityPermissionView()
}
