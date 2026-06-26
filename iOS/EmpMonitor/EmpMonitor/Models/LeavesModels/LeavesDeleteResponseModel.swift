//
//  LeavesDeleteResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/08/24.
//

import Foundation

// MARK: - LeavesDeleteResponseModel
struct LeavesDeleteResponseModel: Codable {
    let statusCode: Int
    let body: LeavesDeleteResponseBody
}

// MARK: - Body
struct LeavesDeleteResponseBody: Codable {
    let status, message: String
    let data: LeavesDeleteResponseData
}

// MARK: - DataClass
struct LeavesDeleteResponseData: Codable {
    let code: Int
    let message: String
    let error: JSONNull?
    let data: [JSONAny]
}
