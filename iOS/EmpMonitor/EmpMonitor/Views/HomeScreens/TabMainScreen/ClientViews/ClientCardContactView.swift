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
            RoundedRectangle(cornerRadius: 10)
                .fill(
                    LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                )
                .frame(height: 88)
                .overlay {
                    HStack(spacing: 15) {
                        Spacer()
                        
                        //MARK: Call
                        VStack {
                            Circle()
                                .fill(Color.white)
                                .frame(width: 33, height: 33)
                                .overlay {
                                    Image(.callIcon)
                                }
                            Text("Call")
                                .font(.system(size: 12, weight: .regular))
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.white)
                        }
                        .onTapGesture {
                            withAnimation {
                                handleTapGesture(selectedCard: "Call")
                            }
                        }
                        
                        Spacer()
                        
                        //MARK: Message
                        VStack {
                            Circle()
                                .fill(Color.white)
                                .frame(width: 33, height: 33)
                                .overlay {
                                    Image(.messageIcon)
                                }
                            Text("Message")
                                .font(.system(size: 12, weight: .regular))
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.white)
                        }
                        .onTapGesture {
                            withAnimation {
                                handleTapGesture(selectedCard: "Message")
                            }
                        }
                        
                        Spacer()
                        
                        //MARK: Direction
                        VStack {
                            Circle()
                                .fill(Color.white)
                                .frame(width: 33, height: 33)
                                .overlay {
                                    Image(.directionIcon)
                                }
                            Text("Direction")
                                .font(.system(size: 12, weight: .regular))
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.white)
                        }
                        .onTapGesture {
                            withAnimation {
                                handleTapGesture(selectedCard: "Direction")
                            }
                        }
                        
                        Spacer()
                        
                        Image(systemName: "chevron.down")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 18, height: 11)
                            .fontWeight(.semibold)
                            .foregroundStyle(Color.white)
                            .onTapGesture {
                                withAnimation {
//                                    showContactCard.toggle()
                                    selectedClientContact = nil
                                }
                            }
                            .padding(.trailing, 10)
                        
                    }
                    .frame(height: 46)
                    .padding(.leading, 25)
                    .padding(.trailing, 10)
                }
                .rotation3DEffect(
                    .degrees(selectedClientContact != nil ? 0 : 180), axis: (x: 1, y: 0.0, z: 0.0)
                )
                .animation(.smooth(duration: 2), value: showContactCard)
    }
    
    private func handleTapGesture(selectedCard: String) {
        switch selectedCard {
        case "Call":
            //TODO: make a call
            //            print("Call")
            if let contact = selectedClientContact?.contactNumber {
                ClientHelper.shared.makeCall(to: contact)
            }
        case "Message":
            if let contact = selectedClientContact?.contactNumber {
                //                print("Message")
                ClientHelper.shared.sendMessage(to: contact)
            }
        case "Direction":
            print("Direction")
//            showClientDetailMap.toggle()
            showSelectedClientContactDetailMap.toggle()
            
        default:
            print("card tapped")
        }
    }

}

//#Preview {
//    ClientCardContactView(showContactCard: .constant(false))
//}
