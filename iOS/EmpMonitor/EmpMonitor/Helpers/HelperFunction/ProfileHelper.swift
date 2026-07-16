//
//  ProfileHelper.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 06/09/24.
//

import Foundation
import UIKit

class ProfileHelper {
    static let shared = ProfileHelper()

    private let cachedImageFileName = "cached_profile_pic"

    func updateProfilePic(with imageURL: URL?) {
        guard let validURL = imageURL else { return }
        saveImageToDisk(imageURL: validURL)
    }

    func saveImageToDisk(imageURL: URL) {
        DispatchQueue.global(qos: .background).async {
            guard let imageData = try? Data(contentsOf: imageURL) else {
                AppLog.debug("ProfileHelper: failed to download image from \(imageURL)")
                return
            }
            let fileURL = self.cachedImageFileURL()
            do {
                try imageData.write(to: fileURL, options: .atomic)
            } catch {
                AppLog.debug("ProfileHelper: failed to save image to disk — \(error)")
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
}
