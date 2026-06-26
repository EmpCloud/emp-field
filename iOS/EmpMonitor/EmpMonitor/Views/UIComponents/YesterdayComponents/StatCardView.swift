//
//  StatCardView.swift
//  EmpMonitor
//
//  Consolidated component for displaying stat cards
//  Replaces: DistanceTravelledView, HoursWorkedView, TaskCompletedView
//

import SwiftUI

struct StatCardView: View {
    let icon: Image
    let value: String
    let label: String
    let bgColor: Color
    let iconBgColor: Color

    var body: some View {
        VStack(spacing: 12) {
            Circle()
                .fill(iconBgColor)
                .frame(width: 50, height: 50)
                .overlay {
                    icon
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 24, height: 24)
                }

            Text(value)
                .font(.system(size: 20, weight: .semibold))
                .foregroundStyle(.white)

            Text(label)
                .font(.system(size: 12, weight: .regular))
                .foregroundStyle(.white.opacity(0.8))
        }
        .frame(maxWidth: .infinity)
        .frame(height: 130)
        .background(bgColor)
        .cornerRadius(12)
        .shadow(color: Color.black.opacity(0.1), radius: 4)
    }
}

#Preview {
    StatCardView(
        icon: Image(systemName: "mappin.circle.fill"),
        value: "5.2 KM",
        label: "Distance",
        bgColor: Color.blue.opacity(0.7),
        iconBgColor: Color.white.opacity(0.3)
    )
}
