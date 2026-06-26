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
                .font(.custom("Poppins-Regular", size: 18))
                .foregroundStyle(Color.mobileText)
                .padding()
                .padding(.leading, 70)
                .padding(.horizontal, 20)
                .frame(maxWidth: .infinity)
                .background(.white)
                .clipShape(RoundedRectangle(cornerRadius: 10))
                .shadow(color: Color.textFieldShadow, radius: 19)
                .keyboardType(.numberPad)
        }
    }
}

#Preview {
    MobileTextField(text: .constant(""), placeholder: "Mobile Number")
}
