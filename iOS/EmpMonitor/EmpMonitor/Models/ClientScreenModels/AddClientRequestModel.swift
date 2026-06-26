//
//  AddClientRequestModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/08/24.
//

import Foundation

// MARK: - AddClientRequestModel
struct AddClientRequestModel: Codable {
    let clientName, emailID, contactNumber: String
    let clientProfilePic: String
    let category, countryCode, address1, address2: String
    let country, state, city, zipCode: String
    let latitude, longitude: Double

    enum CodingKeys: String, CodingKey {
        case clientName
        case emailID = "emailId"
        case contactNumber, clientProfilePic, category, countryCode, address1, address2, country, state, city, zipCode, latitude, longitude
    }
}
