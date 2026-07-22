//
//  AddPictureView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct AddPictureView: View {
    var fillsAvailableWidth: Bool = false

    var body: some View {
        RoundedRectangle(cornerRadius: 10)
            .fill(
                .shadow(.drop(color: Color.black.opacity(0.15), radius: 5, y: 2))
            )
            .foregroundStyle(Color.white)
            .frame(height: 76)
            .frame(maxWidth: fillsAvailableWidth ? .infinity : 200)
            .overlay {
                VStack(spacing: AppSpacing.stackSpacingSmall) {
                    Circle()
                        .fill(
                            .shadow(.inner(color: Color.white, radius: 4))
                        )
                        .foregroundStyle(Color.cameraBG)
                        .frame(width: 42, height: 42)
                        .overlay {
                            Image(systemName: "camera")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 17.88, height: 15.81)
                                .foregroundStyle(Color.white)
                        }
                    
                    Text("Add Picture")
                        .font(AppFont.primary(size: AppFont.Size.caption))
                        .fontWeight(AppFont.Weight.semibold)
                        .foregroundStyle(Color.subText)
                        .lineLimit(1)
                        .minimumScaleFactor(0.85)
                }
            }
    }
}

struct RescheduleTaskTime: View {
    var fillsAvailableWidth: Bool = false

    var body: some View {
        RoundedRectangle(cornerRadius: 10)
            .fill(
                .shadow(.drop(color: Color.black.opacity(0.15), radius: 5, y: 2))
            )
            .foregroundStyle(Color.white)
            .frame(height: 76)
            .frame(maxWidth: fillsAvailableWidth ? .infinity : 200)
            .overlay {
                VStack(spacing: AppSpacing.stackSpacingSmall) {
                    Circle()
                        .fill(
                            .shadow(.inner(color: Color.white, radius: 4))
                        )
                        .foregroundStyle(Color.resumeButton)
                        .frame(width: 42, height: 42)
                        .overlay {
                            Image(.calendarWhiteIcon)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 17.88, height: 15.81)
                                .foregroundStyle(Color.white)
                        }
                    
                    Text("Reschedule")
                        .font(AppFont.primary(size: AppFont.Size.caption))
                        .fontWeight(AppFont.Weight.semibold)
                        .foregroundStyle(Color.subText)
                        .lineLimit(1)
                        .minimumScaleFactor(0.85)
                }
            }
    }
}

#Preview {
    AddPictureView()
}

#Preview {
    RescheduleTaskTime()
}
