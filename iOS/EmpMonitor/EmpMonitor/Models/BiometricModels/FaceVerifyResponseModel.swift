//
//  FaceVerifyResponseModel.swift
//  EmpMonitor
//

import Foundation

struct FaceVerifyResponse: Codable {
    let success: Bool
    let code: String?
    let message: String?
    let data: FaceVerifyData
    let request_id: String?
}

struct FaceVerifyData: Codable {
    let verified: Bool?
    let is_live: Bool?
    let live_score: Double?
    let match: FaceMatchData?
    let top_similarity: Double?
    let latency_ms: Double?

    var isMatched: Bool { (verified == true) && (match?.face_id != nil) }
}

struct FaceMatchData: Codable {
    let face_id: String?
    let image_id: Int?
    let image_url: String?
    let similarity: Double?
    let shard_id: Int?
}
