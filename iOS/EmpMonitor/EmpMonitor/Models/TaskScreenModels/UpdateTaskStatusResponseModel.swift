//
//  UpdateTaskResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 09/09/24.
//

import Foundation

// MARK: - UpdateTaskResponseModel
struct UpdateTaskStatusResponseModel: Codable {
    let statusCode: Int
    let body: UpdateTaskStatusResponseBody
}

// MARK: - Body
struct UpdateTaskStatusResponseBody: Codable {
    let status, message: String
    let data: UpdateTaskStatusResponseData?
}

// MARK: - DataClass
struct UpdateTaskStatusResponseData: Codable {
    let id, orgID, empID: String
    let geologs: [Geolog]
    let createdAt, updatedAt: String
    let v: Int

    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case orgID = "orgId"
        case empID = "emp_id"
        case geologs, createdAt, updatedAt
        case v = "__v"
    }
}

// MARK: - Geolog
struct Geolog: Codable {
    let time: String
    let latitude, longitude: Double?
    let status: Int
    let taskID: String?
    let id: String

    enum CodingKeys: String, CodingKey {
        case time, latitude, longitude, status
        case taskID = "taskId"
        case id = "_id"
    }
}
