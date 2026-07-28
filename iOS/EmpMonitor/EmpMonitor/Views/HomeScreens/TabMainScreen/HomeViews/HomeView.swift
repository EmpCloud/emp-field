//
//  HomeView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import SwiftUI

struct HomeView: View {
    
    
    
//    @EnvironmentObject var appState: AppState    //-------------logout changes -------
    @EnvironmentObject var permissionManager: PermissionManager
    @EnvironmentObject var timerManager: TimerManager
    
    @ObservedObject var checkINViewModel: CheckINViewModel
    @ObservedObject var homeScreenViewModel: HomeScreenViewModel
    @ObservedObject var motViewModel: MOTViewModel
    
    //UserInfo
    @State private var name: String = "-----"
    @State private var department: String = "-----"
    
    @Binding var selectedALHView: String?
    
    @Binding var showCheckIN: Bool
    @Binding var showCheckOUT: Bool
    
    @Binding var showSideMenu: Bool
    @Binding var showMapCheckInView: Bool
    @Binding var showALHView: Bool
    @State private var currentDeviceTime: String = ""
    
    @State private var isBioMetrixCheckIN: Int = 0
    @State private var isWebCheckIN: Int = 0
    @State private var showFaceCheckIn: Bool = false
    
    @State private var date = Date()
    
    //Checkout
    @State private var showCheckOUTAlert: Bool = false
    @State private var yesCheckOut: Bool = false
    
    
    //Warning
    @State private var showWarning: Bool = false

    //Toast
    @State private var toastMessage: ToastMessage?
    
    
    //Mode of travel
    @State private var showMOTPopup: Bool = false
    @Binding var selectedMode: String?
    @Binding var tappedMode: String?
    
    // to refresh the screen
    @State private var offset: CGFloat = 0
    @State private var refreshing: Bool = false
    
    //Task Status used to manage the checkOUT
    @Binding var isTaskRunning: Bool
    
