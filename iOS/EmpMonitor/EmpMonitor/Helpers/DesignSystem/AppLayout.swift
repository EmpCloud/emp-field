//
//  AppLayout.swift
//  EmpMonitor
//
//  Centralized layout and dimension system
//  Replace all hardcoded frame sizes with these constants
//

import Foundation

enum AppLayout {
    // MARK: - Touch Target Sizes (HIG Minimum: 44x44)

    /// Standard button height (44pt - HIG minimum)
    static let buttonHeight: CGFloat = 44

    /// Standard button width (varies)
    static let buttonWidth: CGFloat = 203

    /// Tall button/input height
    static let tallButtonHeight: CGFloat = 50

    /// Small button height (for compact layouts)
    static let smallButtonHeight: CGFloat = 40

    // MARK: - Icon Sizes

    /// Extra small icon (16x16)
    static let iconExtraSmall: CGFloat = 16

    /// Small icon (20x20)
    static let iconSmall: CGFloat = 20

    /// Medium icon (24x24)
    static let iconMedium: CGFloat = 24

    /// Standard icon (40x40)
    static let iconStandard: CGFloat = 40

    /// Large icon (60x60)
    static let iconLarge: CGFloat = 60

    /// Extra large icon (100x100+)
    static let iconExtraLarge: CGFloat = 100

    // MARK: - Text Field Sizes

    /// Standard text field height
    static let textFieldHeight: CGFloat = 46

    /// Large text field height
    static let textFieldHeightLarge: CGFloat = 56

    // MARK: - Card Sizes

    /// Standard card corner radius
    static let cardCornerRadius: CGFloat = 10

    /// Standard card height for list items
    static let cardHeight: CGFloat = 100

    /// Small card (for stat/metric cards)
    static let smallCardSize: CGFloat = 114

    /// Medium card size
    static let mediumCardSize: CGFloat = 150

    // MARK: - List/Table Sizes

    /// Standard row height
    static let rowHeight: CGFloat = 60

    /// Tall row height
    static let rowHeightTall: CGFloat = 80

    /// Section header height
    static let sectionHeaderHeight: CGFloat = 40

    // MARK: - Common Layout Constraints

    /// Maximum width for content on large screens
    static let maxContentWidth: CGFloat = 600

    /// Minimum safe area padding
    static let minimumPadding: CGFloat = 16

    /// Safe area top padding on notched devices
    static let safeAreaTopPadding: CGFloat = 20

    /// Safe area bottom padding
    static let safeAreaBottomPadding: CGFloat = 20

    // MARK: - Shadow & Elevation

    /// Standard shadow radius
    static let shadowRadius: CGFloat = 8

    /// Standard shadow offset
    static let shadowOffset: CGFloat = 2

    // MARK: - Migration Guide

    /*
     Replace hardcoded frame sizes as follows:

     // Old code:
     Button { }
         .frame(width: 203, height: 42)

     // New code:
     Button { }
         .frame(height: AppLayout.buttonHeight)
         .frame(maxWidth: .infinity)

     Common Sizes:
     - Frame 44x44 (buttons)    → AppLayout.buttonHeight
     - Frame 46x? (inputs)      → .frame(height: AppLayout.textFieldHeight)
     - Frame 40x40 (icons)      → .frame(width: AppLayout.iconStandard, height: AppLayout.iconStandard)
     - Frame 20x20 (small icons) → .frame(width: AppLayout.iconSmall, height: AppLayout.iconSmall)
     - Frame 100x100 (cards)    → .frame(width: AppLayout.smallCardSize, height: AppLayout.smallCardSize)

     IMPORTANT: HIG minimum touch target is 44x44pt
     Never make buttons smaller than AppLayout.buttonHeight for tap targets
    */
}
