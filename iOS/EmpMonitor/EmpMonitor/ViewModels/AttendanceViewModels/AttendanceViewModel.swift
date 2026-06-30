//
//  File.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 29/07/24.
//

import Foundation

@MainActor
class AttendanceViewModel: ObservableObject {
    @Published var isLoading: Bool = true
    @Published var error: Error?
    
//    @Published var fetchStatusCode: Int = 0
    
    @Published var attendanceData: [Attendance] = []
    @Published var attendanceStartDate: String = ""
    @Published var attendanceEndDate: String = ""
    
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.fetchAttendance
    }
    
    
    func getAttendanceData() async {
        isLoading = true
        defer { isLoading = false }
        
        let body = AttendanceRequestModel(startDate: attendanceStartDate, endDate: attendanceEndDate)
        
        let token = AuthStore.shared.getAccessToken()
        
        do {
            let fetchData: AttendanceResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: AttendanceResponseModel.self, accessToken: token)
            
            NetworkManager.shared.responseMessage = fetchData.body.message
            NetworkManager.shared.statusCode = fetchData.statusCode
            
            attendanceData = fetchData.body.data?.first?.attendance ?? []
//            fetchStatusCode = fetchData.statusCode
            
//            print(attendanceData)
            
        }catch {
            print("Error: getAttendance error")
            self.error = error
        }
    }
}
