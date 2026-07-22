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
                .font(AppFont.primary(size: AppFont.Size.xSmall))
                .foregroundStyle(Color.addressText2)
                .padding(.horizontal, AppSpacing.md)
                .frame(maxWidth: .infinity)
                .frame(minHeight: AppLayout.minimumTouchTarget)
                .background(Color.rectangleBG)
                .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
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
                .font(AppFont.primary(size: AppFont.Size.xSmall))
                .foregroundStyle(Color.addressText2)
                .padding(.horizontal, AppSpacing.md)
                .frame(maxWidth: .infinity)
                .frame(minHeight: AppLayout.textFieldHeightLarge)
                .background(Color.rectangleBG)
                .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
            
        }
    }
}

#Preview {
    PopupTextField(text: .constant(""), placeholder: "Write Name")
}

#Preview {
    PopupLargeTextField(text: .constant(""), placeholder: "Write Name")
}
