//
//  FileUploadService.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 28/08/24.
//

import Foundation

//struct FileUploadResponse: Codable {
//    
//}

class FileUploadService {
    
    static let shared = FileUploadService()
    
    func uploadFile(to urlString: String, files: [URL], accessToken: String) async throws -> FileUploadResponseModel {
        
        guard let url = URL(string: urlString) else {
            AppLog.debug("uploadfile url error")
            throw NetworkError.invalidURL
        }
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        
        let boundary = "Boundary-\(UUID().uuidString)"
//        request.setValue("multipart/form-data: boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        
        // Set the timeout interval to a higher value, e.g., 120 seconds
        request.timeoutInterval = 120
        
        // Set the headers
        request.setValue("application/json", forHTTPHeaderField: "accept")
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        
        request.setValue(accessToken, forHTTPHeaderField: "x-access-token")
        
        //Generate the body with files
        let body = try createMultipartBody(with: files, boundary: boundary)
        request.httpBody = body
        
        AppLog.debug("[API] POST \(urlString)")
        let(data, response) = try await URLSession.shared.data(for: request)
        let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
        let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
        AppLog.debug("[API] Response (\(httpStatus)) \(urlString)\n\(rawBody)")

        try await validateUploadResponse(data: data, response: response, urlString: urlString)

        let uploadResponse = try JSONDecoder().decode(FileUploadResponseModel.self, from: data)
        return uploadResponse
    }

    func uploadImage(to urlString: String, files: [URL], accessToken: String) async throws -> FileUploadResponseModel {
        
        guard let url = URL(string: urlString) else {
            AppLog.debug("uploadfile url error")
            throw NetworkError.invalidURL
        }
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        
        let boundary = "Boundary-\(UUID().uuidString)"
//        request.setValue("multipart/form-data: boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        
        // Set the timeout interval to a higher value, e.g., 120 seconds
        request.timeoutInterval = 120
        
        // Set the headers
        request.setValue("application/json", forHTTPHeaderField: "accept")
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        
        request.setValue(accessToken, forHTTPHeaderField: "x-access-token")
        
        //Generate the body with files
        let body = try createMultipartBodyImage(with: files, boundary: boundary)
        request.httpBody = body
        
        AppLog.debug("[API] POST \(urlString)")
        let(data, response) = try await URLSession.shared.data(for: request)
        let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
        let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
        AppLog.debug("[API] Response (\(httpStatus)) \(urlString)\n\(rawBody)")

        try await validateUploadResponse(data: data, response: response, urlString: urlString)

        let uploadResponse = try JSONDecoder().decode(FileUploadResponseModel.self, from: data)
        return uploadResponse
    }

    func uploadProfileImage(to urlString: String, files: [URL], accessToken: String) async throws -> ProfileUploadResponseModel {
        
        guard let url = URL(string: urlString) else {
            AppLog.debug("uploadfile url error")
            throw NetworkError.invalidURL
        }
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        
        let boundary = "Boundary-\(UUID().uuidString)"
//        request.setValue("multipart/form-data: boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        
        // Set the timeout interval to a higher value, e.g., 120 seconds
        request.timeoutInterval = 120
        
        // Set the headers
        request.setValue("application/json", forHTTPHeaderField: "accept")
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        
        request.setValue(accessToken, forHTTPHeaderField: "x-access-token")
        
        //Generate the body with files
        let body = try createMultipartBodyImage(with: files, boundary: boundary)
        request.httpBody = body
        
        AppLog.debug("[API] POST \(urlString)")
        let(data, response) = try await URLSession.shared.data(for: request)
        let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
        let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
        AppLog.debug("[API] Response (\(httpStatus)) \(urlString)\n\(rawBody)")

        try await validateUploadResponse(data: data, response: response, urlString: urlString)

        let uploadResponse = try JSONDecoder().decode(ProfileUploadResponseModel.self, from: data)
        return uploadResponse
    }

