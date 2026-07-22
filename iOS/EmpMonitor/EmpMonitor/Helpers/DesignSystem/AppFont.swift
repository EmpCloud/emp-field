//
//  AppFont.swift
//  EmpMonitor
//
//  Centralized typography system.
//

import SwiftUI

enum AppFont {
    static let primaryFamily = "Montserrat"

    enum Size {
        static let nano: CGFloat = 8
        static let micro: CGFloat = 9
        static let xSmall: CGFloat = 10
        static let footnote: CGFloat = 11
        static let caption: CGFloat = 12
        static let callout: CGFloat = 13
        static let body: CGFloat = 14
        static let subheadline: CGFloat = 15
        static let headline: CGFloat = 16
        static let headlineLarge: CGFloat = 17
        static let title3: CGFloat = 18
        static let navigationTitle: CGFloat = 20
        static let title2: CGFloat = 22
        static let title: CGFloat = 24
        static let screenTitle: CGFloat = 25
        static let largeTitle: CGFloat = 28
        static let display: CGFloat = 34
        static let iconLarge: CGFloat = 36
        static let iconXLarge: CGFloat = 40
        static let iconHero: CGFloat = 44
        static let iconDisplay: CGFloat = 50
        static let brandTitle: CGFloat = 56

        static let closeIcon: CGFloat = AppLayout.closeIconSize
        static let iconSmall: CGFloat = AppLayout.iconSmall
        static let iconExtraSmall: CGFloat = AppLayout.iconExtraSmall
    }

    enum Weight {
        static let light: Font.Weight = .light
        static let regular: Font.Weight = .regular
        static let medium: Font.Weight = .medium
        static let semibold: Font.Weight = .semibold
        static let bold: Font.Weight = .bold
    }

    static func primary(size: CGFloat, weight: Font.Weight = Weight.regular) -> Font {
        .custom(primaryFamily, size: size)
            .weight(weight)
    }

    static func symbol(size: CGFloat, weight: Font.Weight = Weight.regular) -> Font {
        .system(size: size, weight: weight)
    }

    static let largeTitle = primary(size: Size.display, weight: Weight.bold)
    static let title = primary(size: Size.largeTitle, weight: Weight.semibold)
    static let title2 = primary(size: Size.title2, weight: Weight.semibold)
    static let headline = primary(size: Size.headlineLarge, weight: Weight.semibold)
    static let subheadline = primary(size: Size.subheadline, weight: Weight.medium)
    static let body = primary(size: Size.body, weight: Weight.regular)
    static let caption = primary(size: Size.caption, weight: Weight.regular)
}
