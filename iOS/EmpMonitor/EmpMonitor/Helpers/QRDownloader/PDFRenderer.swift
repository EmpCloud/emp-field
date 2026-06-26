//
//  PDFRenderer.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 13/09/24.
//

import SwiftUI
import UIKit

struct PDFRenderer: UIViewControllerRepresentable {
    let content: QRCodePopupView
    
    func makeUIViewController(context: Context) -> some UIViewController {
        let hostingController = UIHostingController(rootView: content)
        return hostingController
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        // nothing to do here
    }
    
    //this function captures the view as pdf
    @MainActor
    func renderPDF(to fileURL: URL) {
        let hostingController = UIHostingController(rootView: content)
        let view = hostingController.view
        
        //set the view's bounds so that its properly rendered
        hostingController.view.bounds = CGRect(x: 0, y: 0, width: 400, height: 600)
        
        let pdfRenderer = UIGraphicsPDFRenderer(bounds: view?.bounds ?? CGRect.zero)
        
        do {
            try pdfRenderer.writePDF(to: fileURL) { context in
               context.beginPage()
               view?.drawHierarchy(in: view?.bounds ?? CGRect.zero, afterScreenUpdates: true)
           }

        }
        catch{
            print("Error: pdfRenderer to fileURL ")
        }
    }
}


// Helper struct to present the share sheet using UIActivityViewController
struct ActivityViewController: UIViewControllerRepresentable {
    var activityItems: [Any]
    var applicationActivities: [UIActivity]? = nil

    func makeUIViewController(context: Context) -> UIActivityViewController {
        return UIActivityViewController(activityItems: activityItems, applicationActivities: applicationActivities)
    }

    func updateUIViewController(_ uiViewController: UIActivityViewController, context: Context) {
        // No updates needed
    }
}

