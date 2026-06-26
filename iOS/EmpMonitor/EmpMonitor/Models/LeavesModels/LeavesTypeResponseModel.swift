//
//  LeavesTypeModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 14/08/24.
//

import Foundation

// MARK: - Welcome
struct LeavesTypeResponseModel: Codable {
    let statusCode: Int
    let body: LeavesTypeResponseBody
}

// MARK: - Body
struct LeavesTypeResponseBody: Codable {
    let status, message: String
    let data: LeavesTypeResponseData
}

// MARK: - DataClass
struct LeavesTypeResponseData: Codable {
    let code: Int
    let message: String
    let error: JSONNull?
    let data: [LeavesTypeResponseDetail]
}

// MARK: - Datum
struct LeavesTypeResponseDetail: Codable {
    let id: Int
    let name: String
    let duration: Int?
    let numberOfDays, carryForward: Int

    enum CodingKeys: String, CodingKey {
        case id, name, duration
        case numberOfDays = "number_of_days"
        case carryForward = "carry_forward"
    }
}