    var body: some View {
        NavigationStack {
                VStack {
                        ZStack{
                            
                            //MARK: Background
                            LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                                .ignoresSafeArea()
                            
//                            ScrollView {
                                VStack(alignment: .leading){
                                    //MARK: Content
                                    HStack(alignment: .top, spacing: AppSpacing.stackSpacingDefault) {
                                        VStack(alignment: .leading, spacing: AppSpacing.formFieldSpacing) {
                                            Text("Hi!")
                                                .font(AppFont.primary(size: AppFont.Size.title, weight: AppFont.Weight.regular))
                                                .fontWeight(AppFont.Weight.bold)
                                            
                                            Text(name)
                                                .font(AppFont.primary(size: AppFont.Size.navigationTitle, weight: AppFont.Weight.regular))
                                                .fontWeight(AppFont.Weight.semibold)
                                                .lineLimit(1)
                                                .truncationMode(.tail)
                                            
                                            Text(department)
                                                .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                                                .lineLimit(1)
                                                .truncationMode(.tail)
                                        }
                                        .foregroundStyle(.white)
                                        
                                        Spacer(minLength: AppSpacing.iconTextSpacing)
                                        
                                        Button {
                                            withAnimation {
                                                showMapCheckInView.toggle()
                                            }
                                        } label: {
                                            Text("Map")
                                                .font(AppFont.primary(size: AppFont.Size.callout, weight: AppFont.Weight.bold))
                                                .foregroundStyle(.white)
                                                .underline()
                                                .frame(minWidth: 44, minHeight: 44, alignment: .trailing)
                                        }
                                        .buttonStyle(.plain)
                                        .accessibilityLabel("Open map check-in")
                                    }
                                    .padding(.top)
                                    .padding(.leading, 60)
                                    .padding(.trailing, 24)
                                    
                                    
                                    //MARK: RoundedRectangle
                                    ZStack(alignment: .topLeading){
                                        
                                        VStack(spacing: 20) {
                                            
                                            ScrollView {
                                                VStack(alignment: .leading) {
                                                    
                                                    //MARK: Yesterday's Record
                                                    Text("Yesterday's Record")
                                                        .font(AppFont.primary(size: AppFont.Size.title3, weight: AppFont.Weight.regular))
                                                        .fontWeight(AppFont.Weight.medium)
                                                        .foregroundStyle(Color.headingText)
                                                        .padding(3)
//                                                        .padding(.top, 10)
                                                    
                                                    RoundedRectangle(cornerRadius: 20)
                                                        .fill(Color.white)
                                                        .frame(height: 181)
                                                        .frame(maxWidth: .infinity, alignment: .topLeading)
                                                        .overlay(alignment: .topLeading) {
                                                            VStack(alignment: .leading) {
                                                                Text("\(HelperFunction.shared.getYesterdayDateString())")
                                                                    .font(AppFont.primary(size: AppFont.Size.subheadline, weight: AppFont.Weight.regular))
                                                                    .foregroundStyle(Color.yesterdaysDate)
                                                                    .padding(.top)
                                                                    .padding(.leading, 40)
                                                                
                                                                HStack(alignment: .top) {
                                                                    DistanceTravelledView(homeScreenViewModel: homeScreenViewModel)
                                                                    HoursWorkedView(homeScreenViewModel: homeScreenViewModel)
                                                                    TaskCompletedView(homeScreenViewModel: homeScreenViewModel)
                                                                }
                                                                .padding(.horizontal)
                                                            }
                                                        }
                                                    
                                                    
                                                    //MARK: Attendence, Leaves, Holidays
                                                    HStack(spacing: 0) {
                                                        Spacer(minLength: 0)
                                                        AttendenceHistoryBoxView()
                                                            .onTapGesture {
                                                                selectedALHView = "Attendance History"
                                                                showALHView.toggle()
                                                            }
                                                        Spacer(minLength: 0)
                                                        LeavesBoxView()
                                                            .onTapGesture {
                                                                selectedALHView = "Leaves"
                                                                showALHView.toggle()
                                                            }
                                                        Spacer(minLength: 0)
                                                        HolidayBoxView()
                                                            .onTapGesture {
                                                                selectedALHView = "Holidays"
                                                                showALHView.toggle()
                                                            }
                                                        Spacer(minLength: 0)
                                                    }
                                                    .padding(.top)
                                                    .padding(.bottom, 50)
                                                }
                                                .padding()
                                                .padding(.top, 115)
                                            }
                                            .scrollIndicators(.hidden)
                                            .refreshable {
                                                Task {
                                                    try await homeScreenViewModel.getHomeScreenData()
                                                    
                                                    if homeScreenViewModel.checkData?.data.checkIn != nil {
                                                        showCheckIN = false
                                                        showCheckOUT = true
                                                        
                                                        //to automatically start the timer is CheckOUT timer is nill
                                                        if homeScreenViewModel.checkOUTTime != "--:--" && homeScreenViewModel.checkINTime != ""{
                                                            timerManager.activeTime = timerManager.timeDifference(from: homeScreenViewModel.checkINTime, to: homeScreenViewModel.checkOUTTime) ?? 0
                                                            //                            timerManager.startActiveTimer()
                                                            timerManager.stopActiveTimer()
                                                            
                                                            // stop tracking
                                                            permissionManager.stopLocationUpdates()
                                                            
                                                        }else if homeScreenViewModel.checkINTime != "--:--" && homeScreenViewModel.checkINTime != "" {
                                                            timerManager.activeTime = timerManager.timeDifference(from: homeScreenViewModel.checkINTime, to: nil) ?? 0
                                                            timerManager.startActiveTimer()
                                                            
                                                            // start tracking
                                                            UserDefaults.standard.set(true, forKey: "isCheckedIN")
                                                        }
                                                    }
                                                    else{
                                                        showCheckIN = true
                                                        showCheckOUT = false
                                                    }
                                                    
                                                    isBioMetrixCheckIN = homeScreenViewModel.homeScreenData?.isBioMetricEnabled ?? 0
                                                    isWebCheckIN = homeScreenViewModel.homeScreenData?.isWebEnabled ?? 0
                                                }
                                            }
                                           
                                        }
//                                        .padding(.bottom)
                                        .background(Color.rectangleBG)
                                        .shadow(color: Color.black.opacity(0.25), radius: 12)
                                        .frame(maxWidth: .infinity, minHeight: 525, maxHeight: .infinity, alignment: .topLeading)
                                        .clipShape(RoundedRectangle(cornerRadius: 20))
                                    }
                                    .padding(.top, 50)
                                    .overlay(alignment: .top) {
                                        //MARK: Top Swipe Button View
                                        VStack {
                                            VStack(spacing: AppSpacing.stackSpacingDefault) {
                                                HStack{
                                                    Text("\(HelperFunction.shared.todaysDate())")
                                                        .font(AppFont.primary(size: AppFont.Size.title3, weight: AppFont.Weight.regular))
                                                        .fontWeight(AppFont.Weight.semibold)
                                                        .foregroundStyle(Color.headingText)
                                                    Spacer()
                                                    if homeScreenViewModel.checkINTime != "" && homeScreenViewModel.checkINTime != "--:--" {
                                                        TotalTimeView()
                                                    }
                                                    
                                                }
                                                .padding(.horizontal)
                                                //
                                                HStack{
                                                    Text("Check In")
                                                    Spacer()
                                                    Text("Check Out")
                                                }
                                                .font(AppFont.primary(size: AppFont.Size.body))
                                                .foregroundStyle(
                                                    LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                                                )
                                                .padding(.horizontal)
                                                
                                                HStack {
                                                    HStack(alignment: .bottom, spacing: 0) {
                                                        Text(HelperFunction.shared.formatCheckTime(from: homeScreenViewModel.checkINTime) ?? "--:--")
                                                            .font(AppFont.primary(size: AppFont.Size.title2, weight: AppFont.Weight.regular))
                                                            .fontWeight(AppFont.Weight.medium)
                                                        Text(HelperFunction.shared.formatCheckMeridiem(from: homeScreenViewModel.checkINTime))
                                                            .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                                                            .fontWeight(AppFont.Weight.medium)
                                                        
                                                    }
                                                    .padding(.leading)
                                                    Spacer()
                                                    HStack(alignment: .bottom, spacing: 0) {
                                                        Text(HelperFunction.shared.formatCheckTime(from: homeScreenViewModel.checkOUTTime) ?? "--:--")
                                                            .font(AppFont.primary(size: AppFont.Size.title2, weight: AppFont.Weight.regular))
                                                            .fontWeight(AppFont.Weight.medium)
                                                        Text(HelperFunction.shared.formatCheckMeridiem(from: homeScreenViewModel.checkOUTTime))
                                                            .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                                                            .fontWeight(AppFont.Weight.medium)
                                                    }
                                                    //
                                                }
                                                .foregroundStyle(Color.headingText)

                                                VStack{
                                                    //MARK: Swipe Button
                                                    let geoFencingOn = UserDefaults.standard.integer(forKey: "isGeoFencingOn")
                                                    let autoGeo      = UserDefaults.standard.integer(forKey: "autoCheckInByGeoFencing")

                                                    if geoFencingOn == 1 {
                                                        // Geo-fence mode.
                                                        // autoGeo == 0: show swipe button only when user is inside the fence (manual check-in).
                                                        // autoGeo == 1: check-in fires automatically via shouldAutoCheckIn — no button shown.
                                                        if autoGeo != 1 && permissionManager.isInsideGeoFence && showCheckIN {
                                                            CheckInSwipeButtonView()
                                                                .onSwipeSuccess {
                                                                    self.showCheckIN = false
                                                                    self.showCheckOUT = true
                                                                    self.showMOTPopup = true
                                                                    currentDeviceTime = HelperFunction.shared.currentTime()
                                                                    Task {
                                                                        checkINViewModel.checkINTime = currentDeviceTime
                                                                        if let lat = permissionManager.userLocation?.coordinate.latitude {
                                                                            checkINViewModel.checkINLatitude = Double(lat)
                                                                        }
                                                                        if let long = permissionManager.userLocation?.coordinate.longitude {
                                                                            checkINViewModel.checkINLongitude = Double(long)
                                                                        }
                                                                        UserDefaults.standard.removeObject(forKey: "offlineLocations")
                                                                        do {
                                                                            try await checkINViewModel.markAttendance()
                                                                        } catch {
                                                                            handleCheckInRequestError(error)
                                                                            return
                                                                        }
                                                                        if NetworkManager.shared.statusCode != 200 {
                                                                            showWarning.toggle()
                                                                            showMOTPopup = false
                                                                            showCheckIN = true
                                                                            showCheckOUT = false
                                                                            UserDefaults.standard.setValue(false, forKey: "isCheckedIN")
                                                                            return
                                                                        }
                                                                        AppLog.debug("CheckIN (geo-fence manual): Attendance marked")
                                                                        homeScreenViewModel.checkINTime = checkINViewModel.checkINTime
                                                                        timerManager.startActiveTimer()
                                                                        toastMessage = ToastMessage(style: .success, message: "Checked in successfully")
                                                                        UserDefaults.standard.setValue(true, forKey: "isCheckedIN")
                                                                        permissionManager.startLocationUpdate()
                                                                        try await homeScreenViewModel.getHomeScreenData()
                                                                    }
                                                                }
                                                                .transition(AnyTransition.scale.animation(Animation.spring(response: 0.3, dampingFraction: 0.5)))
                                                        } else if showCheckOUT {
                                                            CheckOutSwipeButtonView()
                                                                .onSwipeSuccess {
                                                                    presentCheckoutAlert()
                                                                }
                                                                .transition(AnyTransition.scale.animation(Animation.spring(response: 0.3, dampingFraction: 0.5)))
                                                        }

                                                    } else if let mobileEnable = homeScreenViewModel.homeScreenData?.isMobileDeviceEnabled, mobileEnable == 1 {
                                                        // Standard mobile check-in via swipe
                                                        if showCheckIN {
                                                            CheckInSwipeButtonView()
                                                                .onSwipeSuccess {
                                                                    self.showCheckIN = false
                                                                    self.showCheckOUT = true
                                                                    self.showMOTPopup = true
                                                                    currentDeviceTime = HelperFunction.shared.currentTime()
                                                                    AppLog.debug("Current Time: \(currentDeviceTime)")
                                                                    Task {
                                                                        checkINViewModel.checkINTime = currentDeviceTime
                                                                        if let lat = permissionManager.userLocation?.coordinate.latitude {
                                                                            checkINViewModel.checkINLatitude = Double(lat)
                                                                        }
                                                                        if let long = permissionManager.userLocation?.coordinate.longitude {
                                                                            checkINViewModel.checkINLongitude = Double(long)
                                                                        }
                                                                        UserDefaults.standard.removeObject(forKey: "offlineLocations")
                                                                        do {
                                                                            try await checkINViewModel.markAttendance()
                                                                        } catch {
                                                                            handleCheckInRequestError(error)
                                                                            return
                                                                        }
                                                                        if NetworkManager.shared.statusCode != 200 {
                                                                            showWarning.toggle()
                                                                            showMOTPopup = false
                                                                            showCheckIN = true
                                                                            showCheckOUT = false
                                                                            UserDefaults.standard.setValue(false, forKey: "isCheckedIN")
                                                                            return
                                                                        }
                                                                        AppLog.debug("CheckIN: Attendance marked")
                                                                        homeScreenViewModel.checkINTime = checkINViewModel.checkINTime
                                                                        timerManager.startActiveTimer()
                                                                        toastMessage = ToastMessage(style: .success, message: "Checked in successfully")
                                                                        UserDefaults.standard.setValue(true, forKey: "isCheckedIN")
                                                                        permissionManager.startLocationUpdate()
                                                                        try await homeScreenViewModel.getHomeScreenData()
                                                                    }
                                                                }
                                                                .transition(AnyTransition.scale.animation(Animation.spring(response: 0.3, dampingFraction: 0.5)))
                                                        } else if showCheckOUT {
                                                            CheckOutSwipeButtonView()
                                                                .onSwipeSuccess {
                                                                    presentCheckoutAlert()
                                                                }
                                                                .transition(AnyTransition.scale.animation(Animation.spring(response: 0.3, dampingFraction: 0.5)))
                                                        }

                                                    } else {
                                                        // Mobile device not enabled — biometric / web check-in
                                                        let bioOn = homeScreenViewModel.homeScreenData?.isBioMetricEnabled ?? 0
                                                        let webOn = homeScreenViewModel.homeScreenData?.isWebEnabled ?? 0

                                                        if bioOn == 1 && webOn == 1 {
                                                            // Both: show face biometric button first, then web
                                                            VStack(spacing: 10) {
                                                                if showCheckIN {
                                                                    CheckINBioMetrixView()
                                                                        .onTap { showFaceCheckIn = true }
                                                                }
                                                                CheckINViaWebBioView()
                                                            }
                                                        } else if bioOn == 1 {
                                                            if showCheckIN {
                                                                CheckINBioMetrixView()
                                                                    .onTap { showFaceCheckIn = true }
                                                            }
                                                            if showCheckOUT {
                                                                CheckOutSwipeButtonView()
                                                                    .onSwipeSuccess {
                                                                        presentCheckoutAlert()
                                                                    }
                                                            }
                                                        } else if webOn == 1 {
                                                            CheckINWebView()
                                                        } else {
                                                            EmptyView()
                                                        }
                                                    }

                                                }
                                            }
                                            .padding(.horizontal, AppSpacing.modalHorizontalPadding)
                                            .padding(.vertical, AppSpacing.controlInnerPadding)
                                        }
	                                        .frame(maxWidth: .infinity)
	                                        .frame(minHeight: 170)
                                        .background(Color.white)
                                        .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
                                        .padding(.horizontal, AppSpacing.screenHorizontalPadding)
                                        
                                    }
                                    
                                }
                                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
                                .padding(.top, 10)
//                            }
                            
                            .onChange(of: showWarning) { _, _ in
                                showCheckOUTAlert = false
                            }
                           
                            
                            if showMOTPopup {
                                ModalOverlayView(dismissOnBackgroundTap: {
                                    withAnimation {
                                        showMOTPopup.toggle()
                                    }
                                }) {
                                    ModeOfTravelView(motViewModel: motViewModel, selectedMode: $selectedMode, tappedMode: $tappedMode, showMode: $showMOTPopup)
                                }
                                
                            }
                            
                           
                            
                            //MARK: CheckOUT Popup
                            if showCheckOUTAlert {
                                ModalOverlayView {
                                    LogoutAlertPopupView(checkINViewModel: checkINViewModel, homeScreenViewModel: homeScreenViewModel, showCheckOUTAlert: $showCheckOUTAlert, yesCheckout: $yesCheckOut, showWarningPopup: $showWarning, isTaskRunning: $isTaskRunning, onCheckoutSuccess: applyCheckoutSuccessUI)
                                        .environmentObject(permissionManager)
                                }
                                
                            }
                            
//                            //MARK: Warning
                            if showWarning {    
                                if isTaskRunning && yesCheckOut {
                                    ModalOverlayView {
                                        WarningPopupView(titleText: "Task Active", description: "Another task is currently in progress. Pause or complete it to initiate a new task.", showWarningPopup: $showWarning)
                                    }
                                }
                                else if NetworkManager.shared.statusCode == 403 {
                                    ModalOverlayView {
                                        WarningPopupView(
                                            titleText: attendanceWarningTitle,
                                            description: attendanceWarningDescription,
                                            showWarningPopup: $showWarning
                                        )
                                    }
                                }
                                else {
                                    ModalOverlayView {
                                        WarningPopupView(titleText: "Try Again", description: NetworkManager.shared.responseMessage, showWarningPopup: $showWarning)
                                    }
                                }
                                
                            }
                            
                            //Loading Screen
                            if homeScreenViewModel.isLoading {
                                ZStack {
                                    ProgressView()
                                }
                                .frame(maxWidth: .infinity, maxHeight: .infinity)
                                .background(Color.white.opacity(0.5))
                            }

                        }
                            .frame(maxWidth: .infinity, alignment: .topLeading)
                            .fullScreenCover(isPresented: $showFaceCheckIn) {
                                FaceCheckInView { checkedInTime in
                                    // Biometric face check-in succeeded
                                    showCheckIN = false
                                    showCheckOUT = true
                                    currentDeviceTime = checkedInTime
                                    homeScreenViewModel.checkINTime = checkedInTime
                                    showFaceCheckIn = false
                                    UserDefaults.standard.setValue(true, forKey: "isCheckedIN")
                                    permissionManager.startLocationUpdate()
                                    timerManager.startActiveTimer()
                                    toastMessage = ToastMessage(style: .success, message: "Biometric check-in successful")
                                    Task {
                                        try? await homeScreenViewModel.getHomeScreenData()
                                    }
                                }
                                .environmentObject(permissionManager)
                            }
                        //MARK: Navigation
                        .navigationDestination(isPresented: $showALHView)  {
                            ALHView(empName: $name, selection: $selectedALHView)
                                .navigationBarBackButtonHidden()
                        }
                        .onAppear {
                            
//                            if let savedData = UserDefaults.standard.data(forKey: "offlineLocations"),
//                               let decodedData = try? JSONDecoder().decode([TrackRequestModelData].self, from: savedData) {
//                                AppLog.debug("Saved Offline data")
//                                AppLog.debug(decodedData)
//                            }
                            
                            if NetworkManager.shared.statusCode == 400 && NetworkManager.shared.responseMessage == "Invalid access token...." {
                                //logout the user
                                AuthStore.shared.clearSession()
                            }
                            permissionManager.requestLocation()
                            
                            let isCheckedIn = UserDefaults.standard.bool(forKey: "isCheckedIN")
                            AppLog.debug("IsCheckIN: \(isCheckedIn)")
                            DeviceStatusDebug.log("Home loaded: isCheckedIN=\(isCheckedIn). Device-status endpoints run only after successful check-in/tracking starts")
                            
                            //Control the login/logout
                            //                AppState.shared.isLoggedIn = true
                            
                            //                let userProfile = AuthStore.shared.getUserProfileData(as: CreateProfileResponseModel.self)
                            self.name = UserDefaults.standard.string(forKey: "UserName") ?? ""
                            self.department = UserDefaults.standard.string(forKey: "UserDepartment") ?? ""
                            
                            Task {
                                
                                //print the tracking frequency
                                AppLog.debug("Frequency: \(permissionManager.distanceThreshold) & \(permissionManager.updateInterval)")
                                
                                try await homeScreenViewModel.getHomeScreenData()
                                
                                if NetworkManager.shared.statusCode == 200 {
                                    if homeScreenViewModel.checkData?.data.checkIn != nil {
                                        showCheckIN = false
                                        showCheckOUT = true
                                        
                                        //to automatically start the timer is CheckOUT timer is nill
                                        if homeScreenViewModel.checkOUTTime != "--:--" {
                                            timerManager.activeTime = timerManager.timeDifference(from: homeScreenViewModel.checkINTime, to: homeScreenViewModel.checkOUTTime) ?? 0
                                            //                            timerManager.startActiveTimer()
                                            timerManager.stopActiveTimer()
                                            
                                            // stop tracking
                                            permissionManager.stopLocationUpdates()
                                            
                                        }else{
                                            timerManager.activeTime = timerManager.timeDifference(from: homeScreenViewModel.checkINTime, to: nil) ?? 0
                                            timerManager.startActiveTimer()
                                            
                                            // start tracking
                                            UserDefaults.standard.setValue(true, forKey: "isCheckedIN")
                                            permissionManager.startLocationUpdate()
                                        }
                                    }
                                    else{
                                        UserDefaults.standard.setValue(false, forKey: "isCheckedIN")
                                        showCheckIN = true
                                        showCheckOUT = false

                                        // Auto check-in by mobile when flag is set from tracking settings
                                        let autoMobile = UserDefaults.standard.integer(forKey: "autoCheckInByMobile")
                                        let mobileEnabled = homeScreenViewModel.homeScreenData?.isMobileDeviceEnabled ?? 0
                                        if autoMobile == 1 && mobileEnabled == 1 {
                                            await performAutoCheckIn()
                                        }

                                        // Geo-fence auto check-in: handle the case where shouldAutoCheckIn
                                        // was already set before onChange registered (TabMainView called
                                        // requestGeoFenceState early and didDetermineState fired first).
                                        let autoGeoEarly = UserDefaults.standard.integer(forKey: "autoCheckInByGeoFencing")
                                        if autoGeoEarly == 1 && permissionManager.shouldAutoCheckIn {
                                            permissionManager.shouldAutoCheckIn = false
                                            await performAutoCheckIn()
                                        }
                                    }

                                    isBioMetrixCheckIN = homeScreenViewModel.homeScreenData?.isBioMetricEnabled ?? 0
                                    isWebCheckIN = homeScreenViewModel.homeScreenData?.isWebEnabled ?? 0

                                    selectedMode = homeScreenViewModel.homeScreenData?.currentMode
                                    tappedMode = selectedMode?.capitalized

                                    // Start geo-fence monitoring for all geo-fence modes (manual + auto)
                                    let geoFencingOn = homeScreenViewModel.homeScreenData?.isGeoFencingOn ?? 0
                                    if geoFencingOn == 1 {
                                        permissionManager.startGeoFenceMonitoring()
                                        permissionManager.requestGeoFenceState()
                                    }
                                }



                            }
                            
                            
                            // just for confirmation that data is present
                            //                if let loggedInUser: UserLoginResponseModel = AuthStore.shared.getLoggedInUser() {
                            //                    AppLog.debug("Logged in info")
                            //                    AppLog.debug(loggedInUser)
                            //                }
                            //                if let userProfile: CreateProfileResponseModel = AuthStore.shared.getUserProfileData(as: CreateProfileResponseModel.self) {
                            //                    AppLog.debug("User Profile info")
                            //                    AppLog.debug(userProfile)
                            //                }
                            //
                            //                AppLog.debug(UserDefaults.standard.string(forKey: "AppState"))
                        }
                        .onChange(of: permissionManager.shouldAutoCheckIn) { _, newValue in
                            if newValue {
                                permissionManager.shouldAutoCheckIn = false
                                Task { await performAutoCheckIn() }
                            }
                        }
                        .onChange(of: permissionManager.shouldAutoCheckOut) { _, newValue in
                            if newValue {
                                permissionManager.shouldAutoCheckOut = false
                                Task { await performAutoCheckOut() }
                            }
                        }

                        //
                        //MARK: Navigation Bar Button
                        //            .toolbar{
                        //                ToolbarItem(placement: .topBarLeading) {
                        //                    Image(.sideMenuIcon)
                        //                        .padding(.horizontal)
                        //                        .onTapGesture {
                        //                            withAnimation(.spring) {
                        //                                showSideMenu.toggle()
                        //                            }
                        //                        }
                        //
                        //
                        //                }
                        //                ToolbarItem(placement: .topBarTrailing) {
                        //                    BellIconView()
                        //                }
                        //                ToolbarItem(placement: .topBarTrailing) {
                        //                    ProfileSmallView()
                        //                        .padding(.horizontal)
                        //                }
                        //            }
                        //            .onAppear{
                        //                UINavigationBar.appearance().titleTextAttributes = [
                        //                    .foregroundColor: UIColor.white
                        //                ]
                        //            }
//                    }
                }
        }
        .toast(message: $toastMessage)
    }

