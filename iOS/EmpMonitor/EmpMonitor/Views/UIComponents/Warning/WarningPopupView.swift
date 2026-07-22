//
//  WarningPopupView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 09/09/24.
//

import SwiftUI

struct WarningPopupView: View {
    
    var titleText: String
    var description: String
    
    @Binding var showWarningPopup: Bool
    
    var body: some View {
        VStack(spacing: AppSpacing.stackSpacingDefault) {
            HStack {
                Spacer()
                Button {
                    withAnimation {
                        showWarningPopup = false
                    }
                } label: {
                    Image(systemName: "xmark")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: AppLayout.closeIconSize, height: AppLayout.closeIconSize)
                        .fontWeight(AppFont.Weight.bold)
                        .foregroundStyle(Color.primaryButton1)
                        .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                        .contentShape(Rectangle())
                }
                .buttonStyle(.plain)
                .accessibilityLabel("Close alert")
            }
            .padding(.horizontal, AppSpacing.controlInnerPadding)
            
            Image(.warningPopupIcon)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 51.26, height: 46)
            
            HStack(alignment: .firstTextBaseline, spacing: AppSpacing.compactIconTextSpacing) {
                Text("Alert:")
                    .foregroundStyle(Color.warningTitle)
                Text(titleText)
                    .foregroundStyle(Color.text1)
            }
            .font(AppFont.primary(size: AppFont.Size.body))
            .fontWeight(AppFont.Weight.medium)
            .multilineTextAlignment(.center)
            .lineLimit(2)
            .fixedSize(horizontal: false, vertical: true)
            .frame(maxWidth: .infinity)
            
            Group {
                if description.count > 160 {
                    ScrollView {
                        warningDescriptionText
                    }
                    .frame(maxHeight: AppLayout.warningDescriptionMaxHeight)
                    .scrollIndicators(.hidden)
                } else {
                    warningDescriptionText
                }
            }
            
            //Button to dismiss
            Button{
                withAnimation {
                    showWarningPopup = false
                }
            }label: {
                Text("OK")
                    .font(AppFont.primary(size: AppFont.Size.callout))
                    .fontWeight(AppFont.Weight.medium)
                    .foregroundStyle(Color.white)
                    .frame(minWidth: 117, minHeight: AppLayout.minimumTouchTarget)
                .background(Color.notification)
                .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
            }
            .buttonStyle(.plain)
            .accessibilityLabel("OK")
            .padding(.top, AppSpacing.xxs)
            
        }
        .padding(.horizontal, AppSpacing.modalHorizontalPadding)
        .padding(.bottom, AppSpacing.modalBottomPadding)
        .frame(maxWidth: AppLayout.popupMaxWidth, alignment: .top)
        .background(Color.white)
//        .border(Color.black)
        .clipShape(RoundedRectangle(cornerRadius: AppRadius.large))
        
    }

    private var warningDescriptionText: some View {
        Text(description)
            .font(AppFont.primary(size: AppFont.Size.callout))
            .fontWeight(AppFont.Weight.medium)
            .foregroundStyle(Color.addressText2)
            .multilineTextAlignment(.center)
            .fixedSize(horizontal: false, vertical: true)
            .frame(maxWidth: .infinity)
    }
}

#Preview {
    WarningPopupView(titleText: "Battery Drain", description: "Please plug in your device to avoid shutdown.", showWarningPopup: .constant(false))
}
