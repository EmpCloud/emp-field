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

    var hasOpenCheckIn: Bool {
        Self.hasOpenCheckIn(checkData?.data)
    }
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.attendance
    }
    
    
    func getHomeScreenData() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        do{
            let fetchData: HomeScreenResponseModel = try await NetworkManager.shared.getData(to: urlString, as: HomeScreenResponseModel.self, accessToken: token)
            let attendance = fetchData.body.data?.data.data
            checkINTime = attendance?.checkIn ?? "--:--"
            checkOUTTime = attendance?.checkOut ?? "--:--"
            checkData = fetchData.body.data?.data
            homeScreenData = fetchData.body.data
            yesterdayDist = getYesterdayDist(distance: homeScreenData?.yesterdayDist)
            AppLog.debug("Home Screen Data")
            AppLog.debug(homeScreenData)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            UserDefaults.standard.set(fetchData.body.data?.currentFrequency, forKey: "CurrentFrequency")
            UserDefaults.standard.set(fetchData.body.data?.currentRadius, forKey: "CurrentRadius")
            UserDefaults.standard.set(Self.hasOpenCheckIn(attendance), forKey: "isCheckedIN")
            
            
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

    static func hasOpenCheckIn(_ attendance: CheckINDetailData?) -> Bool {
        guard isBlankAttendanceValue(attendance?.checkIn) == false else {
            return false
        }

        return isBlankAttendanceValue(attendance?.checkOut)
    }

    private static func isBlankAttendanceValue(_ value: String?) -> Bool {
        guard let value = value?.trimmingCharacters(in: .whitespacesAndNewlines) else {
            return true
        }

        return value.isEmpty || value == "--:--" || value.lowercased() == "null"
    }
}
