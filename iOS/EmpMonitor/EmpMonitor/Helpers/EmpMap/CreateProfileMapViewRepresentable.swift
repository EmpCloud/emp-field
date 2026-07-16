//
//  CreateProfileMapViewRepresentable.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 01/07/24.
//


import SwiftUI
import MapKit

struct CreateProfileMapViewRepresentable: UIViewRepresentable {
    @EnvironmentObject var searchLocationViewModel: SearchLocationViewModel
    @EnvironmentObject var createProfileViewModel: CreateProfileViewModel
    
    @Binding var mapCoordinatorBinding: MapCoordinator?
    
    let mapView = MKMapView()
//    let annotation = MKPointAnnotation()
    
    func makeUIView(context: Context) -> some UIView {
        mapView.delegate = context.coordinator
        mapView.isRotateEnabled = false
        mapView.showsUserLocation = true
        mapView.userTrackingMode = .none  // to prevent automatic centering on user location
        mapView.isUserInteractionEnabled = true
        
        mapView.isZoomEnabled = true
        mapView.isScrollEnabled = true
        
        //Add annotation (pin) at the center
//        let centerCoodrinate = mapView.centerCoordinate
//        let annotation = MKPointAnnotation()
//        annotation.coordinate = centerCoodrinate
//        mapView.addAnnotation(annotation)
        
        // Center the map on the user's location when the view loads
        if let userLocation = mapView.userLocation.location?.coordinate {
            let region = MKCoordinateRegion (
                center: userLocation,
                span: MKCoordinateSpan(latitudeDelta: 0.005, longitudeDelta: 0.005)
            )
            mapView.setRegion(region, animated: true)
            DispatchQueue.main.async {
                searchLocationViewModel.selectedLocationCoordinate = userLocation
                searchLocationViewModel.userCurrentLocation = userLocation
            }
        }
        
        //add gesture recognizer to handle map dragging
        let panGesture = UIPanGestureRecognizer(target: context.coordinator, action: #selector(context.coordinator.handlePanGesture(_:)))
        panGesture.delegate = (context.coordinator as any UIGestureRecognizerDelegate)
        mapView.addGestureRecognizer(panGesture)
        
        DispatchQueue.main.async {
            self.mapCoordinatorBinding = context.coordinator  // passing the mapCoordinator object to other struct
        }
        return mapView
    }
    
    func updateUIView(_ uiView: UIViewType, context: Context) {
        if let coordinate = searchLocationViewModel.selectedLocationCoordinate {
            context.coordinator.updateRegion(to: coordinate)
        }
    }
    
    func makeCoordinator() -> MapCoordinator {
        MapCoordinator(parent: self)
    }
}

extension CreateProfileMapViewRepresentable {
    
//    @MainActor
    class MapCoordinator: NSObject, MKMapViewDelegate, UIGestureRecognizerDelegate {
        
        //MARK: Properties
        let parent: CreateProfileMapViewRepresentable
        
        let annotation = MKPointAnnotation()
        var isInitialRegionSet = false
//        var userLocation: CLLocationCoordinate2D?
        
//        var currentSpan = MKCoordinateSpan(latitudeDelta: 0.005, longitudeDelta: 0.005)
        
        //MARK: Lifecycle
        init(parent: CreateProfileMapViewRepresentable) {
            self.parent = parent
            super.init()
        }
        
//        @MainActor
//        func mapView(_ mapView: MKMapView, didUpdate userLocation: MKUserLocation) {
//            if parent.searchLocationViewModel.selectedLocationCoordinate == nil {
//                let region = MKCoordinateRegion(
//                    center: userLocation.coordinate,
//                    span: MKCoordinateSpan(latitudeDelta: 0.005, longitudeDelta: 0.005)
//                )
//                parent.mapView.setRegion(region, animated: true)
//                
//                DispatchQueue.main.async {
//                    self.parent.searchLocationViewModel.selectedLocationCoordinate = userLocation.coordinate
//                    if let annotation = mapView.annotations.first as? MKPointAnnotation {
//                        annotation.coordinate = userLocation.coordinate
//                    }
//                }
//            }
//        }
        
//        func mapView(_ mapView: MKMapView, regionDidChangeAnimated animated: Bool) {
//            let centerCoordinate = mapView.centerCoordinate
////            if let annotation = mapView.annotations.first as? MKPointAnnotation {
//                annotation.coordinate = centerCoordinate
////            }
//            DispatchQueue.main.async {
//                    self.parent.searchLocationViewModel.selectedLocationCoordinate = centerCoordinate
//                    AppLog.debug("Pin coordinate: \(centerCoordinate.latitude), \(centerCoordinate.longitude)")
//                }
//        }
        
