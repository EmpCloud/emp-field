//
//  NotificationTypeBoxView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 10/09/24.
//

import SwiftUI

struct NotificationTypeBoxView: View {
    
    var text: String
    var bgColor: Color
    
    var body: some View {
        HStack {
            Text(text)
                .font(AppFont.primary(size: AppFont.Size.nano))
                .fontWeight(AppFont.Weight.semibold)
                .foregroundStyle(Color.white)
        }
        .frame(width: 94, height: 23)
        .background(bgColor)
        .clipShape(Capsule())
    }
}

#Preview {
    NotificationTypeBoxView(text: "Continue Task", bgColor: Color.primaryButton1)
}
