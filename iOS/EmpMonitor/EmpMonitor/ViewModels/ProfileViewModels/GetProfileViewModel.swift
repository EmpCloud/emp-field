//
//  GetProfileViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 03/09/24.
//

import Foundation

@MainActor
class GetProfileViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var profileDetail: [ProfileResponseModelDetail] = []
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.fetchProfile
    }
    
    func getProfile() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        do {
            let fetchData: ProfileResponseModel = try await NetworkManager.shared.getData(to: urlString, as: ProfileResponseModel.self, accessToken: token)
            
            profileDetail = fetchData.body.data.resultData
            
            // Persist the fetched profile so the app can route directly to Home on next launch.
            if !fetchData.body.data.resultData.isEmpty {
                AuthStore.shared.saveUserProfileData(fetchData)
            }
            
            // Updating & Storing users profile for later use and direct navigation to homescreen
//            UserDefaults.standard.setObject(fetchData, forKey: "UserProfile")         // since department is not there it casuing the deletion of "UserProfile" in UserDefault
            
//            AppLog.debug("Get Profile fetch Data")
//            AppLog.debug(fetchData)
            
            // to store userProfile Data
            UserDefaults.standard.setValue(fetchData.body.data.resultData.first?.fullName, forKey: "UserName")
//            UserDefaults.standard.setValue(fetchData.body.data.resultData.first?., forKey: "UserDepartment")
            UserDefaults.standard.setValue(fetchData.body.data.resultData.first?.profilePic, forKey: "UserProfilePic")
            
            
            ProfileHelper.shared.updateProfilePic(with: URL(string: fetchData.body.data.resultData.first?.profilePic ?? ""))
            
        }catch {
            AppLog.debug("Error: Get Profile error -> \(error.localizedDescription)")
            self.error = error
        }
    }
}
