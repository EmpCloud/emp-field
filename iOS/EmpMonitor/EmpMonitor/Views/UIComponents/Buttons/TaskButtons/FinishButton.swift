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
                .frame(width: 23, height: 23)
                .overlay {
                    Image(.completeGreenCheckIcon)
                }
        }
        
    }
}

#Preview {
    FinishButton() {
        AppLog.debug("clicked")
    }
}
