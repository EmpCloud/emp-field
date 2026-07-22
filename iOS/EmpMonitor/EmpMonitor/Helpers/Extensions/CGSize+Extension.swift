//
//  CGSize+Extension.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import SwiftUI


//MARK: Helper Class for CheckIn/CheckOut Button
extension CGSize {
    
    static var inactiveThumbSize: CGSize {
        return AppLayout.swipeThumbSize
    }
    
    static var activeThumbSize: CGSize {
        return AppLayout.swipeThumbActiveSize
    }
    
    static var trackSize: CGSize {
        return AppLayout.swipeTrackSize
    }
}
