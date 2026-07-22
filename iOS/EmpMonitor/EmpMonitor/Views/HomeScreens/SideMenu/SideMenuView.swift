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
    @EnvironmentObject var permissionManager: PermissionManager
    
    
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
        GeometryReader { proxy in
            let drawerWidth = min(proxy.size.width * 0.86, 340)

            ZStack(alignment: .topLeading) {
                Color.black.opacity(0.32)
                    .ignoresSafeArea()
                    .contentShape(Rectangle())
                    .onTapGesture {
                        closeMenu()
                    }

                drawerContent(topInset: proxy.safeAreaInsets.top, bottomInset: proxy.safeAreaInsets.bottom)
                    .frame(width: drawerWidth, height: proxy.size.height, alignment: .top)
                    .background(Color.white)
                    .shadow(color: Color.black.opacity(0.18), radius: 18, x: 4, y: 0)
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
        .onAppear {
            self.name = UserDefaults.standard.string(forKey: "UserName") ?? ""
            self.department = UserDefaults.standard.string(forKey: "UserDepartment") ?? ""
        }
    }

    private func drawerContent(topInset: CGFloat, bottomInset: CGFloat) -> some View {
        VStack(alignment: .leading, spacing: AppSpacing.zero) {
            headerView
                .padding(.horizontal, AppSpacing.md)
                .padding(.bottom, AppSpacing.md)

            Divider()

            ScrollView(.vertical, showsIndicators: false) {
                VStack(alignment: .leading, spacing: AppSpacing.xs) {
                    menuRow(
                        title: "Home",
                        icon: Image(.homeIcon),
                        isSelected: selectedTab == 0 && !showALHView
                    ) {
                        selectHome()
                    }

                    menuRow(
                        title: "Attendance History",
                        icon: Image(.attendanceHistoryIcon),
                        isSelected: selectedALHView == "Attendance History" && showALHView
                    ) {
                        selectALH("Attendance History")
                    }

                    menuRow(
                        title: "Leaves",
                        icon: Image(.leavesIcon),
                        isSelected: selectedALHView == "Leaves" && showALHView
                    ) {
                        selectALH("Leaves")
                    }

                    menuRow(
                        title: "Holidays",
                        icon: Image(.holidaysIcon),
                        isSelected: selectedALHView == "Holidays" && showALHView
                    ) {
                        selectALH("Holidays")
                    }

                    menuRow(
                        title: "Tasks",
                        icon: Image(.taskIcon),
                        isSelected: selectedTab == 1
                    ) {
                        selectTab(1)
                    }

                    menuRow(
                        title: "Clients",
                        icon: Image(.clientIcon),
                        isSelected: selectedTab == 2
                    ) {
                        selectTab(2)
                    }

                    menuRow(
                        title: "Settings",
                        icon: Image(.settingsIcon),
                        isSelected: selectedTab == 3
                    ) {
                        selectTab(3)
                    }
                }
                .padding(.horizontal, AppSpacing.md)
                .padding(.vertical, AppSpacing.md)
            }

            Spacer(minLength: AppSpacing.zero)

            Divider()

            menuRow(title: "Logout", icon: Image(.logoutIcon), isDestructive: true) {
                logout()
            }
            .padding(.horizontal, AppSpacing.md)
            .padding(.top, AppSpacing.sm)
            .padding(.bottom, bottomInset + AppSpacing.md)
        }
    }

    private var headerView: some View {
        HStack(spacing: AppSpacing.md) {
            ProfileSmallView()
                .environmentObject(profileImageLoader)
                .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                .background(Color.profileDarkBg.opacity(0.08))
                .clipShape(Circle())

            VStack(alignment: .leading, spacing: AppSpacing.xs) {
                Text(name.isEmpty ? "User Name" : name)
                    .font(AppFont.primary(size: AppFont.Size.subheadline, weight: AppFont.Weight.semibold))
                    .foregroundStyle(Color.text1)
                    .lineLimit(1)
                    .truncationMode(.tail)

                Text(department.isEmpty ? "Department" : department)
                    .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                    .foregroundStyle(Color.addressText2)
                    .lineLimit(1)
                    .truncationMode(.tail)
            }

            Spacer(minLength: AppSpacing.sm)

            Button {
                closeMenu()
            } label: {
                Image(systemName: "xmark")
                    .font(AppFont.primary(size: AppFont.Size.iconExtraSmall, weight: AppFont.Weight.bold))
                    .foregroundStyle(Color.primaryButton1)
                    .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                    .contentShape(Rectangle())
            }
            .buttonStyle(.plain)
            .accessibilityLabel("Close menu")
        }
        .frame(maxWidth: .infinity)
        .frame(minHeight: AppLayout.minimumTouchTarget)
    }

    private func menuRow(
        title: String,
        icon: Image,
        isSelected: Bool = false,
        isDestructive: Bool = false,
        action: @escaping () -> Void
    ) -> some View {
        let tint = isDestructive ? Color.notification : (isSelected ? Color.primaryButton1 : Color.text1)

        return Button(action: action) {
            HStack(spacing: AppSpacing.md) {
                icon
                    .renderingMode(.template)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: AppLayout.iconSmall, height: AppLayout.iconSmall)

                Text(title)
                    .font(AppFont.primary(size: AppFont.Size.body, weight: isSelected ? AppFont.Weight.semibold : AppFont.Weight.regular))
                    .lineLimit(1)
                    .truncationMode(.tail)

                Spacer(minLength: AppSpacing.sm)

                if isSelected {
                    Image(systemName: "chevron.right")
                        .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.semibold))
                }
            }
            .foregroundStyle(tint)
            .padding(.horizontal, AppSpacing.md)
            .frame(maxWidth: .infinity, alignment: .leading)
            .frame(minHeight: AppLayout.minimumTouchTarget)
            .background(isSelected ? Color.primaryButton1.opacity(0.10) : Color.clear)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
        .accessibilityLabel(title)
    }

    private func closeMenu() {
        withAnimation(.spring) {
            showSideMenu = false
        }
    }

    private func selectHome() {
        withAnimation(.spring) {
            selectedTab = 0
            selectedALHView = nil
            showALHView = false
            showSideMenu = false
        }
    }

    private func selectALH(_ destination: String) {
        withAnimation(.spring) {
            selectedTab = 0
            showSideMenu = false
        }

        DispatchQueue.main.asyncAfter(deadline: .now() + 0.25) {
            selectedALHView = destination
            showALHView = true
        }
    }

    private func selectTab(_ tab: Int) {
        withAnimation(.spring) {
            selectedTab = tab
            selectedALHView = nil
            showALHView = false
            showSideMenu = false
        }
    }

    private func logout() {
        if UserDefaults.standard.bool(forKey: "isCheckedIN") {
            permissionManager.stopLocationUpdates()
        }

        UserDefaults.standard.set(false, forKey: "hasAcceptedTerms")
        AuthStore.shared.clearSession()

        withAnimation(.spring) {
            showSideMenu = false
        }

        DispatchQueue.main.asyncAfter(deadline: .now() + 0.25) {
            isLogout = true
        }
    }
}

#Preview {
//    TabMainView()
    SideMenuView(showSideMenu: .constant(false), selectedTab: .constant(0), showALHView: .constant(false), selectedALHView: .constant(nil), isLogout: .constant(false))
//        .environmentObject(AppState())
}
