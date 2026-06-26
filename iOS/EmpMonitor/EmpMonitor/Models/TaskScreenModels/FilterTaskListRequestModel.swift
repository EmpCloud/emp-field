//
//  FilterTaskListRequestModel.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 05/09/24.
//

import Foundation

// MARK: - FilterTaskListRequestModel
struct FilterTaskListRequestModel: Codable {
    let date: String
    let status: Int
}
