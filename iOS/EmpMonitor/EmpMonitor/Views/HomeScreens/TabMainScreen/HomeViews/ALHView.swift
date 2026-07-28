//
//  AttendanceHistoryView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 25/07/24.
//

import SwiftUI

struct ALHView: View {

    @EnvironmentObject var calendarViewModel: CalendarViewModel

    @Environment(\.dismiss) var dismiss


    @StateObject private var leavesViewModel = LeavesViewModel()
    @StateObject private var leaveTypeViewModel = LeaveTypeViewModel()
    @StateObject private var updateLeaveViewModel = UpdateLeaveViewModel()
    @StateObject private var dateViewModel = DateViewModel()
    @StateObject private var editAttendanceViewModel = EditAttendanceViewModel()
    @StateObject private var attendanceViewModel = AttendanceViewModel()

    //to add leaves
    @Binding  var empName: String  // BInding this name with the user name
    @State private var startDate: String?
    @State private var endDate: String?
    @State private var holidayDate: String?
    @State private var setStartDate: Bool? = false
    @State private var setEndDate: Bool? = false
    @State private var reason: String = ""
    @State private var dateTypeSelection: String?
    @State private var leaveTypeSelection: LeavesTypeResponseDetail?
    @State private var leaveTypeOptions: [LeavesTypeResponseDetail] = []

    @Binding var selection: String?

    @State private var showEditAttendance: Bool = false
    @State private var showAddLeaves: Bool = false
    @State private var showCalender: Bool = false
    @State private var showEditLeaves: Bool = false
//    @State private var selectedDate:

    //for edit attendance
    @State private var checkINTime: String = ""
    @State private var checkOUTTime: String = ""
    @State private var setCheckINTime: Bool = false
    @State private var setCheckOUTTime: Bool = false

    //Warning Popup
    @State private var showWarningPopup: Bool = false

    private var supportsCalendar: Bool {
        selection == "Attendance History" || selection == "Leaves" || selection == "Holidays"
    }

