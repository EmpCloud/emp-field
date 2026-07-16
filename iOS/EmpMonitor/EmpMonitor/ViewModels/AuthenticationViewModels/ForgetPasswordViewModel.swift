//
//  File.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 01/10/24.
//

import Foundation

@MainActor
class ForgetPasswordViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var email: String = ""
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.forgotPassword
    }
    
    func forgetPassword() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let body = ForgetPasswordRequestModel(email: email)
        
        do {
            let fetchData: VerifyEmailResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: VerifyEmailResponseModel.self, accessToken: nil)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            AppLog.debug("Forget Password Response:")
            AppLog.debug(fetchData)
            
        }catch {
            AppLog.debug("Error: While calling forget password -> \(error.localizedDescription)")
            self.error = error
        }
    }
}
