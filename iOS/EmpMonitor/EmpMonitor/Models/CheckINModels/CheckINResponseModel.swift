//
//  File.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 12/08/24.
//

import Foundation

struct CheckINResponseModel:Codable {
    let statusCode: Int
    let body: CheckINResponseModelBody
}

// MARK: - Body
struct CheckINResponseModelBody: Codable {
    let status, message: String
    let data: CheckINResponseModelDetail?
//    let error:
}

// MARK: - BodyData
struct CheckINResponseModelDetail: Codable {
    let code: Int
    let message: String
    let error: JSONNull?
    let data: CheckINResponseModelDetailData?
}

// MARK: - DataData
struct CheckINResponseModelDetailData: Codable {
    let time: String
}
