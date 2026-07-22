//
//  ModeOfTravelView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 02/09/24.
//

import SwiftUI

struct ModeOfTravelView: View {
    
    @ObservedObject var motViewModel: MOTViewModel
    
    @Binding var selectedMode: String?
    @Binding var tappedMode: String?
    @Binding var showMode: Bool
    
    var body: some View {
        VStack(spacing: AppSpacing.stackSpacingMedium) {
            Text("Select Mode of Transportation")
                .font(AppFont.primary(size: AppFont.Size.body))
                .fontWeight(AppFont.Weight.medium)
                .multilineTextAlignment(.center)
                .lineLimit(2)

            LazyVGrid(columns: [GridItem(.adaptive(minimum: 98), spacing: AppSpacing.md)], spacing: AppSpacing.md) {
                modeButton("Bike", image: Image(.bike))
                modeButton("Car", image: Image(.car))
                modeButton("Auto", image: Image(.auto))
                modeButton("Bus", image: Image(.bus))
                modeButton("Train", image: Image(.train))
            }

            PrimaryThinButton(text: "Select") {
                //TODO: To select the mode & to make api call
                Task {
                    selectedMode = tappedMode

                    motViewModel.currentModeOfTravel = selectedMode?.lowercased() ?? ""

                    try await motViewModel.setModeOfTransport()

                    if NetworkManager.shared.statusCode == 200 {
                        showMode.toggle()
                    }
                }
            }
            .frame(maxWidth: AppLayout.buttonWidth)
            .disableWithOpacity(tappedMode == nil)
        }
        .padding(.horizontal, AppSpacing.lg)
        .padding(.vertical, AppSpacing.lg)
        .frame(maxWidth: .infinity)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
        .padding(.horizontal)
    }

    private func modeButton(_ mode: String, image: Image) -> some View {
        Button {
            withAnimation {
                tappedMode = mode
            }
        } label: {
            Mode(mode: mode, modeImage: image, selectedMode: $tappedMode)
        }
        .buttonStyle(.plain)
        .accessibilityLabel(mode)
    }
}

#Preview {
    ModeOfTravelView(motViewModel: MOTViewModel(), selectedMode: .constant(nil), tappedMode: .constant(""), showMode: .constant(false))
}
