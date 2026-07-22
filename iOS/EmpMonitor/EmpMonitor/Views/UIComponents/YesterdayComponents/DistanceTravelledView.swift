//
//  DistanceTravelledView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 15/07/24.
//

import SwiftUI

struct DistanceTravelledView: View {
    
    @ObservedObject var homeScreenViewModel: HomeScreenViewModel
    
    @State private var formattedDistance: String = "--"
    
    var body: some View {
        VStack {
            RoundedRectangle(cornerRadius: 10)
                .fill(Color.rectangleBG)
                .frame(width: 106 ,height: 86)
                .overlay {
                    VStack{
                        Circle()
                            .fill(
                                .shadow(.inner(color: Color.blueInnerShadow, radius: 7))
                            )
                            .foregroundStyle(.white)
                            .frame(width: 42, height: 43)
                            .overlay {
                                Image(.pathPolygon)
                            }
                        
                        Text("\(homeScreenViewModel.yesterdayDist)KM" )
                            .font(AppFont.primary(size: AppFont.Size.body))
                            .fontWeight(AppFont.Weight.semibold)
                            .foregroundStyle(Color.yesterdaysDate)
                        
                    }
            }
            
            Text("Distance Travelled")
                .font(AppFont.primary(size: AppFont.Size.caption))
                .fontWeight(AppFont.Weight.medium)
                .foregroundStyle(Color.yesterdayText)
        }
        .onAppear {
//            if let distance = homeScreenViewModel.yesterdayDist {
//                    
//                    switch distance {
//                    case .double(let doubleValue):
//                        // Round off to 2 decimal places
//                        formattedDistance = String(format: "%.2f", doubleValue)
//                    case .string(let stringValue):
//                        formattedDistance = stringValue
//                    }
//                
//            }
        }
    }
}

#Preview {
    DistanceTravelledView(homeScreenViewModel: HomeScreenViewModel())
}
