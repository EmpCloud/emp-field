//
//  PrimaryButton.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI


struct PrimaryButton: View {

    var text: String
    var action: () -> Void

    var body: some View {
        ZStack {
            Button(action: action) {
                Text(text)
                    .font(AppFont.primary(size: AppFont.Size.title3, weight: AppFont.Weight.semibold))
                    .foregroundStyle(.white)
                    .padding(AppSpacing.controlInnerPadding)
                    .frame(maxWidth: .infinity)
                    .frame(height: AppLayout.buttonHeight)
                    .background(ColorGradient.primaryButton)
                    .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
                }
                .accessibilityLabel(text)
        }
    }
}

struct PrimaryThinButton: View {

    var text: String
    var action: () -> Void

    var body: some View {
        ZStack {
            Button(action: action) {
                Text(text)
                    .font(AppFont.primary(size: AppFont.Size.headline, weight: AppFont.Weight.semibold))
                    .foregroundStyle(.white)
                    .padding(AppSpacing.controlInnerPadding)
                    .frame(maxWidth: .infinity)
                    .frame(height: AppLayout.buttonHeight)
                    .background(ColorGradient.primaryButton)
                    .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
                }
                .accessibilityLabel(text)
        }
    }
}

struct RedThinButton: View {
    
    var text: String
    var action: () -> Void
    
    var body: some View {
        ZStack {
            Button(action: action) {
                HStack {
                    if text == "Resume" {
                        Image(systemName: "play.fill")
                            .foregroundStyle(Color.white)
                    }
                    Text(text)
                        .font(AppFont.primary(size: AppFont.Size.subheadline))
                        .foregroundStyle(.white)
                        .lineLimit(1)
                        .minimumScaleFactor(0.85)
                    
                }
                .padding(AppSpacing.controlInnerPadding)
                .frame(maxWidth: .infinity)
                .frame(minHeight: AppLayout.buttonHeight)
                .background(Color.absent)
                .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
            }
        }
    }
}

#Preview {
    PrimaryButton(text: "Primary Button"){
        AppLog.debug("Click me")
    }
}

#Preview {
    PrimaryThinButton(text: "Primary Button"){
        AppLog.debug("Click me")
    }
}

#Preview {
    RedThinButton(text: "Primary Button"){
        AppLog.debug("Click me")
    }
}
