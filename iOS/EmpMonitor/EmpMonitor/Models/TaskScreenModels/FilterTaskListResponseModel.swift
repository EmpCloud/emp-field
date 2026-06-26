//
//  FilterTaskListModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 05/09/24.
//

import Foundation

// MARK: - FilterTaskListResponseModel
struct FilterTaskListResponseModel: Codable {
    let statusCode: Int
    let body: FilterTaskListResponseBody
}

// MARK: - Body
struct FilterTaskListResponseBody: Codable {
    let status, message: String
    let data: [FilterTaskListResponseData]?
}

// MARK: - Datum
struct FilterTaskListResponseData: Codable {
    let value: FilterTaskValue?
    let id, clientID, orgID, taskName: String
    let empID, date, startTime, endTime: String
    let taskDescription: String
    let taskApproveStatus: Int
    let empStartTime, empEndTime: String?
    let tagLogs: [TaskTagLog]
    let files: [FilterTaskFile]
    let images: [FilterTaskImage]
    let taskVolume: Int
    let createdAt, updatedAt: String
    let v: Int
    let clientName, address1, address2, city: String?
    let longitude, latitude: String?
    let currentTagDetails: CurrentTagDetails?

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
        case clientName, address1, address2, city, longitude, latitude, currentTagDetails
    }
}

// MARK: - CurrentTagDetails
struct CurrentTagDetails: Codable {
    let id, orgID, tagName, tagDescription: String?
    let color, createdBy: String?
    let isActive: Bool?
    let order: Int?
    let createdAt, updatedAt: String?
    let v: Int?
    let updatedBy: String?

    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case orgID = "orgId"
        case tagName, tagDescription, color, createdBy, isActive, order, createdAt, updatedAt
        case v = "__v"
        case updatedBy
    }
}

// MARK: - File
struct FilterTaskFile: Codable {
    let url: String
    let id: String

    enum CodingKeys: String, CodingKey {
        case url
        case id = "_id"
    }
}

// MARK: - Image
struct FilterTaskImage: Codable {
    let url: String
    let description, id: String

    enum CodingKeys: String, CodingKey {
        case url, description
        case id = "_id"
    }
}

// MARK: - Value
struct FilterTaskValue: Codable {
    let convertedAmountInUSD : Double?
    let currency: String?
    let amount: Int?
}
