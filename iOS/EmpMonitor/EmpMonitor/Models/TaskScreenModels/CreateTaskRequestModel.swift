//
//  CreateTaskRequestModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 27/08/24.
//

import Foundation

// MARK: - CreateTaskRequestModel
struct CreateTaskRequestModel: Codable {
    let clientID, taskName, startTime, endTime: String
    let date, taskDescription: String
    let files: [DocFile]
    let images: [SelectedImage]
    let value: Value
    let taskVolume: Int
    let tagLogs: [TagLog]

    enum CodingKeys: String, CodingKey {
        case clientID = "clientId"
        case taskName
        case startTime = "start_time"
        case endTime = "end_time"
        case date, taskDescription, files, images, value, taskVolume, tagLogs
    }
}

// MARK: - File
struct DocFile: Codable {
    let url: String
    let id: String?

    enum CodingKeys: String, CodingKey {
        case url
        case id = "_id"
    }
}
//struct DocFile: Codable {
//    let url: String
//}

// MARK: - Image
struct SelectedImage: Codable {
    let url: String
    let description: String?
    let id: String?

    enum CodingKeys: String, CodingKey {
        case url, description
        case id = "_id"
    }
}


// MARK: - TagLog
struct TagLog: Codable {
    let tagName, time: String?
    let id: String?
    
    enum CodingKeys: String, CodingKey {
        case tagName, time
        case id = "_id"
    }
}

// MARK: - Value
struct Value: Codable {
    let currency: String?
    let amount: Int?
    let convertedAmountInUSD: Double?
}
