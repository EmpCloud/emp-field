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
                .font(.custom("Montserrat", size: 10))
                .foregroundStyle(Color.addressText2)
                .padding()
                .frame(maxWidth: .infinity)
                .frame(height: 43)
                .background(Color.rectangleBG)
                .clipShape(RoundedRectangle(cornerRadius: 6))
                .fontWeight(.medium)
            
        }
    }
}

#Preview {
    RescheduleTextFieldView(text: .constant(""), placeholder: "Placeholder")
}
