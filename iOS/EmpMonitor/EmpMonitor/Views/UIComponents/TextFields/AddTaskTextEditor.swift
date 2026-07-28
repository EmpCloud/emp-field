//
//  AddTaskTextEditor.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI
import UniformTypeIdentifiers

struct AddTaskTextEditor: View {
    
    @Binding var selectedPDF: [URL]
    @Binding var isDocumentPickerPresented: Bool
    
    @Binding var descriptionText: String
    
    var body: some View {
        VStack(alignment: .leading) {
            TextEditor(text: $descriptionText)
                .font(AppFont.primary(size: AppFont.Size.caption))
                .foregroundStyle(Color.addressText2)
                .scrollContentBackground(.hidden)
                .padding(.trailing, 60)
                .background(Color.white)
                .overlay {
                    RoundedRectangle(cornerRadius: AppRadius.small)
                        .stroke(Color.taskSearchBar.opacity(0.22), lineWidth: 1)
                }
                .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
                .padding(.top, 2)
                .frame(maxWidth: .infinity)
                .frame(height: 81)
                .overlay(alignment: .topLeading) {
                    HStack {
                        if descriptionText.isEmpty {
                            Text("Task description")
                                .font(AppFont.primary(size: AppFont.Size.caption))
                                .foregroundStyle(Color.addressText2)
                                .padding(10)
                                .allowsHitTesting(false)
                        }
                        Spacer()
                        
                        Button {
                            UIApplication.shared.dismissKeyboard()
                            isDocumentPickerPresented.toggle()
                        } label: {
                            Circle()
                                .fill(
                                    .shadow(.inner(color: Color.blueInnerShadow, radius: 7))
                                )
                                .foregroundStyle(.white)
                                .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                                .overlay {
                                    Image(.docIcon)
                                        .resizable()
                                        .aspectRatio(contentMode: .fit)
                                        .frame(width: AppLayout.iconExtraSmall, height: AppLayout.iconExtraSmall)
                                }
                                .padding(.horizontal, AppSpacing.sm)
                                .padding(.top, AppSpacing.xs)
                        }
                        .buttonStyle(.plain)
                        .accessibilityLabel("Add PDF")
                        
                    }
                }
            
            //Warning for 2 doc.
            if selectedPDF.count > 0 {
                HStack {
                    Text("Warning:")
                        .font(AppFont.primary(size: AppFont.Size.xSmall))
                        .fontWeight(AppFont.Weight.semibold)
                    Text("You can only add upto 2 pdf (PDF =  \(selectedPDF.count))")
                        .font(AppFont.primary(size: AppFont.Size.nano))
                        .fontWeight(AppFont.Weight.medium)
                }
                .foregroundStyle(Color.absent)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)
            }
            VStack(alignment: .leading, spacing: AppSpacing.stackSpacingSmall) {
                //Selected PDF
                ForEach(selectedPDF.indices, id: \.self) { index in
	//                    if let selectedPDF = selectedPDF {
                    HStack(spacing: AppSpacing.sm) {
                        Text("Selected PDF: \(selectedPDF[index].lastPathComponent)")
                            .font(AppFont.primary(size: AppFont.Size.caption))
                            .foregroundStyle(Color.addressText2)
                            .lineLimit(2)

                        Button(action: {
                            self.selectedPDF.remove(at: index)
                        }) {
                            Image(systemName: "xmark.circle.fill")
                                .foregroundColor(.red)
                                .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                        }
                        .buttonStyle(.plain)
                        .accessibilityLabel("Remove selected PDF")
                    }
                    .padding(.horizontal, AppSpacing.sm)

	//                    }
                }
                
            }
        }
    }
}

//#Preview {
//    AddTaskTextEditor(selectedPDF: .constant([nil]), isDocumentPickerPresented:.constant(false)  ,descriptionText: .constant(""))
//}
