//
//  Constants.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/06/24.
//

import Foundation
import UIKit

// MARK: - Logging

/// General debug output is Debug-only; API responses also print in Release for testing.
enum AppLog {
    static func debug(_ message: @autoclosure () -> Any) {
        #if DEBUG
        print(message())
        #endif
    }

    static func apiResponse(_ data: Data, response: URLResponse, request: URLRequest) {
        let status = (response as? HTTPURLResponse)?.statusCode ?? 0
        var endpoint = request.url.flatMap { URLComponents(url: $0, resolvingAgainstBaseURL: false) }
        endpoint?.query = nil
        endpoint?.fragment = nil
        endpoint?.user = nil
        endpoint?.password = nil

        let body: String
        if let json = try? JSONSerialization.jsonObject(with: data, options: .fragmentsAllowed),
           let redactedData = try? JSONSerialization.data(withJSONObject: redactingCredentials(json), options: [.prettyPrinted, .sortedKeys, .fragmentsAllowed]),
           let text = String(data: redactedData, encoding: .utf8) {
            body = text
        } else {
            body = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
        }
        print("[API] \(request.httpMethod ?? "GET") \(endpoint?.string ?? "<unknown URL>") — Response (\(status))\n\(body)")
    }

    // shortcut: Masks JSON credential fields; review free-text bodies and new credential names before distribution.
    private static func redactingCredentials(_ value: Any) -> Any {
        if let object = value as? [String: Any] {
            return object.reduce(into: [String: Any]()) { result, entry in
                let key = entry.key.lowercased().replacingOccurrences(of: "_", with: "").replacingOccurrences(of: "-", with: "")
                let isCredential = key.contains("token") || key.contains("password") || key.contains("secret")
                    || ["authorization", "apikey", "otp", "pin", "passcode", "cookie"].contains(key)
                result[entry.key] = isCredential ? "<redacted>" : redactingCredentials(entry.value)
            }
        }
        if let array = value as? [Any] {
            return array.map { redactingCredentials($0) }
        }
        return value
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

    var socketServerOrigin: String {
        baseURL.replacingOccurrences(of: "/v1", with: "")
    }

    var locationSocketURL: URL? {
        guard var components = URLComponents(string: socketServerOrigin) else { return nil }
        components.scheme = components.scheme == "https" ? "wss" : "ws"
        components.path = "/socket.io/"
        components.queryItems = [
            URLQueryItem(name: "EIO", value: "4"),
            URLQueryItem(name: "transport", value: "websocket")
        ]
        return components.url
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
