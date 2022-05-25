import SwiftUI
import MapKit

struct POIMapView: UIViewRepresentable {
    
    typealias UIViewType = MKMapView
    
    var pois: [UserDataPOI]
    
    func makeUIView(context: Context) -> MKMapView {
        return MKMapView()
    }
    
    func updateUIView(_ uiView: MKMapView, context: Context) {
        uiView.showsUserLocation = false
        
        var avgLatitude: Double = 0
        var avgLongitude: Double = 0
        for poi in pois {
            avgLatitude += poi.point.latitude
            avgLongitude += poi.point.longitude
        }
        avgLatitude /= Double(pois.count)
        avgLongitude /= Double(pois.count)
        
        let location = CLLocationCoordinate2D(latitude: avgLatitude, longitude: avgLongitude)
        let region = MKCoordinateRegion(center: location, span: MKCoordinateSpan(latitudeDelta: 0.01, longitudeDelta: 0.01))
        
        for poi in pois {
            let pinCoordinate = CLLocationCoordinate2DMake(poi.point.latitude, poi.point.longitude)
            let pin = MKPointAnnotation()
            pin.coordinate = pinCoordinate
            pin.title = poi.typeCode
            
            uiView.addAnnotation(pin)
        }
        
        uiView.setRegion(uiView.regionThatFits(region), animated: true)
    }
    
}

struct IdentifiablePlace: Identifiable {
    let id = UUID()
    let location: CLLocationCoordinate2D
    init(lat: Double, long: Double) {
        self.location = CLLocationCoordinate2D(
            latitude: lat,
            longitude: long)
    }
}

struct TrackMapView: View {
    
    let places: [IdentifiablePlace]
    
    @State var region = MKCoordinateRegion()

    var body: some View {
        Map(coordinateRegion: $region,
            annotationItems: places)
        { place in
            MapMarker(coordinate: place.location,
                   tint: Color.purple)
        }
        .onAppear {
            self.region = GetRegion(places: self.places)
        }
    }

}


func GetRegion(places: [IdentifiablePlace]) -> MKCoordinateRegion {
    var latitude: Double = 0
    var longitude: Double = 0
    for place in places {
        latitude += place.location.latitude
        longitude += place.location.longitude
    }
    latitude /= Double(places.count)
    longitude /= Double(places.count)
    return MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: latitude, longitude: longitude), span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05))
}
