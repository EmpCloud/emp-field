//
//  NetworkManager.swift
//  EmpMonitor
//

import Foundation

@MainActor
final class NetworkManager {
    
    static let shared = NetworkManager()
    
    // Retained for backward compatibility with existing view code.
    // Prefer the typed errors thrown by network methods for new code.
    @Published var responseMessage: String = ""
    @Published var errorMessage: String = ""
    @Published var statusCode: Int = 0
    
    private init() { }
    
    // MARK: - GET Request
    
    func getData<U: Codable>(to urlString: String, as type: U.Type, accessToken: String?) async throws -> U {
        let (data, httpStatus) = try await performRequest(
            urlString: urlString,
            method: "GET",
            body: Optional<Data>.none,
            accessToken: accessToken
        )
        return try decodeResponse(data: data, httpStatus: httpStatus, urlString: urlString)
    }
    
    // MARK: - GET Request with Query Parameters
    
    func getDataWithQuery<U: Codable>(to urlString: String, as type: U.Type, accessToken: String?, queryParameters: [String: String]?) async throws -> U {
        guard var urlComponents = URLComponents(string: urlString) else {
            print("[API] GET \(urlString) — Error: Invalid URL")
            throw NetworkError.invalidURL
        }
        
        if let params = queryParameters {
            urlComponents.queryItems = params.map { URLQueryItem(name: $0.key, value: $0.value) }
        }
        
        guard let finalURL = urlComponents.url else {
            print("[API] GET \(urlString) — Error: Invalid URL with query params")
            throw NetworkError.invalidURL
        }
        
        let (data, httpStatus) = try await performRequest(
            urlString: finalURL.absoluteString,
            method: "GET",
            body: Optional<Data>.none,
            accessToken: accessToken
        )
        return try decodeResponse(data: data, httpStatus: httpStatus, urlString: finalURL.absoluteString)
    }
    
    // MARK: - POST Request
    
    func postData<T: Codable, U: Codable>(to urlString: String, body: T, as type: U.Type, accessToken: String?) async throws -> U {
        let bodyData = try JSONEncoder().encode(body)
        let (data, httpStatus) = try await performRequest(
            urlString: urlString,
            method: "POST",
            body: bodyData,
            accessToken: accessToken
        )
        return try decodeResponse(data: data, httpStatus: httpStatus, urlString: urlString)
    }
    
    // MARK: - POST without body
    
    func postDataWithoutParameter<U: Codable>(to urlString: String, as type: U.Type, accessToken: String?) async throws -> U {
        let (data, httpStatus) = try await performRequest(
            urlString: urlString,
            method: "POST",
            body: Optional<Data>.none,
            accessToken: accessToken
        )
        return try decodeResponse(data: data, httpStatus: httpStatus, urlString: urlString)
    }
    
    // MARK: - PUT Request
    
    func putData<T: Codable, U: Codable>(to urlString: String, body: T, as type: U.Type, accessToken: String?, queryParams: String?) async throws -> U {
        guard var urlComponents = URLComponents(string: urlString) else {
            print("[API] PUT \(urlString) — Error: Invalid URL")
            throw NetworkError.invalidURL
        }
        
        if let queryParams = queryParams {
            urlComponents.queryItems = [URLQueryItem(name: "clientId", value: queryParams)]
        }
        
        guard let url = urlComponents.url else {
            print("[API] PUT \(urlString) — Error: Invalid URL after adding query params")
            throw NetworkError.invalidURL
        }
        
        let bodyData = try JSONEncoder().encode(body)
        let (data, httpStatus) = try await performRequest(
            urlString: url.absoluteString,
            method: "PUT",
            body: bodyData,
            accessToken: accessToken
        )
        return try decodeResponse(data: data, httpStatus: httpStatus, urlString: url.absoluteString)
    }
    
    // MARK: - Request Core
    
    private func performRequest(urlString: String, method: String, body: Data?, accessToken: String?) async throws -> (Data, Int) {
        guard let url = URL(string: urlString) else {
            print("[API] \(method) \(urlString) — Error: Invalid URL")
            throw NetworkError.invalidURL
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = method
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        if let token = accessToken {
            request.setValue(token, forHTTPHeaderField: "x-access-token")
        }
        if let body = body {
            request.httpBody = body
        }
        
        print("[API] \(method) \(urlString)")
        let (data, response) = try await URLSession.shared.data(for: request)
        let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
        let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
        print("[API] Response (\(httpStatus)) \(urlString)\n\(rawBody)")
        
        return (data, httpStatus)
    }
    
    // MARK: - Response Decoding & Status Handling
    
    private func decodeResponse<U: Codable>(data: Data, httpStatus: Int, urlString: String) throws -> U {
        switch httpStatus {
        case 200...299:
            do {
                let decoded = try JSONDecoder().decode(U.self, from: data)
                return decoded
            } catch {
                print("[API] Error: Decode failed for \(urlString) — \(error)")
                throw NetworkError.invalidData
            }
            
        case 401:
            let message = decodeErrorMessage(from: data)
            print("[API] 401 Unauthorized — \(message ?? "No message")")
            AuthStore.shared.clearSession()
            throw NetworkError.unauthorized
            
        case 403:
            let message = decodeErrorMessage(from: data)
            print("[API] 403 Forbidden — \(message ?? "No message")")
            throw NetworkError.forbidden
            
        case 400...499:
            let message = decodeErrorMessage(from: data)
            print("[API] \(httpStatus) Client Error — \(message ?? "No message")")
            throw NetworkError.clientError(httpStatus, message)
            
        case 500...599:
            let message = decodeErrorMessage(from: data)
            print("[API] \(httpStatus) Server Error — \(message ?? "No message")")
            throw NetworkError.serverError(httpStatus)
            
        default:
            print("[API] \(httpStatus) Unknown response")
            throw NetworkError.unknown(httpStatus)
        }
    }
    
    private func decodeErrorMessage(from data: Data) -> String? {
        return (try? JSONDecoder().decode(ErrorResponse.self, from: data))?.body.message
    }
}
