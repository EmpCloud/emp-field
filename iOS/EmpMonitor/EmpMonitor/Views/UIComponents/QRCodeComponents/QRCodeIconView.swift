//
//  QRCodeIconView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 13/09/24.
//

import SwiftUI

struct QRCodeIconView: View {
    var body: some View {
        ZStack {
            Image(.qrCodeIcon)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 18.82, height: 18.82)
                .background(
                    Circle()
                        .frame(width: 40, height: 40)
                        .foregroundStyle(.white)
                )
                .padding()
        }
    }
        
}

#Preview {
    QRCodeIconView()
        .background(Color.black)
}
