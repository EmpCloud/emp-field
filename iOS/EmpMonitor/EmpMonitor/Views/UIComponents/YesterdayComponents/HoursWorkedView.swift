//
//  HoursWorkedView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 15/07/24.
//

import SwiftUI

struct HoursWorkedView: View {
    
    @ObservedObject var homeScreenViewModel: HomeScreenViewModel
    
    var body: some View {
        VStack {
            RoundedRectangle(cornerRadius: 10)
                .fill(Color.rectangleBG)
                .frame(width: 106 ,height: 86)
                .overlay {
                    VStack{
                        Circle()
                            .fill(
                                .shadow(.inner(color: Color.blueInnerShadow, radius: 7))
                            )
                            .foregroundStyle(.white)
                            .frame(width: 42, height: 43)
                            .overlay {
                                Image(.clock)
                            }
                        Text(homeScreenViewModel.homeScreenData?.yesterdayHrs ?? "00:00:00")
                            .font(.custom("Montserrat", size: 14))
                            .fontWeight(.semibold)
                            .foregroundStyle(Color.yesterdaysDate)
                    }
            }
            
            Text("Hrs worked")
                .font(.custom("Montserrat", size: 12))
                .fontWeight(.medium)
                .foregroundStyle(Color.yesterdayText)
        }
    }
}

#Preview {
    HoursWorkedView(homeScreenViewModel: HomeScreenViewModel())
}
