//
//  DateViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import Foundation
import SwiftUI

class DateViewModel: ObservableObject {
    @Published var selectedDate = Date()
    @Published var showPicker: Bool = false

    @Published var hour: Int = 12
    @Published var minutes: Int = 0

    @Published var setStartTime: Bool = false
    @Published var setStopTime: Bool = false

    @Published var changeToMin: Bool = false
    @Published var symbol = "AM"

    @Published var angle: Double = 0

    private var formatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm"
        formatter.timeZone = TimeZone.current
        return formatter
    }()

    func generateTime() {
        var components = DateComponents()
        components.timeZone = TimeZone.current
        components.minute = minutes

        // Handle 12:00 AM and 12:00 PM explicitly
        if symbol == "AM" {
            components.hour = (hour == 12) ? 0 : hour
        } else {
            components.hour = (hour == 12) ? 12 : hour + 12
        }

        if let date = Calendar.current.date(from: components) {
            self.selectedDate = date
        }

        withAnimation {
            showPicker.toggle()
            changeToMin = false
        }
    }

    func getFormattedDate() -> String {
        return formatter.string(from: selectedDate)
    }

    func setTime() {
        var calendar = Calendar.current
        calendar.timeZone = TimeZone.current

        hour = calendar.component(.hour, from: selectedDate)
        symbol = hour < 12 ? "AM" : "PM"
        hour = hour == 0 ? 12 : hour > 12 ? hour - 12 : hour

        minutes = calendar.component(.minute, from: selectedDate)
        angle = Double(hour * 30)
    }
}


