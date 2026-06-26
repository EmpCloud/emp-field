//
//  NotificationViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 10/09/24.
//

import Foundation

@MainActor
class NotificationViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var notificationList: [PreviousTask] = []
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.getNotification
    }
    
    func getNotificationList() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = UserDefaults.standard.string(forKey: "x-access-token")
        
        do {
            
            let fetchData: NotificationModel = try await NetworkManager.shared.getData(to: urlString, as: NotificationModel.self, accessToken: token)
            print("Notification Data")
            print(fetchData)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            notificationList = fetchData.body.data.previousTasks
            
        }catch {
            print("Error: NotificationViewModelError -> \(error.localizedDescription)")
            self.error = error
        }
    }
}