    private var attendanceWarningTitle: String {
        if NetworkManager.shared.responseMessage.localizedCaseInsensitiveContains("check-in/check-out is disabled") {
            return "Attendance Restricted"
        }

        return NetworkManager.shared.errorMessage.isEmpty ? "Try Again" : NetworkManager.shared.errorMessage
    }

    private var attendanceWarningDescription: String {
        let backendMessage = NetworkManager.shared.responseMessage
        guard backendMessage.isEmpty == false else {
            return "Please try again."
        }

        if backendMessage.localizedCaseInsensitiveContains("biometric"),
           homeScreenViewModel.homeScreenData?.isBioMetricEnabled != 1 {
            return "Mobile app check-in/check-out is currently disabled for this account. Please contact your admin."
        }

        return backendMessage
    }

    private func presentCheckoutAlert() {
        AppLog.debug("Checkout swipe completed; showing confirmation")
        yesCheckOut = false
        withAnimation {
            showCheckOUTAlert = true
        }
    }

    private func applyCheckoutSuccessUI() {
        showCheckIN = true
        showCheckOUT = false
        yesCheckOut = false
        currentDeviceTime = ""
        toastMessage = ToastMessage(style: .success, message: "Checked out successfully")
    }

    private func performAutoCheckIn() async {
        guard showCheckIN, !UserDefaults.standard.bool(forKey: "isCheckedIN") else { return }
        let time = HelperFunction.shared.currentTime()
        checkINViewModel.checkINTime = time
        if let lat = permissionManager.userLocation?.coordinate.latitude {
            checkINViewModel.checkINLatitude = lat
        }
        if let long = permissionManager.userLocation?.coordinate.longitude {
            checkINViewModel.checkINLongitude = long
        }
        UserDefaults.standard.removeObject(forKey: "offlineLocations")
        do {
            try await checkINViewModel.markAttendance()
        } catch {
            if CheckINViewModel.blockedCheckInMessage(for: error) != nil {
                return
            }
            showWarning = true
            return
        }

        guard NetworkManager.shared.statusCode == 200 else {
            showWarning = true
            return
        }
        showCheckIN = false
        showCheckOUT = true
        // Don't show MOT popup for automatic check-in — user didn't initiate it manually
        homeScreenViewModel.checkINTime = checkINViewModel.checkINTime
        timerManager.startActiveTimer()
        toastMessage = ToastMessage(style: .success, message: "Auto check-in successful at \(time)")
        UserDefaults.standard.setValue(true, forKey: "isCheckedIN")
        UserDefaults.standard.set(Date(), forKey: "lastAutoCheckInTime")
        permissionManager.startLocationUpdate()
        try? await homeScreenViewModel.getHomeScreenData()
    }

