//
//  CreateTaskButton.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct CreateTaskButton: View {
    
    var text: String
    var action: () -> Void
    
    var body: some View {
        ZStack {
            Button(action: action) {
                Image(systemName: "plus")
                    .foregroundStyle(.white)
                    .font(AppFont.title)
                    .background(
                        Circle()
                            .fill(
                                //MARK: Inner Shadow
                                .shadow(.drop(color: Color.black.opacity(0.15), radius: 15, y: 4))
                                .shadow(.inner(color: Color.innerShadowCreateTaskButton, radius: 3))
                            )
                            .foregroundStyle(
                                LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                            )
                        //                        .shadow(color: Color.white.opacity(0.15), radius: 8)
                            .frame(width: 68, height: 68)
                    )
                    .padding(.bottom, 40)
                }
        }
    }
}

#Preview {
    CreateTaskButton(text: ""){
        AppLog.debug("Clicked")
    }
}
