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
    
    //To animate the thumb size when the user starts dragging (Swipping)
    @State private var thumbSize: CGSize = CGSize.inactiveThumbSize
    
    //to keep track of the dragging value. (Initially its Zero)
    @State private var dragOffset: CGSize = .zero
    
    //to keep track when enough is dragged to be considered as checkIN
    @State private var isEnoughSwipped: Bool = false
    
    //Actions
    private var actionSuccess: (() -> Void)?
    
    //the track does not change size
    let trackSize = CGSize.trackSize
    
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
            .offset(x: getDragOffSetX(), y: 0)
            .contentShape(Circle())
            .gesture(
                DragGesture()
                    .onChanged { value in
                        withAnimation {
                            self.handleDragChanged(value)
                        }
                    }
                    .onEnded { _ in
                        self.handleDragEnded()
                    }
            )
        }
        .accessibilityLabel("Swipe to check in")
        .accessibilityHint("Drag the handle to the right to check in")
    }
    
    //MARK: Helper Function
    
    private func getDragOffSetX() -> CGFloat {
        //should not be able to drag outside of the track area
        let clampedDragOffSetX = dragOffset.width.clamp(lower: 0, trackSize.width - thumbSize.width )
        
        return -(trackSize.width/2 - thumbSize.width - (clampedDragOffSetX) + 20)
    }
    
    //MARK: Gesture Handlers
    private func handleDragChanged(_ value: DragGesture.Value) -> Void {
        self.dragOffset = value.translation
        
        let dragWidth = value.translation.width
        let targetDragWidth = self.trackSize.width - (self.thumbSize.width*2)
        
//        let wasInitiated = dragWidth > 2
        let didReachTarget = dragWidth > targetDragWidth
        
//        self.thumbSize = wasInitiated ? CGSize.activeThumbSize : CGSize.inactiveThumbSize
        
        if didReachTarget {
            //to change the UI for CheckOut
            self.isEnoughSwipped = true
        }else{
            //reset
            self.isEnoughSwipped = false
        }
    }
    
    private func handleDragEnded() -> Void {
        //if enough was swipped => Completely swipped
        if self.isEnoughSwipped {
            self.dragOffset = CGSize(width: self.trackSize.width - self.thumbSize.width, height: 0)
//            isCheckIn = true
            
            //checked In
            if nil != self.actionSuccess {
                //wait and give enough time for animation to finish
                
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                    self.actionSuccess!()
//                    isCheckIn = true
                    dragOffset = .zero // after checkIN the arrow should come to initial position
                }
            }
        }
        else {
            self.dragOffset = .zero
        }
    }
}

#Preview {
    CheckInSwipeButtonView()
}
