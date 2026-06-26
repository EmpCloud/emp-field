//
//  SelectClientListView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct SelectClientListView: View {
    
    @ObservedObject var clientListViewModel: ClientListViewModel
    
    @Binding var selectedClient: ClientListResponseData?
    @Binding var filteredSelectClient: [ClientListResponseData]
    @Binding var searchText: String
    
    var body: some View {
        ScrollView {
            VStack {
                ForEach(filteredSelectClient, id: \.id) { clientData in
                    SelectClientCard(selectedClient: $selectedClient, clientData: clientData)
                        .padding(.horizontal)
                        .onTapGesture {
                            selectedClient = clientData
                        }
                    
                }
            }
            .onChange(of: searchText) {
                filterSelectClient()
            }
        }

    }
    
    private func filterSelectClient() {
        if searchText.isEmpty {
            filteredSelectClient = clientListViewModel.clientListData
        }else{
            filteredSelectClient = clientListViewModel.clientListData.filter { clientData in
                clientData.clientName.lowercased().contains(searchText.lowercased())
            }
        }
    }

}

//#Preview {
//    SelectClientListView(clientListViewModel: ClientListViewModel())
//}
