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
        HStack(spacing: AppSpacing.stackSpacingMedium) {
            ClientProfileSmallDarkView(clientProfilePic: clientProfilePic)
//              .environmentObject(profileImageLoader)

            VStack(alignment: .leading, spacing: AppSpacing.xs) {
                Text(clientData.clientName)
                    .font(AppFont.primary(size: AppFont.Size.subheadline, weight: AppFont.Weight.semibold))
                    .fontWeight(AppFont.Weight.semibold)
                    .lineLimit(1)

                HStack(alignment: .top, spacing: AppSpacing.xs) {
                    Circle()
                        .fill(Color.yellow)
                        .frame(width: AppLayout.iconExtraSmall, height: AppLayout.iconExtraSmall)
                        .overlay {
                            Image(.locationPointerIcon)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: AppLayout.closeIconSize, height: AppLayout.closeIconSize)
                        }

                    Text("\(clientData.address1 ?? "") \(clientData.address2 ?? "")")
                        .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                        .lineLimit(2)
                }
            }
            .foregroundStyle(Color.taskClientName)

            Spacer(minLength: AppSpacing.sm)

            Button {
                withAnimation {
//                  showContactCard.toggle()
                    selectedClientContact = clientData
                    selectedClient = nil
                }
            } label: {
                Image(systemName: "chevron.down")
                    .font(AppFont.primary(size: AppFont.Size.iconExtraSmall, weight: AppFont.Weight.semibold))
                    .foregroundStyle(Color.primaryButton1)
                    .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
            }
            .buttonStyle(.plain)
            .accessibilityLabel("Show client contact actions")
        }
        .padding(.leading, AppSpacing.lg)
        .padding(.trailing, AppSpacing.sm)
        .padding(.vertical, AppSpacing.sm)
        .frame(maxWidth: .infinity, alignment: .leading)
        .frame(minHeight: 88)
        .background(selectedClient?.id == clientData.id ? Color.selectClientBG : Color.white)
        .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
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
        
    }
}


//#Preview {
//    ClientCardView(showContactCard: .constant(false))
//}
