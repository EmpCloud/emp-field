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
        
        RoundedRectangle(cornerRadius: AppRadius.small)
            .fill(mode == selectedMode ? Color.primaryButton2 : Color.white)
            .frame(minWidth: 98)
            .frame(minHeight: AppLayout.minimumTouchTarget)
            .overlay(content: {
                RoundedRectangle(cornerRadius: AppRadius.small)
                    .stroke(Color.primaryButton2, lineWidth: 1)
            })
            .overlay {
                HStack(spacing: AppSpacing.iconTextSpacing) {
                    modeImage
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: AppLayout.iconMedium, height: AppLayout.iconMedium)
                    
                    Text(mode)
                        .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.medium))
                        .foregroundStyle(mode == selectedMode ? Color.white : Color.text1)
                        .lineLimit(1)
                        .minimumScaleFactor(0.85)
                }
                .padding(.horizontal, AppSpacing.sm)
            }
    }
}

#Preview {
    Mode(mode: "Bike", modeImage: Image(.carIcon), selectedMode: .constant(""))
}
