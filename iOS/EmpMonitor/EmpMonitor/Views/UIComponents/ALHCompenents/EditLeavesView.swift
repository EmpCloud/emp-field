//
//  EditLeavesView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 16/08/24.
//

import SwiftUI

struct EditLeavesView: View {
    
    @StateObject private var leaveTypeViewModel = LeaveTypeViewModel()
    @StateObject private var leavesDeleteViewModel = LeavesDeleteViewModel()
    
    @ObservedObject var updateLeaveViewModel: UpdateLeaveViewModel
    
    @ObservedObject var leavesViewModel: LeavesViewModel
    
    @Binding var empName: String
    @Binding var startDate: String?
    @Binding var endDate: String?
    @Binding var setStartDate: Bool?
    @Binding var setEndDate: Bool?
    @Binding var reason: String
    
    //Dropdown
    @Binding var dateTypeSelection: String?
    @Binding var leaveTypeSelection: LeavesTypeResponseDetail?
    @Binding var options: [LeavesTypeResponseDetail]
    
  
    @State private var showDateTypeLeave: Bool = false
    @State private var showLeaveTypeLeave: Bool = false
    
    
    @Binding var showEditLeaves: Bool
    @Binding var showCalender: Bool
    
    var body: some View {
        ZStack {
            VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
                ZStack {
                    Text("Edit Leave")
                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.semibold))
                        .frame(maxWidth: .infinity)

                    HStack {
                        Spacer()

                        Button {
                            withAnimation {
                                startDate = nil
                                endDate = nil
                                showEditLeaves.toggle()
                            }
                        } label: {
                            Image(systemName: "xmark")
                                .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                        }
                        .buttonStyle(.plain)
                        .accessibilityLabel("Close edit leave")
                        .accessibilityAddTraits(.isButton)
                    }
                }
                .foregroundStyle(Color.attendanceTitleText)
                .frame(maxWidth: .infinity)
                .padding(.bottom, AppSpacing.xs)
                
                HStack(spacing: 0) {
                    Text("Employee Name")
                        .foregroundStyle(Color.text1)
                    Text("*")
                        .foregroundStyle(Color.red)
                }
                .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                .fontWeight(AppFont.Weight.medium)
                
                PopupTextField(text: $empName, placeholder: "Write Name")
                
                //MARK: Leave type
                HStack(alignment: .top, spacing: AppSpacing.stackSpacingDefault) {
                    VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
                        HStack(spacing: 0) {
                            Text("Day Type")
                            Text("*")
                                .foregroundStyle(Color.red)
                        }
                        PopupDropDownMenu(selection: $dateTypeSelection, showTypeLeave: $showDateTypeLeave, showLeaveTypeLeave: $showLeaveTypeLeave, options: ["First Half", "Second Half", "Full Day"], selectionTitle: dateTypeSelection ?? "First Half")
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)

                    VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
                        HStack(spacing: 0) {
                            Text("Leave Type")
                            Text("*")
                                .foregroundStyle(Color.red)
                        }
                        DynamicPopupDropdownMenu(selection: $leaveTypeSelection, showTypeLeave: $showLeaveTypeLeave, showDayTypeLeave: $showDateTypeLeave, options: options, selectionTitle: leaveTypeSelection?.name ?? "Leave Type")
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
                .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                .fontWeight(AppFont.Weight.medium)
                .foregroundStyle(Color.text1)
                
                //MARK: Leave Date(start/end)
                HStack(alignment: .top, spacing: AppSpacing.stackSpacingDefault) {
                    VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
                        HStack(spacing: 0) {
                            Text("Start Date")
                            Text("*")
                                .foregroundStyle(Color.red)
                        }
                        
                        RoundedRectangle(cornerRadius: AppRadius.small)
                            .fill(Color.rectangleBG)
                            .frame(maxWidth: .infinity)
                            .frame(minHeight: AppLayout.minimumTouchTarget)
                            .overlay(alignment: .leading) {
                                Text(startDate ?? "DD-MM-YYYY")
                                    .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                                    .foregroundStyle(Color.addressText2)
                                    .lineLimit(1)
                                    .minimumScaleFactor(0.8)
                                    .padding(.horizontal, AppSpacing.md)
                            }
                            .onTapGesture {
                                withAnimation {
                                    setStartDate = true
                                    setEndDate = false
                                    showCalender.toggle()
                                }
                            }
                            
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)

                    VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
                        HStack(spacing: 0) {
                            Text("End Date")
                            Text("*")
                                .foregroundStyle(Color.red)
                        }
                        
