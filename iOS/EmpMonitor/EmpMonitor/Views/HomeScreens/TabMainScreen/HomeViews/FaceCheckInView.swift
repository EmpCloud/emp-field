//
//  FaceCheckInView.swift
//  EmpMonitor
//

import SwiftUI
import AVFoundation

struct FaceCheckInView: View {

    @Environment(\.dismiss) private var dismiss
    @EnvironmentObject private var permissionManager: PermissionManager

    @StateObject private var livenessVM = FaceLivenessViewModel()
    @StateObject private var checkInVM  = FaceCheckInViewModel()

    var onCheckInSuccess: (String) -> Void

    var body: some View {
        ZStack {
            Color.black.ignoresSafeArea()

            // Camera preview
            FaceCameraPreview(session: livenessVM.session)
                .ignoresSafeArea()

            // Face oval guide
            FaceOvalGuide()

            // Overlay UI
            VStack {
                // Top bar
                HStack {
                    Spacer()
                    Button {
                        livenessVM.stopSession()
                        dismiss()
                    } label: {
                        Image(systemName: "xmark.circle.fill")
                            .font(AppFont.title)
                            .foregroundStyle(.white)
                            .padding()
                    }
                }

                Spacer()

                // Status card
                statusCard
                    .padding(.bottom, 40)
            }

            // Permission denied overlay
            if livenessVM.cameraPermissionDenied {
                permissionDeniedView
            }
        }
        .onAppear { livenessVM.startSession() }
        .onDisappear { livenessVM.stopSession() }
        .onChange(of: livenessVM.capturedImageData) { _, imageData in
            guard let data = imageData else { return }
            Task {
                let lat = permissionManager.userLocation?.coordinate.latitude ?? 0
                let lon = permissionManager.userLocation?.coordinate.longitude ?? 0
                await checkInVM.checkIn(imageData: data, latitude: lat, longitude: lon) { time in
                    onCheckInSuccess(time)
                    DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
                        dismiss()
                    }
                }
            }
        }
    }

    // MARK: Status card

    @ViewBuilder
    private var statusCard: some View {
        VStack(spacing: 12) {
            switch checkInVM.result {
            case .idle:
                idleStatusView

            case .verifying:
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: .white))
                    .scaleEffect(1.4)
                Text("Verifying face…")
                    .font(AppFont.primary(size: AppFont.Size.subheadline, weight: AppFont.Weight.medium))
                    .foregroundStyle(.white)

            case .success(let time):
                Image(systemName: "checkmark.circle.fill")
                    .font(AppFont.primary(size: AppFont.Size.iconHero))
                    .foregroundStyle(.green)
                Text("Checked In at \(time)")
                    .font(AppFont.primary(size: AppFont.Size.headline, weight: AppFont.Weight.semibold))
                    .foregroundStyle(.white)

            case .noMatch:
                Image(systemName: "person.crop.circle.badge.xmark")
                    .font(AppFont.primary(size: AppFont.Size.iconHero))
                    .foregroundStyle(.red)
                Text("Face not recognized")
                    .font(AppFont.primary(size: AppFont.Size.subheadline, weight: AppFont.Weight.medium))
                    .foregroundStyle(.white)
                retryButton

            case .failure(let msg):
                Image(systemName: "exclamationmark.triangle.fill")
                    .font(AppFont.primary(size: AppFont.Size.iconHero))
                    .foregroundStyle(.yellow)
                Text(msg)
                    .font(AppFont.primary(size: AppFont.Size.callout))
                    .foregroundStyle(.white)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)
                retryButton
            }
        }
        .padding(20)
        .background(.ultraThinMaterial)
        .clipShape(RoundedRectangle(cornerRadius: 20))
        .padding(.horizontal, 32)
    }

    @ViewBuilder
    private var idleStatusView: some View {
        switch livenessVM.state {
        case .waitingForFace:
            Image(systemName: "person.fill.viewfinder")
                .font(AppFont.primary(size: AppFont.Size.iconXLarge))
                .foregroundStyle(.white.opacity(0.8))
            Text("Position your face in the oval")
                .font(AppFont.primary(size: AppFont.Size.subheadline, weight: AppFont.Weight.medium))
                .foregroundStyle(.white)

        case .faceDetected:
            Image(systemName: "checkmark.seal")
                .font(AppFont.primary(size: AppFont.Size.iconXLarge))
                .foregroundStyle(.green)
            Text("Face detected — hold still")
                .font(AppFont.primary(size: AppFont.Size.subheadline, weight: AppFont.Weight.semibold))
                .foregroundStyle(.white)

        case .holdStill(let sec):
            ProgressView()
                .progressViewStyle(CircularProgressViewStyle(tint: .white))
            Text("Capturing in \(sec)s…")
                .font(AppFont.primary(size: AppFont.Size.subheadline, weight: AppFont.Weight.medium))
                .foregroundStyle(.white)

        case .captured:
            ProgressView()
                .progressViewStyle(CircularProgressViewStyle(tint: .white))
            Text("Processing…")
                .font(AppFont.primary(size: AppFont.Size.subheadline, weight: AppFont.Weight.medium))
                .foregroundStyle(.white)

        case .error(let msg):
            Image(systemName: "exclamationmark.triangle")
                .font(AppFont.primary(size: AppFont.Size.iconLarge))
                .foregroundStyle(.yellow)
            Text(msg)
                .font(AppFont.primary(size: AppFont.Size.callout))
                .foregroundStyle(.white)
        }
    }

    private var retryButton: some View {
        Button {
            checkInVM.result = .idle
            livenessVM.capturedImageData = nil
            livenessVM.startSession()
        } label: {
            Text("Try Again")
                .font(AppFont.primary(size: AppFont.Size.body, weight: AppFont.Weight.semibold))
                .foregroundStyle(.white)
                .padding(.horizontal, 24)
                .padding(.vertical, 10)
                .background(Color.primaryButton1)
                .clipShape(Capsule())
        }
    }

    private var permissionDeniedView: some View {
        VStack(spacing: 16) {
            Image(systemName: "camera.slash.fill")
                .font(AppFont.primary(size: AppFont.Size.iconDisplay))
                .foregroundStyle(.white)
            Text("Camera access is required for face check-in.\nEnable it in Settings.")
                .multilineTextAlignment(.center)
                .foregroundStyle(.white)
                .padding(.horizontal)
            Button("Open Settings") {
                HelperFunction.shared.openAppSetting()
            }
            .buttonStyle(.bordered)
            .tint(.white)
        }
    }
}

