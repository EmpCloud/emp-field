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

    // Reused across all requests. Creating a JSONEncoder/JSONDecoder per call is
    // wasteful; these hold no per-request state and use the default configuration
    // (identical behavior to the previous per-call instances).
    private let encoder = JSONEncoder()
    private let decoder = JSONDecoder()

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
            AppLog.debug("[API] GET \(urlString) — Error: Invalid URL")
            throw NetworkError.invalidURL
        }

        if let params = queryParameters {
            urlComponents.queryItems = params.map { URLQueryItem(name: $0.key, value: $0.value) }
        }

        guard let finalURL = urlComponents.url else {
            AppLog.debug("[API] GET \(urlString) — Error: Invalid URL with query params")
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
        let bodyData = try encoder.encode(body)
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

    func putData<T: Codable, U: Codable>(to urlString: String, body: T, as type: U.Type, accessToken: String?) async throws -> U {
        let bodyData = try encoder.encode(body)
        let (data, httpStatus) = try await performRequest(
            urlString: urlString,
            method: "PUT",
            body: bodyData,
            accessToken: accessToken
        )
        return try decodeResponse(data: data, httpStatus: httpStatus, urlString: urlString)
    }
    
    func putData<T: Codable, U: Codable>(to urlString: String, body: T, as type: U.Type, accessToken: String?, queryParams: String?) async throws -> U {
        guard var urlComponents = URLComponents(string: urlString) else {
            AppLog.debug("[API] PUT \(urlString) — Error: Invalid URL")
            throw NetworkError.invalidURL
        }

        if let queryParams = queryParams {
            urlComponents.queryItems = [URLQueryItem(name: "clientId", value: queryParams)]
        }

        guard let url = urlComponents.url else {
            AppLog.debug("[API] PUT \(urlString) — Error: Invalid URL after adding query params")
            throw NetworkError.invalidURL
        }
        
        let bodyData = try encoder.encode(body)
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
            AppLog.debug("[API] \(method) \(urlString) — Error: Invalid URL")
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

        AppLog.debug("[API] \(method) \(urlString)")
        let (data, response) = try await URLSession.shared.data(for: request)
        let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
        // The raw body (which may contain tokens / PII) is only stringified in DEBUG.
        AppLog.debug("[API] Response (\(httpStatus)) \(urlString)\n\(String(data: data, encoding: .utf8) ?? "<non-utf8 data>")")

        return (data, httpStatus)
    }
    
    // MARK: - Response Decoding & Status Handling
    
    private func decodeResponse<U: Codable>(data: Data, httpStatus: Int, urlString: String) throws -> U {
        // The API sometimes returns HTTP 200 with a wrapper statusCode that indicates failure.
        // Check the wrapper statusCode before decoding the full expected response.
        if let wrapperStatus = try? decoder.decode(APIStatusWrapper.self, from: data).statusCode,
           wrapperStatus != 200 {
            let message = decodeErrorMessage(from: data) ?? "Request failed"
            AppLog.debug("[API] Wrapper status \(wrapperStatus) for \(urlString) — \(message)")

            // The backend sometimes reports an expired session with a non-401 wrapper
            // status (e.g. 400 "Session expired. Logged in on another device."), so match
            // on the message as well as the status code before forcing a logout.
            if isSessionExpired(status: wrapperStatus, message: message) {
                recordFailure(status: 401, message: message)
                AppState.shared.handleSessionExpired(message: message)
                throw NetworkError.unauthorized
            }

            recordFailure(status: wrapperStatus, message: message)
            switch wrapperStatus {
            case 401:
                AppState.shared.handleSessionExpired(message: message)
                throw NetworkError.unauthorized
            case 403:
                throw NetworkError.forbidden
            case 400...499:
                throw NetworkError.clientError(wrapperStatus, message)
            case 500...599:
                throw NetworkError.serverError(wrapperStatus)
            default:
                throw NetworkError.unknown(wrapperStatus)
            }
        }
        
        switch httpStatus {
        case 200...299:
            do {
                let decoded = try decoder.decode(U.self, from: data)
                return decoded
            } catch {
                AppLog.debug("[API] Error: Decode failed for \(urlString) — \(error)")
                recordFailure(status: httpStatus, message: NetworkError.invalidData.errorDescription)
                throw NetworkError.invalidData
            }

        case 401:
            let message = decodeErrorMessage(from: data)
            AppLog.debug("[API] 401 Unauthorized — \(message ?? "No message")")
            recordFailure(status: 401, message: message)
            AppState.shared.handleSessionExpired(message: message)
            throw NetworkError.unauthorized

        case 403:
            let message = decodeErrorMessage(from: data)
            AppLog.debug("[API] 403 Forbidden — \(message ?? "No message")")
            recordFailure(status: 403, message: message)
            throw NetworkError.forbidden

        case 400...499:
            let message = decodeErrorMessage(from: data)
            AppLog.debug("[API] \(httpStatus) Client Error — \(message ?? "No message")")
            if isSessionExpired(status: httpStatus, message: message) {
                recordFailure(status: 401, message: message)
                AppState.shared.handleSessionExpired(message: message)
                throw NetworkError.unauthorized
            }
            recordFailure(status: httpStatus, message: message)
            throw NetworkError.clientError(httpStatus, message)

        case 500...599:
            let message = decodeErrorMessage(from: data)
            AppLog.debug("[API] \(httpStatus) Server Error — \(message ?? "No message")")
            recordFailure(status: httpStatus, message: message)
            throw NetworkError.serverError(httpStatus)

        default:
            AppLog.debug("[API] \(httpStatus) Unknown response")
            recordFailure(status: httpStatus, message: nil)
            throw NetworkError.unknown(httpStatus)
        }
    }

    private func recordFailure(status: Int, message: String?) {
        statusCode = status
        responseMessage = message ?? ""
    }

    private func decodeErrorMessage(from data: Data) -> String? {
        // Decode once (the previous version decoded the same payload twice).
        let body = (try? decoder.decode(ErrorResponse.self, from: data))?.body
        if let error = body?.error, !error.isEmpty {
            return error
        }

        return body?.message
    }

    /// A response is treated as an expired session when it is a 401 or when the
    /// server message signals the session was invalidated (e.g. logged in elsewhere),
    /// regardless of the numeric status code the backend returned.
    private func isSessionExpired(status: Int, message: String?) -> Bool {
        if status == 401 { return true }
        guard let message = message?.lowercased() else { return false }
        return message.contains("session expired")
            || message.contains("logged in on another device")
    }
}

// MARK: - API Status Wrapper

private struct APIStatusWrapper: Codable {
    let statusCode: Int
}
