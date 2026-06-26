//
//  CreateProfileViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import Foundation

class CreateProfileViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    @Published var isProfileCreated: Bool = false
    
    @Published var fullName: String = ""
    @Published var age: String = ""
    @Published var gender: String = ""
    @Published var email: String = ""
    @Published var profilePic: String?
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
}

extension CreateProfileViewModel {
    
    @MainActor
    func createProfile() async throws {
        isLoading = true
        defer {self.isLoading = false}
        
        if profilePic == nil {
            
        }
            
        let body = CreateProfileRequestModel(fullName: fullName, age: Int(age) ?? 0, gender: gender, email: email, profilePic: profilePic, address1: address1, address2: address2, latitude: latitude, longitude: longitude, city: city, state: state, country: country, zipCode: zipCode, phoneNumber: phoneNumber)
        
        let token = UserDefaults.standard.string(forKey: "x-access-token")
//        print(token)
        
        do {
            let fetchedData: CreateProfileResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: CreateProfileResponseModel.self, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchedData.statusCode
            
            // Storing users profile for later use and direct navigation to homescreen
            UserDefaults.standard.setObject(fetchedData, forKey: "UserProfile")
            
            // to store userProfile Data
            UserDefaults.standard.setValue(fetchedData.body.data.resultData.first?.fullName, forKey: "UserName")
            UserDefaults.standard.setValue(fetchedData.body.data.resultData.first?.department, forKey: "UserDepartment")
            UserDefaults.standard.setValue(fetchedData.body.data.resultData.first?.profilePic, forKey: "UserProfilePic")
            
            //to update the profile pic
            ProfileHelper.shared.updateProfilePic(with: URL(string: fetchedData.body.data.resultData.first?.profilePic ?? ""))
            
        }catch{
            self.error = error
        }
    }
}
