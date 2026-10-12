"""Verify business-error decoding and leave retry behavior without calling the backend."""

from pathlib import Path
import subprocess
import tempfile


app = Path(__file__).parents[1] / "EmpMonitor"
files = [
    "Helpers/NetworkManagers/NetworkManager.swift",
    "Helpers/NetworkManagers/NetworkError.swift",
    "Helpers/HelperModel/JSONNull.swift",
    "Models/LeavesModels/CreateLeaveRequestModel.swift",
    "Models/LeavesModels/CreateLeaveResponseModel.swift",
    "ViewModels/LeavesViewModels/CreateLeaveViewModel.swift",
]
source = "import Foundation\nimport Combine\n" + "\n".join((app / file).read_text() for file in files)
source += r'''
enum AppLog {
    static func debug(_ message: @autoclosure () -> Any) {}
    static func apiResponse(_ data: Data, response: URLResponse, request: URLRequest) {}
}
@MainActor final class AppState {
    static let shared = AppState()
    func handleSessionExpired(message: String?) {}
}
struct AuthStore {
    static let shared = AuthStore()
    func getAccessToken() -> String? { nil }
}
struct LeaveHelper {
    static let shared = LeaveHelper()
    func getDayType(dayType: String) -> Int { 2 }
}
struct Constants {
    static let shared = Constants()
    let baseURL = "https://example.test"
    enum Endpoint { static let createLeave = "/leaves/create-leave" }
}
// Fixtures change only between fully awaited requests; no requests run concurrently.
final class FixtureProtocol: URLProtocol, @unchecked Sendable {
    static var payload = Data()
    override class func canInit(with request: URLRequest) -> Bool { request.url?.host == "example.test" }
    override class func canonicalRequest(for request: URLRequest) -> URLRequest { request }
    override func startLoading() {
        let response = HTTPURLResponse(url: request.url!, statusCode: 200, httpVersion: nil, headerFields: nil)!
        client?.urlProtocol(self, didReceive: response, cacheStoragePolicy: .notAllowed)
        client?.urlProtocol(self, didLoad: Self.payload)
        client?.urlProtocolDidFinishLoading(self)
    }
    override func stopLoading() {}
}
@main struct Checks {
    @MainActor static func main() async throws {
        URLCache.shared = URLCache(memoryCapacity: 0, diskCapacity: 0, diskPath: nil)
        assert(URLProtocol.registerClass(FixtureProtocol.self))
        defer { URLProtocol.unregisterClass(FixtureProtocol.self) }
        let probation = "This leave type isn't available during probation. Please apply for a leave type your organization allows on probation, or contact HR."
        let errors: [Any] = [
            ["code": 400, "data": NSNull(), "error": "VALIDATION_ERROR", "message": probation],
            "Existing leave overlaps", NSNull(), ["message": ""]
        ]
        let messages = [probation, "Existing leave overlaps", "Leave Request Rejected", "Leave Request Rejected"]
        let model = CreateLeaveViewModel()
        for (detail, message) in zip(errors, messages) {
            FixtureProtocol.payload = try JSONSerialization.data(withJSONObject: ["statusCode": 400, "body": ["status": "failed", "message": "Leave Request Rejected", "error": detail]])
            do {
                try await model.createLeave()
                fatalError("Rejected leave must throw despite HTTP 200")
            } catch NetworkError.clientError(let code, let reason) {
                assert(code == 400 && reason == message)
                assert(NetworkManager.shared.statusCode == 400)
                assert(NetworkManager.shared.responseMessage == message)
                assert(model.error?.localizedDescription == message)
                assert(model.createLeaveData == nil && !model.isLoading)
            }
        }
        FixtureProtocol.payload = Data(#"{"statusCode":200,"body":{"status":"success","message":"Applied","error":null,"data":null}}"#.utf8)
        try await model.createLeave()
        assert(model.error == nil && !model.isLoading)
        assert(model.createLeaveData?.statusCode == 200)
        assert(NetworkManager.shared.responseMessage == "Applied")
        print("Leave error and retry checks passed.")
    }
}
'''

with tempfile.TemporaryDirectory(prefix="emp-leave-check-") as scratch:
    directory = Path(scratch)
    swift = directory / "checks.swift"
    swift.write_text(source)
    binary = directory / "checks"
    subprocess.run(["xcrun", "swiftc", "-swift-version", "5", "-parse-as-library", "-module-cache-path", str(directory / "cache"), str(swift), "-o", str(binary)], check=True)
    subprocess.run([str(binary)], check=True, timeout=30)
