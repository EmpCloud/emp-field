//
//  ErrorResponse.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/07/24.
//

import Foundation

// MARK: - ErrorResponse
struct ErrorResponse: Codable {
    let statusCode: Int
    let body: ErrorBody
}

// MARK: - Body
struct ErrorBody: Codable {
    let status, message: String
    let error: String?
}
