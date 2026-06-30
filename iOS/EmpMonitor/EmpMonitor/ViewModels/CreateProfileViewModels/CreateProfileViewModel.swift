//
//  CreateProfileViewModel.swift
//  EmpMonitor
//

import Foundation

@MainActor
final class CreateProfileViewModel: ObservableObject {
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
    
    func createProfile() async {
        isLoading = true
        defer { self.isLoading = false }
        
        // Ensure phone number is digits-only before sending to the API.
        phoneNumber = HelperFunction.shared.sanitizePhoneNumber(phoneNumber)
        
        let body = CreateProfileRequestModel(fullName: fullName, age: Int(age) ?? 0, gender: gender, email: email, profilePic: profilePic, address1: address1, address2: address2, latitude: latitude, longitude: longitude, city: city, state: state, country: country, zipCode: zipCode, phoneNumber: phoneNumber)
        
        let token = AuthStore.shared.getAccessToken()
        
        do {
            let fetchedData: CreateProfileResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: CreateProfileResponseModel.self, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchedData.statusCode
            
            // Storing users profile securely for later use and direct navigation to homescreen
            AuthStore.shared.saveUserProfileData(fetchedData)
            AppState.shared.updateLoginState()
            
            // to store userProfile Data
            UserDefaults.standard.setValue(fetchedData.body.data.resultData.first?.fullName, forKey: "UserName")
            UserDefaults.standard.setValue(fetchedData.body.data.resultData.first?.department, forKey: "UserDepartment")
            UserDefaults.standard.setValue(fetchedData.body.data.resultData.first?.profilePic, forKey: "UserProfilePic")
            
            //to update the profile pic
            ProfileHelper.shared.updateProfilePic(with: URL(string: fetchedData.body.data.resultData.first?.profilePic ?? ""))
            
            self.isProfileCreated = true
            
        } catch {
            self.error = error
        }
    }
}
