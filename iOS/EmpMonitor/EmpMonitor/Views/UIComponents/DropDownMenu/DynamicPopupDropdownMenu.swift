//
//  DynamicPopupDropdownMenu.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 14/08/24.
//

import SwiftUI

struct DynamicPopupDropdownMenu: View {
    
    @Binding var selection: LeavesTypeResponseDetail?
    
    @Binding var showTypeLeave: Bool
    @Binding var showDayTypeLeave: Bool
    
    var options: [LeavesTypeResponseDetail]
    var selectionTitle: String
    
    var body: some View {
        Menu {
            if options.isEmpty {
                Text("No leave types")
            } else {
                ForEach(options, id: \.id) { option in
                    Button(option.name) {
                        selection = option
                        showTypeLeave = false
                        showDayTypeLeave = false
                    }
                }
            }
        } label: {
            HStack(spacing: AppSpacing.iconTextSpacing) {
                Text(selection?.name ?? selectionTitle)
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
        .disabled(options.isEmpty)
    }
}

//
//#Preview {
//    DynamicPopupDropdownMenu(selection: .constant(nil), showTypeLeave: .constant(false), options: [LeavesTypeResponseDetail.Type], selectionTitle: "Leave Type")
//}
