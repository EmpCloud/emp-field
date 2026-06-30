//
//  DeleteTaskViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 06/09/24.
//

import Foundation

@MainActor
class DeleteTaskViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var taskID: String = ""
    @Published var status: Int = 5
    @Published var currentDateTime: String = ""
    @Published var latitude: String = ""
    @Published var longitude: String = ""
    @Published var value: FilterTaskValue = FilterTaskValue(convertedAmountInUSD: nil, currency: "INR", amount: 0)
    @Published var taskVolume: Int = 0
    @Published var tagLogs: [TaskTagLog] = []
    
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.updateTaskStatus
    }
    
    func deleteTask() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        let body = DeleteTaskRequestModel(taskID: taskID, status: status, currentDateTime: currentDateTime, latitude: latitude, longitude: longitude, value: value, taskVolume: taskVolume, tagLogs: tagLogs)
        
        do{
            let fetchData: DeleteTaskResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: DeleteTaskResponseModel.self, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
//            print(fetchData)
            
        }catch {
            print("Error: Delete Task View Model -> \(error.localizedDescription)")
            self.error = error
        }
    }
}
