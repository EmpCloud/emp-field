//
//  LocationManager.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 19/06/24.
//

import Foundation
import MapKit
import CoreLocation
import BackgroundTasks
import CoreMotion
import AVFoundation
import Photos
import Network
import SwiftData

@MainActor
class PermissionManager: NSObject, ObservableObject {
    
    @Published var userLocation: CLLocation?
    @Published var region = MKCoordinateRegion()
    @Published var locationStatus: CLAuthorizationStatus?
    
    @Published var motionData: CMDeviceMotion? // To check motion
    
    @Published var isCameraAuthorized = false
    @Published var isPhotoLibraryAuthorized = false
    @Published var isLocationAuthorized = false
    @Published var isMotionAuthorized = false
    
    //This is User tracking data for API call
//    @Published var trackRequestData: [TrackRequestModelData] = []
    
    private var locationManager = CLLocationManager()
    private var locationUpdate: CLLocationUpdate.Updates? // for live update after termination
    private var locationUpdatesTask: Task<Void, Never>?
    private let motionManager = CMMotionManager() // Motion Manager
    
    private var locationUpdateTimer: Timer?
    
    //to create to global session to update the location even from background
    var backgroundActivity: CLBackgroundActivitySession?
    var previousLocation: CLLocation? = nil
    
    //Define the interval in seconds (30 seconds here) frequnecy of tracking
    @Published var distanceThreshold: CLLocationDistance = 10.0
    @Published var updateInterval: TimeInterval = 60
    var lastUpdateTime: Date? = nil
    var previousLocationData: CLLocation?
    var prevLatitude: Double = 0.0
    
    //Monitor the network connectivity
    private let monitor = NWPathMonitor()
    private var isOnline = true
    private var lastDeviceStatusHeartbeatAt: Date?
    private let deviceStatusHeartbeatInterval: TimeInterval = 15 * 60

    // Geo-fence auto check-in / check-out signals observed by HomeView
    @Published var shouldAutoCheckIn: Bool = false
    @Published var shouldAutoCheckOut: Bool = false
    @Published var isInsideGeoFence: Bool = false
    private let geoFenceRegionIdentifier = "OrgGeoFence"
    
    
    
    override init() {
        super.init()
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.distanceFilter = kCLDistanceFilterNone
        
        
        setupNetworkMonitor()  // setting up the network monitor
        
//        locationManager.allowsBackgroundLocationUpdates = true // allow background updates
//        locationManager.pausesLocationUpdatesAutomatically = false //prevent automatic pauses
//        locationManager.delegate = self
        
        setupLocationManager()
    }

    deinit {
        locationUpdatesTask?.cancel()
        monitor.cancel()
        motionManager.stopDeviceMotionUpdates()
    }
    
    //Setting up the Manager for background location fetch
    func setupLocationManager() {
        locationManager.delegate = self
        locationManager.allowsBackgroundLocationUpdates = true
        locationManager.pausesLocationUpdatesAutomatically = false
//        locationManager.startMonitoringSignificantLocationChanges()
//        startLocationUpdateTimer(interval: 30)
//        scheduleBackgroundLocationUpdates()
        
//        startLocationUpdate()
    }
   
    //MARK: Setting up Network monitor to upload the offline data
    func setupNetworkMonitor() {
        Task {
            await LocationQueueService.shared.migrateLegacyQueue()
        }
        
        monitor.pathUpdateHandler = { [weak self] path in
            Task { @MainActor [weak self] in
                self?.isOnline = path.status == .satisfied
                if self?.isOnline == true {
                    await self?.uploadOfflineLocations()
                }
            }
        }
        let queue = DispatchQueue(label: "NetworkStatus")
        monitor.start(queue: queue)
    }
    
    //MARK: Location Permission
    func requestLocation() {
        switch locationManager.authorizationStatus {
        case .authorizedAlways, .authorizedWhenInUse:
            locationManager.startUpdatingLocation()
        case .notDetermined:
            locationManager.requestAlwaysAuthorization()
        case .denied, .restricted:
            locationStatus = locationManager.authorizationStatus
            isLocationAuthorized = false
        @unknown default:
            locationStatus = locationManager.authorizationStatus
            isLocationAuthorized = false
        }
    }
    
