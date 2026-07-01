//
//  CreateProfileView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 19/06/24.
//

import SwiftUI

struct CreateProfileView: View {
    
    @EnvironmentObject var createProfileViewModel: CreateProfileViewModel
    
    @EnvironmentObject var searchLocationViewModel: SearchLocationViewModel
    
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    //Camera
    @StateObject private var profileCameraViewModel = ProfileCameraViewModel()
    @StateObject private var uploadFileViewModel = UploadFilesViewModel()
    
    
    @State private var name: String = ""
//    @State private var email: String = ""
//    @State private var phoneNumber: String = ""
    @State private var age: String = ""
    
    @State private var selectedGender: String = ""
    
    @State private var showAddAddress: Bool = false
    
    //Camera
    @State private var showProfileCamera: Bool = false
    @State private var savedImageURL: URL?
    @State private var showCameraPermissionAlert: Bool = false
    
    //Email Validation Warning
    @State private var isValidEmail: Bool = false
    
    //MARK: Form Validation
    private var isFormValid: Bool {
        let hasName = !createProfileViewModel.fullName.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
        let hasEmail = Validator.validateEmail(createProfileViewModel.email)
        let hasPhone = HelperFunction.shared.isValidPhoneNumber(createProfileViewModel.phoneNumber)
        let hasAge = !age.isEmpty && (Int(age) ?? -1) >= 0 && (Int(age) ?? 101) <= 100
        let hasGender = !selectedGender.isEmpty
        return hasName && hasEmail && hasPhone && hasAge && hasGender
    }
    
