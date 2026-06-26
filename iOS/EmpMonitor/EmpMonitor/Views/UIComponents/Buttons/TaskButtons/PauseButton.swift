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
            HStack(spacing: 8) {
                Image(systemName: "pause.circle.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 16, height: 16)
                    .foregroundStyle(Color.white)
                Text("Pause")
                    .font(.system(size: 12, weight: .semibold))
                    .foregroundStyle(Color.white)
            }
            .frame(height: 44)
            .frame(maxWidth: .infinity, alignment: .center)
            .background(Color.absent)
            .cornerRadius(22)
        }
        .accessibilityLabel("Pause Task")
        .accessibilityHint("Double tap to pause this task")
    }
}

#Preview {
    PauseButton() {
        print("Clicked")
    }
}
