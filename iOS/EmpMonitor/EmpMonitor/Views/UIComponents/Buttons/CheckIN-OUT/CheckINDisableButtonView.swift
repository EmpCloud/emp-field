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
        Button(action: action) {
            HStack(spacing: AppSpacing.iconTextSpacing) {
                ZStack {
                    Circle()
                        .fill(
                            LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                        )
                        .frame(width: AppLayout.checkInIconSize, height: AppLayout.checkInIconSize)
                    
                    Image(systemName: "arrow.right")
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

struct CheckOUTDisableButtonView: View {
    
    var text: String
    var action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: AppSpacing.iconTextSpacing) {
                Spacer(minLength: 0)

                Text(text)
                    .font(AppFont.primary(size: AppFont.Size.callout))
                    .fontWeight(AppFont.Weight.medium)
                    .lineLimit(1)
                    .minimumScaleFactor(0.85)

                ZStack {
                    Circle()
                        .fill(
                            LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                        )
                        .frame(width: AppLayout.checkInIconSize, height: AppLayout.checkInIconSize)
                    
                    Image(systemName: "arrow.left")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: AppLayout.iconGlyphSmall.width, height: AppLayout.iconGlyphSmall.height)
                        .foregroundStyle(Color.white)
                }
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
    CheckINDisableButtonView(text: "Swipe to Check In"){
        AppLog.debug("clicked")
    }
}

#Preview {
    CheckOUTDisableButtonView(text: "Swipe to Check Out"){
        AppLog.debug("clicked")
    }
}
