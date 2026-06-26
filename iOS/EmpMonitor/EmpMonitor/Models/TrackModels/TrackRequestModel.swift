//
//  TrackRequestModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 26/09/24.
//

import Foundation

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
