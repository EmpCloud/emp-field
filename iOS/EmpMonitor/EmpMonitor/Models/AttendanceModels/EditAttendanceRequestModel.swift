//
//  EditAttendanceRequestModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 27/08/24.
//

import Foundation

// MARK: - EditAttendanceRequestModel
struct EditAttendanceRequestModel: Codable {
    let date, checkIn, checkOut, reason: String

    enum CodingKeys: String, CodingKey {
        case date
        case checkIn = "check_in"
        case checkOut = "check_out"
        case reason
    }
}
