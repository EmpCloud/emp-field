//
//  CreateLeaveViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 14/08/24.
//

import Foundation

@MainActor
class CreateLeaveViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var createLeaveData: CreateLeaveResponseModel?
    
    @Published var dayType: String = ""
    @Published var leaveType: Int = 0
    @Published var startDate: String = ""
    @Published var endDate: String = ""
    @Published var reason: String = ""
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.createLeave
    }
    
    func createLeave() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        let day = LeaveHelper.shared.getDayType(dayType: dayType)
        
        let body = CreateLeaveRequestModel(dayType: day, leaveType: leaveType, startDate: startDate, endDate: endDate, reason: reason)
        AppLog.debug(body)
        
        do{
            let fetchData: CreateLeaveResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: CreateLeaveResponseModel.self, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            createLeaveData = fetchData
            
            AppLog.debug("Fetch Create Leave data")
            AppLog.debug(fetchData)
        }catch{
            AppLog.debug("Error: Create Leave Error in View Model")
            self.error = error
        }
    }
    
//    private func getDayType(dayType: String) -> Int {
//        switch dayType {
//        case "First Half":
//            return 1
//        case "Second Half":
//            return 3
//        case "Full Day":
//            return 2
//        default:
//            return 0
//        }
//    }
}
