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
        case address1, address2, latitude, longitude, city, state, country, zipCode
        case phoneNumber, timezone, isSuspended, passwordEmailSentCount
        case isGeoFencingOn, isMobileDeviceEnabled, isBioMetricEnabled, isWebEnabled, frequency, geoLogsStatus
        case snapPointsLimit = "snap_points_limit"
        case snapDurationLimit = "snap_duration_limit"
        case createdAt, updatedAt
        case v = "__v"
    }

    init(from decoder: Decoder) throws {
        let c = try decoder.container(keyedBy: CodingKeys.self)
        id          = try c.decode(String.self, forKey: .id)
        fullName    = try c.decode(String.self, forKey: .fullName)
        // age arrives as Int or String depending on API version
        if let intAge = try? c.decode(Int.self, forKey: .age) {
            age = String(intAge)
        } else {
            age = try c.decodeIfPresent(String.self, forKey: .age)
        }
        gender      = try c.decodeIfPresent(String.self, forKey: .gender)
        email       = try c.decode(String.self, forKey: .email)
        password    = try c.decodeIfPresent(String.self, forKey: .password)
        profilePic  = try c.decodeIfPresent(String.self, forKey: .profilePic)
        location    = try c.decode(String.self, forKey: .location)
        department  = try c.decode(String.self, forKey: .department)
        status      = try c.decode(Int.self, forKey: .status)
        // role is absent from the update-profile response; fall back to ""
        role        = try c.decodeIfPresent(String.self, forKey: .role) ?? ""
        empID       = try c.decode(String.self, forKey: .empID)
        orgID       = try c.decode(String.self, forKey: .orgID)
        address1    = try c.decodeIfPresent(String.self, forKey: .address1)
        address2    = try c.decodeIfPresent(String.self, forKey: .address2)
        latitude    = try c.decodeIfPresent(String.self, forKey: .latitude)
        longitude   = try c.decodeIfPresent(String.self, forKey: .longitude)
        city        = try c.decodeIfPresent(String.self, forKey: .city)
        state       = try c.decodeIfPresent(String.self, forKey: .state)
        country     = try c.decodeIfPresent(String.self, forKey: .country)
        zipCode     = try c.decodeIfPresent(String.self, forKey: .zipCode)
        phoneNumber             = try c.decode(String.self, forKey: .phoneNumber)
        timezone                = try c.decode(String.self, forKey: .timezone)
        isSuspended             = try c.decode(Bool.self,   forKey: .isSuspended)
        passwordEmailSentCount  = try c.decode(Int.self,    forKey: .passwordEmailSentCount)
        isGeoFencingOn          = try c.decode(Int.self,    forKey: .isGeoFencingOn)
        isMobileDeviceEnabled   = try c.decode(Int.self,    forKey: .isMobileDeviceEnabled)
        isBioMetricEnabled      = try c.decode(Int.self,    forKey: .isBioMetricEnabled)
        isWebEnabled            = try c.decode(Int.self,    forKey: .isWebEnabled)
        frequency               = try c.decode(Int.self,    forKey: .frequency)
        geoLogsStatus           = try c.decode(Bool.self,   forKey: .geoLogsStatus)
        snapPointsLimit         = try c.decode(Int.self,    forKey: .snapPointsLimit)
        snapDurationLimit       = try c.decode(Int.self,    forKey: .snapDurationLimit)
        createdAt   = try c.decode(String.self, forKey: .createdAt)
        updatedAt   = try c.decode(String.self, forKey: .updatedAt)
        v           = try c.decode(Int.self,    forKey: .v)
    }
}
