//
//  AppAnimation.swift
//  EmpMonitor
//
//  Centralized animation timing and easing
//  Replace all hardcoded animation durations with these constants
//

import SwiftUI

enum AppAnimation {
    // MARK: - Duration Constants

    /// Very fast animation (0.2 seconds)
    static let fast: TimeInterval = 0.2

    /// Standard animation (0.3 seconds)
    static let standard: TimeInterval = 0.3

    /// Medium animation (0.5 seconds)
    static let medium: TimeInterval = 0.5

    /// Slow animation (0.8 seconds)
    static let slow: TimeInterval = 0.8

    /// Very slow animation (1.0+ seconds)
    static let verySlow: TimeInterval = 1.0

    // MARK: - Preset Animation Curves

    /// Fast and smooth (easeInOut)
    static var easeInOut: Animation {
        .easeInOut(duration: standard)
    }

    /// Smooth spring animation
    static var spring: Animation {
        .spring(response: 0.6, dampingFraction: 0.7)
    }

    /// Fast spring animation
    static var fastSpring: Animation {
        .spring(response: 0.4, dampingFraction: 0.7)
    }

    /// Slow spring animation
    static var slowSpring: Animation {
        .spring(response: 0.8, dampingFraction: 0.7)
    }

    // MARK: - Custom Animation Builders

    /// Creates a linear animation with custom duration
    static func linear(duration: TimeInterval) -> Animation {
        .linear(duration: duration)
    }

    /// Creates an easeInOut animation with custom duration
    static func easeInOut(duration: TimeInterval) -> Animation {
        .easeInOut(duration: duration)
    }

    /// Creates a spring animation with custom response
    static func spring(response: Double = 0.6, dampingFraction: Double = 0.7) -> Animation {
        .spring(response: response, dampingFraction: dampingFraction)
    }

    // MARK: - Migration Guide

    /*
     Replace hardcoded animation durations as follows:

     // Old code:
     withAnimation(.easeInOut(duration: 0.3)) {
         showMenu.toggle()
     }

     // New code:
     withAnimation(AppAnimation.easeInOut) {
         showMenu.toggle()
     }

     Duration Mapping:
     - 0.2s (very fast)         → AppAnimation.fast
     - 0.3s (standard)          → AppAnimation.standard
     - 0.5s (medium)            → AppAnimation.medium
     - 0.8s (slow)              → AppAnimation.slow
     - 1.0s+ (very slow)        → AppAnimation.verySlow

     Preset Usage:
     withAnimation(AppAnimation.spring) { ... }
     withAnimation(AppAnimation.easeInOut) { ... }
    */
}
