//
//  CameraPreview.swift
//  EmpMonitor
//

import SwiftUI
import AVFoundation

/// A view backed by the camera preview layer, so the preview always matches the view's
/// size and follows the interface orientation (iPad rotation, split view, resizing).
final class CameraPreviewView: UIView {
    override class var layerClass: AnyClass { AVCaptureVideoPreviewLayer.self }

    var previewLayer: AVCaptureVideoPreviewLayer {
        // Safe: `layerClass` above guarantees the backing layer's type.
        layer as! AVCaptureVideoPreviewLayer
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        updateVideoRotation()
    }

    private func updateVideoRotation() {
        guard let connection = previewLayer.connection,
              let orientation = window?.windowScene?.interfaceOrientation else { return }

        let angle: CGFloat
        switch orientation {
        case .landscapeRight: angle = 0
        case .landscapeLeft: angle = 180
        case .portraitUpsideDown: angle = 270
        default: angle = 90
        }

        if connection.isVideoRotationAngleSupported(angle) {
            connection.videoRotationAngle = angle
        }
    }
}

struct CameraPreview: UIViewRepresentable {
    
    @ObservedObject var cameraViewModel: CameraViewModel
    
    func makeUIView(context: Context) -> CameraPreviewView {
        let view = CameraPreviewView()
        view.previewLayer.session = cameraViewModel.session
        view.previewLayer.videoGravity = .resizeAspectFill
        cameraViewModel.preview = view.previewLayer
        
        // Start session on the camera session queue
        cameraViewModel.session.startRunningIfNeeded()
        
        return view
    }
    
    func updateUIView(_ uiView: CameraPreviewView, context: Context) {}
}

struct ProfileCameraPreview: UIViewRepresentable {
    
    @ObservedObject var profileCameraViewModel: ProfileCameraViewModel
    
    func makeUIView(context: Context) -> CameraPreviewView {
        let view = CameraPreviewView()
        view.previewLayer.session = profileCameraViewModel.session
        view.previewLayer.videoGravity = .resizeAspectFill
        profileCameraViewModel.preview = view.previewLayer
        
        profileCameraViewModel.session.startRunningIfNeeded()
        
        return view
    }
    
    func updateUIView(_ uiView: CameraPreviewView, context: Context) {}
}

private extension AVCaptureSession {
    func startRunningIfNeeded() {
        guard !isRunning else { return }
        DispatchQueue.global(qos: .userInitiated).async { [weak self] in
            self?.startRunning()
        }
    }
}
