//
//  CurrencyPopupView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct CurrencyPopupView: View {
    
    @Binding var selectedCurrency: String
    @Binding var showCurrencyPopup: Bool
    
    static let availableCurrencies: [String] = {
        let locales = Locale.availableIdentifiers.map { Locale(identifier: $0)}
        let currencySet = Set(locales.compactMap { $0.currency?.identifier })
        return Array(currencySet).sorted()
//        return locales.compactMap { $0.currency?.identifier }
    }()
    
    var body: some View {
        ZStack{
            RoundedRectangle(cornerRadius: 10)
                .fill(Color.taskSearchBar)
                .frame(height: 400)
                .overlay {
                    ScrollView {
                        VStack{
                            ForEach(Self.availableCurrencies, id: \.self){ currencyCode in
                                Text("\(Self.currencyName(currencyCode: currencyCode)) (\(currencyCode))")
                                    .onTapGesture {
                                        selectedCurrency = currencyCode
                                        withAnimation {
                                            showCurrencyPopup.toggle()
                                        }
                                    }
                            }
                            .font(AppFont.primary(size: AppFont.Size.subheadline))
                            .foregroundStyle(Color.white)
                            .padding(.vertical, 5)
                        }
                        .frame(maxWidth: .infinity)
                        .padding(.top)
                    }
                    
                }
        }
    }
    
    static func currencyName(currencyCode: String) -> String {
        let locale = Locale(identifier: Locale.identifier(fromComponents: [NSLocale.Key.currencyCode.rawValue: currencyCode]))
        return locale.localizedString(forCurrencyCode: currencyCode) ?? currencyCode
    }
}

#Preview {
    CurrencyPopupView(selectedCurrency: .constant("INR"), showCurrencyPopup: .constant(false))
}
