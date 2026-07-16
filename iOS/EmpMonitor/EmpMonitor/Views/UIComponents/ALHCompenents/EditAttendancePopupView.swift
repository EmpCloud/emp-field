//
//  EditAttendancePopupView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 29/07/24.
//

import SwiftUI

struct EditAttendancePopupView: View {
    
    @ObservedObject var editAttendanceViewModel: EditAttendanceViewModel
    
    @ObservedObject var dateViewModel: DateViewModel
    
    @Binding var showEditAttendance: Bool
    
    @Binding var checkINTime: String
    @Binding var checkOUTTime: String
    
    
    var body: some View {
        ZStack {
//            Color.black.ignoresSafeArea()
            
            VStack(alignment: .center, spacing: 20) {
                HStack {
                    HStack {
                        Text("Edit Attendance")
                    }
                    .frame(maxWidth: .infinity, alignment: .center)
                    Image(systemName: "xmark")
                        .padding(10)
                        .onTapGesture {
                            withAnimation {
                                showEditAttendance.toggle()
                            }
                        }
                }
                .foregroundStyle(Color.attendanceTitleText)
                .font(.custom("Montserrat", size: 14))
                .frame(maxWidth: .infinity, alignment: .trailing)
                .padding(.horizontal)
                .padding(.top)
                
                HStack(spacing: 0) {
                    Text("Change your attendance on ")
                        .foregroundStyle(Color.text1)
                    Text("\(FormatterHelper.shared.formattedFullYearDate(from: editAttendanceViewModel.date))")
                        .foregroundStyle(Color.primaryButton1)
                }
                .font(.custom("Montserrat", size: 11))
                
                HStack(spacing: 30) {
                    VStack(alignment: .leading, spacing: 10) {
                        HStack(spacing: 0) {
                            Text("Check In Date")
                                .font(.custom("Montserrat", size: 12))
                            Text("*")
                                .foregroundStyle(Color.red)
                        }
                        Text("\(FormatterHelper.shared.formattedFullYearDate(from: editAttendanceViewModel.date))")
                            .font(.custom("Montserrat", size: 10))
                            .foregroundStyle(Color.addressText2)
                            .padding(10)
                            .padding(.trailing, 30)
                            .background(Color.rectangleBG)
                            .clipShape(RoundedRectangle(cornerRadius: 6))
                        
                        HStack(spacing: 0) {
                            Text("Check Out Date")
                                .font(.custom("Montserrat", size: 12))
                            Text("*")
                                .foregroundStyle(Color.red)
                        }
                        Text("\(FormatterHelper.shared.formattedFullYearDate(from: editAttendanceViewModel.date))")
                            .font(.custom("Montserrat", size: 10))
                            .foregroundStyle(Color.addressText2)
                            .padding(10)
                            .padding(.trailing, 30)
                            .background(Color.rectangleBG)
                            .clipShape(RoundedRectangle(cornerRadius: 6))
                    }
                    
                    VStack(alignment: .leading, spacing: 10) {
                        HStack(spacing: 0) {
                            Text("Check In Time")
                                .font(.custom("Montserrat", size: 12))
                            Text("*")
                                .foregroundStyle(Color.red)
                        }
                        HStack {
                            if checkINTime != "" {
                                Text("\(checkINTime)")
                            }else{
                                Text("--:-- --")
                            }
                            
                            
                            Image(.clock)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 10, height: 10)
                                .padding(.leading, 60)
                        }
                        .font(.custom("Montserrat", size: 10))
                        .foregroundStyle(Color.addressText2)
                        .padding(10)
                        .frame(width: 144)
                        .background(Color.rectangleBG)
                        .clipShape(RoundedRectangle(cornerRadius: 6))
                        .onTapGesture {
                            dateViewModel.setTime()
                            withAnimation {
                                dateViewModel.setStartTime = true
                                dateViewModel.setStopTime = false
                                dateViewModel.showPicker.toggle()
                            }
                        }
                        
                        HStack(spacing: 0) {
                            Text("Check Out Time")
                                .font(.custom("Montserrat", size: 12))
                            Text("*")
                                .foregroundStyle(Color.red)
                        }
                        HStack {
                            
                            if checkOUTTime != "" {
                                Text("\(checkOUTTime)")
                            }else{
                                Text("--:-- --")
                            }
                            
                            Image(.clock)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(width: 10, height: 10)
                                .padding(.leading, 60)
                        }
                        .font(.custom("Montserrat", size: 10))
                        .foregroundStyle(Color.addressText2)
                        .padding(10)
                        .frame(width: 144)
                        .background(Color.rectangleBG)
                        .clipShape(RoundedRectangle(cornerRadius: 6))
                        .onTapGesture {
                            dateViewModel.setTime()
                            withAnimation {
                                dateViewModel.setStartTime = false
                                dateViewModel.setStopTime = true
                                dateViewModel.showPicker.toggle()
                            }
                        }
                    }
                }
                
                VStack {
                    HStack(spacing: 0) {
                        Text("Remark")
                            .font(.custom("Montserrat", size: 12))
                        
                        Text("*")
                            .foregroundStyle(Color.red)
                    }
                    .frame(maxWidth: .infinity,alignment: .leading)
                    
                    LargeTextEditorView(descriptionText: $editAttendanceViewModel.reason)
                        .toolbarDoneButton()
                    
                }
                .padding(.horizontal, 50)
                
                PrimaryThinButton(text: "Apply") {
                    //TODO: Apple for attendance change
                    Task {
                        editAttendanceViewModel.checkIN = editAttendanceViewModel.formatToISO8601(dateString: FormatterHelper.shared.formattedFullYearDate(from: editAttendanceViewModel.date), timeString: checkINTime) ?? ""
                        editAttendanceViewModel.checkOUT = editAttendanceViewModel.formatToISO8601(dateString: FormatterHelper.shared.formattedFullYearDate(from: editAttendanceViewModel.date), timeString: checkOUTTime) ?? ""
                        
//                        AppLog.debug("CheckIn: \(editAttendanceViewModel.checkIN)")
//                        AppLog.debug("Checkout: \(editAttendanceViewModel.checkOUT)")
                        
                        await editAttendanceViewModel.editAttendanceData()
                        
                        if NetworkManager.shared.statusCode == 200 {
                            
                            showEditAttendance.toggle()
                        }
                    }
                }
                .padding(.horizontal, 50)
                .padding(.bottom)
                
            }
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: 10))
        }
        
        
    }
}

#Preview {
    EditAttendancePopupView(editAttendanceViewModel: EditAttendanceViewModel(), dateViewModel: DateViewModel(), showEditAttendance: .constant(false), checkINTime: .constant(""), checkOUTTime: .constant(""))
}
