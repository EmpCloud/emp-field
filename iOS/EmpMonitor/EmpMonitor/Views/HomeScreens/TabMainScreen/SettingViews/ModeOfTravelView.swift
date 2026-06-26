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
        RoundedRectangle(cornerRadius: 15)
            .fill(Color.white)
            .frame(height: 221)
            .overlay {
                VStack(spacing: 20) {
                    Text("Select Mode of Transportation")
                        .font(.custom("Montserrat", size: 14))
                        .fontWeight(.medium)
                    
                    HStack(spacing: 20) {
                        Mode(mode: "Bike", modeImage: Image(.bike), selectedMode: $tappedMode)
                            .onTapGesture {
                                withAnimation {
                                    tappedMode = "Bike"
                                }
                            }
                        
                        Mode(mode: "Car", modeImage: Image(.car), selectedMode: $tappedMode)
                            .onTapGesture {
                                withAnimation {
                                    tappedMode = "Car"
                                }
                            }
                        Mode(mode: "Auto", modeImage: Image(.auto), selectedMode: $tappedMode)
                            .onTapGesture {
                                withAnimation {
                                    tappedMode = "Auto"
                                }
                            }
                    }
                    
                    HStack(spacing: 20) {
                        Mode(mode: "Bus", modeImage: Image(.bus), selectedMode: $tappedMode)
                            .onTapGesture {
                                withAnimation {
                                    tappedMode = "Bus"
                                }
                            }
                        Mode(mode: "Train", modeImage: Image(.train), selectedMode: $tappedMode)
                            .onTapGesture {
                                withAnimation {
                                    tappedMode = "Train"
                                }
                            }
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
                    .padding(10)
                    .padding(.horizontal, 130)
                    .disableWithOpacity(tappedMode == nil)
                }
            }
            .padding(.horizontal)
            .onTapGesture {
                showMode = true
            }
    }
}

#Preview {
    ModeOfTravelView(motViewModel: MOTViewModel(), selectedMode: .constant(nil), tappedMode: .constant(""), showMode: .constant(false))
}
