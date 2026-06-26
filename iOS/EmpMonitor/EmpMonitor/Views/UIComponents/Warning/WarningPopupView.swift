//
//  WarningPopupView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 09/09/24.
//

import SwiftUI

struct WarningPopupView: View {
    
    var titleText: String
    var description: String
    
    @Binding var showWarningPopup: Bool
    
    var body: some View {
        VStack(spacing: 10) {
            HStack {
                Spacer()
                Image(systemName: "xmark")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 10.75, height: 10.75)
                    .fontWeight(.bold)
                    .foregroundStyle(Color.primaryButton1)
                    .padding(10)
                    .onTapGesture {
                        withAnimation {
                            showWarningPopup = false
//                            showCheckOUTAlert = false
                        }
                    }
            }
            .padding(.horizontal)
            .offset(y: 10.0)
            
            Image(.warningPopupIcon)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 51.26, height: 46)
            
            HStack(spacing: 5){
                Text("Alert:")
                    .foregroundStyle(Color.warningTitle)
                Text(titleText)
                    .foregroundStyle(Color.text1)
            }
            .font(.custom("Montserrat", size: 12))
            .fontWeight(.medium)
            
            Text(description)
                .font(.custom("Montserrat", size: 10))
                .fontWeight(.medium)
                .foregroundStyle(Color.addressText2)
            
            //Button to dismiss
            Button{
                //TODO: to dismiss the warning popup
                withAnimation {
                    showWarningPopup = false
                }
            }label: {
                VStack {
                    Text("OK")
                        .font(.custom("Montserrat", size: 10))
                        .fontWeight(.medium)
                        .foregroundStyle(Color.white)
                }
                .frame(width: 117, height: 24)
                .background(Color.notification)
                .clipShape(RoundedRectangle(cornerRadius: 4))
                .padding()
            }
            
        }
        .frame(width: 329, height: 216, alignment: .top)
        .background(Color.white)
//        .border(Color.black)
        .clipShape(RoundedRectangle(cornerRadius: 20))
        .onTapGesture {
            withAnimation {
                showWarningPopup = true
            }
        }
        
    }
}

#Preview {
    WarningPopupView(titleText: "Battery Drain", description: "Please plug in your device to avoid shutdown.", showWarningPopup: .constant(false))
}
