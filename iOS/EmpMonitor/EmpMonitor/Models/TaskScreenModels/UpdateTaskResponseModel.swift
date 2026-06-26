//
//  UpdateTaskResponseModel.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 13/09/24.
//

import Foundation

// MARK: - UpdateTaskResponseModel
struct UpdateTaskResponseModel: Codable {
    let statusCode: Int
    let body: UpdateTaskResponseBody
}

// MARK: - Body
struct UpdateTaskResponseBody: Codable {
    let status, message: String
    let data: UpdateTaskResponseData?
}

// MARK: - DataClass
struct UpdateTaskResponseData: Codable {
    let value: Value
    let id, clientID, orgID, taskName: String
    let empID, date, startTime, endTime: String
    let taskDescription: String
    let taskApproveStatus: Int
    let empStartTime, empEndTime: String?
    let tagLogs, files, images: [JSONAny]
    let taskVolume: Int
    let createdAt, updatedAt: String
    let v: Int

    enum CodingKeys: String, CodingKey {
        case value
        case id = "_id"
        case clientID = "clientId"
        case orgID = "orgId"
        case taskName
        case empID = "emp_id"
        case date
        case startTime = "start_time"
        case endTime = "end_time"
        case taskDescription, taskApproveStatus, empStartTime, empEndTime, tagLogs, files, images, taskVolume, createdAt, updatedAt
        case v = "__v"
    }
}

