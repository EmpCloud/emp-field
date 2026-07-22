//
//  LiveTimeView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/08/24.
//

import SwiftUI

struct LiveTimeView: View {
    var body: some View {
        TimelineView(.periodic(from: .now, by: 60)) { context in
            Text(Self.formattedTime(from: context.date))
        }
            .font(AppFont.primary(size: AppFont.Size.navigationTitle)) // You can adjust the font size and style as needed
            .foregroundStyle(Color.welcomeText)
    }

    private static func formattedTime(from date: Date) -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "hh:mma"
        formatter.amSymbol = "am"
        formatter.pmSymbol = "pm"
        return formatter.string(from: date).lowercased()
    }
}

#Preview {
    LiveTimeView()
}
