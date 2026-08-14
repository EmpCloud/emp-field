//
//  LocationSocketManager.swift
//  EmpMonitor
//

import Foundation

@MainActor
final class LocationSocketManager: ObservableObject {
    static let shared = LocationSocketManager()

    private let eventName = "location:update"
    private let reconnectDelay: UInt64 = 5_000_000_000
    private var socketTask: URLSessionWebSocketTask?
    private var receiveTask: Task<Void, Never>?
    private var reconnectTask: Task<Void, Never>?
    private var shouldStayConnected = false
    private var isConnecting = false

    @Published private(set) var isConnected = false

    private init() {}

    func connectIfNeeded() {
        guard AuthStore.shared.getAccessToken()?.isEmpty == false else {
            disconnect()
            return
        }
        guard socketTask == nil, !isConnecting else { return }

        guard let url = Constants.shared.locationSocketURL else {
            AppLog.debug("[LocationSocket] Invalid socket URL")
            return
        }

        shouldStayConnected = true
        isConnecting = true

        var request = URLRequest(url: url)
        if let token = AuthStore.shared.getAccessToken() {
            request.setValue(token, forHTTPHeaderField: "x-access-token")
        }

        let task = URLSession.shared.webSocketTask(with: request)
        socketTask = task
        task.resume()
        receiveTask = Task { [weak self] in
            await self?.receiveLoop()
        }
    }

    func disconnect() {
        shouldStayConnected = false
        isConnecting = false
        isConnected = false
        reconnectTask?.cancel()
        reconnectTask = nil
        receiveTask?.cancel()
        receiveTask = nil
        socketTask?.cancel(with: .goingAway, reason: nil)
        socketTask = nil
    }

    private func receiveLoop() async {
        while !Task.isCancelled, let socketTask {
            do {
                let message = try await socketTask.receive()
                handle(message)
            } catch {
                AppLog.debug("[LocationSocket] Receive failed: \(error)")
                resetConnection()
                scheduleReconnectIfNeeded()
                break
            }
        }
    }

    private func handle(_ message: URLSessionWebSocketTask.Message) {
        switch message {
        case .string(let value):
            handleEngineMessage(value)
        case .data(let data):
            guard let value = String(data: data, encoding: .utf8) else { return }
            handleEngineMessage(value)
        @unknown default:
            break
        }
    }

    private func handleEngineMessage(_ message: String) {
        if message.hasPrefix("0") {
            send("40")
            return
        }

        if message == "2" {
            send("3")
            return
        }

        if message.hasPrefix("40") {
            isConnecting = false
            isConnected = true
            AppLog.debug("[LocationSocket] Connected")
            return
        }

        guard message.hasPrefix("42") else { return }
        handleSocketEventPayload(String(message.dropFirst(2)))
    }

    private func handleSocketEventPayload(_ payload: String) {
        guard let data = payload.data(using: .utf8),
              let event = try? JSONDecoder().decode(LocationSocketEvent.self, from: data),
              event.name == eventName else {
            return
        }

        guard shouldHandle(orgId: event.payload.orgId) else {
            AppLog.debug("[LocationSocket] Ignored location update for another org")
            return
        }

        AppLog.debug("[LocationSocket] Received \(eventName) for \(event.payload.locationName)")
        Task {
            await TrackingSettingsViewModel.shared.fetchTrackingSettings()
        }
    }

    private func shouldHandle(orgId: String) -> Bool {
        guard let currentOrgId = AuthStore.shared.getLoggedInUser()?.body.data?.userData.orgID,
              !currentOrgId.isEmpty else {
            return true
        }
        return currentOrgId == orgId
    }

    private func send(_ message: String) {
        socketTask?.send(.string(message)) { error in
            if let error {
                AppLog.debug("[LocationSocket] Send failed: \(error)")
            }
        }
    }

    private func resetConnection() {
        isConnecting = false
        isConnected = false
        receiveTask = nil
        socketTask?.cancel(with: .abnormalClosure, reason: nil)
        socketTask = nil
    }

    private func scheduleReconnectIfNeeded() {
        guard shouldStayConnected, AuthStore.shared.getAccessToken()?.isEmpty == false else { return }
        guard reconnectTask == nil else { return }

        reconnectTask = Task { [weak self] in
            try? await Task.sleep(nanoseconds: self?.reconnectDelay ?? 5_000_000_000)
            guard !Task.isCancelled else { return }
            await MainActor.run {
                self?.reconnectTask = nil
                self?.connectIfNeeded()
            }
        }
    }
}

private struct LocationSocketEvent: Decodable {
    let name: String
    let payload: LocationSocketPayload

    init(from decoder: Decoder) throws {
        var container = try decoder.unkeyedContainer()
        name = try container.decode(String.self)
        payload = try container.decode(LocationSocketPayload.self)
    }
}

private struct LocationSocketPayload: Decodable {
    let orgId: String
    let locationName: String
    let isGlobalUpdate: Bool
    let updatedAt: String
    let message: String

    enum CodingKeys: String, CodingKey {
        case orgId, locationName, isGlobalUpdate, updatedAt, message
    }
}
