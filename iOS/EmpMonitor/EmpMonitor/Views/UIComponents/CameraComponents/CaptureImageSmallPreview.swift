//
//  CaptureImageSmallPreview.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 29/08/24.
//

import SwiftUI

struct CaptureImageSmallPreview: View {
    
    @ObservedObject var cameraViewModel: CameraViewModel
    
    @Binding var selectedImage: UIImage?
    @Binding var showImagePreview: Bool
    @Binding var savedImageURLs: [URL]
    
    var body: some View {
        ScrollView(.horizontal) {
            HStack {
                ForEach(cameraViewModel.savedImages.indices, id: \.self) { index in
                    VStack {
                        Image(uiImage: cameraViewModel.savedImages[index].image)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 100, height: 80)
                            .clipShape(RoundedRectangle(cornerRadius: 5))
                            .onTapGesture {
                                selectedImage = cameraViewModel.savedImages[index].image
                                showImagePreview.toggle()
                            }
                        
                        Button {
                            //                            if let index = cameraViewModel.capturedImages.firstIndex(of: capturedImage) {
                            cameraViewModel.savedImages.remove(at: index)
                            savedImageURLs.removeAll()
                            savedImageURLs = cameraViewModel.getCapturedImageURLs()
                            //                            }
                            
                        } label: {
                            Image(systemName: "xmark.circle.fill")
                                .foregroundColor(.red)
                                .padding(4)
                        }
                    }
                    .padding(.trailing, 8)
                }
            }
            .padding(.horizontal)
        }
    }
}

//#Preview {
//    CaptureImageSmallPreview(cameraViewModel: CameraViewModel(), showImagePreview: .constant(false))
//}
