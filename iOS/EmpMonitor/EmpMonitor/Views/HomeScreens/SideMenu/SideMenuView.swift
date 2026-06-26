//
//  SideMenuView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 24/07/24.
//

import SwiftUI

struct SideMenuView: View {
    
//    @Environment(\.dismiss) var dismiss
    
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    
    @Binding var showSideMenu: Bool
    @Binding var selectedTab: Int
    @Binding var showALHView: Bool
    @Binding var selectedALHView: String?
    
    //UserInfo
    @State private var name: String = ""
    @State private var department: String = ""
    
    //To control the logout button
    @Binding var isLogout: Bool
    
    var body: some View {
        
        ZStack/*(alignment: .topLeading)*/ {
            Color.white.ignoresSafeArea()
            
            VStack(alignment: .leading, spacing: 40) {
                HStack(spacing: 20){
                    
                    ProfileMediumDarkView()
                        .environmentObject(profileImageLoader)
                    
                    VStack(alignment: .leading) {
                        Text(name)
                            .font(.system(size: 16, weight: .semibold))
                            .foregroundStyle(Color.text1)

                        Text(department)
                            .font(.system(size: 12, weight: .regular))
                            .foregroundStyle(Color.addressText2)
                    }
                    .padding(.horizontal)
                    
                    Spacer()

                    Button(action: {
                        withAnimation(.spring) {
                            showSideMenu.toggle()
                        }
                    }) {
                        Image(systemName: "xmark")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 16.48, height: 16.43)
                            .fontWeight(.bold)
                            .foregroundStyle(
                                LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                            )
                    }
                    .padding()
                    .offset(x: 10.0, y: -5.0)
                    .accessibilityLabel("Close menu")
                    .accessibility(addTraits: .isButton)
                }
                .padding(.bottom)
                
                
                Group{
                    Button(action: {
                        withAnimation(.spring) {
                            showSideMenu.toggle()
                            selectedTab = 0
                        }
                    }) {
                        HStack(spacing: 20) {
                            Image(.homeIcon)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                            Text("Home")
                        }
                    }
                    .accessibilityLabel("Home")
                    .accessibility(addTraits: .isButton)
                    Button(action: {
                        withAnimation(.spring) {
                            showSideMenu.toggle()
                            selectedTab = 0
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5){
                                selectedALHView = "Attendance History"
                                showALHView.toggle()
                            }
                        }
                    }) {
                        HStack(spacing: 20) {
                            Image(.attendanceHistoryIcon)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                            Text("Attendance History")
                        }
                    }
                    .accessibilityLabel("Attendance History")
                    .accessibility(addTraits: .isButton)
                    Button(action: {
                        withAnimation(.spring) {
                            showSideMenu.toggle()
                            selectedTab = 0
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5){
                                selectedALHView = "Leaves"
                                showALHView.toggle()
                            }
                        }
                    }) {
                        HStack(spacing: 20) {
                            Image(.leavesIcon)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                            Text("Leaves")
                        }
                    }
                    .accessibilityLabel("Leaves")
                    .accessibility(addTraits: .isButton)
                    Button(action: {
                        withAnimation(.spring) {
                            showSideMenu.toggle()
                            selectedTab = 0
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5){
                                selectedALHView = "Holidays"
                                showALHView.toggle()
                            }
                        }
                    }) {
                        HStack(spacing: 20) {
                            Image(.holidaysIcon)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                            Text("Holidays")
                        }
                    }
                    .accessibilityLabel("Holidays")
                    .accessibility(addTraits: .isButton)
                    Button(action: {
                        withAnimation(.spring) {
                            showSideMenu.toggle()
                            selectedTab = 1
                        }
                    }) {
                        HStack(spacing: 20) {
                            Image(.taskIcon)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                            Text("Tasks")
                        }
                    }
                    .accessibilityLabel("Tasks")
                    .accessibility(addTraits: .isButton)
                    Button(action: {
                        withAnimation(.spring) {
                            showSideMenu.toggle()
                            selectedTab = 2
                        }
                    }) {
                        HStack(spacing: 20) {
                            Image(.clientIcon)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                            Text("Clients")
                        }
                    }
                    .accessibilityLabel("Clients")
                    .accessibility(addTraits: .isButton)
                    Button(action: {
                        withAnimation(.spring) {
                            showSideMenu.toggle()
                            selectedTab = 3
                        }
                    }) {
                        HStack(spacing: 20) {
                            Image(.settingsIcon)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                            Text("Settings")
                        }
                    }
                    .accessibilityLabel("Settings")
                    .accessibility(addTraits: .isButton)
                    Button(action: {
                        UserDefaults.standard.set(false, forKey: "isCheckedIN")
                        UserDefaults.standard.removeObject(forKey: "loggedInUser")
                        showSideMenu.toggle()
                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                            isLogout = true
                        }
                    }) {
                        HStack(spacing: 20) {
                            Image(.logoutIcon)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 20, height: 20)
                            Text("Logout")
                        }
                    }
                    .accessibilityLabel("Logout")
                    .accessibility(addTraits: .isButton)
                }
                .font(.system(size: 14, weight: .regular))
                .foregroundStyle(Color.text1)
            }
//            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
            .padding()
            .padding(.top, -200)
            .padding(.leading)
//            .padding(.bottom, 250)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
        .onAppear {
            self.name = UserDefaults.standard.string(forKey: "UserName") ?? ""
            self.department = UserDefaults.standard.string(forKey: "UserDepartment") ?? ""
        }
    }
}

#Preview {
//    TabMainView()
    SideMenuView(showSideMenu: .constant(false), selectedTab: .constant(0), showALHView: .constant(false), selectedALHView: .constant(nil), isLogout: .constant(false))
//        .environmentObject(AppState())
}
