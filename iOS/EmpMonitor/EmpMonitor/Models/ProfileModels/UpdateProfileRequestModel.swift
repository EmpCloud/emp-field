//
//  UpdateProfileRequestModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 03/09/24.
//

import Foundation

// MARK: - UpdateProfileRequestModel
struct UpdateProfileRequestModel: Codable {
    let fullName: String
    let age: String?       // String? matches Android and avoids sending 0 when field is empty
    let gender: String?    // optional so blank selection sends nil rather than overwriting backend value
    let email: String
    let profilePic: String?
    let address1, address2, latitude, longitude: String
    let city, state, country, zipCode: String
    let phoneNumber: String
}
