//
//  WarningPopupView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 09/09/24.
//

import SwiftUI

/// Presents a warning as a native alert. It renders nothing itself, so place it where
/// the previous custom popup lived and drive it with `showWarningPopup`.
struct WarningPopupView: View {
    
    var titleText: String
    var description: String
    
    @Binding var showWarningPopup: Bool
    
    var body: some View {
        Color.clear
            .frame(width: 0, height: 0)
            .accessibilityHidden(true)
            .alert(titleText.isEmpty ? "Alert" : titleText, isPresented: $showWarningPopup) {
                Button("OK", role: .cancel) { }
            } message: {
                Text(description)
            }
    }
}

#Preview {
    WarningPopupView(titleText: "Battery Drain", description: "Please plug in your device to avoid shutdown.", showWarningPopup: .constant(false))
}
