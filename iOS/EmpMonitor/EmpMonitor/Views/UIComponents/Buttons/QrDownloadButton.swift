//
//  QrDownloadButton.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 13/09/24.
//

import SwiftUI

struct QrDownloadButton: View {
    
    var action: () -> Void
    
    var body: some View {
        
        Button(action: action) {
            HStack(spacing: AppSpacing.iconTextSpacing) {
                Image(.downloadBlueIcon)
                Text("Download QR")
                    .lineLimit(1)
                    .minimumScaleFactor(0.85)
            }
            .font(AppFont.primary(size: AppFont.Size.body))
            .foregroundStyle(Color.primaryButton1)
            .fontWeight(AppFont.Weight.semibold)
            .padding(.horizontal, AppSpacing.md)
            .frame(minWidth: 160)
            .frame(maxWidth: 195)
            .frame(minHeight: AppLayout.buttonHeight)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
        }
        .buttonStyle(.plain)
        .accessibilityLabel("Download QR")
    }
}

#Preview {
    QrDownloadButton() {
        AppLog.debug("Clicked")
    }
}
