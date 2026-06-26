//
//  LocationWarningView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import SwiftUI

struct LocationWarningView: View {
    var body: some View {
        RoundedRectangle(cornerRadius: 6)
            .fill(Color.warningBG)
            .frame(width: 288, height: 55)
            .overlay {
                VStack {
                    VStack {
                        Image(systemName: "xmark")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 8.6, height: 8.57)
                            .foregroundStyle(Color.white)
                            .offset(x: -10)
                            .padding(4)
                    }
                    .frame(maxWidth: .infinity, alignment: .trailing)
              
                    Text("Enable location access for Empmonitor to track \n your daily work and enhance your experience.")
                        .font(.custom("Montserrat", size: 10))
                        .foregroundStyle(Color.white)
                        .multilineTextAlignment(.center)
                        .offset(y: -7)
                }
            }
    }
}

#Preview {
    LocationWarningView()
}
