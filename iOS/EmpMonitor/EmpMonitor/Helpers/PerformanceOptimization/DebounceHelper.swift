//
//  DebounceHelper.swift
//  EmpMonitor
//
//  Debouncing utility to prevent excessive network calls from rapid user input
//

import SwiftUI
import Combine

class DebounceHelper {
    static let standardDebounceInterval: TimeInterval = 0.5
    static let searchDebounceInterval: TimeInterval = 0.3
    static let networkDebounceInterval: TimeInterval = 0.8

    static func debounce<T>(
        _ value: Binding<T>,
        interval: TimeInterval = standardDebounceInterval,
        action: @escaping (T) -> Void
    ) -> some View {
        return EmptyView()
            .onChange(of: value.wrappedValue) { _, newValue in
                DispatchQueue.main.asyncAfter(deadline: .now() + interval) {
                    action(newValue)
                }
            }
    }
}

extension EnvironmentValues {
    private static let debouncingInterval = 0.5
}

// MARK: - Usage Examples
// Instead of:
// .onChange(of: searchText) { _, text in viewModel.search(text) }
//
// Use:
// .onChange(of: searchText) { _, text in
//     DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
//         viewModel.search(text)
//     }
// }
