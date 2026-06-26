//
//  TaskSearchBarView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 21/08/24.
//

import SwiftUI

struct TaskSearchBarView: View {
    
    @Binding var searchText: String
    
    @Binding var selectedFilter: String?
    
    @Binding var showTaskFilter: Bool
    
    var body: some View {
        ZStack {
            
           
            
            RoundedRectangle(cornerRadius: 6)
                .fill(Color.taskSearchBar)
                .frame(height: 45)
                .frame(maxWidth: .infinity)
                .overlay(alignment: .leading) {
                    HStack{
                        Image(systemName: "magnifyingglass")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 19.17, height: 19.17)
                            .foregroundStyle(Color.white)
                        TextField(text: $searchText) {
                                Text("search....")
                                    .font(.custom("Montserrat", size: 12))
                                    .foregroundStyle(Color.white)
                                    
                        }
                        .font(.custom("Montserrat", size: 12))
                        .foregroundStyle(Color.white)
                        .padding(.leading, 10)
                        
                        Spacer()
                        
                        Image(.taskFilterIcon)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 16.76, height: 14.67)
                            .padding(.trailing, 5)
                            .onTapGesture {
                                withAnimation(.spring) {
                                    showTaskFilter.toggle()
                                }
                            }
                    }
                    .padding(.horizontal)
                }
                
        }
//        .frame(maxWidth: .infinity, maxHeight: .infinity)
//        .background(Color.black)
    }
}

#Preview {
    TaskSearchBarView(searchText: .constant(""), selectedFilter: .constant(""), showTaskFilter: .constant(false))
}
