//
//  ResumeButton.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 04/09/24.
//

import SwiftUI

struct ResumeButton: View {
    
    var action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: 8) {
                Image(systemName: "play.circle.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 16, height: 16)
                    .foregroundStyle(Color.white)

                Text("Resume")
                    .font(.system(size: 12, weight: .semibold))
                    .foregroundStyle(Color.white)
            }
            .frame(height: 44)
            .frame(maxWidth: .infinity, alignment: .center)
            .background(Color.resumeButton)
            .cornerRadius(22)
        }
        .accessibilityLabel("Resume Task")
        .accessibilityHint("Double tap to resume this task")
    }
}

#Preview {
    ResumeButton() {
       print("Clicked")
    }
}
