//
//  LocationWarningView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import SwiftUI

struct LocationWarningView: View {
    var body: some View {
        Text("Enable location access for Empmonitor to track your daily work and enhance your experience.")
            .font(AppFont.primary(size: AppFont.Size.xSmall))
            .fontWeight(AppFont.Weight.medium)
            .foregroundStyle(Color.white)
            .multilineTextAlignment(.center)
            .lineLimit(3)
            .minimumScaleFactor(0.9)
            .padding(.horizontal, AppSpacing.md)
            .padding(.vertical, AppSpacing.sm)
            .frame(maxWidth: 288)
            .frame(minHeight: AppLayout.minimumTouchTarget)
            .background(Color.warningBG)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
    }
}

#Preview {
    LocationWarningView()
}
