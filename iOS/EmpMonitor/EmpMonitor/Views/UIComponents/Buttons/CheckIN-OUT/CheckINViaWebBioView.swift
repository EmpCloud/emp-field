//
//  CheckINViaWebBioView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/08/24.
//

import SwiftUI

struct CheckINViaWebBioView: View {
    var body: some View {
        ZStack {
            //swipe track
            Capsule()
                .frame(width: 203, height: 42)
                .foregroundStyle(Color.white)
                .overlay {
                    Capsule().stroke(Color.primaryButton1, lineWidth: 1)
                }
            
            Text("Web/Biometric Check IN")
                .font(.custom("Montserrat", size: 12))
                .foregroundStyle(Color.primaryButton1)
                .fontWeight(.medium)
                .offset(x: 12.0)
            
            ZStack {
                Circle()
                    .fill(
                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                    )
                    .frame(width: 28, height: 28)
                
                Image(.webBioCheckINIcon)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 13.31, height: 17.66)
                    .foregroundStyle(Color.white)
                
            }
            .offset(x: -80, y: 0)
        }
    }
}

#Preview {
    CheckINViaWebBioView()
}
