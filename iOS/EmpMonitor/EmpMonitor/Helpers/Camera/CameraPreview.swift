//
//  CameraPreview.swift
//  EmpMonitor
//

import SwiftUI
import AVFoundation

struct CameraPreview: UIViewRepresentable {
    
    @ObservedObject var cameraViewModel: CameraViewModel
    
    func makeUIView(context: Context) -> UIView {
        let view = UIView(frame: UIScreen.main.bounds)
        let preview = AVCaptureVideoPreviewLayer(session: cameraViewModel.session)
        preview.frame = view.bounds
        preview.videoGravity = .resizeAspectFill
        view.layer.addSublayer(preview)
        cameraViewModel.preview = preview
        
        // Start session on the camera session queue
        cameraViewModel.session.startRunningIfNeeded()
        
        return view
    }
    
    func updateUIView(_ uiView: UIView, context: Context) {}
    
    static func dismantleUIView(_ uiView: UIView, coordinator: ()) {
        // The preview layer is stopped when the view is torn down.
    }
}

struct ProfileCameraPreview: UIViewRepresentable {
    
    @ObservedObject var profileCameraViewModel: ProfileCameraViewModel
    
    func makeUIView(context: Context) -> UIView {
        let view = UIView(frame: UIScreen.main.bounds)
        let preview = AVCaptureVideoPreviewLayer(session: profileCameraViewModel.session)
        preview.frame = view.bounds
        preview.videoGravity = .resizeAspectFill
        view.layer.addSublayer(preview)
        profileCameraViewModel.preview = preview
        
        profileCameraViewModel.session.startRunningIfNeeded()
        
        return view
    }
    
    func updateUIView(_ uiView: UIView, context: Context) {}
}

private extension AVCaptureSession {
    func startRunningIfNeeded() {
        guard !isRunning else { return }
        DispatchQueue.global(qos: .userInitiated).async { [weak self] in
            self?.startRunning()
        }
    }
}
