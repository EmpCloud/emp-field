//
//  AddNewLeavesView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 31/07/24.
//

import SwiftUI

struct AddNewLeavesView: View {
    
    @StateObject private var leaveTypeViewModel = LeaveTypeViewModel()
    @StateObject private var createLeaveViewModel = CreateLeaveViewModel()
    
    @ObservedObject var leavesViewModel: LeavesViewModel
    
    @Binding var empName: String
    @Binding var startDate: String?
    @Binding var endDate: String?
    @Binding var setStartDate: Bool?
    @Binding var setEndDate: Bool?
    
    //Dropdown
    @Binding var dateTypeSelection: String?
    @State private var leaveTypeSelection: LeavesTypeResponseDetail?
    @State private var options: [LeavesTypeResponseDetail] = []
    
  
    @State private var showDateTypeLeave: Bool = false
    @State private var showLeaveTypeLeave: Bool = false
    
    
    @Binding var showAddLeaves: Bool
    @Binding var showCalender: Bool
    
    //Warning Popup
    @Binding var showWarningPopup: Bool
    
    var body: some View {
        ZStack {
            VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
                ZStack {
                    Text("Add New Leave")
                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.semibold))
                        .frame(maxWidth: .infinity)

                    HStack {
                        Spacer()

                        Button {
                            withAnimation {
                                startDate = nil
                                endDate = nil
                                showAddLeaves.toggle()
                            }
                        } label: {
                            Image(systemName: "xmark")
                                .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                        }
                        .buttonStyle(.plain)
                        .accessibilityLabel("Close add leave")
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

                PopupTextField(text: $empName, placeholder: "Write Name")  // Editing is disabled

                //MARK: Leave type
                HStack(alignment: .top, spacing: AppSpacing.stackSpacingDefault) {
                    VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
                        HStack(spacing: 0) {
                            Text("Day Type")
                            Text("*")
                                .foregroundStyle(Color.red)
                        }
                        PopupDropDownMenu(selection: $dateTypeSelection, showTypeLeave: $showDateTypeLeave, showLeaveTypeLeave: $showLeaveTypeLeave, options: ["First Half", "Second Half", "Full Day"], selectionTitle: "First Half")
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

                LargeTextEditorView(descriptionText: $createLeaveViewModel.reason)
                    .toolbarDoneButton()

                //MARK: Apply Button
                PrimaryThinButton(text: "Apply") {
                    Task {
                        if let dayType = dateTypeSelection {
                            createLeaveViewModel.dayType = dayType
                        }
                        if let startDate = startDate {
                            createLeaveViewModel.startDate = startDate
                        }
                        if let endDate = endDate {
                            createLeaveViewModel.endDate = endDate
                        }
                        if let leaveType = leaveTypeSelection {
                            createLeaveViewModel.leaveType = leaveType.id
                        }
                        try await createLeaveViewModel.createLeave()

                        if NetworkManager.shared.statusCode == 400 {
                            showWarningPopup.toggle()
                        }else {
                            await leavesViewModel.getLeaves() // to referesh the leaves screen

                            withAnimation {
                                startDate = nil
                                endDate = nil
                                showAddLeaves.toggle()
                            }
                        }
                    }

                }
                .padding(.top, AppSpacing.xs)
                .disableWithOpacity(empName.isEmpty || dateTypeSelection == nil || leaveTypeSelection == nil || startDate == nil || endDate == nil || createLeaveViewModel.reason.isEmpty)

            }
            .frame(maxWidth: 360)
            .padding(.horizontal, AppSpacing.lg)
            .padding(.vertical, AppSpacing.md)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.large))
            
            //MARK: Warning
            if showWarningPopup {
                ZStack {
                    WarningPopupView(titleText: NetworkManager.shared.responseMessage, description: createLeaveViewModel.createLeaveData?.body.error?.message ?? "", showWarningPopup: $showWarningPopup)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.black.opacity(0.5))
                
            }

        }
        .padding()
//        .border(Color.black)
        .onAppear {
            Task {
                dateTypeSelection = "First Half" // providing the default value
                
               try await leaveTypeViewModel.fetchLeaveType()
                
                if NetworkManager.shared.statusCode == 200 {
                    options = leaveTypeViewModel.leaveTypeData
                    leaveTypeSelection = leaveTypeViewModel.leaveTypeData.first // providing the default value
                }
            }
        }
        
    }
}

#Preview {
    AddNewLeavesView(leavesViewModel: LeavesViewModel(), empName: .constant(""), startDate: .constant(""), endDate: .constant(""), setStartDate: .constant(false), setEndDate: .constant(false), dateTypeSelection: .constant("") , showAddLeaves: .constant(false), showCalender: .constant(false), showWarningPopup: .constant(false))
}
