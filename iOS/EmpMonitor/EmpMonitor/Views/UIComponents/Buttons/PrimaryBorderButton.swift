//
//  PrimaryBorderButton.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 31/07/24.
//

import SwiftUI

struct PrimaryBorderButton: View {
    
    var text: String
    var action: () -> Void
    
    var body: some View {
        ZStack {
            Button(action: action) {
                Text(text)
                    .font(AppFont.primary(size: AppFont.Size.subheadline))
                    .foregroundStyle(Color.attendanceTitleText)
                    .lineLimit(1)
                    .minimumScaleFactor(0.85)
                    .padding(AppSpacing.controlInnerPadding)
                    .frame(maxWidth: .infinity)
                    .frame(minHeight: AppLayout.buttonHeight)
                    .overlay {
                        RoundedRectangle(cornerRadius: AppRadius.medium)
                            .stroke(lineWidth: 2)
                    }
                    .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
            }
        }
//        .padding()
    }
}

struct RedBorderButton: View {
    
    var text: String
    var action: () -> Void
    
    var body: some View {
        ZStack {
            Button(action: action) {
                HStack {
                    Image(systemName: "pause")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: AppLayout.iconGlyphSmall.width, height: AppLayout.iconGlyphSmall.height)
                        .fontWeight(AppFont.Weight.bold)
                        .foregroundStyle(Color.absent)
                    
                    Text(text)
                        .font(AppFont.primary(size: AppFont.Size.subheadline))
                        .foregroundStyle(Color.absent)
                        .lineLimit(1)
                        .minimumScaleFactor(0.85)
                        .padding(AppSpacing.controlInnerPadding)
                       
                }
                
                .frame(maxWidth: .infinity)
                .frame(minHeight: AppLayout.buttonHeight)
                .overlay {
                    RoundedRectangle(cornerRadius: AppRadius.medium)
                        .stroke(Color.absent, lineWidth: 2)
                }
                .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
            }
        }
//        .padding()
    }
}


#Preview {
    PrimaryBorderButton(text: "Primary border button") {
        AppLog.debug(("Print"))
    }
}

#Preview {
    RedBorderButton(text: "Primary border button") {
        AppLog.debug(("Print"))
    }
}
