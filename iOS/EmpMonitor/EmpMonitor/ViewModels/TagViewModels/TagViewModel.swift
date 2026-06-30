//
//  TagViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 12/09/24.
//

import Foundation


@MainActor
class TagViewModel: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?
    
    @Published var tagDataList: [TagResponseData] = []
    
    var urlString: String {
        return Constants.shared.baseURL + Constants.Endpoint.getTags
    }
    
    func getTags() async throws {
        isLoading = true
        defer { isLoading = false }
        
        let token = AuthStore.shared.getAccessToken()
        
        do{
            let fetchData: TagResponseModel = try await NetworkManager.shared.getData(to: urlString, as: TagResponseModel.self, accessToken: token)
            
            NetworkManager.shared.statusCode = fetchData.statusCode
            NetworkManager.shared.responseMessage = fetchData.body.message
            
            print("Got the Tag Data")
            
            tagDataList = fetchData.body.data
//            print(fetchData)
            
        }catch{
            print("Error: GetTags error -> \(error.localizedDescription)")
            self.error = error
        }
    }
}
