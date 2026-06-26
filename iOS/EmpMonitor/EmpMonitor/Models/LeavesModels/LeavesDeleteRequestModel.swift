//
//  LeaveDeleteRequestModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/08/24.
//

import Foundation

struct LeavesDeleteRequestModel: Codable {
    let leaveID: Int
    
    enum CodingKeys: String, CodingKey {
        case leaveID = "leave_id"
    }
    
}
