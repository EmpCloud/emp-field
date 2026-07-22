//
//  AppTypography.swift
//  EmpMonitor
//
//  Semantic aliases over AppFont.
//

import SwiftUI

public enum AppTypography {
    // MARK: - Semantic Font Styles

    /// Large heading text (25pt, semibold)
    /// Used for: Screen titles, major section headers
    public static var title: Font {
        AppFont.primary(size: AppFont.Size.screenTitle, weight: AppFont.Weight.semibold)
    }

    /// Medium heading text (20pt, semibold)
    /// Used for: Section headers, card titles
    public static var headline: Font {
        AppFont.primary(size: AppFont.Size.navigationTitle, weight: AppFont.Weight.semibold)
    }

    /// Standard body text (14pt, regular)
    /// Used for: Main content text, labels, descriptions
    static var body: Font {
        AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.regular)
    }

    /// Small body text (13pt, regular)
    /// Used for: Slightly smaller labels, secondary text
    static var bodySmall: Font {
        AppFont.primary(size: AppFont.Size.callout, weight: AppFont.Weight.regular)
    }

    /// Caption text (12pt, regular)
    /// Used for: Secondary information, helper text, placeholders
    static var caption: Font {
        AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.regular)
    }

    /// Extra small caption (10pt, regular)
    /// Used for: Very small labels, timestamps, metadata
    static var captionSmall: Font {
        AppFont.primary(size: AppFont.Size.xSmall, weight: AppFont.Weight.regular)
    }

    // MARK: - Alternative Weights

    /// Body text with bold weight (14pt, bold)
    static var bodyBold: Font {
        AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.bold)
    }

    /// Caption with bold weight (12pt, bold)
    static var captionBold: Font {
        AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.bold)
    }

    /// Button text (14pt, semibold)
    static var button: Font {
        AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.semibold)
    }

    // MARK: - Montserrat Compatibility

    static func customMontserrat(size: CGFloat, weight: Font.Weight = AppFont.Weight.regular) -> Font {
        AppFont.primary(size: size, weight: weight)
    }

    // MARK: - Migration Guide

    /*
     Prefer AppFont directly for new code:

     Text("Title")
         .font(AppFont.primary(size: AppFont.Size.screenTitle, weight: AppFont.Weight.semibold))

     Size Mapping:
     - 25pt (Titles)           -> AppFont.Size.screenTitle
     - 20pt (Headlines)        -> AppFont.Size.navigationTitle
     - 16pt (Body/Labels)      -> AppFont.Size.headline
     - 14pt (Body/Labels)      -> AppFont.Size.body
     - 13pt (Small body)       -> AppFont.Size.callout
     - 12pt (Captions)         -> AppFont.Size.caption
     - 10pt (Small captions)   -> AppFont.Size.xSmall
     - 8pt (Below minimum)     -> AppFont.Size.caption when possible

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
