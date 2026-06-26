//
//  ClientListResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import Foundation

// MARK: - ClientListResponseModel
struct ClientListResponseModel: Codable {
    let statusCode: Int
    let body: ClientListResponseBody
}

// MARK: - Body
struct ClientListResponseBody: Codable {
    let status, message: String
    let data: [ClientListResponseData]
}

// MARK: - Datum
struct ClientListResponseData: Codable {
    let clientProfilePic: String?
    let id, clientName: String
    let orgID: String?
    let empID: String?
    let clientID: String?
    let contactNumber: String?
    let countryCode: String?
    let address1, address2: String?
    let city: String?
    let state: String?
    let country: String?
    let zipCode, latitude, longitude: String?
    let clientType: Int
    let category: String?
    let status: Int?
    let emailID, createdAt, updatedAt: String?
    let v: Int?
    let updatedBy: String?

    enum CodingKeys: String, CodingKey {
        case clientProfilePic
        case id = "_id"
        case clientName
        case orgID = "orgId"
        case empID = "emp_id"
        case clientID = "clientId"
        case contactNumber, countryCode, address1, address2, city, state, country, zipCode, latitude, longitude, clientType, category, status
        case emailID = "emailId"
        case createdAt, updatedAt
        case v = "__v"
        case updatedBy
    }
}

//enum Country: String, Codable {
//    case afghanistan = "Afghanistan"
//    case india = "India"
//    case null = "null"
//}
//
//enum CountryState: String, Codable {
//    case badghis = "Badghis"
//    case balkh = "Balkh"
//    case karnataka = "Karnataka"
//}

