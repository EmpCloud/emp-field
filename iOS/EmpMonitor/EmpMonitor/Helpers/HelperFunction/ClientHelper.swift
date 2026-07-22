//
//  ClientHelper.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 30/08/24.
//

import Foundation
import UIKit

@MainActor
final class ClientHelper {
    
    static let shared = ClientHelper()
    
    func sendMessage(to phoneNumber: String) {
        let sms = "sms:\(phoneNumber)"
        
        guard let url = URL(string: sms) else { return }
        
        if UIApplication.shared.canOpenURL(url) {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        }else {
            AppLog.debug("Cannot open message app")
        }
    }
    
    func makeCall(to phoneNumber: String) {
        let tel = "tel:\(phoneNumber)"
        
        guard let url = URL(string: tel) else { return }
        
        if UIApplication.shared.canOpenURL(url) {
            UIApplication.shared.open(url,options: [:], completionHandler: nil)
        }else{
            AppLog.debug("Cannot open dialing app")
        }
    }
    
}
