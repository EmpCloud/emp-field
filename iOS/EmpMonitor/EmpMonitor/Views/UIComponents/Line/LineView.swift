//
//  LineView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 24/06/24.
//

import SwiftUI

struct LineView: View {
    var body: some View {
        ZStack {
            Rectangle()
                .frame(maxWidth: .infinity, maxHeight: 1)
                .foregroundStyle(Color.line)
        }
    }
}

#Preview {
    LineView()
}
