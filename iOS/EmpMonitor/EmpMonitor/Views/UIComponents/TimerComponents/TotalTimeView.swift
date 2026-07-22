//
//  TotalTimeView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import SwiftUI

struct TotalTimeView: View {
    
    @EnvironmentObject var timerManager: TimerManager
    
    var body: some View {
        RoundedRectangle(cornerRadius: 5)
            .fill(Color.notification)
            .frame(width: 73 , height: 27)
            .overlay {
                Text("\(timerManager.timeString(from: timerManager.activeTime))")
                    .font(AppFont.primary(size: AppFont.Size.body))
                    .fontWeight(AppFont.Weight.medium)
                    .foregroundStyle(Color.white)
            }
    }
}

#Preview {
    TotalTimeView()
        .environmentObject(TimerManager())
}
