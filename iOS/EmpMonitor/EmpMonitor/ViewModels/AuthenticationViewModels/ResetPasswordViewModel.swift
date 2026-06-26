//
//  ResetPasswordViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 01/10/24.
//

import Foundation

@MainActor
class ResetPasswordViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var email: String = ""
    @Published var verifyToken: String = ""
    @Published var newPassword: String = ""
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.resetPassword
    }
    
    func resetPassword() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let body = ResetPasswordRequestModel(email: email, verifyToken: verifyToken, newPassword: newPassword)
        
        print("Reset password body:")
        print(body)
        
        do {
            let fetchData: ResetPasswordResponseModel = try await NetworkManager.shared.putData(to: urlString, body: body, as: ResetPasswordResponseModel.self, accessToken: nil, queryParams: nil)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            print("Reset Password Done")
            
        }catch {
            print("Error: Reset Password Error -> \(error.localizedDescription)")
            self.error = error
        }
    }
}
