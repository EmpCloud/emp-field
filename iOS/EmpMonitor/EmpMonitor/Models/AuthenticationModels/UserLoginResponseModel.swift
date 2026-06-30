//
//  UserLoginResponseModel.swift
//  EmpMonitor
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
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        id = try container.decode(String.self, forKey: .id)
        fullName = try container.decode(String.self, forKey: .fullName)
        
        // The API returns age as either an Int or a String depending on the endpoint/version.
        if let intAge = try? container.decode(Int.self, forKey: .age) {
            age = String(intAge)
        } else {
            age = try container.decodeIfPresent(String.self, forKey: .age)
        }
        
        gender = try container.decodeIfPresent(String.self, forKey: .gender)
        email = try container.decode(String.self, forKey: .email)
        profilePic = try container.decodeIfPresent(String.self, forKey: .profilePic)
        location = try container.decode(String.self, forKey: .location)
        department = try container.decode(String.self, forKey: .department)
        status = try container.decode(Int.self, forKey: .status)
        role = try container.decode(String.self, forKey: .role)
        empID = try container.decode(String.self, forKey: .empID)
        orgID = try container.decode(String.self, forKey: .orgID)
        address1 = try container.decodeIfPresent(String.self, forKey: .address1)
        address2 = try container.decodeIfPresent(String.self, forKey: .address2)
        latitude = try container.decodeIfPresent(String.self, forKey: .latitude)
        longitude = try container.decodeIfPresent(String.self, forKey: .longitude)
        city = try container.decodeIfPresent(String.self, forKey: .city)
        state = try container.decodeIfPresent(String.self, forKey: .state)
        country = try container.decodeIfPresent(String.self, forKey: .country)
        zipCode = try container.decodeIfPresent(String.self, forKey: .zipCode)
        phoneNumber = try container.decode(String.self, forKey: .phoneNumber)
        timezone = try container.decode(String.self, forKey: .timezone)
        isSuspended = try container.decode(Bool.self, forKey: .isSuspended)
        isGeoFencingOn = try container.decode(Int.self, forKey: .isGeoFencingOn)
        isMobileDeviceEnabled = try container.decode(Int.self, forKey: .isMobileDeviceEnabled)
        isBioMetricEnabled = try container.decode(Int.self, forKey: .isBioMetricEnabled)
        isWebEnabled = try container.decode(Int.self, forKey: .isWebEnabled)
        frequency = try container.decode(Int.self, forKey: .frequency)
        geoLogsStatus = try container.decode(Bool.self, forKey: .geoLogsStatus)
        snapPointsLimit = try container.decode(Int.self, forKey: .snapPointsLimit)
        snapDurationLimit = try container.decode(Int.self, forKey: .snapDurationLimit)
        createdAt = try container.decode(String.self, forKey: .createdAt)
        updatedAt = try container.decode(String.self, forKey: .updatedAt)
        v = try container.decode(Int.self, forKey: .v)
    }
}