                        RoundedRectangle(cornerRadius: AppRadius.small)
                            .fill(Color.rectangleBG)
                            .frame(maxWidth: .infinity)
                            .frame(minHeight: AppLayout.minimumTouchTarget)
                            .overlay(alignment: .leading) {
                                Text(endDate ?? "DD-MM-YYYY")
                                    .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                                    .foregroundStyle(Color.addressText2)
                                    .lineLimit(1)
                                    .minimumScaleFactor(0.8)
                                    .padding(.horizontal, AppSpacing.md)
                            }
                            .onTapGesture {
                                withAnimation {
                                    setStartDate = false
                                    setEndDate = true
                                    showCalender.toggle()
                                }
                            }
                            
                        
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
                .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                .fontWeight(AppFont.Weight.medium)
                .foregroundStyle(Color.text1)
                
                
                //MARK: Reason
                
                HStack(spacing: 0) {
                    Text("Reason")
                    Text("*")
                        .foregroundStyle(Color.red)
                }
                .foregroundStyle(Color.text1)
                .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                .fontWeight(AppFont.Weight.medium)
                
//                RoundedRectangle(cornerRadius: 10)
//                    .fill(Color.rectangleBG)
//                    .frame(height: 68)
                
                LargeTextEditorView(descriptionText: $reason)
                
                
                //MARK: Buttons
                HStack(spacing: AppSpacing.stackSpacingDefault) {
                    
                    //MARK: Delete Leave Button
                    PrimaryBorderButton(text: "Delete Leave") {
                        //TODO: to show delete leaves popup
                        
                        Task{
                            leavesDeleteViewModel.leaveID = updateLeaveViewModel.leaveID
                            try await leavesDeleteViewModel.deleteLeave()
                            
                            if NetworkManager.shared.statusCode == 200 {
                                showEditLeaves.toggle()
                            }
                            
                            await leavesViewModel.getLeaves() // to referesh the leaves screen
                        }
                        
                    }
                    
                    //MARK: Save Button
                    PrimaryThinButton(text: "Save") {
                        //TODO: Save the updated leave
                        
                        Task {
                            if let dayType = dateTypeSelection {
                                updateLeaveViewModel.dayType = dayType
                            }
                            if let startDate = startDate {
                                updateLeaveViewModel.startDate = startDate
                            }
                            if let endDate = endDate {
                               updateLeaveViewModel.endDate = endDate
                            }
                            if let leaveType = leaveTypeSelection {
                                updateLeaveViewModel.leaveType = leaveType.id
                            }
                            updateLeaveViewModel.reason = reason
                            
                            try await updateLeaveViewModel.updateLeave()
                            
                            
                            await leavesViewModel.getLeaves() // to referesh the leaves screen
                            
                            withAnimation {
                                startDate = nil
                                endDate = nil
                                showEditLeaves.toggle()
                            }
                            
                        }
                        
                    }
                    .disableWithOpacity(empName.isEmpty || dateTypeSelection == nil || leaveTypeSelection == nil || startDate == nil || endDate == nil || reason.isEmpty)
                }
                .padding(.top, AppSpacing.xs)

            }
            .frame(maxWidth: 360)
            .padding(.horizontal, AppSpacing.lg)
            .padding(.vertical, AppSpacing.md)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.large))
        }
        .padding(AppSpacing.md)
//        .border(Color.black)
        .onAppear {
            Task {
//                startDate = updateLeaveViewModel.startDate
//                endDate = updateLeaveViewModel.endDate
                AppLog.debug(startDate)
                AppLog.debug(endDate)
                AppLog.debug("LeaveID: \(updateLeaveViewModel.leaveID)")
                AppLog.debug("Leave Type: \(leaveTypeSelection?.name)")
                
               try await leaveTypeViewModel.fetchLeaveType()
                
                if NetworkManager.shared.statusCode == 200 {
                    options = leaveTypeViewModel.leaveTypeData
                    leaveTypeSelection = leaveTypeViewModel.leaveTypeData.first // providing the default value
//                    dateTypeSelection =
                }
            }
        }
        
    }
}

//#Preview {
//    EditLeavesView(updateLeaveViewModel: UpdateLeaveViewModel(), leavesViewModel: LeavesViewModel(), empName: .constant(""), startDate: .constant("") , endDate: .constant(""), setStartDate: .constant(false), setEndDate: .constant(false), reason: .constant(""), showEditLeaves: .constant(false), showCalender: .constant(false))
//}
