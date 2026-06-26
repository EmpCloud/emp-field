//
//  UserDefaults.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 26/06/24.
//

import Foundation

extension UserDefaults {
    
    func setObject<Object>(_ object: Object, forKey key: String) where Object: Encodable {
        let encoder = JSONEncoder()
        if let encoded = try? encoder.encode(object) {
            set(encoded, forKey: key)
        }
    }
    
    func getObject<Object>(forKey key: String, as type: Object.Type) -> Object? where Object: Decodable {
        if let data = data(forKey: key){
            let decoder = JSONDecoder()
            return try? decoder.decode(type, from: data)
        }
        return nil
    }
    
    func removeUser(forkey key: String) {
        removeObject(forKey: key)
    }
}
