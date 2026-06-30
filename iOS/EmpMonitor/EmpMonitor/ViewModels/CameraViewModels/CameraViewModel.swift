//
//  CameraViewModel.swift
//  EmpMonitor
//

import Foundation
import AVFoundation
import UIKit
import SwiftUI

@MainActor
struct SavedImage {
    let image: UIImage
    let description: String
    let url: URL?
}

final class CameraViewModel: NSObject, ObservableObject {
    
    @Published var isTaken: Bool = false
    @Published var alert = false
    @Published var capturedImage: UIImage?
    @Published var savedImages: [SavedImage] = []
    @Published var isFlashLightON: Bool = false
    @Published var cameraAuthStatus: Bool = false
    
    // Non-published AVFoundation objects: they are heavy and mutated on a background queue.
    var session = AVCaptureSession()
    var output = AVCapturePhotoOutput()
    var preview: AVCaptureVideoPreviewLayer?
    var currentDevice: AVCaptureDevice?
    
    private let sessionQueue = DispatchQueue(label: "com.empmonitor.camera.session")
    private var isUsingFrontCamera = false
    
    override init() {
        super.init()
        checkPermissions()
    }
    
    func checkPermissions() {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .authorized:
            setUpCamera()
            cameraAuthStatus = true
            print("to set the camera")
        case .notDetermined:
            AVCaptureDevice.requestAccess(for: .video) { [weak self] granted in
                Task { @MainActor [weak self] in
                    guard let self else { return }
                    self.cameraAuthStatus = granted
                    if granted {
                        self.setUpCamera()
                    }
                }
            }
        case .denied, .restricted:
            cameraAuthStatus = false
            alert.toggle()
        default:
            break
        }
    }
    
    func setUpCamera() {
        sessionQueue.async { [weak self] in
            guard let self else { return }
            self.session.beginConfiguration()
            defer { self.session.commitConfiguration() }
            
            do {
                let device = self.isUsingFrontCamera
                    ? AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .front)
                    : AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .back)
                
                guard let device = device else {
                    print("Camera device not available")
                    return
                }
                let input = try AVCaptureDeviceInput(device: device)
                
                Task { @MainActor [weak self] in
                    self?.currentDevice = device
                }
                
                if self.session.canAddInput(input) {
                    self.session.addInput(input)
                }
                if self.session.canAddOutput(self.output) {
                    self.session.addOutput(self.output)
                }
            } catch {
                print("Camera setup failed: \(error.localizedDescription)")
            }
        }
    }
    
    func takePicture() {
        let settings = AVCapturePhotoSettings()
        sessionQueue.async { [weak self] in
            guard let self else { return }
            self.output.capturePhoto(with: settings, delegate: self)
        }
    }
    
    func retake() {
        if isTaken {
            capturedImage = nil
        }
        isTaken = false
        sessionQueue.async { [weak self] in
            self?.session.startRunning()
        }
    }
    
    func toggleFlashlight() {
        guard let device = currentDevice, device.hasTorch else { return }
        
        sessionQueue.async { [weak self] in
            guard let self else { return }
            do {
                try device.lockForConfiguration()
                if device.torchMode == .on {
                    device.torchMode = .off
                    Task { @MainActor in self.isFlashLightON = false }
                } else {
                    try device.setTorchModeOn(level: 1.0)
                    Task { @MainActor in self.isFlashLightON = true }
                }
                device.unlockForConfiguration()
            } catch {
                print("Flashlight could not be used: \(error.localizedDescription)")
            }
        }
    }
    
    func flipCamera() {
        sessionQueue.async { [weak self] in
            guard let self else { return }
            self.session.beginConfiguration()
            self.session.inputs.forEach { self.session.removeInput($0) }
            self.isUsingFrontCamera.toggle()
            self.setUpCamera()
            self.session.commitConfiguration()
        }
    }
    
    func saveImage(_ image: UIImage, description: String) {
        guard let imageData = image.jpegData(compressionQuality: 0.8) else { return }
        
        let fileName = UUID().uuidString + ".jpg"
        let fileURL = FileManager.default.temporaryDirectory.appendingPathComponent(fileName)
        
        do {
            try imageData.write(to: fileURL)
            let capturedImage = SavedImage(image: image, description: description, url: fileURL)
            savedImages.append(capturedImage)
        } catch {
            print("Error: Failed to save images: \(error.localizedDescription)")
        }
    }
    
    func getCapturedImageURLs() -> [URL] {
        return savedImages.compactMap { $0.url }
    }
}

extension CameraViewModel: AVCapturePhotoCaptureDelegate {
    func photoOutput(_ output: AVCapturePhotoOutput, didFinishProcessingPhoto photo: AVCapturePhoto, error: Error?) {
        guard let data = photo.fileDataRepresentation() else { return }
        if let image = UIImage(data: data) {
            if savedImages.count < 4 {
                capturedImage = image
            }
        }
        isTaken = true
        sessionQueue.async { [weak self] in
            self?.session.stopRunning()
        }
    }
}
