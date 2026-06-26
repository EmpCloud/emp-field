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
            print("uploadfile url error")
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
        
        print("[API] POST \(urlString)")
        let(data, response) = try await URLSession.shared.data(for: request)
        let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
        let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
        print("[API] Response (\(httpStatus)) \(urlString)\n\(rawBody)")

        guard let httpResponse = response as? HTTPURLResponse, (200...299).contains(httpResponse.statusCode) else {
            throw NetworkError.invalidResponse
        }

        let uploadResponse = try JSONDecoder().decode(FileUploadResponseModel.self, from: data)
        return uploadResponse
    }

    func uploadImage(to urlString: String, files: [URL], accessToken: String) async throws -> FileUploadResponseModel {
        
        guard let url = URL(string: urlString) else {
            print("uploadfile url error")
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
        
        print("[API] POST \(urlString)")
        let(data, response) = try await URLSession.shared.data(for: request)
        let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
        let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
        print("[API] Response (\(httpStatus)) \(urlString)\n\(rawBody)")

        guard let httpResponse = response as? HTTPURLResponse, (200...299).contains(httpResponse.statusCode) else {
            throw NetworkError.invalidResponse
        }

        let uploadResponse = try JSONDecoder().decode(FileUploadResponseModel.self, from: data)
        return uploadResponse
    }

    func uploadProfileImage(to urlString: String, files: [URL], accessToken: String) async throws -> ProfileUploadResponseModel {
        
        guard let url = URL(string: urlString) else {
            print("uploadfile url error")
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
        
        print("[API] POST \(urlString)")
        let(data, response) = try await URLSession.shared.data(for: request)
        let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
        let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
        print("[API] Response (\(httpStatus)) \(urlString)\n\(rawBody)")

        guard let httpResponse = response as? HTTPURLResponse, (200...299).contains(httpResponse.statusCode) else {
            throw NetworkError.invalidResponse
        }

        let uploadResponse = try JSONDecoder().decode(ProfileUploadResponseModel.self, from: data)
        return uploadResponse
    }
    
    private func createMultipartBody(with files: [URL], boundary: String) throws -> Data {
           var body = Data()
           let boundaryPrefix = "--\(boundary)\r\n"
           
//        print("Files: \(files)")
        
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
                    print("Failed to load file data from URL: \(fileURL). Error: \(error.localizedDescription)")
                    throw error
                }
                
                body.append("\r\n")
            }else{
                print("Could,t access security-scoped URL: \(fileURL)")
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
           
//        print("Files: \(files)")
        
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
                    print("Failed to load file data from URL: \(fileURL). Error: \(error.localizedDescription)")
                    throw error
                }

                body.append("\r\n")
            } else {
                print("Could not access security-scoped URL: \(fileURL)")
                throw NSError(domain: "FileUpload", code: -1, userInfo: [NSLocalizedDescriptionKey: "Cannot access file: \(fileURL)"])
            }
        }
           
           body.append("--\(boundary)--\r\n")
           
           return body
       }
            
}


