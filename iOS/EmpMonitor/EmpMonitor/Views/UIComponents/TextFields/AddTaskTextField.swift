//
//  AddTaskTextField.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct AddTaskTextField: View {
    
    @Binding var text: String
    var placeholder: String
    var body: some View {
        ZStack {
            TextField(placeholder, text: $text)
                .padding(.horizontal, AppSpacing.md)
                .frame(minHeight: AppLayout.textFieldHeight)
                .frame(maxWidth: .infinity)
                .background(Color.white)
                .overlay {
                    RoundedRectangle(cornerRadius: AppRadius.small)
                        .stroke(Color.taskSearchBar.opacity(0.22), lineWidth: 1)
                }
                .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
                .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.medium))
                .accessibilityLabel(placeholder)
                .accessibilityHint("Enter \(placeholder.lowercased())")
        }
    }
}

#Preview {
    AddTaskTextField(text: .constant(""), placeholder: "Task Name")
}
