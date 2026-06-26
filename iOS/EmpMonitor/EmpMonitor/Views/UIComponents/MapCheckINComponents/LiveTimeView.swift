//
//  LiveTimeView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/08/24.
//

import SwiftUI

struct LiveTimeView: View {
    @State private var currentTime: String = ""

    var body: some View {
        Text(currentTime)
            .font(.custom("Ubuntu-Regular", size: 20)) // You can adjust the font size and style as needed
            .foregroundStyle(Color.welcomeText)
            .onAppear {
                updateTime()
                startTimer()
            }
    }

    private func updateTime() {
        let formatter = DateFormatter()
        formatter.dateFormat = "hh:mma"
        formatter.amSymbol = "am"
        formatter.pmSymbol = "pm"
        
        let now = Date()
        currentTime = formatter.string(from: now).lowercased()
    }

    private func startTimer() {
        Timer.scheduledTimer(withTimeInterval: 60, repeats: true) { _ in
            updateTime()
        }
    }
}

#Preview {
    LiveTimeView()
}
