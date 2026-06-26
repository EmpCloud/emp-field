//
//  TaskCalendarView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct TaskCalendarView: View {
    
    @EnvironmentObject var calendarViewModel: CalendarViewModel
    
    private let colums = Array(repeating: GridItem(.flexible()), count: 7)
    private let daysOfWeek = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"]
    
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
                    .font(.system(size: 14, weight: .semibold))
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
                        .font(.system(size: 14, weight: .semibold))
                        .foregroundStyle(
                            LinearGradient(gradient: Gradient(colors: [Color.week1, Color.week2]), startPoint: .top, endPoint: .bottom)
                        )
                        .frame(maxWidth: .infinity)
                }
            }
            .padding(.horizontal)
            
            LazyVGrid(columns: colums, spacing: 10) {
            
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
                        .font(.system(size: 16, weight: .medium))
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
                    
                    withAnimation {
                        showCalendar.toggle()
                    }
                    
                }label: {
                    Text("OK")
                        .font(.system(size: 12, weight: .semibold))
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
            
//            HStack(spacing: 40) {
//                VStack{
//                    Text("Start Time")
//                        .font(.custom("Montserrat", size: 14))
//                        .fontWeight(.bold)
//                        .foregroundStyle(Color.subText)
//                    RoundedRectangle(cornerRadius: 6)
//                        .fill(Color.scheduleBG)
//                        .frame(width: 124, height: 46)
//                        .overlay {
//                            HStack{
//                                Text("10:00AM")
//                                    .font(.custom("Montserrat", size: 12))
//                                    .fontWeight(.bold)
//                                    .foregroundStyle(Color.subText)
//                                
//                                Circle()
//                                    .fill(Color.white)
//                                    .frame(width: 35.46, height: 35.46)
//                                    .overlay {
//                                        Image(systemName: "clock")
//                                            .resizable()
//                                            .aspectRatio(contentMode: .fit)
//                                            .frame(width: 17.71, height: 17.71)
//                                            .foregroundStyle(Color.primaryButton1)
//                                    }
//                            }
//                        }
//                }
//                VStack{
//                    Text("Stop Time")
//                        .font(.custom("Montserrat", size: 14))
//                        .fontWeight(.bold)
//                        .foregroundStyle(Color.subText)
//                    RoundedRectangle(cornerRadius: 6)
//                        .fill(Color.scheduleBG)
//                        .frame(width: 124, height: 46)
//                        .overlay {
//                            HStack{
//                                Text("10:00PM")
//                                    .font(.custom("Montserrat", size: 12))
//                                    .fontWeight(.bold)
//                                    .foregroundStyle(Color.subText)
//                                
//                                Circle()
//                                    .fill(Color.white)
//                                    .frame(width: 35.46, height: 35.46)
//                                    .overlay {
//                                        Image(systemName: "clock")
//                                            .resizable()
//                                            .aspectRatio(contentMode: .fit)
//                                            .frame(width: 17.71, height: 17.71)
//                                            .foregroundStyle(Color.primaryButton1)
//                                    }
//                            }
//                        }
//                }
//            }
//            .padding(.top, 40)
        }
        .padding(.vertical)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 10))
        .padding(25)
        .onTapGesture {
            showCalendar = true
        }
    }
}

#Preview {
    TaskCalendarView(startDate: .constant(""), endDate: .constant(""), setStartDate: .constant(false), setEndDate: .constant(false), showCalendar: .constant(false))
        .environmentObject(CalendarViewModel())
}
