//
//  AddClientViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/08/24.
//

import Foundation

@MainActor
class AddClientViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var clientName: String = ""
    @Published var emailID: String = ""
    @Published var contactNumber: String = ""
    @Published var clientProfilePic: String = ""
    @Published var category: String = ""
    @Published var countryCode: String = ""
    @Published var address1: String = ""
    @Published var address2: String = ""
    @Published var country: String = ""
    @Published var state: String = ""
    @Published var city: String = ""
    @Published var zipCode: String = ""
    @Published var latitude: Double = 0
    @Published var longitude: Double = 0
    
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.createClient
    }
    
    func addClient() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        let body = AddClientRequestModel(clientName: clientName, emailID: emailID, contactNumber: contactNumber, clientProfilePic: clientProfilePic, category: category, countryCode: countryCode, address1: address1, address2: address2, country: country, state: state, city: city, zipCode: zipCode, latitude: latitude, longitude: longitude)
        
        AppLog.debug("Create Client Body:")
        AppLog.debug(body)
        do{
            let fetchedData: AddClientResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: AddClientResponseModel.self, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchedData.statusCode
            NetworkManager.shared.responseMessage = fetchedData.body.message
            NetworkManager.shared.errorMessage = fetchedData.body.error ?? ""
            
        }catch{
            AppLog.debug("Error: AddClient error -> \(error)")
            self.error = error
        }
    }
}
