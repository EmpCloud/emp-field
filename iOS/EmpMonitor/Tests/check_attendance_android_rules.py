"""Check iOS attendance labels against Android AttendanceAdapter rules."""

from pathlib import Path
import re
import subprocess
import tempfile


view = Path(__file__).parents[1] / "EmpMonitor/Views/HomeScreens/TabMainScreen/HomeViews/AttendanceHistoryView.swift"
text = view.read_text()
start = text.index("    private func checkAttendanceStatus")
end = text.index("    private func applyColor", start)
methods = text[start:end].replace("private func", "func")

source = """import Foundation

struct MinHour { let manualHours: Int }
struct OpenRequest { }
struct OpenAttendanceRequest { let requestStatus: String }
struct Attendance {
    let date: String
    let loggedDuration: Int?
    let minHours: [MinHour]
    let leaveType: Int?
    let leaveName: String?
    let openRequest: OpenRequest?
    let holidayName: String?
    let holidayStatus: Int
    let startTime: String?
    let endTime: String?
    let dayOff: Bool
    let halfDayLeave: String?
    let openAttendanceRequest: OpenAttendanceRequest?
}
struct FormatterHelper {
    static let shared = FormatterHelper()
    func date(from value: String) -> Date? {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        formatter.locale = Locale(identifier: "en_US_POSIX")
        return formatter.date(from: value)
    }
}
enum AppLog { static func debug(_ value: @autoclosure () -> String) { } }
struct AttendanceHistoryView {
""" + methods + r"""
}

func record(_ date: String = "2020-01-01", duration: Int? = nil, start: String? = nil, end: String? = nil, dayOff: Bool = true, leaveType: Int? = 0, leaveName: String? = nil, holiday: String? = nil, holidayStatus: Int = 0, request: OpenAttendanceRequest? = nil, openLeave: OpenRequest? = nil, halfLeave: String? = nil) -> Attendance {
    Attendance(date: date, loggedDuration: duration, minHours: [MinHour(manualHours: 28800)], leaveType: leaveType, leaveName: leaveName, openRequest: openLeave, holidayName: holiday, holidayStatus: holidayStatus, startTime: start, endTime: end, dayOff: dayOff, halfDayLeave: halfLeave, openAttendanceRequest: request)
}
let view = AttendanceHistoryView()
assert(view.checkAttendanceStatus(attendance: record(duration: 14400, start: "09:00", end: "13:00")) == "Half Day Present")
assert(view.checkAttendanceStatus(attendance: record(duration: 28800, start: "09:00", end: "17:00")) == "Present")
assert(view.checkAttendanceStatus(attendance: record(duration: 100, start: "09:00", end: "09:01")) == "Absent")
assert(view.checkAttendanceStatus(attendance: record(start: "09:00")) == "Absent")
assert(view.checkAttendanceStatus(attendance: record(start: "09:00", leaveType: 1)) == "Half Day Present")
assert(view.checkAttendanceStatus(attendance: record(end: "17:00")) == "Half Day Present")
assert(view.checkAttendanceStatus(attendance: record(dayOff: false)) == "Week Off")
assert(view.checkAttendanceStatus(attendance: record(holiday: "Holiday", holidayStatus: 1)) == "Holiday")
assert(view.checkAttendanceStatus(attendance: record("2999-01-01")) == "-- --")
assert(view.checkAttendanceStatus(attendance: record(leaveType: 1, leaveName: "Annual Leave")) == "Annual Leave")
assert(view.checkAttendanceStatus(attendance: record(duration: 28800, start: "09:00", end: "17:00", request: OpenAttendanceRequest(requestStatus: "3"))) == "Pending")
print("Android attendance rule checks passed.")
"""

with tempfile.TemporaryDirectory(prefix="emp-attendance-check-") as scratch:
    directory = Path(scratch)
    swift = directory / "main.swift"
    swift.write_text(source)
    binary = directory / "checks"
    subprocess.run(["xcrun", "swiftc", "-module-cache-path", str(directory / "cache"), str(swift), "-o", str(binary)], check=True)
    subprocess.run([str(binary)], check=True)
