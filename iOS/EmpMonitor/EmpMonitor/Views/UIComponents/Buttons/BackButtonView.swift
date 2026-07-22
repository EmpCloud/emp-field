//
//  BackButtonView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 26/06/24.
//

import SwiftUI

struct BackButtonView: View {
    var body: some View {
        Circle()
            .fill(Color.white)
            .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
            .overlay {
                Image(systemName: "arrow.backward")
                    .foregroundStyle(Color.primaryButton1)
            }
    }
}

struct BackButtonDarkView: View {
    var body: some View {
        Circle()
            .fill(Color.primaryButton1)
            .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
            .overlay {
                Image(systemName: "arrow.backward")
                    .foregroundStyle(Color.white)
            }
    }
}

#Preview {
    Group{
        BackButtonView()
        BackButtonDarkView()
    }
}
