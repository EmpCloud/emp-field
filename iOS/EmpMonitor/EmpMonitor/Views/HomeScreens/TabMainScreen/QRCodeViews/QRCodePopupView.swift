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
        
        ZStack(alignment: .top) {
            Image(.topSetupScreenBg)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .overlay(alignment: .topTrailing) {
                    HStack {
                        Image(systemName: "xmark")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 11, height: 11)
                            .fontWeight(.bold)
                            .foregroundStyle(Color.white)
                            .padding()
                            .onTapGesture {
                                showQrCode.toggle()
                            }
                    }
                }
            
            
            VStack {
                
                //Downloaded image
                VStack(spacing: 10) {
                    
                    //Image view (show image or loading indicator)
                    if let qrCodeImage = qrCodeImage {
                        Image(uiImage: qrCodeImage)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 301, height: 301)
                    }else {
                        ProgressView()
                            .frame(width: 100, height: 100)
                            .onAppear {
                                loadQRCodeImage()
                            }
                    }

//                    AsyncImage(url: URL(string: imageURL)) { phase in
//                        switch phase {
//                        case .empty:
//                            //placeholder view while the image is being loaded
//                            ProgressView()
//                                .frame(width: 100, height: 100)
//                        case .success(let image):
//                            //Display the image
//                            image
//                                .resizable()
//                                .aspectRatio(contentMode: .fit)
//                                .frame(width: 301, height: 301)
//                                .onAppear {
//                                    isImageLoaded = true
//                                }
//                            
//                        case .failure(let error):
//                            
//                            Text("Failed to load: \(error.localizedDescription)")
//                            
//                            Image(systemName: "photo")
//                                .resizable()
//                                .aspectRatio(contentMode: .fit)
//                                .frame(width: 100, height: 100)
//                            
//                            
//                        @unknown default:
//                            // Handle any unexpected errors
//                            Image(systemName: "exclamationmark.triangle")
//                                .resizable()
//                                .aspectRatio(contentMode: .fit)
//                                .frame(width: 100, height: 100)
//                        }
//                    }
                }
                .frame(width: 301, height: 301)
                
                HStack {
                    Text(userName)
                        .foregroundStyle(Color.text1)
//                    Text("Geller")
//                        .foregroundStyle(Color.attendanceTitleText)
                }
                .font(.custom("Montserrat", size: 26))
                .fontWeight(.semibold)
                .padding(.top, 20)
            
                Text(department)
                    .font(.custom("Montserrat", size: 14))
                    .fontWeight(.medium)
                    .foregroundStyle(Color.text1)
                
                Text("www.empmonitor.com")
                    .font(.custom("Montserrat", size: 12))
                    .fontWeight(.medium)
                    .foregroundStyle(Color.text1)
                    .accentColor(.text1)
                    .padding(.top)
                
            }
            .padding(.top, 120)
            
        }
        .frame(height: 557, alignment: .top)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 8))
        .padding()
        .onChange(of: downloadPDF) { _, newValue in
            Task {
                await downloadPDF()
            }
        }
        
        .onAppear {
            let user = UserDefaults.standard.getObject(forKey: "loggedInUser", as: UserLoginResponseModel.self)
            
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
    
    //Load QR code image using URLSession
    func loadQRCodeImage() {
        guard let url = URL(string: imageURL) else { return }

        print("[API] GET \(imageURL)")
        let task = URLSession.shared.dataTask(with: url) { data, response, error in
            let httpStatus = (response as? HTTPURLResponse)?.statusCode ?? 0
            guard let data = data, let image = UIImage(data: data), error == nil else {
                print("[API] Response (\(httpStatus)) \(self.imageURL) — image load failed: \(error?.localizedDescription ?? "Unknown error")")
                return
            }
            print("[API] Response (\(httpStatus)) \(self.imageURL) — image loaded (\(data.count) bytes)")

            DispatchQueue.main.async {
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
            print("PDF saved at : \(savedPDFURL)")
            pdfURL = savedPDFURL
            isShareSheetPresented = true
        }
        catch {
            print("Error: failed to save PDF: \(error.localizedDescription)")
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