    private func performAutoCheckOut() async {
        // Require at least 5 minutes since auto check-in to prevent GPS jitter from triggering
        // an immediate checkout right after a successful auto check-in.
        if let lastCheckIn = UserDefaults.standard.object(forKey: "lastAutoCheckInTime") as? Date,
           Date().timeIntervalSince(lastCheckIn) < 300 {
            return
        }
        guard showCheckOUT, UserDefaults.standard.bool(forKey: "isCheckedIN") else { return }
        let time = HelperFunction.shared.currentTime()
        checkINViewModel.checkINTime = time
        if let lat = permissionManager.userLocation?.coordinate.latitude {
            checkINViewModel.checkINLatitude = lat
        }
        if let long = permissionManager.userLocation?.coordinate.longitude {
            checkINViewModel.checkINLongitude = long
        }
        try? await checkINViewModel.markCheckOUTAttendance()

        if NetworkManager.shared.statusCode == 200 {
            showCheckOUT = false
            timerManager.stopActiveTimer()
            permissionManager.stopLocationUpdates()
            UserDefaults.standard.setValue(false, forKey: "isCheckedIN")
            toastMessage = ToastMessage(style: .success, message: "Auto check-out successful")
            try? await homeScreenViewModel.getHomeScreenData()
        }
    }

