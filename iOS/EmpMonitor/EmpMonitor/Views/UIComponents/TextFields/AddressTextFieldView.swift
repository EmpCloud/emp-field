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
                .padding(.horizontal)
                .frame(height: 31.3)
                .frame(maxWidth: .infinity)
                .background(Color.white)
                .clipShape(RoundedRectangle(cornerRadius: 6))
                .font(.custom("Montserrat", size: 12))
                .fontWeight(.medium)
        }
    }
}

#Preview {
    AddressTextFieldView(text: .constant("address"), placeholder: "Enter something")
}
