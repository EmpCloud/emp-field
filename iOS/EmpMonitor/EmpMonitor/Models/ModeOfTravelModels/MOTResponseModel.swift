//
//  MOTResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/09/24.
//

import Foundation

// MARK: - MOTResponseModel
struct MOTResponseModel: Codable {
    let statusCode: Int
    let body: MOTResponseBody
}

// MARK: - Body
struct MOTResponseBody: Codable {
    let status, message: String
    let data: MOTResponseData
}

// MARK: - DataClass
struct MOTResponseData: Codable {
    let id, orgID, empID: String
    let currentRadius, currentFrequency: Int
    let currentMode: String
    let defaultConfig: [DefaultConfig]
    let createdAt, updatedAt: String
    let v: Int

    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case orgID = "orgId"
        case empID = "emp_id"
        case currentRadius, currentFrequency, currentMode, defaultConfig, createdAt, updatedAt
        case v = "__v"
    }
}

// MARK: - DefaultConfig
struct DefaultConfig: Codable {
    let mode: String
    let frequency, radius: Int
}
