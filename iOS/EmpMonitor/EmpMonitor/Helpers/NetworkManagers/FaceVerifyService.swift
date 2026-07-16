//
//  FaceVerifyService.swift
//  EmpMonitor
//

import Foundation

final class FaceVerifyService {

    static let shared = FaceVerifyService()
    private init() {}

    private let faceMLBaseURL = "https://biometric-bhl.empcloud.com"

    // POST /api/v2/faces/verify  (multipart/form-data)
    // Parts: file (image/jpeg), company_id (text/plain)
    func verifyFace(imageData: Data, companyId: String) async throws -> FaceVerifyResponse {
        let urlString = "\(faceMLBaseURL)/api/v2/faces/verify?liveness=false"
        guard let url = URL(string: urlString) else { throw NetworkError.invalidURL }

        let boundary = "Boundary-\(UUID().uuidString)"

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.timeoutInterval = 60
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")

        request.httpBody = buildMultipart(imageData: imageData, companyId: companyId, boundary: boundary)

        AppLog.debug("[FaceVerify] POST \(urlString)")
        let (data, response) = try await URLSession.shared.data(for: request)
        let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
        let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
        AppLog.debug("[FaceVerify] Response (\(httpStatus))\n\(rawBody)")

        guard (200...299).contains(httpStatus) else { throw NetworkError.serverError(httpStatus) }

        return try JSONDecoder().decode(FaceVerifyResponse.self, from: data)
    }

    private func buildMultipart(imageData: Data, companyId: String, boundary: String) -> Data {
        var body = Data()
        let crlf = "\r\n"
        let prefix = "--\(boundary)\(crlf)"

        // Part 1: company_id text field
        body.append(prefix)
        body.append("Content-Disposition: form-data; name=\"company_id\"\(crlf)\(crlf)")
        body.append(companyId)
        body.append(crlf)

        // Part 2: face image file
        body.append(prefix)
        body.append("Content-Disposition: form-data; name=\"file\"; filename=\"face.jpg\"\(crlf)")
        body.append("Content-Type: image/jpeg\(crlf)\(crlf)")
        body.append(imageData)
        body.append(crlf)

        // Closing boundary
        body.append("--\(boundary)--\(crlf)")

        return body
    }
}
