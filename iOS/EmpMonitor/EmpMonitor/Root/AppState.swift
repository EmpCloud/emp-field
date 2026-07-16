//
//  AppState.swift
//  EmpMonitor
//

import Foundation
import SwiftUI
import Security

// MARK: - Keychain Errors

enum KeychainError: Error {
    case itemNotFound
    case duplicateItem
    case invalidStatus(OSStatus)
    case invalidItemFormat
}

// MARK: - Keychain Manager

final class KeychainManager {
    static let shared = KeychainManager()
    private init() {}
    
    private let service = Bundle.main.bundleIdentifier ?? "com.empmonitor.app"
    
    private func query(for account: String) -> [String: Any] {
        [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: account
        ]
    }
    
    @discardableResult
    func save(_ data: Data, account: String) throws -> Bool {
        var query = query(for: account)
        query[kSecValueData as String] = data
        
        let status = SecItemAdd(query as CFDictionary, nil)
        if status == errSecDuplicateItem {
            let updateQuery: [String: Any] = [kSecValueData as String: data]
            let updateStatus = SecItemUpdate(query as CFDictionary, updateQuery as CFDictionary)
            guard updateStatus == errSecSuccess else {
                throw KeychainError.invalidStatus(updateStatus)
            }
            return true
        }
        guard status == errSecSuccess else {
            throw KeychainError.invalidStatus(status)
        }
        return true
    }
    
    func read(account: String) throws -> Data {
        var query = query(for: account)
        query[kSecReturnData as String] = true
        query[kSecMatchLimit as String] = kSecMatchLimitOne
        
        var result: AnyObject?
        let status = SecItemCopyMatching(query as CFDictionary, &result)
        guard status == errSecSuccess else {
            if status == errSecItemNotFound {
                throw KeychainError.itemNotFound
            }
            throw KeychainError.invalidStatus(status)
        }
        guard let data = result as? Data else {
            throw KeychainError.invalidItemFormat
        }
        return data
    }
    
    @discardableResult
    func delete(account: String) throws -> Bool {
        let status = SecItemDelete(query(for: account) as CFDictionary)
        guard status == errSecSuccess || status == errSecItemNotFound else {
            throw KeychainError.invalidStatus(status)
        }
        return true
    }
}

// MARK: - Auth Store

@MainActor
final class AuthStore: ObservableObject {
    static let shared = AuthStore()
    
    private enum KeychainAccount: String {
        case accessToken = "x-access-token"
        case loggedInUser = "loggedInUser"
        case userProfile = "UserProfile"
    }
    
    private let keychain = KeychainManager.shared
    
    @Published private(set) var isLoggedIn: Bool = false
    @Published private(set) var accessToken: String? = nil
    
    private init() {
        migrateFromUserDefaultsIfNeeded()
        self.accessToken = keychain.string(for: KeychainAccount.accessToken.rawValue)
        self.isLoggedIn = accessToken != nil && keychain.has(KeychainAccount.accessToken.rawValue) && keychain.has(KeychainAccount.loggedInUser.rawValue)
    }
    
    // MARK: Access Token
    
    func saveAccessToken(_ token: String) {
        do {
            try keychain.save(token.data(using: .utf8) ?? Data(), account: KeychainAccount.accessToken.rawValue)
            self.accessToken = token
            self.isLoggedIn = token.isEmpty == false && keychain.has(KeychainAccount.loggedInUser.rawValue)
        } catch {
            AppLog.debug("[AuthStore] Failed to save access token: \(error)")
        }
    }
    
    func getAccessToken() -> String? {
        if let token = accessToken { return token }
        let token = keychain.string(for: KeychainAccount.accessToken.rawValue)
        self.accessToken = token
        return token
    }
    
    func deleteAccessToken() {
        do {
            try keychain.delete(account: KeychainAccount.accessToken.rawValue)
            self.accessToken = nil
            self.isLoggedIn = false
        } catch {
            AppLog.debug("[AuthStore] Failed to delete access token: \(error)")
        }
    }
    
    // MARK: Logged In User
    
    func saveLoggedInUser(_ user: UserLoginResponseModel) {
        do {
            let data = try JSONEncoder().encode(user)
            try keychain.save(data, account: KeychainAccount.loggedInUser.rawValue)
            self.isLoggedIn = getAccessToken() != nil
        } catch {
            AppLog.debug("[AuthStore] Failed to save logged in user: \(error)")
        }
    }
    
    func getLoggedInUser() -> UserLoginResponseModel? {
        return keychain.decode(UserLoginResponseModel.self, account: KeychainAccount.loggedInUser.rawValue)
    }
    
