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
                            .font(.custom("Montserrat", size: 14))
                            .fontWeight(.semibold)
                        Text("Not Allowed to Check IN / Check OUT")
                            .font(.custom("Montserrat", size: 13))
                            .fontWeight(.medium)
                    }
                    
                }
                .foregroundStyle(Color.white)
            }
    }
}

#Preview {
    GeoFencingWarningView()
}
