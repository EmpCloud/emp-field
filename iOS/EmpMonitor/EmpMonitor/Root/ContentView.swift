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
    
    @State private var isProfileChecked: Bool = false
    
    var body: some View {
        NavigationStack{
            ZStack {
                if showSplashScreen || !isProfileChecked {
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
                restoreUserInfoFromSavedProfileIfNeeded()
                
                profileImageLoader.profileImageURL = UserDefaults.standard.string(forKey: "UserProfilePic") ?? ""
                profileImageLoader.loadProfileImage()
                
                Task {
                    await verifyProfileForExistingSession()
                    
                    // Refresh tracking settings once on cold start so the latest login/tracking style is applied.
                    if AuthStore.shared.getLoggedInUser() != nil,
                       AuthStore.shared.getAccessToken() != nil {
                        await TrackingSettingsViewModel.shared.fetchTrackingSettings()
                    }
                }
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
        let hasProfile = hasExistingProfile()

        if hasLogin && hasProfile {
            TabMainView()
        } else if hasProfile || hasAcceptedTerms {
            LoginView()
        } else {
            WelcomeView()
        }
    }
    
    private var hasAcceptedTerms: Bool {
        UserDefaults.standard.bool(forKey: "hasAcceptedTerms")
    }
    
    private func hasExistingProfile() -> Bool {
        let createProfile = AuthStore.shared.getUserProfileData(as: CreateProfileResponseModel.self)
        let fetchedProfile = AuthStore.shared.getUserProfileData(as: ProfileResponseModel.self)
        return createProfile?.body.data.resultData.first != nil
            || fetchedProfile?.body.data.resultData.first != nil
    }
    
    /// After a reinstall Keychain persists but UserDefaults do not, so repopulate the
    /// display name and avatar URL from the saved profile if they are missing.
    private func restoreUserInfoFromSavedProfileIfNeeded() {
        let name = UserDefaults.standard.string(forKey: "UserName")
        guard name == nil || name?.isEmpty == true else { return }
        
        if let createProfile = AuthStore.shared.getUserProfileData(as: CreateProfileResponseModel.self),
           let detail = createProfile.body.data.resultData.first {
            UserDefaults.standard.set(detail.fullName, forKey: "UserName")
            UserDefaults.standard.set(detail.department, forKey: "UserDepartment")
            UserDefaults.standard.set(detail.profilePic, forKey: "UserProfilePic")
        } else if let fetchedProfile = AuthStore.shared.getUserProfileData(as: ProfileResponseModel.self),
                  let detail = fetchedProfile.body.data.resultData.first {
            UserDefaults.standard.set(detail.fullName, forKey: "UserName")
            UserDefaults.standard.set(detail.profilePic, forKey: "UserProfilePic")
        }
    }
    
    @MainActor
    private func verifyProfileForExistingSession() async {
        defer { isProfileChecked = true }
        guard AuthStore.shared.getLoggedInUser() != nil,
              AuthStore.shared.getAccessToken() != nil else { return }
        guard !hasExistingProfile() else { return }

        let profileVM = GetProfileViewModel()
        try? await profileVM.getProfile()
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
