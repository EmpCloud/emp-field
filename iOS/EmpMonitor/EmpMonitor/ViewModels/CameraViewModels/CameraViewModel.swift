//
//  CameraViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 23/08/24.
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


class CameraViewModel: NSObject, ObservableObject {
    
    @Published var isTaken: Bool = false
    @Published var alert = false
    @Published var session = AVCaptureSession()
    @Published var output = AVCapturePhotoOutput()
    @Published var preview: AVCaptureVideoPreviewLayer!
    @Published var capturedImage: UIImage? // to capture that particular image
    @Published var savedImages: [SavedImage] = []  // to save the captured Images
    @Published var currentDevice: AVCaptureDevice?
    @Published var isFlashLightON: Bool = false
    @Published var cameraAuthStatus: Bool = false
    
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
            AVCaptureDevice.requestAccess(for: .video) { granted in
                if granted {
                    self.cameraAuthStatus = true
                    self.setUpCamera()
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
        session.beginConfiguration()
        do {
            let device = isUsingFrontCamera ? AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .front) : AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .back)

            guard let device = device else {
                print("Camera device not available")
                session.commitConfiguration()
                return
            }
            let input = try AVCaptureDeviceInput(device: device)
            currentDevice = device

            if session.canAddInput(input) {
                session.addInput(input)
            }
            if session.canAddOutput(output) {
                session.addOutput(output)
            }
            session.commitConfiguration()
        } catch {
            print("Camera setup failed: \(error.localizedDescription)")
        }
    }
    
    func takePicture() {
        let settings = AVCapturePhotoSettings()
        output.capturePhoto(with: settings, delegate: self)
    }
    
    func retake() {
        DispatchQueue.main.async {
            if self.isTaken {
//                self.savedImages.removeLast()
                self.capturedImage = nil        // removing the last captured Image
            }
            self.isTaken = false
        }
        DispatchQueue.global().async {
            self.session.startRunning()
        }
    }
    
    func toggleFlashlight() {
        guard let device = currentDevice, device.hasTorch else { return }
        
        do{
            try device.lockForConfiguration()
            if device.torchMode == .on {
                device.torchMode = .off
                isFlashLightON = false
            }else {
                try device.setTorchModeOn(level: 1.0)
                isFlashLightON = true
            }
            device.unlockForConfiguration()
        }catch {
            print("Flashlight could not be used: \(error.localizedDescription)")
        }
    }
    
    func flipCamera() {
        session.beginConfiguration()
        
        session.inputs.forEach { input in
            session.removeInput(input)
        }
        
        isUsingFrontCamera.toggle()
        setUpCamera()
        
        session.commitConfiguration()
    }
    
    //temporary
    func saveImage(_ image: UIImage, description: String) {
        guard let imageData = image.jpegData(compressionQuality: 0.8) else { return }
        
        let fileName = UUID().uuidString + ".jpg"
        let fileURL = FileManager.default.temporaryDirectory.appendingPathComponent(fileName)
        
        do{
            try imageData.write(to: fileURL)
            let capturedImage = SavedImage(image: image, description: description, url: fileURL)
            savedImages.append(capturedImage)
        }catch {
            print("Error: Failed to save images: \(error.localizedDescription)")
        }
    }
    
    
//    func saveImage(_ image: UIImage, description: String) {
//        guard let imageData = image.jpegData(compressionQuality: 0.8) else { return }
//
//        let fileName = UUID().uuidString + ".jpg"
//        let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0]
//        let fileURL = documentsDirectory.appendingPathComponent(fileName)
//
//        do {
//            try imageData.write(to: fileURL)
//            let capturedImage = SavedImage(image: image, description: description, url: fileURL)
//            savedImages.append(capturedImage)
//        } catch {
//            print("Error: Failed to save image: \(error.localizedDescription)")
//        }
//    }
    
    func getCapturedImageURLs() -> [URL] {
        return savedImages.compactMap { $0.url }
    }
}




extension CameraViewModel: AVCapturePhotoCaptureDelegate {
    func photoOutput(_ output: AVCapturePhotoOutput, didFinishProcessingPhoto photo: AVCapturePhoto, error: Error?) {
        guard let data = photo.fileDataRepresentation() else { return }
        if let image = UIImage(data: data) {
            if savedImages.count < 4 {
                DispatchQueue.main.async {
//                    self.savedImages.append(SavedImage(image: image, description: "", url: nil))
                    self.capturedImage = image
                    
//        capturedImage = UIImage(data: data
                }
            }
        }

        DispatchQueue.main.async {
            self.isTaken = true
            self.session.stopRunning()
        }
    }
}
