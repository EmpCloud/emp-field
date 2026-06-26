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
        ZStack {
            RoundedRectangle(cornerRadius: 6)
                .fill(Color.createTextField)
                .frame(height: 37)
                .clipShape(RoundedRectangle(cornerRadius: 6))
                .overlay(alignment: .leading) {
                    HStack {
                        Text(placeholder)
                            .font(.custom("Montserrat", size: 12))
                        
                        Spacer()
                        
                        Image(.editAddressTextFieldIcon)
                            .onTapGesture {
                                showClientAddress.toggle()
                            }
                    }
                    .padding(.horizontal)
                }
        }
    }
}

#Preview {
    EditAddressTextField(showClientAddress: .constant(false), placeholder: "Bidadi, Bengaluru, India")
}
