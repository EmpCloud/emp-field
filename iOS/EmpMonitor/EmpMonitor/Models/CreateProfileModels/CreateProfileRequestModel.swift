//
//  CreateProfileRequestModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import Foundation

struct CreateProfileRequestModel: Codable {
    let fullName: String
    let age: Int
    let gender, email: String
    let profilePic: String?
    let address1, address2, latitude, longitude: String
    let city, state, country, zipCode: String
    let phoneNumber: String
}
