//
//  ClientCardView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 28/08/24.
//

import SwiftUI

struct ClientCardView: View {
    
    @StateObject var profileImageLoader = ProfileImageLoader()
    
    @Binding var showContactCard: Bool
    @Binding var selectedClient: ClientListResponseData?
    @Binding var selectedClientContact: ClientListResponseData?
    
    var clientData: ClientListResponseData
    @State var clientProfilePic: UIImage?
    
    var body: some View {
//        HStack{
            RoundedRectangle(cornerRadius: 10)
            .fill(selectedClient?.id == clientData.id ? Color.selectClientBG : Color.white)
                .frame(height: 88)
                .overlay {
                    HStack(spacing: 20) {
                        ClientProfileSmallDarkView(clientProfilePic: clientProfilePic)
//                            .environmentObject(profileImageLoader)
                        
                        VStack(alignment: .leading) {
                            Text(clientData.clientName)
                                .font(.system(size: 15, weight: .semibold))
                                .fontWeight(.semibold)
                            
                            HStack {
                                Circle()
                                    .fill(Color.yellow)
                                    .frame(width: 17, height: 17)
                                    .overlay {
                                        Image(.locationPointerIcon)
                                            .resizable()
                                            .aspectRatio(contentMode: .fit)
                                            .frame(width: 11, height: 11)
                                    }
                                
                                    
                                Text("\(clientData.address1 ?? "") \(clientData.address2 ?? "")")
                                    .font(.system(size: 12, weight: .regular))
                            }
                        }
                        .foregroundStyle(Color.taskClientName)
                        
                        Spacer()
                        
                        Image(systemName: "chevron.down")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 18, height: 11)
                            .fontWeight(.semibold)
                            .foregroundStyle(Color.primaryButton1)
                            .onTapGesture {
                                withAnimation {
//                                    showContactCard.toggle()
                                    selectedClientContact = clientData
                                    selectedClient = nil
                                }
                            }
                            .padding(.trailing, 10)
                    }
                    .frame(height: 46)
                    .padding(.leading, 25)
                    .padding(.trailing, 10)
                }
                .rotation3DEffect(
                    .degrees(selectedClientContact?.id == clientData.id ? 180 : 0), axis: (x: 1, y: 0.0, z: 0.0)
                )
                .animation(.smooth(duration: 1), value: showContactCard)
                .onChange(of: profileImageLoader.clientProfileImage) { _, _ in
                    profileImageLoader.loadClientProfileImage(clientImageURL: clientData.clientProfilePic ?? "")
                    self.clientProfilePic = profileImageLoader.clientProfileImage
                }
                .onAppear {
                    profileImageLoader.loadClientProfileImage(clientImageURL: clientData.clientProfilePic ?? "")
                    self.clientProfilePic = profileImageLoader.clientProfileImage
                }
        
//        }
//        .padding(.horizontal)
    }
}


//#Preview {
//    ClientCardView(showContactCard: .constant(false))
//}
