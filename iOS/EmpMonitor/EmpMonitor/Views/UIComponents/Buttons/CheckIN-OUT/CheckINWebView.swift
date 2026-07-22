//
//  CheckINWebView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/08/24.
//

import SwiftUI

struct CheckINWebView: View {
    var body: some View {
        HStack(spacing: AppSpacing.iconTextSpacing) {
            ZStack {
                Circle()
                    .fill(
                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                    )
                    .frame(width: AppLayout.checkInIconCompactSize, height: AppLayout.checkInIconCompactSize)
                
                Image(.webCheckINIcon)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: AppLayout.iconGlyphSmall.width, height: AppLayout.iconGlyphSmall.height)
                    .foregroundStyle(Color.white)
                
            }

            Text("Check In via Web")
                .font(AppFont.primary(size: AppFont.Size.callout))
                .foregroundStyle(Color.primaryButton1)
                .fontWeight(AppFont.Weight.medium)
                .lineLimit(1)
                .minimumScaleFactor(0.85)

            Spacer(minLength: 0)
        }
        .padding(.horizontal, AppSpacing.controlInnerPadding)
        .frame(width: AppLayout.checkInControlWidth)
        .frame(minHeight: AppLayout.checkInControlHeight)
        .background(Color.white)
        .overlay {
            Capsule().stroke(Color.primaryButton1, lineWidth: 1)
        }
        .clipShape(Capsule())
        .accessibilityLabel("Check in via web")
    }
}

#Preview {
    CheckINWebView()
}
