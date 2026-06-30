//
//  HolidaysViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/07/24.
//

import Foundation

@MainActor
class HolidaysViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    @Published var fetchStatusCode: Int = 0

    @Published var holidaysData: [HolidaysResponseData] = []
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.getHoliday
    }
    
    func getHolidays() async {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        do{
            let holidaysResponse: HolidaysResponseModel = try await NetworkManager.shared.getData(to: urlString, as: HolidaysResponseModel.self, accessToken: token)
            
            NetworkManager.shared.responseMessage = holidaysResponse.body.message
            NetworkManager.shared.statusCode = holidaysResponse.statusCode
            fetchStatusCode = holidaysResponse.statusCode

            holidaysData = holidaysResponse.body.data ?? []
            
        }catch{
            print("Error: Holiday Fetch Error")
            self.error = error
        }
    }
}