        func updateRegion(to coordinate: CLLocationCoordinate2D) {
            let currentSpan = parent.mapView.region.span // this will make the span dynamic for the user
            let region = MKCoordinateRegion(
                center: coordinate,
//                span: MKCoordinateSpan(latitudeDelta: 0.005, longitudeDelta: 0.005)  // just stop the dynamic span for presentation
                span: currentSpan
            )
//            self.currentSpan = parent.mapView.region.span
            parent.mapView.setRegion(region, animated: true)
            
            
            parent.mapView.removeAnnotations(parent.mapView.annotations) // remove all the annotation present in the map View
            
//            let annotation = MKPointAnnotation()
            annotation.coordinate = coordinate
            self.parent.mapView.addAnnotation(annotation)
            self.parent.mapView.selectAnnotation(annotation, animated: true)
            
//            reverseGeocodeLocation(for: coordinate)
        }
        
        
        @MainActor
        @objc func handlePanGesture(_ gesture: UIPanGestureRecognizer) {
            guard gesture.state == .ended else { return }
            AppLog.debug("Handle Gesture is called")
            
            let mapView = parent.mapView
            let centerCoordinate = mapView.centerCoordinate
            
//            if let annotation = mapView.annotations.first as? MKPointAnnotation {
//                annotation.coordinate = centerCoordinate
//            }
            
//            DispatchQueue.main.async {
//                self.parent.searchLocationViewModel.selectedLocationCoordinate = centerCoordinate
//                AppLog.debug("Pin coordinate: \(centerCoordinate.latitude), \(centerCoordinate.longitude)")
//            }
            
            updateAnnotation(to: centerCoordinate)
            reverseGeocodeLocation(for: centerCoordinate)
        }
        
        
        func mapView(_ mapView: MKMapView, didUpdate userLocation: MKUserLocation) {
            if !isInitialRegionSet {
                setInitialRegion(userLocation.coordinate)
                isInitialRegionSet = true
            }
        }
        
        private func setInitialRegion(_ coordinate: CLLocationCoordinate2D) {
            let region = MKCoordinateRegion(
                center: coordinate,
                span: MKCoordinateSpan(latitudeDelta: 0.005, longitudeDelta: 0.005))
            
            parent.mapView.setRegion(region, animated: true)
            
            DispatchQueue.main.async {
                self.parent.searchLocationViewModel.selectedLocationCoordinate = coordinate
                self.parent.searchLocationViewModel.userCurrentLocation = coordinate    // setting userCurrentLocation for button
                self.annotation.coordinate = coordinate
                self.parent.mapView.addAnnotation(self.annotation)
                self.parent.mapView.selectAnnotation(self.annotation, animated: true)
            }
            
            // User current location name will be displayed in the MapView first time
            reverseGeocodeLocation(for: coordinate)
        }
        
        func updateAnnotation(to coordinate: CLLocationCoordinate2D) {
            annotation.coordinate = coordinate
            
            DispatchQueue.main.async {
                self.parent.searchLocationViewModel.selectedLocationCoordinate = coordinate
                AppLog.debug("Pin coordinate: \(coordinate.latitude), \(coordinate.longitude)")
                
                //passing the coordinate value to the createProfileViewModel
                self.parent.createProfileViewModel.latitude = String(coordinate.latitude)
                self.parent.createProfileViewModel.longitude = String(coordinate.longitude)
                
                AppLog.debug(self.parent.createProfileViewModel.latitude)
                AppLog.debug(self.parent.createProfileViewModel.longitude)
                
            }
        }
        
        //UIGestureRecognizerDelegate method to allow simultaneous gesture recognition
        func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldRecognizeSimultaneouslyWith otherGestureRecognizer: UIGestureRecognizer) -> Bool {
            return true
        }
        
        //Reverse Geocoding to get location name from coordinate
        func reverseGeocodeLocation(for coordinate: CLLocationCoordinate2D) {
            let location = CLLocation(latitude: coordinate.latitude, longitude: coordinate.longitude)
            let geocoder = CLGeocoder()
            
            geocoder.reverseGeocodeLocation(location) { [weak self] placemarks, error in
                guard let self = self else { return }
                if let error = error {
                    AppLog.debug("Reverse geocoding failed with error: \(error)")
                    return
                }
                guard let placemark = placemarks?.first else {
                    AppLog.debug("No placemarks found")
                    return
                }
                
                //This will add address in the address view just after drag
                let title = placemark.name ?? "Unkown location"
                let subtitle = "\(placemark.locality ?? ""), \(placemark.administrativeArea ?? ""), \(placemark.country ?? "")"
                let state = placemark.administrativeArea ?? ""
                let city = placemark.locality ?? ""
                let zipCode = placemark.postalCode ?? ""
                let country = placemark.country ?? ""
                
                
                DispatchQueue.main.async {
                    self.parent.searchLocationViewModel.selectedLocationTitle = title
                    self.parent.searchLocationViewModel.selectedLocationSubtitle = subtitle
                    self.parent.searchLocationViewModel.selectedLocationState = state
                    self.parent.searchLocationViewModel.selectedLocationCity = city
                    self.parent.searchLocationViewModel.selectedLocationZipcode = zipCode
                    self.parent.searchLocationViewModel.selectedLocationCountry = country
                    self.parent.searchLocationViewModel.selectedLocationLatitude = coordinate.latitude
                    self.parent.searchLocationViewModel.selectedLocationLongitude = coordinate.longitude
                    
                    AppLog.debug("Location: \(title), \(subtitle)")
                    AppLog.debug("State: \(state)")
                    AppLog.debug("City: \(city)")
                    AppLog.debug("ZipCode: \(zipCode)")
                    AppLog.debug("Dragged Pin Coordinate")
                    AppLog.debug("Coordinate: \(coordinate.latitude), \(coordinate.longitude)")
                    
                    AppLog.debug("Searched pinCoordinate")
                    AppLog.debug("\(self.parent.searchLocationViewModel.selectedLocationLatitude), \(self.parent.searchLocationViewModel.selectedLocationLongitude)")
                }
            }
        }
        
        //MARK: To point the user current location
        func centerOnUserLocation(userLocation: CLLocationCoordinate2D) {
            let region = MKCoordinateRegion(
                center: userLocation,
                span: MKCoordinateSpan(latitudeDelta: 0.005, longitudeDelta: 0.005)
                )
            
            parent.mapView.setRegion(region, animated: true)
            updateAnnotation(to: userLocation)
            
            //Reverse geocode to update the location details
            reverseGeocodeLocation(for: userLocation)
        }
        
    }
}
