//
//  AddAddressView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 24/06/24.
//

import SwiftUI

struct AddAddressView: View {
    
    @EnvironmentObject var searchLocationViewModel: SearchLocationViewModel
    @EnvironmentObject var permissionManager: PermissionManager
    @EnvironmentObject var createProfileViewModel: CreateProfileViewModel
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    @Environment(\.dismiss) var dismiss
    
    @State private var searchText: String = ""
    @State private var isSearching = false
    @State private var isLocationON = false
    @State private var searchPlaceholder = "Search for area, street name..."
    @State private var isCompleteAddress: Bool = false
    
    @State private var showMapAddress: Bool = false
    @State private var showHomeScreen: Bool = false
    @State private var showPhoneValidationAlert: Bool = false
    
    //address
    @State private var address: String = ""
    
    var body: some View {
        
        ZStack (alignment: .top) {
            LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                .ignoresSafeArea(.all)
            
            ScrollView {
                VStack(spacing: AppSpacing.stackSpacingMedium) {
                ProfileLargeView()
                    .environmentObject(profileImageLoader)
//                    .padding(.bottom)
                    .padding(AppSpacing.md)
                    .padding(.top, AppSpacing.md)
                
                
                VStack{
                    
                    VStack {
                        Text("Add Address")
                            .font(AppFont.primary(size: AppFont.Size.headline, weight: AppFont.Weight.semibold))
                            .foregroundStyle(Color.addAddressText)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.top, 30)
                        
                    }
                    .padding(.trailing, 20)
                    .padding(.leading, 20)
                    
                    //MARK: Custom Search Bar
                    Button(action: {
                        withAnimation {
                            isCompleteAddress = true
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                showMapAddress = true
                            }
                        }
                    }) {
                        AddressSearchBarView()
                    }
                    .padding(.horizontal)
                    .accessibilityLabel("Search for address")
                    .accessibilityHint("Double tap to open address search")
                    
                    
                    //MARK: Enable Location
                    VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
                                Toggle(isOn: $isLocationON) {
                                    Text("Enable your device location")
                                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
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
                                    .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular))
                                    .frame(minHeight: AppLayout.minimumTouchTarget)
                                    .contentShape(Rectangle())
                                })
                                
                                
                                if isCompleteAddress {
                                    VStack(spacing: AppSpacing.stackSpacingDefault) {
                                        VStack(alignment: .leading) {
                                            Text("Address line 1*")
                                            AddressTextFieldView(text: $searchLocationViewModel.selectedLocationTitle, placeholder: "")
                                                .textContentType(.streetAddressLine1)
                                                .autocorrectionDisabled()
                                        }
                                        .padding(.horizontal)

                                        VStack(alignment: .leading) {
                                            Text("Address line 2")
                                            AddressTextFieldView(text: $searchLocationViewModel.selectedLocationSubtitle, placeholder: "")
                                                .textContentType(.streetAddressLine2)
                                                .autocorrectionDisabled()
                                        }
                                        .padding(.horizontal)

                                        VStack(alignment: .leading) {
                                            Text("State*")
                                            AddressTextFieldView(text: $searchLocationViewModel.selectedLocationState, placeholder: "")
                                                .textContentType(.addressState)
                                                .textInputAutocapitalization(.words)
                                                .autocorrectionDisabled()
                                        }
                                        .padding(.horizontal)

                                        VStack(alignment: .leading) {
                                            Text("City*")
                                            AddressTextFieldView(text: $searchLocationViewModel.selectedLocationCity, placeholder: "")
                                                .textContentType(.addressCity)
                                                .textInputAutocapitalization(.words)
                                                .autocorrectionDisabled()
                                        }
                                        .padding(.horizontal)

                                        VStack(alignment: .leading) {
                                            Text("Zip Code")
                                            AddressTextFieldView(text: $searchLocationViewModel.selectedLocationZipcode, placeholder: "")
                                                .keyboardType(.numberPad)
                                                .textContentType(.postalCode)
                                                .toolbarDoneButton()
                                        }
                                        .padding(.horizontal)

                                    }
                                    .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                                    .foregroundStyle(Color.addressText2)
                                }
                                
                    }
                    .padding(.horizontal, AppSpacing.lg)
                    .padding(.vertical, AppSpacing.md)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(Color(UIColor(red: 241/255, green: 247/255, blue: 255/255, alpha: 1.0)))
                    .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
                    .padding(.horizontal)
                    .padding(.vertical, AppSpacing.xs)
                    
                    Spacer(minLength: AppSpacing.md)
                    
                    //MARK: Button
                    PrimaryButton(text: "Submit") {
                        
                        createProfileViewModel.country = searchLocationViewModel.selectedLocationCountry
                        
//                                TODO: To make an API call to create profile
//                                AppLog.debug("Lat: \(createProfileViewModel.latitude)")
//                                AppLog.debug("Long: \(createProfileViewModel.longitude)")
//                                AppLog.debug("address1: \(createProfileViewModel.address1)")
//                                AppLog.debug("address2: \(createProfileViewModel.address2)")
//                                AppLog.debug("State: \(createProfileViewModel.state)")
//                                AppLog.debug("City: \(createProfileViewModel.city)")
//                                AppLog.debug("Name: \(createProfileViewModel.fullName)")
//                                AppLog.debug("Gender: \(createProfileViewModel.gender)")
//                                AppLog.debug("Age: \(createProfileViewModel.age)")
//                                AppLog.debug("Email: \(createProfileViewModel.email)")
//                                AppLog.debug("Profile: \(createProfileViewModel.profilePic)")
//                                AppLog.debug("Country: \(createProfileViewModel.country)")
//                                AppLog.debug("Zip: \(createProfileViewModel.zipCode)")
//                                AppLog.debug("Phone: \(createProfileViewModel.phoneNumber)")
                        
                        
                        
                        // Sanitize and validate phone number before submitting.
                        createProfileViewModel.phoneNumber = HelperFunction.shared.sanitizePhoneNumber(createProfileViewModel.phoneNumber)
                        
                        guard HelperFunction.shared.isValidPhoneNumber(createProfileViewModel.phoneNumber) else {
                            showPhoneValidationAlert = true
                            return
                        }
                        
                        Task {
                            await createProfileViewModel.createProfile()
                            if createProfileViewModel.isProfileCreated {
                                showHomeScreen = true
                            }
                        }
                    }
                    .padding(.horizontal)
                    .padding(.bottom, AppSpacing.lg)
                    .disableWithOpacity(searchLocationViewModel.selectedLocationTitle.isEmpty || searchLocationViewModel.selectedLocationCity.isEmpty || searchLocationViewModel.selectedLocationState.isEmpty)
                    .alert("Invalid Phone Number", isPresented: $showPhoneValidationAlert) {
                        Button("OK", role: .cancel) { }
                    } message: {
                        Text("Please enter a valid phone number with at least 8 digits.")
                    }
                    
                    
                }
                .frame(maxWidth: .infinity, alignment: .topLeading)
                .background(Color.white)
                .clipShape(RoundedRectangle(cornerRadius: AppRadius.large))
                .padding(AppSpacing.md)
                
                }
            }
            .scrollDismissesKeyboard(.immediately)

            if !permissionManager.isLocationAuthorized {
                VStack {
                    LocationWarningView()
                }
                .frame(maxHeight: .infinity, alignment: .bottom)
                .padding(.bottom, 150)
            }
            
        }
        .navigationTitle("Create Profile")
        .navigationBarTitleDisplayMode(.inline)
        .fullScreenCover(isPresented: $showHomeScreen) {
            TabMainView()
                .environmentObject(profileImageLoader)
        }
