//
//  UpdateLeaveResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 16/08/24.
//

import Foundation

struct UpdateLeaveResponseModel: Codable {
    let statusCode: Int
    let body: UpdateLeaveResponseBody
}

// MARK: - Body
struct UpdateLeaveResponseBody: Codable {
    let status, message: String
    let data: UpdateLeaveResponseData
}

// MARK: - BodyData
struct UpdateLeaveResponseData: Codable {
    let code: Int
    let message: String
    let error: JSONNull?
    let data: UpdateLeaveResponseDetail
}

// MARK: - DataData
struct UpdateLeaveResponseDetail: Codable {
    let leave: UpdateLeave
}

// MARK: - Leave
struct UpdateLeave: Codable {
    let leaveID, numberOfDays: Int

    enum CodingKeys: String, CodingKey {
        case leaveID = "leave_id"
        case numberOfDays = "number_of_days"
    }
}
