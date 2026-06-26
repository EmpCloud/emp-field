//
//  LocationPermissionView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI

struct LocationPermissionView: View {
    
    @EnvironmentObject var permissionManager: PermissionManager
    
    var body: some View {
        ZStack(alignment: .top) {
            Color.white
            
            Image(.topSetupScreenBg)
                .frame(alignment: .top)
                .ignoresSafeArea(.all)
            
            VStack() {
              
                Image(.locationPermission)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .padding(.top, 150)
                
                ZStack {
                    VStack {
                        HStack{
                            Text("Location")
                                .font(.custom("Montserrat", size: 25))
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.welcomeText)
                            Text("Permission")
                                .font(.custom("Montserrat", size: 25))
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.sideTitleText)
                        }
                        
                        Text("Asking for your location")
                            .font(.custom("Montserrat", size: 16))
                            .foregroundStyle(Color.subText)
                            .padding()
                    }
                    
                    
                }
                .frame(alignment: .top)
                .padding(.top, -25)
               
            }
            
            
        }
        .onAppear{
            permissionManager.requestLocation()
        }
        .ignoresSafeArea(.all)
        
    }
}

#Preview {
    LocationPermissionView()
        .environmentObject(PermissionManager())
}
