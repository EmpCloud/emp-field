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
        
        ZStack {
            VStack(spacing: 5) {
                Spacer()
                RoundedRectangle(cornerRadius: 6)
                    .fill(Color.rectangleBG)
                    .frame(height: 43)
                    .overlay {
                        HStack {
                            Text(selectedStageTitle ?? "Select task stage")
                                .font(.custom("Montserrat", size: 10))
                                .foregroundStyle(Color.addressText2)
                            
                            Spacer()
                            
                            Image(systemName: "chevron.down")
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 10.17, height: 5.75)
                                .foregroundStyle(Color.addressText2)
                        }
                        .padding(.horizontal)
                    }
    //        .frame(maxWidth: .infinity, maxHeight: .infinity , alignment: .top)
                    .onTapGesture {
                        withAnimation {
                            showTaskStage.toggle()
                        }
                    }
                    .padding(.bottom, 460)
            }
            
            ZStack{
                if showTaskStage {
                    OptionView()
                        .padding(.top, 60)
                }

            }

        }
//        .padding(.top, -45)
        
        
    }
    
    func OptionView() -> some View {
        RoundedRectangle(cornerRadius: 6)
            .fill(Color.rectangleBG)
            .frame(height: 225)
            .overlay {
                ScrollView {
                    VStack(spacing: 15) {
                        ForEach(options, id: \.id) { option in
                            HStack {
                                Circle()
                                    .fill(Color.notification.opacity(0.3))
                                    .frame(width: 10, height: 10)
                                    .overlay {
                                        Circle()
                                            .fill(Color.notification)
                                            .frame(width: 6, height: 6)
                                    }
                                Text(option.tagName)
                                    .font(.custom("Montserrat", size: 10))
                                    .foregroundStyle(Color.addressText2)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .padding(.vertical, 4)
                                    .onTapGesture {
                                        withAnimation {
                                            selectedStage = option
                                            showTaskStage.toggle()
                                            selectedStageTitle = selectedStage?.tagName
                                        }
                                    }
                            }
                            
                        }
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity ,alignment: .topLeading)
                    .padding()
                }
                .scrollDisabled(options.count <= 6)
                .scrollIndicators(.hidden)
                
            }
    }
}

//#Preview {
//    TaskStageDropDownView(selectionTitle: "Select task stage")
//}
