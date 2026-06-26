//
//  UpdateClientResponseModel.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 02/09/24.
//

import Foundation

// MARK: - UpdateClientResponseModel
struct UpdateClientResponseModel: Codable {
    let statusCode: Int
    let body: UpdateClientResponseBody
}

// MARK: - Body
struct UpdateClientResponseBody: Codable {
    let status, message: String
    let data: UpdateClientResponseData?
}

// MARK: - DataClass
struct UpdateClientResponseData: Codable {
    let id, clientName, orgID: String
    let empID: String?
    let emailID, contactNumber, clientProfilePic, countryCode: String
    let address1, address2, city, state: String
    let country, zipCode, latitude, longitude: String
    let clientType: Int
    let category: String
    let status: Int
    let assignedEmployees: String?
    let createdAt, updatedAt: String
    let v: Int
    let updatedBy: String?

    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case clientName
        case orgID = "orgId"
        case empID = "emp_id"
        case emailID = "emailId"
        case contactNumber, clientProfilePic, countryCode, address1, address2, city, state, country, zipCode, latitude, longitude, clientType, category, status, assignedEmployees, createdAt, updatedAt
        case v = "__v"
        case updatedBy
    }
}
