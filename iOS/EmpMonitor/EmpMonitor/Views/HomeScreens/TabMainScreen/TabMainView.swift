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
    @Environment(\.horizontalSizeClass) private var horizontalSizeClass
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
    @StateObject private var notificationViewModel = NotificationViewModel()
    
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
                
                // System tab bar: at the bottom on iPhone, floating at the top on iPad.
                TabView(selection: $selectedTab) {
                    Tab("Home", systemImage: "house", value: 0) {
                        HomeView(checkINViewModel: checkINViewModel, homeScreenViewModel: homeScreenViewModel, motViewModel: motViewModel, selectedALHView: $selectedALHView, showCheckIN: $showCheckIN, showCheckOUT: $showCheckOUT, showSideMenu: $showSideMenu, showMapCheckInView: $showMapCheckInView, showALHView: $showALHView, selectedMode: $selectedMode, tappedMode: $tappedMode, isTaskRunning: $isTaskRunning)
                    }

                    Tab("Task", systemImage: "list.bullet.clipboard", value: 1) {
                        TaskView(showSideMenu: $showSideMenu, isTaskRunning: $isTaskRunning)
                            .environmentObject(permissionManager)
                    }

                    Tab("Client", systemImage: "person.2", value: 2) {
                        ClientView(selectedClient: $selectedClient, selectedClientContact: $selectedClientContact, showSideMenu: $showSideMenu)
                            .environmentObject(profileImageLoader)
                    }

                    Tab("Settings", systemImage: "gearshape", value: 3) {
                        Settings(motViewModel: motViewModel, selectedMode: $selectedMode, tappedMode: $tappedMode, isLogout: $isLogout, showQRCode: $showQRCode)
                    }
                }
                .tint(Color.primaryButton1)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            // iPhone keeps the sheet; wide layouts (iPad) get the map full screen so nothing is clipped.
            .sheet(isPresented: mapCheckInPresentation(fullScreen: false)) {
                mapCheckInView(showsHeader: true)
            }
            .fullScreenCover(isPresented: mapCheckInPresentation(fullScreen: true)) {
                NavigationStack {
                    mapCheckInView(showsHeader: false)
                        .navigationTitle("Map Check-In")
                        .navigationBarTitleDisplayMode(.inline)
                        // The screen's white content reaches the top edge, so give the bar the app's
                        // blue and a dark scheme to keep the title and Close button readable.
                        .toolbarBackground(Color.appBg1, for: .navigationBar)
                        .toolbarBackgroundVisibility(.visible, for: .navigationBar)
                        .toolbarColorScheme(.dark, for: .navigationBar)
                        .toolbar {
                            ToolbarItem(placement: .topBarTrailing) {
                                Button("Close", systemImage: "xmark") {
                                    showMapCheckInView = false
                                }
                                .tint(.white)
                            }
                        }
                }
            }
            .fullScreenCover(isPresented: $showQRCode) {
                qrCodeModal
            }
            //MARK: Navigation Bar Button
            .navigationTitle(tabTitle(selectedTab: selectedTab))
            .navigationBarTitleDisplayMode(.inline)
            .navigationDestination(isPresented: $showProfile) {
                ProfileView(getProfileViewModel: getProfileViewModel, updateProfileViewModel: updateProfileViewModel, selectedGender: $selectedGender)
                    .environmentObject(profileImageLoader)
                    .navigationBarBackButtonHidden()
            }
            .navigationDestination(isPresented: $showNotification) {
                NotificationView(notificationViewModel: notificationViewModel)
                    .navigationBarBackButtonHidden()
            }
            .onChange(of: showNotification) { _, isShowing in
                if !isShowing {
                    Task { try? await notificationViewModel.getNotificationList() }
                }
            }
            .fullScreenCover(isPresented: $isLogout) {
                LoginView()
                    .environmentObject(profileImageLoader)
            }
            .toolbar{
                if !showSideMenu {  // controlling the visiblity of the toolbar when the sideMenu is visible
                    ToolbarItem(placement: .topBarLeading) {
                        Button {
                            withAnimation(.spring) {
                                showSideMenu.toggle()
                            }
                        } label: {
                            Image(.sideMenuIcon)
                                .padding(.horizontal)
                                .frame(minWidth: 44, minHeight: 44)
                        }
                        .buttonStyle(.plain)
                        .accessibilityLabel("Open menu")
                    }
                    ToolbarItem(placement: .topBarTrailing) {
                        Button {
                            withAnimation {
                                showQRCode.toggle()
                            }
                        } label: {
                            QRCodeIconView()
                        }
                        .buttonStyle(.plain)
                        .accessibilityLabel("Show QR code")
                    }
                    ToolbarItem(placement: .topBarTrailing) {
                        Button {
                            withAnimation {
                                showNotification.toggle()
                            }
                        } label: {
                            BellIconView(hasNotifications: notificationViewModel.hasUnreadNotifications)
                        }
                        .buttonStyle(.plain)
                        .accessibilityLabel("Notifications")
                    }
                    ToolbarItem(placement: .topBarTrailing) {
                        Button {
                            Task {
                                do {
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
                                
                                    showProfile.toggle()
                                } catch {
                                    AppLog.debug("Task failed: \(error.localizedDescription)")
                                }
                            }
                        } label: {
                            ProfileSmallView()
                                .environmentObject(profileImageLoader)
                                .frame(minWidth: 44, minHeight: 44)
                        }
                        .buttonStyle(.plain)
                        .accessibilityLabel("Profile")
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
                Task { try? await notificationViewModel.getNotificationList() }
            }
        }
        // Presented over the whole navigation stack so the drawer and its dimming also
        // cover the navigation bar and tab bar instead of sitting underneath them.
        .overlay {
            SideMenuView(showSideMenu: $showSideMenu, selectedTab: $selectedTab, showALHView: $showALHView, selectedALHView: $selectedALHView, isLogout: $isLogout)
        }
    }

    private func mapCheckInView(showsHeader: Bool) -> some View {
        MapCheckInView(showsHeader: showsHeader, homeScreenViewModel: homeScreenViewModel, checkINViewModel: checkINViewModel, showCheckIN: $showCheckIN, showCheckOUT: $showCheckOUT, motViewModel: motViewModel, selecetedMode: $selectedMode, tappedMode: $tappedMode, isTaskRunning: $isTaskRunning)
            .environmentObject(profileImageLoader)
    }

    /// Splits the single "show map" flag into a sheet (compact width) or a full-screen cover (regular width).
    private func mapCheckInPresentation(fullScreen: Bool) -> Binding<Bool> {
        Binding(
            get: { showMapCheckInView && (horizontalSizeClass == .regular) == fullScreen },
            set: { isPresented in
                if !isPresented { showMapCheckInView = false }
            }
        )
    }

    private var qrCodeModal: some View {
        ModalOverlayView(backgroundOpacity: 0.58, isScrollable: false, dismissOnBackgroundTap: {
            showQRCode = false
        }) {
            VStack(spacing: AppSpacing.md) {
                QRCodePopupView(
                    imageURL: $imageURL,
                    downloadPDF: $downloadPDF,
                    isImageLoaded: $isImagedLoaded,
                    qrCodeImage: $qrCodeImage,
                    showQrCode: $showQRCode,
                    pdfURL: $pdfURL,
                    isShareSheetPresented: $isShareSheetPresented
                )

                QrDownloadButton {
                    if isImagedLoaded {
                        downloadPDF.toggle()
                    }
                }
            }
            .frame(maxWidth: .infinity, alignment: .center)
        }
        .sheet(isPresented: $isShareSheetPresented) {
            if let pdfURL = pdfURL {
                ActivityViewController(activityItems: [pdfURL])
            }
        }
    }
    
    private func tabTitle(selectedTab: Int) -> String {
        switch selectedTab {
        case 0:
            // Wide layouts (iPad) have room for a title; on iPhone the greeting fills that role.
            return horizontalSizeClass == .regular ? "Home" : ""
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
