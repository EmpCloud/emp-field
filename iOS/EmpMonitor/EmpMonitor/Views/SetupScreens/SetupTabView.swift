//
//  SetupTabView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI

struct SetupTabView: View {
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    @State private var selectedTab: Int = 0
    
    @State private var nextScreen: Bool = false
    
    
    var body: some View {
        
        TabView(selection: $selectedTab) {
            
	            ProductivityInsightsView()
	                .tag(0)
	                .tabItem {
	                    Text("Insight")
	                }
	            
	            LocationPermissionView()
	                .tag(1)
	                .tabItem {
	                    Text("Location")
	                }
            
            BackgroundPermissionView()
                .tag(2)
             
            ActivityPermissionView()
                .tag(3)
            
            Camera_GallaryPermissionView()
                .tag(4)
            
            VideoTutorialView()
                .tag(5)
            
            SetupSuccessView()
                .tag(6)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .tabViewStyle(PageTabViewStyle(indexDisplayMode: .always))
        .ignoresSafeArea(.all)
        .onAppear(perform: {
            setupAppearance()
        })
        
        PrimaryButton(text: selectedTab == 6 ? "Finish" : "Next") {
            if selectedTab == 6 {
                nextScreen = true
            }
            selectedTab += 1
        }
        .padding(.bottom)
        .padding(.horizontal, 50)
        .fullScreenCover(isPresented: $nextScreen, content: {
            LoginView()
                .environmentObject(profileImageLoader)
        })
        
    }
    //MARK: Tab View Appearance Setup
    func setupAppearance() {
        UIPageControl.appearance().currentPageIndicatorTintColor = UIColor(red: 156/255, green: 197/255, blue: 254/255, alpha: 1.0)
       
        UIPageControl.appearance().pageIndicatorTintColor = UIColor.black.withAlphaComponent(0.2)
    }
}
#Preview {
    SetupTabView()
}
