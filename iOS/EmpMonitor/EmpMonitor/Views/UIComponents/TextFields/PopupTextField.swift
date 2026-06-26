//
//  PopupTextField.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 31/07/24.
//

import SwiftUI

struct PopupTextField: View {
    
    @Binding var text: String
    
    var placeholder: String
    
    var body: some View {
        ZStack {
            TextField(placeholder, text: $text)
                .font(.custom("Montserrat", size: 10))
                .foregroundStyle(Color.addressText2)
                .padding(12)
                .frame(maxWidth: .infinity)
                .background(Color.rectangleBG)
                .clipShape(RoundedRectangle(cornerRadius: 6))
                .allowsHitTesting(false)
                
        }
    }
}

struct PopupLargeTextField: View {
    
    @Binding var text: String
    
    var placeholder: String
    
    var body: some View {
        ZStack {
            TextField(placeholder, text: $text)
                .font(.custom("Montserrat", size: 10))
                .foregroundStyle(Color.addressText2)
                .padding()
                .frame(maxWidth: .infinity)
                .frame(height: 49)
                .background(Color.rectangleBG)
                .clipShape(RoundedRectangle(cornerRadius: 6))
            
        }
    }
}

#Preview {
    PopupTextField(text: .constant(""), placeholder: "Write Name")
}

#Preview {
    PopupLargeTextField(text: .constant(""), placeholder: "Write Name")
}
