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
            
            print("[API] POST \(urlString)")
            let (data, response) = try await URLSession.shared.data(for: request)
            let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
            let rawBody = String(data: data, encoding: .utf8) ?? "<non-utf8 data>"
            print("[API] Response (\(httpStatus)) \(urlString)\n\(rawBody)")
            guard httpStatus == 200 else {
                throw NetworkError.invalidResponse
            }

            guard let userLoginData = try? JSONDecoder().decode(UserLoginResponseModel.self, from: data) else {
                print("Invalid data")
                throw NetworkError.invalidData
            }
            
            NetworkManager.shared.statusCode = userLoginData.statusCode
            NetworkManager.shared.responseMessage = userLoginData.body.message

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
            print("Error: UserLoginViewModel")
        }
    }

}
