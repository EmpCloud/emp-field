//
//  TrackRequestModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 26/09/24.
//

import Foundation
import SwiftData

//MARK: TrackRequestModel
struct TrackRequestModel: Codable {
    let trackData: [TrackRequestModelData]
}
// MARK: - TrackRequestModelData
struct TrackRequestModelData: Codable, Equatable {
    let date, time: String
    let latitude, longitude: Double
    
    
    //Conformance to Equatable for checking the uniqueness based on coordinates
    static func == (lhs: TrackRequestModelData, rhs: TrackRequestModelData) -> Bool {
        return lhs.latitude == rhs.latitude && lhs.longitude == rhs.longitude
    }
}

// MARK: - SwiftData persisted offline location log
@Model
final class LocationLog {
    var timestamp: Date
    var latitude: Double
    var longitude: Double
    var isUploaded: Bool
    var retryCount: Int
    
    init(
        timestamp: Date = Date(),
        latitude: Double,
        longitude: Double,
        isUploaded: Bool = false,
        retryCount: Int = 0
    ) {
        self.timestamp = timestamp
        self.latitude = latitude
        self.longitude = longitude
        self.isUploaded = isUploaded
        self.retryCount = retryCount
    }
    
    /// Converts the stored log into the request model used by the tracking API.
    var trackRequestData: TrackRequestModelData {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        dateFormatter.timeZone = TimeZone.current
        
        let timeFormatter = DateFormatter()
        timeFormatter.dateFormat = "HH:mm:ss"
        timeFormatter.timeZone = TimeZone.current
        
        return TrackRequestModelData(
            date: dateFormatter.string(from: timestamp),
            time: timeFormatter.string(from: timestamp),
            latitude: latitude,
            longitude: longitude
        )
    }
}
