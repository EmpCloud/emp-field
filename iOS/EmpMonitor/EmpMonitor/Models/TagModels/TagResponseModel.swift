//
//  TagResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 12/09/24.
//

import Foundation

// MARK: - TagResponseModel
struct TagResponseModel: Codable {
    let statusCode: Int
    let body: TagResponseBody
}

// MARK: - Body
struct TagResponseBody: Codable {
    let status, message: String
    let data: [TagResponseData]
}

// MARK: - Datum
struct TagResponseData: Codable {
    let id, orgID, tagName: String
    let tagDescription: String?
    let color, createdBy: String
    let isActive: Bool
    let createdAt, updatedAt: String
    let v: Int

    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case orgID = "orgId"
        case tagName, tagDescription, color, createdBy, isActive, createdAt, updatedAt
        case v = "__v"
    }
}

