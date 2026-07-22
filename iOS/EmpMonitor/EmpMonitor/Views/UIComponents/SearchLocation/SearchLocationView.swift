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
            ZStack {
                Text("Search Location")
                    .font(AppFont.primary(size: AppFont.Size.subheadline))
                    .fontWeight(AppFont.Weight.semibold)
                    .foregroundStyle(Color.searchTitleText)

                HStack {
                    Spacer()

                    Button {
                        withAnimation {
                            showSearchLocationView.toggle()
                        }
                    } label: {
                        Image(systemName: "xmark")
                            .font(AppFont.primary(size: AppFont.Size.iconSmall, weight: AppFont.Weight.semibold))
                            .foregroundStyle(Color.searchTitleText)
                            .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                    }
                    .buttonStyle(.plain)
                    .accessibilityLabel("Close search")
                }
            }
            .padding(.horizontal, AppSpacing.md)
            .padding(.vertical, AppSpacing.sm)
            
            //MARK: Search bar
            HStack(spacing: AppSpacing.iconTextSpacing) {
                Image(systemName: "magnifyingglass")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: AppLayout.iconSmall, height: AppLayout.iconSmall)
                    .foregroundStyle(Color.searchIcon)

                TextField(text: $searchLocationViewModel.queryFragment) {
                    Text("Search for area, street name...")
                        .font(AppFont.primary(size: AppFont.Size.caption))
                        .foregroundStyle(Color.mapSearchBarText)
                }
                .font(AppFont.primary(size: AppFont.Size.caption))
                .foregroundStyle(Color.mapSearchBarText)
                .autocorrectionDisabled(true)

                if !searchLocationViewModel.queryFragment.isEmpty {
                    Button {
                        searchLocationViewModel.queryFragment = ""
                    } label: {
                        Image(systemName: "xmark")
                            .font(AppFont.primary(size: AppFont.Size.closeIcon, weight: AppFont.Weight.semibold))
                            .foregroundStyle(Color.searchIcon)
                            .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                    }
                    .buttonStyle(.plain)
                    .accessibilityLabel("Clear search")
                }

//                        TextField(text: $placeViewModel.queryFragment) {
//                            Text("Search for area, street name...")
//                                    .font(AppFont.primary(size: AppFont.Size.caption))
//                                    .foregroundStyle(Color.mapSearchBarText)
//                                    
//                        }
//                        .font(AppFont.primary(size: AppFont.Size.caption))
//                        .foregroundStyle(Color.mapSearchBarText)
//                        .onChange(of: placeViewModel.queryFragment){ text in
//                            if !text.isEmpty {
//                                placeViewModel.search(text: text, region: permissionManager.region)
//                            }else{
//                                placeViewModel.places = []
//                            }
//                        }
//                        .autocorrectionDisabled(true)
            }
            .padding(.horizontal, AppSpacing.md)
            .frame(maxWidth: .infinity)
            .frame(minHeight: AppLayout.minimumTouchTarget)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
            .shadow(color: .gray.opacity(0.2), radius: 4)
            .padding(.horizontal)
            .padding(.bottom, AppSpacing.lg)
	                
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
//                            AppLog.debug("lat: \(place.latitude)")
//                            AppLog.debug("long: \(place.longitude)")
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
