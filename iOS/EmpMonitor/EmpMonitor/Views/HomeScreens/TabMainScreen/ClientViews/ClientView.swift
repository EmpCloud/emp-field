//
//  Client.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import SwiftUI

struct ClientView: View {
    
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    @StateObject private var clientListViewModel = ClientListViewModel()
    
    @State private var searchText: String = ""
    @State private var showContactCard: Bool = false
    @State private var filteredClient: [ClientListResponseData] = []
    @State private var showClientDetailMap: Bool = false
    @State private var showSelectedClientDetailMap: Bool = false
    @State private var showSelectedClientContactDetailMap: Bool = false
    @State private var showAddClient: Bool = false
    
    
    @Binding var selectedClient: ClientListResponseData?
    @Binding var selectedClientContact: ClientListResponseData?
    
    @Binding var showSideMenu: Bool
    
 
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
                                    ClientSearchBarView(searchText: $searchText)
                                }
                                .padding(.top, AppSpacing.sectionTopSpacing)
                                .padding(.horizontal, AppSpacing.screenHorizontalPadding)
                                
                                VStack{
//                                    if NetworkManager.shared.statusCode == 200 {
                                    ClientListView(clientListViewModel: clientListViewModel, filteredClient: $filteredClient, searchText: $searchText, showContactCard: $showContactCard, selectedClient: $selectedClient, selectedClientContact: $selectedClientContact, showClientDetailMap: $showClientDetailMap, showSelectedClientDetailMap: $showSelectedClientDetailMap, showSelectedClientContactDetailMap: $showSelectedClientContactDetailMap)
                                        .environmentObject(profileImageLoader)
                                        .padding(.horizontal, AppSpacing.screenHorizontalPadding)
//                                    }else{
//                                        VStack{
//                                            Image(.noDataFound)
//                                            Text("No Data Found")
//                                                
//                                        }
//                                        .padding(.top, 200)
//                                    }
                                    
                                }
                                .frame(maxWidth: .infinity, alignment: .center)
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                        }
                    }
                
                //MARK: Add Client button
                ZStack{
                    
                    CreateTaskButton(text: ""){
                        //TODO: Create the client
                        showAddClient.toggle()
                    }
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottom)
                .padding(.bottom, AppSpacing.floatingActionBottomPadding)
                
            }
            .onAppear {
                //TODO: Client List API
                Task {
                    try await clientListViewModel.getClientList()
                    filteredClient = []
                    filteredClient = clientListViewModel.clientListData
                }
            }
//            .navigationDestination(isPresented: $showClientDetailMap) {
//                if let selectedClient = selectedClient {
//                    ClientDetailMapView(clientData: selectedClient)
////                        .environmentObject(profileImageLoader)
//                        .navigationBarBackButtonHidden()
//                }
//                else if let selectedClientContact = selectedClientContact {
//                    ClientDetailMapView(clientData: selectedClientContact)
////                        .environmentObject(profileImageLoader)
//                        .navigationBarBackButtonHidden()
//                }
//                
//            }
            .navigationDestination(isPresented: $showSelectedClientDetailMap) {
                if let selectedClient = selectedClient {
                    ClientDetailMapView(clientData: selectedClient)
                        .navigationBarBackButtonHidden()
                }
            }
            .navigationDestination(isPresented: $showSelectedClientContactDetailMap) {
                if let selectedClientContact = selectedClientContact {
                    ClientDetailMapView(clientData: selectedClientContact)
                        .navigationBarBackButtonHidden()
                }
            }
            .navigationDestination(isPresented: $showAddClient) {
                AddClientView()
                    .navigationBarBackButtonHidden()
            }
        }
        
    }
}

#Preview {
//    ClientView(showSideMenu: .constant(false))
    TabMainView()
}
