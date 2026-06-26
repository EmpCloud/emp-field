//
//  CircularAppIcon.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI

struct CircularAppIcon: View {
    var body: some View {
        ZStack{
            
            Circle()
                .fill(
                    //MARK: Inner Shadow
                    .shadow(.inner(color: Color.innerShadow, radius: 12, y: -6))
                    
                )
                .foregroundStyle(
                    LinearGradient(gradient: Gradient(colors: [Color.white, Color.circularAppIcon]), startPoint: .top, endPoint: .bottom)
                )
            //MARK: Outer Shadow
                .shadow(color: Color(red: 0/255, green: 0/255, blue: 0/255, opacity: 0.15), radius: 16, y: 2)
                .overlay(content: {
                    Image(.loginScreenAppIcon)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 78.67, height: 78.67)
                })
                .frame(width: 115, height: 115)
                .padding(.top, 50)
        }
    }
}

#Preview {
    CircularAppIcon()
}
