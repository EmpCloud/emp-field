//
//  MapSearchBarView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 24/06/24.
//

import SwiftUI

struct MapSearchBarView: View {
    @Binding var searchText: String
    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 6)
                .fill(Color.white)
                .frame(height: 45)
                .frame(maxWidth: .infinity)
                .overlay(alignment: .leading) {
                    HStack{
                        Image(systemName: "magnifyingglass")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 19.17, height: 19.17)
                            .foregroundStyle(Color.searchIcon)
                        TextField(text: $searchText) {
                                Text("Search for area, street name...")
                                    .font(.custom("Montserrat", size: 12))
                                    .foregroundStyle(Color.mapSearchBarText)
                                    
                        }
                        .font(.custom("Montserrat", size: 12))
                        .foregroundStyle(Color.mapSearchBarText)
                    }
                    .padding(.horizontal)
                }
                
        }
//        .frame(maxWidth: .infinity, maxHeight: .infinity)
//        .background(Color.black)
    }
}

#Preview {
    MapSearchBarView(searchText: .constant(""))
}
