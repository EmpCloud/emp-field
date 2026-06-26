//
//  AddressSearchBarView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 24/06/24.
//

import SwiftUI

struct AddressSearchBarView: View {
    
    @State var searchText: String = ""
    @State var isSearching: Bool = false
    @State var searchPlaceholder: String = ""
    
    
    
    var body: some View {
        ZStack {
            HStack{
//                TextField("Search for area, street name...", text: $searchText)
                RoundedRectangle(cornerRadius: 5)
                    .fill(
                        LinearGradient(gradient: Gradient(colors: [Color.blueGradient1, Color.blueGradient2]), startPoint: .top, endPoint: .bottom)
                    )
                    .frame(maxWidth: .infinity)
                    .frame(height: 35)
                    .font(.custom("Montserrat", size: 12))
//                    .padding(10)
//                    .padding(.horizontal, 50)
//                    .background(
//                        LinearGradient(gradient: Gradient(colors: [Color.blueGradient1, Color.blueGradient2]), startPoint: .top, endPoint: .bottom)
//                    )
//                    .clipShape(RoundedRectangle(cornerRadius: 5))
                    .overlay(alignment: .leading) {
                        HStack {
                            Button(action: {
                                //perform search
                            }, label: {
                                Image(systemName: "magnifyingglass")
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(width: 19.17, height: 19.17)
                                    .foregroundStyle(Color.searchIcon)
                            })
                            .padding()

                            Text("Search for area, street name...")
                                .font(.custom("Montserrat", size: 12))
                                .foregroundStyle(Color.mapSearchBarText)
                        }
                    }
                
            }
        }
    }
}

#Preview {
    AddressSearchBarView()
}
