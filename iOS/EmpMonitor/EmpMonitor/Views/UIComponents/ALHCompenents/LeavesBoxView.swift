//
//  LeavesBoxView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 15/07/24.
//

import SwiftUI

struct LeavesBoxView: View {
    var body: some View {
        RoundedRectangle(cornerRadius: 10)
            .fill(Color.leavesBG)
            .shadow(color: Color.black.opacity(0.1), radius: 4, x: 0, y: 1)
            .frame(width: 114, height: 114)
            .overlay {
                VStack(spacing: 10){
                    Circle()
                        .fill(
                            .shadow(.inner(color: Color.blueInnerShadow, radius: 7))
                        )
                        .foregroundStyle(Color.white)
                        .frame(width: 44, height: 44)
                        .overlay {
                            Image(.leave)
                                .accessibilityLabel("Leaves icon")
                        }
                    Text("Leaves")
                        .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.semibold))
                        .foregroundStyle(.white)
                    
                }.multilineTextAlignment(.center)
            }
            .accessibilityElement(children: .combine)
            .accessibilityLabel("Leaves")
    }
}

#Preview {
    LeavesBoxView()
}
