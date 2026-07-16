//
//  EditClientView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 02/09/24.
//

import SwiftUI
import Combine

struct EditClientView: View {
    
    @Environment(\.dismiss) var dismiss
    @ObservedObject var profileImageLoader: ProfileImageLoader
    
    @ObservedObject var updateClientViewModel: UpdateClientViewModel
    
    @StateObject private var profileCameraViewModel = ProfileCameraViewModel()
    @StateObject private var uploadFileViewModel = UploadFilesViewModel()
    
    @EnvironmentObject var searchLocationViewModel: SearchLocationViewModel
    
    //to refresh the Client List data after updating the data of any particular client
//    @ObservedObject var clientListViewModel: ClientListViewModel
//    @Binding var filteredClient: [ClientListResponseData]
    
    
    //clientData
    var clientData: ClientListResponseData
    @Binding var clientProfilePic: UIImage?
    @Binding var clientProfileURL: String?
    
    
    //Country Code
    @State private var phoneNumber: String = ""
    @State var presentCountryCode: Bool = false
    @State private var countryCode: String = "+91"
    @State private var countryFlag: String = "🇮🇳"
    @State private var searchCountry: String = ""
    @State private var countryPattern: String = "### ### ####"
    @State private var countryLimit: Int = 17
    
    @FocusState private var keyIsFocused: Bool
    
    let countries: [CPData] = Bundle.main.decode("CountryNumbers.json")
    
    var filteredResorts: [CPData] {
        if searchCountry.isEmpty {
            return countries
        }else {
            return countries.filter { $0.name.contains(searchCountry) }
        }
    }
    
    
    //Camera
    @State private var showProfileCamera: Bool = false
    @State private var savedImageURL: URL?
    
    @State private var showUpdateClientAddress: Bool = false
    
    @FocusState private var isKeyboardShowing: Bool
    
    
    //Warning
    @State private var showWarmningPopup: Bool = false
    
    //Email Validation Warning
    @State private var isValidEmail: Bool = false
    
    //After updating the Client Navigation to client list and dismiss the second screen also
    @Binding var secondScreenDismiss: Bool
    
