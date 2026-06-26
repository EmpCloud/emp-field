//
//  CheckINDisableButtonView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 21/08/24.
//

import SwiftUI

struct CheckINDisableButtonView: View {
    
    var text: String
    var action: () -> Void
    
    var body: some View {
        ZStack {
            Button(action: action) {
                ZStack {
                    //swipe track
                    Capsule()
                        .frame(width: 203, height: 42)
                        .foregroundStyle(Color.swipeBG)
                    
                    Text(text)
                        .font(.custom("Montserrat", size: 13))
                        .fontWeight(.medium)
                        .offset(x: 10.0)
                    
                    ZStack {
                        Circle()
                            .fill(
                                LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                            )
                            .frame(width: 36, height: 36)
                        
                        Image(systemName: "arrow.right")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 13.31, height: 17.66)
                            .foregroundStyle(Color.white)
                        
                    }
                    .offset(x: -80, y: 0)
                }
            }
        }
    }
}

struct CheckOUTDisableButtonView: View {
    
    var text: String
    var action: () -> Void
    
    var body: some View {
        ZStack {
            Button(action: action) {
                ZStack {
                    //swipe track
                    Capsule()
                        .frame(width: 203, height: 42)
                        .foregroundStyle(Color.swipeBG)
                    
                    Text(text)
                        .font(.custom("Montserrat", size: 13))
                        .fontWeight(.medium)
                        .offset(x: -10.0)
                    
                    ZStack {
                        Circle()
                            .fill(
                                LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                            )
                            .frame(width: 36, height: 36)
                        
                        Image(systemName: "arrow.left")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 13.31, height: 17.66)
                            .foregroundStyle(Color.white)
                        
                    }
                    .offset(x: 80, y: 0)
                }
            }
        }
    }
}

#Preview {
    CheckINDisableButtonView(text: "Swipe to check IN"){
        print("clicked")
    }
}

#Preview {
    CheckOUTDisableButtonView(text: "Swipe to check OUT"){
        print("clicked")
    }
}
