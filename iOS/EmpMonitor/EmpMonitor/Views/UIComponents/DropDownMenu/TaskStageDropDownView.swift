//
//  TaskStageDropDownView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 11/09/24.
//

import SwiftUI

struct TaskStageDropDownView: View {
    
//    @Binding var selection: LeavesTypeResponseDetail?
//    @State private var selectedStage: String = ""
    @Binding var selectedStage: TagResponseData?
    
    @State var showTaskStage: Bool = false
    
    var options: [TagResponseData]
//    var options: [String] = ["Stage 1", "Satge 2", "Satge 3","Stage 4", "Satge 5", "Satge 6"]
    
    @Binding var selectedStageTitle: String?
    
    var body: some View {
        VStack(spacing: AppSpacing.stackSpacingSmall) {
            Button {
                withAnimation {
                    showTaskStage.toggle()
                }
            } label: {
                HStack(spacing: AppSpacing.iconTextSpacing) {
                    Text(selectedStageTitle ?? "Select Task Stage")
                        .font(AppFont.primary(size: AppFont.Size.caption))
                        .foregroundStyle(Color.addressText2)
                        .lineLimit(1)
                        .truncationMode(.tail)

                    Spacer()

                    Image(systemName: "chevron.down")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 10.17, height: 5.75)
                        .foregroundStyle(Color.addressText2)
                        .rotationEffect(.degrees(showTaskStage ? 180 : 0))
                }
                .padding(.horizontal, AppSpacing.md)
                .frame(maxWidth: .infinity)
                .frame(minHeight: AppLayout.buttonHeight)
                .background(Color.rectangleBG)
                .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
            }
            .buttonStyle(.plain)

            if showTaskStage {
                OptionView()
                    .transition(.opacity.combined(with: .move(edge: .top)))
                    .zIndex(1)
            }
        }
        .zIndex(showTaskStage ? 1 : 0)
    }
    
    func OptionView() -> some View {
        RoundedRectangle(cornerRadius: AppRadius.small)
            .fill(Color.rectangleBG)
            .frame(height: 225)
            .overlay {
                ScrollView {
                    VStack(spacing: AppSpacing.stackSpacingMedium) {
                        ForEach(options, id: \.id) { option in
                            Button {
                                withAnimation {
                                    selectedStage = option
                                    showTaskStage.toggle()
                                    selectedStageTitle = selectedStage?.tagName
                                }
                            } label: {
                                HStack(spacing: AppSpacing.iconTextSpacing) {
                                    Circle()
                                        .fill(Color.notification.opacity(0.3))
                                        .frame(width: 10, height: 10)
                                        .overlay {
                                            Circle()
                                                .fill(Color.notification)
                                                .frame(width: 6, height: 6)
                                        }

                                    Text(option.tagName)
                                        .font(AppFont.primary(size: AppFont.Size.caption))
                                        .foregroundStyle(Color.addressText2)
                                        .frame(maxWidth: .infinity, alignment: .leading)
                                        .lineLimit(1)
                                        .truncationMode(.tail)
                                }
                                .padding(.vertical, AppSpacing.xs)
                                .frame(maxWidth: .infinity, alignment: .leading)
                            }
                            .buttonStyle(.plain)
                        }
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity ,alignment: .topLeading)
                    .padding(AppSpacing.md)
                }
                .scrollDisabled(options.count <= 6)
                .scrollIndicators(.hidden)
            }
    }
}

//#Preview {
//    TaskStageDropDownView(selectionTitle: "Select task stage")
//}
