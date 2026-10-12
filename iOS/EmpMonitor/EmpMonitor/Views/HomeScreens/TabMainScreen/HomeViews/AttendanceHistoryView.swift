//
//  AttendanceHistoryView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 26/07/24.
//

import SwiftUI

struct AttendanceHistoryView: View {
    
    @ObservedObject var attendanceViewModel: AttendanceViewModel
    
    @ObservedObject var editAttendanceViewModel: EditAttendanceViewModel
   
    @Binding var showEditAttendance: Bool
    
    var body: some View {
        ScrollView {
            if attendanceViewModel.isLoading {
                loadingPlaceholder
            } else if attendanceViewModel.attendanceData.isEmpty {
                emptyPlaceholder
            } else {
                // System grid: columns size themselves to their content and share any extra
                // width, so the table adapts to iPhone, iPad, rotation and split view.
                Grid(alignment: .leading, horizontalSpacing: AppSpacing.sm, verticalSpacing: 0) {
                    GridRow {
                        Text("Date")
                            .frame(maxWidth: .infinity, alignment: .leading)
                        Text("Status")
                            .frame(maxWidth: .infinity, alignment: .leading)
                        Text("Clocked-in")
                            .frame(maxWidth: .infinity)
                        Text("Clocked-out")
                            .frame(maxWidth: .infinity)
                        Color.clear
                            .gridCellUnsizedAxes([.horizontal, .vertical])
                    }
                    .font(AppFont.primary(size: AppFont.Size.caption))
                    .foregroundStyle(Color.attendanceTitleText)
                    .lineLimit(1)
                    .minimumScaleFactor(0.8)
                    .padding(.vertical, 10)

                    ForEach(attendanceViewModel.attendanceData, id: \.date) { attendance in
                        GridRow {
                            Text(FormatterHelper.shared.formattedDate(from: attendance.date))
                                .frame(maxWidth: .infinity, alignment: .leading)

                            Text("\(checkAttendanceStatus(attendance: attendance))")
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .foregroundStyle(applyColor(status: checkAttendanceStatus(attendance: attendance)))

                            if let clockedIN = FormatterHelper.shared.checkTimeFormatter(from: attendance.startTime ?? "") {
                                Text(clockedIN)
                                    .frame(maxWidth: .infinity)
                                    .foregroundStyle(Color.present)
                            } else {
                                Text("--:--")
                                    .frame(maxWidth: .infinity)
                            }

                            if let clockedOUT = FormatterHelper.shared.checkTimeFormatter(from: attendance.endTime ?? "") {
                                Text(clockedOUT)
                                    .frame(maxWidth: .infinity)
                                    .foregroundStyle(Color.absent)
                            } else {
                                Text("--:--")
                                    .frame(maxWidth: .infinity)
                            }

                            Button {
                                withAnimation {
                                    editAttendanceViewModel.date = attendance.date
                                    showEditAttendance.toggle()
                                }
                            } label: {
                                Image(.editAttendanceIcon)
                                    .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                                    .contentShape(Rectangle())
                            }
                            .buttonStyle(.plain)
                            .accessibilityLabel("Edit attendance")
                        }
                        .font(AppFont.primary(size: AppFont.Size.footnote))
                        .fontWeight(AppFont.Weight.semibold)
                        .lineLimit(2)
                        .minimumScaleFactor(0.8)

                        Divider()
                    }
                }
                .padding(.horizontal, AppSpacing.md)
                .background(Color.white)
                .clipShape(RoundedRectangle(cornerRadius: 20))
                .padding(.horizontal, 7)
            }
        }
        .padding(.top, 10)
        .onAppear {
            Task {
                attendanceViewModel.attendanceStartDate = HelperFunction.shared.getFirstDateOfCurrentMonth()
                attendanceViewModel.attendanceEndDate = HelperFunction.shared.dateAfter30Days(from: attendanceViewModel.attendanceStartDate) ?? attendanceViewModel.attendanceStartDate
                await attendanceViewModel.getAttendanceData()
                
                
            }
        }
        .refreshable {
            Task {
                attendanceViewModel.attendanceStartDate = HelperFunction.shared.getFirstDateOfCurrentMonth()
                attendanceViewModel.attendanceEndDate = HelperFunction.shared.dateAfter30Days(from: attendanceViewModel.attendanceStartDate) ?? attendanceViewModel.attendanceStartDate
                await attendanceViewModel.getAttendanceData()
                
                
            }
        }
    }
    
