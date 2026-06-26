//
//  Data+Extensions.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 28/08/24.
//

import Foundation

extension Data {
    mutating public func append(_ string: String) {
        if let data = string.data(using: .utf8) {
            append(data)
        }
    }
}
