//
//  UpdateLeaveRequestModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 16/08/24.
//

import Foundation

struct UpdateLeaveRequestModel: Codable {
    let leaveID, dayType, leaveType: Int
    let startDate, endDate, reason: String
    
    enum CodingKeys: String, CodingKey {
        case leaveID = "leave_id"
        case dayType = "day_type"
        case leaveType = "leave_type"
        case startDate = "start_date"
        case endDate = "end_date"
        case reason
    }
}
