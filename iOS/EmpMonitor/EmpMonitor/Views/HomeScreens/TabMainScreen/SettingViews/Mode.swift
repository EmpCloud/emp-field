//
//  SwiftUIView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 02/09/24.
//

import SwiftUI

struct Mode: View {
    
    var mode: String
    var modeImage: Image
    
    @Binding var selectedMode: String?
    
    var body: some View {
        
        RoundedRectangle(cornerRadius: 8)
            .fill(mode == selectedMode ? Color.primaryButton2 : Color.white)
            .frame(width: 98,height: 31)
            .overlay(content: {
                RoundedRectangle(cornerRadius: 8)
                    .stroke(Color.primaryButton2, lineWidth: 1)
            })
            .overlay {
                HStack {
                    modeImage
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 23, height: 23)
                    
                    Text(mode)
                        .font(.system(size: 12, weight: .medium))
                        .foregroundStyle(mode == selectedMode ? Color.white : Color.text1)
                }
            }
    }
}

#Preview {
    Mode(mode: "Bike", modeImage: Image(.carIcon), selectedMode: .constant(""))
}