// MARK: - Face Oval Overlay

private struct FaceOvalGuide: View {
    var body: some View {
        GeometryReader { geo in
            let ovalWidth  = geo.size.width * 0.62
            let ovalHeight = ovalWidth * 1.35
            let x = (geo.size.width  - ovalWidth)  / 2
            let y = (geo.size.height - ovalHeight) / 2 - 30

            ZStack {
                // Dim everything outside the oval
                Rectangle()
                    .fill(Color.black.opacity(0.55))
                    .mask(
                        Rectangle()
                            .overlay(
                                Ellipse()
                                    .frame(width: ovalWidth, height: ovalHeight)
                                    .offset(x: x - geo.size.width / 2 + ovalWidth / 2,
                                            y: y - geo.size.height / 2 + ovalHeight / 2)
                                    .blendMode(.destinationOut)
                            )
                            .compositingGroup()
                    )

                // Oval border
                Ellipse()
                    .stroke(Color.white, lineWidth: 2.5)
                    .frame(width: ovalWidth, height: ovalHeight)
                    .offset(x: x - geo.size.width / 2 + ovalWidth / 2,
                            y: y - geo.size.height / 2 + ovalHeight / 2)
            }
        }
        .ignoresSafeArea()
    }
}

// MARK: - Camera Preview

struct FaceCameraPreview: UIViewRepresentable {
    let session: AVCaptureSession

    func makeUIView(context: Context) -> PreviewView {
        let view = PreviewView()
        view.previewLayer.session = session
        view.previewLayer.videoGravity = .resizeAspectFill
        return view
    }

    func updateUIView(_ uiView: PreviewView, context: Context) {}

    class PreviewView: UIView {
        override class var layerClass: AnyClass { AVCaptureVideoPreviewLayer.self }
        var previewLayer: AVCaptureVideoPreviewLayer { layer as! AVCaptureVideoPreviewLayer }
    }
}

#Preview {
    FaceCheckInView(onCheckInSuccess: { _ in })
        .environmentObject(PermissionManager())
}
