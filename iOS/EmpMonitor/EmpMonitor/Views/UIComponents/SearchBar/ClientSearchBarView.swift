//
//  ClientSearchBarView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct ClientSearchBarView: View {
    
    @Binding var searchText: String
    @State private var showFilter: Bool = false
    
    var body: some View {
        GenericSearchBar(
            searchText: $searchText,
            showFilter: $showFilter,
            placeholder: "Search",
            bgColor: Color.taskSearchBar,
            showFilterButton: false,
            onFilterTap: nil
        )
    }
}

#Preview {
    ClientSearchBarView(searchText: .constant(""))
}
