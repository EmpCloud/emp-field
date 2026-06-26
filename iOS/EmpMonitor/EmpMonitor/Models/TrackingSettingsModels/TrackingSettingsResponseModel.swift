//
//  TrackingSettingsResponseModel.swift
//  EmpMonitor
//

import Foundation

struct TrackingSettingsResponseModel: Codable {
    let statusCode: Int
    let body: TrackingSettingsBody
}

struct TrackingSettingsBody: Codable {
    let status: String
    let message: String
    let data: TrackingSettingsData
}

struct TrackingSettingsData: Codable {
    let latitude: String?
    let longitude: String?
    let orgRadius: Int
    let isBioMetricEnabled: Int
    let isWebEnabled: Int
    let geoLogStatus: Bool
    let currentFrequency: Int
    let currentRadius: Int
    let currentMode: String
    let isMobileDeviceEnabled: Int
    let autoCheckInByMobile: Int
    let isGeoFencingOn: Int
    let autoCheckInByGeoFencing: Int
}
