//
//  QuickAccessCardView.swift
//  EmpMonitor
//
//  Consolidated component for quick access stat cards
//  Replaces: AttendenceHistoryBoxView, HolidayBoxView, LeavesBoxView
//

import SwiftUI

struct QuickAccessCardView: View {
    let icon: Image
    let title: String
    let bgColor: Color
    let action: (() -> Void)?

    var body: some View {
        VStack(spacing: 10) {
            Circle()
                .fill(Color.white.opacity(0.3))
                .frame(width: 44, height: 44)
                .overlay {
                    icon
                        .accessibilityLabel("\(title) icon")
                }

            Text(title)
                .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.semibold))
                .foregroundStyle(.white)
                .multilineTextAlignment(.center)
        }
        .frame(width: 114, height: 114)
        .background(bgColor)
        .cornerRadius(10)
        .shadow(color: Color.black.opacity(0.1), radius: 4, x: 0, y: 1)
        .accessibilityElement(children: .combine)
        .accessibilityLabel(title)
        .onTapGesture {
            action?()
        }
    }
}

#Preview {
    QuickAccessCardView(
        icon: Image(systemName: "calendar"),
        title: "Holidays",
        bgColor: Color.blue.opacity(0.6),
        action: nil
    )
}
