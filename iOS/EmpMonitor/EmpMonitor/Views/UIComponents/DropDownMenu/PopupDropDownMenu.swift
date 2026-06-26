//
//  PopupDropDownMenu.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 31/07/24.
//

import SwiftUI

struct PopupDropDownMenu: View {
    
    @Binding var selection: String?
    
    @Binding var showTypeLeave: Bool
    @Binding var showLeaveTypeLeave: Bool
    
    var options: [String]
    var selectionTitle: String
    
    var body: some View {
        
        VStack(spacing: 5) {
            RoundedRectangle(cornerRadius: 6)
                .fill(Color.rectangleBG)
                .frame(width: 144, height: 34)
                .overlay {
                    HStack {
                        Text(selection ?? selectionTitle)
                            .font(.custom("Montserrat", size: 10))
                            .foregroundStyle(Color.addressText2)
                        
                        Spacer()
                        
                        Image(systemName: "chevron.down")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 10.17, height: 5.75)
                            .foregroundStyle(Color.addressText2)
                    }
                    .padding(.horizontal)
                }
            
            if showTypeLeave {
                OptionView()
//                    .padding(.top, 140)
            }
        }
        .onTapGesture {
            withAnimation {
                showTypeLeave.toggle()
                showLeaveTypeLeave = false
            }
        }
    }
    
    func OptionView() -> some View {
        RoundedRectangle(cornerRadius: 6)
            .fill(Color.rectangleBG)
            .frame(width: 144, height: 100)
            .overlay {
                VStack(spacing: 10) {
                    ForEach(options, id: \.self) { option in
                        Text(option)
                            .font(.custom("Montserrat", size: 10))
                            .foregroundStyle(Color.addressText2)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.vertical, 2)
                            .onTapGesture {
                                withAnimation {
                                    selection = option
                                    showTypeLeave.toggle()
                                }
                            }
                    }
//                    Text("First Half")
//                        .font(.custom("Montserrat", size: 10))
//                        .foregroundStyle(Color.addressText2)
//                        .frame(maxWidth: .infinity, alignment: .leading)
//                        .padding(.vertical, 2)
//                    Text("Second Half")
//                        .font(.custom("Montserrat", size: 10))
//                        .foregroundStyle(Color.addressText2)
//                        .frame(maxWidth: .infinity, alignment: .leading)
//                        .padding(.vertical, 2)
//                    Text("Full Day")
//                        .font(.custom("Montserrat", size: 10))
//                        .foregroundStyle(Color.addressText2)
//                        .frame(maxWidth: .infinity, alignment: .leading)
//                        .padding(.vertical, 2)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity ,alignment: .topLeading)
                .padding()
            }
    }
}

#Preview {
    PopupDropDownMenu(selection: .constant(nil), showTypeLeave: .constant(false), showLeaveTypeLeave: .constant(false), options: ["first half", "second half", "full day"], selectionTitle: "First Half")
}