    private var loadingPlaceholder: some View {
        VStack(spacing: 12) {
            ProgressView()
                .scaleEffect(1.2)
            Text("Loading...")
                .font(AppFont.primary(size: AppFont.Size.body))
                .foregroundStyle(Color.subText)
        }
        .frame(maxWidth: .infinity, minHeight: 400)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 20))
        .padding(.horizontal, 7)
    }
    
    private var emptyPlaceholder: some View {
        VStack(spacing: 12) {
            Image(.noDataFound)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 114, height: 114)
            Text("No attendance records")
                .font(AppFont.primary(size: AppFont.Size.body))
        }
        .frame(maxWidth: .infinity, minHeight: 400)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 20))
        .padding(.horizontal, 7)
    }
    
    private func checkAttendanceStatus(attendance: Attendance) -> String {
        let hasStart = !(attendance.startTime ?? "").isEmpty
        let hasEnd = !(attendance.endTime ?? "").isEmpty

        if attendance.dayOff == false && !hasStart && !hasEnd {
            return "Week Off"
        }

        if !hasStart && !hasEnd {
            if attendance.holidayStatus == 1, let holidayName = attendance.holidayName, !holidayName.isEmpty {
                return holidayName
            }
            if attendance.leaveType != 0, let leaveName = attendance.leaveName, !leaveName.isEmpty {
                return leaveName
            }
            return isOnOrBeforeToday(date: attendance.date) ? "Absent" : "-- --"
        }

        if hasStart && hasEnd {
            if attendance.holidayStatus == 1, let holidayName = attendance.holidayName, !holidayName.isEmpty {
                return workedDurationStatus(attendance, includeHalfDayLeave: false)
            }
            if attendance.openAttendanceRequest?.requestStatus == "3" {
                return "Pending"
            }
            if attendance.openAttendanceRequest?.requestStatus == "1", attendance.halfDayLeave != nil {
                return halfDayLabel(attendance)
            }
            return workedDurationStatus(attendance)
        }

        if hasStart && !hasEnd {
            if attendance.openRequest == nil && attendance.leaveType == 0 {
                return attendance.halfDayLeave == nil ? "Absent" : halfDayLabel(attendance)
            }
            return checkForPastAttendance(date: attendance.date) ? halfDayLabel(attendance) : "Absent"
        }

        // Android treats an attendance record with only a checkout as a half day.
        return halfDayLabel(attendance)
    }

    private func workedDurationStatus(_ attendance: Attendance, includeHalfDayLeave: Bool = true) -> String {
        let minimum = attendance.minHours.first?.manualHours ?? 28800
        let reportedDuration = max(attendance.loggedDuration ?? 0, 0)
        let measuredDuration = elapsedAttendanceDuration(attendance) ?? 0
        let duration = max(reportedDuration, measuredDuration)
        if duration >= minimum {
            return "Present"
        }
        if duration >= minimum / 2 {
            return includeHalfDayLeave && attendance.halfDayLeave != nil ? halfDayLabel(attendance) : "Half Day Present"
        }
        return "Absent"
    }

    private func elapsedAttendanceDuration(_ attendance: Attendance) -> Int? {
        guard let start = attendance.startTime, let end = attendance.endTime,
              let startDate = attendanceDate(start, on: attendance.date),
              let endDate = attendanceDate(end, on: attendance.date) else {
            return nil
        }
        let seconds = endDate.timeIntervalSince(startDate)
        return seconds > 0 ? Int(seconds) : nil
    }

    private func attendanceDate(_ value: String, on date: String) -> Date? {
        FormatterHelper.shared.date(from: value)
            ?? FormatterHelper.shared.date(from: "\(date) \(value)")
    }

    private func halfDayLabel(_ attendance: Attendance) -> String {
        guard let leave = attendance.halfDayLeave, !leave.isEmpty else {
            return "Half Day Present"
        }
        return "Half Day Present\n/\(leave)"
    }
    
    //MARK: To Check weather the date is past date for not
    private func checkForPastAttendance(date: String) -> Bool {
        //Convert the dateString to date object
        guard let inputDate = FormatterHelper.shared.date(from: date) else {
            AppLog.debug("Invalid date Format")
            return false
        }
        
        //get the current date without the time component
        let currentDate = Date()
        let startOfToday = Calendar.current.startOfDay(for: currentDate)
        
        //compare the input date with the current date
        return inputDate < startOfToday
    }

    private func isOnOrBeforeToday(date: String) -> Bool {
        guard let inputDate = FormatterHelper.shared.date(from: date) else {
            AppLog.debug("Invalid date Format")
            return false
        }
        return inputDate <= Calendar.current.startOfDay(for: Date())
    }
    
    private func applyColor(status: String) -> Color {
        switch status {
        case "Absent":
            return Color.absent
        case "Present":
            return Color.present
        default:
            return Color.subText
        }
    }
    
    
//    private func initials(from input: String) -> String {
//        let words = input.split(separator: " ")
//        let initials = words.map { word in
//            word.prefix(1).uppercased()
//        }
//        return initials.joined()
//    }
}

#Preview {
    AttendanceHistoryView(attendanceViewModel: AttendanceViewModel(), editAttendanceViewModel: EditAttendanceViewModel(), showEditAttendance: .constant(false))
//    ALHView()
}
