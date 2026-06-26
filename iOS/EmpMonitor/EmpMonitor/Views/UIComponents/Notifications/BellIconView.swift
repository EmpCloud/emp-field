//
//  BellIconView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import SwiftUI

struct BellIconView: View {
    var body: some View {
        ZStack {
            Image(.bellIcon)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 14.12, height: 16.47)
                .background(
                    Circle()
                        .frame(width: 40, height: 40)
                        .foregroundStyle(.white)
                        .overlay(alignment: .topTrailing, content: {
                            Circle()
                                .fill(Color.notification)
                                .frame(width: 10, height: 10)
                                .offset(x: -1, y: 1)
                        })
                )
                .padding()
        }
//        .padding()
//        .background(Color.black)
    }
        
}

#Preview {
    BellIconView()
}
