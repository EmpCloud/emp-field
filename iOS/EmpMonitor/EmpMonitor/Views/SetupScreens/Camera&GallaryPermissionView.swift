//
//  Camera&GallaryPermissionView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI

struct Camera_GallaryPermissionView: View {
    
    @EnvironmentObject var permissionManager: PermissionManager
    
    var body: some View {
        ZStack(alignment: .top) {
            Color.white
                .ignoresSafeArea(.all)
            
            Image(.topSetupScreenBg)
                .ignoresSafeArea()
            
            VStack {
                
                Image(.cameraGallaryPermission)
                    .padding(.top, 120)
                
                HStack{
                    VStack{
                        Text("Camera & Gallary")
                            .font(.custom("Montserrat", size: 25))
                            .fontWeight(.semibold)
                            .foregroundStyle(Color.welcomeText)
                        
                        Text("Permission")
                            .font(.custom("Montserrat", size: 25))
                            .fontWeight(.semibold)
                            .foregroundStyle(Color.sideTitleText)
                    }
                }
                .padding(.top, 30)
                
                Text("Activity tracking permission")
                    .font(.custom("Montserrat", size: 16))
                    .foregroundStyle(Color.subText)
                    .padding()
            }

        }
        .onAppear{
            permissionManager.requestCameraPermission()
            permissionManager.requestPhotoLibraryPermission()
        }
    }
}

#Preview {
    Camera_GallaryPermissionView()
}
