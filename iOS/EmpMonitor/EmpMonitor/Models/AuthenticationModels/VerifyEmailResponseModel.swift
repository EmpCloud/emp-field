//
//  VerifyEmailResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 27/09/24.
//

import Foundation

// MARK: - Welcome
struct VerifyEmailResponseModel: Codable {
    let statusCode: Int
    let body: VerifyEmailResponseBody
}

// MARK: - Body
struct VerifyEmailResponseBody: Codable {
    let status, message: String
}
