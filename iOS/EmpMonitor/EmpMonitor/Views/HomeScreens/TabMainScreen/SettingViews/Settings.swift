//
//  Settings.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import SwiftUI

struct Settings: View {
    
    @Environment(\.dismiss) var dismiss
    
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    @StateObject private var getProfileViewModel = GetProfileViewModel()
    @StateObject private var updateProfileViewModel = UpdateProfileViewModel()
    
    @ObservedObject var motViewModel: MOTViewModel
    
    @State private var showMode: Bool = false
    @State private var showTermCondition: Bool = false
    @State private var showPrivacyPolicy: Bool = false
    @State private var showProfile: Bool = false
    
    @State private var userName: String = ""
    @State private var department: String = ""
    
    //for profile screen
    @State private var selectedGender: String = ""
    
    //Mode of travel
    @Binding var selectedMode: String?
    @Binding var tappedMode: String?
    
    //For logout button
    @Binding var isLogout: Bool
    
    //QRcode
    @Binding var showQRCode: Bool
    
    var body: some View {
        ZStack {
            LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                .ignoresSafeArea()
            
            RoundedRectangle(cornerRadius: 20)
                .fill(Color.rectangleBG)
                .padding(.top)
                .ignoresSafeArea(edges: .bottom)
                .shadow(color: .black.opacity(0.25), radius: 12)
                .overlay(alignment: .top) {
                    VStack {
                        HStack(spacing: 30) {
                            ProfileLargeView()
                                .padding(.top)
                                .environmentObject(profileImageLoader)
                            
                            VStack(alignment: .leading) {
                                Text(userName)
                                    .font(.system(size: 20, weight: .semibold))
                                    .foregroundStyle(Color.subText)
                                Text(department)
                                    .font(.system(size: 12, weight: .semibold))
                                    .foregroundStyle(Color.subText)
                            }
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(30)
                        
                        LineView()
                        
                        Button(action: {
                            Task {
                                showProfile.toggle()
                                try await getProfileViewModel.getProfile()

                                if let profileDetail = getProfileViewModel.profileDetail.first {
                                    updateProfileViewModel.fullName = profileDetail.fullName
                                    updateProfileViewModel.age = String(profileDetail.age ?? "")
                                    updateProfileViewModel.gender = profileDetail.gender ?? ""
                                    updateProfileViewModel.email = profileDetail.email ?? ""
                                    updateProfileViewModel.profilePic = profileDetail.profilePic ?? ""
                                    updateProfileViewModel.address1 = profileDetail.address1 ?? ""
                                    updateProfileViewModel.address2 = profileDetail.address2 ?? ""
                                    updateProfileViewModel.latitude = profileDetail.latitude ?? ""
                                    updateProfileViewModel.longitude = profileDetail.longitude ?? ""
                                    updateProfileViewModel.city = profileDetail.city ?? ""
                                    updateProfileViewModel.state = profileDetail.state ?? ""
                                    updateProfileViewModel.country = profileDetail.country ?? ""
                                    updateProfileViewModel.zipCode = profileDetail.zipCode ?? ""
                                    updateProfileViewModel.phoneNumber = profileDetail.phoneNumber ?? ""

                                    selectedGender = updateProfileViewModel.gender
                                }
                            }
                        }) {
                            HStack {
                                Image(.globeIcon)
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(width: 18, height: 18)
                                Text("Profile")
                                    .font(.system(size: 14, weight: .regular))
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .foregroundStyle(Color.primary)
                        }
                        .padding()
                        .padding(.horizontal, 15)
                        .accessibilityLabel("Profile")
                        .accessibilityHint("Double tap to view your profile")
                        
                        Button(action: {
                            withAnimation {
                                showTermCondition.toggle()
                            }
                        }) {
                            HStack {
                                Image(.termConditionIcon)
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(width: 18, height: 18)
                                Text("Terms & Conditions")
                                    .font(.system(size: 14, weight: .regular))
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .foregroundStyle(Color.primary)
                        }
                        .padding(.horizontal, 30)
                        .padding(.bottom)
                        .accessibilityLabel("Terms & Conditions")
                        .accessibilityHint("Double tap to read terms and conditions")
                        
                        Button(action: {
                            withAnimation {
                                showPrivacyPolicy.toggle()
                            }
                        }) {
                            HStack {
                                Image(.termConditionIcon)
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(width: 18, height: 18)
                                Text("Privacy Policy")
                                    .font(.system(size: 14, weight: .regular))
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .foregroundStyle(Color.primary)
                        }
                        .padding(.horizontal, 30)
                        .padding(.bottom)
                        .accessibilityLabel("Privacy Policy")
                        .accessibilityHint("Double tap to read privacy policy")
                        
                        Button(action: {
                            withAnimation {
                                showMode.toggle()
                            }
                        }) {
                            HStack {
                                Image(.carIcon)
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(width: 18, height: 18)
                                Text("Mode of Travel")
                                    .font(.system(size: 14, weight: .regular))
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .foregroundStyle(Color.primary)
                        }
                        .padding(.horizontal, 30)
                        .padding(.bottom)
                        .accessibilityLabel("Mode of Travel")
                        .accessibilityHint("Double tap to select your mode of travel")
                        
                        
                        Button(action: {
                            withAnimation {
                                showQRCode.toggle()
                            }
                        }) {
                            HStack {
                                Image(.qrCodeIcon)
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(width: 18, height: 18)
                                Text("QR Code")
                                    .font(.system(size: 14, weight: .regular))
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .foregroundStyle(Color.primary)
                        }
                        .padding(.horizontal, 30)
                        .padding(.bottom)
                        .accessibilityLabel("QR Code")
                        .accessibilityHint("Double tap to view your QR code")
                        
                        
                        LineView()
                        
                        
                        Button(action: {
                            UserDefaults.standard.set(false, forKey: "isCheckedIN")
                            UserDefaults.standard.set(false, forKey: "hasAcceptedTerms")
                            AuthStore.shared.clearSession()
                            isLogout.toggle()
                        }) {
                            HStack {
                                Image(.logoutBlueIcon)
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(width: 18, height: 18)
                                Text("Logout")
                                    .font(.system(size: 14, weight: .regular))
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .foregroundStyle(Color.primary)
                        }
                        .padding()
                        .padding(.horizontal, 15)
                        .accessibilityLabel("Logout")
                        .accessibilityHint("Double tap to sign out of your account")
                        
                        
                            
                    }
                    .foregroundStyle(Color.addressText2)
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
                    
                }
            
            if showMode {
                ZStack {
                    ModeOfTravelView(motViewModel: motViewModel, selectedMode: $selectedMode, tappedMode: $tappedMode, showMode: $showMode)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.black.opacity(0.5))
                .onTapGesture {
                    withAnimation {
                        showMode.toggle()
                    }
                }
                
            }
            
        }
        .onAppear {
//            let userData = AuthStore.shared.getLoggedInUser()
            
            userName = UserDefaults.standard.string(forKey: "UserName") ?? "User Name"
            department = UserDefaults.standard.string(forKey: "UserDepartment") ?? "Department"
            
        }
        .sheet(isPresented: $showTermCondition, content: {
            TermConditionView()
        })
        .sheet(isPresented: $showPrivacyPolicy, content: {
            PrivacyPolicyView()
        })
        .navigationDestination(isPresented: $showProfile) {
            ProfileView(getProfileViewModel: getProfileViewModel, updateProfileViewModel: updateProfileViewModel, selectedGender: $selectedGender)
                .environmentObject(profileImageLoader)
                .navigationBarBackButtonHidden()
        }
    }
}

#Preview {
    TabMainView()
        .environmentObject(SearchLocationViewModel())
//    Settings()
}
