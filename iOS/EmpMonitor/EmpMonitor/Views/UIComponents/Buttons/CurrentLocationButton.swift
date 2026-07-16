//
//  CurrentLocationButton.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 24/06/24.
//

import SwiftUI

struct CurrentLocationButton: View {
    var text: String
    var action: () -> Void
    var body: some View {
        ZStack {
            Button(action: action){
                HStack {
                    Text(text)
                        .font(.custom("Poppins-Regular", size: 10))
                        .foregroundStyle(.white)
                        .padding()
                        .frame(height: 27)
                        .frame(maxWidth: .infinity)
                        .background(ColorGradient.primaryButton)
                        .clipShape(RoundedRectangle(cornerRadius: 6))
                }
                .overlay(alignment: .leading) {
                    Image(.currentLocationIcon)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 14, height: 14)
                        .padding(.leading)
                }
            }
        }
    }
}

#Preview {
    CurrentLocationButton(text: "Use Current Location"){
        AppLog.debug("click me")
    }
}
