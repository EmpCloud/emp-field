//
//  TrackViewModel.swift
//  EmpMonitor
//

import Foundation

@MainActor
final class TrackViewModel: ObservableObject {
    
    static let shared = TrackViewModel()
    
    @Published var isLoading: Bool = false
    @Published var error: Error?
    @Published var trackRequestData: [TrackRequestModelData] = []
    
    private init() { }
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.getLocation
    }
    
    func trackUser() async {
        isLoading = true
        defer { isLoading = false }
        
        let body = trackRequestData
        
        do {
            let fetchData: TrackResponseModel = try await NetworkManager.shared.postData(
                to: urlString,
                body: body,
                as: TrackResponseModel.self,
                accessToken: AuthStore.shared.getAccessToken()
            )
            
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
            
            AppLog.debug("Track response data")
            AppLog.debug(fetchData)
            
        } catch {
            AppLog.debug("Error: tracking user error \(error.localizedDescription)")
            self.error = error
            NetworkManager.shared.statusCode = (error as? NetworkError).map { _ in 0 } ?? 0
        }
    }
}
