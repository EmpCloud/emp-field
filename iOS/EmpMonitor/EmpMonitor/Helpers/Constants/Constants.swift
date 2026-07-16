//
//  Constants.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/06/24.
//

import Foundation

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
