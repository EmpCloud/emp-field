//
//  UpdateTaskRequestModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 09/09/24.
//

import Foundation

struct UpdateTaskStatusRequestModel: Codable {
    let taskID: String
    let status: Int
    let currentDateTime, latitude, longitude: String
    let value: TaskValue
    let taskVolume: Int
    let tagLogs: [TaskTagLog]

    enum CodingKeys: String, CodingKey {
        case taskID = "taskId"
        case status, currentDateTime, latitude, longitude, value, taskVolume, tagLogs
    }
}

// MARK: - TagLog
struct TaskTagLog: Codable {
    let tagName: String?
    let time: String?
    let id: String?
    
    enum CodingKeys: String, CodingKey {
        case tagName, time
        case id = "_id"
    }
}
