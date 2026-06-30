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
    let profilePic: String?
    let gender, email, address1: String?
    let address2, latitude, longitude, city: String?
    let state, country, zipCode, phoneNumber: String?

    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case fullName, age, gender, email, profilePic, address1, address2, latitude, longitude, city, state, country, zipCode, phoneNumber
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        id = try container.decode(String.self, forKey: .id)
        fullName = try container.decode(String.self, forKey: .fullName)
        
        // The API returns age as either an Int or a String depending on the endpoint/version.
        if let intAge = try? container.decode(Int.self, forKey: .age) {
            age = String(intAge)
        } else {
            age = try container.decodeIfPresent(String.self, forKey: .age)
        }
        
        profilePic = try container.decodeIfPresent(String.self, forKey: .profilePic)
        gender = try container.decodeIfPresent(String.self, forKey: .gender)
        email = try container.decodeIfPresent(String.self, forKey: .email)
        address1 = try container.decodeIfPresent(String.self, forKey: .address1)
        address2 = try container.decodeIfPresent(String.self, forKey: .address2)
        latitude = try container.decodeIfPresent(String.self, forKey: .latitude)
        longitude = try container.decodeIfPresent(String.self, forKey: .longitude)
        city = try container.decodeIfPresent(String.self, forKey: .city)
        state = try container.decodeIfPresent(String.self, forKey: .state)
        country = try container.decodeIfPresent(String.self, forKey: .country)
        zipCode = try container.decodeIfPresent(String.self, forKey: .zipCode)
        phoneNumber = try container.decodeIfPresent(String.self, forKey: .phoneNumber)
    }
}
