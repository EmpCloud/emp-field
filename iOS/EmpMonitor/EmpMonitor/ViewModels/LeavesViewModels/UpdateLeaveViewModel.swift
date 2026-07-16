//
//  File.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 16/08/24.
//

import Foundation

@MainActor
class UpdateLeaveViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var leaveID: Int = 0
    @Published var dayType: String = ""
    @Published var leaveType: Int = 0
    @Published var startDate: String = ""
    @Published var endDate: String = ""
    @Published var reason: String = ""
    @Published var employeeName: String = ""
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.updateLeaves
    }
    
    func updateLeave() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        let day = LeaveHelper.shared.getDayType(dayType: dayType)
        
        let body = UpdateLeaveRequestModel(leaveID: leaveID, dayType: day, leaveType: leaveType, startDate: startDate, endDate: endDate, reason: reason)
        
        do{
            
            let fetchData: UpdateLeaveResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: UpdateLeaveResponseModel.self, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            AppLog.debug("Updated Data:")
            AppLog.debug(fetchData)
            
        }catch{
            AppLog.debug("Error: Updateleave Data error in Update leave view model")
            self.error = error
        }
    }
}
