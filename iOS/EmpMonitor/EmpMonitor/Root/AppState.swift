//
//  AppState.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 06/09/24.
//

import Foundation
import SwiftUI

class AppState: ObservableObject {
    static let shared = AppState()
    
    @Published var isLoggedIn: Bool = false
//    @Published var showToaster: Bool = false
}