    private func handleCheckInRequestError(_ error: Error) {
        guard let blockedMessage = CheckINViewModel.blockedCheckInMessage(for: error) else {
            showWarning = true
            showMOTPopup = false
            showCheckIN = true
            showCheckOUT = false
            UserDefaults.standard.setValue(false, forKey: "isCheckedIN")
            return
        }

        showCheckIN = false
        showCheckOUT = true
        showMOTPopup = false
        if blockedMessage == CheckINViewModel.duplicateCheckInMessage {
            UserDefaults.standard.setValue(true, forKey: "isCheckedIN")
        }
        toastMessage = ToastMessage(style: .warning, message: blockedMessage)
    }
}

#Preview {
    HomeView(checkINViewModel: CheckINViewModel(), homeScreenViewModel: HomeScreenViewModel(), motViewModel: MOTViewModel(), selectedALHView: .constant(nil), showCheckIN: .constant(false), showCheckOUT: .constant(false), showSideMenu: .constant(false), showMapCheckInView: .constant(false), showALHView: .constant(false), selectedMode: .constant(nil), tappedMode: .constant(""), isTaskRunning: .constant(false))
        .environmentObject(PermissionManager())
        .environmentObject(TimerManager())
//        .environmentObject(AppState())    //-------------logout changes -------
}
