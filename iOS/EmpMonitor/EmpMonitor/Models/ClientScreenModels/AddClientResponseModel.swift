//
//  AddClientResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/08/24.
//

import Foundation

// MARK: - AddClientResponseModel
struct AddClientResponseModel: Codable {
    let statusCode: Int
    let body: AddClientResponseBody
}

// MARK: - Body
struct AddClientResponseBody: Codable {
    let status, message: String
    let error: String?
    let data: AddClientResponseData?
}

// MARK: - DataClass
struct AddClientResponseData: Codable {
    let clientName, orgID, empID, emailID: String
    let contactNumber: String
    let clientProfilePic: String?
    let countryCode, address1, address2, city: String
    let state, country, zipCode, latitude: String
    let longitude: String
    let clientType: Int
    let category: String
    let status: Int
    let id, createdAt, updatedAt: String
    let v: Int

    enum CodingKeys: String, CodingKey {
        case clientName
        case orgID = "orgId"
        case empID = "emp_id"
        case emailID = "emailId"
        case contactNumber, clientProfilePic, countryCode, address1, address2, city, state, country, zipCode, latitude, longitude, clientType, category, status
        case id = "_id"
        case createdAt, updatedAt
        case v = "__v"
    }
}
