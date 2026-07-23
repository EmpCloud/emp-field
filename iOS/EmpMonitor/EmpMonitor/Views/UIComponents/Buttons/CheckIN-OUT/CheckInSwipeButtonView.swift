//
//  CheckInButtonView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import SwiftUI

extension CheckInSwipeButtonView {
    func onSwipeSuccess(_ action: @escaping () -> Void) -> Self {
        var this = self
        this.actionSuccess = action
        return this
    }
}

struct CheckInSwipeButtonView: View {
    
    static let shared = CheckInSwipeButtonView()
    
    //to check weather the user in Check-IN or not
    @State var isCheckIn: Bool = false
    
    // Tracks right drag from the resting left-side position.
    @State private var dragOffsetX: CGFloat = 0
    
    //to keep track when enough is dragged to be considered as checkIN
    @State private var isEnoughSwipped: Bool = false
    
    //Actions
    private var actionSuccess: (() -> Void)?
    
    //the track does not change size
    let trackSize = CGSize.trackSize

    private var thumbRestingX: CGFloat {
        -(trackSize.width - AppLayout.swipeThumbVisualSize) / 2
    }

    private var maxDragDistance: CGFloat {
        trackSize.width - AppLayout.swipeThumbVisualSize
    }

    private var successThreshold: CGFloat {
        maxDragDistance * 0.65
    }
    
    init() {
        
    }
    
    var body: some View {
        ZStack {
            //swipe track
            Capsule()
                .frame(width: trackSize.width, height: trackSize.height)
                .foregroundStyle(Color.swipeBG)
            
            Text("Swipe to Check In")
                .font(AppFont.primary(size: AppFont.Size.callout))
                .fontWeight(AppFont.Weight.medium)
                .offset(x: 10.0)
                .lineLimit(1)
                .minimumScaleFactor(0.85)
            
            ZStack {
                Circle()
                    .fill(
                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                    )
                    .frame(width: AppLayout.swipeThumbVisualSize, height: AppLayout.swipeThumbVisualSize)
                
                Image(systemName: "arrow.right")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: AppLayout.iconGlyphSmall.width, height: AppLayout.iconGlyphSmall.height)
                    .foregroundStyle(Color.white)
                
            }
            .offset(x: thumbRestingX + dragOffsetX, y: 0)
            .contentShape(Circle())
        }
        .frame(width: trackSize.width, height: trackSize.height)
        .contentShape(Capsule())
        .highPriorityGesture(
            DragGesture(minimumDistance: 8)
                .onChanged { value in
                    self.handleDragChanged(value)
                }
                .onEnded { _ in
                    self.handleDragEnded()
                }
        )
        .accessibilityLabel("Swipe to check in")
        .accessibilityHint("Drag the handle to the right to check in")
    }
    
    //MARK: Helper Function
    
    //MARK: Gesture Handlers
    private func handleDragChanged(_ value: DragGesture.Value) -> Void {
        let nextOffset = value.translation.width.clamp(lower: 0, maxDragDistance)
        dragOffsetX = nextOffset
        isEnoughSwipped = nextOffset >= successThreshold
    }
    
    private func handleDragEnded() -> Void {
        //if enough was swipped => Completely swipped
        if self.isEnoughSwipped {
            withAnimation(.spring()) {
                self.dragOffsetX = maxDragDistance
            }
//            isCheckIn = true
            
            //checked In
            if nil != self.actionSuccess {
                //wait and give enough time for animation to finish
                
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                    self.actionSuccess!()
//                    isCheckIn = true
                    dragOffsetX = 0 // after checkIN the arrow should come to initial position
                }
            }
        }
        else {
            withAnimation(.spring()) {
                self.dragOffsetX = 0
            }
        }
    }
}

#Preview {
    CheckInSwipeButtonView()
}
