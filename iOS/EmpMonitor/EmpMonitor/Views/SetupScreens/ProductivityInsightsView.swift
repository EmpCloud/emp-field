//
//  ProductivityInsightsView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI

struct ProductivityInsightsView: View {
    var body: some View {
        ZStack(alignment: .top) {
            Color.white
                .ignoresSafeArea(.all)
            
            Image(.topSetupScreenBg)
            
            VStack {
                
                
                Image(.productivity)
                    .padding(.top, 150)
                HStack{
                    Text("Productivity")
                        .font(.custom("Montserrat", size: 25))
                        .fontWeight(.semibold)
                        .foregroundStyle(Color.welcomeText)
                    Text("Insights")
                        .font(.custom("Montserrat", size: 25))
                        .fontWeight(.semibold)
                        .foregroundStyle(Color.sideTitleText)
                }
                    .font(.largeTitle)
                    .padding(.top, 90)
                Text("One can monitor the productivicty of the user")
                    .font(.custom("Montserrat", size: 16))
                    .foregroundStyle(Color.subText)
                    .padding()
            }
            
        }
        .ignoresSafeArea(.all)
    }
}

#Preview {
    ProductivityInsightsView()
}
