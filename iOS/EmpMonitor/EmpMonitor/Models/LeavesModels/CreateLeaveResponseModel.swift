//
//  CreateLeaveResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 14/08/24.
//

import Foundation

struct CreateLeaveResponseModel: Codable {
    let statusCode: Int
    let body: CreateLeaveResponseBody
}

// MARK: - Body
struct CreateLeaveResponseBody: Codable {
    let status, message: String
    let error: ErrorMessage?
    let data: CreateLeaveResponseData?
}

// MARK: - BodyData
struct CreateLeaveResponseData: Codable {
    let code: Int
    let message: String
    let error: JSONNull?
    let data: CreateLeaveResponseDetail
}

// MARK: - DataData
struct CreateLeaveResponseDetail: Codable {
    let leave: CreateLeave
}

// MARK: - Leave
struct CreateLeave: Codable {
    let leaveID, numberOfDays: Int
    let timezone: String

    enum CodingKeys: String, CodingKey {
        case leaveID = "leave_id"
        case numberOfDays = "number_of_days"
        case timezone
    }
}

// MARK: - Error
struct ErrorMessage: Codable {
    let code: Int
    let message: String
    let error, data: JSONNull?
}
