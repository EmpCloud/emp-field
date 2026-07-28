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
        let requestAge = try validatedAge()
        
        let body = UpdateProfileRequestModel(
            fullName: fullName,
            age: requestAge,
            gender: gender.isEmpty ? nil : gender,
            email: email,
            profilePic: profilePic.isEmpty ? nil : profilePic,
            address1: address1, address2: address2,
            latitude: latitude, longitude: longitude,
            city: city, state: state, country: country, zipCode: zipCode,
            phoneNumber: phoneNumber
        )

        do {
            let fetchData: UpdateProfileResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: UpdateProfileResponseModel.self, accessToken: token)

            AppLog.debug(fetchData.body.data.resultData.first as Any)

            AuthStore.shared.saveUserProfileData(fetchData)

            UserDefaults.standard.setValue(fetchData.body.data.resultData.first?.fullName, forKey: "UserName")
            UserDefaults.standard.setValue(fetchData.body.data.resultData.first?.department, forKey: "UserDepartment")
            UserDefaults.standard.setValue(fetchData.body.data.resultData.first?.profilePic, forKey: "UserProfilePic")

            ProfileHelper.shared.updateProfilePic(with: URL(string: fetchData.body.data.resultData.first?.profilePic ?? ""))

        } catch {
            AppLog.debug("Error: Update Profile -> \(error.localizedDescription)")
            self.error = error
            throw error
        }
    }

    private func validatedAge() throws -> String? {
        let trimmedAge = age.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !trimmedAge.isEmpty else {
            return nil
        }

        guard let ageValue = Int(trimmedAge), (1...100).contains(ageValue) else {
            throw NetworkError.clientError(400, "Age must be a positive number between 1 and 100.")
        }

        return String(ageValue)
    }
}
