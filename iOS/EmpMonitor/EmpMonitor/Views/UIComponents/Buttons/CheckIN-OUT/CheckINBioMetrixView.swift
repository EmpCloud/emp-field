//
//  CheckINBioMetrixView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/08/24.
//

import SwiftUI

extension CheckINBioMetrixView {
    func onTap(_ action: @escaping () -> Void) -> Self {
        var copy = self
        copy.tapAction = action
        return copy
    }
}

struct CheckINBioMetrixView: View {
    var tapAction: (() -> Void)?

    var body: some View {
        Button {
            tapAction?()
        } label: {
            ZStack {
                Capsule()
                    .frame(width: 220, height: 42)
                    .foregroundStyle(Color.white)
                    .overlay {
                        Capsule().stroke(Color.primaryButton1, lineWidth: 1)
                    }

                Text("Check IN via. Biometric")
                    .font(.custom("Montserrat", size: 12))
                    .foregroundStyle(Color.primaryButton1)
                    .fontWeight(.medium)
                    .offset(x: 12.0)

                ZStack {
                    Circle()
                        .fill(
                            LinearGradient(
                                gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]),
                                startPoint: .top,
                                endPoint: .bottom
                            )
                        )
                        .frame(width: 28, height: 28)

                    Image(.bioCheckINIcon)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 13.31, height: 17.66)
                        .foregroundStyle(Color.white)
                }
                .offset(x: -88, y: 0)
            }
        }
        .buttonStyle(.plain)
    }
}

#Preview {
    CheckINBioMetrixView()
}
