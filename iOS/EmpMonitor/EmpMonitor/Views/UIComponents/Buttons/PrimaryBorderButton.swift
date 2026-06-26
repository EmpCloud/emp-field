//
//  PrimaryBorderButton.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 31/07/24.
//

import SwiftUI

struct PrimaryBorderButton: View {
    
    var text: String
    var action: () -> Void
    
    var body: some View {
        ZStack {
            Button(action: action) {
                Text(text)
                    .font(.custom("Poppins-Regular", size: 15))
                    .foregroundStyle(Color.attendanceTitleText)
                    .padding(10)
                    .frame(maxWidth: .infinity)
                    .overlay {
                        RoundedRectangle(cornerRadius: 10)
                            .stroke(lineWidth: 2)
                    }
                    .clipShape(RoundedRectangle(cornerRadius: 10))
            }
        }
//        .padding()
    }
}

struct RedBorderButton: View {
    
    var text: String
    var action: () -> Void
    
    var body: some View {
        ZStack {
            Button(action: action) {
                HStack {
                    Image(systemName: "pause")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 14.15, height: 17.15)
                        .fontWeight(.bold)
                        .foregroundStyle(Color.absent)
                    
                    Text(text)
                        .font(.custom("Poppins-Regular", size: 15))
                        .foregroundStyle(Color.absent)
                        .padding(10)
                       
                }
                
                .frame(maxWidth: .infinity)
                .overlay {
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(Color.absent, lineWidth: 2)
                }
                .clipShape(RoundedRectangle(cornerRadius: 10))
            }
        }
//        .padding()
    }
}


#Preview {
    PrimaryBorderButton(text: "Primary border button") {
        print(("Print"))
    }
}

#Preview {
    RedBorderButton(text: "Primary border button") {
        print(("Print"))
    }
}
