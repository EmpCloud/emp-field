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
    
    //to animate the thumb size when the user starts dragging(Swiping)
    @State private var thumbSize: CGSize = CGSize.inactiveThumbSize
    
    //to keep track of the dragging value. (Initially its Zero)
    @State private var dragOffSet: CGSize = CGSize(width: CGSize.trackSize.width - CGSize.inactiveThumbSize.width, height: 0)
    
    // to keep track when enouhg is dragged to be considered as CheckedOUT
    @State private var isEnoughSwipped: Bool = false
    
    //Actions
    private var actionSuccess: (() -> Void)?
    
    // the track does not change size
    let trackSize = CGSize.trackSize
    
    init() {}
    
    
    var body: some View {
        ZStack {
            // swipe track
            Capsule()
                .frame(width: trackSize.width, height: trackSize.height)
                .foregroundStyle(
                    LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                )
            
            Text("Swipe to Check OUT")
                .font(.custom("Montserrat", size: 13))
                .foregroundStyle(Color.white)
                .offset(x: -15)
            
            
            ZStack {
                Circle()
                    .frame(width: 36, height: 36)
                    .foregroundStyle(Color.white)
                
                Image(systemName: "arrow.left")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 13.31, height: 17.66)
                    .foregroundStyle(
                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                    )
            }
            .offset(x: getDragOffSetX())
            .gesture(
                DragGesture()
                    .onChanged({ value  in
                        withAnimation {
                            self.handleDragChanged(value)
                        }
                    })
                    .onEnded({ _ in
                        self.handleDragEnded()
                    })
            )
        }
    }
    
//MARK: Helpers
    private func getDragOffSetX() -> CGFloat {
        //should not be able to drag outside of the track area
        let clampedDragOffSetX = dragOffSet.width.clamp(lower: 0, trackSize.width - thumbSize.width)
        
        return -(trackSize.width/2 - thumbSize.width/2 - clampedDragOffSetX)
    }
    
    // MARK: Gesture Handlers
    private func handleDragChanged(_ value: DragGesture.Value) -> Void {
        self.dragOffSet = value.translation
        
        let dragWidth = value.translation.width
        
        //calculate target drag width for left swipe
        let tragetDragWith = -(self.trackSize.width - (self.thumbSize.width*2))
        
        // check for negative drag width (left siwpe)
        let isLeftSwipe = dragWidth < 0
        
        //Handle left swipe logic based on target reaching
        let didReachTarget = isLeftSwipe && abs(dragWidth) > abs(tragetDragWith)
        
        if didReachTarget {
            // to change the UI for CheckOUT
            self.isEnoughSwipped = true
        }else{
            //reset
            self.isEnoughSwipped = false
        }
    }
    
    private func handleDragEnded() -> Void {
        //if enough was swipped ==> Completely swipped
        if self.isEnoughSwipped {
            self.dragOffSet = .zero
            
            //CheckOUT
            if nil != self.actionSuccess {
                //wait and give enough time for animation to finish
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                    self.actionSuccess!()
                    self.dragOffSet = CGSize(width: CGSize.trackSize.width - CGSize.inactiveThumbSize.width, height: 0)
                }
            }
        }else{
            self.dragOffSet = CGSize(width: CGSize.trackSize.width - CGSize.inactiveThumbSize.width, height: 0)
        }
    }
    
}

#Preview {
    CheckOutSwipeButtonView()
}