    private func validateUploadResponse(data: Data, response: URLResponse, urlString: String) async throws {
        guard let httpResponse = response as? HTTPURLResponse else {
            await recordFailure(status: 0, message: NetworkError.invalidResponse.errorDescription)
            throw NetworkError.invalidResponse
        }

        let httpStatus = httpResponse.statusCode
        let message = decodeErrorMessage(from: data)

        if let wrapperStatus = try? JSONDecoder().decode(UploadStatusWrapper.self, from: data).statusCode,
           wrapperStatus != 200 {
            if isSessionExpired(status: wrapperStatus, message: message) {
                await expireSession(message: message)
                throw NetworkError.unauthorized
            }

            await recordFailure(status: wrapperStatus, message: message)
            switch wrapperStatus {
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

        guard (200...299).contains(httpStatus) else {
            if isSessionExpired(status: httpStatus, message: message) {
                await expireSession(message: message)
                throw NetworkError.unauthorized
            }

            await recordFailure(status: httpStatus, message: message)
            switch httpStatus {
            case 403:
                throw NetworkError.forbidden
            case 400...499:
                throw NetworkError.clientError(httpStatus, message)
            case 500...599:
                throw NetworkError.serverError(httpStatus)
            default:
                throw NetworkError.unknown(httpStatus)
            }
        }
    }

    private func recordFailure(status: Int, message: String?) async {
        await MainActor.run {
            NetworkManager.shared.statusCode = status
            NetworkManager.shared.responseMessage = message ?? ""
        }
    }

    private func expireSession(message: String?) async {
        await MainActor.run {
            NetworkManager.shared.statusCode = 401
            NetworkManager.shared.responseMessage = message ?? ""
            AppState.shared.handleSessionExpired(message: message)
        }
    }

    private func decodeErrorMessage(from data: Data) -> String? {
        let body = (try? JSONDecoder().decode(ErrorResponse.self, from: data))?.body
        return body?.message ?? body?.error
    }

    private func isSessionExpired(status: Int, message: String?) -> Bool {
        if status == 401 { return true }
        guard let message = message?.lowercased() else { return false }
        return message.contains("session expired")
            || message.contains("logged in on another device")
    }
    
    private func createMultipartBody(with files: [URL], boundary: String) throws -> Data {
           var body = Data()
           let boundaryPrefix = "--\(boundary)\r\n"
           
//        AppLog.debug("Files: \(files)")
        
        for fileURL in files {
            if fileURL.startAccessingSecurityScopedResource() {
                
                defer { fileURL.stopAccessingSecurityScopedResource() }
                
                let fileName = fileURL.lastPathComponent
                let mimeType = mimeType(for: fileURL)
                
                body.append(boundaryPrefix)
                body.append("Content-Disposition: form-data; name=\"files\"; filename=\"\(fileName)\"\r\n")
                body.append("Content-Type: \(mimeType)\r\n\r\n")
                
                do {
                    let fileData = try Data(contentsOf: fileURL)
                    body.append(fileData)
                } catch {
                    AppLog.debug("Failed to load file data from URL: \(fileURL). Error: \(error.localizedDescription)")
                    throw error
                }
                
                body.append("\r\n")
            }else{
                AppLog.debug("Could,t access security-scoped URL: \(fileURL)")
            }
            
        }
           
           body.append("--\(boundary)--\r\n")
           
           return body
       }
    
    private func mimeType(for url: URL) -> String {
        let pathExtension = url.pathExtension.lowercased()
        switch pathExtension {
        case "pdf":
            return "application/pdf"
        case "doc", "docx":
            return "application/msword"
        case "jpg", "jpeg":
            return "image/jpeg"
        case "png":
            return "image/jpeg"
        default:
            return "application/octet-stream"
        }
    }
         
    
    private func createMultipartBodyImage(with files: [URL], boundary: String) throws -> Data {
           var body = Data()
           let boundaryPrefix = "--\(boundary)\r\n"
           
//        AppLog.debug("Files: \(files)")
        
        for fileURL in files {
            if fileURL.startAccessingSecurityScopedResource() {
                defer { fileURL.stopAccessingSecurityScopedResource() }

                let fileName = fileURL.lastPathComponent
                let mimeType = mimeType(for: fileURL)

                body.append(boundaryPrefix)
                body.append("Content-Disposition: form-data; name=\"files\"; filename=\"\(fileName)\"\r\n")
                body.append("Content-Type: \(mimeType)\r\n\r\n")

                do {
                    let fileData = try Data(contentsOf: fileURL)
                    body.append(fileData)
                } catch {
                    AppLog.debug("Failed to load file data from URL: \(fileURL). Error: \(error.localizedDescription)")
                    throw error
                }

                body.append("\r\n")
            } else {
                AppLog.debug("Could not access security-scoped URL: \(fileURL)")
                throw NSError(domain: "FileUpload", code: -1, userInfo: [NSLocalizedDescriptionKey: "Cannot access file: \(fileURL)"])
            }
        }
           
           body.append("--\(boundary)--\r\n")
           
           return body
       }
    
}

private struct UploadStatusWrapper: Decodable {
    let statusCode: Int
}

