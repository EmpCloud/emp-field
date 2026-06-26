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
            VStack(alignment: .leading, spacing: 10) {
                HStack {
                    HStack {
                        Text("Add New Leave")
                            .font(.system(size: 12, weight: .regular))
                            .fontWeight(.medium)
                    }
                    .frame(maxWidth: .infinity)
                    Image(systemName: "xmark")
                        .padding(10)
                        .onTapGesture {
                            withAnimation {
                                startDate = nil
                                endDate = nil
                                showAddLeaves.toggle()
                            }
                        }
                }
                .foregroundStyle(Color.attendanceTitleText)
                .frame(maxWidth: .infinity , alignment: .trailing)
                .padding(.bottom)
                
                HStack(spacing: 0) {
                    Text("Employee Name")
                        .foregroundStyle(Color.text1)
                    Text("*")
                        .foregroundStyle(Color.red)
                }
                .font(.system(size: 12, weight: .regular))
                .fontWeight(.medium)
                
                PopupTextField(text: $empName, placeholder: "Write Name")  // Editing is disabled
                
                //MARK: Leave type
                HStack {
                    VStack(alignment: .leading, spacing: 10) {
                        HStack(spacing: 0) {
                            Text("Day Type")
                            Text("*")
                                .foregroundStyle(Color.red)
                        }
                        PopupDropDownMenu(selection: $dateTypeSelection, showTypeLeave: $showDateTypeLeave, showLeaveTypeLeave: $showLeaveTypeLeave, options: ["First Half", "Second Half", "Full Day"], selectionTitle: "First Half")
                    }
                    
                    Spacer()
                    
                    VStack(alignment: .leading, spacing: 10) {
                        HStack(spacing: 0) {
                            Text("Leave Type")
                            Text("*")
                                .foregroundStyle(Color.red)
                        }
                        DynamicPopupDropdownMenu(selection: $leaveTypeSelection, showTypeLeave: $showLeaveTypeLeave, showDayTypeLeave: $showDateTypeLeave, options: options, selectionTitle: leaveTypeSelection?.name ?? "Leave Type")
                    }
                }
                .font(.system(size: 12, weight: .regular))
                .fontWeight(.medium)
                .foregroundStyle(Color.text1)
                
                //MARK: Leave Date(start/end)
                HStack {
                    VStack(alignment: .leading, spacing: 10) {
                        HStack(spacing: 0) {
                            Text("Start Date")
                            Text("*")
                                .foregroundStyle(Color.red)
                        }
                        
                        RoundedRectangle(cornerRadius: 6)
                            .fill(Color.rectangleBG)
                            .frame(width: 144, height: 34)
                            .overlay(alignment: .leading) {
                                Text(startDate ?? "dd-mm-yyyy")
                                    .font(.system(size: 12, weight: .regular))
                                    .padding(.leading)
                            }
                            .onTapGesture {
                                withAnimation {
                                    setStartDate = true
                                    setEndDate = false
                                    showCalender.toggle()
                                }
                            }
//                        PopupTextField(text: $startDate, placeholder: "dd-mm-yyyy")
                            
                    }
                    
                    Spacer()
                    
                    VStack(alignment: .leading, spacing: 10) {
                        HStack(spacing: 0) {
                            Text("End Date")
                            Text("*")
                                .foregroundStyle(Color.red)
                        }
                        
                        RoundedRectangle(cornerRadius: 6)
                            .fill(Color.rectangleBG)
                            .frame(width: 144, height: 34)
                            .overlay(alignment: .leading) {
                                Text(endDate ?? "dd-mm-yyyy")
                                    .font(.system(size: 12, weight: .regular))
                                    .padding(.leading)
                            }
                            .onTapGesture {
                                withAnimation {
                                    setStartDate = false
                                    setEndDate = true
                                    showCalender.toggle()
                                }
                            }
//                        PopupTextField(text: $endDate, placeholder: "dd-mm-yyyy")
                            
                        
                    }
                }
                .font(.system(size: 12, weight: .regular))
                .fontWeight(.medium)
                .foregroundStyle(Color.text1)
                
                
                //MARK: Reason
                
                HStack(spacing: 0) {
                    Text("Reason")
                    Text("*")
                        .foregroundStyle(Color.red)
                }
                .foregroundStyle(Color.text1)
                .font(.system(size: 12, weight: .regular))
                .fontWeight(.medium)
                
//                RoundedRectangle(cornerRadius: 10)
//                    .fill(Color.rectangleBG)
//                    .frame(height: 68)
                
                LargeTextEditorView(descriptionText: $createLeaveViewModel.reason)
                    .toolbarDoneButton()
                
                
                //MARK: Apple Button
                PrimaryThinButton(text: "Apply") {
                    //TODO: apply for leave
                    
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
                .padding(.top)
                .disableWithOpacity(empName.isEmpty || dateTypeSelection == nil || leaveTypeSelection == nil || startDate == nil || endDate == nil || createLeaveViewModel.reason.isEmpty)

            }
            .frame(height: 454)
            .padding()
            .padding(.horizontal)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: 20))
            
            //MARK: Warning
            if showWarningPopup {
                ZStack {
                    WarningPopupView(titleText: NetworkManager.shared.responseMessage, description: createLeaveViewModel.createLeaveData?.body.error?.message ?? "", showWarningPopup: $showWarningPopup)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.black.opacity(0.5))
                .onTapGesture {
                    withAnimation {
                        showWarningPopup.toggle()
                    }
                }
                
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
