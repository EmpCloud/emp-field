//
//  CompleteButton.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 05/09/24.
//

import SwiftUI

struct FinishButton: View {
    
    var action: () -> Void
    
    var body: some View {
        Button(action: action) {
            Circle()
                .fill(Color.present.opacity(0.2))
                .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                .overlay {
                    Image(.completeGreenCheckIcon)
                }
        }
        .buttonStyle(.plain)
        .accessibilityLabel("Finish task")
        
    }
}

#Preview {
    FinishButton() {
        AppLog.debug("clicked")
    }
}
