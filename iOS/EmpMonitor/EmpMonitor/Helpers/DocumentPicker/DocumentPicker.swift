//
//  DocumentPicker.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 23/08/24.
//

import SwiftUI
import UniformTypeIdentifiers

struct DocumentPicker: UIViewControllerRepresentable {
    
    @Binding var selectedPDF: [URL]
    
    func makeCoordinator() -> Coordinator {
        Coordinator(parent: self)
    }
    
    func makeUIViewController(context: Context) -> UIDocumentPickerViewController{
        let picker = UIDocumentPickerViewController(forOpeningContentTypes: [UTType.pdf])
        picker.allowsMultipleSelection = true // allow multiple selections
        picker.delegate = context.coordinator
        return picker
    }
    
    func updateUIViewController(_ uiViewController: UIDocumentPickerViewController, context: Context) {}
    
    class Coordinator: NSObject, UIDocumentPickerDelegate {
        var parent: DocumentPicker
        
        init(parent: DocumentPicker) {
            self.parent = parent
        }
        
        func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
            if urls.count > 2 {
                // If the user selects more than 2 file, only keep the first 2
                parent.selectedPDF = Array(urls.prefix(2))
            }else{
                parent.selectedPDF = urls
            }
//            if let selectedURL = urls.first {
//                parent.selectedPDF = selectedURL
//            }
        }
        
        func documentPickerWasCancelled(_ controller: UIDocumentPickerViewController) {
//            parent.selectedPDF = []
        }
    }
    
}
