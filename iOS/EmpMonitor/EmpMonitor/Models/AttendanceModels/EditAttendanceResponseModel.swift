//
//  EditAttendanceResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 27/08/24.
//

import Foundation

// MARK: - EditAttendanceResponseModel
struct EditAttendanceResponseModel: Codable {
    let statusCode: Int
    let body: EditAttendanceResponseBody
}

// MARK: - Body
struct EditAttendanceResponseBody: Codable {
    let status, message: String
    let data: EditAttendanceResponseData
}

// MARK: - DataClass
struct EditAttendanceResponseData: Codable {
    let code: Int
    let message: String
    let error, data: JSONNull?
}
