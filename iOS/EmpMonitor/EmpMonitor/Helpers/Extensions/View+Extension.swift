//
//  View+Extension.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/07/24.
//

import Foundation
import SwiftUI

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
                        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
                    }
                }
            }
        }
}
