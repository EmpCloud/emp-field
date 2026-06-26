//
//  DynamicPopupDropdownMenu.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 14/08/24.
//

import SwiftUI

struct DynamicPopupDropdownMenu: View {
    
    @Binding var selection: LeavesTypeResponseDetail?
    
    @Binding var showTypeLeave: Bool
    @Binding var showDayTypeLeave: Bool
    
    var options: [LeavesTypeResponseDetail]
    var selectionTitle: String
    
    var body: some View {
        
        VStack(spacing: 5) {
            RoundedRectangle(cornerRadius: 6)
                .fill(Color.rectangleBG)
                .frame(width: 144, height: 34)
                .overlay {
                    HStack {
                        Text(selection?.name ?? selectionTitle)
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
                showDayTypeLeave = false
            }
        }
    }
    
    func OptionView() -> some View {
        RoundedRectangle(cornerRadius: 6)
            .fill(Color.rectangleBG)
            .frame(width: 144, height: 100)
            .overlay {
                VStack(spacing: 10) {
                    ForEach(options, id: \.id) { option in
                        Text(option.name)
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
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity ,alignment: .topLeading)
                .padding()
            }
    }
}

//
//#Preview {
//    DynamicPopupDropdownMenu(selection: .constant(nil), showTypeLeave: .constant(false), options: [LeavesTypeResponseDetail.Type], selectionTitle: "Leave Type")
//}
