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
    
//    init() {
//        permissionManager.backgroundActivity = CLBackgroundActivitySession()
//        permissionManager.startLocationUpdate()
//    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
//                .environmentObject(appState)
                .environmentObject(permissionManager)
                .environmentObject(createProfileViewModel)
                .environmentObject(searchLocationViewModel)
                .environmentObject(calendarViewModel)
                .environmentObject(timerManager)
                .environmentObject(profileImageLoader)
                .onAppear {
                    permissionManager.backgroundActivity = CLBackgroundActivitySession()
                    permissionManager.startLocationUpdate()

                    // Remove legacy image blob stored in UserDefaults (caused ~5MB limit warning)
                    UserDefaults.standard.removeObject(forKey: "ProfilePic")

                    if NetworkManager.shared.statusCode == 401 {
                        AppState.shared.isLoggedIn = false
                        AuthStore.shared.clearSession()
                    }
                }
        }
    }
}
