//
//  VideoTutorialView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI

struct VideoTutorialView: View {

    var youtubeURL: URL {
        URL(string: Constants.StaticURL.videoTutorial) ?? URL(fileURLWithPath: "")
    }
    
    var body: some View {
        ZStack(alignment: .top) {
            Color.white
                .ignoresSafeArea(.all)
            
            Image(.topSetupScreenBg)
                .ignoresSafeArea()
            
            VStack {
                
//                Image(.videoTutorial)  // Here i want to setup a webYoutube
                RoundedRectangle(cornerRadius: 20)
                    .fill(
                        LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                    )
                    .frame(width: 370, height: 207)
                    .overlay {
                        YoutubeWebView(url: youtubeURL)
                            .frame(width: 350, height: 187)
                            .clipShape(RoundedRectangle(cornerRadius: 10))
//                            .padding(10)
                    }
                    .padding(.top, 170)
                
                    
                    
                
                HStack{
                    Text("Video")
                        .font(.custom("Montserrat", size: 25))
                        .fontWeight(.semibold)
                        .foregroundStyle(Color.welcomeText)
                    
                    Text("Tutorial")
                        .font(.custom("Montserrat", size: 25))
                        .fontWeight(.semibold)
                        .foregroundStyle(Color.sideTitleText)
                }
                .padding(.top, 100)
                
                Text("Activity tracking permission")
                    .font(.custom("Montserrat", size: 16))
                    .foregroundStyle(Color.subText)
                    .padding()
            }

        }
    }
}

#Preview {
    VideoTutorialView()
}
