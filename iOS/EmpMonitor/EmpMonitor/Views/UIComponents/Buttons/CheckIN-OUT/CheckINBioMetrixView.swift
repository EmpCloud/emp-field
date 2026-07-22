//
//  CheckINBioMetrixView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/08/24.
//

import SwiftUI

extension CheckINBioMetrixView {
    func onTap(_ action: @escaping () -> Void) -> Self {
        var copy = self
        copy.tapAction = action
        return copy
    }
}

struct CheckINBioMetrixView: View {
    var tapAction: (() -> Void)?

    var body: some View {
        Button {
            tapAction?()
        } label: {
            HStack(spacing: AppSpacing.iconTextSpacing) {
                ZStack {
                    Circle()
                        .fill(
                            LinearGradient(
                                gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]),
                                startPoint: .top,
                                endPoint: .bottom
                            )
                        )
                        .frame(width: AppLayout.checkInIconCompactSize, height: AppLayout.checkInIconCompactSize)

                    Image(.bioCheckINIcon)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: AppLayout.iconGlyphSmall.width, height: AppLayout.iconGlyphSmall.height)
                        .foregroundStyle(Color.white)
                }

                Text("Check In via Biometric")
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
            .contentShape(Capsule())
        }
        .buttonStyle(.plain)
        .accessibilityLabel("Check in via biometric")
    }
}

#Preview {
    CheckINBioMetrixView()
}
