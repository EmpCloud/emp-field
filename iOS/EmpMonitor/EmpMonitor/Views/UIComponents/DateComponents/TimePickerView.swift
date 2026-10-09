//
//  TimePickerView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct TimePickerView: View {
    @ObservedObject var dateViewModel: DateViewModel

    @Binding var startTime: String
    @Binding var stopTime: String

    var body: some View {
        VStack(spacing: AppSpacing.sm) {
            Text(dateViewModel.setStopTime ? "Select end time" : "Select start time")
                .font(AppFont.primary(size: AppFont.Size.subheadline, weight: AppFont.Weight.semibold))
                .foregroundStyle(Color.attendanceTitleText)
                .padding(.top, AppSpacing.md)

            DatePicker("Time", selection: $dateViewModel.selectedDate, displayedComponents: .hourAndMinute)
                .datePickerStyle(.wheel)
                .labelsHidden()

            HStack {
                Button("Cancel") {
                    dateViewModel.showPicker = false
                }
                .frame(minWidth: AppLayout.minimumTouchTarget, minHeight: AppLayout.minimumTouchTarget)

                Spacer()

                Button("Save") {
                    if dateViewModel.setStartTime {
                        startTime = dateViewModel.getFormattedDate()
                    } else if dateViewModel.setStopTime {
                        stopTime = dateViewModel.getFormattedDate()
                    }
                    dateViewModel.showPicker = false
                }
                .fontWeight(AppFont.Weight.bold)
                .frame(minWidth: AppLayout.minimumTouchTarget, minHeight: AppLayout.minimumTouchTarget)
            }
            .font(AppFont.primary(size: AppFont.Size.subheadline))
            .tint(Color.primaryButton1)
            .padding(.horizontal, AppSpacing.md)
            .padding(.bottom, AppSpacing.sm)
        }
        .frame(maxWidth: 320)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: AppRadius.large))
        .padding(AppSpacing.lg)
    }
}



#Preview {
    TimePickerView(dateViewModel: DateViewModel(), startTime: .constant(""), stopTime: .constant(""))
}
