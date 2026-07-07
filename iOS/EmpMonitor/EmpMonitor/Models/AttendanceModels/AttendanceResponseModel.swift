//
//  AttendanceResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 29/07/24.
//

import Foundation

// MARK: - AttendanceResponseModel
struct AttendanceResponseModel: Codable {
    let statusCode: Int
    let body: AttendanceBody
}

// MARK: - Body
struct AttendanceBody: Codable {
    let status, message: String
    let data: [AttendanceResponseModelData]?
}

// MARK: - AttendanceResponseModelData
struct AttendanceResponseModelData: Codable {
    let id, uID: Int
    let name: String
    let status, organizationID: Int
    let locationID: Int?
    let location: String?
    let departmentID: Int
    let department, empCode: String
    let shiftID: Int?
    let shiftName: String?
    let totalCount, orgTotalCount: Int
    let geolocation: JSONNull?
    let timezone: String?
    let dateJoin: String?
    let manualClockIn: String?
    let attendanceColors: AttendanceColors?
    let includeWeeklyOffs, includeHolidays: Bool?
    let attendance: [Attendance]

    enum CodingKeys: String, CodingKey {
        case id
        case uID = "u_id"
        case name, status
        case organizationID = "organization_id"
        case locationID = "location_id"
        case location
        case departmentID = "department_id"
        case department
        case empCode = "emp_code"
        case shiftID = "shift_id"
        case shiftName = "shift_name"
        case totalCount = "total_count"
        case orgTotalCount = "org_total_count"
        case geolocation, timezone
        case dateJoin = "date_join"
        case manualClockIn = "manual_clock_in"
        case attendanceColors = "attendance_colors"
        case includeWeeklyOffs, includeHolidays, attendance
    }
}

// MARK: - Attendance
struct Attendance: Codable {
    let employeeID: Int
    let attendanceID: Int?
    let date: String
    let activeTime, officeTime, totalTime: Int
    let loggedDuration: Int?
    let status: Int
    let minHours: [MinHour]
    let isManualAttendance: Int
    let leaveType: Int?
    let leaveName: String?
    let openRequest: OpenRequest?
    let holidayName: String?
    let holidayStatus: Int
    let startTime: String?
    let endTime, customStatus, attendanceRequestStatus, checkOutDetail: String?
    let checkInDetail, overriddenBy: JSONNull?
    let dayOff: Bool
    let halfDayStatus: Int?
    let halfDayLeave: String?
    let halfDay: Int
    let openAttendanceRequest: OpenAttendanceRequest?

    enum CodingKeys: String, CodingKey {
        case employeeID = "employee_id"
        case attendanceID = "attendance_id"
        case date
        case activeTime = "active_time"
        case officeTime = "office_time"
        case totalTime = "total_time"
        case loggedDuration = "logged_duration"
        case status
        case minHours = "min_hours"
        case isManualAttendance = "is_manual_attendance"
        case leaveType = "leave_type"
        case leaveName = "leave_name"
        case openRequest = "open_request"
        case holidayName = "holiday_name"
        case holidayStatus = "holiday_status"
        case startTime = "start_time"
        case endTime = "end_time"
        case customStatus = "custom_status"
        case attendanceRequestStatus = "attendance_request_status"
        case checkOutDetail = "check_out_detail"
        case checkInDetail = "check_in_detail"
        case overriddenBy = "overridden_by"
        case dayOff = "day_off"
        case halfDayStatus = "half_day_status"
        case halfDayLeave = "half_day_leave"
        case halfDay = "half_day"
        case openAttendanceRequest = "open_attendance_request"
    }
}

//enum HolidayName: String, Codable {
//    case akileshBirthday = "Akilesh Birthday"
//    case empty = ""
//    case ganeshBirthday = "Ganesh birthday"
//}
//
//enum LeaveName: String, Codable {
//    case unpaid = "Unpaid"
//}

// MARK: - MinHour
struct MinHour: Codable {
    let name: String?
    let value, type, manualHours: Int

    enum CodingKeys: String, CodingKey {
        case name, value, type
        case manualHours = "manual_hours"
    }
}

// MARK: - OpenRequest
struct OpenRequest: Codable {
    let date: String?
    let status, leaveTypeDays, id: Int
    let leaveType: Int?
    let leaveName: String
    let employeeID, employeeLeaveType: Int?
    let dayStatus: String?
    let dayType, leaveDuration: Int?
    let startDate, endDate, apliedTo: String?

    enum CodingKeys: String, CodingKey {
        case date, status
        case leaveTypeDays = "leave_type_days"
        case id
        case leaveType = "leave_type"
        case leaveName = "leave_name"
        case employeeID = "employee_id"
        case employeeLeaveType = "employee_leave_type"
        case dayStatus = "day_status"
        case dayType = "day_type"
        case leaveDuration = "leave_duration"
        case startDate = "start_date"
        case endDate = "end_date"
        case apliedTo = "aplied_to"
    }
}

//enum Name: String, Codable {
//    case attendanceHours = "attendance_hours"
//}

// MARK: - OpenAttendanceRequest
struct OpenAttendanceRequest: Codable {
    let employeeID, attendanceID: Int
    let date, requestStatus: String

    enum CodingKeys: String, CodingKey {
        case employeeID = "employee_id"
        case attendanceID = "attendance_id"
        case date
        case requestStatus = "request_status"
    }
}

// MARK: - AttendanceColors
struct AttendanceColors: Codable {
    let leaves: String?
    let attendanceOverride: String?
    let holidays: String?
    let weekOff: String?
    let absent: String?

    enum CodingKeys: String, CodingKey {
        case leaves
        case attendanceOverride = "attendance_override"
        case holidays, weekOff, absent
    }
}

// MARK: - WeeklyDataClass
struct WeeklyDataClass: Codable {
    let mon, tue, wed, thu: Fri
    let fri, sat, sun: Fri
}

// MARK: - Fri
struct Fri: Codable {
    let status: Bool
    let time: Time
}

// MARK: - Time
struct Time: Codable {
    let start, end: String
}














