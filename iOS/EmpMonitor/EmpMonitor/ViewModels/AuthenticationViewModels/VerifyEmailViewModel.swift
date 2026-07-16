//
//  VerifyEmailViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 27/09/24.
//

import Foundation

@MainActor
class VerifyEmailViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var email: String = ""
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.verifyEmail
    }
    
    func verifyUserEmail() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let body = VerifyEmailRequestModel(userMail: email)
        
        do {
            let fetchData: VerifyEmailResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: VerifyEmailResponseModel.self, accessToken: nil)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
//            AppLog.debug(fetchData.body.message)
//            AppLog.debug(fetchData.statusCode)
            
        }catch {
            AppLog.debug("Error: Email Verification Failed: \(error.localizedDescription)")
            self.error = error
        }
    }
}
