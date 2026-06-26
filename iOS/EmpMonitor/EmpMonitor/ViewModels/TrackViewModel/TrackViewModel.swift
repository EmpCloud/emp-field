//
//  TrackViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 26/09/24.
//

import Foundation

class TrackViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    static let shared = TrackViewModel()
    
    @Published var trackRequestData: [TrackRequestModelData] = []
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.getLocation
    }
    
    func trackUser() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = UserDefaults.standard.string(forKey: "x-access-token")
        
        let body = trackRequestData
        
        do{
            let fetchData: TrackResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: TrackResponseModel.self, accessToken: token)

            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message

            // Persist updated tracking settings so PermissionManager picks them up next cycle
            let frequency = fetchData.body.data.currentFrequency
            let radius = fetchData.body.data.currentRadius
            if frequency > 0 {
                UserDefaults.standard.set(frequency, forKey: "CurrentFrequency")
            }
            if radius > 0 {
                UserDefaults.standard.set(radius, forKey: "CurrentRadius")
            }

            print("Track response data")
            print(fetchData)

        }catch {
            print("Error: tracking user error \(error.localizedDescription)")
            self.error = error
        }
    }
}
