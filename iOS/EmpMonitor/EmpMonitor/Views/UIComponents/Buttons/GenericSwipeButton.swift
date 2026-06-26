//
//  GenericSwipeButton.swift
//  EmpMonitor
//
//  Unified swipe-to-action button component
//  Replaces: CheckInSwipeButtonView, CheckOutSwipeButtonView
//

import SwiftUI

enum SwipeDirection {
    case left, right
}

struct GenericSwipeButton: View {
    let direction: SwipeDirection
    let text: String
    let bgColor: Color
    let textColor: Color
    let onSwipeSuccess: () -> Void

    @State private var offset: CGFloat = 0
    @State private var isDragging: Bool = false
    private let swipeThreshold: CGFloat = 80

    var body: some View {
        ZStack(alignment: direction == .right ? .leading : .trailing) {
            RoundedRectangle(cornerRadius: 15)
                .fill(bgColor)
                .frame(height: 56)

            HStack(spacing: 12) {
                if direction == .right {
                    Image(systemName: "arrow.right")
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundStyle(textColor)
                }

                Text(text)
                    .font(.system(size: 14, weight: .semibold))
                    .foregroundStyle(textColor)

                if direction == .left {
                    Image(systemName: "arrow.left")
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundStyle(textColor)
                }
            }
            .padding(.horizontal, 20)
            .offset(x: direction == .right ? max(0, offset) : min(0, offset))
        }
        .gesture(
            DragGesture()
                .onChanged { value in
                    isDragging = true
                    let translation = value.translation.width
                    offset = direction == .right ? translation : -translation

                    if abs(offset) > swipeThreshold {
                        onSwipeSuccess()
                        offset = 0
                    }
                }
                .onEnded { _ in
                    isDragging = false
                    withAnimation(.spring) {
                        offset = 0
                    }
                }
        )
        .accessibilityLabel(text)
        .accessibilityHint("Swipe \(direction == .right ? "right" : "left") to confirm")
    }
}

#Preview {
    VStack(spacing: 20) {
        GenericSwipeButton(
            direction: .right,
            text: "Swipe to Check In",
            bgColor: Color.green.opacity(0.7),
            textColor: .white,
            onSwipeSuccess: {}
        )

        GenericSwipeButton(
            direction: .left,
            text: "Swipe to Check Out",
            bgColor: Color.red.opacity(0.7),
            textColor: .white,
            onSwipeSuccess: {}
        )
    }
    .padding()
}
