//
//  TaskListResponseModel.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 21/08/24.
//

import Foundation

// MARK: - TaskListResponseModel
struct TaskListResponseModel: Codable {
    let statusCode: Int
    let body: TaskListResponseBody
}

// MARK: - Body
struct TaskListResponseBody: Codable {
    let status, message: String
    let data: [TaskListResponseData]
}

// MARK: - Datum
struct TaskListResponseData: Codable {
    let value: Value?
    let id, clientID, orgID, taskName: String
    let empID, date, startTime, endTime: String
    let taskDescription: String
    let taskApproveStatus: Int
    let empStartTime, empEndTime: String?
    let tagLogs: [JSONAny]
    let files: [TaskFile]
    let images: [TaskImage]
    let taskVolume: Int
    let createdAt, updatedAt: String
    let v: Int
    let clientName: String?
    let address1, address2, city: String?
    let longitude, latitude: String?

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
        case clientName, address1, address2, city, longitude, latitude
    }
}

// MARK: - File
struct TaskFile: Codable {
    let url: String
    let id: String

    enum CodingKeys: String, CodingKey {
        case url
        case id = "_id"
    }
}

// MARK: - Image
struct TaskImage: Codable {
    let url: String
    let description: String?
    let id: String

    enum CodingKeys: String, CodingKey {
        case url, description
        case id = "_id"
    }
}

//// MARK: - Value
//struct Value: Codable {
//    let currency: String
//    let amount: Int
//}
