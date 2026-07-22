//
//  NotificationModel.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 10/09/24.
//

import Foundation

// MARK: - NotificationModel
struct NotificationModel: Codable {
    let statusCode: Int
    let body: NotificationBody
}

// MARK: - Body
struct NotificationBody: Codable {
    let status, message: String
    let data: NotificationData
}

// MARK: - DataClass
struct NotificationData: Codable {
    let previousTasks: [PreviousTask]
    let rescheduledTasks: [PreviousTask]

    enum CodingKeys: String, CodingKey {
        case previousTasks, rescheduledTasks
    }

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        previousTasks = (try? container.decode([PreviousTask].self, forKey: .previousTasks)) ?? []
        rescheduledTasks = (try? container.decode([PreviousTask].self, forKey: .rescheduledTasks)) ?? []
    }
}

// MARK: - PreviousTask
struct PreviousTask: Codable {
    let value: Value?
    let id, clientID, orgID, taskName: String
    let empID, date, startTime, endTime: String
    let taskDescription: String
    let taskApproveStatus: Int
    let empStartTime, empEndTime: String?
    let tagLogs: [TagLog]
    let tagID: String?
    let files: [DocFile]
    let images: [SelectedImage]
    let taskVolume: Int?
    let recurrenceID: String?
    let recurrenceDetails: NotificationRecurrenceDetails?
    let createdAt, updatedAt: String
    let v: Int
    let clientName, address1, address2, city: String?
    let longitude, latitude: String?
    let currentTagDetails: CurrentTagDetails?

    enum CodingKeys: String, CodingKey {
        case value
        case id = "_id"
        case clientID = "clientId"
        case orgID = "orgId"
        case taskName
        case empID = "emp_id"
        case date
        case startTime = "start_time"
        case endTime = "end_time"
        case taskDescription, taskApproveStatus, empStartTime, empEndTime, tagLogs, files, images, taskVolume, recurrenceDetails, createdAt, updatedAt
        case tagID = "tagId"
        case recurrenceID = "recurrenceId"
        case v = "__v"
        case clientName, address1, address2, city, longitude, latitude, currentTagDetails
    }
}

struct NotificationRecurrenceDetails: Codable {
    let taskCycle: Int?
    let startDate, endDate: String?
    let daysOfWeek: [String]?
    let id: String?

    enum CodingKeys: String, CodingKey {
        case taskCycle = "TaskCycle"
        case startDate, endDate, daysOfWeek
        case id = "_id"
    }
}

// MARK: - File
//struct File: Codable {
//    let url: String
//    let id: String
//
//    enum CodingKeys: String, CodingKey {
//        case url
//        case id = "_id"
//    }
//}

//// MARK: - Image
//struct Image: Codable {
//    let url: String
//    let description, id: String
//
//    enum CodingKeys: String, CodingKey {
//        case url, description
//        case id = "_id"
//    }
//}

//// MARK: - TagLog
//struct TagLog: Codable {
//    let tagName, time, id: String
//
//    enum CodingKeys: String, CodingKey {
//        case tagName, time
//        case id = "_id"
//    }
//}

//// MARK: - Value
//struct Value: Codable {
//    let currency: String?
//    let amount: Int
//}
