//
//  Constants.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/06/24.
//

import Foundation
import UIKit

// MARK: - Logging

/// Lightweight logging that is compiled out of Release builds.
///
/// A drop-in replacement for `print(_:)`. Using an `@autoclosure` means the
/// argument expression (and any string interpolation / value conversion it
/// performs) is never evaluated in Release — this both avoids the runtime cost
/// of building log output and prevents sensitive API payloads / tokens / PII
/// from ever being written to the device console in production.
enum AppLog {
    static func debug(_ message: @autoclosure () -> Any) {
        #if DEBUG
        print(message())
        #endif
    }
}

enum DeviceStatusDebug {
    static let tag = "[DEVICE_STATUS_MOBILE_INTEGRATION]"

    static func log(_ message: @autoclosure () -> String) {
        AppLog.debug("\(tag) \(message())")
    }
}

// MARK: - Device Status

struct DeviceStatusSnapshot: Codable {
    let batteryPercent: Int?
    let isCharging: Bool?
    let status: String?

    static var current: DeviceStatusSnapshot {
        current(status: nil)
    }

    static func current(status: String?) -> DeviceStatusSnapshot {
        UIDevice.current.isBatteryMonitoringEnabled = true

        let rawLevel = UIDevice.current.batteryLevel
        let percent: Int?
        if rawLevel < 0 {
            percent = nil
        } else {
            percent = min(100, max(0, Int((rawLevel * 100).rounded())))
        }

        let charging: Bool?
        switch UIDevice.current.batteryState {
        case .charging, .full:
            charging = true
        case .unplugged:
            charging = false
        case .unknown:
            charging = nil
        @unknown default:
            charging = nil
        }

        return DeviceStatusSnapshot(batteryPercent: percent, isCharging: charging, status: status)
    }

    var logDescription: String {
        "batteryPercent=\(batteryPercent.map(String.init) ?? "nil"), isCharging=\(isCharging.map(String.init) ?? "nil"), status=\(status ?? "nil")"
    }
}

class Constants {

    static let shared = Constants()

    private init() {}

    // MARK: - Base URL — staging for Debug builds, production for Release
    var baseURL: String {
        #if DEBUG
        return "https://staging-emp-api-m.empmonitor.com/v1"
        #else
        return "https://field-api.empmonitor.com/v1"
        #endif
    }

    // MARK: - Device status integration URL
    //
    // Used only for the device-status integration contract:
    // - POST /track/get-location with battery/charging fields
    // - PUT /user/device-status heartbeat
    var deviceStatusIntegrationBaseURL: String {
        return baseURL
    }

    // MARK: - Static web URLs (not versioned with the API)
    enum StaticURL {
        static let termsAndConditions = "https://empmonitor.com/terms-and-conditions/"
        static let privacyPolicy      = "https://empmonitor.com/privacy-policy/"
        static let videoTutorial      = "https://youtu.be/xs49wIGeqTc"
        static let qrCodeBase         = "https://service.empmonitor.com/api/v3/bio-metric/qr-code"
    }

    // MARK: - API endpoint paths (appended to baseURL)
    enum Endpoint {
        // Auth
        static let login             = "/open-user/user-login"
        static let verifyEmail       = "/open-user/verify-email"
        static let verifyOTP         = "/open-user/verifyOTP"
        static let forgotPassword    = "/open-user/forgot-password"
        static let resetPassword     = "/open-user/reset-password"
        static let trackingSettings  = "/open-user/get-tracking-settings"
        static let deviceStatus      = "/user/device-status"

        // Profile
        static let fetchProfile          = "/profile/fetchProfile"
        static let updateProfile         = "/profile/updateProfile"
        static let uploadProfileImage    = "/profile/uploadProfileImage"
        static let updateModeOfTransport = "/profile/Update-Emp-mode-of-transport"

        // Attendance
        static let attendance        = "/attendance/attendance"
        static let markAttendance    = "/attendance/mark-attendance"
        static let fetchAttendance   = "/attendance/fetch-attendance"
        static let attendanceRequest = "/attendance/attendance-request"

        // Holidays
        static let getHoliday = "/holiday/get-holiday"

        // Leaves
        static let getLeaves      = "/leaves/get-leaves"
        static let fetchLeaveType = "/leaves/fetch-leave-type"
        static let createLeave    = "/leaves/create-leave"
        static let updateLeaves   = "/leaves/update-leaves"
        static let deleteLeaves   = "/leaves/delete-leaves"

        // Clients
        static let fetchClients              = "/client/fetch"
        static let createClient              = "/client/create"
        static let updateClient              = "/client/update"
        static let clientUploadProfileImage  = "/client/clientUploadProfileImage"

        // Tasks
        static let fetchTasks       = "/task/fetch"
        static let filterTask       = "/task/filterTask"
        static let createTask       = "/task/create"
        static let updateTaskStatus = "/task/update-taskStatus"
        static let updateTask       = "/task/update"
        static let uploadTaskFiles  = "/task/uploadTask-files"
        static let getNotification  = "/task/getNotification"

        // Tags
        static let getTags = "/tags/getTags"

        // Location tracking
        static let getLocation = "/track/get-location"
    }
}
