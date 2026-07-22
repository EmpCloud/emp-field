//
//  CheckOutSwipeButtonView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import SwiftUI

extension CheckOutSwipeButtonView {
    func onSwipeSuccess(_ action: @escaping () -> Void) -> Self {
        var this = self
        this.actionSuccess = action
        return this
    }
}


struct CheckOutSwipeButtonView: View {
    
    static let shared = CheckOutSwipeButtonView()
    
    //to check whether the user is CheckOUT or not
    @State var isCheckOUT: Bool = false
    
    // Tracks left drag from the resting right-side position.
    @State private var dragOffsetX: CGFloat = 0
    
    // to keep track when enouhg is dragged to be considered as CheckedOUT
    @State private var isEnoughSwipped: Bool = false
    
    //Actions
    private var actionSuccess: (() -> Void)?
    
    // the track does not change size
    let trackSize = CGSize.trackSize

    private var thumbRestingX: CGFloat {
        (trackSize.width - AppLayout.swipeThumbVisualSize) / 2
    }

    private var maxDragDistance: CGFloat {
        trackSize.width - AppLayout.swipeThumbVisualSize
    }

    private var successThreshold: CGFloat {
        maxDragDistance * 0.65
    }
    
    init() {}
    
    
    var body: some View {
        ZStack {
            // swipe track
            Capsule()
                .frame(width: trackSize.width, height: trackSize.height)
                .foregroundStyle(
                    LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                )
            
            Text("Swipe to Check Out")
                .font(AppFont.primary(size: AppFont.Size.callout))
                .foregroundStyle(Color.white)
                .offset(x: -15)
                .lineLimit(1)
                .minimumScaleFactor(0.85)
            
            
            ZStack {
                Circle()
                    .frame(width: AppLayout.swipeThumbVisualSize, height: AppLayout.swipeThumbVisualSize)
                    .foregroundStyle(Color.white)
                
                Image(systemName: "arrow.left")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: AppLayout.iconGlyphSmall.width, height: AppLayout.iconGlyphSmall.height)
                    .foregroundStyle(
                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                    )
            }
            .offset(x: thumbRestingX + dragOffsetX)
            .contentShape(Circle())
        }
        .frame(width: trackSize.width, height: trackSize.height)
        .contentShape(Capsule())
        .gesture(
            DragGesture(minimumDistance: 8)
                .onChanged { value in
                    self.handleDragChanged(value)
                }
                .onEnded { _ in
                    self.handleDragEnded()
                }
        )
        .accessibilityLabel("Swipe to check out")
        .accessibilityHint("Drag the handle to the left to check out")
    }
    
    // MARK: Gesture Handlers
    private func handleDragChanged(_ value: DragGesture.Value) -> Void {
        let nextOffset = value.translation.width.clamp(lower: -maxDragDistance, 0)
        dragOffsetX = nextOffset
        isEnoughSwipped = abs(nextOffset) >= successThreshold
    }
    
    private func handleDragEnded() -> Void {
        //if enough was swipped ==> Completely swipped
        if self.isEnoughSwipped {
            withAnimation(.spring()) {
                self.dragOffsetX = -maxDragDistance
            }
            
            //CheckOUT
            if nil != self.actionSuccess {
                //wait and give enough time for animation to finish
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                    self.actionSuccess!()
                    self.dragOffsetX = 0
                }
            }
        }else{
            withAnimation(.spring()) {
                self.dragOffsetX = 0
            }
        }
    }
    
}

#Preview {
    CheckOutSwipeButtonView()
}
