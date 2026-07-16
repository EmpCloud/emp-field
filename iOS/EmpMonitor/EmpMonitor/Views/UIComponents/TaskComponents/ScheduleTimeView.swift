//
//  ScheduleTimeView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 22/08/24.
//

import SwiftUI

struct ScheduleStartTimeView: View {
    
//    @ObservedObject var dateViewModel: DateViewModel
    @State private var currentTime = Date()
    
    @Binding var startTime: String
    
    
    var body: some View {
        VStack {
            //            Text("Start Time")
            //                .font(.custom("Montserrat", size: 14))
            //                .fontWeight(.bold)
            //                .foregroundStyle(Color.subText)
            RoundedRectangle(cornerRadius: 6)
                .fill(Color.white)
                .frame(height: 56)
                .overlay {
                    HStack{
                        //                        if startTime != "" {
                        //                            Text(startTime)
                        //                                .font(.custom("Montserrat", size: 12))
                        //                                .fontWeight(.bold)
                        //                                .foregroundStyle(Color.gray.opacity(0.7))
                        //                        }else{
                        //                            Text("Start Time")
                        //                                .font(.custom("Montserrat", size: 12))
                        //                                .fontWeight(.bold)
                        //                                .foregroundStyle(Color.gray.opacity(0.7))
                        //                        }
                        
                        DatePicker("Start Time", selection: $currentTime, in: Date()... ,displayedComponents: .hourAndMinute)
                            .labelsHidden()
                            .onChange(of: currentTime) { _, value in
                                startTime = formatTime(date: value)
                                AppLog.debug("Current Time: \(value)")
                                AppLog.debug("Start Time: \(startTime)")

                            }
                            .colorInvert()
                            .colorMultiply(.present)
                        
                        
                        Spacer()
                        
                        Circle()
                            .fill(
                                .shadow(.inner(color: Color.taskSearchBar.opacity(0.5), radius: 4))
                            )
                            .foregroundStyle(Color.white)
                            .frame(width: 35.46, height: 35.46)
                            .overlay {
                                Image(systemName: "clock")
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(width: 17.71, height: 17.71)
                                    .foregroundStyle(Color.primaryButton1)
                            }
                    }
                    .padding(.horizontal)
                }
        }
    }
    
    private func formatTime(date: Date) -> String {
        let formatter = DateFormatter()
        formatter.timeZone = TimeZone.current  // Use the current local time zone
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss"  // Desired output format
        return formatter.string(from: date)
    }
}

struct ScheduleStopTimeView: View {
    
//    @ObservedObject var dateViewModel: DateViewModel
    @Binding var stopTime: String
    @State private var currentTime = Date()
    
    var body: some View {
        VStack {
//            Text("Start Time")
//                .font(.custom("Montserrat", size: 14))
//                .fontWeight(.bold)
//                .foregroundStyle(Color.subText)
            RoundedRectangle(cornerRadius: 6)
                .fill(Color.white)
                .frame(height: 56)
                .overlay {
                    HStack{
//                        if stopTime != "" {
//                            Text(stopTime)
//                                .font(.custom("Montserrat", size: 12))
//                                .fontWeight(.bold)
//                                .foregroundStyle(Color.gray.opacity(0.7))
//                        }else{
//                            Text("Stop Time")
//                                .font(.custom("Montserrat", size: 12))
//                                .fontWeight(.bold)
//                                .foregroundStyle(Color.gray.opacity(0.7))
//                        }
                        
                        DatePicker("Stop Time", selection: $currentTime, in: Date()... ,displayedComponents: .hourAndMinute)
                            .labelsHidden()
                            .onChange(of: currentTime) { _, value in
                                stopTime = formatTime(date: currentTime)
                                AppLog.debug("stopTime: \(stopTime)")
                            }
                            .colorInvert()
                            .colorMultiply(.absent)
                            
                        
                        Spacer()
                        
                        Circle()
                            .fill(
                                .shadow(.inner(color: Color.taskSearchBar.opacity(0.5), radius: 4))
                            )
                            .foregroundStyle(Color.white)
                            .frame(width: 35.46, height: 35.46)
                            .overlay {
                                Image(systemName: "clock")
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(width: 17.71, height: 17.71)
                                    .foregroundStyle(Color.primaryButton1)
                            }
                    }
                    .padding(.horizontal)
                }
        }
    }
    private func formatTime(date: Date) -> String {
        let formatter = DateFormatter()
        formatter.timeZone = TimeZone.current  // Use the current local time zone
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss"  // Desired output format
        return formatter.string(from: date)
    }
}

#Preview {
    ScheduleStartTimeView(startTime: .constant(""))
}

//#Preview {
//    ScheduleStopTimeView(stopTime: .constant(""))
//}

