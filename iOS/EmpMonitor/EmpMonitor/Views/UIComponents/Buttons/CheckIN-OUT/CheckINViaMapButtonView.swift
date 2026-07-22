//
//  CheckINViaMap.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/08/24.
//

import SwiftUI

struct CheckINViaMap: View {
    
    var text: String
    var action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: AppSpacing.iconTextSpacing) {
                ZStack {
                    Circle()
                        .fill(
                            LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                        )
                        .frame(width: AppLayout.checkInIconSize, height: AppLayout.checkInIconSize)
                    
                    Image(.checkINViaMapIcon)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: AppLayout.iconGlyphSmall.width, height: AppLayout.iconGlyphSmall.height)
                        .foregroundStyle(Color.white)
                }

                Text(text)
                    .font(AppFont.primary(size: AppFont.Size.callout))
                    .fontWeight(AppFont.Weight.medium)
                    .lineLimit(1)
                    .minimumScaleFactor(0.85)

                Spacer(minLength: 0)
            }
            .padding(.horizontal, AppSpacing.controlInnerPadding)
            .frame(width: AppLayout.checkInControlWidth)
            .frame(minHeight: AppLayout.checkInControlHeight)
            .background(Color.swipeBG)
            .clipShape(Capsule())
            .contentShape(Capsule())
        }
        .buttonStyle(.plain)
        .accessibilityLabel(text)
    }
}

#Preview {
    CheckINViaMap(text: "Check In via Map", action: { AppLog.debug("Clicked")})
}
