//
//  HomeScreenResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 12/08/24.
//

import Foundation

// MARK: - HomeScreenResponseModel
struct HomeScreenResponseModel: Codable {
    let statusCode: Int
    let body: HomeScreenResponseBody
}

// MARK: - Body
struct HomeScreenResponseBody: Codable {
    let status, message: String
    let data: HomeScreenResponseDetail?
}

// MARK: - BodyData
struct HomeScreenResponseDetail: Codable {
    let orglatitude: String?
    let orglongitude: String?
    let orgRadius, isGeoFencingOn, isMobileDeviceEnabled, isBioMetricEnabled: Int
    let isWebEnabled: Int
    let geoLogStatus: Bool
    let currentFrequency, currentRadius: Int
    let currentMode, yesterdaytask, yesterdayHrs: String
    let yesterdayDist: StringOrDouble  // to handle both String and Double type
    let data: CheckINData
}

// MARK: - CheckINData
struct CheckINData: Codable {
    let data: CheckINDetailData
}

// MARK: - CheckINDetailData
struct CheckINDetailData: Codable {
    let checkIn: String?
    let checkOut: String?
    
    enum CodingKeys: String, CodingKey {
        case checkIn = "check_in"
        case checkOut = "check_out"
    }
}

//MARK: StringORInt to handle String and  Int
enum StringOrDouble: Codable {
    case string(String)
    case double(Double)
    
    init(from decoder: any Decoder) throws {
        let container = try decoder.singleValueContainer()
        
        if let doubleValue = try? container.decode(Double.self) {
            self = .double(doubleValue)
        }else if let stringValue = try? container.decode(String.self) {
            self = .string(stringValue)
        }else {
            throw DecodingError.typeMismatch(StringOrDouble.self, DecodingError.Context(codingPath: decoder.codingPath, debugDescription: "Expected String or Int"))
        }
    }
    
    func encode(to encoder: any Encoder) throws {
        var container = encoder.singleValueContainer()
        
        switch self {
        case .double(let value):
            try container.encode(value)
        case .string(let value):
            try container.encode(value)
        }
    }
    
    //Helper to return the value as String for display purposes
    var value: String {
        switch self {
        case .double(let doubleValue):
            return String(doubleValue)
        case .string(let stringValue):
            return stringValue
        }
    }
}

