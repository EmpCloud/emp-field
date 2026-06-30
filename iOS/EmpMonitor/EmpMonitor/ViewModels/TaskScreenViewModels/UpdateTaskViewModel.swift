//
//  UpdateTaskViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 09/09/24.
//

import Foundation

@MainActor
class UpdateTaskViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var taskID: String = ""
    @Published var status: Int = 0
    @Published var currentDateTime: String = ""
    @Published var latitude: String = ""
    @Published var longitude: String = ""
//    @Published var value: FilterTaskValue = FilterTaskValue(convertedAmountInUSD: nil, currency: "INR", amount: 0)
    @Published var taskValue: TaskValue = TaskValue(amount: 0, currency: "INR")
    @Published var taskVolume: Int = 0
    @Published var tagLogs: [TaskTagLog] = []
    
    //for update task
    @Published var clientID: String = ""
    @Published var taskName: String = ""
    @Published var startTime: String = ""
    @Published var endTime: String = ""
    @Published var taskDescription: String = ""
    @Published var date: String = ""
    @Published var files: [DocFile] = []
    @Published var images: [SelectedImage] = []
    
    
    var statusUrlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.updateTaskStatus
    }
    
    var updateUrlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.updateTask
    }
    
    //MARK: To update the task status
    func updateTaskStatus() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        let body = UpdateTaskStatusRequestModel(taskID: taskID, status: status, currentDateTime: currentDateTime, latitude: latitude, longitude: longitude, value: taskValue, taskVolume: taskVolume, tagLogs: tagLogs)
        
        print("Task Status Update body:")
        print(body)
        
        do{
            let fetchData: UpdateTaskStatusResponseModel = try await NetworkManager.shared.postData(to: statusUrlString, body: body, as: UpdateTaskStatusResponseModel.self, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
        }catch {
            print("Error: Update Task Status View Model -> \(error.localizedDescription)")
            self.error = error
        }
    }
    
    
    
    //MARK: To update the task status
    func updateTask() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        let body = UpdateTaskRequestModel(taskID: taskID, clientID: clientID, taskName: taskName, startTime: startTime, endTime: endTime, taskDescription: taskDescription, date: date, files: files, images: images, value: taskValue, taskVolume: taskVolume, tagLogs: tagLogs)
        
        print("Update task Body")
        print(body)
        
        do{
            let fetchData: UpdateTaskResponseModel = try await NetworkManager.shared.putData(to: updateUrlString, body: body, as: UpdateTaskResponseModel.self, accessToken: token, queryParams: nil)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            print("Update Task Data")
            print(fetchData)
            
        }catch {
            print("Error: Update  Task View Model -> \(error.localizedDescription)")
            self.error = error
        }
    }
}
