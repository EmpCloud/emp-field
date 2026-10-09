//
//  LargeTextEditorView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 14/08/24.
//

import SwiftUI

struct LargeTextEditorView: View {
    
    @Binding var descriptionText: String
    
    // TextEditor insets its text by 5pt at the sides and 8pt at the top. Account for both so
    // the typed text and the placeholder sit at the same 16pt inset as the other popup fields.
    private let editorSideInset: CGFloat = 5
    private let editorTopInset: CGFloat = 8
    
    var body: some View {
        TextEditor(text: $descriptionText)
            .font(AppFont.primary(size: AppFont.Size.xSmall))
            .foregroundStyle(Color.addressText2)
            .scrollContentBackground(.hidden)
            .background(Color.rectangleBG)
            .padding(.top, AppSpacing.xxs)
            .padding(.horizontal, AppSpacing.md - editorSideInset)
            .frame(maxWidth: .infinity)
            .frame(height: 68)
            .background(Color.rectangleBG)
            .overlay(alignment: .topLeading) {
                if descriptionText.isEmpty {
                    Text("Write reason here")
                        .font(AppFont.primary(size: AppFont.Size.xSmall))
                        .foregroundStyle(Color.addressText2)
                        .padding(.top, AppSpacing.xxs + editorTopInset)
                        .padding(.horizontal, AppSpacing.md)
                        .allowsHitTesting(false)
                }
            }
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
    }
}

#Preview {
    LargeTextEditorView(descriptionText: .constant("Reason"))
}
