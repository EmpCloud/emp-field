//
//  TrackingSettingsViewModel.swift
//  EmpMonitor
//

import Foundation

@MainActor
class TrackingSettingsViewModel: ObservableObject {
    static let shared = TrackingSettingsViewModel()

    @Published var trackingSettings: TrackingSettingsData?
    @Published var isLoading: Bool = false

    private init() {}

    private var urlString: String {
        Constants.shared.baseURL + Constants.Endpoint.trackingSettings
    }

    func fetchTrackingSettings() async {
        isLoading = true
        defer { isLoading = false }

        let token = UserDefaults.standard.string(forKey: "x-access-token")

        do {
            let response: TrackingSettingsResponseModel = try await NetworkManager.shared.getData(
                to: urlString,
                as: TrackingSettingsResponseModel.self,
                accessToken: token
            )

            guard response.statusCode == 200 else { return }

            let data = response.body.data
            trackingSettings = data
            UserDefaults.standard.set(data.currentFrequency, forKey: "CurrentFrequency")
            UserDefaults.standard.set(data.currentRadius, forKey: "CurrentRadius")
            UserDefaults.standard.set(data.isMobileDeviceEnabled, forKey: "isMobileDeviceEnabled")
            UserDefaults.standard.set(data.autoCheckInByMobile, forKey: "autoCheckInByMobile")
            UserDefaults.standard.set(data.isGeoFencingOn, forKey: "isGeoFencingOn")
            UserDefaults.standard.set(data.autoCheckInByGeoFencing, forKey: "autoCheckInByGeoFencing")
            if let lat = data.latitude { UserDefaults.standard.set(lat, forKey: "OrgLatitude") }
            if let lon = data.longitude { UserDefaults.standard.set(lon, forKey: "OrgLongitude") }
            UserDefaults.standard.set(data.orgRadius, forKey: "OrgRadius")

            print("Tracking settings fetched: mobile=\(data.isMobileDeviceEnabled) autoMobile=\(data.autoCheckInByMobile) geoFence=\(data.isGeoFencingOn) autoGeo=\(data.autoCheckInByGeoFencing)")
        } catch {
            print("Error: TrackingSettingsViewModel.fetchTrackingSettings: \(error)")
        }
    }
}
