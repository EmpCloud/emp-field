//
//  LeavesView.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 29/07/24.
//

import SwiftUI

struct LeavesView: View {
    
    @ObservedObject var leaveTypeViewModel: LeaveTypeViewModel
    
    @ObservedObject var leavesViewModel: LeavesViewModel
    @ObservedObject var updateLeaveViewModel: UpdateLeaveViewModel
    
    @Binding var showAddLeaves: Bool
    @Binding var showEditLeaves: Bool
    
    @Binding var empName: String
    @Binding var startDate: String?
    @Binding var endDate: String?
    @Binding var setStartDate: Bool?
    @Binding var setEndDate: Bool?
    @Binding var reason: String 
    @Binding var dateTypeSelection: String?
    @Binding var leaveTypeSelection: LeavesTypeResponseDetail?
    @Binding var leaveTypeOptions: [LeavesTypeResponseDetail]
    
    //For leave Type
    @State private var options: [LeavesTypeResponseDetail] = []
    
    var body: some View {
        ScrollView {
            VStack {
                if leavesViewModel.isLoading {
                    loadingPlaceholder
                } else if leavesViewModel.leavesData.isEmpty {
                    emptyPlaceholder
                } else {
                    LazyVGrid(columns: [GridItem(.fixed(1))], alignment: .leading, spacing: nil) {
                        
                        //MARK: Titles
                        LazyHGrid(rows: [GridItem(.fixed(5))], alignment: .center, spacing: 15) {
                            Text("Date")
                                .frame(width: 50, alignment: .leading)
                            Text("Leave")
                                .frame(width: 35, alignment: .center)
                            Text("Day type")
                                .frame(width: 75, alignment: .leading)
                            Text("Days")
                                .frame(width: 40, alignment: .leading)
                            Text("Status")
                        }
                        .font(.custom("Montserrat", size: 12))
                        .foregroundStyle(Color.attendanceTitleText)
                        .padding(.leading, 12)
                        .padding(.vertical, 10)
                        .padding(.top, 10)
                        
                        //MARK: Data
                        ForEach(leavesViewModel.leavesData, id: \.id) { leave in
                            LazyHGrid(rows: [GridItem(.fixed(5))], alignment: .center, spacing: 15) {
                                VStack {
                                    Text("\(FormatterHelper.shared.formattedDate(from: leave.startDate))")
                                    Text("-")
                                    Text("\(FormatterHelper.shared.formattedDate(from: leave.endDate))")
                                }
                                
                                Text("\(leaveTypeInitials(leaveOptions: options, leaveTypeCode: leave.leaveType))")
                                    .frame(width: 35, alignment: .center)
                                    .fontWeight(.medium)
                                Text("\(LeaveHelper.shared.getLeaveDayType(status: leave.dayType))")
                                    .frame(width: 75, alignment: .leading)
                                Text(leave.numberOfDays.truncatingRemainder(dividingBy: 1) == 0 ? String(Int(leave.numberOfDays)) : String(leave.numberOfDays))
                                    .frame(width: 40, alignment: .center)
                                
                                HStack {
                                    
                                    Text("\(LeaveHelper.shared.getLeaveStatus(status: leave.status))")
                                        .foregroundStyle(LeaveHelper.shared.getLeaveStatusColor(status: leave.status))
                                        .frame(width: 65, alignment: .leading)
                                    
                                    Image(.editAttendanceIcon)
                                        .onTapGesture {
                                            
                                            Task {
                                                updateLeaveViewModel.leaveID = leave.id
                                                empName = leave.employeeName ?? ""
                                                dateTypeSelection = LeaveHelper.shared.getLeaveDayType(status: leave.dayType)
                                                let leaveTypeID = leave.leaveType
                                                for leaveType in leaveTypeOptions {
                                                    if leaveTypeID == leaveType.id {
                                                        leaveTypeSelection = leaveType
                                                        AppLog.debug("Typess: \(leaveTypeSelection?.name)")
                                                    }
                                                }
                                                startDate = FormatterHelper.shared.formattedDateReverse(from: leave.startDate)
                                                endDate = FormatterHelper.shared.formattedDateReverse(from: leave.endDate)
                                                reason = leave.reason ?? ""
                                                AppLog.debug("Leave Data")
                                                AppLog.debug("LeaveID: \(leave.id)")
                                                showEditLeaves.toggle()
                                            }
                                        }
                                }
                            }
                            .font(.custom("Montserrat", size: 12))
                            .foregroundStyle(Color.subText)
                            .padding(.leading, 12)
                            
                            LineView()
                                .padding(.vertical, 5)
                        }
                    }
                    .background(
                        Color.white
                    )
                    .clipShape(RoundedRectangle(cornerRadius: 20))
                }
                
                if !leavesViewModel.isLoading {
                    PrimaryBorderButton(text: "Add leaves") {
                        //TODO: to show add leaves popup
                        showAddLeaves.toggle()
                    }
                }
            }
            
        }
        .padding(.top, 10)
        .padding(.horizontal, 7)
        .onAppear {
            Task {
                leavesViewModel.startDate = HelperFunction.shared.getFirstDateOfCurrentMonth()
                leavesViewModel.endDate = HelperFunction.shared.dateAfter30Days(from: leavesViewModel.startDate) ?? leavesViewModel.startDate
                
                await leavesViewModel.getLeaves()
            }
            
            Task {
//                dateTypeSelection = "First Half" // providing the default value
                
               try await leaveTypeViewModel.fetchLeaveType()
                
                if NetworkManager.shared.statusCode == 200 {
                    options = leaveTypeViewModel.leaveTypeData
//                    leaveTypeSelection = leaveTypeViewModel.leaveTypeData.first // providing the default value
                }
            }
        }
        .refreshable {
            Task {
                leavesViewModel.startDate = HelperFunction.shared.getFirstDateOfCurrentMonth()
                leavesViewModel.endDate = HelperFunction.shared.dateAfter30Days(from: leavesViewModel.startDate) ?? leavesViewModel.startDate
                
                await leavesViewModel.getLeaves()
            }
            
            Task {
//                dateTypeSelection = "First Half" // providing the default value
                
               try await leaveTypeViewModel.fetchLeaveType()
                
                if NetworkManager.shared.statusCode == 200 {
                    options = leaveTypeViewModel.leaveTypeData
//                    leaveTypeSelection = leaveTypeViewModel.leaveTypeData.first // providing the default value
                }
            }
        }
    }
    
