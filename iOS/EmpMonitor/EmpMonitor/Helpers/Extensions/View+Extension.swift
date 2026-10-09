//
//  View+Extension.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import Foundation
import SwiftUI
import UIKit

extension View {
    func disableWithOpacity(_ condition: Bool) -> some View {
        self
            .disabled(condition)
            .opacity(condition ? 0.5 : 1.0)
    }
    
    func dismissKeyboardOnTapOutside() -> some View {
        background(KeyboardDismissTapInstaller())
    }
}

extension UIApplication {
    func dismissKeyboard() {
        sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
    }
}

private struct KeyboardDismissTapInstaller: UIViewRepresentable {
    func makeCoordinator() -> Coordinator {
        Coordinator()
    }

    func makeUIView(context: Context) -> UIView {
        let view = UIView(frame: .zero)
        view.isUserInteractionEnabled = false
        return view
    }

    func updateUIView(_ uiView: UIView, context: Context) {
        DispatchQueue.main.async {
            context.coordinator.installIfNeeded(in: uiView.window)
        }
    }

    final class Coordinator: NSObject, UIGestureRecognizerDelegate {
        private weak var installedWindow: UIWindow?
        private weak var tapGesture: UITapGestureRecognizer?

        func installIfNeeded(in window: UIWindow?) {
            guard let window else { return }

            if installedWindow === window, tapGesture != nil {
                return
            }

            if let tapGesture {
                installedWindow?.removeGestureRecognizer(tapGesture)
            }

            let gesture = UITapGestureRecognizer(target: self, action: #selector(handleTap))
            gesture.cancelsTouchesInView = false
            gesture.delegate = self

            window.addGestureRecognizer(gesture)
            installedWindow = window
            tapGesture = gesture
        }

        @objc private func handleTap() {
            UIApplication.shared.dismissKeyboard()
        }

        func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool {
            guard let touchedView = touch.view else { return true }
            return !touchedView.isInsideTextInput
        }
    }
}

private extension UIView {
    var isInsideTextInput: Bool {
        if self is UITextField || self is UITextView || self is UISearchBar {
            return true
        }

        return superview?.isInsideTextInput ?? false
    }
}

extension View {
    /// Limits the view to the system's readable content width (the same guide UIKit exposes
    /// as `readableContentGuide`), so forms stay comfortable to read on iPad in any
    /// orientation or window size, and follow the user's text size.
    func readableContentWidth() -> some View {
        modifier(ReadableContentWidth())
    }
}

private struct ReadableContentWidth: ViewModifier {
    @State private var readableWidth: CGFloat = .infinity

    func body(content: Content) -> some View {
        content
            .frame(maxWidth: readableWidth)
            .frame(maxWidth: .infinity)
            .background(ReadableWidthReader(width: $readableWidth))
    }
}

/// Reads the width of the system readable content guide for the space it is placed in.
private struct ReadableWidthReader: UIViewRepresentable {
    @Binding var width: CGFloat

    func makeUIView(context: Context) -> GuideView {
        let view = GuideView()
        view.isUserInteractionEnabled = false
        view.onLayout = { newWidth in
            guard newWidth > 0, abs(newWidth - width) > 0.5 else { return }
            DispatchQueue.main.async { width = newWidth }
        }
        return view
    }

    func updateUIView(_ uiView: GuideView, context: Context) {}

    final class GuideView: UIView {
        var onLayout: ((CGFloat) -> Void)?

        override func layoutSubviews() {
            super.layoutSubviews()
            onLayout?(readableContentGuide.layoutFrame.width)
        }
    }
}

extension Image {
    /// A decorative background stretched to the container width at its designed height, so
    /// wave artwork drawn for a 393pt-wide canvas also spans wider phones and iPad.
    @MainActor
    static func fullWidthBackground(_ resource: ImageResource) -> some View {
        Image(resource)
            .resizable()
            .frame(maxWidth: .infinity)
            .frame(height: UIImage(resource: resource).size.height)
            .accessibilityHidden(true)
    }
}
