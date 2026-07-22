//
//  AddressTextFieldView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 24/06/24.
//

import SwiftUI

struct AddressTextFieldView: View {
    
    @Binding var text: String
    var placeholder: String
    var body: some View {
        ZStack {
            TextField(placeholder, text: $text)
                .padding(.horizontal, AppSpacing.md)
                .frame(minHeight: AppLayout.minimumTouchTarget)
                .frame(maxWidth: .infinity)
                .background(Color.white)
                .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
                .font(AppFont.primary(size: AppFont.Size.caption))
                .fontWeight(AppFont.Weight.medium)
        }
    }
}

#Preview {
    AddressTextFieldView(text: .constant("address"), placeholder: "Enter something")
}
