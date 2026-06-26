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
            if holidaysViewModel.fetchStatusCode == 200 {
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
                            // only to display the upcoming holidays
//                            if FormatterHelper.shared.isFutureDate(dateString: holiday.holidayDate) {
                                Text(holiday.holidayName)
                                    .frame(width: 200, alignment: .leading)
                                    .fontWeight(.medium)
                                    .multilineTextAlignment(.leading)
                                Text(FormatterHelper.shared.formattedDateWithDay(from: holiday.holidayDate))
                                .foregroundStyle(FormatterHelper.shared.isFutureDate(dateString: holiday.holidayDate) ? Color.primaryButton1 : Color.black)
//                            }
                        }
                        .font(.custom("Montserrat", size: 12))
                        .padding(.leading, 25)
                        
                        LineView()
                            .padding(15)
                    }
                }
                .background(Color.white)
                .clipShape(RoundedRectangle(cornerRadius: 20))
                
            }else{
                VStack {
                    Image(.noDataFound)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 114, height: 114)
                    Text("No Data Found")
                        .font(.custom("Montserrat", size: 14))
                }
                .frame(height: 500)
                .frame(maxWidth: .infinity)
            }
        }
        .padding(.top, 10)
        .overlay {
            if holidaysViewModel.isLoading {
                VStack {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle())
                        .scaleEffect(1.2)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.black.opacity(0.05))
            }
        }
        .onAppear {
            Task {
                await holidaysViewModel.getHolidays()
            }
        }
    }
}

#Preview {
    HolidaysView()
}
