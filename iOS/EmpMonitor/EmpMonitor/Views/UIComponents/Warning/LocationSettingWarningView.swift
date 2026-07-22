//
//  LocationSettingWarningView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 01/10/24.
//

import SwiftUI

struct LocationSettingWarningView: View {
    
    var titleText: String
    var description: String
    
    @Binding var showWarningPopup: Bool
    
    var body: some View {
        VStack(spacing: AppSpacing.stackSpacingDefault) {
            HStack(alignment: .center) {
                Spacer()

                Button {
                    withAnimation {
                        showWarningPopup = false
                    }
                } label: {
                    Image(systemName: "xmark")
                        .font(AppFont.primary(size: AppFont.Size.closeIcon, weight: AppFont.Weight.bold))
                        .foregroundStyle(Color.primaryButton1)
                        .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                }
                .buttonStyle(.plain)
                .accessibilityLabel("Close warning")
            }
            .frame(height: AppLayout.minimumTouchTarget)
            
            Image(.warningPopupIcon)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 51.26, height: 46)
            
            HStack(spacing: AppSpacing.xs) {
                Text("Alert:")
                    .foregroundStyle(Color.warningTitle)
                Text(titleText)
                    .foregroundStyle(Color.text1)
                    .lineLimit(2)
                    .multilineTextAlignment(.center)
            }
            .font(AppFont.primary(size: AppFont.Size.caption))
            .fontWeight(AppFont.Weight.medium)
            
            ScrollView(showsIndicators: false) {
                Text(description)
                    .font(AppFont.primary(size: AppFont.Size.xSmall))
                    .fontWeight(AppFont.Weight.medium)
                    .foregroundStyle(Color.addressText2)
                    .multilineTextAlignment(.center)
                    .fixedSize(horizontal: false, vertical: true)
                    .frame(maxWidth: .infinity)
            }
            .frame(maxHeight: AppLayout.warningDescriptionMaxHeight)
            
            PrimaryBorderButton(text: "Settings") {
                //open settings
                HelperFunction.shared.openAppSetting()
            }
        }
        .padding(.horizontal, AppSpacing.lg)
        .padding(.bottom, AppSpacing.lg)
        .frame(maxWidth: AppLayout.popupMaxWidth, alignment: .top)
        .background(Color.white)
//        .border(Color.black)
        .clipShape(RoundedRectangle(cornerRadius: AppRadius.large))
        
    }
}

#Preview {
    LocationSettingWarningView(titleText: "", description: "", showWarningPopup: .constant(false))
}
