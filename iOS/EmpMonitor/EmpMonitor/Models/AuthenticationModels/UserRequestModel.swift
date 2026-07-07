//
//  UserRequestModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/06/24.
//

import Foundation

struct UserRequestModel: Codable {
    let userMail: String
    let password: String
    let deviceId: String
}
