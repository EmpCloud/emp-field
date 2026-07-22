//
//  MapCheckInView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 25/07/24.
//

import SwiftUI
import MapKit

struct MapCheckInView: View {
    
    @EnvironmentObject var permissionManager: PermissionManager
    @EnvironmentObject var timerManager: TimerManager
    @EnvironmentObject var searchLocationViewModel: SearchLocationViewModel
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    @ObservedObject var homeScreenViewModel: HomeScreenViewModel
    @ObservedObject var checkINViewModel: CheckINViewModel
    
    @Binding var showCheckIN: Bool
    @Binding var showCheckOUT: Bool
    @State private var currentDeviceTime: String = ""
    
    //UserInfo
    @State private var name: String = "-----"
    @State private var department: String = "-----"
    
    //For checkingOUT
    @State private var showCheckOUTAlert: Bool = false
    @State private var yesCheckOut: Bool = false
    
    //For Mode of Travel
    @ObservedObject var motViewModel: MOTViewModel
    @Binding var selecetedMode: String?
    @Binding var tappedMode: String?
    @State private var showMOTPopup: Bool = false
    
    //Warning
    @State private var showWarning: Bool = false
    
    //Task Status used to manage the checkOUT
    @Binding var isTaskRunning: Bool
    
    
    @State var userLocation: CLLocationCoordinate2D?
    
    //Office location
     private var officeLocation: CLLocationCoordinate2D {
        return CLLocationCoordinate2D(latitude: lat, longitude: long)
    }
    
    var lat: Double {
        if let latitude = Double(homeScreenViewModel.homeScreenData?.orglatitude ?? "\(permissionManager.userLocation?.coordinate.latitude ?? 0.0)"){
            return latitude
        }
        return 0.0
    }
    var long: Double {
        if let longitude = Double(homeScreenViewModel.homeScreenData?.orglongitude ?? "\(permissionManager.userLocation?.coordinate.longitude ?? 0.0)"){
            return longitude
        }
        return 0.0
    }
    
    //Radius of CheckIN
    @State private var checkINRadius: Int?
    
    @State private var checkINAccessDistance: Int?
    
