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
    
    let availableCurrencies: [String] = {
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
                            ForEach(availableCurrencies, id: \.self){ currencyCode in
                                Text("\(currencyName(currencyCode: currencyCode)) (\(currencyCode))")
                                    .onTapGesture {
                                        selectedCurrency = currencyCode
                                        withAnimation {
                                            showCurrencyPopup.toggle()
                                        }
                                    }
                            }
                            .font(.custom("Montserrat", size: 15))
                            .foregroundStyle(Color.white)
                            .padding(.vertical, 5)
                        }
                        .frame(maxWidth: .infinity)
                        .padding(.top)
                    }
                    
                }
        }
    }
    
    private func currencyName(currencyCode: String) -> String {
        let locale = Locale(identifier: Locale.identifier(fromComponents: [NSLocale.Key.currencyCode.rawValue: currencyCode]))
        return locale.localizedString(forCurrencyCode: currencyCode) ?? currencyCode
    }
}

#Preview {
    CurrencyPopupView(selectedCurrency: .constant("INR"), showCurrencyPopup: .constant(false))
}