    //MARK: Motion Permission
    func requestMotionPermission() {
        isMotionAuthorized = motionManager.isDeviceMotionAvailable
    }
    
    
    //MARK: Motion Activity Updates
    func startMotionUpdates() {
        guard motionManager.isDeviceMotionAvailable else {
            AppLog.debug("Device motion is not available.")
            return
        }

        guard !motionManager.isDeviceMotionActive else { return }
        
        motionManager.deviceMotionUpdateInterval = 1.0
        motionManager.startDeviceMotionUpdates(to: .main) { [weak self] motion, error in
            guard error == nil else {
                if let error = error {
                    AppLog.debug("Error: \(error)")
                }
                return
            }

            if let motion = motion {
                self?.motionData = motion
            }
        }
    }
    
    //MARK: Motion Updates
    func stopMotionUpdates() {
        motionManager.stopDeviceMotionUpdates()
    }
    
    
    //MARK: Camera Permission
    func requestCameraPermission() {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .authorized:
            isCameraAuthorized = true
        case .notDetermined:
            AVCaptureDevice.requestAccess(for: .video) { [weak self] granted in
                Task { @MainActor [weak self] in
                    self?.isCameraAuthorized = granted
                }
            }
        default:
            isCameraAuthorized = false
        }
    }
    
    //MARK: Gallary Permission Request
    func requestPhotoLibraryPermission() {
        let status = PHPhotoLibrary.authorizationStatus()
        switch status {
        case .authorized, .limited:
            isPhotoLibraryAuthorized = true
        case .notDetermined:
            PHPhotoLibrary.requestAuthorization { [weak self] status in
                Task { @MainActor [weak self] in
                    self?.isPhotoLibraryAuthorized = (status == .authorized || status == .limited)
                }
            }
        default:
            isPhotoLibraryAuthorized = false
        }
        }
    
    
    //MARK: Live update
