import Foundation
import CoreLocation
import Combine

class LocationManager: NSObject, ObservableObject {
    static let shareSingleOne = LocationManager()
    override init() {
        super.init()
        self.locationManager.delegate = self
        self.locationManager.distanceFilter = 1
        self.locationManager.desiredAccuracy = kCLLocationAccuracyBest
        self.locationManager.requestAlwaysAuthorization()
        self.locationManager.requestWhenInUseAuthorization()
        self.locationManager.pausesLocationUpdatesAutomatically = false
        if #available(iOS 15.0, *) {
            locationManager.allowsBackgroundLocationUpdates = true
        }
        self.locationManager.startUpdatingLocation()
    }
    @Published var locationStatus: CLAuthorizationStatus?
    {
        willSet {
            objectWillChange.send()
        }
    }
    @Published var lastLocation: CLLocation? {
        willSet {
            objectWillChange.send()
        }
    }
    var statusString: String {
        guard let status = locationStatus else {
            return "unknown"
        }
        switch status {
        case .notDetermined: return "notDetermined"
        case .authorizedWhenInUse: return "authorizedWhenInUse"
        case .authorizedAlways: return "authorizedAlways"
        case .restricted: return "restricted"
        case .denied: return "denied"
        default: return "unknown"
        }
    }
    let objectWillChange = PassthroughSubject<Void, Never>()
    private let locationManager = CLLocationManager()
}

extension LocationManager: CLLocationManagerDelegate {
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        self.locationStatus = status
    }
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let location = locations.last else { return }
        self.lastLocation = location
    }
}
