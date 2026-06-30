//
//  UpdateProfileViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 03/09/24.
//

import Foundation

@MainActor
class UpdateProfileViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var fullName: String = ""
    @Published var age: String = ""
    @Published var gender: String = ""
    @Published var email: String = ""
    @Published var profilePic: String = ""
    @Published var address1: String = ""
    @Published var address2: String = ""
    @Published var latitude: String = ""
    @Published var longitude: String = ""
    @Published var city: String = ""
    @Published var state: String = ""
    @Published var country: String = ""
    @Published var zipCode: String = ""
    @Published var phoneNumber: String = ""
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.updateProfile
    }
    
    func updateProfile() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        let body = UpdateProfileRequestModel(fullName: fullName, age: Int(age) ?? 0, gender: gender, email: email, profilePic: profilePic, address1: address1, address2: address2, latitude: latitude, longitude: longitude, city: city, state: state, country: country, zipCode: zipCode, phoneNumber: phoneNumber)
        
        do {
            let fetchData: UpdateProfileResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: UpdateProfileResponseModel.self, accessToken: token)
            
            print(fetchData.body.data.resultData.first)
            
            // Updating & Storing users profile securely for later use and direct navigation to homescreen
            AuthStore.shared.saveUserProfileData(fetchData)
            
            
            // to store userProfile Data
            UserDefaults.standard.setValue(fetchData.body.data.resultData.first?.fullName, forKey: "UserName")
            UserDefaults.standard.setValue(fetchData.body.data.resultData.first?.department, forKey: "UserDepartment")
            UserDefaults.standard.setValue(fetchData.body.data.resultData.first?.profilePic, forKey: "UserProfilePic")
            
            //to update the profile pic
            ProfileHelper.shared.updateProfilePic(with: URL(string: fetchData.body.data.resultData.first?.profilePic ?? ""))
            
        }catch {
            print("Error: Get Profile error -> \(error.localizedDescription)")
            self.error = error
        }
    }
}

