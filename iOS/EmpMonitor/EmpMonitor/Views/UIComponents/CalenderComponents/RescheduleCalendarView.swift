//
//  RescheduleCalendarView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 12/09/24.
//

import SwiftUI

struct RescheduleCalendarView: View {
    
    @EnvironmentObject var calendarViewModel: CalendarViewModel
    
    @ObservedObject var dateViewModel: DateViewModel
    
    @State private var pickedDate = Date()
    
    // for rescheduling
    @Binding var startDate: String?
    @Binding var endDate: String?
    @Binding var setStartDate: Bool?
    @Binding var setEndDate: Bool?
    
    @Binding var rescheduleStartTime: String?
    @Binding var rescheduleStopTime: String?
    
    @State private var calendarStartTime: String?
    @State private var calendarStopTime: String?
    
    //time picker
    @Binding var startTime: String
    @Binding var stopTime: String
    
    @Binding var showCalendar: Bool
//    @Binding var showTimePicker: Bool
    
    var body: some View {
        VStack(spacing: 0) {
            HStack {
                Spacer()
                Button {
                    withAnimation {
                        showCalendar = false
                        setStartDate = false
                    }
                } label: {
                    Image(systemName: "xmark")
                        .foregroundStyle(Color.primaryButton1)
                        .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                }
                .buttonStyle(.plain)
                .accessibilityLabel("Close calendar")
            }
            .padding(.horizontal, AppSpacing.sm)

            DatePicker("Date", selection: $pickedDate, displayedComponents: .date)
                .datePickerStyle(.graphical)
                .labelsHidden()
                .tint(Color.primaryButton1)
                .padding(.horizontal, AppSpacing.md)

            HStack(spacing: AppSpacing.md) {
                timeField(title: "Start Time", value: calendarStartTime) {
                    dateViewModel.setStopTime = false
                    dateViewModel.setStartTime = true
                }
                timeField(title: "Stop Time", value: calendarStopTime) {
                    dateViewModel.setStopTime = true
                    dateViewModel.setStartTime = false
                }
            }
            .padding(.horizontal, AppSpacing.md)
            .padding(.top, AppSpacing.sm)

            HStack {
                Spacer()
                Button("OK") {
                    let selectedDateString = calendarViewModel.fullDateString(from: pickedDate)
                    if setStartDate == true {
                        startDate = selectedDateString
                    }
                    if setEndDate == true {
                        endDate = selectedDateString
                    }
                    withAnimation {
                        rescheduleStartTime = calendarStartTime
                        rescheduleStopTime = calendarStopTime
                        showCalendar = false
                    }
                }
                .font(AppFont.primary(size: AppFont.Size.subheadline, weight: AppFont.Weight.semibold))
                .tint(Color.primaryButton1)
                .frame(minWidth: AppLayout.minimumTouchTarget, minHeight: AppLayout.minimumTouchTarget)
                .disableWithOpacity(calendarStartTime == nil || calendarStopTime == nil)
            }
            .padding(.horizontal, AppSpacing.md)
            .padding(.vertical, AppSpacing.sm)
        }
        .frame(maxWidth: 380)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: AppRadius.large))
        .padding(AppSpacing.lg)
        .onAppear {
            if let startDate, let date = Self.dateFormatter.date(from: startDate) {
                pickedDate = date
            }
        }
        .onChange(of: startTime){ _, newTime in
            calendarStartTime = "\(newTime)"
        }
        .onChange(of: stopTime){ _, newTime in
            calendarStopTime = "\(newTime)"
        }
    }

    private func timeField(title: String, value: String?, select: @escaping () -> Void) -> some View {
        VStack(spacing: AppSpacing.xs) {
            Text(title)
                .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.bold))
                .foregroundStyle(Color.subText)

            Button {
                withAnimation {
                    select()
                    dateViewModel.showPicker = true
                }
            } label: {
                HStack {
                    Text(value ?? "--:--")
                        .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.bold))
                        .foregroundStyle(Color.subText)
                    Spacer(minLength: AppSpacing.xs)
                    Image(systemName: "clock")
                        .foregroundStyle(Color.primaryButton1)
                }
                .padding(.horizontal, AppSpacing.md)
                .frame(maxWidth: .infinity, minHeight: 46)
                .background(Color.scheduleBG)
                .clipShape(RoundedRectangle(cornerRadius: 6))
            }
            .buttonStyle(.plain)
            .accessibilityLabel(title)
            .accessibilityValue(value ?? "Not set")
        }
        .frame(maxWidth: .infinity)
    }

    private static let dateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter
    }()
}

#Preview {
    RescheduleCalendarView(dateViewModel: DateViewModel(), startDate: .constant(""), endDate: .constant(""), setStartDate: .constant(false), setEndDate: .constant(false),rescheduleStartTime: .constant(""), rescheduleStopTime: .constant(""), startTime: .constant(""), stopTime: .constant(""), showCalendar: .constant(false))
        .environmentObject(CalendarViewModel())
}
