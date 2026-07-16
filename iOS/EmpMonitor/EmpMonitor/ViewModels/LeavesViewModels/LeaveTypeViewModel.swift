//
//  LeaveTypeViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 14/08/24.
//

import Foundation

@MainActor
class LeaveTypeViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var leaveTypeData: [LeavesTypeResponseDetail] = []
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.fetchLeaveType
    }
    
    func fetchLeaveType() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        do{
            let fetchedData: LeavesTypeResponseModel = try await NetworkManager.shared.postDataWithoutParameter(to: urlString, as: LeavesTypeResponseModel.self, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchedData.statusCode
            NetworkManager.shared.responseMessage = fetchedData.body.message
            
//            AppLog.debug("Fetch Data:")
//            AppLog.debug(fetchedData)
            
            leaveTypeData = fetchedData.body.data.data
            
        }catch{
            AppLog.debug("Error: FetchLeaveType Error in View Model")
            self.error = error
        }
    }
}
