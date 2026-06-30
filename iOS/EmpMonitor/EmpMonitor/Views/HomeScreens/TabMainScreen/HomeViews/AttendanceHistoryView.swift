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
                LazyVGrid(columns: [GridItem(.fixed(1))], alignment: .leading) {
                    LazyHGrid(rows: [GridItem(.fixed(4))], spacing: 0) {
                        Text("Date")
                            .frame(width: 70, alignment: .leading)
                        Text("Status")
                            .frame(width: 105, alignment: .leading)
                        Text("Clocked-in")
                            .frame(width: 85)
                        Text("Clocked-out")
                            .frame(width: 90)
//                        .padding(.leading, 3)
                    }
                    .font(.custom("Montserrat", size: 12))
                    .foregroundStyle(Color.attendanceTitleText)
                    .padding(.leading, 10)
                    .padding(.vertical, 10)
                    
                    ForEach(attendanceViewModel.attendanceData, id: \.date) { attendance in
                        LazyHGrid(rows: [GridItem(.fixed(1))], spacing: 0) {
                            Text(FormatterHelper.shared.formattedDate(from: attendance.date))
                                .frame(width: 70, alignment: .leading)
                            
                            Text("\(checkAttendanceStatus(attendance: attendance))")
                                .frame(width: 105, alignment: .leading)
                                .foregroundStyle(applyColor(status: checkAttendanceStatus(attendance: attendance)))
                            
                            if let clockedIN = FormatterHelper.shared.checkTimeFormatter(from: attendance.startTime ?? "") {
                                Text(clockedIN)
                                    .frame(width: 85)
                                    .foregroundStyle(Color.present)
                            }else{
                                Text("--:--")
                                    .frame(width: 85)
                            }
                            
                            HStack(spacing: 15) {
                                if let clockedIN = FormatterHelper.shared.checkTimeFormatter(from: attendance.endTime ?? "") {
                                    Text(clockedIN)
                                        .frame(width: 60)
                                        .foregroundStyle(Color.absent)
                                }else{
                                    Text("--:--")
                                        .frame(width: 60)
                                }
                                
                                Image(.editAttendanceIcon)
                                    .onTapGesture {
                                        withAnimation {
                                            editAttendanceViewModel.date = attendance.date
                                            showEditAttendance.toggle()
                                        }
                                    }
                            }
                            .frame(width: 95)
                        }
                        LineView()
                            .padding(.vertical, 7)
                    }
                    .font(.custom("Montserrat", size: 11))
                    .fontWeight(.semibold)
                    .padding(.horizontal, 10)
                }
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
                .font(.custom("Montserrat", size: 14))
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
                .font(.custom("Montserrat", size: 14))
        }
        .frame(maxWidth: .infinity, minHeight: 400)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 20))
        .padding(.horizontal, 7)
    }
    
    private func checkAttendanceStatus(attendance: Attendance) -> String {
        if attendance.openRequest?.status == 0 || attendance.openAttendanceRequest != nil{
            if attendance.openAttendanceRequest?.requestStatus == "1" {
                return "Approved"
            }
            else if attendance.openAttendanceRequest?.requestStatus == "2" {
                return "Rejected"
            }
            else{
                return "Pending"
            }
           
        }
        else if attendance.startTime != nil && attendance.endTime != nil{
            if attendance.loggedDuration ?? 0 > attendance.minHours.first?.manualHours ?? 28800 {
                return "Present"
            }
            else if attendance.loggedDuration ?? 0 > ((attendance.minHours.first?.manualHours ?? 28800) / 2) && attendance.loggedDuration ?? 0 < attendance.minHours.first?.manualHours ?? 28800 {
                if attendance.openRequest != nil {
                    return "Half Day | \(LeaveHelper.shared.getInitials(from: attendance.openRequest?.leaveName ?? ""))"
                }
                return "Half Day | Un-Paid"
            }else{
                return "Absent"
            }
        }
        else if attendance.startTime != nil && attendance.endTime == nil {
            return "Absent"
        }
        else if attendance.holidayStatus == 1 {
            return attendance.holidayName ?? ""
        }
        else if attendance.dayOff == false{
            return "Week Off"
        }
        else if attendance.leaveName == "Unpaid" && checkForPastAttendance(date: attendance.date) {
            return "Absent"
        }
        else if attendance.leaveName == "Unpaid" && !checkForPastAttendance(date: attendance.date) {
            return "   --:--   "
        }
        else if  attendance.leaveName != nil {
            return attendance.leaveName ?? ""
        }
//        else if attendance.leaveName != nil && !checkForPastAttendance(date: attendance.date){
//            if attendance.leaveName == "Unpaid" {  // to handle the future date attendance
//                return "   --:--   "
//            }else {
//                return attendance.leaveName ?? ""
//            }
//            
//        }
        else {
            return "Absent"
        }
//        else if attendance.startTime == nil && attendance.endTime == nil && checkForPastAttendance(date: attendance.date){
//            return "Absent"
//        }
    }
    
    //MARK: To Check weather the date is past date for not
    private func checkForPastAttendance(date: String) -> Bool {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
//        dateFormatter.timeZone = TimeZone(
        
        //Convert the dateString to date object
        guard let inputDate = dateFormatter.date(from: date) else {
            print("Invalid date Format")
            return false
        }
        
        //get the current date without the time component
        let currentDate = Date()
        let startOfToday = Calendar.current.startOfDay(for: currentDate)
        
        //compare the input date with the current date
        return inputDate < startOfToday
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
