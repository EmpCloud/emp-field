//
//  LocationSettingWarningView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 01/10/24.
//

import SwiftUI

struct LocationSettingWarningView: View {
    
    var titleText: String
    var description: String
    
    @Binding var showWarningPopup: Bool
    
    var body: some View {
        VStack(spacing: 10) {
            HStack {
                Spacer()
//                Image(systemName: "xmark")
//                    .resizable()
//                    .aspectRatio(contentMode: .fit)
//                    .frame(width: 10.75, height: 10.75)
//                    .fontWeight(.bold)
//                    .foregroundStyle(Color.primaryButton1)
//                    .padding(10)
//                    .onTapGesture {
//                        withAnimation {
//                            showWarningPopup = false
//                        }
//                    }
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
                .multilineTextAlignment(.center)
                .padding(.horizontal)
            
            PrimaryBorderButton(text: "Settings") {
                //open settings
                HelperFunction.shared.openAppSetting()
            }
            .padding()
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
    LocationSettingWarningView(titleText: "", description: "", showWarningPopup: .constant(false))
}
