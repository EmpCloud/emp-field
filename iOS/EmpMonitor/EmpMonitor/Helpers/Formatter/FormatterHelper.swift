//
//  FormatterHelper.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 29/07/24.
//

import Foundation

class FormatterHelper {
    
    static let shared = FormatterHelper()
    
    //MARK: Return True if the Date is Future date (input format: "2024-12-25T00:00:00.000Z")
    func isFutureDate(dateString: String) -> Bool {
        //Define the date format
        let dateFormatter = ISO8601DateFormatter()
        dateFormatter.formatOptions = [.withInternetDateTime, .withFractionalSeconds]
        
        //Convert the date String to a Date object
        if let inputDate = dateFormatter.date(from: dateString){
            let currentDate = Date() //Current date
            
            //Compare the input date with current date
            return inputDate > currentDate
        }else{
            //if the date string is invalid, return false
            print("Invalid date format")
            return false
        }
    }
    
    
    //MARK: Today's Date - "2024-12-25"
    func getTodaysDate() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"  // Setting the desired date format
        let today = Date()  // Get today's date
        return dateFormatter.string(from: today)  // Convert date to string with the desired format
    }
    
    //MARK: Today's Date "2024-12-25" -> "Oct, 10"
    func getTodaysMonthDay(from dateString: String) -> String? {  // to pass todays date in string format
        let inputFormatter = DateFormatter()
        inputFormatter.dateFormat = "yyyy-MM-dd"  // Input date format (adjust as needed)
        inputFormatter.locale = Locale(identifier: "en_US_POSIX") // Ensure consistent locale
        
        if let date = inputFormatter.date(from: dateString) {
            let outputFormatter = DateFormatter()
            outputFormatter.dateFormat = "MMM, \n dd"  // Desired output format
            outputFormatter.locale = Locale(identifier: "en_US_POSIX")
            outputFormatter.timeZone = TimeZone.current
            return outputFormatter.string(from: date).uppercased()
        } else {
            print("Error: Invalid date string format.")
            return nil
        }
    }
    
    //MARK: Today's Date  -> "2024-12-25 02:35:00"
    func getCurrentDateTimeFormatted() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        dateFormatter.timeZone = TimeZone.current
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        
        let currentDate = Date()
        return dateFormatter.string(from: currentDate)
    }


    
    //MARK: "01-01-24"
    func formattedDate(from isoDateString: String) -> String {
        let dateFormatter = DateFormatter()
        
        // Input format
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
        guard let date = dateFormatter.date(from: isoDateString) 
        else {
            let formatter = DateFormatter()
            formatter.dateFormat = "yyyy-MM-dd"
            guard let date = formatter.date(from: isoDateString) else{
                return ""
            }
            // Output format
            formatter.dateFormat = "dd-MM-yy"
            return formatter.string(from: date)
        }
        
        // Output format
        dateFormatter.dateFormat = "dd-MM-yy"
        dateFormatter.timeZone = TimeZone.current
        
        return dateFormatter.string(from: date)
    }
    
    //MARK: "01-01-2024"
    func formattedFullYearDate(from isoDateString: String) -> String {
        let dateFormatter = DateFormatter()
        
        // Input format
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
        guard let date = dateFormatter.date(from: isoDateString)
        else {
            let formatter = DateFormatter()
            formatter.dateFormat = "yyyy-MM-dd"
            guard let date = formatter.date(from: isoDateString) else{
                return ""
            }
            // Output format
            formatter.dateFormat = "dd-MM-yyyy"
            return formatter.string(from: date)
        }
        
        // Output format
        dateFormatter.dateFormat = "dd-MM-yyyy"
        dateFormatter.timeZone = TimeZone.current
        
        return dateFormatter.string(from: date)
    }
    
    //MARK: "2024-01-01"
    func formattedDateReverse(from isoDateString: String) -> String {
        let dateFormatter = DateFormatter()
        
        // Input format
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
        guard let date = dateFormatter.date(from: isoDateString)
        else {
            let formatter = DateFormatter()
            formatter.dateFormat = "yyyy-MM-dd"
            guard let date = formatter.date(from: isoDateString) else{
                return ""
            }
            // Output format
            formatter.dateFormat = "yyyy-MM-dd"
            return formatter.string(from: date)
        }
        
        // Output format
        dateFormatter.dateFormat = "yyyy-MM-dd"
        dateFormatter.timeZone = TimeZone.current
        
        return dateFormatter.string(from: date)
    }
    
    
    //MARK: "Mon, 01-01-2024"
    func formattedDateWithDay(from dateString: String) -> String {
        let outputFormatter = DateFormatter()
        outputFormatter.dateFormat = "E, dd-MM-yyyy"
        outputFormatter.timeZone = TimeZone.current

        // Try plain date first (e.g. "2026-04-14" from holiday endpoint)
        let plainFormatter = DateFormatter()
        plainFormatter.dateFormat = "yyyy-MM-dd"
        plainFormatter.timeZone = TimeZone(abbreviation: "UTC")
        if let date = plainFormatter.date(from: dateString) {
            return outputFormatter.string(from: date)
        }

        // Fall back to ISO 8601 with time (e.g. "2026-06-01T00:00:00.000Z" from attendance endpoint)
        let isoFormatter = DateFormatter()
        isoFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        isoFormatter.timeZone = TimeZone(abbreviation: "UTC")
        if let date = isoFormatter.date(from: dateString) {
            return outputFormatter.string(from: date)
        }

        return dateString
    }
    
    
    //MARK: "2024-08-12 17:11:09"  ==> "09:55 AM"
    func checkTimeFormatter(from dateTimeString: String) -> String? {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        
        // Convert the input string to a Date object
        if let date = dateFormatter.date(from: dateTimeString) {
            // Set the output date format to "hh:mm a"
            dateFormatter.dateFormat = "hh:mm a"
            dateFormatter.amSymbol = "AM"
            dateFormatter.pmSymbol = "PM"
            
            // Convert the Date object back to a string in the desired format
            return dateFormatter.string(from: date)
        }
        
        return nil // Return nil if the input date string is not valid
    }
    
    
    //MARK: "2024-09-12" & "02:00 AM"  ==> "2023-12-25 20:01:44"
    func formatDateAndTime(dateString: String, timeString: String) -> String? {
        // Combine date and time strings into one string
        let combinedString = "\(dateString) \(timeString)"
        
        // Define input format of date and time (expecting 24-hour format)
        let inputFormatter = DateFormatter()
        inputFormatter.dateFormat = "yyyy-MM-dd HH:mm" // For 24-hour format time
        
        // Convert the combined string to Date object
        if let date = inputFormatter.date(from: combinedString) {
            // Define output format
            let outputFormatter = DateFormatter()
            outputFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
            
            // Convert Date object back to string in the desired format
            let formattedString = outputFormatter.string(from: date)
            return formattedString
        }
        
        // Return nil if date parsing fails
        return nil
    }
    
    //MARK: Time input "19:20"  -> "2024-10-03 19:20:00"
    func getDateTimeJoined(with time: String) -> String? {
        //Get the current date
        let currentDate = Date()
        let calendar = Calendar.current
        
        //Extract the year, month and day from the current  date
        let year = calendar.component(.year, from: currentDate)
        let month = calendar.component(.month, from: currentDate)
        let day = calendar.component(.day, from: currentDate)
        
        //Combine the date components with the given time string
        let dateTimeString = String(format: "%04d-%02d-%02d %@", year, month, day, time)
        
        //Create a dateFormatter to parse the combined date and time string
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm"
        
        //convert the string to a date object
        guard let date = dateFormatter.date(from: dateTimeString) else {
            print("Error: Invalid time format")
            return nil
        }
        
        
        //format the date object into the desired "yyyy-MM-dd HH:mm:ss" format
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        let formattedDate = dateFormatter.string(from: date)
        
        return formattedDate
    }
}