//    var oldLocation: CLLocation?
    
    func startLocationUpdate() {
        
        //check if the user is checkedIN
        let isCheckedIN = UserDefaults.standard.bool(forKey: "isCheckedIN")
        
        //ONLY proceed if the user is checkedIN
        guard isCheckedIN else {
            DeviceStatusDebug.log("Tracking start skipped: user is not checked in; device-status endpoints will not hit on login only")
            AppLog.debug("User is not checked in. Location updates will not start.")
            return
        }

        guard locationUpdatesTask == nil else {
            DeviceStatusDebug.log("Tracking start skipped: live location updates are already active")
            AppLog.debug("Location updates already active.")
            return
        }
        
        //Customize the tracking frequency
        distanceThreshold = CLLocationDistance(UserDefaults.standard.integer(forKey: "CurrentRadius"))
//        distanceThreshold = 0
//        updateInterval = 0
        updateInterval = TimeInterval(UserDefaults.standard.integer(forKey: "CurrentFrequency"))
        
        
        
        
        // location permission taken after that
        locationUpdatesTask = Task { [weak self] in
            guard let self else { return }
            defer {
                self.locationUpdatesTask = nil
                if !UserDefaults.standard.bool(forKey: "isCheckedIN") {
                    self.backgroundActivity = nil
                }
            }

            do {
                
                //Assign the CLBackgroundActivitySession to global var
                self.backgroundActivity = CLBackgroundActivitySession()
                await self.maybeSendDeviceStatusHeartbeat(force: true)
                
                
                // to fetch all location
//                let locationUpdates = CLLocationUpdate.liveUpdates()
                
//                AppLog.debug("Live update")
                
                // to fetch the filter location
                let threshold = self.distanceThreshold
                let locationUpdates = CLLocationUpdate.liveUpdates().filter { [weak self] update in
                    guard let self else { return false }
                    let previousLocation = await MainActor.run { self.previousLocation }
                    guard let previousLocation = previousLocation else {
                        await MainActor.run { self.previousLocation = update.location }
                        return true
                    }
                    let distanceMoved = update.location?.distance(from: previousLocation)
                    if distanceMoved ?? 0.0 >= threshold || Self.isStationary(update) {
                        await MainActor.run { self.previousLocation = update.location }
                        return true
                    }
                    return false
                }
                
//                AppLog.debug(locationUpdates)
                
//                var count = 1
                self.checkTimeAndStopTrackingIfNeeded()  // to stop tracking at midnight
               
//                let currentTime = Date()
//                if lastUpdateTime == nil || currentTime.timeIntervalSince(lastUpdateTime!) >= updateInterval {
                    
                    for try await update in locationUpdates {
                        if Task.isCancelled {
                            AppLog.debug("Location tracking task cancelled.")
                            break
                        }

                        // Exit loop when user checks out or midnight stop triggers
                        guard UserDefaults.standard.bool(forKey: "isCheckedIN") else {
                            AppLog.debug("User checked out. Stopping location updates.")
                            break
                        }

                        self.checkTimeAndStopTrackingIfNeeded()  // to stop tracking at midnight

                        if let location = update.location {
                            self.userLocation = location

                            let currentTime = Date()

                            if self.prevLatitude == 0.0 || self.prevLatitude != location.coordinate.latitude {
                                self.prevLatitude = location.coordinate.latitude

                                if self.lastUpdateTime == nil || currentTime.timeIntervalSince(self.lastUpdateTime!) >= self.updateInterval {
                                    self.lastUpdateTime = currentTime
                                    AppLog.debug("Last time: \(self.lastUpdateTime ?? Date())  & current Time: \(currentTime)")
                                    AppLog.debug("Location Lat: \(location.coordinate.latitude) & Long :\(location.coordinate.longitude)")
                                    AppLog.debug("=================")
                                    self.sendLocationToServer(location: location)
                                }
                            }
                        }

                        if Self.isStationary(update) {
                            // Upload any queued offline data but keep the loop running
                            await self.uploadOfflineLocations()
                            await self.maybeSendDeviceStatusHeartbeat()
                            AppLog.debug("user is stationary")
                        }

                    }
//                }
                
                
//                sendLocationToServer()
                
            } catch is CancellationError {
                AppLog.debug("Location tracking cancelled.")
            } catch {
                AppLog.debug("Some live location error occured: \(error)")
            }
            self.checkTimeAndStopTrackingIfNeeded()  // to stop tracking at midnight
        }
    }
    
    func stopLocationUpdates() {
        let wasCheckedIn = UserDefaults.standard.bool(forKey: "isCheckedIN")
        if wasCheckedIn {
            let checkoutStatus = DeviceStatusSnapshot.current(status: "inactive")
            let accessToken = AuthStore.shared.getAccessToken()
            DeviceStatusDebug.log("Checkout inactive status queued before stopping tracking, payload=\(checkoutStatus.logDescription)")
            Task {
                await TrackViewModel.shared.updateDeviceStatus(snapshot: checkoutStatus, accessToken: accessToken)
            }
        } else {
            DeviceStatusDebug.log("Checkout inactive status skipped: user was not checked in")
        }

        //Set check-out status
        UserDefaults.standard.set(false, forKey: "isCheckedIN")
        lastDeviceStatusHeartbeatAt = nil
        lastUpdateTime = nil
        previousLocation = nil
        prevLatitude = 0.0
        locationUpdatesTask?.cancel()
        locationUpdatesTask = nil
        locationManager.stopUpdatingLocation()
        
        //End the background activity session
        backgroundActivity = nil
    }
    
    
    //MARK: To add only the filter and unique location which will be sent to the server
    func startGeoFenceMonitoring() {
        guard let orgLat = Double(UserDefaults.standard.string(forKey: "OrgLatitude") ?? ""),
              let orgLong = Double(UserDefaults.standard.string(forKey: "OrgLongitude") ?? "") else {
            AppLog.debug("GeoFence: missing org location — call fetchTrackingSettings first")
            return
        }
        let orgRadius = Double(UserDefaults.standard.integer(forKey: "OrgRadius"))
        let clampedRadius = min(max(orgRadius, 1), locationManager.maximumRegionMonitoringDistance)

        // Remove any existing region with the same identifier before re-registering
        for region in locationManager.monitoredRegions where region.identifier == geoFenceRegionIdentifier {
            locationManager.stopMonitoring(for: region)
        }

        let region = CLCircularRegion(
            center: CLLocationCoordinate2D(latitude: orgLat, longitude: orgLong),
            radius: clampedRadius,
            identifier: geoFenceRegionIdentifier
        )
        region.notifyOnEntry = true
        region.notifyOnExit  = true
        locationManager.startMonitoring(for: region)
        AppLog.debug("GeoFence monitoring started: lat=\(orgLat), long=\(orgLong), radius=\(clampedRadius)m")
    }

    func stopGeoFenceMonitoring() {
        for region in locationManager.monitoredRegions where region.identifier == geoFenceRegionIdentifier {
            locationManager.stopMonitoring(for: region)
        }
    }

    /// Asks Core Location for the user's current state relative to the geo-fence region.
    /// The result arrives in `locationManager(_:didDetermineState:for:)`.
    func requestGeoFenceState() {
        guard let orgLat = Double(UserDefaults.standard.string(forKey: "OrgLatitude") ?? ""),
              let orgLong = Double(UserDefaults.standard.string(forKey: "OrgLongitude") ?? "") else {
            AppLog.debug("GeoFence: cannot request state — missing org location")
            return
        }
        let orgRadius = Double(UserDefaults.standard.integer(forKey: "OrgRadius"))
        let clampedRadius = min(max(orgRadius, 1), locationManager.maximumRegionMonitoringDistance)
        let region = CLCircularRegion(
            center: CLLocationCoordinate2D(latitude: orgLat, longitude: orgLong),
            radius: clampedRadius,
            identifier: geoFenceRegionIdentifier
        )
        locationManager.requestState(for: region)
    }

    private func maybeSendDeviceStatusHeartbeat(force: Bool = false) async {
        guard UserDefaults.standard.bool(forKey: "isCheckedIN") else {
            DeviceStatusDebug.log("Heartbeat skipped: user is not checked in")
            return
        }

        let now = Date()
        if !force,
           let lastDeviceStatusHeartbeatAt,
           now.timeIntervalSince(lastDeviceStatusHeartbeatAt) < deviceStatusHeartbeatInterval {
            let remaining = Int(deviceStatusHeartbeatInterval - now.timeIntervalSince(lastDeviceStatusHeartbeatAt))
            DeviceStatusDebug.log("Heartbeat skipped: waiting \(max(0, remaining))s before next heartbeat")
            return
        }

        DeviceStatusDebug.log("Heartbeat sending: force=\(force), intervalSeconds=\(Int(deviceStatusHeartbeatInterval))")
        let didUpdate = await TrackViewModel.shared.updateDeviceStatus()
        if didUpdate {
            lastDeviceStatusHeartbeatAt = now
            DeviceStatusDebug.log("Heartbeat completed: lastDeviceStatusHeartbeatAt=\(now)")
        } else {
            DeviceStatusDebug.log("Heartbeat failed: timestamp not updated")
        }
    }

    nonisolated private static func isStationary(_ update: CLLocationUpdate) -> Bool {
        if #available(iOS 18.0, *) {
            return update.stationary
        } else {
            return update.isStationary
        }
    }

    func addUniqueLocationData(latitude: Double, longitude: Double) -> TrackRequestModelData {
        //Get the current data and time
        let currentDate = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        let date = dateFormatter.string(from: currentDate)
        
        let timeFormatter = DateFormatter()
        timeFormatter.dateFormat = "HH:mm:ss"
        let time = timeFormatter.string(from: currentDate)
        let deviceStatus = DeviceStatusSnapshot.current
        
        //create a new TrackRequestModelData entry
        let newLocationData = TrackRequestModelData(
            date: date,
            time: time,
            latitude: latitude,
            longitude: longitude,
            batteryPercent: deviceStatus.batteryPercent,
            isCharging: deviceStatus.isCharging
        )
        DeviceStatusDebug.log("Location point created with device fields: \(newLocationData.deviceStatusLogDescription)")
        
        return newLocationData
        
//        //ensure the data is unique(no duplicates)
//        if !trackRequestData.contains(newLocationData) {
//            trackRequestData.append(newLocationData)
//            AppLog.debug("Added new Location: \(newLocationData)")
//        }else {
//            AppLog.debug("Duplicate location")
//        }
    }


}


