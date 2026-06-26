//
//  NetworkManager.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 20/06/24.
//

import Foundation

class NetworkManager {
    
    static let shared = NetworkManager()
    
    @Published var responseMessage: String = ""
    @Published var errorMessage: String = ""
    @Published var statusCode: Int = 0
    
    private init() { }
    
    //MARK: GET Request
    func getData<U: Codable>(to urlString: String, as type: U.Type, accessToken: String?) async throws -> U {

        guard let url = URL(string: urlString) else {
            print("[API] GET \(urlString) — Error: Invalid URL")
            throw NetworkError.invalidURL
        }

        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        if let token = accessToken {
            request.setValue("\(token)", forHTTPHeaderField: "x-access-token")
        }

        request.setValue("application/json", forHTTPHeaderField: "Content-Type")

        print("[API] GET \(urlString)")
        let (data, response) = try await URLSession.shared.data(for: request)
        let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
        let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
        print("[API] Response (\(httpStatus)) \(urlString)\n\(rawBody)")

        guard httpStatus == 200 else {
            if httpStatus == 400 {
                guard let networkResponse = try? JSONDecoder().decode(ErrorResponse.self, from: data) else {
                    print("[API] Error: Invalid Data Error in getData Error Response")
                    throw NetworkError.invalidData
                }
                NetworkManager.shared.responseMessage = networkResponse.body.message
                NetworkManager.shared.statusCode = networkResponse.statusCode
            }
            if httpStatus == 401 {
                UserDefaults.standard.removeObject(forKey: "loggedInUser")
                guard let networkResponse = try? JSONDecoder().decode(ErrorResponse.self, from: data) else {
                    print("[API] Error: Invalid Data Error in getData Error Response")
                    throw NetworkError.invalidData
                }
                NetworkManager.shared.responseMessage = networkResponse.body.message
                NetworkManager.shared.statusCode = networkResponse.statusCode
            }
            throw NetworkError.invalidResponse
        }

        do {
            let decodeData = try JSONDecoder().decode(U.self, from: data)
            return decodeData
        } catch {
            print("[API] Error: Decode failed in getData — \(error)")
            throw NetworkError.invalidData
        }
    }
    
    //MARK: GET Request with Query Parameters
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

        var request = URLRequest(url: finalURL)
        request.httpMethod = "GET"

        if let token = accessToken {
            request.setValue(token, forHTTPHeaderField: "x-access-token")
        }
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")

        print("[API] GET \(finalURL.absoluteString)")
        let (data, response) = try await URLSession.shared.data(for: request)
        let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
        let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
        print("[API] Response (\(httpStatus)) \(finalURL.absoluteString)\n\(rawBody)")

        guard httpStatus == 200 else {
            if httpStatus == 400 {
                guard let networkResponse = try? JSONDecoder().decode(ErrorResponse.self, from: data) else {
                    print("[API] Error: Invalid Data in getDataWithQuery Error Response")
                    throw NetworkError.invalidData
                }
                NetworkManager.shared.responseMessage = networkResponse.body.message ?? ""
                NetworkManager.shared.statusCode = networkResponse.statusCode
            }
            throw NetworkError.invalidResponse
        }

        do {
            let decodeData = try JSONDecoder().decode(U.self, from: data)
            return decodeData
        } catch {
            print("[API] Error: Decode failed in getDataWithQuery — \(error)")
            throw NetworkError.invalidData
        }
    }
    
    //MARK: POST Request
    //Generic
    func postData<T: Codable, U: Codable>(to urlString: String, body: T, as type: U.Type, accessToken: String?) async throws -> U {

        guard let url = URL(string: urlString) else {
            print("[API] POST \(urlString) — Error: Invalid URL")
            throw NetworkError.invalidURL
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"

        if let token = accessToken {
            request.setValue("\(token)", forHTTPHeaderField: "x-access-token")
        }
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = try JSONEncoder().encode(body)

        print("[API] POST \(urlString)")
        let (data, response) = try await URLSession.shared.data(for: request)
        let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
        let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
        print("[API] Response (\(httpStatus)) \(urlString)\n\(rawBody)")

        guard httpStatus == 200 else {
            if httpStatus == 400 {
                guard let networkResponse = try? JSONDecoder().decode(ErrorResponse.self, from: data) else {
                    print("[API] Error: Invalid Data in postData Error Response")
                    throw NetworkError.invalidData
                }
                NetworkManager.shared.responseMessage = networkResponse.body.message
                NetworkManager.shared.statusCode = networkResponse.statusCode
            }
            throw NetworkError.invalidResponse
        }

        do {
            let decodeData = try JSONDecoder().decode(U.self, from: data)
            return decodeData
        } catch {
            print("[API] Error: Decode failed in postData — \(error)")
            throw NetworkError.invalidData
        }
    }
    
    //POST without body
    func postDataWithoutParameter<U: Codable>(to urlString: String, as type: U.Type, accessToken: String?) async throws -> U {

        guard let url = URL(string: urlString) else {
            print("[API] POST \(urlString) — Error: Invalid URL")
            throw NetworkError.invalidURL
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"

        if let token = accessToken {
            request.setValue("\(token)", forHTTPHeaderField: "x-access-token")
        }
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")

        print("[API] POST \(urlString)")
        let (data, response) = try await URLSession.shared.data(for: request)
        let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
        let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
        print("[API] Response (\(httpStatus)) \(urlString)\n\(rawBody)")

        guard httpStatus == 200 else {
            if httpStatus == 400 {
                guard let networkResponse = try? JSONDecoder().decode(ErrorResponse.self, from: data) else {
                    print("[API] Error: Invalid Data in postDataWithoutParameter Error Response")
                    throw NetworkError.invalidData
                }
                NetworkManager.shared.responseMessage = networkResponse.body.message
                NetworkManager.shared.statusCode = networkResponse.statusCode
            }
            throw NetworkError.invalidResponse
        }

        do {
            let decodeData = try JSONDecoder().decode(U.self, from: data)
            return decodeData
        } catch {
            print("[API] Error: Decode failed in postDataWithoutParameter — \(error)")
            throw NetworkError.invalidData
        }
    }
    
    
    //MARK: PUT Request
    //Generic
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

        var request = URLRequest(url: url)
        request.httpMethod = "PUT"

        if let token = accessToken {
            request.setValue("\(token)", forHTTPHeaderField: "x-access-token")
        }
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = try JSONEncoder().encode(body)

        print("[API] PUT \(url.absoluteString)")
        let (data, response) = try await URLSession.shared.data(for: request)
        let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
        let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
        print("[API] Response (\(httpStatus)) \(url.absoluteString)\n\(rawBody)")

        guard httpStatus == 200 else {
            if httpStatus == 400 {
                guard let networkResponse = try? JSONDecoder().decode(ErrorResponse.self, from: data) else {
                    print("[API] Error: Invalid Data in putData Error Response")
                    throw NetworkError.invalidData
                }
                NetworkManager.shared.responseMessage = networkResponse.body.message
                NetworkManager.shared.statusCode = networkResponse.statusCode
            }
            throw NetworkError.invalidResponse
        }

        do {
            let decodeData = try JSONDecoder().decode(U.self, from: data)
            return decodeData
        } catch {
            print("[API] Error: Decode failed in putData — \(error)")
            throw NetworkError.invalidData
        }
    }
    
}
