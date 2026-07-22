//
//  QRCodePopupView.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 13/09/24.
//

import SwiftUI

struct QRCodePopupView: View {
    
    @Binding var imageURL: String
    @State private var userName: String = ""
    @State private var department: String = ""
    
    @Binding var downloadPDF: Bool
    
    @Binding var isImageLoaded: Bool
    @Binding var qrCodeImage: UIImage?
    @Binding var showQrCode: Bool
    @Binding var pdfURL: URL?
    @Binding var isShareSheetPresented: Bool
    
    var body: some View {
        ZStack(alignment: .topTrailing) {
            VStack(spacing: AppSpacing.zero) {
                ZStack {
                    Color(red: 0.93, green: 0.96, blue: 1.0)
                    Image(.topSetupScreenBg)
                        .resizable()
                        .scaledToFill()
                        .opacity(0.18)
                }
                .frame(height: 124)
                .clipped()

                Color.white
            }

            VStack(spacing: AppSpacing.md) {
                Spacer(minLength: AppSpacing.lg)

                qrCodePanel

                VStack(spacing: AppSpacing.sm) {
                    Text(userName)
                        .font(AppFont.primary(size: AppFont.Size.title))
                        .fontWeight(AppFont.Weight.semibold)
                        .foregroundStyle(Color.text1)
                        .lineLimit(2)
                        .minimumScaleFactor(0.82)
                        .multilineTextAlignment(.center)

                    Text(department)
                        .font(AppFont.primary(size: AppFont.Size.body))
                        .fontWeight(AppFont.Weight.medium)
                        .foregroundStyle(Color.text1.opacity(0.72))
                        .lineLimit(2)
                        .multilineTextAlignment(.center)

                    Text("www.empmonitor.com")
                        .font(AppFont.primary(size: AppFont.Size.caption))
                        .fontWeight(AppFont.Weight.medium)
                        .foregroundStyle(Color.text1.opacity(0.62))
                        .accentColor(.text1)
                        .padding(.top, AppSpacing.sm)
                }
                .frame(maxWidth: .infinity)

                Spacer(minLength: AppSpacing.md)
            }
            .frame(maxWidth: .infinity, minHeight: 420, alignment: .center)
            .padding(.horizontal, AppSpacing.lg)

            Button {
                showQrCode.toggle()
            } label: {
                Image(systemName: "xmark")
                    .font(AppFont.primary(size: AppFont.Size.closeIcon, weight: AppFont.Weight.bold))
                    .foregroundStyle(Color.text1)
                    .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                    .background(Color.white.opacity(0.9))
                    .clipShape(Circle())
            }
            .buttonStyle(.plain)
            .padding(.top, AppSpacing.md)
            .padding(.trailing, AppSpacing.md)
            .accessibilityLabel("Close QR code")
        }
        .frame(maxWidth: 340, minHeight: 420, alignment: .center)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
        .shadow(color: Color.black.opacity(0.14), radius: 18, x: 0, y: 8)
        .padding(.horizontal, AppSpacing.md)
        .onChange(of: downloadPDF) { _, newValue in
            Task {
                await downloadPDF()
            }
        }
        
        .onAppear {
            let user = AuthStore.shared.getLoggedInUser()
            
            if let userName = user?.body.data?.userData.fullName {
                self.userName = userName
            }
            if let department = user?.body.data?.userData.department {
                self.department = department
            }
            
            let empID = user?.body.data?.userData.empID
            if let id = empID {
                imageURL = "\(Constants.StaticURL.qrCodeBase)?data=\(id)"
            }
        }
    }

    private var qrCodePanel: some View {
        ZStack {
            RoundedRectangle(cornerRadius: AppRadius.small)
                .fill(Color.white)
                .overlay {
                    RoundedRectangle(cornerRadius: AppRadius.small)
                        .stroke(Color.black.opacity(0.08), lineWidth: 1)
                }
                .shadow(color: Color.black.opacity(0.10), radius: 14, x: 0, y: 6)

            if let qrCodeImage = qrCodeImage {
                Image(uiImage: qrCodeImage)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .padding(AppSpacing.md)
                    .accessibilityLabel("Employee QR code")
            } else {
                ProgressView()
                    .frame(width: AppLayout.iconExtraLarge, height: AppLayout.iconExtraLarge)
                    .onAppear {
                        loadQRCodeImage()
                    }
            }
        }
        .frame(width: 252, height: 252)
    }
    
    //Load QR code image using URLSession
    func loadQRCodeImage() {
        let requestURLString = imageURL
        guard let url = URL(string: requestURLString) else { return }

        AppLog.debug("[API] GET \(requestURLString)")
        let task = URLSession.shared.dataTask(with: url) { data, response, error in
            let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
            guard let data = data, let image = UIImage(data: data), error == nil else {
                AppLog.debug("[API] Response (\(httpStatus)) \(requestURLString) - image load failed: \(error?.localizedDescription ?? "Unknown error")")
                return
            }
            AppLog.debug("[API] Response (\(httpStatus)) \(requestURLString) - image loaded (\(data.count) bytes)")

            Task { @MainActor in
                self.qrCodeImage = image
                self.isImageLoaded = true
            }
        }
        task.resume()
    }
    
    
    //function to generate and download PDF
    func downloadPDF() async {
        let renderer = PDFRenderer(content: self)
        
        //save PDF to file
        do{
            let savedPDFURL = try await savePDFToDocument(renderer: renderer)
            AppLog.debug("PDF saved at : \(savedPDFURL)")
            pdfURL = savedPDFURL
            isShareSheetPresented = true
        }
        catch {
            AppLog.debug("Error: failed to save PDF: \(error.localizedDescription)")
        }
        
    }


    // function to save PDF in the device's Document folder
    func savePDFToDocument(renderer: PDFRenderer) async throws -> URL {
        let fileManager = FileManager.default
        guard let documentsURL = fileManager.urls(for: .documentDirectory, in: .userDomainMask).first else {
            throw NSError(domain: "FileManager", code: -1, userInfo: [NSLocalizedDescriptionKey: "Unable to access Documents directory"])
        }
        let fileURL = documentsURL.appendingPathComponent("QRCode.pdf")

        //render the pdf directly to file
        await renderer.renderPDF(to: fileURL)

        return fileURL
    }
}




#Preview {
    QRCodePopupView(imageURL: .constant(""), downloadPDF: .constant(false), isImageLoaded: .constant(false), qrCodeImage: .constant(UIImage(systemName: "xmark")), showQrCode: .constant(false), pdfURL: .constant(nil), isShareSheetPresented: .constant(false))
}
