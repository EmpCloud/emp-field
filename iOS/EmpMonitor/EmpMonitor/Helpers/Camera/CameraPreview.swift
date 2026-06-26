//
//  CameraPreview.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 23/08/24.
//

import SwiftUI
import AVFoundation

//@MainActor
struct CameraPreview: UIViewRepresentable {
    
    @ObservedObject var cameraViewModel: CameraViewModel
    
    func makeUIView(context: Context) -> UIView {
        let view = UIView(frame: UIScreen.main.bounds)
        cameraViewModel.preview = AVCaptureVideoPreviewLayer(session: cameraViewModel.session)
        cameraViewModel.preview.frame = view.bounds
        cameraViewModel.preview.videoGravity = .resizeAspectFill
        view.layer.addSublayer(cameraViewModel.preview)
        
        //Start session on a background thread
        DispatchQueue.global(qos: .userInitiated).async {
            cameraViewModel.session.startRunning()
        }
        
        return view
    }
    
    func updateUIView(_ uiView: UIView, context: Context) {}
}

struct ProfileCameraPreview: UIViewRepresentable {
    
    @ObservedObject var profileCameraViewModel: ProfileCameraViewModel
    
    func makeUIView(context: Context) -> UIView {
        let view = UIView(frame: UIScreen.main.bounds)
        profileCameraViewModel.preview = AVCaptureVideoPreviewLayer(session: profileCameraViewModel.session)
        profileCameraViewModel.preview.frame = view.bounds
        profileCameraViewModel.preview.videoGravity = .resizeAspectFill
        view.layer.addSublayer(profileCameraViewModel.preview)
        
        //Start session on a background thread
        DispatchQueue.global(qos: .userInitiated).async {
            profileCameraViewModel.session.startRunning()
        }
        
        return view
    }
    
    func updateUIView(_ uiView: UIView, context: Context) {}
}


