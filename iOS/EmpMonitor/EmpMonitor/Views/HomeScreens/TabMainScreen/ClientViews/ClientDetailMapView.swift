//
//  ClientDetailMapView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/08/24.
//

import SwiftUI
import MapKit

struct ClientDetailMapView: View {
    
    @StateObject var profileImageLoader = ProfileImageLoader()
    
    @Environment(\.dismiss) var dismiss
    
    @EnvironmentObject var permissionManager: PermissionManager
    @EnvironmentObject var timerManager: TimerManager
    @EnvironmentObject var searchLocationViewModel: SearchLocationViewModel
    
    @StateObject private var updateClientViewModel = UpdateClientViewModel()
    
    //to refresh the Client List data after updating the data of any particular client
//    @ObservedObject var clientListViewModel: ClientListViewModel
//    @Binding var filteredClient: [ClientListResponseData]
    
    //User Current Location
    @State private var userLocation: CLLocationCoordinate2D?
    
    var clientLocation: CLLocationCoordinate2D {
        return CLLocationCoordinate2D(latitude: lat, longitude: long)
    }
    
    var lat: Double {
        if let latitude = Double(clientData.latitude ?? "\(permissionManager.userLocation?.coordinate.latitude ?? 0.0)") {
            return latitude
        }
        return 0.0 // Provide a default value if the conversion fails
    }
    
    var long: Double {
        if let longitude = Double(clientData.longitude ?? "\(permissionManager.userLocation?.coordinate.longitude ?? 0.0)") {
            return longitude
        }
        return 0.0 // Provide a default value if the conversion fails
    }
    
    @State private var selectedClientContactCard: String = "Direction"
    @State private var showEditClient: Bool = false
    
    var clientData: ClientListResponseData
    @State var clientProfilePic: UIImage?
    @State var clientProfileURL: String?
    
    //After updating the Client Navigation to client list and dismiss the second screen also
    @State private var secondScreenDismiss: Bool = false
    
    var body: some View {
        
        ZStack {
            LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                .ignoresSafeArea()
            
            VStack {
                
                //MARK: Map View
                VStack(spacing: 25) {
                    
                    
                    //MARK: MAP View
//                   EmpMapViewRepresentable()
                    ClientDetailMap(userLocation: $userLocation, clientLocation: clientLocation)
                    
                    
                    HStack(alignment: .top, spacing: 20) {
                        ClientProfileSmallDarkView(clientProfilePic: clientProfilePic)
//                            .environmentObject(profileImageLoader)
                        
                        //Client Info
                        VStack(alignment: .leading, spacing: 5){
                            Text(clientData.clientName)
                                .font(.system(size: 15, weight: .semibold))
                                .fontWeight(.semibold)
                            HStack(alignment: .top){
                                Circle()
                                    .fill(Color.yellow)
                                    .frame(width: 19, height: 19)
                                    .overlay {
                                        Image(.locationPointerIcon)
                                    }
                                Text("\(clientData.address1 ?? "") \(clientData.address2 ?? "")")
                            }
                            .font(.system(size: 12, weight: .regular))
                            .fontWeight(.medium)
                        }
                        
                        Spacer()
                        //Edit button
                        Image(.editAttendanceIcon)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 20, height: 20)
                            .onTapGesture {
                                withAnimation {
                                    
                                    updateClientViewModel.clientName = clientData.clientName
                                    updateClientViewModel.clientID = clientData.clientID ?? ""
                                    updateClientViewModel.emailID = clientData.emailID ?? ""
                                    updateClientViewModel.contactNumber = clientData.contactNumber ?? ""
                                    updateClientViewModel.clientProfilePic = clientData.clientProfilePic ?? ""
                                    updateClientViewModel.category = clientData.category ?? ""
                                    updateClientViewModel.countryCode = clientData.countryCode ?? ""
                                    updateClientViewModel.address1 = clientData.address1 ?? ""
                                    updateClientViewModel.address2 = clientData.address2 ?? ""
                                    updateClientViewModel.country = clientData.country ?? ""
                                    updateClientViewModel.state = clientData.state ?? ""
                                    updateClientViewModel.city = clientData.city ?? ""
                                    updateClientViewModel.zipCode = clientData.zipCode ?? ""
                                    if let lat = Double(clientData.latitude ?? "") {
                                        updateClientViewModel.latitude = lat
                                    }
                                    
                                    if let long = Double(clientData.longitude ?? ""){
                                        updateClientViewModel.longitude = long
                                    }
                                    
                                    showEditClient.toggle()
                                }
                            }
                    }
//                    .padding(.vertical, 10)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal, 40)
                    .foregroundStyle(Color.taskClientName)
          
                    HStack {
                        ClientContactView(text: "Call", contactIcon: Image(.callIcon), selectedClientContactCard: $selectedClientContactCard, clientData: clientData)
                        ClientContactView(text: "Message", contactIcon: Image(.messageIcon), selectedClientContactCard: $selectedClientContactCard, clientData: clientData)
                        ClientContactView(text: "Direction", contactIcon: Image(.directionIcon), selectedClientContactCard: $selectedClientContactCard, clientData: clientData)
                    }
                    
                    
//                    PrimaryButton(text: "Start Task") {
//                        //TODO: To start a task
//                    }
//                    .padding(.horizontal)
//                    .padding(.bottom, 30)
                    
                }
                .padding(.bottom, 40)
                .background(Color.white)
                .clipShape(RoundedRectangle(cornerRadius: 25))
                .padding(.top)
                .ignoresSafeArea(edges: .bottom)
            }
        }
        .onChange(of: profileImageLoader.clientProfileImage) { _, _ in
//            profileImageLoader.loadClientProfileImage(clientImageURL: clientData.clientProfilePic ?? "")
            self.clientProfilePic = profileImageLoader.clientProfileImage
        }
        .onAppear {
            
            userLocation = CLLocationCoordinate2D(latitude: permissionManager.userLocation?.coordinate.latitude ?? lat, longitude: permissionManager.userLocation?.coordinate.longitude ?? long)
            
//            clientProfileURL = clientData.clientProfilePic
            profileImageLoader.loadClientProfileImage(clientImageURL: clientProfileURL ?? clientData.clientProfilePic ?? "")
            self.clientProfilePic = profileImageLoader.clientProfileImage
            
            if secondScreenDismiss {
                dismiss()
            }
        }

        .navigationDestination(isPresented: $showEditClient, destination: {
            EditClientView(profileImageLoader: profileImageLoader, updateClientViewModel: updateClientViewModel, clientData: clientData, clientProfilePic: $clientProfilePic, clientProfileURL: $clientProfileURL, secondScreenDismiss: $secondScreenDismiss)
//                .environmentObject(profileImageLoader)
                .navigationBarBackButtonHidden()
        })
        .toolbar{
            ToolbarItem(placement: .topBarLeading) {
                BackButtonView()
                    .onTapGesture {
                        dismiss()
                    }
            }
        }
    }
}
//
//#Preview {
//    ClientDetailMapView()
//        .environmentObject(SearchLocationViewModel())
//        .environmentObject(PermissionManager())
//        .environmentObject(TimerManager())
//}
