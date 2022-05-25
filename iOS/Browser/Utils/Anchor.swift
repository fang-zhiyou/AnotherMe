import Foundation

struct CityCoordinate {
    let Chengdu = Point(latitude: 30.660897, longitude: 104.088089)
    let Beijing = Point(latitude: 39.940193, longitude: 116.460954)
    let Shanghai = Point(latitude: 31.242941, longitude: 121.497142)
    let Xiamen = Point(latitude: 24.478001, longitude: 118.117939)
}

struct Anchor: Hashable {
    let name: String
    let coordinate: Point
}

let anchors = [
    Anchor(name: "Chengdu", coordinate: CityCoordinate().Chengdu),
    Anchor(name: "Beijing", coordinate: CityCoordinate().Beijing),
    Anchor(name: "Shanghai", coordinate: CityCoordinate().Shanghai),
    Anchor(name: "Xiamen", coordinate: CityCoordinate().Xiamen)
]