    var body: some View {
        ZStack {
            LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                .ignoresSafeArea()

            RoundedRectangle(cornerRadius: 20)
                .fill(Color.rectangleBG)
                .padding(.top)
                .ignoresSafeArea(edges: .bottom)
                .shadow(color: .black.opacity(0.25), radius: 12)
                .overlay(alignment: .top) {
                    ZStack(alignment: .top) {
                        VStack(alignment: .leading) {

                            //MARK: Calender
                            if supportsCalendar {
                                VStack {
                                    Image(.calenderIcon)
                                        .padding(10)
                                        .background(
                                            LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                                        )
                                        .clipShape(RoundedRectangle(cornerRadius: 6))
                                        .padding(.top, 30)
                                        .padding(.leading,30)
                                        .onTapGesture {
                                            withAnimation {
                                                setStartDate = true
                                                setEndDate = false
                                                showCalender.toggle()
                                            }
                                        }

                                }
                            }



                            ZStack {
                                if selection == "Attendance History" {
                                    AttendanceHistoryView(attendanceViewModel: attendanceViewModel, editAttendanceViewModel: editAttendanceViewModel, showEditAttendance: $showEditAttendance)
                                }
                                else if selection == "Leaves" {
                                    LeavesView(leaveTypeViewModel: leaveTypeViewModel, leavesViewModel: leavesViewModel, updateLeaveViewModel: updateLeaveViewModel, showAddLeaves: $showAddLeaves, showEditLeaves: $showEditLeaves, empName: $empName, startDate: $startDate, endDate: $endDate, setStartDate: $setStartDate, setEndDate: $setEndDate, reason: $reason, dateTypeSelection: $dateTypeSelection, leaveTypeSelection: $leaveTypeSelection, leaveTypeOptions: $leaveTypeOptions)
                                }
                                else if selection == "Holidays" {
                                    HolidaysView(selectedDate: $holidayDate)
                                }

                            }

                        }
                        .frame(maxWidth: .infinity, alignment: .leading)

//                        //MARK: Top Filter
                        FilterDropDownView(selection: $selection)
                            .padding(.leading, 50)
                            .padding(.horizontal, 30)
                            .padding(.top, 30)

                    }
                }

            //MARK: Edit Attendance Popup
            if showEditAttendance {
                ModalOverlayView(backgroundOpacity: 0.7) {
                    EditAttendancePopupView(editAttendanceViewModel: editAttendanceViewModel, dateViewModel: dateViewModel, showEditAttendance: $showEditAttendance, checkINTime: $checkINTime, checkOUTTime: $checkOUTTime)
                }
            }

            //MARK: TimePicker
            if dateViewModel.showPicker{
                ModalOverlayView(backgroundOpacity: 0.8, dismissOnBackgroundTap: {
                    dateViewModel.showPicker.toggle()
                }) {
                    TimePickerView(dateViewModel: dateViewModel, startTime: $checkINTime, stopTime: $checkOUTTime)
                }
            }

            //MARK: Add leaves Popup
            if showAddLeaves {
                ModalOverlayView(backgroundOpacity: 0.8) {
                    AddNewLeavesView(leavesViewModel: leavesViewModel, empName: $empName, startDate: $startDate, endDate: $endDate, setStartDate: $setStartDate, setEndDate: $setEndDate, dateTypeSelection: $dateTypeSelection, showAddLeaves: $showAddLeaves, showCalender: $showCalender, showWarningPopup: $showWarningPopup)
//                        .background(Color.white)
//                        .clipShape(RoundedRectangle(cornerRadius: 20))
//                        .padding()
                }
            }

            //MARK: Edit leaves Popup
            if showEditLeaves {
                ModalOverlayView(backgroundOpacity: 0.8) {
                    EditLeavesView(updateLeaveViewModel: updateLeaveViewModel, leavesViewModel: leavesViewModel, empName: $empName, startDate: $startDate, endDate: $endDate, setStartDate: $setStartDate, setEndDate: $setEndDate, reason: $reason, dateTypeSelection: $dateTypeSelection, leaveTypeSelection: $leaveTypeSelection, options: $leaveTypeOptions, showEditLeaves: $showEditLeaves, showCalender: $showCalender)
//                        .background(Color.white)
//                        .clipShape(RoundedRectangle(cornerRadius: 20))
//                        .padding()
                }
                .onDisappear {
                    Task {
                        leavesViewModel.startDate = HelperFunction.shared.getFirstDateOfCurrentMonth()
                        leavesViewModel.endDate = HelperFunction.shared.dateAfter30Days(from: leavesViewModel.startDate) ?? leavesViewModel.startDate

                        await leavesViewModel.getLeaves()
                    }

                    Task {
        //                dateTypeSelection = "First Half" // providing the default value

                       try await leaveTypeViewModel.fetchLeaveType()

                        if NetworkManager.shared.statusCode == 200 {
                            leaveTypeOptions = leaveTypeViewModel.leaveTypeData
        //                    leaveTypeSelection = leaveTypeViewModel.leaveTypeData.first // providing the default value
                        }
                    }
                }
            }

            //MARK: Calender
            if showCalender {
                ModalOverlayView(backgroundOpacity: 0.8) {
                    CalenderView(
                        attendanceViewModel: attendanceViewModel,
                        leavesViewModel: leavesViewModel,
                        viewSelection: $selection,
                        startDate: selection == "Holidays" ? $holidayDate : $startDate,
                        endDate: $endDate,
                        setStartDate: $setStartDate,
                        setEndDate: $setEndDate,
                        showCalendar: $showCalender
                    )
                }
            }

        }
        .toolbar {
            ToolbarItem(placement: .topBarLeading) {
//                HStack {
                    BackButtonView()
                        .onTapGesture {
                            dismiss()
//                        }

                }
            }
            ToolbarItem(placement: .principal) {
                Text(selection ?? "Attendance")
                    .font(AppFont.primary(size: AppFont.Size.navigationTitle))
                    .fontWeight(AppFont.Weight.semibold)
                    .foregroundStyle(Color.white)
//                        .padding(.horizontal, 70)
            }
            ToolbarItem(placement: .topBarTrailing) {
                if selection == "Leaves" {
                    Button {
                        showAddLeaves.toggle()
                    } label: {
                        Image(systemName: "plus")
                            .font(AppFont.primary(size: AppFont.Size.headlineLarge, weight: AppFont.Weight.semibold))
                            .foregroundStyle(Color.white)
                            .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                    }
                    .buttonStyle(.plain)
                    .accessibilityLabel("Add leave")
                }
            }
        }
        .onChange(of: selection) { _, _ in
            if !supportsCalendar {
                showCalender = false
            }
        }
    }
}

#Preview {
    ALHView(empName: .constant(""), selection: .constant("Attendance History"))
        .environmentObject(CalendarViewModel())
}
