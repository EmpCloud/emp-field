//
//  ClientSearchBarView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct ClientSearchBarView: View {
    
    @Binding var searchText: String
    
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
                    }
                    .padding(.horizontal)
                }
                
        }
    }
}

#Preview {
    ClientSearchBarView(searchText: .constant(""))
}
