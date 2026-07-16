//
//  QrDownloadButton.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 13/09/24.
//

import SwiftUI

struct QrDownloadButton: View {
    
    var action: () -> Void
    
    var body: some View {
        
        Button(action: action) {
            HStack {
                Image(.downloadBlueIcon)
                Text("Download QR")
            }
            .font(.custom("Montserrat", size: 14))
            .foregroundStyle(Color.primaryButton1)
            .fontWeight(.semibold)
            .frame(width: 195, height: 44)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: 4))
        }
    }
}

#Preview {
    QrDownloadButton() {
        AppLog.debug("Clicked")
    }
}
