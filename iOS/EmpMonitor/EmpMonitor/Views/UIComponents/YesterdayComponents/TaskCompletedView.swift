//
//  TaskCompletedView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 15/07/24.
//

import SwiftUI

struct TaskCompletedView: View {
    
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
                                Image(.completedTask)
                            }
                        Text(homeScreenViewModel.homeScreenData?.yesterdaytask ?? "--/--")
                            .font(.custom("Montserrat", size: 14))
                            .fontWeight(.semibold)
                            .foregroundStyle(Color.yesterdaysDate)
                    }
            }
            
            Text("Task \n Completed")
                .font(.custom("Montserrat", size: 12))
                .fontWeight(.medium)
                .foregroundStyle(Color.yesterdayText)
                .multilineTextAlignment(.center)
        }
    }
}

#Preview {
    TaskCompletedView(homeScreenViewModel: HomeScreenViewModel())
}