    private var loadingPlaceholder: some View {
        VStack(spacing: 12) {
            ProgressView()
                .scaleEffect(1.2)
            Text("Loading...")
                .font(.custom("Montserrat", size: 14))
                .foregroundStyle(Color.subText)
        }
        .frame(maxWidth: .infinity, minHeight: 400)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 20))
    }
    
    private var emptyPlaceholder: some View {
        VStack(spacing: 12) {
            Image(.noLeaves)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 114, height: 114)
            Text("No Leaves Found")
                .font(.custom("Montserrat", size: 14))
        }
        .frame(maxWidth: .infinity, minHeight: 300)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 20))
    }
    
    private func leaveTypeInitials(leaveOptions: [LeavesTypeResponseDetail], leaveTypeCode: Int) -> String {
        // find the leave type that matches the provided leaveType
        if let leave = leaveOptions.first(where: { $0.id == leaveTypeCode }) {
            //split the leave name into words and get the initials of each word
            let words = leave.name.split(separator: " ")
            let initials = words.compactMap { $0.first }.map { String($0) }.joined()
            return initials.uppercased()
        }
        
        //Return an empty string if no match is founc
        return "L"
    }
}

//#Preview {
//    LeavesView(leavesViewModel: LeavesViewModel(), updateLeaveViewModel: UpdateLeaveViewModel(), showAddLeaves: .constant(false), showEditLeaves: .constant(false), empName: .constant(""), startDate: .constant(""), endDate: .constant(""), setStartDate: .constant(false), setEndDate: .constant(false), reason: .constant(""), dateTypeSelection: .constant(""))
//}
