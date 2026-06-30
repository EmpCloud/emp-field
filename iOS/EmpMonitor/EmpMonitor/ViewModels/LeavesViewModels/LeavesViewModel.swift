//
//  LeavesViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/07/24.
//

import Foundation

@MainActor
class LeavesViewModel: ObservableObject {
    @Published var isLoading: Bool = true
    @Published var error: Error?
    
    @Published var startDate: String = ""
    @Published var endDate: String = ""
    
    @Published var leavesData: [LeavesResponseData] = []
    @Published var fetchStatusCode: Int = 0
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.getLeaves
    }
    
    func getLeaves() async {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        let body = LeavesRequestModel(startDate: startDate, endDate: endDate)
        
        do{
            let fetchData: LeavesResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: LeavesResponseModel.self, accessToken: token)
            
            NetworkManager.shared.responseMessage = fetchData.body.message
            NetworkManager.shared.statusCode = fetchData.statusCode
            
            leavesData = fetchData.body.data ?? []
            fetchStatusCode = fetchData.statusCode
            
            print("Leaves Data:")
            print(leavesData)
            
        }catch{
            print("Error: error in getleaves")
            self.error = error
            
        }
    }
}
