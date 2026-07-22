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
                        .font(AppFont.primary(size: AppFont.Size.headline, weight: AppFont.Weight.semibold))
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
                                                .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                                .foregroundStyle(Color.gray)
                                        }else{
                                            Text("Search for area, street name...")
                                                .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
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
	                        .frame(maxWidth: AppLayout.checkInControlWidth)
	                        .padding(.horizontal, AppSpacing.md)
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
                
	                VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
	                    HStack(alignment: .top, spacing: AppSpacing.iconTextSpacing) {
	                        Image(.addressTargetIcon)
	                            .resizable()
	                            .aspectRatio(contentMode: .fit)
	                            .frame(width: AppLayout.iconMedium, height: AppLayout.iconMedium)

	                        Text(searchLocationViewModel.selectedLocationTitle)
	                            .font(AppFont.primary(size: AppFont.Size.title3, weight: AppFont.Weight.semibold))
	                            .lineLimit(2)
	                            .fixedSize(horizontal: false, vertical: true)
	                    }

	                    Text(searchLocationViewModel.selectedLocationSubtitle)
	                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
	                        .foregroundStyle(Color.addressText2)
	                        .lineLimit(2)
	                        .fixedSize(horizontal: false, vertical: true)
	                        .padding(.leading, AppLayout.iconMedium + AppSpacing.iconTextSpacing)

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
	                }
	                    .padding(AppSpacing.md)
	                    .frame(maxWidth: .infinity, alignment: .leading)
	                    .background(Color.white)
	                    .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
	                    .padding(.top, -15)
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
