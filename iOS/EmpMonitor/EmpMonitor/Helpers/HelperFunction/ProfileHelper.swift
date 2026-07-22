//
//  ProfileHelper.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 06/09/24.
//

import Foundation
import UIKit

final class ProfileHelper: Sendable {
    static let shared = ProfileHelper()

    private let cachedImageFileName = "cached_profile_pic"

    func updateProfilePic(with imageURL: URL?) {
        guard let validURL = imageURL else { return }
        saveImageToDisk(imageURL: validURL)
    }

    func saveImageToDisk(imageURL: URL) {
        if imageURL.isFileURL {
            copyImageFile(from: imageURL)
            return
        }

        URLSession.shared.downloadTask(with: imageURL) { [weak self] temporaryURL, _, error in
            if let error {
                AppLog.debug("ProfileHelper: failed to download image from \(imageURL) - \(error.localizedDescription)")
                return
            }

            guard let self, let temporaryURL else {
                AppLog.debug("ProfileHelper: downloaded image is unavailable for \(imageURL)")
                return
            }

            do {
                try self.replaceCachedImage(with: temporaryURL)
            } catch {
                AppLog.debug("ProfileHelper: failed to save image to disk - \(error.localizedDescription)")
            }
        }.resume()
    }

    private func copyImageFile(from imageURL: URL) {
        DispatchQueue.global(qos: .background).async {
            do {
                try self.replaceCachedImage(with: imageURL)
            } catch {
                AppLog.debug("ProfileHelper: failed to copy image to disk - \(error.localizedDescription)")
            }
        }
    }

    func loadImageFromDisk() -> UIImage? {
        let fileURL = cachedImageFileURL()
        guard let data = try? Data(contentsOf: fileURL) else { return nil }
        return UIImage(data: data)
    }

    func removeImageFromDisk() {
        try? FileManager.default.removeItem(at: cachedImageFileURL())
    }

    private func cachedImageFileURL() -> URL {
        let caches = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask)[0]
        return caches.appendingPathComponent(cachedImageFileName)
    }

    private func replaceCachedImage(with sourceURL: URL) throws {
        let fileManager = FileManager.default
        let fileURL = cachedImageFileURL()

        if fileManager.fileExists(atPath: fileURL.path) {
            try fileManager.removeItem(at: fileURL)
        }

        try fileManager.copyItem(at: sourceURL, to: fileURL)
    }
}
