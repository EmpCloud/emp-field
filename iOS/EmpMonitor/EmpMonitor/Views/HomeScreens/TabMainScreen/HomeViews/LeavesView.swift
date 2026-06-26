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
                
                if leavesViewModel.fetchStatusCode == 200 {
                    
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
                                                //TODO: things to change here since now we are using same object
    //                                            updateLeaveViewModel.leaveID = leave.id
    //                                            updateLeaveViewModel.employeeName = leave.employeeName
    //                                            updateLeaveViewModel.dayType = leave.dayType
    //                                            updateLeaveViewModel.leaveType = leave.leaveType
    //                                            updateLeaveViewModel.startDate = FormatterHelper.shared.formattedDateReverse(from: leave.startDate)
    //                                            updateLeaveViewModel.endDate = FormatterHelper.shared.formattedDateReverse(from: leave.endDate)
    //                                            updateLeaveViewModel.reason = leave.reason
                                                updateLeaveViewModel.leaveID = leave.id
                                                empName = leave.employeeName ?? ""
                                                dateTypeSelection = LeaveHelper.shared.getLeaveDayType(status: leave.dayType)
                                                let leaveTypeID = leave.leaveType
                                                for leaveType in leaveTypeOptions {
                                                    if leaveTypeID == leaveType.id {
                                                        leaveTypeSelection = leaveType
                                                        print("Typess: \(leaveTypeSelection?.name)")
                                                    }
                                                }
    //                                            updateLeaveViewModel.leaveType = leave.leaveType
                                                startDate = FormatterHelper.shared.formattedDateReverse(from: leave.startDate)
                                                endDate = FormatterHelper.shared.formattedDateReverse(from: leave.endDate)
                                                reason = leave.reason ?? ""
                                                print("Leave Data")
                                                print("LeaveID: \(leave.id)")
    //                                            print(updateLeaveViewModel.leaveID)
    //                                            print(updateLeaveViewModel.employeeName)
    //                                            print(updateLeaveViewModel.dayType)
    //                                            print(updateLeaveViewModel.leaveType)
    //                                            print(FormatterHelper.shared.formattedDateReverse(from: updateLeaveViewModel.startDate))
    //                                            print(updateLeaveViewModel.endDate)
    //                                            print(updateLeaveViewModel.reason)
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
                    
                    PrimaryBorderButton(text: "Add leaves") {
                        //TODO: to show add leaves popup
                        showAddLeaves.toggle()
                    }
                    
                }else{
                    VStack {
                        Image(.noLeaves)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 114, height: 114)
                        Text("No Leaves Found")
                            .font(.custom("Montserrat", size: 14))
                    }
                    .frame(height: 300)
                    .frame(maxWidth: .infinity)
                    .background(Color.white)
                    .clipShape(RoundedRectangle(cornerRadius: 20))
                    
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
