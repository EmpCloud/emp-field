//
//  ColorGradient.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import Foundation
import SwiftUI

struct ColorGradient {
    static let primaryButton = LinearGradient(
        gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]),
        startPoint: .top,
        endPoint: .bottom
    )
}
