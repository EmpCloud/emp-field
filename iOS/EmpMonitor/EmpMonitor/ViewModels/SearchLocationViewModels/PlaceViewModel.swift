//
//  PlaceViewModel.swift
//  EmpMonitor
//

import Foundation
import MapKit

@MainActor
final class PlaceViewModel: NSObject, ObservableObject, MKLocalSearchCompleterDelegate {
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
    
    deinit {
        debounceTimer?.invalidate()
    }
    
    private func debounceSearch(_ query: String) {
        debounceTimer?.invalidate()
        debounceTimer = Timer.scheduledTimer(withTimeInterval: 0.5, repeats: false) { [weak self] _ in
            Task { @MainActor [weak self] in
                self?.searchCompleter.queryFragment = query
            }
            print("Input Para: \(query)")
        }
    }
    
    func search(text: String, region: MKCoordinateRegion) {
        let searchRequest = MKLocalSearch.Request()
        searchRequest.naturalLanguageQuery = text
        searchRequest.region = region
        let search = MKLocalSearch(request: searchRequest)
        
        search.start { [weak self] response, error in
            Task { @MainActor [weak self] in
                if let error = error {
                    print("Error: \(error.localizedDescription)")
                    return
                }
                
                guard let response = response else {
                    print("No response.")
                    return
                }
                
                self?.places = response.mapItems.map(Place.init)
                print("Places: \(self?.places ?? [])")
            }
        }
    }
    
    nonisolated func completerDidUpdateResults(_ completer: MKLocalSearchCompleter) {
        // Handle search completion results if needed
    }
    
    nonisolated func completer(_ completer: MKLocalSearchCompleter, didFailWithError error: Error) {
        print("Local search completer failed with error: \(error.localizedDescription)")
    }
}
