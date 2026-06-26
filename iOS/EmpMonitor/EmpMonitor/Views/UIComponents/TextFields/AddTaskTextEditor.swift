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
                .font(.custom("Montserrat", size: 12))
                .foregroundStyle(Color.addressText2)
                .scrollContentBackground(.hidden)
                .padding(.trailing, 60)
                .background(Color.white)
                .clipShape(RoundedRectangle(cornerRadius: 6))
                .padding(.top, 2)
                .frame(maxWidth: .infinity)
                .frame(height: 81)
                .overlay(alignment: .topLeading) {
                    HStack {
                        if descriptionText.isEmpty {
                            Text("Reason of meeting")
                                .font(.custom("Montserrat", size: 12))
                                .foregroundStyle(Color.addressText2)
                                .padding(10)
                        }
                        Spacer()
                        
                        Circle()
                            .fill(
                                .shadow(.inner(color: Color.blueInnerShadow, radius: 7))
                            )
                            .foregroundStyle(.white)
                            .frame(width: 38.54, height: 38.54)
                            .overlay {
                                Image(.docIcon)
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(width: 16.83, height: 16.5)
                            }
                            .padding(.horizontal)
                            .offset(y: 5)
                            .onTapGesture {
                                isDocumentPickerPresented.toggle()
                            }
                        
                    }
                }
            
            //Warning for 2 doc.
            if selectedPDF.count > 0 {
                HStack {
                    Text("Warning:")
                        .font(.custom("Montserrat", size: 10))
                        .fontWeight(.semibold)
                    Text("You can only add upto 2 pdf (PDF =  \(selectedPDF.count))")
                        .font(.custom("Montserrat", size: 8))
                        .fontWeight(.medium)
                }
                .foregroundStyle(Color.absent)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)
            }
            HStack {
                //Selected PDF
                ForEach(selectedPDF.indices, id: \.self) { index in
//                    if let selectedPDF = selectedPDF {
                    Text("Selected PDF: \(selectedPDF[index].lastPathComponent)")
                            .font(.custom("Montserrat", size: 12))
                            .foregroundStyle(Color.addressText2)
                            .padding(10)
                        
                        Button(action: {
                            self.selectedPDF.remove(at: index)
                        }) {
                            Image(systemName: "xmark.circle.fill")
                                .foregroundColor(.red)
                                .padding(.trailing)
                        }

//                    }
                }
                
            }
        }
    }
}

//#Preview {
//    AddTaskTextEditor(selectedPDF: .constant([nil]), isDocumentPickerPresented:.constant(false)  ,descriptionText: .constant(""))
//}
