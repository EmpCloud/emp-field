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
        Button(action: action) {
            ZStack(alignment: .leading) {
                Image(.currentLocationIcon)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: AppLayout.iconExtraSmall, height: AppLayout.iconExtraSmall)
                    .padding(.leading, AppSpacing.md)

                Text(text)
                    .font(AppFont.primary(size: AppFont.Size.xSmall))
                    .lineLimit(1)
                    .minimumScaleFactor(0.85)
                    .frame(maxWidth: .infinity)
                    .padding(.horizontal, AppSpacing.lg)
            }
            .foregroundStyle(.white)
            .frame(maxWidth: .infinity)
            .frame(minHeight: AppLayout.minimumTouchTarget)
            .background(ColorGradient.primaryButton)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
        }
        .buttonStyle(.plain)
        .accessibilityLabel(text)
    }
}

#Preview {
    CurrentLocationButton(text: "Use Current Location"){
        AppLog.debug("click me")
    }
}
