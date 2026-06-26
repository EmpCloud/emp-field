//
//  ProfileUploadResponseModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/08/24.
//

import Foundation

// MARK: - ProfileUploadResponseModel
struct ProfileUploadResponseModel: Codable {
    let statusCode: Int
    let body: ProfileUploadResponseBody
}

// MARK: - Body
struct ProfileUploadResponseBody: Codable {
    let status, message: String
    let data: ProfileUploadResponseData
}

// MARK: - DataClass
struct ProfileUploadResponseData: Codable {
    let profileURL: String
}
