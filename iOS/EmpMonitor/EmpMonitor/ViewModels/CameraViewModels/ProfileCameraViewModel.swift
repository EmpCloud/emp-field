//
//  ProfileCameraViewModel.swift
//  EmpMonitor
//

import Foundation
import AVFoundation
import UIKit
import SwiftUI

@MainActor
struct ProfileImage {
    let image: UIImage
    let url: URL?
}

final class ProfileCameraViewModel: NSObject, ObservableObject {
    
    @Published var isTaken: Bool = false
    @Published var alert = false
    @Published var capturedImage: UIImage?
    @Published var profileImage: ProfileImage?
    @Published var isFlashLightON: Bool = false
    @Published var cameraAuthStatus: Bool = false
    
    // Non-published AVFoundation objects: they are heavy and mutated on a background queue.
    var session = AVCaptureSession()
    var output = AVCapturePhotoOutput()
    var preview: AVCaptureVideoPreviewLayer?
    var currentDevice: AVCaptureDevice?
    
    private let sessionQueue = DispatchQueue(label: "com.empmonitor.profilecamera.session")
    private var isUsingFrontCamera = false
    
    override init() {
        super.init()
        checkPermissions()
    }

    deinit {
        let session = session
        sessionQueue.async {
            session.stopRunning()
        }
        cleanupTemporaryImage()
    }
    
    func checkPermissions() {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .authorized:
            AppLog.debug("Camera access granted")
            setUpCamera()
            cameraAuthStatus = true
        case .notDetermined:
            AVCaptureDevice.requestAccess(for: .video) { [weak self] granted in
                Task { @MainActor [weak self] in
                    guard let self else { return }
                    if granted {
                        self.setUpCamera()
                    }
                    self.cameraAuthStatus = granted
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
                    AppLog.debug("Camera device not available")
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
                AppLog.debug("Camera setup failed: \(error.localizedDescription)")
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
                AppLog.debug("Flashlight could not be used: \(error.localizedDescription)")
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
    
    func saveImage(_ image: UIImage) {
        guard let imageData = image.jpegData(compressionQuality: 0.8) else { return }
        
        let fileName = UUID().uuidString + ".jpg"
        let fileURL = FileManager.default.temporaryDirectory.appendingPathComponent(fileName)
        
        do {
            try imageData.write(to: fileURL)
            cleanupTemporaryImage()
            let capturedImage = ProfileImage(image: image, url: fileURL)
            profileImage = capturedImage
        } catch {
            AppLog.debug("Error: Failed to save images: \(error.localizedDescription)")
        }
    }
    
    func getCapturedImageURLs() -> URL? {
        return profileImage?.url
    }

    private func cleanupTemporaryImage() {
        if let fileURL = profileImage?.url {
            try? FileManager.default.removeItem(at: fileURL)
        }
    }
}

extension ProfileCameraViewModel: AVCapturePhotoCaptureDelegate {
    func photoOutput(_ output: AVCapturePhotoOutput, didFinishProcessingPhoto photo: AVCapturePhoto, error: Error?) {
        guard let data = photo.fileDataRepresentation() else { return }
        let image = UIImage(data: data)
        Task { @MainActor [weak self] in
            self?.capturedImage = image
            self?.isTaken = true
        }
        sessionQueue.async { [weak self] in
            self?.session.stopRunning()
        }
    }
}
