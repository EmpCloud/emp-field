//
//  NetworkToastView.swift
//  EmpMonitor
//

import SwiftUI

// MARK: - Toast Style

enum ToastStyle: Equatable {
    case success
    case error
    case warning
    case info

    var icon: String {
        switch self {
        case .success: return "checkmark.circle.fill"
        case .error:   return "xmark.circle.fill"
        case .warning: return "exclamationmark.triangle.fill"
        case .info:    return "info.circle.fill"
        }
    }

    var tintColor: Color {
        switch self {
        case .success: return Color(red: 0.22, green: 0.78, blue: 0.45)
        case .error:   return Color(red: 0.93, green: 0.29, blue: 0.29)
        case .warning: return Color(red: 1.0,  green: 0.65, blue: 0.0)
        case .info:    return Color(red: 0.27, green: 0.62, blue: 0.95)
        }
    }
}

// MARK: - Toast Message

struct ToastMessage: Equatable {
    var style: ToastStyle
    var message: String
    var duration: Double = 3.0
}

// MARK: - Toast View

struct ToastView: View {
    let toast: ToastMessage

    var body: some View {
        HStack(spacing: 10) {
            Image(systemName: toast.style.icon)
                .foregroundStyle(toast.style.tintColor)
                .font(.system(size: 18, weight: .semibold))
            Text(toast.message)
                .font(.system(size: 13, weight: .medium))
                .foregroundStyle(.white)
                .multilineTextAlignment(.leading)
                .lineLimit(3)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(Color(red: 0.13, green: 0.13, blue: 0.13))
        .clipShape(Capsule())
        .shadow(color: .black.opacity(0.25), radius: 6, y: 3)
        .padding(.horizontal, 20)
    }
}

// MARK: - Toast ViewModifier

struct ToastModifier: ViewModifier {
    @Binding var toast: ToastMessage?
    @State private var workItem: DispatchWorkItem?

    func body(content: Content) -> some View {
        content
            .overlay(alignment: .bottom) {
                if let toast {
                    ToastView(toast: toast)
                        .padding(.bottom, 90)
                        .transition(.move(edge: .bottom).combined(with: .opacity))
                        .onAppear { scheduleToastDismissal() }
                }
            }
            .animation(.spring(response: 0.4, dampingFraction: 0.75), value: toast)
    }

    private func scheduleToastDismissal() {
        workItem?.cancel()
        let task = DispatchWorkItem { toast = nil }
        workItem = task
        DispatchQueue.main.asyncAfter(deadline: .now() + (toast?.duration ?? 3.0), execute: task)
    }
}

extension View {
    func toast(message: Binding<ToastMessage?>) -> some View {
        modifier(ToastModifier(toast: message))
    }
}

// MARK: - Legacy alias (unused, kept for build compatibility)

typealias NetworkToastView = ToastView

#Preview {
    VStack(spacing: 20) {
        ToastView(toast: ToastMessage(style: .success, message: "Checked in successfully"))
        ToastView(toast: ToastMessage(style: .error, message: "Failed to check in. Please try again."))
        ToastView(toast: ToastMessage(style: .warning, message: "Auto check-out triggered"))
        ToastView(toast: ToastMessage(style: .info, message: "Syncing location data…"))
    }
    .padding()
}
