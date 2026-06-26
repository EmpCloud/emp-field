//
//  HelperFunction.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import Foundation
import UIKit
import SwiftUI
import CoreLocation

class HelperFunction {
    
    static let shared = HelperFunction()
    
    func openAppSetting() {
        if let url = URL(string: UIApplication.openSettingsURLString),
           UIApplication.shared.canOpenURL(url) {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        }
    }
    
    func calculateDistance(from userLocation: CLLocationCoordinate2D, to clientLocation: CLLocationCoordinate2D) -> Int  {
        let userLocation = CLLocation(latitude: userLocation.latitude, longitude: userLocation.longitude)
        let clientLocation = CLLocation(latitude: clientLocation.latitude, longitude: clientLocation.longitude)
        
        let distanceInMeters = userLocation.distance(from: clientLocation)
        
        return Int(distanceInMeters)
    }
    
    func currentTime() -> String {
        let date = Date()
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm:ss"
        let currentDeviceTime = formatter.string(from: date)
        return currentDeviceTime
    }
    
    func todaysDate() -> String {
        let date = Date()
        let formatter = DateFormatter()
        formatter.dateFormat = "E, MMM d"
        let todayDate = formatter.string(from: date)
        return todayDate
    }
    
    func getYesterdayDateString() -> String {
        let calendar = Calendar.current
        let today = Date()
        
        if let yesterday = calendar.date(byAdding: .day, value: -1, to: today) {
            let formatter = DateFormatter()
            formatter.dateFormat = "E, MMM d"
            return formatter.string(from: yesterday)
        } else {
            return ""
        }
    }

    
    func formatCheckTime(from checkTime: String) -> String? {
        let inputDateFormatter = DateFormatter()
        inputDateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        
        let outputDateFormatter = DateFormatter()
        outputDateFormatter.dateFormat = "HH:mm"
        
        if let date = inputDateFormatter.date(from: checkTime) {
            return outputDateFormatter.string(from: date)
        }else{
            return nil // return nil if the input date string is not valid or not present
        }
    }
    //to get today's date
    func todaysDateInYMD() -> String {
        let date = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        let todayDate = dateFormatter.string(from: date)
        return todayDate
    }
    
    //take todays date and give date 30 day after
    func dateAfter30Days(from dateString: String) -> String? {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        
        // Convert the input string to a Date object
        if let date = dateFormatter.date(from: dateString) {
            // Add 30 days to the date
            let newDate = Calendar.current.date(byAdding: .day, value: 30, to: date)
            
            // Convert the new Date object back to a string
            if let newDate = newDate {
                return dateFormatter.string(from: newDate)
            }
        }
        
        return nil // Return nil if the input date string is not valid
    }
    
    //First date of the current month
    func getFirstDateOfCurrentMonth() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        
        let calendar = Calendar.current
        let currentDate = Date()
        
        // Get the components of the current date
        let components = calendar.dateComponents([.year, .month], from: currentDate)
        
        // Get the first day of the current month
        if let firstDateOfMonth = calendar.date(from: components) {
            return dateFormatter.string(from: firstDateOfMonth)
        }
        
        // Return an empty string in case of failure
        return ""
    }
    
    func validateAgeInput(inputAge: String) -> String{
        if let age = Int(inputAge) {
            if age > 100 {
                return "100"
            }else if age < 0{
                return "0"
            }else{
                return ""
            }
        }else{
            return ""
        }
    }
}
