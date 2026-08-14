//
//  EmpMonitorApp.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI
import UIKit
import BackgroundTasks
import CoreLocation

@main
struct EmpMonitorApp: App {
    
//    @StateObject var appState = AppState()
    @StateObject private var permissionManager = PermissionManager()
    @StateObject private var createProfileViewModel = CreateProfileViewModel()
    @StateObject private var searchLocationViewModel = SearchLocationViewModel()
    @StateObject private var calendarViewModel = CalendarViewModel()
    @StateObject private var timerManager = TimerManager()
    @StateObject private var profileImageLoader = ProfileImageLoader()
    @Environment(\.scenePhase) private var scenePhase
    
//    init() {
//        permissionManager.backgroundActivity = CLBackgroundActivitySession()
//        permissionManager.startLocationUpdate()
//    }
    init() {
        UIDevice.current.isBatteryMonitoringEnabled = true
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .dismissKeyboardOnTapOutside()
//                .environmentObject(appState)
                .environmentObject(permissionManager)
                .environmentObject(createProfileViewModel)
                .environmentObject(searchLocationViewModel)
                .environmentObject(calendarViewModel)
                .environmentObject(timerManager)
                .environmentObject(profileImageLoader)
                .onAppear {
                    permissionManager.startLocationUpdate()
                    LocationSocketManager.shared.connectIfNeeded()
                    // Remove legacy image blob stored in UserDefaults (caused ~5MB limit warning)
                    UserDefaults.standard.removeObject(forKey: "ProfilePic")

                    if NetworkManager.shared.statusCode == 401 {
                        AppState.shared.isLoggedIn = false
                        AuthStore.shared.clearSession()
                    }
                }
                .onChange(of: scenePhase) { _, newPhase in
                    switch newPhase {
                    case .active:
                        LocationSocketManager.shared.connectIfNeeded()
                    case .background:
                        LocationSocketManager.shared.disconnect()
                    default:
                        break
                    }
                }
        }
    }
}
