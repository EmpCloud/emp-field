//
//  HolidaysResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/07/24.
//

import Foundation

// MARK: - HolidaysResponseModel
struct HolidaysResponseModel: Codable {
    let statusCode: Int
    let body: HolidaysResponseBody
}

// MARK: - Body
struct HolidaysResponseBody: Codable {
    let status, message: String
    let data: [HolidaysResponseData]?
}

// MARK: - HolidaysResponseData
struct HolidaysResponseData: Codable {
    let id: Int
    let holidayName, holidayDate: String
    let organizationID: Int

    enum CodingKeys: String, CodingKey {
        case id
        case holidayName = "holiday_name"
        case holidayDate = "holiday_date"
        case organizationID = "organization_id"
    }
}
