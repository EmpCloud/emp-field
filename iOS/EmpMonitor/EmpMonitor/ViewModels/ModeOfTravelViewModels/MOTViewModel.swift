//
//  MOTViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/09/24.
//

import Foundation

@MainActor
class MOTViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var currentModeOfTravel: String = ""
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.updateModeOfTransport
    }
    
    func setModeOfTransport() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        let body = MOTRequestModel(currentMode: currentModeOfTravel)
        
        do {
            let fetchData: MOTResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: MOTResponseModel.self, accessToken: token)
            
            AppLog.debug("MOTResponse Data")
            AppLog.debug(fetchData)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
        }catch {
            AppLog.debug("Error: Set Mode of travel issue: \(error.localizedDescription)")
        }
    }
}
