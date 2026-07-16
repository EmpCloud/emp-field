//
//  ClientListViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import Foundation

@MainActor
class ClientListViewModel: ObservableObject {
    @Published var isLoading: Bool = true
    @Published var error: Error?
    
    @Published var clientListData: [ClientListResponseData] = []
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.fetchClients
    }
    
    func getClientList() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        do{
            let fetchData: ClientListResponseModel = try await NetworkManager.shared.getData(to: urlString, as: ClientListResponseModel.self, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            clientListData = fetchData.body.data
            
//            AppLog.debug("Client List Data")
//            AppLog.debug(fetchData)
            
        }catch{
            AppLog.debug("Error: ClientListViewModel data error")
            self.error = error
        }
    }
}
