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
                .frame(height: 86)
                .frame(maxWidth: .infinity)
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
                            .font(AppFont.primary(size: AppFont.Size.body))
                            .fontWeight(AppFont.Weight.semibold)
                            .foregroundStyle(Color.yesterdaysDate)
                    }
            }
            
            Text("Task \n Completed")
                .font(AppFont.primary(size: AppFont.Size.caption))
                .fontWeight(AppFont.Weight.medium)
                .foregroundStyle(Color.yesterdayText)
                .multilineTextAlignment(.center)
        }
        .frame(maxWidth: .infinity)
    }
}

#Preview {
    TaskCompletedView(homeScreenViewModel: HomeScreenViewModel())
}
