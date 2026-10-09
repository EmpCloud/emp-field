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
                .lineLimit(1)
        }
        .padding(.horizontal, AppSpacing.sm)
        .frame(minWidth: 94, minHeight: 23)
        .background(bgColor)
        .clipShape(Capsule())
    }
}

#Preview {
    NotificationTypeBoxView(text: "Continue Task", bgColor: Color.primaryButton1)
}
