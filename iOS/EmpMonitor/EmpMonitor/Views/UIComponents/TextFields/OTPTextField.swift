//
//  OTPTextField.swift
//  EmpMonitor
//
//  DEPRECATED: Use GenericTextField instead
//  This file is kept for build compatibility during consolidation
//

import SwiftUI

// Placeholder for backwards compatibility
struct OTPTextField: View {
    @Binding var text: String

    var body: some View {
        TextField("OTP", text: $text)
            .keyboardType(.numberPad)
            .font(.system(size: 14, weight: .regular))
            .padding(12)
            .background(Color.white)
            .cornerRadius(8)
    }
}
