//
//  GenericTextField.swift
//  EmpMonitor
//
//  Unified text input component
//

import SwiftUI

enum TextFieldStyle {
    case standard, popup, auth, minimal
}

struct GenericTextField: View {
    @Binding var text: String
    let placeholder: String
    let style: TextFieldStyle
    let isSecure: Bool = false
    let isEditable: Bool = true

    @State private var showPassword: Bool = false

    private var bgColor: Color {
        style == .popup ? Color(red: 241/255, green: 247/255, blue: 255/255) : .white
    }

    private var height: CGFloat {
        switch style {
        case .popup: return 43
        case .auth, .standard: return 46
        case .minimal: return 40
        }
    }

    var body: some View {
        HStack(spacing: 8) {
            if isSecure && !showPassword {
                SecureField(placeholder, text: $text)
                    .disabled(!isEditable)
                    .font(.system(size: 14, weight: .regular))
            } else {
                TextField(placeholder, text: $text)
                    .disabled(!isEditable)
                    .font(.system(size: 14, weight: .regular))
            }

            if isSecure {
                Button(action: { showPassword.toggle() }) {
                    Image(systemName: showPassword ? "eye.slash" : "eye")
                        .foregroundStyle(.gray)
                }
            }
        }
        .padding(.horizontal, 12)
        .frame(height: height)
        .background(bgColor)
        .cornerRadius(8)
        .overlay(RoundedRectangle(cornerRadius: 8).stroke(Color.gray.opacity(0.3), lineWidth: 1))
        .accessibilityLabel(placeholder)
    }
}

#Preview {
    VStack(spacing: 16) {
        GenericTextField(text: .constant(""), placeholder: "Standard", style: .standard)
        GenericTextField(text: .constant(""), placeholder: "Popup", style: .popup)
    }
    .padding()
}
