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

    enum CodingKeys: String, CodingKey {
        case statusCode
        case body
    }

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        if let statusCode = try container.decodeIfPresent(Int.self, forKey: .statusCode),
           let body = try container.decodeIfPresent(TrackResponseBody.self, forKey: .body) {
            self.statusCode = statusCode
            self.body = body
            return
        }

        self.statusCode = 200
        self.body = try TrackResponseBody(from: decoder)
    }
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

    enum CodingKeys: String, CodingKey {
        case currentMode
        case currentFrequency
        case currentRadius
    }

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        currentMode = try container.decodeIfPresent(String.self, forKey: .currentMode) ?? ""
        currentFrequency = try container.decodeIfPresent(Int.self, forKey: .currentFrequency) ?? 0
        currentRadius = try container.decodeIfPresent(Int.self, forKey: .currentRadius) ?? 0
    }
}
