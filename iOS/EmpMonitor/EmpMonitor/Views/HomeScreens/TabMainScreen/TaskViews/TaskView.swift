//
//  TaskView.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 11/07/24.
//

import SwiftUI

struct TaskView: View {
    
    @EnvironmentObject var permissionManager: PermissionManager
    
    @StateObject private var taskListViewModel = TaskListViewModel()
    @StateObject private var deleteTaskViewModel = DeleteTaskViewModel()
    @StateObject private var updateTaskViewModel = UpdateTaskViewModel()
    @StateObject private var tagViewModel = TagViewModel()
    
    //Task data
    @State private var taskData: [TaskListResponseData] = []
    @State private var filterTaskData: [FilterTaskListResponseData]?
    @State private var taskStatus: [String : Int] = [:]  // to configure the type of button on a particular task
    @State private var selectedTask: FilterTaskListResponseData?
    @State private var showTaskReschedule: Bool = false
    
    @State private var searchText: String = ""
    
    //Filter
    @State private var showTaskFilter: Bool = false
    @State private var selectedFilter: String?
    @State private var selectedDate: String?
    @State private var todaysDate = FormatterHelper.shared.getTodaysDate()
    @State private var endDate: String?
    @State private var setSelectedDate: Bool?
    @State private var setEndDate: Bool?
    @State private var showCalender: Bool = false
//    @State private var startDate: String?
    
    //swipeGesture
    @State private var swipeAmount: [String : CGFloat] = [:]
    @State private var swipedTaskID: String?
    @State private var swipedTask: FilterTaskListResponseData?
    
    @State private var showAddTask: Bool = false
    
    @Binding var showSideMenu: Bool
    
    
    //ShowAlert
    @State private var showAlert: Bool = false
    @State private var showDeleteAlert: Bool = false
    @State private var showCompleteAlert: Bool = false
    
    
    //Warning Popup
    @State private var showWarningPopup: Bool = false
    
 
    //Refresh Screen after updating the status
    @State private var refreshScreen: Bool = false
    
    //Task Status used to manage the checkOUT
    @Binding var isTaskRunning: Bool
    
    
    
