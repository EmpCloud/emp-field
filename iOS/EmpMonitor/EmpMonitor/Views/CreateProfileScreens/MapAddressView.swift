//
//  MapAddressView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 24/06/24.
//

import SwiftUI
import MapKit

struct MapAddressView: View {
    
    @Environment(\.dismiss) private var dismiss
    @EnvironmentObject var searchLocationViewModel: SearchLocationViewModel
    @EnvironmentObject var createProfileViewModel: CreateProfileViewModel
    
    @State private var searchText: String = ""
    @State private var showSearchLocationView: Bool = false
    @State private var createProfileMapViewCoordinator: CreateProfileMapViewRepresentable.MapCoordinator?
    
    var body: some View {
        ZStack {
            VStack(alignment: .leading, spacing: 0){
                HStack {
                    //MARK: Add Address Title
                    Text("Add Address")
                        .font(.system(size: 16, weight: .semibold))
                        .padding(.top)
                        .padding(.leading, 10)
                        .padding(.bottom)
                    
                    Spacer()
                    
                    //MARK: Back Button
                    BackButtonDarkView()
                        .onTapGesture {
                            dismiss()
                        }
                }
                .padding(.horizontal ,20)
                
                //MARK: Map
                CreateProfileMapViewRepresentable(mapCoordinatorBinding: $createProfileMapViewCoordinator)
                .overlay(alignment: .top) {
                    VStack{
                        
                        //MARK: Search Bar Rectangle
                        Button(action: {
                            withAnimation {
                                showSearchLocationView.toggle()
                            }
                        }) {
                            RoundedRectangle(cornerRadius: 8)
                                .fill(Color.white)
                                .frame(height: 44)
                                .frame(maxWidth: .infinity)
                                .overlay(alignment: .leading) {
                                    HStack(spacing: 8) {
                                        Image(systemName: "magnifyingglass")
                                            .resizable()
                                            .aspectRatio(contentMode: .fit)
                                            .frame(width: 20, height: 20)
                                            .foregroundStyle(Color.gray)

                                        if searchLocationViewModel.selectedLocationTitle != "" {
                                            Text("\(searchLocationViewModel.selectedLocationTitle)")
                                                .font(.system(size: 14, weight: .regular))
                                                .foregroundStyle(Color.gray)
                                        }else{
                                            Text("Search for area, street name...")
                                                .font(.system(size: 14, weight: .regular))
                                                .foregroundStyle(Color.gray)
                                        }
                                    }
                                    .padding(.horizontal)
                                }
                        }
                        .padding(.top)
                        .padding(.horizontal)
                        .accessibilityLabel("Search for location")
                        .accessibilityHint("Double tap to search for an address")
                        
                        Spacer()
                        
                        //MARK: Current location Button
                        CurrentLocationButton(text: "Use Current Location") {
                            if let userLocation = searchLocationViewModel.userCurrentLocation {
                                AppLog.debug(userLocation)
                                createProfileMapViewCoordinator?.centerOnUserLocation(userLocation: userLocation)
                            }
                        }
                        .padding(.horizontal, 110)
                        .padding(.bottom, 40)
                    }
                }
                .onAppear {
                    if let userLocation = searchLocationViewModel.userCurrentLocation {
                        AppLog.debug(userLocation)
                        createProfileMapViewCoordinator?.centerOnUserLocation(userLocation: userLocation)
                    }
                }
//                .overlay(alignment: .center) {
//                    Image(systemName: "mappin.circle.fill")
//                        .resizable()
//                        .foregroundColor(.red)
//                        .frame(width: 40, height: 40)
//                        .offset(y: -20)
//                }
                
                RoundedRectangle(cornerRadius: 16)
                    .fill(Color.white)
                    .frame(height: 173)
                    .frame(maxWidth: .infinity)
                    .padding(.top, -15)
                    .overlay {
                        VStack(alignment: .leading) {
                            HStack {
                                Image(.addressTargetIcon)
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(width: 22, height: 22)
                                Text(searchLocationViewModel.selectedLocationTitle)
                                    .font(.system(size: 18, weight: .semibold))
//                                Text("Bidadi")
//                                    .font(.custom("Montserrat", size: 20))
                            }
                            .padding(.horizontal, 40)
                            Text(searchLocationViewModel.selectedLocationSubtitle)
                                .font(.system(size: 14, weight: .regular))
                                .padding(.horizontal, 70)
                            
//                            Text("Bengaluru, India")
//                                .font(.custom("Montserrat", size: 14))
//                                .padding(.horizontal, 70)
                            
                            PrimaryButton(text: "Confirm Location") {
                                //TODO: To set the selected location to the createProfileViewModel properties
                                createProfileViewModel.address1 = searchLocationViewModel.selectedLocationTitle
                                createProfileViewModel.address2 = searchLocationViewModel.selectedLocationSubtitle
                                createProfileViewModel.state = searchLocationViewModel.selectedLocationState
                                createProfileViewModel.city = searchLocationViewModel.selectedLocationCity
                                createProfileViewModel.zipCode = searchLocationViewModel.selectedLocationZipcode
                                createProfileViewModel.country = searchLocationViewModel.selectedLocationCountry
                                
                                if let latitude = searchLocationViewModel.selectedLocationLatitude {
                                    createProfileViewModel.latitude = "\(latitude)"
                                }
                                if let longitude = searchLocationViewModel.selectedLocationLongitude {
                                    createProfileViewModel.longitude = "\(longitude)"
                                }
                                
                                // To confirm the location
                                dismiss()
                            }
                            .padding()
                        }
                        .padding()
                    }
            }
            .ignoresSafeArea(edges: .bottom)
            
            if showSearchLocationView {
                SearchLocationView(showSearchLocationView: $showSearchLocationView)
            }
        }
        .onDisappear {
            AppLog.debug(createProfileViewModel.age)
        }
        
    }
}

#Preview {
    MapAddressView()
        .environmentObject(SearchLocationViewModel())
        .environmentObject(CreateProfileViewModel())
}
