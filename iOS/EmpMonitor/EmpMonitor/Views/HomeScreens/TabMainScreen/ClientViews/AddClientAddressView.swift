//
//  AddClientAddressView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/08/24.
//

import SwiftUI

struct AddClientAddressView: View {
    
    @EnvironmentObject var searchLocationViewModel: SearchLocationViewModel
    @EnvironmentObject var permissionManager: PermissionManager
    @ObservedObject var addClientViewModel: AddClientViewModel
    
    @Environment(\.dismiss) var dismiss
    
    @State private var searchText: String = ""
    @State private var isSearching = false
    @State private var isLocationON = false
    @State private var searchPlaceholder = "Search for area, street name..."
    @State private var isCompleteAddress: Bool = false
    
    @State private var showMapAddress: Bool = false
//    @State private var showHomeScreen: Bool = false
    
    //address
    @State private var address: String = ""
    
    var body: some View {
        
        ZStack (alignment: .top) {
            LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                .ignoresSafeArea(.all)
            
            VStack(){
                ClientProfileLargeView()
                    .padding(.vertical, 20)
                
                
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
                                                        .keyboardType(.numberPad)
                                                        .toolbarDoneButton()
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
                                
                                //TODO: To make save the address for API Call
                                addClientViewModel.address1 = searchLocationViewModel.selectedLocationTitle
                                addClientViewModel.address2 = searchLocationViewModel.selectedLocationSubtitle
                                addClientViewModel.state = searchLocationViewModel.selectedLocationState
                                addClientViewModel.city = searchLocationViewModel.selectedLocationCity
                                addClientViewModel.zipCode = searchLocationViewModel.selectedLocationZipcode
                                addClientViewModel.country = searchLocationViewModel.selectedLocationCountry
                                addClientViewModel.latitude = searchLocationViewModel.selectedLocationLatitude ?? 0
                                addClientViewModel.longitude = searchLocationViewModel.selectedLocationLongitude ?? 0
//
                                
                                AppLog.debug("Client Address")
                                AppLog.debug("Lat: \(addClientViewModel.latitude)")
                                AppLog.debug("Long: \(addClientViewModel.longitude)")
                                AppLog.debug("address1: \(addClientViewModel.address1)")
                                AppLog.debug("address2: \(addClientViewModel.address2)")
                                AppLog.debug("State: \(addClientViewModel.state)")
                                AppLog.debug("City: \(addClientViewModel.city)")
                                AppLog.debug("ZipCode: \(addClientViewModel.zipCode)")
                                
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
                        .padding(.vertical, 20)
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
        .navigationTitle("Add Client")
        .navigationBarTitleDisplayMode(.inline)
//        .fullScreenCover(isPresented: $showHomeScreen) {
//            TabMainView()
//        }
//        .navigationDestination(isPresented: $showHomeScreen) {
//            TabMainView()
//                .navigationBarBackButtonHidden()
//        }
        .onAppear {
            
            //taking data from UserDefault and passing it to createProfileViewModel
//            let userData = AuthStore.shared.getLoggedInUser()
            
//            createProfileViewModel.address1 = userData?.body.data.userData.address1 ?? ""
//            createProfileViewModel.address2 = userData?.body.data.userData.address2 ?? ""
//            createProfileViewModel.state = userData?.body.data.userData.state ?? ""
//            createProfileViewModel.city = userData?.body.data.userData.city ?? ""
//            createProfileViewModel.zipCode = userData?.body.data.userData.zipCode ?? ""
//            createProfileViewModel.country = userData?.body.data.userData.country ?? ""
//            createProfileViewModel.latitude = userData?.body.data.userData.latitude ?? ""
//            createProfileViewModel.longitude = userData?.body.data.userData.longitude ?? ""
            
//            createProfileViewModel.address1 = searchLocationViewModel.selectedLocationTitle
//            createProfileViewModel.address2 = searchLocationViewModel.selectedLocationSubtitle
//            createProfileViewModel.state = searchLocationViewModel.selectedLocationState
//            createProfileViewModel.city = searchLocationViewModel.selectedLocationCity
//            createProfileViewModel.zipCode = searchLocationViewModel.selectedLocationZipcode
//            //TODO: To enable the user to able to change the country
//            createProfileViewModel.country = userData?.body.data.userData.country ?? "" // we are not chaning the country for now.
//            if let latitude = searchLocationViewModel.selectedLocationCoordinate?.latitude {
//                createProfileViewModel.latitude = "\(latitude)"
//            }
//            if let longitude = searchLocationViewModel.selectedLocationCoordinate?.longitude {
//                createProfileViewModel.longitude = "\(longitude)"
//            }
            
            AppLog.debug("Client Address")
            AppLog.debug("Lat: \(addClientViewModel.latitude)")
            AppLog.debug("Long: \(addClientViewModel.longitude)")
            AppLog.debug("address1: \(addClientViewModel.address1)")
            AppLog.debug("address2: \(addClientViewModel.address2)")
            AppLog.debug("State: \(addClientViewModel.state)")
            AppLog.debug("City: \(addClientViewModel.city)")
            AppLog.debug("ZipCode: \(addClientViewModel.zipCode)")
//            AppLog.debug("Name: \(addClientViewModel.fullName)")
//            AppLog.debug("Gender: \(createProfileViewModel.gender)")
//            AppLog.debug("Age: \(createProfileViewModel.age)")
            
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
    AddClientAddressView(addClientViewModel: AddClientViewModel())
        .environmentObject(SearchLocationViewModel())
        .environmentObject(PermissionManager())
//        .environmentObject(CreateProfileViewModel())
}
