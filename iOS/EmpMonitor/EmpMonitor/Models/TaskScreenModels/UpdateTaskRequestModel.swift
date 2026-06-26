//
//  UpdateTaskRequestModel.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 13/09/24.
//

import Foundation

// MARK: - UpdateTaskRequestModel
struct UpdateTaskRequestModel: Codable {
    let taskID, clientID, taskName, startTime: String
    let endTime, taskDescription, date: String
    let files: [DocFile]
    let images: [SelectedImage]
    let value: TaskValue
    let taskVolume: Int
    let tagLogs: [TaskTagLog]

    enum CodingKeys: String, CodingKey {
        case taskID = "taskId"
        case clientID = "clientId"
        case taskName
        case startTime = "start_time"
        case endTime = "end_time"
        case taskDescription, date, files, images, value, taskVolume, tagLogs
    }
}

// MARK: - Value
struct TaskValue: Codable {
    let amount: Int?
    let currency: String?
}

//// MARK: - File
//struct File: Codable {
//    let url: String
//}

//// MARK: - TagLog
//struct TagLog: Codable {
//    let tagName, time: String
//}

//// MARK: - Value
//struct Value: Codable {
//    let currency: String
//    let amount: Int
//}
