//
//  PauseButton.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 04/09/24.
//

import SwiftUI

struct PauseButton: View {
    
    var action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: AppSpacing.compactIconTextSpacing) {
                Image(systemName: "pause.circle.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 16, height: 16)
                    .foregroundStyle(Color.white)
                Text("Pause")
                    .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.semibold))
                    .foregroundStyle(Color.white)
            }
            .padding(.horizontal, AppSpacing.sm)
            .frame(height: 34)
            .frame(minWidth: 72, alignment: .center)
            .background(Color.absent)
            .clipShape(Capsule())
        }
        .buttonStyle(.plain)
        .frame(minHeight: AppLayout.minimumTouchTarget)
        .contentShape(Rectangle())
        .accessibilityLabel("Pause Task")
        .accessibilityHint("Double tap to pause this task")
    }
}

#Preview {
    PauseButton() {
        AppLog.debug("Clicked")
    }
}
