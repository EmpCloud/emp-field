//
//  TermConditionView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 03/09/24.
//

import SwiftUI

struct TermConditionView: View {
    @Environment(\.dismiss) var dismiss

    var termConditionURL: String {
        Constants.StaticURL.termsAndConditions
    }

    var body: some View {
        VStack {
            HStack {
                Text("Terms & Conditions")
                    .font(.system(size: 18, weight: .semibold))
                Spacer()
                Button(action: { dismiss() }) {
                    Image(systemName: "xmark.circle.fill")
                        .font(.system(size: 24))
                        .foregroundStyle(.gray)
                }
                .accessibilityLabel("Close")
            }
            .padding()

            WebPageView(urlString: termConditionURL)
        }
        .foregroundStyle(Color.addressText2)
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .background(Color.rectangleBG)
        .shadow(color: .black.opacity(0.25), radius: 12)
    }
}

#Preview {
    TermConditionView()
}
