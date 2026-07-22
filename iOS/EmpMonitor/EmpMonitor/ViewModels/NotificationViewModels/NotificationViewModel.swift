//
//  NotificationViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 10/09/24.
//

import Foundation

@MainActor
class NotificationViewModel: ObservableObject {
    private static let readNotificationIDsKey = "ReadNotificationIDs"

    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var notificationList: [PreviousTask] = []
    @Published private(set) var readNotificationIDs: Set<String> = NotificationViewModel.loadReadNotificationIDs()

    var hasUnreadNotifications: Bool {
        notificationList.contains { isUnread($0) }
    }
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.getNotification
    }
    
    func getNotificationList() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        do {
            
            let fetchData: NotificationModel = try await NetworkManager.shared.getData(to: urlString, as: NotificationModel.self, accessToken: token)
            AppLog.debug("Notification Data")
            AppLog.debug(fetchData)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            notificationList = (fetchData.body.data.previousTasks + fetchData.body.data.rescheduledTasks)
                .sorted { $0.updatedAt > $1.updatedAt }
            
        }catch {
            AppLog.debug("Error: NotificationViewModelError -> \(error.localizedDescription)")
            self.error = error
        }
    }

    func isUnread(_ notification: PreviousTask) -> Bool {
        !readNotificationIDs.contains(notification.id)
    }

    func markAsRead(_ notification: PreviousTask) {
        markAsRead(notificationIDs: [notification.id])
    }

    func markAllAsRead() {
        markAsRead(notificationIDs: notificationList.map(\.id))
    }

    private func markAsRead(notificationIDs: [String]) {
        guard !notificationIDs.isEmpty else { return }

        var updatedReadIDs = readNotificationIDs
        updatedReadIDs.formUnion(notificationIDs)
        readNotificationIDs = updatedReadIDs
        persistReadNotificationIDs()
    }

    private func persistReadNotificationIDs() {
        UserDefaults.standard.set(Array(readNotificationIDs), forKey: Self.readNotificationIDsKey)
    }

    private static func loadReadNotificationIDs() -> Set<String> {
        Set(UserDefaults.standard.stringArray(forKey: readNotificationIDsKey) ?? [])
    }
}
