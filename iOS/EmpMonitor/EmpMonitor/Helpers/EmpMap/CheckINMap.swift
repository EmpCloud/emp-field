//
//  CheckINMap.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 20/08/24.
//

import SwiftUI
import MapKit

@MainActor
struct CheckINMap: UIViewRepresentable {
    
    @Binding var userLocation: CLLocationCoordinate2D?
    var officeLocation: CLLocationCoordinate2D
    let radius: CLLocationDistance
    @Binding var checkINAccessDistance: Int?
    
//    var lastPolylineDrawTime: Date?
    @State var lastUserLocation: CLLocationCoordinate2D?
    
    let mapView = MKMapView()
//    let annotation = MKPointAnnotation()
    
    @MainActor
    func makeUIView(context: Context) -> some UIView {
        mapView.delegate = context.coordinator
        mapView.isRotateEnabled = false
        mapView.showsUserLocation = true
        mapView.userTrackingMode = .none  // to prevent automatic centering on user location
        mapView.isUserInteractionEnabled = true
        
        mapView.isZoomEnabled = true
        mapView.isScrollEnabled = true
        
        
        // Center the map on the user's location when the view loads
        if let userLocation = mapView.userLocation.location?.coordinate {
            let region = MKCoordinateRegion (
                center: userLocation,
                span: MKCoordinateSpan(latitudeDelta: 0.005, longitudeDelta: 0.005)
            )
            mapView.setRegion(region, animated: true)
//            self.userLocation = userLocation
        }
        
       // add the circular overlay (geofence) around the office location
        let circle = MKCircle(center: officeLocation, radius: radius)
        mapView.addOverlay(circle)
        
        return mapView
    }
    
    func updateUIView(_ uiView: UIViewType, context: Context) {
//        if let coordinate = searchLocationViewModel.selectedLocationCoordinate {
//            context.coordinator.updateRegion(to: coordinate)
//        }
    }
    
    //MARK: func to draw polyline
    private func drawPolyline(_ mapView: MKMapView) {
//        
//        guard shouldDrawPolyline() else { return }
//
//        lastPolylineDrawTime = Date()
        
        guard let currentLocation = userLocation else { return }

         if let lastLocation = lastUserLocation {
             let distanceMoved = CLLocation(latitude: lastLocation.latitude, longitude: lastLocation.longitude)
                 .distance(from: CLLocation(latitude: currentLocation.latitude, longitude: currentLocation.longitude))
             
             guard distanceMoved > 50 else { return } // Only draw if moved more than 50 meters
         }

         lastUserLocation = currentLocation
        
        let userPlacemark = MKPlacemark(coordinate: userLocation ?? CLLocationCoordinate2D(latitude: 12.970005196080612, longitude:  77.58629190248757))
        let officePlacemark = MKPlacemark(coordinate: officeLocation)
        
        let request = MKDirections.Request()
        request.source = MKMapItem(placemark: userPlacemark)
        request.destination = MKMapItem(placemark: officePlacemark)
        request.transportType = .automobile
        
        let directions = MKDirections(request: request)
        directions.calculate { response, error in
            guard let response = response else { return }
//            mapView.removeOverlays(mapView.overlays)     // this was removing the Circular overlay
            if let route = response.routes.first {
                mapView.addOverlay(route.polyline)
                mapView.setVisibleMapRect(route.polyline.boundingMapRect, animated: true)
            }
        }
    }
//    //MARK: Prevent Frequent draw of polyline
//    private func shouldDrawPolyline() -> Bool {
//        guard let lastTime = lastPolylineDrawTime else {
//            return true
//        }
//        return Date().timeIntervalSince(lastTime) > 10 // Allow redraw only if 10 seconds have passed
//    }

    
    func makeCoordinator() -> MapCoordinator {
        MapCoordinator(parent: self)
    }
    
    func calculateDistance(from userLocation: CLLocationCoordinate2D, to officeLocation: CLLocationCoordinate2D)  {
        let userLocation = CLLocation(latitude: userLocation.latitude, longitude: userLocation.longitude)
        let officeLocation = CLLocation(latitude: officeLocation.latitude, longitude: officeLocation.longitude)
        
        let distanceInMeters = userLocation.distance(from: officeLocation)
        
        checkINAccessDistance =  Int(distanceInMeters)
    }


}


extension CheckINMap {
    
//    @MainActor
    class MapCoordinator: NSObject, MKMapViewDelegate, UIGestureRecognizerDelegate {
        
        //MARK: Properties
        let parent: CheckINMap
        
