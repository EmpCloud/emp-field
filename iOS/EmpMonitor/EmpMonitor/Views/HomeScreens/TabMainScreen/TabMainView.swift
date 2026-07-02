//
//  TabView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import SwiftUI
import PDFKit
import UIKit

struct TabMainView: View {
    
    @EnvironmentObject var permissionManager: PermissionManager
    @EnvironmentObject var searchLocationViewModel: SearchLocationViewModel
    @EnvironmentObject var timerManager: TimerManager
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    @StateObject private var homeScreenViewModel = HomeScreenViewModel()
    @StateObject private var checkINViewModel = CheckINViewModel()
    @StateObject private var motViewModel = MOTViewModel()
    
    //Profile
    @StateObject private var getProfileViewModel = GetProfileViewModel()
    @StateObject private var updateProfileViewModel = UpdateProfileViewModel()
    @State private var showProfile: Bool = false
    @State private var selectedGender: String = ""
    
    //Home
    @State private var showALHView: Bool = false
    @State private var selectedALHView: String?
    
    //Side Menu
    @State private var selectedTab: Int = 0
    @State private var showSideMenu: Bool = false
    @State private var showMapCheckInView: Bool = false
    @State private var isLogout: Bool = false
    
    @State private var showCheckIN: Bool = true
    @State private var showCheckOUT: Bool = false
    
    //Client Screen
    @State private var selectedClient: ClientListResponseData?
    @State private var selectedClientContact: ClientListResponseData?
    
    //Notification
    @State private var showNotification: Bool = false
    
    //QRCode
    @State private var showQRCode: Bool = false
    @State private var imageURL: String = ""
    @State private var pdfURL: URL?
    @State private var isShareSheetPresented = false
    @State private var downloadPDF: Bool = false
    @State private var isImagedLoaded: Bool = false
    @State private var qrCodeImage: UIImage? = nil
    
    //mode of travel
    @State private var selectedMode: String?
    @State private var tappedMode: String?
    
    
    //Task Status
    @State private var isTaskRunning: Bool = false
    
