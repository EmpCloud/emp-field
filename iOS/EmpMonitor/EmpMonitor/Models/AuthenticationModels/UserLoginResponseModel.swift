//
//  UserLoginResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/06/24.
//

import Foundation

// MARK: - UserLoginResponseModel
struct UserLoginResponseModel: Codable {
    let statusCode: Int
    let body: UserBody
}

// MARK: - Body
struct UserBody: Codable {
    let status, message: String
    let data: UserDataClass?
}

// MARK: - DataClass
struct UserDataClass: Codable {
    let userData: UserLoginData
    let accessToken: String
}

// MARK: - UserData
struct UserLoginData: Codable {
    let id, fullName: String
    let age: String?
    let gender: String?
    let email: String
    let profilePic: String?
    let location, department: String
    let status: Int
    let role, empID, orgID: String
    let address1, address2, latitude, longitude: String?
    let city, state, country, zipCode: String?
    let phoneNumber, timezone: String
    let isSuspended: Bool
    let isGeoFencingOn, isMobileDeviceEnabled, isBioMetricEnabled, isWebEnabled: Int
    let frequency: Int
    let geoLogsStatus: Bool
    let snapPointsLimit, snapDurationLimit: Int
    let createdAt, updatedAt: String
    let v: Int
    
    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case fullName, age, gender, email, profilePic, location, department, status, role
        case empID = "emp_id"
        case orgID = "orgId"
        case address1, address2, latitude, longitude, city, state, country, zipCode, phoneNumber, timezone, isSuspended, isGeoFencingOn, isMobileDeviceEnabled, isBioMetricEnabled, isWebEnabled, frequency, geoLogsStatus
        case snapPointsLimit = "snap_points_limit"
        case snapDurationLimit = "snap_duration_limit"
        case createdAt, updatedAt
        case v = "__v"
    }
}