        let annotation = MKPointAnnotation()
        var isInitialRegionSet = false
        
        
        //MARK: Lifecycle
        init(parent: CheckINMap) {
            self.parent = parent
            super.init()
        }
        
        
        func updateRegion(to coordinate: CLLocationCoordinate2D) {
            let currentSpan = parent.mapView.region.span // this will make the span dynamic for the user
            let region = MKCoordinateRegion(
                center: coordinate,
                span: currentSpan
            )
            parent.mapView.setRegion(region, animated: true)
            
            
            parent.mapView.removeAnnotations(parent.mapView.annotations) // remove all the annotation present in the map View
            
            annotation.coordinate = coordinate
            self.parent.mapView.addAnnotation(annotation)
            self.parent.mapView.selectAnnotation(annotation, animated: true)
            
        }
        
        
        func mapView(_ mapView: MKMapView, didUpdate userLocation: MKUserLocation) {
            if !isInitialRegionSet {
                setInitialRegion(userLocation.coordinate)
                isInitialRegionSet = true
            }
            Task {
                parent.userLocation = userLocation.coordinate
                parent.drawPolyline(mapView) // draw polyline after setting up the user location
                parent.calculateDistance(from: userLocation.coordinate, to: parent.officeLocation)
            }
        }
        
        private func setInitialRegion(_ coordinate: CLLocationCoordinate2D) {
            let region = MKCoordinateRegion(
                center: coordinate,
                span: MKCoordinateSpan(latitudeDelta: 0.005, longitudeDelta: 0.005))
            
            parent.mapView.setRegion(region, animated: true)
            addAndSelectAnnotation(withCoordinate: parent.officeLocation)
//            parent.drawPolyline(parent.mapView)
        }
        
        func updateAnnotation(to coordinate: CLLocationCoordinate2D) {
            annotation.coordinate = coordinate
        }
        
        
        //MARK: To point the user current location
        func centerOnUserLocation(userLocation: CLLocationCoordinate2D) {
            let region = MKCoordinateRegion(
                center: userLocation,
                span: MKCoordinateSpan(latitudeDelta: 0.005, longitudeDelta: 0.005)
                )
            
            parent.mapView.setRegion(region, animated: true)
            updateAnnotation(to: userLocation)
            
        }
        
        //MARK: For Polyline
//        func mapView(_ mapView: MKMapView, rendererFor overlay: any MKOverlay) -> MKOverlayRenderer {
//            if let polyline = overlay as? MKPolyline {
//                let renderer = MKPolylineRenderer(overlay: polyline)
//                renderer.strokeColor = .primaryButton1
//                renderer.lineWidth = 4
//                return renderer
//            }
//            return MKOverlayRenderer(overlay: overlay)
//        }
        
        // MARK: Render the circular overlay
        func mapView(_ mapView: MKMapView, rendererFor overlay: MKOverlay) -> MKOverlayRenderer {
            //Render the circular overlay for geofence
            if let circleOverlay = overlay as? MKCircle {
                let circleRenderer = MKCircleRenderer(circle: circleOverlay)
                circleRenderer.fillColor = UIColor.primaryButton1.withAlphaComponent(0.3)
//                circleRenderer.fillColor = UIColor.blue.withAlphaComponent(0.3)
                circleRenderer.strokeColor = UIColor.primaryButton1
                circleRenderer.lineWidth = 2
                return circleRenderer
            }
            
            // For polyline
            if let polyline = overlay as? MKPolyline {
                let renderer = MKPolylineRenderer(overlay: polyline)
                renderer.strokeColor = .primaryButton1
                renderer.lineWidth = 4
                return renderer
            }
            
            return MKOverlayRenderer(overlay: overlay)
        }
        
        //MARK: To pin point the office
        func addAndSelectAnnotation(withCoordinate coordinate: CLLocationCoordinate2D) {
            
            parent.mapView.removeAnnotations(parent.mapView.annotations) // remove all the annotation present in the map View
            
            let anno = MKPointAnnotation()
            anno.coordinate = coordinate
            self.parent.mapView.addAnnotation(anno)
            self.parent.mapView.selectAnnotation(anno, animated: true)
            
            // to make the mapView according to the annotations
            self.parent.mapView.showAnnotations(parent.mapView.annotations, animated: true)
        }
        
        //MARK: Style the Annotation
        
        func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
            // Check if it's the user location annotation and return nil so it uses the default blue dot
            if annotation is MKUserLocation {
                return nil
            }

            let identifier = "BuildingAnnotation"

            var annotationView = mapView.dequeueReusableAnnotationView(withIdentifier: identifier)
            
            if annotationView == nil {
                annotationView = MKAnnotationView(annotation: annotation, reuseIdentifier: identifier)
                annotationView?.canShowCallout = true
                
                // Set your custom building image here
                annotationView?.image = UIImage(named: "Office_icon") // Replace "buildingIcon" with the name of your image asset
            } else {
                annotationView?.annotation = annotation
            }

            return annotationView
        }
    }
    
    

    
}



//#Preview {
//    CheckINMap()
//}
