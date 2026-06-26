//
//  CreateLeaveRequestModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 14/08/24.
//

import Foundation

// MARK: - CreateLeaveRequestModel
struct CreateLeaveRequestModel: Codable {
    let dayType, leaveType: Int
    let startDate, endDate, reason: String

    enum CodingKeys: String, CodingKey {
        case dayType = "day_type"
        case leaveType = "leave_type"
        case startDate = "start_date"
        case endDate = "end_date"
        case reason
    }
}
