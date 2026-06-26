//
//  ResetPasswordResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 01/10/24.
//

import Foundation

struct ResetPasswordResponseModel: Codable {
    let statusCode: Int
    let body: ResetPasswordResponseBody
}

// MARK: - Body
struct ResetPasswordResponseBody: Codable {
    let status, message: String
}
