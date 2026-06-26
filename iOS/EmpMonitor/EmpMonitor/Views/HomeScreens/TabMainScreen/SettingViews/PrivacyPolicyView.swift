//
//  PrivacyPolicy.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 03/09/24.
//

import SwiftUI

struct PrivacyPolicyView: View {
    @Environment(\.dismiss) var dismiss

    var privacyPolicyURL: String {
        Constants.StaticURL.privacyPolicy
    }

    var body: some View {
        VStack {
            HStack {
                Text("Privacy Policy")
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

            WebPageView(urlString: privacyPolicyURL)
        }
        .foregroundStyle(Color.addressText2)
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .background(Color.rectangleBG)
        .shadow(color: .black.opacity(0.25), radius: 12)
    }
}

#Preview {
    PrivacyPolicyView()
}
