//
//  AddClientView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/08/24.
//

import SwiftUI
import Combine

struct AddClientView: View {
    
    @Environment(\.dismiss) var dismiss
    
    @StateObject private var addClientViewModel = AddClientViewModel()
    @StateObject private var profileCameraViewModel = ProfileCameraViewModel()
    @StateObject private var uploadFileViewModel = UploadFilesViewModel()
    
    @EnvironmentObject var searchLocationViewModel: SearchLocationViewModel
    
    
    @State private var name: String = ""
    @State private var email: String = ""
    @State private var phoneNumber: String = ""
    @State private var categories: String = ""
    @State private var address: String = ""
    
    //Country Code
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
    
    @State private var showAddClientAddress: Bool = false
    
    @FocusState private var isKeyboardShowing: Bool
    
    
    //Warning
    @State private var showWarningPopup: Bool = false
    
    //Email Validation Warning
    @State private var isValidEmail: Bool = false

    
    var body: some View {
        
        ZStack(alignment: .top) {
            LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                .ignoresSafeArea(.all)
            
            VStack(){
                
                //MARK: Profile Image
                ClientProfileLargeWithCameraView(showProfileCamera: $showProfileCamera)
                    .padding(.top, 100)
                    .padding(.bottom, 10)
                
                        //MARK: Form
                        VStack(alignment: .leading, spacing: 15){
                            
                            VStack(spacing: 3) {
                                Text("Full Name*")
                                    .font(.system(size: 15, weight: .semibold))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                TextFieldEditableCreateProfileView(text: $addClientViewModel.clientName, placeholder: "Enter Full Name")
                            }
                            .padding(.trailing, 20)
                            .padding(.leading, 20)
                            
                            
                            //MARK: Email
                            VStack(spacing: 3) {
                                Text("Email ID")
                                    .font(.system(size: 15, weight: .semibold))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                
                                //MARK: Email Validation warning
                                if !isValidEmail && !addClientViewModel.emailID.isEmpty {
                                    VStack(alignment: .leading) {
                                        Text("Please enter a valid email*")
                                            .font(.custom("Ubuntu-Regular", size: 10))
                                            .foregroundStyle(Color.absent)
                                    }
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                }
                                
                                TextFieldEditableCreateProfileView(text: $addClientViewModel.emailID, placeholder: "Enter Email ID")
                            }
                            .padding(.trailing, 20)
                            .padding(.leading, 20)
                            .onChange(of: addClientViewModel.emailID) { _, newEmail in
                                withAnimation {
                                    isValidEmail = Validator.validateEmail(newEmail)
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
                            .onChange(of: phoneNumber) { _, newNumber in
                                addClientViewModel.contactNumber = phoneNumber
                            }
                            
                            
                            
                            //MARK: Categories
                            VStack(spacing: 3) {
                                Text("Categories*")
                                    .font(.system(size: 15, weight: .semibold))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                TextFieldEditableCreateProfileView(text: $addClientViewModel.category, placeholder: "Enter categories")
                            }
                            .padding(.trailing, 20)
                            .padding(.leading, 20)
                            
                            //edit address
                            if addClientViewModel.address1 != "" {
                                
                                //MARK: Address
                                VStack(spacing: 3) {
                                    Text("Address")
                                        .font(.system(size: 15, weight: .semibold))
                                        .frame(maxWidth: .infinity, alignment: .leading)
                                    EditAddressTextField(showClientAddress: $showAddClientAddress, placeholder: addClientViewModel.address1)
                                }
                                .padding(.trailing, 20)
                                .padding(.leading, 20)
                                
                            }else{
                                //MARK: Button
                                PrimaryBorderButton(text: "Add Address") {
                                    //To add address
                                    showAddClientAddress = true
                                    
                                }
                                .padding(.horizontal, 20)
                            }
                            
                            
                            
                            //MARK: Button
                            PrimaryThinButton(text: "Add Client") {
                                //TODO: To make api call to add the client
                                Task {
                                    addClientViewModel.countryCode = countryCode
                                    try await addClientViewModel.addClient()
                                    
                                    if NetworkManager.shared.statusCode == 200 {
                                        dismiss()
                                    }else {
                                        showWarningPopup.toggle()
                                    }
                                    
//                                    AppLog.debug(addClientViewModel.clientName)
//                                    AppLog.debug(addClientViewModel.emailID)
//                                    AppLog.debug(addClientViewModel.contactNumber)
//                                    AppLog.debug(addClientViewModel.clientProfilePic)
//                                    AppLog.debug(addClientViewModel.category)
//                                    AppLog.debug(addClientViewModel.countryCode)
//                                    AppLog.debug(addClientViewModel.address1)
//                                    AppLog.debug(addClientViewModel.address2)
//                                    AppLog.debug(addClientViewModel.country)
//                                    AppLog.debug(addClientViewModel.state)
//                                    AppLog.debug(addClientViewModel.city)
//                                    AppLog.debug(addClientViewModel.zipCode)
//                                    AppLog.debug(addClientViewModel.latitude)
//                                    AppLog.debug(addClientViewModel.longitude)
                                }
                                
                            }
                            .padding(.horizontal, 20)
                            .padding(.vertical)
                            .padding(.top)
                            .disableWithOpacity(addClientViewModel.clientName.isEmpty || addClientViewModel.contactNumber.isEmpty || addClientViewModel.address1.isEmpty || addClientViewModel.state.isEmpty || addClientViewModel.city.isEmpty || addClientViewModel.category.isEmpty)
                            
                            
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
            //                .padding(.bottom, 70)
            
            
            if showWarningPopup {
                ZStack {
                    WarningPopupView(titleText: NetworkManager.shared.responseMessage, description: NetworkManager.shared.errorMessage, showWarningPopup: $showWarningPopup)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.black.opacity(0.5))
                .onTapGesture {
                    showWarningPopup.toggle()
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
                        addClientViewModel.clientProfilePic = uploadFileViewModel.fetchProfileURL
                    }
                    
                    AppLog.debug("Client Profile Image received URLS: \(addClientViewModel.clientProfilePic)")
                }
            }
        }
        .navigationTitle("Add Client")
        .navigationBarTitleDisplayMode(.inline)
        .navigationDestination(isPresented: $showAddClientAddress) {
            AddClientAddressView(addClientViewModel: addClientViewModel)
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

#Preview {
    AddClientView()
        .environmentObject(PermissionManager())
        .environmentObject(CreateProfileViewModel())
        .environmentObject(SearchLocationViewModel())
}
