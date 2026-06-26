//
//  FilterDropDownView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 25/07/24.
//

import SwiftUI

enum FilterOptions {
    case attendance
    case leaves
    case holidays
}

struct FilterDropDownView: View {
    
    @State private var showFilterDropDown: Bool = false
    
    let options: [String] = ["Attendance History", "Leaves", "Holidays"]
    
    @Binding var selection: String?
    
    var body: some View {
        ZStack {
            
//            HStack {
                
                //MARK: Filter
                VStack {
                    HStack {
                        if let selected = selection {
                            Text(selected)
                                .font(.custom("Montserrat", size: 14))
                                .fontWeight(.semibold)
                        }else{
                            Text("Attendance History")
                                .font(.custom("Montserrat", size: 14))
                                .fontWeight(.semibold)
                        }
                        
                        
                        Spacer()
                        
                        Image(systemName: "chevron.down")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 14.96, height: 8.61)
                            .padding(7)
                            .padding(.vertical, 5)
                    }
                    .padding(.leading, 20)
                    .padding(5)
                    .foregroundStyle(Color.white)
                    .onTapGesture {
                        withAnimation(.smooth) {
                            showFilterDropDown.toggle()
                        }
                    }
                    
                    if showFilterDropDown {
                        OptionView()
                    }
                }
                .background(
                    LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                )
                .clipShape(RoundedRectangle(cornerRadius: 6))
                    
//            }
            .frame(maxWidth: .infinity)
        }
        .frame(maxWidth: .infinity)
        .onTapGesture {
            withAnimation {
                showFilterDropDown.toggle()
            }
        }
    }
    
    func OptionView() -> some View {
        VStack(alignment: .leading) {
            ForEach(options, id: \.self) { option in
                Text(option)
                    .font(.custom("Montserrat", size: 14))
                    .foregroundStyle(Color.white)
                    .fontWeight(.semibold)
                    .padding(.horizontal, 25)
                    .padding(.bottom, 10)
                    .padding(.bottom, options.last == option ? 7 : 0)
                    .onTapGesture {
                        withAnimation {
                            selection = option
                            showFilterDropDown.toggle()
                        }
                    }
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .transition(.move(edge: .bottom))
        .zIndex(1)
    }
}

#Preview {
    FilterDropDownView(selection: .constant("Selected filter"))
}
