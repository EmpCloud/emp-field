//
//  TrackViewModel.swift
//  EmpMonitor
//

import Foundation

@MainActor
final class TrackViewModel: ObservableObject {
    
    static let shared = TrackViewModel()
    
    @Published var isLoading: Bool = false
    @Published var error: Error?
    @Published var trackRequestData: [TrackRequestModelData] = []
    
    private init() { }
    
    var urlString: String {
        return Constants.shared.deviceStatusIntegrationBaseURL + Constants.Endpoint.getLocation
    }

    private var deviceStatusURLString: String {
        return Constants.shared.deviceStatusIntegrationBaseURL + Constants.Endpoint.deviceStatus
    }
    
    func trackUser() async {
        isLoading = true
        defer { isLoading = false }
        
        let body = trackRequestData
        DeviceStatusDebug.log("POST location piggyback url=\(urlString), points=\(body.count), lastPoint=\(body.last?.deviceStatusLogDescription ?? "none")")
        
        do {
            let fetchData: TrackResponseModel = try await NetworkManager.shared.postData(
                to: urlString,
                body: body,
                as: TrackResponseModel.self,
                accessToken: AuthStore.shared.getAccessToken()
            )
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            // Persist updated tracking settings so PermissionManager picks them up next cycle
            let frequency = fetchData.body.data.currentFrequency
            let radius = fetchData.body.data.currentRadius
            if frequency > 0 {
                UserDefaults.standard.set(frequency, forKey: "CurrentFrequency")
            }
            if radius > 0 {
                UserDefaults.standard.set(radius, forKey: "CurrentRadius")
            }
            
            DeviceStatusDebug.log("POST location piggyback success statusCode=\(fetchData.statusCode), message=\(fetchData.body.message), currentFrequency=\(frequency), currentRadius=\(radius)")
            AppLog.debug("Track response data")
            AppLog.debug(fetchData)
            
        } catch {
            DeviceStatusDebug.log("POST location piggyback failed error=\(error.localizedDescription), statusCode=\(NetworkManager.shared.statusCode), message=\(NetworkManager.shared.responseMessage)")
            AppLog.debug("Error: tracking user error \(error.localizedDescription)")
            self.error = error
            NetworkManager.shared.statusCode = (error as? NetworkError).map { _ in 0 } ?? 0
        }
    }

    @discardableResult
    func updateDeviceStatus(snapshot: DeviceStatusSnapshot = .current, accessToken: String? = nil) async -> Bool {
        guard let token = accessToken ?? AuthStore.shared.getAccessToken() else {
            DeviceStatusDebug.log("PUT heartbeat skipped: missing access token, payload=\(snapshot.logDescription)")
            return false
        }

        DeviceStatusDebug.log("PUT heartbeat url=\(deviceStatusURLString), payload=\(snapshot.logDescription)")

        do {
            let response: DeviceStatusResponseModel = try await NetworkManager.shared.putData(
                to: deviceStatusURLString,
                body: snapshot,
                as: DeviceStatusResponseModel.self,
                accessToken: token
            )

            DeviceStatusDebug.log("PUT heartbeat success status=\(response.status ?? "nil"), message=\(response.message ?? "nil"), serverBattery=\(response.data?.batteryPercent.map(String.init) ?? "nil"), serverCharging=\(response.data?.isCharging.map(String.init) ?? "nil"), serverDeviceStatus=\(response.data?.deviceStatus ?? "nil"), lastSeenAt=\(response.data?.lastSeenAt ?? "nil")")
            AppLog.debug("Device status response")
            AppLog.debug(response)
            return true
        } catch {
            DeviceStatusDebug.log("PUT heartbeat failed error=\(error.localizedDescription), statusCode=\(NetworkManager.shared.statusCode), message=\(NetworkManager.shared.responseMessage)")
            AppLog.debug("Error: device status update error \(error.localizedDescription)")
            self.error = error
            return false
        }
    }
}

private struct DeviceStatusResponseModel: Codable {
    let status: String?
    let message: String?
    let data: DeviceStatusResponseData?
}

private struct DeviceStatusResponseData: Codable {
    let empId: String?
    let deviceStatus: String?
    let batteryPercent: Int?
    let isCharging: Bool?
    let lastSeenAt: String?

    enum CodingKeys: String, CodingKey {
        case empId = "emp_id"
        case deviceStatus
        case batteryPercent
        case isCharging
        case lastSeenAt
    }
}
