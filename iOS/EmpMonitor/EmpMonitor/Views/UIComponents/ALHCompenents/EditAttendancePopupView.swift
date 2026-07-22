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
            
            VStack(alignment: .leading, spacing: AppSpacing.stackSpacingMedium) {
                ZStack {
                    Text("Edit Attendance")
                        .frame(maxWidth: .infinity, alignment: .center)

                    HStack {
                        Spacer()

                        Button {
                            withAnimation {
                                showEditAttendance.toggle()
                            }
                        } label: {
                            Image(systemName: "xmark")
                                .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                        }
                        .buttonStyle(.plain)
                        .accessibilityLabel("Close edit attendance")
                    }
                }
                .foregroundStyle(Color.attendanceTitleText)
                .font(AppFont.primary(size: AppFont.Size.body))
                .fontWeight(AppFont.Weight.semibold)
                .frame(maxWidth: .infinity)
                
                HStack(spacing: AppSpacing.xxs) {
                    Text("Change your attendance on ")
                        .foregroundStyle(Color.text1)
                    Text(attendanceDateText)
                        .foregroundStyle(Color.primaryButton1)
                        .lineLimit(1)
                        .minimumScaleFactor(0.85)
                }
                .font(AppFont.primary(size: AppFont.Size.footnote))
                .frame(maxWidth: .infinity, alignment: .center)
                
                HStack(alignment: .top, spacing: AppSpacing.stackSpacingDefault) {
                    VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
                        requiredLabel("Check In Date")
                        readOnlyDateField(attendanceDateText)
                        
                        requiredLabel("Check Out Date")
                        readOnlyDateField(attendanceDateText)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    
                    VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
                        requiredLabel("Check In Time")
                        timePickerButton(checkINTime) {
                            dateViewModel.setTime()
                            withAnimation {
                                dateViewModel.setStartTime = true
                                dateViewModel.setStopTime = false
                                dateViewModel.showPicker.toggle()
                            }
                        }
                        
                        requiredLabel("Check Out Time")
                        timePickerButton(checkOUTTime) {
                            dateViewModel.setTime()
                            withAnimation {
                                dateViewModel.setStartTime = false
                                dateViewModel.setStopTime = true
                                dateViewModel.showPicker.toggle()
                            }
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
                
                VStack(spacing: AppSpacing.stackSpacingDefault) {
                    requiredLabel("Remark")
                    .frame(maxWidth: .infinity,alignment: .leading)
                    
                    LargeTextEditorView(descriptionText: $editAttendanceViewModel.reason)
                        .toolbarDoneButton()
                    
                }
                
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
                
            }
            .frame(maxWidth: 360)
            .padding(.horizontal, AppSpacing.lg)
            .padding(.vertical, AppSpacing.md)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
        }
        
        
    }

    private var attendanceDateText: String {
        FormatterHelper.shared.formattedFullYearDate(from: editAttendanceViewModel.date)
    }

    private func requiredLabel(_ title: String) -> some View {
        HStack(spacing: AppSpacing.zero) {
            Text(title)
            Text("*")
                .foregroundStyle(Color.red)
        }
        .font(AppFont.primary(size: AppFont.Size.caption))
        .foregroundStyle(Color.text1)
    }

    private func readOnlyDateField(_ text: String) -> some View {
        Text(text)
            .font(AppFont.primary(size: AppFont.Size.xSmall))
            .foregroundStyle(Color.addressText2)
            .lineLimit(1)
            .minimumScaleFactor(0.75)
            .padding(.horizontal, AppSpacing.sm)
            .frame(maxWidth: .infinity, alignment: .leading)
            .frame(minHeight: AppLayout.minimumTouchTarget)
            .background(Color.rectangleBG)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
    }

    private func timePickerButton(_ text: String, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            HStack(spacing: AppSpacing.sm) {
                Text(text.isEmpty ? "--:-- --" : text)
                    .lineLimit(1)
                    .minimumScaleFactor(0.8)

                Spacer(minLength: AppSpacing.zero)

                Image(.clock)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: AppLayout.iconExtraSmall, height: AppLayout.iconExtraSmall)
            }
            .font(AppFont.primary(size: AppFont.Size.xSmall))
            .foregroundStyle(Color.addressText2)
            .padding(.horizontal, AppSpacing.sm)
            .frame(maxWidth: .infinity)
            .frame(minHeight: AppLayout.minimumTouchTarget)
            .background(Color.rectangleBG)
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
        }
        .buttonStyle(.plain)
    }
}

#Preview {
    EditAttendancePopupView(editAttendanceViewModel: EditAttendanceViewModel(), dateViewModel: DateViewModel(), showEditAttendance: .constant(false), checkINTime: .constant(""), checkOUTTime: .constant(""))
}
