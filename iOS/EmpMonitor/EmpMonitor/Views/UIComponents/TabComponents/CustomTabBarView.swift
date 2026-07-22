//
//  CustomTabBarView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/09/24.
//

import SwiftUI

struct CustomTabBarView: View {
    
    @Binding var tabSelection: Int
    @Namespace private var animationNamespace
    
    // To control the animation state
    @State private var isTapped: Bool = false
    
    let tabBarItem: [(image: String, title: String)] = [
        ("house", "Home"),
        ("list.bullet.clipboard", "Task"),
        ("person.badge.minus", "Client"),
        ("gearshape", "Settings")
    ]
    var body: some View {
        ZStack {
            Rectangle()
                .fill(Color.white)
                .frame(height: AppLayout.tabBarHeight)
            
            //Content of tabbar
            HStack {
                ForEach(0..<4) { index in
                    Spacer()
                    TabBarItemView(
                        image: tabBarItem[index].image,
                        title: tabBarItem[index].title,
                        isSelected: tabSelection == index,
                        isTapped: $isTapped,
                        animationNamespace: animationNamespace,
                        onTap: {
                            tabSelection = index
                            isTapped = true
                        }
                    )
                    Spacer()
                }
            }
            .frame(height: AppLayout.tabBarHeight)
            .padding(.horizontal, AppSpacing.screenHorizontalPadding)
            
        }
    }
}

// MARK: - Helper View
struct TabBarItemView: View {
    let image: String
    let title: String
    let isSelected: Bool
    @Binding var isTapped: Bool
    let animationNamespace: Namespace.ID
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            VStack(spacing: AppSpacing.formFieldSpacing) {
                if isSelected {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(Color.blue)
                        .frame(height: 6)
                        .matchedGeometryEffect(id: "SelectedTabId", in: animationNamespace)
                } else {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(Color.clear)
                        .frame(height: 6)
                }

                Spacer()
                VStack {
                    Image(systemName: image)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 20, height: 20)

                    Text(title)
                        .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular))
                        .padding(.bottom, AppSpacing.screenHorizontalPadding)
                }
                .scaleEffect(isSelected ? 1.2 : 1)
                .animation(.spring, value: isTapped)
            }
            .foregroundStyle(isSelected ? Color.blue : Color.gray)
            .padding(.bottom, AppSpacing.tabBarBottomPadding)
        }
        .accessibilityLabel(title)
        .accessibility(addTraits: .isButton)
    }
}

#Preview("CustomTabBarView") {
    CustomTabBarView(tabSelection: .constant(0))
        .background(Color.black)
}
