//
//  ContentView.swift
//  EmpMonitor
//

import SwiftUI

struct ContentView: View {
    
    @EnvironmentObject var createProfileViewModel: CreateProfileViewModel
    @EnvironmentObject var permissionManager: PermissionManager
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    @StateObject private var appState = AppState.shared
    
    @StateObject private var networkMonitor = NetworkMonitor()
    
    @State private var showLocationPermissionAlert: Bool = false
    
    @State private var showSplashScreen: Bool = true
    
    @State private var showInternetAlert: Bool = false
    
    var body: some View {
        NavigationStack{
            ZStack {
                if showSplashScreen {
                    SplashView(showSplashScreen: $showSplashScreen)
                } else {
                    rootView
                }
                
                if AuthStore.shared.getAccessToken() != nil {
                    if showLocationPermissionAlert {
                        ZStack {
                            LocationSettingWarningView(titleText: "Location Required", description: "To re-enable, please go to Settings and turn on Location Service for this app.", showWarningPopup: $showLocationPermissionAlert)
                        }
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                        .background(Color.black.opacity(0.5))
                    }
                }
            }
            .onAppear {
                checkLocationStatus()
                
                profileImageLoader.profileImageURL = UserDefaults.standard.string(forKey: "UserProfilePic") ?? ""
                profileImageLoader.loadProfileImage()
            }
            .onChange(of: permissionManager.locationStatus) { _, newValue in
                checkLocationStatus()
            }
            .onChange(of: profileImageLoader.profileImageURL) { _, newURL in
                profileImageLoader.loadProfileImage()
            }
            .onChange(of: networkMonitor.isConnected) { _, isConnect in
                if !isConnect {
                    showInternetAlert = true
                }
            }
            .alert(isPresented: $showInternetAlert) {
                Alert(
                    title: Text("No Internet Connection"),
                    message: Text("Please check your internet connection."),
                    dismissButton: .default(Text("OK"))
                )
            }
        }
    }
    
    @ViewBuilder
    private var rootView: some View {
        let hasLogin = AuthStore.shared.getLoggedInUser() != nil
        let hasProfile = AuthStore.shared.getUserProfileData(as: CreateProfileResponseModel.self) != nil
        
        if hasLogin && hasProfile {
            TabMainView()
        } else if hasLogin {
            CreateProfileView()
        } else if hasProfile {
            LoginView()
        } else {
            WelcomeView()
        }
    }
    
    func checkLocationStatus() {
        if let status = permissionManager.locationStatus {
            if status == .denied || status == .restricted || status == .notDetermined {
                showLocationPermissionAlert = true
            } else {
                showLocationPermissionAlert = false
            }
        } else {
            showLocationPermissionAlert = false
        }
    }
}

#Preview {
    ContentView()
        .environmentObject(CreateProfileViewModel())
        .environmentObject(PermissionManager())
        .environmentObject(ProfileImageLoader())
}
