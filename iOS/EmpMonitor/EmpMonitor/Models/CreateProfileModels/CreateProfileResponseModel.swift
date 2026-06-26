//
//  CreateProfileResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 15/07/24.
//

import Foundation

// MARK: - CreateProfileResponseModel
struct CreateProfileResponseModel: Codable {
    let statusCode: Int
    let body: CreateProfileReponseBody
}

// MARK: - Body
struct CreateProfileReponseBody: Codable {
    let status, message: String
    let data: CreateProfileReponseData
}

// MARK: - DataClass
struct CreateProfileReponseData: Codable {
    let resultData: [CreateProfileReponseDetailData]
}

// MARK: - ResultData
struct CreateProfileReponseDetailData: Codable {
    let id, fullName: String
    let age: Int
    let gender, email, password: String
    let profilePic: String?
    let location, department: String
    let status: Int
    let role, empID, orgID, address1: String
    let address2, latitude, longitude, city: String
    let state, country, zipCode, phoneNumber: String
    let timezone: String
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

