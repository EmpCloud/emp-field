//
//  ProfileImageLoader.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 15/10/24.
//

import Foundation
import UIKit

@MainActor
final class ProfileImageLoader: ObservableObject {
    @Published var isLoading: Bool = false
    @Published var error: Error?

    @Published var profileImageURL: String = ""
    @Published var profileImage: UIImage?
    @Published var clientProfileImage: UIImage?

    private static let imageCache = NSCache<NSString, UIImage>()
    private var currentProfileTask: Task<Void, Never>?
    private var currentClientTask: Task<Void, Never>?

    //Load QR code image using URLSession with caching
    func loadProfileImage() {
        let cacheKey = profileImageURL
        guard let url = URL(string: cacheKey) else { return }

        if let cachedImage = Self.imageCache.object(forKey: cacheKey as NSString) {
            profileImage = cachedImage
            return
        }

        currentProfileTask?.cancel()
        currentProfileTask = Task { [weak self] in
            do {
                let (data, _) = try await URLSession.shared.data(from: url)
                guard !Task.isCancelled,
                      let self,
                      let image = UIImage(data: data) else {
                    return
                }

                Self.imageCache.setObject(image, forKey: cacheKey as NSString)
                guard self.profileImageURL == cacheKey else { return }
                self.profileImage = image
            } catch is CancellationError {
                return
            } catch {
                self?.error = error
                AppLog.debug("Failed to load image: \(error.localizedDescription)")
            }
        }
    }

    //Load client profile image using URLSession with caching
    func loadClientProfileImage(clientImageURL: String) {
        let cacheKey = clientImageURL
        guard let url = URL(string: cacheKey) else { return }

        if let cachedImage = Self.imageCache.object(forKey: cacheKey as NSString) {
            clientProfileImage = cachedImage
            return
        }

        currentClientTask?.cancel()
        currentClientTask = Task { [weak self] in
            do {
                let (data, _) = try await URLSession.shared.data(from: url)
                guard !Task.isCancelled,
                      let self,
                      let image = UIImage(data: data) else {
                    return
                }

                Self.imageCache.setObject(image, forKey: cacheKey as NSString)
                self.clientProfileImage = image
            } catch is CancellationError {
                return
            } catch {
                self?.error = error
                AppLog.debug("Failed to load image: \(error.localizedDescription)")
            }
        }
    }

    deinit {
        currentProfileTask?.cancel()
        currentClientTask?.cancel()
    }
}
