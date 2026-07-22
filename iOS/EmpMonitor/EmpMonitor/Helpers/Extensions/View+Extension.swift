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
    
    func toolbarDoneButton() -> some View {
            self.toolbar {
                ToolbarItemGroup(placement: .keyboard) {
                    Spacer()  // Push the Done button to the right
                    Button("Done") {
                        UIApplication.shared.dismissKeyboard()
                    }
                }
            }
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