    var body: some View {
        
        NavigationStack{
            
            ZStack{
                
                //MARK: BG Gradient
                LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                    .ignoresSafeArea()
                
                //TODO: To create the content of the View
                
                RoundedRectangle(cornerRadius: 25.0)
                    .fill(Color.rectangleBG)
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .padding(.top)
                    .ignoresSafeArea(edges: .bottom)
                    .overlay(alignment: .top) {
                        ZStack(alignment: .top) {
                            VStack(alignment: .leading) {
                                VStack{
                                    RoundedRectangle(cornerRadius: 6)
                                        .fill(Color.taskSearchBar)
                                        .frame(width: 48, height: 45)
                                        .overlay {
                                            VStack(spacing: 0) {
                                                if let filterDate = FormatterHelper.shared.getTodaysMonthDay(from: selectedDate ?? todaysDate) {
                                                    Text(filterDate)
                                                }
                                            }
                                            .font(.system(size: 12, weight: .medium))
                                            .foregroundStyle(Color.white)
                                        }
                                        .onTapGesture {
                                            withAnimation {
                                                setSelectedDate = true
                                                showCalender.toggle()
                                            }
                                        }
                                }
                                .padding(.top, 30)
                                .padding(.horizontal)
                                
                                VStack{
                                    if taskListViewModel.fetchStatus == 200 {
                                        TaskListView(taskListViewModel: taskListViewModel, deleteTaskViewModel: deleteTaskViewModel, updateTaskViewModel: updateTaskViewModel, filterTaskData: $filterTaskData, taskStatus: $taskStatus, selectedTask: $selectedTask, showTaskReschedule: $showTaskReschedule, searchText: $searchText, swipeAmount: $swipeAmount, swipedTaskID: $swipedTaskID, swipedTask: $swipedTask, showDeleteAlert: $showDeleteAlert, showAlert: $showAlert, showCompleteAlert: $showCompleteAlert, showWarningPopup: $showWarningPopup, refreshScreen: $refreshScreen, isTaskRunning: $isTaskRunning)
                                    }else{
                                        ScrollView {
                                            VStack{
                                                Image(.noDataFound)
                                                    .resizable()
                                                    .aspectRatio(contentMode: .fit)
                                                    .frame(width: 114, height: 114)
                                                Text("No Data Found")
                                                    .font(.system(size: 14, weight: .medium))
                                                    
                                            }
                                            .padding(.top, 200)
                                        }
                                        // User Can refresh the task list if the task list has no data
                                        .refreshable {
                                            Task {
                                                try await taskListViewModel.getFilterTaskList()
                                                
                                                if NetworkManager.shared.statusCode == 200 {
                                                    filterTaskData = taskListViewModel.filterTaskData
                                                }
                                            }
                                        }
                                    }
                                    
                                }
                                .frame(maxWidth: .infinity, alignment: .center)
                                .refreshable {
                                    Task {
                                        try await taskListViewModel.getFilterTaskList()
                                        
                                        if NetworkManager.shared.statusCode == 200 {
                                            filterTaskData = taskListViewModel.filterTaskData
                                        }
                                    }
                                }
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                            
                            
                            //MARK: FilterMenu
                            if showTaskFilter {
                                RoundedRectangle(cornerRadius: 6)
                                    .fill(
                                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                                    )
                                    .frame(height: 92)
                                    .padding(.top, 30)
                                    .padding(.horizontal, 30)
                                    .padding(.leading, 50)
                                    .overlay(alignment: .bottom) {
                                        HStack(spacing: 30){
                                            HStack{
                                                Image(systemName: selectedFilter == "All" ? "checkmark.square" :"square")
                                                Text("All")
                                            }
                                            .onTapGesture {
                                                Task {
                                                    selectedFilter = "All"  // setting the selected UI
                                                    
                                                    
//                                                    taskListViewModel.date = FormatterHelper.shared.getTodaysDate()
                                                    taskListViewModel.status = 0 // passing the value for "All"
                                                    
                                                   try await taskListViewModel.getFilterTaskList()
                                                    
                                                    print(NetworkManager.shared.statusCode)
                                                    print(NetworkManager.shared.responseMessage)
                                                    
                                                    if NetworkManager.shared.statusCode == 200 {
                                                        filterTaskData = taskListViewModel.filterTaskData
                                                    }
                                                    
                                                    
                                                }
                                                withAnimation(.smooth) {
                                                    showTaskFilter.toggle()
                                                }
                                            }
                                            
                                            HStack{
                                                Image(systemName: selectedFilter == "Current" ? "checkmark.square" :"square")
                                                Text("Current")
                                            }
                                            .onTapGesture {
                                                Task {
                                                    selectedFilter = "Current"  // setting the selected UI
                                                    
                                                    
//                                                    taskListViewModel.date = FormatterHelper.shared.getTodaysDate()
                                                    taskListViewModel.status = 1 // passing the value for "All"
                                                    
                                                   try await taskListViewModel.getFilterTaskList()
                                                    
                                                    if NetworkManager.shared.statusCode == 200 {
                                                        filterTaskData = taskListViewModel.filterTaskData
                                                    }
                                                    
                                                    
                                                }
                                                withAnimation(.smooth) {
                                                    showTaskFilter.toggle()
                                                }
                                            }
                                            
                                            HStack{
                                                Image(systemName: selectedFilter == "Finished" ? "checkmark.square" :"square")
                                                Text("Finished")
                                            }
                                            .onTapGesture {
                                                Task {
                                                    selectedFilter = "Finished"  // setting the selected UI
                                                    
                                                    
//                                                    taskListViewModel.date = FormatterHelper.shared.getTodaysDate()
                                                    taskListViewModel.status = 4 // passing the value for "All"
                                                    
                                                   try await taskListViewModel.getFilterTaskList()
                                                    
                                                    if NetworkManager.shared.statusCode == 200 {
                                                        filterTaskData = taskListViewModel.filterTaskData
                                                    }
                                                    
                                                    
                                                }
                                                withAnimation(.smooth) {
                                                    showTaskFilter.toggle()
                                                }
                                            }
                                            
                                        }
                                        .font(.custom("Montserrat", size: 12))
                                        .fontWeight(.medium)
                                        .foregroundStyle(Color.white)
                                        .padding()
                                        .padding(.leading, 40)
                                    }
                            }
                            
                            //MARK: Search bar with filter
                            TaskSearchBarView(searchText: $searchText, selectedFilter: $selectedFilter, showTaskFilter: $showTaskFilter)
                                .padding(.leading, 50)
                                .padding(.horizontal, 30)
                                .padding(.top, 30)
                            
                            //MARK: Warning Popup
                            if showWarningPopup {
                                ZStack {
                                    WarningPopupView(titleText: "\(NetworkManager.shared.statusCode)", description: NetworkManager.shared.responseMessage, showWarningPopup: $showWarningPopup)
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
                    }
                
                //MARK: Add task button
                ZStack{
                    
                    CreateTaskButton(text: ""){
                        //TODO: Create the task
                        showAddTask.toggle()
                    }
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottom)
                
                if showCalender {
                    ZStack {
                        TaskCalendarView(startDate: $selectedDate, endDate: $endDate, setStartDate: $setSelectedDate, setEndDate: $setEndDate, showCalendar: $showCalender)
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .background(Color.black.opacity(0.5))
                    .onTapGesture {
                        showCalender.toggle()
                    }
                }
            }
//            .refreshable {
//                Task {
//                    try await taskListViewModel.getFilterTaskList()
//                    
//                    if NetworkManager.shared.statusCode == 200 {
//                        filterTaskData = taskListViewModel.filterTaskData
//                    }
//                }
//            }
            .onChange(of: selectedDate) { _, _ in
                Task {
                    taskListViewModel.date = selectedDate ?? FormatterHelper.shared.getTodaysDate()
                    taskListViewModel.status = 0
                   try await taskListViewModel.getFilterTaskList()
                    
                    if NetworkManager.shared.statusCode == 200 {
                        filterTaskData = taskListViewModel.filterTaskData
                    }
                    
                    print(NetworkManager.shared.statusCode)
                    print(NetworkManager.shared.responseMessage)
                }
            }
            .onChange(of: refreshScreen) { _, _ in
                Task {
                    taskListViewModel.date = FormatterHelper.shared.getTodaysDate()
                    taskListViewModel.status = 0
                    taskListViewModel.filterTaskData = []
                    print("-----filter task Data")
                    print(taskListViewModel.filterTaskData)
                    
                    try await taskListViewModel.getFilterTaskList()
                    
                    if NetworkManager.shared.statusCode == 200 {
                        filterTaskData = taskListViewModel.filterTaskData
                    }
                }
                
            }
            .navigationDestination(isPresented: $showAddTask) {
                AddTaskView()
                    .navigationBarBackButtonHidden()
            }
            .navigationDestination(isPresented: $showTaskReschedule) {
                TaskRescheduleView(tagViewModel: tagViewModel, selectedTask: $selectedTask, taskStatus: $taskStatus, refreshScreen: $refreshScreen
                )
                    .navigationBarBackButtonHidden()
            }
            .onAppear{
                selectedDate = nil // to reset the date
                Task {
                    taskListViewModel.date = FormatterHelper.shared.getTodaysDate()
                    taskListViewModel.status = 0
                    taskListViewModel.filterTaskData = []
                    print("-----filter task Data")
                    print(taskListViewModel.filterTaskData)
                   try await taskListViewModel.getFilterTaskList()
                    
                    if NetworkManager.shared.statusCode == 200 {
                        filterTaskData = taskListViewModel.filterTaskData
                    }
                    
                }
                // just to refresh
//                DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
//                    Task {
//                        try await taskListViewModel.getFilterTaskList()
//                        
//                        if NetworkManager.shared.statusCode == 200 {
//                            filterTaskData = taskListViewModel.filterTaskData
//                        }
//                    }
//                }
            }
            .alert(isPresented: $showAlert) {
                if showDeleteAlert {
                    Alert(title: Text("Do you really want to delete the task ?"),
                          primaryButton: .destructive(Text("Delete"), action: {
                        
                        withAnimation {
//                            //TODO: to remove with animation
                            taskListViewModel.removeTask(taskID: swipedTaskID ?? "")
                        }
                        
                        deleteTaskViewModel.taskID = swipedTaskID ?? ""
                        deleteTaskViewModel.currentDateTime = FormatterHelper.shared.getCurrentDateTimeFormatted()
//                        deleteTaskViewModel.latitude = swipedTask?.latitude ?? ""
//                        deleteTaskViewModel.longitude = swipedTask?.longitude ?? ""
                        if let lat = permissionManager.userLocation?.coordinate.latitude{
                            updateTaskViewModel.latitude = String(lat)
                        }
                        if let long = permissionManager.userLocation?.coordinate.longitude{
                            updateTaskViewModel.longitude = String(long)
                        }
                        deleteTaskViewModel.value = swipedTask?.value ?? FilterTaskValue(convertedAmountInUSD: nil, currency: "INR", amount: 0)
//                        deleteTaskViewModel.value = TaskValue(amount: swipedTask?.value?.amount, currency: selectedTask?.value?.currency)
                        deleteTaskViewModel.taskVolume = swipedTask?.taskVolume ?? 0
                        deleteTaskViewModel.tagLogs = swipedTask?.tagLogs ?? []
                        
                        Task {
//                            taskListViewModel.removeTask(taskID: swipedTaskID ?? "")
                            try await deleteTaskViewModel.deleteTask()
                            
                            if NetworkManager.shared.statusCode == 200 {
                                taskListViewModel.removeTask(taskID: swipedTaskID ?? "")
                                taskStatus[swipedTaskID ?? ""] = 5
                                
                                //Refresh the list after deleting
                                Task {
                                    try await taskListViewModel.getFilterTaskList()
                                    
                                    if NetworkManager.shared.statusCode == 200 {
                                        filterTaskData = taskListViewModel.filterTaskData
                                    }
                                }
                            }
                            else {
                                showWarningPopup.toggle()
                            }
                        }
                    }),
                          secondaryButton: .cancel())
                }
                else{
                    // MARK: To complete the task
                    Alert(title: Text("Do you really want to complete the task ?"),
                          primaryButton: .default(Text("Complete"), action: {
                        
                        withAnimation {
                            //TODO: to remove with animation
                        }
                        
                        updateTaskViewModel.taskID = swipedTaskID ?? ""
                        updateTaskViewModel.status = 4
                        updateTaskViewModel.currentDateTime = FormatterHelper.shared.getCurrentDateTimeFormatted()
//                        updateTaskViewModel.latitude = swipedTask?.latitude ?? ""
//                        updateTaskViewModel.longitude = swipedTask?.longitude ?? ""
                        if let lat = permissionManager.userLocation?.coordinate.latitude{
                            updateTaskViewModel.latitude = String(lat)
                        }
                        if let long = permissionManager.userLocation?.coordinate.longitude{
                            updateTaskViewModel.longitude = String(long)
                        }
//                        updateTaskViewModel.value = swipedTask?.value ?? FilterTaskValue(convertedAmountInUSD: nil, currency: "INR", amount: 0)
                        updateTaskViewModel.taskValue = TaskValue(amount: swipedTask?.value?.amount, currency: selectedTask?.value?.currency)
                        updateTaskViewModel.taskVolume = swipedTask?.taskVolume ?? 0
                        updateTaskViewModel.tagLogs = swipedTask?.tagLogs ?? []
                        
                        Task {
                            try await updateTaskViewModel.updateTaskStatus()
                            
                            if NetworkManager.shared.statusCode == 200 {
                                taskStatus[swipedTaskID ?? ""] = 4
                            }
                            else {
                                showWarningPopup.toggle()
                            }
                        }
                    }),
                          secondaryButton: .cancel())
                }
            }
            
        }
        
    }
}

#Preview {
    TaskView(showSideMenu: .constant(false), isTaskRunning: .constant(false))
        .environmentObject(PermissionManager())
//    TabMainView()
}


