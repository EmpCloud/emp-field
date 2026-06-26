//
//  LeaveHelper.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 16/08/24.
//

import Foundation
import SwiftUI

class LeaveHelper {
    static let shared = LeaveHelper()
    
    //Leave Status Helper
    func getLeaveStatus(status: Int) -> String{
        switch status {
        case 0:
            "Pending"
        case 1:
            "Approved"
        case 2:
            "Rejected"
        default:
            ""
        }
    }
    //leave status color
    func getLeaveStatusColor(status: Int) -> Color {
        switch status {
        case 0:
            Color.notification
        case 1:
            Color.present
        case 2:
            Color.absent
        default:
            Color.black
        }
    }
    
    //leave status color
    func getLeaveDayType(status: Int) -> String {
        switch status {
        case 1:
            "First Half"
        case 2:
            "Full Day"
        case 3:
            "Second Half"
        default:
            ""
        }
    }
    
    //leave status color
    func getLeaveType(status: Int, leaveTypes: String) -> String {
        switch status {
        case 1:
            "First Half"
        case 2:
            "Full Day"
        case 3:
            "Second Half"
        default:
            ""
        }
    }
    
    
    func getDayType(dayType: String) -> Int {
        switch dayType {
        case "First Half":
            return 1
        case "Second Half":
            return 3
        case "Full Day":
            return 2
        default:
            return 0
        }
    }
    
    
    //return initial of the leave eg: EL/CL
    func getInitials(from input: String) -> String {
        let words = input.split(separator: " ")
        let initials = words.map { word in
            word.prefix(1).uppercased()
        }
        return initials.joined()
    }
}
