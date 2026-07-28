//
//  Holidays.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 29/07/24.
//

import SwiftUI

struct HolidaysView: View {
    
    @Binding private var selectedDate: String?
    @StateObject private var holidaysViewModel = HolidaysViewModel()

    init(selectedDate: Binding<String?> = .constant(nil)) {
        _selectedDate = selectedDate
    }

    private var displayedHolidays: [HolidaysResponseData] {
        guard let selectedDate, !selectedDate.isEmpty else {
            return holidaysViewModel.holidaysData
        }

        return holidaysViewModel.holidaysData.filter {
            FormatterHelper.shared.formattedDateReverse(from: $0.holidayDate) == selectedDate
        }
    }
    
    var body: some View {
        ScrollView {
            if holidaysViewModel.isLoading {
                loadingPlaceholder
            } else if holidaysViewModel.holidaysData.isEmpty {
                emptyPlaceholder
            } else {
                VStack(spacing: 0) {
                    HStack {
                        Text("Holidays")
                            .frame(maxWidth: .infinity, alignment: .leading)
                        Text("Dates")
                            .frame(width: 128, alignment: .leading)
                    }
                    .font(AppFont.primary(size: AppFont.Size.headline))
                    .foregroundStyle(Color.attendanceTitleText)
                    .fontWeight(AppFont.Weight.medium)
                    .padding(.horizontal, 20)
                    .padding(.top, 18)
                    .padding(.bottom, 12)

                    if let selectedDate, !selectedDate.isEmpty {
                        HStack(spacing: 8) {
                            Text(FormatterHelper.shared.formattedDateWithDay(from: selectedDate))
                                .font(AppFont.primary(size: AppFont.Size.caption))
                                .fontWeight(AppFont.Weight.medium)
                                .foregroundStyle(Color.primaryButton1)

                            Button {
                                self.selectedDate = nil
                            } label: {
                                Image(systemName: "xmark")
                                    .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.semibold))
                                    .foregroundStyle(Color.primaryButton1)
                                    .frame(width: 22, height: 22)
                            }
                            .buttonStyle(.plain)
                        }
                        .padding(.leading, 12)
                        .padding(.trailing, 4)
                        .padding(.vertical, 6)
                        .background(Color.primaryButton1.opacity(0.08))
                        .clipShape(Capsule())
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(.horizontal, 20)
                        .padding(.bottom, 8)
                    }

                    if displayedHolidays.isEmpty {
                        emptyContent
                            .frame(maxWidth: .infinity, minHeight: 280)
                    } else {
                        ForEach(displayedHolidays, id: \.id) { holiday in
                            HStack(alignment: .top, spacing: 12) {
                                Text(holiday.holidayName)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .fontWeight(AppFont.Weight.medium)
                                    .multilineTextAlignment(.leading)

                                Text(FormatterHelper.shared.formattedDateWithDay(from: holiday.holidayDate))
                                    .frame(width: 128, alignment: .leading)
                                    .foregroundStyle(FormatterHelper.shared.isFutureDate(dateString: holiday.holidayDate) ? Color.primaryButton1 : Color.black)
                            }
                            .font(AppFont.primary(size: AppFont.Size.caption))
                            .padding(.horizontal, 20)
                            .padding(.vertical, 12)

                            LineView()
                                .padding(.horizontal, 15)
                        }
                    }
                }
                .background(Color.white)
                .clipShape(RoundedRectangle(cornerRadius: 20))
            }
        }
        .padding(.top, 10)
        .onAppear {
            Task {
                await holidaysViewModel.getHolidays()
            }
        }
    }
    
    private var loadingPlaceholder: some View {
        VStack(spacing: 12) {
            ProgressView()
                .scaleEffect(1.2)
            Text("Loading...")
                .font(AppFont.primary(size: AppFont.Size.body))
                .foregroundStyle(Color.subText)
        }
        .frame(maxWidth: .infinity, minHeight: 400)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 20))
    }
    
    private var emptyPlaceholder: some View {
        emptyContent
            .frame(maxWidth: .infinity, minHeight: 400)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: 20))
    }

    private var emptyContent: some View {
        VStack(spacing: 12) {
            Image(.noDataFound)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 114, height: 114)
            Text("No holidays found")
                .font(AppFont.primary(size: AppFont.Size.body))
        }
    }
}

#Preview {
    HolidaysView()
}
