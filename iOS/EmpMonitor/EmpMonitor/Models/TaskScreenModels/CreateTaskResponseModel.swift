//
//  CreateTaskResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 27/08/24.
//

import Foundation

// MARK: - CreateTaskResponseModel
struct CreateTaskResponseModel: Codable {
    let statusCode: Int
    let body: CreateTaskResponseBody
}

// MARK: - Body
struct CreateTaskResponseBody: Codable {
    let status, message: String
    let data: CreateTaskResponseData?
    let error: AddTaskError?
}

// MARK: - DataClass
struct CreateTaskResponseData: Codable {
    let clientID, orgID, taskName, empID: String
    let date, startTime, endTime, taskDescription: String
    let taskApproveStatus: Int
    let empStartTime, empEndTime: String?
    let tagLogs: [JSONAny]
    let files: [FileUrl]
    let images: [SelectedResponseImage]
    let value: ResponseValue
    let taskVolume: Int
    let id, createdAt, updatedAt: String
    let v: Int

    enum CodingKeys: String, CodingKey {
        case clientID = "clientId"
        case orgID = "orgId"
        case taskName
        case empID = "emp_id"
        case date
        case startTime = "start_time"
        case endTime = "end_time"
        case taskDescription, taskApproveStatus, empStartTime, empEndTime, tagLogs, files, images, value, taskVolume
        case id = "_id"
        case createdAt, updatedAt
        case v = "__v"
    }
}

// MARK: - File
struct FileUrl: Codable {
    let url: String
    let id: String

    enum CodingKeys: String, CodingKey {
        case url
        case id = "_id"
    }
}

// MARK: - Image
struct SelectedResponseImage: Codable {
    let url: String
    let description: String?
    let id: String

    enum CodingKeys: String, CodingKey {
        case url, description
        case id = "_id"
    }
}

// MARK: - Value
struct ResponseValue: Codable {
    let currency: String?
    let amount: Double?
}


// MARK: - Error
struct AddTaskError: Codable {
    let details: [Detail]

}

// MARK: - Detail
struct Detail: Codable {
    let message: String
    let path: [String]
    let type: String
}
