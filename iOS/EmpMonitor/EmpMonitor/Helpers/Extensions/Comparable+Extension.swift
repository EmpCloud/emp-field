//
//  Comparable+Extension.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import SwiftUI


//MARK: Helper Class for CheckIn/CheckOut Button
extension Comparable where Self: Comparable {
    func clamp(lower: Self, _ upper: Self) -> Self {
        return min(max(self, lower), upper)
    }
}
