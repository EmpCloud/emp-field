//
//  AddressCellView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 27/06/24.
//

import SwiftUI

struct AddressCellView: View {
    
    let locationName: String
    let completeAddress: String
    
    var body: some View {
        VStack {
            HStack(spacing: 30) {
                VStack(alignment: .leading, spacing: 10){
                    Text(locationName)
                        .font(.custom("Montserrat", size: 12))
                        .foregroundStyle(Color.text1)
                    Text(completeAddress)
                        .font(.custom("Montserrat", size: 10))
                        .foregroundStyle(Color.mapSearchBarText)
                }
                Spacer()
                VStack {
                    Image(.addressLocationIcon)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 12, height: 16)
                    Text("12km")
                        .font(.custom("Montserrat", size: 10))
                }
                
            }
            .padding(.horizontal)
            //MARK:
            LineView()
                .padding(.vertical)
                .padding(.horizontal)

        }
        
    }
}

#Preview {
    AddressCellView(locationName: "", completeAddress: "")
}
