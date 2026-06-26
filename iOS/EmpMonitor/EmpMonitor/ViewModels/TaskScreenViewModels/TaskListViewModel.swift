//
//  TaskListViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 21/08/24.
//

import Foundation

@MainActor
class TaskListViewModel: ObservableObject {
    @Published var isLoading: Bool = true
    @Published var error: Error?
    
//    @Published var taskData: [TaskListResponseData] = []
    
    
    
    //filter properties
    @Published var date: String = ""
    @Published var status: Int = 0
    @Published var filterTaskData: [FilterTaskListResponseData] = []
    @Published var fetchStatus: Int = 0
    
    private var removedTask: (task: FilterTaskListResponseData, index: Int)?
    
   
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.fetchTasks
    }
    
    var filterURLString: String {
        return Constants.shared.baseURL + Constants.Endpoint.filterTask
    }
    
    func getTaskList() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = UserDefaults.standard.string(forKey: "x-access-token")
        
        do{
            let fetchData: TaskListResponseModel = try await NetworkManager.shared.getData(to: urlString, as: TaskListResponseModel.self, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
//            taskData = fetchData.body.data
            
        }catch{
            print("Error: TaskListViewModel data error")
            self.error = error
        }
    }
    
    func getFilterTaskList() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = UserDefaults.standard.string(forKey: "x-access-token")
        
        let body = FilterTaskListRequestModel(date: date, status: status)
        
        print("Fetch Filter task List body:")
        print(body)
        
        do{
            let fetchData: FilterTaskListResponseModel = try await NetworkManager.shared.postData(to: filterURLString, body: body, as: FilterTaskListResponseModel.self, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            filterTaskData = fetchData.body.data ?? []
//            print("Filtered Task Data : \(filterTaskData)")
            fetchStatus = fetchData.statusCode
            
        }catch{
            print("Error: FilterTaskListViewModel data error -> \(error)")
            self.error = error
        }
    }
    
    //MARK: To remove the task from the list with animation
    func removeTask(taskID: String) {
        print(taskID)
        if let index = filterTaskData.firstIndex(where: { $0.id == taskID }) {
            removedTask = (task: filterTaskData[index], index: index)
            print(index)
            filterTaskData.remove(at: index)
        }
    }
}
