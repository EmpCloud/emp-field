//
//  DeleteTaskRequestModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 06/09/24.
//

import Foundation

// MARK: - DeleteTaskRequestModel
struct DeleteTaskRequestModel: Codable {
    let taskID: String
    let status: Int
    let currentDateTime, latitude, longitude: String
    let value: FilterTaskValue
    let taskVolume: Int
    let tagLogs: [TaskTagLog]

    enum CodingKeys: String, CodingKey {
        case taskID = "taskId"
        case status, currentDateTime, latitude, longitude, value, taskVolume, tagLogs
    }
}


//// MARK: - Value
//struct DeleteValue: Codable {
//    let currency: String
//    let amount: Int
//}
