//
//  CheckINModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 12/08/24.
//

import Foundation

// MARK: - CheckINRequestModel
struct CheckINRequestModel: Codable {
    let time: String
    let latitude, longitude: Double
}