extension PermissionManager: CLLocationManagerDelegate {
    nonisolated func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let location = locations.last else { return }
        Task { @MainActor [weak self] in
            self?.userLocation = location
            self?.region = MKCoordinateRegion(center: location.coordinate, latitudinalMeters: 5000, longitudinalMeters: 5000)
        }
        manager.stopUpdatingLocation()
    }
    
    nonisolated func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        Task { @MainActor [weak self] in
            self?.locationStatus = status
            switch status {
            case .notDetermined:
                self?.isLocationAuthorized = false
                AppLog.debug("Location status not determined")
            case .restricted:
                self?.isLocationAuthorized = false
                AppLog.debug("Location status restricted")
            case .denied:
                AppLog.debug("Location status denied")
                self?.isLocationAuthorized = false
            case .authorizedAlways:
                AppLog.debug("Location status is always authorized")
                self?.isLocationAuthorized = true
            case .authorizedWhenInUse:
                AppLog.debug("Location status is when in use authorized")
                self?.isLocationAuthorized = true
            @unknown default:
                AppLog.debug("Location status unknown")
            }
        }
    }
    
    // MARK: Geo-fence delegate callbacks

    nonisolated func locationManager(_ manager: CLLocationManager, didDetermineState state: CLRegionState, for region: CLRegion) {
        guard region.identifier == geoFenceRegionIdentifier else { return }
        let inside = (state == .inside)
        AppLog.debug("GeoFence: initial state = \(inside ? "inside" : "outside/unknown")")
        Task { @MainActor [weak self] in
            self?.isInsideGeoFence = inside
            guard inside,
                  !UserDefaults.standard.bool(forKey: "isCheckedIN"),
                  UserDefaults.standard.integer(forKey: "autoCheckInByGeoFencing") == 1 else { return }
            self?.shouldAutoCheckIn = true
        }
    }

    nonisolated func locationManager(_ manager: CLLocationManager, didEnterRegion region: CLRegion) {
        guard region.identifier == geoFenceRegionIdentifier else { return }
        AppLog.debug("GeoFence: entered org region")
        Task { @MainActor [weak self] in
            self?.isInsideGeoFence = true
            guard !UserDefaults.standard.bool(forKey: "isCheckedIN"),
                  UserDefaults.standard.integer(forKey: "autoCheckInByGeoFencing") == 1 else { return }
            self?.shouldAutoCheckIn = true
        }
    }

    nonisolated func locationManager(_ manager: CLLocationManager, didExitRegion region: CLRegion) {
        guard region.identifier == geoFenceRegionIdentifier else { return }
        AppLog.debug("GeoFence: exited org region")
        Task { @MainActor [weak self] in
            self?.isInsideGeoFence = false
            guard UserDefaults.standard.bool(forKey: "isCheckedIN"),
                  UserDefaults.standard.integer(forKey: "autoCheckInByGeoFencing") == 1 else { return }
            self?.shouldAutoCheckOut = true
        }
    }

    nonisolated func locationManager(_ manager: CLLocationManager, monitoringDidFailFor region: CLRegion?, withError error: Error) {
        AppLog.debug("GeoFence monitoring failed: \(error.localizedDescription)")
    }

    //MARK: Send location API Call
    func sendLocationToServer(location: CLLocation) {
        // Implement API Call here
        AppLog.debug("Sending location to server: \(location.coordinate.latitude) \(location.coordinate.longitude)")
        
        let latitude = location.coordinate.latitude
        let longitude = location.coordinate.longitude
        
        let newLocationData = addUniqueLocationData(latitude: latitude, longitude: longitude)
        let trackRequestData = [newLocationData]
        DeviceStatusDebug.log("Location piggyback prepared: online=\(isOnline), payload=\(newLocationData.deviceStatusLogDescription)")

//        AppLog.debug(trackRequestData)
        if isOnline {
            Task { [weak self] in
                guard let self else { return }
                TrackViewModel.shared.trackRequestData = trackRequestData
                await TrackViewModel.shared.trackUser()
                if NetworkManager.shared.statusCode == 200 {
                    self.lastDeviceStatusHeartbeatAt = Date()
                    DeviceStatusDebug.log("Location piggyback accepted; heartbeat timestamp refreshed")
                } else {
                    DeviceStatusDebug.log("Location piggyback not accepted; statusCode=\(NetworkManager.shared.statusCode), message=\(NetworkManager.shared.responseMessage)")
                }

                // Apply any frequency/radius updates the server returned
                let newFrequency = UserDefaults.standard.integer(forKey: "CurrentFrequency")
                let newRadius = UserDefaults.standard.integer(forKey: "CurrentRadius")
                if newFrequency > 0 {
                    self.updateInterval = TimeInterval(newFrequency)
                }
                if newRadius > 0 {
                    self.distanceThreshold = CLLocationDistance(newRadius)
                }
            }
        }
        else {
            Task {
                await LocationQueueService.shared.enqueue(newLocationData)
                DeviceStatusDebug.log("Location point stored offline with device fields: \(newLocationData.deviceStatusLogDescription)")
                AppLog.debug("Stored offline location: \(latitude), \(longitude)")
            }
        }
        
        
        
        // for the testing of the BG api hit
//        Task {
//            try await GetProfileViewModel().getProfile()
//        }
    }
    
    //MARK: Upload any queued offline locations to the server
    func uploadOfflineLocations() async {
        do {
            let logs = try await LocationQueueService.shared.fetchUnsent(limit: 100)
            guard !logs.isEmpty else { return }
            
            let trackData = logs.map(\.trackRequestData)
            DeviceStatusDebug.log("Offline flush sending \(trackData.count) queued points; lastPoint=\(trackData.last?.deviceStatusLogDescription ?? "none")")
            AppLog.debug("Uploading offline locations:")
            AppLog.debug(trackData)
            
            TrackViewModel.shared.trackRequestData = trackData
            await TrackViewModel.shared.trackUser()
            
            if NetworkManager.shared.statusCode == 200 {
                try await LocationQueueService.shared.delete(logs)
                DeviceStatusDebug.log("Offline flush success; deleted \(logs.count) queued points")
                AppLog.debug("Uploaded offline locations.")
            } else {
                try await LocationQueueService.shared.incrementRetry(logs)
                DeviceStatusDebug.log("Offline flush failed; keeping \(logs.count) queued points, statusCode=\(NetworkManager.shared.statusCode), message=\(NetworkManager.shared.responseMessage)")
                AppLog.debug("Failed to upload offline locations. Keeping queue for retry.")
            }
        } catch {
            DeviceStatusDebug.log("Offline flush error=\(error.localizedDescription)")
            AppLog.debug("Error uploading offline locations: \(error)")
        }
    }
    
    func checkTimeAndStopTrackingIfNeeded() {
        let now = Date()
        var calendar = Calendar.current
        calendar.timeZone = TimeZone.current
        
        if let stopTime = calendar.date(bySettingHour: 23, minute: 59, second: 0, of: now), now > stopTime {
            //stop location tracking
            self.stopLocationUpdates()
            AppLog.debug("Location tracking stopped at midnight")
            Task {
                try? await LocationQueueService.shared.deleteAll()
            }  // deleting yesterday tracking data
        }
    }
    
