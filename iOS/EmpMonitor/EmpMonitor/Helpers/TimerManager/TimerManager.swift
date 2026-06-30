//
//  TimerManager.swift
//  EmpMonitor
//

import Foundation

@MainActor
final class TimerManager: ObservableObject {
    
    @Published var activeTime: TimeInterval = 0
    @Published var isActiveTimeRunning: Bool?
    
    private var activeTimer: Timer?
    
    deinit {
        activeTimer?.invalidate()
    }
    
    //MARK: Start Active Timer
    func startActiveTimer() {
        activeTimer?.invalidate()
        isActiveTimeRunning = true
        activeTimer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: true) { [weak self] _ in
            Task { @MainActor [weak self] in
                guard let self else { return }
                self.activeTime += 1
            }
        }
    }
    
    //MARK: Stop Active Timer
    func stopActiveTimer() {
        activeTimer?.invalidate()
        activeTimer = nil
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
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        
        guard let checkINDate = dateFormatter.date(from: checkINTimeString) else {
            return nil
        }
        
        if let checkOUTTimeString = checkOUTTimeString,
           let checkOUTDate = dateFormatter.date(from: checkOUTTimeString) {
            return checkOUTDate.timeIntervalSince(checkINDate)
        } else {
            let currentDate = Date()
            return currentDate.timeIntervalSince(checkINDate)
        }
    }
}
