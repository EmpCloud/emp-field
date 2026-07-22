//
//  SelectClientView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct SelectClientView: View {
    
    @Environment(\.dismiss) var dismiss
    
    @StateObject private var clientListViewModel = ClientListViewModel()
    
    @State private var searchText: String = ""
    @State private var filteredSelectClient: [ClientListResponseData] = []
    @Binding var selectedClient: ClientListResponseData?
    
    var body: some View {
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
                            VStack {
                               ClientSearchBarView(searchText: $searchText)
                                    .padding()
                                    .padding(.top)
                                
                                VStack{
//                                    if NetworkManager.shared.statusCode == 200 {
                                    SelectClientListView(clientListViewModel: clientListViewModel, selectedClient: $selectedClient, filteredSelectClient: $filteredSelectClient, searchText: $searchText)
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
                                
                                PrimaryButton(text: "Select Client") {
                                    //TODO: To select client
                                    dismiss()
                                }
                                .padding(.horizontal, 40)
                                .disableWithOpacity(selectedClient != nil ? false : true)
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                        
                        }
                    }
            }
            .onAppear{
                Task {
                    try await clientListViewModel.getClientList()
                    
                    filteredSelectClient = clientListViewModel.clientListData
                }
            }
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    BackButtonView()
                        .onTapGesture {
                            dismiss()
                            
                        }
                }
                ToolbarItem(placement: .principal) {
                    Text("Select Client")
                        .font(AppFont.primary(size: AppFont.Size.navigationTitle))
                        .fontWeight(AppFont.Weight.semibold)
                        .foregroundStyle(Color.white)
                }
            }
    }
}

//#Preview {
//    SelectClientView(selectedClient: <#Binding<ClientListResponseData?>#>)
//}
