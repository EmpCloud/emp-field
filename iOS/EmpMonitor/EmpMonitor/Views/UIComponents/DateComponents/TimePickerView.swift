//
//  TimePickerView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct TimePickerView: View {
    @ObservedObject var dateViewModel: DateViewModel

    @Binding var startTime: String
    @Binding var stopTime: String
    
//    @Binding var showWarningPopup: Bool

    var body: some View {
        ZStack {
            VStack(spacing: 0) {
                HStack(spacing: 18) {
                    Spacer()
                    HStack(spacing: 0) {
                        Text("\(dateViewModel.hour):")
                            .font(AppFont.primary(size: AppFont.Size.subheadline))
                            .fontWeight(dateViewModel.changeToMin ? AppFont.Weight.light : AppFont.Weight.bold)
                            .onTapGesture {
                                dateViewModel.angle = Double(dateViewModel.hour * 30)
                                dateViewModel.changeToMin = false
                            }

                        Text("\(dateViewModel.minutes < 10 ? "0" : "")\(dateViewModel.minutes)")
                            .font(AppFont.primary(size: AppFont.Size.subheadline))
                            .fontWeight(dateViewModel.changeToMin ? AppFont.Weight.bold : AppFont.Weight.light)
                            .onTapGesture {
                                dateViewModel.angle = Double(dateViewModel.minutes * 6)
                                dateViewModel.changeToMin = true
                            }
                    }

                    VStack(spacing: 8) {
                        Text("AM")
                            .font(AppFont.primary(size: AppFont.Size.caption))
                            .fontWeight(dateViewModel.symbol == "AM" ? AppFont.Weight.bold : AppFont.Weight.light)
                            .onTapGesture {
                                dateViewModel.symbol = "AM"
                            }

                        Text("PM")
                            .font(AppFont.primary(size: AppFont.Size.caption))
                            .fontWeight(dateViewModel.symbol == "PM" ? AppFont.Weight.bold : AppFont.Weight.light)
                            .onTapGesture {
                                dateViewModel.symbol = "PM"
                            }
                    }
                    .frame(width: 50)
                }
                .padding()
                .foregroundStyle(Color.white)

                GeometryReader { reader in
                    ZStack {
                        let width = reader.frame(in: .global).width / 2

                        Circle()
                            .fill(Color.primaryButton1)
                            .frame(width: 40, height: 40)
                            .offset(x: width - 50)
                            .rotationEffect(.init(degrees: dateViewModel.angle))
                            .gesture(DragGesture().onChanged(onChanged(value:))
                                .onEnded(onEnd(value:)))
                            .rotationEffect(.init(degrees: -90))

                        ForEach(1...12, id: \.self) { index in
                            VStack {
                                Text("\(dateViewModel.changeToMin ? index * 5 : index)")
                                    .font(AppFont.primary(size: AppFont.Size.caption))
                                    .fontWeight(AppFont.Weight.semibold)
                                    .foregroundStyle(Color.white)
                                    .rotationEffect(.init(degrees: Double(-index) * 30))
                            }
                            .offset(y: -width + 50)
                            .rotationEffect(.init(degrees: Double(index) * 30))
                        }

                        Circle()
                            .fill(Color.primaryButton1)
                            .frame(width: 10, height: 10)
                            .overlay(alignment: .bottom) {
                                Rectangle()
                                    .fill(Color.primaryButton1)
                                    .frame(width: 2, height: width / 2)
                            }
                            .rotationEffect(.init(degrees: dateViewModel.angle))
                    }
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                }
                .frame(height: 300)

                HStack {
                    Spacer()

                    Button {
                        dateViewModel.generateTime()
                        if dateViewModel.setStartTime {
                            startTime = "\(dateViewModel.getFormattedDate())"
//                            AppLog.debug("startTime: \(startTime)")
                            
                        } else if dateViewModel.setStopTime {
                            
                            stopTime = "\(dateViewModel.getFormattedDate())"
//                            AppLog.debug("stopTime: \(stopTime)")
                            
                        }
                        
                        dateViewModel.showPicker = false
                        
                    } label: {
                        Text("Save")
                            .font(AppFont.primary(size: AppFont.Size.subheadline))
                            .fontWeight(AppFont.Weight.bold)
                    }
                }
                .padding()
            }
            .frame(width: getWidth() - 120)
            .background(Color.taskSearchBar)
            .clipShape(RoundedRectangle(cornerRadius: 8))
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .onTapGesture {
                withAnimation {
                    dateViewModel.changeToMin = false
                    dateViewModel.showPicker = true
                }
            }
            
//            if showWarningPopup {
//                ZStack {
//                    WarningPopupView(titleText: "Try Again !", description: "End time should be greater than Start time", showWarningPopup: $showWarningPopup)
//                }
//                .frame(maxWidth: .infinity, maxHeight: .infinity)
//                .background(Color.black.opacity(0.5))
//                .onTapGesture {
//                    showWarningPopup.toggle()
//                }
//            }
        }
        
        
    }

    func onChanged(value: DragGesture.Value) {
        let vector = CGVector(dx: value.location.x, dy: value.location.y)
        let radians = atan2(vector.dy - 20, vector.dx - 20)
        var angle = radians * 180 / .pi

        if angle < 0 { angle = 360 + angle }

        dateViewModel.angle = Double(angle)

        if !dateViewModel.changeToMin {
            let roundValue = 30 * Int(round(dateViewModel.angle / 30))
            dateViewModel.angle = Double(roundValue)
        } else {
            let progress = dateViewModel.angle / 360
            dateViewModel.minutes = Int(progress * 60)
        }
    }

    func onEnd(value: DragGesture.Value) {
        if !dateViewModel.changeToMin {
            dateViewModel.hour = Int(dateViewModel.angle / 30)

            withAnimation {
                dateViewModel.angle = Double(dateViewModel.minutes * 6)
                dateViewModel.changeToMin = true
            }
        }
    }
}

extension View {
    func getWidth() -> CGFloat {
        return UIScreen.main.bounds.width
    }
}


#Preview {
    TimePickerView(dateViewModel: DateViewModel(), startTime: .constant(""), stopTime: .constant(""))
}
