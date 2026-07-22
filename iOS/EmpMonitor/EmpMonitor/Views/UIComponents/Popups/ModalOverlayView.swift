//
//  ModalOverlayView.swift
//  EmpMonitor
//

import SwiftUI

struct ModalOverlayView<Content: View>: View {
    var backgroundOpacity: Double = 0.5
    var horizontalPadding: CGFloat = AppSpacing.md
    var verticalPadding: CGFloat = AppSpacing.md
    var isScrollable: Bool = true
    var dismissOnBackgroundTap: (() -> Void)?
    @ViewBuilder var content: () -> Content

    var body: some View {
        GeometryReader { proxy in
            let safeContentHeight = max(
                AppSpacing.zero,
                proxy.size.height
                - proxy.safeAreaInsets.top
                - proxy.safeAreaInsets.bottom
                - (verticalPadding * 2)
            )

            ZStack {
                Color.black.opacity(backgroundOpacity)
                    .ignoresSafeArea()
                    .contentShape(Rectangle())
                    .onTapGesture {
                        dismissOnBackgroundTap?()
                    }

                if isScrollable {
                    ScrollView {
                        VStack {
                            Spacer(minLength: AppSpacing.zero)
                            content()
                            Spacer(minLength: AppSpacing.zero)
                        }
                        .frame(maxWidth: .infinity)
                        .frame(minHeight: safeContentHeight)
                        .padding(.horizontal, horizontalPadding)
                        .padding(.top, proxy.safeAreaInsets.top + verticalPadding)
                        .padding(.bottom, proxy.safeAreaInsets.bottom + verticalPadding)
                    }
                    .scrollIndicators(.hidden)
                } else {
                    VStack {
                        Spacer(minLength: AppSpacing.zero)
                        content()
                        Spacer(minLength: AppSpacing.zero)
                    }
                    .frame(maxWidth: .infinity)
                    .frame(minHeight: safeContentHeight)
                    .padding(.horizontal, horizontalPadding)
                    .padding(.top, proxy.safeAreaInsets.top + verticalPadding)
                    .padding(.bottom, proxy.safeAreaInsets.bottom + verticalPadding)
                }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}
