//
//  UpdateProfileResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 03/09/24.
//

import Foundation

// MARK: - Welcome
struct UpdateProfileResponseModel: Codable {
    let statusCode: Int
    let body: UpdateProfileResponseBody
}

// MARK: - Body
struct UpdateProfileResponseBody: Codable {
    let status, message: String
    let data: UpdateProfileResponseData
}

// MARK: - DataClass
struct UpdateProfileResponseData: Codable {
    let resultData: [UpdateProfileResponseDetail]
}

// MARK: - ResultDatum
struct UpdateProfileResponseDetail: Codable {
    let id, fullName: String
    let age: String?
    let gender: String?
    let email: String
    let password: String?
    let profilePic: String?
    let location, department: String
    let status: Int
    let role, empID, orgID: String
    let address1, address2, latitude, longitude: String?
    let city, state, country, zipCode: String?
    let phoneNumber, timezone: String
    let isSuspended: Bool
    let passwordEmailSentCount, isGeoFencingOn, isMobileDeviceEnabled, isBioMetricEnabled: Int
    let isWebEnabled, frequency: Int
    let geoLogsStatus: Bool
    let snapPointsLimit, snapDurationLimit: Int
    let createdAt, updatedAt: String
    let v: Int

    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case fullName, age, gender, email, password, profilePic, location, department, status, role
        case empID = "emp_id"
        case orgID = "orgId"
        case address1, address2, latitude, longitude, city, state, country, zipCode, phoneNumber, timezone, isSuspended, passwordEmailSentCount, isGeoFencingOn, isMobileDeviceEnabled, isBioMetricEnabled, isWebEnabled, frequency, geoLogsStatus
        case snapPointsLimit = "snap_points_limit"
        case snapDurationLimit = "snap_duration_limit"
        case createdAt, updatedAt
        case v = "__v"
    }
}
