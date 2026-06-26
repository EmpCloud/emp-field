//
//  TextFieldCreateProfileView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 24/06/24.
//

import SwiftUI

struct TextFieldCreateProfileView: View {
    
    @Binding var text: String
    @Binding var isEditable: Bool
    var placeholder: String
    var body: some View {
        ZStack {
            TextField(placeholder, text: .constant(text))
                .font(.custom("Montserrat", size: 12))
                .padding()
                .background(Color.createTextField)
                .clipShape(RoundedRectangle(cornerRadius: 6))
                .allowsHitTesting(isEditable)
        }
    }
}

struct TextFieldEditableCreateProfileView: View {
    
    @Binding var text: String
    var placeholder: String
    var body: some View {
        ZStack {
            TextField(placeholder, text: $text)
                .font(.custom("Montserrat", size: 12))
                .padding()
                .background(Color.createTextField)
                .clipShape(RoundedRectangle(cornerRadius: 6))
        }
    }
}

struct MobileTextFieldCreateProfileView: View {
    
    @Binding var text: String
    var placeholder: String
    var body: some View {
        ZStack {
            TextField(placeholder, text: $text)
                .font(.custom("Montserrat", size: 12))
                .padding()
                .padding(.leading, 70)
                .padding(.horizontal)
                .background(Color.createTextField)
                .clipShape(RoundedRectangle(cornerRadius: 6))
        }
    }
}

#Preview {
    TextFieldCreateProfileView(text: .constant("text"), isEditable: .constant(false), placeholder: "Enter Full name")
}
#Preview {
    TextFieldEditableCreateProfileView(text: .constant("text"), placeholder: "Enter Full name")
}