// MARK: Timer function to send Updated Location
//    func startLocationUpdateTimer(interval: TimeInterval) {
//        if locationUpdateTimer == nil {
//            locationUpdateTimer = Timer.scheduledTimer(withTimeInterval: interval
//                                                       , repeats: true) { [weak self] _ in
////                self?.updateLocation()
//                self?.startLocationUpdate()
//            }
//        }
//    }
    
//    func updateLocation() {
//        locationManager.startUpdatingLocation()
//        DispatchQueue.main.asyncAfter(deadline: .now() + 1) { [weak self] in
//            self?.locationManager.stopUpdatingLocation()
//            if let location = self?.locationManager.location {
//                self?.sendLocationToServer(location: location)
//            }
//        }
//    }
//    
//    func stopLocationUpdateTimer() {
//        locationUpdateTimer?.invalidate()
//        locationUpdateTimer = nil
//    }
    
    
    //MARK: to handle the BG location update
    // MARK: Schedule Background Location Updates
//    func scheduleBackgroundLocationUpdates() {
//        BGTaskScheduler.shared.register(forTaskWithIdentifier: "com.example.background-location-updates", using: nil) { task in
//            self.handleBackgroundLocationUpdates(task: task)
//        }
//    }
//    
//    
//    // MARK: Handle Background Location Updates
//    func handleBackgroundLocationUpdates(task: BGTask) {
//        let queue = OperationQueue()
//        queue.maxConcurrentOperationCount = 1
//        task.expirationHandler = {
//            task.setTaskCompleted(success: false)
//        }
//        queue.addOperation {
//            self.updateLocation()
//            task.setTaskCompleted(success: true)
//        }
//    }
}






