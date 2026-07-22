//
//  GenericButton.swift
//  EmpMonitor
//
//  Unified button component supporting multiple variants
//  Consolidates: PrimaryButton, PrimaryThinButton, RedThinButton, EditButton, StartButton, PauseButton, ResumeButton, FinishButton
//

import SwiftUI

enum GenericButtonStyle {
    case primary
    case primaryThin
    case red
    case secondary
    case outline
}

struct GenericButton: View {
    let text: String
    let style: GenericButtonStyle
    let action: () -> Void
    var icon: String? = nil
    var isDisabled: Bool = false

    private var backgroundColor: LinearGradient {
        switch style {
        case .primary:
            return LinearGradient(
                gradient: Gradient(colors: [Color.blue, Color.blue.opacity(0.8)]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        case .primaryThin:
            return LinearGradient(
                gradient: Gradient(colors: [Color.blue.opacity(0.7), Color.blue.opacity(0.5)]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        case .red:
            return LinearGradient(
                gradient: Gradient(colors: [Color.red, Color.red.opacity(0.8)]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        case .secondary:
            return LinearGradient(
                gradient: Gradient(colors: [Color.gray, Color.gray.opacity(0.8)]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        case .outline:
            return LinearGradient(
                gradient: Gradient(colors: [Color.white, Color.white.opacity(0.95)]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        }
    }

    private var textColor: Color {
        style == .outline ? .blue : .white
    }

    private var height: CGFloat {
        switch style {
        case .primaryThin:
            return AppLayout.buttonHeight
        default:
            return AppLayout.buttonHeight
        }
    }

    var body: some View {
        Button(action: action) {
            HStack(spacing: AppSpacing.iconTextSpacing) {
                if let icon = icon {
                    Image(systemName: icon)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: AppLayout.iconSmall, height: AppLayout.iconSmall)
                }
                Text(text)
                    .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.semibold))
            }
            .foregroundStyle(textColor)
            .frame(maxWidth: .infinity)
            .frame(height: height)
            .background(backgroundColor)
            .cornerRadius(AppRadius.small)
            .overlay(
                style == .outline ?
                RoundedRectangle(cornerRadius: AppRadius.small).stroke(Color.blue, lineWidth: 1) :
                nil
            )
        }
        .disabled(isDisabled)
        .opacity(isDisabled ? 0.5 : 1.0)
        .accessibilityLabel(text)
        .accessibility(addTraits: .isButton)
    }
}

#Preview {
    VStack(spacing: AppSpacing.stackSpacingDefault) {
        GenericButton(text: "Primary Button", style: .primary, action: {})
        GenericButton(text: "Thin Button", style: .primaryThin, action: {})
        GenericButton(text: "Delete", style: .red, action: {}, icon: "trash")
        GenericButton(text: "Outline", style: .outline, action: {})
    }
    .padding(AppSpacing.pagePadding)
}
