//
//  AppRadius.swift
//  EmpMonitor
//
//  Centralized corner radius system
//  Replace all hardcoded cornerRadius values with these constants
//

import Foundation

public enum AppRadius {
    public static let small: CGFloat = 6       // Small buttons, inputs, tags
    public static let medium: CGFloat = 10     // Cards, moderate corners
    public static let large: CGFloat = 20      // Large modals, prominent elements
    public static let extraLarge: CGFloat = 25 // Full rounded (for pills, large badges)

    // Migration Guide:
    // .cornerRadius(4)    → .cornerRadius(AppRadius.small)
    // .cornerRadius(6)    → .cornerRadius(AppRadius.small)
    // .cornerRadius(8)    → .cornerRadius(AppRadius.small)
    // .cornerRadius(10)   → .cornerRadius(AppRadius.medium)
    // .cornerRadius(12)   → .cornerRadius(AppRadius.medium)
    // .cornerRadius(15)   → .cornerRadius(AppRadius.medium)
    // .cornerRadius(20)   → .cornerRadius(AppRadius.large)
    // .cornerRadius(25)   → .cornerRadius(AppRadius.extraLarge)
}
