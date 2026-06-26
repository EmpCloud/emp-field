//
//  EmpMapViewRepresentable.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 27/06/24.
//

import SwiftUI
import MapKit

struct EmpMapViewRepresentable: UIViewRepresentable {
    
    @EnvironmentObject var searchLocationViewModel: SearchLocationViewModel
    
    let mapView = MKMapView()
//    let permissionManger = PermissionManager()
    
    
    
    func makeUIView(context: Context) -> some UIView {
        mapView.delegate = context.coordinator
        mapView.isRotateEnabled = false
        mapView.showsUserLocation = true
        mapView.userTrackingMode = .follow
        
        return mapView
    }
    
    
    //MARK: Update Map View : To update the map view accordingly
    func updateUIView(_ uiView: UIViewType, context: Context) {
        if let coordinate = searchLocationViewModel.selectedLocationCoordinate {
            print("DEBUG: Selected Location in MapView \(coordinate)")
            context.coordinator.addAndSelectAnnotation(withCoordinate: coordinate)
        }
        
    }
    
    func makeCoordinator() -> MapCoordinator {
        return MapCoordinator(parent: self)
    }
}

extension EmpMapViewRepresentable {
    
    class MapCoordinator: NSObject, MKMapViewDelegate {
        
        //MARK: Properties
        let parent: EmpMapViewRepresentable
        
        //MARK: Lifecycle
        init(parent: EmpMapViewRepresentable) {
            self.parent = parent
            super.init()
        }
        
        
        //MARK: MKMapViewDelegate
        func mapView(_ mapView: MKMapView, didUpdate userLocation: MKUserLocation) {
            let region = MKCoordinateRegion(
                center: CLLocationCoordinate2D(latitude: userLocation.coordinate.latitude, longitude: userLocation.coordinate.longitude),
                span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
            )
            
            parent.mapView.setRegion(region, animated: true)
        }
        
        //MARK: Helpers
        
        func addAndSelectAnnotation(withCoordinate coordinate: CLLocationCoordinate2D) {
            
            parent.mapView.removeAnnotations(parent.mapView.annotations) // remove all the annotation present in the map View
            
            let anno = MKPointAnnotation()
            anno.coordinate = coordinate
            self.parent.mapView.addAnnotation(anno)
            self.parent.mapView.selectAnnotation(anno, animated: true)
            
            // to make the mapView according to the annotations
            self.parent.mapView.showAnnotations(parent.mapView.annotations, animated: true)
        }
    }
}
