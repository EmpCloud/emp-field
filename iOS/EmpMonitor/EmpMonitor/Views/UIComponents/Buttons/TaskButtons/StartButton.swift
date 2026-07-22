//
//  StartButton.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 04/09/24.
//

import SwiftUI

struct StartButton: View {
    
    var action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: AppSpacing.compactIconTextSpacing) {
                Image(systemName: "play.circle.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 16, height: 16)
                    .foregroundStyle(Color.white)
                Text("Start")
                    .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.semibold))
                    .foregroundStyle(Color.white)
            }
            .padding(.horizontal, AppSpacing.sm)
            .frame(height: 34)
            .frame(minWidth: 72, alignment: .center)
            .background(Color.profileDarkBg)
            .clipShape(Capsule())
        }
        .buttonStyle(.plain)
        .frame(minHeight: AppLayout.minimumTouchTarget)
        .contentShape(Rectangle())
        .accessibilityLabel("Start Task")
        .accessibilityHint("Double tap to start this task")
    }
}

#Preview {
    StartButton(){
        AppLog.debug("Clicked")
    }
}