    var body: some View {
        
        ZStack(alignment: .top) {
            LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                .ignoresSafeArea(.all)
            
            VStack(){
                
                //MARK: Profile Image
                ClientProfileLargeWithCameraView(showProfileCamera: $showProfileCamera, clientProfilePic: clientProfilePic)
                    .padding(.top, 100)
                    .padding(.bottom, 10)
                
                        //MARK: Form
                        VStack(alignment: .leading, spacing: 15){
                            
                            VStack(spacing: 3) {
                                Text("Full Name*")
                                    .font(.system(size: 15, weight: .semibold))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                TextFieldEditableCreateProfileView(text: $updateClientViewModel.clientName, placeholder: "Enter Full Name")
                            }
                            .padding(.trailing, 20)
                            .padding(.leading, 20)
                            
                            
                            //MARK: Email
                            VStack(spacing: 3) {
                                Text("Email ID")
                                    .font(.system(size: 15, weight: .semibold))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                
                                //MARK: Email Validation warning
                                if !isValidEmail && !updateClientViewModel.emailID.isEmpty {
                                    VStack(alignment: .leading) {
                                        Text("Please enter a valid email*")
                                            .font(.custom("Ubuntu-Regular", size: 10))
                                            .foregroundStyle(Color.absent)
                                    }
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                }
                                
                                TextFieldEditableCreateProfileView(text: $updateClientViewModel.emailID, placeholder: "Enter Email ID")
                            }
                            .padding(.trailing, 20)
                            .padding(.leading, 20)
                            .onChange(of: updateClientViewModel.emailID) { _, newEmail in
                                withAnimation {
                                    isValidEmail = Validator.validateEmail(newEmail)
                                }
                            }
                            .onAppear{
                                withAnimation {
                                    isValidEmail = Validator.validateEmail(updateClientViewModel.emailID)
                                }
                            }
                            
                            
                            //MARK: Phone
                            VStack(spacing: 3) {
                                Text("Mobile No.*")
                                    .font(.system(size: 15, weight: .semibold))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                
                                ZStack(alignment: .leading) {
                                    MobileTextFieldCreateProfileView(text: $phoneNumber, placeholder: "Enter mobile no.")
                                        .keyboardType(.numberPad)
                                        .focused($keyIsFocused)
                                        .toolbarDoneButton()
                                        .onReceive(Just(phoneNumber)) { _ in
                                            applyPatternOnNumbers(&phoneNumber, pattern: countryPattern, replacementCharacter: "#")
                                        }
                                    
                                    //MARK: Country flag and code
                                    Button{
                                        presentCountryCode = true
                                        keyIsFocused = false
                                    }label: {
                                        HStack(spacing: 7) {
                                            Text("\(countryFlag)")
                                            Text("\(countryCode)")
                                                .font(.custom("Poppins-Regular", size: 15))
                                                .foregroundStyle(Color.mobileText)
                                            
                                        }
                                    }
                                    .padding(.leading)
                                }
                            }
                            .padding(.trailing, 20)
                            .padding(.leading, 20)
                            
                            
                            
                            //MARK: Categories
                            VStack(spacing: 3) {
                                Text("Categories*")
                                    .font(.system(size: 15, weight: .semibold))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                TextFieldEditableCreateProfileView(text: $updateClientViewModel.category, placeholder: "Enter categories")
                            }
                            .padding(.trailing, 20)
                            .padding(.leading, 20)
                            
                            //edit address
                            if updateClientViewModel.address1 != "" {
                                
                                //MARK: Categories
                                VStack(spacing: 3) {
                                    Text("Address")
                                        .font(.system(size: 15, weight: .semibold))
                                        .frame(maxWidth: .infinity, alignment: .leading)
                                    EditAddressTextField(showClientAddress: $showUpdateClientAddress, placeholder: updateClientViewModel.address1)
                                }
                                .padding(.trailing, 20)
                                .padding(.leading, 20)
                                
                            }else{
                                //MARK: Button
                                PrimaryBorderButton(text: "Add Address") {
                                    //To add address
                                    showUpdateClientAddress = true
                                    
                                }
                                .padding(.horizontal, 20)
                            }
                            
                            
                            
                            //MARK: Button
                            PrimaryThinButton(text: "Update Client") {
                                //TODO: To make api call to add the client
                                Task {
                                    updateClientViewModel.clientID = clientData.id
                                    updateClientViewModel.contactNumber = phoneNumber
                                    updateClientViewModel.countryCode = countryCode
                                    try await updateClientViewModel.updateClient()
                                    
                                    if NetworkManager.shared.statusCode == 200 {
                                        dismiss()
                                        secondScreenDismiss.toggle()
                                    }else {
                                        showWarmningPopup.toggle()
                                    }
                                    
                                    
                                    AppLog.debug(updateClientViewModel.clientName)
                                    AppLog.debug(updateClientViewModel.emailID)
                                    AppLog.debug(updateClientViewModel.contactNumber)
                                    AppLog.debug(updateClientViewModel.clientProfilePic)
                                    AppLog.debug(updateClientViewModel.category)
                                    AppLog.debug(updateClientViewModel.countryCode)
                                    AppLog.debug(updateClientViewModel.address1)
                                    AppLog.debug(updateClientViewModel.address2)
                                    AppLog.debug(updateClientViewModel.country)
                                    AppLog.debug(updateClientViewModel.state)
                                    AppLog.debug(updateClientViewModel.city)
                                    AppLog.debug(updateClientViewModel.zipCode)
                                    AppLog.debug(updateClientViewModel.latitude)
                                    AppLog.debug(updateClientViewModel.longitude)
                                }
                                
                            }
                            .padding(.horizontal, 20)
                            .padding(.vertical)
                            .padding(.top)
                            .disableWithOpacity(updateClientViewModel.clientName.isEmpty || updateClientViewModel.contactNumber.isEmpty || updateClientViewModel.address1.isEmpty || updateClientViewModel.state.isEmpty || updateClientViewModel.city.isEmpty || updateClientViewModel.category.isEmpty)
                            
                            
                        }
                        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
//                        .padding()
                        .padding(.top)
                        .background(Color.white)
                        .clipShape(RoundedRectangle(cornerRadius: 20))
                        .padding(.vertical, 20)
                        .padding(.horizontal, 10)
                
                
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
            .onChange(of: profileImageLoader.clientProfileImage) { _, newURL in
//                if let url = newURL {
//                    profileImageLoader.loadClientProfileImage(clientImageURL: url)
//                    self.clientProfilePic = profileImageLoader.clientProfileImage
//                }
                self.clientProfilePic = profileImageLoader.clientProfileImage
                
            }
//            .onAppear {
//                profileImageLoader.loadClientProfileImage(clientImageURL: clientData.clientProfilePic ?? "")
//                self.clientProfilePic = profileImageLoader.clientProfileImage
//            }
            
            
            
            if showWarmningPopup {
                ZStack {
                    WarningPopupView(titleText: "Try Again!", description: NetworkManager.shared.responseMessage, showWarningPopup: $showWarmningPopup)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.black.opacity(0.5))
                .onTapGesture {
                    showWarmningPopup.toggle()
                }
            }
        }
        .onChange(of: savedImageURL) { _, _ in
            Task {
//                addClientViewModel.clientProfilePic = "" // removing the old URLs
                if let imageURL = savedImageURL {
                    uploadFileViewModel.selectedImageURLs.append(imageURL)
                    await uploadFileViewModel.uploadProfileImages()
                    
                    if NetworkManager.shared.statusCode == 200 {
                        updateClientViewModel.clientProfilePic = uploadFileViewModel.fetchProfileURL
                        
                        //update the profile after updating the profile pic
                        updateClientViewModel.clientID = clientData.id
                        try await updateClientViewModel.updateClient()
                        if NetworkManager.shared.statusCode == 200 {
                            //TODO: Profile pic uploaded successfully
                            AppLog.debug("Client Profile pic uploaded successfully")
                            
                            //TODO: Just Refresh the Profile Pic
                            clientProfileURL = updateClientViewModel.clientProfilePic
                            if let url = clientProfileURL {
                                profileImageLoader.loadClientProfileImage(clientImageURL: url)
                                
                            }
                            
                        }
                    }
                    
                    AppLog.debug("Profile Image received URLS: \(updateClientViewModel.clientProfilePic)")
                }
            }
        }
        .onAppear {
            countryCode = updateClientViewModel.countryCode
            countryFlag = countries.filter{ $0.dial_code.contains(updateClientViewModel.countryCode)}.first?.flag ?? ""
            
            phoneNumber = updateClientViewModel.contactNumber
//            updateClientViewModel.clientName = clientData.clientName
//            updateClientViewModel.emailID = clientData.emailID ?? ""
//            updateClientViewModel.contactNumber = clientData.contactNumber ?? ""
//            updateClientViewModel.clientProfilePic = clientData.clientProfilePic ?? ""
//            updateClientViewModel.category = clientData.category ?? ""
//            updateClientViewModel.countryCode = clientData.countryCode ?? ""
//            updateClientViewModel.address1 = clientData.address1 ?? ""
//            updateClientViewModel.address2 = clientData.address2 ?? ""
//            updateClientViewModel.country = clientData.country ?? ""
//            updateClientViewModel.state = clientData.state ?? ""
//            updateClientViewModel.city = clientData.city ?? ""
//            updateClientViewModel.zipCode = clientData.zipCode ?? ""
//            if let lat = Double(clientData.latitude ?? "") {
//                updateClientViewModel.latitude = lat
//            }
//            
//            if let long = Double(clientData.latitude ?? ""){
//                updateClientViewModel.longitude = long
//            }
        }
        .navigationTitle("Update Client")
        .navigationBarTitleDisplayMode(.inline)
        .navigationDestination(isPresented: $showUpdateClientAddress) {
            UpdateClientAddressView(updateClientViewModel: updateClientViewModel)
                .navigationBarBackButtonHidden()
                .environmentObject(searchLocationViewModel)
            //                    .environmentObject(createProfileViewModel)
        }
        .navigationDestination(isPresented: $showProfileCamera, destination: {
            ProfileCameraView(profileCameraViewModel: profileCameraViewModel)
                .navigationBarBackButtonHidden()
        })
        .sheet(isPresented: $presentCountryCode) {
            NavigationStack {
                List(filteredResorts) { country in
                    HStack {
                        Text(country.flag)
                        Text(country.name)
                            .font(.system(size: 15, weight: .semibold))
                        Spacer()
                        Text(country.dial_code)
                    }
                    .onTapGesture {
                        self.countryFlag = country.flag
                        self.countryCode = country.dial_code
                        self.countryPattern = country.pattern
                        self.countryLimit = country.limit
                        presentCountryCode = false
                        searchCountry = ""
                    }
                }
                .listStyle(.plain)
                .searchable(text: $searchCountry, prompt: "Your country")
                
            }
            .presentationDetents([.medium, .large])
            
        }
        .ignoresSafeArea(.keyboard)
        .toolbar(content: {
            ToolbarItem(placement: .topBarLeading) {
                BackButtonView()
                    .onTapGesture {
                        dismiss()
                    }
            }
            
//            ToolbarItem(placement: .keyboard) {
//                Button("Done"){
//                    isKeyboardShowing.toggle()
//                }
//            }
        })
        .onAppear{
            
            savedImageURL = profileCameraViewModel.getCapturedImageURLs()
            
            AppLog.debug(savedImageURL)
            
            UINavigationBar.appearance().titleTextAttributes = [
                .foregroundColor: UIColor.white
            ]
        }
        .ignoresSafeArea(.container)
    }
    
    //MARK: Phone number pattern
    func applyPatternOnNumbers(_ stringvar: inout String, pattern: String, replacementCharacter: Character) {
        var pureNumber = stringvar.replacingOccurrences(of: "[^0-9]", with: "", options: .regularExpression)
        for index in 0 ..< pattern.count {
            guard index < pureNumber.count else {
                stringvar = pureNumber
                return
            }
            let stringIndex = String.Index(utf16Offset: index, in: pattern)
            let patternCharacter = pattern[stringIndex]
            guard patternCharacter != replacementCharacter else {
                continue
            }
            pureNumber.insert(patternCharacter, at: stringIndex)
        }
        stringvar = pureNumber
    }
        
}

//#Preview {
//    EditClientView(clientData: ClientListResponseData)
//        .environmentObject(PermissionManager())
//        .environmentObject(CreateProfileViewModel())
//        .environmentObject(SearchLocationViewModel())
//}
