//
//  PrimaryButton.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI


struct PrimaryButton: View {

    var text: String
    var action: () -> Void

    var body: some View {
        ZStack {
            Button(action: action) {
                Text(text)
                    .font(.system(size: 18, weight: .semibold))
                    .foregroundStyle(.white)
                    .padding()
                    .frame(maxWidth: .infinity)
                    .frame(height: 44)
                    .background(ColorGradient.primaryButton)
                    .clipShape(RoundedRectangle(cornerRadius: 10))
                }
                .accessibilityLabel(text)
        }
    }
}

struct PrimaryThinButton: View {

    var text: String
    var action: () -> Void

    var body: some View {
        ZStack {
            Button(action: action) {
                Text(text)
                    .font(.system(size: 16, weight: .semibold))
                    .foregroundStyle(.white)
                    .padding(10)
                    .frame(maxWidth: .infinity)
                    .frame(height: 44)
                    .background(ColorGradient.primaryButton)
                    .clipShape(RoundedRectangle(cornerRadius: 10))
                }
                .accessibilityLabel(text)
        }
    }
}

struct RedThinButton: View {
    
    var text: String
    var action: () -> Void
    
    var body: some View {
        ZStack {
            Button(action: action) {
                HStack {
                    if text == "Resume" {
                        Image(systemName: "play.fill")
                            .foregroundStyle(Color.white)
                    }
                    Text(text)
                        .font(.custom("Poppins-Regular", size: 15))
                        .foregroundStyle(.white)
                    
                }
                .padding(10)
                .frame(maxWidth: .infinity)
                .background(Color.absent)
                .clipShape(RoundedRectangle(cornerRadius: 10))
            }
        }
    }
}

#Preview {
    PrimaryButton(text: "Primary Button"){
        AppLog.debug("Click me")
    }
}

#Preview {
    PrimaryThinButton(text: "Primary Button"){
        AppLog.debug("Click me")
    }
}

#Preview {
    RedThinButton(text: "Primary Button"){
        AppLog.debug("Click me")
    }
}

