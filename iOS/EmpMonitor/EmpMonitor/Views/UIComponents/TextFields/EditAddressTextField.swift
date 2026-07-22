//
//  EditAddressTextField.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/08/24.
//

import SwiftUI

struct EditAddressTextField: View {
    
    @Binding var showClientAddress: Bool
    
    var placeholder: String
    
    var body: some View {
        Button {
            showClientAddress.toggle()
        } label: {
            HStack(spacing: AppSpacing.iconTextSpacing) {
                Text(placeholder)
                    .font(AppFont.primary(size: AppFont.Size.caption))
                    .lineLimit(1)
                    .minimumScaleFactor(0.8)

                Spacer(minLength: AppSpacing.sm)

                Image(.editAddressTextFieldIcon)
            }
            .foregroundStyle(Color.text1)
            .padding(.horizontal, AppSpacing.md)
            .frame(maxWidth: .infinity)
            .frame(minHeight: AppLayout.minimumTouchTarget)
            .background(Color.createTextField)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
        }
        .buttonStyle(.plain)
        .accessibilityLabel("Edit address")
    }
}

#Preview {
    EditAddressTextField(showClientAddress: .constant(false), placeholder: "Bidadi, Bengaluru, India")
}
