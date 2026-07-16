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
        RoundedRectangle(cornerRadius: 10)
            .fill(selectedClientContactCard == text ? Color.primaryButton1 : Color.clientContactCardBG)
            .frame(width: 112, height: 76)
            .shadow(color: .black.opacity(0.15), radius: 5, y: 2)
            .overlay {
                VStack {
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
                        .font(.custom("Montserrat", size: 12))
                        .fontWeight(.medium)
                        .foregroundStyle(selectedClientContactCard == text ? Color.white : Color.subText)
                    
                }
            }
            .onTapGesture {
                withAnimation {
                    selectedClientContactCard = text
                    handleTapGesture(selectedCard: selectedClientContactCard)
                }
            }
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
