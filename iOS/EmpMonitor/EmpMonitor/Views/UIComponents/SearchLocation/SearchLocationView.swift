//
//  SearchLocationView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 27/06/24.
//

import SwiftUI

struct SearchLocationView: View {
        
    @EnvironmentObject var searchLocationViewModel: SearchLocationViewModel
    @EnvironmentObject var permissionManager: PermissionManager
    
    @StateObject var placeViewModel = PlaceViewModel()
    
    @State private var searchText: String = ""
    @Binding var showSearchLocationView: Bool
    
    var body: some View {
        VStack {
            // MARK: Title
            HStack {
                Spacer()
                Text("Search Location")
                    .font(.custom("Montserrat", size: 15))
                    .fontWeight(.semibold)
                    .foregroundStyle(Color.searchTitleText)
                .padding(.vertical)
                
                
                VStack{
                    Image(systemName: "xmark")
                        .resizable().aspectRatio(contentMode: .fit)
                        .frame(width: 20, height: 20)
                        .padding()
                        .offset(x: 10)
                        .onTapGesture {
                            withAnimation {
                                showSearchLocationView.toggle()
                            }
                        }
                }
                .frame(width: UIScreen.main.bounds.width * 0.25)
                .padding(.leading, 30)
                    
            }
            
            //MARK: Search bar
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
                        TextField(text: $searchLocationViewModel.queryFragment) {
                            Text("Search for area, street name...")
                                    .font(.custom("Montserrat", size: 12))
                                    .foregroundStyle(Color.mapSearchBarText)
                                    
                        }
                        .font(.custom("Montserrat", size: 12))
                        .foregroundStyle(Color.mapSearchBarText)
                        .autocorrectionDisabled(true)
                        
//                        TextField(text: $placeViewModel.queryFragment) {
//                            Text("Search for area, street name...")
//                                    .font(.custom("Montserrat", size: 12))
//                                    .foregroundStyle(Color.mapSearchBarText)
//                                    
//                        }
//                        .font(.custom("Montserrat", size: 12))
//                        .foregroundStyle(Color.mapSearchBarText)
//                        .onChange(of: placeViewModel.queryFragment){ text in
//                            if !text.isEmpty {
//                                placeViewModel.search(text: text, region: permissionManager.region)
//                            }else{
//                                placeViewModel.places = []
//                            }
//                        }
//                        .autocorrectionDisabled(true)
                        
                        Image(systemName: "xmark")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 10.75, height: 10.27)
                            .foregroundStyle(Color.searchIcon)
//                            .onTapGesture {
//                                placeViewModel.queryFragment = ""
//                            }
                    }
                    .padding(.horizontal)
                }
                .shadow(color: .gray.opacity(0.2), radius: 4)
                .padding(.horizontal)
                .padding(.bottom, 25)
                
            //MARK: Search result List
            
            ScrollView {
                ForEach(searchLocationViewModel.results, id: \.self){ result in
                    AddressCellView(locationName: result.title, completeAddress: result.subtitle)
                        .padding(.horizontal, 15)
                        .onTapGesture {
                            searchLocationViewModel.selectedLocation(result)
                            searchLocationViewModel.selectedLocationTitle = result.title
                            searchLocationViewModel.selectedLocationSubtitle = result.subtitle
                            showSearchLocationView.toggle()
                        }
                }
                
//                ForEach(placeViewModel.places){ place in
//                    AddressCellView(locationName: place.name
//                                    , completeAddress: place.address)
//                        .padding(.horizontal, 15)
//                        .onTapGesture {
////                            searchLocationViewModel.selectedLocation(place)
//                            print("lat: \(place.latitude)")
//                            print("long: \(place.longitude)")
//                            showSearchLocationView.toggle()
//                        }
//                }
            }
        }
        .background(Color.white)
    }
}

#Preview {
    SearchLocationView(showSearchLocationView: .constant(false))
        .environmentObject(SearchLocationViewModel())
        .environmentObject(PermissionManager())
}
