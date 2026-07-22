//
//  FilterDropDownView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 25/07/24.
//

import SwiftUI

enum FilterOptions {
    case attendance
    case leaves
    case holidays
}

struct FilterDropDownView: View {

    let options: [String] = ["Attendance History", "Leaves", "Holidays"]
    
    @Binding var selection: String?
    
    var body: some View {
        Menu {
            ForEach(options, id: \.self) { option in
                Button {
                    selection = option
                } label: {
                    Text(option)
                }
            }
        } label: {
            HStack(spacing: AppSpacing.iconTextSpacing) {
                Text(selection ?? "Attendance History")
                    .font(AppFont.primary(size: AppFont.Size.body))
                    .fontWeight(AppFont.Weight.semibold)
                    .lineLimit(1)
                    .minimumScaleFactor(0.85)

                Spacer(minLength: AppSpacing.sm)

                Image(systemName: "chevron.down")
                    .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.semibold))
            }
            .padding(.horizontal, AppSpacing.md)
            .foregroundStyle(Color.white)
            .frame(maxWidth: .infinity)
            .frame(minHeight: AppLayout.buttonHeight)
            .background(ColorGradient.primaryButton)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
            .contentShape(RoundedRectangle(cornerRadius: AppRadius.small))
        }
        .menuStyle(.button)
        .buttonStyle(.plain)
        .accessibilityLabel("Attendance filter")
    }
}

#Preview {
    FilterDropDownView(selection: .constant("Selected filter"))
}
