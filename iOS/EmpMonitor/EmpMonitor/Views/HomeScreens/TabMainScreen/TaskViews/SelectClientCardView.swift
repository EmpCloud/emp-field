//
//  SelectClientCard.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI
import CoreLocation

struct SelectClientCard: View {
    
    @EnvironmentObject var permissionManager: PermissionManager
    @StateObject var profileImageLoader = ProfileImageLoader()
    
    @State private var distance: Int?
    @Binding var selectedClient: ClientListResponseData?
    
    var clientData: ClientListResponseData
    @State var clientProfilePic: UIImage?
    
    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 10)
                .fill(Color.white)
                .frame(height: 77)
                .overlay {
                    HStack(spacing: 20) {
                        ClientProfileSmallDarkView(clientProfilePic: clientProfilePic)
//                            .environmentObject(profileImageLoader)
                        VStack(alignment: .leading, spacing: 3){
                            Text(clientData.clientName)
                                .font(.system(size: 15, weight: .semibold))
                                .foregroundStyle(Color.taskClientName)
                            Text("\(clientData.address1 ?? "") \(clientData.address2 ?? "")")
                                .font(.system(size: 12, weight: .medium))
                                .foregroundStyle(Color.taskClientName)
                        }
                        Spacer()
                        HStack{
                            Text("\(distance ?? 0)km")
                                .font(.system(size: 12, weight: .semibold))
                                .foregroundStyle(Color.subText)
                            
                            Circle()
                                .fill(distance ?? 0 < 11 ? Color.present : Color.absent)
                                .frame(width: 19, height: 19)
                                .overlay {
                                    Image(.locationPointerIcon)
                                }

                        }
                    }
                    .padding(30)
                }
                .overlay {
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(selectedClient?.id == clientData.id ? Color.primaryButton1 : Color.white)
                }
        }
        .onChange(of: profileImageLoader.clientProfileImage) { _, _ in
            profileImageLoader.loadClientProfileImage(clientImageURL: clientData.clientProfilePic ?? "")
            self.clientProfilePic = profileImageLoader.clientProfileImage
        }
        .onAppear {
            profileImageLoader.loadClientProfileImage(clientImageURL: clientData.clientProfilePic ?? "")
            self.clientProfilePic = profileImageLoader.clientProfileImage
        }
        .onAppear {
            //Calculate the distance
            let userLocation = CLLocationCoordinate2D(latitude: permissionManager.userLocation?.coordinate.latitude ?? 0, longitude: permissionManager.userLocation?.coordinate.longitude ?? 0)
            if let latitudeString = clientData.latitude,
               let longitudeString = clientData.longitude,
               let latitude = Double(latitudeString),
               let longitude = Double(longitudeString) {
                let clientLocation = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
                // Now you can use clientLocation
                distance = HelperFunction.shared.calculateDistance(from: userLocation, to: clientLocation) / 1000
            }
            
            
        }
    }
}

//#Preview {
//    SelectClientCard(clientData: ClientListResponseData)
//}
