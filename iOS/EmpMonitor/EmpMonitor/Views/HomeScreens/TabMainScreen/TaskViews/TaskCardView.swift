//
//  TaskCardView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 21/08/24.
//

import SwiftUI

struct TaskCardView: View {
    
    @EnvironmentObject var permissionManager: PermissionManager
    
    @ObservedObject var updateTaskViewModel: UpdateTaskViewModel
    
    var filterTaskDetail: FilterTaskListResponseData
    @Binding var selectedTask: FilterTaskListResponseData?
    
    @State private var showPauseButton: Bool = false
    
    @Binding var taskStatus: [String : Int]  // for task status "start", "pause", "resume"
    
    //Warning
    @Binding var showWarningPopup: Bool
    
    //Refresh Screen after updating the status
    @Binding var refreshScreen: Bool
    
    
    //Task Status used to manage the checkOUT
    @Binding var isTaskRunning: Bool
        
    var body: some View {
        //MARK: Task
        HStack(spacing: 0) {
            Rectangle()
                .fill(Color.blue.opacity(0.3))
                .frame(width: 14, height: 95)
                .padding(.trailing)
                .clipShape(RoundedRectangle(cornerRadius: 15))
                .padding(.leading, 25)
                .padding(.top, 5)
                .padding(.bottom, 5)
            
            
            VStack(alignment: .leading, spacing: 10){
                HStack{
                    Text(filterTaskDetail.taskName)
                        .font(.system(size: 15, weight: .semibold))
                        .foregroundStyle(Color.taskClientName)
                    Spacer()
                    showButton(taskStatus[filterTaskDetail.id] ?? 0)
                }
                .padding(.horizontal)
                
                Text(filterTaskDetail.taskDescription)
                    .font(.system(size: 12, weight: .medium))
                    .foregroundStyle(Color.subText)
                    .padding(.leading)
                
                HStack{
                    if let startTime = FormatterHelper.shared.checkTimeFormatter(from: filterTaskDetail.startTime), let endTime = FormatterHelper.shared.checkTimeFormatter(from: filterTaskDetail.endTime) {
                        Text("\(startTime) - \(endTime)")
                            .font(.system(size: 12, weight: .semibold))
                            .foregroundStyle(Color.attendanceTitleText)
                        Spacer()
                    }
                    
                }
                .padding(.leading)
            }
//            .padding(14)
            .frame(height: 98)
            .background(selectedTask?.id == filterTaskDetail.id ? Color.selectClientBG : Color.white)
            .padding(.leading)
            .clipShape(RoundedRectangle(cornerRadius: 15))
            //                        .padding(.trailing)
            .offset(x: -32)
        }
        .onAppear {
//            taskStatus.removeValue(forKey: filterTaskDetail.id)
            taskStatus[filterTaskDetail.id] = filterTaskDetail.taskApproveStatus
            
            print("Filter task each logs")
            print(filterTaskDetail.tagLogs)
//            print(filterTaskDetail)
        }
        
    }
    