    var body: some View {
        
        ZStack {
            LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                .ignoresSafeArea()
            VStack {
                //MARK: User Name
                HStack(spacing: 20) {
                    ProfileMediumView()
                        .environmentObject(profileImageLoader)
                        .padding()
                        .padding(.leading, 20)
                    
                    VStack(alignment: .leading) {
                        Text(name)
                            .font(AppFont.primary(size: AppFont.Size.navigationTitle))
                            .fontWeight(AppFont.Weight.bold)
                        
                        Text(department)
                            .font(AppFont.primary(size: AppFont.Size.xSmall))
                    }
                    .foregroundStyle(.white)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(10)
                
                //MARK: Map View
                VStack(spacing: 7) {
                    
                    
                    //MARK: MAP View
                    //                   EmpMapViewRepresentable()
                    CheckINMap(userLocation: $userLocation, officeLocation: officeLocation, radius: CLLocationDistance(checkINRadius ?? 100), checkINAccessDistance: $checkINAccessDistance)
                        .overlay(alignment: .bottom) {
                            if homeScreenViewModel.homeScreenData?.isGeoFencingOn == 1 {
                                if checkINAccessDistance ?? 0 > homeScreenViewModel.homeScreenData?.orgRadius ?? 500 {
                                    GeoFencingWarningView()
                                        .padding()
                                }
                            }
                            
                        }
                    
                    Text("\(HelperFunction.shared.todaysDate())")
                        .font(AppFont.primary(size: AppFont.Size.title))
                        .foregroundStyle(Color.welcomeText)
                    
                    HStack(alignment: .bottom) {
                        //                        Text("09:55")
                        //                            .font(AppFont.primary(size: AppFont.Size.navigationTitle))
                        //                        Text("am")
                        //                            .font(AppFont.primary(size: AppFont.Size.body))
                        //                            .padding(.leading, -8)
                        LiveTimeView()
                    }
                    .padding(.vertical, 5)
                    .foregroundStyle(Color.welcomeText)
                    
                    
                    //MARK: Check in-out Timer
                    HStack(spacing: 0){
                        Rectangle()
                            .fill(Color(UIColor(red: 212/255, green: 235/255, blue: 255/255, alpha: 1.0)))
                            .overlay {
                                VStack {
                                    HStack{
                                        Image(systemName: "checkmark.square.fill")
                                            .resizable()
                                            .scaledToFit()
                                            .foregroundStyle(Color.primaryButton1)
                                            .frame(width: 12, height: 12)
                                        Text("IN Time")
                                            .font(AppFont.primary(size: AppFont.Size.xSmall))
                                            .foregroundStyle(Color(red: 79/255, green: 78/255, blue: 78/255, opacity: 1.0))
                                    }
                                    
                                    HStack(alignment: .bottom){
                                        Text(HelperFunction.shared.formatCheckTime(from: homeScreenViewModel.checkINTime) ?? "--:--")
                                            .font(AppFont.primary(size: AppFont.Size.title))
	                                        Text("AM")
                                            .font(AppFont.primary(size: AppFont.Size.caption))
                                            .padding(.leading, -5)
                                            .padding(.bottom, 3)
                                    }
                                    .padding(.leading)
                                    .foregroundStyle(Color(red: 20/255, green: 20/255, blue: 20/255, opacity: 1.0))
                                }
                                .frame(maxWidth: .infinity, alignment: .leading)
                                
                            }
                        
                        Rectangle()
                            .fill(Color(UIColor(red: 238/255, green: 247/255, blue: 255/255, alpha: 1.0)))
                            .overlay {
                                VStack {
                                    HStack{
                                        Image(systemName: "checkmark.square")
                                            .resizable()
                                            .scaledToFit()
                                            .foregroundStyle(Color.primaryButton1)
                                            .frame(width: 12, height: 12)
                                        Text("OUT Time")
                                            .font(AppFont.primary(size: AppFont.Size.xSmall))
                                            .foregroundStyle(Color(red: 79/255, green: 78/255, blue: 78/255, opacity: 1.0))
                                    }
                                    .padding(.leading)
                                    
                                    HStack(alignment: .bottom){
                                        Text(HelperFunction.shared.formatCheckTime(from: homeScreenViewModel.checkOUTTime) ?? "--:--")
                                            .font(AppFont.primary(size: AppFont.Size.title))
	                                        Text("AM")
                                            .font(AppFont.primary(size: AppFont.Size.caption))
                                            .padding(.leading, -5)
                                            .padding(.bottom, 3)
                                    }
                                    .padding(.leading)
                                    .foregroundStyle(Color(red: 20/255, green: 20/255, blue: 20/255, opacity: 1.0))
                                }
                                .frame(maxWidth: .infinity, alignment: .leading)
                                
                            }
                        
                    }
	                    .frame(maxWidth: 300)
	                    .frame(minHeight: 70)
	                    .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
                    .padding(.horizontal, 30)
                    
                    
                    
                    VStack{
                        //MARK: Swipe Button
                        if showCheckIN {
                            
                            if homeScreenViewModel.homeScreenData?.isGeoFencingOn == 1 {  // valdiate if the geoLocation ON
                                if checkINAccessDistance ?? 0 > homeScreenViewModel.homeScreenData?.orgRadius ?? 500 {
                                    CheckINDisableButtonView(text: "Swipe to check IN") {
                                        //DO nothing
                                    }
                                    .disableWithOpacity(true)
                                }
                                // Condition when geoFencing is on and User is inside the fencing
                                else{
                                    CheckInSwipeButtonView()
                                        .onSwipeSuccess {
                                            self.showCheckIN = false
                                            self.showCheckOUT = true
                                            self.showMOTPopup = true  // Mode of travel visiblity
                                            currentDeviceTime = HelperFunction.shared.currentTime()
                                            AppLog.debug("Current Time: \(currentDeviceTime)")
                                            AppLog.debug("Lat: \(permissionManager.userLocation?.coordinate.latitude ?? 0)")
                                            AppLog.debug("Long: \(permissionManager.userLocation?.coordinate.longitude ?? 0)")
                                            Task {
                                                checkINViewModel.checkINTime = currentDeviceTime
                                                if let lat = permissionManager.userLocation?.coordinate.latitude{
                                                    checkINViewModel.checkINLatitude = Double(lat)
                                                }
                                                if let long = permissionManager.userLocation?.coordinate.longitude {
                                                    checkINViewModel.checkINLongitude = Double(long)
                                                }
                                                try await checkINViewModel.markAttendance()
                                                if NetworkManager.shared.statusCode != 200 {
                                                    showWarning.toggle()
                                                    showCheckIN = true
                                                    showCheckOUT = false
                                                    UserDefaults.standard.setValue(false, forKey: "isCheckedIN")
                                                    return
                                                }
                                                AppLog.debug("CheckIN: Attendance marked")
                                                homeScreenViewModel.checkINTime = checkINViewModel.checkINTime
                                                timerManager.startActiveTimer()
                                                UserDefaults.standard.setValue(true, forKey: "isCheckedIN")
                                                permissionManager.startLocationUpdate()
                                            }
                                            
                                            // to refresh the screen after checkIN
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
                                                    }else if homeScreenViewModel.checkINTime != "--:--" && homeScreenViewModel.checkINTime != ""{
                                                        timerManager.activeTime = timerManager.timeDifference(from: homeScreenViewModel.checkINTime, to: nil) ?? 0
                                                        timerManager.startActiveTimer()
                                                    }
                                                }
                                                else{
                                                    UserDefaults.standard.setValue(false, forKey: "isCheckedIN")
                                                    showCheckIN = true
                                                    showCheckOUT = false
                                                }
                                                
                                                //                                            isBioMetrixCheckIN = homeScreenViewModel.homeScreenData?.isBioMetricEnabled ?? 0
                                                //                                            isWebCheckIN = homeScreenViewModel.homeScreenData?.isWebEnabled ?? 0
                                            }
                                        }
                                        .transition(AnyTransition.scale.animation(Animation.spring(response: 0.3, dampingFraction: 0.5)))
                                }
                            }
                            else{
                                CheckInSwipeButtonView()
                                    .onSwipeSuccess {
                                        self.showCheckIN = false
                                        self.showCheckOUT = true
                                        self.showMOTPopup = true  // Mode of travel visiblity
                                        currentDeviceTime = HelperFunction.shared.currentTime()
                                        AppLog.debug("Current Time: \(currentDeviceTime)")
                                        AppLog.debug("Lat: \(permissionManager.userLocation?.coordinate.latitude ?? 0)")
                                        AppLog.debug("Long: \(permissionManager.userLocation?.coordinate.longitude ?? 0)")
                                        Task {
                                            checkINViewModel.checkINTime = currentDeviceTime
                                            if let lat = permissionManager.userLocation?.coordinate.latitude{
                                                checkINViewModel.checkINLatitude = Double(lat)
                                            }
                                            if let long = permissionManager.userLocation?.coordinate.longitude {
                                                checkINViewModel.checkINLongitude = Double(long)
                                            }
                                            try await checkINViewModel.markAttendance()
                                            if NetworkManager.shared.statusCode != 200 {
                                                showWarning.toggle()
                                                showCheckIN = true
                                                showCheckOUT = false
                                                UserDefaults.standard.setValue(false, forKey: "isCheckedIN")
                                                return
                                            }
                                            AppLog.debug("CheckIN: Attendance marked")
                                            homeScreenViewModel.checkINTime = checkINViewModel.checkINTime
                                            timerManager.startActiveTimer()
                                            UserDefaults.standard.setValue(true, forKey: "isCheckedIN")
                                            permissionManager.startLocationUpdate()
                                        }
                                        
                                        // to refresh the screen after checkIN
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
                                                }else if homeScreenViewModel.checkINTime != "--:--" && homeScreenViewModel.checkINTime != ""{
                                                    timerManager.activeTime = timerManager.timeDifference(from: homeScreenViewModel.checkINTime, to: nil) ?? 0
                                                    timerManager.startActiveTimer()
                                                }
                                            }
                                            else{
                                                UserDefaults.standard.setValue(false, forKey: "isCheckedIN")
                                                showCheckIN = true
                                                showCheckOUT = false
                                            }
                                            
                                            //                                            isBioMetrixCheckIN = homeScreenViewModel.homeScreenData?.isBioMetricEnabled ?? 0
                                            //                                            isWebCheckIN = homeScreenViewModel.homeScreenData?.isWebEnabled ?? 0
                                        }
                                    }
                                    .transition(AnyTransition.scale.animation(Animation.spring(response: 0.3, dampingFraction: 0.5)))
                            }
                            
                        }
                        
