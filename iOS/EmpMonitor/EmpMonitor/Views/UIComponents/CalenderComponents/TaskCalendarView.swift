//
//  TaskCalendarView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct TaskCalendarView: View {
    @EnvironmentObject var calendarViewModel: CalendarViewModel

    @State private var selectedDate = Date()

    @Binding var startDate: String?
    @Binding var endDate: String?
    @Binding var setStartDate: Bool?
    @Binding var setEndDate: Bool?

    @Binding var showCalendar: Bool

    var body: some View {
        VStack(spacing: 0) {
            HStack {
                Spacer()
                Button {
                    showCalendar = false
                } label: {
                    Image(systemName: "xmark")
                        .foregroundStyle(Color.primaryButton1)
                        .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                }
                .buttonStyle(.plain)
                .accessibilityLabel("Close calendar")
            }
            .padding(.horizontal, AppSpacing.sm)

            DatePicker("Date", selection: $selectedDate, displayedComponents: .date)
                .datePickerStyle(.graphical)
                .labelsHidden()
                .tint(Color.primaryButton1)
                .padding(.horizontal, AppSpacing.md)

            HStack {
                Spacer()
                Button("OK") {
                    let selectedDateString = calendarViewModel.fullDateString(from: selectedDate)
                    if setStartDate == true {
                        startDate = selectedDateString
                    }
                    if setEndDate == true {
                        endDate = selectedDateString
                    }
                    withAnimation {
                        showCalendar = false
                    }
                }
                .font(AppFont.primary(size: AppFont.Size.subheadline, weight: AppFont.Weight.semibold))
                .tint(Color.primaryButton1)
                .frame(minWidth: AppLayout.minimumTouchTarget, minHeight: AppLayout.minimumTouchTarget)
            }
            .padding(.horizontal, AppSpacing.md)
            .padding(.bottom, AppSpacing.sm)
        }
        .frame(maxWidth: 380)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: AppRadius.large))
        .padding(AppSpacing.lg)
        .onAppear {
            if let startDate, let date = Self.dateFormatter.date(from: startDate) {
                selectedDate = date
            }
        }
    }

    private static let dateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter
    }()
}

#Preview {
    TaskCalendarView(startDate: .constant(""), endDate: .constant(""), setStartDate: .constant(false), setEndDate: .constant(false), showCalendar: .constant(false))
        .environmentObject(CalendarViewModel())
}
