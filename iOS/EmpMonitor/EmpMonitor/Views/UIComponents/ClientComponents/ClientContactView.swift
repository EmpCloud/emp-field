//
//  ClientContactCardView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/08/24.
//

import SwiftUI

struct ClientContactView: View {
    
    var text: String
    var contactIcon: Image
    
    @Binding var selectedClientContactCard: String
    var clientData: ClientListResponseData
    
    var body: some View {
        Button {
            withAnimation {
                selectedClientContactCard = text
                handleTapGesture(selectedCard: selectedClientContactCard)
            }
        } label: {
            VStack(spacing: AppSpacing.xs) {
                    Circle()
                        .fill(
                            .shadow(.inner(color: Color.taskSearchBar, radius: 4))
                        )
                        .foregroundStyle(Color.white)
                        .frame(width: 38, height: 38)
                        .overlay {
                            contactIcon
                        }
                    Text(text)
                        .font(AppFont.primary(size: AppFont.Size.caption))
                        .fontWeight(AppFont.Weight.medium)
                        .foregroundStyle(selectedClientContactCard == text ? Color.white : Color.subText)
                        .lineLimit(1)
                        .minimumScaleFactor(0.85)

                }
            .frame(width: 112)
            .frame(minHeight: 76)
            .background(selectedClientContactCard == text ? Color.primaryButton1 : Color.clientContactCardBG)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
            .shadow(color: .black.opacity(0.15), radius: 5, y: 2)
        }
        .buttonStyle(.plain)
        .accessibilityLabel(text)
    }
    

    private func handleTapGesture(selectedCard: String) {
        switch selectedCard {
        case "Call":
            //TODO: make a call
            //            AppLog.debug("Call")
            if let contact = clientData.contactNumber {
                ClientHelper.shared.makeCall(to: contact)
            }
        case "Message":
            if let contact = clientData.contactNumber {
                //                AppLog.debug("Message")
                ClientHelper.shared.sendMessage(to: contact)
            }
        case "Direction":
            AppLog.debug("Direction")
            
        default:
            AppLog.debug("card tapped")
        }
    }
}

//#Preview {
//    ClientContactCardView(text: "Call", contactIcon: Image(systemName: "xmark"), selectedClientContactCard: .constant(""))
//}
