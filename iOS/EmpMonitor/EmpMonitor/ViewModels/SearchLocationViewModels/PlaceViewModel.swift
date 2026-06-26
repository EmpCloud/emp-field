//
//  PlaceViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 08/07/24.
//

import Foundation
import MapKit

@MainActor
class PlaceViewModel: NSObject, ObservableObject, MKLocalSearchCompleterDelegate {
    @Published var places: [Place] = []
    private let searchCompleter = MKLocalSearchCompleter()
    private var debounceTimer: Timer?
    
    @Published var queryFragment: String = "" {
           didSet {
               print(queryFragment)
               debounceSearch(queryFragment)
           }
       }
    
    override init() {
            super.init()
            searchCompleter.delegate = self
            searchCompleter.resultTypes = .address
        }
    
    private func debounceSearch(_ query: String) {
            debounceTimer?.invalidate()
            debounceTimer = Timer.scheduledTimer(withTimeInterval: 0.5, repeats: false) { _ in
                self.searchCompleter.queryFragment = query
                print("Input Para: \(query)")
            }
        }
    
    func search(text: String, region: MKCoordinateRegion) {
        let searchRequest = MKLocalSearch.Request()
        searchRequest.naturalLanguageQuery = text
        searchRequest.region = region
        let search = MKLocalSearch(request: searchRequest)
        
        search.start { response, error in
            if let error = error {
                print("Error: \(error.localizedDescription)")
                return
            }
            
            guard let response = response else {
                print("No response.")
                return
            }
            
            self.places = response.mapItems.map(Place.init)
            print("Places: \(self.places)")
        }
    }
    
    
    func completerDidUpdateResults(_ completer: MKLocalSearchCompleter) {
        // Handle search completion results if needed
//        completer.results.first.ti
    }
    
    func completer(_ completer: MKLocalSearchCompleter, didFailWithError error: Error) {
        print("Local search completer failed with error: \(error.localizedDescription)")
    }
}
