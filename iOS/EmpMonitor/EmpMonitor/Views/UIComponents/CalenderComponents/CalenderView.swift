//
//  CalenderView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 13/08/24.
//

import SwiftUI

struct CalenderView: View {
    
    @EnvironmentObject var calendarViewModel: CalendarViewModel
    
    @ObservedObject  var attendanceViewModel: AttendanceViewModel
    @ObservedObject var leavesViewModel: LeavesViewModel
    
    
    private let colums = Array(repeating: GridItem(.flexible()), count: 7)
    private let daysOfWeek = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"]
    
    @Binding var viewSelection: String?
    
    @State private var selectedDate: String?
    
    // for add leaves
    @Binding var startDate: String?
    @Binding var endDate: String?
    @Binding var setStartDate: Bool?
    @Binding var setEndDate: Bool?
    
    @Binding var showCalendar: Bool
    
    var body: some View {
        VStack {
            VStack{
                Button{
                    showCalendar.toggle()
                }label: {
                    Image(systemName: "xmark")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 10.75, height: 10.75)
                        .foregroundStyle(Color.primaryButton1)
                }
                .padding(5)
                .frame(maxWidth: .infinity, alignment: .trailing)
                .padding(.trailing, 30)
            }
            
            HStack(spacing: 50) {
                Button{
                    calendarViewModel.previousMonth()
                }label: {
                    Image(systemName: "chevron.left")
                }
                
                Text(calendarViewModel.monthYearString())
                    .font(AppFont.primary(size: AppFont.Size.body))
                    .fontWeight(AppFont.Weight.semibold)
                    .foregroundStyle(
                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                    )
                
                Button{
                    calendarViewModel.nextMonth()
                }label: {
                    Image(systemName: "chevron.right")
                }
            }
            .padding(.bottom)
            
            HStack {
                ForEach(daysOfWeek, id: \.self) { day in
                    Text(day.uppercased())
                        .font(AppFont.primary(size: AppFont.Size.body))
                        .fontWeight(AppFont.Weight.semibold)
                        .foregroundStyle(
                            LinearGradient(gradient: Gradient(colors: [Color.week1, Color.week2]), startPoint: .top, endPoint: .bottom)
                        )
                        .frame(maxWidth: .infinity)
                }
            }
            .padding(.horizontal)
            
            LazyVGrid(columns: colums, spacing: 10) {
                
                //Calculate the number of leading empty days
//                let firstDay = calendarViewModel.startOfMonth
//                let leadingEmptyDays = calendarViewModel.calendar.component(.weekday, from: firstDay) - 1
                
                //add empty spaces before the first day of the month
                ForEach(0..<calendarViewModel.leadingEmptyDays(), id: \.self) { _ in
                      Text("") // placeholder for empty days
                        .frame(width: 25, height: 25)
                }
                
                //Add actual days of the current month
                ForEach(calendarViewModel.currentMonthDates, id: \.self) { currentDate in
                    
//                    let currentDate = calendarViewModel.currentMonthDates[index]
                    let currentDateString = calendarViewModel.fullDateString(from: currentDate)
                    let presentDateString = calendarViewModel.presentFullDate()
                    
                    Text(calendarViewModel.dayString(from: currentDate))
                        .font(AppFont.primary(size: AppFont.Size.headline))
                        .fontWeight(AppFont.Weight.medium)
                        .foregroundStyle(
                            (selectedDate == currentDateString && presentDateString == currentDateString) ? Color.white : presentDateString == currentDateString ? Color.primaryButton1 : selectedDate == currentDateString ? Color.white : Color.black
                        )
                        .frame(width: 25, height: 25)
                        .overlay {
                            if presentDateString == currentDateString {
                                Circle()
                                    .stroke(LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom), lineWidth: 2)
                            }
                        }
                        .background(
                            Circle()
                                .fill( selectedDate == currentDateString ?
                                       LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom) :
                                        LinearGradient(gradient: Gradient(colors: [Color.white, Color.white]), startPoint: .top, endPoint: .bottom)
                                )
                                .frame(width: 25, height: 25)
                        )
                        .clipShape(Circle())
                        .onTapGesture {
                            selectedDate = currentDateString
                        }
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                }
            }
            .padding(.top)
            .padding(.horizontal)
            
            VStack {
                Button {
                    if let setStartDate = setStartDate, setStartDate {
                        if let date = selectedDate {
                            startDate = date
                        }
                    }
                    if let setEndDate = setEndDate, setEndDate {
                        if let date = selectedDate {
                            endDate = date
                        }
                    }
                    
                    //TODO:  to refresh the screen according to the new dates
                    switch viewSelection {
                    case "Attendance History":
                        //TODO: Call attendance api will new date
                        AppLog.debug("Attedance")
                        Task {
                            attendanceViewModel.attendanceStartDate = startDate ?? HelperFunction.shared.getFirstDateOfCurrentMonth()
                            attendanceViewModel.attendanceEndDate = HelperFunction.shared.dateAfter30Days(from: attendanceViewModel.attendanceStartDate) ?? attendanceViewModel.attendanceStartDate
                            await attendanceViewModel.getAttendanceData()
                            withAnimation {
                                showCalendar.toggle()
                            }
                        }
                    case "Leaves":
                        //TODO: call leaves api with new date
                        AppLog.debug("Leaves")
                        Task {
                            leavesViewModel.startDate = startDate ?? HelperFunction.shared.getFirstDateOfCurrentMonth()
                            leavesViewModel.endDate = HelperFunction.shared.dateAfter30Days(from: leavesViewModel.startDate) ?? leavesViewModel.startDate
                            
                            await leavesViewModel.getLeaves()
                            
                            withAnimation {
                                showCalendar.toggle()
                            }
                        }
                    default:
                        //TODO: Do nothing
                        AppLog.debug("default")
                    }
                    
                }label: {
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
            }
            .frame(maxWidth: .infinity, alignment: .trailing)
            .padding(.trailing, 30)
//            .padding(.top, -20)
        }
        .padding(.vertical)
        .background(Color.white)
//        .border(Color.black)
        .clipShape(RoundedRectangle(cornerRadius: 10))
        .padding(25)
    }
    
    func reloadScreen(viewSelection: String) {
        switch viewSelection {
        case "Attendance History":
            //TODO: Call attendance api will new date
            AppLog.debug("Attedance")
        case "Leaves":
            //TODO: call leaves api with new date
            AppLog.debug("Leaves")
            Task {
                leavesViewModel.startDate = startDate ?? HelperFunction.shared.getFirstDateOfCurrentMonth()
                leavesViewModel.endDate = HelperFunction.shared.dateAfter30Days(from: leavesViewModel.startDate) ?? leavesViewModel.startDate
                
                await leavesViewModel.getLeaves()
            }
        default:
            //TODO: Do nothing
            AppLog.debug("default")
        }
    }
}

#Preview {
    CalenderView(attendanceViewModel: AttendanceViewModel(), leavesViewModel: LeavesViewModel(), viewSelection: .constant(""), startDate: .constant(""), endDate: .constant(""), setStartDate: .constant(false), setEndDate: .constant(false), showCalendar: .constant(false))
        .environmentObject(CalendarViewModel())
}
