//
//  TaskSearchBarView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 21/08/24.
//

import SwiftUI

struct TaskSearchBarView: View {
    
    @Binding var searchText: String
    
    @Binding var selectedFilter: String?
    
    @Binding var showTaskFilter: Bool
    
    var body: some View {
        GenericSearchBar(
            searchText: $searchText,
            showFilter: $showTaskFilter,
            placeholder: "Search",
            bgColor: Color.taskSearchBar,
            showFilterButton: true,
            onFilterTap: nil
        )
    }
}

#Preview {
    TaskSearchBarView(searchText: .constant(""), selectedFilter: .constant(""), showTaskFilter: .constant(false))
}
