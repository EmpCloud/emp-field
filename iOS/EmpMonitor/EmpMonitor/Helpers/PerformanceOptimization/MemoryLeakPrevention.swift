//
//  MemoryLeakPrevention.swift
//  EmpMonitor
//
//  Guidelines and utilities to prevent common memory leaks in SwiftUI
//

import Foundation
import Combine

/*
 MEMORY LEAK PREVENTION GUIDELINES

 1. TIMER CLEANUP
    ❌ Bad:
    Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { _ in
        self.updateUI()
    }

    ✅ Good:
    private var timer: Timer?

    deinit {
        timer?.invalidate()
    }

    Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { [weak self] _ in
        self?.updateUI()
    }

 2. CLOSURE CAPTURES
    ❌ Bad:
    networkManager.fetch { result in
        self.data = result
    }

    ✅ Good:
    networkManager.fetch { [weak self] result in
        self?.data = result
    }

 3. @ObservedObject IN LISTS
    ❌ Bad (N+1 rendering):
    List {
        ForEach(items) { item in
            ItemView(viewModel: itemViewModel)  // @ObservedObject per item
        }
    }

    ✅ Good:
    List {
        ForEach(items, id: \.id) { item in
            ItemView(item: item)  // Pass data, not ViewModel
        }
    }

 4. DISPATCH QUEUE CLEANUP
    ❌ Bad:
    DispatchSourceTimer.makeTimerSource(queue: .global()) { [self] in
        self.process()
    }

    ✅ Good:
    private var timer: DispatchSourceTimer?

    deinit {
        timer?.cancel()
    }

 5. COMBINE SUBSCRIPTION CLEANUP
    ❌ Bad:
    cancellables = []  // Lost reference
    $searchText
        .debounce(for: 0.3, scheduler: DispatchQueue.main)
        .sink { self.search($0) }

    ✅ Good:
    @StateObject private var cancellables = AnyCancellable.Set()

    $searchText
        .debounce(for: 0.3, scheduler: DispatchQueue.main)
        .sink { [weak self] in self?.search($0) }
        .store(in: &cancellables)
*/

// MARK: - Utilities

/// Helper to safely store Combine subscriptions
extension Set where Element == AnyCancellable {
    mutating func add(_ cancellable: AnyCancellable) {
        self.insert(cancellable)
    }
}

/// Weak reference wrapper for preventing retain cycles in closures
class WeakReference<T: AnyObject> {
    weak var value: T?
    init(_ value: T) {
        self.value = value
    }
}
