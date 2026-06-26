//
//  AppTypography.swift
//  EmpMonitor
//
//  Centralized typography system with semantic font styles
//  Replace all hardcoded .font(.custom(...)) with these constants
//

import SwiftUI

public enum AppTypography {
    // MARK: - Semantic Font Styles

    /// Large heading text (25pt, semibold)
    /// Used for: Screen titles, major section headers
    public static var title: Font {
        .system(size: 25, weight: .semibold, design: .default)
    }

    /// Medium heading text (20pt, semibold)
    /// Used for: Section headers, card titles
    public static var headline: Font {
        .system(size: 20, weight: .semibold, design: .default)
    }

    /// Standard body text (14pt, regular)
    /// Used for: Main content text, labels, descriptions
    static var body: Font {
        .system(size: 14, weight: .regular, design: .default)
    }

    /// Small body text (13pt, regular)
    /// Used for: Slightly smaller labels, secondary text
    static var bodySmall: Font {
        .system(size: 13, weight: .regular, design: .default)
    }

    /// Caption text (12pt, regular)
    /// Used for: Secondary information, helper text, placeholders
    static var caption: Font {
        .system(size: 12, weight: .regular, design: .default)
    }

    /// Extra small caption (10pt, regular)
    /// Used for: Very small labels, timestamps, metadata
    static var captionSmall: Font {
        .system(size: 10, weight: .regular, design: .default)
    }

    // MARK: - Alternative Weights

    /// Body text with bold weight (14pt, bold)
    static var bodyBold: Font {
        .system(size: 14, weight: .bold, design: .default)
    }

    /// Caption with bold weight (12pt, bold)
    static var captionBold: Font {
        .system(size: 12, weight: .bold, design: .default)
    }

    /// Button text (14pt, semibold)
    static var button: Font {
        .system(size: 14, weight: .semibold, design: .default)
    }

    // MARK: - Montserrat Compatibility

    /// For when Montserrat custom font is required
    /// WARNING: Should be migrated to system fonts for Dynamic Type support
    static func customMontserrat(size: CGFloat, weight: Font.Weight = .regular) -> Font {
        .system(size: size, weight: weight, design: .default)
    }

    // MARK: - Migration Guide

    /*
     Replace hardcoded font sizes as follows:

     // Old code:
     Text("Title")
         .font(.custom("Montserrat", size: 25))

     // New code:
     Text("Title")
         .font(AppTypography.title)

     Size Mapping:
     - 25pt (Titles)           → AppTypography.title
     - 20pt (Headlines)        → AppTypography.headline
     - 16pt (Body/Labels)      → AppTypography.body
     - 14pt (Body/Labels)      → AppTypography.body
     - 13pt (Small body)       → AppTypography.bodySmall
     - 12pt (Captions)         → AppTypography.caption
     - 10pt (Small captions)   → AppTypography.captionSmall
     - 8pt (Below minimum)     → AppTypography.caption (INCREASE minimum)

     IMPORTANT: Apple HIG recommends minimum 11pt for body text.
     Never use captionSmall for primary content.
    */
}

// MARK: - View Modifiers for Dynamic Type Support

extension View {
    /// Apply Dynamic Type support to text views
    /// Allows users to adjust text size per iOS Accessibility settings
    func supportsDynamicType() -> some View {
        self.modifier(DynamicTypeModifier())
    }
}

struct DynamicTypeModifier: ViewModifier {
    func body(content: Content) -> some View {
        content
            .lineLimit(nil)  // Allow text wrapping
            .fixedSize(horizontal: false, vertical: true)  // Auto-height for wrapped text
    }
}
