//
//  LogoutAlertPopupView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/09/24.
//

import SwiftUI

struct LogoutAlertPopupView: View {
    
    @EnvironmentObject var timerManager: TimerManager
    @EnvironmentObject var permissionManager: PermissionManager
    
    @ObservedObject var checkINViewModel: CheckINViewModel
    @ObservedObject var homeScreenViewModel: HomeScreenViewModel
    
    @Binding var showCheckOUTAlert: Bool
    @Binding var yesCheckout: Bool
    @Binding var showWarningPopup: Bool

    //Task Status used to manage the checkOUT
    @Binding var isTaskRunning: Bool

    var onCheckoutSuccess: () -> Void = {}
    
    var body: some View {
        
        ZStack {
            VStack(alignment: .leading, spacing: 20) {
                HStack {
                    Text("Logout Alert")
                        .font(.custom("Montserrat", size: 15))
                        .fontWeight(.medium)
                    
                    
                    Spacer()
                    
                    
                    Image(systemName: "xmark")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 10.75, height: 10.75)
                        .fontWeight(.bold)
                        .foregroundStyle(Color.primaryButton1)
                        .padding(10)
                        .onTapGesture {
                            withAnimation {
                                showCheckOUTAlert.toggle()
                            }
                        }
                }
                
                Text("Proceeding further will stop your live location tracking and you'll be Checked out for the day!")
                    .font(.custom("Montserrat", size: 13))
                    .fontWeight(.light)
                
                Text("Please Confirm your Action.")
                    .font(.custom("Montserrat", size: 15))
                    .fontWeight(.medium)
                
                HStack {
                    PrimaryBorderButton(text: "Abort") {
                        //TODO: to cancel the logout
                        withAnimation {
                            showCheckOUTAlert.toggle()
                        }
                    }
                    
                    RedThinButton(text: "Yes, Check me out") {
                        //TODO: To checkout the user
                        Task {
                            yesCheckout = true
                            
                            if yesCheckout {
                                
                                if isTaskRunning { // to verify if any task is running or not
                                    showWarningPopup.toggle()
                                }else {
                                    //Check if there is offline data,  upload it then checkOUT
                                    await permissionManager.uploadOfflineLocations()
                                    
                                    try await checkINViewModel.markCheckOUTAttendance()
                                    
                                    if NetworkManager.shared.statusCode == 200 {
                                        AppLog.debug("CheckOUT: Attendance marked")

                                        timerManager.stopActiveTimer()
                                        UserDefaults.standard.setValue(false, forKey: "isCheckedIN") // stop the tracking

                                        // stop tracking
                                        permissionManager.stopLocationUpdates()

                                        // to refresh the screen after checkOUT
                                        try await homeScreenViewModel.getHomeScreenData()

                                        yesCheckout = false
                                        onCheckoutSuccess()

                                        withAnimation {
                                            showCheckOUTAlert.toggle()
                                        }
                                    }else if NetworkManager.shared.statusCode == 403 {
                                        showWarningPopup.toggle()
                                    }else {
                                        showWarningPopup.toggle()
                                    }
                                }
                                
                                
                            }
                            
                            
                        }
                    }
                }
            }
            .padding()
            .padding(.vertical, 10)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: 10))
            .padding()
            
//            //MARK: Warning
//            if showWarningPopup {
//                ZStack {
//                    if NetworkManager.shared.responseMessage == "Checkout Restricted" {
//                        WarningPopupView(titleText: "Checkout Restricted", description: "Check Before 1 hour is not allowed", showWarningPopup: $showWarningPopup)
//                    }
//                }
//                .frame(maxWidth: .infinity, maxHeight: .infinity)
//                .background(Color.black.opacity(0.5))
//                .onTapGesture {
//                    showWarningPopup.toggle()
//                }
                
                
//            }
        }
       
        
    }
}

#Preview {
    LogoutAlertPopupView(checkINViewModel: CheckINViewModel(), homeScreenViewModel: HomeScreenViewModel(), showCheckOUTAlert: .constant(false), yesCheckout: .constant(false), showWarningPopup: .constant(false), isTaskRunning: .constant(false))
        .environmentObject(TimerManager())
        .environmentObject(PermissionManager())
}
