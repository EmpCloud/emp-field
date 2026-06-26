//
//  TimerManager.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/08/24.
//

import Foundation

@MainActor
class TimerManager: ObservableObject {
    
    @Published var activeTime: TimeInterval = 0
    @Published var isActiveTimeRunning: Bool?
    
    
    private var activeTimer: Timer?
    
    //MARK: Start Active Timer
    func startActiveTimer() {
        activeTimer?.invalidate()
        isActiveTimeRunning = true
        activeTimer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: true) { _ in
            self.activeTime += 1
        }
    }
    
    //MARK: Stop Active Timer
    func stopActiveTimer() {
        activeTimer?.invalidate()
        isActiveTimeRunning = false
    }
    
    func getActiveTime() -> TimeInterval {
        return activeTime
    }
    func isTimerRunning() -> Bool {
        return isActiveTimeRunning ?? false
    }
    
    //MARK: To format the Time
    func timeString(from time: TimeInterval) -> String {
        let hours = Int(time) / 3600
        let minutes = Int(time) % 3600 / 60
        let seconds = Int(time) % 60
        return String(format: "%02d:%02d:%02d", hours, minutes, seconds)
    }
    
    //MARK: TO calculate the time difference
    func timeDifference(from checkINTimeString: String, to checkOUTTimeString: String?) -> TimeInterval? {
        
        //create a DateFormatter to parse the input String
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        
        //Convert the input string to a date Object
        guard let checkINDate = dateFormatter.date(from: checkINTimeString) else {
            return nil // return nill if the string couldn't be parsed
        }
        
        //Check if check-out time is present
        if let checkOUTTimeString = checkOUTTimeString,
           let checkOUTDate = dateFormatter.date(from: checkOUTTimeString) {
            //calculate the time difference between check-in/out time
            return checkOUTDate.timeIntervalSince(checkINDate)
            
        }else{
            //Get the current Date and time
            let currentDate = Date()
            
            //Calculate the time differnce in seconds
            let timeDifference = currentDate.timeIntervalSince(checkINDate)
            return timeDifference
        }
        
        
    }
}
