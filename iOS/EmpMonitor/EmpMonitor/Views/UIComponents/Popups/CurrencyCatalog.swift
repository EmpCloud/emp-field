//
//  CurrencyCatalog.swift
//  EmpMonitor
//

import Foundation

/// Currency codes and display names offered by the task value pickers.
enum CurrencyCatalog {
    static let availableCurrencies: [String] = {
        let locales = Locale.availableIdentifiers.map { Locale(identifier: $0)}
        let currencySet = Set(locales.compactMap { $0.currency?.identifier })
        return Array(currencySet).sorted()
//        return locales.compactMap { $0.currency?.identifier }
    }()

    static func currencyName(currencyCode: String) -> String {
        let locale = Locale(identifier: Locale.identifier(fromComponents: [NSLocale.Key.currencyCode.rawValue: currencyCode]))
        return locale.localizedString(forCurrencyCode: currencyCode) ?? currencyCode
    }
}