    var body: some View {
        NavigationStack {
            ZStack {
                //MARK: Background
                LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                    .ignoresSafeArea()
                
                TabView(selection: $selectedTab) {
                    
//                    Group {
//                    ScrollView {
                    HomeView(checkINViewModel: checkINViewModel, homeScreenViewModel: homeScreenViewModel, motViewModel: motViewModel, selectedALHView: $selectedALHView, showCheckIN: $showCheckIN, showCheckOUT: $showCheckOUT, showSideMenu: $showSideMenu, showMapCheckInView: $showMapCheckInView, showALHView: $showALHView, selectedMode: $selectedMode, tappedMode: $tappedMode, isTaskRunning: $isTaskRunning)
//                            .tabItem {
//                                Image(systemName: "house")
//                                Text("Home")
//                            }
                            .tag(0)
//                    }
                        
                        
                    TaskView(showSideMenu: $showSideMenu, isTaskRunning: $isTaskRunning)
                        .environmentObject(permissionManager)
//                            .tabItem {
//                                Image(systemName: "list.bullet.clipboard")
//                                Text("Task")
//                            }
                            .tag(1)
                        
                        //           InsightView()
                        //                .tabItem {
                        //                    Text("Insight")
                        //                }
                        //                .tag(2)
                        
                        ClientView(selectedClient: $selectedClient, selectedClientContact: $selectedClientContact, showSideMenu: $showSideMenu)
                        .environmentObject(profileImageLoader)
//                            .tabItem {
//                                Image(systemName: "person.badge.minus")
//                                Text("Client")
//                            }
                            .tag(2)
                        
                    Settings(motViewModel: motViewModel, selectedMode: $selectedMode, tappedMode: $tappedMode, isLogout: $isLogout, showQRCode: $showQRCode)
//                            .tabItem {
//                                Image(systemName: "gearshape")
//                                Text("Settings")
//                            }
                            .tag(3)
//                    }
//                    .toolbarBackground(.visible, for: .tabBar)
                    
                }
                .overlay(alignment: .bottom) {
                    CustomTabBarView(tabSelection: $selectedTab)
//                        .padding(.bottom, 10)
                }
                .zIndex(1)
                
                //MARK: QRCode
                if showQRCode {
                    ZStack {
                        VStack(spacing: 30) {
                            QRCodePopupView(imageURL: $imageURL, downloadPDF: $downloadPDF, isImageLoaded: $isImagedLoaded, qrCodeImage: $qrCodeImage, showQrCode: $showQRCode, pdfURL: $pdfURL, isShareSheetPresented: $isShareSheetPresented)
                            
                            QrDownloadButton() {
                                //TODO: Download the QR code
                                if isImagedLoaded {
                                    downloadPDF.toggle()
                                }
                            }
                        }
                    }
                    .zIndex(2)
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .background(Color.black.opacity(0.5))
                    
                }
                
                if showSideMenu{
                    SideMenuView(showSideMenu: $showSideMenu, selectedTab: $selectedTab, showALHView: $showALHView, selectedALHView: $selectedALHView, isLogout: $isLogout)
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                        .zIndex(2)
                        .transition(AnyTransition.move(edge: .leading))
                }
                
                
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .ignoresSafeArea(edges: .bottom)
            .sheet(isPresented: $showMapCheckInView) {
                MapCheckInView(homeScreenViewModel: homeScreenViewModel, checkINViewModel: checkINViewModel, showCheckIN: $showCheckIN, showCheckOUT: $showCheckOUT, motViewModel: motViewModel, selecetedMode: $selectedMode, tappedMode: $tappedMode, isTaskRunning: $isTaskRunning)
                    .environmentObject(profileImageLoader)
            }
            .sheet(isPresented: $isShareSheetPresented, content: {
                if let pdfURL = pdfURL {
                    ActivityViewController(activityItems: [pdfURL])
                }
            })
            //MARK: Navigation Bar Button
            .navigationTitle(tabTitle(selectedTab: selectedTab))
            .navigationBarTitleDisplayMode(.inline)
            .navigationDestination(isPresented: $showProfile) {
                ProfileView(getProfileViewModel: getProfileViewModel, updateProfileViewModel: updateProfileViewModel, selectedGender: $selectedGender)
                    .environmentObject(profileImageLoader)
                    .navigationBarBackButtonHidden()
            }
            .navigationDestination(isPresented: $showNotification) {
                NotificationView()
                    .navigationBarBackButtonHidden()
            }
            .fullScreenCover(isPresented: $isLogout) {
                LoginView()
                    .environmentObject(profileImageLoader)
            }
            .toolbar{
                if !showSideMenu {  // controlling the visiblity of the toolbar when the sideMenu is visible
                    ToolbarItem(placement: .topBarLeading) {
                        Image(.sideMenuIcon)
                            .padding(.horizontal)
                            .onTapGesture {
                                withAnimation(.spring) {
                                    showSideMenu.toggle()
                                }
                            }
                        
                        
                    }
                    ToolbarItem(placement: .topBarTrailing) {
                        QRCodeIconView()
                            .onTapGesture {
                                withAnimation {
                                    showQRCode.toggle()
                                }
                            }
                    }
                    ToolbarItem(placement: .topBarTrailing) {
                        BellIconView()
                            .onTapGesture {
                                withAnimation {
                                    showNotification.toggle()
                                }
                            }
                    }
                    ToolbarItem(placement: .topBarTrailing) {
                        ProfileSmallView()
                            .environmentObject(profileImageLoader)
                        //                            .padding(.horizontal)
                            .onTapGesture {
                                Task {
                                    try await getProfileViewModel.getProfile()
                                    
                                    
                                    if let profileDetail = getProfileViewModel.profileDetail.first {
                                        updateProfileViewModel.fullName = profileDetail.fullName
                                        updateProfileViewModel.age = String(profileDetail.age ?? "")
                                        updateProfileViewModel.gender = profileDetail.gender ?? ""
                                        updateProfileViewModel.email = profileDetail.email ?? ""
                                        updateProfileViewModel.profilePic = profileDetail.profilePic ?? ""
                                        updateProfileViewModel.address1 = profileDetail.address1 ?? ""
                                        updateProfileViewModel.address2 = profileDetail.address2 ?? ""
                                        updateProfileViewModel.latitude = profileDetail.latitude ?? ""
                                        updateProfileViewModel.longitude = profileDetail.longitude ?? ""
                                        updateProfileViewModel.city = profileDetail.city ?? ""
                                        updateProfileViewModel.state = profileDetail.state ?? ""
                                        updateProfileViewModel.country = profileDetail.country ?? ""
                                        updateProfileViewModel.zipCode = profileDetail.zipCode ?? ""
                                        updateProfileViewModel.phoneNumber = profileDetail.phoneNumber ?? ""
                                        
                                        selectedGender = updateProfileViewModel.gender
                                    }
                                    //                    print(getProfileViewModel.profileDetail)
                                    showProfile.toggle()
                                }
                                
                            }
                    }
                    
                }
            }
            .onAppear{
                UINavigationBar.appearance().titleTextAttributes = [
                    .foregroundColor: UIColor.white
                ]
                // Kick off geo-fence monitoring early so didDetermineState can resolve
                // before HomeView's onAppear Task fetches home data.
                if UserDefaults.standard.integer(forKey: "isGeoFencingOn") == 1 {
                    permissionManager.startGeoFenceMonitoring()
                    permissionManager.requestGeoFenceState()
                }
            }
        }
    }
    
    private func tabTitle(selectedTab: Int) -> String {
        switch selectedTab {
        case 0:
            return ""
        case 1:
            return "Task"
        case 2:
            return "Client"
        case 3:
            return "Settings"
        default:
            return ""
        }
    }

}

#Preview {
    TabMainView()
        .environmentObject(SearchLocationViewModel())
        .environmentObject(TimerManager())
        .environmentObject(PermissionManager())
//        .environmentObject(AppState())
}
