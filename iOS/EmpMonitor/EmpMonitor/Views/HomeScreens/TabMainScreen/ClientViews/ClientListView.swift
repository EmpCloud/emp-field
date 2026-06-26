//
//  ClientListView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 28/08/24.
//

import SwiftUI

struct ClientListView: View {
    
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    @ObservedObject var clientListViewModel: ClientListViewModel
    
    @Binding var filteredClient: [ClientListResponseData]
    @Binding var searchText: String
    @Binding var showContactCard: Bool
    @Binding var selectedClient: ClientListResponseData?
    @Binding var selectedClientContact: ClientListResponseData?
    @Binding var showClientDetailMap: Bool
    @Binding var showSelectedClientDetailMap: Bool
    @Binding var showSelectedClientContactDetailMap: Bool
    
    var body: some View {
        ScrollView {
            VStack {
                ForEach(filteredClient, id: \.id){ clientData in
                    if selectedClientContact?.id == clientData.id /*showContactCard*/ {
                        ClientCardContactView(showContactCard: $showContactCard, selectedClientContact: $selectedClientContact, showClientDetailMap: $showClientDetailMap, showSelectedClientContactDetailMap: $showSelectedClientContactDetailMap)
                    }else{
                        ClientCardView(showContactCard: $showContactCard, selectedClient: $selectedClient, selectedClientContact: $selectedClientContact, clientData: clientData)
                            .environmentObject(profileImageLoader)
                            .onTapGesture {
                                Task {
                                    selectedClient = clientData
                                    selectedClientContact = nil
                                    
                                    withAnimation {
    //                                    showClientDetailMap.toggle()
                                        showSelectedClientDetailMap.toggle()
                                    }
                                }
                            }
                    }
                    
                }
            }
            .onChange(of: searchText) { _, _ in
                filterClient()
            }
        }
        .refreshable {
            //TODO: Client List API
            Task {
                try await clientListViewModel.getClientList()
                
                filteredClient = clientListViewModel.clientListData
            }
        }
    }
    
    private func filterClient() {
        if searchText.isEmpty {
            filteredClient = clientListViewModel.clientListData
        }else{
            filteredClient = clientListViewModel.clientListData.filter { clientData in
                clientData.clientName.lowercased().contains(searchText.lowercased())
            }
        }
    }
}

//#Preview {
//    ClientListView(clientListViewModel: ClientListViewModel(), showContactCard: .constant(false), selectedClient: <#Binding<ClientListResponseData?>#>)
//}
