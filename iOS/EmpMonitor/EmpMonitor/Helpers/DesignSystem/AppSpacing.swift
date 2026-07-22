//
//  AppSpacing.swift
//  EmpMonitor
//
//  Centralized spacing system using 8pt grid
//  Replace all hardcoded padding values with these constants
//

import Foundation

public enum AppSpacing {
    // 8pt grid spacing scale
    public static let zero: CGFloat = 0        // Flush layouts where spacing is intentionally removed
    public static let xxs: CGFloat = 2         // Hairline/visual alignment nudges
    public static let xs: CGFloat = 4          // Minimum spacing between elements
    public static let sm: CGFloat = 8          // Small gaps (between icon+text, tight layouts)
    public static let md: CGFloat = 16         // Default spacing (padding, margins)
    public static let lg: CGFloat = 24         // Large sections (between card stacks)
    public static let xl: CGFloat = 32         // Extra large spacing (page margins)
    public static let xxl: CGFloat = 48        // Extra extra large (major sections)

    // Common padding combinations
    public static let horizontalPadding: CGFloat = md
    public static let verticalPadding: CGFloat = md
    public static let pagePadding: CGFloat = md
    public static let screenHorizontalPadding: CGFloat = md
    public static let cardPadding: CGFloat = md
    public static let modalHorizontalPadding: CGFloat = lg
    public static let modalBottomPadding: CGFloat = lg
    public static let controlInnerPadding: CGFloat = sm
    public static let compactControlInnerPadding: CGFloat = xs

    // Stack spacing
    public static let stackSpacingTight: CGFloat = 0
    public static let stackSpacingSmall: CGFloat = xs
    public static let stackSpacingDefault: CGFloat = sm
    public static let stackSpacingMedium: CGFloat = md
    public static let stackSpacingLarge: CGFloat = lg

    // Semantic gaps used across repeated controls
    public static let iconTextSpacing: CGFloat = sm
    public static let compactIconTextSpacing: CGFloat = xs
    public static let formFieldSpacing: CGFloat = xs
    public static let formSectionSpacing: CGFloat = lg
    public static let sectionTopSpacing: CGFloat = 32
    public static let floatingActionBottomPadding: CGFloat = 72
    public static let tabBarBottomPadding: CGFloat = lg

    // Migration Guide:
    // .padding(10)                    → .padding(AppSpacing.sm)
    // .padding(16)                    → .padding(AppSpacing.md)
    // .padding(20)                    → .padding(AppSpacing.md) or .padding(AppSpacing.lg)
    // .padding(.horizontal, 20)       → .padding(.horizontal, AppSpacing.md)
    // .padding(.vertical, 20)         → .padding(.vertical, AppSpacing.md)
    // VStack(spacing: 10)             → VStack(spacing: AppSpacing.sm)
    // VStack(spacing: 20)             → VStack(spacing: AppSpacing.md)
}
