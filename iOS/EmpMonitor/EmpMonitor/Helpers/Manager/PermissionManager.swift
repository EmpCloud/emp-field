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
        locationManager.startUpdatingLocation()
//        locationManager.delegate = self
        
        setupLocationManager()
        
        startMotionUpdates()
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
        locationManager.requestAlwaysAuthorization()
    }
    
    //MARK: Motion Permission
    func requestMotionPermission() {
            if motionManager.isDeviceMotionAvailable {
                motionManager.startDeviceMotionUpdates()
                isMotionAuthorized = true
            } else {
                isMotionAuthorized = false
            }
        }
    
    
    //MARK: Motion Activity Updates
    func startMotionUpdates() {
        guard motionManager.isDeviceMotionActive else {
            print("Device motion is not available.")
            return
        }
        
        motionManager.deviceMotionUpdateInterval = 1.0 / 60.0
        motionManager.startDeviceMotionUpdates(to: .main) { [weak self] motion, error in
            guard error == nil else {
                if let error = error {
                    print("Error: \(error)")
                }
                return
            }

            if let motion = motion {
                self?.motionData = motion
                print("Motion data: Roll: \(motion.attitude.roll), Pitch: \(motion.attitude.pitch), Yaw: \(motion.attitude.yaw)")
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
                DispatchQueue.main.async {
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
                DispatchQueue.main.async {
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
            print("User is not checked in. Location updates will not start.")
            return
        }
        
        //Customize the tracking frequency
        distanceThreshold = CLLocationDistance(UserDefaults.standard.integer(forKey: "CurrentRadius"))
//        distanceThreshold = 0
//        updateInterval = 0
        updateInterval = TimeInterval(UserDefaults.standard.integer(forKey: "CurrentFrequency"))
        
        
        
        
        // location permission taken after that
        Task { [weak self] in
            guard let self else { return }
            do {
                
                //Assign the CLBackgroundActivitySession to global var
                self.backgroundActivity = CLBackgroundActivitySession()
                
                
                // to fetch all location
//                let locationUpdates = CLLocationUpdate.liveUpdates()
                
//                print("Live update")
                
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
                    if distanceMoved ?? 0.0 >= threshold || update.isStationary {
                        await MainActor.run { self.previousLocation = update.location }
                        return true
                    }
                    return false
                }
                
//                print(locationUpdates)
                
//                var count = 1
                self.checkTimeAndStopTrackingIfNeeded()  // to stop tracking at midnight
               
//                let currentTime = Date()
//                if lastUpdateTime == nil || currentTime.timeIntervalSince(lastUpdateTime!) >= updateInterval {
                    
                    for try await update in locationUpdates {

                        // Exit loop when user checks out or midnight stop triggers
                        guard UserDefaults.standard.bool(forKey: "isCheckedIN") else {
                            print("User checked out. Stopping location updates.")
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
                                    print("Last time: \(self.lastUpdateTime ?? Date())  & current Time: \(currentTime)")
                                    print("Location Lat: \(location.coordinate.latitude) & Long :\(location.coordinate.longitude)")
                                    print("=================")
                                    self.sendLocationToServer(location: location)
                                }
                            }
                        }

                        if update.isStationary {
                            // Upload any queued offline data but keep the loop running
                            await self.uploadOfflineLocations()
                            print("user is stationary")
                        }

                    }
//                }
                
                
//                sendLocationToServer()
                
            }catch {
                debugPrint("Some live location error occured: \(error)")
            }
            self.checkTimeAndStopTrackingIfNeeded()  // to stop tracking at midnight
        }
    }
    
    func stopLocationUpdates() {
        //Set check-out status
        UserDefaults.standard.set(false, forKey: "isCheckedIN")
        
        //End the background activity session
        backgroundActivity = nil
    }
    
    
    //MARK: To add only the filter and unique location which will be sent to the server
    func startGeoFenceMonitoring() {
        guard let orgLat = Double(UserDefaults.standard.string(forKey: "OrgLatitude") ?? ""),
              let orgLong = Double(UserDefaults.standard.string(forKey: "OrgLongitude") ?? "") else {
            print("GeoFence: missing org location — call fetchTrackingSettings first")
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
        print("GeoFence monitoring started: lat=\(orgLat), long=\(orgLong), radius=\(clampedRadius)m")
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
            print("GeoFence: cannot request state — missing org location")
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

    func addUniqueLocationData(latitude: Double, longitude: Double) -> TrackRequestModelData {
        //Get the current data and time
        let currentDate = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        let date = dateFormatter.string(from: currentDate)
        
        let timeFormatter = DateFormatter()
        timeFormatter.dateFormat = "HH:mm:ss"
        let time = timeFormatter.string(from: currentDate)
        
        //create a new TrackRequestModelData entry
        let newLocationData = TrackRequestModelData(date: date, time: time, latitude: latitude, longitude: longitude)
        
        return newLocationData
        
//        //ensure the data is unique(no duplicates)
//        if !trackRequestData.contains(newLocationData) {
//            trackRequestData.append(newLocationData)
//            print("Added new Location: \(newLocationData)")
//        }else {
//            print("Duplicate location")
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
                print("Location status not determined")
            case .restricted:
                self?.isLocationAuthorized = false
                print("Location status restricted")
            case .denied:
                print("Location status denied")
                self?.isLocationAuthorized = false
            case .authorizedAlways:
                print("Location status is always authorized")
                self?.isLocationAuthorized = true
            case .authorizedWhenInUse:
                print("Location status is when in use authorized")
                self?.isLocationAuthorized = true
            @unknown default:
                print("Location status unknown")
            }
        }
    }
    
    // MARK: Geo-fence delegate callbacks

    nonisolated func locationManager(_ manager: CLLocationManager, didDetermineState state: CLRegionState, for region: CLRegion) {
        guard region.identifier == geoFenceRegionIdentifier else { return }
        let inside = (state == .inside)
        print("GeoFence: initial state = \(inside ? "inside" : "outside/unknown")")
        Task { @MainActor [weak self] in
            self?.isInsideGeoFence = inside
            if inside && !UserDefaults.standard.bool(forKey: "isCheckedIN") {
                self?.shouldAutoCheckIn = true
            }
        }
    }

    nonisolated func locationManager(_ manager: CLLocationManager, didEnterRegion region: CLRegion) {
        guard region.identifier == geoFenceRegionIdentifier else { return }
        print("GeoFence: entered org region")
        Task { @MainActor [weak self] in
            self?.isInsideGeoFence = true
            guard !UserDefaults.standard.bool(forKey: "isCheckedIN") else { return }
            self?.shouldAutoCheckIn = true
        }
    }

    nonisolated func locationManager(_ manager: CLLocationManager, didExitRegion region: CLRegion) {
        guard region.identifier == geoFenceRegionIdentifier else { return }
        print("GeoFence: exited org region")
        Task { @MainActor [weak self] in
            self?.isInsideGeoFence = false
            guard UserDefaults.standard.bool(forKey: "isCheckedIN") else { return }
            self?.shouldAutoCheckOut = true
        }
    }

    nonisolated func locationManager(_ manager: CLLocationManager, monitoringDidFailFor region: CLRegion?, withError error: Error) {
        print("GeoFence monitoring failed: \(error.localizedDescription)")
    }

    //MARK: Send location API Call
    func sendLocationToServer(location: CLLocation) {
        // Implement API Call here
        print("Sending location to server: \(location.coordinate.latitude) \(location.coordinate.longitude)")
        
        let latitude = location.coordinate.latitude
        let longitude = location.coordinate.longitude
        
        var trackRequestData: [TrackRequestModelData] = []
        
        trackRequestData.append(addUniqueLocationData(latitude: latitude, longitude: longitude))
        let newLocationData = addUniqueLocationData(latitude: latitude, longitude: longitude)  // this data will be stored when the user is offline
        
//        print(trackRequestData)
        if isOnline {
            Task { [weak self] in
                guard let self else { return }
                TrackViewModel.shared.trackRequestData = trackRequestData
                await TrackViewModel.shared.trackUser()

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
                await LocationQueueService.shared.enqueue(latitude: latitude, longitude: longitude)
                print("Stored offline location: \(latitude), \(longitude)")
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
            print("Uploading offline locations:")
            print(trackData)
            
            TrackViewModel.shared.trackRequestData = trackData
            await TrackViewModel.shared.trackUser()
            
            if NetworkManager.shared.statusCode == 200 {
                try await LocationQueueService.shared.delete(logs)
                print("Uploaded offline locations.")
            } else {
                try await LocationQueueService.shared.incrementRetry(logs)
                print("Failed to upload offline locations. Keeping queue for retry.")
            }
        } catch {
            print("Error uploading offline locations: \(error)")
        }
    }
    
    func checkTimeAndStopTrackingIfNeeded() {
        let now = Date()
        var calendar = Calendar.current
        calendar.timeZone = TimeZone.current
        
        if let stopTime = calendar.date(bySettingHour: 23, minute: 59, second: 0, of: now), now > stopTime {
            //stop location tracking
            self.stopLocationUpdates()
            print("Location tracking stopped at midnight")
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
//                print("Sending motion data to server: Roll: \(motionData.attitude.roll), Pitch: \(motionData.attitude.pitch), Yaw: \(motionData.attitude.yaw)")
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
            fatalError("Failed to create LocationQueueService container: \(error)")
        }
    }()

    /// Stores a single location update for later upload.
    func enqueue(latitude: Double, longitude: Double) async {
        let log = LocationLog(latitude: latitude, longitude: longitude)
        modelContext.insert(log)
        do {
            try modelContext.save()
        } catch {
            print("[LocationQueueService] Failed to enqueue location: \(error)")
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
                longitude: entry.longitude
            )
            modelContext.insert(log)
        }

        do {
            try modelContext.save()
            UserDefaults.standard.removeObject(forKey: "offlineLocations")
            print("[LocationQueueService] Migrated \(legacy.count) legacy offline locations to SwiftData.")
        } catch {
            print("[LocationQueueService] Failed to migrate legacy queue: \(error)")
        }
    }
}
