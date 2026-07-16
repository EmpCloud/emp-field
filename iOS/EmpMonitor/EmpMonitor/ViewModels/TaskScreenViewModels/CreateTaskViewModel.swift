//
//  CreateTaskViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 27/08/24.
//

import Foundation
import UIKit

@MainActor
class CreateTaskViewModel: ObservableObject {
    @Published var isLoading: Bool = true
    @Published var error: Error?
    
    @Published var clientID: String = ""
    @Published var taskName: String = ""
    @Published var startTime: String = ""
    @Published var endTime: String = ""
    @Published var date: String = ""
    @Published var taskDescription: String = ""
    @Published var files: [DocFile] = []
    @Published var images: [SelectedImage] = []
    @Published var value: Value = Value(currency: "", amount: 0, convertedAmountInUSD: nil)
    @Published var taskVolume: Int = 0
    @Published var tagLogs: [TagLog] = []
    
    @Published var selectedPDFURLs: [String] = []
    @Published var selectedImageURLs: [String] = []
    
    
    
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.createTask
    }
    
    func createTask() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        let body = CreateTaskRequestModel(clientID: clientID, taskName: taskName, startTime: startTime, endTime: endTime, date: date, taskDescription: taskDescription, files: files, images: images, value: value, taskVolume: taskVolume, tagLogs: tagLogs)
        
        AppLog.debug("Body: Create Task")
        AppLog.debug(body)
        
        do{
            let fetchData: CreateTaskResponseModel = try await NetworkManager.shared.postData(to: urlString, body: body, as: CreateTaskResponseModel.self, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.error?.details.first?.message ?? fetchData.body.message
//            AppLog.debug("Create task data: \(fetchData)")
            
        }catch{
            AppLog.debug("Error: TaskListViewModel data error")
            self.error = error
        }
    }
    
    func addLatestPDFURLs(_ filesURLs: [FilesURL]) {
        for file in filesURLs {
            selectedPDFURLs.append(file.url)
        }
        
        AppLog.debug("SelectedPDFURLs: \(selectedPDFURLs)")
    }
    
    func addLatestImageURLs(_ filesURLs: [FilesURL]) {
        for file in filesURLs {
            selectedImageURLs.append(file.url)
        }
        
        AppLog.debug("SelectedImageURLs: \(selectedImageURLs)")
    }
    
    func addToImages(description: String, url: String ) {
        self.images.append(SelectedImage(url: url, description: description, id: nil))
    }
    
    func addToFiles(url: String) {
        files.append(DocFile(url: url, id: nil))
    }
    
//    func saveImageToTemporaryDirectory(_ image: UIImage) -> URL? {
//        let fileManager = FileManager.default
//        let tempDirectory = fileManager.temporaryDirectory
//        let imageName = UUID().uuidString + ".jpg"
//        let imageURL = tempDirectory.appendingPathComponent(imageName)
//        
//        guard let imageData = image.jpegData(compressionQuality: 0.8) else {
//            return nil
//        }
//        
//        do{
//            try imageData.write(to: imageURL)
//            return imageURL
//        }catch {
//            AppLog.debug("Error : Saving image to temporary directory: \(error.localizedDescription)")
//            return nil
//        }
//    }
    
    
}
