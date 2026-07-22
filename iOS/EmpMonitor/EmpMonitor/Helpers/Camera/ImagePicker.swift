//
//  ImagePicker.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 26/08/24.
//

import Foundation
import SwiftUI
import PhotosUI
import UniformTypeIdentifiers

struct ImagePicker: UIViewControllerRepresentable {
    
//    @ObservedObject var cameraViewModel: CameraViewModel
    @Binding var selectedImages: [UIImage]
    var maxSelectionCount: Int = 1
    
    func makeUIViewController(context: Context) -> PHPickerViewController {
        var config = PHPickerConfiguration()
        config.filter = .images
        config.selectionLimit = maxSelectionCount
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
                        provider.loadDataRepresentation(forTypeIdentifier: UTType.image.identifier) { [weak self] data, _ in
                            guard let data else { return }
                            Task { @MainActor [weak self] in
                                guard let self,
                                      let uiImage = UIImage(data: data),
                                      self.parent.maxSelectionCount == 0 || self.parent.selectedImages.count < self.parent.maxSelectionCount else {
                                    return
                                }
                                self.parent.selectedImages.append(uiImage)
//                                    if self.parent.cameraViewModel.capturedImage.count < 4 {
//                                        self.parent.cameraViewModel.capturedImage.append(uiImage)
//                                    }
                            }
                        }
                    }
                }
    }
}
