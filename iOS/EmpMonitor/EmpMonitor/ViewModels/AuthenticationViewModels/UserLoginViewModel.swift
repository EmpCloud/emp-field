//
//  UserLoginViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/06/24.
//

import Foundation
import UIKit


class UserLoginViewModel: ObservableObject {
    @Published var userLoginData: UserLoginData?
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
//    @Published var isLoggedIn: Bool = false     //-------------logout changes -------
    @Published var email: String = ""
    @Published var password: String = ""
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.login
    }
}

extension UserLoginViewModel {
    @MainActor
    func loginUser() async throws {
        
        self.isLoading = true
        
        defer { self.isLoading = false }
        
        do{
            guard let url = URL(string: urlString) else {
                throw NetworkError.invalidURL
            }
            
            let deviceId = UIDevice.current.identifierForVendor?.uuidString ?? ""
            let credentials = UserRequestModel(userMail: email, password: password, deviceId: deviceId)
            
            var request = URLRequest(url: url)
            request.httpMethod = "POST"
            request.setValue("application/json", forHTTPHeaderField: "Content-Type")
            request.httpBody = try JSONEncoder().encode(credentials)
            
            AppLog.debug("[API] POST \(urlString)")
            let (data, response) = try await URLSession.shared.data(for: request)
            let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
            let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
            AppLog.debug("[API] Response (\(httpStatus)) \(urlString)\n\(rawBody)")
            let errorMessage = decodeErrorMessage(from: data)
            if isSessionExpired(status: httpStatus, message: errorMessage) {
                expireSession(message: errorMessage)
                throw NetworkError.unauthorized
            }
            guard httpStatus == 200 else {
                recordFailure(status: httpStatus, message: errorMessage ?? NetworkError.invalidResponse.errorDescription)
                throw NetworkError.invalidResponse
            }

            guard let userLoginData = try? JSONDecoder().decode(UserLoginResponseModel.self, from: data) else {
                AppLog.debug("Invalid data")
                recordFailure(status: httpStatus, message: NetworkError.invalidData.errorDescription)
                throw NetworkError.invalidData
            }
            
            NetworkManager.shared.statusCode = userLoginData.statusCode
            NetworkManager.shared.responseMessage = userLoginData.body.message

            if isSessionExpired(status: userLoginData.statusCode, message: userLoginData.body.message) {
                expireSession(message: userLoginData.body.message)
                throw NetworkError.unauthorized
            }

            // Only continue if business-level login succeeded and a token was issued
            guard userLoginData.statusCode == 200,
                  let accessToken = userLoginData.body.data?.accessToken else {
                return
            }

            // Save session securely in Keychain via AuthStore
            AuthStore.shared.saveAccessToken(accessToken)
            AuthStore.shared.saveLoggedInUser(userLoginData)
            AppState.shared.updateLoginState()

            // to store userProfile Data
            UserDefaults.standard.setValue(userLoginData.body.data?.userData.fullName, forKey: "UserName")
            UserDefaults.standard.setValue(userLoginData.body.data?.userData.department, forKey: "UserDepartment")
            UserDefaults.standard.removeObject(forKey: "UserProfilePic")
            UserDefaults.standard.setValue(userLoginData.body.data?.userData.profilePic, forKey: "UserProfilePic")
            // Profile fetch and tracking settings are handled by the caller after loginUser() returns.

        }catch{
            self.error = error
            AppLog.debug("Error: UserLoginViewModel")
        }
    }

    @MainActor
    private func recordFailure(status: Int, message: String?) {
        NetworkManager.shared.statusCode = status
        NetworkManager.shared.responseMessage = message ?? ""
    }

    @MainActor
    private func expireSession(message: String?) {
        recordFailure(status: 401, message: message)
        AppState.shared.handleSessionExpired(message: message)
    }

    private func decodeErrorMessage(from data: Data) -> String? {
        let body = (try? JSONDecoder().decode(ErrorResponse.self, from: data))?.body
        return body?.message ?? body?.error
    }

    private func isSessionExpired(status: Int, message: String?) -> Bool {
        if status == 401 { return true }
        guard let message = message?.lowercased() else { return false }
        return message.contains("session expired")
            || message.contains("logged in on another device")
    }

}
