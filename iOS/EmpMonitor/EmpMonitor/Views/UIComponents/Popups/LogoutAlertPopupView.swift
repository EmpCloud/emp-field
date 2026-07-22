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
            VStack(alignment: .leading, spacing: AppSpacing.stackSpacingMedium) {
                HStack {
                    Text("Checkout Alert")
                        .font(AppFont.primary(size: AppFont.Size.subheadline))
                        .fontWeight(AppFont.Weight.medium)
                    
                    
                    Spacer()
                    
                    
                    Button {
                        withAnimation {
                            showCheckOUTAlert.toggle()
                        }
                    } label: {
                        Image(systemName: "xmark")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: AppLayout.closeIconSize, height: AppLayout.closeIconSize)
                            .fontWeight(AppFont.Weight.bold)
                            .foregroundStyle(Color.primaryButton1)
                            .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                            .contentShape(Rectangle())
                    }
                    .buttonStyle(.plain)
                    .accessibilityLabel("Close checkout alert")
                }
                
                Text("Checking out will stop live location tracking for the day.")
                    .font(AppFont.primary(size: AppFont.Size.callout))
                    .fontWeight(AppFont.Weight.light)
                    .fixedSize(horizontal: false, vertical: true)
                
                Text("Please confirm your action.")
                    .font(AppFont.primary(size: AppFont.Size.subheadline))
                    .fontWeight(AppFont.Weight.medium)
                    .fixedSize(horizontal: false, vertical: true)
                
                HStack(spacing: AppSpacing.stackSpacingDefault) {
                    PrimaryBorderButton(text: "Cancel") {
                        //TODO: to cancel the logout
                        yesCheckout = false
                        withAnimation {
                            showCheckOUTAlert.toggle()
                        }
                    }
                    
                    RedThinButton(text: "Check Out") {
                        //TODO: To checkout the user
                        Task {
                            do {
                                yesCheckout = true

                                if isTaskRunning { // to verify if any task is running or not
                                    showWarningPopup.toggle()
                                    return
                                }

                                checkINViewModel.checkINTime = HelperFunction.shared.currentTime()
                                if let lat = permissionManager.userLocation?.coordinate.latitude {
                                    checkINViewModel.checkINLatitude = lat
                                }
                                if let long = permissionManager.userLocation?.coordinate.longitude {
                                    checkINViewModel.checkINLongitude = long
                                }

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
                                } else {
                                    showWarningPopup.toggle()
                                }
                            } catch {
                                AppLog.debug("CheckOUT failed: \(error)")
                                showWarningPopup = true
                            }
                        }
                    }
                }
            }
            .padding(AppSpacing.pagePadding)
            .padding(.vertical, AppSpacing.stackSpacingDefault)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
            .padding(AppSpacing.pagePadding)
            
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
