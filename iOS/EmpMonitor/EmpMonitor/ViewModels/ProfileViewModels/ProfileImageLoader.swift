//
//  ProfileImageLoader.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 15/10/24.
//

import Foundation
import UIKit

class ProfileImageLoader: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?

    @Published var profileImageURL: String = ""
    @Published var profileImage: UIImage?
    @Published var clientProfileImage: UIImage?

    private static let imageCache = NSCache<NSString, UIImage>()
    private var currentProfileTask: URLSessionDataTask?
    private var currentClientTask: URLSessionDataTask?

    //Load QR code image using URLSession with caching
    func loadProfileImage() {
        guard let url = URL(string: profileImageURL) else { return }

        if let cachedImage = Self.imageCache.object(forKey: profileImageURL as NSString) {
            DispatchQueue.main.async {
                self.profileImage = cachedImage
            }
            return
        }

        currentProfileTask?.cancel()
        let task = URLSession.shared.dataTask(with: url) { [weak self] data, response, error in
            guard let self = self, let data = data, let image = UIImage(data: data), error == nil else {
                DispatchQueue.main.async {
                    self?.error = error
                }
                AppLog.debug("Failed to load image: \(error?.localizedDescription ?? "Unknown error")")
                return
            }

            Self.imageCache.setObject(image, forKey: self.profileImageURL as NSString)
            DispatchQueue.main.async {
                self.profileImage = image
            }
        }
        currentProfileTask = task
        task.resume()
    }

    //Load client profile image using URLSession with caching
    func loadClientProfileImage(clientImageURL: String) {
        guard let url = URL(string: clientImageURL) else { return }

        if let cachedImage = Self.imageCache.object(forKey: clientImageURL as NSString) {
            DispatchQueue.main.async {
                self.clientProfileImage = cachedImage
            }
            return
        }

        currentClientTask?.cancel()
        let task = URLSession.shared.dataTask(with: url) { [weak self] data, response, error in
            guard let self = self, let data = data, let image = UIImage(data: data), error == nil else {
                DispatchQueue.main.async {
                    self?.error = error
                }
                AppLog.debug("Failed to load image: \(error?.localizedDescription ?? "Unknown error")")
                return
            }

            Self.imageCache.setObject(image, forKey: clientImageURL as NSString)
            DispatchQueue.main.async {
                self.clientProfileImage = image
            }
        }
        currentClientTask = task
        task.resume()
    }

    deinit {
        currentProfileTask?.cancel()
        currentClientTask?.cancel()
    }
}
