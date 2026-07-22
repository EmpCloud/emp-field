//
//  UpdateProfileAddressView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 03/09/24.
//

import SwiftUI

struct UpdateProfileAddressView: View {
    
    @EnvironmentObject var searchLocationViewModel: SearchLocationViewModel
    @EnvironmentObject var permissionManager: PermissionManager
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    @ObservedObject var updateProfileViewModel: UpdateProfileViewModel
    
    @Environment(\.dismiss) var dismiss
    
    @State private var searchText: String = ""
    @State private var isSearching = false
    @State private var isLocationON = false
    @State private var searchPlaceholder = "Search for area, street name..."
    @State private var isCompleteAddress: Bool = true
    
    @State private var showMapAddress: Bool = false
//    @State private var showHomeScreen: Bool = false
    
    //address
    @State private var address: String = ""
    
    var body: some View {
        
        ZStack (alignment: .top) {
            LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                .ignoresSafeArea(.all)
            
            VStack(){
                ProfileLargeView()
                    .environmentObject(profileImageLoader)
//                    .padding(.vertical, 10)
                    .padding(.top, 20)
                
                
//                RoundedRectangle(cornerRadius: /*@START_MENU_TOKEN@*/25.0/*@END_MENU_TOKEN@*/)
//                    .padding()
//                    .foregroundColor(.white)
//                    .overlay {
                        VStack(alignment: .leading){
                            
                            VStack {
                                Text("Add Address")
                                    .font(AppFont.primary(size: AppFont.Size.subheadline))
                                    .fontWeight(AppFont.Weight.semibold)
                                    .foregroundStyle(Color.addAddressText)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .padding(.top, 30)
                                
                            }
                            .padding(.trailing, 20)
                            .padding(.leading, 20)
                            
                            //MARK: Custom Search Bar
                            AddressSearchBarView()
                                .padding(.horizontal)
                                .onTapGesture {
                                    withAnimation {
                                        isCompleteAddress = true
                                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                            showMapAddress = true
                                        }
                                    }
                                }
                            
                            
                            //MARK: Enable Location
                            RoundedRectangle(cornerRadius: 10)
                                .foregroundStyle(Color(UIColor(red: 241/255, green: 247/255, blue: 255/255, alpha: 1.0)))
                                .padding(.horizontal)
                                .padding(.vertical, 5)
                                .frame(height: isCompleteAddress ? 420 : 120)
                                .overlay(alignment: .leading) {
                                    
                                    //MARK: Toggle button
                                    VStack(alignment: .leading){
                                        Toggle(isOn: $isLocationON) {
                                            Text("Enable your device location")
                                                .font(AppFont.primary(size: AppFont.Size.caption))
                                                .foregroundStyle(Color.subText)
                                        }
                                        .toggleStyle(SwitchToggleStyle(tint: Color(UIColor.systemBlue)))
                                        .onTapGesture {
                                            //TODO: Open Setting button
                                            HelperFunction.shared.openAppSetting()
                                        }
                                        
                                        //MARK: Line
                                        LineView()
                                            .padding(.vertical, 5)
                                        
                                        //MARK: Add address
                                        Button(action: {
                                            withAnimation {
                                                isCompleteAddress = true
                                                showMapAddress = true
                                            }
                                        }, label: {
                                            HStack {
                                                Text("+")
                                                    .font(AppFont.title2)
                                                Text("Add complete Address")
                                                    .foregroundStyle(Color.subText)
                                            }
                                            .font(AppFont.primary(size: AppFont.Size.caption))
                                            .padding(.bottom, 5)
                                        })
                                        
                                        
                                        if isCompleteAddress {
                                            VStack(spacing: 9){
                                                VStack(alignment: .leading) {
                                                    Text("Address line 1*")
                                                    AddressTextFieldView(text: $searchLocationViewModel.selectedLocationTitle, placeholder: "")
                                                }
                                                .padding(.horizontal)
                                                
                                                VStack(alignment: .leading) {
                                                    Text("Address line 2")
                                                    AddressTextFieldView(text: $searchLocationViewModel.selectedLocationSubtitle, placeholder: "")
                                                }
                                                .padding(.horizontal)
                                                
                                                VStack(alignment: .leading) {
                                                    Text("State*")
                                                    AddressTextFieldView(text: $searchLocationViewModel.selectedLocationState, placeholder: "")
                                                }
                                                .padding(.horizontal)
                                                
                                                VStack(alignment: .leading) {
                                                    Text("City*")
                                                    AddressTextFieldView(text: $searchLocationViewModel.selectedLocationCity, placeholder: "")
                                                }
                                                .padding(.horizontal)
                                                
                                                VStack(alignment: .leading) {
                                                    Text("Zip Code")
                                                    AddressTextFieldView(text: $searchLocationViewModel.selectedLocationZipcode, placeholder: "")
                                                }
                                                .padding(.horizontal)
                                                
                                            }
                                            .font(AppFont.primary(size: AppFont.Size.xSmall))
                                            .foregroundStyle(Color.addressText2)
                                        }
                                        
                                    }
                                    .padding(.horizontal, 30)
                                }
                            
                            Spacer()
                            
                            //MARK: Button
                            PrimaryButton(text: "Submit") {
                                
                                Task {
                                    //TODO: To make save the address for API Call
                                    updateProfileViewModel.address1 = searchLocationViewModel.selectedLocationTitle
                                    updateProfileViewModel.address2 = searchLocationViewModel.selectedLocationSubtitle
                                    updateProfileViewModel.state = searchLocationViewModel.selectedLocationState
                                    updateProfileViewModel.city = searchLocationViewModel.selectedLocationCity
                                    updateProfileViewModel.zipCode = searchLocationViewModel.selectedLocationZipcode
                                    updateProfileViewModel.country = searchLocationViewModel.selectedLocationCountry
                                    updateProfileViewModel.latitude = String(searchLocationViewModel.selectedLocationLatitude ?? 0)
                                    updateProfileViewModel.longitude = String(searchLocationViewModel.selectedLocationLongitude ?? 0)
    //
                                    
                                    AppLog.debug("Profile Address")
                                    AppLog.debug("Lat: \(updateProfileViewModel.latitude)")
                                    AppLog.debug("Long: \(updateProfileViewModel.longitude)")
                                    AppLog.debug("address1: \(updateProfileViewModel.address1)")
                                    AppLog.debug("address2: \(updateProfileViewModel.address2)")
                                    AppLog.debug("State: \(updateProfileViewModel.state)")
                                    AppLog.debug("City: \(updateProfileViewModel.city)")
                                    AppLog.debug("ZipCode: \(updateProfileViewModel.zipCode)")
                                    
                                    
                           
                                    
                                }
                                
                                
                                dismiss()
                            }
                            .padding(.horizontal)
                            .padding(.bottom, 30)
                            .disableWithOpacity(searchLocationViewModel.selectedLocationTitle.isEmpty || searchLocationViewModel.selectedLocationCity.isEmpty || searchLocationViewModel.selectedLocationState.isEmpty)
                            
                            
                        }
                        .frame(maxWidth: .infinity)
//                        .padding()
                        .background(Color.white)
                        .clipShape(RoundedRectangle(cornerRadius: 25))
                        .padding(.bottom, 10)
                        .padding(.vertical, 10)
                        .padding(.horizontal, 10)
//                    }
                
                
            }
            if !permissionManager.isLocationAuthorized {
                VStack {
                    LocationWarningView()
                }
                .frame(maxHeight: .infinity, alignment: .bottom)
                .padding(.bottom, 150)
            }
            
        }
        .navigationTitle("Profile")
        .navigationBarTitleDisplayMode(.inline)
//        .fullScreenCover(isPresented: $showHomeScreen) {
//            TabMainView()
//        }
//        .navigationDestination(isPresented: $showHomeScreen) {
//            TabMainView()
//                .navigationBarBackButtonHidden()
//        }
        .onAppear {
            
            searchLocationViewModel.selectedLocationTitle = updateProfileViewModel.address1
            searchLocationViewModel.selectedLocationSubtitle = updateProfileViewModel.address2
            searchLocationViewModel.selectedLocationState = updateProfileViewModel.state
            searchLocationViewModel.selectedLocationCity = updateProfileViewModel.city
            searchLocationViewModel.selectedLocationZipcode = updateProfileViewModel.zipCode
            searchLocationViewModel.selectedLocationCountry = updateProfileViewModel.country
            searchLocationViewModel.selectedLocationLatitude = Double(updateProfileViewModel.latitude)
            searchLocationViewModel.selectedLocationLongitude = Double(updateProfileViewModel.longitude)
 
//            AppLog.debug("Client Address")
//            AppLog.debug("Lat: \(updateClientViewModel.latitude)")
//            AppLog.debug("Long: \(updateClientViewModel.longitude)")
//            AppLog.debug("address1: \(updateClientViewModel.address1)")
//            AppLog.debug("address2: \(updateClientViewModel.address2)")
//            AppLog.debug("State: \(updateClientViewModel.state)")
//            AppLog.debug("City: \(updateClientViewModel.city)")
//            AppLog.debug("ZipCode: \(updateClientViewModel.zipCode)")
            
                UINavigationBar.appearance().titleTextAttributes = [
                    .foregroundColor: UIColor.white
                ]
            
            //condition to control the toggle button
            if permissionManager.isLocationAuthorized {
                isLocationON = true
            }
            else{
                isLocationON = false
            }
        }
        .sheet(isPresented: $showMapAddress, content: {
            MapAddressView()
        })
        .toolbar {
            ToolbarItem(placement: .topBarLeading) {
                Button {
                    dismiss()
                }label: {
                    BackButtonView()
                }
            }
        }

    }
        
}

#Preview {
    UpdateProfileAddressView(updateProfileViewModel: UpdateProfileViewModel())
        .environmentObject(SearchLocationViewModel())
        .environmentObject(PermissionManager())
}
