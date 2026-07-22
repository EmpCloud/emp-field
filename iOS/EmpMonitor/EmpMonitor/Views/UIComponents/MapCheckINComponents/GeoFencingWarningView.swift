//
//  GeoFencingWarningView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 21/08/24.
//

import SwiftUI

struct GeoFencingWarningView: View {
    var body: some View {
        RoundedRectangle(cornerRadius: 15)
            .fill(Color.red)
            .frame(height: 68)
            .overlay {
                HStack{
                    Image(.geoFencingWarning)
                    VStack {
                        Text("You're outside geofenced perimeter!")
                            .font(AppFont.primary(size: AppFont.Size.body))
                            .fontWeight(AppFont.Weight.semibold)
                        Text("Check In / Check Out Not Allowed")
                            .font(AppFont.primary(size: AppFont.Size.callout))
                            .fontWeight(AppFont.Weight.medium)
                    }
                    
                }
                .foregroundStyle(Color.white)
            }
    }
}

#Preview {
    GeoFencingWarningView()
}
