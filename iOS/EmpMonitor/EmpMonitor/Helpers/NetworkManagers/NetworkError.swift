//
//  NetworkError.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 20/06/24.
//

import Foundation


enum NetworkError: Error {
    case invalidURL
    case invalidResponse
    case invalidData
    case badRequest
    case unauthorized
    case notFound
    case serverError
    case unKnown(Error)
    
    var errorDescription: String? {
        switch self {
        case .invalidURL:
            return "The URL was invalid. Please Try Again"
        case .invalidResponse:
            return "Invalid Server Response. Please Try Again"
        case .invalidData:
            return "The User data is invalid. Please Try Again"
        case .badRequest:
            return "Bad Request"
        case .unauthorized:
            return "Unauthorized"
        case .notFound:
            return "User Not Found"
        case .serverError:
            return "Server Error"
        case .unKnown(let error):
            return error.localizedDescription
        
        }
    }
}
