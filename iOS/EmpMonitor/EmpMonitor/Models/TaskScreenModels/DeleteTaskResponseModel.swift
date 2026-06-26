//
//  DeleteTaskResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 06/09/24.
//

import Foundation

// MARK: - DeleteTaskResponseModel
struct DeleteTaskResponseModel: Codable {
    let statusCode: Int
    let body: DeleteTaskResponseBody
}

// MARK: - Body
struct DeleteTaskResponseBody: Codable {
    let status, message: String
}
