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
            .font(AppFont.primary(size: AppFont.Size.xSmall))
            .foregroundStyle(Color.addressText2)
            .scrollContentBackground(.hidden)
            .background(Color.rectangleBG)
            .padding(.top, AppSpacing.xxs)
            .padding(.horizontal, AppSpacing.sm)
            .frame(maxWidth: .infinity)
            .frame(height: 68)
            .background(Color.rectangleBG)
            .overlay(alignment: .topLeading) {
                if descriptionText.isEmpty {
                    Text("Write reason here")
                        .font(AppFont.primary(size: AppFont.Size.xSmall))
                        .foregroundStyle(Color.addressText2)
                        .padding(AppSpacing.sm)
                        .allowsHitTesting(false)
                }
            }
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
    }
}

#Preview {
    LargeTextEditorView(descriptionText: .constant("Reason"))
}
