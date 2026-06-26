//
//  UpdateClientViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 02/09/24.
//

import Foundation

@MainActor
class UpdateClientViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var clientName: String = ""
    @Published var clientID: String = ""
    @Published var emailID: String = ""
    @Published var contactNumber: String = ""
    @Published var clientProfilePic: String = ""
    @Published var category: String = ""
    @Published var countryCode: String = "+91"
    @Published var address1: String = ""
    @Published var address2: String = ""
    @Published var country: String = ""
    @Published var state: String = ""
    @Published var city: String = ""
    @Published var zipCode: String = ""
    @Published var latitude: Double = 0
    @Published var longitude: Double = 0
    
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.updateClient
    }
    
    func updateClient() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = UserDefaults.standard.string(forKey: "x-access-token")
        
        let body = UpdateClientRequestModel(clientName: clientName, emailID: emailID, contactNumber: contactNumber, clientProfilePic: clientProfilePic, category: category, countryCode: countryCode, address1: address1, address2: address2, country: country, state: state, city: city, zipCode: zipCode, latitude: latitude, longitude: longitude)
        print("Client ID: \(clientID)")
        do{
            let fetchedData: UpdateClientResponseModel = try await NetworkManager.shared.putData(to: urlString, body: body, as: UpdateClientResponseModel.self, accessToken: token, queryParams: clientID)
            
            NetworkManager.shared.statusCode = fetchedData.statusCode
            NetworkManager.shared.responseMessage = fetchedData.body.message
            
            
        }catch{
            print("Error: AddClient error -> \(error)")
            self.error = error
        }
    }
}

