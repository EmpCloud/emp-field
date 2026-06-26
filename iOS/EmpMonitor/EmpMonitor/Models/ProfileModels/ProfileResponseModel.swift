//
//  ProfileResponseModel.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 03/09/24.
//

import Foundation

// MARK: - ProfileResponseModel
struct ProfileResponseModel: Codable {
    let statusCode: Int
    let body: ProfileResponseBody
}

// MARK: - Body
struct ProfileResponseBody: Codable {
    let status, message: String
    let data: ProfileResponseData
}

// MARK: - DataClass
struct ProfileResponseData: Codable {
    let resultData: [ProfileResponseModelDetail]
}

// MARK: - ResultDatum
struct ProfileResponseModelDetail: Codable {
    let id, fullName: String
    let age: String?
    let  profilePic: String?
    let gender, email, address1: String?
    let address2, latitude, longitude, city: String?
    let state, country, zipCode, phoneNumber: String?

    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case fullName, age, gender, email, profilePic, address1, address2, latitude, longitude, city, state, country, zipCode, phoneNumber
    }
}
