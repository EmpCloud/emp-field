//
//  ResetPasswordRequestBody.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 01/10/24.
//

import Foundation

// MARK: - ResetPasswordRequestBody
struct ResetPasswordRequestModel: Codable {
    let email, verifyToken, newPassword: String
}