    func deleteLoggedInUser() {
        do {
            try keychain.delete(account: KeychainAccount.loggedInUser.rawValue)
            self.isLoggedIn = false
        } catch {
            AppLog.debug("[AuthStore] Failed to delete logged in user: \(error)")
        }
    }
    
    // MARK: User Profile
    
    func saveUserProfileData<T: Encodable>(_ profile: T) {
        do {
            let data = try JSONEncoder().encode(profile)
            try keychain.save(data, account: KeychainAccount.userProfile.rawValue)
        } catch {
            AppLog.debug("[AuthStore] Failed to save user profile: \(error)")
        }
    }
    
    func getUserProfileData<T: Decodable>(as type: T.Type) -> T? {
        return keychain.decode(type, account: KeychainAccount.userProfile.rawValue)
    }
    
    func deleteUserProfile() {
        do {
            try keychain.delete(account: KeychainAccount.userProfile.rawValue)
        } catch {
            AppLog.debug("[AuthStore] Failed to delete user profile: \(error)")
        }
    }
    
    // MARK: Session
    
    func clearSession() {
        deleteAccessToken()
        deleteLoggedInUser()
        deleteUserProfile()
        let keysToRemove = [
            "isCheckedIN", "UserName", "UserDepartment", "UserProfilePic",
            "autoCheckInByGeoFencing", "autoCheckInByMobile",
            "isMobileDeviceEnabled", "isGeoFencingOn",
            "OrgLatitude", "OrgLongitude", "OrgRadius",
            "CurrentFrequency", "CurrentRadius",
            "offlineLocations", "lastAutoCheckInTime"
        ]
        keysToRemove.forEach { UserDefaults.standard.removeObject(forKey: $0) }
        Task { try? await LocationQueueService.shared.deleteAll() }
    }
    
    // MARK: Migration
    
    private func migrateFromUserDefaultsIfNeeded() {
        guard !UserDefaults.standard.bool(forKey: "AuthStoreMigrated") else { return }
        
        if let token = UserDefaults.standard.string(forKey: "x-access-token"), !token.isEmpty {
            _ = try? keychain.save(token.data(using: .utf8) ?? Data(), account: KeychainAccount.accessToken.rawValue)
        }
        if let userData = UserDefaults.standard.data(forKey: "loggedInUser") {
            _ = try? keychain.save(userData, account: KeychainAccount.loggedInUser.rawValue)
        }
        if let profileData = UserDefaults.standard.data(forKey: "UserProfile") {
            _ = try? keychain.save(profileData, account: KeychainAccount.userProfile.rawValue)
        }
        
        UserDefaults.standard.set(true, forKey: "AuthStoreMigrated")
        UserDefaults.standard.removeObject(forKey: "x-access-token")
        UserDefaults.standard.removeObject(forKey: "loggedInUser")
        UserDefaults.standard.removeObject(forKey: "UserProfile")
    }
}

// MARK: - Keychain Helpers

private extension KeychainManager {
    func string(for account: String) -> String? {
        guard let data = try? read(account: account) else { return nil }
        return String(data: data, encoding: .utf8)
    }
    
    func decode<T: Decodable>(_ type: T.Type, account: String) -> T? {
        guard let data = try? read(account: account) else { return nil }
        return try? JSONDecoder().decode(type, from: data)
    }
    
    func has(_ account: String) -> Bool {
        return (try? read(account: account)) != nil
    }
}

// MARK: - App State

@MainActor
final class AppState: ObservableObject {
    static let shared = AppState()

    @Published var isLoggedIn: Bool = false

    /// Non-nil when the backend reported the session as invalid (e.g. logged in on
    /// another device / token expired). The UI observes this to present a logout alert.
    @Published var sessionExpiredMessage: String? = nil

    private init() {
        self.isLoggedIn = AuthStore.shared.isLoggedIn
    }

    func updateLoginState() {
        self.isLoggedIn = AuthStore.shared.isLoggedIn
    }

    /// Forces a logout when the backend reports the session is no longer valid.
    /// Clears stored credentials and surfaces a message the UI can alert on.
    /// Guarded so overlapping API failures only trigger a single alert/logout.
    func handleSessionExpired(message: String?) {
        guard sessionExpiredMessage == nil else { return }
        AuthStore.shared.clearSession()
        self.isLoggedIn = false
        self.sessionExpiredMessage = message?.isEmpty == false
            ? message
            : "Your session has expired. Please log in again."
    }
}
