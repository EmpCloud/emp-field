//
//  VerifyEmailOTPViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 01/10/24.
//

import Foundation

@MainActor
class VerifyEmailOTPViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.verifyOTP
    }
    
    func verifyEmailOTP(email: String, emailOTP: String) async throws {
        isLoading = true
        defer { isLoading = false }
        
        let queryParams = [
            "email": email,
            "verifyOtp": emailOTP
        ]
        
        do {
            let fetchData: VerifyEmailResponseModel = try await NetworkManager.shared.getDataWithQuery(to: urlString, as: VerifyEmailResponseModel.self, accessToken: nil, queryParameters: queryParams)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            AppLog.debug("Verify Email OTP Data ")
            AppLog.debug(fetchData)
            
        }catch {
            AppLog.debug("Error: verifyEmail OTP \(error.localizedDescription)")
            self.error = error
        }
    }
}
