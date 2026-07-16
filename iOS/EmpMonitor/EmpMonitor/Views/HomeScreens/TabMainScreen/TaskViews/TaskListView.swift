//
//  TaskListView.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 21/08/24.
//

import SwiftUI

struct TaskListView: View {
    
    @ObservedObject var taskListViewModel: TaskListViewModel
    @ObservedObject var deleteTaskViewModel: DeleteTaskViewModel
    @ObservedObject var updateTaskViewModel: UpdateTaskViewModel
    
    @Binding var filterTaskData: [FilterTaskListResponseData]?
    @Binding var taskStatus: [String : Int]   // to bind the button type eg: Start, Pause, Resume
    @Binding var selectedTask: FilterTaskListResponseData?
    @Binding var showTaskReschedule: Bool
    
    //Search
    @Binding var searchText: String
    
    //Swipe Gesture
    @Binding var swipeAmount: [String : CGFloat]
    @GestureState private var isDragging: Bool = false
    @Binding var swipedTaskID: String?  // track the currently swipe task Id
    @Binding var swipedTask: FilterTaskListResponseData?
    @Binding var showDeleteAlert: Bool
    @Binding var showAlert: Bool
    @Binding var showCompleteAlert: Bool
    
    
    //Warning:
    @Binding var showWarningPopup: Bool

    //Refresh Screen after updating the status
    @Binding var refreshScreen: Bool
    
    //Task Status used to manage the checkOUT
    @Binding var isTaskRunning: Bool
    
    var body: some View {
        ScrollView {
            VStack {
                if let filterTaskData = filterTaskData {
                    ForEach(filterTaskData, id: \.id) { filterTaskDetail in
                        
                        ZStack {
                            if swipeAmount[filterTaskDetail.id, default: 0] < 0 {
                                //MARK: Delete Task
                                Color.absent
                                    .frame(height: 95)
                                    .clipShape(RoundedRectangle(cornerRadius: 15))
                                    .padding(.horizontal, 34)
                                    .overlay(alignment: .trailing) {
                                        Button {
                                            withAnimation {
                                                showAlert = true
                                                showDeleteAlert = true
                                            }
                                        } label: {
                                            VStack {
                                                Image(.deleteWhiteIcon)
                                                    .accessibilityLabel("Delete")
                                                Text("Delete Task")
                                                    .font(.system(size: 12, weight: .semibold))
                                                    .foregroundStyle(Color.white)
                                            }
                                            .padding(.trailing, 50)
                                        }
                                        .accessibilityLabel("Delete Task")
                                        .accessibilityHint("Swipe left to delete this task")
                                    }
                            }
                            else if swipeAmount[filterTaskDetail.id, default: 0] > 0 {
                                //MARK: Finish Task
                                Color.present
                                    .frame(height: 95)
                                    .clipShape(RoundedRectangle(cornerRadius: 15))
                                    .padding(.horizontal, 34)
                                    .overlay(alignment: .leading) {
                                        Button {
                                            withAnimation {
                                                showAlert = true
                                                showDeleteAlert = false
                                                showCompleteAlert = true
                                            }
                                        } label: {
                                            VStack {
                                                Circle()
                                                    .fill(Color.white.opacity(0.3))
                                                    .frame(width: 23, height: 23)
                                                    .overlay {
                                                        Image(.completeCheckIcon)
                                                            .accessibilityLabel("Complete")
                                                    }

                                                Text("Finish Task")
                                                    .font(.system(size: 12, weight: .semibold))
                                                    .foregroundStyle(Color.white)
                                            }
                                            .padding(.leading, 50)
                                        }
                                        .accessibilityLabel("Finish Task")
                                        .accessibilityHint("Swipe right to mark this task as finished")
                                    }
                            }
                            
                            //MARK: Task
                            TaskCardView(updateTaskViewModel: updateTaskViewModel, filterTaskDetail: filterTaskDetail, selectedTask: $selectedTask, taskStatus: $taskStatus, showWarningPopup: $showWarningPopup, refreshScreen: $refreshScreen, isTaskRunning: $isTaskRunning)
                                .onTapGesture {
                                    selectedTask = filterTaskDetail
                                    showTaskReschedule.toggle()
                                }
                                .offset(x: swipeAmount[filterTaskDetail.id, default: 0])
                            //adjust the opacity based on swipe amount
                                .opacity(1.0 - Double(min(abs(swipeAmount[filterTaskDetail.id, default: 0]) / 1200, 1.0)))
                                .gesture(DragGesture()
                                    .updating($isDragging, body: { value, state, _ in
                                        // to validate the correct drag..
                                        state = true
                                        onChanged(value: value, task: filterTaskDetail)
                                    })
                                        .onEnded({ value in
                                            onEnd(value: value, task: filterTaskDetail)
                                        })
                                )
                                .onChange(of: swipedTaskID) { _, newTaskID in
                                    withAnimation(.snappy(duration: 0.5)) {
                                        if newTaskID != filterTaskDetail.id {
                                            swipeAmount[filterTaskDetail.id] = 0
                                        }
                                    }
                                }
                        }
                        
                    }
                }
                
            }
            .onChange(of: searchText) { _, _ in
                searchTask()
            }
        }
        .refreshable {  
//            selectedDate = nil // to reset the date
                Task {
                    taskListViewModel.date = FormatterHelper.shared.getTodaysDate()
                    taskListViewModel.status = 0
                    taskListViewModel.filterTaskData = []
                    AppLog.debug("-----filter task Data")
                    AppLog.debug(taskListViewModel.filterTaskData)
                   try await taskListViewModel.getFilterTaskList()
                    
                    if NetworkManager.shared.statusCode == 200 {
                        filterTaskData = taskListViewModel.filterTaskData
                    }
                    
                }
        }
    }
    
    //MARK: swipe Gesture functions
    func onChanged(value: DragGesture.Value, task: FilterTaskListResponseData) {
        if isDragging {
            swipeAmount[task.id] = value.translation.width
            swipedTaskID = task.id
            swipedTask = task
        }
    }
    
    func onEnd(value: DragGesture.Value, task: FilterTaskListResponseData) {
        withAnimation(.spring(duration: 0.5)) {
            if let swipe = swipeAmount[task.id] {
                if swipe < -80 {
                    swipeAmount[task.id] = -80
                    
                    if swipe <= -UIScreen.main.bounds.width * 0.5 {
                        //TODO: delete the task
                        withAnimation {
//                            AppLog.debug(swipedTaskID)
                            showAlert = true
                            showDeleteAlert = true
                        }
                    }
                }
                else if swipe > 80 {
                    swipeAmount[task.id] = 80
                    
                    if swipe >= UIScreen.main.bounds.width * 0.5 {
                        //TODO: Finish the task
                        withAnimation {
                            showAlert = true
                            showDeleteAlert = false
                            showCompleteAlert = true
                        }
                    }
                }else{
                    swipeAmount[task.id] = 0
                }
            }else{
                swipeAmount[task.id] = 0
            }
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                withAnimation {
                    swipeAmount[task.id] = 0
                }
            }
        }
    }
    
    
    //MARK: To handle the Task Search
    private func searchTask() {
        if searchText.isEmpty {
            filterTaskData = taskListViewModel.filterTaskData
        }else{
            filterTaskData = taskListViewModel.filterTaskData.filter { taskData in
                taskData.taskName.lowercased().contains(searchText.lowercased())
            }
        }
    }
}

//#Preview {
//    TaskListView()
//}
