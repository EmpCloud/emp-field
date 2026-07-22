//
//  MobileTextField.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 19/06/24.
//

import SwiftUI

struct MobileTextField: View {

    @Binding var text: String
    var placeholder: String

    var body: some View {
        ZStack {
            TextField(placeholder, text: $text)
                .font(AppFont.primary(size: AppFont.Size.title3))
                .foregroundStyle(Color.mobileText)
                .padding(.leading, 70)
                .padding(.trailing, AppSpacing.lg)
                .frame(maxWidth: .infinity)
                .frame(minHeight: AppLayout.textFieldHeightLarge)
                .background(.white)
                .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
                .shadow(color: Color.textFieldShadow, radius: 19)
                .keyboardType(.phonePad)
        }
    }
}

#Preview {
    MobileTextField(text: .constant(""), placeholder: "Mobile Number")
}
