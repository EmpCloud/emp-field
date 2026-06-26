//
//  ContentView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI

struct ContentView: View {
    
//    @EnvironmentObject var appState: AppState
    @EnvironmentObject var createProfileViewModel: CreateProfileViewModel
    @EnvironmentObject var permissionManager: PermissionManager
    
    @StateObject private var profileImageLoader: ProfileImageLoader = ProfileImageLoader()
    
    @StateObject private var networkMonitor = NetworkMonitor()
    
    @State private var showLocationPermissionAlert: Bool = false
    
    @State private var showSplashScreen: Bool = true
    
    
    @State private var showInternetAlert: Bool = false
    //Toaster
//    @State private var showToaster: Bool = false
    
    var body: some View {
        NavigationStack{
            ZStack {
                if showSplashScreen {
                    SplashView(showSplashScreen: $showSplashScreen)
                } else {
                    if (UserDefaults.standard.string(forKey: "AppState") != nil) {
                        if let loggedInUser: UserLoginResponseModel = UserDefaults.standard.getObject(forKey: "loggedInUser", as: UserLoginResponseModel.self), let userProfile: CreateProfileResponseModel = UserDefaults.standard.getObject(forKey: "UserProfile", as: CreateProfileResponseModel.self)  {
                            
                            TabMainView()
                                .environmentObject(profileImageLoader)
                            
                        }
                        else if let loggedInUser: UserLoginResponseModel = UserDefaults.standard.getObject(forKey: "loggedInUser", as: UserLoginResponseModel.self)  {
                            
                            CreateProfileView()
                                .environmentObject(profileImageLoader)
                            
                        }
                        else if let userProfile: CreateProfileResponseModel = UserDefaults.standard.getObject(forKey: "UserProfile", as: CreateProfileResponseModel.self){
                            
                            LoginView()
                                .environmentObject(profileImageLoader)
                            
                        }
                        else{
                            WelcomeView()
                                .environmentObject(profileImageLoader)
                        }
                    }else{
                        if let loggedInUser: UserLoginResponseModel = UserDefaults.standard.getObject(forKey: "loggedInUser", as: UserLoginResponseModel.self), let userProfile: CreateProfileResponseModel = UserDefaults.standard.getObject(forKey: "UserProfile", as: CreateProfileResponseModel.self)  {
                            
                            TabMainView()
                                .environmentObject(profileImageLoader)
                            
                        }
                        else if let loggedInUser: UserLoginResponseModel = UserDefaults.standard.getObject(forKey: "loggedInUser", as: UserLoginResponseModel.self)  {
                            
                            CreateProfileView()
                                .environmentObject(profileImageLoader)
                            
                        }
                        else if let userProfile: CreateProfileResponseModel = UserDefaults.standard.getObject(forKey: "UserProfile", as: CreateProfileResponseModel.self){
                            
                            LoginView()
                                .environmentObject(profileImageLoader)
                            
                        }
                        else{
                            WelcomeView()
                                .environmentObject(profileImageLoader)
                            
                        }
                    }
                    
                }
             
                if UserDefaults.standard.string(forKey: "UserName") != nil {
                    if showLocationPermissionAlert {
                        ZStack {
                            LocationSettingWarningView(titleText: "Location Required", description: "To re-enable, please go to Settings and turn on Location Service for this app.", showWarningPopup: $showLocationPermissionAlert)
                        }
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                        .background(Color.black.opacity(0.5))
                    }
                }
                
                
                
//                if appState.showToaster {
//                    ZStack{
//                        NetworkToastView(text: NetworkManager.shared.responseMessage)
//                    }
//                    .frame(maxHeight: .infinity, alignment: .top)
//                    .ignoresSafeArea()
//                    .transition(.move(edge: .top))
//                    .onAppear {
//                        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
//                            withAnimation {
//                                appState.showToaster.toggle()
//                                
//                            }
//                        }
//                    }
//                }
            }
            .onAppear {
                checkLocationStatus()
                
                profileImageLoader.profileImageURL = UserDefaults.standard.string(forKey: "UserProfilePic") ?? ""
                profileImageLoader.loadProfileImage()
                
//                if let loggedInUser: UserLoginResponseModel = UserDefaults.standard.getObject(forKey: "loggedInUser", as: UserLoginResponseModel.self) {
//                    print("Logged in info")
//                    print(loggedInUser)
//                }
//                if let userProfile: CreateProfileResponseModel = UserDefaults.standard.getObject(forKey: "UserProfile", as: CreateProfileResponseModel.self) {
//                    print("User Profile info")
//                    print(userProfile)
//                }
                
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
    
    func checkLocationStatus() {
        if let status = permissionManager.locationStatus {
            if status == .denied || status == .restricted || status == .notDetermined {
                showLocationPermissionAlert.toggle()
            }else{
                showLocationPermissionAlert = false
            }
        }else{
            showLocationPermissionAlert = false
        }
    }
}

#Preview {
    ContentView()
//        .environmentObject(AppState())
        .environmentObject(CreateProfileViewModel())
        .environmentObject(PermissionManager())
        .environmentObject(ProfileImageLoader())
}
