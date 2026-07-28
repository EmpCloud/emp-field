//
//  FaceCheckInViewModel.swift
//  EmpMonitor
//

import Foundation

enum FaceCheckInResult {
    case idle
    case verifying
    case success(time: String)
    case noMatch
    case failure(String)
}

@MainActor
final class FaceCheckInViewModel: ObservableObject {

    @Published var result: FaceCheckInResult = .idle

    // Called from FaceCheckInView after image data is captured
    func checkIn(imageData: Data,
                 latitude: Double,
                 longitude: Double,
                 onSuccess: @escaping (String) -> Void) async {
        guard UserDefaults.standard.bool(forKey: "isCheckedIN") == false else {
            result = .failure(CheckINViewModel.duplicateCheckInMessage)
            return
        }

        result = .verifying

        let userData = AuthStore.shared.getLoggedInUser()
        let companyId = userData?.body.data?.userData.orgID ?? ""

        do {
            // Step A: Verify face against ML server
            let verifyResponse = try await FaceVerifyService.shared.verifyFace(
                imageData: imageData,
                companyId: companyId
            )
            AppLog.debug("[FaceCheckIn] Verify response: verified=\(verifyResponse.data.verified ?? false), face_id=\(verifyResponse.data.match?.face_id ?? "nil")")

            guard verifyResponse.data.isMatched else {
                result = .noMatch
                return
            }

            // Step B: Mark attendance via the existing field API
            // Face recognised → use the same mark-attendance endpoint used by swipe check-in
            let checkInVM = CheckINViewModel()
            checkInVM.checkINTime = HelperFunction.shared.currentTime()
            checkInVM.checkINLatitude = latitude
            checkInVM.checkINLongitude = longitude

            UserDefaults.standard.removeObject(forKey: "offlineLocations")
            try await checkInVM.markAttendance()

            if NetworkManager.shared.statusCode == 200 || checkInVM.checkINTime != "" {
                let time = checkInVM.checkINTime
                result = .success(time: time)
                onSuccess(time)
            } else {
                result = .failure("Attendance could not be recorded. Please try again.")
            }

        } catch {
            AppLog.debug("[FaceCheckIn] Error: \(error)")
            result = .failure(CheckINViewModel.blockedCheckInMessage(for: error) ?? error.localizedDescription)
        }
    }
}
