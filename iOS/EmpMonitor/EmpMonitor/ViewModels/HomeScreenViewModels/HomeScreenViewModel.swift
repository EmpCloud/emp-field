//
//  HomeScreenViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 12/08/24.
//

import Foundation

@MainActor
class HomeScreenViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var checkINTime: String = ""
    @Published var checkOUTTime: String = ""
    @Published var checkData: CheckINData?
    @Published var homeScreenData: HomeScreenResponseDetail?
    @Published var yesterdayDist: String = "--"
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.attendance
    }
    
    
    func getHomeScreenData() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = UserDefaults.standard.string(forKey: "x-access-token")
        
        do{
            let fetchData: HomeScreenResponseModel = try await NetworkManager.shared.getData(to: urlString, as: HomeScreenResponseModel.self, accessToken: token)
            checkINTime = fetchData.body.data?.data.data.checkIn ?? "--:--"
            checkOUTTime = fetchData.body.data?.data.data.checkOut ?? "--:--"
            checkData = fetchData.body.data?.data
            homeScreenData = fetchData.body.data
            yesterdayDist = getYesterdayDist(distance: homeScreenData?.yesterdayDist)
            print("Home Screen Data")
            print(homeScreenData)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            UserDefaults.standard.set(fetchData.body.data?.currentFrequency, forKey: "CurrentFrequency")
            UserDefaults.standard.set(fetchData.body.data?.currentRadius, forKey: "CurrentRadius")
            
            
        }catch{
            self.error = error
        }
    }
    
    
    func getYesterdayDist(distance: StringOrDouble?) -> String {
        if let distance = distance {
            switch distance {
            case .double(let doubleValue):
                // Round off to 2 decimal places
                return String(format: "%.2f", doubleValue)
            case .string(let stringValue):
                return stringValue
            }
        }
         return "--"
    }
}

