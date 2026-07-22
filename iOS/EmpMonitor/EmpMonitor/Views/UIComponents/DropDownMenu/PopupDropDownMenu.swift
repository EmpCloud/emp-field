//
//  PopupDropDownMenu.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 31/07/24.
//

import SwiftUI

struct PopupDropDownMenu: View {
    
    @Binding var selection: String?
    
    @Binding var showTypeLeave: Bool
    @Binding var showLeaveTypeLeave: Bool
    
    var options: [String]
    var selectionTitle: String
    
    var body: some View {
        Menu {
            ForEach(options, id: \.self) { option in
                Button(option) {
                    selection = option
                    showTypeLeave = false
                    showLeaveTypeLeave = false
                }
            }
        } label: {
            HStack(spacing: AppSpacing.iconTextSpacing) {
                Text((selection?.isEmpty == false ? selection : nil) ?? selectionTitle)
                    .font(AppFont.primary(size: AppFont.Size.caption))
                    .foregroundStyle(Color.addressText2)
                    .lineLimit(1)
                    .truncationMode(.tail)

                Spacer()

                Image(systemName: "chevron.down")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 10.17, height: 5.75)
                    .foregroundStyle(Color.addressText2)
            }
            .padding(.horizontal, AppSpacing.md)
            .frame(maxWidth: .infinity)
            .frame(minHeight: AppLayout.minimumTouchTarget)
            .background(Color.rectangleBG)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
        }
        .buttonStyle(.plain)
    }
}

#Preview {
    PopupDropDownMenu(selection: .constant(nil), showTypeLeave: .constant(false), showLeaveTypeLeave: .constant(false), options: ["first half", "second half", "full day"], selectionTitle: "First Half")
}
