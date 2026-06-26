//
//  BackButtonView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 26/06/24.
//

import SwiftUI

struct BackButtonView: View {
    var body: some View {
        ZStack {
            Circle()
                .fill(Color.white)
                .frame(width: 40, height: 40)
                .overlay {
                    Image(systemName: "arrow.backward")
                        .foregroundStyle(Color.primaryButton1)
                }
        }
    }
}

struct BackButtonDarkView: View {
    var body: some View {
        ZStack {
            Circle()
                .fill(Color.primaryButton1)
                .frame(width: 40, height: 40)
                .overlay {
                    Image(systemName: "arrow.backward")
                        .foregroundStyle(Color.white)
                }
        }
    }
}

#Preview {
    Group{
        BackButtonView()
        BackButtonDarkView()
    }
}
