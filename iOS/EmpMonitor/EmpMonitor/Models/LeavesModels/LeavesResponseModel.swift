//
//  LeavesResponse.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/07/24.
//

import Foundation

// MARK: - LeavesResponseModel
struct LeavesResponseModel: Codable {
    let statusCode: Int
    let body: LeavesResponseBody
}

// MARK: - Body
struct LeavesResponseBody: Codable {
    let status, message: String
    let data: [LeavesResponseData]?
}

// MARK: - Datum
struct LeavesResponseData: Codable {
    let id: Int
    let empID: String?
    let employeeID: Int
    let employeeName: String?
    let startDate, endDate: String
    let status: Int
    let numberOfDays: Double
    let dayType, leaveType: Int
    let name: String?
    let reason: String?
    let dayStatus: String?
    let leaveStatus: LeaveStatus?

    enum CodingKeys: String, CodingKey {
        case id
        case empID = "emp_id"
        case employeeID = "employee_id"
        case employeeName = "employee_name"
        case startDate = "start_date"
        case endDate = "end_date"
        case status
        case numberOfDays = "number_of_days"
        case dayType = "day_type"
        case leaveType = "leave_type"
        case name, reason
        case dayStatus = "day_status"
        case leaveStatus = "leave_status"
    }
}

// MARK: - LeaveStatus
struct LeaveStatus: Codable {
    let pendingLeaves, approvedLeaves, rejectedLeaves: Int

    enum CodingKeys: String, CodingKey {
        case pendingLeaves = "pending_leaves"
        case approvedLeaves = "approved_leaves"
        case rejectedLeaves = "rejected_leaves"
    }
}
