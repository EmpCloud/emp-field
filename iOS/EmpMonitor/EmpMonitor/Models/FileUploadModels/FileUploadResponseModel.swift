//
//  File.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 29/08/24.
//

import Foundation

// MARK: - FileUploadResponseModel
struct FileUploadResponseModel: Codable {
    let code: Int
    let data: FileUploadResponseData
    let error: [JSONAny]
}

// MARK: - DataClass
struct FileUploadResponseData: Codable {
    let filesUrls: [FilesURL]
}

// MARK: - FilesURL
struct FilesURL: Codable {
    let url: String
    let message: String
}
