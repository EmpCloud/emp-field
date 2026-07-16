//
//  CheckINViewModels.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 12/08/24.
//

import Foundation

@MainActor
class CheckINViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var checkINTime: String = ""
    @Published var checkOUTTime: String = ""
    @Published var checkINLatitude: Double = 0
    @Published var checkINLongitude: Double = 0
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.markAttendance
    }
    
    func markAttendance() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        let body = CheckINRequestModel(time: checkINTime, latitude: checkINLatitude, longitude: checkINLongitude)
        
        AppLog.debug("Check in Request")
        AppLog.debug(body)
        
        do{
            let fetchData: CheckINResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: CheckINResponseModel.self, accessToken: token)
//            if fetchData.statusCode == 200 {
                checkINTime = fetchData.body.data?.data?.time ?? ""
//                NetworkManager.shared.statusCode = fetchData.body.data?.code ?? 0
                NetworkManager.shared.statusCode = fetchData.statusCode
//                NetworkManager.shared.responseMessage = fetchData.body.data?.message ?? ""
            NetworkManager.shared.responseMessage = fetchData.body.message
                AppLog.debug("CheckIN Data: ")
                AppLog.debug(fetchData)
//            }
            
        }catch {
            AppLog.debug("Error: CheckINViewModel Mark attendance error")
            self.error = error
        }
    }
    
    func markCheckOUTAttendance() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        let body = CheckINRequestModel(time: checkINTime, latitude: checkINLatitude, longitude: checkINLongitude)
        
        AppLog.debug("Check in Request")
        AppLog.debug(body)
        
        do{
            let fetchData: CheckINResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: CheckINResponseModel.self, accessToken: token)
            if fetchData.statusCode == 200 {
                checkINTime = fetchData.body.data?.data?.time ?? ""
                NetworkManager.shared.statusCode = fetchData.body.data?.code ?? 0
//                NetworkManager.shared.statusCode = fetchData.statusCode
                NetworkManager.shared.responseMessage = fetchData.body.data?.message ?? ""
//            NetworkManager.shared.responseMessage = fetchData.body.message
                AppLog.debug("CheckIN Data: ")
                AppLog.debug(fetchData)
            }
            
        }catch {
            AppLog.debug("Error: CheckINViewModel Mark attendance error")
            self.error = error
        }
    }
}
