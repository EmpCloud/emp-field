//
//  UploadFilesViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 28/08/24.
//

import Foundation

@MainActor
class UploadFilesViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var selectedPDF: [URL] = []
    @Published var selectedImageURLs: [URL] = []
    @Published var fetchedURL: [FilesURL] = []
    @Published var fetchProfileURL: String = ""
 
    var urlString = Constants.shared.baseURL + Constants.Endpoint.uploadTaskFiles
    
    var profileURLString = Constants.shared.baseURL + Constants.Endpoint.clientUploadProfileImage
    
    var userProfileURLString = Constants.shared.baseURL + Constants.Endpoint.uploadProfileImage
    
    func upload() async {
        isLoading = true
        defer { isLoading = false }
        
        let accessToken = AuthStore.shared.getAccessToken()
        
        guard let token = accessToken else { return }
        
        do {
            
            fetchedURL.removeAll() // to make sure each time it is empty to fill new data
            
            let fetchData: FileUploadResponseModel = try await FileUploadService.shared.uploadFile(to: urlString, files: selectedPDF, accessToken: token)
            
            fetchedURL = fetchData.data.filesUrls
            
        }catch{
            AppLog.debug("Error: Uploading failed- \(error.localizedDescription)")
            self.error = error
        }

    }
    
    func uploadImages() async {
        isLoading = true
        defer { isLoading = false }
        
        let accessToken = AuthStore.shared.getAccessToken()
        
        guard let token = accessToken else { return }
        
        do {
            
            fetchedURL.removeAll() // to make sure each time it is empty to fill new data
            
            let fetchData: FileUploadResponseModel = try await FileUploadService.shared.uploadImage(to: urlString, files: selectedImageURLs, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchData.code
            NetworkManager.shared.responseMessage = fetchData.data.filesUrls.first?.message ?? ""
            
            fetchedURL = fetchData.data.filesUrls
            
        }catch{
            AppLog.debug("Error: Uploading failed- \(error.localizedDescription)")
            self.error = error
        }

    }
    
    func uploadProfileImages() async {
        isLoading = true
        defer { isLoading = false }
        fetchProfileURL = ""
        error = nil
        
        let accessToken = AuthStore.shared.getAccessToken()
        
        guard let token = accessToken else { return }
        
        do {
            
            fetchedURL.removeAll() // to make sure each time it is empty to fill new data
            
            let fetchData: ProfileUploadResponseModel = try await FileUploadService.shared.uploadProfileImage(to: profileURLString, files: selectedImageURLs, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            fetchProfileURL = fetchData.body.data.profileURL
            
        }catch{
            AppLog.debug("Error: Uploading failed- \(error)")
            self.error = error
        }

    }
    
    
    
    func uploadUserProfileImages() async {
        isLoading = true
        defer { isLoading = false }
        fetchProfileURL = ""
        error = nil
        
        let accessToken = AuthStore.shared.getAccessToken()
        
        guard let token = accessToken else { return }
        
        do {
            
            fetchedURL.removeAll() // to make sure each time it is empty to fill new data
            
            let fetchData: ProfileUploadResponseModel = try await FileUploadService.shared.uploadProfileImage(to: userProfileURLString, files: selectedImageURLs, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            fetchProfileURL = fetchData.body.data.profileURL
            
            //to update the ProfilePic
            UserDefaults.standard.setValue(fetchData.body.data.profileURL, forKey: "UserProfilePic")
            //to update the profile pic
            ProfileHelper.shared.updateProfilePic(with: URL(string: UserDefaults.standard.string(forKey: "UserProfilePic") ?? ""))
            
        }catch{
            AppLog.debug("Error: Uploading failed- \(error)")
            self.error = error
        }

    }

}
