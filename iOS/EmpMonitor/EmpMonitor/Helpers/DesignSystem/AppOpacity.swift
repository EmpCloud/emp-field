//
//  AppOpacity.swift
//  EmpMonitor
//
//  Centralized opacity/transparency system
//  Replace all hardcoded .opacity(...) values with these constants
//

import Foundation

enum AppOpacity {
    // Fully visible
    static let full: Double = 1.0

    // Strong/prominent opacity
    static let strong: Double = 0.9

    // Standard/default opacity
    static let standard: Double = 0.8

    // Secondary/muted opacity
    static let secondary: Double = 0.7

    // Subtle/faded opacity
    static let subtle: Double = 0.5

    // Disabled/inactive state
    static let disabled: Double = 0.4

    // Light/very faded
    static let light: Double = 0.25

    // Very light/barely visible
    static let veryLight: Double = 0.15

    // Nearly transparent
    static let verySubtle: Double = 0.1

    // Completely transparent
    static let transparent: Double = 0.0

    // MARK: - Migration Guide

    /*
     Replace hardcoded opacity values as follows:

     // Old code:
     Text("Label")
         .opacity(0.5)

     // New code:
     Text("Label")
         .opacity(AppOpacity.secondary)

     Opacity Mapping:
     - 1.0 (fully opaque)        → AppOpacity.full
     - 0.8-0.9 (strong)          → AppOpacity.strong or .standard
     - 0.7 (slightly muted)      → AppOpacity.secondary
     - 0.5 (disabled/muted)      → AppOpacity.disabled or .subtle
     - 0.25 (light/faded)        → AppOpacity.light
     - 0.15 (very light)         → AppOpacity.veryLight
     - 0.1-0.2 (barely visible)  → AppOpacity.verySubtle
     - 0.0 (hidden)              → AppOpacity.transparent
    */
}
