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
                .padding(.horizontal)
                .frame(height: 46)
                .frame(maxWidth: .infinity)
                .background(Color.white)
                .clipShape(RoundedRectangle(cornerRadius: 6))
                .font(.system(size: 14, weight: .medium))
                .accessibilityLabel(placeholder)
                .accessibilityHint("Enter \(placeholder.lowercased())")
        }
    }
}

#Preview {
    AddTaskTextField(text: .constant(""), placeholder: "Task Name")
}
