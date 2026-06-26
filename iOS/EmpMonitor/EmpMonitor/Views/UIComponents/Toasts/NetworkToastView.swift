//
//  NetworkToastView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 23/09/24.
//

import SwiftUI

struct NetworkToastView: View {
    
    var text: String
    
    var body: some View {
        ZStack{
            Text(text)
                .font(.custom("Montserrat", size: 12))
                .fontWeight(.medium)
                .foregroundStyle(Color.white)
                .multilineTextAlignment(.center)
                .padding(.top, 20)
                .padding(.horizontal, 10)
                
        }
        .padding()
//        .frame(minheight: 100, alignment: .bottom)
        .frame(maxWidth: .infinity, minHeight: 100, alignment: .bottom)
        .background(Color.black)
        .clipShape(Capsule())
        .padding(10)
        
//        .frame(maxWidth: .infinity, maxHeight: .infinity)
//        .background(Color.red)
//        .shadow(color: .black.opacity(0.5), radius: 3, y: 3)
        
        
    }
}

#Preview {
    NetworkToastView(text: "Network Response wuegdfhhj jbfh kjwbf hdh hdb hdsj djhhd b  db h cj jd  j xkj bcjcj   nkjdbncicc    kcnbcjk  ncv j  d")
//        .background(Color.blue)
//    ContentView()
//        .environmentObject(AppState())
//        .environmentObject(CreateProfileViewModel())
}