//MARK: Code for help  Periodically Send Location Data
//import SwiftUI
//
//@main
//struct MyApp: App {
//    
//    @StateObject var locationManager = LocationManager()
//    @Environment(\.scenePhase) var scenePhase
//    
//    var body: some Scene {
//        WindowGroup {
//            ContentView()
//                .environmentObject(locationManager)
//                .onAppear {
//                    startLocationUpdateTimer()
//                }
//        }
//        .onChange(of: scenePhase) { newPhase in
//            if newPhase == .background {
//                locationManager.locationManager.startUpdatingLocation()
//            }
//        }
//    }
//    
//    func startLocationUpdateTimer() {
//        Timer.scheduledTimer(withTimeInterval: 900, repeats: true) { _ in
//            if let location = locationManager.userLocation {
//                locationManager.sendLocationToServer(location: location)
//            }
//        }
//    }
//}



//MARK: Code for help  Periodically Send Location Data With Motion Data
//import SwiftUI
//
//@main
//struct MyApp: App {
//    
//    @StateObject var locationManager = LocationManager()
//    @Environment(\.scenePhase) var scenePhase
//    
//    var body: some Scene {
//        WindowGroup {
//            ContentView()
//                .environmentObject(locationManager)
//                .onAppear {
//                    startLocationUpdateTimer()
//                }
//        }
//        .onChange(of: scenePhase) { newPhase in
//            if newPhase == .background {
//                locationManager.locationManager.startUpdatingLocation()
//                locationManager.startMotionUpdates()
//            } else if newPhase == .active {
//                locationManager.startMotionUpdates()
//            } else if newPhase == .inactive {
//                locationManager.stopMotionUpdates()
//            }
//        }
//    }
//    
//    func startLocationUpdateTimer() {
//        Timer.scheduledTimer(withTimeInterval: 900, repeats: true) { _ in
//            if let location = locationManager.userLocation {
//                locationManager.sendLocationToServer(location: location)
//            }
//            if let motionData = locationManager.motionData {
//                // Implement your API call for motion data here
//                AppLog.debug("Sending motion data to server: Roll: \(motionData.attitude.roll), Pitch: \(motionData.attitude.pitch), Yaw: \(motionData.attitude.yaw)")
//            }
//        }
//    }
//}


