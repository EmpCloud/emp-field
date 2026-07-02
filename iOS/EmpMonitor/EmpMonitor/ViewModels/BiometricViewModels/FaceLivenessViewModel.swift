//
//  FaceLivenessViewModel.swift
//  EmpMonitor
//

import AVFoundation
import Vision
import UIKit
import SwiftUI

enum FaceLivenessState {
    case waitingForFace
    case faceDetected
    case holdStill(Int)   // countdown seconds
    case captured
    case error(String)
}

@MainActor
final class FaceLivenessViewModel: NSObject, ObservableObject {

    @Published var state: FaceLivenessState = .waitingForFace
    @Published var capturedImageData: Data?
    @Published var cameraPermissionDenied = false

    // AVFoundation
    let session = AVCaptureSession()
    private let photoOutput = AVCapturePhotoOutput()
    private let videoOutput = AVCaptureVideoDataOutput()
    private let sessionQueue = DispatchQueue(label: "com.empmonitor.face.session")
    private let analysisQueue = DispatchQueue(label: "com.empmonitor.face.vision")

    // Countdown
    private var stableFaceFrames = 0
    private let framesNeeded = 20   // ~2 s at 10 fps analysis
    private var captureTriggered = false

    // Vision — accessed from nonisolated video-output queue, so opt out of actor isolation
    nonisolated(unsafe) private var faceRequest = VNDetectFaceRectanglesRequest()

    override init() {
        super.init()
    }

    func startSession() {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .authorized:
            configure()
        case .notDetermined:
            AVCaptureDevice.requestAccess(for: .video) { [weak self] granted in
                Task { @MainActor [weak self] in
                    if granted { self?.configure() }
                    else { self?.cameraPermissionDenied = true }
                }
            }
        default:
            cameraPermissionDenied = true
        }
    }

    func stopSession() {
        sessionQueue.async { [weak self] in self?.session.stopRunning() }
    }

    // Called by the capture button / auto-capture path
    func capturePhoto() {
        guard !captureTriggered else { return }
        captureTriggered = true
        let settings = AVCapturePhotoSettings()
        sessionQueue.async { [weak self] in
            guard let self else { return }
            self.photoOutput.capturePhoto(with: settings, delegate: self)
        }
    }

    // MARK: - Private

    private func configure() {
        sessionQueue.async { [weak self] in
            guard let self else { return }
            self.session.beginConfiguration()
            self.session.sessionPreset = .photo

            guard
                let device = AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .front),
                let input = try? AVCaptureDeviceInput(device: device)
            else {
                self.session.commitConfiguration()
                Task { @MainActor [weak self] in
                    self?.state = .error("Camera not available")
                }
                return
            }

            if self.session.canAddInput(input) { self.session.addInput(input) }

            self.videoOutput.setSampleBufferDelegate(self, queue: self.analysisQueue)
            self.videoOutput.alwaysDiscardsLateVideoFrames = true
            if self.session.canAddOutput(self.videoOutput) { self.session.addOutput(self.videoOutput) }

            if self.session.canAddOutput(self.photoOutput) { self.session.addOutput(self.photoOutput) }

            self.session.commitConfiguration()
            self.session.startRunning()
        }
    }
}

// MARK: - Video sample buffer → Vision

extension FaceLivenessViewModel: AVCaptureVideoDataOutputSampleBufferDelegate {

    nonisolated func captureOutput(
        _ output: AVCaptureOutput,
        didOutput sampleBuffer: CMSampleBuffer,
        from connection: AVCaptureConnection
    ) {
        guard let pixelBuffer = CMSampleBufferGetImageBuffer(sampleBuffer) else { return }

        let handler = VNImageRequestHandler(cvPixelBuffer: pixelBuffer, orientation: .leftMirrored)
        try? handler.perform([faceRequest])

        let faceFound = (faceRequest.results?.isEmpty == false)
        let faceArea: Float = faceRequest.results?.first.map { obs in
            let b = obs.boundingBox
            return Float(b.width * b.height)
        } ?? 0

        // Face must fill at least 4 % of the frame to be "in position"
        let faceInPosition = faceFound && faceArea > 0.04

        Task { @MainActor [weak self] in
            guard let self, self.capturedImageData == nil else { return }
            if faceInPosition {
                self.stableFaceFrames += 1
                let remaining = max(0, self.framesNeeded - self.stableFaceFrames)
                if remaining > 0 {
                    let seconds = Int(ceil(Double(remaining) / 10.0))
                    self.state = remaining > self.framesNeeded / 2 ? .faceDetected : .holdStill(seconds)
                } else if !self.captureTriggered {
                    self.state = .captured
                    self.capturePhoto()
                }
            } else {
                self.stableFaceFrames = max(0, self.stableFaceFrames - 2)
                if self.stableFaceFrames == 0 { self.state = .waitingForFace }
            }
        }
    }
}

// MARK: - Photo capture delegate

extension FaceLivenessViewModel: AVCapturePhotoCaptureDelegate {

    nonisolated func photoOutput(
        _ output: AVCapturePhotoOutput,
        didFinishProcessingPhoto photo: AVCapturePhoto,
        error: Error?
    ) {
        guard let data = photo.fileDataRepresentation() else {
            Task { @MainActor [weak self] in self?.state = .error("Capture failed") }
            return
        }
        // Compress to ~20% as the Android app does
        let compressed = UIImage(data: data).flatMap { $0.jpegData(compressionQuality: 0.2) } ?? data
        Task { @MainActor [weak self] in
            guard let self else { return }
            self.capturedImageData = compressed
            self.session.stopRunning()
        }
    }
}
