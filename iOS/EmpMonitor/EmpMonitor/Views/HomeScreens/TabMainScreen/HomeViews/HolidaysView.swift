//
//  Holidays.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 29/07/24.
//

import SwiftUI

struct HolidaysView: View {
    
    @StateObject private var holidaysViewModel = HolidaysViewModel()
    
    var body: some View {
        ScrollView {
            if holidaysViewModel.isLoading {
                loadingPlaceholder
            } else if holidaysViewModel.holidaysData.isEmpty {
                emptyPlaceholder
            } else {
                LazyVGrid(columns: [GridItem(.fixed(UIScreen.main.bounds.width), alignment: .leading)]) {
                    
                    //MARK: Title
                    LazyHGrid(rows: [GridItem(.fixed(2))] ,spacing: 160) {
                        Text("Holidays")
                        Text("Dates")
                    }
                    .font(.custom("Montserrat", size: 16))
                    .foregroundStyle(Color.attendanceTitleText)
                    .fontWeight(.medium)
                    .padding(.leading, 25)
                    .padding(.vertical)
                    

                    //MARK: Data
                    ForEach(holidaysViewModel.holidaysData, id: \.id) { holiday in
                        LazyHGrid(rows: [GridItem(.fixed(2))], spacing: 30) {
                                Text(holiday.holidayName)
                                    .frame(width: 200, alignment: .leading)
                                    .fontWeight(.medium)
                                    .multilineTextAlignment(.leading)
                                Text(FormatterHelper.shared.formattedDateWithDay(from: holiday.holidayDate))
                                .foregroundStyle(FormatterHelper.shared.isFutureDate(dateString: holiday.holidayDate) ? Color.primaryButton1 : Color.black)
                        }
                        .font(.custom("Montserrat", size: 12))
                        .padding(.leading, 25)
                        
                        LineView()
                            .padding(15)
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
                .font(.custom("Montserrat", size: 14))
                .foregroundStyle(Color.subText)
        }
        .frame(maxWidth: .infinity, minHeight: 400)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 20))
    }
    
    private var emptyPlaceholder: some View {
        VStack(spacing: 12) {
            Image(.noDataFound)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 114, height: 114)
            Text("No holidays found")
                .font(.custom("Montserrat", size: 14))
        }
        .frame(maxWidth: .infinity, minHeight: 400)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 20))
    }
}

#Preview {
    HolidaysView()
}