//        .navigationDestination(isPresented: $showHomeScreen) {
//            TabMainView()
//                .navigationBarBackButtonHidden()
//        }
        .onAppear {
            
            //taking data from UserDefault and passing it to createProfileViewModel
            let userData = AuthStore.shared.getLoggedInUser()
            
//            createProfileViewModel.address1 = userData?.body.data.userData.address1 ?? ""
//            createProfileViewModel.address2 = userData?.body.data.userData.address2 ?? ""
//            createProfileViewModel.state = userData?.body.data.userData.state ?? ""
//            createProfileViewModel.city = userData?.body.data.userData.city ?? ""
//            createProfileViewModel.zipCode = userData?.body.data.userData.zipCode ?? ""
//            createProfileViewModel.country = userData?.body.data.userData.country ?? ""
//            createProfileViewModel.latitude = userData?.body.data.userData.latitude ?? ""
//            createProfileViewModel.longitude = userData?.body.data.userData.longitude ?? ""
            
            createProfileViewModel.address1 = searchLocationViewModel.selectedLocationTitle
            createProfileViewModel.address2 = searchLocationViewModel.selectedLocationSubtitle
            createProfileViewModel.state = searchLocationViewModel.selectedLocationState
            createProfileViewModel.city = searchLocationViewModel.selectedLocationCity
            createProfileViewModel.zipCode = searchLocationViewModel.selectedLocationZipcode
            createProfileViewModel.country = searchLocationViewModel.selectedLocationCountry
            //TODO: To enable the user to able to change the country
            createProfileViewModel.country = userData?.body.data?.userData.country ?? "" // we are not chaning the country for now.
            if let latitude = searchLocationViewModel.selectedLocationCoordinate?.latitude {
                createProfileViewModel.latitude = "\(latitude)"
            }
            if let longitude = searchLocationViewModel.selectedLocationCoordinate?.longitude {
                createProfileViewModel.longitude = "\(longitude)"
            }
            
            
            AppLog.debug("Lat: \(createProfileViewModel.latitude)")
            AppLog.debug("Long: \(createProfileViewModel.longitude)")
            AppLog.debug("address1: \(createProfileViewModel.address1)")
            AppLog.debug("address2: \(createProfileViewModel.address2)")
            AppLog.debug("State: \(createProfileViewModel.state)")
            AppLog.debug("City: \(createProfileViewModel.city)")
            AppLog.debug("Name: \(createProfileViewModel.fullName)")
            AppLog.debug("Gender: \(createProfileViewModel.gender)")
            AppLog.debug("Age: \(createProfileViewModel.age)")
            
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
    AddAddressView()
        .environmentObject(SearchLocationViewModel())
        .environmentObject(PermissionManager())
        .environmentObject(CreateProfileViewModel())
}
