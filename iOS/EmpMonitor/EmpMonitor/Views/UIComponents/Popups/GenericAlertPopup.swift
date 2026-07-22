//
//  GenericAlertPopup.swift
//  EmpMonitor
//
//  Unified alert popup component
//  Replaces: WarningPopupView, LogoutAlertPopupView
//

import SwiftUI

struct AlertButton {
    let label: String
    let style: AlertButtonStyle
    let action: () -> Void
}

enum AlertButtonStyle {
    case primary, secondary, destructive
}

struct GenericAlertPopup: View {
    let title: String
    let message: String
    let buttons: [AlertButton]
    let icon: Image?
    let onDismiss: () -> Void

    var body: some View {
        ZStack {
            Color.black.opacity(0.4)
                .ignoresSafeArea()

            VStack(spacing: 16) {
                if let icon = icon {
                    icon
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 60, height: 60)
                }

                Text(title)
                    .font(AppFont.primary(size: AppFont.Size.title3, weight: AppFont.Weight.semibold))
                    .foregroundStyle(.black)

                Text(message)
                    .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                    .foregroundStyle(.gray)
                    .multilineTextAlignment(.center)

                VStack(spacing: 12) {
                    ForEach(0..<buttons.count, id: \.self) { index in
                        Button(action: buttons[index].action) {
                            Text(buttons[index].label)
                                .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.semibold))
                                .frame(maxWidth: .infinity)
                                .frame(height: 44)
                                .background(buttonBackground(for: buttons[index].style))
                                .foregroundStyle(buttonForeground(for: buttons[index].style))
                                .cornerRadius(8)
                        }
                    }
                }
            }
            .padding(24)
            .background(Color.white)
            .cornerRadius(16)
            .padding(16)
        }
        .onTapGesture {
            onDismiss()
        }
        .accessibilityElement(children: .contain)
    }

    private func buttonBackground(for style: AlertButtonStyle) -> Color {
        switch style {
        case .primary: return .blue
        case .secondary: return Color.gray.opacity(0.2)
        case .destructive: return .red
        }
    }

    private func buttonForeground(for style: AlertButtonStyle) -> Color {
        switch style {
        case .primary, .destructive: return .white
        case .secondary: return .black
        }
    }
}

#Preview {
    ZStack {
        Color.black.opacity(0.4)
            .ignoresSafeArea()

        GenericAlertPopup(
            title: "Confirm Action",
            message: "Are you sure you want to proceed?",
            buttons: [
                AlertButton(label: "Cancel", style: .secondary, action: {}),
                AlertButton(label: "Confirm", style: .primary, action: {})
            ],
            icon: Image(systemName: "checkmark.circle"),
            onDismiss: {}
        )
    }
}