// MARK: - Offline Location Queue Service

/// Persists location updates that could not be uploaded immediately and replays them
/// when connectivity returns. Uses SwiftData instead of UserDefaults so the queue can
/// grow safely and support per-entry retry tracking.
@ModelActor
actor LocationQueueService {

    static let shared: LocationQueueService = {
        do {
            let container = try ModelContainer(for: LocationLog.self)
            return LocationQueueService(modelContainer: container)
        } catch {
            AppLog.debug("[LocationQueueService] Persistent container failed, using in-memory fallback: \(error)")
            do {
                let fallbackConfiguration = ModelConfiguration(isStoredInMemoryOnly: true)
                let fallbackContainer = try ModelContainer(for: LocationLog.self, configurations: fallbackConfiguration)
                return LocationQueueService(modelContainer: fallbackContainer)
            } catch {
                fatalError("Failed to create fallback LocationQueueService container: \(error)")
            }
        }
    }()

    private let maxStoredLogs = 1_000

    /// Stores a single location update for later upload.
    func enqueue(_ locationData: TrackRequestModelData) async {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        dateFormatter.timeZone = TimeZone.current
        let timestamp = dateFormatter.date(from: "\(locationData.date) \(locationData.time)") ?? Date()

        let log = LocationLog(
            timestamp: timestamp,
            latitude: locationData.latitude,
            longitude: locationData.longitude,
            batteryPercent: locationData.batteryPercent,
            isCharging: locationData.isCharging
        )
        modelContext.insert(log)
        do {
            try modelContext.save()
            try pruneIfNeeded()
        } catch {
            AppLog.debug("[LocationQueueService] Failed to enqueue location: \(error)")
        }
    }

    /// Returns the oldest unsent location logs, capped to a sane batch size.
    func fetchUnsent(limit: Int = 100) async throws -> [LocationLog] {
        var descriptor = FetchDescriptor<LocationLog>(
            predicate: #Predicate { $0.isUploaded == false },
            sortBy: [SortDescriptor(\.timestamp, order: .forward)]
        )
        descriptor.fetchLimit = limit
        return try modelContext.fetch(descriptor)
    }

    /// Removes successfully uploaded logs from the queue.
    func delete(_ logs: [LocationLog]) async throws {
        for log in logs {
            modelContext.delete(log)
        }
        try modelContext.save()
    }

    /// Increments the retry counter for failed logs so backoff logic can be added later.
    func incrementRetry(_ logs: [LocationLog]) async throws {
        for log in logs {
            log.retryCount += 1
        }
        try modelContext.save()
    }

    /// Deletes every stored log. Used when ending the tracking session (e.g. at midnight).
    func deleteAll() async throws {
        let all = try await fetchUnsent(limit: Int.max)
        try await delete(all)
    }

    /// Imports any locations that were previously queued in UserDefaults so they are not lost.
    func migrateLegacyQueue() async {
        guard let savedData = UserDefaults.standard.data(forKey: "offlineLocations"),
              let legacy = try? JSONDecoder().decode([TrackRequestModelData].self, from: savedData),
              !legacy.isEmpty else {
            return
        }

        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        dateFormatter.timeZone = TimeZone.current

        for entry in legacy {
            let dateString = "\(entry.date) \(entry.time)"
            let timestamp = dateFormatter.date(from: dateString) ?? Date()
            let log = LocationLog(
                timestamp: timestamp,
                latitude: entry.latitude,
                longitude: entry.longitude,
                batteryPercent: entry.batteryPercent,
                isCharging: entry.isCharging
            )
            modelContext.insert(log)
        }

        do {
            try modelContext.save()
            try pruneIfNeeded()
            UserDefaults.standard.removeObject(forKey: "offlineLocations")
            AppLog.debug("[LocationQueueService] Migrated \(legacy.count) legacy offline locations to SwiftData.")
        } catch {
            AppLog.debug("[LocationQueueService] Failed to migrate legacy queue: \(error)")
        }
    }

    /// Keeps the durable offline queue bounded so a long outage cannot consume
    /// unbounded local storage. Oldest unsent points are dropped first.
    private func pruneIfNeeded() throws {
        let descriptor = FetchDescriptor<LocationLog>(
            sortBy: [SortDescriptor(\.timestamp, order: .forward)]
        )
        let logs = try modelContext.fetch(descriptor)
        let overflowCount = logs.count - maxStoredLogs
        guard overflowCount > 0 else { return }

        for log in logs.prefix(overflowCount) {
            modelContext.delete(log)
        }
        try modelContext.save()
        AppLog.debug("[LocationQueueService] Pruned \(overflowCount) old offline locations.")
    }
}
