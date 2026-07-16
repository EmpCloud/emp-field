//
//  SearchLocationViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 28/06/24.
//

import Foundation
import MapKit

@MainActor
class SearchLocationViewModel: NSObject, ObservableObject {
    
    
    //MARK: Properties
    
    @Published var results = [MKLocalSearchCompletion]()
    @Published var selectedLocation: MKLocalSearchCompletion?
    @Published var selectedLocationCoordinate: CLLocationCoordinate2D?
    @Published var userCurrentLocation:CLLocationCoordinate2D?
    @Published var selectedLocationTitle: String = ""
    @Published var selectedLocationSubtitle: String = ""
    @Published var selectedLocationState: String = ""
    @Published var selectedLocationCity: String = ""
    @Published var selectedLocationZipcode: String = ""
    @Published var selectedLocationCountry: String = ""
    @Published var selectedLocationLatitude: Double?
    @Published var selectedLocationLongitude: Double?
    
    private let searchCompleter = MKLocalSearchCompleter()
    
    var queryFragment: String = "" {
        didSet{
            AppLog.debug("DEBUG: Query fragment is \(queryFragment)")
            searchCompleter.queryFragment = queryFragment
        }
    }
    
    override init(){
        super.init()
        searchCompleter.delegate = self
        searchCompleter.queryFragment = queryFragment
    }
    
    //MARK: Helper
    func selectedLocation(_ localSearchLocation: MKLocalSearchCompletion) {
        self.selectedLocation = localSearchLocation // to display the selected Location in the MapView

        locationSearch(forLocalSearchCompletion: localSearchLocation) { [weak self] response, error in
            guard let self = self else { return }

            if let error = error {
                AppLog.debug("DEBUG: Location search failed with error \(error)")
                return
            }

//            guard let response = response else { return }
//
//            var matchingCoordinate: CLLocationCoordinate2D?
//
//
//            for item in response.mapItems {
//                AppLog.debug("Name: \(item.name)")
//                AppLog.debug("Address: \(item.placemark.title)")
//                AppLog.debug("SelectedLocation : \(self.selectedLocation?.title)")
//                AppLog.debug("Local Search Location : \(localSearchLocation.title)")
//                if item.name == localSearchLocation.title  {
//                    matchingCoordinate = item.placemark.coordinate
//                    break
//                }
//            }
//
//            if let coordinate = matchingCoordinate {
//                self.selectedLocationCoordinate = coordinate
//                AppLog.debug("DEBUG: Location coordinates \(coordinate)")
//            }else {
//                AppLog.debug("DEBUG: No matching coordinates found for selected location")
//            }


            guard let item = response?.mapItems.first else { return }
            let coordinate = item.placemark.coordinate
            self.selectedLocationCoordinate = coordinate
//            DispatchQueue.main.async {
//                self.selectedLocationCoordinate = coordinate
//                AppLog.debug("DEBUG: Location coordinates \(coordinate)")
//            }

            AppLog.debug("DEBUG: Location coordinates \(coordinate)")
            
            //This will add address in the address view just after search
            let title = item.placemark.name ?? "Unkown location"
            let subtitle = "\(item.placemark.locality ?? ""), \(item.placemark.administrativeArea ?? ""), \(item.placemark.country ?? "")"
            let state = item.placemark.administrativeArea ?? ""
            let city = item.placemark.locality ?? ""
            let zipCode = item.placemark.postalCode ?? ""
            let country = item.placemark.country ?? ""
            
            
            DispatchQueue.main.async {
                self.selectedLocationTitle = title
                self.selectedLocationSubtitle = subtitle
                self.selectedLocationState = state
                self.selectedLocationCity = city
                self.selectedLocationCountry = country
                self.selectedLocationZipcode = zipCode
                self.selectedLocationLatitude = coordinate.latitude
                self.selectedLocationLongitude = coordinate.longitude
                
                AppLog.debug("Location: \(title), \(subtitle)")
                AppLog.debug("State: \(state)")
                AppLog.debug("City: \(city)")
                AppLog.debug("Country: \(country)")
                AppLog.debug("ZipCode: \(zipCode)")
                AppLog.debug("Search Coordinate")
                AppLog.debug("Coordinate: \(coordinate.latitude), \(coordinate.longitude)")

            }
        }
        
    }
    
    // to search the exact location coordinates from the title and subtitle
    func locationSearch(forLocalSearchCompletion localSearch: MKLocalSearchCompletion, completion: @escaping MKLocalSearch.CompletionHandler) {
        let searchRequest = MKLocalSearch.Request()
        searchRequest.naturalLanguageQuery = "\(localSearch.title) \(localSearch.subtitle)"
        let search = MKLocalSearch(request: searchRequest)
        
        search.start(completionHandler: completion)
    }
    
}


//MARK: MKLocalSearchCompleterDelegate

extension SearchLocationViewModel: MKLocalSearchCompleterDelegate {
    nonisolated func completerDidUpdateResults(_ completer: MKLocalSearchCompleter) {
        Task { @MainActor [weak self] in
            self?.results = completer.results
        }
    }
}