    //MARK: Show Button Based on condition
    private func showButton(_ status: Int) -> some View {
        switch status {
        case 0:
            return AnyView(StartButton {
                //TODO: To start the task
                Task {
                    updateTaskViewModel.taskID = filterTaskDetail.id
                    updateTaskViewModel.status = 1
                    updateTaskViewModel.currentDateTime = FormatterHelper.shared.getCurrentDateTimeFormatted()
                    if let lat = permissionManager.userLocation?.coordinate.latitude{
                        updateTaskViewModel.latitude = String(lat)
                    }
//                    updateTaskViewModel.latitude = filterTaskDetail.latitude ?? ""
//                    updateTaskViewModel.longitude = filterTaskDetail.longitude ?? ""
                    if let long = permissionManager.userLocation?.coordinate.longitude{
                        updateTaskViewModel.longitude = String(long)
                    }
                    updateTaskViewModel.taskValue = TaskValue(amount: filterTaskDetail.value?.amount, currency: filterTaskDetail.value?.currency)
                    updateTaskViewModel.taskVolume = filterTaskDetail.taskVolume
                    updateTaskViewModel.tagLogs = filterTaskDetail.tagLogs
                    
//                    print("Taglogs:  \(filterTaskDetail.tagLogs.first)")
                    
                    // adding a validation for the lat, long confirmation not to be empty
                    if !updateTaskViewModel.longitude.isEmpty  && !updateTaskViewModel.latitude.isEmpty {
                        try await updateTaskViewModel.updateTaskStatus()
                    }else {
                        showWarningPopup = true
                        NetworkManager.shared.responseMessage = "Coordinate Error"
                    }
                    
                    
                    if NetworkManager.shared.statusCode == 400 {
                        showWarningPopup = true
                    }
                    else{
                        taskStatus[filterTaskDetail.id] = 1
                        refreshScreen.toggle()
                        isTaskRunning = true
                    }
                    
                }
            }
            .onAppear {
                isTaskRunning = false
            }
        )
        case 1, 3:
            return AnyView(PauseButton {
                //TODO: To start the task
                Task {
                    updateTaskViewModel.taskID = filterTaskDetail.id
                    updateTaskViewModel.status = 2
                    updateTaskViewModel.currentDateTime = FormatterHelper.shared.getCurrentDateTimeFormatted()
//                    updateTaskViewModel.latitude = filterTaskDetail.latitude ?? ""
//                    updateTaskViewModel.longitude = filterTaskDetail.longitude ?? ""
                    if let lat = permissionManager.userLocation?.coordinate.latitude{
                        updateTaskViewModel.latitude = String(lat)
                    }
                    if let long = permissionManager.userLocation?.coordinate.longitude{
                        updateTaskViewModel.longitude = String(long)
                    }
//                    updateTaskViewModel.value = filterTaskDetail.value ?? FilterTaskValue(convertedAmountInUSD: nil, currency: "", amount: 0)
                    updateTaskViewModel.taskValue = TaskValue(amount: filterTaskDetail.value?.amount, currency: filterTaskDetail.value?.currency)
                    updateTaskViewModel.taskVolume = filterTaskDetail.taskVolume
                    updateTaskViewModel.tagLogs = filterTaskDetail.tagLogs
                    
//                    print("Taglogs:  \(filterTaskDetail.tagLogs.first)")
                    
                    // adding a validation for the lat, long confirmation not to be empty
                    if !updateTaskViewModel.longitude.isEmpty  && !updateTaskViewModel.latitude.isEmpty {
                        try await updateTaskViewModel.updateTaskStatus()
                    }else {
                        showWarningPopup = true
                        NetworkManager.shared.responseMessage = "Coordinate Error"
                    }
                    
                    if NetworkManager.shared.statusCode == 400 {
                        showWarningPopup = true
                    }else{
                        taskStatus[filterTaskDetail.id] = 2
                        refreshScreen.toggle()
                        isTaskRunning = false
                    }
                    
                }
            }
                .onAppear {
                isTaskRunning = true
                }
            )
        case 2:
            return AnyView(ResumeButton {
                //TODO: To pause the task
                Task {
                    updateTaskViewModel.taskID = filterTaskDetail.id
                    updateTaskViewModel.status = 3
                    updateTaskViewModel.currentDateTime = FormatterHelper.shared.getCurrentDateTimeFormatted()
//                    updateTaskViewModel.latitude = filterTaskDetail.latitude ?? ""
//                    updateTaskViewModel.longitude = filterTaskDetail.longitude ?? ""
                    if let lat = permissionManager.userLocation?.coordinate.latitude{
                        updateTaskViewModel.latitude = String(lat)
                    }
                    if let long = permissionManager.userLocation?.coordinate.longitude{
                        updateTaskViewModel.longitude = String(long)
                    }
//                    updateTaskViewModel.value = filterTaskDetail.value ?? FilterTaskValue(convertedAmountInUSD: nil, currency: "", amount: 0)
                    updateTaskViewModel.taskValue = TaskValue(amount: filterTaskDetail.value?.amount, currency: filterTaskDetail.value?.currency)
                    updateTaskViewModel.taskVolume = filterTaskDetail.taskVolume
                    updateTaskViewModel.tagLogs = filterTaskDetail.tagLogs
                    
//                    print("Taglogs:  \(filterTaskDetail.tagLogs.first)")
                    
                    // adding a validation for the lat, long confirmation not to be empty
                    if !updateTaskViewModel.longitude.isEmpty  && !updateTaskViewModel.latitude.isEmpty {
                        try await updateTaskViewModel.updateTaskStatus()
                    }else {
                        showWarningPopup = true
                        NetworkManager.shared.responseMessage = "Coordinate Error"
                    }
                    
                    if NetworkManager.shared.statusCode == 400 {
                        showWarningPopup = true
                    }
                    else{
                        taskStatus[filterTaskDetail.id] = 3
                        refreshScreen.toggle()
                        isTaskRunning = true
                    }
                }
            }
                .onAppear {
                    isTaskRunning = false
                }
            )
        case 4:
            return AnyView(FinishButton {
                //TODO: To finish the task
            })
        default:
            return AnyView(EmptyView())
        }
    }
    
}

//#Preview {
//    TaskCardView()
//}
