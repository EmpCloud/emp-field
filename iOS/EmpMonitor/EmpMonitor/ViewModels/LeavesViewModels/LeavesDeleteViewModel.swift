//
//  LeavesDeleteViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/08/24.
//

import Foundation

@MainActor
class LeavesDeleteViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var leaveID: Int = 0
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.deleteLeaves
    }
    
    func deleteLeave() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        let body = LeavesDeleteRequestModel(leaveID: leaveID)
        
        do{
            
            let fetchData: LeavesDeleteResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: LeavesDeleteResponseModel.self, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            print("Delete Data Response:")
            print(fetchData)
            
        }catch{
            print("Error: Delete Data error in Delete leave view model")
            self.error = error
        }
    }
}
