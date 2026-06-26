//
//  EditButton.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 03/09/24.
//

import SwiftUI

struct EditButton: View {
    var action: () -> Void
    var body: some View {
        Button(action: action) {
            HStack(spacing: 8) {
                Image(.editWhiteIcon)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 16, height: 16)
                    .foregroundStyle(Color.white)

                Text("Edit")
                    .font(.system(size: 12, weight: .semibold))
                    .foregroundStyle(Color.white)
            }
            .frame(height: 44)
            .frame(maxWidth: .infinity, alignment: .center)
            .background(Color.profileDarkBg)
            .cornerRadius(6)
        }
        .accessibilityLabel("Edit")
        .accessibilityHint("Double tap to edit this item")
    }
}

#Preview {
    EditButton(){
        print("Clicked")
    }
}
