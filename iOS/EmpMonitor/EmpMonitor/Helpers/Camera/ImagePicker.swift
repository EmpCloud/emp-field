//
//  ImagePicker.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 26/08/24.
//

import Foundation
import SwiftUI
import PhotosUI

struct ImagePicker: UIViewControllerRepresentable {
    
//    @ObservedObject var cameraViewModel: CameraViewModel
    @Binding var selectedImages: [UIImage]
    
    func makeUIViewController(context: Context) -> PHPickerViewController {
        var config = PHPickerConfiguration()
        config.filter = .images
        config.selectionLimit = 1 // limit selection to 4 images
        let picker = PHPickerViewController(configuration: config)
        picker.delegate = context.coordinator
        return picker
    }
    
    func updateUIViewController(_ uiViewController: PHPickerViewController, context: Context) {}
    
    func makeCoordinator() -> Coordinator {
        Coordinator(parent: self)
    }
    
    class Coordinator: NSObject, PHPickerViewControllerDelegate {
        let parent: ImagePicker
        
        init(parent: ImagePicker) {
            self.parent = parent
        }
        
        func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
                    picker.dismiss(animated: true)

                    parent.selectedImages.removeAll()
//            parent.cameraViewModel.capturedImage.removeAll()// Clear previous selection
            
                    for result in results {
                       let provider = result.itemProvider
                        provider.loadObject(ofClass: UIImage.self) { image, _ in
                            DispatchQueue.main.async {
                                if let uiImage = image as? UIImage {
                                    if self.parent.selectedImages.count < 1 {
                                        self.parent.selectedImages.append(uiImage)
                                    }
//                                    if self.parent.cameraViewModel.capturedImage.count < 4 {
//                                        self.parent.cameraViewModel.capturedImage.append(uiImage)
//                                    }
                                }
                            }
                        }
                    }
                }
    }
}
