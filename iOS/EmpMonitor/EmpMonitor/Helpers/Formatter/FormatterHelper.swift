//
//  FormatterHelper.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 29/07/24.
//

import Foundation

class FormatterHelper {

    static let shared = FormatterHelper()

    // MARK: - Cached formatters
    //
    // `DateFormatter` is expensive to create (locale/timezone/ICU setup), so these
    // helpers previously allocated one (sometimes several) per call — costly when
    // called repeatedly while rendering lists/calendars.
    //
    // Each formatter below is configured exactly once and never mutated afterwards,
    // so the output is byte-for-byte identical to the previous per-call instances.
    // Reusing an immutably-configured DateFormatter for parsing/formatting is
    // thread-safe on iOS 7+, so sharing them across the singleton is safe.

    private static let posix = Locale(identifier: "en_US_POSIX")
    private static let utc = TimeZone(abbreviation: "UTC")

    private static func make(_ format: String, locale: Locale? = nil, timeZone: TimeZone? = nil) -> DateFormatter {
        let formatter = DateFormatter()
        formatter.dateFormat = format
        if let locale { formatter.locale = locale }
        if let timeZone { formatter.timeZone = timeZone }
        return formatter
    }

    /// ISO 8601 with fractional seconds, e.g. "2024-12-25T00:00:00.000Z".
    private let iso8601Fractional: ISO8601DateFormatter = {
        let formatter = ISO8601DateFormatter()
        formatter.formatOptions = [.withInternetDateTime, .withFractionalSeconds]
        return formatter
    }()

    // "yyyy-MM-dd" in the current time zone (DateFormatter's default time zone is
    // already TimeZone.current, so the explicit-`.current` and default uses collapse
    // to this single instance).
    private let ymd = FormatterHelper.make("yyyy-MM-dd")
    private let ymdUTC = FormatterHelper.make("yyyy-MM-dd", timeZone: FormatterHelper.utc)
    private let ymdPosix = FormatterHelper.make("yyyy-MM-dd", locale: FormatterHelper.posix)
    private let monthDayPosix = FormatterHelper.make("MMM, \n dd", locale: FormatterHelper.posix, timeZone: .current)
    private let dateTimeFullPosixCurrent = FormatterHelper.make("yyyy-MM-dd HH:mm:ss", locale: FormatterHelper.posix, timeZone: .current)
    private let isoZuluUTC = FormatterHelper.make("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timeZone: FormatterHelper.utc)
    private let ddMMyyCurrent = FormatterHelper.make("dd-MM-yy", timeZone: .current)
    private let ddMMyyyyCurrent = FormatterHelper.make("dd-MM-yyyy", timeZone: .current)
    private let dayDateCurrent = FormatterHelper.make("E, dd-MM-yyyy", timeZone: .current)
    private let dateTimeNoTZ = FormatterHelper.make("yyyy-MM-dd HH:mm:ss")
    private let ymdHmNoTZ = FormatterHelper.make("yyyy-MM-dd HH:mm")
    private let hmmA: DateFormatter = {
        let formatter = FormatterHelper.make("hh:mm a")
        formatter.amSymbol = "AM"
        formatter.pmSymbol = "PM"
        return formatter
    }()

    //MARK: Return True if the Date is Future date (input format: "2024-12-25T00:00:00.000Z")
    func isFutureDate(dateString: String) -> Bool {
        //Convert the date String to a Date object
        if let inputDate = iso8601Fractional.date(from: dateString){
            let currentDate = Date() //Current date

            //Compare the input date with current date
            return inputDate > currentDate
        }else{
            //if the date string is invalid, return false
            AppLog.debug("Invalid date format")
            return false
        }
    }


    //MARK: Today's Date - "2024-12-25"
    func getTodaysDate() -> String {
        let today = Date()  // Get today's date
        return ymd.string(from: today)  // Convert date to string with the desired format
    }

    //MARK: Today's Date "2024-12-25" -> "Oct, 10"
    func getTodaysMonthDay(from dateString: String) -> String? {  // to pass todays date in string format
        if let date = ymdPosix.date(from: dateString) {
            return monthDayPosix.string(from: date).uppercased()
        } else {
            AppLog.debug("Error: Invalid date string format.")
            return nil
        }
    }

    //MARK: Today's Date  -> "2024-12-25 02:35:00"
    func getCurrentDateTimeFormatted() -> String {
        let currentDate = Date()
        return dateTimeFullPosixCurrent.string(from: currentDate)
    }



    //MARK: "01-01-24"
    func formattedDate(from isoDateString: String) -> String {
        // Input format
        guard let date = isoZuluUTC.date(from: isoDateString)
        else {
            guard let date = ymd.date(from: isoDateString) else{
                return ""
            }
            // Output format
            return ddMMyyCurrent.string(from: date)
        }

        // Output format
        return ddMMyyCurrent.string(from: date)
    }

    //MARK: "01-01-2024"
    func formattedFullYearDate(from isoDateString: String) -> String {
        // Input format
        guard let date = isoZuluUTC.date(from: isoDateString)
        else {
            guard let date = ymd.date(from: isoDateString) else{
                return ""
            }
            // Output format
            return ddMMyyyyCurrent.string(from: date)
        }

        // Output format
        return ddMMyyyyCurrent.string(from: date)
    }

    //MARK: "2024-01-01"
    func formattedDateReverse(from isoDateString: String) -> String {
        // Input format
        guard let date = isoZuluUTC.date(from: isoDateString)
        else {
            guard let date = ymd.date(from: isoDateString) else{
                return ""
            }
            // Output format
            return ymd.string(from: date)
        }

        // Output format
        return ymd.string(from: date)
    }


    //MARK: "Mon, 01-01-2024"
    func formattedDateWithDay(from dateString: String) -> String {
        // Try plain date first (e.g. "2026-04-14" from holiday endpoint)
        if let date = ymdUTC.date(from: dateString) {
            return dayDateCurrent.string(from: date)
        }

        // Fall back to ISO 8601 with time (e.g. "2026-06-01T00:00:00.000Z" from attendance endpoint)
        if let date = isoZuluUTC.date(from: dateString) {
            return dayDateCurrent.string(from: date)
        }

        return dateString
    }


    //MARK: "2024-08-12 17:11:09"  ==> "09:55 AM"
    func checkTimeFormatter(from dateTimeString: String) -> String? {
        // Convert the input string to a Date object
        if let date = dateTimeNoTZ.date(from: dateTimeString) {
            // Convert the Date object back to a string in the "hh:mm a" format
            return hmmA.string(from: date)
        }

        return nil // Return nil if the input date string is not valid
    }


    //MARK: "2024-09-12" & "02:00 AM"  ==> "2023-12-25 20:01:44"
    func formatDateAndTime(dateString: String, timeString: String) -> String? {
        // Combine date and time strings into one string
        let combinedString = "\(dateString) \(timeString)"

        // Convert the combined string to Date object (expecting 24-hour format)
        if let date = ymdHmNoTZ.date(from: combinedString) {
            // Convert Date object back to string in the desired format
            let formattedString = dateTimeNoTZ.string(from: date)
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

        //convert the string to a date object
        guard let date = ymdHmNoTZ.date(from: dateTimeString) else {
            AppLog.debug("Error: Invalid time format")
            return nil
        }


        //format the date object into the desired "yyyy-MM-dd HH:mm:ss" format
        let formattedDate = dateTimeNoTZ.string(from: date)

        return formattedDate
    }
}
