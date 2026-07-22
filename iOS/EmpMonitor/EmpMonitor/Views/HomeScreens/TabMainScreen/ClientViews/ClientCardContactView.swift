//
//  ClientCardContactView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 28/08/24.
//

import SwiftUI

struct ClientCardContactView: View {
    
    @Binding var showContactCard: Bool
    @Binding var selectedClientContact: ClientListResponseData?
    @Binding var showClientDetailMap: Bool
//    @Binding var showSelectedClientDetailMap: Bool
    @Binding var showSelectedClientContactDetailMap: Bool
    
    var body: some View {
        HStack(spacing: AppSpacing.md) {
            Spacer(minLength: AppSpacing.zero)

            contactAction(title: "Call", image: Image(.callIcon))
            contactAction(title: "Message", image: Image(.messageIcon))
            contactAction(title: "Direction", image: Image(.directionIcon))

            Button {
                withAnimation {
//                  showContactCard.toggle()
                    selectedClientContact = nil
                }
            } label: {
                Image(systemName: "chevron.down")
                    .font(AppFont.primary(size: AppFont.Size.iconExtraSmall, weight: AppFont.Weight.semibold))
                    .foregroundStyle(Color.white)
                    .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
            }
            .buttonStyle(.plain)
            .accessibilityLabel("Collapse contact actions")
        }
        .padding(.horizontal, AppSpacing.md)
        .frame(maxWidth: .infinity)
        .frame(minHeight: 88)
        .background(
            LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
        )
        .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
        .rotation3DEffect(
            .degrees(selectedClientContact != nil ? 0 : 180), axis: (x: 1, y: 0.0, z: 0.0)
        )
        .animation(.smooth(duration: 2), value: showContactCard)
    }

    private func contactAction(title: String, image: Image) -> some View {
        Button {
            withAnimation {
                handleTapGesture(selectedCard: title)
            }
        } label: {
            VStack(spacing: AppSpacing.xs) {
                Circle()
                    .fill(Color.white)
                    .frame(width: 33, height: 33)
                    .overlay {
                        image
                    }
                Text(title)
                    .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.semibold))
                    .foregroundStyle(Color.white)
                    .lineLimit(1)
                    .minimumScaleFactor(0.85)
            }
            .frame(minWidth: 64, minHeight: 64)
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
        .accessibilityLabel(title)
    }
    
    private func handleTapGesture(selectedCard: String) {
        switch selectedCard {
        case "Call":
            //TODO: make a call
            //            AppLog.debug("Call")
            if let contact = selectedClientContact?.contactNumber {
                ClientHelper.shared.makeCall(to: contact)
            }
        case "Message":
            if let contact = selectedClientContact?.contactNumber {
                //                AppLog.debug("Message")
                ClientHelper.shared.sendMessage(to: contact)
            }
        case "Direction":
            AppLog.debug("Direction")
//            showClientDetailMap.toggle()
            showSelectedClientContactDetailMap.toggle()
            
        default:
            AppLog.debug("card tapped")
        }
    }

}

//#Preview {
//    ClientCardContactView(showContactCard: .constant(false))
//}
