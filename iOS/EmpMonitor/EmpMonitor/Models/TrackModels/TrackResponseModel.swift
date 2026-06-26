//
//  TrackResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 26/09/24.
//

import Foundation

// MARK: - TrackResponseModel
struct TrackResponseModel: Codable {
    let statusCode: Int
    let body: TrackResponseBody
}

// MARK: - Body
struct TrackResponseBody: Codable {
    let status, message: String
    let data: TrackResponseData
}

// MARK: - DataClass
struct TrackResponseData: Codable {
    let currentMode: String
    let currentFrequency, currentRadius: Int
}
