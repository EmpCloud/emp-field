//
//  CalenderViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 13/08/24.
//

import SwiftUI
import Foundation

@MainActor
class CalendarViewModel: ObservableObject {
    
    @Published var currentDate = Date()
    
    let calendar: Calendar
    
    init() {
        var calendar = Calendar.current
        calendar.firstWeekday = 1  // 1 = sunday , 2 = monday
        self.calendar = calendar
        
    }
    
    var currentMonthDates: [Date] {
        guard let monthInterval = calendar.dateInterval(of: .month, for: currentDate) else {
            return []
        }
        var dates: [Date] = []
        var date = monthInterval.start

        while date < monthInterval.end {
            dates.append(date)
            guard let nextDate = calendar.date(byAdding: .day, value: 1, to: date) else { break }
            date = nextDate
        }

        return dates
    }
    
    var startOfMonth: Date {
        let components = calendar.dateComponents([.year, .month], from: currentDate)
        return calendar.date(from: components) ?? currentDate
    }
    
    func previousMonth() {
        if let newDate = calendar.date(byAdding: .month, value: -1, to: currentDate) {
            currentDate = newDate
        }
    }

    func nextMonth() {
        if let newDate = calendar.date(byAdding: .month, value: 1, to: currentDate) {
            currentDate = newDate
        }
    }
    
    func dayString(from date: Date) -> String {
        let day = calendar.component(.day, from: date)
        return String(day)
    }
    
    func monthYearString() -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "MMMM yyyy"
        return formatter.string(from: currentDate)
    }
    
    func fullDateString(from date: Date) -> String {
            let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter.string(from: date)
    }
    
    func presentFullDate() -> String {
        return fullDateString(from: Date())
    }
    
    
    func leadingEmptyDays() -> Int {
        let firstDay = startOfMonth
        let weekday = calendar.component(.weekday, from: firstDay)
        
//        AppLog.debug("FirstDay: \(firstDay)")
//        AppLog.debug("Weekday: \(weekday)")
//        AppLog.debug("Result: \((weekday + 6) % 7)")
//        self.emptyDays = (weekday + 6) % 7 
        return (weekday + 6) % 7  //to adjust and ensure correct leading spaces
    }
}
