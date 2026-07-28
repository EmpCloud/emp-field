//
//  EditAttendanceViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 27/08/24.
//

import Foundation

@MainActor
class EditAttendanceViewModel: ObservableObject {
    @Published var isLoading: Bool = true
    @Published var error: Error?
    
    @Published var date: String = ""
    @Published var checkIN: String = ""
    @Published var checkOUT: String = ""
    @Published var reason: String = ""
    
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.attendanceRequest
    }
    
    
    func editAttendanceData() async {
        isLoading = true
        defer { isLoading = false }
        
        let requestDate = FormatterHelper.shared.apiDateString(from: date)
        let body = EditAttendanceRequestModel(date: requestDate, checkIn: checkIN, checkOut: checkOUT, reason: reason)
        
        let token = AuthStore.shared.getAccessToken()
        
        do {
            let fetchData: EditAttendanceResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: EditAttendanceResponseModel.self, accessToken: token)
            
            NetworkManager.shared.responseMessage = fetchData.body.message
            NetworkManager.shared.statusCode = fetchData.statusCode
            
//            AppLog.debug("Edit Attendance Data: \(fetchData)")
            
        }catch {
            AppLog.debug("Error: editAttendanceData error :\(error)")
            self.error = error
        }
    }
    
    //MARK: To combine and convert date and time in appropriate format
    
    func formatToISO8601(dateString: String, timeString: String) -> String? {
        // Define the date and time formatters
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm"
        dateFormatter.timeZone = TimeZone.current
        
        // Combine date and time into a single string
        let combinedString = "\(FormatterHelper.shared.apiDateString(from: dateString)) \(timeString)"
//        AppLog.debug("Comnined String: \(combinedString)")
        
        // Convert the combined string into a Date object
        guard let date = dateFormatter.date(from: combinedString) else {
            return nil
        }
        
        // Format the Date object into the desired ISO 8601 format
        let isoFormatter = ISO8601DateFormatter()
        isoFormatter.timeZone = TimeZone(secondsFromGMT: 0) // UTC
        isoFormatter.formatOptions = [.withInternetDateTime, .withFractionalSeconds]
        
        let iso8601String = isoFormatter.string(from: date)
        
        return iso8601String
    }
}