                        if showCheckOUT {
                            
                            if homeScreenViewModel.homeScreenData?.isGeoFencingOn == 1 {
                                if checkINAccessDistance ?? 0 > homeScreenViewModel.homeScreenData?.orgRadius ?? 500 {
                                    CheckOUTDisableButtonView(text: "Swipe to check OUT") {
                                        //DO nothing
                                    }
                                    .disableWithOpacity(true)
                                }
                                // Condition when geoFencing is on and User is inside the fencing
                                else{
                                    CheckOutSwipeButtonView()
                                        .onSwipeSuccess{
                                            showCheckOUT = false
                                            
                                            Task {
                                                currentDeviceTime = HelperFunction.shared.currentTime()
                                                AppLog.debug("Current Time: \(currentDeviceTime)")
                                                checkINViewModel.checkINTime = currentDeviceTime
                                                if let lat = permissionManager.userLocation?.coordinate.latitude{
                                                    checkINViewModel.checkINLatitude = Double(lat)
                                                }
                                                if let long = permissionManager.userLocation?.coordinate.longitude {
                                                    checkINViewModel.checkINLongitude = Double(long)
                                                }
                                                //show checkOutAlert
                                                showCheckOUTAlert.toggle()
                                                //                                            try await checkINViewModel.markAttendance()
                                                //                                            AppLog.debug("CheckOUT: Attendance marked")
                                                //
                                                //                                            timerManager.stopActiveTimer()
                                            }
                                            
                                            // to refresh the screen after checkIN
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
                                                    }else if homeScreenViewModel.checkINTime != "--:--" && homeScreenViewModel.checkINTime != ""{
                                                        timerManager.activeTime = timerManager.timeDifference(from: homeScreenViewModel.checkINTime, to: nil) ?? 0
                                                        timerManager.startActiveTimer()
                                                    }
                                                }
                                                else{
                                                    UserDefaults.standard.setValue(false, forKey: "isCheckedIN")
                                                    showCheckIN = true
                                                    showCheckOUT = false
                                                }
                                                
                                                //                                            isBioMetrixCheckIN = homeScreenViewModel.homeScreenData?.isBioMetricEnabled ?? 0
                                                //                                            isWebCheckIN = homeScreenViewModel.homeScreenData?.isWebEnabled ?? 0
                                            }
                                        }
                                        .transition(AnyTransition.scale.animation(Animation.spring(response: 0.3, dampingFraction: 0.5)))
                                        .onDisappear(){
                                            showCheckOUT = true
                                        }
                                    
                                }
                            }
                            else{
                                CheckOutSwipeButtonView()
                                    .onSwipeSuccess{
                                        showCheckOUT = false
                                        
                                        Task {
                                            currentDeviceTime = HelperFunction.shared.currentTime()
                                            AppLog.debug("Current Time: \(currentDeviceTime)")
                                            checkINViewModel.checkINTime = currentDeviceTime
                                            if let lat = permissionManager.userLocation?.coordinate.latitude{
                                                checkINViewModel.checkINLatitude = Double(lat)
                                            }
                                            if let long = permissionManager.userLocation?.coordinate.longitude {
                                                checkINViewModel.checkINLongitude = Double(long)
                                            }
                                            //show checkOutAlert
                                            showCheckOUTAlert.toggle()
                                            //                                            try await checkINViewModel.markAttendance()
                                            //                                            AppLog.debug("CheckOUT: Attendance marked")
                                            //
                                            //                                            timerManager.stopActiveTimer()
                                        }
                                        
                                        // to refresh the screen after checkIN
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
                                                }else if homeScreenViewModel.checkINTime != "--:--" && homeScreenViewModel.checkINTime != ""{
                                                    timerManager.activeTime = timerManager.timeDifference(from: homeScreenViewModel.checkINTime, to: nil) ?? 0
                                                    timerManager.startActiveTimer()
                                                }
                                            }
                                            else{
                                                UserDefaults.standard.setValue(false, forKey: "isCheckedIN")
                                                showCheckIN = true
                                                showCheckOUT = false
                                            }
                                            
                                            //                                            isBioMetrixCheckIN = homeScreenViewModel.homeScreenData?.isBioMetricEnabled ?? 0
                                            //                                            isWebCheckIN = homeScreenViewModel.homeScreenData?.isWebEnabled ?? 0
                                        }
                                    }
                                    .transition(AnyTransition.scale.animation(Animation.spring(response: 0.3, dampingFraction: 0.5)))
                                    .onDisappear(){
                                        showCheckOUT = true
                                    }
                                
                            }
                        }
                    }
	                    .frame(minHeight: AppLayout.checkInControlHeight)
                    .padding(.top)
                    
                }
                .background(Color.white)
                //                .clipShape(RoundedRectangle(cornerRadius: 25))
                //                .frame(maxHeight: .infinity)
            }
            .onChange(of: showWarning) { _, _ in
                showCheckOUTAlert = false
            }
            .onAppear {
                self.name = UserDefaults.standard.string(forKey: "UserName") ?? ""
                self.department = UserDefaults.standard.string(forKey: "UserDepartment") ?? ""
                self.checkINRadius = homeScreenViewModel.homeScreenData?.orgRadius
                
                // to send the user current location to MapView
                userLocation = CLLocationCoordinate2D(latitude: permissionManager.userLocation?.coordinate.latitude ?? 12.970005196080612, longitude: permissionManager.userLocation?.coordinate.longitude ?? 77.58629190248757)
            }
            
            
            //MARK: MOT Popup after checkIN
            if showMOTPopup {
                ModalOverlayView(dismissOnBackgroundTap: {
                    showMOTPopup.toggle()
                }) {
                    ModeOfTravelView(motViewModel: motViewModel, selectedMode: $selecetedMode, tappedMode: $tappedMode, showMode: $showMOTPopup)
                }
            }
            //MARK: CheckOUT Popup
            if showCheckOUTAlert {
                ModalOverlayView {
                    LogoutAlertPopupView(checkINViewModel: checkINViewModel, homeScreenViewModel: homeScreenViewModel, showCheckOUTAlert: $showCheckOUTAlert, yesCheckout: $yesCheckOut, showWarningPopup: $showWarning, isTaskRunning: $isTaskRunning)
                }
            }
            
            //MARK: Warning
            if showWarning {
                if NetworkManager.shared.statusCode == 403 {
                    ModalOverlayView {
                        WarningPopupView(
                            titleText: attendanceWarningTitle,
                            description: attendanceWarningDescription,
                            showWarningPopup: $showWarning
                        )
                    }
                }else {
                    ModalOverlayView {
                        WarningPopupView(titleText: "Try Again", description: NetworkManager.shared.responseMessage, showWarningPopup: $showWarning)
                    }
                }
                
            }
        }
        
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
}

#Preview {
    MapCheckInView(homeScreenViewModel: HomeScreenViewModel(), checkINViewModel: CheckINViewModel(), showCheckIN: .constant(false), showCheckOUT: .constant(false), motViewModel: MOTViewModel(), selecetedMode: .constant(""), tappedMode: .constant(nil), isTaskRunning: .constant(false))
        .environmentObject(SearchLocationViewModel())
        .environmentObject(PermissionManager())
        .environmentObject(TimerManager())
}
