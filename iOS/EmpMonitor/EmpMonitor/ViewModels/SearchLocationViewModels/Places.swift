//
//  Places.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 08/07/24.
//

import Foundation
import MapKit

struct Place: Identifiable {
    let id = UUID().uuidString
    private var mapItem: MKMapItem
    
    init(mapItem: MKMapItem) {
        self.mapItem = mapItem
    }
    
    var name: String {
        self.mapItem.name ?? ""
    }
    
    var address: String {
        let placemark = self.mapItem.placemark
        var cityAndState = ""
        var address = ""
        
        cityAndState = placemark.locality ?? "" // city
        if let state = placemark.administrativeArea {
            //show either state or  city, state
            cityAndState = cityAndState.isEmpty ? state : "\(cityAndState), \(state)"
        }
        
        address = placemark.subThoroughfare ?? "" // address number
        if let street = placemark.thoroughfare {
            // just show the street unless there is a street number then add space + street
            address = address.isEmpty ? street : "\(address) \(street)"
        }
        
        if address.trimmingCharacters(in: .whitespaces).isEmpty && !cityAndState.isEmpty {
            // No address? Then just cityAndState with no space
            address = cityAndState
        }else{
            // no cityAndState? then just address, otherwise address, cityAndState
            address = cityAndState.isEmpty ? address : "\(address), \(cityAndState)"
        }
        
        return address
    }
    
    var latitude: Double {  //CLLocationDegress is of type Double
        self.mapItem.placemark.coordinate.latitude
    }
    
    var longitude: Double {  //CLLocationDegress is of type Double
        self.mapItem.placemark.coordinate.longitude
    }
}
