//
//  NetworkError.swift
//  EmpMonitor
//

import Foundation

enum NetworkError: Error {
    case invalidURL
    case invalidResponse
    case invalidData
    case badRequest
    case unauthorized
    case forbidden
    case notFound
    case clientError(Int, String?)
    case serverError(Int)
    case unknown(Int)
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
        case .forbidden:
            return "Forbidden"
        case .notFound:
            return "User Not Found"
        case .clientError(_, let message):
            return message ?? "Request failed"
        case .serverError(let code):
            return "Server Error (\(code))"
        case .unknown(let code):
            return "Unexpected response (\(code))"
        case .unKnown(let error):
            return error.localizedDescription
        }
    }
}
