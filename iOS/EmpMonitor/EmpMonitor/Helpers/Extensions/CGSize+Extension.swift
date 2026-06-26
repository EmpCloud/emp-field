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
        return CGSize(width: 42, height: 42)
    }
    
    static var activeThumbSize: CGSize {
        return CGSize(width: 72, height: 72)
    }
    
    static var trackSize: CGSize {
        return CGSize(width: 203, height: 42)
    }
}
