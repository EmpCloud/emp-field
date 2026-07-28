//
//  CalenderView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 13/08/24.
//

import SwiftUI

struct CalenderView: View {

    @EnvironmentObject var calendarViewModel: CalendarViewModel

    @ObservedObject var attendanceViewModel: AttendanceViewModel
    @ObservedObject var leavesViewModel: LeavesViewModel

    @Binding var viewSelection: String?

    @State private var selectedDate = Date()

    // for add leaves
    @Binding var startDate: String?
    @Binding var endDate: String?
    @Binding var setStartDate: Bool?
    @Binding var setEndDate: Bool?

    @Binding var showCalendar: Bool

    var body: some View {
        VStack(spacing: 12) {
            HStack {
                Spacer()

                Button {
                    showCalendar = false
                } label: {
                    Image(systemName: "xmark")
                        .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.semibold))
                        .foregroundStyle(Color.primaryButton1)
                        .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                        .contentShape(Rectangle())
                }
                .buttonStyle(.plain)
            }
            .padding(.horizontal, 16)
            .padding(.top, 8)

            DatePicker("", selection: $selectedDate, displayedComponents: .date)
                .datePickerStyle(.graphical)
                .labelsHidden()
                .tint(Color.primaryButton1)
                .padding(.horizontal, 20)

            Button {
                applySelectedDate()
            } label: {
                Text("OK")
                    .font(AppFont.primary(size: AppFont.Size.caption))
                    .fontWeight(AppFont.Weight.semibold)
                    .foregroundStyle(Color.white)
                    .frame(width: 58, height: 33)
                    .background(
                        RoundedRectangle(cornerRadius: 4)
                            .fill(
                                LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                            )
                    )
                    .clipShape(RoundedRectangle(cornerRadius: 4))
            }
            .buttonStyle(.plain)
            .frame(maxWidth: .infinity, alignment: .trailing)
            .padding(.trailing, 20)
        }
        .padding(.bottom, 18)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 14))
        .padding(24)
        .onAppear {
            if let selectedCalendarDate = calendarDate(from: startDate) {
                selectedDate = selectedCalendarDate
            }
        }
    }

    private func applySelectedDate() {
        let selectedDateString = calendarViewModel.fullDateString(from: selectedDate)

        if setStartDate == true {
            startDate = selectedDateString
        }

        if setEndDate == true {
            endDate = selectedDateString
        }

        switch viewSelection {
        case "Attendance History":
            Task {
                attendanceViewModel.attendanceStartDate = startDate ?? HelperFunction.shared.getFirstDateOfCurrentMonth()
                attendanceViewModel.attendanceEndDate = HelperFunction.shared.dateAfter30Days(from: attendanceViewModel.attendanceStartDate) ?? attendanceViewModel.attendanceStartDate
                await attendanceViewModel.getAttendanceData()

                withAnimation {
                    showCalendar = false
                }
            }
        case "Leaves":
            Task {
                leavesViewModel.startDate = startDate ?? HelperFunction.shared.getFirstDateOfCurrentMonth()
                leavesViewModel.endDate = HelperFunction.shared.dateAfter30Days(from: leavesViewModel.startDate) ?? leavesViewModel.startDate
                await leavesViewModel.getLeaves()

                withAnimation {
                    showCalendar = false
                }
            }
        case "Holidays":
            withAnimation {
                showCalendar = false
            }
        default:
            withAnimation {
                showCalendar = false
            }
        }
    }

    private func calendarDate(from dateString: String?) -> Date? {
        guard let dateString, !dateString.isEmpty else { return nil }

        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter.date(from: dateString)
    }
}

#Preview {
    CalenderView(attendanceViewModel: AttendanceViewModel(), leavesViewModel: LeavesViewModel(), viewSelection: .constant(""), startDate: .constant(""), endDate: .constant(""), setStartDate: .constant(false), setEndDate: .constant(false), showCalendar: .constant(false))
        .environmentObject(CalendarViewModel())
}
