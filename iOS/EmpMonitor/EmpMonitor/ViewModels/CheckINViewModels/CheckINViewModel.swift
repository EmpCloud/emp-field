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
            applyAttendanceResponse(fetchData)
            AppLog.debug("CheckIN Data: ")
            AppLog.debug(fetchData)
        }catch {
            AppLog.debug("Error: CheckINViewModel Mark attendance error - \(error)")
            self.error = error
            applyFailureStatus(error)
            throw error
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
            applyAttendanceResponse(fetchData)
            AppLog.debug("CheckIN Data: ")
            AppLog.debug(fetchData)
        }catch {
            AppLog.debug("Error: CheckINViewModel Mark attendance error - \(error)")
            self.error = error
            applyFailureStatus(error)
            throw error
        }
    }

    private func applyAttendanceResponse(_ fetchData: CheckINResponseModel) {
        let attendanceCode = fetchData.body.data?.code ?? fetchData.statusCode
        let attendanceMessage = fetchData.body.data?.message ?? fetchData.body.message

        NetworkManager.shared.statusCode = attendanceCode
        NetworkManager.shared.responseMessage = attendanceMessage
        NetworkManager.shared.errorMessage = fetchData.body.message

        guard attendanceCode == 200 else {
            checkINTime = ""
            AppLog.debug("Attendance business error (\(attendanceCode)): \(attendanceMessage)")
            return
        }

        checkINTime = fetchData.body.data?.data?.time ?? ""
    }

    private func applyFailureStatus(_ error: Error) {
        guard let networkError = error as? NetworkError else {
            NetworkManager.shared.statusCode = 0
            NetworkManager.shared.responseMessage = error.localizedDescription
            return
        }

        switch networkError {
        case .unauthorized:
            NetworkManager.shared.statusCode = 401
            if NetworkManager.shared.responseMessage.isEmpty {
                NetworkManager.shared.responseMessage = networkError.errorDescription ?? "Unauthorized"
            }
        case .forbidden:
            NetworkManager.shared.statusCode = 403
            NetworkManager.shared.responseMessage = networkError.errorDescription ?? "Forbidden"
        case .clientError(let code, let message):
            NetworkManager.shared.statusCode = code
            NetworkManager.shared.responseMessage = message ?? networkError.errorDescription ?? "Request failed"
        case .serverError(let code):
            NetworkManager.shared.statusCode = code
            NetworkManager.shared.responseMessage = networkError.errorDescription ?? "Server Error (\(code))"
        case .unknown(let code):
            NetworkManager.shared.statusCode = code
            NetworkManager.shared.responseMessage = networkError.errorDescription ?? "Unexpected response (\(code))"
        default:
            NetworkManager.shared.statusCode = 0
            NetworkManager.shared.responseMessage = networkError.errorDescription ?? error.localizedDescription
        }
    }
}
