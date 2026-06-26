//
//  ImagePreviewView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 27/08/24.
//

import SwiftUI

struct ImagePreviewView: View {
    let image: UIImage
    var body: some View {
        Image(uiImage: image)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .background(Color.black)
            .ignoresSafeArea()
    }
}

//#Preview {
//    ImagePreviewView(image: UIImage)
//}
