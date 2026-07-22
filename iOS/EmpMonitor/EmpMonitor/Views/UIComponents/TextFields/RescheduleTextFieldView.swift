//
//  RescheduleTextFieldView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/09/24.
//

import SwiftUI

struct RescheduleTextFieldView: View {
    
    @Binding var text: String
    
    var placeholder: String
    
    var body: some View {
        ZStack {
            TextField(placeholder, text: $text)
                .font(AppFont.primary(size: AppFont.Size.xSmall))
                .foregroundStyle(Color.addressText2)
                .padding(.horizontal, AppSpacing.md)
                .frame(maxWidth: .infinity)
                .frame(minHeight: AppLayout.minimumTouchTarget)
                .background(Color.rectangleBG)
                .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
                .fontWeight(AppFont.Weight.medium)
            
        }
    }
}

#Preview {
    RescheduleTextFieldView(text: .constant(""), placeholder: "Placeholder")
}
