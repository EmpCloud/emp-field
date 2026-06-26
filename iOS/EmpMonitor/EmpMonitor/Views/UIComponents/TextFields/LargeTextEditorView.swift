//
//  LargeTextEditorView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 14/08/24.
//

import SwiftUI

struct LargeTextEditorView: View {
    
    @Binding var descriptionText: String
    
    var body: some View {
        TextEditor(text: $descriptionText)
            .font(.custom("Montserrat", size: 10))
            .foregroundStyle(Color.addressText2)
            .scrollContentBackground(.hidden)
            .background(Color.rectangleBG)
            .padding(.top, 2)
            .padding(.horizontal, 7)
            .frame(maxWidth: .infinity)
            .frame(height: 68)
            .background(Color.rectangleBG)
            .overlay(alignment: .topLeading) {
                if descriptionText.isEmpty {
                    Text("Write reason here")
                        .font(.custom("Montserrat", size: 10))
                        .foregroundStyle(Color.addressText2)
                        .padding(10)
                }
            }
            .clipShape(RoundedRectangle(cornerRadius: 6))
    }
}

#Preview {
    LargeTextEditorView(descriptionText: .constant("Reason"))
}
