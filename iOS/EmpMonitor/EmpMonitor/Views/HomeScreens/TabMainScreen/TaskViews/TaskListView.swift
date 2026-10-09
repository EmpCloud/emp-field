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
        List {
            if let filterTaskData = filterTaskData {
                ForEach(filterTaskData, id: \.id) { filterTaskDetail in
                    TaskCardView(updateTaskViewModel: updateTaskViewModel, filterTaskDetail: filterTaskDetail, selectedTask: $selectedTask, taskStatus: $taskStatus, showWarningPopup: $showWarningPopup, refreshScreen: $refreshScreen, isTaskRunning: $isTaskRunning)
                        .contentShape(Rectangle())
                        .onTapGesture {
                            selectedTask = filterTaskDetail
                            showTaskReschedule.toggle()
                        }
                        .padding(.vertical, AppSpacing.xs)
                        .listRowInsets(EdgeInsets())
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.clear)
                        //MARK: Finish Task
                        .swipeActions(edge: .leading, allowsFullSwipe: true) {
                            Button {
                                confirm(filterTaskDetail, isDelete: false)
                            } label: {
                                Label("Finish Task", systemImage: "checkmark.circle")
                            }
                            .tint(Color.present)
                        }
                        //MARK: Delete Task
                        .swipeActions(edge: .trailing, allowsFullSwipe: true) {
                            // No destructive role: the row must stay until the user confirms.
                            Button {
                                confirm(filterTaskDetail, isDelete: true)
                            } label: {
                                Label("Delete Task", systemImage: "trash")
                            }
                            .tint(Color.absent)
                        }
                }
            }
        }
        .listStyle(.plain)
        .scrollContentBackground(.hidden)
        .onChange(of: searchText) { _, _ in
            searchTask()
        }
        .refreshable {
            taskListViewModel.date = FormatterHelper.shared.getTodaysDate()
            taskListViewModel.status = 0
            taskListViewModel.filterTaskData = []
            try? await taskListViewModel.getFilterTaskList()

            if NetworkManager.shared.statusCode == 200 {
                filterTaskData = taskListViewModel.filterTaskData
            }
        }
    }

    /// Records which task was swiped and asks the parent to confirm the action.
    private func confirm(_ task: FilterTaskListResponseData, isDelete: Bool) {
        swipedTaskID = task.id
        swipedTask = task
        showDeleteAlert = isDelete
        showCompleteAlert = !isDelete
        showAlert = true
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