    var body: some View {
        NavigationStack {
            ZStack(alignment: .top) {
                LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                    .ignoresSafeArea(.all)
                    .onTapGesture {
                        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
                    }
                
                ScrollView {
                    VStack(){
                        
                        //MARK: Profile Image
                        ProfileLargeWithCameraView(profileCameraViewModel: profileCameraViewModel, showCameraPermissionAlert: $showCameraPermissionAlert, showProfileCamera: $showProfileCamera)
                            .environmentObject(profileImageLoader)
                            .padding(.top, 100)
                            .padding(.bottom, 10)
                        
                        RoundedRectangle(cornerRadius: 25.0)
                            .fill(Color.white)
                            .frame(width: 363, height: 556)
                            .padding()
                            .overlay {
                                //MARK: Form
                                VStack(alignment: .leading, spacing: 15){
                                    
                                    VStack(spacing: 3) {
                                        Text("Full Name*")
                                            .font(.system(size: 15, weight: .semibold))
                                            .frame(maxWidth: .infinity, alignment: .leading)
                                        if createProfileViewModel.fullName.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                                            Text("Full name is required*")
                                                .font(.system(size: 12, weight: .regular))
                                                .frame(maxWidth: .infinity, alignment: .leading)
                                                .foregroundStyle(Color.absent)
                                        }
                                        TextFieldEditableCreateProfileView(text: $createProfileViewModel.fullName, placeholder: "Enter Full Name")
                                            .textInputAutocapitalization(.words)
                                            .autocorrectionDisabled()
                                    }
                                    .padding(.trailing, 20)
                                    .padding(.leading, 20)

                                    //MARK: Email
                                    VStack(spacing: 3) {
                                        Text("Email ID*")
                                            .font(.system(size: 15, weight: .semibold))
                                            .frame(maxWidth: .infinity, alignment: .leading)
                                        
                                        
                                        //MARK: Email Validation warning
                                        if !isValidEmail && !createProfileViewModel.email.isEmpty {
                                            VStack(alignment: .leading) {
                                                Text("Please enter a valid email*")
                                                    .font(.system(size: 12, weight: .regular))
                                                    .foregroundStyle(Color.absent)
                                            }
                                            .frame(maxWidth: .infinity, alignment: .leading)
                                            .padding(.horizontal)
                                        }
                                        
                                        
                                        TextFieldEditableCreateProfileView(text: $createProfileViewModel.email, placeholder: "Enter Email ID")
                                            .keyboardType(.emailAddress)
                                            .textContentType(.emailAddress)
                                            .autocapitalization(.none)
                                            .autocorrectionDisabled()
                                    }
                                    .padding(.trailing, 20)
                                    .padding(.leading, 20)
                                    .onChange(of: createProfileViewModel.email) { _, newEmail in
                                        withAnimation {
                                            isValidEmail = Validator.validateEmail(newEmail)
                                        }
                                    }
                                    
                                    
                                    //MARK: Phone
                                    VStack(spacing: 3) {
                                        Text("Mobile No.*")
                                            .font(.system(size: 15, weight: .semibold))
                                            .frame(maxWidth: .infinity, alignment: .leading)
                                        if !HelperFunction.shared.isValidPhoneNumber(createProfileViewModel.phoneNumber) {
                                            Text("Valid phone number is required*")
                                                .font(.system(size: 12, weight: .regular))
                                                .frame(maxWidth: .infinity, alignment: .leading)
                                                .foregroundStyle(Color.absent)
                                        }
                                        TextFieldEditableCreateProfileView(text: $createProfileViewModel.phoneNumber, placeholder: "Enter mobile no.")
                                            .keyboardType(.numberPad)
                                    }
                                    .padding(.trailing, 20)
                                    .padding(.leading, 20)
                                    
                                    //MARK: Age
                                    VStack(spacing: 3) {
                                        Text("Select Age")
                                            .font(.system(size: 15, weight: .semibold))
                                            .frame(maxWidth: .infinity, alignment: .leading)
                                        if Int(age) ?? 100 > 100 || Int(age) ?? 0 < 0 {
                                            Text("Age Should be between 0-100*")
                                                .font(.system(size: 12, weight: .regular))
                                                .frame(maxWidth: .infinity, alignment: .leading)
                                                .foregroundStyle(Color.absent)
                                        }
                                        TextFieldEditableCreateProfileView(text: $age, placeholder: "Age")
                                            .keyboardType(.numberPad)
                                    }
                                    .padding(.trailing, 20)
                                    .padding(.leading, 20)
                                    
                                    
                                    //MARK: Gender
                                    VStack(spacing: 3)  {
                                        Text("Select Gender*")
                                            .font(.system(size: 15, weight: .semibold))
                                            .frame(maxWidth: .infinity, alignment: .leading)
                                        if selectedGender.isEmpty {
                                            Text("Gender is required*")
                                                .font(.system(size: 12, weight: .regular))
                                                .frame(maxWidth: .infinity, alignment: .leading)
                                                .foregroundStyle(Color.absent)
                                        }
                                        HStack{
                                            
                                            ForEach(["Male", "Female", "Other"], id: \.self) { gender in
                                                Button {
                                                    selectedGender = gender
                                                } label: {
                                                    Circle()
                                                        .fill(gender == selectedGender.capitalized ? Color.blue: Color.white)
                                                        .frame(width: 14, height: 14)
                                                        .overlay {
                                                            Circle()
                                                                .stroke(Color(UIColor.lightGray), lineWidth: 3.0)
                                                        }
                                                    Text(gender)
                                                        .font(.system(size: 14, weight: .regular))
                                                    
                                                }
                                                .padding(.trailing, 20)

                                            }
                                        }
                                        .padding(.top, 3)
                                        .foregroundStyle(Color(red: 95/255, green: 95/255, blue: 95/255, opacity: 1.0))
                                        
                                    }
                                    .padding(.trailing, 20)
                                    .padding(.leading, 20)
                                    
                                    
                                    //MARK: Line
                                    LineView()
                                        .padding()
                                        .padding(.horizontal)
                                    
                                    
                                    //MARK: Button
                                    PrimaryButton(text: "Add Address") {
                                        //To add address
                                        guard isFormValid else { return }
                                        
                                        createProfileViewModel.age = age
                                        createProfileViewModel.gender = selectedGender
                                        showAddAddress = true
                                        
                                    }
                                    .padding(.horizontal, 50)
                                    .disableWithOpacity(!isFormValid)

                                    
                                }
//                                .background(Color.red)
                                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
                                .padding()
                                .padding(.top, 30)
                            }


                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
                    .padding(.bottom, 70)
                }
                .scrollDismissesKeyboard(.immediately)
                .toolbarDoneButton()
            }
            .onChange(of: savedImageURL) { oldSavedImageURL, newSavedImageURL in
                Task {
                    if let imageURL = newSavedImageURL {
                        uploadFileViewModel.selectedImageURLs.append(imageURL)
                        await uploadFileViewModel.uploadUserProfileImages()
                        
                        if NetworkManager.shared.statusCode == 200 {
                            createProfileViewModel.profilePic = uploadFileViewModel.fetchProfileURL
                            
                            // Using the uploaded image url to display the profile pic
                            profileImageLoader.profileImageURL = createProfileViewModel.profilePic ?? ""
//                            profileImageLoader.profileImageURL = UserDefaults.standard.string(forKey: "UserProfilePic") ?? ""
                            
                        }
                        
                        print("Profile Pic Uploaded: \(createProfileViewModel.profilePic)")
                    }
                }
            }
            .navigationTitle("Create Profile")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button("Sign Out") {
                        Task { @MainActor in
                            AuthStore.shared.clearSession()
                            AppState.shared.updateLoginState()
                        }
                    }
                    .foregroundStyle(.white)
                }
            }
            .navigationDestination(isPresented: $showAddAddress) {
                AddAddressView()
                    .navigationBarBackButtonHidden()
                    .environmentObject(searchLocationViewModel)
                    .environmentObject(createProfileViewModel)
                    .environmentObject(profileImageLoader)
            }
//            .onChange(of: selectedGender) { oldValue, newValue in
//                createProfileViewModel.gender = newValue
//            }
//            .onChange(of: age) { oldValue, newValue in
//                createProfileViewModel.age = newValue
//            }
//            .toolbar(content: {
//                ToolbarItem(placement: .keyboard) {
//                    Button("Done"){
//                        isKeyboardShowing.toggle()
//                    }
//                }
//            })
            .onAppear{
                
                //when this screen appears click image should be populate if click/selected
                savedImageURL = profileCameraViewModel.getCapturedImageURLs()
                
                let userData = AuthStore.shared.getLoggedInUser()
                
    //            name = userData?.body.data.userData.fullName ?? ""
    //            email = userData?.body.data.userData.email ?? ""
    //            phoneNumber = userData?.body.data.userData.phoneNumber ?? ""
                if createProfileViewModel.age == "" {
                    age = userData?.body.data?.userData.age ?? ""
                }else{
                    age = createProfileViewModel.age
                }
                if createProfileViewModel.gender == "" {
                    selectedGender = userData?.body.data?.userData.gender ?? ""
                }else{
                    selectedGender = createProfileViewModel.gender
                }
                
                createProfileViewModel.fullName = userData?.body.data?.userData.fullName ?? ""
                createProfileViewModel.email = userData?.body.data?.userData.email ?? ""
                // The API expects phoneNumber to contain digits only.
                createProfileViewModel.phoneNumber = HelperFunction.shared.sanitizePhoneNumber(userData?.body.data?.userData.phoneNumber ?? "")
                
//                createProfileViewModel.age = String(userData?.body.data.userData.age ?? 0)
//                createProfileViewModel.age = age
//                createProfileViewModel.gender = userData?.body.data.userData.gender ?? ""
//                createProfileViewModel.gender = selectedGender
                
                createProfileViewModel.profilePic = userData?.body.data?.userData.profilePic
                
    //            print("User:\(userData?.body.data.userData)")

                
                UINavigationBar.appearance().titleTextAttributes = [
                    .foregroundColor: UIColor.white
                ]
            }
            .navigationDestination(isPresented: $showProfileCamera, destination: {
                ProfileCameraView(profileCameraViewModel: profileCameraViewModel)
                    .navigationBarBackButtonHidden()
            })
            .ignoresSafeArea(.container)
        }
        
        
        
    }
    
  
}

#Preview {
    CreateProfileView()
        .environmentObject(PermissionManager())
        .environmentObject(CreateProfileViewModel())
        .environmentObject(SearchLocationViewModel())
        .environmentObject(ProfileImageLoader())
}
