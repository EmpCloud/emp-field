//
//  SwiftUIView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct TextFieldButton: View {
    var placeholder: String
    var imageName: String
    var body: some View {
        RoundedRectangle(cornerRadius: 6)
            .fill(Color.white)
            .frame(height: 46)
            .overlay(alignment: .leading) {
                HStack{
                    Text(placeholder)
                        .font(AppFont.primary(size: AppFont.Size.caption))
                        .fontWeight(AppFont.Weight.medium)
                        .foregroundStyle(placeholder == "Add Client" ? Color.gray : Color.subText)
                        
                    
                    Spacer()
                    
                    Image(systemName: imageName)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 18.29, height: 18.53)
                        .foregroundStyle(Color.taskSearchBar)
                }
                .padding(.horizontal)
            }
        
    }
}

#Preview {
    TextFieldButton(placeholder: "Add Client", imageName: "chevron.right")
}
